/*
 * Created by JFormDesigner on Sat Sep 29 12:26:53 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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

		tuple.setValue(TimeSeriesSchema.ATT_AGENTNAME, agentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_MATRIXNAME, matrixField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agensDetailField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrixDetailField.getText());
		try {tuple.setValue(TimeSeriesSchema.ATT_AGENTID, Integer.parseInt(agensIDField.getText()));}
		catch (Exception e) {}
		try {tuple.setValue(TimeSeriesSchema.ATT_MATRIXID, Integer.parseInt(matrixIDField.getText()));}
		catch (Exception e) {}
		tuple.setValue(TimeSeriesSchema.ATT_COMMENT, commentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_TEMPERATURE, temperatureField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_PH, phField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_WATERACTIVITY, waterActivityField.getValue());
		
		return tuple;
	}
	public void setTS(PmmTimeSeries ts) throws PmmException {
		agentField.setText(ts.getAgentName() == null ? "" : ts.getAgentName());
		matrixField.setText(ts.getMatrixName() == null ? "" : ts.getMatrixName());
		agensDetailField.setText(ts.getAgentDetail() == null ? "" : ts.getAgentDetail());
		matrixDetailField.setText(ts.getMatrixDetail() == null ? "" : ts.getMatrixDetail());
		agensIDField.setText(ts.getAgentId() == null ? "" : ts.getAgentId()+"");
		matrixIDField.setText(ts.getMatrixId() == null ? "" : ts.getMatrixId()+"");
		commentField.setText(ts.getComment());
		if (!Double.isNaN(ts.getTemperature())) temperatureField.setValue(ts.getTemperature());
		if (!Double.isNaN(ts.getPh())) phField.setValue(ts.getPh());
		if (!Double.isNaN(ts.getWaterActivity())) waterActivityField.setValue(ts.getWaterActivity());
	}
	public void setTS(String agent, String agentDetail, Integer agentID, String matrix, String matrixDetail, Integer matrixID,
			String comment, double temp, double ph, double aw) throws PmmException {
		agentField.setText(agent);
		agensDetailField.setText(agentDetail);
		agensIDField.setText(agentID+"");
		matrixField.setText(matrix);
		matrixDetailField.setText(matrixDetail);
		matrixIDField.setText(matrixID+"");
		commentField.setText(comment);
		if (!Double.isNaN(temp)) temperatureField.setValue(temp); else temperatureField.setValue(null);
		if (!Double.isNaN(ph)) phField.setValue(ph); else phField.setValue(null);
		if (!Double.isNaN(aw)) waterActivityField.setValue(aw); else waterActivityField.setValue(null);
	}

	private void button1ActionPerformed(ActionEvent e) {
		MyTable age = DBKernel.myList.getTable("Agenzien");
		Integer agensID = null;
		try {agensID = Integer.parseInt(agensIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.myList.openNewWindow(
				age,
				agensID,
				(Object) "Agenzien",
				null,
				1,
				1,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			Object agensname = DBKernel.getValue("Agenzien", "ID", newVal.toString(), "Agensname");
			agentField.setText(agensname+"");
			agensDetailField.setText(""+DBKernel.getValue("Matrices", "ID", newVal.toString(), "AgensDetail"));
			agensIDField.setText(""+newVal);
		}		
	}

	private void button2ActionPerformed(ActionEvent e) {
		MyTable mat = DBKernel.myList.getTable("Matrices");
		Integer matrixID = null;
		try {matrixID = Integer.parseInt(matrixIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.myList.openNewWindow(
				mat,
				matrixID,
				(Object) "Matrices",
				null,
				1,
				1,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			Object matrixname = DBKernel.getValue("Matrices", "ID", newVal.toString(), "Matrixname");
			matrixField.setText(matrixname+"");
			matrixDetailField.setText(""+DBKernel.getValue("Matrices", "ID", newVal.toString(), "MatrixDetail"));
			matrixIDField.setText(""+newVal);
		}		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		agentLabel = new JLabel();
		agentField = new StringTextField(true);
		button1 = new JButton();
		agensDetailField = new StringTextField();
		agensIDField = new JTextField();
		matrixLabel = new JLabel();
		matrixField = new StringTextField(true);
		button2 = new JButton();
		matrixDetailField = new StringTextField();
		matrixIDField = new JTextField();
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
			Borders.DLU2));
		setLayout(new FormLayout(
			"default, $lcgap, default:grow, 3*($lcgap, default)",
			"5*(default, $lgap), default"));

		//---- agentLabel ----
		agentLabel.setText("Agent:");
		agentLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_AGENTNAME) + ":");
		add(agentLabel, CC.xy(1, 1));
		add(agentField, CC.xy(3, 1));

		//---- button1 ----
		button1.setText("...");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		add(button1, CC.xy(5, 1));

		//---- agensDetailField ----
		agensDetailField.setColumns(4);
		agensDetailField.setVisible(false);
		add(agensDetailField, CC.xy(7, 1));

		//---- agensIDField ----
		agensIDField.setColumns(5);
		agensIDField.setVisible(false);
		add(agensIDField, CC.xy(9, 1));

		//---- matrixLabel ----
		matrixLabel.setText("Matrix:");
		matrixLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_MATRIXNAME) + ":");
		add(matrixLabel, CC.xy(1, 3));
		add(matrixField, CC.xy(3, 3));

		//---- button2 ----
		button2.setText("...");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});
		add(button2, CC.xy(5, 3));

		//---- matrixDetailField ----
		matrixDetailField.setVisible(false);
		matrixDetailField.setColumns(5);
		add(matrixDetailField, CC.xy(7, 3));

		//---- matrixIDField ----
		matrixIDField.setColumns(5);
		matrixIDField.setVisible(false);
		add(matrixIDField, CC.xy(9, 3));

		//---- commentLabel ----
		commentLabel.setText(":");
		commentLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_COMMENT) + ":");
		add(commentLabel, CC.xy(1, 5));
		add(commentField, CC.xywh(3, 5, 3, 1));

		//---- tempLabel ----
		tempLabel.setText("Temperature:");
		tempLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":");
		add(tempLabel, CC.xy(1, 7));
		add(temperatureField, CC.xywh(3, 7, 3, 1));

		//---- phLabel ----
		phLabel.setText("pH:");
		phLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_PH) + ":");
		add(phLabel, CC.xy(1, 9));
		add(phField, CC.xywh(3, 9, 3, 1));

		//---- awLabel ----
		awLabel.setText("aw:");
		awLabel.setText(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":");
		add(awLabel, CC.xy(1, 11));
		add(waterActivityField, CC.xywh(3, 11, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel agentLabel;
	private StringTextField agentField;
	private JButton button1;
	private StringTextField agensDetailField;
	private JTextField agensIDField;
	private JLabel matrixLabel;
	private StringTextField matrixField;
	private JButton button2;
	private StringTextField matrixDetailField;
	private JTextField matrixIDField;
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
