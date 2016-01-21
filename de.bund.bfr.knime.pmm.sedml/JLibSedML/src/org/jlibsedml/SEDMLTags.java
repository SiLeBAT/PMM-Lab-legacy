package org.jlibsedml;
/**
 * This class contains all the XML tags and attribute names in a SEDML document.
 */
public class SEDMLTags {
    
    private SEDMLTags(){}
    
	// SBML, MathML, SEDML namespaces
	public static final String SEDML_NS = "http://sed-ml.org/";
	public static final String SBML_NS = "http://www.sbml.org/sbml/level2";
	public static final String SBML_NS_L2V4 = "http://www.sbml.org/sbml/level2/version4";
	public static final String MATHML_NS = "http://www.w3.org/1998/Math/MathML";
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	
	// namespace prefixes:
	public static final String MATHML_NS_PREFIX = "math";
	public static final String SBML_NS_PREFIX = "sbml";
	
    public final static String ROOT_NODE_TAG			= "sedML";
    public final static String SED						= "Sed";
    public final static String VERSION_TAG				= "version";
	public static final String LEVEL_TAG 				= "level";

    
    public final static String NOTES					= "notes";
    public final static String ANNOTATION				= "annotation";
    public final static String META_ID_ATTR_NAME		= "metaid";
    public final static String MODELS					= "listOfModels";
    public final static String SIMS						= "listOfSimulations";
    public final static String TASKS					= "listOfTasks";
    public final static String DATAGENERATORS			= "listOfDataGenerators";
    public final static String OUTPUTS					= "listOfOutputs";

    // model attributes
    public final static String MODEL_TAG				= "model";
    public final static String MODEL_ATTR_ID			= "id";
    public final static String MODEL_ATTR_NAME			= "name";
    public final static String MODEL_ATTR_LANGUAGE		= "language";
    public final static String MODEL_ATTR_SOURCE		= "source";
    // types of model changes
    public final static String CHANGES					= "listOfChanges";
    public final static String CHANGE_ATTRIBUTE			= "changeAttribute";
    public final static String CHANGE_XML				= "changeXML";
    public final static String ADD_XML					= "addXML";
    public final static String REMOVE_XML				= "removeXML";
    public final static String NEW_XML                  = "newXML";
    public final static String COMPUTE_CHANGE			= "computeChange";
    public static final String COMPUTE_CHANGE_VARS      = "listOfVariables";
    public static final String COMPUTE_CHANGE_PARAMS    = "listOfParameters";

    // change attributes
    public final static String CHANGE_ATTR_TARGET		= "target";
    public final static String CHANGE_ATTR_NEWVALUE		= "newValue";
    public final static String CHANGE_ATTR_NEWXML		= "newXML";
    public final static String CHANGE_ATTR_MATH			= "math";

    // simulation attributes
    public final static String SIM_ATTR_ID				= "id";
    public final static String SIM_ATTR_NAME			= "name";
    public final static String SIM_ATTR_ALGORITM		= "algorithm";
    // types of simulations
    public final static String SIM_UTC					= "uniformTimeCourse";
    public final static String SIM_ANY                  = "anySimulation";
    public final static String SIM_OS                   = "oneStep";
    public final static String SIM_SS                   = "steadyState";
    
    //algorithm element
    public final static String ALGORITHM_TAG                = "algorithm";
    public static final String ALGORITHM_ATTR_KISAOID       = "kisaoID";
    public static final String ALGORITHM_PARAMETER_TAG      = "algorithmParameter";
    public final static String ALGORITHM_PARAMETER_LIST     = "listOfAlgorithmParameters";
    public static final String ALGORITHM_PARAMETER_KISAOID  = "kisaoID";
    public final static String ALGORITHM_PARAMETER_VALUE    = "value";
    
    // uniform time course attributes
    public final static String UTCA_INIT_T				= "initialTime";
    public final static String UTCA_OUT_START_T			= "outputStartTime";
    public final static String UTCA_OUT_END_T			= "outputEndTime";
    public final static String UTCA_POINTS_NUM			= "numberOfPoints";
    
    // one step attributes
    public final static String OS_STEP                  = "step";
    
    // task attributes
    public final static String TASK_TAG					= "task";
    public final static String TASK_ATTR_ID				= "id";
    public final static String TASK_ATTR_NAME			= "name";
    public final static String TASK_ATTR_MODELREF		= "modelReference";
    public final static String TASK_ATTR_SIMREF			= "simulationReference";
    // repeated task attributes
    public final static String REPEATED_TASK_TAG        = "repeatedTask";
    public final static String REPEATED_TASK_RESET_MODEL    = "resetModel";
    public final static String REPEATED_TASK_ATTR_RANGE     = "range";      // should be REPEATED_TASK_ATTR_RANGEREF
    public final static String REPEATED_TASK_RANGES_LIST    = "listOfRanges";
    public final static String REPEATED_TASK_CHANGES_LIST   = "listOfChanges";
    public final static String REPEATED_TASK_SUBTASKS_LIST  = "listOfSubTasks";
    public final static String SUBTASK_TAG                  = "subTask";
    public final static String SUBTASK_ATTR_ORDER           = "order";
    public final static String SUBTASK_ATTR_TASK            = "task";
    public final static String DEPENDENTTASK_TAG            = "dependentTask";
    public final static String DEPENDENT_TASK_SUBTASKS_LIST = "listOfDependentTasks";

    // set value
    public static final String SET_VALUE                    = "setValue";
    public static final String SET_VALUE_ATTR_TARGET        = "target";
    public static final String SET_VALUE_ATTR_RANGE_REF     = "range";
    public static final String SET_VALUE_ATTR_MODEL_REF     = "modelReference";

    // ranges
    public final static String RANGE_ATTR_ID                = "id";
    public final static String VECTOR_RANGE_TAG             = "vectorRange";
    public final static String VECTOR_RANGE_VALUE_TAG       = "value";
    public final static String UNIFORM_RANGE_TAG            = "uniformRange";
    public final static String UNIFORM_RANGE_ATTR_START     = "start";
    public final static String UNIFORM_RANGE_ATTR_END       = "end";
    public final static String UNIFORM_RANGE_ATTR_NUMP      = "numberOfPoints";
    public final static String UNIFORM_RANGE_ATTR_TYPE      = "type";
    public final static String FUNCTIONAL_RANGE_TAG         = "functionalRange";
    public final static String FUNCTIONAL_RANGE_INDEX       = "index";
    public final static String FUNCTIONAL_RANGE_VAR_LIST    = "listOfVariables";
    public final static String FUNCTIONAL_RANGE_PAR_LIST    = "listOfParameters";
    public final static String FUNCTION_TAG                 = "function";
    public final static String FUNCTION_MATH_TAG            = "math";

    
    // data generator attributes and children
    public final static String DATAGENERATOR_TAG            = "dataGenerator";
    public final static String DATAGEN_ATTR_ID              = "id";
    public final static String DATAGEN_ATTR_NAME            = "name";
    public final static String DATAGEN_ATTR_MATH            = "math";
    public final static String DATAGEN_ATTR_VARS_LIST       = "listOfVariables";
    public final static String DATAGEN_ATTR_PARAMS_LIST     = "listOfParameters";
    public final static String DATAGEN_ATTR_VARIABLE        = "variable";
    public final static String DATAGEN_ATTR_PARAMETER       = "parameter";
   
    // types of outputs
    public final static String OUTPUT_P2D					= "plot2D";
    public final static String OUTPUT_P3D					= "plot3D";
    public final static String OUTPUT_REPORT				= "report";
    // outputs attributes and children
    public final static String OUTPUT_ID					= "id";
    public final static String OUTPUT_NAME					= "name";
    public final static String OUTPUT_CURVES_LIST			= "listOfCurves";
    public final static String OUTPUT_SURFACES_LIST			= "listOfSurfaces";
    public final static String OUTPUT_DATASETS_LIST			= "listOfDataSets";
    public final static String OUTPUT_CURVE					= "curve";
    public final static String OUTPUT_SURFACE				= "surface";
    public final static String OUTPUT_DATASET				= "dataSet";
    public final static String OUTPUT_LOG_X					= "logX";
    public final static String OUTPUT_LOG_Y					= "logY";
    public final static String OUTPUT_LOG_Z					= "logZ";
    public final static String OUTPUT_DATA_REFERENCE		= "dataReference";
    public final static String OUTPUT_DATA_REFERENCE_X		= "xDataReference";
    public final static String OUTPUT_DATA_REFERENCE_Y		= "yDataReference";
    public final static String OUTPUT_DATA_REFERENCE_Z		= "zDataReference";
    public final static String OUTPUT_DATASET_LABEL		    = "label";
    
    
    // variable attributes
    public final static String VARIABLE_ID					= "id";
    public final static String VARIABLE_NAME				= "name";
    public final static String VARIABLE_TARGET				= "target";
    public final static String VARIABLE_SYMBOL				= "symbol";
    public final static String VARIABLE_TASK				= "taskReference";
    public final static String VARIABLE_MODEL				= "modelReference";
    
    // parameter attributes
    public final static String PARAMETER_ID					= "id";
    public final static String PARAMETER_NAME				= "name";
    public final static String PARAMETER_VALUE				= "value";
    
    // object kind
	public final static String CHANGE_ATTRIBUTE_KIND 		= "ChangeAttribute";
	public final static String CHANGE_XML_KIND 				= "ChangeXML";
	public final static String ADD_XML_KIND 				= "AddXML";
	public final static String REMOVE_XML_KIND 				= "RemoveXML";
    public final static String COMPUTE_CHANGE_KIND          = "ComputeChange";
    public final static String SET_VALUE_KIND               = "SetValue";
	public final static String DATAGEN_VARIABLE_KIND 		= "DataGenVariable";		// refers to a task
	public final static String CHANGE_MATH_VARIABLE_KIND 	= "ChangeMathVariable";		// refers to a model
	public final static String PLOT2D_KIND 					= "Plot2D";					// refers to a data generator
	public final static String PLOT3D_KIND 					= "Plot3D";
	public final static String REPORT_KIND 					= "Report";
    public final static String SIMUL_UTC_KIND               = "uniformTimeCourse";
    public final static String SIMUL_OS_KIND                = "oneStep";
    public final static String SIMUL_SS_KIND                = "steadyState";
    public final static String SIMUL_ANY_KIND				= "anySimulation";
    
    
	
	

}