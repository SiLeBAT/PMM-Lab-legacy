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

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class CombaseReader implements Enumeration<PmmTimeSeries> {
	
	private BufferedReader reader;
	private PmmTimeSeries next;
	private HashMap<String, Integer> newIDs = new HashMap<String, Integer>();
	
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
			String[] token = line.split( "\t" );
						
			if (token.length < 2)
				continue;
			
			if (token[0].isEmpty())
				continue;

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
			if( key.equals( "organism" ) ) {
				next.setAgentDetail( token[ 1 ] );
				continue;
			}
			
			// fetch environment
			if( key.equals( "environment" ) ) {
				next.setMatrixDetail( token[ 1 ] );
				continue;
			}
			
			// fetch temperature
			if( key.equals( "temperature" ) ) {				
				int pos = token[ 1 ].indexOf( " " );
				if( !token[ 1 ].endsWith( " °C" ) )
					throw new PmmException( "Temperature unit must be [°C]" );
				next.setTemperature( parse( token[ 1 ].substring( 0, pos ) ) );
				continue;
			}
			
			// fetch pH
			if( key.equals( "ph" ) ) {
				next.setPh( parse( token[ 1 ] ) );
				continue;
			}
			
			// fetch water activity
			if( key.equals( "water activity" ) ) {
				next.setWaterActivity( parse( token[ 1 ] ) );
				continue;
			}
			
			// fetch conditions
			if (key.equals("conditions")) {
				PmmXmlDoc xml = combase2XML(token[1]);
				next.setMisc(xml);
				continue;
			}
			
			if( key.startsWith( "time" ) && token[ 1 ].equals( "logc" ) ) {
				
				if( !key.endsWith( " (h)" ) )
					throw new Exception( "Time unit must be [h]." );
				
				while( true ) {
					
					line = reader.readLine();
					
					if( line == null )
						return;
					
					if( line.replaceAll( "\\t\"", "" ).isEmpty() )
						break;
					
					token = line.split( "\t" );
					
					for(int i = 0; i < token.length; i++ )
						token[ i ] = token[ i ].replaceAll( "[^a-zA-Z0-9° \\.\\(\\)/,]", "" );
					
					if( token.length < 2 )
						break;

					double t = parse(token[0]);
					double logc = parse( token[ 1 ] );
					
					if( Double.isNaN( t ) || Double.isNaN( logc ) )
						continue;
					
					next.add( t, logc );
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
				}
				// ersetzen mehrerer Spaces im Text durch lediglich eines, Bsp.: "was    ist los?" -> "was ist los?"
				String description = val.trim().replaceAll(" +", " ");
				if (!newIDs.containsKey(description)) newIDs.put(description, MathUtilities.getRandomNegativeInt());
				MiscXml mx = new MiscXml(newIDs.get(description), null, description, dbl, unit);
				result.add(mx);
			}
		}
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
