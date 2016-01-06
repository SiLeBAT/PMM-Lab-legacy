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
 * Configuration of a quick form node.
 *
 * @author Patrick Winter, KNIME.com AG, Zurich, Switzerland
 * @param <VAL> The value implementation of the quick form node.
 */
public abstract class QuickFormConfig
        <VAL extends DialogNodeValue> {

    private static final String CFG_LABEL = "label";
    private static final String CFG_DESCRIPTION = "description";
    private static final String CFG_HIDE_IN_WIZARD = "hideInWizard";
    private static final String CFG_HIDE_IN_DIALOG = "hideInDialog";
    private static final String CFG_DEFAULT_VALUE = "defaultValue";
    private static final String CFG_REQUIRED = "required";

    private static final String DEFAULT_LABEL = "Label";
    private static final String DEFAULT_DESCRIPTION = "Enter Description";
    private static final boolean DEFAULT_HIDE_IN_WIZARD = false;
    private static final boolean DEFAULT_HIDE_IN_DIALOG = false;
    private static final boolean DEFAULT_REQUIRED = true;

    private String m_label = DEFAULT_LABEL;
    private String m_description = DEFAULT_DESCRIPTION;
    private boolean m_hideInWizard = DEFAULT_HIDE_IN_WIZARD;
    private boolean m_hideInDialog = DEFAULT_HIDE_IN_DIALOG;
    private VAL m_defaultValue = createEmptyValue();
    private boolean m_required = DEFAULT_REQUIRED;

    /**
     * @return the label
     */
    public String getLabel() {
        return m_label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(final String label) {
        this.m_label = label;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.m_description = description;
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
     * @return the hideInDialog
     */
    public boolean getHideInDialog() {
        return m_hideInDialog;
    }

    /**
     * @param hideInDialog the hideInDialog to set
     */
    public void setHideInDialog(final boolean hideInDialog) {
        m_hideInDialog = hideInDialog;
    }

    /**
     * @return the required
     */
    public boolean getRequired() {
        return m_required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(final boolean required) {
        m_required = required;
    }

    /**
     * @param settings The settings to save to
     */
    public void saveSettings(final NodeSettingsWO settings) {
        NodeSettingsWO defaultValueSettings = settings.addNodeSettings(CFG_DEFAULT_VALUE);
        m_defaultValue.saveToNodeSettings(defaultValueSettings);
        settings.addString(CFG_LABEL, m_label);
        settings.addString(CFG_DESCRIPTION, m_description);
        settings.addBoolean(CFG_HIDE_IN_WIZARD, m_hideInWizard);
        settings.addBoolean(CFG_HIDE_IN_DIALOG, m_hideInDialog);
        settings.addBoolean(CFG_REQUIRED, m_required);
    }

    /**
     * @param settings The settings to load from
     * @throws InvalidSettingsException If the settings are not valid
     */
    public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        NodeSettingsRO defaultValueSettings = settings.getNodeSettings(CFG_DEFAULT_VALUE);
        m_defaultValue = createEmptyValue();
        m_defaultValue.loadFromNodeSettings(defaultValueSettings);
        m_label = settings.getString(CFG_LABEL);
        m_description = settings.getString(CFG_DESCRIPTION);
        m_hideInWizard = settings.getBoolean(CFG_HIDE_IN_WIZARD);
        m_hideInDialog = settings.getBoolean(CFG_HIDE_IN_DIALOG);
        m_required = settings.getBoolean(CFG_REQUIRED);
    }

    /**
     * @param settings The settings to load from
     */
    public void loadSettingsInDialog(final NodeSettingsRO settings) {
        m_defaultValue = createEmptyValue();
        NodeSettingsRO defaultValueSettings;
        try {
            defaultValueSettings = settings.getNodeSettings(CFG_DEFAULT_VALUE);
            m_defaultValue.loadFromNodeSettingsInDialog(defaultValueSettings);
        } catch (InvalidSettingsException e) {
            // Stay with defaults
        }
        m_label = settings.getString(CFG_LABEL, DEFAULT_LABEL);
        m_description = settings.getString(CFG_DESCRIPTION, DEFAULT_DESCRIPTION);
        m_hideInWizard = settings.getBoolean(CFG_HIDE_IN_WIZARD, DEFAULT_HIDE_IN_WIZARD);
        m_hideInDialog = settings.getBoolean(CFG_HIDE_IN_DIALOG, DEFAULT_HIDE_IN_DIALOG);
        m_required = settings.getBoolean(CFG_REQUIRED, DEFAULT_REQUIRED);
    }

    /**
     * @return the default value
     */
    public VAL getDefaultValue() {
        return m_defaultValue;
    }

    /**
     * Creates an instance of a value used for the default value of this config.
     *
     * @return Create a value instance
     */
    protected abstract VAL createEmptyValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("label=");
        sb.append(m_label);
        sb.append(", ");
        sb.append("description=");
        sb.append(m_description);
        sb.append(", ");
        sb.append("hideInWizard=");
        sb.append(m_hideInWizard);
        sb.append(", ");
        sb.append("hideInDialog=");
        sb.append(m_hideInDialog);
        sb.append(", ");
        sb.append("defaultValue=");
        sb.append("{");
        sb.append(m_defaultValue);
        sb.append("}");
        sb.append(", ");
        sb.append("required=");
        sb.append(m_required);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_label)
                .append(m_description)
                .append(m_hideInWizard)
                .append(m_hideInDialog)
                .append(m_defaultValue)
                .append(m_required)
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
        QuickFormConfig<VAL> other = (QuickFormConfig<VAL>)obj;
        return new EqualsBuilder()
                .append(m_label, other.m_label)
                .append(m_description, other.m_description)
                .append(m_hideInWizard, other.m_hideInWizard)
                .append(m_hideInDialog, other.m_hideInDialog)
                .append(m_defaultValue, other.m_defaultValue)
                .append(m_required, other.m_required)
                .isEquals();
    }

}
