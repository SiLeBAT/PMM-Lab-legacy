package de.bund.bfr.knime.pmm.sbmlwriter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

public class Test extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int LEVEL = 2;
	private static final int VERSION = 4;

	private JTextField idField;

	private List<JComboBox<Unit.Kind>> kindBoxes;
	private List<JTextField> scaleFields;
	private List<JTextField> exponentFields;

	private JButton computeButton;
	private JTextField stringField;
	private JButton copyButton;
	private JTextField sbmlField;

	@SuppressWarnings("deprecation")
	public Test() {
		super("Units to SBML");

		idField = new JTextField();

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridLayout(1, 1));
		northPanel.add(new JLabel("ID"));
		northPanel.add(idField);

		kindBoxes = new ArrayList<JComboBox<Unit.Kind>>();
		scaleFields = new ArrayList<JTextField>();
		exponentFields = new ArrayList<JTextField>();

		JPanel centerPanel = new JPanel();

		centerPanel.setLayout(new GridLayout(5, 3));
		centerPanel.add(new JLabel("Kind"));
		centerPanel.add(new JLabel("Scale"));
		centerPanel.add(new JLabel("Exponent"));

		for (int i = 0; i < 4; i++) {
			List<Unit.Kind> units = new ArrayList<Unit.Kind>();

			units.add(null);
			units.addAll(Arrays.asList(Unit.Kind.values()));
			units.remove(Unit.Kind.CELSIUS);
			units.remove(Unit.Kind.LITER);
			units.remove(Unit.Kind.METER);

			JComboBox<Unit.Kind> box = new JComboBox<Unit.Kind>(
					units.toArray(new Unit.Kind[0]));
			JTextField scaleField = new JTextField();
			JTextField expField = new JTextField();

			centerPanel.add(box);
			centerPanel.add(scaleField);
			centerPanel.add(expField);

			kindBoxes.add(box);
			scaleFields.add(scaleField);
			exponentFields.add(expField);
		}

		computeButton = new JButton("Compute");
		computeButton.addActionListener(this);
		stringField = new JTextField();
		stringField.setEditable(false);
		copyButton = new JButton("Copy");
		copyButton.addActionListener(this);
		sbmlField = new JTextField();
		sbmlField.setEditable(false);

		JPanel southPanel = new JPanel();

		southPanel.setLayout(new GridLayout(2, 2));
		southPanel.add(computeButton);
		southPanel.add(stringField);
		southPanel.add(copyButton);
		southPanel.add(sbmlField);

		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Test();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == computeButton) {
			UnitDefinition unit = new UnitDefinition(idField.getText().trim(),
					LEVEL, VERSION);

			for (int i = 0; i < 4; i++) {
				Unit.Kind kind = (Unit.Kind) kindBoxes.get(i).getSelectedItem();

				if (kind == null) {
					continue;
				}

				int scale = 0;
				double exponent = 1.0;

				try {
					scale = Integer.parseInt(scaleFields.get(i).getText()
							.trim());
				} catch (Exception ex) {
				}

				try {
					exponent = Double.parseDouble(exponentFields.get(i)
							.getText().trim());
				} catch (Exception ex) {
				}

				if (scale != 0 && exponent != 1.0) {
					unit.addUnit(new Unit(scale, kind, exponent, LEVEL, VERSION));
				} else if (scale != 0) {
					unit.addUnit(new Unit(scale, kind, LEVEL, VERSION));
				} else if (exponent != 1.0) {
					unit.addUnit(new Unit(kind, exponent, LEVEL, VERSION));
				} else {
					unit.addUnit(new Unit(kind, LEVEL, VERSION));
				}
			}

			stringField.setText(unit.toString());

			try {
				sbmlField.setText(SBMLUtilities.toXml(unit));
				sbmlField.selectAll();
			} catch (Exception ex) {
				sbmlField.setText(ex.getMessage());
			}
		} else if (e.getSource() == copyButton) {
			StringSelection stsel = new StringSelection(sbmlField.getText());

			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(stsel, stsel);
		}
	}
}
