package de.bund.bfr.knime.pmm.fskx;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.reader.Util;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.Model1Annotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.SBMLFactory;

public class FSKUtil {

  private FSKUtil() {}

  public static KnimeTuple processMetaData(final SBMLDocument sbmlDocument) {
    /**
     * <ol>
     * <li>Gets SBML model
     * <li>Parses annotation
     * <li>Gets rule and converts it to CatalogModelXml
     * <li>Parse constraints
     * <li>Gets species
     * <li>Gets matrix
     * <li>Gets dependent variable
     * <li>Gets limits for the dependent variable
     * <li>Parse independent variables
     * <li>Gets limits for the independent variables
     * <li>Parse independent variables
     * <li>Gets limits for the independent variables
     * <li>Parse constant parameters
     * <li>Creates EstModelXml
     * <li>Creates PMFCompartment
     * <li>Reads model literature
     * <li>Reads estimated model literature
     * <li>Creates and returns tuple
     * <li>Creates PmmXmlDoc with MiscXmls
     * <li>Creates MdInfoXml
     */

    // Gets SBML model
    final Model model = sbmlDocument.getModel();

    // Parses annotation
    final Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

    // Gets rule and converts it to CatalogModelXml
    final ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    final int formulaId = rule.getPmmlabID();
    final String formulaName = rule.getFormulaName();
    final int modelClass = rule.getModelClass().ordinal();
    final CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, null, modelClass);

    // Parse constraints
    final Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

    // Gets species
    final PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
    final AgentXml agentXml = new AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
        species.getDetail(), null);

    // Creates PMFCompartment
    final PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
    final MatrixXml matrixXml = new MatrixXml(MathUtilities.getRandomNegativeInt(),
        compartment.getName(), compartment.getDetail(), null);

    // Gets dependent variable
    final DepXml depXml = new DepXml("Value");
    final String depUnitID = species.getUnits();
    if (depUnitID != null) {
      if (depUnitID.equals("dimensionless")) {
        depXml.setUnit("dimensionless");
        depXml.setCategory("Dimensionless quantity");
      } else {
        final String depUnitName = model.getUnitDefinition(depUnitID).getName();
        depXml.setUnit(depUnitName);
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
        }
      }
    }
    if (species.isSetDescription()) {
      depXml.setDescription(species.getDescription());
    }

    // Gets limits for the dependent variable
    if (limits.containsKey(species.getId())) {
      final Limits depLimits = limits.get(species.getId());
      depXml.setMax(depLimits.getMax());
      depXml.setMin(depLimits.getMin());
    }

    // Parse independent variables
    final Parameter indepParam = model.getParameter(Categories.getTime());
    final IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
    final String indepUnitID = indepParam.getUnits();
    if (!indepUnitID.isEmpty()
        && !indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
      final String unitName = model.getUnitDefinition(indepUnitID).getName();
      indepXml.setUnit(unitName);
      indepXml.setCategory(Categories.getTimeCategory().getName());
      indepXml.setDescription(Categories.getTime());
    }

    // Get limits for the independent variable
    if (limits.containsKey(indepParam.getId())) {
      final Limits indepLimits = limits.get(indepParam.getId());
      indepXml.setMax(indepLimits.getMax());
      indepXml.setMin(indepLimits.getMin());
    }

    // Parse constant parameters
    final PmmXmlDoc paramCell = new PmmXmlDoc();
    for (final Parameter param : model.getListOfParameters()) {
      if (!param.isConstant())
        continue;

      final ParamXml paramXml = new ParamXml(param.getId(), null, param.getValue());

      final String unitID = param.getUnits();
      if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
        final String unitName = model.getUnitDefinition(unitID).getName();
        paramXml.setUnit(unitName);
        if (DBUnits.getDBUnits().containsKey(unitName)) {
          paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
        }
      }

      final PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
      if (coefficient.isSetP()) {
        paramXml.setP(coefficient.getP());
      }
      if (coefficient.isSetError()) {
        paramXml.setError(coefficient.getError());
      }
      if (coefficient.isSetT()) {
        paramXml.setT(coefficient.getT());
      }
      if (coefficient.isSetDescription()) {
        paramXml.setDescription(coefficient.getDescription());
      }
      if (coefficient.isSetCorrelations()) {
        for (Correlation correlation : coefficient.getCorrelations()) {
          paramXml.addCorrelation(correlation.getName(), correlation.getValue());
        }
      }
      // Adds limits
      if (limits.containsKey(param.getId())) {
        final Limits constLimits = limits.get(param.getId());
        paramXml.setMax(constLimits.getMax());
        paramXml.setMin(constLimits.getMin());
      }
      paramCell.add(paramXml);
    }

    // Creates EstModelXml
    final EstModelXml estModel = Util.uncertainties2EstModel(m1Annot.getUncertainties());
    if (model.isSetName()) {
      estModel.setName(model.getName());
    }

    // Reads model literature
    final PmmXmlDoc mLit = new PmmXmlDoc();
    for (final Reference ref : rule.getReferences()) {
      final String author = ref.getAuthor();
      final Integer year = ref.getYear();
      final String title = ref.getTitle();
      final String abstractText = ref.getAbstractText();
      final String journal = ref.getJournal();
      final String volume = ref.getVolume();
      final String issue = ref.getIssue();
      final Integer page = ref.getPage();
      final Integer approvalMode = ref.getApprovalMode();
      final String website = ref.getWebsite();
      final Integer type = ref.isSetType() ? ref.getType().value() : null;
      final String comment = ref.getComment();

      final LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal,
          volume, issue, page, approvalMode, website, type, comment);
      mLit.add(lit);
    }

    // Reads estimated model literature
    final PmmXmlDoc emLit = new PmmXmlDoc();
    for (final Reference ref : rule.getReferences()) {
      final String author = ref.getAuthor();
      final Integer year = ref.getYear();
      final String title = ref.getTitle();
      final String abstractText = ref.getAbstractText();
      final String journal = ref.getJournal();
      final String volume = ref.getVolume();
      final String issue = ref.getIssue();
      final Integer page = ref.getPage();
      final Integer approvalMode = ref.getApprovalMode();
      final String website = ref.getWebsite();
      final Integer type = ref.isSetType() ? ref.getType().value() : null;
      final String comment = ref.getComment();

      final LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal,
          volume, issue, page, approvalMode, website, type, comment);
      emLit.add(lit);
    }

    // Creates PmmXmlDoc with MiscXmls
    final PmmXmlDoc miscCell;
    if (compartment.isSetModelVariables()) {
      final Map<String, Double> miscs = new HashMap<>(compartment.getModelVariables().length);
      for (final ModelVariable modelVariable : compartment.getModelVariables()) {
        miscs.put(modelVariable.getName(), modelVariable.getValue());
      }
      miscCell = Util.parseMiscs(miscs);
    } else {
      miscCell = new PmmXmlDoc();
    }

    // Creates MdInfoXml
    final MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

    // Creates and return tuple
    final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());
    // TimeSeriesSchema cells
    tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
    tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
    tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agentXml));
    tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrixXml));
    tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
    tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
    tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
    tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
    tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
    // Model1Schema cells
    tuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
    tuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
    tuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
    tuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
    tuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
    tuple.setValue(Model1Schema.ATT_MLIT, mLit);
    tuple.setValue(Model1Schema.ATT_EMLIT, emLit);
    tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
    tuple.setValue(Model1Schema.ATT_DBUUID, "?");
    return tuple;
  }

  /**
   * Obtain the libraries used in a list of lines from an R code.
   * 
   * @param lines Lines from an R code.
   * @return List of libraries names.
   */
  public static List<String> extractLibrariesFromLines(final String[] lines) {
    final List<String> libraryNames = new LinkedList<>();

    final String regex1 = "^\\s*\\b(library|require)\\((\"?.+\"?)\\)";
    // final String regex2 = "library|require\((\w)\)";

    final Pattern PATTERN = Pattern.compile(regex1);
    for (final String line : lines) {
      final Matcher matcher = PATTERN.matcher(line);
      if (matcher.find()) {
        final String libraryName = matcher.group(2).replace("\"", "");
        libraryNames.add(libraryName);
      }
    }

    return libraryNames;
  }

  /**
   * Obtain the source files used in a list of lines from an R code.
   * 
   * @param lines Lines from an R code.
   * @return List of source filenames.
   */
  public static List<String> extractSourcesFromLines(final String[] lines) {
    final List<String> sourceNames = new LinkedList<>();

    final String regex1 = "^\\s*\\b(source)\\((\"?.+\"?)\\)";
    // final String regex2 = "library|require\((\w)\)";

    final Pattern PATTERN = Pattern.compile(regex1);
    for (final String line : lines) {
      final Matcher matcher = PATTERN.matcher(line);
      if (matcher.find()) {
        final String sourceName = matcher.group(2).replace("\"", "");
        sourceNames.add(sourceName);
      }
    }

    return sourceNames;
  }

  /**
   * 
   */
  public static String createSimplifiedScript(final String[] lines) {
    final StringBuilder sb = new StringBuilder();
    for (String line : lines) {
      if (!line.startsWith("#")) {
        sb.append(line + '\n');
      }
    }
    return sb.toString();
  }

  public static DataTableSpec createFSKTableSpec() {
    final String[] columnNames = {"origModel", "simpModel", "origParams", "simpParams",
        "origVisualization", "simpVisualization", "RLibraries", "RSources"};
    final DataType[] columnTypes = {StringCell.TYPE, StringCell.TYPE, StringCell.TYPE,
        StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE};
    final DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(columnNames, columnTypes);

    return new DataTableSpec(columnSpecs);
  }
}
