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
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
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
 * Case 2a: Two step secondary model file. Secondary models generated with the
 * classical 2-step approach from primary models.
 * 
 * @author Miguel Alba
 */
public class TwoStepSecondaryModelFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI NUML_URI = URIFactory.createNuMLURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();

	public static List<TwoStepSecondaryModel> read(String filename) throws Exception {

		List<TwoStepSecondaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Get data entries
		HashMap<String, NuMLDocument> dataEntries = new HashMap<>();
		for (ArchiveEntry entry : ca.getEntriesWithFormat(NUML_URI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = NuMLReader.read(stream);
			stream.close();
			dataEntries.put(entry.getFileName(), doc);
		}

		// Gets master files
		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode metadataAnnotation = new PMFMetadataNode(metaParent);
		Set<String> masterFiles = metadataAnnotation.masterFiles;

		// List of SBML entries
		List<ArchiveEntry> sbmlEntries = ca.getEntriesWithFormat(SBML_URI);

		// Classify models into primary or secondary models
		int numSecModels = masterFiles.size();
		int numPrimModels = sbmlEntries.size() - masterFiles.size();
		HashMap<String, SBMLDocument> secModels = new HashMap<>(numSecModels);
		HashMap<String, SBMLDocument> primModels = new HashMap<>(numPrimModels);

		for (ArchiveEntry entry : sbmlEntries) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			SBMLDocument doc = READER.readSBMLFromStream(stream);
			stream.close();

			if (masterFiles.contains(entry.getFileName())) {
				secModels.put(entry.getFileName(), doc);
			} else {
				primModels.put(entry.getFileName(), doc);
			}
		}
		ca.close();

		for (Map.Entry<String, SBMLDocument> entry : secModels.entrySet()) {
			String secModelName = entry.getKey();
			SBMLDocument secModelDoc = entry.getValue();
			
			Model md = secModelDoc.getModel();

			XMLNode annot = md.getAnnotation().getNonRDFannotation();
			XMLNode metadata = annot.getChildElement("metadata", "");
			List<PrimaryModelWData> pmwds = new LinkedList<>();

			List<XMLNode> refs = metadata.getChildElements(PrimaryModelNode.TAG, "");
			for (XMLNode ref : refs) {
				PrimaryModelWData pm;
				
				String primName = ref.getChild(0).getCharacters();
				SBMLDocument primDoc = primModels.get(primName);
				NuMLDocument numlDoc;

				// look for DataSourceNode
				XMLNode m1Annot = primDoc.getModel().getAnnotation().getNonRDFannotation();
				XMLNode m1Metadata = m1Annot.getChildElement("metadata", "");
				XMLNode node = m1Metadata.getChildElement("dataSource", "");

				if (node == null) {
					pm = new PrimaryModelWData(primName, primDoc, null, null);
				} else {
					DataSourceNode dsn = new DataSourceNode(node);
					String dataFileName = dsn.getFile();
					numlDoc = dataEntries.get(dataFileName);
					
					pm = new PrimaryModelWData(primName, primDoc, dataFileName, numlDoc);
				}
				pmwds.add(pm);
			}

			TwoStepSecondaryModel tssm = new TwoStepSecondaryModel(secModelName, secModelDoc, pmwds);
			models.add(tssm);
		}

		return models;
	}

	/**
	 */
	public static void write(String dir, String filename, List<TwoStepSecondaryModel> models) throws Exception {

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
		for (TwoStepSecondaryModel model : models) {
			// Creates tmp file for the secondary model
			File secTmp = File.createTempFile("sec", "");
			secTmp.deleteOnExit();

			// Writes model to secTmp and adds it to the file
			WRITER.write(model.getSecDoc(), secTmp);
			ArchiveEntry masterEntry = ca.addEntry(secTmp, model.getSecDocName(), SBML_URI);
			masterFiles.add(masterEntry.getPath().getFileName().toString());

			for (PrimaryModelWData primModel : model.getPrimModels()) {

				if (primModel.getModelDoc() != null) {
					// Creates tmp file for this primary model's data
					File numlTmp = File.createTempFile("temp2", "");
					numlTmp.deleteOnExit();

					// Writes data to numlTmp and adds it to the file
					NuMLWriter.write(primModel.getDataDoc(), numlTmp);
					ca.addEntry(numlTmp, primModel.getDataDocName(), NUML_URI);
				}

				// Creates tmp file for the primary model
				File primTmp = File.createTempFile("prim", "");
				primTmp.deleteOnExit();

				// Writes model to primTmp and adds it to the file
				WRITER.write(primModel.getModelDoc(), primTmp);
				ca.addEntry(primTmp, primModel.getModelDocName(), SBML_URI);
			}
		}

		// Adds description with model type
		ModelType modelType = ModelType.TWO_STEP_SECONDARY_MODEL;
		Element metadataAnnotation = new PMFMetadataNode(modelType, masterFiles).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
