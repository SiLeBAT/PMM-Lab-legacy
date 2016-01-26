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
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * Case 2c: Manual secondary models. Secondary models generated manually.
 * 
 * @author Miguel Alba
 */
public class ManualTertiaryModelFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI PMF_URI = URIFactory.createPMFURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();

	public static List<ManualTertiaryModel> readPMF(String filename) throws Exception {
		return read(filename, SBML_URI);
	}

	public static List<ManualTertiaryModel> readPMFX(String filename) throws Exception {
		return read(filename, PMF_URI);
	}

	public static void writePMF(String dir, String filename, List<ManualTertiaryModel> models) throws Exception {
		// Creates CombineArchive name
		String caName = String.format("%s/%s.pmf", dir, filename);
		write(caName, SBML_URI, models);
	}

	public static void writePMFX(String dir, String filename, List<ManualTertiaryModel> models) throws Exception {
		// Creates CombineArchive name
		String caName = String.format("%s/%s.pmfx", dir, filename);
		write(caName, PMF_URI, models);
	}

	private static List<ManualTertiaryModel> read(String filename, URI modelURI) throws Exception {

		List<ManualTertiaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
		Set<String> masterFiles = metadataAnnotation.masterFiles;

		List<ArchiveEntry> sbmlEntries = ca.getEntriesWithFormat(modelURI);

		// Classify models into tertiary or secondary models
		int numTertDocs = masterFiles.size();
		int numSecDocs = sbmlEntries.size() - numTertDocs;
		Map<String, SBMLDocument> tertDocs = new HashMap<>(numTertDocs);
		Map<String, SBMLDocument> secDocs = new HashMap<>(numSecDocs);

		for (ArchiveEntry entry : sbmlEntries) {
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

			List<String> secModelNames = new LinkedList<>();
			List<SBMLDocument> secModels = new LinkedList<>();
			CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			// Gets secondary model ids
			for (ExternalModelDefinition emd : plugin.getListOfExternalModelDefinitions()) {
				String secModelName = emd.getSource();
				secModelNames.add(secModelName);

				SBMLDocument secModel = secDocs.get(secModelName);
				secModels.add(secModel);
			}

			ManualTertiaryModel mtm = new ManualTertiaryModel(tertDocName, tertDoc, secModelNames, secModels);
			models.add(mtm);
		}

		return models;
	}

	private static void write(String filename, URI modelURI, List<ManualTertiaryModel> models) throws Exception {

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(filename);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		Set<String> masterFiles = new HashSet<>(models.size());
		
		// Add models and data
		for (ManualTertiaryModel model : models) {
			// Creates tmp file for the tert model
			File tertTmp = File.createTempFile("sec", "");
			tertTmp.deleteOnExit();

			// Writes tertiary model to tertTmp and adds it to the file
			WRITER.write(model.getTertiaryDoc(), tertTmp);

			ArchiveEntry masterEntry = ca.addEntry(tertTmp, model.getTertiaryDocName(), modelURI);
			masterFiles.add(masterEntry.getPath().getFileName().toString());

			for (int i = 0; i < model.getSecDocs().size(); i++) {
				SBMLDocument secDoc = model.getSecDocs().get(i);
				String secDocName = model.getSecDocNames().get(i);

				// Creates tmp file for the secondary model
				File secTmp = File.createTempFile("sec", "");
				secTmp.deleteOnExit();

				// Writes model to secTmp and adds it to the file
				WRITER.write(secDoc, secTmp);
				ca.addEntry(secTmp, secDocName, modelURI);
			}
		}

		ModelType modelType = ModelType.MANUAL_TERTIARY_MODEL;
		Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;

		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
