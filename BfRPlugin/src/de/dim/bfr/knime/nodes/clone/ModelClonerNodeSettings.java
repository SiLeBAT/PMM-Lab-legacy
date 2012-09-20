/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
 ******************************************************************************/
package de.dim.bfr.knime.nodes.clone;

import java.util.SortedMap;
import java.util.TreeMap;

import org.knime.base.node.io.filereader.ColProperty;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This class hold the settings for the Table Creator Node.
 *
 * @author Heiko Hofer
 */
public final class ModelClonerNodeSettings {
	private static final String CFG_ROW_INDICES = "rowIndices";
    private static final String CFG_COLUMN_INDICES = "columnIndices";
    private static final String CFG_VALUES = "values";
    private static final String CFG_COLUMN_PROPERTIES = "columnProperties";
    private static final String CFG_ROW_ID_PREFIX = "rowIdPrefix";
    private static final String CFG_ROW_ID_SUFFIX = "rowIdSuffix";
    private static final String CFG_ROW_ID_START_VALUE = "rowIdStartValue";
    private static final String CFG_HIGHLIGHT_OUTPUT_TABLE =
        "highlightOutputTable";

    private int[] m_rowIndices = new int[0];
    private int[] m_colIndices = new int[0];
    private String[] m_values = new String[0];
    private SortedMap<Integer, ColProperty> m_colProps =
        new TreeMap<Integer, ColProperty>();
    private String m_rowIdPrefix = "Row";
    private String m_rowIdSuffix = "";
    private int m_rowIdStartValue = 1;
    private boolean m_hightlightOutputTable = false;

    /**
     * @return the rowIndices
     */
    public final int[] getRowIndices() {
        return m_rowIndices;
    }
    /**
     * @param rowIndices the rowIndices to set
     */
    public final void setRowIndices(final int[] rowIndices) {
        m_rowIndices = rowIndices;
    }
    /**
     * @return the colIndices
     */
    public final int[] getColumnIndices() {
        return m_colIndices;
    }
    /**
     * @param colIndices the colIndices to set
     */
    public final void setColumnIndices(final int[] colIndices) {
        m_colIndices = colIndices;
    }
    /**
     * @return the values
     */
    public final String[] getValues() {
        return m_values;
    }
    /**
     * @param values the values to set
     */
    public final void setValues(final String[] values) {
        m_values = values;
    }

    /**
     * @return the colProps
     */
    public final SortedMap<Integer, ColProperty> getColumnProperties() {
        return m_colProps;
    }
    /**
     * @param colProps the colProps to set
     */
    public final void setColumnProperties(
            final SortedMap<Integer, ColProperty> colProps) {
        m_colProps = colProps;
    }

    /**
     * @return the rowIdPrefix
     */
    public final String getRowIdPrefix() {
        return m_rowIdPrefix;
    }
    /**
     * @param rowIdPrefix the rowIdPrefix to set
     */
    public final void setRowIdPrefix(final String rowIdPrefix) {
        m_rowIdPrefix = rowIdPrefix;
    }
    /**
     * @return the rowIdSuffix
     */
    public final String getRowIdSuffix() {
        return m_rowIdSuffix;
    }
    /**
     * @param rowIdSuffix the rowIdSuffix to set
     */
    public final void setRowIdSuffix(final String rowIdSuffix) {
        m_rowIdSuffix = rowIdSuffix;
    }
    /**
     * @return the rowIdStartValue
     */
    public final int getRowIdStartValue() {
        return m_rowIdStartValue;
    }
    /**
     * @param rowIdStartValue the rowIdStartValue to set
     */
    public final void setRowIdStartValue(final int rowIdStartValue) {
        m_rowIdStartValue = rowIdStartValue;
    }
    /**
     * @return the hightlightOutputTable
     */
    public final boolean getHightlightOutputTable() {
        return m_hightlightOutputTable;
    }
    /**
     * @param hightlightOutputTable the hightlightOutputTable to set
     */
    public final void setHightlightOutputTable(final boolean hightlightOutputTable) {
        m_hightlightOutputTable = hightlightOutputTable;
    }
    /**
     * Loads the settings from the node settings object.
     *
     * @param settings a node settings object
     * @throws InvalidSettingsException if some settings are missing
     */
    public void loadSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_rowIndices = settings.getIntArray(CFG_ROW_INDICES);
        m_colIndices = settings.getIntArray(CFG_COLUMN_INDICES);
        m_values = settings.getStringArray(CFG_VALUES);
        m_rowIdPrefix = settings.getString(CFG_ROW_ID_PREFIX);
        m_rowIdSuffix = settings.getString(CFG_ROW_ID_SUFFIX);
        m_rowIdStartValue = settings.getInt(CFG_ROW_ID_START_VALUE);
        m_hightlightOutputTable = settings.getBoolean(
                CFG_HIGHLIGHT_OUTPUT_TABLE);
        readColumnPropsFromConfig(
                settings.getNodeSettings(CFG_COLUMN_PROPERTIES));
    }

    /**
     * Loads the settings from the node settings object using default values if
     * some settings are missing.
     *
     * @param settings a node settings object
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {
        m_rowIndices = settings.getIntArray(CFG_ROW_INDICES, new int[0]);
        m_colIndices = settings.getIntArray(CFG_COLUMN_INDICES, new int[0]);
        m_values = settings.getStringArray(CFG_VALUES, new String[0]);
        m_rowIdPrefix = settings.getString(CFG_ROW_ID_PREFIX, "Row");
        m_rowIdSuffix = settings.getString(CFG_ROW_ID_SUFFIX, "");
        m_rowIdStartValue = settings.getInt(CFG_ROW_ID_START_VALUE, 1);
        m_hightlightOutputTable = settings.getBoolean(
                CFG_HIGHLIGHT_OUTPUT_TABLE, false);
        if (settings.containsKey(CFG_COLUMN_PROPERTIES)) {
            try {
                readColumnPropsFromConfig(
                        settings.getNodeSettings(CFG_COLUMN_PROPERTIES));
            } catch (InvalidSettingsException e) {
                m_colProps = new TreeMap<Integer, ColProperty>();
            }
        } else {
            m_colProps = new TreeMap<Integer, ColProperty>();
        }
    }

    /**
     * Saves the settings into the node settings object.
     *
     * @param settings a node settings object
     */
    public void saveSettings(final NodeSettingsWO settings) {
        settings.addIntArray(CFG_ROW_INDICES, m_rowIndices);
        settings.addIntArray(CFG_COLUMN_INDICES, m_colIndices);
        settings.addStringArray(CFG_VALUES, m_values);
        settings.addString(CFG_ROW_ID_PREFIX, m_rowIdPrefix);
        settings.addString(CFG_ROW_ID_SUFFIX, m_rowIdSuffix);
        settings.addInt(CFG_ROW_ID_START_VALUE, m_rowIdStartValue);
        settings.addBoolean(CFG_HIGHLIGHT_OUTPUT_TABLE,
                m_hightlightOutputTable);
        saveColumnPropsToConfig(
                settings.addNodeSettings(CFG_COLUMN_PROPERTIES));
    }


    private void saveColumnPropsToConfig(final NodeSettingsWO cfg) {

        for (Integer c : m_colProps.keySet()) {
            ColProperty cProp = m_colProps.get(c);
            if (cProp != null) {
                cProp.saveToConfiguration(cfg.addNodeSettings(c.toString()));
            }
        }

    }

    private void readColumnPropsFromConfig(final NodeSettingsRO cfg)
            throws InvalidSettingsException {
        m_colProps = new TreeMap<Integer, ColProperty>();

        for (String key : cfg.keySet()) {
            Integer col = Integer.valueOf(key);
            m_colProps.put(col, new ColProperty(cfg.getNodeSettings(key)));
        }
    }
}
