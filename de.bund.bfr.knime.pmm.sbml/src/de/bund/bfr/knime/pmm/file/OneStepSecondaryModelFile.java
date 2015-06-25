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
import de.bund.bfr.knime.pmm.model.OneStepSecondaryModel;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 2b: One step secondary model file. Secondary models generated
 * "implicitly" during 1-step fitting of tertiary models.
 * 
 * @author Miguel Alba
 */
public class OneStepSecondaryModelFile {

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
	public static List<OneStepSecondaryModel> read(String filename)
			throws Exception {

		List<OneStepSecondaryModel> models = new LinkedList<>();

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

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream);
			stream.close();

			// look for DataSourceNode
			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) doc
					.getPlugin(CompConstants.shortLabel);
			ModelDefinition md = secCompPlugin.getModelDefinition(0);
			List<NuMLDocument> numlDocs = new LinkedList<>();
			XMLNode m2Annot = md.getAnnotation().getNonRDFannotation();
			for (XMLNode node : m2Annot.getChildElements("dataSource", "")) {
				DataSourceNode dsn = new DataSourceNode(node);
				String dataFileName = dsn.getFile();
				numlDocs.add(dataEntries.get(dataFileName));
			}

			OneStepSecondaryModel ossm = new OneStepSecondaryModel(doc,
					numlDocs);
			models.add(ossm);
		}
		ca.close();
		return models;
	}

	/**
	 */
	public static void write(String dir, String filename,
			List<OneStepSecondaryModel> models, ExecutionContext exec)
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
		for (OneStepSecondaryModel model : models) {
			// Creates tmp file for the SBML model
			File sbmlTmp = File.createTempFile("sbml", "");
			sbmlTmp.deleteOnExit();

			// Creates name for the secondary model
			String mdName = String.format("%s_%s.%s", filename, modelCounter,
					SBML_EXTENSION);

			// Writes model to secTmp and adds it to the file
			sbmlWriter.write(model.getSBMLDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp, mdName, sbmlURI);

			short dataCounter = 0;
			for (NuMLDocument numlDoc : model.getNumlDocs()) {
				// Creates tmp file for the NuML model
				File numlTmp = File.createTempFile("numl", "");
				numlTmp.deleteOnExit();

				// Creates name for the data file
				String dataName = String.format("data%d.numl", dataCounter);

				// Writes model to numlTmp and adds it to the file
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
		metaElement.addContent("One step secondary model");
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}
