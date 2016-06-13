package de.bund.bfr.knime.pmm.common.reader;

import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.model.ExperimentalData;

public class ExperimentalDataReader implements Reader {

	public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec dataSpec = SchemaFactory.createDataSchema().createSpec();
		BufferedDataContainer dataContainer = exec.createDataContainer(dataSpec);

		// Reads in experimental data from file
		List<ExperimentalData> eds;
		if (isPMFX) {
			eds = ExperimentalDataFile.readPMFX(filepath);
		} else {
			eds = ExperimentalDataFile.readPMF(filepath);
		}

		// Creates tuples and adds them to the container
		for (ExperimentalData ed : eds) {
			KnimeTuple tuple = new DataTuple(ed.getDoc()).getTuple();
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