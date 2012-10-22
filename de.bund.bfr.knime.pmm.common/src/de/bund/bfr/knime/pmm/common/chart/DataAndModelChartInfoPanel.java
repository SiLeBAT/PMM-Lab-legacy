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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.FormattedDoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesTable;

public class DataAndModelChartInfoPanel extends JPanel implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private List<String> ids;
	private List<List<String>> parameters;
	private List<List<?>> parameterValues;

	private JPanel labelPanel;
	private JPanel fieldPanel;

	private Map<JButton, List<Point2D.Double>> timeSeriesByButton;

	public DataAndModelChartInfoPanel(List<String> ids,
			List<List<String>> parameters, List<List<?>> parameterValues) {
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

	@SuppressWarnings("unchecked")
	public void showID(String id) {
		labelPanel.removeAll();
		fieldPanel.removeAll();
		timeSeriesByButton = new LinkedHashMap<JButton, List<Point2D.Double>>();

		if (id != null) {
			int index = ids.indexOf(id);
			List<String> params = parameters.get(index);
			List<?> values = parameterValues.get(index);

			for (int i = 0; i < params.size(); i++) {
				labelPanel.add(new JLabel(params.get(i) + ":"));

				if (values.get(i) instanceof String || values.get(i) == null) {
					StringTextField field = new StringTextField(true);

					field.setValue((String) values.get(i));
					field.setEditable(false);
					fieldPanel.add(field);
				} else if (values.get(i) instanceof Double) {
					FormattedDoubleTextField field = new FormattedDoubleTextField(
							true);

					field.setValue((Double) values.get(i));
					field.setEditable(false);
					fieldPanel.add(field);
				} else if (values.get(i) instanceof List) {
					List<Point2D.Double> list = (List<Point2D.Double>) values
							.get(i);
					JPanel buttonPanel = new JPanel();
					JButton button = new JButton("View");

					button.addActionListener(this);
					buttonPanel.setLayout(new BorderLayout());
					buttonPanel.add(button, BorderLayout.WEST);
					timeSeriesByButton.put(button, list);
					fieldPanel.add(buttonPanel);
				}
			}

			for (int i = 0; i < params.size(); i++) {
				Component label = labelPanel.getComponent(i);
				Component comp = fieldPanel.getComponent(i);

				label.setPreferredSize(new Dimension(
						label.getPreferredSize().width,
						comp.getPreferredSize().height));
			}
		}

		labelPanel.revalidate();
		fieldPanel.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Point2D.Double> timeSeries = timeSeriesByButton.get(e.getSource());
		TimeSeriesDialog dialog = new TimeSeriesDialog(timeSeries);

		dialog.setVisible(true);
	}

	private class TimeSeriesDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		public TimeSeriesDialog(List<Point2D.Double> timeSeries) {
			super(JOptionPane
					.getFrameForComponent(DataAndModelChartInfoPanel.this),
					TimeSeriesSchema.DATAPOINTS, true);

			JButton okButton = new JButton("OK");
			JPanel bottomPanel = new JPanel();

			okButton.addActionListener(this);
			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(new JScrollPane(new TimeSeriesTable(timeSeries, false)),
					BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(DataAndModelChartInfoPanel.this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

}
