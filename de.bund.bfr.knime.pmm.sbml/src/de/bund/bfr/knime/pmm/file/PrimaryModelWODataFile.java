package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.PrimaryModelWOData;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 1b: Primary models without data file.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWODataFile {

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in a primary model with data.
	 */
	public static List<PrimaryModelWOData> read(String filename)
			throws Exception {

		List<PrimaryModelWOData> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBMLReader
		SBMLReader sbmlReader = new SBMLReader();
		URI sbmlURI = URIFactory.createSBMLURI();

		// Parse models in the PMF file
		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(sbmlURI);
		for (ArchiveEntry modelEntry : modelEntries) {
			InputStream stream = Files.newInputStream(modelEntry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument sbmlDoc = sbmlReader.readSBMLFromStream(stream);
			stream.close();
			PrimaryModelWOData model = new PrimaryModelWOData(sbmlDoc);
			models.add(model);
		}

		ca.close();
		return models;
	}

	/**
	 * Writes experiments to PrimaryModelWDataFile.
	 */
	public static void write(String dir, String filename,
			List<PrimaryModelWOData> models, ExecutionContext exec)
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
		URI sbmlURI = URIFactory.createSBMLURI();

		// Add models and data
		short modelCounter = 0;
		for (PrimaryModelWOData model : models) {
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
		String modelType = ModelType.PRIMARY_MODEL_WODATA.name();
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).getNode();
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
