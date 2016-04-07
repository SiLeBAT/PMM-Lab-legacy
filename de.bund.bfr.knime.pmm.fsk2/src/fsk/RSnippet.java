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
 *   15.03.2012 (hofer): created
 */
package fsk;

import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

/**
 * The R snippet which can be controlled by changing the settings or by changing the contents of the
 * snippets document.
 *
 * @author Heiko Hofer.
 */
public class RSnippet {

    /**
     * Identifier for row index (starting with 0).
     */
    public static final String ROWINDEX = "ROWINDEX";

    /**
     * Identifier for row ID.
     */
    public static final String ROWID = "ROWID";

    /**
     * Identifier for row count.
     */
    public static final String ROWCOUNT = "ROWCOUNT";

    /**
     * The version 1.x of the java snippet.
     */
    static final String VERSION_1_X = "version 1.x";

    private RSyntaxDocument m_document;

    private RSnippetSettings m_settings;

    private NodeLogger m_logger;

    RSnippet() {
        m_settings = new RSnippetSettings() {

            @Override
            String getScript() {
                if (m_document == null) {
                    return super.getScript();
                } else {
                    try {
                        return m_document.getText(0, m_document.getLength());
                    } catch (final BadLocationException e) {
                        // never happens
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            void setScript(String script) {
                if (m_document != null) {
                    try {
                        final String s = m_document.getText(0, m_document.getLength());
                        if (!s.equals(script)) {
                            m_document.replace(0, m_document.getLength(), script, null);
                        }
                    } catch (final BadLocationException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
        };
    }

    /**
     * Get the updated settings java snippet.
     *
     * @return the settings.
     */
    public RSnippetSettings getSettings() {
        return m_settings;
    }

    /**
     * Get the updated settings java snippet.
     *
     * @return the settings
     */
    public RSyntaxDocument getDocument() {
        // Lazy initializatioin of the document
        if (m_document == null) {
            final String initScript = m_settings.getScript();
            m_document = createDocument();
            // this changes the document to, if present
            m_settings.setScript(initScript);
        }

        return m_document;
    }

    /**
     * Create the document with the default skeleton.
     */
    private RSyntaxDocument createDocument() {
        return new RSnippetDocument();
    }

    /**
     * Create the outspec of the java snippet node. This method is typically used in the configure of
     * a node.
     *
     * @param spec                   the spec of the data table at the inport.
     * @param flowVariableRepository the flow variables at the inport
     * @return the spec at the output.
     * @throws InvalidSettingsException when settings are inconsistent with the spec or the flow
     *                                  variables at the inport.
     */
    DataTableSpec configure(final DataTableSpec spec,
                            final FlowVariableRepository flowVariableRepository) throws InvalidSettingsException {
        return spec;
    }

    /**
     * Create a template for this snippet.
     *
     * @param metaCategory the meta category of the template.
     * @return the template with a new uuid.
     */
    @SuppressWarnings("rawtype")
    public RSnippetTemplate createTemplate(final Class metaCategory) {
        return new RSnippetTemplate(metaCategory, getSettings());
    }

    /**
     * Attach logger to be used by this java snippet instance.
     *
     * @param logger the node logger.
     */
    void attachLogger(final NodeLogger logger) {
        m_logger = logger;
    }
}
