package de.bund.bfr.knime.pmm.pmf2fsk;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.emfjson.jackson.module.EMFModule;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.reader.ReaderUtils;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.pmm.predictorview.PredictorViewNodeModel;
import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;
import de.bund.bfr.knime.pmm.predictorview.TableReader;
import de.bund.bfr.pmfml.file.PMFMetadataNode;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.ModelCategory;
import metadata.ModelMath;
import metadata.ModificationDate;
import metadata.Parameter;
import metadata.Product;
import metadata.Reference;
import metadata.Scope;
import metadata.impl.GeneralInformationImpl;
import metadata.Contact;
import metadata.DataBackground;


import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;

import org.knime.core.util.FileUtil;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
public class PMF2FSKNodeModel extends NodeModel {
	public int nr_model_name = 0, 
			nr_model_id = 1,
			nr_model_link = 2,
			nr_organism = 3,
			nr_organism_detail = 4,
			nr_environment = 5,
			nr_environment_details = 6,
			nr_model_creator = 7,
			nr_creator_family_name = 8,
			nr_creator_contact = 9,
			nr_model_reference_description = 10,
			nr_model_reference_description_link = 11,
			nr_model_created = 12, 
			nr_model_modified = 13,
			nr_model_rights = 14,
			nr_model_notes = 15,
			nr_model_curation_status = 16,
			nr_model_type = 17,
			nr_model_subject = 18,
			nr_model_foodprocess = 19,
			nr_model_dependent_variables = 20,
			nr_model_dependent_variables_units = 21,
			nr_model_dependent_variables_minimum = 22,
			nr_model_dependent_variables_maximum = 23,
			nr_model_independent_variables = 24,
			nr_model_independent_variables_units = 25,
			nr_model_independent_variables_minimum = 26,
			nr_model_independent_variables_maximum = 27,
			nr_has_Data = 28;

	public enum ResourceType {
        /** Model script */
        modelScript,

        /** Parameters script */
        parametersScript,

        /** Visualization script */
        visualizationScript,

        /** Model metadata encoded in fskml */
        metaData,

        /** Binary file with workspace */
        workspace
    }
	
	ObjectMapper OBJECT_MAPPER;
	 
	private final PMF2FSKNodeSettings settings = new PMF2FSKNodeSettings();
	// configuration keys
	  public static final String CFGKEY_FILE = "filename";
	  // defaults for persistent state
	  private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	  // persistent state
	  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	  private final boolean isPmfx;
	//private final WriterNodeSettings nodeSettings = new WriterNodeSettings();
	  /**
	   * Constructor for the node model.
	   */
	  public PMF2FSKNodeModel(final boolean isPmfx) {
	    // 1 input port and 1 output port
	    super(0, 0);
	   
	    OBJECT_MAPPER = EMFModule.setupDefaultMapper();
	    
	    this.isPmfx = isPmfx;
	  }


		private List<KnimeTuple> tuples;
		private Double previousConcValues;
		private Map<String,Double> convertedPreConcValues = new HashMap<String,Double>();
		
		private String previousConcUnit;
		
	
		private TableReader reader;
		private SettingsHelper set;
	

		private boolean defaultBehaviour;

		private List<String> warnings;
		private String model_function;
		
		//private List getMyParams(BufferedDataTable[] input)
		private List<Parameter> getMyParams(BufferedDataTable[] input)
		{
			
			try {
				set = new SettingsHelper();
				
				if(input.length > 1) {
					List<KnimeTuple> prePredictuples =  PredictorViewNodeModel.getTuplesData(input[1]);
					

					if(prePredictuples!=null) {
						for(KnimeTuple tuple : prePredictuples) {
							PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
							TimeSeriesXml timeSeriesXml = (TimeSeriesXml) mdData.get(mdData.size()-1);
							previousConcValues = timeSeriesXml.getConcentration();
							previousConcUnit = timeSeriesXml.getConcentrationUnit();
							
							//TEST
//							paramList+="\n"+previousConcValues;
//							paramList+="\n"+previousConcUnit;
							
							
						}
					}
					if(prePredictuples!=null && prePredictuples.size()>1) {
						if(warnings == null) { 
							warnings = new ArrayList<String>();
						}
						warnings.add("The previous predictor view provided "+prePredictuples.size() +" Models. Only the last one with the value "+previousConcValues+" is considered and here as initial concetration provided");
					}
				}
							tuples = PredictorViewNodeModel.getTuples(input[0]);
				// This code is added to check if formulas (which are being applied in the current Workflow) have somekind of Starting Parameters
				// if so this snippet of code will add it to the Set as NewConcentrationParameters which will affect the the creation of TableReader at line of TableReader creation
				// to Include the NewConcentrationParameters
				for(KnimeTuple tuple : tuples) {
					PmmXmlDoc pmmXmlDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
					List<PmmXmlElementConvertable> PmmXmlElementConvertableList =pmmXmlDoc.getElementSet(); 
					for(PmmXmlElementConvertable pxml:PmmXmlElementConvertableList) {
						if(((ParamXml)pxml).isStartParam()) {
							set.setSelectedIDs(new ArrayList<String>());
							set.getConcentrationParameters().put(((CatalogModelXml) tuple.getPmmXml(
									Model1Schema.ATT_MODELCATALOG).get(0)).getId()
									+ "", ((ParamXml)pxml).getName());
							//TEST
//							paramList+="\n"+((CatalogModelXml) tuple.getPmmXml(	Model1Schema.ATT_MODELCATALOG).get(0)).getId()	+ " "+ ((ParamXml)pxml).getName();
//							paramList+="\n"+set.getConcentrationParameters();
							set.setNewConcentrationParameters(set.getConcentrationParameters());
							
						}
					}
				}
				convertedPreConcValues.put(previousConcUnit, previousConcValues);
				reader = new TableReader(tuples, set.getConcentrationParameters(),
						set.getLagParameters(), defaultBehaviour);
				//TEST
//				paramList+= "\n"+set.getLagParameters();
				
		        
				
			} catch (ConvertException e) {
				
			}
			
			

			
			Map<String, List<Double>> paramsX = new LinkedHashMap<>();
			Map<String, Double> minValues = new LinkedHashMap<>();
			Map<String, Double> maxValues = new LinkedHashMap<>();
			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = reader.getUnits();
			
			List<Parameter> pList = new ArrayList<Parameter>();
			//Constant parameters (function parameters):
//			for(int i = 0; i < reader.getParameterData().size(); i++)
//			{
//				for(Map.Entry<String, Double> entry : reader.getParameterData().get(i).entrySet())
//				{
//					if(entry.getValue() != null)
//					{
//						Parameter p = MetadataFactory.eINSTANCE.createParameter();		
//						p.setParameterClassification(metadata.ParameterClassification.CONSTANT);	//Classification (Constant,Input,Output)
//						p.setParameterID(entry.getKey());											//Param ID
//						p.setParameterName(entry.getKey());											//Name
//						p.setParameterUnit("x");													//Unit
//						p.setParameterDataType(metadata.ParameterType.DOUBLE);						//DataType
//						p.setParameterValue(entry.getValue().toString());							//value
//						pList.add(p);
//					}
//						
//				}
//			}
			
			//function arguments (Input)
			
			
			
			for (Plotable plotable : reader.getPlotables().values()) {
			
				//model function
				model_function = plotable.getFunction().replace("=", "<-");
				String script = "eq <- function(";
				
				
//				p.setParameterID(reader.getConditions().get(i));
//				p.setParameterName(reader.getConditions().get(i));
//				p.setParameterUnit(reader.getConditionUnits().get(i).get(0));
//				p.setParameterDataType(metadata.ParameterType.DOUBLE);
//				p.setParameterValueMax(reader.getConditionMaxValues().get(i).get(0).toString());
//				p.setParameterValueMin(reader.getConditionMinValues().get(i).get(0).toString());
//				pList.add(p);

				
			
				//arguments(Intput)
				for(String param : plotable.getFunctionArguments().keySet())
				{
					//script:
					script+=param+",";
					
					//parameters
					Parameter arg = MetadataFactory.eINSTANCE.createParameter();
					if(param.contains(AttributeUtilities.TIME))
					{
						arg.setParameterClassification(metadata.ParameterClassification.OUTPUT);
						
					}
					else
					{
						arg.setParameterClassification(metadata.ParameterClassification.INPUT);
						arg.setParameterValue(plotable.getMinArguments().get(param).toString());	
					}
					arg.setParameterID(param);
					arg.setParameterName(plotable.getCategories().get(param).get(0));
					arg.setParameterUnit(plotable.getUnits().get(param));
					arg.setParameterDataType(metadata.ParameterType.DOUBLE);
					arg.setParameterValueMax(plotable.getMaxArguments().get(param).toString());
					arg.setParameterValueMin(plotable.getMinArguments().get(param).toString());
					
					pList.add(arg);
				}
				
				//script
				script = script.substring(0, script.length()-1);
				script +="){\n"
						+ model_function+"\n"
						+"return("+plotable.getFunctionValue()+");\n"
								+ "}\n"
						+ "library(ggplot2);\n"
						+ "ggplot(data.frame(x=c("
						+ plotable.getMinArguments().get(AttributeUtilities.TIME).toString() + "," + plotable.getMaxArguments().get(AttributeUtilities.TIME).toString()+")),aes(x))"
						+ " + stat_function(fun=eq";
				if(plotable.getFunctionArguments().size() < 2)
				{
					script += ")\n";
				}else
				{
					script += ", args=list(";
					for(String param : plotable.getFunctionArguments().keySet())
					{
						if(!param.contains(AttributeUtilities.TIME))
							script += param +",";//+"="+ plotable.getMinArguments().get(param) +",";
						
					}
					script = script.substring(0, script.length()-1);
					script += "))\n";
				}
				model_function = script;
				
				//output parameters
				Parameter p = MetadataFactory.eINSTANCE.createParameter();
				p.setParameterClassification(metadata.ParameterClassification.OUTPUT);
				p.setParameterID(plotable.getFunctionValue());
				p.setParameterName(plotable.getCategories().get(plotable.getFunctionValue()).get(0));
//				p.
				
				
				p.setParameterUnit(plotable.getUnits().get(plotable.getFunctionValue()));
				p.setParameterDataType(metadata.ParameterType.DOUBLE);
				pList.add(p);
				
				//function parameters (Constant)
				for(String param : plotable.getFunctionParameters().keySet())
				{
					Parameter arg = MetadataFactory.eINSTANCE.createParameter();
					arg.setParameterClassification(metadata.ParameterClassification.INPUT);
					arg.setParameterID(param);
					arg.setParameterName(param);
					arg.setParameterUnit("Unit");
					arg.setParameterDataType(metadata.ParameterType.DOUBLE);
					arg.setParameterValue(plotable.getFunctionParameters().get(param).toString());
					pList.add(arg);
				}
				
//				for (String param : plotable.getCategories().keySet()) {
//					if (param == plotable.getFunctionValue() ) {
//						continue;
//					}
//						
//					categories.get(param).addAll(
//							plotable.getCategories().get(param));
//
//				}
			}
			
//
//			for (Plotable plotable : reader.getPlotables().values()) {
//				for (String arg : plotable.getMinArguments().keySet()) {
//					Double oldMin = minValues.get(arg);
//					String unit = plotable.getUnits().get(arg);
//					Category cat = Categories.getCategoryByUnit(plotable.getUnits()
//							.get(arg));
//					Double newMin = null;
//
//					try {
//						newMin = cat.convert(plotable.getMinArguments().get(arg),
//								unit, units.get(arg));
//					} catch (ConvertException e) {
//						e.printStackTrace();
//					}
//
//					if (oldMin == null) {
//						minValues.put(arg, newMin);
//					} else if (newMin != null) {
//						minValues.put(arg, Math.min(newMin, oldMin));
//						//TEST
//						paramList+=" ; "+ newMin;
//					}
//				}
//
//				for (String arg : plotable.getMaxArguments().keySet()) {
//					Double oldMax = maxValues.get(arg);
//					String unit = plotable.getUnits().get(arg);
//					Category cat = Categories.getCategoryByUnit(plotable.getUnits()
//							.get(arg));
//					Double newMax = null;
//
//					try {
//						newMax = cat.convert(plotable.getMaxArguments().get(arg),
//								unit, units.get(arg));
//					} catch (ConvertException e) {
//						e.printStackTrace();
//					}
//
//					if (oldMax == null) {
//						maxValues.put(arg, newMax);
//					} else if (newMax != null) {
//						maxValues.put(arg, Math.max(newMax, oldMax));
//						paramList+= " ; "+ newMax;
//					}
//				}
//			}
//
//			for (String var : paramsX.keySet()) {
//				if (minValues.get(var) != null) {
//					paramsX.put(var, Arrays.asList(minValues.get(var)));
//				}
//			}
//
//			
//			String concentrationParameters = "";
//			for (String param : set.getConcentrationParameters().keySet()) {
//				concentrationParameters = set.getConcentrationParameters().get(param);
//				//TEST
//				paramList+="\n"+set.getConcentrationParameters().get(param);
//			}
//			
//			paramList+=" ; "+convertedPreConcValues.toString();
//			
//			paramList+=" ; "+previousConcUnit;
					
			//configPanel.setParameters(AttributeUtilities.CONCENTRATION, paramsX,minValues, maxValues, categories, units,AttributeUtilities.TIME);

//			if (set.getUnitX() != null) { 
//				
//				//TEST
//				paramList+=" ; "+set.getUnitX();
//			} else {
//				
//				paramList+=" ; "+units.get(AttributeUtilities.TIME);
//			}

//			if (set.getUnitY() != null) {
//				paramList+=" ; "+set.getUnitY();
//			} else {
//				paramList+=" ; "+units.get(AttributeUtilities.CONCENTRATION);
//			}

//			if (!set.getParamXValues().isEmpty()) {
//				//configPanel.setParamXValues(set.getParamXValues());
//			}

//			paramList+= System.lineSeparator()+set.isManualRange();
//			paramList+=" ; "+set.getMinX();
//			paramList+=" ; "+set.getMaxX();
//			paramList+=" ; "+set.getMinY();
//			paramList+=" ; "+set.getMaxY();
//			paramList+=" ; "+set.getTransformX();
//			paramList+=" ; "+set.getTransformY();
//			
		
//			for(String visible : reader.getDoubleColumns().keySet()){
//				if(visible.equals("RMSE")){
//					List<Double> obj = reader.getDoubleColumns().remove(visible);
//					reader.getDoubleColumns().put(visible+"("+configPanel.getUnitY()+")", obj);
//					break;
//				}
//			}
			
//					
//			for(String myId : reader.getIds())
//			{
//				paramList+=" ; "+myId;
//			}
					 
			
//			reader.getStringColumns(); 
//			
//			for(Map.Entry<String, List<String>> entry : reader.getStringColumns().entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue().get(0);
//			}
//			reader.getDoubleColumns();
//			for(Map.Entry<String, List<Double>> entry : reader.getDoubleColumns().entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue().get(0);
//			}
//			reader.getConditions(); reader.getConditionValues();
//			reader.getConditionMinValues(); reader.getConditionMaxValues();
//			reader.getConditionUnits(); 
//			reader.getFilterableStringColumns(); 
//			reader.getParameterData(); 
//			reader.getFormulas();
//	
//			reader.getVariableData();
//			for(Map.Entry<String, String> entry : reader.getVariableData().get(0).entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue();
//			}
//			
//			for(Map.Entry<String, String> entry : reader.getVariableData().get(1).entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue();
//			}
//			
//			for(Map.Entry<String, String> entry : reader.getVariableData().get(2).entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue();
//			}
//			for(Map.Entry<String, String> entry : reader.getVariableData().get(3).entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue();
//			}
		
			//reader.getParameterData(); 
//			for(Map.Entry<String, Double> entry : reader.getParameterData().entrySet())
//			{
//				paramList+=" ; "+entry.getKey() + ":" + entry.getValue();
//			}
//			

			
			//samplePanel = new ChartSamplePanel();
			//samplePanel.setTimeValues(set.getTimeValues());
			//samplePanel.setInverse(set.isSampleInverse());
			

			//selectionPanel.setSelectedIDs(set.getSelectedIDs());

			
			
			
			
			
			
			//List<Parameter> paramList = new ArrayList<Parameter>();
			//Parameter p = MetadataFactory.eINSTANCE.createParameter();
			
			//paramList.add(p);
			return pList;

		}
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
	      final ExecutionContext exec) throws Exception {
	
		  BufferedDataTable[] tables = null;
		  tables = loadPMF(exec);
		  
//		    KnimeSchema schema = null;
//			ModelType modelType = null;
//			List<KnimeTuple> tuples;
//
//			DataTableSpec spec = inData[0].getSpec();
//			// Table has the structure Model1 + Model2 + Data
//			if (SchemaFactory.conformsM12DataSchema(spec)) {
//				schema = SchemaFactory.createM12DataSchema();
//				tuples = PmmUtilities.getTuples(inData[0], schema);
//				if (hasData(tuples)) {
//					boolean identical = identicalEstModels(tuples);
//					
//					if (identical) {
//						modelType = ModelType.ONE_STEP_TERTIARY_MODEL;
//					} else {
//						modelType = ModelType.TWO_STEP_TERTIARY_MODEL;
//					}
//					
//				} else {
//					modelType = ModelType.MANUAL_TERTIARY_MODEL;
//				}
//			}
//
//			// Table has Model1 + Data
//			else if (SchemaFactory.conformsM1DataSchema(spec)) {
//				schema = SchemaFactory.createM1DataSchema();
//				tuples = PmmUtilities.getTuples(inData[0], schema);
//
//				// Check every tuple. If any tuple has data (number of data points >
//				// 0) then assigns PRIMARY_MODEL_WDATA. Otherwise it assigns
//				// PRIMARY_MODEL_WODATA
//				modelType = ModelType.PRIMARY_MODEL_WODATA;
//				for (KnimeTuple tuple : tuples) {
//					PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
//					if (mdData.size() > 0) {
//						modelType = ModelType.PRIMARY_MODEL_WDATA;
//						break;
//					}
//				}
//			}
//
//			// Table only has data
//			else if (SchemaFactory.conformsDataSchema(spec)) {
//				schema = SchemaFactory.createDataSchema();
//				tuples = PmmUtilities.getTuples(inData[0], schema);
//				modelType = ModelType.EXPERIMENTAL_DATA;
//			}
//
//			// Table only has secondary model cells
//			else if (SchemaFactory.conformsM2Schema(spec)) {
//				schema = SchemaFactory.createM2Schema();
//				tuples = PmmUtilities.getTuples(inData[0], schema);
//				modelType = ModelType.MANUAL_SECONDARY_MODEL;
//			} else {
//				throw new Exception();
//			}
//
//			
//			
//			BufferedDataContainer container = exec
//					.createDataContainer(schema.createSpec());
//			
//			KnimeTuple mytuple = tuples.get(1);
//			
//			for(int i = 0; i < mytuple.getNumCells();i++)
//			{
//				
//				DataCell cell = mytuple.getCell(i);
//				
//				mytuple.setCell("NEU"+i, new StringCell("Hallo Welt"));
//				
////				Set<String> indepSet = new LinkedHashSet<>();
////
////				for (KnimeTuple tuple : tuples) {
////					PmmXmlDoc indep = tuple.getPmmXml();
////
////					for (PmmXmlElementConvertable el : indep.getElementSet()) {
////						IndepXml element = (IndepXml) el;
////
////						indepSet.add(element.getName());
////					}
////				}
//				
//			}
//			//container.addRowToTable(mytuple);
//			container.addRowToTable(tuples.get(1));
//			
//
//			container.close();
//
//		    
//		    			
//			// Check for existing file -> shows warning if despite overwrite being
//			// false the user still executes the nod
//
//			//WriterUtils.write(tuples, isPmfx, dir, mdName, metadata, settings.splitModels, modelNotes, exec, modelType);
//
//			return new BufferedDataTable[] {container.getTable()};
	        
	        
		    
			BufferedDataTable table = tables[0];//inData[0];
			
			
			BufferedDataTable table2 = tables[1];//inData[1];
			List<String> metaInfos = new ArrayList<String>();
			for(DataRow row : table2)
			{
				
				for(int i = 0; i < row.getNumCells(); i++)
				{
					metaInfos.add(row.getCell(i).toString());
				}
				
			}
			
        	KnimeSchema schema = null;
			List<KnimeTuple> tuples;
			DataTableSpec spec = tables[0].getSpec();
			if (SchemaFactory.conformsM2Schema(spec)) {
				
				schema = SchemaFactory.createM2Schema();
				tuples = PmmUtilities.getTuples(tables[0], schema);

				for (KnimeTuple tuple : tuples) {
				
					try {
						
						PmmXmlDoc my_xml = (PmmXmlDoc) tuple.getPmmXml(Model2Schema.ATT_EMLIT);
					
						LiteratureItem myLit = (LiteratureItem) my_xml.get(0);
						metaInfos.set(this.nr_model_creator, myLit.getAuthor());
						metaInfos.set(this.nr_model_reference_description, myLit.getTitle());
						
						// MODEL NAME
						//Organism | Environment | GROWTH Model | Creator | Gropin Database ROW
						
						String row_name = filename.getStringValue();
						metaInfos.set(this.nr_environment, metaInfos.get(this.nr_environment).split("_", 2)[0]);
						row_name = row_name.substring(row_name.lastIndexOf("Row"),	row_name.lastIndexOf('.'));
						metaInfos.set(this.nr_model_name, metaInfos.get(this.nr_organism) + 
								" | "+ metaInfos.get(this.nr_environment) +
								" | "+ metaInfos.get(this.nr_model_subject) + " Model"+
								" | "+ metaInfos.get(this.nr_model_creator) + 
								" | "+ "Gropin Database " + row_name );
						
						
						// MODEL ID
						// Gropin Database Row
						metaInfos.set(this.nr_model_id, "Gropin Database " + row_name);
					}catch(Exception e)
					{
					}
				}
			}

			
			
			
			GeneralInformation myGI = MetadataFactory.eINSTANCE.createGeneralInformation();
			Scope myScp = MetadataFactory.eINSTANCE.createScope();
			DataBackground myDBg = MetadataFactory.eINSTANCE.createDataBackground();
			ModelMath myMM = MetadataFactory.eINSTANCE.createModelMath();
			
		
			
			
// GENERAL INFORMATION
			
			myGI.setSoftware("PMMlab");
			myGI.setName(metaInfos.get(this.nr_model_name));
			myGI.setIdentifier(metaInfos.get(this.nr_model_id));
			try {
				String date = metaInfos.get(this.nr_model_creator);
				date = date.substring( date.lastIndexOf("., ")+3 )  ;
				myGI.setCreationDate(Date.valueOf(date+"-01-01"));
			}catch(Exception e){
				myGI.setCreationDate(Date.valueOf(LocalDate.now()));
			}
			
			
			myGI.setRights(metaInfos.get(this.nr_model_rights));
			myGI.setStatus(metaInfos.get(this.nr_model_curation_status));
			myGI.setObjective(metaInfos.get(this.nr_model_subject));
// -- CONTACT
// -- -- CREATORS
			
			
			
			
// //GI		
			Hazard myHaz = MetadataFactory.eINSTANCE.createHazard();
			Product myProd = MetadataFactory.eINSTANCE.createProduct();
			Contact myCreator = MetadataFactory.eINSTANCE.createContact();
			Reference myRef = MetadataFactory.eINSTANCE.createReference();
			ModificationDate myModDate= MetadataFactory.eINSTANCE.createModificationDate();
			ModelCategory myMCat = MetadataFactory.eINSTANCE.createModelCategory();
			
			List<Parameter> paramList = new ArrayList<Parameter>();
			///paramList = getMyParams(tables);
			
			myHaz.setHazardName(metaInfos.get(this.nr_organism));
			myHaz.setHazardDescription(metaInfos.get(this.nr_organism_detail));
			
			
			
			myProd.setProductName(metaInfos.get(this.nr_environment) );
			myProd.setProductDescription(metaInfos.get(this.nr_environment_details));
			
			myRef.setPublicationTitle(metaInfos.get(this.nr_model_reference_description));
			myRef.setPublicationWebsite(metaInfos.get(this.nr_model_reference_description_link));
			myRef.setIsReferenceDescription(false);
			myMCat.setModelClass(metaInfos.get(this.nr_model_type));
			
//			myParamDep.setParameterName(metaInfos.get(this.nr_model_dependent_variables));
//			myParamDep.setParameterUnit(metaInfos.get(this.nr_model_dependent_variables_units));
//			metaInfos.set( this.nr_model_dependent_variables_minimum, (metaInfos.get(this.nr_model_dependent_variables_minimum) == "?") ? "0" : metaInfos.get(this.nr_model_dependent_variables_minimum));
//			metaInfos.set( this.nr_model_dependent_variables_maximum, (metaInfos.get(this.nr_model_dependent_variables_maximum) == "?") ? "0" : metaInfos.get(this.nr_model_dependent_variables_maximum));
//			myParamDep.setParameterValueMin(metaInfos.get(this.nr_model_dependent_variables_minimum));
//			myParamDep.setParameterValueMax(metaInfos.get(this.nr_model_dependent_variables_maximum));
//			
//			myParamInDep.setParameterName(metaInfos.get(this.nr_model_independent_variables));
//			myParamInDep.setParameterUnit(metaInfos.get(this.nr_model_independent_variables_units));
//			myParamInDep.setParameterValueMin(metaInfos.get(this.nr_model_independent_variables_minimum));
//			myParamInDep.setParameterValueMax(metaInfos.get(this.nr_model_independent_variables_maximum));
			
			
			Contact myAuthor = MetadataFactory.eINSTANCE.createContact();
			myAuthor.setGivenName(metaInfos.get(this.nr_model_creator));
			myAuthor.setEmail(metaInfos.get(this.nr_creator_contact));
			myAuthor.setFamilyName(metaInfos.get(this.nr_creator_family_name));
			
			myGI.setAuthor(myAuthor);
			myGI.getReference().add(myRef);
			myGI.getModelCategory().add(myMCat);
			myGI.setAvailable(true);
			myScp.getHazard().add(myHaz);
			myScp.getProduct().add(myProd);
			
			//PARAMETERS:
			List<Parameter> myParams = getMyParams(tables);
			
			myMM.getParameter().addAll(myParams);
			
//			myMM.getParameter().add(myParamDep);
//			myMM.getParameter().add(myParamInDep);
			
			
			ObjectMapper myMapper = OBJECT_MAPPER;
			ObjectNode myON = myMapper.createObjectNode();
			
			myON.set("version",myMapper.valueToTree(MetadataPackage.eNS_URI));
			myON.set("generalInformation", myMapper.valueToTree(myGI));
			
			
			myON.set("scope", myMapper.valueToTree(myScp));
			
			 
			
			myON.set("dataBackground", myMapper.valueToTree(myDBg));
			myON.set("modelMath", myMapper.valueToTree(myMM));
			
			/*
			 * dependent variable (Y Axis): Value
			 * independet (X axis) time -> max (=60h)
			 *  
			 * independent sec: variables user changes: 
			 */
			
			String outPath = filename.getStringValue() + ".fskx";


		    //final File archiveFile =  FileUtil.getFileFromURL(FileUtil.toURL("D:\\test_out.fskx"));//(nodeSettings.filePath));
			final File archiveFile =  FileUtil.getFileFromURL(FileUtil.toURL(outPath));
		    archiveFile.delete();

		    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

		      // Add version
		      
		        DefaultJDOMFactory factory = new DefaultJDOMFactory();
		        Namespace dcTermsNamespace = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

		        Element conformsToNode = factory.element("conformsTo", dcTermsNamespace);
		        conformsToNode.setText("2.0");

		        Element element = factory.element("element");
		        element.addContent(conformsToNode);

		        MetaDataObject metaDataObject = new DefaultMetaDataObject(element);
		        archive.addDescription(metaDataObject);
		        File file;

		        
		      Element element_child,element_parent;  
		      // Adds model script
		      file = File.createTempFile("temp", ".r");
		      
		      
		      
		      
			  final ArchiveEntry modelEntry = archive.addEntry(file, "model.r", new URI("http://purl.org/NET/mediatypes/application/r"));
			  file.delete();
			  element_child = factory.element("type", Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/"));
			  element_child.setText(ResourceType.modelScript.name());
		      element_parent = factory.element("element");
		      element_parent.addContent(element_child);
			  modelEntry.addDescription(new DefaultMetaDataObject(element_parent));

			  
//			  file = File.createTempFile("temp", ".r");
//			  final ArchiveEntry paramEntry = archive.addEntry(file, "param.r", new URI("http://purl.org/NET/mediatypes/application/r"));
//			  file.delete();
//			  element_child = factory.element("type", Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/"));
//			  element_child.setText(ResourceType.parametersScript.name());
//		      element_parent = factory.element("element");
//		      element_parent.addContent(element_child);
//		      paramEntry.addDescription(new DefaultMetaDataObject(element_parent));
			  
			  
			  file = File.createTempFile("temp", ".r");
			 
			  FileUtils.writeStringToFile(file, model_function, "UTF-8");
			  final ArchiveEntry visEntry = archive.addEntry(file, "visualization.r", new URI("http://purl.org/NET/mediatypes/application/r"));
			  file.delete();
			  element_child = factory.element("type", Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/"));
			  element_child.setText(ResourceType.visualizationScript.name());
		      element_parent = factory.element("element");
		      element_parent.addContent(element_child);
		      visEntry.addDescription(new DefaultMetaDataObject(element_parent));
			  
			  
			  file = File.createTempFile("temp", ".r");
			  final ArchiveEntry workEntry = archive.addEntry(file, "workspace.r", new URI("http://purl.org/NET/mediatypes/application/r"));
			  file.delete();
			  element_child = factory.element("type", Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/"));
			  element_child.setText(ResourceType.workspace.name());
		      element_parent = factory.element("element");
		      element_parent.addContent(element_child);
		      workEntry.addDescription(new DefaultMetaDataObject(element_parent));
//			  
			  
			  
			  
		      // add original pmfx file
		      Path outpath = Paths.get(filename.getStringValue());
		      
		      
		      archive.addEntry(KnimeUtils.getFile(filename.getStringValue()), outpath.getFileName().toString(), new URI("http://purl.org/NET/mediatypes/application/x-pmf"));
		      


		      // Adds model metadata
		      
		      file = File.createTempFile("temp", ".json");
			   
		      myMapper.writeValue(file, myON);

			  archive.addEntry(file, "metaData.json", new URI("https://www.iana.org/assignments/media-types/application/json"));
			  file.delete();
             
	     
		      // Add simulations
		      {
	    	 	
	           
		       File tempFile = FileUtil.createTempFile("sim", "");
		       //SEDMLDocument sedmlDoc = createSedml(myParams);
		       //sedmlDoc.writeDocument(tempFile);
		       FileWriter fileWriter = new FileWriter(tempFile);
		       PrintWriter printWriter = new PrintWriter(fileWriter);
		       printWriter.print(getSEDML_Text(myParams));
		       
		       printWriter.close();
		    	  
		    	
		        archive.addEntry(tempFile, "sim.sedml", new URI("http://identifiers.org/combine.specifications/sed-ml"));
		        tempFile.delete();
		      }
              
		      
		     
		      
		      
		      
		      
		      archive.pack();
		    } catch (Exception e) {
		      FileUtils.deleteQuietly(archiveFile);
		      
		    }
			
			
			return new BufferedDataTable[] { };
	    
	  }
      
	  
	  private String getSEDML_Text(List<Parameter> pList)
	  {
		  		  String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
		  		"<sedML xmlns=\"http://sed-ml.org/\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" level=\"1\" version=\"1\">\r\n" + 
		  		"  <!--This file was generated by jlibsedml, version 2.2.3.-->\r\n" + 
		  		"  <listOfSimulations>\r\n" + 
		  		"    <steadyState id=\"steadyState\" name=\"\">\r\n" + 
		  		"      <annotation>\r\n" + 
		  		"        <sourceScript language=\"https://iana.org/assignments/mediatypes/text/x-r\" src=\"./param.r\" />\r\n" + 
		  		"      </annotation>\r\n" + 
		  		"      <algorithm kisaoID=\" \" />\r\n" + 
		  		"    </steadyState>\r\n" + 
		  		"  </listOfSimulations>\r\n" + 
		  		"  <listOfModels>\r\n" + 
		  		"    <model id=\"defaultSimulation\" name=\"\" language=\"https://iana.org/assignments/mediatypes/text/x-r\" source=\"./model.r\">\r\n" + 
		  		"      <listOfChanges>\r\n";
		  		  
		  		  
		  		for(Parameter p : pList)
		  		{
		  			if(p.getParameterClassification() != metadata.ParameterClassification.OUTPUT)
		  			{
		  				str+="        <changeAttribute newValue=\""+ p.getParameterValue()+"\" target=\""+p.getParameterID()+"\" />\r\n";
		  			}
		  		}
		  		  
		  		  
		  		 
		  		str+="      </listOfChanges>\r\n" + 
		  		"    </model>\r\n" + 
		  		"  </listOfModels>\r\n" + 
		  		"  <listOfTasks>\r\n" + 
		  		"    <task id=\"task0\" name=\"\" modelReference=\"defaultSimulation\" simulationReference=\"steadyState\" />\r\n" + 
		  		"  </listOfTasks>\r\n" + 
		  		"  <listOfDataGenerators>\r\n";
		  		for(Parameter p : pList)
		  		{
		  			if(p.getParameterClassification() != metadata.ParameterClassification.OUTPUT)
		  			{
		  				str+="    <dataGenerator id=\""+p.getParameterID()+"\" name=\"\">\r\n" + 
		  				  		"      <math:math>\r\n" + 
		  				  		"        <math:ci>"+p.getParameterID()+"</math:ci>\r\n" + 
		  				  		"      </math:math>\r\n" + 
		  				  		"    </dataGenerator>\r\n";
		  			}
		  		}
		  		
		  		
		  		str+="  </listOfDataGenerators>\r\n" + 
		  		"  <listOfOutputs>\r\n" + 
		  		"    <plot2D id=\"plot1\" name=\"\">\r\n" + 
		  		"      <annotation>\r\n" + 
		  		"        <sourceScript language=\"https://iana.org/assignments/mediatypes/text/x-r\" src=\"./visualization.r\" />\r\n" + 
		  		"      </annotation>\r\n" + 
		  		"    </plot2D>\r\n" + 
		  		"  </listOfOutputs>\r\n" + 
		  		"</sedML>\r\n" + 
		  		"";
		  
		  
		  return str;
	  }
	  
	  
	  
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  protected void reset() {}

	  /**
	   * 
	   * {@inheritDoc}
	   */
	  @Override
	  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
		      throws InvalidSettingsException {
		    return new DataTableSpec[] {null, null};
		  }

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  protected void saveSettingsTo(final NodeSettingsWO settings) {
		    filename.saveSettingsTo(settings);
		  }

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
		      throws InvalidSettingsException {
		    filename.loadSettingsFrom(settings);
		  }

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		    // TODO check if the settings could be applied to our model
		    // e.g. if the count is in a certain range (which is ensured by the
		    // SettingsModel).
		    // Do not actually set any values of any member variables.
		    filename.validateSettings(settings);
		  }

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
		      throws IOException, CanceledExecutionException {}

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
		      throws IOException, CanceledExecutionException {}

		  // Load PMF file
		  private BufferedDataTable[] loadPMF(final ExecutionContext exec) throws Exception {
		    // Get model type from annotation in the metadata file

		    // a) Open archive
		    File file = KnimeUtils.getFile(filename.getStringValue());
		    CombineArchive ca = new CombineArchive(file, true);

		    // b) Get annotation
		    MetaDataObject mdo = ca.getDescriptions().get(0);
		    Element metaParent = mdo.getXmlDescription();
		    PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);

		    // c) Close archive
		    ca.close();

		    BufferedDataContainer[] containers =
		        ReaderUtils.readPMF(file, isPmfx, exec, pmfMetadataNode.getModelType());
		    BufferedDataTable[] tables = {containers[0].getTable(), containers[1].getTable()};
		    return tables;
		  }	
	  


		

		
		

		  

}
