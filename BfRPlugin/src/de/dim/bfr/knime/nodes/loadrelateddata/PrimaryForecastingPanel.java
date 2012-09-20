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
package de.dim.bfr.knime.nodes.loadrelateddata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.dim.bfr.GeschaetztStatistikModell;

public class PrimaryForecastingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PRIMARY_FORECASTING= "PRIMARY_FORECASTING";
	private JTable table;
	private JScrollPane modelsJSP;
	private ModelsParamsTableModel modelsModel;

	public PrimaryForecastingPanel() {
		super(new BorderLayout());
		this.setName(PRIMARY_FORECASTING);
		this.setPreferredSize(new Dimension(700, 500));
		TabListener listener = new TabListener();
		this.addComponentListener(listener);
		
		table = new JTable();
		refreshNoModelsData(table);
		modelsJSP = new JScrollPane(table);
		this.add(modelsJSP, BorderLayout.CENTER);
	}

	protected void refreshNoModelsData(final JTable... table) {
		for (final JTable t : table) {
			final Object[][] rowData = new Object[1][1];
			rowData[0][0] = "no data";
			SwingUtilities.invokeLater(new Runnable() {

				private ModelsParamsTableModel noModelsModel;

				@Override
				public void run() {
					noModelsModel = new ModelsParamsTableModel(rowData,
							new String[] { "" });
					DataTableModelListener listener = new DataTableModelListener();
					noModelsModel.addTableModelListener(listener);
					TableModelEvent event = new TableModelEvent(noModelsModel);
					noModelsModel.newDataAvailable(event);
					t.setModel(noModelsModel);
					t.setRowSelectionAllowed(false);
					t.setColumnSelectionAllowed(false);
					t.setCellSelectionEnabled(false);
				}
			});
		}
	}
	
	public void refreshPrimModelsPanel(
			List<GeschaetztStatistikModell> geschModelle, int[] selectedRows) {
		Object[][] rowData = new Object[geschModelle.size()][8];

		for (int i = 0; i < geschModelle.size(); i++) {
			rowData[i][0] = geschModelle.get(i).getId();
			rowData[i][1] = geschModelle.get(i).getBedingung().getId();
			rowData[i][2] = geschModelle.get(i).getStatistikModel().getName();
			rowData[i][3] = geschModelle.get(i).isManuellEingetragen();
			rowData[i][4] = geschModelle.get(i).getRSquared();
			rowData[i][5] = geschModelle.get(i).getRss();
			rowData[i][6] = geschModelle.get(i).getScore();
			if (geschModelle.get(i).getKommentar()!= null) {
				if (geschModelle.get(i).getKommentar().length() >= 30)
					rowData[i][7] = geschModelle.get(i).getKommentar().substring(0, 30);
				else
					rowData[i][7] = geschModelle.get(i).getKommentar();
			}else {
				rowData[i][7] = "";
			}
		}
		String[] colData = new String[]{"ID", "Versuchsbedingung", "Modell", "ManuellEingetragen", "Rsquared", "RSS", "Score", "Kommentar"};
		modelsModel = new ModelsParamsTableModel(rowData, colData);
		DataTableModelListener listener = new DataTableModelListener();
		modelsModel.addTableModelListener(listener);
		TableModelEvent event = new TableModelEvent(modelsModel);
		modelsModel.newDataAvailable(event);
		table.setModel(modelsModel);
		table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("Modell")).setPreferredWidth(210);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(modelsModel);
		table.setRowSorter(sorter);
		if (selectedRows!= null)
			SelectionHandler.setSelection(table, selectedRows);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable modelsTable) {
		this.table = modelsTable;
	}

	public JScrollPane getModelsJSP() {
		return modelsJSP;
	}

	public void setModelsJSP(JScrollPane modelsJSP) {
		this.modelsJSP = modelsJSP;
	}

	public ModelsParamsTableModel getModelsModel() {
		return modelsModel;
	}

	public void setModelsModel(ModelsParamsTableModel modelsModel) {
		this.modelsModel = modelsModel;
	}
	
	public void refreshPanel() {
		this.remove(this.getModelsJSP());
		this.setModelsJSP(new JScrollPane(this.getTable()));
		this.add(this.getModelsJSP());
	}
}
