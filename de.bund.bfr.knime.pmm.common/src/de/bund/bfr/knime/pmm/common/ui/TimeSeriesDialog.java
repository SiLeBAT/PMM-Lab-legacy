package de.bund.bfr.knime.pmm.common.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class TimeSeriesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	public TimeSeriesDialog(JComponent owner, List<TimeSeriesXml> timeSeries) {
		super(JOptionPane.getFrameForComponent(owner),
				AttributeUtilities.DATAPOINTS, true);

		JButton okButton = new JButton("OK");
		JPanel bottomPanel = new JPanel();

		okButton.addActionListener(this);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(okButton);

		setLayout(new BorderLayout());
		add(new JScrollPane(new TimeSeriesTable(timeSeries, false, false)),
				BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		pack();

		setResizable(false);
		setLocationRelativeTo(owner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
