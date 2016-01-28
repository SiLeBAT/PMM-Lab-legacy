package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmf.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.GlobalModelIdNode;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.PrimaryModelNode;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceSBMLNode;

public class TwoStepTertiaryModelParser implements Parser {

	@Override
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception {

		List<TwoStepTertiaryModel> tms = new LinkedList<>();

		// Sort global models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = TableReader.sortGlobalModels(tuples);

		for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
			List<List<KnimeTuple>> tuplesList = new LinkedList<>();
			for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
				tuplesList.add(tertiaryInstance);
			}
			// We have a list of tertiary instances. Each instance has the same
			// microbial data yet different data. Then we'll create a
			// TwoTertiaryModel from the first instance and create the data from
			// every instance.
			int modelNum = tms.size();
			TwoStepTertiaryModel tm = parse(tuplesList, isPMFX, modelNum, mdName, metadata, notes);
			tms.add(tm);
		}

		if (isPMFX) {
			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<TwoStepTertiaryModel> model = new LinkedList<>();
					model.add(tms.get(numModel));
					TwoStepTertiaryModelFile.writePMFX(dir, modelName, model);
				}
			} else {
				TwoStepTertiaryModelFile.writePMFX(dir, mdName, tms);
			}
		} else {
			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					List<TwoStepTertiaryModel> model = new LinkedList<>();
					model.add(tms.get(numModel));
					TwoStepTertiaryModelFile.writePMF(dir, modelName, model);
				}
			} else {
				TwoStepTertiaryModelFile.writePMF(dir, mdName, tms);
			}

		}
	}

	private static TwoStepTertiaryModel parse(List<List<KnimeTuple>> tupleList, boolean isPMFX, int modelNum,
			String mdName, Metadata metadata, String notes) {

		final String modelExtension = isPMFX ? "pmf" : "sbml";

		List<PrimaryModelWData> primModels = new LinkedList<>();
		List<SBMLDocument> secDocs = new LinkedList<>();

		// Parse primary models and their data from every instance. Each
		// instance has an unique primary model and data set
		for (List<KnimeTuple> instance : tupleList) {
			// Get first tuple: All the tuples of an instance have the same
			// primary model
			KnimeTuple tuple = instance.get(0);
			int instanceNum = primModels.size();
			PrimaryModelWData pm;

			Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

			SBMLDocument sbmlDoc = m1Parser.getDocument();
			String sbmlDocName = String.format("%s_%d_%d.%s", mdName, modelNum, instanceNum, modelExtension);
			XMLNode metadataNode = sbmlDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata",
					"");

			if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
				NuMLDocument numlDoc = new DataParser(tuple, metadata, notes).getDocument();
				String numlDocName = String.format("%s_%d_%d.numl", mdName, modelNum, instanceNum);

				// Adds DataSourceNode to the model
				metadataNode.addChild(new DataSourceNode(numlDocName).getNode());

				primModels.add(new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc));

			} else {
				pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
				primModels.add(pm);
			}
		}

		// Parse secondary models from the first instance (all the instance have
		// the same secondary models)
		List<KnimeTuple> firstInstance = tupleList.get(0);
		for (KnimeTuple tuple : firstInstance) {
			SBMLDocument secDoc = new Model2Parser(tuple, metadata, notes).getDocument();

			// Adds annotations for the primary models
			XMLNode metadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata",
					"");
			for (PrimaryModelWData pm : primModels) {
				if (pm.getDataDocName() != null) {
					metadataNode.addChild(new PrimaryModelNode(pm.getModelDocName()).getNode());
				}
			}

			secDocs.add(secDoc);
		}

		// Creates tertiary model
		String tertDocName = String.format("%s_%s.%s", mdName, modelNum, modelExtension);
		SBMLDocument tertDoc = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);
		// Enable Hierarchical Compositon package
		tertDoc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
		TableReader.addNamespaces(tertDoc);

		// Adds document annotation
		tertDoc.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());

		Model model = tertDoc.createModel("model");
		KnimeTuple aTuple = tupleList.get(0).get(0);

		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode metadataNode = new XMLNode(metadataTriple);
		model.getAnnotation().setNonRDFAnnotation(metadataNode);

		// Builds global model id node
		int gmId = aTuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
		metadataNode.addChild(new GlobalModelIdNode(gmId).getNode());

		// Get literature references
		PmmXmlDoc litDoc = aTuple.getPmmXml(Model1Schema.ATT_EMLIT);
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// Builds reference nodes
		for (LiteratureItem lit : lits) {
			Reference ref = Util.literatureItem2Reference(lit);
			metadataNode.addChild(new ReferenceSBMLNode(ref).getNode());
		}

		// Gets a primary model
		Model primModel = primModels.get(0).getModelDoc().getModel();

		// Adds species
		Species species = primModel.getSpecies(0);
		model.addSpecies(new Species(species));

		// Adds compartment
		Compartment compartment = primModel.getCompartment(0);
		model.addCompartment(new Compartment(compartment));

		// Adds rule
		AssignmentRule rule = (AssignmentRule) primModel.getRule(0);
		model.addRule(new AssignmentRule(rule));

		// Assigns parameters of the primary model
		for (Parameter p : primModel.getListOfParameters()) {
			Parameter p2 = new Parameter(p);
			if (p2.isSetAnnotation()) {
				p2.setAnnotation(new Annotation());
			}
			model.addParameter(p2);
		}

		CompModelPlugin modelPlugin = (CompModelPlugin) model.getPlugin(CompConstants.shortLabel);

		// Creates ExternalModelDefinition
		List<String> secDocNames = new LinkedList<>();
		for (SBMLDocument secDoc : secDocs) {
			// Gets model definition id from secDoc
			String mdId = secDoc.getModel().getId();

			String secDocName = String.format("%s.%s", secDoc.getModel().getId(), modelExtension);
			secDocNames.add(secDocName);

			// Creates and adds an ExternalModelDefinition to the tertiary model
			ExternalModelDefinition emd = compDocPlugin.createExternalModelDefinition(mdId);
			emd.setSource(secDocName);
			emd.setModelRef(mdId);

			String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();

			// Creates submodel
			Submodel submodel = modelPlugin.createSubmodel("submodel_" + depId);
			submodel.setModelRef(mdId);

			Parameter parameter = model.getParameter(depId);

			CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
			ReplacedBy replacedBy = plugin.createReplacedBy();
			replacedBy.setIdRef(depId);
			replacedBy.setSubmodelRef(submodel.getId());
		}

		// Assigns unit definitions of the primary model
		model.setListOfUnitDefinitions(new ListOf<UnitDefinition>(primModel.getListOfUnitDefinitions()));

		TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDocName, tertDoc, primModels, secDocNames, secDocs);
		return tstm;
	}
}