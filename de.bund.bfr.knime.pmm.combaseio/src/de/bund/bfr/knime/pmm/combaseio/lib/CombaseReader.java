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
package de.bund.bfr.knime.pmm.combaseio.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class CombaseReader implements Enumeration<PmmTimeSeries> {
	
	private BufferedReader reader;
	private PmmTimeSeries next;
	private HashMap<String, Integer> newAgentIDs = new HashMap<String, Integer>();
	private HashMap<String, Integer> newMatrixIDs = new HashMap<String, Integer>();
	private HashMap<String, Integer> newIDs = new HashMap<String, Integer>();
	private HashMap<String, String> newParams = new HashMap<String, String>();
	
	public CombaseReader(final String filename) throws FileNotFoundException, IOException, Exception {
		InputStreamReader isr = null;
		File file = new File(filename);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-16LE");
		}
		else {
			try {
				URL url = new URL(filename);
				isr = new InputStreamReader(url.openStream(), "UTF-16LE");
				isr.read(); 
			}
			catch (Exception e) {throw new FileNotFoundException("File not found");}
		}
		if (isr != null) {
			reader = new BufferedReader(isr);
			step();
		}
	}
	
	public void close() throws IOException {
		reader.close();
	}
	
	public PmmTimeSeries nextElement() {

		PmmTimeSeries ret;
		
		ret = next;
		
		try {
			step();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public boolean hasMoreElements() {
		return next != null;
	}
	
	private void step() throws IOException, Exception {		
		// initialize next time series
		next = new PmmTimeSeries();
		
		while (true) {
			String line = reader.readLine();

			if( line == null ) {
				next = null;
				return;
			}
			
			// split up token
			String[] token = line.split("\t");
						
			if (token.length < 2) continue;
			
			if (token[0].isEmpty()) continue;

			for (int i = 0; i < token.length; i++) {
				//token[i] = token[i].replaceAll("[^a-zA-Z0-9° \\.\\(\\)_/\\+\\-\\*,:]", "");
				token[i] = token[i].replaceAll("\"", "");
			}
			String key = token[0].toLowerCase().trim();
//	 	    utf16lemessage[0] = (byte)0xFF;   utf16lemessage[1] = (byte)0xFE;
			if (key.length() > 1 && key.charAt(0) == 65279) key = key.substring(1);
			
			// fetch record id
			if (key.equals("recordid")) {
				next.setCombaseId(token[1]);
				continue;
			}
			
			// fetch organism
			if (key.equals("organism")) {
				//next.setAgentDetail( token[ 1 ] );
				setAgent(next, token[1]);
				continue;
			}
			
			// fetch environment
			if (key.equals("environment")) {
				//next.setMatrixDetail(token[1]);
				setMatrix(next, token[1]);
				continue;
			}
			
			// fetch temperature
			if (key.equals("temperature")) {				
				int pos = token[ 1 ].indexOf(" ");
				if (!token[1].endsWith(" °C")) throw new PmmException( "Temperature unit must be [°C]" );
				Double value = parse(token[1].substring(0, pos));
				//next.setTemperature(value);
				next.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE, AttributeUtilities.ATT_TEMPERATURE, value, null, "°C");
				continue;
			}
			
			// fetch pH
			if( key.equals( "ph" ) ) {
				Double value = parse(token[1]);
				//next.setPh(value);
				next.addMisc(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, AttributeUtilities.ATT_PH, value, null, null);
				continue;
			}
			
			// fetch water activity
			if( key.equals( "water activity" ) ) {
				Double value = parse(token[1]);
				//next.setWaterActivity(value);
				next.addMisc(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_WATERACTIVITY, AttributeUtilities.ATT_WATERACTIVITY, value, null, null);
				continue;
			}
			
			// fetch conditions
			if (key.equals("conditions")) {
				PmmXmlDoc xml = combase2XML(token[1]);
				next.addMiscs(xml);
				continue;
			}
			
			if (key.equals("maximum rate")) {
				next.setMaximumRate(parse(token[1]));
				continue;
			}
				
			
			if (key.startsWith("time") && token[ 1 ].equals( "logc")) {
				if (!key.endsWith("(h)")) throw new Exception("Time unit must be [h].");
				while(true) {					
					line = reader.readLine();					
					if (line == null) return;					
					if (line.replaceAll( "\\t\"", "" ).isEmpty()) break;					
					token = line.split("\t");					
					for (int i = 0; i < token.length; i++) {
						token[i] = token[i].replaceAll("[^a-zA-Z0-9° \\.\\(\\)/,]", "");
					}					
					if (token.length < 2) {
						break;
					}
					double t = parse(token[0]);
					double logc = parse(token[ 1 ]);					
					if (Double.isNaN(t) || Double.isNaN(logc)) {
						continue;
					}					
					next.add(t, null, logc, null);
				}
				break;
			}
		}
	}
	
	private static double parse( String num ) {
		double n = Double.NaN;
		
		num = num.toLowerCase();
		num = num.trim();
		if( num.equals( "no growth" ) )
			return 0;		
		
		try {			
			num = num.replaceAll( "[a-zA-Z\\(\\)\\s]", "" );
			num = num.replaceAll( ",", "." );
			n = Double.valueOf( num );			
		}
		catch( Exception e ) {}
		
		return n;
	}
	
	private PmmXmlDoc combase2XML(String misc) {
		PmmXmlDoc result = null;
		if (misc != null) {
			result = new PmmXmlDoc(); 
			List<String> conds = condSplit(misc);
			for (int i=0;i<conds.size();i++) {
				String val = conds.get(i).trim();
				int index = val.indexOf(':');
				int index2 = 0;
				String unit = null;
				Double dbl = null;
				if (index >= 0) {
					try {
						dbl = Double.parseDouble(val.substring(index + 1));
						if (val.charAt(index - 1) == ')') {
							for (index2 = index - 1;index2 >= 0 && val.charAt(index2) != '(';index2--) {
								;
							}
							unit = val.substring(index2 + 1, index - 1);
							val = val.substring(0, index2);
						}
					}
					catch (Exception e) {e.printStackTrace();}
				} else {
					dbl = 1.0;
				}
				// ersetzen mehrerer Spaces im Text durch lediglich eines, Bsp.: "was    ist los?" -> "was ist los?"
				String description = val.trim().replaceAll(" +", " ");
				MiscXml mx = getMiscXml(description, dbl, null, unit);
				//new MiscXml(newIDs.get(description), getCombaseName(description), description, dbl, unit);
				result.add(mx);
			}
		}
		return result;
	}
	private void setMatrix(PmmTimeSeries next, String matrixname) {
		Integer id = null;
		String matrixdetail = null;
		int index = matrixname.indexOf("("); 
		if (index > 0) {
			matrixdetail = matrixname.substring(index).trim();
			matrixname = matrixname.substring(0, index).trim();
		}
		if (!newMatrixIDs.containsKey(matrixname)) {
			id = DBKernel.getID("Matrices", "Matrixname", matrixname);
			if (id == null)  {
				System.err.println(matrixname + "... unknown Matrix ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newMatrixIDs.put(matrixname, id);
		}
		else id = newMatrixIDs.get(matrixname);
		matrixdetail = id < 0 ? matrixname + " (" + matrixdetail + ")" : matrixdetail;
		next.setMatrix(id, id < 0 ? null : matrixname, matrixdetail);
	}
	private void setAgent(PmmTimeSeries next, String agentsname) {
		Integer id = null;
		if (!newAgentIDs.containsKey(agentsname)) {
			id = DBKernel.getID("Agenzien", "Agensname", agentsname);
			if (id == null)  {
				System.err.println(agentsname + "... unknown Agens ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newAgentIDs.put(agentsname, id);
		}
		else id = newAgentIDs.get(agentsname);
		next.setAgent(id, id < 0 ? null : agentsname, id < 0 ? agentsname : null);
	}
	private MiscXml getMiscXml(String description, Double dbl, String category, String unit) {
		if (!newIDs.containsKey(description)) {
			Integer id = DBKernel.getID("SonstigeParameter", "Beschreibung", description.toLowerCase());
			if (id == null)  {
				System.err.println(description + "... unknown Misc ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newIDs.put(description, id);
		}
		if (!newParams.containsKey(description)) {
			Object param = DBKernel.getValue("SonstigeParameter", "Beschreibung", description.toLowerCase(), "Parameter");
			if (param == null)  {
				System.err.println(description + "... unknown Misc parameter...");
				param = getCombaseName(description);
			}
			newParams.put(description, param.toString());
		}
		return new MiscXml(newIDs.get(description), newParams.get(description), description, dbl, category, unit);
	}
	private String getCombaseName(String description) {
		String result = "";
		String des = description.toLowerCase();
    	if (des.equals("alta fermentation product in the environment")) result = "ALTA";
    	else if (des.equals("acetic acid (possibly as salt) in the environment")) result = "acetic_acid";
    	else if (des.equals("anaerobic environment")) result = "anaerobic";
    	else if (des.equals("ascorbic acid (possibly as salt) in the environment")) result = "ascorbic_acid";
    	else if (des.equals("benzoic acid (possibly as salt) in the environment")) result = "benzoic_acid";
    	else if (des.equals("citric acid (possibly as salt) in the environment")) result = "citric_acid";
    	else if (des.equals("carbon-dioxide in the environment")) result = "CO_2";
    	else if (des.equals("other species in the environment")) result = "competition";
    	else if (des.equals("cut (minced, chopped, ground, etc)")) result = "cut";
    	else if (des.equals("dried food")) result = "dried";
    	else if (des.equals("ethylenenediaminetetraacetic acid in the environment")) result = "EDTA";
    	else if (des.equals("ethanol in the environment")) result = "ethanol";
    	else if (des.equals("fat in the environment")) result = "fat";
    	else if (des.equals("frozen food")) result = "frozen";
    	else if (des.equals("fructose in the environment")) result = "fructose";
    	else if (des.equals("glucose in the environment")) result = "glucose";
    	else if (des.equals("glycerol in the environment")) result = "glycerol";
    	else if (des.equals("hydrochloric acid in the environment")) result = "HCl";
    	else if (des.equals("inoculation in/on previously heated (cooked, baked, pasteurized, etc) but not sterilised food/medium")) result = "heated";
    	else if (des.equals("in an environment that has been irradiated")) result = "irradiated";
    	else if (des.equals("irradiation at constant rate during the observation time")) result = "irradiation";
    	else if (des.equals("lactic acid (possibly as salt) in the environment")) result = "lactic_acid";
    	else if (des.equals("food fermented by lactic acid bacteria")) result = "lactic_bacteria_fermented";
    	else if (des.equals("modified atmosphere environment")) result = "Modified_Atmosphere";
    	else if (des.equals("malic acid in the environment")) result = "malic_acid";
    	else if (des.equals("moisture in the environment")) result = "moisture";
    	else if (des.equals("glycerol monolaurate (emulsifier) in the environment")) result = "monolaurin";
    	else if (des.equals("nitrogen in the environment")) result = "N_2";
    	else if (des.equals("sodium chloride in the environment")) result = "NaCl";
    	else if (des.equals("nisin in the environment")) result = "nisin";
    	else if (des.equals("sodium or potassium nitrite in the environment")) result = "nitrite";
    	else if (des.equals("oxygen (aerobic conditions) in the environment")) result = "O_2";
    	else if (des.equals("propionic acid (possibly as salt) in the environment")) result = "propionic_acid";
    	else if (des.equals("raw")) result = "raw";
    	else if (des.equals("shaken (agitated, stirred)")) result = "shaken";
    	else if (des.equals("smoked food")) result = "smoked";
    	else if (des.equals("sorbic acid (possibly as salt) in the environment")) result = "sorbic_acid";
    	else if (des.equals("sterilised before inoculation")) result = "sterile";
    	else if (des.equals("sucrose in the environment")) result = "sucrose";
    	else if (des.equals("sugar in the environment")) result = "sugar";
    	else if (des.equals("vacuum-packed")) result = "vacuum";
    	else if (des.equals("oregano essential oil in the environment")) result = "oregano";
    	else if (des.equals("with the indigenous flora in the environment (but not counted)")) result = "indigenous_flora";
    	else if (des.equals("pressure controlled")) result = "pressure";
    	else if (des.equals("in presence of diacetic acid (possibly as salt)")) result = "diacetic_acid";
    	else if (des.equals("in presence of betaine")) result = "betaine";
    	else System.err.println(description);
    	
    	return result;
	}
	private List<String> condSplit(final String misc) {
		if (misc == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(misc, ",");
		int openParenthesis = 0;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (openParenthesis > 0) {
				nextToken = result.get(result.size() - 1) + "," + nextToken;
				result.remove(result.size() - 1);
			}
			result.add(nextToken);
			openParenthesis = 0;
			int index = -1;
			while ((index = nextToken.indexOf("(", index+1)) >= 0) {
				openParenthesis++;
			}
			while ((index = nextToken.indexOf(")", index+1)) >= 0) {
				openParenthesis--;
			}
		}
		return result;
	}
}
