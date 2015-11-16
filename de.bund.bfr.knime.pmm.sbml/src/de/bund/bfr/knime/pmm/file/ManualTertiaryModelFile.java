package de.bund.bfr.knime.pmm.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.CharSet;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.stax.SBMLReader;

import com.ctc.wstx.io.CharsetNames;

import de.bund.bfr.knime.pmm.file.uri.URIFactory;
import de.bund.bfr.knime.pmm.model.ManualTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

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

		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
		Set<String> masterFiles = metadataAnnotation.getMasterFiles();

		List<ArchiveEntry> sbmlEntries = ca.getEntriesWithFormat(sbmlURI);

		// Classify models into tertiary or secondary models
		int numTertDocs = masterFiles.size();
		int numSecDocs = sbmlEntries.size() - numTertDocs;
		Map<String, SBMLDocument> tertDocs = new HashMap<>(numTertDocs);
		Map<String, SBMLDocument> secDocs = new HashMap<>(numSecDocs);

		for (ArchiveEntry entry : sbmlEntries) {
			 InputStream stream = Files.newInputStream(entry.getPath(),
			 StandardOpenOption.READ);
			 SBMLDocument doc = sbmlReader.readSBMLFromStream(stream, new
			 NoLogging());
			 stream.close();
			
			if (masterFiles.contains(entry.getFileName())) {
				tertDocs.put(entry.getFileName(), doc);
			} else {
				secDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (Map.Entry<String, SBMLDocument> entry : tertDocs.entrySet()) {
			String tertDocName = entry.getKey();
			SBMLDocument tertDoc = entry.getValue();

			List<SBMLDocument> secModels = new LinkedList<>();
			List<String> secModelNames = new LinkedList<>();
			CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			for (ExternalModelDefinition emd : plugin.getListOfExternalModelDefinitions()) {
				String secModelName = emd.getSource();
				SBMLDocument secModel = secDocs.get(secModelName);
				secModels.add(secModel);
			}

			ManualTertiaryModel mtm = new ManualTertiaryModel(tertDoc, tertDocName, secModels, secModelNames);
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

		Set<String> masterFiles = new HashSet<>(models.size());

		// Add models and data
		short modelCounter = 0;
		for (ManualTertiaryModel model : models) {
			// Creates tmp file for the tert model
			File tertTmp = File.createTempFile("sec", "");
			tertTmp.deleteOnExit();

			// Writes tertiary model to tertTmp and adds it to the file
			sbmlWriter.write(model.getTertDoc(), tertTmp);

			ArchiveEntry masterEntry = ca.addEntry(tertTmp, model.getTerDocName(), sbmlURI);
			masterFiles.add(masterEntry.getPath().getFileName().toString());

			for (int secDocCounter = 0; secDocCounter < model.getSecDocs().size(); secDocCounter++) {
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Writes model to secTmp and adds it to the file
				sbmlWriter.write(model.getSecDocs().get(secDocCounter), secTmp);
				ca.addEntry(secTmp, model.getSecDocNames().get(secDocCounter), sbmlURI);
			}

			// Increments counter and update progress bar
			modelCounter++;
			exec.setProgress((float) modelCounter / models.size());
		}

		String modelType = ModelType.MANUAL_TERTIARY_MODEL.name();
		Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).getNode();

		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}