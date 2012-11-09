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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import de.bund.bfr.knime.pmm.common.ui.TimeSeriesTable;

public class ChartSamplePanel extends JPanel implements CellEditorListener {

	private static final long serialVersionUID = 1L;

	private static final int ROW_COUNT = 10;

	private TimeSeriesTable table;

	private List<EditListener> listeners;

	public ChartSamplePanel() {
		table = new TimeSeriesTable(ROW_COUNT, true, false);
		table.getTimeColumn().getCellEditor().addCellEditorListener(this);
		listeners = new ArrayList<ChartSamplePanel.EditListener>();

		setLayout(new BorderLayout());
		add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
	}

	public void addEditListener(EditListener listener) {
		listeners.add(listener);
	}

	public void removeEditListener(EditListener listener) {
		listeners.remove(listener);
	}

	public List<Double> getTimeValues() {
		List<Double> timeList = new ArrayList<Double>();

		for (int i = 0; i < ROW_COUNT; i++) {
			if (table.getTime(i) != null) {
				timeList.add(table.getTime(i));
			}
		}

		return timeList;
	}

	public void setDataPoints(double[][] points) {
		Map<Double, Double> logcValues = new LinkedHashMap<Double, Double>();

		if (points != null && points.length == 2) {
			for (int i = 0; i < points[0].length; i++) {
				logcValues.put(points[0][i], points[1][i]);
			}
		}

		for (int i = 0; i < ROW_COUNT; i++) {
			Double logc = logcValues.get(table.getTime(i));

			if (logc != null) {
				table.setLogc(i, logc);
			}
		}

		table.repaint();
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		fireTimeValuesChanged();
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
	}

	private void fireTimeValuesChanged() {
		for (EditListener listener : listeners) {
			listener.timeValuesChanged();
		}
	}

	public static interface EditListener {

		public void timeValuesChanged();

	}

}
