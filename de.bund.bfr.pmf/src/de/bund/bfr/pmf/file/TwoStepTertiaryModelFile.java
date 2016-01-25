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
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.bund.bfr.pmf.sbml.PrimaryModelNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * Case 3a: File with tertiary model generated with 2-step fit approach.
 * 
 * @author Miguel Alba
 */
public class TwoStepTertiaryModelFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI NUML_URI = URIFactory.createNuMLURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();

	public static List<TwoStepTertiaryModel> read(String filename) throws Exception {

		List<TwoStepTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Get data entries
		Map<String, NuMLDocument> dataEntries = new HashMap<>();
		for (ArchiveEntry entry : ca.getEntriesWithFormat(NUML_URI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = NuMLReader.read(stream);
			stream.close();
			dataEntries.put(entry.getFileName(), doc);
		}

		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
		Set<String> masterFiles = metadataAnnotation.masterFiles;

		// Classify models into tertiary or secondary models
		Map<String, SBMLDocument> tertDocs = new HashMap<>(masterFiles.size());
		Map<String, SBMLDocument> primDocs = new HashMap<>();
		Map<String, SBMLDocument> secDocs = new HashMap<>();

		for (ArchiveEntry entry : ca.getEntriesWithFormat(SBML_URI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = READER.readSBMLFromStream(stream);
			stream.close();

			if (masterFiles.contains(entry.getFileName())) {
				tertDocs.put(entry.getFileName(), doc);
			} else if (doc.getModel().getListOfSpecies().size() == 0) {
				secDocs.put(entry.getFileName(), doc);
			} else {
				primDocs.put(entry.getFileName(), doc);
			}
		}

		ca.close();

		for (Map.Entry<String, SBMLDocument> entry : tertDocs.entrySet()) {
			String tertDocName = entry.getKey();
			SBMLDocument tertDoc = entry.getValue();

			List<String> secModelNames = new LinkedList<>();
			List<SBMLDocument> secModels = new LinkedList<>();
			List<PrimaryModelWData> primModels = new LinkedList<>();

			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model documents
			ListOf<ExternalModelDefinition> emds = secCompPlugin.getListOfExternalModelDefinitions();
			for (ExternalModelDefinition emd : emds) {
				String secModelName = emd.getSource();
				secModelNames.add(secModelName);

				SBMLDocument secModel = secDocs.get(secModelName);
				secModels.add(secModel);
			}

			/**
			 * All the secondary models of a Two step tertiary model are linked
			 * to the same primary models. Thus these primary models can be
			 * retrieved from the first secondary model
			 */
			Model md = secModels.get(0).getModel();

			XMLNode metadata = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
			for (XMLNode pmNode : metadata.getChildElements(PrimaryModelNode.TAG, "")) {
				// Gets model name from annotation
				String mdName = pmNode.getChild(0).getCharacters();
				// Gets primary model
				SBMLDocument mdDoc = primDocs.get(mdName);
				// Gets data source annotation of the primary model
				XMLNode mdDocMetadata = mdDoc.getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");
				XMLNode node = mdDocMetadata.getChildElement("dataSource", "");
				// Gets data name from this annotation
				String dataName = new DataSourceNode(node).getFile();
				// Gets data file
				NuMLDocument dataDoc = dataEntries.get(dataName);

				primModels.add(new PrimaryModelWData(mdName, mdDoc, dataName, dataDoc));
			}

			TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDocName, tertDoc, primModels, secModelNames,
					secModels);
			models.add(tstm);
		}

		return models;
	}

	/**
	 */
	public static void write(String dir, String filename, List<TwoStepTertiaryModel> models) throws Exception {

		// Creates CombineArchive name
		String caName = String.format("%s/%s.pmf", dir, filename);

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(caName);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(caName));

		Set<String> masterFiles = new HashSet<>(models.size());

		// Add models and data
		for (TwoStepTertiaryModel model : models) {

			// List of primary model names
			List<String> pmNames = new LinkedList<>();

			for (PrimaryModelWData pm : model.getPrimModels()) {

				// Creates tmp file for the data
				File numlTmp = File.createTempFile("data", "");
				numlTmp.deleteOnExit();

				// Writes data to numlTmp and add it to the file
				NuMLWriter.write(pm.getDataDoc(), numlTmp);
				ca.addEntry(numlTmp, pm.getDataDocName(), NUML_URI);

				// Creates tmp file for the model
				File sbmlTmp = File.createTempFile("model", "");
				sbmlTmp.deleteOnExit();

				// Creates model file name
				pmNames.add(pm.getModelDocName());

				// Writes model to sbmlTmp and add it to the file
				WRITER.write(pm.getModelDoc(), sbmlTmp);
				ca.addEntry(sbmlTmp, pm.getModelDocName(), SBML_URI);
			}

			for (int i = 0; i < model.getSecDocs().size(); i++) {
				String secDocName= model.getSecDocNames().get(i);
				SBMLDocument secDoc = model.getSecDocs().get(i);
				
				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Writes model to secTmp and adds it to the file
				WRITER.write(secDoc, secTmp);
				ca.addEntry(secTmp, secDocName, SBML_URI);
			}

			// Creates tmp file for the secondary model
			File tertTmp = File.createTempFile("tert", "");
			tertTmp.deleteOnExit();

			// Writes tertiary model to tertTmp and adds it to the file
			WRITER.write(model.getTertDoc(), tertTmp);
			ArchiveEntry masterEntry = ca.addEntry(tertTmp, model.getTertDocName(), SBML_URI);
			masterFiles.add(masterEntry.getPath().getFileName().toString());
		}

		// Adds description with model type
		ModelType modelType = ModelType.TWO_STEP_TERTIARY_MODEL;
		Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}