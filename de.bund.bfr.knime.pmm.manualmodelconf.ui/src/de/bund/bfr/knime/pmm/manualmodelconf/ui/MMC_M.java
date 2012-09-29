/*
 * Created by JFormDesigner on Sat Sep 29 12:27:04 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import de.bund.bfr.knime.pmm.common.ui.*;

/**
 * @author Armin Weiser
 */
public class MMC_M extends JPanel {
	public MMC_M() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		modelNameLabel = new JLabel();
		modelNameBox = new JComboBox();
		label2 = new JLabel();
		formulaArea = new JTextField();
		tableLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		table = new ModelTableModel();
		literatureLabel = new JLabel();

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Model Properties"),
			Borders.DLU2_BORDER));
		setLayout(new FormLayout(
			"default, 3*($lcgap, default:grow)",
			"3*(default, $lgap), default"));

		//---- modelNameLabel ----
		modelNameLabel.setText("Model from DB:");
		add(modelNameLabel, CC.xy(1, 1));
		add(modelNameBox, CC.xywh(3, 1, 5, 1));

		//---- label2 ----
		label2.setText("Model formula:");
		add(label2, CC.xy(1, 3));
		add(formulaArea, CC.xywh(3, 3, 5, 1));

		//---- tableLabel ----
		tableLabel.setText("Parameter definition:");
		add(tableLabel, CC.xy(1, 5));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(table);
		}
		add(scrollPane1, CC.xywh(3, 5, 5, 1));

		//---- literatureLabel ----
		literatureLabel.setText("Literatur:");
		add(literatureLabel, CC.xy(1, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel modelNameLabel;
	private JComboBox modelNameBox;
	private JLabel label2;
	private JTextField formulaArea;
	private JLabel tableLabel;
	private JScrollPane scrollPane1;
	private ModelTableModel table;
	private JLabel literatureLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
