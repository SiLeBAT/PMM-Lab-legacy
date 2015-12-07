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
package de.bund.bfr.knime.pmm.pmfwriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.DateInputDialog;

/**
 * StandardDialogComponent allowing the input of an user specified Date. Thereby the Date can be optional, as well as
 * the different fields of the date can be displayed as needed.
 *
 *
 * @author Sebastian Peter, University of Konstanz
 * @since 2.8
 */
public class DialogComponentDate extends DialogComponent {

    private DateInputDialog m_dialogcomp;

    private SettingsModelDate m_datemodel;

    /**
     * Instantiates a new DateDialogComponent, where the model stores the user input and the label is put as a
     * description on to the dialog. Using this constructor the date is optional.
     *
     * @param model to store the inputed date
     * @param label to place on the dialog
     */
    public DialogComponentDate(final SettingsModelDate model, final String label) {
        this(model, label, true);
    }

    /**
     * Instantiates a new DateDialogComponent, where the model stores the user input and the label is put as a
     * description on to the dialog. Using this constructor the date can be optional or mandatory.
     *
     *
     * @param model to store the inputed date
     * @param label to place on the dialog
     * @param optional specifies whether the date is optional (true) or mandatory (false)
     */
    public DialogComponentDate(final SettingsModelDate model, final String label, final boolean optional) {
        super(model);
        m_datemodel = model;
        m_dialogcomp = new DateInputDialog(DateInputDialog.Mode.NOTIME, optional);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label));
        panel.add(m_dialogcomp);
        getComponentPanel().add(panel);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponent() {
        m_dialogcomp.setDateAndMode(m_datemodel.getTimeInMillis(),
                                    DateInputDialog.getModeForStatus(m_datemodel.getSelectedFields()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettingsBeforeSave() throws InvalidSettingsException {
        m_datemodel.setTimeInMillis(m_dialogcomp.getSelectedDate().getTime());
        m_datemodel.setSelectedFields(m_dialogcomp.getIntForStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkConfigurabilityBeforeLoad(final PortObjectSpec[] specs) throws NotConfigurableException {
        //nothing todo here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEnabledComponents(final boolean enabled) {
        m_dialogcomp.setEnabled(enabled);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolTipText(final String text) {
        //todo !?

    }
}
