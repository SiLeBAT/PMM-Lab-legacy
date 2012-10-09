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
package de.bund.bfr.knime.pmm.common;

import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_AGENTDETAIL;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_COMMENT;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_LOGC;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_MATRIXDETAIL;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_MISC;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_MISCID;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_PH;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_TEMPERATURE;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_TIME;
import static de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema.ATT_WATERACTIVITY;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;



public class PmmTimeSeries extends KnimeTuple implements PmmXmlElementConvertable {
	
	private static final String ELEMENT_TIMESERIES = "TimeSeries";
	private static final String ELEMENT_TSTUPLE = "TimeSeriesTuple";
	private String warningMsg = null;

	public PmmTimeSeries() throws PmmException {
		
		super( new TimeSeriesSchema() );
				
		setCondId( MathUtilities.getRandomNegativeInt() );
		setMatrixId( MathUtilities.getRandomNegativeInt() );
		setAgentId( MathUtilities.getRandomNegativeInt() );
	}
	
	public PmmTimeSeries( final int condId ) throws PmmException {
		this();
		setCondId( condId );
	}
	
	public PmmTimeSeries( String combaseId ) throws PmmException {
		this();
		setCombaseId( combaseId );
	}
	
	public PmmTimeSeries( int condId, String combaseId ) throws PmmException {
		this( condId );
		setCombaseId( combaseId );
	}
	
	public PmmTimeSeries( KnimeTuple tuple ) throws PmmException {
		
		super( tuple.getSchema() );
		
		int i;
		
		for( i = 0; i < size(); i++ ) {
			setCell( i, tuple.getCell( i ) );
		}
			
	}
	
	@Override
	public String toString() {
		
		String ret;
		
		ret = "";
		
		try {
		
			ret += "RecordID       : "+getCombaseId()+"\n";
			ret += "Organism       : "+getAgentDetail()+"\n";
			ret += "Environment    : "+getMatrixDetail()+"\n";
			ret += "Temperature    : "+getTemperature()+"\n";
			ret += "pH             : "+getPh()+"\n";
			ret += "Water activity : "+getWaterActivity()+"\n";
			ret += "Conditions     : "+getCommasepMisc()+"\n";
			try {
				ret += "Time           : "+getString( TimeSeriesSchema.ATT_TIME );
				ret += "LocC           : "+getString( TimeSeriesSchema.ATT_LOGC );
			} catch( PmmException e ) {
				e.printStackTrace( System.err );
			}
			ret += "\n";
			
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
		
		return ret;
	}
	
	@Override
	public Element toXmlElement() {
		
		Element ret, el;
		List<Double> tlist;
		List<Double> clist;
		int i;
		
		ret = new Element( ELEMENT_TIMESERIES );
		
		try {
		
			if( getCombaseId() != null ) {
				if( !getCombaseId().isEmpty() ) {
					ret.setAttribute( TimeSeriesSchema.ATT_COMBASEID, getCombaseId() );
				}
			}
			
			// TODO: add id and detail
			
			if( hasAgentDetail() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_AGENTNAME, getAgentDetail() );
			}
			
			if( hasMatrixDetail() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_MATRIXNAME, getMatrixDetail() );
			}
			
			if( hasTemperature() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_TEMPERATURE, String.valueOf( getTemperature() ) );
			}
			
			if( hasPh() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_PH, String.valueOf( getPh() ) );
			}
			
			if( hasWaterActivity() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_WATERACTIVITY, String.valueOf( getWaterActivity() ) );
			}
			
			if( hasMisc() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_MISC, getCommasepMisc() );
			}
			
			/* if( !isNaN( maximumRate ) )
				ret.setAttribute( ATT_MAXIMUMRATE, String.valueOf( maximumRate ) );
			
			if( !isNaN( doublingTime ) )
				ret.setAttribute( ATT_DOUBLINGTIME, String.valueOf( doublingTime ) ); */
			
			tlist = this.getDoubleList( TimeSeriesSchema.ATT_TIME );
			clist = this.getDoubleList( TimeSeriesSchema.ATT_LOGC );
			
			if( tlist != null ) {
				for( i = 0; i < tlist.size(); i++ ) {
								
					el = new Element( ELEMENT_TSTUPLE );
					el.setAttribute( TimeSeriesSchema.ATT_TIME, String.valueOf( tlist.get( i ) ) );
					el.setAttribute( TimeSeriesSchema.ATT_LOGC, String.valueOf( clist.get( i ) ) );
					ret.addContent( el );
				}
			}
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
				
		return ret;
	}
	
	public void add( double t, double n ) throws PmmException {
		
		addValue( TimeSeriesSchema.ATT_TIME, t );
		addValue( TimeSeriesSchema.ATT_LOGC, n );
	}
	
	public void add( String t, String n ) {
		
		try {
			add( Double.valueOf( t ), Double.valueOf( n ) );
		}
		catch( Exception ex ) {
			ex.printStackTrace( System.err );
		}
	}
	
	public void setLiterature( int litId, String litRef ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_LITIDTS, litId );
		setValue( TimeSeriesSchema.ATT_LITTS, litRef );
	}
	
	public boolean isEmpty() throws PmmException {
		return isNull( TimeSeriesSchema.ATT_TIME );
	}
	
	public Integer getCondId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_CONDID );
	}
	
	public String getCombaseId() throws PmmException {
		return getString( TimeSeriesSchema.ATT_COMBASEID );
	}
	
	public String getAgentName() throws PmmException {
		return getString( TimeSeriesSchema.ATT_AGENTNAME );
	}
	
	public String getMatrixName() throws PmmException {
		return getString( TimeSeriesSchema.ATT_MATRIXNAME );
	}
	
	public Double getTemperature() throws PmmException {
		return getDouble( TimeSeriesSchema.ATT_TEMPERATURE );
	}
	
	public Double getPh() throws PmmException {
		return getDouble( TimeSeriesSchema.ATT_PH );
	}
	
	public Double getWaterActivity() throws PmmException {
		return getDouble( TimeSeriesSchema.ATT_WATERACTIVITY );
	}
	
	public String getCommasepMisc() throws PmmException {
		return getString( TimeSeriesSchema.ATT_MISC );
	}
	
	public String getCommasepMiscId() throws PmmException {
		return getString( ATT_MISCID );
	}
	
	public LinkedList<double[]> getTimeSeries() throws PmmException {
		
		LinkedList<double[]> ts;
		int i, n;
		List<Double> t, logc;
		
		ts = new LinkedList<double[]>();
		
		t = getDoubleList( TimeSeriesSchema.ATT_TIME );
		logc = getDoubleList( TimeSeriesSchema.ATT_LOGC );
		n = t.size();
		
		for( i = 0; i < n; i++ ) {
			ts.add( new double[] { t.get( i ), logc.get( i ) } );
		}
		
		
		return ts;
	}
	
	public Integer getAgentId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_AGENTID );
	}
	
	public Integer getMatrixId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_MATRIXID );
	}
	
	public String getAgentDetail() throws PmmException {
		return getString( TimeSeriesSchema.ATT_AGENTDETAIL );
	}
	
	public String getMatrixDetail() throws PmmException {
		return getString( TimeSeriesSchema.ATT_MATRIXDETAIL );
	}
	
	public String getComment() throws PmmException {
		return getString( TimeSeriesSchema.ATT_COMMENT );
	}
	public String getWarning() {
		return warningMsg;
	}
	public void setWarning(String warningMsg) {
		this.warningMsg = warningMsg;
	}
	
	
	@Deprecated
	public Double getMaximumRate() {
		return Double.NaN;
	}
	
	@Deprecated
	public Double getDoublingTime() {
		return Double.NaN;
	}
	
	public boolean hasCombaseId() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_COMBASEID );
	}
	
	public boolean hasAgentName() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_AGENTNAME );
	}
	
	public boolean hasMatrixName() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_MATRIXNAME );
	}
	
	public boolean hasTemperature() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_TEMPERATURE );
	}
	
	public boolean hasPh() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_PH );
	}
	
	public boolean hasWaterActivity() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_WATERACTIVITY );
	}
	
	public boolean hasMisc() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_MISC );
	}
	
	public boolean hasCondId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_CONDID ) >= 0;
	}
	
	public boolean hasAgentId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_AGENTID ) >= 0;
	}
	
	public boolean hasMatrixId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_MATRIXID ) >= 0;
	}
	
	public boolean hasMatrixDetail() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_MATRIXDETAIL );
	}
	
	public boolean hasAgentDetail() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_AGENTDETAIL );
	}
	
	public boolean hasComment() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_COMMENT );
	}
	
	@Deprecated
	public boolean hasMaximumRate() { return false; }
	
	@Deprecated
	public boolean hasDoublingTime() { return false; } 
	
	
	public void setCondId( final int condId ) throws PmmException { 
		setValue( TimeSeriesSchema.ATT_CONDID, condId );
	}
	
	public void setCombaseId( final String combaseId ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_COMBASEID, combaseId );
	}
	
	public void setAgentName( final String agentName ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_AGENTNAME, agentName );
	}
	
	public void setMatrixName( final String matrixName ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_MATRIXNAME, matrixName );
	}
	
	public void setCommasepMisc( final String misc ) throws PmmException {
		setValue( ATT_MISC, misc );
	}
	
	public void setCommasepMiscId( final String miscId ) throws PmmException  {
		setValue( ATT_MISCID, miscId );
	}
	
	public void setAgentDetail( final String agentDetail ) throws PmmException {
		setValue( ATT_AGENTDETAIL, agentDetail );
	}
	public void setMatrixDetail( final String matrixDetail ) throws PmmException {
		setValue( ATT_MATRIXDETAIL, matrixDetail );
	}
	
	public void setAgentId( final Integer agentId ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_AGENTID, agentId );
	}
	
	public void setMatrixId( final Integer matrixId ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_MATRIXID, matrixId );
	}
	
	public void setComment( final String comment ) throws PmmException {
		setValue( ATT_COMMENT, comment );
	}
	
	public void setMatrix( final int matrixId, final String matrixName, final String matrixDetail )
	throws PmmException {
		setMatrixId( matrixId );
		setMatrixName( matrixName );
		setMatrixDetail( matrixDetail );
	}
	
	public void setAgent( final int agentId, final String agentName, final String agentDetail )
	throws PmmException {
		setAgentId( agentId );
		setAgentName( agentName );
		setAgentDetail( agentDetail );
	}
	
	public void setTemperature( final Double temperature ) throws PmmException {
		
		if( temperature == null ) {
			setValue( ATT_TEMPERATURE, null );
			return;
		}
		
		if( temperature.isNaN() || temperature.isInfinite() ) {
			setValue( ATT_TEMPERATURE, null );
			return;
		}
		
		if( temperature < -273.15 ) {
			throw new PmmException( "Temperature cannot undergo absolute zero." );
		}
		
		setValue( ATT_TEMPERATURE, temperature );
	}
	
	public void setTemperature( final String temperature ) throws PmmException {
		
		if( temperature == null ) {
			setValue( ATT_TEMPERATURE, null );
		} else {
			setTemperature( Double.valueOf( temperature ) );
		}
	}
	
	public void setPh( final Double ph ) throws PmmException {
		
		if( ph == null ) {
			setValue( ATT_PH, null );
			return;
		}
		
		if( ph.isInfinite() || ph.isNaN() ) {
			setValue( ATT_PH, null );
			return;
		}
		
		if( ph < 0 ) {
			throw new PmmException( "pH cannot be negative." );
		}
		
		if( ph > 14 ) {
			throw new PmmException( "pH cannot exceed 14." );
		}
		
		setValue( ATT_PH, ph );
	}
	
	public void setPh( final String ph ) throws PmmException {
		
		if( ph == null ) {
			setValue( ATT_PH, null );
		} else {
			setPh( Double.valueOf( ph ) );
		}
	}
	
	public void setWaterActivity( final Double aw ) throws PmmException {
		
		if( aw == null ) {
			setValue( ATT_WATERACTIVITY, null );
			return;
		}
			
		if( aw.isNaN() || aw.isInfinite() ) {
			setValue( ATT_WATERACTIVITY, null );
			return;
		}
				
		if( aw < 0 ) {
			throw new PmmException( "Water activity connot be lower than 0." );
		}
		
		if( aw > 1 ) {
			throw new PmmException( "Water activity cannot exceed 1." );
		}
		
		setValue( ATT_WATERACTIVITY, aw );
	}
	
	public void setWaterActivity( final String aw ) throws PmmException {
		
		if( aw == null ) {
			setValue( ATT_WATERACTIVITY, null );
		} else {
			setWaterActivity( Double.valueOf( aw ) );
		}
	}
	
	public void setCommasepTime( final String t ) throws PmmException {
		
		if( t == null ) {
			setValue( ATT_TIME, null );
		} else {
			setValue( ATT_TIME, t.replaceAll( "E0", "" ) );
		}
	}
	
	public void setCommasepLogc( final String c ) throws PmmException {
		
		if( c == null ) {
			setValue( ATT_LOGC, null );
		} else {
			setValue( ATT_LOGC, c.replaceAll( "E0", "" ) );
		}
	}
	
	@Deprecated
	public void setMaximumRate( final double mr ) {}
	
	@Deprecated
	public void setDoublingTime( final double dt ) {}

}
