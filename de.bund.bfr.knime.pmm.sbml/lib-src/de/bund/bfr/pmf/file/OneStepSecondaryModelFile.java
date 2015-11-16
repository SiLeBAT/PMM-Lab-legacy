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

import org.jdom2.Element;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 2b: One step secondary model file. Secondary models generated
 * "implicitly" during 1-step fitting of tertiary models.
 * 
 * @author Miguel Alba
 */
public class OneStepSecondaryModelFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI NuML_URI = URIFactory.createNuMLURI();

	public static List<OneStepSecondaryModel> read(String filename) throws Exception {

		List<OneStepSecondaryModel> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Get data entries
		List<ArchiveEntry> dataEntries = ca.getEntriesWithFormat(NuML_URI);
		Map<String, NuMLDocument> dataEntriesMap = new HashMap<>(dataEntries.size());
		for (ArchiveEntry entry : dataEntries) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			NuMLDocument doc = NuMLReader.read(stream);
			stream.close();
			dataEntriesMap.put(entry.getFileName(), doc);
		}

		for (ArchiveEntry entry : ca.getEntriesWithFormat(SBML_URI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			String docName = entry.getFileName();
			SBMLDocument doc = SBMLReader.readSBMLFromStream(stream);
			stream.close();

			// look for DataSourceNode
			CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
			ModelDefinition md = secCompPlugin.getModelDefinition(0);
			XMLNode m2Annot = md.getAnnotation().getNonRDFannotation();
			XMLNode metadata = m2Annot.getChildElement("metadata", "");

			List<String> numlDocNames = new LinkedList<>();
			List<NuMLDocument> numlDocs = new LinkedList<>();

			for (XMLNode node : metadata.getChildElements("dataSource", "")) {
				DataSourceNode dsn = new DataSourceNode(node);
				String dataFileName = dsn.getFile();

				numlDocNames.add(dataFileName);
				numlDocs.add(dataEntriesMap.get(dataFileName));
			}

			OneStepSecondaryModel ossm = new OneStepSecondaryModel(docName, doc, numlDocNames, numlDocs);
			models.add(ossm);
		}
		ca.close();
		return models;
	}

	/**
	 */
	public static void write(String dir, String filename, List<OneStepSecondaryModel> models) throws Exception {

		// Creates CombineArchive name
		String caName = String.format("%s/%s.pmf", dir, filename);

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(caName);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Add models and data
		for (OneStepSecondaryModel model : models) {

			for (int i = 0; i < model.getDataDocs().size(); i++) {
				NuMLDocument numlDoc = model.getDataDocs().get(i);
				String numlDocName = model.getDataDocNames().get(i);

				// Creates tmp file for the NuML model
				File numlTmp = File.createTempFile("numl", "");
				numlTmp.deleteOnExit();

				// Writes model to numlTp and addsit to the file
				NuMLWriter.write(numlDoc, numlTmp);
				ca.addEntry(numlTmp, numlDocName, NuML_URI);
			}

			// Creates tmp file for the SBML model
			File sbmlTmp = File.createTempFile("sbml", "");
			sbmlTmp.deleteOnExit();

			// Writes model to secTmp and adds it to the file
			SBMLWriter.write(model.getModelDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp, model.getModelDocName(), SBML_URI);
		}

		// Adds description with model type
		ModelType modelType = ModelType.ONE_STEP_SECONDARY_MODEL;
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
