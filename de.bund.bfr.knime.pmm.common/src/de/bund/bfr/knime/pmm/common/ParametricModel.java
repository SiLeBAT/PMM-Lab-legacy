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
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

public class ParametricModel implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAMETRICMODEL = "ParametricModel";
	
	// hier fest verdrahtet und von den zentralen KnimSchema Variablen unabhängig gemacht.
	// Hintergrund ist, dass sonst bei KnimeSchema-Änderungen der MMC seine gespeicherten Daten vergisst!
	private static final String ATT_FORMULA = "Formula";
	private static final String ATT_INDEP = "IndependentXml";
	private static final String ATT_DEP = "DependentXml";
	private static final String ATT_PARAM = "ParameterXml";
	
	private static final String ATT_PARAMNAME = "ParamName";
	private static final String ATT_DEPVAR = "DepVar";
	private static final String ATT_INDEPVAR = "IndepVar";
	private static final String ATT_VALUE = "Value";
	private static final String ATT_MODELNAME = "ModelName";
	private static final String ATT_MODELID = "ModelCatalogId";
	private static final String ATT_ESTMODELID = "EstModelId";
	private static final String ATT_RMS = "RMS";
	private static final String ATT_RSS = "RSS";
	private static final String ATT_AIC = "AIC";
	private static final String ATT_BIC = "BIC";
	private static final String ATT_RSQUARED = "Rsquared";
	private static final String ATT_MINVALUE = "MinValue";
	private static final String ATT_MAXVALUE = "MaxValue";
	private static final String ATT_MININDEP = "MinIndep";
	private static final String ATT_MAXINDEP = "MaxIndep";
	private static final String ATT_PARAMERR = "StandardError";
	private static final String ATT_VARPARMAP = "VarParMap";

	private static final String ELEMENT_PARAM = "Parameter";
	private static final String ATT_CONDID = "CondId";

	private DepXml depXml = null;
	private PmmXmlDoc independent = null;
	private PmmXmlDoc parameter = null;
	private HashMap<String, String> varParMap;
	/*
	private HashMap<String, Double> paramMin;
	private HashMap<String, Double> paramMax;
	private HashMap<String, Double> indepMin;
	private HashMap<String, Double> indepMax;
	*/
	private String modelName;
	private String formula;
	/*
	private SortedMap<String, Double> param;
	private SortedSet<String> indepVar;
	private Comparator<String> stringComparator = new Comparator<String>() {
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };
    */
	//private Hashtable<String, Double> paramError;
	private int level;
	//private String depVar;
	private LinkedList<LiteratureItem> estLit;
	private LinkedList<LiteratureItem> modelLit;
	private int modelId;
	private int estModelId;
	private Double rsquared;
	private Double rss;
	private Double rms;
	private Double aic;
	private Double bic;
	private int condId;
		
	private static final String ATT_LEVEL = "Level";
	
	protected ParametricModel() {		
		rss = Double.NaN;
		rsquared = Double.NaN;
		rms = Double.NaN;
		aic = Double.NaN;
		bic = Double.NaN;
		condId = MathUtilities.getRandomNegativeInt();
		modelId = MathUtilities.getRandomNegativeInt();
		estModelId = MathUtilities.getRandomNegativeInt();
		
		//param = new TreeMap<String, Double>(stringComparator); // new Hashtable<String, Double>();
		//paramError = new Hashtable<String, Double>();
		//indepVar = new TreeSet<String>(stringComparator);
		estLit = new LinkedList<LiteratureItem>();
		modelLit = new LinkedList<LiteratureItem>();
		/*
		paramMin = new HashMap<String, Double>();
		paramMax = new HashMap<String, Double>();
		indepMin = new HashMap<String, Double>();
		indepMax = new HashMap<String, Double>();
		
		*/
		varParMap = new HashMap<String, String>();
		independent = new PmmXmlDoc();
		parameter = new PmmXmlDoc();
	}
	
	public ParametricModel(final String modelName, final String formula,
		final String depVar, final int level, final int modelId, final int estModelId ) {
		
		this(modelName, formula, depVar, level, modelId);
		
		this.estModelId = estModelId;
		
	}
	
	public ParametricModel(final String modelName, final String formula,
		final String depVar, final int level, final int modelId) {
		
		this(modelName, formula, depVar, level);
				
		this.modelId = modelId;
	}
	
	
	public ParametricModel(final String modelName, final String formula,
			final String depVar, final int level) {
		
		this();			
		this.modelName = modelName;
		setFormula( formula );
		//this.depVar = depVar;
		depXml = new DepXml(depVar);
		this.level = level;
	}
	
	public ParametricModel(final Element modelElement) {
		this();
		
		modelName = modelElement.getAttributeValue( ATT_MODELNAME );
		level = Integer.valueOf( modelElement.getAttributeValue( ATT_LEVEL ) );
		//param = new TreeMap<String, Double>(stringComparator); // new Hashtable<String, Double>();
		//paramError = new Hashtable<String, Double>();
		//indepVar = new TreeSet<String>(stringComparator);
		estLit = new LinkedList<LiteratureItem>();
		modelLit = new LinkedList<LiteratureItem>();
		modelId = Integer.valueOf( modelElement.getAttributeValue( ATT_MODELID ) );
		estModelId = Integer.valueOf( modelElement.getAttributeValue( ATT_ESTMODELID ) );
		if (modelElement.getAttributeValue(ATT_RSS) != null) rss = Double.valueOf( modelElement.getAttributeValue( ATT_RSS ) );
		rms = Double.valueOf( modelElement.getAttributeValue( ATT_RMS ) );
		if (modelElement.getAttributeValue(ATT_AIC) != null) aic = Double.valueOf( modelElement.getAttributeValue(ATT_AIC) );
		if (modelElement.getAttributeValue(ATT_BIC) != null) bic = Double.valueOf( modelElement.getAttributeValue(ATT_BIC) );
		rsquared = Double.valueOf( modelElement.getAttributeValue( ATT_RSQUARED ) );
		condId = Integer.valueOf( modelElement.getAttributeValue( ATT_CONDID ) );
		
		for (Element el : modelElement.getChildren()) {
			if (el.getName().equals(ATT_FORMULA)) {
				formula = el.getText();
			}
			else if (el.getName().equals(ELEMENT_PARAM)) {
				//param.put( el.getAttributeValue( ATT_PARAMNAME ), Double.valueOf( el.getAttributeValue( ATT_VALUE ) ) );
				//paramError.put( el.getAttributeValue( ATT_PARAMNAME ), Double.valueOf( el.getAttributeValue( ATT_PARAMERR ) ) );
				boolean minNull = el.getAttributeValue(ATT_MINVALUE) == null || el.getAttributeValue(ATT_MINVALUE).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXVALUE) == null || el.getAttributeValue(ATT_MAXVALUE).equals("null");
				boolean valNull = el.getAttributeValue(ATT_VALUE) == null || el.getAttributeValue(ATT_VALUE).equals("null");
				boolean errNull = el.getAttributeValue(ATT_PARAMERR) == null || el.getAttributeValue(ATT_PARAMERR).equals("null");
				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MINVALUE));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXVALUE));
				Double val = valNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_VALUE));
				Double err = errNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_PARAMERR));
				//paramMin.put( el.getAttributeValue( ATT_PARAMNAME ), minNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MINVALUE ) ) );
				//paramMax.put( el.getAttributeValue( ATT_PARAMNAME ), maxNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MAXVALUE ) ) );
				ParamXml px = new ParamXml(el.getAttributeValue(ATT_PARAMNAME),
						val,
						err, min, max, null, null);
				parameter.add(px);
			}
			else if (el.getName().equals(ATT_INDEPVAR)) {
				//indepVar.add( el.getAttributeValue( ATT_PARAMNAME ) );
				boolean minNull = el.getAttributeValue(ATT_MININDEP) == null || el.getAttributeValue(ATT_MININDEP).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXINDEP) == null || el.getAttributeValue(ATT_MAXINDEP).equals("null");
				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MININDEP));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXINDEP));
				//indepMin.put( el.getAttributeValue( ATT_PARAMNAME ), minNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MININDEP ) ) );
				//indepMax.put( el.getAttributeValue( ATT_PARAMNAME ), maxNull ? Double.NaN : Double.valueOf( el.getAttributeValue( ATT_MAXINDEP ) ) );
				IndepXml ix = new IndepXml(el.getAttributeValue(ATT_PARAMNAME), min, max);
				independent.add(ix);
			}
			else if (el.getName().equals(ATT_VARPARMAP)) {
				varParMap.put(el.getAttributeValue("NEW"), el.getAttributeValue("OLD"));
			}
			else if (el.getName().equals(ATT_DEPVAR)) {
				depXml = new DepXml(el.getAttributeValue(ATT_PARAMNAME));
				//depVar = el.getAttributeValue( ATT_PARAMNAME );
			}
			else if (el.getName().equals(LiteratureItem.ELEMENT_LITERATURE)) {				
				LiteratureItem lit = new LiteratureItem( el );
				
				if( lit.getTag() == null || lit.getTag().equals( LiteratureItem.TAG_EM ) ) {
					estLit.add(new LiteratureItem(el) );
				} else {
					modelLit.add(new LiteratureItem(el));
				}
			}
			else if (el.getName().equals(ATT_INDEP)) {
				try {
					independent = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_DEP)) {
				try {
					PmmXmlDoc dep = new PmmXmlDoc(el.getText());
					depXml = (DepXml) dep.get(0);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_PARAM)) {
				try {
					parameter = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				assert false;
			}
		}
		for (String name : varParMap.keySet()) {
			String origName = varParMap.get(name);
			if (depXml.getOrigName().equals(origName)) {
				depXml.setName(name);
			}
			else {
				for (PmmXmlElementConvertable el : parameter.getElementSet()) {
					if (el instanceof ParamXml) {
						ParamXml px = (ParamXml) el;
						if (px.getOrigName().equals(origName)) {
							px.setName(name);
							break;
						}
					}
				}
				for (PmmXmlElementConvertable el : independent.getElementSet()) {
					if (el instanceof IndepXml) {
						IndepXml ix = (IndepXml) el;
						if (ix.getOrigName().equals(origName)) {
							ix.setName(name);
							break;
						}
					}
				}
			}
		}
	}
	public void setDepXml(DepXml depXml) {this.depXml = depXml;}
	public void setIndependent(PmmXmlDoc independent) {this.independent = independent;}
	public void setParameter(PmmXmlDoc parameter) {this.parameter = parameter;}
	public DepXml getDepXml() {return depXml;}
	public PmmXmlDoc getIndependent() {return independent;}
	public PmmXmlDoc getParameter() {return parameter;}
	
	public ParametricModel clone() {
		ParametricModel clonedPM = new ParametricModel(modelName, formula, depXml.getName(), level, modelId, estModelId); 

		try {
			clonedPM.setRms(rms);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		clonedPM.setAic(aic);
		clonedPM.setBic(bic);
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
		clonedPM.setCondId(condId);

		clonedPM.setDepXml(new DepXml(depXml.toXmlElement()));
		try {
			clonedPM.setIndependent(new PmmXmlDoc(independent.toXmlString()));
			clonedPM.setParameter(new PmmXmlDoc(parameter.toXmlString()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		/*
		for (Map.Entry<String, Double> entry : param.entrySet()) {
		    String key = entry.getKey();
		    Double value = entry.getValue();
		    clonedPM.addParam(key, value, paramError.get(key), paramMin.get(key), paramMax.get(key));
		}
		for (String key : indepVar) {
		    clonedPM.addIndepVar(key, indepMin.get(key), indepMax.get(key));
		}
		for (String newDepVar : varParMap.keySet()) {
			if (newDepVar != null) {
				clonedPM.addVarParMap(newDepVar, varParMap.get(newDepVar));
			}
		}
*/

		for (LiteratureItem item : modelLit) {
			clonedPM.addModelLit(item);			
		}
		for (LiteratureItem item : estLit) {
			clonedPM.addEstModelLit(item);	
		}

		return clonedPM;
	}
	
	public void setRsquared( final Double r2 ) throws PmmException {				
		if (r2 == null) this.rsquared = Double.NaN;
		else {
			if( r2 > 1 ) {
				throw new PmmException( "Rsquared must not exceed 1." );
			}		
			rsquared = r2;
		}
	}
	public void setRss( final Double rss ) throws PmmException {		
		if (rss == null) this.rss = Double.NaN;
		else {
			if( Double.isInfinite( rss ) ) {
				throw new PmmException( "RSS must be a real positive number." );
			}
			
			if( rss < 0 ) {
				throw new PmmException( "RSS must be a real positive number." );
			}
			
			this.rss = rss;
		}
	}
	
	public void setCondId( final int condId ) { this.condId = condId; }
	
	public void addParam(final String paramName) {
		addParam(paramName, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	}
	public void addParam( final String paramName, final Double value, final Double error ) {
		addParam(paramName, value, error, Double.NaN, Double.NaN);
	}
	
	public void addParam( final String paramName, final Double value, final Double error, final Double min, final Double max ) {
		ParamXml px = new ParamXml(paramName, value, error, min, max, null, null);
		parameter.add(px);
		/*
		param.put( paramName, value );
		paramError.put( paramName, error );
		paramMin.put( paramName, min );
		paramMax.put( paramName, max );
		*/
	}
		
	public void setIndepMax(final String name, final Double max) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(name)) {
					ix.setMax(max);
					break;
				}
			}
		}
		//indepMax.put( name, max );
	}
	public void setIndepMin(final String name, final Double min) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(name)) {
					ix.setMin(min);
					break;
				}
			}
		}
		//indepMin.put( name, min );
	}
	public void setParamValue( final String name, final Double value ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setValue(value);
					break;
				}
			}
		}
		//param.put( name, value );
	}	
	public void setParamError( final String name, final Double error ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setError(error);
					break;
				}
			}
		}
		//paramError.put( name, value );
	}	
	public void setParamMin( final String name, final Double min ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setMin(min);
					break;
				}
			}
		}
		//paramMin.put( name, min );
	}	
	public void setParamMax( final String name, final Double max ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setMax(max);
					break;
				}
			}
		}
		//paramMax.put( name, max );
	}
	
	public void setDepVar(final String depVar) {
		depXml.setName(depVar);
		//this.depVar = depVar;
	}
	public void setRms( final Double rms ) throws PmmException {		
		if (rms == null) this.rms = Double.NaN;
		else {
			if( Double.isInfinite( rms ) ) {
				throw new PmmException( "RMS must be a real positive number." );
			}
			
			if( rms < 0 ) {
				throw new PmmException( "RMS must be a real positive number." );
			}
			
			this.rms = rms;
		}
	}
	public void setAic( final Double aic ) { this.aic = aic; }
	public void setBic( final Double bic ) { this.bic = bic; }
	
	public void removeIndepVar( final String varName ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(varName)) {
					independent.getElementSet().remove(el);
					break;
				}
			}
		}
		//if (indepVar.contains(varName)) indepVar.remove(varName);
	}
	public void removeParam( final String varName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(varName)) {
					parameter.getElementSet().remove(el);
					break;
				}
			}
		}
		//if (param.containsKey(varName)) param.remove(varName);
	}
	public void addIndepVar( final String varName ) {
		addIndepVar(varName, Double.NaN, Double.NaN);
	}
	
	public void addVarParMap(String newDepVar, String oldDepVar) {
		if (!newDepVar.equals(oldDepVar)) varParMap.put(newDepVar, oldDepVar);
		else if (varParMap.containsKey(newDepVar)) varParMap.remove(newDepVar);
	}
	public String getVarPar(String newDepVar) {
		return varParMap.get(newDepVar);
	}
	public void setVarParMap(HashMap<String, String> newVarParMap) {
		varParMap = newVarParMap;
	}
	public void addIndepVar( final String varName, final Double min, final Double max ) {
		IndepXml ix = new IndepXml(varName, min, max);
		independent.add(ix);
		/*
		indepVar.add( varName );
		indepMin.put( varName, min );
		indepMax.put( varName, max );
		*/
	}
	
	public void removeParams() {
		parameter = new PmmXmlDoc();
		//param.clear();
	}
	public void removeIndepVars() {
		independent = new PmmXmlDoc();
		//indepVar.clear();
	}
	public void removeEstModelLits() {
		estLit.clear();
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
	
	public void removeModelLits() {
		modelLit.clear();
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
	
	public Double getParamValue( final String paramName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getValue();
				}
			}
		}
		return null;//param.get( paramName );
	}
	public Double getParamError( final String paramName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getError();
				}
			}
		}
		return null;//paramError.get( paramName );
	}
	
	public Double getParamMin( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getMin();
				}
			}
		}
		return null;//paramMin.get( name );
	}
	
	public Double getParamMax( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getMax();
				}
			}
		}
		return null;//paramMax.get( name );
	}
	
	public boolean containsIndep( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsParam( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public Double getIndepMin( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(name)) {
					return ix.getMin();
				}
			}
		}
		return null;//indepMin.get( name );
	}

	public Double getIndepMax( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.getName().equals(name)) {
					return ix.getMax();
				}
			}
		}
		return null;//indepMax.get( name );
	}
	
	public SortedMap<String, Boolean> getAllParVars(){
		SortedMap<String, Boolean> result = new TreeMap<String, Boolean>();
		if (parameter != null && parameter.getElementSet() != null) {
			for (PmmXmlElementConvertable el : parameter.getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					result.put(px.getName(), false);
				}
			}			
		}
		if (independent != null && independent.getElementSet() != null) {
			for (PmmXmlElementConvertable el : independent.getElementSet()) {
				if (el instanceof IndepXml) {
					IndepXml ix = (IndepXml) el;
					result.put(ix.getName(), true);
				}
			}
		}
		return result;
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
	public Double getBic() { return bic; }
	public String getModelName() { return modelName; }
	//public SortedSet<String> getIndepVarSet() {return indepVar;}
	//public Set<String> getParamNameSet() {return param.keySet();}
	
	public LinkedList<LiteratureItem> getEstModelLit() { return estLit; }
	public LinkedList<LiteratureItem> getModelLit() { return modelLit; }
	
	public String getDepVar() {return depXml == null ? null : depXml.getName();}
	
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element( ELEMENT_PARAMETRICMODEL );
		modelElement.setAttribute( ATT_MODELNAME, modelName );
		modelElement.setAttribute( ATT_LEVEL, String.valueOf( level ) );
		modelElement.setAttribute( ATT_MODELID, String.valueOf( modelId ) );
		modelElement.setAttribute( ATT_ESTMODELID, String.valueOf( estModelId ) );
		modelElement.setAttribute( ATT_CONDID, String.valueOf( condId ) );
		modelElement.setAttribute( ATT_RSS, String.valueOf( rss ) );
		modelElement.setAttribute( ATT_RMS, String.valueOf( rms ) );
		modelElement.setAttribute( ATT_AIC, String.valueOf( aic ) );
		modelElement.setAttribute( ATT_BIC, String.valueOf( bic ) );
		
		modelElement.setAttribute( ATT_RSQUARED, String.valueOf( rsquared ) );
		
		Element element = new Element( ATT_FORMULA );
		element.addContent( formula );
		modelElement.addContent(element);
		
		element = new Element(ATT_PARAM);
		element.addContent(parameter.toXmlString());
		modelElement.addContent(element);
		
		element = new Element(ATT_INDEP);
		element.addContent(independent.toXmlString());
		modelElement.addContent(element);
		
		element = new Element(ATT_DEP);
		PmmXmlDoc pd = new PmmXmlDoc();
		pd.add(depXml);
		element.addContent(pd.toXmlString());
		modelElement.addContent(element);
		/*
		element = new Element( ATT_DEPVAR );
		element.setAttribute( ATT_PARAMNAME, depVar );
		modelElement.addContent( element );
		for (String s : getParamNameSet()) {			
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
		for (String newDepVar : varParMap.keySet()) {
			if (newDepVar != null) {
				element = new Element(ATT_VARPARMAP);
				element.setAttribute("NEW", newDepVar);
				element.setAttribute("OLD", varParMap.get(newDepVar));
				modelElement.addContent(element);
			}
		}
		*/
		for( LiteratureItem item : modelLit ) {
			modelElement.addContent( item.toXmlElement() );
		}
		for( LiteratureItem item : estLit ) {
			modelElement.addContent( item.toXmlElement() );
		}
		
		return modelElement;
	}
	
	public boolean hasAic() { return !Double.isNaN( aic ); }
	public boolean hasBic() { return !Double.isNaN( bic ); }
	public boolean hasRms() { return !Double.isNaN( rms ); }
	
	public KnimeTuple getKnimeTuple() throws PmmException {		
		KnimeTuple tuple;
		
		if( level == 1 ) {
					
			tuple = new KnimeTuple( new Model1Schema() );
			
			tuple.setValue( Model1Schema.ATT_FORMULA, getFormula() );
			//tuple.setValue( Model1Schema.ATT_DEPVAR, getDepVar() );
    		PmmXmlDoc depDoc = new PmmXmlDoc();
    		depDoc.add(new DepXml(getDepVar()));
    		tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
			tuple.setValue( Model1Schema.ATT_MODELNAME, getModelName() );
			tuple.setValue( Model1Schema.ATT_MODELID, getModelId() );
			tuple.setValue( Model1Schema.ATT_ESTMODELID, getEstModelId() );
			tuple.setValue( Model1Schema.ATT_RMS, getRms() );
			tuple.setValue( Model1Schema.ATT_AIC, getAic() );
			tuple.setValue( Model1Schema.ATT_BIC, getBic() );
			tuple.setValue( Model1Schema.ATT_RSQUARED, getRsquared() );
			/*
    		PmmXmlDoc paramDoc = new PmmXmlDoc();
    		for( String paramName : getParamNameSet() ) {
    			/*
				tuple.addValue(Model1Schema.ATT_PARAMNAME, paramName);
				tuple.addValue(Model1Schema.ATT_VALUE, getParamValue(paramName));
				tuple.addValue(Model1Schema.ATT_PARAMERR, getParamError(paramName));
				tuple.addValue(Model1Schema.ATT_MINVALUE, getParamMin(paramName));
				tuple.addValue(Model1Schema.ATT_MAXVALUE, getParamMax(paramName));
				*/
			/*
				ParamXml px = new ParamXml(paramName,getParamValue(paramName),getParamError(paramName),getParamMin(paramName),getParamMax(paramName),null,null);
				paramDoc.add(px);
			}		
		*/
    		tuple.setValue(Model1Schema.ATT_PARAMETER, parameter);
    		/*
    		PmmXmlDoc indepDoc = new PmmXmlDoc();
			for( String indep : getIndepVarSet() ) {
				/*
				tuple.addValue(Model1Schema.ATT_INDEPVAR, indep);
				tuple.addValue(Model1Schema.ATT_MININDEP, getIndepMin(indep));
				tuple.addValue(Model1Schema.ATT_MAXINDEP, getIndepMax(indep));
				*/
			/*
				IndepXml ix = new IndepXml(indep,getIndepMin(indep),getIndepMax(indep));
				indepDoc.add(ix);
			}
		*/
    		tuple.setValue(Model1Schema.ATT_INDEPENDENT, independent);
    		/*
			for (String newDepVar : varParMap.keySet()) {
				if (newDepVar != null) {
					//tuple.addMap(Model1Schema.ATT_VARPARMAP, newDepVar, varParMap.get(newDepVar));
					tuple.addValue(Model1Schema.ATT_VARPARMAP, newDepVar + "=" + varParMap.get(newDepVar));
				}
			}
    		*/
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
			//tuple.setValue( Model2Schema.ATT_DEPVAR, getDepVar() );
    		PmmXmlDoc depDoc = new PmmXmlDoc();
    		depDoc.add(new DepXml(getDepVar()));
    		tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
			tuple.setValue( Model2Schema.ATT_MODELNAME, getModelName() );
			tuple.setValue( Model2Schema.ATT_MODELID, getModelId() );
			tuple.setValue( Model2Schema.ATT_ESTMODELID, getEstModelId() );
			tuple.setValue( Model2Schema.ATT_RMS, getRms() );
			tuple.setValue( Model2Schema.ATT_AIC, getAic() );
			tuple.setValue( Model2Schema.ATT_BIC, getBic() );
			tuple.setValue( Model2Schema.ATT_RSQUARED, getRsquared() );
			/*
    		PmmXmlDoc paramDoc = new PmmXmlDoc();
    		for(String paramName : getParamNameSet()) {
    			/*
				tuple.addValue(Model2Schema.ATT_PARAMNAME, paramName);
				tuple.addValue(Model2Schema.ATT_VALUE, getParamValue(paramName));
				tuple.addValue(Model2Schema.ATT_PARAMERR, getParamError(paramName));
				tuple.addValue(Model2Schema.ATT_MINVALUE, getParamMin(paramName));
				tuple.addValue(Model2Schema.ATT_MAXVALUE, getParamMax(paramName));
				*/
			/*
				ParamXml px = new ParamXml(paramName,getParamValue(paramName),getParamError(paramName),getParamMin(paramName),getParamMax(paramName),null,null);
				paramDoc.add(px);
			}		
		*/
    		tuple.setValue(Model2Schema.ATT_PARAMETER, parameter);

			/*
    		PmmXmlDoc indepDoc = new PmmXmlDoc();
			for (String indep : getIndepVarSet()) {
				/*
				tuple.addValue(Model2Schema.ATT_INDEPVAR, indep);
				tuple.addValue(Model2Schema.ATT_MININDEP, getIndepMin(indep));
				tuple.addValue(Model2Schema.ATT_MAXINDEP, getIndepMax(indep));
				*/
			/*
				IndepXml ix = new IndepXml(indep,getIndepMin(indep),getIndepMax(indep));
				indepDoc.add(ix);
			}
				*/
    		tuple.setValue(Model2Schema.ATT_INDEPENDENT, independent);
    		/*
			for (String newDepVar : varParMap.keySet()) {
				if (newDepVar != null) {
					//tuple.addMap(Model2Schema.ATT_VARPARMAP, newDepVar, varParMap.get(newDepVar));
					tuple.addValue(Model2Schema.ATT_VARPARMAP, newDepVar + "=" + varParMap.get(newDepVar));
				}
			}
			*/
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
	
	public String toString() {
		return modelName;
	}
}
