/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.bund.bfr.knime.pmm.common.ui.StringTextField;

public class DataAndModelChartInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<String> ids;
	private List<List<String>> parameters;
	private List<List<String>> parameterValues;

	private JPanel labelPanel;
	private JPanel fieldPanel;

	public DataAndModelChartInfoPanel(List<String> ids,
			List<List<String>> parameters, List<List<String>> parameterValues) {
		this.ids = ids;
		this.parameters = parameters;
		this.parameterValues = parameterValues;

		labelPanel = new JPanel();
		fieldPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(0, 1, 5, 5));
		labelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		fieldPanel.setLayout(new GridLayout(0, 1, 5, 5));
		fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		JPanel viewPanel = new JPanel();
		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(labelPanel, BorderLayout.WEST);
		mainPanel.add(fieldPanel, BorderLayout.CENTER);
		viewPanel.setLayout(new BorderLayout());
		viewPanel.add(mainPanel, BorderLayout.NORTH);
		scrollPane.setViewportView(viewPanel);

		setBorder(BorderFactory.createTitledBorder("Info Box"));
		setLayout(new GridLayout(1, 1));
		add(scrollPane);
	}

	public void showID(String id) {
		labelPanel.removeAll();
		fieldPanel.removeAll();

		if (id != null) {
			int index = ids.indexOf(id);
			List<String> params = parameters.get(index);
			List<String> values = parameterValues.get(index);

			for (int i = 0; i < params.size(); i++) {
				StringTextField field = new StringTextField(true);

				field.setValue(values.get(i));
				labelPanel.add(new JLabel(params.get(i) + ":"));
				field.setEditable(false);
				fieldPanel.add(field);
			}
		}

		labelPanel.revalidate();
		fieldPanel.revalidate();
	}

}
