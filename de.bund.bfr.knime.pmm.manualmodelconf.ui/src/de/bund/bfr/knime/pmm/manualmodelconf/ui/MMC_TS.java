/*
 * Created by JFormDesigner on Sat Sep 29 12:26:53 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.*;

/**
 * @author Armin Weiser
 */
public class MMC_TS extends JPanel {
	public MMC_TS() {
		initComponents();
	}

	public PmmTimeSeries getTS() throws PmmException {
		if (!temperatureField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		PmmTimeSeries tuple = new PmmTimeSeries();

		tuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrixField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_COMMENT, commentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_TEMPERATURE, temperatureField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_PH, phField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_WATERACTIVITY, waterActivityField.getValue());
		
		return tuple;
	}
	public void setTS(String agent, String matrix, String comment, double temp, double ph, double aw) throws PmmException {
		agentField.setText(agent);
		matrixField.setText(matrix);
		commentField.setText(comment);
		temperatureField.setValue(temp);
		phField.setValue(ph);
		waterActivityField.setValue(aw);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		agentLabel = new JLabel();
		agentField = new StringTextField(true);
		matrixLabel = new JLabel();
		matrixField = new StringTextField(true);
		commentLabel = new JLabel();
		commentField = new StringTextField(true);
		tempLabel = new JLabel();
		temperatureField = new DoubleTextField(true);
		phLabel = new JLabel();
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH, true);
		awLabel = new JLabel();
		waterActivityField = new DoubleTextField(PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY, true);

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Microbial Data Properties"),
			Borders.DLU2_BORDER));
		setLayout(new FormLayout(
			"default, $lcgap, default:grow",
			"5*(default, $lgap), default"));

		//---- agentLabel ----
		agentLabel.setText("Agent:");
		agentLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_AGENTNAME) + ":");
		add(agentLabel, CC.xy(1, 1));
		add(agentField, CC.xy(3, 1));

		//---- matrixLabel ----
		matrixLabel.setText("Matrix:");
		matrixLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_MATRIXNAME) + ":");
		add(matrixLabel, CC.xy(1, 3));
		add(matrixField, CC.xy(3, 3));

		//---- commentLabel ----
		commentLabel.setText(":");
		commentLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_COMMENT) + ":");
		add(commentLabel, CC.xy(1, 5));
		add(commentField, CC.xy(3, 5));

		//---- tempLabel ----
		tempLabel.setText("Temperature:");
		tempLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":");
		add(tempLabel, CC.xy(1, 7));
		add(temperatureField, CC.xy(3, 7));

		//---- phLabel ----
		phLabel.setText("pH:");
		phLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_PH) + ":");
		add(phLabel, CC.xy(1, 9));
		add(phField, CC.xy(3, 9));

		//---- awLabel ----
		awLabel.setText("aw:");
		awLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":");
		add(awLabel, CC.xy(1, 11));
		add(waterActivityField, CC.xy(3, 11));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel agentLabel;
	private StringTextField agentField;
	private JLabel matrixLabel;
	private StringTextField matrixField;
	private JLabel commentLabel;
	private StringTextField commentField;
	private JLabel tempLabel;
	private DoubleTextField temperatureField;
	private JLabel phLabel;
	private DoubleTextField phField;
	private JLabel awLabel;
	private DoubleTextField waterActivityField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
