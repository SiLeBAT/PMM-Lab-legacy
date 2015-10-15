package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.annotation.sbml.DataSourceNode;
import de.bund.bfr.knime.pmm.annotation.sbml.PrimaryModelNode;
import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.TwoStepTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 3a: File with tertiary model generated with 2-step fit approach.
 * 
 * @author Miguel Alba
 */
public class TwoStepTertiaryModelFile {

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	public static List<TwoStepTertiaryModel> read(String filename) throws Exception {

		List<TwoStepTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		// Creates URIs
		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Get data entries
		Map<String, NuMLDocument> dataEntries = new HashMap<>();
		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = numlReader.read(stream);
			stream.close();
			dataEntries.put(entry.getFileName(), doc);
		}

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>();
		Map<String, SBMLDocument> primDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream, new NoLogging());
			CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
			stream.close();
			
			// Tertiary model -> has external model definitions
			if (plugin.getNumExternalModelDefinitions() > 0) {
				tertDocs.put(entry.getFileName(), doc);
			}

			// Secondary model
			else if (doc.getModel().getListOfSpecies().size() == 0) {
				secDocs.put(entry.getFileName(), doc);
			}
			
			// Primary model -> Has no model definitions
			else {
				primDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (SBMLDocument tertDoc : tertDocs.values()) {
			List<SBMLDocument> secModels = new LinkedList<>();
			List<PrimaryModelWData> primModels = new LinkedList<>();
			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model documents
			ListOf<ExternalModelDefinition> emds = secCompPlugin.getListOfExternalModelDefinitions();
			for (ExternalModelDefinition emd : emds) {
				secModels.add(secDocs.get(emd.getSource()));
			}

			// All the secondary models of a Two step tertiary model are linked
			// to the same primary models. Thus these primary models can be
			// retrieved from the first secondary model
			Model md = secModels.get(0).getModel();

			XMLNode metadata = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
			for (XMLNode pmNode : metadata.getChildElements(PrimaryModelNode.TAG, "")) {
				// Gets model name from annotation
				String mdName = pmNode.getChild(0).getCharacters();
				// Gets primary model
				SBMLDocument mdDoc = primDocs.get(mdName);
				// Gets data source annotation of the primary model
				XMLNode mdDocMetadata = mdDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
				XMLNode node = mdDocMetadata.getChildElement("dataSource", "");
				// Gets data name from this annotation
				String dataName = new DataSourceNode(node).getFile();
				// Gets data file
				NuMLDocument dataDoc = dataEntries.get(dataName);

				primModels.add(new PrimaryModelWData(mdDoc, dataDoc));
			}

			TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDoc, primModels, secModels);
			models.add(tstm);
		}

		return models;
	}

	/**
	 */
	public static void write(String dir, String filename, List<TwoStepTertiaryModel> models, ExecutionContext exec)
			throws Exception {

		// Creates CombineArchive name
		String caName = String.format("%s/%s.%s", dir, filename, PMF_EXTENSION);

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(caName);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Creates SBMLWriter
		TidySBMLWriter sbmlWriter = new TidySBMLWriter();
		sbmlWriter.setProgramName("SBML Writer node");
		sbmlWriter.setProgramVersion("1.0");

		// Creates NuMLWriter
		NuMLWriter numlWriter = new NuMLWriter();

		// Creates SBML URI
		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Add models and data
		short modelCounter = 0;
		for (TwoStepTertiaryModel model : models) {

			// List of primary model names
			List<String> pmNames = new LinkedList<>();

			short instCounter = 0; // instance counter
			for (PrimaryModelWData pm : model.getPrimModels()) {

				// Creates tmp file for the data
				File numlTmp = File.createTempFile("data", "");
				numlTmp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("%s_%d_%d.%s", filename, modelCounter, instCounter, NuML_EXTENSION);

				// Writes data to numlTmp and add it to the file
				numlWriter.write(pm.getNuMLDoc(), numlTmp);
				ca.addEntry(numlTmp, dataName, numlURI);

				// Adds DataSourceNode to the model
				XMLNode metadataNode = pm.getSBMLDoc().getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
				metadataNode.addChild(new DataSourceNode(dataName).getNode());

				// Creates tmp file for the model
				File sbmlTmp = File.createTempFile("model", "");
				sbmlTmp.deleteOnExit();

				// Creates model file name
				String mdName = String.format("%s_%d_%d.%s", filename, modelCounter, instCounter, SBML_EXTENSION);
				pmNames.add(mdName);

				// Writes model to sbmlTmp and add it to the file
				sbmlWriter.write(pm.getSBMLDoc(), sbmlTmp);
				ca.addEntry(sbmlTmp, mdName, sbmlURI);

				instCounter++;
			}

			// //////////////////////////////////////////////////////////////////////////////
			for (SBMLDocument secDoc : model.getSecDocs()) {
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Creates name for the sec model
				Model md = secDoc.getModel();
				String secMdName = String.format("%s.%s", md.getId(), SBML_EXTENSION);

				// Adds annotations for the primary models
				XMLNode metadataNode = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
				for (String name : pmNames) {
					metadataNode.addChild(new PrimaryModelNode(name).getNode());
				}

				// Writes model to secTmp and adds it to the file
				sbmlWriter.write(secDoc, secTmp);
				ca.addEntry(secTmp, secMdName, sbmlURI);
			}

			// Creates tmp file for the secondary model
			File tertTmp = File.createTempFile("tert", "");
			tertTmp.deleteOnExit();

			// Creates name for the tertiary model
			String mdName = String.format("%s_%s.%s", filename, modelCounter, SBML_EXTENSION);

			// Writes tertiary model to tertTmp and adds it to the file
			sbmlWriter.write(model.getTertDoc(), tertTmp);
			ca.addEntry(tertTmp, mdName, sbmlURI);

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		// Adds description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent(ModelType.TWO_STEP_TERTIARY_MODEL.name());
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}