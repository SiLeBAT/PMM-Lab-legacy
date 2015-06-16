package de.bund.bfr.knime.pmm.sbmlutil;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;

public class OSFTModelFile {

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	public static List<TertiaryModel> read(String filename) throws Exception {
		// Create list for tertiary models
		List<TertiaryModel> tms = new LinkedList<>();

		// Creates Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		// Creates URIs
		URI sbmlURI = new URI(SBML_URI_STR);

		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(sbmlURI);
		for (ArchiveEntry modelEntry : modelEntries) {
			InputStream stream = Files.newInputStream(modelEntry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument sbmlDoc = sbmlReader.readSBMLFromStream(stream);

			// Parse data
			LinkedList<NuMLDocument> numlDocs = new LinkedList<>();
			XMLNode modelAnnotation = sbmlDoc.getModel().getAnnotation()
					.getNonRDFannotation();
			for (XMLNode dataNode : modelAnnotation.getChildElements(
					"dataSourceNode", "")) {
				DataSourceNode dataSourceNode = new DataSourceNode(dataNode);
				String dataFileName = dataSourceNode.getFile();

				ArchiveEntry dataEntry = ca.getEntry(dataFileName);
				if (dataEntry != null) {
					stream = Files.newInputStream(dataEntry.getPath(),
							StandardOpenOption.READ);
					NuMLDocument numlDoc = numlReader.read(stream);
					numlDocs.add(numlDoc);
				}
			}

			tms.add(new TertiaryModel(sbmlDoc, numlDocs));
		}

		ca.close();
		return tms;
	}

	public static void write(String dir, String filename,
			List<TertiaryModel> models, ExecutionContext exec) throws Exception {

		// Creates Combine Archive name
		String caName = dir + "/" + filename + "." + PMF_EXTENSION;

		// Removes previous Combine Archive if it exists
		File fileTemp = new File(caName);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		// Creates new Combine Archive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Creates SBML Writer
		TidySBMLWriter sbmlWriter = new TidySBMLWriter();
		sbmlWriter.setProgramName("SBMLWriter node");
		sbmlWriter.setProgramVersion("1.0");

		// Creates NuML writer
		NuMLWriter numlWriter = new NuMLWriter();

		// Creates SBML and NuML URIs
		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Add models and data
		short modelCounter = 0;
		for (TertiaryModel tm : models) {
			short dataCounter = 0;
			for (NuMLDocument numlDoc : tm.getNuMLDocuments()) {
				// Process data
				// ...
				// Create temp file for the data
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("%s_%s_%s.%s", filename,
						modelCounter, dataCounter, NuML_EXTENSION);

				// Writes data to temp file and add it to the PMF file
				numlWriter.write(numlDoc, numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);

				// Adds data source node to the model
				DataSourceNode node = new DataSourceNode(dataName);
				tm.getSBMLDocument().getModel().getAnnotation()
						.getNonRDFannotation().addChild(node.getNode());

				dataCounter++;
			}

			// Creates temp file for the model
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();

			// Create model file name
			String mdName = String.format("%s_%d.%s", filename, modelCounter,
					SBML_EXTENSION);

			// Writes model to temp file and add it to the PMF file
			sbmlWriter.write(tm.getSBMLDocument(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);

			// Increments modelCounter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		ca.pack();
		ca.close();
	}
}