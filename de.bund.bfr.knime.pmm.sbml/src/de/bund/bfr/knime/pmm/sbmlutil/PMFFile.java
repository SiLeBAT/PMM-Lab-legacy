/**
 * PMF File.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLWriter;

import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.CombineArchive;

public class PMFFile {

	public static void write(String dir, String filename,
			List<Experiment> exps, ExecutionContext exec) throws Exception {

		// Create Combine Archive name
		String caName = String.format("%s/%s.pmf", dir, filename);

		// Remove previous Combine Archive if it exists
		File fileTemp = new File(caName);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		// Create new Combine Archive
		CombineArchive ca = new CombineArchive(new File(caName));

		// Create SBML writer
		SBMLWriter sbmlWriter = new SBMLWriter();
		sbmlWriter.setProgramName("SBML Writer node");
		sbmlWriter.setProgramVersion("1.0");

		// Create NuML writer
		NuMLWriter numlWriter = new NuMLWriter();

		// Create SBML and NuML URIs
		URI sbmlURI = new URI(
				"http://identifiers.org/combine/specifications/sbml");
		URI numlURI = new URI("http://numl.googlecode/svn/trunk/NUMLSchema.xsd");

		// Add model and data
		short counter = 0;
		for (Experiment exp : exps) {
			// Add data set
			if (exp.getData() != null) {
				// Create temp file for the model
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();
				
				// Create data file name
				String dataName = String.format("%s_%s.numl", filename, counter);
				
				// Write data to temp file and add it to the PMF file
				numlWriter.write(exp.getData(), numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);
				
				// Add data source node to the model
				DataSourceNode node = new DataSourceNode(dataName);
				exp.getModel().getModel().getAnnotation().getNonRDFannotation().addChild(node.getNode());
			}
			
			// Create temp file for the model
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();
			
			// Create model file name
			String mdName = String.format("%s_%d.sbml", filename, counter);
			
			// Write model to temp file and add it to the PMF file
			sbmlWriter.write(exp.getModel(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);
			
			// Increment counter and update progress bar
			counter++;
			exec.setProgress((float) counter / exps.size());
		}
		ca.pack();
		ca.close();
	}
}
