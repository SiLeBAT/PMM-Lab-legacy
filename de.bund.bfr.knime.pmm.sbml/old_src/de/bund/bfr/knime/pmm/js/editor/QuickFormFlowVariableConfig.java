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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.dialog.DialogNodeValue;

/**
 * Configuration of a flow variable quick form node.
 *
 * @author Patrick Winter, KNIME.com AG, Zurich, Switzerland
 * @param <VAL> The value used for the default value
 */
public abstract class QuickFormFlowVariableConfig
        <VAL extends DialogNodeValue>
        extends QuickFormConfig<VAL> {

    private static final String CFG_FLOW_VARIABLE_NAME = "flowvariablename";

    private static final String DEFAULT_FLOW_VARIABLE_NAME = "new variable";

    private String m_flowVariableName = DEFAULT_FLOW_VARIABLE_NAME;

    /**
     * @return the flowVariableName
     */
    public String getFlowVariableName() {
        return m_flowVariableName;
    }

    /**
     * @param flowVariableName the flowVariableName to set
     */
    public void setFlowVariableName(final String flowVariableName) {
        this.m_flowVariableName = flowVariableName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        super.loadSettings(settings);
        m_flowVariableName = settings.getString(CFG_FLOW_VARIABLE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettingsInDialog(final NodeSettingsRO settings) {
        super.loadSettingsInDialog(settings);
        m_flowVariableName = settings.getString(CFG_FLOW_VARIABLE_NAME, DEFAULT_FLOW_VARIABLE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings(final NodeSettingsWO settings) {
        super.saveSettings(settings);
        settings.addString(CFG_FLOW_VARIABLE_NAME, m_flowVariableName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", ");
        sb.append("flowVariableName=");
        sb.append(m_flowVariableName);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode())
                .append(m_flowVariableName)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        QuickFormFlowVariableConfig<VAL> other = (QuickFormFlowVariableConfig<VAL>)obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(m_flowVariableName, other.m_flowVariableName)
                .isEquals();
    }

}
