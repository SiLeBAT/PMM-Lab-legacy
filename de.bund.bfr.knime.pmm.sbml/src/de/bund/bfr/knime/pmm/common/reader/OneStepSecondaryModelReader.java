package de.bund.bfr.knime.pmm.common.reader;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;

public class OneStepSecondaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Reads in models from file
		List<OneStepSecondaryModel> models;
		if (isPMFX) {
			models = OneStepSecondaryModelFile.readPMFX(filepath);
		} else {
			models = OneStepSecondaryModelFile.readPMF(filepath);
		}

		// Creates tuples and adds them to the container
		for (OneStepSecondaryModel ossm : models) {
			List<KnimeTuple> tuples = parse(ossm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template of the first primary model file
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getModelDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
		List<KnimeTuple> rows = new LinkedList<>();

		// Parses primary model
		KnimeTuple primTuple = new Model1Tuple(ossm.getModelDoc()).getTuple();

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) ossm.getModelDoc()
				.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		KnimeTuple secTuple = new Model2Tuple(secModel).getTuple();

		// Parses data files
		for (NuMLDocument numlDoc : ossm.getDataDocs()) {
			KnimeTuple dataTuple = new DataTuple(numlDoc).getTuple();
			rows.add(Util.mergeTuples(dataTuple, primTuple, secTuple));
		}

		return rows;
	}
}