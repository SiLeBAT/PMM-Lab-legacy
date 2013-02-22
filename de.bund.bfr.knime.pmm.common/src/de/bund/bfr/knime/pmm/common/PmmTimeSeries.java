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

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class PmmTimeSeries extends KnimeTuple implements PmmXmlElementConvertable {
	
	private static final String ELEMENT_TIMESERIES = "TimeSeries";
	//private static final String ELEMENT_TSTUPLE = "TimeSeriesTuple";
	private static final String ELEMENT_TSXML = "TimeSeriesXml";
	private static final String ELEMENT_MDINFO = "MdInfoXml";
	private String warningMsg = null;
	private double mr;

	public PmmTimeSeries() throws PmmException {		
		super(new TimeSeriesSchema());
				
		setCondId( MathUtilities.getRandomNegativeInt() );
		setMatrixId( MathUtilities.getRandomNegativeInt() );
		setAgentId( MathUtilities.getRandomNegativeInt() );
		
	}
	
	public PmmTimeSeries(final int condId) throws PmmException {
		this();
		setCondId(condId);
	}
	
	public PmmTimeSeries(String combaseId) throws PmmException {
		this();
		setCombaseId(combaseId);
	}
	
	public PmmTimeSeries(int condId, String combaseId) throws PmmException {
		this(condId);
		setCombaseId(combaseId);
	}
	
	public PmmTimeSeries(KnimeTuple tuple) throws PmmException {		
		super(tuple.getSchema());
		for (int i = 0; i < size(); i++) {
			setCell(i, tuple.getCell(i));
		}			
	}
		
	@Override
	public Element toXmlElement() {
		
		Element ret = new Element(ELEMENT_TIMESERIES);
		
		try {
		
			if (getCombaseId() != null) {
				if (!getCombaseId().isEmpty()) {
					ret.setAttribute(TimeSeriesSchema.ATT_COMBASEID, getCombaseId());
				}
			}
			
			// TODO: add id and detail
			
			/*
			if( hasTemperature() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_TEMPERATURE, String.valueOf( getTemperature() ) );
			}
			
			if( hasPh() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_PH, String.valueOf( getPh() ) );
			}
			
			if( hasWaterActivity() ) {
				ret.setAttribute( TimeSeriesSchema.ATT_WATERACTIVITY, String.valueOf( getWaterActivity() ) );
			}
			*/
			if (hasMatrix()) {
				ret.setAttribute(TimeSeriesSchema.ATT_MATRIX, getMatrix().toXmlString());
			}
			if (hasAgent()) {
				ret.setAttribute(TimeSeriesSchema.ATT_AGENT, getAgent().toXmlString());
			}
			if (hasMisc()) {
				ret.setAttribute(TimeSeriesSchema.ATT_MISC, getMisc().toXmlString());
			}
			/* if( !isNaN( maximumRate ) )
				ret.setAttribute( ATT_MAXIMUMRATE, String.valueOf( maximumRate ) );
			
			if( !isNaN( doublingTime ) )
				ret.setAttribute( ATT_DOUBLINGTIME, String.valueOf( doublingTime ) ); 
			
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
			*/
			Element element = new Element(ELEMENT_TSXML);
			PmmXmlDoc timeSeriesXmlDoc = this.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			element.addContent(timeSeriesXmlDoc.toXmlString());
			ret.addContent(element);
			element = new Element(ELEMENT_MDINFO);
			PmmXmlDoc mdInfoXmlDoc = this.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
			element.addContent(mdInfoXmlDoc.toXmlString());
			ret.addContent(element);
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
				
		return ret;
	}
	
	public void add(String name, Double t, Double n) throws PmmException {		
		TimeSeriesXml tsx = new TimeSeriesXml(name, t, n);
		PmmXmlDoc timeSeriesXmlDoc = this.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		timeSeriesXmlDoc.add(tsx);
		this.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXmlDoc);
		setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXmlDoc);
		//addValue(TimeSeriesSchema.ATT_TIME, t);
		//addValue(TimeSeriesSchema.ATT_LOGC, n);
	}
	public void add(Double t, Double n) throws PmmException {		
		add("t"+System.currentTimeMillis(), t, n);
	}
	
	public void add(String t, String n) {		
		try {
			add(Double.valueOf(t), Double.valueOf(n));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	public void setLiterature(PmmXmlDoc li) throws PmmException {
		setValue(TimeSeriesSchema.ATT_LITMD, li);
	}
	public void setMisc(PmmXmlDoc misc) throws PmmException {
		setValue(TimeSeriesSchema.ATT_MISC, misc);
	}
	
	public boolean isEmpty() throws PmmException {
		return isNull(TimeSeriesSchema.ATT_TIMESERIES); // TimeSeriesSchema.ATT_TIME
	}
	
	public Integer getCondId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_CONDID );
	}
	
	public String getCombaseId() throws PmmException {
		return getString( TimeSeriesSchema.ATT_COMBASEID );
	}
	
	public String getAgentName() throws PmmException {
		return (String) getAgentAttribute(false, true, false);
//		return getString( TimeSeriesSchema.ATT_AGENTNAME );
	}
	
	public String getMatrixName() throws PmmException {
		return (String) getMatrixAttribute(false, true, false);
		//return getString( TimeSeriesSchema.ATT_MATRIXNAME );
	}
	
	private Object getMatrixAttribute(boolean id, boolean name, boolean detail) throws PmmException {
		PmmXmlDoc matrixXmlDoc = getMatrix();
		if (matrixXmlDoc == null) matrixXmlDoc = new PmmXmlDoc();
		MatrixXml mx = null;
    	for (PmmXmlElementConvertable el : matrixXmlDoc.getElementSet()) {
    		if (el instanceof MatrixXml) {
    			mx = (MatrixXml) el;
    			if (id) return mx.getID();
    			if (name) return mx.getName();
    			if (detail) return mx.getDetail();
    			break;
    		}
    	}
       	return null;
	}
	private Object getAgentAttribute(boolean id, boolean name, boolean detail) throws PmmException {
		PmmXmlDoc agentXmlDoc = getAgent();
		if (agentXmlDoc == null) agentXmlDoc = new PmmXmlDoc();
		AgentXml ax = null;
    	for (PmmXmlElementConvertable el : agentXmlDoc.getElementSet()) {
    		if (el instanceof AgentXml) {
    			ax = (AgentXml) el;
    			if (id) return ax.getID();
    			if (name) return ax.getName();
    			if (detail) return ax.getDetail();
    			break;
    		}
    	}
    	return null;
	}
	public Double getMiscValue(String attribute, Double defaultValue) throws PmmException {
		PmmXmlDoc miscXmlDoc = getMisc();
		if (miscXmlDoc != null) {
			MiscXml mx = null;
	    	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
	    		if (el instanceof MiscXml) {
	    			mx = (MiscXml) el;
	    			if (mx.getName().equalsIgnoreCase(attribute)) {
	    				return mx.getValue();
	    			}
	    		}
	    	}
		}
		return defaultValue;
	}
	
	public Double getTemperature() throws PmmException {
		return getMiscValue(AttributeUtilities.ATT_TEMPERATURE, null); // return Double.NaN; // getDouble( TimeSeriesSchema.ATT_TEMPERATURE )
	}

	public Double getPh() throws PmmException {
		return getMiscValue(AttributeUtilities.ATT_PH, null);// return Double.NaN; // getDouble( TimeSeriesSchema.ATT_PH )
	}
	
	public Double getWaterActivity() throws PmmException {
		return getMiscValue(AttributeUtilities.ATT_WATERACTIVITY, null);// return Double.NaN; getDouble( TimeSeriesSchema.ATT_WATERACTIVITY )
	}
	public PmmXmlDoc getMisc() throws PmmException {
		return getPmmXml(TimeSeriesSchema.ATT_MISC);
	}
	public PmmXmlDoc getMatrix() throws PmmException {
		return getPmmXml(TimeSeriesSchema.ATT_MATRIX);
	}
	public PmmXmlDoc getAgent() throws PmmException {
		return getPmmXml(TimeSeriesSchema.ATT_AGENT);
	}
	public PmmXmlDoc getLiterature() throws PmmException {
		return getPmmXml(TimeSeriesSchema.ATT_LITMD);
	}
	
	public PmmXmlDoc getMdInfo() {
		PmmXmlDoc mdInfoXmlDoc = null;
		try {
			mdInfoXmlDoc = this.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
		}
		catch (PmmException e) {
			e.printStackTrace();
		}
		return mdInfoXmlDoc;
	}
	public PmmXmlDoc getTimeSeries() {
		PmmXmlDoc timeSeriesXmlDoc = null;
		try {
			timeSeriesXmlDoc = this.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		}
		catch (PmmException e) {
			e.printStackTrace();
		}
		return timeSeriesXmlDoc;
	}
	
	public Integer getAgentId() throws PmmException {
		return (Integer) getAgentAttribute(true, false, false);
		//return getInt( TimeSeriesSchema.ATT_AGENTID );
	}
	
	public Integer getMatrixId() throws PmmException {
		return (Integer) getMatrixAttribute(true, false, false);
		//return getInt( TimeSeriesSchema.ATT_MATRIXID );
	}
	
	public String getAgentDetail() throws PmmException {
		return (String) getAgentAttribute(false, false, true);
		//return getString( TimeSeriesSchema.ATT_AGENTDETAIL );
	}
	
	public String getMatrixDetail() throws PmmException {
		return (String) getMatrixAttribute(false, false, true);
		//return getString( TimeSeriesSchema.ATT_MATRIXDETAIL );
	}
	
	public String getComment() throws PmmException {
		PmmXmlDoc mdInfoXmlDoc = getMdInfo();
		if (mdInfoXmlDoc != null) {
			MdInfoXml mdix = null;
	    	for (PmmXmlElementConvertable el : mdInfoXmlDoc.getElementSet()) {
	    		if (el instanceof MdInfoXml) {
	    			mdix = (MdInfoXml) el;
	    			return mdix.getComment();
	    		}
	    	}
		}
		return null;//getString( TimeSeriesSchema.ATT_COMMENT );
	}
	public String getWarning() {
		return warningMsg;
	}
	public void setWarning(String warningMsg) {
		this.warningMsg = warningMsg;
	}
	
	
	public Double getMaximumRate() { return mr; }
	
	public boolean hasCombaseId() throws PmmException {
		return !isNull( TimeSeriesSchema.ATT_COMBASEID );
	}
	
	public boolean hasValue(String attribute, boolean defaultValue) throws PmmException {
		PmmXmlDoc miscXmlDoc = getMisc();
		if (miscXmlDoc != null) {
			MiscXml mx = null;
	    	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
	    		if (el instanceof MiscXml) {
	    			mx = (MiscXml) el;
	    			if (mx.getName().equalsIgnoreCase(attribute)) {
	    				return true;
	    			}
	    		}
	    	}
		}
		return defaultValue;
	}
	
	public boolean hasTemperature() throws PmmException {
		return hasValue(AttributeUtilities.ATT_TEMPERATURE, false); // !isNull( TimeSeriesSchema.ATT_TEMPERATURE )
	}

	public boolean hasPh() throws PmmException {
		return hasValue(AttributeUtilities.ATT_PH, false); // !isNull( TimeSeriesSchema.ATT_PH )
	}
	
	public boolean hasWaterActivity() throws PmmException {
		return hasValue(AttributeUtilities.ATT_WATERACTIVITY, false); // !isNull( TimeSeriesSchema.ATT_WATERACTIVITY )
	}
	
	public boolean hasMisc() throws PmmException {
		return !isNull(TimeSeriesSchema.ATT_MISC);
	}
	public boolean hasMatrix() throws PmmException {
		return !isNull(TimeSeriesSchema.ATT_MATRIX);
	}
	public boolean hasAgent() throws PmmException {
		return !isNull(TimeSeriesSchema.ATT_AGENT);
	}
	
	public boolean hasCondId() throws PmmException {
		return getInt( TimeSeriesSchema.ATT_CONDID ) >= 0;
	}
	
	public void setCondId( final int condId ) throws PmmException { 
		setValue( TimeSeriesSchema.ATT_CONDID, condId );
	}
	
	public void setCombaseId( final String combaseId ) throws PmmException {
		setValue( TimeSeriesSchema.ATT_COMBASEID, combaseId );
	}
	
	private void setAgentAttribute(Integer id, String name, String detail) throws PmmException {
		PmmXmlDoc agentXmlDoc = getAgent();
		if (agentXmlDoc == null) agentXmlDoc = new PmmXmlDoc();
		AgentXml ax = null;
    	for (PmmXmlElementConvertable el : agentXmlDoc.getElementSet()) {
    		if (el instanceof AgentXml) {
    			ax = (AgentXml) el;
    			if (id != null) ax.setID(id);
    			if (name != null) ax.setName(name);
    			if (detail != null) ax.setDetail(detail);
    			break;
    		}
    	}
    	if (ax == null) ax = new AgentXml(id, name, detail);
    	agentXmlDoc = new PmmXmlDoc();
   		agentXmlDoc.add(ax);    		
    	setValue(TimeSeriesSchema.ATT_AGENT, agentXmlDoc);		
	}
	private void setMatrixAttribute(Integer id, String name, String detail) throws PmmException {
		PmmXmlDoc matrixXmlDoc = getMatrix();
		if (matrixXmlDoc == null) matrixXmlDoc = new PmmXmlDoc();
		MatrixXml mx = null;
    	for (PmmXmlElementConvertable el : matrixXmlDoc.getElementSet()) {
    		if (el instanceof MatrixXml) {
    			mx = (MatrixXml) el;
    			if (id != null) mx.setID(id);
    			if (name != null) mx.setName(name);
    			if (detail != null) mx.setDetail(detail);
    			break;
    		}
    	}
    	if (mx == null) mx = new MatrixXml(id, name, detail);
    	matrixXmlDoc = new PmmXmlDoc();
    	matrixXmlDoc.add(mx);    		
    	setValue(TimeSeriesSchema.ATT_MATRIX, matrixXmlDoc);		
	}
	
	public void setAgentName( final String agentName ) throws PmmException {
		//setValue( TimeSeriesSchema.ATT_AGENTNAME, agentName );		
		setAgentAttribute(null, agentName, null);
	}
	public void setMatrixName( final String matrixName ) throws PmmException {
		//setValue( TimeSeriesSchema.ATT_MATRIXNAME, matrixName );
		setMatrixAttribute(null, matrixName, null);
	}
	
	public void setAgentDetail( final String agentDetail ) throws PmmException {
		//setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agentDetail );
		setAgentAttribute(null, null, agentDetail);
	}
	public void setMatrixDetail( final String matrixDetail ) throws PmmException {
		//setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrixDetail );
		setMatrixAttribute(null, null, matrixDetail);
	}
	
	public void setAgentId( final Integer agentId ) throws PmmException {
		//setValue( TimeSeriesSchema.ATT_AGENTID, agentId );
		setAgentAttribute(agentId, null, null);
	}
	
	public void setMatrixId( final Integer matrixId ) throws PmmException {
		//setValue( TimeSeriesSchema.ATT_MATRIXID, matrixId );
		setMatrixAttribute(matrixId, null, null);
	}
	
	public void setMatrix( final int matrixId, final String matrixName, final String matrixDetail ) throws PmmException {
		setMatrixId( matrixId );
		setMatrixName( matrixName );
		setMatrixDetail( matrixDetail );
	}
	
	public void setAgent( final int agentId, final String agentName, final String agentDetail ) throws PmmException {
		setAgentId( agentId );
		setAgentName( agentName );
		setAgentDetail( agentDetail );
	}

	public void addMisc(int attrID, String attribute, String description, Double value, String unit) throws PmmException {
		PmmXmlDoc miscXmlDoc = getMisc();
		if (miscXmlDoc == null) miscXmlDoc = new PmmXmlDoc();
		MiscXml mx = null;
		boolean paramFound = false;
    	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
    		if (el instanceof MiscXml) {
    			mx = (MiscXml) el;
    			if (mx.getName().equalsIgnoreCase(attribute)) {
    				paramFound = true;
    				break;
    			}
    		}
    	}
    	if (paramFound) miscXmlDoc.remove(mx);
    	if (value != null && !value.isInfinite() && !value.isNaN()) {
    		mx = new MiscXml(attrID,attribute,description,value,unit);
    		miscXmlDoc.add(mx);    		
    	}
    	setValue(TimeSeriesSchema.ATT_MISC, miscXmlDoc);
	}
	public void addMiscs(final PmmXmlDoc misc) throws PmmException {
		PmmXmlDoc miscXmlDoc = getMisc();
		if (miscXmlDoc == null) {
			setValue(TimeSeriesSchema.ATT_MISC, misc);			
		}
		else {
			MiscXml mx = null;
	    	for (PmmXmlElementConvertable add1 : misc.getElementSet()) {
	    		if (add1 instanceof MiscXml) {
		    		MiscXml mx2Add = (MiscXml) add1;
					boolean paramFound = false;
			    	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
			    		if (el instanceof MiscXml) {
			    			mx = (MiscXml) el;
			    			if (mx.getName().equalsIgnoreCase(mx2Add.getName())) {
			    				paramFound = true;
			    				break;
			    			}
			    		}
			    	}
			    	if (paramFound) miscXmlDoc.remove(mx);
			    	miscXmlDoc.add(add1);    		
	    		}
	    	}
	    	setValue(TimeSeriesSchema.ATT_MISC, miscXmlDoc);
		}
	}
	
	public void setMdInfo(final PmmXmlDoc MdInfo) throws PmmException {
		setValue(TimeSeriesSchema.ATT_MDINFO, MdInfo);
	}
	public void setMdData(final PmmXmlDoc MdData) throws PmmException {
		setValue(TimeSeriesSchema.ATT_TIMESERIES, MdData);
	}
		
	public void setMaximumRate( final double mr ) { this.mr = mr; }
}
