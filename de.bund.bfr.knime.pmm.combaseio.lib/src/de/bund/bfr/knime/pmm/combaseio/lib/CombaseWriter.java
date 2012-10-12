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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;

public class CombaseWriter {

	private LinkedList<PmmTimeSeries> buffer;
	private String filename;
	private String encoding;
	
	public CombaseWriter( final String filename, final String encoding ) {
		this.filename = filename;
		this.encoding = encoding;
		buffer = new LinkedList<PmmTimeSeries>();
	}
	
	public void add( final PmmTimeSeries candidate ) throws PmmException {
		
		if( candidate == null )
			throw new PmmException( "Candidate must not be null." );
		
		buffer.add( candidate );
	}
		
	public void flush()
	throws UnsupportedEncodingException, FileNotFoundException, IOException, PmmException {

		if (encoding.equals("UTF-16LE")) {
			flush16le();
			return;
		}
		OutputStreamWriter out;
		
		out = new OutputStreamWriter(
			new BufferedOutputStream(
			new FileOutputStream( filename ) ), encoding );
		
		for( PmmTimeSeries candidate : buffer ) {
			
			if( candidate.hasCombaseId() ) {
				out.write( "\"RecordID\"\t\""+candidate.getCombaseId()+"\"\n" );
			}
			
			if( candidate.hasAgentDetail() ) {
				out.write( "\"Organism\"\t\""+candidate.getAgentDetail()+"\"\n" );
			}
			
			if( candidate.hasMatrixDetail() ) {
				out.write( "\"Environment\"\t\""+candidate.getMatrixDetail()+"\"\n" );
			}
			
			if( candidate.hasTemperature() ) {
				out.write( "\"Temperature\"\t\""+candidate.getTemperature()+" °C\"\n" );
			}
			
			if( candidate.hasPh() ) {
				out.write( "\"pH\"\t\""+candidate.getPh()+"\"\n" );
			}
			
			if( candidate.hasWaterActivity() ) {
				out.write( "\"Water Activity\"\t\""+candidate.getWaterActivity()+"\"\n" );
			}
			/*
			if( candidate.hasMisc() ) {
				out.write( "\"Conditions\"\t\""+candidate.getCommasepMisc()+"\"\n" );
			}
			*/
			/* if( candidate.hasMaximumRate() ) {
				out.write( "\"Maximum Rate\"\t\""+candidate.getMaximumRate()+"\"\n" );
			}
			
			if( candidate.hasDoublingTime() ) {
				out.write( "\"Doubling Time (h)\"\t\""+candidate.getDoublingTime()+"\"\n" );
			} */
			
			out.write( "\"Time (h)\"\t\"logc\"\n" );
			
			if( !candidate.isEmpty() ) {
				for( double[] tuple : candidate.getTimeSeries() ) {
					out.write( "\""+tuple[ 0 ]+"\"\t\""+tuple[ 1 ]+"\"\n" );
				}
			}
			
			out.write( "\n\n\n" );
		}
		
		out.close();
	}
	public void flush16le()
	throws UnsupportedEncodingException, FileNotFoundException, IOException, PmmException {
		StringBuffer buf = new StringBuffer();
		for( PmmTimeSeries candidate : buffer ) {
			
			if( candidate.hasCombaseId() ) {
				buf.append( "\"RecordID\"\t\""+candidate.getCombaseId()+"\"\n" );
			}
			
			if( candidate.hasAgentDetail() ) {
				buf.append( "\"Organism\"\t\""+candidate.getAgentDetail()+"\"\n" );
			}
			
			if( candidate.hasMatrixDetail() ) {
				buf.append( "\"Environment\"\t\""+candidate.getMatrixDetail()+"\"\n" );
			}
			
			if( candidate.hasTemperature() ) {
				buf.append( "\"Temperature\"\t\""+candidate.getTemperature()+" °C\"\n" );
			}
			
			if( candidate.hasPh() ) {
				buf.append( "\"pH\"\t\""+candidate.getPh()+"\"\n" );
			}
			
			if( candidate.hasWaterActivity() ) {
				buf.append( "\"Water Activity\"\t\""+candidate.getWaterActivity()+"\"\n" );
			}
			/*
			if( candidate.hasMisc() ) {
				buf.append( "\"Conditions\"\t\""+candidate.getCommasepMisc()+"\"\n" );
			}
			*/
			/* if( candidate.hasMaximumRate() ) {
				buf.append( "\"Maximum Rate\"\t\""+candidate.getMaximumRate()+"\"\n" );
			}
			
			if( candidate.hasDoublingTime() ) {
				buf.append( "\"Doubling Time (h)\"\t\""+candidate.getDoublingTime()+"\"\n" );
			} */
			
			buf.append( "\"Time (h)\"\t\"logc\"\n" );
			
			if( !candidate.isEmpty() ) {
				for( double[] tuple : candidate.getTimeSeries() ) {
					buf.append( "\""+tuple[ 0 ]+"\"\t\""+tuple[ 1 ]+"\"\n" );
				}
			}
			
			buf.append( "\n\n\n" );
		}
		OutputStream out = new FileOutputStream(filename);
		out.write(encodeString(buf.toString()));
		out.close();
	}
	public static byte[] encodeString(final String message) {

	    byte[] tmp = null;
	    try {
	        tmp = message.getBytes("UTF-16LE");
	    } catch(UnsupportedEncodingException e) {
	        // should not possible
	        AssertionError ae =
	        new AssertionError("Could not encode UTF-16LE");
	        ae.initCause(e);
	        throw ae;
	    }

	    // use brute force method to add BOM
	    byte[] utf16lemessage = new byte[2 + tmp.length];
	    utf16lemessage[0] = (byte)0xFF;
	    utf16lemessage[1] = (byte)0xFE;
	    System.arraycopy(tmp, 0,
	                     utf16lemessage, 2,
	                     tmp.length);
	    return utf16lemessage;
	}	
}
