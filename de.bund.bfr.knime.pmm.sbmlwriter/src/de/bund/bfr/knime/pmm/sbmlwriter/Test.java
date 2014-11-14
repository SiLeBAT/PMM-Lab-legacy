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

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class Test extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int LEVEL = 2;
	private static final int VERSION = 4;

	private static String[] TRANSFORMATIONS = { null, "log10", "ln" };

	private JTextField idField;
	private JComboBox<String> transformBox;

	private List<JComboBox<Unit.Kind>> kindBoxes;
	private List<JTextField> scaleFields;
	private List<JTextField> exponentFields;
	private List<JTextField> multiplierFields;

	private JButton computeButton;
	private JTextField stringField;
	private JButton copyButton;
	private JTextField sbmlField;

	@SuppressWarnings("deprecation")
	public Test() {
		super("Units to SBML");

		idField = new JTextField();
		transformBox = new JComboBox<>(TRANSFORMATIONS);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridLayout(1, 4));
		northPanel.add(new JLabel("ID"));
		northPanel.add(idField);
		northPanel.add(new JLabel("Transform"));
		northPanel.add(transformBox);

		kindBoxes = new ArrayList<>();
		scaleFields = new ArrayList<>();
		exponentFields = new ArrayList<>();
		multiplierFields = new ArrayList<>();

		JPanel centerPanel = new JPanel();

		centerPanel.setLayout(new GridLayout(5, 4));
		centerPanel.add(new JLabel("Kind"));
		centerPanel.add(new JLabel("Scale"));
		centerPanel.add(new JLabel("Exponent"));
		centerPanel.add(new JLabel("Multiplier"));

		for (int i = 0; i < 4; i++) {
			List<Unit.Kind> units = new ArrayList<>();

			units.add(null);
			units.addAll(Arrays.asList(Unit.Kind.values()));
			units.remove(Unit.Kind.CELSIUS);
			units.remove(Unit.Kind.LITER);
			units.remove(Unit.Kind.METER);

			JComboBox<Unit.Kind> box = new JComboBox<>(
					units.toArray(new Unit.Kind[0]));
			JTextField scaleField = new JTextField();
			JTextField expField = new JTextField();
			JTextField mulField = new JTextField();

			centerPanel.add(box);
			centerPanel.add(scaleField);
			centerPanel.add(expField);
			centerPanel.add(mulField);

			kindBoxes.add(box);
			scaleFields.add(scaleField);
			exponentFields.add(expField);
			multiplierFields.add(mulField);
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
				double multiplier = 1.0;

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

				try {
					multiplier = Double.parseDouble(multiplierFields.get(i)
							.getText().trim());
				} catch (Exception ex) {
				}

				Unit u = new Unit(kind, LEVEL, VERSION);

				if (scale != 0) {
					u.setScale(scale);
				}

				if (exponent != 1.0) {
					u.setExponent(exponent);
				}

				if (multiplier != 1.0) {
					u.setMultiplier(multiplier);
				}

				unit.addUnit(u);
			}

			if (transformBox.getSelectedItem() != null) {
				Annotation annotation = new Annotation();
				XMLAttributes attributes = new XMLAttributes();

				attributes.add("name", (String) transformBox.getSelectedItem());
				annotation.setNonRDFAnnotation(new XMLNode(new XMLTriple(
						"transformation", null, null), attributes));
				unit.setAnnotation(annotation);
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
