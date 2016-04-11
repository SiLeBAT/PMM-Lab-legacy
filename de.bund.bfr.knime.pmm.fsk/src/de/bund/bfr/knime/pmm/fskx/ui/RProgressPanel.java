/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * -------------------------------------------------------------------
 */
package de.bund.bfr.knime.pmm.fskx.ui;

import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.util.ViewUtils;
import org.knime.core.node.workflow.NodeProgressEvent;
import org.knime.core.node.workflow.NodeProgressListener;
import org.knime.core.util.KNIMETimer;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class RProgressPanel extends JPanel implements NodeProgressListener {

  /** Generated serialVersionUID */
  private static final long serialVersionUID = -4151720960897235808L;

  private ExecutionMonitor m_exec = new ExecutionMonitor();
  private JButton m_cancelButton;
  private JProgressBar m_progressBar;
  private JLabel m_message;
  private CardLayout m_cardLayout;

  public RProgressPanel() {
    super(new CardLayout());

    m_cardLayout = (CardLayout) super.getLayout();

    JPanel defaultPanel = new JPanel();
    defaultPanel.setPreferredSize(new Dimension(0, 0));
    add(defaultPanel, "default");

    m_cancelButton = new JButton("Cancel");
    m_cancelButton.addActionListener(e -> {
        m_exec.getProgressMonitor().setExecuteCanceled();
        stopMonitoring();
    });

    m_progressBar = new JProgressBar(0, 100);
    m_progressBar.setValue(0);
    m_progressBar.setStringPainted(true);
    m_message = new JLabel();

    add(new ProgressPanel(), "progress");
    m_cardLayout.show(this, "default");
  }

  private class ProgressPanel extends JPanel {

    /** Generated serialVersionUID */
    private static final long serialVersionUID = -2599150322975586850L;

    ProgressPanel() {
      super(new BorderLayout(5, 5));

      // Center panel
      JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
      centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
      centerPanel.add(m_message);
      add(centerPanel, BorderLayout.EAST);

      // Right panel
      JPanel rightPanel = new JPanel();
      rightPanel.add(m_progressBar);
      rightPanel.add(m_cancelButton);
      add(rightPanel, BorderLayout.EAST);

      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
  }

  private final AtomicBoolean m_updateInProgress = new AtomicBoolean(false);

  public void stopMonitoring() {
    ViewUtils.runOrInvokeLaterInEDT(this::stopMonitoringInternal);
  }

  private void stopMonitoringInternal() {
    m_cancelButton.setEnabled(false);
    m_exec.getProgressMonitor().removeProgressListener(this);
    m_cardLayout.show(this, "default");
  }

  public void startMonitoring(final ExecutionMonitor exec) {
    m_exec.getProgressMonitor().removeProgressListener(this);
    m_exec = exec;
    m_exec.getProgressMonitor().addProgressListener(this);

    if (m_exec.getProgressMonitor().getProgress() == null) {
      m_progressBar.setIndeterminate(true);
    } else {
      progressChangedInternal();
    }
    m_cancelButton.setEnabled(true);
    m_cardLayout.show(this, "parent");
    m_updateInProgress.set(false);
  }

  @Override
  public void progressChanged(final NodeProgressEvent pe) {
    // if another state is waiting to be processed, simply return and leave the work to the
    // previously started thread. This works because we are retrieving the current state
    // information!
    if (m_updateInProgress.compareAndSet(false, true)) {
      ViewUtils.runOrInvokeLaterInEDT(() -> {
        // let others know we are in the middle of processing this update - they will now need to
        // start their own job.
        m_updateInProgress.set(false);
        progressChangedInternal();
      });
    }
  }

  private void progressChangedInternal() {
    Double progress = m_exec.getProgressMonitor().getProgress();
    String message = m_exec.getProgressMonitor().getMessage();
    m_message.setText(message);
    if (progress != null) {
      if (m_progressBar.isIndeterminate()) {
        m_progressBar.setIndeterminate(false);
      }
      int p = (int) Math.round(progress * 100);
      m_progressBar.setValue(p);

      if (p >= 100) {

        // delay showing nothing by 200ms for the user to see that progress reached 100%
        KNIMETimer.getInstance().schedule(new TimerTask() {
          @Override
          public void run() {
            if (p >= 100) {
              m_cardLayout.show(RProgressPanel.this, "default");
            }
          }
        }, 200);
      }
    }
  }
}

