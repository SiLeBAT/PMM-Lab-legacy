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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;

import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TsReaderUi extends JPanel {
	
	private static final long serialVersionUID = 20120913;
	private JTextField matrixField, literatureField;
	private JTextField agentField;
	private LinkedHashMap<String, DoubleTextField[]> params;
	private JPanel theParamPanel;
	
	public TsReaderUi() {
		
		JPanel panel;
		
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );

		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Organism" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 300, 75 ) );
		add( panel );
		
		panel.add( new JLabel( "Organism name   " ), BorderLayout.WEST );
		agentField = new JTextField();
		panel.add( agentField, BorderLayout.CENTER );

		
		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Matrix" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 75 ) );
		add( panel );
		
		panel.add( new JLabel( "Matrix name   " ), BorderLayout.WEST );
		
		matrixField = new JTextField();
		panel.add( matrixField, BorderLayout.CENTER );

		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Literature" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 75 ) );
		add( panel );
		
		panel.add( new JLabel( "Author/Title   " ), BorderLayout.WEST );
		
		literatureField = new JTextField();
		panel.add( literatureField, BorderLayout.CENTER );

		theParamPanel = new JPanel();
		theParamPanel.setBorder( BorderFactory.createTitledBorder( "Parameters" ) );
		theParamPanel.setLayout(new GridLayout(6, 3));
		theParamPanel.setPreferredSize( new Dimension( 250, 200 ) );
		add( theParamPanel );

		handleParams();
	}

	private void handleParams() {
		DoubleTextField[] dtf;
		if (params == null) {
			params = new LinkedHashMap<String, DoubleTextField[]>();
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("Temperature", dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("pH", dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("aw", dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("param1", dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("param2", dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("param3", dtf);
		}
		
		theParamPanel.removeAll();
		//theParamPanel.setVisible(false);
		int lfd = 0;
		for (String par : params.keySet()) {
			theParamPanel.add(new JTextField(par));
			dtf = params.get(par);
			theParamPanel.add(dtf[0]);
			theParamPanel.add(dtf[1]);
			lfd++;
			if (lfd > 5) break;
		}	
		//theParamPanel.revalidate();
		//theParamPanel.validate();
		//theParamPanel.setVisible(true);
	}

	public String getMatrixString() { return matrixField.getText(); }
	public String getAgentString() { return agentField.getText(); }
	public String getLiteratureString() { return literatureField.getText(); }
	
	public LinkedHashMap<String, DoubleTextField[]> getParameter() {
		return params;
	}
	public void setParameter(LinkedHashMap<String, DoubleTextField[]> params) {
		this.params = params;
		handleParams();
	}
	
	public void setMatrixString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		matrixField.setText( str );
	}
	
	public void setAgentString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		agentField.setText( str );
	}
	
	public void setLiteratureString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Literature Filter string must not be null." );
		
		literatureField.setText( str );
	}
	
	public static boolean passesFilter(
		final String matrixString,
		final String agentString,
		final String literatureString,
		final HashMap<String, double[]> parameter,
		final KnimeTuple tuple ) throws PmmException {
			
		if (matrixString != null && !matrixString.trim().isEmpty()) {
			String s = tuple.getString( TimeSeriesSchema.ATT_MATRIXNAME );
			String sd = tuple.getString( TimeSeriesSchema.ATT_MATRIXDETAIL );
			if (s == null) s = ""; else s = s.toLowerCase();
			if (sd == null) sd = ""; else sd = sd.toLowerCase();
			if (!s.contains(matrixString.toLowerCase()) && !sd.contains(matrixString.toLowerCase())) return false;
		}
		
		if (agentString != null && !agentString.trim().isEmpty()) {
			String s = tuple.getString( TimeSeriesSchema.ATT_AGENTNAME );
			String sd = tuple.getString( TimeSeriesSchema.ATT_AGENTDETAIL );
			if (s == null) s = ""; else s = s.toLowerCase();
			if (sd == null) sd = ""; else sd = sd.toLowerCase();
			if (!s.contains(agentString.toLowerCase()) && !sd.contains(agentString.toLowerCase())) return false;
		}
		
		if (literatureString != null && !literatureString.trim().isEmpty()) {
			PmmXmlDoc litXmlDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
        	for (PmmXmlElementConvertable el : litXmlDoc.getElementSet()) {
        		if (el instanceof LiteratureItem) {
        			LiteratureItem lit = (LiteratureItem) el;
        			String s = lit.getAuthor();
        			String sd = lit.getTitle();
        			if (s == null) s = ""; else s = s.toLowerCase();
        			if (sd == null) sd = ""; else sd = sd.toLowerCase();
        			if (!s.contains(literatureString.toLowerCase()) && !sd.contains(literatureString.toLowerCase())) return false;
        		}
        	}
		}
		
		for (String par : parameter.keySet()) {
			double[] dbl = parameter.get(par);
			if (par.equalsIgnoreCase("temperature")) {
				double temp = tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);
				if (temp < dbl[0] || temp > dbl[1]) {
					return false;
				}
			}
			else if (par.equalsIgnoreCase("ph")) {
				double temp = tuple.getDouble(TimeSeriesSchema.ATT_PH);
				if (temp < dbl[0] || temp > dbl[1]) {
					return false;
				}
			}
			else if (par.equalsIgnoreCase("aw")) {
				double temp = tuple.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY);
				if (temp < dbl[0] || temp > dbl[1]) {
					return false;
				}
			}
			else {
				boolean paramFound = false;
				PmmXmlDoc miscXmlDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
	        	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
	        		if (el instanceof MiscXml) {
	        			MiscXml mx = (MiscXml) el;
	        			if (mx.getName().toLowerCase().equals(par)) {
	        				if (mx.getValue() < dbl[0] || mx.getValue() > dbl[1]) {
	        					return false;
	        				}
	        				else {
	        					paramFound = true;
	        					break;
	        				}
	        			}
	        		}
	        	}
	        	if (!paramFound) return false;
			}
		}
		
		return true;
	}
	
	public void setActive() {
		matrixField.setEnabled( true );
		agentField.setEnabled( true );
		literatureField.setEnabled( true );
	}
	
	public void setInactive() {
		matrixField.setEnabled( false );
		agentField.setEnabled( false );
		literatureField.setEnabled( false );
	}
	

}
