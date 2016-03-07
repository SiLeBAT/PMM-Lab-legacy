package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.ext.comp.Submodel;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.pmf.file.ManualTertiaryModelFile;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.bund.bfr.pmf.sbml.Metadata;

public class ManualTertiaryModelParser implements Parser {

  @Override
  public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName,
      Metadata metadata, boolean splitModels, String notes, ExecutionContext exec)
          throws Exception {

    List<ManualTertiaryModel> tms = new LinkedList<>();

    // Sort global models
    Map<Integer, Map<Integer, List<KnimeTuple>>> gms = TableReader.sortGlobalModels(tuples);

    // Parse tertiary models
    int modelCounter = 0;
    for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
      List<List<KnimeTuple>> tuplesList = new LinkedList<>();
      for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
        tuplesList.add(tertiaryInstance);
      }
      // We have a list of tertiary instances. Each instance has the same
      // microbial data yet different data. Then we'll create a
      // TwoTertiaryModel from the first instance and create the data from
      // every instance.
      ManualTertiaryModel tm = parse(tuplesList, isPMFX, mdName, modelCounter, metadata, notes);
      tms.add(tm);

      modelCounter++;
    }

    if (isPMFX) {
      if (splitModels) {
        for (int numModel = 0; numModel < tms.size(); numModel++) {
          String modelName = mdName + Integer.toString(numModel);
          List<ManualTertiaryModel> model = new LinkedList<>();
          model.add(tms.get(numModel));
          ManualTertiaryModelFile.writePMFX(dir, modelName, model);
        }
      } else {
        ManualTertiaryModelFile.writePMFX(dir, mdName, tms);
      }
    } else {
      if (splitModels) {
        for (int numModel = 0; numModel < tms.size(); numModel++) {
          String modelName = mdName + Integer.toString(numModel);
          List<ManualTertiaryModel> model = new LinkedList<>();
          model.add(tms.get(numModel));
          ManualTertiaryModelFile.writePMF(dir, modelName, model);
        }
      } else {
        ManualTertiaryModelFile.writePMF(dir, mdName, tms);
      }
    }
  }

  private static ManualTertiaryModel parse(List<List<KnimeTuple>> tupleList, boolean isPMFX,
      String mdName, int modelNum, Metadata metadata, String notes) {

    final String modelExtension = isPMFX ? "pmf" : "sbml";

    // We'll get microbial data from the first instance
    List<KnimeTuple> firstInstance = tupleList.get(0);
    // and the primary model from the first tuple
    KnimeTuple firstTuple = firstInstance.get(0);

    // Creates SBMLDocument for the tertiary model
    Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
    SBMLDocument tertDoc = m1Parser.getDocument();
    String tertDocName = String.format("%s_%s.%s", mdName, modelNum, modelExtension);

    CompSBMLDocumentPlugin compDocPlugin =
        (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
    CompModelPlugin compModelPlugin =
        (CompModelPlugin) tertDoc.getModel().getPlugin(CompConstants.shortLabel);

    // Add submodels and model definitions
    List<String> secDocNames = new LinkedList<>();
    List<SBMLDocument> secDocs = new LinkedList<>();

    for (int i = 0; i < firstInstance.size(); i++) {
      KnimeTuple tuple = firstInstance.get(i);

      Model2Parser m2Parser = new Model2Parser(tuple, metadata, notes);
      SBMLDocument secDoc = m2Parser.getDocument();

      String emdId = secDoc.getModel().getId();
      String secDocName = String.format("%s_%s_%s.%s", mdName, modelNum, emdId, modelExtension);

      secDocNames.add(secDocName);
      secDocs.add(secDoc);

      // Creates ExternalModelDefinition
      ExternalModelDefinition emd =
          new ExternalModelDefinition(emdId, TableReader.LEVEL, TableReader.VERSION);
      emd.setSource(secDocName);
      emd.setModelRef(emdId);

      compDocPlugin.addExternalModelDefinition(emd);

      Submodel submodel = compModelPlugin.createSubmodel(emdId);
      submodel.setModelRef(emdId);

      String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();
      Parameter parameter = tertDoc.getModel().getParameter(depId);

      CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
      ReplacedBy replacedBy = plugin.createReplacedBy();
      replacedBy.setIdRef(depId);
      replacedBy.setSubmodelRef(emdId);
    }

    ManualTertiaryModel mtm = new ManualTertiaryModel(tertDocName, tertDoc, secDocNames, secDocs);
    return mtm;
  }
}
