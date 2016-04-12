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
 * ------------------------------------------------------------------------
 *
 * History
 *   26.03.2012 (hofer): created
 */
package de.bund.bfr.knime.pmm.fskx.template;


import javax.swing.*;

import de.bund.bfr.knime.pmm.fskx.rsnippet.RSnippet;
import de.bund.bfr.knime.pmm.fskx.rsnippet.RSnippetTemplate;

import java.awt.*;
import java.util.Collections;
import java.util.Set;

/**
 * A dialog with most basic settings for an output field.
 *
 * @author Heiko Hofer.
 */
public class AddTemplateDialog extends JDialog {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 2146545320197893488L;

    private de.bund.bfr.knime.pmm.fskx.rsnippet.RSnippetTemplate m_result;

    private JComboBox<String> m_category;

    private JTextField m_name;

    private JTextArea m_description;

    private RSnippet m_settings;

    private Class<?> m_metaCategory;

    /**
     * Create a new dialog.
     *
     * @param parent         frame who owns this dialog.
     * @param snippet        the snippet to create the template.
     * @param metaCategories the meta category
     */
    private AddTemplateDialog(final Frame parent, final RSnippet snippet,
                              final Class<?> metaCategories) {

        super(parent, true);

        m_settings = snippet;
        m_metaCategory = metaCategories;

        setTitle("Add a Java snippet template.");

        // instantiate the components of the dialog
        JPanel p = createPanel();

        ControlPanel controlPanel = new ControlPanel();

        // add dialog and control panel to the content pane
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        cont.add(p, BorderLayout.CENTER);
        cont.add(controlPanel, BorderLayout.SOUTH);

        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private class ControlPanel extends JPanel {

        /**
         * Generated serialVersionUID.
         */
        private static final long serialVersionUID = 7799396943525060811L;

        ControlPanel() {
            super(new FlowLayout(FlowLayout.RIGHT));

            JButton ok = new JButton("OK");
            ok.addActionListener(e -> onOK());

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e -> onCancel());

            add(ok);
            add(cancel);
        }
    }

    private JPanel createPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.BASELINE;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;

        Insets leftInsets = new Insets(3, 8, 3, 8);
        Insets rightInsets = new Insets(3, 0, 3, 8);
        Insets leftCategoryInsets = new Insets(11, 8, 3, 8);
        Insets rightCategoryInsets = new Insets(11, 0, 3, 8);

        c.gridx = 0;
        c.insets = leftCategoryInsets;
        c.gridwidth = 1;
        c.weightx = 0;
        c.weighty = 0;

        TemplateProvider provider = TemplateProvider.getDefault();
        m_category = new JComboBox<>();
        m_category.setEditable(true);
        Set<String> categories =
                provider.getCategories(Collections.singletonList(m_metaCategory));
        categories.remove(TemplateProvider.ALL_CATEGORY);
        for (String category : categories) {
            m_category.addItem(category);
        }
        p.add(new JLabel("Category:"), c);

        c.gridx++;
        c.insets = rightCategoryInsets;
        p.add(m_category, c);

        c.gridy++;
        c.gridx = 0;
        c.insets = leftInsets;
        p.add(new JLabel("Title:"), c);

        c.gridx++;
        c.insets = rightInsets;
        m_name = new JTextField("");
        p.add(m_name, c);

        c.gridy++;
        c.gridx = 0;
        c.insets = leftInsets;

        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        m_description = new JTextArea();
        JScrollPane descScroller = new JScrollPane(m_description);
        descScroller.setBorder(BorderFactory.createTitledBorder("Description"));
        p.add(descScroller, c);

        p.setPreferredSize(new Dimension(400, 300));

        return p;
    }

    /**
     * Save settings in field m_result.
     */
    private RSnippetTemplate takeOverSettings() {
        RSnippetTemplate template = m_settings.createTemplate(m_metaCategory);
        template.setCategory((String) m_category.getSelectedItem());
        template.setName(m_name.getText());
        template.setDescription(m_description.getText());

        return template;
    }

    /**
     * Called when user presses the cancel button or closes the window.
     */
    private void onOK() {
        m_result = takeOverSettings();
        if (m_result != null) {
            shutDown();
        }
    }

    /**
     * Called when user presses the cancel button or closes the window.
     */
    private void onCancel() {
        m_result = null;
        shutDown();
    }

    /**
     * Blows away the dialog.
     */
    private void shutDown() {
        setVisible(false);
    }

    /**
     * Opens a Dialog to receive user settings. If the user cancels the dialog <code>null</code> will
     * be returned. If okay is pressed, the settings from the dialog will be stored in a new
     * {@link RSnippetTemplate} object.<br>
     * If user's settings are incorrect an error dialog pops up and the user values are discarded.
     *
     * @param parent         frame who owns this dialog
     * @param snippet        the snippet to creat the template
     * @param metaCategories the meta category
     * @return new template are null in case of cancellation
     */
    public static RSnippetTemplate openUserDialog(final Frame parent, final RSnippet snippet,
                                                  final Class<?> metaCategories) {
        AddTemplateDialog dialog = new AddTemplateDialog(parent, snippet, metaCategories);
        return dialog.showDialog();
    }

    /**
     * Shows the dialog and waits for it to return. If the user pressed Ok it returns the OutCol
     * definition
     */
    private RSnippetTemplate showDialog() {
        pack();
        centerDialog();

        setVisible(true);
    /* ---- won't come back before dialog is disposed -------- */
    /* ---- on Ok we transfer the settings into the m_result -- */
        return m_result;
    }

    /**
     * Sets this dialog in the center of the screen observing the current screen size.
     */
    private void centerDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();
        setBounds(Math.max(0, (screenSize.width - size.width) / 2),
                Math.max(0, (screenSize.height - size.height) / 2), Math.min(screenSize.width, size.width),
                Math.min(screenSize.height, size.height));
    }
}
