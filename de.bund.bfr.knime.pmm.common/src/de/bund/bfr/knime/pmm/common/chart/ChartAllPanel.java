package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class ChartAllPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel) {
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel,
				selectionPanel), BorderLayout.CENTER);
	}

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel,
			ChartSamplePanel samplePanel) {
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.EAST);

		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(selectionPanel, BorderLayout.CENTER);
		bottomPanel.add(samplePanel, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, bottomPanel),
				BorderLayout.CENTER);
	}
}
