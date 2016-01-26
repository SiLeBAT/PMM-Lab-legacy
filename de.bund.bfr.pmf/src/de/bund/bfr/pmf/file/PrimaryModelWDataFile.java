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

import org.jdom2.Element;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.bund.bfr.pmf.sbml.DataSourceNode;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * @author Miguel Alba
 */
public class PrimaryModelWDataFile {

	private static final URI SBML_URI = URIFactory.createSBMLURI();
	private static final URI NUML_URI = URIFactory.createNuMLURI();
	private static final URI PMF_URI = URIFactory.createPMFURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();

	public static List<PrimaryModelWData> readPMF(String filename) throws Exception {
		return read(filename, SBML_URI);
	}
	
	public static List<PrimaryModelWData> readPMFX(String filename) throws Exception {
		return read(filename, PMF_URI);
	}

	/**
	 * Writes experiments to PrimaryModelWDataFile.
	 */
	public static void writePMF(String dir, String filename, List<PrimaryModelWData> models) throws Exception {
		String caName = String.format("%s/%s.pmf", dir, filename);
		write(caName, SBML_URI, models);
	}

	/**
	 * Writes experiments to PrimaryModelWDataFile.
	 */
	public static void writePMFX(String dir, String filename, List<PrimaryModelWData> models) throws Exception {
		String caName = String.format("%s/%s.pmf", dir, filename);
		write(caName, PMF_URI, models);
	}

	private static List<PrimaryModelWData> read(String filename, URI modelURI) throws Exception {

		List<PrimaryModelWData> models = new LinkedList<>();

		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));

		// Gets data entries
		List<ArchiveEntry> dataEntries = ca.getEntriesWithFormat(NUML_URI);
		HashMap<String, ArchiveEntry> dataEntriesMap = new HashMap<>(dataEntries.size());
		for (ArchiveEntry dataEntry : dataEntries) {
			dataEntriesMap.put(dataEntry.getFileName(), dataEntry);
		}

		// Parse models in the PMF file
		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(modelURI);
		for (ArchiveEntry modelEntry : modelEntries) {

			InputStream stream = Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
			SBMLDocument sbmlDoc = READER.readSBMLFromStream(stream);
			stream.close();

			String sbmlDocName = modelEntry.getFileName();

			// Parse data
			PrimaryModelWData model;
			XMLNode modelAnnotation = sbmlDoc.getModel().getAnnotation().getNonRDFannotation();
			if (modelAnnotation == null) {
				model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
			} else {
				XMLNode metadataNode = modelAnnotation.getChildElement("metadata", "");
				XMLNode node = metadataNode.getChildElement("dataSource", "");

				// this model has no data
				if (node == null) {
					model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
				} else {
					DataSourceNode dataSourceNode = new DataSourceNode(node);
					String dataFileName = dataSourceNode.getFile();
					ArchiveEntry dataEntry = dataEntriesMap.get(dataFileName);
					if (dataEntry == null) {
						model = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
					} else {
						stream = Files.newInputStream(dataEntry.getPath(), StandardOpenOption.READ);
						NuMLDocument numlDoc = NuMLReader.read(stream);
						stream.close();
						model = new PrimaryModelWData(sbmlDocName, sbmlDoc, dataFileName, numlDoc);
					}
				}
			}
			models.add(model);
		}

		ca.close();
		return models;
	}

	private static void write(String filename, URI modelURI, List<PrimaryModelWData> models) throws Exception {

		// Removes previous CombineArchive if it exists
		File fileTmp = new File(filename);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}

		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));
		
		// Add models and data
		for (PrimaryModelWData model : models) {
			// Adds data set
			if (model.getDataDoc() != null) {
				// Creates tmp file for the model
				File numlTmp = File.createTempFile("temp2", "");
				numlTmp.deleteOnExit();

				// Writes data to numlTmp andd adds it to the file
				NuMLWriter.write(model.getDataDoc(), numlTmp);
				ca.addEntry(numlTmp, model.getDataDocName(), NUML_URI);
			}

			// Creates tmp file for the model
			File sbmlTmp = File.createTempFile("temp1", "");
			sbmlTmp.deleteOnExit();

			// Writes model to sbmlTmp and adds it to the file
			WRITER.write(model.getModelDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp, model.getModelDocName(), modelURI);
		}

		// Adds description with model type
		ModelType modelType = ModelType.PRIMARY_MODEL_WDATA;
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));

		ca.pack();
		ca.close();
	}
}
