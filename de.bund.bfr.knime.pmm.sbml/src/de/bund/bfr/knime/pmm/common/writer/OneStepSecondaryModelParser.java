package de.bund.bfr.knime.pmm.common.writer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.pmf.PMFUtil;
import de.bund.bfr.pmf.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.Metadata;

/**
 * Parse tuples from a table with primary models without data.
 */
public class OneStepSecondaryModelParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		// Sort tuples according to its secondary model
		Map<Integer, List<KnimeTuple>> secMap = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			// Get secondary EstModelXml
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);

			if (secMap.containsKey(estModel.getId())) {
				secMap.get(estModel.getId()).add(tuple);
			} else {
				List<KnimeTuple> ltup = new LinkedList<>();
				ltup.add(tuple);
				secMap.put(estModel.getId(), ltup);
			}
		}

		// For the tuples of every secondary model
		List<OneStepSecondaryModel> sms = new LinkedList<>();
		for (List<KnimeTuple> ltup : secMap.values()) {
			int modelCounter = sms.size();
			OneStepSecondaryModel model = parse(ltup, isPMFX, mdName, modelCounter, metadata, notes);
			sms.add(model);
		}

		if (isPMFX) {
			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<OneStepSecondaryModel> model = new LinkedList<>();
					model.add(sms.get(numModel));
					OneStepSecondaryModelFile.writePMFX(dir, modelName, model);
				}
			} else {
				OneStepSecondaryModelFile.writePMFX(dir, mdName, sms);
			}
		} else {
			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<OneStepSecondaryModel> model = new LinkedList<>();
					model.add(sms.get(numModel));
					OneStepSecondaryModelFile.writePMF(dir, modelName, model);
				}
			} else {
				OneStepSecondaryModelFile.writePMF(dir, mdName, sms);
			}
		}
	}

	private static OneStepSecondaryModel parse(List<KnimeTuple> tuples, boolean isPMFX, String mdName, int modelNum,
			Metadata metadata, String notes) {
		
		final String modelExtension = isPMFX ? ".pmf" : ".sbml";
		KnimeTuple firstTuple = tuples.get(0);

		// Retrieve Model2Schema cells
		EstModelXml secEstModel = (EstModelXml) firstTuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);

		Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
		SBMLDocument doc = m1Parser.getDocument();
		String docName = String.format("%s_%d.%s", mdName, modelNum, modelExtension);

		Model model = doc.getModel();
		model.setId(PMFUtil.createId("model" + secEstModel.getId()));
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
		CompModelPlugin compModelPlugin = (CompModelPlugin) model.getPlugin(CompConstants.shortLabel);

		// Create secondary model
		Model secModel = new Model2Parser(firstTuple, metadata, notes).getDocument().getModel();
		ModelDefinition md = new ModelDefinition(secModel);
		compDocPlugin.addModelDefinition(md);

		Submodel submodel = compModelPlugin.createSubmodel("submodel");
		submodel.setModelRef(secModel.getId());

		// Parse data sets and create NuML documents
		XMLNode metadataNode = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
		List<NuMLDocument> numlDocs = new LinkedList<>();
		List<String> numlDocNames = new LinkedList<>();
		for (KnimeTuple tuple : tuples) {
			String numlDocName = String.format("data%d.numl", numlDocs.size());
			numlDocNames.add(numlDocName);

			DataParser dataParser = new DataParser(tuple, metadata, notes);
			NuMLDocument numlDoc = dataParser.getDocument();
			numlDocs.add(numlDoc);

			// Adds DataSourceNode to the model
			metadataNode.addChild(new DataSourceNode(numlDocName).getNode());
		}

		OneStepSecondaryModel ossm = new OneStepSecondaryModel(docName, doc, numlDocNames, numlDocs);
		return ossm;
	}
}