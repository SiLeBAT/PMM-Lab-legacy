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

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.ManualTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 3c: File with tertiary models generated manually.
 * 
 * @author Miguel Alba
 */
public class ManualTertiaryModelFile {

	// Extensions
	final static String SBML_EXTENSION = "sbml";
	final static String PMF_EXTENSION = "pmf";

	public static List<ManualTertiaryModel> read(String filename)
			throws IOException, JDOMException, ParseException, CombineArchiveException, XMLStreamException {

		List<ManualTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Creates SBML and NuML readers
		SBMLReader sbmlReader = new SBMLReader();

		URI sbmlURI = URIFactory.createSBMLURI();

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(sbmlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = sbmlReader.readSBMLFromStream(stream, new NoLogging());
			stream.close();

			if (doc.getModel().getListOfSpecies().size() == 0) {
				secDocs.put(entry.getFileName(), doc);
			} else {
				tertDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (SBMLDocument tertDoc : tertDocs.values()) {
			List<SBMLDocument> secModels = new LinkedList<>();
			CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model ids
			for (ExternalModelDefinition emd : plugin.getListOfExternalModelDefinitions()) {
				secModels.add(secDocs.get(emd.getSource()));
			}

			ManualTertiaryModel mtm = new ManualTertiaryModel(tertDoc, secModels);
			models.add(mtm);
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
	 */
	public static void write(String dir, String filename, List<ManualTertiaryModel> models, ExecutionContext exec)
			throws IOException, JDOMException, ParseException, CombineArchiveException, SBMLException,
			XMLStreamException, TransformerException {

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

		Element metaParent = new Element("metaParent");
		
		// Add models and data
		short modelCounter = 0;
		for (ManualTertiaryModel model : models) {
			// Creates tmp file for the tert model
			File tertTmp = File.createTempFile("sec", "");
			tertTmp.deleteOnExit();

			// Creates name for the tert model
			String mdName = String.format("%s_%s.%s", filename, modelCounter, SBML_EXTENSION);

			// Writes tertiary model to tertTmp and adds it to the file
			sbmlWriter.write(model.getTertDoc(), tertTmp);
			ArchiveEntry masterEntry = ca.addEntry(tertTmp, mdName, sbmlURI);
			
			Element masterFileElement = new Element("masterFile");
			masterFileElement.addContent(masterEntry.getPath().getFileName().toString());
			metaParent.addContent(masterFileElement);

			for (SBMLDocument secDoc : model.getSecDocs()) {
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Creates name for the sec model
				Model md = secDoc.getModel();
				String secMdName = String.format("%s_%s_%s.%s", filename, modelCounter, md.getId(), SBML_EXTENSION);

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
		metaElement.addContent(ModelType.MANUAL_TERTIARY_MODEL.name());
		metaParent.addContent(metaElement);
		
		ca.addDescription(new DefaultMetaDataObject(metaParent));

		ca.pack();
		ca.close();
	}
}