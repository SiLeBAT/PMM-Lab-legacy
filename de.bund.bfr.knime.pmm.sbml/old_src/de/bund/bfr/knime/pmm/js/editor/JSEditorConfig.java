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

import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 *
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland, University of Konstanz
 */
final class JSEditorConfig extends QuickFormFlowVariableConfig<JSEditorValue> {

    /** Default row maximum. */
    static final int DEFAULT_MAX_ROWS = 2500;

    /** File containing default script. */
    private static final String DEFAULT_SCRIPT_CSS = "default_script.css";

    /** File containing default CSS. */
    private static final String DEFAULT_SCRIPT_JS = "default_script.js";

    private static final NodeLogger LOGGER = NodeLogger.getLogger(JSEditorConfig.class);

    private static final String HIDE_IN_WIZARD = "hideInWizard";
    private static final String MAX_ROWS = "maxRows";
    private static final String JS_CODE = "jsCode";
    private static final String CSS_CODE = "cssCode";
    private static final String DEPENDENCIES = "dependencies";
    //private static final String VIEW_NAME = "viewName";

    private boolean m_hideInWizard = false;
    private int m_maxRows = DEFAULT_MAX_ROWS;
    private String m_jsCode;
    private String m_cssCode;
    private String[] m_dependencies;
    //private String m_viewName;

    /**
     *
     */
    public JSEditorConfig() {
        m_dependencies = new String[0];
    }

    /**
     * @return the hideInWizard
     */
    public boolean getHideInWizard() {
        return m_hideInWizard;
    }

    /**
     * @param hideInWizard the hideInWizard to set
     */
    public void setHideInWizard(final boolean hideInWizard) {
        m_hideInWizard = hideInWizard;
    }

    /**
     * @return the maxRows
     */
    public int getMaxRows() {
        return m_maxRows;
    }

    /**
     * @param maxRows the maxRows to set
     */
    public void setMaxRows(final int maxRows) {
        m_maxRows = maxRows;
    }

    /**
     * @return the jsCode
     */
    public String getJsCode() {
        return m_jsCode;
    }

    /**
     * @param jsCode the jsCode to set
     */
    public void setJsCode(final String jsCode) {
        m_jsCode = jsCode;
    }

    /**
     * @return the cssCode
     */
    public String getCssCode() {
        return m_cssCode;
    }

    /**
     * @param cssCode the cssCode to set
     */
    public void setCssCode(final String cssCode) {
        m_cssCode = cssCode;
    }

    /**
     * @return the dependencies
     */
    public String[] getDependencies() {
        return m_dependencies;
    }

    /**
     * @param dependencies the dependencies to set
     */
    public void setDependencies(final String[] dependencies) {
        m_dependencies = dependencies;
    }

    /**
     * @return the viewName
     */
    /*public String getViewName() {
        return m_viewName;
    }*/

    /**
     * @param viewName the viewName to set
     */
    /*public void setViewName(final String viewName) {
        m_viewName = viewName;
    }*/

    /** Saves current parameters to settings object.
     * @param settings To save to.
     */
    public void saveSettings(final NodeSettingsWO settings) {
        settings.addBoolean(HIDE_IN_WIZARD, getHideInWizard());
        settings.addInt(MAX_ROWS, getMaxRows());
        settings.addString(JS_CODE, m_jsCode);
        settings.addString(CSS_CODE, m_cssCode);
        settings.addStringArray(DEPENDENCIES, m_dependencies);
        //settings.addString(VIEW_NAME, m_viewName);
    }

    /** Loads parameters in NodeModel.
     * @param settings To load from.
     * @throws InvalidSettingsException If incomplete or wrong.
     */
    public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        // added in 2.11
        setHideInWizard(settings.getBoolean(HIDE_IN_WIZARD, false));
        // added in 2.11
        setMaxRows(settings.getInt(MAX_ROWS, DEFAULT_MAX_ROWS));
        m_jsCode = settings.getString(JS_CODE);
        m_cssCode = settings.getString(CSS_CODE);
        m_dependencies = settings.getStringArray(DEPENDENCIES);
        //m_viewName = settings.getString(VIEW_NAME);
    }

    /** Loads parameters in Dialog.
     * @param settings To load from.
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {
        setHideInWizard(settings.getBoolean(HIDE_IN_WIZARD, false));
        setMaxRows(settings.getInt(MAX_ROWS, DEFAULT_MAX_ROWS));
        m_jsCode = settings.getString(JS_CODE, null);
        if (m_jsCode == null) {
            try {
                m_jsCode = IOUtils.toString(JSEditorConfig.class.getResource(DEFAULT_SCRIPT_JS), Charsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(String.format("Could not read default javascript from file \"%s\"", DEFAULT_SCRIPT_JS), e);
                m_jsCode = "";
            }
        }
        m_cssCode = settings.getString(CSS_CODE, null);
        if (m_cssCode == null) {
            try {
                m_cssCode = IOUtils.toString(JSEditorConfig.class.getResource(DEFAULT_SCRIPT_CSS), Charsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error(String.format("Could not read default css from file \"%s\"", DEFAULT_SCRIPT_CSS), e);
                m_cssCode = "";
            }
        }
        m_dependencies = settings.getStringArray(DEPENDENCIES, new String[0]);
        //m_viewName = settings.getString(VIEW_NAME, "");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected JSEditorValue createEmptyValue() {
    	return new JSEditorValue();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	return new HashCodeBuilder().appendSuper(super.hashCode()).toHashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
    	if (obj == null) {
    		return false;
    	}
    	if (obj == this) {
    		return true;
    	}
    	if (obj.getClass() == getClass()) {
    		return false;
    	}
        return new EqualsBuilder().appendSuper(super.equals(obj)).isEquals();
    }
}
