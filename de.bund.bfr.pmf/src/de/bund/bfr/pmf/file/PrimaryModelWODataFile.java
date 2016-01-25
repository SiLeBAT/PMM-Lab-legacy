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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 1b: Primary models without data file.
 * 
 * @author Miguel Alba
 */
public class PrimaryModelWODataFile {
	
	private static final URI SBML_URI = URIFactory.createNuMLURI();
	
	private static final SBMLReader READER = new SBMLReader();
	private static final SBMLWriter WRITER = new SBMLWriter();
	
	public static List<PrimaryModelWOData> read(String filename) throws Exception {
		
		List<PrimaryModelWOData> models = new LinkedList<>();
		
		// Creates CombineArchive
		CombineArchive ca = new CombineArchive(new File(filename));
		
		// Parse models in the PMF file
		List<ArchiveEntry> modelEntries = ca.getEntriesWithFormat(SBML_URI);
		for (ArchiveEntry modelEntry : modelEntries) {
			InputStream stream = Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
			SBMLDocument sbmlDoc = READER.readSBMLFromStream(stream);
			stream.close();
			String sbmlDocName = modelEntry.getFileName();
			PrimaryModelWOData model = new PrimaryModelWOData(sbmlDocName, sbmlDoc);
			models.add(model);
		}
		
		ca.close();
		return models;
	}
	
	public static void write(String dir, String filename, List<PrimaryModelWOData> models) throws Exception {
		
		// Creates CombineArchive name
		String caName = String.format("%s/%s.pmf", dir, filename);
		
		// Removes previous CombineArchive if it exists
		File fileTmp = new File(caName);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}
		
		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(caName));
		
		// Adds models and data
		for (PrimaryModelWOData model : models) {
			// Creates tmp file for the model
			File sbmlTmp = File.createTempFile("temp1", "");
			sbmlTmp.deleteOnExit();
			
			// Writes model to sbmlTmp and adds it to the file
			WRITER.write(model.getDoc(), sbmlTmp);
			ca.addEntry(sbmlTmp,  model.getDocName(), SBML_URI);
		}
		
		// Adds description with model type
		ModelType modelType = ModelType.PRIMARY_MODEL_WODATA;
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));
		
		ca.pack();
		ca.close();
	}
}
