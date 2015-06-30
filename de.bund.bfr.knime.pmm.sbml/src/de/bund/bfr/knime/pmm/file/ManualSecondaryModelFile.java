package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;

import de.bund.bfr.knime.pmm.model.ManualSecondaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 2c: Manual secondary models. Secondary models generated manually.
 * 
 * @author Miguel Alba
 */
public class ManualSecondaryModelFile {

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in a manually generated secondary model.
	 */
	public static List<ManualSecondaryModel> read(String filename)
			throws Exception {

		List<ManualSecondaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBMLReader
		SBMLReader sbmlReader = new SBMLReader();
		URI sbmlURI = new URI(SBML_URI_STR);

		// Parse models in the PMF file
		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument sbmlDoc = sbmlReader.readSBMLFromStream(stream);
			stream.close();
			models.add(new ManualSecondaryModel(sbmlDoc));
		}

		ca.close();
		return models;
	}

	/**
	 * Writes out a manually generated secondary model.
	 */
	public static void write(String dir, String filename,
			List<ManualSecondaryModel> models, ExecutionContext exec)
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

		// Creates SBML URI
		URI sbmlURI = new URI(SBML_URI_STR);

		// Add models and data
		short modelCounter = 0;
		for (ManualSecondaryModel model : models) {
			// Creates tmp file for the model
			File sbmlTmp = File.createTempFile("temp1", "");
			sbmlTmp.deleteOnExit();

			// Creates model file name
			String mdName = String.format("%s_%d.%s", filename, modelCounter,
					SBML_EXTENSION);

			// Writes model to sbmlTmp and add it to the file
			sbmlWriter.write(model.getSBMLDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp, mdName, sbmlURI);

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		// Adds description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent(ModelType.MANUAL_SECONDARY_MODEL.name());
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}
