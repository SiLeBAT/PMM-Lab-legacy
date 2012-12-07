/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
/*
 * Created by JFormDesigner on Wed Apr 25 14:36:15 CEST 2012
 */

package de.bund.bfr.knime.foodprocess.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import com.jgoodies.forms.factories.*;

import org.knime.core.node.InvalidSettingsException;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import de.bund.bfr.knime.foodprocess.FoodProcessNodeModel;
import de.bund.bfr.knime.foodprocess.lib.AgentsDef;
import de.bund.bfr.knime.foodprocess.lib.FoodProcessDef;
import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.lib.InPortDef;
import de.bund.bfr.knime.foodprocess.lib.OutPortDef;
import de.bund.bfr.knime.foodprocess.lib.ParametersDef;

/**
 * @author Armin Weiser
 */
public class FoodProcessUi extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8499308737142764556L;
	FoodProcessDef foodProcessDef;
	
	public FoodProcessUi() {
		//initComponents();
		myInitComponents();
	}

	public void setSettings(final FoodProcessSetting fps) {
		foodProcessDef.setSetting(fps);
	}
	public FoodProcessSetting getSettings() throws InvalidSettingsException {
		return foodProcessDef.getSetting();
	}
	private void myInitComponents() {
		ParametersDef foodProcessParamsDef;
		InPortDef[] inPortDef;
		OutPortDef[] outPortDef;
		ParametersDef portParameterDef;
		AgentsDef agentsDef;

		initComponentsCopy();
		
		foodProcessDef = new FoodProcessDef(FoodProcessNodeModel.N_PORT_IN, FoodProcessNodeModel.N_PORT_OUT);
		foodProcessParamsDef = foodProcessDef.getParametersDef();
		
		panel3.add(foodProcessDef.getNameBox(), CC.xywh(3, 1, 5, 1));

		panel1.add(foodProcessParamsDef.getTemperature(), CC.xy(3, 1));
		panel1.add(foodProcessParamsDef.getTemperature_func(), CC.xy(5, 1));
		panel1.add(foodProcessParamsDef.getTemperatureUnit(), CC.xy(7, 1));

		panel1.add(foodProcessParamsDef.getPh(), CC.xy(3, 3));
		panel1.add(foodProcessParamsDef.getPh_func(), CC.xy(5, 3));
		
		panel1.add(foodProcessParamsDef.getAw(), CC.xy(3, 5));
		panel1.add(foodProcessParamsDef.getAw_func(), CC.xy(5, 5));
		
		panel1.add(foodProcessParamsDef.getPressure(), CC.xy(3, 7));
		panel1.add(foodProcessParamsDef.getPressure_func(), CC.xy(5, 7));
		panel1.add(foodProcessParamsDef.getPressureUnit(), CC.xy(7, 7));

		panel3.add(foodProcessDef.getCapacityField(), CC.xy(3, 3));
		panel3.add(foodProcessDef.getCapacityNomBox(), CC.xy(5, 3));
		panel3.add(foodProcessDef.getCapacityDenomBox(), CC.xy(7, 3));
		
		panel3.add(foodProcessDef.getDurationField(), CC.xy(3, 5));
		panel3.add(foodProcessDef.getDurationBox(), CC.xy(5, 5));
		
		panel3.add(foodProcessDef.getStepwidthField(), CC.xy(3, 7));
		panel3.add(foodProcessDef.getStepwidthBox(), CC.xy(5, 7));

		inPortDef = foodProcessDef.getInPortDef();
		panel4.add(foodProcessDef.getExpertIn(), CC.xy(1, 3));
		for(int i = 0; i < FoodProcessNodeModel.N_PORT_IN; i++ ) {
			JLabel label = new JLabel("In-Port " + (i+1));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel4.add(label, CC.xywh(4*i+5, 1, 3, 1));
			portParameterDef = inPortDef[i].getParametersDef();
			panel4.add(portParameterDef.getVolume(), CC.xy(4*i+5, 7));
			panel4.add(portParameterDef.getVolume_func(), CC.xy(4*i+7, 7));
			panel4.add(portParameterDef.getTemperature(), CC.xy(4*i+5, 9));
			panel4.add(portParameterDef.getTemperature_func(), CC.xy(4*i+7, 9));
			panel4.add(portParameterDef.getPh(), CC.xy(4*i+5, 11));
			panel4.add(portParameterDef.getPh_func(), CC.xy(4*i+7, 11));
			panel4.add(portParameterDef.getAw(), CC.xy(4*i+5, 13));
			panel4.add(portParameterDef.getAw_func(), CC.xy(4*i+7, 13));
			panel4.add(portParameterDef.getPressure(), CC.xy(4*i+5, 15));
			panel4.add(portParameterDef.getPressure_func(), CC.xy(4*i+7, 15));
		}
		// Use first In-Port Units as Unit-Container for all In-Ports
		portParameterDef = inPortDef[0].getParametersDef();
		panel4.add(portParameterDef.getVolumeUnit(), CC.xy(3, 7));
		panel4.add(portParameterDef.getTemperatureUnit(), CC.xy(3, 9));
		panel4.add(portParameterDef.getPressureUnit(), CC.xy(3, 15));

		outPortDef = foodProcessDef.getOutPortDef();
		panel2.add(foodProcessDef.getExpertOut(), CC.xy(1, 7));
		for(int i = 0; i < FoodProcessNodeModel.N_PORT_OUT; i++ ) {
			JLabel label = new JLabel("Out-Port " + (i+1));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel2.add(label, CC.xywh(4*i+5, 1, 3, 1));
			panel2.add(outPortDef[i].getOutFlux(), CC.xy(4*i+7, 3));
			panel2.add(outPortDef[i].getNewMatrixDefinition(), CC.xywh(4*i+5, 5, 3, 1));
			/*
			for (int j=0;j < FoodProcessNodeModel.N_PORT_IN;j++) {
				panel2.add(outPortDef[i].getFromInPort()[j], CC.xywh(4*i+5, 11+2*j, 3, 1));				
			}
			*/
			portParameterDef = outPortDef[i].getParametersDef();
			panel2.add(portParameterDef.getVolume(), CC.xy(4*i+5, 11));
			panel2.add(portParameterDef.getVolume_func(), CC.xy(4*i+7, 11));
			panel2.add(portParameterDef.getTemperature(), CC.xy(4*i+5, 13));
			panel2.add(portParameterDef.getTemperature_func(), CC.xy(4*i+7, 13));
			panel2.add(portParameterDef.getPh(), CC.xy(4*i+5, 15));
			panel2.add(portParameterDef.getPh_func(), CC.xy(4*i+7, 15));
			panel2.add(portParameterDef.getAw(), CC.xy(4*i+5, 17));
			panel2.add(portParameterDef.getAw_func(), CC.xy(4*i+7, 17));
			panel2.add(portParameterDef.getPressure(), CC.xy(4*i+5, 19));
			panel2.add(portParameterDef.getPressure_func(), CC.xy(4*i+7, 19));
		}
		// Use first Out-Port Units as Unit-Container for all Out-Ports
		portParameterDef = outPortDef[0].getParametersDef();
		panel2.add(portParameterDef.getVolumeUnit(), CC.xy(3, 11));
		panel2.add(portParameterDef.getTemperatureUnit(), CC.xy(3, 13));
		panel2.add(portParameterDef.getPressureUnit(), CC.xy(3, 19));
		
		agentsDef = foodProcessDef.getAgentsDef();
		panel6.add(agentsDef.getRecipeGuess(), CC.xy(1, 1));
		panel6.add(agentsDef.getManualDef(), CC.xy(1, 3));
		agentsDef.getRecipeGuess().setSelected(true);
		scrollPane1.setViewportView(agentsDef.getAgentsDef());
		panel6.add(scrollPane1, CC.xy(1, 5));
	}
	private void initComponentsCopy() {
		panel3 = new JPanel();
		label1 = new JLabel();
		panel1 = new JPanel();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		tabbedPane1 = new JTabbedPane();
		panel4 = new JPanel();
		label99 = new JLabel();
		label100 = new JLabel();
		label101 = new JLabel();
		label102 = new JLabel();
		label103 = new JLabel();
		label104 = new JLabel();
		panel2 = new JPanel();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		label16 = new JLabel();
		label17 = new JLabel();
		label18 = new JLabel();
		label19 = new JLabel();
		label93 = new JLabel();
		label94 = new JLabel();
		label95 = new JLabel();
		label96 = new JLabel();
		label97 = new JLabel();
		label98 = new JLabel();
		panel6 = new JPanel();
		button5 = new JButton();
		scrollPane1 = new JScrollPane();
		label9 = new JLabel();
		label10 = new JLabel();
		label11 = new JLabel();
		label12 = new JLabel();

		//======== this ========
		setMinimumSize(new Dimension(671, 430));
		setPreferredSize(new Dimension(674, 440));
		setLayout(new FormLayout(
			"default, $lcgap, default:grow",
			"default, $lgap, 203dlu:grow"));

		//======== panel3 ========
		{
			panel3.setBorder(new TitledBorder("Process Properties"));
			/*
			panel3.setLayout(new FormLayout(
				"4*(default, $lcgap), default:grow",
				"4*(default, $lgap), default:grow, $lgap"));

			panel3.setLayout(new FormLayout(
					"4*(default, $lcgap), default:grow",
					"default:grow, 3*($lgap, default), $lgap, default:grow"));
*/
			panel3.setLayout(new FormLayout(
				"3*(default, $lcgap), default",
				"default:grow, 3*($lgap, default), $lgap, default:grow"));

//---- label1 ----
			label1.setText("Process Name");
			panel3.add(label1, CC.xy(1, 1));

			//---- label2 ----
			label2.setText("Capacity");
			panel3.add(label2, CC.xy(1, 3));

			//---- label3 ----
			label3.setText("Duration");
			panel3.add(label3, CC.xy(1, 5));

			//---- label4 ----
			label4.setText("Step Width");
			panel3.add(label4, CC.xy(1, 7));
		}
		//add(panel3, CC.xywh(1, 1, 3, 1));
		add(panel3, CC.xy(1, 1));

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder("Process Parameters"));
			/*
			panel1.setLayout(new FormLayout(
					"3*(default, $lcgap), default",
					"3*(default, $lgap), default"));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7}});

			panel1.setLayout(new FormLayout(
					"3*(default, $lcgap), default",
					"4*(default, $lgap), default:grow"));
				((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7}});
				*/
				panel1.setLayout(new FormLayout(
						"default, $lcgap, default:grow, 2*($lcgap, default)",
						"4*(default, $lgap), default:grow"));
					((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7}});

				//---- label5 ----
			label5.setText("Temperature");
			panel1.add(label5, CC.xy(1, 1));


			//---- label6 ----
			label6.setText("pH");
			panel1.add(label6, CC.xy(1, 3));

			//---- label7 ----
			label7.setText("aw");
			panel1.add(label7, CC.xy(1, 5));

			//---- label8 ----
			label8.setText("Pressure");
			panel1.add(label8, CC.xy(1, 7));

		}
		add(panel1, CC.xy(3, 1));
		//panel3.add(panel1, CC.xywh(9, 1, 1, 10));

		//======== tabbedPane1 ========
		{
			tabbedPane1.setPreferredSize(new Dimension(469, 380));

			//======== panel4 ========
			{
				panel4.setBorder(Borders.TABBED_DIALOG_BORDER);
				/*
				panel4.setLayout(new FormLayout(
					"9*(default, $lcgap), default",
					"3*(default, $lgap), fill:default, 4*($lgap, default)"));
				((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{3, 7, 11, 15, 19}, {5, 9, 13, 17}});
				
				panel4.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"3*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default:grow"));
					((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}, {7, 11, 15, 19}});
				 */
				panel4.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"3*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default:grow"));
					((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

				//---- label99 ----
				label99.setText("Parameters");
				label99.setFont(new Font("Segoe UI", Font.BOLD, 12));
				panel4.add(label99, CC.xy(1, 5));

				//---- label100 ----
				label100.setText("Volume");
				panel4.add(label100, CC.xy(1, 7));

				//---- label101 ----
				label101.setText("Temperature");
				panel4.add(label101, CC.xy(1, 9));

				//---- label102 ----
				label102.setText("pH");
				panel4.add(label102, CC.xy(1, 11));

				//---- label103 ----
				label103.setText("aw");
				panel4.add(label103, CC.xy(1, 13));

				//---- label104 ----
				label104.setText("Pressure");
				panel4.add(label104, CC.xy(1, 15));
			}
			tabbedPane1.addTab("In Ports", panel4);


			//======== panel2 ========
			{
				panel2.setBorder(Borders.TABBED_DIALOG_BORDER);
				/*
				panel2.setLayout(new FormLayout(
					"9*(default, $lcgap), default",
					"10*(default, $lgap), fill:default, 4*($lgap, default), $lgap, default:grow"));
				((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{3, 7, 11, 15}, {5, 9, 13, 17}});
				 
				panel2.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"5*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default:grow"));
					((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}, {7, 11, 15, 19}});
					
				panel2.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"5*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default:grow"));
					((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});
					*/
				panel2.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"5*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default, $lgap, default:grow"));
					((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});
				
				//---- label17 ----
				label17.setText("Out Flux");
				panel2.add(label17, CC.xy(1, 3));

				//---- label18 ----
				label18.setText("%");
				label18.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label18, CC.xy(3, 3));

				//---- label19 ----
				label19.setText("New Matrix Definition");
				panel2.add(label19, CC.xy(1, 5));

/*
				//---- label29 ----
				label29.setText("Recipe");
				label29.setFont(new Font("Segoe UI", Font.BOLD, 12));
				panel2.add(label29, CC.xy(1, 9));

				//---- label20 ----
				label20.setText("From In-Port 1");
				panel2.add(label20, CC.xy(1, 11));

				//---- label24 ----
				label24.setText("%");
				label24.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label24, CC.xy(3, 11));

				//---- label21 ----
				label21.setText("From In-Port 2");
				panel2.add(label21, CC.xy(1, 13));

				//---- label25 ----
				label25.setText("%");
				label25.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label25, CC.xy(3, 13));

				//---- label22 ----
				label22.setText("From In-Port 3");
				panel2.add(label22, CC.xy(1, 15));

				//---- label26 ----
				label26.setText("%");
				label26.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label26, CC.xy(3, 15));

				//---- label23 ----
				label23.setText("From In-Port 4");
				panel2.add(label23, CC.xy(1, 17));

				//---- label27 ----
				label27.setText("%");
				label27.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label27, CC.xy(3, 17));
*/
				//---- label93 ----
				label93.setText("Parameters");
				label93.setFont(new Font("Segoe UI", Font.BOLD, 12));
				panel2.add(label93, CC.xy(1, 9));

				//---- label94 ----
				label94.setText("Volume");
				panel2.add(label94, CC.xy(1, 11));

				//---- label95 ----
				label95.setText("Temperature");
				panel2.add(label95, CC.xy(1, 13));

				//---- label96 ----
				label96.setText("pH");
				panel2.add(label96, CC.xy(1, 15));

				//---- label97 ----
				label97.setText("aw");
				panel2.add(label97, CC.xy(1, 17));

				//---- label98 ----
				label98.setText("Pressure");
				panel2.add(label98, CC.xy(1, 19));

				//---- button5 ----
				button5.setText("Recipe");
				panel2.add(button5, CC.xy(1, 21));
			}
			tabbedPane1.addTab("Out Ports", panel2);


			//======== panel6 ========
			{
				panel6.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel6.setPreferredSize(new Dimension(464, 220));
				panel6.setLayout(new FormLayout(
						"default:grow",
						"2*(default, $lgap), default:grow, $lgap, fill:default:grow"));

			}
			tabbedPane1.addTab("Agents", panel6);

		}
		add(tabbedPane1, CC.xywh(1, 3, 3, 1));

	
		//---- label9 ----
		label9.setText("Expert mode allows for special settings, example see documentation");
		label9.setForeground(Color.gray);
		panel2.add(label9, CC.xywh(5, 7, 15, 1));
		//---- label10 ----
		label10.setText("Recipe allows for special settings for the matrices, example see documentation");
		label10.setForeground(Color.gray);
		panel2.add(label10, CC.xywh(5, 21, 15, 1));
		//---- label11 ----
		label11.setText("Agents are given by weight relative to the conentration, example see documentation");
		label11.setForeground(Color.gray);
		panel6.add(label11, CC.xy(1, 7));
		//---- label12 ----
		label12.setText("Expert mode allows for special settings, example see documentation");
		label12.setForeground(Color.gray);
		panel4.add(label12, CC.xywh(5, 3, 15, 1));
	}

	private void checkBox1ActionPerformed(final ActionEvent e) {
	}

	private void checkBox1ItemStateChanged(final ItemEvent e) {
	}

	private void radioButton1StateChanged(ChangeEvent e) {
		// TODO add your code here
	}

	private void button5ActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	@SuppressWarnings("unused")
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel3 = new JPanel();
		label1 = new JLabel();
		comboBox1 = new JComboBox<String>();
		label2 = new JLabel();
		formattedTextField1 = new JFormattedTextField();
		comboBox2 = new JComboBox<String>();
		comboBox3 = new JComboBox<String>();
		label3 = new JLabel();
		formattedTextField2 = new JFormattedTextField();
		comboBox4 = new JComboBox<String>();
		label4 = new JLabel();
		formattedTextField3 = new JFormattedTextField();
		comboBox5 = new JComboBox<String>();
		panel1 = new JPanel();
		label5 = new JLabel();
		formattedTextField4 = new JFormattedTextField();
		button1 = new JButton();
		comboBox6 = new JComboBox<String>();
		label6 = new JLabel();
		formattedTextField5 = new JFormattedTextField();
		button2 = new JButton();
		label7 = new JLabel();
		formattedTextField6 = new JFormattedTextField();
		button3 = new JButton();
		label8 = new JLabel();
		formattedTextField7 = new JFormattedTextField();
		button4 = new JButton();
		comboBox9 = new JComboBox<String>();
		tabbedPane1 = new JTabbedPane();
		panel4 = new JPanel();
		label38 = new JLabel();
		label40 = new JLabel();
		label42 = new JLabel();
		label44 = new JLabel();
		checkBox3 = new JCheckBox();
		label12 = new JLabel();
		label99 = new JLabel();
		label100 = new JLabel();
		comboBox39 = new JComboBox<String>();
		formattedTextField134 = new JFormattedTextField();
		button15 = new JButton();
		formattedTextField135 = new JFormattedTextField();
		button16 = new JButton();
		formattedTextField136 = new JFormattedTextField();
		button17 = new JButton();
		formattedTextField137 = new JFormattedTextField();
		button18 = new JButton();
		label101 = new JLabel();
		comboBox40 = new JComboBox<String>();
		formattedTextField138 = new JFormattedTextField();
		button19 = new JButton();
		formattedTextField139 = new JFormattedTextField();
		button60 = new JButton();
		formattedTextField140 = new JFormattedTextField();
		button61 = new JButton();
		formattedTextField141 = new JFormattedTextField();
		button62 = new JButton();
		label102 = new JLabel();
		formattedTextField142 = new JFormattedTextField();
		button63 = new JButton();
		formattedTextField143 = new JFormattedTextField();
		button64 = new JButton();
		formattedTextField144 = new JFormattedTextField();
		button65 = new JButton();
		formattedTextField145 = new JFormattedTextField();
		button66 = new JButton();
		label103 = new JLabel();
		formattedTextField146 = new JFormattedTextField();
		button67 = new JButton();
		formattedTextField147 = new JFormattedTextField();
		button68 = new JButton();
		formattedTextField148 = new JFormattedTextField();
		button69 = new JButton();
		formattedTextField149 = new JFormattedTextField();
		button70 = new JButton();
		label104 = new JLabel();
		comboBox41 = new JComboBox<String>();
		formattedTextField150 = new JFormattedTextField();
		button71 = new JButton();
		formattedTextField151 = new JFormattedTextField();
		button72 = new JButton();
		formattedTextField152 = new JFormattedTextField();
		button73 = new JButton();
		formattedTextField153 = new JFormattedTextField();
		button74 = new JButton();
		panel2 = new JPanel();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		label16 = new JLabel();
		label17 = new JLabel();
		label18 = new JLabel();
		formattedTextField74 = new JFormattedTextField();
		formattedTextField75 = new JFormattedTextField();
		formattedTextField76 = new JFormattedTextField();
		formattedTextField77 = new JFormattedTextField();
		label19 = new JLabel();
		comboBox8 = new JComboBox();
		comboBox11 = new JComboBox();
		comboBox12 = new JComboBox();
		comboBox14 = new JComboBox();
		checkBox1 = new JCheckBox();
		label9 = new JLabel();
		label93 = new JLabel();
		label94 = new JLabel();
		comboBox36 = new JComboBox<String>();
		formattedTextField94 = new JFormattedTextField();
		button9 = new JButton();
		formattedTextField95 = new JFormattedTextField();
		button10 = new JButton();
		formattedTextField96 = new JFormattedTextField();
		button11 = new JButton();
		formattedTextField97 = new JFormattedTextField();
		button12 = new JButton();
		label95 = new JLabel();
		comboBox37 = new JComboBox<String>();
		formattedTextField98 = new JFormattedTextField();
		button14 = new JButton();
		formattedTextField102 = new JFormattedTextField();
		button48 = new JButton();
		formattedTextField106 = new JFormattedTextField();
		button52 = new JButton();
		formattedTextField110 = new JFormattedTextField();
		button56 = new JButton();
		label96 = new JLabel();
		formattedTextField99 = new JFormattedTextField();
		button45 = new JButton();
		formattedTextField103 = new JFormattedTextField();
		button49 = new JButton();
		formattedTextField107 = new JFormattedTextField();
		button53 = new JButton();
		formattedTextField111 = new JFormattedTextField();
		button57 = new JButton();
		label97 = new JLabel();
		formattedTextField100 = new JFormattedTextField();
		button46 = new JButton();
		formattedTextField104 = new JFormattedTextField();
		button50 = new JButton();
		formattedTextField108 = new JFormattedTextField();
		button54 = new JButton();
		formattedTextField112 = new JFormattedTextField();
		button58 = new JButton();
		label98 = new JLabel();
		comboBox38 = new JComboBox<String>();
		formattedTextField101 = new JFormattedTextField();
		button47 = new JButton();
		formattedTextField105 = new JFormattedTextField();
		button51 = new JButton();
		formattedTextField109 = new JFormattedTextField();
		button55 = new JButton();
		formattedTextField113 = new JFormattedTextField();
		button59 = new JButton();
		button5 = new JButton();
		label10 = new JLabel();
		panel6 = new JPanel();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		label11 = new JLabel();

		//======== this ========
		setMinimumSize(new Dimension(671, 430));
		setPreferredSize(new Dimension(674, 440));
		setLayout(new FormLayout(
			"default, $lcgap, default:grow",
			"default, $lgap, fill:203dlu:grow"));

		//======== panel3 ========
		{
			panel3.setBorder(new TitledBorder("Process Properties"));
			panel3.setLayout(new FormLayout(
				"3*(default, $lcgap), default",
				"default:grow, 3*($lgap, default), $lgap, default:grow"));

			//---- label1 ----
			label1.setText("Process Name");
			panel3.add(label1, CC.xy(1, 1));

			//---- comboBox1 ----
			comboBox1.setEditable(true);
			panel3.add(comboBox1, CC.xywh(3, 1, 5, 1));

			//---- label2 ----
			label2.setText("Capacity");
			panel3.add(label2, CC.xy(1, 3));

			//---- formattedTextField1 ----
			formattedTextField1.setColumns(5);
			panel3.add(formattedTextField1, CC.xy(3, 3));

			//---- comboBox2 ----
			comboBox2.setModel(new DefaultComboBoxModel<String>(new String[] {
				"l",
				"kg"
			}));
			panel3.add(comboBox2, CC.xy(5, 3));

			//---- comboBox3 ----
			comboBox3.setModel(new DefaultComboBoxModel<String>(new String[] {
				" ",
				"s",
				"min",
				"h",
				"d",
				"m",
				"y"
			}));
			panel3.add(comboBox3, CC.xy(7, 3));

			//---- label3 ----
			label3.setText("Duration");
			panel3.add(label3, CC.xy(1, 5));
			panel3.add(formattedTextField2, CC.xy(3, 5));

			//---- comboBox4 ----
			comboBox4.setModel(new DefaultComboBoxModel<String>(new String[] {
				"s",
				"min",
				"h",
				"d",
				"m",
				"y"
			}));
			panel3.add(comboBox4, CC.xy(5, 5));

			//---- label4 ----
			label4.setText("Step Width");
			panel3.add(label4, CC.xy(1, 7));
			panel3.add(formattedTextField3, CC.xy(3, 7));

			//---- comboBox5 ----
			comboBox5.setModel(new DefaultComboBoxModel<String>(new String[] {
				"s",
				"min",
				"h",
				"d",
				"m",
				"y"
			}));
			panel3.add(comboBox5, CC.xy(5, 7));
		}
		add(panel3, CC.xy(1, 1));

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder("Process Parameters"));
			panel1.setLayout(new FormLayout(
				"default, $lcgap, default:grow, 2*($lcgap, default)",
				"4*(default, $lgap), default:grow"));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7}});

			//---- label5 ----
			label5.setText("Temperature");
			panel1.add(label5, CC.xy(1, 1));

			//---- formattedTextField4 ----
			formattedTextField4.setColumns(10);
			panel1.add(formattedTextField4, CC.xy(3, 1));

			//---- button1 ----
			button1.setText("...");
			panel1.add(button1, CC.xy(5, 1));

			//---- comboBox6 ----
			comboBox6.setModel(new DefaultComboBoxModel<String>(new String[] {
				"\u00b0C",
				"\u00b0F",
				"K"
			}));
			panel1.add(comboBox6, CC.xy(7, 1));

			//---- label6 ----
			label6.setText("pH");
			panel1.add(label6, CC.xy(1, 3));

			//---- formattedTextField5 ----
			formattedTextField5.setColumns(10);
			panel1.add(formattedTextField5, CC.xy(3, 3));

			//---- button2 ----
			button2.setText("...");
			panel1.add(button2, CC.xy(5, 3));

			//---- label7 ----
			label7.setText("aw");
			panel1.add(label7, CC.xy(1, 5));

			//---- formattedTextField6 ----
			formattedTextField6.setColumns(10);
			panel1.add(formattedTextField6, CC.xy(3, 5));

			//---- button3 ----
			button3.setText("...");
			panel1.add(button3, CC.xy(5, 5));

			//---- label8 ----
			label8.setText("Pressure");
			panel1.add(label8, CC.xy(1, 7));

			//---- formattedTextField7 ----
			formattedTextField7.setColumns(10);
			panel1.add(formattedTextField7, CC.xy(3, 7));

			//---- button4 ----
			button4.setText("...");
			panel1.add(button4, CC.xy(5, 7));

			//---- comboBox9 ----
			comboBox9.setModel(new DefaultComboBoxModel<String>(new String[] {
				"bar",
				"Pa"
			}));
			panel1.add(comboBox9, CC.xy(7, 7));
		}
		add(panel1, CC.xy(3, 1));

		//======== tabbedPane1 ========
		{
			tabbedPane1.setPreferredSize(new Dimension(469, 380));

			//======== panel4 ========
			{
				panel4.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel4.setLayout(new FormLayout(
					"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
					"3*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default:grow"));
				((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

				//---- label38 ----
				label38.setText("In-Port 1");
				label38.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label38, CC.xywh(5, 1, 3, 1));

				//---- label40 ----
				label40.setText("In-Port 2");
				label40.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label40, CC.xywh(9, 1, 3, 1));

				//---- label42 ----
				label42.setText("In-Port 3");
				label42.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label42, CC.xywh(13, 1, 3, 1));

				//---- label44 ----
				label44.setText("In-Port 4");
				label44.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label44, CC.xywh(17, 1, 3, 1));

				//---- checkBox3 ----
				checkBox3.setText("Advanced");
				panel4.add(checkBox3, CC.xy(1, 3));

				//---- label12 ----
				label12.setText("Expert mode allows for special settings, example see documentation");
				label12.setForeground(Color.gray);
				panel4.add(label12, CC.xywh(5, 3, 15, 1));

				//---- label99 ----
				label99.setText("Parameters");
				label99.setFont(new Font("Segoe UI", Font.BOLD, 12));
				panel4.add(label99, CC.xy(1, 5));

				//---- label100 ----
				label100.setText("Volume");
				panel4.add(label100, CC.xy(1, 7));

				//---- comboBox39 ----
				comboBox39.setModel(new DefaultComboBoxModel<String>(new String[] {
					"l",
					"kg"
				}));
				panel4.add(comboBox39, CC.xy(3, 7));
				panel4.add(formattedTextField134, CC.xy(5, 7));

				//---- button15 ----
				button15.setText("...");
				panel4.add(button15, CC.xy(7, 7));
				panel4.add(formattedTextField135, CC.xy(9, 7));

				//---- button16 ----
				button16.setText("...");
				panel4.add(button16, CC.xy(11, 7));
				panel4.add(formattedTextField136, CC.xy(13, 7));

				//---- button17 ----
				button17.setText("...");
				panel4.add(button17, CC.xy(15, 7));
				panel4.add(formattedTextField137, CC.xy(17, 7));

				//---- button18 ----
				button18.setText("...");
				panel4.add(button18, CC.xy(19, 7));

				//---- label101 ----
				label101.setText("Temperature");
				panel4.add(label101, CC.xy(1, 9));

				//---- comboBox40 ----
				comboBox40.setModel(new DefaultComboBoxModel<String>(new String[] {
					"\u00b0C",
					"\u00b0F",
					"K"
				}));
				panel4.add(comboBox40, CC.xy(3, 9));
				panel4.add(formattedTextField138, CC.xy(5, 9));

				//---- button19 ----
				button19.setText("...");
				panel4.add(button19, CC.xy(7, 9));
				panel4.add(formattedTextField139, CC.xy(9, 9));

				//---- button60 ----
				button60.setText("...");
				panel4.add(button60, CC.xy(11, 9));
				panel4.add(formattedTextField140, CC.xy(13, 9));

				//---- button61 ----
				button61.setText("...");
				panel4.add(button61, CC.xy(15, 9));
				panel4.add(formattedTextField141, CC.xy(17, 9));

				//---- button62 ----
				button62.setText("...");
				panel4.add(button62, CC.xy(19, 9));

				//---- label102 ----
				label102.setText("pH");
				panel4.add(label102, CC.xy(1, 11));
				panel4.add(formattedTextField142, CC.xy(5, 11));

				//---- button63 ----
				button63.setText("...");
				panel4.add(button63, CC.xy(7, 11));
				panel4.add(formattedTextField143, CC.xy(9, 11));

				//---- button64 ----
				button64.setText("...");
				panel4.add(button64, CC.xy(11, 11));
				panel4.add(formattedTextField144, CC.xy(13, 11));

				//---- button65 ----
				button65.setText("...");
				panel4.add(button65, CC.xy(15, 11));
				panel4.add(formattedTextField145, CC.xy(17, 11));

				//---- button66 ----
				button66.setText("...");
				panel4.add(button66, CC.xy(19, 11));

				//---- label103 ----
				label103.setText("aw");
				panel4.add(label103, CC.xy(1, 13));
				panel4.add(formattedTextField146, CC.xy(5, 13));

				//---- button67 ----
				button67.setText("...");
				panel4.add(button67, CC.xy(7, 13));
				panel4.add(formattedTextField147, CC.xy(9, 13));

				//---- button68 ----
				button68.setText("...");
				panel4.add(button68, CC.xy(11, 13));
				panel4.add(formattedTextField148, CC.xy(13, 13));

				//---- button69 ----
				button69.setText("...");
				panel4.add(button69, CC.xy(15, 13));
				panel4.add(formattedTextField149, CC.xy(17, 13));

				//---- button70 ----
				button70.setText("...");
				panel4.add(button70, CC.xy(19, 13));

				//---- label104 ----
				label104.setText("Pressure");
				panel4.add(label104, CC.xy(1, 15));

				//---- comboBox41 ----
				comboBox41.setModel(new DefaultComboBoxModel<String>(new String[] {
					"bar",
					"Pa"
				}));
				panel4.add(comboBox41, CC.xy(3, 15));
				panel4.add(formattedTextField150, CC.xy(5, 15));

				//---- button71 ----
				button71.setText("...");
				panel4.add(button71, CC.xy(7, 15));
				panel4.add(formattedTextField151, CC.xy(9, 15));

				//---- button72 ----
				button72.setText("...");
				panel4.add(button72, CC.xy(11, 15));
				panel4.add(formattedTextField152, CC.xy(13, 15));

				//---- button73 ----
				button73.setText("...");
				panel4.add(button73, CC.xy(15, 15));
				panel4.add(formattedTextField153, CC.xy(17, 15));

				//---- button74 ----
				button74.setText("...");
				panel4.add(button74, CC.xy(19, 15));
			}
			tabbedPane1.addTab("In Ports", panel4);


			//======== panel2 ========
			{
				panel2.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel2.setLayout(new FormLayout(
					"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
					"5*(default, $lgap), fill:default, 4*($lgap, default), $lgap, fill:default, $lgap, default:grow"));
				((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

				//---- label13 ----
				label13.setText("Out-Port 1");
				label13.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label13, CC.xywh(5, 1, 3, 1));

				//---- label14 ----
				label14.setText("Out-Port 2");
				label14.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label14, CC.xywh(9, 1, 3, 1));

				//---- label15 ----
				label15.setText("Out-Port 3");
				label15.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label15, CC.xywh(13, 1, 3, 1));

				//---- label16 ----
				label16.setText("Out-Port 4");
				label16.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label16, CC.xywh(17, 1, 3, 1));

				//---- label17 ----
				label17.setText("Out Flux");
				panel2.add(label17, CC.xy(1, 3));

				//---- label18 ----
				label18.setText("%");
				label18.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label18, CC.xy(3, 3));
				panel2.add(formattedTextField74, CC.xy(7, 3));
				panel2.add(formattedTextField75, CC.xy(11, 3));
				panel2.add(formattedTextField76, CC.xy(15, 3));
				panel2.add(formattedTextField77, CC.xy(19, 3));

				//---- label19 ----
				label19.setText("New Matrix Definition");
				panel2.add(label19, CC.xy(1, 5));

				//---- comboBox8 ----
				comboBox8.setEditable(true);
				panel2.add(comboBox8, CC.xywh(5, 5, 3, 1));

				//---- comboBox11 ----
				comboBox11.setEditable(true);
				panel2.add(comboBox11, CC.xywh(9, 5, 3, 1));

				//---- comboBox12 ----
				comboBox12.setEditable(true);
				panel2.add(comboBox12, CC.xywh(13, 5, 3, 1));

				//---- comboBox14 ----
				comboBox14.setEditable(true);
				panel2.add(comboBox14, CC.xywh(17, 5, 3, 1));

				//---- checkBox1 ----
				checkBox1.setText("Advanced");
				checkBox1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						checkBox1ActionPerformed(e);
					}
				});
				checkBox1.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						checkBox1ItemStateChanged(e);
					}
				});
				panel2.add(checkBox1, CC.xywh(1, 7, 2, 1));

				//---- label9 ----
				label9.setText("Expert mode allows for special settings, example see documentation");
				label9.setForeground(Color.gray);
				panel2.add(label9, CC.xywh(5, 7, 15, 1));

				//---- label93 ----
				label93.setText("Parameters");
				label93.setFont(new Font("Segoe UI", Font.BOLD, 12));
				panel2.add(label93, CC.xy(1, 9));

				//---- label94 ----
				label94.setText("Volume");
				panel2.add(label94, CC.xy(1, 11));

				//---- comboBox36 ----
				comboBox36.setModel(new DefaultComboBoxModel<String>(new String[] {
					"l",
					"kg"
				}));
				panel2.add(comboBox36, CC.xy(3, 11));
				panel2.add(formattedTextField94, CC.xy(5, 11));

				//---- button9 ----
				button9.setText("...");
				panel2.add(button9, CC.xy(7, 11));
				panel2.add(formattedTextField95, CC.xy(9, 11));

				//---- button10 ----
				button10.setText("...");
				panel2.add(button10, CC.xy(11, 11));
				panel2.add(formattedTextField96, CC.xy(13, 11));

				//---- button11 ----
				button11.setText("...");
				panel2.add(button11, CC.xy(15, 11));
				panel2.add(formattedTextField97, CC.xy(17, 11));

				//---- button12 ----
				button12.setText("...");
				panel2.add(button12, CC.xy(19, 11));

				//---- label95 ----
				label95.setText("Temperature");
				panel2.add(label95, CC.xy(1, 13));

				//---- comboBox37 ----
				comboBox37.setModel(new DefaultComboBoxModel<String>(new String[] {
					"\u00b0C",
					"\u00b0F",
					"K"
				}));
				panel2.add(comboBox37, CC.xy(3, 13));
				panel2.add(formattedTextField98, CC.xy(5, 13));

				//---- button14 ----
				button14.setText("...");
				panel2.add(button14, CC.xy(7, 13));
				panel2.add(formattedTextField102, CC.xy(9, 13));

				//---- button48 ----
				button48.setText("...");
				panel2.add(button48, CC.xy(11, 13));
				panel2.add(formattedTextField106, CC.xy(13, 13));

				//---- button52 ----
				button52.setText("...");
				panel2.add(button52, CC.xy(15, 13));
				panel2.add(formattedTextField110, CC.xy(17, 13));

				//---- button56 ----
				button56.setText("...");
				panel2.add(button56, CC.xy(19, 13));

				//---- label96 ----
				label96.setText("pH");
				panel2.add(label96, CC.xy(1, 15));
				panel2.add(formattedTextField99, CC.xy(5, 15));

				//---- button45 ----
				button45.setText("...");
				panel2.add(button45, CC.xy(7, 15));
				panel2.add(formattedTextField103, CC.xy(9, 15));

				//---- button49 ----
				button49.setText("...");
				panel2.add(button49, CC.xy(11, 15));
				panel2.add(formattedTextField107, CC.xy(13, 15));

				//---- button53 ----
				button53.setText("...");
				panel2.add(button53, CC.xy(15, 15));
				panel2.add(formattedTextField111, CC.xy(17, 15));

				//---- button57 ----
				button57.setText("...");
				panel2.add(button57, CC.xy(19, 15));

				//---- label97 ----
				label97.setText("aw");
				panel2.add(label97, CC.xy(1, 17));
				panel2.add(formattedTextField100, CC.xy(5, 17));

				//---- button46 ----
				button46.setText("...");
				panel2.add(button46, CC.xy(7, 17));
				panel2.add(formattedTextField104, CC.xy(9, 17));

				//---- button50 ----
				button50.setText("...");
				panel2.add(button50, CC.xy(11, 17));
				panel2.add(formattedTextField108, CC.xy(13, 17));

				//---- button54 ----
				button54.setText("...");
				panel2.add(button54, CC.xy(15, 17));
				panel2.add(formattedTextField112, CC.xy(17, 17));

				//---- button58 ----
				button58.setText("...");
				panel2.add(button58, CC.xy(19, 17));

				//---- label98 ----
				label98.setText("Pressure");
				panel2.add(label98, CC.xy(1, 19));

				//---- comboBox38 ----
				comboBox38.setModel(new DefaultComboBoxModel<String>(new String[] {
					"bar",
					"Pa"
				}));
				panel2.add(comboBox38, CC.xy(3, 19));
				panel2.add(formattedTextField101, CC.xy(5, 19));

				//---- button47 ----
				button47.setText("...");
				panel2.add(button47, CC.xy(7, 19));
				panel2.add(formattedTextField105, CC.xy(9, 19));

				//---- button51 ----
				button51.setText("...");
				panel2.add(button51, CC.xy(11, 19));
				panel2.add(formattedTextField109, CC.xy(13, 19));

				//---- button55 ----
				button55.setText("...");
				panel2.add(button55, CC.xy(15, 19));
				panel2.add(formattedTextField113, CC.xy(17, 19));

				//---- button59 ----
				button59.setText("...");
				panel2.add(button59, CC.xy(19, 19));

				//---- button5 ----
				button5.setText("Recipe");
				button5.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button5ActionPerformed(e);
					}
				});
				panel2.add(button5, CC.xy(1, 21));

				//---- label10 ----
				label10.setText("Recipe allows for special settings for the matrices, example see documentation");
				label10.setForeground(Color.gray);
				panel2.add(label10, CC.xywh(5, 21, 15, 1));
			}
			tabbedPane1.addTab("Out Ports", panel2);


			//======== panel6 ========
			{
				panel6.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel6.setPreferredSize(new Dimension(464, 220));
				panel6.setLayout(new FormLayout(
					"default:grow",
					"2*(default, $lgap), default:grow, $lgap, fill:default:grow"));

				//---- radioButton1 ----
				radioButton1.setText("Guess from Recipe");
				radioButton1.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						radioButton1StateChanged(e);
					}
				});
				panel6.add(radioButton1, CC.xy(1, 1));

				//---- radioButton2 ----
				radioButton2.setText("Define manually");
				panel6.add(radioButton2, CC.xy(1, 3));

				//======== scrollPane1 ========
				{

					//---- table1 ----
					table1.setModel(new DefaultTableModel(
						new Object[][] {
							{"Agent 1", null, null, null, null},
						},
						new String[] {
							"Agent Name", "Out Port 1", "Out Port 2", "Out Port 3", "Out Port 4"
						}
					) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 3886272447206890942L;
						Class<?>[] columnTypes = new Class<?>[] {
							String.class, Double.class, Double.class, Double.class, Double.class
						};
						boolean[] columnEditable = new boolean[] {
							false, true, true, true, true
						};
						@Override
						public Class<?> getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
						@Override
						public boolean isCellEditable(int rowIndex, int columnIndex) {
							return columnEditable[columnIndex];
						}
					});
					scrollPane1.setViewportView(table1);
				}
				panel6.add(scrollPane1, CC.xy(1, 5));

				//---- label11 ----
				label11.setText("Agents are given by weight relative to the conentration, example see documentation");
				label11.setForeground(Color.gray);
				panel6.add(label11, CC.xy(1, 7));
			}
			tabbedPane1.addTab("Agents", panel6);

		}
		add(tabbedPane1, CC.xywh(1, 3, 3, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButton1);
		buttonGroup1.add(radioButton2);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel3;
	private JLabel label1;
	private JComboBox<String> comboBox1;
	private JLabel label2;
	private JFormattedTextField formattedTextField1;
	private JComboBox<String> comboBox2;
	private JComboBox<String> comboBox3;
	private JLabel label3;
	private JFormattedTextField formattedTextField2;
	private JComboBox<String> comboBox4;
	private JLabel label4;
	private JFormattedTextField formattedTextField3;
	private JComboBox<String> comboBox5;
	private JPanel panel1;
	private JLabel label5;
	private JFormattedTextField formattedTextField4;
	private JButton button1;
	private JComboBox<String> comboBox6;
	private JLabel label6;
	private JFormattedTextField formattedTextField5;
	private JButton button2;
	private JLabel label7;
	private JFormattedTextField formattedTextField6;
	private JButton button3;
	private JLabel label8;
	private JFormattedTextField formattedTextField7;
	private JButton button4;
	private JComboBox<String> comboBox9;
	private JTabbedPane tabbedPane1;
	private JPanel panel4;
	private JLabel label38;
	private JLabel label40;
	private JLabel label42;
	private JLabel label44;
	private JCheckBox checkBox3;
	private JLabel label12;
	private JLabel label99;
	private JLabel label100;
	private JComboBox<String> comboBox39;
	private JFormattedTextField formattedTextField134;
	private JButton button15;
	private JFormattedTextField formattedTextField135;
	private JButton button16;
	private JFormattedTextField formattedTextField136;
	private JButton button17;
	private JFormattedTextField formattedTextField137;
	private JButton button18;
	private JLabel label101;
	private JComboBox<String> comboBox40;
	private JFormattedTextField formattedTextField138;
	private JButton button19;
	private JFormattedTextField formattedTextField139;
	private JButton button60;
	private JFormattedTextField formattedTextField140;
	private JButton button61;
	private JFormattedTextField formattedTextField141;
	private JButton button62;
	private JLabel label102;
	private JFormattedTextField formattedTextField142;
	private JButton button63;
	private JFormattedTextField formattedTextField143;
	private JButton button64;
	private JFormattedTextField formattedTextField144;
	private JButton button65;
	private JFormattedTextField formattedTextField145;
	private JButton button66;
	private JLabel label103;
	private JFormattedTextField formattedTextField146;
	private JButton button67;
	private JFormattedTextField formattedTextField147;
	private JButton button68;
	private JFormattedTextField formattedTextField148;
	private JButton button69;
	private JFormattedTextField formattedTextField149;
	private JButton button70;
	private JLabel label104;
	private JComboBox<String> comboBox41;
	private JFormattedTextField formattedTextField150;
	private JButton button71;
	private JFormattedTextField formattedTextField151;
	private JButton button72;
	private JFormattedTextField formattedTextField152;
	private JButton button73;
	private JFormattedTextField formattedTextField153;
	private JButton button74;
	private JPanel panel2;
	private JLabel label13;
	private JLabel label14;
	private JLabel label15;
	private JLabel label16;
	private JLabel label17;
	private JLabel label18;
	private JFormattedTextField formattedTextField74;
	private JFormattedTextField formattedTextField75;
	private JFormattedTextField formattedTextField76;
	private JFormattedTextField formattedTextField77;
	private JLabel label19;
	private JComboBox comboBox8;
	private JComboBox comboBox11;
	private JComboBox comboBox12;
	private JComboBox comboBox14;
	private JCheckBox checkBox1;
	private JLabel label9;
	private JLabel label93;
	private JLabel label94;
	private JComboBox<String> comboBox36;
	private JFormattedTextField formattedTextField94;
	private JButton button9;
	private JFormattedTextField formattedTextField95;
	private JButton button10;
	private JFormattedTextField formattedTextField96;
	private JButton button11;
	private JFormattedTextField formattedTextField97;
	private JButton button12;
	private JLabel label95;
	private JComboBox<String> comboBox37;
	private JFormattedTextField formattedTextField98;
	private JButton button14;
	private JFormattedTextField formattedTextField102;
	private JButton button48;
	private JFormattedTextField formattedTextField106;
	private JButton button52;
	private JFormattedTextField formattedTextField110;
	private JButton button56;
	private JLabel label96;
	private JFormattedTextField formattedTextField99;
	private JButton button45;
	private JFormattedTextField formattedTextField103;
	private JButton button49;
	private JFormattedTextField formattedTextField107;
	private JButton button53;
	private JFormattedTextField formattedTextField111;
	private JButton button57;
	private JLabel label97;
	private JFormattedTextField formattedTextField100;
	private JButton button46;
	private JFormattedTextField formattedTextField104;
	private JButton button50;
	private JFormattedTextField formattedTextField108;
	private JButton button54;
	private JFormattedTextField formattedTextField112;
	private JButton button58;
	private JLabel label98;
	private JComboBox<String> comboBox38;
	private JFormattedTextField formattedTextField101;
	private JButton button47;
	private JFormattedTextField formattedTextField105;
	private JButton button51;
	private JFormattedTextField formattedTextField109;
	private JButton button55;
	private JFormattedTextField formattedTextField113;
	private JButton button59;
	private JButton button5;
	private JLabel label10;
	private JPanel panel6;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JLabel label11;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
