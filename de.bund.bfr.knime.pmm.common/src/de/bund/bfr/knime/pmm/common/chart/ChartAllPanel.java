package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class ChartAllPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JSplitPane splitPane;

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel) {
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.EAST);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel,
				selectionPanel);

		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel,
			ChartSamplePanel samplePanel) {
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.EAST);

		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(selectionPanel, BorderLayout.CENTER);
		bottomPanel.add(samplePanel, BorderLayout.EAST);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel,
				bottomPanel);

		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	public int getDividerLocation() {
		return splitPane.getDividerLocation();
	}

	public void setDividerLocation(int location) {
		splitPane.setDividerLocation(location);
	}
}
