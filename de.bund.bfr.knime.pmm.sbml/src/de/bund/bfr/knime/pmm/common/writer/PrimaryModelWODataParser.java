package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.pmf.file.PrimaryModelWODataFile;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.bund.bfr.pmf.sbml.Metadata;

/**
 * Parse tuples from a table with primary models without data.
 */
public class PrimaryModelWODataParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		final String modelExtension = isPMFX ? "pmf" : "sbml";

		List<PrimaryModelWOData> pms = new LinkedList<>();
		for (KnimeTuple tuple : tuples) {
			Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

			SBMLDocument sbmlDoc = m1Parser.getDocument();
			String sbmlDocName = String.format("%s_%d.%s", mdName, pms.size(), modelExtension);

			PrimaryModelWOData pm = new PrimaryModelWOData(sbmlDocName, sbmlDoc);
			pms.add(pm);
		}
		
		if (isPMFX) {
			PrimaryModelWODataFile.writePMFX(dir, mdName, pms);
		} else {
			PrimaryModelWODataFile.writePMF(dir, mdName, pms);
		}
	}
}