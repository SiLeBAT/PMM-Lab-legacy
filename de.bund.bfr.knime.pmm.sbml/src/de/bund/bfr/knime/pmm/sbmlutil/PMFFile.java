/**
 * PMF File.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;

public class PMFFile {

	// URI strings
	final static String SBML_URI_STR = "http://identifiers.org/combine/specifications/sbml";
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Read experiments from a PMF file in disk.
	 * 
	 * @param filename
	 *            : PMF file name.
	 * @return List of experiments.
	 * @throws Exception
	 */
	public static List<Experiment> read(String filename) throws Exception {
		// Create lists for models and data
		Map<String, SBMLDocument> models = new HashMap<>();
		Map<String, NuMLDocument> data = new HashMap<>();

		// Create Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Create SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Parse models in the PMF file
		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			String modelname = entry.getFileName().replace(
					"." + SBML_EXTENSION, "");
			models.put(modelname, sbmlReader.readSBMLFromStream(stream));
			stream.close();
		}

		// Parse data in the PMF file
		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			String modelname = entry.getFileName().replace(
					"." + NuML_EXTENSION, "");
			data.put(modelname, numlReader.read(stream));
			stream.close();
		}

		ca.close();

		List<Experiment> exps = new LinkedList<>();
		for (Entry<String, SBMLDocument> entry : models.entrySet()) {
			String modelName = entry.getKey();
			SBMLDocument sbmlDoc = entry.getValue();

			// Current model has associated data
			if (data.containsKey(modelName)) {
				NuMLDocument numlDoc = data.get(modelName);
				exps.add(new Experiment(sbmlDoc, numlDoc));
			}
			// Current model has not associated data
			else {
				exps.add(new Experiment(sbmlDoc));
			}
		}
		
		return exps;
	}

	/**
	 * Write experiments to PMF file in disk.
	 * 
	 * @param dir
	 *            : Directory path.
	 * @param filename
	 *            : PMF file name.
	 * @param exps
	 *            : List of experiments.
	 * @param exec
	 *            : Node's execution context.
	 * @throws Exception
	 */
	public static void write(String dir, String filename,
			List<Experiment> exps, ExecutionContext exec) throws Exception {

		// Create Combine Archive name
		String caName = String.format("%s/%s.%s", dir, filename, PMF_EXTENSION);

		// Remove previous Combine Archive if it exists
		File fileTemp = new File(caName);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		// Create new Combine Archive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Create SBML writer
		SBMLWriter sbmlWriter = new SBMLWriter();
		sbmlWriter.setProgramName("SBML Writer node");
		sbmlWriter.setProgramVersion("1.0");

		// Create NuML writer
		NuMLWriter numlWriter = new NuMLWriter();

		// Create SBML and NuML URIs
		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Add model and data
		short counter = 0;
		for (Experiment exp : exps) {
			// Add data set
			if (exp.getData() != null) {
				// Create temp file for the model
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();

				// Create data file name
				String dataName = String.format("%s_%s.%s", filename, counter,
						NuML_EXTENSION);

				// Write data to temp file and add it to the PMF file
				numlWriter.write(exp.getData(), numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);

				// Add data source node to the model
				DataSourceNode node = new DataSourceNode(dataName);
				exp.getModel().getModel().getAnnotation().getNonRDFannotation()
						.addChild(node.getNode());
			}

			// Create temp file for the model
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();

			// Create model file name
			String mdName = String.format("%s_%d.%s", filename, counter,
					SBML_EXTENSION);

			// Write model to temp file and add it to the PMF file
			sbmlWriter.write(exp.getModel(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);

			// Increment counter and update progress bar
			counter++;
			exec.setProgress((float) counter / exps.size());
		}
		ca.pack();
		ca.close();
	}
}
