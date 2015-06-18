/**
 * PMF File.
 * @author Miguel Alba
 */
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
import java.util.Map.Entry;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.knime.pmm.sbmlutil.Experiment;
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
	 * Reads in experiments from a PMF file in disk.
	 * 
	 * @param filename
	 *            PMF file name.
	 * @return List of experiments.
	 * @throws Exception
	 */
	public static List<Experiment> read(String filename) throws Exception {
		// Creates lists for models and data
		Map<String, SBMLDocument> models = new HashMap<>();
		Map<String, NuMLDocument> data = new HashMap<>();

		// Creates Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		URI sbmlURI = new URI(SBML_URI_STR);
		URI numlURI = new URI(NuML_URI_STR);

		// Parses models in the PMF file
		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			String modelname = entry.getFileName().replace(
					"." + SBML_EXTENSION, "");
			models.put(modelname, sbmlReader.readSBMLFromStream(stream));
			stream.close();
		}

		// Parses data in the PMF file
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
	 * Writes experiments to PMF file in disk.
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
			List<Experiment> exps, ExecutionContext exec) throws Exception {

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
		for (Experiment exp : exps) {
			// Adds data set
			if (exp.getData() != null) {
				// Creates temp file for the model
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("%s_%s.%s", filename, counter,
						NuML_EXTENSION);

				// Writes data to temp file and add it to the PMF file
				numlWriter.write(exp.getData(), numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);

				// Adds data source node to the model
				DataSourceNode node = new DataSourceNode(dataName);
				exp.getModel().getModel().getAnnotation().getNonRDFannotation()
						.addChild(node.getNode());
			}

			// Creates temp file for the model
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();

			// Creates model file name
			String mdName = String.format("%s_%d.%s", filename, counter,
					SBML_EXTENSION);

			// Writes model to temp file and add it to the PMF file
			sbmlWriter.write(exp.getModel(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);

			// Increments counter and update progress bar
			counter++;
			exec.setProgress((float) counter / exps.size());
		}
		ca.pack();
		ca.close();
	}
}
