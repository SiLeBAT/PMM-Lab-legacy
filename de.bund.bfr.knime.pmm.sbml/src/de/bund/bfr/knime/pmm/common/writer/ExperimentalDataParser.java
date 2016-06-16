package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.pmf.file.ExperimentalDataFile;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.Metadata;

/**
 * Parse tuples from a table with timeseries.
 */
public class ExperimentalDataParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		List<ExperimentalData> eds = new LinkedList<>();
		for (int i = 0; i < tuples.size(); i++) {
			KnimeTuple tuple = tuples.get(i);

			String docName = String.format("%s_%d.numl", mdName, i);
			NuMLDocument doc = new DataParser(tuple, metadata, notes).getDocument();

			ExperimentalData ed = new ExperimentalData(docName, doc);
			eds.add(ed);
		}

		if (isPMFX) {
			ExperimentalDataFile.writePMFX(dir, mdName, eds);
		} else {
			ExperimentalDataFile.writePMF(dir, mdName, eds);
		}
	}
}