package de.bund.bfr.knime.pmm.sbmlutil;

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
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

public class PrimaryModelFile {

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in experiments from a PMF file in disk.
	 * 
	 * @param filename
	 *            PMF file name.
	 * @return List of experiments.
	 * @throws Exception
	 */
	public static List<PrimaryModel> read(String filename) throws Exception {
		List<PrimaryModel> models = new LinkedList<>();

		// Creates Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		URI sbmlURI = new URI(SBML_URI_STR);

		// Parses models in the PMF file
		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(sbmlURI);
		for (ArchiveEntry modelEntry : modelEntries) {
			InputStream stream = Files.newInputStream(modelEntry.getPath(),
					StandardOpenOption.READ);
			SBMLDocument sbmlDoc = sbmlReader.readSBMLFromStream(stream);
			stream.close();

			// Parse data
			XMLNode modelAnnotation = sbmlDoc.getModel().getAnnotation()
					.getNonRDFannotation();
			DataSourceNode dataSourceNode = new DataSourceNode(modelAnnotation);
			String dataFileName = dataSourceNode.getFile();
			
			ArchiveEntry dataEntry = ca.getEntry(dataFileName);
			PrimaryModel model;
			if (dataEntry != null) {
				stream = Files.newInputStream(dataEntry.getPath(), StandardOpenOption.READ);
				NuMLDocument numlDoc = numlReader.read(stream);
				model = new PrimaryModel(sbmlDoc, numlDoc);
			} else {
				model = new PrimaryModel(sbmlDoc);
			}
			models.add(model);
		}

		ca.close();

		return models;
	}

	/**
	 * Writes experiments to Primary Model PMF file in disk.
	 * 
	 * @param dir
	 *            Directory path.
	 * @param filename
	 *            PMF file name.
	 * @param exps
	 *            List of experiments.
	 * @param exec
	 *            Node's execution context.
	 * @throws Exception
	 */
	public static void write(String dir, String filename,
			List<PrimaryModel> models, ExecutionContext exec) throws Exception {

		// Creates Combine Archive name
		String caName = String.format("%s/%s.%s", dir, filename, PMF_EXTENSION);

		// Removes previous Combine Archive if it exists
		File fileTemp = new File(caName);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		// Creates new Combine Archive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Creates SBML writer
		TidySBMLWriter sbmlWriter = new TidySBMLWriter();
		sbmlWriter.setProgramName("SBML Writer node");
		sbmlWriter.setProgramVersion("1.0");

		// Creates NuML writer
		NuMLWriter numlWriter = new NuMLWriter();

		// Creates SBML and NuML URIs
		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Adds model and data
		short counter = 0;
		for (PrimaryModel model : models) {
			// Adds data set
			if (model.getNuMLDocument() != null) {
				// Creates temp file for the model
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("%s_%s.%s", filename, counter,
						NuML_EXTENSION);

				// Writes data to temp file and add it to the PMF file
				numlWriter.write(model.getNuMLDocument(), numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);

				// Adds data source node to the model
				DataSourceNode node = new DataSourceNode(dataName);
				model.getSBMLDocument().getModel().getAnnotation()
						.getNonRDFannotation().addChild(node.getNode());
			}

			// Creates temp file for the model
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();

			// Creates model file name
			String mdName = String.format("%s_%d.%s", filename, counter,
					SBML_EXTENSION);

			// Writes model to temp file and add it to the PMF file
			sbmlWriter.write(model.getSBMLDocument(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);

			// Increments counter and update progress bar
			counter++;
			exec.setProgress((float) counter / models.size());
		}
		
		// Add description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent("Primary model");
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));	
		
		ca.pack();
		ca.close();
	}
}