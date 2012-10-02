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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

public class ParametricModel implements PmmXmlElementConvertable {
	// das Speicherverhalten von MMC sollte mal KNIME-like gemacht werden!!!! Wenn mal wieder Zeit ist!
	// jetzt werden die attribute zum xml abspeichern (die eben auch die attribute zum knime configure abspeichern sind)
	// hier fest verdrahtet und von den zentralen KnimSchema Variablen unabhängig gemacht.
	// Hintergrund ist, dass sonst bei KnimeSchema-Änderungen der MMC seine gespeicherten Daten vergisst!
	// Am Ende heisst das: XML erst mal wieder löschen!
	private static final String ATT_FORMULA = "Formula";
	private static final String ATT_PARAMNAME = "ParamName";
	private static final String ATT_DEPVAR = "DepVar";
	private static final String ATT_INDEPVAR = "IndepVar";
	private static final String ATT_VALUE = "Value";
	private static final String ATT_MODELNAME = "ModelName";
	private static final String ATT_MODELID = "ModelCatalogId";
	private static final String ATT_ESTMODELID = "EstModelId";
	private static final String ATT_RMS = "RMS";
	private static final String ATT_RSQUARED = "Rsquared";
	private static final String ATT_MINVALUE = "MinValue";
	private static final String ATT_MAXVALUE = "MaxValue";
	private static final String ATT_MININDEP = "MinIndep";
	private static final String ATT_MAXINDEP = "MaxIndep";
	private static final String ATT_PARAMERR = "StandardError";

	public static final String ELEMENT_PARAMETRICMODEL = "ParametricModel";
	private static final String ELEMENT_PARAM = "Parameter";
	private static final String ATT_CONDID = "CondId";

	
	private HashMap<String, Double> paramMin;
	private HashMap<String, Double> paramMax;
	private HashMap<String, Double> indepMin;
	private HashMap<String, Double> indepMax;
	private String modelName;
	private String formula;
	private Hashtable<String, Double> param;
	private Hashtable<String, Double> paramError;
	private int level;
	private LinkedList<String> indepVar;
	private String depVar;
	private LinkedList<LiteratureItem> estLit;
	private LinkedList<LiteratureItem> modelLit;
	private int modelId;
	private int estModelId;
	private Double rsquared;
	private Double rss;
	private Double rms;
	private Double aic;
	private int condId;
		
	private static final String ATT_LEVEL = "Level";
	
	protected ParametricModel() {
		
		
		rss = Double.NaN;
		rsquared = Double.NaN;
		rms = Double.NaN;
		aic = Double.NaN;
		condId = MathUtilities.getRandomNegativeInt();
		modelId = MathUtilities.getRandomNegativeInt();
		estModelId = MathUtilities.getRandomNegativeInt();
		
		param = new Hashtable<String, Double>();
		paramError = new Hashtable<String, Double>();
		indepVar = new LinkedList<String>();
		estLit = new LinkedList<LiteratureItem>();
		modelLit = new LinkedList<LiteratureItem>();
		
		paramMin = new HashMap<String, Double>();
		paramMax = new HashMap<String, Double>();
		indepMin = new HashMap<String, Double>();
		indepMax = new HashMap<String, Double>();
	}
	
	public ParametricModel(
		final String modelName, final String formula,
		final String depVar, final int level, final int modelId, final int estModelId ) {
		
		this( modelName, formula, depVar, level, modelId );
		
		this.estModelId = estModelId;
		
	}
	
	public ParametricModel(
		final String modelName, final String formula,
		final String depVar, final int level, final int modelId ) {
		
		this( modelName, formula, depVar, level );
				
		this.modelId = modelId;
	}
	
	
	public ParametricModel(
			final String modelName, final String formula,
			final String depVar, final int level ) {
		
		this();
			
		this.modelName = modelName;
		setFormula( formula );
		this.depVar = depVar;
		this.level = level;
	}
	
	public ParametricModel( final Element modelElement ) {
		
		this();
		
		LiteratureItem lit;
		
		modelName = modelElement.getAttributeValue( ATT_MODELNAME );
		level = Integer.valueOf( modelElement.getAttributeValue( ATT_LEVEL ) );
		param = new Hashtable<String, Double>();
		paramError = new Hashtable<String, Double>();
		indepVar = new LinkedList<String>();
		estLit = new LinkedList<LiteratureItem>();
		modelLit = new LinkedList<LiteratureItem>();
		modelId = Integer.valueOf( modelElement.getAttributeValue( ATT_MODELID ) );
		estModelId = Integer.valueOf( modelElement.getAttributeValue( ATT_ESTMODELID ) );
		rss = Double.valueOf( modelElement.getAttributeValue( ATT_RMS ) );
		rsquared = Double.valueOf( modelElement.getAttributeValue( ATT_RSQUARED ) );
		condId = Integer.valueOf( modelElement.getAttributeValue( ATT_CONDID ) );
		
		for( Element el : modelElement.getChildren() ) {
			
			//System.out.println( el.getName() );
			
			if( el.getName().equals( ATT_FORMULA ) ) {
				formula = el.getText();
				continue;
			}
			
			if( el.getName().equals( ELEMENT_PARAM ) ) {
				param.put( el.getAttributeValue( ATT_PARAMNAME ), Double.valueOf( el.getAttributeValue( ATT_VALUE ) ) );
				paramError.put( el.getAttributeValue( ATT_PARAMNAME ), Double.valueOf( el.getAttributeValue( ATT_PARAMERR ) ) );
				//System.out.println( el.getAttributeValue( ATT_MINVALUE ) );
				//System.out.println( el.getAttributeValue( ATT_MAXVALUE ) );
				boolean minNull = el.getAttributeValue(ATT_MINVALUE) == null || el.getAttributeValue(ATT_MINVALUE).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXVALUE) == null || el.getAttributeValue(ATT_MAXVALUE).equals("null");
				paramMin.put( el.getAttributeValue( ATT_PARAMNAME ), minNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MINVALUE ) ) );
				paramMax.put( el.getAttributeValue( ATT_PARAMNAME ), maxNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MAXVALUE ) ) );
				continue;
			}
			
			if( el.getName().equals( ATT_INDEPVAR ) ) {
				indepVar.add( el.getAttributeValue( ATT_PARAMNAME ) );
				boolean minNull = el.getAttributeValue(ATT_MININDEP) == null || el.getAttributeValue(ATT_MININDEP).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXINDEP) == null || el.getAttributeValue(ATT_MAXINDEP).equals("null");
				indepMin.put( el.getAttributeValue( ATT_PARAMNAME ), minNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MININDEP ) ) );
				indepMax.put( el.getAttributeValue( ATT_PARAMNAME ), maxNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MAXINDEP ) ) );
				continue;
			}
			
			if( el.getName().equals( ATT_DEPVAR ) ) {
				depVar = el.getAttributeValue( ATT_PARAMNAME );
				continue;
			}
			
			if( el.getName().equals( LiteratureItem.ELEMENT_LITERATURE ) ) {
				
				lit = new LiteratureItem( el );
				
				if( lit.getTag() == null || lit.getTag().equals( LiteratureItem.TAG_EM ) ) {
					estLit.add(new LiteratureItem(el) );
				} else {
					modelLit.add(new LiteratureItem(el) );
				}
				
				continue;
			}
			
			assert false;
		}
	}
	public ParametricModel clone() {
		ParametricModel clonedPM = new ParametricModel(modelName, formula, depVar, level, modelId, estModelId); 

		try {
			clonedPM.setRms(rms);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		try {
			clonedPM.setRss(rss);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		try {
			clonedPM.setRsquared(rsquared);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		clonedPM.setAic(aic);
		clonedPM.setCondId(condId);

		for (Map.Entry<String, Double> entry : param.entrySet()) {
		    String key = entry.getKey();
		    Double value = entry.getValue();
		    clonedPM.addParam(key, value, paramError.get(key), paramMin.get(key), paramMax.get(key));
		}
		for (String key : indepVar) {
		    clonedPM.addIndepVar(key, indepMin.get(key), indepMax.get(key));
		}

		for (LiteratureItem item : modelLit) {
			clonedPM.addModelLit(item);			
		}
		for (LiteratureItem item : estLit) {
			clonedPM.addEstModelLit(item);	
		}

		return clonedPM;
	}
	
	public void setRsquared( final Double r2 ) throws PmmException {
				
		if( r2 > 1 ) {
			throw new PmmException( "Rsquared must not exceed 1." );
		}
		
		rsquared = r2;
	}
	public void setRss( final Double rss ) throws PmmException {
		
		if( Double.isInfinite( rms ) ) {
			throw new PmmException( "RMS must be a real positive number." );
		}
		
		if( rms < 0 ) {
			throw new PmmException( "RMS must be a real positive number." );
		}
		
		this.rss = rss;
	}
	
	public void setCondId( final int condId ) { this.condId = condId; }
	
	public void addParam(final String paramName) {
		addParam(paramName, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	}
	public void addParam( final String paramName, final Double value, final Double error ) {
		addParam(paramName, value, error, Double.NaN, Double.NaN);
	}
	
	public void addParam( final String paramName, final Double value, final Double error, final Double min, final Double max ) {
		param.put( paramName, value );
		paramError.put( paramName, error );
		paramMin.put( paramName, min );
		paramMax.put( paramName, max );
	}
		
	public void setIndepMax( final String name, final Double max ) {
		indepMax.put( name, max );
	}
	public void setIndepMin( final String name, final Double min ) {
		indepMin.put( name, min );
	}
	public void setParamValue( final String name, final Double value ) {
		param.put( name, value );
	}	
	public void setParamMin( final String name, final Double min ) {
		paramMin.put( name, min );
	}	
	public void setParamMax( final String name, final Double max ) {
		paramMax.put( name, max );
	}
	
	public void setDepVar(final String depVar) {
		this.depVar = depVar;
	}
	public void setRms( final Double rms ) throws PmmException {
		
		if( Double.isInfinite( rms ) ) {
			throw new PmmException( "RMS must be a real positive number." );
		}
		
		if( rms < 0 ) {
			throw new PmmException( "RMS must be a real positive number." );
		}
		
		this.rms = rms;
	}
	public void setAic( final Double aic ) { this.aic = aic; }
	
	public void removeIndepVar( final String varName ) {
		indepVar.remove(varName);
	}
	public void removeParam( final String varName ) {
		param.remove(varName);
	}
	public void addIndepVar( final String varName ) {
		addIndepVar(varName, Double.NaN, Double.NaN);
	}
	
	public void addIndepVar( final String varName, final Double min, final Double max ) {
		indepVar.add( varName );
		indepMin.put( varName, min );
		indepMax.put( varName, max );
	}
	
	public void addEstModelLit( final String author, final int year ) {
		estLit.add( new LiteratureItem( author, year, LiteratureItem.TAG_EM ) );
	}
	
	public void addEstModelLit( final String author, final int year, final int id ) {
		estLit.add( new LiteratureItem( author, year, LiteratureItem.TAG_EM, id ) );
	}
	
	public void addEstModelLit( final LiteratureItem item ) {
		estLit.add( item );
	}
	
	public void addModelLit( final String author, final int year ) {
		modelLit.add( new LiteratureItem( author, year, LiteratureItem.TAG_M ) );
	}
	
	public void addModelLit( final String author, final int year, final int id ) {
		modelLit.add( new LiteratureItem( author, year, LiteratureItem.TAG_M, id ) );
	}
	
	public void addModelLit( final LiteratureItem item ) {
		modelLit.add( item );
	}
	
	public Double getParamMin( final String name ) {
		return paramMin.get( name );
	}
	
	public Double getParamMax( final String name ) {
		return paramMax.get( name );
	}
	
	public Double getIndepMin( final String name ) {
		return indepMin.get( name );
	}
	
	public Double getIndepMax( final String name ) {
		return indepMax.get( name );
	}
	
	public Set<String> getParamNameSet() {
		return param.keySet();
	}
	public SortedMap<String, Boolean> getAllParVars(){
		SortedMap<String, Boolean> result = new TreeMap<String, Boolean>();
		for (String key : getParamNameSet()) {
			result.put(key, false);
		}
		for (String key : getIndepVarSet()) {
			result.put(key, true);
		}
		return result;
	}
	public Double getParamValue( final String paramName ) {
		return param.get( paramName );
	}
	public Double getParamError( final String paramName ) {
		return paramError.get( paramName );
	}
	
	public void setFormula( final String formula ) {
		
		this.formula = formula.replaceAll( "~", "=" );
		this.formula = this.formula.replaceAll( "\\s", "" );
	}
	
	public String getFormula() {
		return formula;
	}
	
	public int getLevel() { return level; }
	public int getModelId() { return modelId; }
	public void setModelId(final int modelId) {this.modelId = modelId;}
	public void setModelName(final String modelName) {this.modelName = modelName;}
	public int getEstModelId() { return estModelId; }
	public void setEstModelId(final int estModelId) {this.estModelId = estModelId;}
	public int getCondId() { return condId; }
	public Double getRss() { return rss; }
	public Double getRsquared() { return rsquared; }
	public Double getRms() { return rms; }
	public Double getAic() { return aic; }
	public String getModelName() { return modelName; }
	public LinkedList<String> getIndepVarSet() { return indepVar; }
	
	public LinkedList<LiteratureItem> getEstModelLit() { return estLit; }
	public LinkedList<LiteratureItem> getModelLit() { return modelLit; }
	
	public String getDepVar() { return depVar; }
	
	@Override
	public Element toXmlElement() {
		
		Element modelElement, element;
		
		modelElement = new Element( ELEMENT_PARAMETRICMODEL );
		modelElement.setAttribute( ATT_MODELNAME, modelName );
		modelElement.setAttribute( ATT_LEVEL, String.valueOf( level ) );
		modelElement.setAttribute( ATT_MODELID, String.valueOf( modelId ) );
		modelElement.setAttribute( ATT_ESTMODELID, String.valueOf( estModelId ) );
		modelElement.setAttribute( ATT_CONDID, String.valueOf( condId ) );
		modelElement.setAttribute( ATT_RMS, String.valueOf( rss ) );
		modelElement.setAttribute( ATT_RSQUARED, String.valueOf( rsquared ) );
		
		element = new Element( ATT_FORMULA );
		element.addContent( formula );
		modelElement.addContent( element );
		
		element = new Element( ATT_DEPVAR );
		element.setAttribute( ATT_PARAMNAME, depVar );
		modelElement.addContent( element );
		
		for( String s : param.keySet() ) {
			
			element = new Element( ELEMENT_PARAM );
			element.setAttribute( ATT_PARAMNAME, s );
			element.setAttribute( ATT_VALUE, String.valueOf( param.get( s ) ) );
			element.setAttribute( ATT_PARAMERR, String.valueOf( paramError.get( s ) ) );
			element.setAttribute( ATT_MINVALUE, String.valueOf( paramMin.get( s ) ) );
			element.setAttribute( ATT_MAXVALUE, String.valueOf( paramMax.get( s ) ) );
			
			modelElement.addContent( element );
		}
		
		for( String s : indepVar ) {
			if (s != null) {
				element = new Element( ATT_INDEPVAR );
				element.setAttribute( ATT_PARAMNAME, s );
				element.setAttribute( ATT_MININDEP, String.valueOf( indepMin.get( s ) ) );
				element.setAttribute( ATT_MAXINDEP, String.valueOf( indepMax.get( s ) ) );
				
				modelElement.addContent( element );
			}
		}
		
		for( LiteratureItem item : modelLit ) {
			modelElement.addContent( item.toXmlElement() );
		}
		for( LiteratureItem item : estLit ) {
			modelElement.addContent( item.toXmlElement() );
		}
		
		return modelElement;
	}
	
	public boolean hasAic() { return !Double.isNaN( aic ); }
	public boolean hasRms() { return !Double.isNaN( rms ); }
	
	public KnimeTuple getKnimeTuple() throws PmmException {
		
		KnimeTuple tuple;
		
		if( level == 1 ) {
					
			tuple = new KnimeTuple( new Model1Schema() );
			
			tuple.setValue( Model1Schema.ATT_FORMULA, getFormula() );
			tuple.setValue( Model1Schema.ATT_DEPVAR, getDepVar() );
			tuple.setValue( Model1Schema.ATT_MODELNAME, getModelName() );
			tuple.setValue( Model1Schema.ATT_MODELID, getModelId() );
			tuple.setValue( Model1Schema.ATT_ESTMODELID, getEstModelId() );
			tuple.setValue( Model1Schema.ATT_RMS, getRms() );
			tuple.setValue( Model1Schema.ATT_RSQUARED, getRsquared() );
			
			for( String paramName : getParamNameSet() ) {
				tuple.addValue( Model1Schema.ATT_PARAMNAME, paramName );
				tuple.addValue( Model1Schema.ATT_VALUE, getParamValue( paramName ) );
				tuple.addValue( Model1Schema.ATT_PARAMERR, getParamError( paramName ) );
				tuple.addValue( Model1Schema.ATT_MINVALUE, getParamMin( paramName ) );
				tuple.addValue( Model1Schema.ATT_MAXVALUE, getParamMax( paramName ) );
			}		
			for( String indep : getIndepVarSet() ) {
				tuple.addValue( Model1Schema.ATT_INDEPVAR, indep );
				tuple.addValue( Model1Schema.ATT_MININDEP, getIndepMin( indep ) );
				tuple.addValue( Model1Schema.ATT_MAXINDEP, getIndepMax( indep ) );
			}
			for( LiteratureItem litItem : getEstModelLit() ) {
				tuple.addValue( Model1Schema.ATT_LITEM, litItem.toString() );
				tuple.addValue( Model1Schema.ATT_LITIDEM, litItem.getId() );
			}
			for( LiteratureItem litItem : getModelLit() ) {
				tuple.addValue( Model1Schema.ATT_LITM, litItem.toString() );
				tuple.addValue( Model1Schema.ATT_LITIDM, litItem.getId() );
			}
			
		}
		else {
			
			tuple = new KnimeTuple( new Model2Schema() );
			
			tuple.setValue( Model2Schema.ATT_FORMULA, getFormula() );
			tuple.setValue( Model2Schema.ATT_DEPVAR, getDepVar() );
			tuple.setValue( Model2Schema.ATT_MODELNAME, getModelName() );
			tuple.setValue( Model2Schema.ATT_MODELID, getModelId() );
			tuple.setValue( Model2Schema.ATT_ESTMODELID, getEstModelId() );
			tuple.setValue( Model2Schema.ATT_RMS, getRms() );
			tuple.setValue( Model2Schema.ATT_RSQUARED, getRsquared() );
			
			for( String paramName : getParamNameSet() ) {
				// temporär wird hier der Parameter mit ; getrennt, da die Parameter nicht korrekt abgelegt wurden für Sekundary Modelle im MMC
				//String var = paramName.substring(paramName.indexOf(";") + 1);
				tuple.addValue( Model2Schema.ATT_PARAMNAME, paramName ); // var
				//tuple.addValue( Model2Schema.ATT_PARAMNAME, paramName );
				tuple.addValue( Model2Schema.ATT_VALUE, getParamValue( paramName ) );
				tuple.addValue( Model2Schema.ATT_PARAMERR, getParamError( paramName ) );
				tuple.addValue( Model2Schema.ATT_MINVALUE, getParamMin( paramName ) );
				tuple.addValue( Model2Schema.ATT_MAXVALUE, getParamMax( paramName ) );
			}
			for( String indep : getIndepVarSet() ) {
				tuple.addValue( Model2Schema.ATT_INDEPVAR, indep );
				tuple.addValue( Model2Schema.ATT_MININDEP, getIndepMin( indep ) );
				tuple.addValue( Model2Schema.ATT_MAXINDEP, getIndepMax( indep ) );
			}
			for( LiteratureItem litItem : getEstModelLit() ) {
				tuple.addValue( Model2Schema.ATT_LITEM, litItem.toString() );
				tuple.addValue( Model2Schema.ATT_LITIDEM, litItem.getId() );
			}
			for( LiteratureItem litItem : getModelLit() ) {
				tuple.addValue( Model2Schema.ATT_LITM, litItem.toString() );
				tuple.addValue( Model2Schema.ATT_LITIDM, litItem.getId() );
			}
		}
		
		
		
		return tuple;
	}
}
