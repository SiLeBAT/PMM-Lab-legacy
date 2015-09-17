package de.bund.bfr.knime.pmm.pmfreader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.annotation.MetadataAnnotation;
import de.bund.bfr.knime.pmm.annotation.Model1Annotation;
import de.bund.bfr.knime.pmm.annotation.Model2Annotation;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.file.ExperimentalDataFile;
import de.bund.bfr.knime.pmm.file.ManualSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.ManualTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWDataFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWODataFile;
import de.bund.bfr.knime.pmm.file.TwoStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.TwoStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.model.ExperimentalData;
import de.bund.bfr.knime.pmm.model.ManualSecondaryModel;
import de.bund.bfr.knime.pmm.model.ManualTertiaryModel;
import de.bund.bfr.knime.pmm.model.OneStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.OneStepTertiaryModel;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.PrimaryModelWOData;
import de.bund.bfr.knime.pmm.model.TwoStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.TwoStepTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Coefficient;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Metadata;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.knime.pmm.sbmlutil.ReaderUtils;
import de.bund.bfr.knime.pmm.sbmlutil.SecDep;
import de.bund.bfr.knime.pmm.sbmlutil.SecIndep;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;
import de.bund.bfr.numl.NuMLDocument;
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
		Element metaElement = metaParent.getChild("modeltype");
		String modelType = metaElement.getText();

		// c) Close archive
		ca.close();

		if (modelType.equals(ModelType.EXPERIMENTAL_DATA.name())) {
			reader = new ExperimentalDataReader();
		} else if (modelType.equals(ModelType.PRIMARY_MODEL_WDATA.name())) {
			reader = new PrimaryModelWDataReader();
		} else if (modelType.equals(ModelType.PRIMARY_MODEL_WODATA.name())) {
			reader = new PrimaryModelWODataReader();
		} else if (modelType.equals(ModelType.TWO_STEP_SECONDARY_MODEL.name())) {
			reader = new TwoStepSecondaryModelReader();
		} else if (modelType.equals(ModelType.ONE_STEP_SECONDARY_MODEL.name())) {
			reader = new OneStepSecondaryModelReader();
		} else if (modelType.equals(ModelType.MANUAL_SECONDARY_MODEL.name())) {
			reader = new ManualSecondaryModelReader();
		} else if (modelType.equals(ModelType.TWO_STEP_TERTIARY_MODEL.name())) {
			reader = new TwoStepTertiaryModelReader();
		} else if (modelType.equals(ModelType.ONE_STEP_TERTIARY_MODEL.name())) {
			reader = new OneStepTertiaryModelReader();
		} else if (modelType.equals(ModelType.MANUAL_TERTIARY_MODEL.name())) {
			reader = new ManualTertiaryModelReader();
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
			KnimeTuple tuple = parse(ed);
			dataContainer.addRowToTable(tuple);
			exec.setProgress((float) dataContainer.size() / eds.size());
		}

		dataContainer.close();

		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);

		// Gets metadata from the first NuMLDocument (all the NuMLDocuments in a
		// ExperimentalData file share the same metadata.
		NuMLDocument numlDocument = eds.get(0).getNuMLDoc();
		Metadata metadata = new DataFile(numlDocument).getMetadata();
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { dataContainer, metadataContainer };
	}

	private KnimeTuple parse(ExperimentalData ed) {
		return new DataTuple(ed.getNuMLDoc()).getTuple();
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

		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getSBMLDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private KnimeTuple parse(PrimaryModelWData pm) {
		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		KnimeTuple dataTuple = new DataTuple(pm.getNuMLDoc()).getTuple();
		row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));

		// primary model cells
		KnimeTuple m1Tuple = new Model1Tuple(pm.getSBMLDoc()).getTuple();
		row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getSBMLDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private KnimeTuple parse(PrimaryModelWOData pm) {
		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		KnimeTuple dataTuple = new DataTuple(pm.getSBMLDoc()).getTuple();
		row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));

		// primary model cells
		KnimeTuple m1Tuple = new Model1Tuple(pm.getSBMLDoc()).getTuple();
		row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));

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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getSecDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private List<KnimeTuple> parse(TwoStepSecondaryModel tssm) {
		// create n rows for n secondary models
		List<KnimeTuple> rows = new LinkedList<>();

		KnimeTuple m2Tuple = new Model2Tuple(tssm.getSecDoc().getModel()).getTuple();

		for (PrimaryModelWData pmwd : tssm.getPrimModels()) {
			KnimeTuple dataTuple;
			if (pmwd.getNuMLDoc() != null) {
				dataTuple = new DataTuple(pmwd.getNuMLDoc()).getTuple();
			} else {
				dataTuple = new DataTuple(pmwd.getSBMLDoc()).getTuple();
			}
			KnimeTuple m1Tuple = new Model1Tuple(pmwd.getSBMLDoc()).getTuple();

			KnimeTuple row = new M12DataTuple(dataTuple, m1Tuple, m2Tuple).getTuple();
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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getSBMLDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };

	}

	private List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
		List<KnimeTuple> rows = new LinkedList<>();

		// Parses primary model
		KnimeTuple primTuple = new Model1Tuple(ossm.getSBMLDoc()).getTuple();

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) ossm.getSBMLDoc()
				.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		KnimeTuple secTuple = new Model2Tuple(secModel).getTuple();

		// Parses data files
		for (NuMLDocument numlDoc : ossm.getNuMLDocs()) {
			KnimeTuple dataTuple = new DataTuple(numlDoc).getTuple();
			rows.add(new M12DataTuple(dataTuple, primTuple, secTuple).getTuple());
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
			KnimeTuple tuple = new Model2Tuple(model.getSBMLDoc().getModel()).getTuple();
			modelContainer.addRowToTable(tuple);
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getSBMLDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getTertDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private List<KnimeTuple> parse(TwoStepTertiaryModel tstm) {

		List<KnimeTuple> secTuples = new LinkedList<>();
		for (SBMLDocument secDoc : tstm.getSecDocs()) {
			secTuples.add(new Model2Tuple(secDoc.getModel()).getTuple());
		}
		
		List<KnimeTuple> tuples = new LinkedList<>();
		for (PrimaryModelWData pm : tstm.getPrimModels()) {
			KnimeTuple dataTuple = new DataTuple(pm.getNuMLDoc()).getTuple();
			KnimeTuple m1Tuple = new Model1Tuple(pm.getSBMLDoc()).getTuple();
			for (KnimeTuple m2Tuple : secTuples) {
				tuples.add(new M12DataTuple(dataTuple, m1Tuple, m2Tuple).getTuple());
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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getTertDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private List<KnimeTuple> parse(OneStepTertiaryModel ostm) {

		KnimeTuple primTuple = new Model1Tuple(ostm.getTertDoc()).getTuple();
		List<KnimeTuple> secTuples = new LinkedList<>();
		for (SBMLDocument secDoc : ostm.getSecDocs()) {
			secTuples.add(new Model2Tuple(secDoc.getModel()).getTuple());
		}

		List<KnimeTuple> tuples = new LinkedList<>();

		int instanceCounter = 1;
		
		for (NuMLDocument numlDoc : ostm.getDataDocs()) {
			KnimeTuple dataTuple = new DataTuple(numlDoc).getTuple();
			for (KnimeTuple secTuple : secTuples) {
				KnimeTuple tuple = new M12DataTuple(dataTuple, primTuple, secTuple).getTuple();
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

		// Gets metadata from the first model
		SBMLDocument aDoc = models.get(0).getTertDoc();
		Metadata metadata = new MetadataAnnotation(aDoc.getAnnotation()).getMetadata();
		// Creates metadata table and container
		DataTableSpec metadataSpec = new MetadataSchema().createSpec();
		BufferedDataContainer metadataContainer = exec.createDataContainer(metadataSpec);
		metadataContainer.addRowToTable(ReaderUtils.createMetadataTuple(metadata));
		metadataContainer.close();

		return new BufferedDataContainer[] { modelContainer, metadataContainer };
	}

	private List<KnimeTuple> parse(ManualTertiaryModel mtm) {

		KnimeTuple dataTuple = new DataTuple(mtm.getTertDoc()).getTuple();
		KnimeTuple m1Tuple = new Model1Tuple(mtm.getTertDoc()).getTuple();

		List<KnimeTuple> rows = new LinkedList<>();
		for (SBMLDocument secDoc : mtm.getSecDocs()) {
			KnimeTuple m2Tuple = new Model2Tuple(secDoc.getModel()).getTuple();
			rows.add(new M12DataTuple(dataTuple, m1Tuple, m2Tuple).getTuple());
		}

		return rows;
	}
}

/**
 * Model with primary and secondary models and data.
 * 
 * @author Miguel de Alba
 */
class M12DataTuple {

	KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

	public M12DataTuple(KnimeTuple dataTuple, KnimeTuple m1Tuple, KnimeTuple m2Tuple) {

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));

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
	}

	public KnimeTuple getTuple() {
		return tuple;
	}
}

abstract class TupleBase {
	
	protected KnimeTuple tuple;
	
	public KnimeTuple getTuple() {
		return tuple;
	}
}

class DataTuple extends TupleBase {

	static KnimeSchema schema = SchemaFactory.createDataSchema(); // time series schema

	public DataTuple(NuMLDocument numlDocument) {

		DataFile df = new DataFile(numlDocument);

		String timeUnit = df.getTimeUnit();
		String concUnit = df.getConcUnit();

		// Gets concentration unit object type from DB
		UnitsFromDB ufdb = DBUnits.getDBUnits().get(concUnit);
		String concUnitObjectType = ufdb.getObject_type();

		// Gets time series
		PmmXmlDoc mdData = ReaderUtils.createTimeSeries(timeUnit, concUnit, concUnitObjectType, df.getData());

		// Gets model variables
		PmmXmlDoc miscDoc = ReaderUtils.parseMiscs(df.getMiscs());

		// Gets literature items
		PmmXmlDoc litDoc = new PmmXmlDoc();
		for (LiteratureItem literatureItem : df.getLits()) {
			litDoc.add(literatureItem);
		}

		// Creates empty model info
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		// Creates and fills tuple
		tuple = new KnimeTuple(schema);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, df.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, df.getCombaseID());
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(df.getAgent()));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(df.getMatrix()));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, litDoc);
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
	}
	
	public DataTuple(SBMLDocument sbmlDoc) {

		Model model = sbmlDoc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());
		
		Agent agent = new Agent(model.getSpecies(0));
		Matrix matrix = new Matrix(model.getCompartment(0));
		
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);
		
		tuple = new KnimeTuple(SchemaFactory.createDataSchema());
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agent.toAgentXml()));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrix.toMatrixXml()));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
	}
}

class Model1Tuple extends TupleBase {

	static KnimeSchema schema = SchemaFactory.createM1Schema();  // model1 schema

	public Model1Tuple(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		Agent agent = new Agent(model.getSpecies(0));

		DepXml depXml = new DepXml("Value");
		String depUnitID = agent.getSpecies().getUnits();
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
		depXml.setDescription(agent.getDescription());

		// Gets limits
		if (limits.containsKey(agent.getSpecies().getId())) {
			Limits depLimits = limits.get(agent.getSpecies().getId());
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
		List<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			paramCell.add(new Coefficient(constParam).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		Uncertainties uncertainties = m1Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		// Reads model literature
		PmmXmlDoc mLit = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLit.add(lit);
		}

		// Reads estimated model literature
		PmmXmlDoc emLit = new PmmXmlDoc();
		for (LiteratureItem lit : m1Annot.getLits()) {
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
	}
}

class Model2Tuple extends TupleBase {

	static KnimeSchema schema = SchemaFactory.createM2Schema();

	public Model2Tuple(Model model) {

		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// Parses rule
		Model2Rule rule = new Model2Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parses dep
		String depName = rule.getRule().getVariable();
		SecDep secDep = new SecDep(model.getParameter(depName));
		DepXml depXml = secDep.toDepXml(model.getListOfUnitDefinitions(), limits);

		PmmXmlDoc indeps = new PmmXmlDoc();
		PmmXmlDoc consts = new PmmXmlDoc();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				consts.add(new Coefficient(param).toParamXml(model.getListOfUnitDefinitions(), limits));
			} else if (!param.getId().equals(depName)) {
				indeps.add(new SecIndep(param).toIndepXml(model.getListOfUnitDefinitions(), limits));
			}
		}

		// Get model annotations
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		Uncertainties uncertainties = m2Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		// Gets model literature
		PmmXmlDoc mLits = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLits.add(lit);
		}

		// Gets estimated model literature
		PmmXmlDoc emLits = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			emLits.add(lit);
		}

		tuple = new KnimeTuple(SchemaFactory.createM2Schema());
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
	}
}