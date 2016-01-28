package de.bund.bfr.knime.pmm.common.reader;

import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.file.PrimaryModelWDataFile;
import de.bund.bfr.pmf.model.PrimaryModelWData;

public class PrimaryModelWDataReader implements Reader {

	public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<PrimaryModelWData> models;
		if (isPMFX) {
			models = PrimaryModelWDataFile.readPMFX(filepath);
		} else {
			models = PrimaryModelWDataFile.readPMF(filepath);
		}

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
		KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).getTuple();
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
		KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).getTuple();
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