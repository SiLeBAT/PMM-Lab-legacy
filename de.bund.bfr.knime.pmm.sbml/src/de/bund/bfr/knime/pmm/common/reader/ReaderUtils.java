package de.bund.bfr.knime.pmm.common.reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.file.ManualTertiaryModelFile;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.file.PrimaryModelWODataFile;
import de.bund.bfr.pmfml.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.ExperimentalData;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.PrimaryModelWOData;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.Uncertainties;

public class ReaderUtils {

  private ReaderUtils() {}

  /**
   * Parses a list of constraints and returns a dictionary that maps variables and their limit
   * values.
   * 
   * @param constraints
   */
  public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
    Map<String, Limits> paramLimits = new HashMap<>();

    for (Constraint currConstraint : constraints) {
      LimitsConstraint lc = new LimitsConstraint(currConstraint);
      Limits lcLimits = lc.getLimits();
      paramLimits.put(lcLimits.getVar(), lcLimits);
    }

    return paramLimits;
  }

  /**
   * Parses misc items.
   * 
   * @param miscs . Dictionary that maps miscs names and their values.
   * @return
   */
  public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
    PmmXmlDoc cell = new PmmXmlDoc();

    if (miscs != null) {
      // First misc item has id -1 and the rest of items have negative
      // ints
      int counter = -1;
      for (Entry<String, Double> entry : miscs.entrySet()) {
        String name = entry.getKey();
        Double value = entry.getValue();

        List<String> categories;
        String description, unit;

        switch (name) {
          case "Temperature":
            categories = Arrays.asList(Categories.getTempCategory().getName());
            description = name;
            unit = Categories.getTempCategory().getStandardUnit();

            cell.add(new MiscXml(counter, name, description, value, categories, unit));

            counter -= 1;
            break;

          case "pH":
            categories = Arrays.asList(Categories.getPhCategory().getName());
            description = name;
            unit = Categories.getPhUnit();

            cell.add(new MiscXml(counter, name, description, value, categories, unit));

            counter -= 1;
            break;
        }
      }
    }
    return cell;
  }

  /**
   * Creates time series
   */
  public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit,
      String concUnitObjectType, double[][] data) {

    PmmXmlDoc mdData = new PmmXmlDoc();

    Double concStdDev = null;
    Integer numberOfMeasurements = null;

    for (double[] point : data) {
      double conc = point[0];
      double time = point[1];
      String name = "t" + mdData.size();

      TimeSeriesXml t =
          new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
      t.setConcentrationUnitObjectType(concUnitObjectType);
      mdData.add(t);
    }

    return mdData;
  }

  public static EstModelXml uncertainties2EstModel(Uncertainties uncertainties) {
    int estModelId = uncertainties.getID();
    String modelName = uncertainties.getModelName();
    Double sse = uncertainties.getSSE();
    Double rms = uncertainties.getRMS();
    Double r2 = uncertainties.getR2();
    Double aic = uncertainties.getAIC();
    Double bic = uncertainties.getBIC();
    Integer dof = uncertainties.getDOF();
    EstModelXml estModel = new EstModelXml(estModelId, modelName, sse, rms, r2, aic, bic, dof);
    return estModel;
  }

  public static CatalogModelXml model1Rule2CatModel(ModelRule rule) {
    int formulaId = rule.getPmmlabID();
    String formulaName = rule.getFormulaName();
    ModelClass modelClass = rule.getModelClass();

    String formulaString = rule.getFormula();
    formulaString = formulaString.replace("time", "Time");
    formulaString = formulaString.replace("log(", "ln(");

    String formula = String.format("Value=%s", formulaString);

    CatalogModelXml catModel =
        new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
    return catModel;
  }

  public static CatalogModelXml model2Rule2CatModel(ModelRule rule) {
    int formulaId = rule.getPmmlabID();
    String formulaName = rule.getFormulaName();
    ModelClass modelClass = rule.getModelClass();

    String formulaString = rule.getFormula();
    formulaString = formulaString.replace("time", "Time");
    formulaString = formulaString.replace("log(", "ln(");

    String formula = String.format("%s=%s", rule.getVariable(), formulaString);

    CatalogModelXml catModel =
        new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
    return catModel;
  }

  public static KnimeTuple mergeTuples(KnimeTuple dataTuple, KnimeTuple m1Tuple, KnimeTuple m2Tuple) {

    KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

    tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
    tuple.setValue(TimeSeriesSchema.ATT_COMBASEID,
        dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
    tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
    tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
    tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
    tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
    tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
    tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
    tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
    tuple.setValue(TimeSeriesSchema.ATT_METADATA,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

    // Copies model1 columns
    tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
    tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
    tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
    tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
    tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
    tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
    tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
    tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
        m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
    tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
    tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

    // Copies model2 columns
    tuple.setValue(Model2Schema.ATT_MODELCATALOG, m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
    tuple.setValue(Model2Schema.ATT_DEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
    tuple.setValue(Model2Schema.ATT_INDEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
    tuple.setValue(Model2Schema.ATT_PARAMETER, m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
    tuple.setValue(Model2Schema.ATT_ESTMODEL, m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL));
    tuple.setValue(Model2Schema.ATT_MLIT, m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
    tuple.setValue(Model2Schema.ATT_EMLIT, m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
    tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
        m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
    tuple.setValue(Model2Schema.ATT_DBUUID, m2Tuple.getString(Model2Schema.ATT_DBUUID));
    tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
        m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
    tuple.setValue(Model2Schema.ATT_METADATA, m2Tuple.getPmmXml(Model2Schema.ATT_METADATA));

    return tuple;
  }

  public static BufferedDataContainer[] readPMF(String filepath, boolean isPMFX,
      ExecutionContext exec, ModelType modelType) throws Exception {

    Reader reader;
    switch (modelType) {
      case EXPERIMENTAL_DATA:
        reader = new ExperimentalDataReader();
        break;
      case PRIMARY_MODEL_WDATA:
        reader = new PrimaryModelWDataReader();
        break;
      case PRIMARY_MODEL_WODATA:
        reader = new PrimaryModelWODataReader();
        break;
      case TWO_STEP_SECONDARY_MODEL:
        reader = new TwoStepSecondaryModelReader();
        break;
      case ONE_STEP_SECONDARY_MODEL:
        reader = new OneStepSecondaryModelReader();
        break;
      case MANUAL_SECONDARY_MODEL:
        reader = new ManualSecondaryModelReader();
        break;
      case TWO_STEP_TERTIARY_MODEL:
        reader = new TwoStepTertiaryModelReader();
        break;
      case ONE_STEP_TERTIARY_MODEL:
        reader = new OneStepTertiaryModelReader();
        break;
      case MANUAL_TERTIARY_MODEL:
        reader = new ManualTertiaryModelReader();
        break;
      default:
        throw new IllegalArgumentException("Invalid model type: " + modelType);
    }

    return reader.read(filepath, isPMFX, exec);
  }

  /**
   * Reader interface
   * 
   * @author Miguel Alba
   */
  private interface Reader {
    /**
     * Read models from a CombineArchive and returns a Knime table with them
     * 
     * @param isPMFX. If true the reads PMFX file. Else then read PMF file.
     * @throws Exception
     */
    BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception;
  }

  private static class ExperimentalDataReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec dataSpec = SchemaFactory.createDataSchema().createSpec();
      BufferedDataContainer dataContainer = exec.createDataContainer(dataSpec);

      // Reads in experimental data from file
      List<ExperimentalData> eds;
      if (isPMFX) {
        eds = ExperimentalDataFile.readPMFX(filepath);
      } else {
        eds = ExperimentalDataFile.readPMF(filepath);
      }

      // Creates tuples and adds them to the container
      for (ExperimentalData ed : eds) {
        KnimeTuple tuple = new DataTuple(ed.getDoc()).getTuple();
        dataContainer.addRowToTable(tuple);
        exec.setProgress((float) dataContainer.size() / eds.size());
      }

      dataContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          eds.stream().map(ExperimentalData::getDoc).map(FSMRUtils::processData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {dataContainer, fsmrContainer};
    }
  }

  private static class PrimaryModelWDataReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<PrimaryModelWData> models;
      if (isPMFX) {
        models = PrimaryModelWDataFile.readPMFX(filepath);
      } else {
        models = PrimaryModelWDataFile.readPMF(filepath);
      }

      // Creates tuples and adds them to the container
      for (PrimaryModelWData model : models) {
        KnimeTuple tuple = parse(model);
        modelContainer.addRowToTable(tuple);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(PrimaryModelWData::getModelDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private KnimeTuple parse(PrimaryModelWData pm) {
      // Add cells to the row
      KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

      // time series cells
      KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).getTuple();
      row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
      row.setValue(TimeSeriesSchema.ATT_COMBASEID,
          dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
      row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
      row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
      row.setValue(TimeSeriesSchema.ATT_TIMESERIES,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
      row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
      row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
      row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
      row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
      row.setValue(TimeSeriesSchema.ATT_METADATA,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

      // primary model cells
      KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).getTuple();
      row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
      row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
      row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
      row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
      row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
      row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
      row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
      row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
          m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
      row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
      row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));
      return row;
    }
  }

  private static class PrimaryModelWODataReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<PrimaryModelWOData> models;
      if (isPMFX) {
        models = PrimaryModelWODataFile.readPMFX(filepath);
      } else {
        models = PrimaryModelWODataFile.readPMF(filepath);
      }

      // Creates tuples and adds them to the container
      for (PrimaryModelWOData model : models) {
        KnimeTuple tuple = parse(model);
        modelContainer.addRowToTable(tuple);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Creates tuples and adds them to the container
      List<KnimeTuple> fsmrTuples =
          models.stream().map(PrimaryModelWOData::getDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with 'fsmrTuple'
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private KnimeTuple parse(PrimaryModelWOData pm) {
      // Add cells to the row
      KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

      // time series cells
      KnimeTuple dataTuple = new DataTuple(pm.getDoc()).getTuple();
      row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
      row.setValue(TimeSeriesSchema.ATT_COMBASEID,
          dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
      row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
      row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
      row.setValue(TimeSeriesSchema.ATT_TIMESERIES,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
      row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
      row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
      row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
      row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
      row.setValue(TimeSeriesSchema.ATT_METADATA,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

      // primary model cells
      KnimeTuple m1Tuple = new Model1Tuple(pm.getDoc()).getTuple();
      row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
      row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
      row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
      row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
      row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
      row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
      row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
      row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
          m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
      row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
      row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

      return row;
    }
  }

  private static class TwoStepSecondaryModelReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<TwoStepSecondaryModel> models;
      if (isPMFX) {
        models = TwoStepSecondaryModelFile.readPMFX(filepath);
      } else {
        models = TwoStepSecondaryModelFile.readPMF(filepath);
      }

      // Creates tuples and adds them to the container
      for (TwoStepSecondaryModel tssm : models) {
        List<KnimeTuple> tuples = parse(tssm);
        for (KnimeTuple tuple : tuples) {
          modelContainer.addRowToTable(tuple);
        }
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<SBMLDocument> primModels =
          models.stream().map(mod -> mod.getPrimModels().get(0).getModelDoc())
              .collect(Collectors.toList());
      List<KnimeTuple> fsmrTuples =
          primModels.stream().map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR templates
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private List<KnimeTuple> parse(TwoStepSecondaryModel tssm) {
      // create n rows for n secondary models
      List<KnimeTuple> rows = new LinkedList<>();

      KnimeTuple m2Tuple = new Model2Tuple(tssm.getSecDoc().getModel()).getTuple();

      for (PrimaryModelWData pmwd : tssm.getPrimModels()) {
        KnimeTuple dataTuple;
        if (pmwd.getDataDoc() != null) {
          dataTuple = new DataTuple(pmwd.getDataDoc()).getTuple();
        } else {
          dataTuple = new DataTuple(pmwd.getModelDoc()).getTuple();
        }
        KnimeTuple m1Tuple = new Model1Tuple(pmwd.getModelDoc()).getTuple();

        KnimeTuple row = ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple);
        rows.add(row);
      }

      return rows;
    }
  }

  private static class OneStepSecondaryModelReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
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

      // Gets KNIME tuples with the FSMR templates
      List<SBMLDocument> primModels =
          models.stream().map(OneStepSecondaryModel::getModelDoc).collect(Collectors.toList());
      List<KnimeTuple> fsmrTuples =
          primModels.stream().map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with the FSMR templates
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
      List<KnimeTuple> rows = new LinkedList<>();

      // Parses primary model
      KnimeTuple primTuple = new Model1Tuple(ossm.getModelDoc()).getTuple();

      // Parses secondary model
      CompSBMLDocumentPlugin secCompPlugin =
          (CompSBMLDocumentPlugin) ossm.getModelDoc().getPlugin(CompConstants.shortLabel);
      ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
      KnimeTuple secTuple = new Model2Tuple(secModel).getTuple();

      // Parses data files
      for (NuMLDocument numlDoc : ossm.getDataDocs()) {
        KnimeTuple dataTuple = new DataTuple(numlDoc).getTuple();
        rows.add(ReaderUtils.mergeTuples(dataTuple, primTuple, secTuple));
      }

      return rows;
    }
  }

  private static class ManualSecondaryModelReader implements Reader {

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

  private static class TwoStepTertiaryModelReader implements Reader {

    public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec)
        throws Exception {
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

      // Creates tuples and adds them to the container
      List<KnimeTuple> fsmrTuples =
          models.stream().map(TwoStepTertiaryModel::getTertDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
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
          tuples.add(ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple));
        }
      }

      return tuples;
    }
  }

  private static class OneStepTertiaryModelReader implements Reader {

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
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

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
          KnimeTuple tuple = ReaderUtils.mergeTuples(dataTuple, primTuple, secTuple);
          tuple.setValue(TimeSeriesSchema.ATT_CONDID, instanceCounter);
          tuples.add(tuple);
        }
        instanceCounter++;
      }

      return tuples;
    }
  }

  private static class ManualTertiaryModelReader implements Reader {

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

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(ManualTertiaryModel::getTertiaryDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
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

        rows.add(ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple));
      }

      return rows;
    }
  }

}