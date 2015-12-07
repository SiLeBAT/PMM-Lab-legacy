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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONDataTable;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland, University of Konstanz
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JSEditorRepresentation extends JSONViewContent {

    private static final String JS_CODE = "jsCode";
    private static final String CSS_CODE = "cssCode";
    private static final String JS_DEPENDENCIES = "jsDependencies";
    private static final String CSS_DEPENDENCIES = "cssDependencies";
    
    private String m_jsCode;
    private String m_cssCode;
    private String[] m_jsDependencies;
    private String[] m_cssDependencies;
    private JSONDataTable m_table;
    
    /** Serialization constructor. Don't use. */
    public JSEditorRepresentation() { }
    
    /**
     * @param dependencies
     * @param jsCode
     * @param table 
     */
    public JSEditorRepresentation(final String jsCode, final String cssCode, final String[] jsDependencies, final String[] cssDependencies, final JSONDataTable table) {
        m_jsDependencies = jsDependencies;
        m_cssDependencies = cssDependencies;
        m_jsCode = jsCode;
        m_cssCode = cssCode;
        m_table = table;
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
     * @return the jsDependencies
     */
    public String[] getJsDependencies() {
        return m_jsDependencies;
    }
    
    /**
     * @param jsDependencies the jsDependencies to set
     */
    public void setJsDependencies(final String[] jsDependencies) {
        m_jsDependencies = jsDependencies;
    }
    
    /**
     * @return the cssDependencies
     */
    public String[] getCssDependencies() {
        return m_cssDependencies;
    }
    
    /**
     * @param cssDependencies the cssDependencies to set
     */
    public void setCssDependencies(final String[] cssDependencies) {
        m_cssDependencies = cssDependencies;
    }
    
    /**
     * @return the table
     */
    public JSONDataTable getTable() {
        return m_table;
    }
    
    /**
     * @param table the table to set
     */
    public void setTable(final JSONDataTable table) {
        m_table = table;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToNodeSettings(final NodeSettingsWO settings) {
        settings.addString(JS_CODE, m_jsCode);
        settings.addString(CSS_CODE, m_cssCode);
        settings.addStringArray(JS_DEPENDENCIES, m_jsDependencies);
        settings.addStringArray(CSS_DEPENDENCIES, m_cssDependencies);
        if (m_table != null) {
            m_table.saveJSONToNodeSettings(settings);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_jsCode = settings.getString(JS_CODE, "");
        m_cssCode = settings.getString(CSS_CODE, "");
        m_jsDependencies = settings.getStringArray(JS_DEPENDENCIES, new String[0]);
        m_cssDependencies = settings.getStringArray(CSS_DEPENDENCIES, new String[0]);
        m_table = JSONDataTable.loadFromNodeSettings(settings);
    }
}
