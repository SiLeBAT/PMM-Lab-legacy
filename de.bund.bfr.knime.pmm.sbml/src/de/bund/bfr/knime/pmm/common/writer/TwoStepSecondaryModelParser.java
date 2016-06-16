package de.bund.bfr.knime.pmm.common.writer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmf.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.PrimaryModelNode;

/**
 * Parse tuples from a table with primary models without data.
 */
public class TwoStepSecondaryModelParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		// Sort secondary models
		Map<Integer, List<KnimeTuple>> secTuples = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			// Get secondary EstModel
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);
			if (secTuples.containsKey(estModel.getId())) {
				secTuples.get(estModel.getId()).add(tuple);
			} else {
				List<KnimeTuple> tlist = new LinkedList<>();
				tlist.add(tuple);
				secTuples.put(estModel.getId(), tlist);
			}
		}

		// For the tuples of every secondary model
		List<TwoStepSecondaryModel> sms = new LinkedList<>();
		for (List<KnimeTuple> tupleList : secTuples.values()) {
			TwoStepSecondaryModel model = parse(tupleList, isPMFX, sms.size(), mdName, metadata, notes);
			sms.add(model);
		}

		if (isPMFX) {
			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<TwoStepSecondaryModel> model = new LinkedList<>();
					model.add(sms.get(numModel));
					TwoStepSecondaryModelFile.writePMFX(dir, modelName, model);
				}
			} else {
				TwoStepSecondaryModelFile.writePMFX(dir, mdName, sms);
			}
		} else {
			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<TwoStepSecondaryModel> model = new LinkedList<>();
					model.add(sms.get(numModel));
					TwoStepSecondaryModelFile.writePMF(dir, modelName, model);
				}
			} else {
				TwoStepSecondaryModelFile.writePMF(dir, mdName, sms);
			}
		}
	}

	private static TwoStepSecondaryModel parse(List<KnimeTuple> tuples, boolean isPMFX, int modelNum, String mdName,
			Metadata metadata, String notes) {
		/**
		 * <ol>
		 * <li>Create n SBMLDocument for primary models</li>
		 * <li>Parse data and create n NuMLDocument</li>
		 * <li>Create SBMLDocument for secondary model</li>
		 * </ol>
		 */
		final String modelExtension = isPMFX ? "pmf" : "sbml";
		
		List<PrimaryModelWData> primModels = new LinkedList<>();
		for (int i = 0; i < tuples.size(); i++) {
			KnimeTuple tuple = tuples.get(i);
			PrimaryModelWData pm;

			Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

			SBMLDocument sbmlDoc = m1Parser.getDocument();
			String sbmlDocName = String.format("%s.%s", sbmlDoc.getModel().getId(), modelExtension);

			XMLNode metadataNode = sbmlDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata",
					"");
			if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
				DataParser dataParser = new DataParser(tuple, metadata, notes);

				NuMLDocument numlDoc = dataParser.getDocument();
				String numlDocName = String.format("%s.numl", sbmlDoc.getModel().getId());

				// Adds DataSourceNode to the model
				DataSourceNode dsn = new DataSourceNode(numlDocName);
				metadataNode.addChild(dsn.getNode());

				pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc);
			} else {
				pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
			}

			primModels.add(pm);
		}

		// We get the first tuple to query the Model2 columns which are the same
		// for all the tuples of the secondary model
		KnimeTuple firstTuple = tuples.get(0);
		Model2Parser m2Parser = new Model2Parser(firstTuple, metadata, notes);

		SBMLDocument secDoc = m2Parser.getDocument();
		String secDocName = String.format("%s_%d.%s", mdName, modelNum, modelExtension);
		// Adds annotation for the primary models
		XMLNode metadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
		for (PrimaryModelWData pmwd : primModels) {
			PrimaryModelNode node = new PrimaryModelNode(pmwd.getModelDocName());
			metadataNode.addChild(node.getNode());
		}

		// Creates and return TwoStepSecondaryModel
		return new TwoStepSecondaryModel(secDocName, secDoc, primModels);
	}
}