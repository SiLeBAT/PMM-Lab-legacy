package de.bund.bfr.knime.pmm.common.reader;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;

public class OneStepTertiaryModelReader implements Reader {

  public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
      throws Exception {
    // Creates table spec and container
    DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
    BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

    // Read in models from file
    List<OneStepTertiaryModel> models;
    if (isPMFX) {
      models = OneStepTertiaryModelFile.readPMFX(filepath);
    } else {
      models = OneStepTertiaryModelFile.readPMF(filepath);
    }

    // Creates tuples and adds them to the container
    for (OneStepTertiaryModel ostm : models) {
      List<KnimeTuple> tuples = parse(ostm);
      for (KnimeTuple tuple : tuples) {
        modelContainer.addRowToTable(tuple);
      }
      exec.setProgress((float) modelContainer.size() / models.size());
    }

    modelContainer.close();

    // Gets KNIME tuples with the FSMR templates
    List<KnimeTuple> fsmrTuples =
        models.stream().map(OneStepTertiaryModel::getTertiaryDoc)
            .map(FSMRUtils::processModelWithMicrobialData).map(FSMRUtils::createTupleFromTemplate)
            .collect(Collectors.toList());
    
    // Creates container with OpenFSMR tuples
    DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
    BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
    fsmrTuples.forEach(fsmrContainer::addRowToTable);
    fsmrContainer.close();

    return new BufferedDataContainer[] {modelContainer, fsmrContainer};
  }

  private List<KnimeTuple> parse(OneStepTertiaryModel ostm) {

    KnimeTuple primTuple = new Model1Tuple(ostm.getTertiaryDoc()).getTuple();
    List<KnimeTuple> secTuples = new LinkedList<>();
    for (SBMLDocument secDoc : ostm.getSecDocs()) {
      secTuples.add(new Model2Tuple(secDoc.getModel()).getTuple());
    }

    List<KnimeTuple> tuples = new LinkedList<>();

    int instanceCounter = 1;

    for (NuMLDocument numlDoc : ostm.getDataDocs()) {
      KnimeTuple dataTuple = new DataTuple(numlDoc).getTuple();
      for (KnimeTuple secTuple : secTuples) {
        KnimeTuple tuple = Util.mergeTuples(dataTuple, primTuple, secTuple);
        tuple.setValue(TimeSeriesSchema.ATT_CONDID, instanceCounter);
        tuples.add(tuple);
      }
      instanceCounter++;
    }

    return tuples;
  }
}
