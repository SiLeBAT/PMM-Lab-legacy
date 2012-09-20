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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.dim.bfr.GeschaetztStatistikModell;

public class PrimaryGraphsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PRIMARY_GRAPHS = "PRIMARY_GRAPHS";
	private JRadioButton modelsAndDataRadioButton;
	private String modelsAndDataText = "models and data";
	private String modelsOnlyText = "models";
	private String dataOnlyText = "data";
	private JRadioButton modelsOnlyRadioButton;
	private JRadioButton dataOnlyRadioButton;
	private ButtonGroup group;
	private JTable table;
	private JScrollPane primGraphJSP;
	private ModelsParamsTableModel noModelsModel;
	private ModelsParamsTableModel primModelsModel;
	private String noDataText;
	private String noModelsText;
	

	public PrimaryGraphsPanel() {
		super(new BorderLayout());
		this.setPreferredSize(new Dimension(700, 500));
		TitledBorder plotTypeBorder = BorderFactory.createTitledBorder("visualisation");
		
		modelsAndDataRadioButton 	= new JRadioButton(modelsAndDataText);
		modelsOnlyRadioButton 		= new JRadioButton(modelsOnlyText);
		dataOnlyRadioButton = new JRadioButton(dataOnlyText);				
		
		group = new ButtonGroup();
	    group.add(modelsAndDataRadioButton);
	    group.add(modelsOnlyRadioButton);
	    group.add(dataOnlyRadioButton);
	    
		JPanel radioButtonPanel = new JPanel(new BorderLayout());
		
		radioButtonPanel.add(modelsAndDataRadioButton,BorderLayout.NORTH);
		radioButtonPanel.add(modelsOnlyRadioButton,BorderLayout.CENTER);
		radioButtonPanel.add(dataOnlyRadioButton,BorderLayout.SOUTH);
		
		radioButtonPanel.setBorder(plotTypeBorder);
	    
		table = new JTable();
		refreshNoModelsData(table);
		primGraphJSP = new JScrollPane(table);
		
		this.add(radioButtonPanel, BorderLayout.NORTH);
		this.add(primGraphJSP, BorderLayout.CENTER);
		TabListener listener = new TabListener();
		this.addComponentListener(listener);
		this.setName(PRIMARY_GRAPHS);
	}

	public JRadioButton getModelsAndDataRadioButton() {
		return modelsAndDataRadioButton;
	}

	public void setModelsAndDataRadioButton(JRadioButton modelsAndDataRadioButton) {
		this.modelsAndDataRadioButton = modelsAndDataRadioButton;
	}

	public String getModelsAndDataText() {
		return modelsAndDataText;
	}

	public void setModelsAndDataText(String modelsAndDataText) {
		this.modelsAndDataText = modelsAndDataText;
	}

	public String getModelsOnlyText() {
		return modelsOnlyText;
	}

	public void setModelsOnlyText(String modelsOnlyText) {
		this.modelsOnlyText = modelsOnlyText;
	}

	public String getDataOnlyText() {
		return dataOnlyText;
	}

	public void setDataOnlyText(String dataOnlyText) {
		this.dataOnlyText = dataOnlyText;
	}

	public JRadioButton getModelsOnlyRadioButton() {
		return modelsOnlyRadioButton;
	}

	public void setModelsOnlyRadioButton(JRadioButton modelsOnlyRadioButton) {
		this.modelsOnlyRadioButton = modelsOnlyRadioButton;
	}

	public JRadioButton getDataOnlyRadioButton() {
		return dataOnlyRadioButton;
	}

	public void setDataOnlyRadioButton(JRadioButton dataOnlyRadioButton) {
		this.dataOnlyRadioButton = dataOnlyRadioButton;
	}

	public ButtonGroup getGroup() {
		return group;
	}

	public void setGroup(ButtonGroup group) {
		this.group = group;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable primModelsTable) {
		this.table = primModelsTable;
	}

	public JScrollPane getPrimGraphJSP() {
		return primGraphJSP;
	}

	public void setPrimGraphJSP(JScrollPane primGraphJSP) {
		this.primGraphJSP = primGraphJSP;
	}

	public ModelsParamsTableModel getNoModelsModel() {
		return noModelsModel;
	}

	public void setNoModelsModel(ModelsParamsTableModel noModelsModel) {
		this.noModelsModel = noModelsModel;
	}
	
	protected void refreshNoModelsData(final JTable... table) {
			for (final JTable t : table) {
				final Object[][] rowData = new Object[1][1];
				rowData[0][0] = "no data";
				SwingUtilities.invokeLater(new Runnable() {
		

					@Override
					public void run() {
						noModelsModel = new ModelsParamsTableModel(rowData, new String[] { "" });
						DataTableModelListener listener = new DataTableModelListener();
						noModelsModel.addTableModelListener(listener);
						TableModelEvent event = new TableModelEvent(noModelsModel);
						noModelsModel.newDataAvailable(event);
						t.setModel(noModelsModel);
						t.setRowSelectionAllowed(false);
						t.setColumnSelectionAllowed(false);
						t.setCellSelectionEnabled(false);
						TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(noModelsModel);
						t.setRowSorter(sorter);
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
		primModelsModel = new ModelsParamsTableModel(rowData, colData);
		DataTableModelListener listener = new DataTableModelListener();
		primModelsModel.addTableModelListener(listener);
		TableModelEvent event = new TableModelEvent(primModelsModel);
		primModelsModel.newDataAvailable(event);
		table.setModel(primModelsModel);
		table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("Modell")).setPreferredWidth(210);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(primModelsModel);
		table.setRowSorter(sorter);
		if (selectedRows!= null)
			SelectionHandler.setSelection(table, selectedRows);
	}

	public void updateRadioButtons(boolean dataAvailable, boolean modelsAvailable) {
		dataOnlyRadioButton.setEnabled(dataAvailable);
		modelsAndDataRadioButton.setEnabled(dataAvailable && modelsAvailable);
		modelsOnlyRadioButton.setEnabled(modelsAvailable);	
		
		if(dataAvailable && modelsAvailable){
			modelsAndDataRadioButton.setSelected(true);
		}else if(modelsAvailable){
			modelsOnlyRadioButton.setSelected(true);
		}else if(dataAvailable){
			dataOnlyRadioButton.setSelected(true);
		}

		String dataOnly = dataOnlyText;
		String modelsOnly = modelsOnlyText;
		String modelsAndData = modelsAndDataText;
		
		if(!dataAvailable){
			dataOnly += " ("+noDataText+")";
			modelsAndData += " ("+noDataText+")";
		}
		if(!modelsAvailable){
			modelsOnly += " ("+noModelsText+")";
			modelsAndData += " ("+noModelsText+")";
		}
		
		dataOnlyRadioButton.setText(dataOnly);
		modelsOnlyRadioButton.setText(modelsOnly);
		modelsAndDataRadioButton.setText(modelsAndData);
		
	}

	public String getPlotType() {
		if (this.getDataOnlyRadioButton().isSelected())
			return LoadRelatedDataNodeModel.PLOT_TYPE_D;
		if (this.getModelsOnlyRadioButton().isSelected())
			return LoadRelatedDataNodeModel.PLOT_TYPE_M;
		if (this.getModelsAndDataRadioButton().isSelected())
			return LoadRelatedDataNodeModel.PLOT_TYPE_MD;
		return null;
	}
	
	public void setPlotType(String plotType) {
		if (plotType != null) {
			if (plotType.equals(LoadRelatedDataNodeModel.PLOT_TYPE_M)) {
				this.modelsOnlyRadioButton.setSelected(true);
			}
			else if (plotType.equals(LoadRelatedDataNodeModel.PLOT_TYPE_D)) {
				this.dataOnlyRadioButton.setSelected(true);
			}
			else {
				this.modelsAndDataRadioButton.setSelected(true);
			}
		}
	}

	public void refreshPanel() {
		this.remove(this.getPrimGraphJSP());
		this.setPrimGraphJSP(new JScrollPane(this.getTable()));
		this.add(this.getPrimGraphJSP(), BorderLayout.CENTER);
	}
	
}
