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
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
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

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	public static List<TwoStepTertiaryModel> read(String filename)
			throws Exception {

		List<TwoStepTertiaryModel> models = new LinkedList<>();

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
			stream.close();
			dataEntries.put(entry.getFileName(), doc);
		}

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream);
			stream.close();

			// Secondary model -> Has no primary model
			if (doc.getModel() == null) {
				secDocs.put(entry.getFileName(), doc);
			} else {
				tertDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (SBMLDocument tertDoc : tertDocs.values()) {
			List<SBMLDocument> secModels = new LinkedList<>();
			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) tertDoc
					.getPlugin(CompConstants.shortLabel);
			// Gets secondary model ids
			ListOf<ModelDefinition> mdList = secCompPlugin
					.getListOfModelDefinitions();
			for (ModelDefinition md : mdList) {
				secModels.add(secDocs.get(md.getId()));
			}

			// Gets data files
			List<NuMLDocument> numlDocs = new LinkedList<>();
			XMLNode m1Annot = tertDoc.getModel().getAnnotation()
					.getNonRDFannotation();
			for (XMLNode node : m1Annot.getChildElements("dataSource", "")) {
				DataSourceNode dsn = new DataSourceNode(node);
				numlDocs.add(dataEntries.get(dsn.getFile()));
			}
			TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDoc,
					secModels, numlDocs);
			models.add(tstm);
		}

		return models;
	}

	/**
	 */
	public static void write(String dir, String filename,
			List<TwoStepTertiaryModel> models, ExecutionContext exec)
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
		for (TwoStepTertiaryModel model : models) {
			// Creates tmp file for the secondary model
			File tertTmp = File.createTempFile("sec", "");
			tertTmp.deleteOnExit();

			// Creates name for the secondary model
			String mdName = String.format("%s_%s.%s", filename, modelCounter,
					SBML_EXTENSION);

			// Writes tertiary model to tertTmp and adds it to the file
			sbmlWriter.write(model.getTertiaryDoc(), tertTmp);
			ca.addEntry(tertTmp, mdName, sbmlURI);

			for (SBMLDocument secDoc : model.getSecDocs()) {
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Creates name for the sec model
				CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) secDoc
						.getPlugin(CompConstants.shortLabel);
				ModelDefinition md = secCompPlugin.getModelDefinition(0);
				String secMdName = String.format("%s.%s", md.getId(),
						SBML_EXTENSION);

				// Writes model to secTmp and adds it to the file
				sbmlWriter.write(secDoc, secTmp);
				ca.addEntry(secTmp, secMdName, sbmlURI);
			}

			short dataCounter = 0;
			for (NuMLDocument numlDoc : model.getDataDocs()) {
				// Creates tmp file for this primary model's data
				File numlTmp = File.createTempFile("data", "");
				numlTmp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("data_%d_%d.%s", modelCounter,
						dataCounter, NuML_EXTENSION);

				// Writes data to numlTmp and adds it to the file
				numlWriter.write(numlDoc, numlTmp);
				ca.addEntry(numlTmp, dataName, numlURI);

				dataCounter++;
			}

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