package de.bund.bfr.knime.pmm.common.reader;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;

public class TwoStepTertiaryModelReader implements Reader {

	public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

		// Read in models from file
		List<TwoStepTertiaryModel> models;
		if (isPMFX) {
			models = TwoStepTertiaryModelFile.readPMFX(filepath);
		} else {
			models = TwoStepTertiaryModelFile.readPMF(filepath);
		}

		// Creates tuples and adds them to the container
		for (TwoStepTertiaryModel tssm : models) {
			List<KnimeTuple> tuples = parse(tssm);
			for (KnimeTuple tuple : tuples) {
				modelContainer.addRowToTable(tuple);
			}
			exec.setProgress((float) modelContainer.size() / models.size());
		}

		modelContainer.close();

		// Gets template
		FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getTertDoc());
		KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);
		// Creates container with 'fsmrTuple'
		DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		fsmrContainer.addRowToTable(fsmrTuple);
		fsmrContainer.close();

		return new BufferedDataContainer[] { modelContainer, fsmrContainer };
	}

	private List<KnimeTuple> parse(TwoStepTertiaryModel tstm) {

		List<KnimeTuple> secTuples = new LinkedList<>();
		for (SBMLDocument secDoc : tstm.getSecDocs()) {
			secTuples.add(new Model2Tuple(secDoc.getModel()).getTuple());
		}

		List<KnimeTuple> tuples = new LinkedList<>();
		for (PrimaryModelWData pm : tstm.getPrimModels()) {
			KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).getTuple();
			KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).getTuple();
			for (KnimeTuple m2Tuple : secTuples) {
				tuples.add(Util.mergeTuples(dataTuple, m1Tuple, m2Tuple));
			}
		}

		return tuples;
	}
}
