package de.bund.bfr.knime.pmm.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.stax.SBMLReader;
import org.xml.sax.SAXException;

import de.bund.bfr.knime.pmm.annotation.sbml.DataSourceNode;
import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.OneStepTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 3b: File with tertiary model generated with 1-step fit approach.
 * 
 * @author Miguel Alba
 */
public class OneStepTertiaryModelFile {

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String NuML_EXTENSION = "numl";
	final static String PMF_EXTENSION = "pmf";

	public static List<OneStepTertiaryModel> read(String filename)
			throws IOException, JDOMException, ParseException, CombineArchiveException, XMLStreamException, ParserConfigurationException, SAXException {

		List<OneStepTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		// Creates URIs
		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Get data entries
		HashMap<String, NuMLDocument> dataEntries = new HashMap<>();
		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = numlReader.read(stream);
			stream.close();
			dataEntries.put(entry.getFileName(), doc);
		}

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream, new NoLogging());
			stream.close();

			// Secondary model -> Has no primary model
			if (doc.getModel().getListOfSpecies().size() == 0) {
				secDocs.put(entry.getFileName(), doc);
			} else {
				tertDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (SBMLDocument tertDoc : tertDocs.values()) {
			List<SBMLDocument> secModels = new LinkedList<>();

			CompSBMLDocumentPlugin tertPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model ids
			ListOf<ExternalModelDefinition> emds = tertPlugin.getListOfExternalModelDefinitions();
			for (ExternalModelDefinition emd : emds) {
				SBMLDocument secDoc = secDocs.get(emd.getSource());
				secModels.add(secDoc);
			}

			// Gets data files from the tertiary model document
			List<NuMLDocument> numlDocs = new LinkedList<>();
			XMLNode tertAnnot = tertDoc.getModel().getAnnotation().getNonRDFannotation();
			XMLNode tertAnnotMetadata = tertAnnot.getChildElement("metadata", "");
			for (XMLNode node : tertAnnotMetadata.getChildElements("dataSource", "")) {
				DataSourceNode dsn = new DataSourceNode(node);
				numlDocs.add(dataEntries.get(dsn.getFile()));
			}
			OneStepTertiaryModel tstm = new OneStepTertiaryModel(tertDoc, secModels, numlDocs);
			models.add(tstm);
		}

		return models;
	}

	/**
	 * @throws IOException 
	 * @throws CombineArchiveException 
	 * @throws ParseException 
	 * @throws JDOMException 
	 * @throws XMLStreamException 
	 * @throws SBMLException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public static void write(String dir, String filename, List<OneStepTertiaryModel> models, ExecutionContext exec) throws IOException, JDOMException, ParseException, CombineArchiveException, SBMLException, XMLStreamException, TransformerException, SAXException, ParserConfigurationException, TransformerFactoryConfigurationError {

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
		URI sbmlURI = URIFactory.createSBMLURI();
		URI numlURI = URIFactory.createNuMLURI();

		// Add models and data
		short modelCounter = 0;
		for (OneStepTertiaryModel model : models) {

			List<String> dataNames = new LinkedList<>();
			short dataCounter = 0;
			for (NuMLDocument numlDoc : model.getDataDocs()) {
				// Creates tmp file for this primary model's data
				File numlTmp = File.createTempFile("data", "");
				numlTmp.deleteOnExit();

				// Creates data file name
				String dataName = String.format("data_%d_%d.%s", modelCounter, dataCounter, NuML_EXTENSION);
				dataNames.add(dataName);

				// Writes data to numlTmp and adds it to the file
				numlWriter.write(numlDoc, numlTmp);
				ca.addEntry(numlTmp, dataName, numlURI);

				dataCounter++;
			}

			// Creates tmp file for the tertiary model
			File tertTmp = File.createTempFile("tert", "");
			tertTmp.deleteOnExit();

			// Creates name for the secondary model
			String mdName = String.format("%s_%s.%s", filename, modelCounter, SBML_EXTENSION);

			// Gets non RDF annotation of the tertiary model
			XMLNode tertAnnot = model.getTertDoc().getModel().getAnnotation().getNonRDFannotation();
			XMLNode tertAnnotMetadata = tertAnnot.getChildElement("metadata", "");

			// Adds DataSourceNodes to the tertiary model
			for (String dataName : dataNames) {
				tertAnnotMetadata.addChild(new DataSourceNode(dataName).getNode());
			}

			// Writes tertiary model to tertTmp and adds it to the file
			sbmlWriter.write(model.getTertDoc(), tertTmp);
			ca.addEntry(tertTmp, mdName, sbmlURI);

			for (SBMLDocument secDoc : model.getSecDocs()) {
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Creates name for the sec model
				Model md = secDoc.getModel();
				XMLNode mdMetadata = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");

				// Adds DataSourceNodes to the sec model
				for (String dataName : dataNames) {
					mdMetadata.addChild(new DataSourceNode(dataName).getNode());
				}

				String secMdName = String.format("%s.%s", md.getId(), SBML_EXTENSION);

				// Writes model to secTmp and adds it to the file
				sbmlWriter.write(secDoc, secTmp);
				ca.addEntry(secTmp, secMdName, sbmlURI);
			}

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		// Adds description with model type
		Element metaElement = new Element("modeltype");
		metaElement.addContent(ModelType.ONE_STEP_TERTIARY_MODEL.name());
		Element metaParent = new Element("metaParent");
		metaParent.addContent(metaElement);
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}