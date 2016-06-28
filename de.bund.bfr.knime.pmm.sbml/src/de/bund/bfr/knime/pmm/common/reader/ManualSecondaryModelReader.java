package de.bund.bfr.knime.pmm.common.reader;

import java.util.List;
import java.util.stream.Collectors;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;

public class ManualSecondaryModelReader implements Reader {

  public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
      throws Exception {
    // Creates table spec and container
    DataTableSpec modelSpec = SchemaFactory.createM2Schema().createSpec();
    BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

    // Reads in models from file
    List<ManualSecondaryModel> models;
    if (isPMFX) {
      models = ManualSecondaryModelFile.readPMFX(filepath);
    } else {
      models = ManualSecondaryModelFile.readPMF(filepath);
    }

    // Creates tuples and adds them to the container
    for (ManualSecondaryModel model : models) {
      KnimeTuple tuple = new Model2Tuple(model.getDoc().getModel()).getTuple();
      modelContainer.addRowToTable(tuple);
      exec.setProgress((float) modelContainer.size() / models.size());
    }

    modelContainer.close();

    // Creates tuples and adds them to the container
    List<KnimeTuple> fsmrTuples =
        models.stream().map(ManualSecondaryModel::getDoc)
            .map(FSMRUtils::processModelWithoutMicrobialData)
            .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());
    
    // Creates cotnainer with OpenFSMR tuples
    DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
    BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
    fsmrTuples.forEach(fsmrContainer::addRowToTable);
    fsmrContainer.close();

    return new BufferedDataContainer[] {modelContainer, fsmrContainer};
  }
}
