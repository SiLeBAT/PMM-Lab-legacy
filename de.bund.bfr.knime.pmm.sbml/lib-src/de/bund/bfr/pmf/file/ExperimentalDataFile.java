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

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.NuMLReader;
import de.bund.bfr.pmf.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 * Case 0: Experimental data file.
 * 
 * @author Miguel Alba
 */
public class ExperimentalDataFile {

	private static final URI numlURI = URIFactory.createNuMLURI();
	
	private ExperimentalDataFile() {}
	
	public static List<ExperimentalData> read(String filename) throws Exception {
		
		List<ExperimentalData> dataRecords = new LinkedList<>();
		
		// Creates Combine Archive
		CombineArchive ca = new CombineArchive(new File(filename));
		
		for (ArchiveEntry entry : ca.getEntriesWithFormat(numlURI)) {
			InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
			String docName = entry.getFileName();
			NuMLDocument doc = NuMLReader.read(stream);
			stream.close();

			ExperimentalData ed = new ExperimentalData(docName, doc);
			dataRecords.add(ed);
		}
		
		ca.close();
		return dataRecords;
	}
	
	public static void write(String dir, String filename, List<ExperimentalData> dataRecords) throws Exception {
		
		// Creates name for the CombineArchive
		String caName = String.format("%s/%s.pmf", dir, filename);
		
		// Removes previous CombineArchive if it exists
		File fileTmp = new File(caName);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}
		
		// Creates new CombineArchive
		CombineArchive ca = new CombineArchive(new File(caName));
		
		// Add dataRecords
		for (int i = 0; i < dataRecords.size(); i++) {
			ExperimentalData ed = dataRecords.get(i);

			// Creates tmp file for ed
			File numlTmp = File.createTempFile("numlTmp", "");
			numlTmp.deleteOnExit();
			
			// Writes data to numlTmp and adds it to the PMF file (ca)
			NuMLWriter.write(ed.getDoc(), numlTmp);
			ca.addEntry(numlTmp, ed.getDocName(), numlURI);
		}
		
		ModelType modelType = ModelType.EXPERIMENTAL_DATA;
		Element metadataAnnotation = new PMFMetadataNode(modelType, new HashSet<String>(0)).node;
		ca.addDescription(new DefaultMetaDataObject(metadataAnnotation));
		
		ca.pack();
		ca.close();
	}
}
