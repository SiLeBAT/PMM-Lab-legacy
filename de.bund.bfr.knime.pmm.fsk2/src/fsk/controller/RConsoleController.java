package fsk.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.util.ViewUtils;
import org.knime.core.util.Pair;

import fsk.controller.IRController.RException;
import fsk.controller.RCommandQueue.RCommandExecutionListener;
import fsk.ui.RConsole;
import fsk.ui.RProgressPanel;

/**
 * Class managing a console and execution of commands pushed to the command queue.
 *
 * @author Heiko Hofer
 * @author Jonathan Hale
 */
public class RConsoleController implements RCommandExecutionListener {

    private RConsole m_pane;

    private final Action m_cancelAction;
    private final Action m_clearAction;
    private final RController m_controller;
    private final RCommandQueue m_commandQueue;

    private final DocumentListener m_docListener;

    /**
     * Constructor
     *
     * @param controller RController to use for executing commands etc.
     */
    public RConsoleController(final RController controller, final RCommandQueue queue) {
        m_controller = controller;
        queue.addRCommandExecutionListener(this);
        m_commandQueue = queue;
        m_cancelAction = new AbstractAction("Terminate") {
            private static final long serialVersionUID = 5020552337130596607L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                // cancel();
            }
        };

        m_cancelAction.putValue(Action.SMALL_ICON, ViewUtils.loadIcon(getClass(), "progress_stop.gif"));
        m_cancelAction.putValue(Action.SHORT_DESCRIPTION, "Terminate");
        m_cancelAction.setEnabled(false);

        m_clearAction = new AbstractAction("Clear Console") {
            private static final long serialVersionUID = -2133885712510027838L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                ViewUtils.invokeLaterInEDT(() -> clear());
            }
        };
        m_clearAction.putValue(Action.SMALL_ICON, ViewUtils.loadIcon(getClass(), "clear_co.gif"));
        m_clearAction.putValue(Action.SHORT_DESCRIPTION, "Clear Console");
        m_clearAction.setEnabled(false);

        m_docListener = new DocumentListener() {
            @Override
            public void removeUpdate(final DocumentEvent e) {
                updateClearAction();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                updateClearAction();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                updateClearAction();
            }

            private void updateClearAction() {
                m_clearAction.setEnabled(m_pane.getDocument().getLength() > 0);
            }
        };
    }

    /**
     * Cancel the currently executing command.
     *
     * @see RConsoleController#getCancelAction()
     */
    public void cancel() {
        m_commandQueue.stopExecutionThread();

        // clear commands in queue
        m_commandQueue.clear();

        try {
            m_controller.terminateAndRelaunch();
        } catch (final Exception e) {
            append("Could not properly terminate current command.", 1);
        }
    }

    /**
     * Clear the console pane.
     *
     * @see RConsoleController#getClearAction()
     */
    private void clear() {
        if (m_pane != null) {
            m_pane.setText("");
        }
    }

    /**
     * Attach a RConsole to output any commands and their output.
     *
     * @param pane
     */
    public void attachOutput(final RConsole pane) {
        if (m_pane != null) {
            detach(m_pane);
        }

        m_pane = pane;
        if (m_pane != null) {
            m_pane.getDocument().addDocumentListener(m_docListener);
        }
        m_clearAction.setEnabled(false);
    }

    /**
     * Detach a RConsole. There will be no output to it by this RConsoleController anymore.
     *
     * @param pane
     */
    public void detach(final RConsole pane) {
        if (pane == null || m_pane != pane) {
            throw new RuntimeException("Wrong text pane to detach.");
        }
        m_pane.getDocument().removeDocumentListener(m_docListener);
        m_pane = null;
    }

    /**
     * Whether the given RConsole is currently attached to this RConsoleController.
     *
     * @param pane
     * @return <code>true</code> if the given pane is the currently attached one.
     */
    public boolean isAttached(final RConsole pane) {
        return m_pane == pane;
    }

    /**
     * Update the state of R engine (currently executing command or not executing). This updates the enabled state of
     * the terminate action.
     *
     * @param busy Whether currently executing.
     */
    private void updateBusyState(final boolean busy) {
        m_cancelAction.setEnabled(busy);
    }

    /**
     * Interface for creating custom ExecutionMonitors to use for execution of commands for example. Allows the user to
     * for example attach a {@link RProgressPanel} for example.
     *
     * @author Jonathan Hale
     */
    public interface ExecutionMonitorFactory {
        /**
         * Create a new ExecutionMonitor.
         *
         * @return The created {@link ExecutionMonitor}, never <code>null</code>
         */
        ExecutionMonitor create();
    }

    private final AtomicBoolean m_updateScheduled = new AtomicBoolean(false);

    private final ReentrantLock m_appendBufferLock = new ReentrantLock(true);
    private Deque<Pair<StringBuilder, Integer>> m_buffer = new ArrayDeque<>();

    public void append(final String text, final int oType) {

        if (m_pane != null) {
            // update is scheduled contribute to this update
            m_appendBufferLock.lock();
            try {
                if (m_buffer.size() > 0 && m_buffer.peekLast().getSecond() == oType) {
                    m_buffer.peekLast().getFirst().append(text);
                } else {
                    m_buffer.offer(new Pair<StringBuilder, Integer>(new StringBuilder(text), oType));
                }
            } finally {
                m_appendBufferLock.unlock();
            }
            // if update is not scheduled
            if (m_updateScheduled.compareAndSet(false, true)) {
                final Runnable doWork = () -> {
                    Queue<Pair<StringBuilder, Integer>> buffer = null;
                    m_appendBufferLock.lock();
                    try {
                        m_updateScheduled.set(false);
                        buffer = m_buffer;
                        m_buffer = new ArrayDeque<>();
                    } finally {
                        m_appendBufferLock.unlock();
                    }

                    final StyledDocument doc = m_pane.getStyledDocument();
                    while (buffer.size() > 0) {
                        final Pair<StringBuilder, Integer> toWrite = buffer.poll();
                        try {
                            final Style style =
                                    toWrite.getSecond() == 0 ? m_pane.getNormalStyle() : m_pane.getErrorStyle();
                            doc.insertString(doc.getLength(), toWrite.getFirst().toString(), style);
                            final int maxDocLength = 20 * 1024 * 1024 / 2; // 20MB
                            if (doc.getLength() > maxDocLength) {
                                // TODO: Cut by whole line
                                doc.remove(0, doc.getLength() - maxDocLength);
                            }

                        } catch (final BadLocationException e) {
                            // never happens
                            throw new RuntimeException(e);
                        }
                    }
                };

                ViewUtils.runOrInvokeLaterInEDT(doWork);
            }
        }
    }

    /**
     * @return Action for canceling current R execution
     */
    public Action getCancelAction() {
        return m_cancelAction;
    }

    /**
     * @return Action for clearing the console.
     */
    public Action getClearAction() {
        return m_clearAction;
    }

    // --- RCommandExecutionListener methods ---

    @Override
    public void onCommandExecutionStart(final RCommand command) {
        updateBusyState(true);

        if (!command.isShowInConsole()) {
            return;
        }

        final StringTokenizer tokenizer = new StringTokenizer(command.getCommand(), "\n");

        boolean first = true;
        while (tokenizer.hasMoreTokens()) {
            final String line = tokenizer.nextToken();
            if (first) {
                append("> " + line + "\n", 0);
                first = false;
            } else {
                append("+ " + line + "\n", 0);
            }
        }
    }

    @Override
    public void onCommandExecutionEnd(final RCommand command, final String stdout,
                                      final String stderr) {
        updateBusyState(false);

        if (!command.isShowInConsole()) {
            // we do not want the output to appear in the console.
            return;
        }

        append(stdout, 0);
        append(stderr, 1);
    }

    @Override
    public void onCommandExecutionCanceled() {
        updateBusyState(false);
    }

    @Override
    public void onCommandExecutionError(final RException e) {
        Throwable exception = e;

        do {
            append("ERROR: " + exception.getMessage(), 1);
            if (exception == exception.getCause()) {
                // avoid infinite loops for exceptions which set themselves as
                // their cause.
                break;
            }
            exception = exception.getCause();
        } while (exception != null);
    }
}
