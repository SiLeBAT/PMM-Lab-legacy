package de.bund.bfr.knime.pmm.common.reader;

import java.util.LinkedList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.file.ManualTertiaryModelFile;
import de.bund.bfr.pmf.model.ManualTertiaryModel;

public class ManualTertiaryModelReader implements Reader {

  public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
      throws Exception {
    // Creates table spec and container
    DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
    BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

    // Read in models from file
    List<ManualTertiaryModel> models;
    if (isPMFX) {
      models = ManualTertiaryModelFile.readPMFX(filepath);
    } else {
      models = ManualTertiaryModelFile.readPMF(filepath);
    }

    // Creates tuples and adds them to the container
    for (ManualTertiaryModel mtm : models) {
      List<KnimeTuple> tuples = parse(mtm);
      tuples.forEach(modelContainer::addRowToTable);
      exec.setProgress((float) modelContainer.size() / models.size());
    }

    modelContainer.close();

    // Gets template
    FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(models.get(0).getTertiaryDoc());
    KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

    // Creates container with 'fsmrTuple'
    DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
    BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
    fsmrContainer.addRowToTable(fsmrTuple);
    fsmrContainer.close();

    return new BufferedDataContainer[] {modelContainer, fsmrContainer};
  }

  private List<KnimeTuple> parse(ManualTertiaryModel mtm) {

		KnimeTuple dataTuple = new DataTuple(mtm.getTertiaryDoc()).getTuple();
		KnimeTuple m1Tuple = new Model1Tuple(mtm.getTertiaryDoc()).getTuple();

		List<KnimeTuple> rows = new LinkedList<>();
		for (SBMLDocument secDoc : mtm.getSecDocs()) {
			KnimeTuple m2Tuple = new Model2Tuple(secDoc.getModel()).getTuple();
			
			EstModelXml estModelXml = (EstModelXml) m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);
			estModelXml.setId(MathUtilities.getRandomNegativeInt());
			m2Tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModelXml));
			
			rows.add(Util.mergeTuples(dataTuple, m1Tuple, m2Tuple));
		}

		return rows;
	}
}
