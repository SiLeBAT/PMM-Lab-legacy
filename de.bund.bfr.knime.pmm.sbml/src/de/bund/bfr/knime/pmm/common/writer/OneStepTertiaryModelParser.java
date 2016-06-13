package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.sbml.DataSourceNode;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.PrimaryModelNode;

/**
 * One Step Fit Tertiary Model
 * 
 * @author Miguel Alba
 */
public class OneStepTertiaryModelParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		List<OneStepTertiaryModel> tms = new LinkedList<>();

		// Sort global models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = TableReader.sortGlobalModels(tuples);

		// Parse tertiary models
		for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
			List<List<KnimeTuple>> tuplesList = new LinkedList<>();
			for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
				tuplesList.add(tertiaryInstance);
			}
			/**
			 * We have a list of tertiary instances. Each instance has the same
			 * microbial data yet different data. Then we'll create a
			 * TwoTertiaryModel from the first instance and create the data from
			 * every instance.
			 */
			int mdNum = tms.size();
			OneStepTertiaryModel tm = parse(tuplesList, isPMFX, mdName, mdNum, metadata, notes);
			tms.add(tm);
		}

		if (isPMFX) {
			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<OneStepTertiaryModel> model = new LinkedList<>();
					model.add(tms.get(numModel));
					OneStepTertiaryModelFile.writePMFX(dir, modelName, model);
				}
			} else {
				OneStepTertiaryModelFile.writePMFX(dir, mdName, tms);
			}
		} else {
			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<OneStepTertiaryModel> model = new LinkedList<>();
					model.add(tms.get(numModel));
					OneStepTertiaryModelFile.writePMF(dir, modelName, model);
				}
			} else {
				OneStepTertiaryModelFile.writePMF(dir, mdName, tms);
			}
		}
	}

	private static OneStepTertiaryModel parse(List<List<KnimeTuple>> tupleList, boolean isPMFX, String mdName,
			int mdNum, Metadata metadata, String notes) {

		final String modelExtension = isPMFX ? "pmf" : "sbml";

		List<String> numlDocNames = new LinkedList<>();
		List<NuMLDocument> numlDocs = new LinkedList<>();
		for (List<KnimeTuple> instance : tupleList) {
			// Get first tuple: All the tuples of an instance have the same data
			KnimeTuple tuple = instance.get(0);
			if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
				int dataCounter = numlDocs.size();
				String numlDocName = String.format("data_%d_%d.numl", mdNum, dataCounter);
				numlDocNames.add(numlDocName);

				DataParser dataParser = new DataParser(tuple, metadata, notes);
				NuMLDocument numlDoc = dataParser.getDocument();
				numlDocs.add(numlDoc);
			}
		}

		// We'll get microbial data from the first instance
		List<KnimeTuple> firstInstance = tupleList.get(0);
		// and the primary model from the first tuple
		KnimeTuple firstTuple = firstInstance.get(0);

		Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
		SBMLDocument tertDoc = m1Parser.getDocument();
		String tertDocName = String.format("%s_%s.%s", mdName, mdNum, modelExtension);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);

		// Adds DataSourceNode to the tertiary model
		XMLNode tertMetadataNode = tertDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata",
				"");
		for (String numlDocName : numlDocNames) {
			tertMetadataNode.addChild(new DataSourceNode(numlDocName).getNode());
		}

		CompModelPlugin modelPlugin = (CompModelPlugin) tertDoc.getModel().getPlugin(CompConstants.shortLabel);

		// Add submodels and model definitions
		List<String> secDocNames = new LinkedList<>();
		List<SBMLDocument> secDocs = new LinkedList<>();
		for (KnimeTuple tuple : firstInstance) {

			SBMLDocument secDoc = new Model2Parser(tuple, metadata, notes).getDocument();

			String secModelId = secDoc.getModel().getId();
			String secDocName = String.format("%s.%s", secModelId, modelExtension);

			secDocNames.add(secDocName);
			secDocs.add(secDoc);

			// Creates and adds an ExternalModelDefinition
			ExternalModelDefinition emd = compDocPlugin.createExternalModelDefinition(secModelId);
			emd.setSource(secDocName);
			emd.setModelRef(secModelId);

			String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();

			Submodel submodel = modelPlugin.createSubmodel("submodel_" + depId);
			submodel.setModelRef(emd.getId());

			Parameter parameter = tertDoc.getModel().getParameter(depId);

			CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
			ReplacedBy replacedBy = plugin.createReplacedBy();
			replacedBy.setIdRef(depId);
			replacedBy.setSubmodelRef(submodel.getId());

			// Add annotation for the primary model
			XMLNode secMetadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation()
					.getChildElement("metadata", "");
			secMetadataNode.addChild(new PrimaryModelNode(tertDocName).getNode());

			// Adds DataSourceNodes to the sec model
			for (String numlDocName : numlDocNames) {
				secMetadataNode.addChild(new DataSourceNode(numlDocName).getNode());
			}
		}

		OneStepTertiaryModel tstm = new OneStepTertiaryModel(tertDocName, tertDoc, secDocNames, secDocs, numlDocNames,
				numlDocs);
		return tstm;
	}
}