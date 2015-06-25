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

import de.bund.bfr.knime.pmm.model.ExperimentalData;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 0: Experimental Data file
 * @author Miguel Alba
 */
public class ExperimentalDataFile {

	// URI string
	final static String NuML_URI_STR = "http://numl.googlecode/svn/trunk/NUMLSchema.xsd";

	// Extension
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in data experiments from a PMF file in disk.
	 */
	public static List<ExperimentalData> read(String filename) throws Exception {
		List<ExperimentalData> dataRecords = new LinkedList<>();

		// Create Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Create NuMLReader
		NuMLReader numlReader = new NuMLReader();

		URI numlURI = new URI(NuML_URI_STR);

		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(),
					StandardOpenOption.READ);
			NuMLDocument numlDoc = numlReader.read(stream);
			dataRecords.add(new ExperimentalData(numlDoc));
		}

		ca.close();
		return dataRecords;
	}

	/**
	 * Writes data records to a ExperimentalDataFile.
	 */
	public static void write(String dir, String filename,
			List<ExperimentalData> dataRecords, ExecutionContext exec)
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

		// Creates NuMLWriter
		NuMLWriter numlWriter = new NuMLWriter();

		URI numlURI = new URI(NuML_URI_STR);

		// Adds dataRecords
		short dataCounter = 0;
		for (ExperimentalData ed : dataRecords) {
			// Creates tmp file for ed
			File numlTmp = File.createTempFile("numlTmp", "");
			numlTmp.deleteOnExit();

			// Creates data file name
			String dataName = String.format("%s_%s.%s", filename, dataCounter,
					NuML_EXTENSION);
			
			// Writes data to numlTmp and it to the PMF file (ca)
			numlWriter.write(ed.getNuMLDocument(), numlTmp);
			ca.addEntry(numlTmp, dataName, numlURI);
			
			// Increments counter and update progress bar
			dataCounter++;
			exec.setProgress((float) dataCounter / dataRecords.size());
		}
		
		// Add description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent("Experimental data");
		
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		
		ca.addDescription(new DefaultMetaDataObject(metaParent));
		
		// pack and close CombineArchive
		ca.pack();
		ca.close();
	}
}
