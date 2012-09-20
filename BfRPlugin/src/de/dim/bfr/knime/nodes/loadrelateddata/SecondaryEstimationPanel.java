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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.emf.common.util.EList;

import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.KlasseTyp;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.knime.util.PluginUtils;

public class SecondaryEstimationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SECONDARY_ESTIMATION = "SECONDARY_ESTIMATION";

	private JComboBox modelNameBox;
	private JList modelTypeList;
	private JComboBox parameterBox;
	private JPanel selectionPanel;
	private JScrollPane modelTypeJSP;
	private int[] selectedPrimModelIDs;

	private int[] selectedParamIds;
	private String[] selectedModelTypes;
	private ArrayList<GeschaetztStatistikModell> selectedPrimModels;
	private GridBagConstraints gbc_modelTypeJSP;
	private JLabel modelNameLabel;
	private JLabel parameterLabel;
	private JLabel modelTypeLabel;

	public SecondaryEstimationPanel() {
		this.setName(SECONDARY_ESTIMATION);
		this.setPreferredSize(new Dimension(300,300));
		TabListener tabListener = new TabListener();
		this.addComponentListener(tabListener);

		selectionPanel = new JPanel();
		selectionPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		selectionPanel.setPreferredSize(new Dimension(300,300));
		GridBagLayout gbl_selectionPanel = new GridBagLayout();
		gbl_selectionPanel.columnWidths = new int[]{144, 144, 0};
		gbl_selectionPanel.rowHeights = new int[]{100, 100, 100, 0};
		gbl_selectionPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_selectionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		selectionPanel.setLayout(gbl_selectionPanel);
		
		modelNameLabel = new JLabel("model name");
		modelNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_modelNameLabel = new GridBagConstraints();
		gbc_modelNameLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_modelNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_modelNameLabel.gridx = 0;
		gbc_modelNameLabel.gridy = 0;
		selectionPanel.add(modelNameLabel, gbc_modelNameLabel);
		modelNameBox = new JComboBox();
		GridBagConstraints gbc_modelNameBox = new GridBagConstraints();
		gbc_modelNameBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_modelNameBox.insets = new Insets(0, 0, 5, 5);
		gbc_modelNameBox.gridx = 1;
		gbc_modelNameBox.gridy = 0;
		selectionPanel.add(modelNameBox, gbc_modelNameBox);
		modelNameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (modelNameBox.getSelectedObjects().length > 0) {
					String selectedSecModelName = modelNameBox.getModel()
							.getSelectedItem().toString();
					getSelectedPrimModels(selectedSecModelName,
							LoadRelatedDataNodeDialog.getGeschModelle());
					refreshParamBox(selectedPrimModels);
					refreshModelTypesList(selectedPrimModels);
				}
			}
		});
		parameterLabel = new JLabel("parameter");
		GridBagConstraints gbc_parameterLabel = new GridBagConstraints();
		gbc_parameterLabel.insets = new Insets(0, 0, 5, 5);
		gbc_parameterLabel.gridx = 0;
		gbc_parameterLabel.gridy = 1;
		selectionPanel.add(parameterLabel, gbc_parameterLabel);
		parameterBox = new JComboBox();
		GridBagConstraints gbc_parameterBox = new GridBagConstraints();
		gbc_parameterBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_parameterBox.insets = new Insets(0, 0, 5, 5);
		gbc_parameterBox.gridx = 1;
		gbc_parameterBox.gridy = 1;
		selectionPanel.add(parameterBox, gbc_parameterBox);
		modelTypeLabel = new JLabel("model types");
		GridBagConstraints gbc_modelTypeLabel = new GridBagConstraints();
		gbc_modelTypeLabel.insets = new Insets(0, 0, 0, 5);
		gbc_modelTypeLabel.gridx = 0;
		gbc_modelTypeLabel.gridy = 2;
		selectionPanel.add(modelTypeLabel, gbc_modelTypeLabel);
		modelTypeList = new JList();
		modelTypeJSP = new JScrollPane(modelTypeList);
		
		gbc_modelTypeJSP = new GridBagConstraints();
		gbc_modelTypeJSP.insets = new Insets(0, 0, 15, 5);
		gbc_modelTypeJSP.fill = GridBagConstraints.BOTH;
		gbc_modelTypeJSP.gridx = 1;
		gbc_modelTypeJSP.gridy = 2;
		selectionPanel.add(modelTypeJSP, gbc_modelTypeJSP);

		DefaultListModel listModel = new DefaultListModel();
		ListDataListener listener = new ModelListDataListener();
		listModel.addListDataListener(listener);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		modelTypeList.setModel(listModel);
		this.add(selectionPanel);
	}

	protected void getSelectedPrimModels(String selectedSecModelName,
			List<GeschaetztStatistikModell> geschModelle) {
		selectedPrimModels = new ArrayList<GeschaetztStatistikModell>();
		for (GeschaetztStatistikModell e : geschModelle) {
			if (e.getStatistikModel().getName().equals(selectedSecModelName))
				selectedPrimModels.add(e);
		}
		selectedPrimModelIDs = new int[selectedPrimModels.size()];
		for (int i = 0; i < selectedPrimModels.size(); i++) {
			selectedPrimModelIDs[i] = selectedPrimModels.get(i).getId();
		}
	}

	protected int[] getSelectedParam(
			List<GeschaetztStatistikModell> selectedSecModels) {
		String selectedParamName = parameterBox.getSelectedItem().toString();
		List<StatistikModellParameter> selectedParam = new ArrayList<StatistikModellParameter>();
		for (GeschaetztStatistikModell m : selectedSecModels) {
			for (StatistikModellParameter p : m.getStatistikModel()
					.getParameter()) {
				if (p.getName().equals(selectedParamName)) {
					selectedParam.add(p);
				}
			}
		}
		selectedParamIds = new int[selectedParam.size()];
		for (int i = 0; i < selectedParam.size(); i++) {
			selectedParamIds[i] = selectedParam.get(i).getId();
		}
		return selectedParamIds;
	}
	
	protected String getSelectedParamName(){
		return parameterBox.getSelectedItem().toString();
	}

	protected void refreshParamBox(
			List<GeschaetztStatistikModell> selectedSecModels) {
		List<String> paramNames = new ArrayList<String>();
		for (int i = 0; i < selectedSecModels.size(); i++) {
			EList<StatistikModellParameter> params = selectedSecModels.get(i)
					.getStatistikModel().getParameter();
			for (StatistikModellParameter p : params) {
				if (p.getRole().equals(ParameterRoleType.PARAMETER))
					paramNames.add(p.getName());
			}
		}
		parameterBox.removeAllItems();
		@SuppressWarnings("unchecked")
		List<String> paramNamesUnique = PluginUtils
				.createUniqueList(paramNames, true);
		for (String e : paramNamesUnique) {
			parameterBox.addItem(e);
		}
	}

	protected void refreshModelTypesList(
			List<GeschaetztStatistikModell> selectedSecModels) {
		selectionPanel.remove(modelTypeJSP);
		DefaultListModel listModel = (DefaultListModel) modelTypeList
				.getModel();
		listModel.removeAllElements();
		List<String> modelTypes = new ArrayList<String>();
		for (int i = 8; i<15; i++) {
			modelTypes.add(KlasseTyp.get(i).getLiteral());
		}
		@SuppressWarnings("unchecked")
		List<String> modelTypesUniqe = PluginUtils.createUniqueList(modelTypes, true);
		for (String e : modelTypesUniqe) {
			listModel.addElement(e);
		}

		modelTypeList = new JList(listModel);
		modelTypeList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							getSelectedModelTypes(modelTypeList.getSelectedValues());
							
						}
					}
				});
		modelTypeList.setSelectedIndex(0);
		modelTypeJSP = new JScrollPane(modelTypeList);
		selectionPanel.add(modelTypeJSP, gbc_modelTypeJSP);
	}

	protected void getSelectedModelTypes(Object[] selectedValues) {
		List<String> selections = new ArrayList<String>();
		for (Object o : selectedValues) {
			selections.add(o.toString());
		}
		selectedModelTypes = selections.toArray(new String[selections.size()]);
	}

	public JComboBox getModelNameBox() {
		return modelNameBox;
	}

	public void setModelNameBox(JComboBox modelNameBox) {
		this.modelNameBox = modelNameBox;
	}

	public JList getModelTypeList() {
		return modelTypeList;
	}

	public void setModelTypeList(JList modelTypeList) {
		this.modelTypeList = modelTypeList;
	}

	public JComboBox getParameterBox() {
		return parameterBox;
	}

	public void setParameterBox(JComboBox parameterBox) {
		this.parameterBox = parameterBox;
	}

	public JPanel getSelectionPanel() {
		return selectionPanel;
	}

	public void setSelectionPanel(JPanel selectionPanel) {
		this.selectionPanel = selectionPanel;
	}

	public JScrollPane getModelTypeJSP() {
		return modelTypeJSP;
	}

	public void setModelTypeJSP(JScrollPane modelTypeJSP) {
		this.modelTypeJSP = modelTypeJSP;
	}

	public int[] getSelectedPrimModelIDs() {
		return selectedPrimModelIDs;
	}

	public void setSelectedPrimModelIDs(int[] selectedPrimModelIDs) {
		this.selectedPrimModelIDs = selectedPrimModelIDs;
	}

	public ArrayList<GeschaetztStatistikModell> getSelectedPrimModels() {
		return selectedPrimModels;
	}

	public void setSelectedPrimModels(
			ArrayList<GeschaetztStatistikModell> selectedPrimModels) {
		this.selectedPrimModels = selectedPrimModels;
	}

	public int[] getSelectedParamIds() {
		return selectedParamIds;
	}

	public void setSelectedParamIds(int[] selectedParamIds) {
		this.selectedParamIds = selectedParamIds;
	}

	public String[] getSelectedModelTypes() {
		return selectedModelTypes;
	}

	public void setSelectedModelTypes(String[] selectedModelTypes) {
		this.selectedModelTypes = selectedModelTypes;
	}

	public void refreshNoModelsData() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				modelNameBox.removeAllItems();
				parameterBox.removeAllItems();
				modelTypeList.clearSelection();
				disablePanel();
			}
		});
	}
	private void disablePanel() {
		modelNameLabel.setEnabled(false);
		modelNameBox.setEnabled(false);
		parameterLabel.setEnabled(false);
		parameterBox.setEnabled(false);
		modelTypeLabel.setEnabled(false);
		modelTypeList.setEnabled(false);
	}

	public void refreshPanel(List<GeschaetztStatistikModell> primGeschModelle) {
		List<String> modelNames = new ArrayList<String>();
		for (int i = 0; i < primGeschModelle.size(); i++) {
			modelNames.add(primGeschModelle.get(i).getStatistikModel().getName());
		}
		
		@SuppressWarnings("unchecked")
		final List<String> modelNamesUnique = PluginUtils
				.createUniqueList(modelNames, true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				enablePanel();
				getModelNameBox().removeAllItems();
				for (String e : modelNamesUnique) {
					getModelNameBox().addItem(e);
				}
			}
		});
	}

	private void enablePanel() {
		modelNameLabel.setEnabled(true);
		modelNameBox.setEnabled(true);
		parameterLabel.setEnabled(true);
		parameterBox.setEnabled(true);
		modelTypeLabel.setEnabled(true);
		modelTypeList.setEnabled(true);	
	}

	public void restoreSettings(final String modelName, final String paramName,
			final int[] modelTypesIndices) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				modelNameBox.setSelectedItem(modelName);
				parameterBox.setSelectedItem(paramName);
				modelTypeList.setSelectedIndices(modelTypesIndices);			
			}
		});

	}
}
