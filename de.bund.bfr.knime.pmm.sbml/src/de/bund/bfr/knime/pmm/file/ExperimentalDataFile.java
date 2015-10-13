package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.node.ExecutionContext;
import org.xml.sax.SAXException;

import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.ExperimentalData;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 0: Experimental Data file
 * 
 * @author Miguel Alba
 */
public class ExperimentalDataFile {

	// Extension
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	/**
	 * Reads in data experiments from a PMF file in disk.
	 * 
	 * @throws CombineArchiveException
	 * @throws ParseException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static List<ExperimentalData> read(String filename) throws Exception {
		List<ExperimentalData> dataRecords = new LinkedList<>();

		// Create Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Create NuMLReader
		NuMLReader numlReader = new NuMLReader();

		URI numlURI = URIFactory.createNuMLURI();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument numlDoc = numlReader.read(stream);
			stream.close();
			dataRecords.add(new ExperimentalData(numlDoc));
		}

		ca.close();
		return dataRecords;
	}

	/**
	 * Writes data records to a ExperimentalDataFile.
	 * 
	 * @throws CombineArchiveException
	 * @throws ParseException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static void write(String dir, String filename, List<ExperimentalData> dataRecords, ExecutionContext exec)
			throws IOException, JDOMException, ParseException, CombineArchiveException, TransformerException,
			SAXException, ParserConfigurationException, TransformerFactoryConfigurationError {

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

		URI numlURI = URIFactory.createNuMLURI();

		// Adds dataRecords
		short dataCounter = 0;
		for (ExperimentalData ed : dataRecords) {
			// Creates tmp file for ed
			File numlTmp = File.createTempFile("numlTmp", "");
			numlTmp.deleteOnExit();

			// Creates data file name
			String dataName = String.format("%s_%s.%s", filename, dataCounter, NuML_EXTENSION);

			// Writes data to numlTmp and it to the PMF file (ca)
			numlWriter.write(ed.getNuMLDoc(), numlTmp);
			ca.addEntry(numlTmp, dataName, numlURI);

			// Increments counter and update progress bar
			dataCounter++;
			exec.setProgress((float) dataCounter / dataRecords.size());
		}

		// Add description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent(ModelType.EXPERIMENTAL_DATA.name());

		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);

		ca.addDescription(new DefaultMetaDataObject(metaParent));

		// pack and close CombineArchive
		ca.pack();
		ca.close();
	}
}
