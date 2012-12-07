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
package de.bund.bfr.knime.foodprocess.lib;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import lombok.Getter;
import de.bund.bfr.knime.foodprocess.ui.MyChartDialog;

public class ParametersDef {

	@Getter private JTextField volume;
	@Getter private JButton volume_func;
	@Getter private JComboBox<String> volumeUnit;
	@Getter private JTextField temperature;
	@Getter private JButton temperature_func;
	@Getter private JComboBox<String> temperatureUnit;
	@Getter private JTextField ph;
	@Getter private JButton ph_func;
	@Getter private JTextField aw;
	@Getter private JButton aw_func;
	@Getter private JTextField pressure;
	@Getter private JButton pressure_func;
	@Getter private JComboBox<String> pressureUnit;

	public ParametersDef() {
		//NumberFormat nf = NumberFormat.getNumberInstance( java.util.Locale.US );

		volume = new JTextField();
		volume.setColumns(10);
		//volume.setValue( 0. );
		
		volume_func = new JButton("...");
		volume_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Percent", volume);
			}
		});
		volumeUnit = new JComboBox<String>( FoodProcessDef.COMBO_VOLUMEUNIT );
		
		temperature = new JTextField();
		temperature.setColumns(10);
		
		temperature_func = new JButton("...");
		temperature_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Temperature", temperature);
			}
		});
		temperatureUnit = new JComboBox<String>( FoodProcessDef.COMBO_TEMPERATUREUNIT );

		ph = new JTextField();
		ph.setColumns(10);
		
		ph_func = new JButton("...");
		ph_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "pH", ph);
			}
		});

		aw = new JTextField();
		aw.setColumns(10);
		
		aw_func = new JButton("...");
		aw_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "aw", aw);
			}
		});

		pressure = new JTextField();
		pressure.setColumns(10);
		
		pressure_func = new JButton("...");
		pressure_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Pressure", pressure);
			}
		});
		pressureUnit = new JComboBox<String>( FoodProcessDef.COMBO_PRESSUREUNIT );
	}
	public ParametersSetting getSetting() {
		ParametersSetting ps = new ParametersSetting();
		
		//val = volume.getValue() == null ? null : ((Number)volume.getValue()).doubleValue();
		ps.setVolume( volume.getText() );
		
		//val = temperature.getValue() == null ? null : ((Number)temperature.getValue()).doubleValue();
		//assert val >= -459.67;
		ps.setTemperature( temperature.getText() );
		
		//val = ph.getValue() == null ? null : ((Number)ph.getValue()).doubleValue();
		//assert val >= 0;
		//assert val <= 14;
		ps.setPh( ph.getText() );
		
		//val = aw.getValue() == null ? null : ((Number)aw.getValue()).doubleValue();
		//assert val >= 0;
		//assert val <= 1;
		ps.setAw( aw.getText() );
		
		//val = pressure.getValue() == null ? null : ((Number)pressure.getValue()).doubleValue();
		ps.setPressure( pressure.getText() );
		
		ps.setTemperatureUnit( ( String )temperatureUnit.getSelectedItem() );	
		ps.setVolumeUnit(( String )volumeUnit.getSelectedItem());
		ps.setPressureUnit( ( String )pressureUnit.getSelectedItem() );
		
		
		return ps;
	}
	public void setSetting( final ParametersSetting ps ) {
		String val;
		
		val = ps.getVolume();
		//assert val >= 0;
		volume.setText( val );
		
		val = ps.getTemperature();
		//assert val >= -459.67;
		temperature.setText( val );
		
		val = ps.getPh();
		//assert val >= 0;
		//assert val <= 14;
		ph.setText( val );
		
		val = ps.getAw();
		//assert val >= 0;
		//assert val <= 1;
		aw.setText( val );
		
		val = ps.getPressure();
		pressure.setText( val );
		
		temperatureUnit.setSelectedItem( ps.getTemperatureUnit() );
		volumeUnit.setSelectedItem( ps.getVolumeUnit() );
		pressureUnit.setSelectedItem( ps.getPressureUnit() );
	}
	public void setEnabledBase( final boolean enabled ) {
		volume.setEnabled(enabled);
		volume_func.setEnabled(enabled);
		temperature.setEnabled(enabled);
		temperature_func.setEnabled(enabled);
		ph.setEnabled(enabled);
		ph_func.setEnabled(enabled);
		aw.setEnabled(enabled);
		aw_func.setEnabled(enabled);
		pressure.setEnabled(enabled);
		pressure_func.setEnabled(enabled);
	}
	public void setEnabledUnits( final boolean enabled ) {
		volumeUnit.setEnabled(enabled);
		temperatureUnit.setEnabled(enabled);
		pressureUnit.setEnabled(enabled);
	}
	private void func_ActionPerformed(final ActionEvent e, final String yAxis, final JTextField tf) {
		JButton button = (JButton) e.getSource();
        Container c = button.getParent();
        while (c != null) {
            if (c instanceof Frame) {
				break;
			}
            c = c.getParent();
        }
        MyChartDialog mcd = new MyChartDialog(( Window )c, tf.getText(), "Zeit", yAxis);
        mcd.setModal(true);
        mcd.setVisible(true);
        //button.setToolTipText(mcd.getDatenpunkte());
        tf.setText(mcd.getCleanDataset(mcd.getStrDataSet()));
	}
}
