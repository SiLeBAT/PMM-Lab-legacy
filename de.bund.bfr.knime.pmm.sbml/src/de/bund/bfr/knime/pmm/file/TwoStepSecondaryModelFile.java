package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.TwoStepSecondaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 2a: Two step secondary model file. Secondary models generated with the
 * classical 2-step approach from primary models.
 * 
 * @author Miguel Alba
 */
public class TwoStepSecondaryModelFile {

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * TODO ...
	 */
	public static List<TwoStepSecondaryModel> read(String filename)
			throws Exception {

		List<TwoStepSecondaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		// Creates URIs
		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Get data entries
		HashMap<String, NuMLDocument> dataEntries = new HashMap<>();
		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			NuMLDocument doc = numlReader.read(stream);
			dataEntries.put(entry.getFileName(), doc);
		}

		// Classify models into primary or secondary models
		HashMap<String, SBMLDocument> secModels = new HashMap<>();
		HashMap<String, SBMLDocument> primModels = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream);
			stream.close();

			// Secondary model -> Has no primary model
			if (doc.getModel() == null) {
				secModels.put(entry.getFileName(), doc);
			} else {
				primModels.put(entry.getFileName(), doc);
			}
		}
		ca.close();

		for (SBMLDocument secModel : secModels.values()) {
			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) secModel
					.getPlugin(CompConstants.shortLabel);
			ModelDefinition md = secCompPlugin.getModelDefinition(0);

			XMLNode metadata = md.getAnnotation().getNonRDFannotation()
					.getChildElement("metadata", "");
			List<PrimaryModelWData> pmwds = new LinkedList<>();

			List<XMLNode> refs = metadata.getChildElements("primaryModel", "");
			for (XMLNode ref : refs) {
				String primName = ref.getChild(0).getCharacters();
				SBMLDocument primDoc = primModels.get(primName);
				NuMLDocument numlDoc;

				// look for DataSourceNode
				XMLNode m1Annot = primDoc.getModel().getAnnotation()
						.getNonRDFannotation();
				XMLNode node = m1Annot.getChildElement("dataSourceNode", "");

				if (node == null) {
					numlDoc = null;
				} else {
					DataSourceNode dsn = new DataSourceNode(node);
					String dataFileName = dsn.getFile();
					numlDoc = dataEntries.get(dataFileName);
				}
				pmwds.add(new PrimaryModelWData(primDoc, numlDoc));
			}

			TwoStepSecondaryModel tssm = new TwoStepSecondaryModel(secModel,
					pmwds);
			models.add(tssm);
		}

		return models;
	}

	/**
	 */
	public static void write(String dir, String filename,
			List<TwoStepSecondaryModel> models, ExecutionContext exec)
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
		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Add models and data
		short modelCounter = 0;
		for (TwoStepSecondaryModel model : models) {
			// Creates tmp file for the secondary model
			File secTmp = File.createTempFile("sec", "");
			secTmp.deleteOnExit();

			// Creates name for the secondary model
			String mdName = String.format("%s_%s.%s", filename, modelCounter,
					SBML_EXTENSION);

			// Writes model to secTmp and adds it to the file
			sbmlWriter.write(model.getSecDoc(), secTmp);
			ca.addEntry(secTmp, mdName, sbmlURI);

			for (PrimaryModelWData primModel : model.getPrimModels()) {
				// Creates tmp file for the primary model
				File primTmp = File.createTempFile("prim", "");
				primTmp.deleteOnExit();

				// Creates name for the primary model
				mdName = String.format("%s.%s", primModel.getSBMLDoc()
						.getModel().getId(), SBML_EXTENSION);

				// Writes model to primTmp and adds it to the file
				sbmlWriter.write(primModel.getSBMLDoc(), primTmp);
				ca.addEntry(primTmp, mdName, sbmlURI);

				if (primModel.getNuMLDoc() != null) {
					// Creates tmp file for this primary model's data
					File numlTmp = File.createTempFile("temp2", "");
					numlTmp.deleteOnExit();

					// Creates data file name
					String dataName = String.format("%s.%s", primModel
							.getSBMLDoc().getModel().getId(), NuML_EXTENSION);

					// Writes data to numlTmp and adds it to the file
					numlWriter.write(primModel.getNuMLDoc(), numlTmp);
					ca.addEntry(numlTmp, dataName, numlURI);
				}
			}

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		// Adds description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent(ModelType.TWO_STEP_SECONDARY_MODEL.name());
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}
