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
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SelectOptionsPanel extends JPanel implements ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PRIMARY_GRAPHS = "primary graphs";
	private static final String PRIMARY_ESTIMATION = "primary estimation";
	private static final String SECONDARY_ESTIMATION = "secondary estimation";
	private static final String PRIMARY_FORECASTING = "primary forecasting";
	private static final String SECONDARY_FORECASTING = "secondary forecasting";
	private String activeComponent;
	private JComboBox optionsBox;
	private JPanel optionsBoxPanel;
	private JPanel containerPanel;
	private PrimaryGraphsPanel primaryGraphsPanel;
	private PrimaryForecastingPanel primaryForecastingPanel;
	private PrimaryEstimationPanel primaryEstimationPanel;
	private SecondaryForecastingPanel secondaryForecastingPanel;
	private SecondaryEstimationPanel secondaryEstimationPanel;
	
	public SelectOptionsPanel() {
		super(new BorderLayout());
		optionsBoxPanel = new JPanel();
		String[] options = new String[]{PRIMARY_GRAPHS, PRIMARY_FORECASTING, PRIMARY_ESTIMATION, SECONDARY_FORECASTING, SECONDARY_ESTIMATION};
		optionsBox = new JComboBox(options);
		optionsBox.setEditable(false);
		
		optionsBox.addItemListener(this);
		optionsBoxPanel.add(optionsBox);
		optionsBoxPanel.setBorder(BorderFactory.createTitledBorder("load data for: "));
		
		primaryGraphsPanel = new PrimaryGraphsPanel();
		primaryForecastingPanel = new PrimaryForecastingPanel();
		primaryEstimationPanel = new PrimaryEstimationPanel();
		secondaryForecastingPanel = new SecondaryForecastingPanel();
		secondaryEstimationPanel = new SecondaryEstimationPanel();
		
		containerPanel = new JPanel(new CardLayout());
		containerPanel.add(primaryGraphsPanel, PRIMARY_GRAPHS);
		containerPanel.add(primaryEstimationPanel, PRIMARY_ESTIMATION);
		containerPanel.add(primaryForecastingPanel, PRIMARY_FORECASTING);
		containerPanel.add(secondaryForecastingPanel, SECONDARY_FORECASTING);
		containerPanel.add(secondaryEstimationPanel, SECONDARY_ESTIMATION);
		
		this.add(optionsBoxPanel, BorderLayout.PAGE_START);
		this.add(containerPanel, BorderLayout.CENTER);
		this.activeComponent = optionsBox.getSelectedItem().toString();
	}

	public JComboBox getOptionsBox() {
		return optionsBox;
	}

	public void setOptionsBox(JComboBox optionsBox) {
		this.optionsBox = optionsBox;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		CardLayout containerLayout = (CardLayout) containerPanel.getLayout();
		containerLayout.show(containerPanel, (String) e.getItem());
		this.activeComponent = (String)e.getItem();
	}

	public PrimaryGraphsPanel getPrimaryGraphsPanel() {
		return primaryGraphsPanel;
	}

	public void setPrimaryGraphsPanel(PrimaryGraphsPanel primaryGraphsPanel) {
		this.primaryGraphsPanel = primaryGraphsPanel;
	}

	public JPanel getOptionsBoxPanel() {
		return optionsBoxPanel;
	}

	public void setOptionsBoxPanel(JPanel optionsBoxPanel) {
		this.optionsBoxPanel = optionsBoxPanel;
	}

	public SecondaryEstimationPanel getSecondaryEstimationPanel() {
		return secondaryEstimationPanel;
	}

	public void setSecondaryEstimationPanel(
			SecondaryEstimationPanel secondaryEstimationPanel) {
		this.secondaryEstimationPanel = secondaryEstimationPanel;
	}

	public PrimaryForecastingPanel getPrimaryForecastingPanel() {
		return primaryForecastingPanel;
	}

	public void setPrimaryForecastingPanel(
			PrimaryForecastingPanel primaryForecastingPanel) {
		this.primaryForecastingPanel = primaryForecastingPanel;
	}

	public JPanel getContainerPanel() {
		return containerPanel;
	}

	public void setContainerPanel(JPanel containerPanel) {
		this.containerPanel = containerPanel;
	}

	public String getActiveComponent() {
		return activeComponent;
	}

	public void setActiveComponent(String activeComponent) {
		CardLayout containerLayout = (CardLayout) containerPanel.getLayout();
		containerLayout.show(containerPanel, activeComponent);
		optionsBox.getModel().setSelectedItem(activeComponent);
	}

	public SecondaryForecastingPanel getSecondaryForecastingPanel() {
		return secondaryForecastingPanel;
	}

	public void setSecondaryForecastingPanel(
			SecondaryForecastingPanel secondaryForecastingPanel) {
		this.secondaryForecastingPanel = secondaryForecastingPanel;
	}

	public void setPrimaryEstimationPanel(PrimaryEstimationPanel primaryEstimationPanel) {
		this.primaryEstimationPanel = primaryEstimationPanel;
	}

	public PrimaryEstimationPanel getPrimaryEstimationPanel() {
		return primaryEstimationPanel;
	}

}
