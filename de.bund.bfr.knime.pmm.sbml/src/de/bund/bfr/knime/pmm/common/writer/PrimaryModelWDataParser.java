package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.sbml.DataSourceNode;
import de.bund.bfr.pmfml.sbml.Metadata;

/**
 * Parse tuples from a table with primary models with data.
 */
public class PrimaryModelWDataParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {
		
		final String modelExtension = isPMFX ? "pmf" : "sbml";

		List<PrimaryModelWData> pms = new LinkedList<>();

		for (KnimeTuple tuple : tuples) {
			PrimaryModelWData pm;

			Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);
			SBMLDocument sbmlDoc = m1Parser.getDocument();
			String sbmlDocName = String.format("%s_%d.%s", mdName, pms.size(), modelExtension);

			if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
				DataParser dataParser = new DataParser(tuple, metadata, notes);
				NuMLDocument numlDoc = dataParser.getDocument();
				String numlDocName = String.format("%s_%d.numl", mdName, pms.size());

				// Adds DataSourceNode to the model
				XMLNode dsn = new DataSourceNode(numlDocName).getNode();
				sbmlDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "").addChild(dsn);

				pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc);
			} else {
				pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
			}
			pms.add(pm);
		}

		if (isPMFX) {
			PrimaryModelWDataFile.writePMFX(dir, mdName, pms);
		} else {
			PrimaryModelWDataFile.writePMF(dir, mdName, pms);
		}
	}
}