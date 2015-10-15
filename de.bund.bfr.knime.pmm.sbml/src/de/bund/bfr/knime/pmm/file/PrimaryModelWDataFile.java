package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.annotation.sbml.DataSourceNode;
import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 1a: Primary models with data file.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWDataFile {

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in a primary model with data.
	 */
	public static List<PrimaryModelWData> read(String filename) throws Exception {

		List<PrimaryModelWData> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Get data entries
		List<ArchiveEntry> dataEntries = ca.getEntriesWithFormat(numlURI);
		HashMap<String, ArchiveEntry> dataEntriesMap = new HashMap<>(dataEntries.size());
		for (ArchiveEntry dataEntry : dataEntries) {
			dataEntriesMap.put(dataEntry.getFileName(), dataEntry);
		}

		// Parse models in the PMF file
		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(sbmlURI);
		for (ArchiveEntry modelEntry : modelEntries) {

			InputStream stream = Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
			SBMLDocument sbmlDoc = sbmlReader.readSBMLFromStream(stream, new NoLogging());
			stream.close();

			// Parse data
			PrimaryModelWData model;
			XMLNode modelAnnotation = sbmlDoc.getModel().getAnnotation().getNonRDFannotation();
			if (modelAnnotation == null) {
				model = new PrimaryModelWData(sbmlDoc, null);
			} else {
				XMLNode metadataNode = modelAnnotation.getChildElement("metadata", "");
				XMLNode node = metadataNode.getChildElement("dataSource", "");

				// this model has no data
				if (node == null) {
					model = new PrimaryModelWData(sbmlDoc, null);
				} else {
					DataSourceNode dataSourceNode = new DataSourceNode(node);
					String dataFileName = dataSourceNode.getFile();
					ArchiveEntry dataEntry = dataEntriesMap.get(dataFileName);
					if (dataEntry == null) {
						model = new PrimaryModelWData(sbmlDoc, null);
					} else {
						stream = Files.newInputStream(dataEntry.getPath(), StandardOpenOption.READ);
						NuMLDocument numlDoc = numlReader.read(stream);
						stream.close();
						model = new PrimaryModelWData(sbmlDoc, numlDoc);
					}
				}
			}
			models.add(model);
		}

		ca.close();
		return models;
	}

	/**
	 * Writes experiments to PrimaryModelWDataFile.
	 */
	public static void write(String dir, String filename, List<PrimaryModelWData> models, ExecutionContext exec)
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

		// Creates SBML and NuML URIs
		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Add models and data
		short modelCounter = 0;
		for (PrimaryModelWData model : models) {
			// Adds data set
			if (model.getNuMLDoc() != null) {
				// Creates tmp file for the model
				File numlTmp = File.createTempFile("temp2", "");
				numlTmp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("%s_%s.%s", filename, modelCounter, NuML_EXTENSION);

				// Writes data to numlTmp and add it to the file
				numlWriter.write(model.getNuMLDoc(), numlTmp);
				ca.addEntry(numlTmp, dataName, numlURI);

				// Adds DataSourceNode to the model
				XMLNode dsn = new DataSourceNode(dataName).getNode();
				XMLNode metadataNode = model.getSBMLDoc().getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");
				metadataNode.addChild(dsn);
			}

			// Creates tmp file for the model
			File sbmlTmp = File.createTempFile("temp1", "");
			sbmlTmp.deleteOnExit();

			// Creates model file name
			String mdName = String.format("%s_%d.%s", filename, modelCounter, SBML_EXTENSION);

			// Writes model to sbmlTmp and add it to the file
			sbmlWriter.write(model.getSBMLDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp, mdName, sbmlURI);

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		// Adds description with model type
		String modelType = ModelType.PRIMARY_MODEL_WDATA.name();
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).getNode();
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
