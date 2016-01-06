/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.js.editor;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.knime.base.util.flowvariable.FlowVariableProvider;
import org.knime.base.util.flowvariable.FlowVariableResolver;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.JSONDataTable;
import org.knime.js.core.node.AbstractWizardNodeModel;

/**
 *
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland, University of Konstanz
 */
final class JSEditorNodeModel extends AbstractWizardNodeModel<JSEditorRepresentation, JSEditorValue>
        implements FlowVariableProvider {

    private final JSEditorConfig m_config;

    /**
     */
    JSEditorNodeModel() {
        super(new PortType[]{BufferedDataTable.TYPE_OPTIONAL}, new PortType[0]);
        m_config = new JSEditorConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        if (StringUtils.isEmpty(m_config.getJsCode())) {
            throw new InvalidSettingsException("No script defined");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] performExecute(final PortObject[] inObjects, final ExecutionContext exec)
        throws Exception {

        synchronized (getLock()) {
            JSEditorRepresentation representation = getViewRepresentation();
            //create JSON table if data available
            if (inObjects[0] != null) {
              //construct dataset
                BufferedDataTable table = (BufferedDataTable)inObjects[0];
                if (m_config.getMaxRows() < table.size()) {
                    setWarningMessage("Only the first " + m_config.getMaxRows() + " rows are displayed.");
                }
                JSONDataTable jsonTable = new JSONDataTable(table, 1, m_config.getMaxRows(), exec);
                representation.setTable(jsonTable);
            }

            representation.setJsCode(parseTextAndReplaceVariables());
            representation.setCssCode(m_config.getCssCode());
            setPathsFromLibNames(m_config.getDependencies());
        }
        
        String models = getAvailableInputFlowVariables().get("models").getStringValue();
        pushFlowVariableString("models", models);
        return new PortObject[0];
    }

    private String parseTextAndReplaceVariables() throws InvalidSettingsException {
        String flowVarCorrectedText = null;
        if (m_config.getJsCode() != null) {
            try {
                flowVarCorrectedText = FlowVariableResolver.parse(m_config.getJsCode(), this);
            } catch (NoSuchElementException nse) {
                throw new InvalidSettingsException(nse.getMessage(), nse);
            }
        }
        return flowVarCorrectedText;
    }

    private static final String ID_WEB_RES = "org.knime.js.core.webResources";

    private static final String ATTR_RES_BUNDLE_ID = "webResourceBundleID";

    private static final String ID_IMPORT_RES = "importResource";

    private static final String ATTR_PATH = "relativePath";

    private static final String ATTR_TYPE = "type";

    private void setPathsFromLibNames(final String[] libNames) {
        ArrayList<String> jsPaths = new ArrayList<String>();
        ArrayList<String> cssPaths = new ArrayList<String>();
        for (String lib : libNames) {
            IConfigurationElement confElement = getConfigurationFromWebResID(lib);
            if (confElement != null) {
                for (IConfigurationElement resElement : confElement.getChildren(ID_IMPORT_RES)) {
                    String path = resElement.getAttribute(ATTR_PATH);
                    String type = resElement.getAttribute(ATTR_TYPE);
                    if (path != null && type != null) {
                        if (type.equalsIgnoreCase("javascript")) {
                            jsPaths.add(path);
                        } else if (type.equalsIgnoreCase("css")) {
                            cssPaths.add(path);
                        }
                    } else {
                        setWarningMessage("Required library " + lib + " is not correctly configured");
                    }
                }
            } else {
                setWarningMessage("Required library is not registered: " + lib);
            }
        }
        
        JSEditorRepresentation representation = getViewRepresentation();
        representation.setJsDependencies(jsPaths.toArray(new String[0]));
        representation.setCssDependencies(cssPaths.toArray(new String[0]));
    }

    private IConfigurationElement getConfigurationFromWebResID(final String id) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] configurationElements = registry.getConfigurationElementsFor(ID_WEB_RES);
        for (IConfigurationElement element : configurationElements) {
            if (id.equals(element.getAttribute(ATTR_RES_BUNDLE_ID))) {
                return element;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationError validateViewValue(final JSEditorValue viewContent) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSEditorRepresentation createEmptyViewRepresentation() {
        return new JSEditorRepresentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSEditorValue createEmptyViewValue() {
        return new JSEditorValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJavascriptObjectID() {
        return "knime_generic_view";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_config.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        new JSEditorConfig().loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_config.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performReset() {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHideInWizard() {
        return m_config.getHideInWizard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCurrentValue(final NodeSettingsWO content) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInteractiveViewName() {
        return (new JSEditorNodeFactory()).getInteractiveViewName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void useCurrentValueAsDefault() {
        // nothing to do
    }

}
