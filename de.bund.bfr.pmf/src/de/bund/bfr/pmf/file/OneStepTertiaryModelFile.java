/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.file;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.OneStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * Case 3b: File with tertiary model generated with 1-step fit approach.
 * 
 * @author Miguel Alba
 */
public class OneStepTertiaryModelFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI PMF_URI = URIFactory.createPMFURI();
	private static final URI NuML_URI = URIFactory.createNuMLURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();

	public static List<OneStepTertiaryModel> readPMF(String filename) throws Exception {
		return read(filename, SBML_URI);
	}

	public static List<OneStepTertiaryModel> readPMFX(String filename) throws Exception {
		return read(filename, PMF_URI);
	}

	public static void writePMF(String dir, String filename, List<OneStepTertiaryModel> models) throws Exception {
		String caName = String.format("%s/%s.pmf", dir, filename);
		write(caName, SBML_URI, models);
	}

	public static void writePMFX(String dir, String filename, List<OneStepTertiaryModel> models) throws Exception {
		String caName = String.format("%s/%s.pmfx", dir, filename);
		write(caName, PMF_URI, models);
	}

	public static List<OneStepTertiaryModel> read(String filename, URI modelURI) throws Exception {

		List<OneStepTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Get data entries
		List<ArchiveEntry> dataEntries = ca.getEntriesWithFormat(NuML_URI);
		HashMap<String, NuMLDocument> dataEntriesMap = new HashMap<>(dataEntries.size());
		for (ArchiveEntry entry : dataEntries) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = NuMLReader.read(stream);
			stream.close();
			dataEntriesMap.put(entry.getFileName(), doc);
		}

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
		Set<String> masterFiles = metadataAnnotation.masterFiles;

		for (ArchiveEntry entry : ca.getEntriesWithFormat(modelURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = READER.readSBMLFromStream(stream);
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

			CompSBMLDocumentPlugin tertPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model ids
			ListOf<ExternalModelDefinition> emds = tertPlugin.getListOfExternalModelDefinitions();
			for (ExternalModelDefinition emd : emds) {
				String secModelName = emd.getSource();
				secModelNames.add(secModelName);

				SBMLDocument secDoc = secDocs.get(secModelName);
				secModels.add(secDoc);
			}

			// Gets data files from the tertiary model document
			List<String> numlDocNames = new LinkedList<>();
			List<NuMLDocument> numlDocs = new LinkedList<>();

			XMLNode tertAnnot = tertDoc.getModel().getAnnotation().getNonRDFannotation();
			XMLNode tertAnnotMetadata = tertAnnot.getChildElement("metadata", "");
			for (XMLNode node : tertAnnotMetadata.getChildElements(DataSourceNode.TAG, "")) {
				DataSourceNode dsn = new DataSourceNode(node);
				String numlDocName = dsn.getFile();
				numlDocNames.add(numlDocName);

				NuMLDocument numlDoc = dataEntriesMap.get(numlDocName);
				numlDocs.add(numlDoc);
			}
			OneStepTertiaryModel tstm = new OneStepTertiaryModel(tertDocName, tertDoc, secModelNames, secModels,
					numlDocNames, numlDocs);
			models.add(tstm);
		}

		return models;
	}

	private static void write(String filename, URI modelURI, List<OneStepTertiaryModel> models) throws Exception {

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(filename);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));
		
		Set<String> masterFiles = new HashSet<>(models.size());
		// Add models and data
		for (OneStepTertiaryModel model : models) {

			for (int i = 0; i < model.getDataDocs().size(); i++) {
				NuMLDocument numlDoc = model.getDataDocs().get(i);
				String numlDocName = model.getDataDocNames().get(i);

				// Creates tmp file for this primary model's data
				File numlTmp = File.createTempFile("data", "");
				numlTmp.deleteOnExit();

				// Writes data to numlTmp and adds it to the file
				NuMLWriter.write(numlDoc, numlTmp);
				ca.addEntry(numlTmp, numlDocName, NuML_URI);
			}

			// Creates tmp file for the tertiary model
			File tertTmp = File.createTempFile("tert", "");
			tertTmp.deleteOnExit();

			// Writes tertiary model to tertTmp and adds it to the file
			WRITER.write(model.getTertiaryDoc(), tertTmp);
			ArchiveEntry masterEntry = ca.addEntry(tertTmp, model.getTertiaryDocName(), modelURI);
			masterFiles.add(masterEntry.getPath().getFileName().toString());

			for (int i = 0; i < model.getSecDocs().size(); i++) {
				String secDocName = model.getSecDocNames().get(i);
				SBMLDocument secDoc = model.getSecDocs().get(i);

				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Writes model to secTmp and adds it to the file
				WRITER.write(secDoc, secTmp);
				ca.addEntry(secTmp, secDocName, modelURI);
			}
		}

		// Adds description with model type
		ModelType modelType = ModelType.ONE_STEP_TERTIARY_MODEL;
		Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
