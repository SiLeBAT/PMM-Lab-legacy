package de.bund.bfr.knime.pmm.pmfreader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.Model1Metadata;
import de.bund.bfr.knime.pmm.extendedtable.Model2Metadata;
import de.bund.bfr.knime.pmm.extendedtable.TimeSeriesMetadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.EMLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDAgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDMatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.file.ExperimentalDataFile;
import de.bund.bfr.pmf.file.ManualSecondaryModelFile;
import de.bund.bfr.pmf.file.ManualTertiaryModelFile;
import de.bund.bfr.pmf.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmf.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmf.file.PMFMetadataNode;
import de.bund.bfr.pmf.file.PrimaryModelWDataFile;
import de.bund.bfr.pmf.file.PrimaryModelWODataFile;
import de.bund.bfr.pmf.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmf.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.model.ManualSecondaryModel;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.model.OneStepTertiaryModel;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.Tuple;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Model1Annotation;
import de.bund.bfr.pmf.sbml.Model2Annotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.bund.bfr.pmf.sbml.SecDep;
import de.bund.bfr.pmf.sbml.SecIndep;
import de.bund.bfr.pmf.sbml.Uncertainties;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of SBMLReader.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */
public class PMFReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	Reader reader; // current reader

	/**
	 * Constructor for the node model.
	 */
	protected PMFReaderNodeModel() {
		// 0 input ports and 2 input ports
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		BufferedDataTable[] tables = null;
		tables = loadPMF(exec);
		return tables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null, null };
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
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
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	// Load PMF file
	private BufferedDataTable[] loadPMF(final ExecutionContext exec) throws Exception {
		// Get model type from annotation in the metadata file

		// a) Open archive
		String filepath = filename.getStringValue();
		CombineArchive ca = new CombineArchive(new File(filepath));

		// b) Get annotation
		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);

		// c) Close archive
		ca.close();

		switch (pmfMetadataNode.getModelType()) {
		case EXPERIMENTAL_DATA:
			reader = new ExperimentalDataReader();
			break;
		case PRIMARY_MODEL_WDATA:
			reader = new PrimaryModelWDataReader();
			break;
		case PRIMARY_MODEL_WODATA:
			reader = new PrimaryModelWODataReader();
			break;
		case TWO_STEP_SECONDARY_MODEL:
			reader = new TwoStepSecondaryModelReader();
			break;
		case ONE_STEP_SECONDARY_MODEL:
			reader = new OneStepSecondaryModelReader();
			break;
		case MANUAL_SECONDARY_MODEL:
			reader = new ManualSecondaryModelReader();
			break;
		case TWO_STEP_TERTIARY_MODEL:
			reader = new TwoStepTertiaryModelReader();
			break;
		case ONE_STEP_TERTIARY_MODEL:
			reader = new OneStepTertiaryModelReader();
			break;
		case MANUAL_TERTIARY_MODEL:
			reader = new ManualTertiaryModelReader();
			break;
		}

		BufferedDataContainer[] containers = reader.read(filepath, exec);
		BufferedDataTable[] tables = { containers[0].getTable(), containers[1].getTable() };
		return tables;
	}
}

/**
 * Reader interface
 * 
 * @author Miguel Alba
 */
interface Reader {
	/**
	 * Read models from a CombineArchive and returns a Knime table with them
	 * 
	 * @throws Exception
	 */
	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception;
}

class ExperimentalDataReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec dataSpec = SchemaFactory.createDataSchema().createSpec();
		BufferedDataContainer dataContainer = exec.createDataContainer(dataSpec);

		// Reads in experimental data from file
		List<ExperimentalData> eds = ExperimentalDataFile.read(filepath);

		// Creates tuples and adds them to the container
		for (ExperimentalData ed : eds) {
			KnimeTuple tuple = new DataTuple(ed.getDoc()).tuple;
			dataContainer.addRowToTable(tuple);
			exec.setProgress((float) dataContainer.size() / eds.size());
		}

		dataContainer.close();

		// Gets template of the first data file
		FSMRTemplate template = FSMRUtils.processData(eds.get(0).getDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fmsrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { dataContainer, fsmrContainer };
	}
}

class PrimaryModelWDataReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<PrimaryModelWData> models = PrimaryModelWDataFile.read(filepath);

		// Creates tuples and adds them to the container
		for (PrimaryModelWData model : models) {
			KnimeTuple tuple = parse(model);
			modelContainer.addRowToTable(tuple);
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template of the first model file
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getModelDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private KnimeTuple parse(PrimaryModelWData pm) {
		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).tuple;
		row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
		row.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

		// primary model cells
		KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).tuple;
		row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
		row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));
		return row;
	}
}

class PrimaryModelWODataReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<PrimaryModelWOData> models = PrimaryModelWODataFile.read(filepath);

		// Creates tuples and adds them to the container
		for (PrimaryModelWOData model : models) {
			KnimeTuple tuple = parse(model);
			modelContainer.addRowToTable(tuple);
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template of the first model file
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private KnimeTuple parse(PrimaryModelWOData pm) {
		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		KnimeTuple dataTuple = new DataTuple(pm.getDoc()).tuple;
		row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
		row.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

		// primary model cells
		KnimeTuple m1Tuple = new Model1Tuple(pm.getDoc()).tuple;
		row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
		row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

		return row;
	}
}

class TwoStepSecondaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<TwoStepSecondaryModel> models = TwoStepSecondaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (TwoStepSecondaryModel tssm : models) {
			List<KnimeTuple> tuples = parse(tssm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template of the first primary model file
		FSMRTemplate template = FSMRUtils
				.processModelWithMicrobialData(models.get(0).getPrimModels().get(0).getModelDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(TwoStepSecondaryModel tssm) {
		// create n rows for n secondary models
		List<KnimeTuple> rows = new LinkedList<>();

		KnimeTuple m2Tuple = new Model2Tuple(tssm.getSecDoc().getModel()).tuple;

		for (PrimaryModelWData pmwd : tssm.getPrimModels()) {
			KnimeTuple dataTuple;
			if (pmwd.getDataDoc() != null) {
				dataTuple = new DataTuple(pmwd.getDataDoc()).tuple;
			} else {
				dataTuple = new DataTuple(pmwd.getModelDoc()).tuple;
			}
			KnimeTuple m1Tuple = new Model1Tuple(pmwd.getModelDoc()).tuple;

			KnimeTuple row = Util.mergeTuples(dataTuple, m1Tuple, m2Tuple);
			rows.add(row);
		}

		return rows;
	}
}

class OneStepSecondaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<OneStepSecondaryModel> models = OneStepSecondaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (OneStepSecondaryModel ossm : models) {
			List<KnimeTuple> tuples = parse(ossm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template of the first primary model file
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getModelDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
		List<KnimeTuple> rows = new LinkedList<>();

		// Parses primary model
		KnimeTuple primTuple = new Model1Tuple(ossm.getModelDoc()).tuple;

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) ossm.getModelDoc()
				.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		KnimeTuple secTuple = new Model2Tuple(secModel).tuple;

		// Parses data files
		for (NuMLDocument numlDoc : ossm.getDataDocs()) {
			KnimeTuple dataTuple = new DataTuple(numlDoc).tuple;
			rows.add(Util.mergeTuples(dataTuple, primTuple, secTuple));
		}

		return rows;
	}
}

class ManualSecondaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM2Schema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<ManualSecondaryModel> models = ManualSecondaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (ManualSecondaryModel model : models) {
			KnimeTuple tuple = new Model2Tuple(model.getDoc().getModel()).tuple;
			modelContainer.addRowToTable(tuple);
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template
		FSMRTemplate template = FSMRUtils.processModelWithoutMicrobialData(models.get(0).getDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}
}

class TwoStepTertiaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Read in models from file
		List<TwoStepTertiaryModel> models = TwoStepTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (TwoStepTertiaryModel tssm : models) {
			List<KnimeTuple> tuples = parse(tssm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getTertDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(TwoStepTertiaryModel tstm) {

		List<KnimeTuple> secTuples = new LinkedList<>();
		for (SBMLDocument secDoc : tstm.getSecDocs()) {
			secTuples.add(new Model2Tuple(secDoc.getModel()).tuple);
		}

		List<KnimeTuple> tuples = new LinkedList<>();
		for (PrimaryModelWData pm : tstm.getPrimModels()) {
			KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).tuple;
			KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).tuple;
			for (KnimeTuple m2Tuple : secTuples) {
				tuples.add(Util.mergeTuples(dataTuple, m1Tuple, m2Tuple));
			}
		}

		return tuples;
	}
}

class OneStepTertiaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Read in models from file
		List<OneStepTertiaryModel> models = OneStepTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (OneStepTertiaryModel ostm : models) {
			List<KnimeTuple> tuples = parse(ostm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getTertiaryDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(OneStepTertiaryModel ostm) {

		KnimeTuple primTuple = new Model1Tuple(ostm.getTertiaryDoc()).tuple;
		List<KnimeTuple> secTuples = new LinkedList<>();
		for (SBMLDocument secDoc : ostm.getSecDocs()) {
			secTuples.add(new Model2Tuple(secDoc.getModel()).tuple);
		}

		List<KnimeTuple> tuples = new LinkedList<>();

		int instanceCounter = 1;

		for (NuMLDocument numlDoc : ostm.getDataDocs()) {
			KnimeTuple dataTuple = new DataTuple(numlDoc).tuple;
			for (KnimeTuple secTuple : secTuples) {
				KnimeTuple tuple = Util.mergeTuples(dataTuple, primTuple, secTuple);
				tuple.setValue(TimeSeriesSchema.ATT_CONDID, instanceCounter);
				tuples.add(tuple);
			}
			instanceCounter++;
		}

		return tuples;
	}
}

class ManualTertiaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Read in models from file
		List<ManualTertiaryModel> models = ManualTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (ManualTertiaryModel mtm : models) {
			List<KnimeTuple> tuples = parse(mtm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getTertiaryDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(ManualTertiaryModel mtm) {

		KnimeTuple dataTuple = new DataTuple(mtm.getTertiaryDoc()).tuple;
		KnimeTuple m1Tuple = new Model1Tuple(mtm.getTertiaryDoc()).tuple;

		List<KnimeTuple> rows = new LinkedList<>();
		for (SBMLDocument secDoc : mtm.getSecDocs()) {
			KnimeTuple m2Tuple = new Model2Tuple(secDoc.getModel()).tuple;
			rows.add(Util.mergeTuples(dataTuple, m1Tuple, m2Tuple));
		}

		return rows;
	}
}

class DataTuple {

	KnimeTuple tuple;
	// time series schema
	private static KnimeSchema schema = SchemaFactory.createDataSchema();

	public DataTuple(NuMLDocument doc) {

		int condID = doc.getResultComponent().getCondID();
		String combaseID = (doc.getResultComponent().isSetCombaseID()) ? doc.getResultComponent().getCombaseID() : "?";

		String timeUnit = doc.getTimeOntologyTerm().getUnitDefinition().getName();
		String concUnit = doc.getConcentrationOntologyTerm().getUnitDefinition().getName();

		// Gets concentration unit object type from DB
		UnitsFromDB ufdb = DBUnits.getDBUnits().get(concUnit);
		String concUnitObjectType = ufdb.getObject_type();

		PMFSpecies species = doc.getConcentrationOntologyTerm().getSpecies();
		AgentXml originalAgentXml = new AgentXml();
		originalAgentXml.setName(species.getName());
		MDAgentXml agentXml = new MDAgentXml();
		agentXml.setName(species.getName());
		if (species.isSetDetail()) {
			originalAgentXml.setDetail(species.getDetail());
			agentXml.setDetail(species.getDetail());
		}

		PMFCompartment compartment = doc.getConcentrationOntologyTerm().getCompartment();
		MatrixXml originalMatrixXml = new MatrixXml();
		MDMatrixXml matrixXml = new MDMatrixXml();
		originalMatrixXml.setName(compartment.getName());
		matrixXml.setName(compartment.getName());
		if (compartment.isSetDetail()) {
			originalMatrixXml.setDetail(compartment.getDetail());
			matrixXml.setDetail(compartment.getDetail());
		}

		// Gets time series
		Tuple[] dimensions = doc.getResultComponent().getDimensions();
		double[][] data = new double[dimensions.length][2];
		for (int i = 0; i < dimensions.length; i++) {
			Tuple tuple = dimensions[i];
			data[i] = new double[] { tuple.getConcValue().getValue(), tuple.getTimeValue().getValue() };
		}
		PmmXmlDoc mdData = Util.createTimeSeries(timeUnit, concUnit, concUnitObjectType, data);

		// Gets model variables
		ModelVariable[] modelVariables = compartment.getModelVariables();
		Map<String, Double> miscs = new HashMap<>(modelVariables.length);
		for (ModelVariable modelVariable : modelVariables) {
			miscs.put(modelVariable.getName(), modelVariable.getValue());
		}
		PmmXmlDoc miscDoc = Util.parseMiscs(miscs);

		// Creates empty model info
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		// Gets literature items
		PmmXmlDoc litDoc = new PmmXmlDoc();
		for (Reference reference : doc.getResultComponent().getReferences()) {
			String author = (reference.isSetAuthor()) ? reference.getAuthor() : null;
			Integer year = (reference.isSetYear()) ? reference.getYear() : null;
			String title = (reference.isSetTitle()) ? reference.getTitle() : null;
			String abstractText = (reference.isSetAbstractText()) ? reference.getAbstractText() : null;
			String journal = (reference.isSetJournal()) ? reference.getJournal() : null;
			String volume = (reference.isSetVolume()) ? reference.getVolume() : null;
			String issue = (reference.isSetIssue()) ? reference.getIssue() : null;
			Integer page = (reference.isSetPage()) ? reference.getPage() : null;
			Integer approvalMode = (reference.isSetApprovalMode()) ? reference.getApprovalMode() : null;
			String website = (reference.isSetWebsite()) ? reference.getWebsite() : null;
			ReferenceType type = (reference.isSetType()) ? reference.getType() : null;
			String comment = (reference.isSetComment()) ? reference.getComment() : null;

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type.value(), comment);
			litDoc.add(lit);

			MDLiteratureItem mdLit = new MDLiteratureItem(author, year, title, abstractText, journal, volume, issue,
					page, approvalMode, website, type.value(), comment);
			metadata.addLiteratureItem(mdLit);
		}

		// Creates and fills tuple
		tuple = new KnimeTuple(schema);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, litDoc);
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}

	public DataTuple(SBMLDocument sbmlDoc) {

		Model model = sbmlDoc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		AgentXml originalAgentXml = new AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);
		MDAgentXml agentXml = new MDAgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		MatrixXml originalMatrixXml = new MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);
		MDMatrixXml matrixXml = new MDMatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		PmmXmlDoc miscCell = new PmmXmlDoc();
		if (compartment.isSetModelVariables()) {
			Map<String, Double> miscs = new HashMap<String, Double>(compartment.getModelVariables().length);
			for (ModelVariable modelVariable : compartment.getModelVariables()) {
				miscs.put(modelVariable.getName(), modelVariable.getValue());
			}
			miscCell = Util.parseMiscs(miscs);
		}

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		tuple = new KnimeTuple(SchemaFactory.createDataSchema());
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}
}

class Model1Tuple {

	KnimeTuple tuple;
	private static KnimeSchema schema = SchemaFactory.createM1Schema(); // model1
																		// schema

	public Model1Tuple(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = Util.model1Rule2CatModel(rule);

		// Parse constraints
		Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));

		DepXml depXml = new DepXml("Value");
		String depUnitID = species.getUnits();
		if (depUnitID != null) {
			if (depUnitID.equals("dimensionless")) {
				depXml.setUnit("dimensionless");
				depXml.setCategory("Dimensionless quantity");
			} else {
				String depUnitName = model.getUnitDefinition(depUnitID).getName();
				depXml.setUnit(depUnitName);
				depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
			}
		}
		if (species.isSetDescription()) {
			depXml.setDescription(species.getDescription());
		}

		// Gets limits
		if (limits.containsKey(species.getId())) {
			Limits depLimits = limits.get(species.getId());
			depXml.setMax(depLimits.getMax());
			depXml.setMin(depLimits.getMin());
		}

		// Parses indeps
		Parameter indepParam = model.getParameter(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.isEmpty() && !indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}

		// Get limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMax(indepLimits.getMax());
			indepXml.setMin(indepLimits.getMin());
		}

		// Parse Consts
		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter param : model.getListOfParameters()) {
			if (!param.isConstant())
				continue;

			ParamXml paramXml = new ParamXml(param.getId(), param.getValue());

			String unitID = param.getUnits();
			if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
			}

			PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
			if (coefficient.isSetP()) {
				paramXml.setP(coefficient.getP());
			}
			if (coefficient.isSetError()) {
				paramXml.setError(coefficient.getError());
			}
			if (coefficient.isSetT()) {
				paramXml.setT(coefficient.getT());
			}
			if (coefficient.isSetDescription()) {
				paramXml.setDescription(coefficient.getDescription());
			}
			if (coefficient.isSetCorrelations()) {
				for (Correlation correlation : coefficient.getCorrelations()) {
					paramXml.addCorrelation(correlation.getName(), correlation.getValue());
				}
			}
			// Adds limits
			if (limits.containsKey(param.getId())) {
				Limits constLimits = limits.get(param.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}
			paramCell.add(paramXml);
		}

		EstModelXml estModel = Util.uncertainties2EstModel(m1Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		Model1AgentXml agentXml = new Model1AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		Model1MatrixXml matrixXml = new Model1MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);

		Model1Metadata metadata = new Model1Metadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		// Reads model literature
		PmmXmlDoc mLit = new PmmXmlDoc();
		for (Reference ref : rule.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLit.add(literatureItem);

			MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(mLiteratureItem);
		}

		// Reads estimated model literature
		PmmXmlDoc emLit = new PmmXmlDoc();
		for (Reference ref : m1Annot.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(emLiteratureItem);

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type, comment);
			emLit.add(lit);
		}

		tuple = new KnimeTuple(schema);
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
		tuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		tuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model1Schema.ATT_MLIT, mLit);
		tuple.setValue(Model1Schema.ATT_EMLIT, emLit);
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		tuple.setValue(Model1Schema.ATT_DBUUID, "?");
		tuple.setValue(Model1Schema.ATT_METADATA, metadata);
	}
}

class Model2Tuple {

	KnimeTuple tuple;
	private static KnimeSchema schema = SchemaFactory.createM2Schema();

	public Model2Tuple(Model model) {

		Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

		// Parses rule
		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = Util.model2Rule2CatModel(rule);

		// Parses dep
		String depName = rule.getRule().getVariable();

		SecDep secDep = new SecDep(model.getParameter(depName));
		DepXml depXml = new DepXml(secDep.getParam().getId());
		depXml.setDescription(secDep.getDescription());
		if (secDep.getParam().isSetUnits()) {
			// Adds unit
			String unitID = secDep.getParam().getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.setUnit(unitName);

			// Adds unit category
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				UnitsFromDB dbUnit = dbUnits.get(unitName);
				depXml.setCategory(dbUnit.getKind_of_property_quantity());
			}

			// Adds limits
			if (limits.containsKey(secDep.getParam().getId())) {
				Limits depLimits = limits.get(secDep.getParam().getId());
				depXml.setMax(depLimits.getMax());
				depXml.setMin(depLimits.getMin());
			}
		}

		PmmXmlDoc indeps = new PmmXmlDoc();
		PmmXmlDoc consts = new PmmXmlDoc();

		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				ParamXml paramXml = processCoefficient(param, model.getListOfUnitDefinitions(), limits);
				consts.add(paramXml);
			} else if (!param.getId().equals(depName)) {
				IndepXml indepXml = processIndep(param, model.getListOfUnitDefinitions(), limits);
				indeps.add(indepXml);
			}
		}

		// Get model annotations
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		EstModelXml estModel = Util.uncertainties2EstModel(m2Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		Model2Metadata metadata = new Model2Metadata();

		if (model.getListOfSpecies().size() == 1) {
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			Model2AgentXml agentXml = new Model2AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
					species.getDetail(), null);
			metadata.setAgentXml(agentXml);
		}

		if (model.getListOfCompartments().size() == 1) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			Model2MatrixXml matrixXml = new Model2MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
					compartment.getDetail(), null);
			metadata.setMatrixXml(matrixXml);
		}

		// Gets model literature
		PmmXmlDoc mLits = new PmmXmlDoc();
		for (Reference ref : rule.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLits.add(literatureItem);

			MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(mLiteratureItem);
		}

		// Gets estimated model literature
		PmmXmlDoc emLits = new PmmXmlDoc();
		for (Reference ref : m2Annot.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type, comment);
			emLits.add(lit);

			EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(emLiteratureItem);
		}

		tuple = new KnimeTuple(schema);
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, indeps);
		tuple.setValue(Model2Schema.ATT_PARAMETER, consts);
		tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model2Schema.ATT_MLIT, mLits);
		tuple.setValue(Model2Schema.ATT_EMLIT, emLits);
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		tuple.setValue(Model2Schema.ATT_DBUUID, "?");
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Annot.getGlobalModelID());
		tuple.setValue(Model2Schema.ATT_METADATA, metadata);
	}

	private ParamXml processCoefficient(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue());

		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.setUnit(unitName);
			paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
		if (coefficient.isSetDescription()) {
			paramXml.setDescription(coefficient.getDescription());
		}

		// Adds correlations
		if (coefficient.isSetCorrelations()) {
			for (Correlation corr : coefficient.getCorrelations()) {
				paramXml.addCorrelation(corr.getName(), corr.getValue());
			}
		}

		// Adds limits
		if (limits.containsKey(param.getId())) {
			Limits constLimits = limits.get(param.getId());
			paramXml.setMax(constLimits.getMax());
			paramXml.setMin(constLimits.getMin());
		}

		return paramXml;
	}

	private IndepXml processIndep(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {

		// Adds limits
		Double min = null;
		Double max = null;
		if (limits.containsKey(param.getId())) {
			Limits indepLimits = limits.get(param.getId());
			min = indepLimits.getMin();
			max = indepLimits.getMax();
		}

		IndepXml indepXml = new IndepXml(param.getId(), min, max);

		SecIndep secIndep = new SecIndep(param);
		indepXml.setDescription(secIndep.getDescription());

		// Adds unit and unit category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		return indepXml;
	}
}

class Util {

	private Util() {
	}

	/**
	 * Parses a list of constraints and returns a dictionary that maps variables
	 * and their limit values.
	 * 
	 * @param constraints
	 */
	public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
		Map<String, Limits> paramLimits = new HashMap<>();

		for (Constraint currConstraint : constraints) {
			LimitsConstraint lc = new LimitsConstraint(currConstraint);
			Limits lcLimits = lc.getLimits();
			paramLimits.put(lcLimits.getVar(), lcLimits);
		}

		return paramLimits;
	}

	/**
	 * Parses misc items.
	 * 
	 * @param miscs
	 *            . Dictionary that maps miscs names and their values.
	 * @return
	 */
	public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		if (miscs != null) {
			// First misc item has id -1 and the rest of items have negative
			// ints
			int counter = -1;
			for (Entry<String, Double> entry : miscs.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				List<String> categories;
				String description, unit;

				switch (name) {
				case "Temperature":
					categories = Arrays.asList(Categories.getTempCategory().getName());
					description = name;
					unit = Categories.getTempCategory().getStandardUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;

				case "pH":
					categories = Arrays.asList(Categories.getPhCategory().getName());
					description = name;
					unit = Categories.getPhUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;
				}
			}
		}
		return cell;
	}

	/**
	 * Creates time series
	 */
	public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit, String concUnitObjectType,
			double[][] data) {

		PmmXmlDoc mdData = new PmmXmlDoc();

		Double concStdDev = null;
		Integer numberOfMeasurements = null;

		for (double[] point : data) {
			double conc = point[0];
			double time = point[1];
			String name = "t" + mdData.size();

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
			t.setConcentrationUnitObjectType(concUnitObjectType);
			mdData.add(t);
		}

		return mdData;
	}

	public static EstModelXml uncertainties2EstModel(Uncertainties uncertainties) {
		int estModelId = uncertainties.getID();
		String modelName = uncertainties.getModelName();
		Double sse = uncertainties.getSSE();
		Double rms = uncertainties.getRMS();
		Double r2 = uncertainties.getR2();
		Double aic = uncertainties.getAIC();
		Double bic = uncertainties.getBIC();
		Integer dof = uncertainties.getDOF();
		EstModelXml estModel = new EstModelXml(estModelId, modelName, sse, rms, r2, aic, bic, dof);
		return estModel;
	}

	public static CatalogModelXml model1Rule2CatModel(ModelRule rule) {
		int formulaId = rule.getPmmlabID();
		String formulaName = rule.getFormulaName();
		ModelClass modelClass = rule.getModelClass();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");

		String formula = String.format("Value=%s", formulaString);

		CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
		return catModel;
	}

	public static CatalogModelXml model2Rule2CatModel(ModelRule rule) {
		int formulaId = rule.getPmmlabID();
		String formulaName = rule.getFormulaName();
		ModelClass modelClass = rule.getModelClass();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");

		String formula = String.format("%s=%s", rule.getVariable(), formulaString);

		CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
		return catModel;
	}

	public static KnimeTuple mergeTuples(KnimeTuple dataTuple, KnimeTuple m1Tuple, KnimeTuple m2Tuple) {

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

		// Copies model1 columns
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
		tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

		// Copies model2 columns
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
		tuple.setValue(Model2Schema.ATT_PARAMETER, m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
		tuple.setValue(Model2Schema.ATT_ESTMODEL, m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL));
		tuple.setValue(Model2Schema.ATT_MLIT, m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
		tuple.setValue(Model2Schema.ATT_EMLIT, m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model2Schema.ATT_DBUUID, m2Tuple.getString(Model2Schema.ATT_DBUUID));
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
		tuple.setValue(Model2Schema.ATT_METADATA, m2Tuple.getPmmXml(Model2Schema.ATT_METADATA));

		return tuple;
	}
}
