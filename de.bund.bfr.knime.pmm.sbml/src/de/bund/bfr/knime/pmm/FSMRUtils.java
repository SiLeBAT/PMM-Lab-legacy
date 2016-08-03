/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.util.filters.Filter;

import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.bund.bfr.pmfml.numl.ConcentrationOntology;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.numl.ResultComponent;
import de.bund.bfr.pmfml.numl.TimeOntology;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.PMFUnitDefinition;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class FSMRUtils {

  public static FSMRTemplate processData(NuMLDocument doc) {
    return new DataTemplateCreator(doc).createTemplate();
  }

  public static FSMRTemplate processModelWithMicrobialData(SBMLDocument doc) {
    return new PrimaryModelTemplateCreator(doc).createTemplate();
  }

  // Secondary models
  public static FSMRTemplate processTwoStepSecondaryModel(TwoStepSecondaryModel model) {
    return new TwoStepSecondaryModelTemplateCreator(model).createTemplate();
  }

  public static FSMRTemplate processOneStepSecondaryModel(OneStepSecondaryModel model) {
    return new OneStepSecondaryModelTemplateCreator(model).createTemplate();
  }

  public static FSMRTemplate processManualSecondaryModel(ManualSecondaryModel model) {
    return new ManualSecondaryModelTemplateCreator(model).createTemplate();
  }

  // Tertiary models
  public static FSMRTemplate processTwoStepTertiaryModel(TwoStepTertiaryModel model) {
    return new TwoStepTertiaryModelTemplateCreator(model).createTemplate();
  }

  public static FSMRTemplate processOneStepTertiaryModel(OneStepTertiaryModel model) {
    return new OneStepTertiaryModelTemplateCreator(model).createTemplate();
  }

  public static FSMRTemplate processManualTertiaryModel(ManualTertiaryModel model) {
    return new ManualTertiaryModelTemplateCreator(model).createTemplate();
  }

  public static KnimeTuple createTupleFromTemplate(FSMRTemplate template) {
    KnimeTuple tuple = new KnimeTuple(new OpenFSMRSchema());

    if (template.isSetModelName()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_NAME, template.getModelName());
    }

    if (template.isSetModelId()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_ID, template.getModelId());
    }

    if (template.isSetModelLink()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_LINK, template.getModelLink().toString());
    }

    if (template.isSetOrganismName()) {
      tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_NAME, template.getOrganismName());
    }

    if (template.isSetOrganismDetails()) {
      tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL, template.getOrganismDetails());
    }

    if (template.isSetMatrixName()) {
      tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_NAME, template.getMatrixName());
    }

    if (template.isSetMatrixDetails()) {
      tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL, template.getMatrixDetails());
    }

    if (template.isSetCreator()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATOR, template.getCreator());
    }

    if (template.isSetFamilyName()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_FAMILY_NAME, template.getFamilyName());
    }

    if (template.isSetContact()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CONTACT, template.getContact());
    }

    if (template.isSetReferenceDescription()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION,
          template.getReferenceDescription());
    }

    if (template.isSetReferenceDescriptionLink()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK,
          template.getReferenceDescriptionLink().toString());
    }

    if (template.isSetCreatedDate()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATED_DATE, template.getCreatedDate().toString());
    }

    if (template.isSetModifiedDate()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE, template.getModifiedDate().toString());
    }

    if (template.isSetRights()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_RIGHTS, template.getRights());
    }

    if (template.isSetNotes()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_NOTES, template.getNotes());
    }

    if (template.isSetCurationStatus()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS, template.getCurationStatus());
    }

    if (template.isSetModelType()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_TYPE, template.getModelType().toString());
    }

    if (template.isSetModelSubject()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_SUBJECT, template.getModelSubject().toString());
    }

    if (template.isSetFoodProcess()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS, template.getFoodProcess());
    }

    if (template.isSetDependentVariable()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE, template.getDependentVariable());
    }

    if (template.isSetDependentVariableUnit()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT,
          template.getDependentVariableUnit());
    }

    if (template.isSetDependentVariableMin()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN,
          template.getDependentVariableMin());
    }

    if (template.isSetDependentVariableMax()) {
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX,
          template.getDependentVariableMax());
    }

    if (template.isSetIndependentVariables()) {
      // Join independent variables with commas. E.g. "time,temperature"
      String formattedVars =
          Arrays.stream(template.getIndependentVariables()).collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE, formattedVars);
    }

    if (template.isSetIndependentVariablesUnits()) {
      String formattedUnits =
          Arrays.stream(template.getIndependentVariablesUnits()).collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS, formattedUnits);
    }

    if (template.isSetIndependentVariablesMins()) {
      String formattedMins = Arrays.stream(template.getIndependentVariablesMins())
          .mapToObj(Double::toString).collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS, formattedMins);
    }

    if (template.isSetIndependentVariablesMaxs()) {
      String formattedMaxs = Arrays.stream(template.getIndependentVariablesMaxs())
          .mapToObj(Double::toString).collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS, formattedMaxs);
    }

    if (template.isSetHasData()) {
      tuple.setValue(OpenFSMRSchema.ATT_HAS_DATA, template.getHasData() ? 1 : 0);
    }

    return tuple;
  }
}

abstract class TemplateCreator {
  protected FSMRTemplate template = new FSMRTemplateImpl();

  public final FSMRTemplate createTemplate() {
    setModelId();
    setModelName();
    setOrganismData();
    setMatrixData();
    setMetadata();
    setModelSubject();
    setModelNotes();
    setDependentVariableData();
    setIndependentVariableData();
    setHasData();

    return template;
  }

  abstract void setModelId();

  abstract void setModelName();

  abstract void setOrganismData();

  abstract void setMatrixData();

  abstract void setMetadata();

  abstract void setModelSubject();

  abstract void setModelNotes();

  abstract void setDependentVariableData();

  abstract void setIndependentVariableData();

  abstract void setHasData();

  // util methods
  protected static List<Limits> getLimitsFromModel(Model model) {
    return model.getListOfConstraints().stream().map(LimitsConstraint::new)
        .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  void setOrganismDataFromSpecies(final Species species) {
    PMFSpecies pmfSpecies = SBMLFactory.createPMFSpecies(species);

    if (species.isSetName())
      template.setOrganismName(species.getName());
    if (pmfSpecies.isSetDetail())
      template.setOrganismDetails(pmfSpecies.getDetail());
  }

  void setMatrixDataFromCompartment(final Compartment compartment) {
    PMFCompartment pmfCompartment = SBMLFactory.createPMFCompartment(compartment);

    if (compartment.isSetName())
      template.setMatrixName(compartment.getName());
    if (pmfCompartment.isSetDetail())
      template.setMatrixDetails(pmfCompartment.getDetail());
  }

  void setNotesFromModel(final Model model) {
    if (model.isSetNotes()) {
      try {
        String htmlString = model.getNotesString();
        Document htmlDoc = Jsoup.parse(htmlString);
        String notes = htmlDoc.text();
        template.setNotes(notes);
      } catch (XMLStreamException error) {
        System.err.println("error accessing the notes of " + model);
        error.printStackTrace();
      }
    }
  }

  void setMetaDataFromAnnotation(final Annotation annotation) {
    Metadata metadata = new MetadataAnnotation(annotation).getMetadata();

    if (metadata.isSetGivenName())
      template.setCreator(metadata.getGivenName());

    if (metadata.isSetFamilyName())
      template.setFamilyName(metadata.getFamilyName());

    if (metadata.isSetContact())
      template.setContact(metadata.getContact());

    if (metadata.isSetReferenceLink()) {
      String referenceLinkAsString = metadata.getReferenceLink();
      try {
        URL referenceLinkAsURL = new URL(referenceLinkAsString);
        template.setReferenceDescriptionLink(referenceLinkAsURL);
      } catch (MalformedURLException e) {
        System.err.println(referenceLinkAsString + " is not a valid URL");
        e.printStackTrace();
      }
    }

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

    if (metadata.isSetCreatedDate()) {
      String createdDateAsString = metadata.getCreatedDate();

      try {
        Date createdDate = dateFormat.parse(createdDateAsString);
        template.setCreatedDate(createdDate);
      } catch (ParseException e) {
        System.err.println(createdDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }

    if (metadata.isSetModifiedDate()) {
      String modifiedDateAsString = metadata.getModifiedDate();

      try {
        Date modifiedDate = dateFormat.parse(modifiedDateAsString);
        template.setModifiedDate(modifiedDate);
      } catch (ParseException e) {
        System.err.println(modifiedDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }

    if (metadata.isSetRights())
      template.setRights(metadata.getRights());

    if (metadata.isSetType())
      template.setModelType(metadata.getType());
  }

  void setDependentVariableData(final Model model, final List<Limits> limits) {

    Species species = model.getSpecies(0);

    if (species.isSetUnits()) {
      String unitId = species.getUnits();

      // Sets unit
      String unitName = model.getUnitDefinition(unitId).getName();
      template.setDependentVariableUnit(unitName);

      // Sets variable
      if (!unitId.equals("dimensionless") && DBUnits.getDBUnits().containsKey(unitName)) {
        String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
        template.setDependentVariable(unitCategory);
      }

      // Sets minimum and maximum values
      for (Limits lim : limits) {
        if (lim.getVar().equals(species.getId())) {
          if (lim.getMin() != null)
            template.setDependentVariableMin(lim.getMin());
          if (lim.getMax() != null)
            template.setDependentVariableMax(lim.getMax());
          break;
        }
      }
    }
  }
}


// TODO: rename to DataTemplateCreator once the original class is removed
class DataTemplateCreator extends TemplateCreator {

  private NuMLDocument doc;

  DataTemplateCreator(final NuMLDocument doc) {
    this.doc = doc;
  }

  @Override
  void setModelId() {
    // TODO: set model id
  }

  @Override
  void setModelName() {
    // TODO: set model name
  }

  @Override
  void setOrganismData() {
    ConcentrationOntology ontology = doc.getConcentrationOntologyTerm();
    Species species = ontology.getSpecies().getSpecies();
    setOrganismDataFromSpecies(species);
  }

  @Override
  void setMatrixData() {
    ConcentrationOntology ontology = doc.getConcentrationOntologyTerm();
    Compartment compartment = ontology.getCompartment().getCompartment();
    setMatrixDataFromCompartment(compartment);
  }

  @Override
  void setMetadata() {
    ResultComponent rc = doc.getResultComponent();

    // Creator
    if (rc.isSetCreatorGivenName())
      template.setCreator(rc.getCreatorGivenName());

    // Family name
    if (rc.isSetCreatorFamilyName())
      template.setFamilyName(rc.getCreatorFamilyName());

    // Contact
    if (rc.isSetCreatorContact())
      template.setContact(rc.getCreatorContact());

    // No link to reference description

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

    // Created date
    if (rc.isSetCreatedDate()) {
      String dateAsString = rc.getCreatedDate();
      try {
        Date date = dateFormat.parse(dateAsString);
        template.setCreatedDate(date);
      } catch (ParseException error) {
        System.err.println(dateAsString + " is not a valid date");
        error.printStackTrace();
      }
    }

    // Modified date
    if (rc.isSetModifiedDate()) {
      String dateAsString = rc.getModifiedDate();
      try {
        Date date = dateFormat.parse(dateAsString);
        template.setModifiedDate(date);
      } catch (ParseException error) {
        System.err.println(dateAsString + " is not a valid date");
        error.printStackTrace();
      }
    }

    // Model rights
    if (rc.isSetRights())
      template.setRights(rc.getRights());

    // Model type
    template.setModelType(ModelType.EXPERIMENTAL_DATA);
  }

  @Override
  void setModelSubject() {
    template.setModelSubject(ModelClass.UNKNOWN);
  }

  @Override
  void setModelNotes() {
    // TODO: setModelNotes
  }

  @Override
  void setDependentVariableData() {
    ConcentrationOntology ontology = doc.getConcentrationOntologyTerm();
    PMFUnitDefinition unitDef = ontology.getUnitDefinition();

    // Sets unit
    String unitName = unitDef.getName();
    template.setDependentVariableUnit(unitName);

    // Sets variable
    if (!unitDef.getId().equals("dimensionless") && DBUnits.getDBUnits().containsKey(unitName)) {
      String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
      template.setDependentVariable(unitCategory);
    }

    /**
     * NuMLDocument does not keep the limits of the variables, so it is not possible to retrieve the
     * min & max values of the dependent variable.
     */
  }

  @Override
  void setIndependentVariableData() {
    TimeOntology ontology = doc.getTimeOntologyTerm();
    PMFUnitDefinition unitDef = ontology.getUnitDefinition();

    // Sets unit
    String unitName = unitDef.getName();
    template.setIndependentVariablesUnits(new String[] {unitName});

    // Sets variable
    if (DBUnits.getDBUnits().containsKey(unitName)) {
      String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
      template.setIndependentVariables(new String[] {unitCategory});
    }

    /**
     * NuMLDocument does not keep the limits of the variables, so it is not possible to retrieve the
     * min & max values of the dependent variable.
     */
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class PrimaryModelTemplateCreator extends TemplateCreator {

  private SBMLDocument doc;
  private final List<Limits> limits;


  PrimaryModelTemplateCreator(final SBMLDocument doc) {
    this.doc = doc;

    // Caches limits
    limits = doc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
        .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  void setModelId() {
    template.setModelId(doc.getModel().getId());
  }

  @Override
  void setModelName() {
    Model model = doc.getModel();
    if (model.isSetName()) {
      template.setModelName(model.getName());
    }
  }

  @Override
  void setOrganismData() {
    setOrganismDataFromSpecies(doc.getModel().getSpecies(0));
  }

  @Override
  void setMatrixData() {
    setMatrixDataFromCompartment(doc.getModel().getCompartment(0));
  }

  @Override
  void setMetadata() {
    setMetaDataFromAnnotation(doc.getAnnotation());
  }

  @Override
  void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) doc.getModel().getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  void setModelNotes() {
    setNotesFromModel(doc.getModel());
  }

  @Override
  void setDependentVariableData() {
    setDependentVariableData(doc.getModel(), limits);
  }

  @Override
  void setIndependentVariableData() {
    Model model = doc.getModel();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        return !((Parameter) o).isConstant();
      }
    });

    final int numParams = indepParams.size();
    String[] units = new String[numParams];
    String[] categories = new String[numParams];
    double[] mins = new double[numParams];
    double[] maxs = new double[numParams];

    for (int i = 0; i < numParams; i++) {
      Parameter param = indepParams.get(i);
      units[i] = param.getUnits();

      // Sets independent variable unit
      String unitName = model.getUnitDefinition(units[i]).getName();
      if (!units[i].equals("dimensionless") && DBUnits.getDBUnits().containsKey(unitName)) {
        categories[i] = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
      } else {
        categories[i] = " ";
      }

      // Sets minimum and maximum value
      for (Limits lcLimits : limits) {
        if (lcLimits.getVar().equals(param.getId())) {
          mins[i] = lcLimits.getMin();
          maxs[i] = lcLimits.getMax();
        }
      }
    }

    template.setIndependentVariables(categories);
    template.setIndependentVariablesUnits(units);
    template.setIndependentVariablesMins(mins);
    template.setIndependentVariablesMaxs(maxs);
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class TwoStepSecondaryModelTemplateCreator extends TemplateCreator {

  private final SBMLDocument secModelDoc;
  private final SBMLDocument primModelDoc;

  private final List<Limits> secModelLimits;
  private final List<Limits> primModelLimits;

  TwoStepSecondaryModelTemplateCreator(TwoStepSecondaryModel model) {
    secModelDoc = model.getSecDoc();
    primModelDoc = model.getPrimModels().get(0).getModelDoc();

    // Caches limits
    primModelLimits = getLimitsFromModel(primModelDoc.getModel());
    secModelLimits = getLimitsFromModel(secModelDoc.getModel());
  }

  @Override
  void setModelId() {
    if (secModelDoc.getModel().isSetId())
      template.setModelId(secModelDoc.getModel().getId());
  }

  @Override
  void setModelName() {
    if (secModelDoc.getModel().isSetName())
      template.setModelName(secModelDoc.getModel().getName());
  }

  @Override
  void setOrganismData() {
    setOrganismDataFromSpecies(primModelDoc.getModel().getSpecies(0));
  }

  @Override
  void setMatrixData() {
    setMatrixDataFromCompartment(primModelDoc.getModel().getCompartment(0));
  }

  @Override
  void setMetadata() {
    setMetaDataFromAnnotation(secModelDoc.getAnnotation());
  }

  @Override
  void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) secModelDoc.getModel().getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  void setModelNotes() {
    setNotesFromModel(secModelDoc.getModel());
  }

  @Override
  void setDependentVariableData() {
    setDependentVariableData(primModelDoc.getModel(), primModelLimits);
  }

  @Override
  void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    // Gets independent variables from primary model
    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    // Gets independent variables from secondary model
    addIndepsFromSecondaryModel(vars, units, mins, maxs);

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = units.toArray(new String[units.size()]);
    double[] minsArray = mins.stream().mapToDouble(Double::doubleValue).toArray();
    double[] maxsArray = maxs.stream().mapToDouble(Double::doubleValue).toArray();

    template.setIndependentVariables(varsArray);
    template.setIndependentVariablesUnits(unitsArray);
    template.setIndependentVariablesMins(minsArray);
    template.setIndependentVariablesMaxs(maxsArray);
  }

  private void addIndepsFromPrimaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs) {
    final Model model = primModelDoc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    final String depName = rule.getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = model.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : primModelLimits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }

  private void addIndepsFromSecondaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs) {
    final Model model = secModelDoc.getModel();
    ModelRule rule2 = new ModelRule((AssignmentRule) model.getRule(0));
    final String depName = rule2.getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = model.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : secModelLimits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class OneStepSecondaryModelTemplateCreator extends TemplateCreator {

  private final SBMLDocument doc;
  private final Model primModel;
  private final ModelDefinition secModel;

  private final List<Limits> secModelLimits;
  private final List<Limits> primModelLimits;

  OneStepSecondaryModelTemplateCreator(OneStepSecondaryModel model) {
    doc = model.getModelDoc();
    primModel = model.getModelDoc().getModel();

    CompSBMLDocumentPlugin plugin =
        (CompSBMLDocumentPlugin) model.getModelDoc().getPlugin(CompConstants.shortLabel);
    secModel = plugin.getModelDefinition(0);

    // Caches limits
    primModelLimits = getLimitsFromModel(primModel);
    secModelLimits = getLimitsFromModel(secModel);
  }

  @Override
  void setModelId() {
    if (primModel.isSetId())
      template.setModelId(primModel.getId());
  }

  @Override
  void setModelName() {
    if (primModel.isSetName())
      template.setModelName(primModel.getName());
  }

  @Override
  void setOrganismData() {
    setOrganismDataFromSpecies(primModel.getSpecies(0));
  }

  @Override
  void setMatrixData() {
    setMatrixDataFromCompartment(primModel.getCompartment(0));
  }

  @Override
  void setMetadata() {
    setMetaDataFromAnnotation(doc.getAnnotation());
  }

  @Override
  void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) primModel.getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  void setModelNotes() {
    setNotesFromModel(primModel);
  }

  @Override
  void setDependentVariableData() {
    setDependentVariableData(primModel, primModelLimits);
  }

  @Override
  void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    // Gets independent variables from primary model
    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    // Gets independent variables from secondary model
    addIndepsFromSecondaryModel(vars, units, mins, maxs);

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = units.toArray(new String[units.size()]);
    double[] minsArray = mins.stream().mapToDouble(Double::doubleValue).toArray();
    double[] maxsArray = maxs.stream().mapToDouble(Double::doubleValue).toArray();

    template.setIndependentVariables(varsArray);
    template.setIndependentVariablesUnits(unitsArray);
    template.setIndependentVariablesMins(minsArray);
    template.setIndependentVariablesMaxs(maxsArray);
  }

  private void addIndepsFromPrimaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs) {
    ModelRule rule = new ModelRule((AssignmentRule) primModel.getRule(0));
    final String depName = rule.getVariable();

    List<Parameter> indepParams = primModel.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = primModel.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : primModelLimits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }

  private void addIndepsFromSecondaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs) {
    ModelRule rule = new ModelRule((AssignmentRule) secModel.getRule(0));
    final String depName = rule.getVariable();

    List<Parameter> indepParams = secModel.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = secModel.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : secModelLimits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class ManualSecondaryModelTemplateCreator extends TemplateCreator {

  private final SBMLDocument doc;
  private final List<Limits> limits;

  ManualSecondaryModelTemplateCreator(ManualSecondaryModel model) {
    doc = model.getDoc();

    // Caches limits
    limits = getLimitsFromModel(doc.getModel());
  }

  @Override
  void setModelId() {
    if (doc.getModel().isSetId())
      template.setModelId(doc.getModel().getId());
  }

  @Override
  void setModelName() {
    if (doc.getModel().isSetName())
      template.setModelName(doc.getModel().getName());
  }

  @Override
  void setOrganismData() {
    // Does nothing - manual tertiary models has no associated microbial data
  }

  @Override
  void setMatrixData() {
    // Does nothing - manual tertiary models have no associated microbial data
  }

  @Override
  void setMetadata() {
    setMetaDataFromAnnotation(doc.getAnnotation());
  }

  @Override
  void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) doc.getModel().getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  void setModelNotes() {
    setNotesFromModel(doc.getModel());
  }

  @Override
  void setDependentVariableData() {

    Model model = doc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    String depName = rule.getRule().getVariable();
    Parameter param = model.getParameter(depName);

    if (param.isSetUnits()) {
      // Adds unit
      String unitId = param.getUnits();
      String unitName = model.getUnitDefinition(unitId).getName();
      template.setDependentVariableUnit(unitName);

      // Adds unit category
      if (DBUnits.getDBUnits().containsKey(unitName)) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        template.setDependentVariable(ufdb.getKind_of_property_quantity());
      }
    }

    for (Limits lim : limits) {
      if (lim.getVar().equals(depName)) {
        if (lim.getMin() != null)
          template.setDependentVariableMin(lim.getMin());
        if (lim.getMax() != null)
          template.setDependentVariableMax(lim.getMax());
        break;
      }
    }
  }

  @Override
  void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    final Model model = doc.getModel();
    final String depName = new ModelRule((AssignmentRule) model.getRule(0)).getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = model.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : limits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = units.toArray(new String[units.size()]);
    double[] minsArray = mins.stream().mapToDouble(Double::doubleValue).toArray();
    double[] maxsArray = maxs.stream().mapToDouble(Double::doubleValue).toArray();

    template.setIndependentVariables(varsArray);
    template.setIndependentVariablesUnits(unitsArray);
    template.setIndependentVariablesMins(minsArray);
    template.setIndependentVariablesMaxs(maxsArray);
  }

  @Override
  void setHasData() {
    template.setHasData(false);
  }
}



abstract class TertiaryModelTemplateCreator extends TemplateCreator {

  private final SBMLDocument primDoc;
  private final List<SBMLDocument> secDocs;

  private final List<Limits> primModelLimits;

  TertiaryModelTemplateCreator(SBMLDocument primaryModelDoc,
      List<SBMLDocument> secondaryModelDocs) {
    primDoc = primaryModelDoc;
    secDocs = secondaryModelDocs;

    // Caches limits
    primModelLimits = getLimitsFromModel(primDoc.getModel());
  }

  @Override
  void setModelId() {
    if (primDoc.getModel().isSetId())
      template.setModelId(primDoc.getModel().getId());
  }

  @Override
  void setModelName() {
    if (primDoc.getModel().isSetName())
      template.setModelName(primDoc.getModel().getName());
  }

  @Override
  void setOrganismData() {
    setOrganismDataFromSpecies(primDoc.getModel().getSpecies(0));
  }

  @Override
  void setMatrixData() {
    setMatrixDataFromCompartment(primDoc.getModel().getCompartment(0));
  }

  @Override
  void setMetadata() {
    setMetaDataFromAnnotation(primDoc.getAnnotation());
  }

  @Override
  void setModelSubject() {
    ModelRule rule = new ModelRule((AssignmentRule) primDoc.getModel().getRule(0));
    template.setModelSubject(rule.getModelClass());
  }

  @Override
  void setModelNotes() {
    setNotesFromModel(primDoc.getModel());
  }

  @Override
  void setDependentVariableData() {
    setDependentVariableData(primDoc.getModel(), primModelLimits);
  }

  @Override
  void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    for (SBMLDocument doc : secDocs) {
      addIndepsFromSecondaryModel(vars, units, mins, maxs, doc.getModel());
    }

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = units.toArray(new String[units.size()]);
    double[] minsArray = mins.stream().mapToDouble(Double::doubleValue).toArray();
    double[] maxsArray = maxs.stream().mapToDouble(Double::doubleValue).toArray();

    template.setIndependentVariables(varsArray);
    template.setIndependentVariablesUnits(unitsArray);
    template.setIndependentVariablesMins(minsArray);
    template.setIndependentVariablesMaxs(maxsArray);
  }

  private void addIndepsFromPrimaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs) {
    final Model model = primDoc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    final String depName = rule.getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    for (Parameter param : indepParams) {
      final String unitId = param.getUnits();

      // unit
      units.add(unitId);

      // category
      String unitName = model.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless") && DBUnits.getDBUnits().containsKey(unitName)) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        vars.add(ufdb.getKind_of_property_quantity());
      }

      // min & max
      for (Limits lim : primModelLimits) {
        if (lim.getVar().equals(param.getId())) {
          if (lim.getMin() != null)
            mins.add(lim.getMin());
          if (lim.getMax() != null)
            maxs.add(lim.getMax());
        }
      }
    }
  }

  private void addIndepsFromSecondaryModel(Set<String> vars, Set<String> units, Set<Double> mins,
      Set<Double> maxs, Model secModel) {
    ModelRule rule = new ModelRule((AssignmentRule) secModel.getRule(0));
    final String depName = rule.getVariable();

    List<Parameter> indepParams = secModel.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    List<Limits> limits = getLimitsFromModel(secModel);

    for (Parameter param : indepParams) {

      if (param.isSetUnits()) {
        final String unitId = param.getUnits();

        // unit
        units.add(unitId);

        // category
        String unitName = secModel.getUnitDefinition(unitId).getName();
        if (!unitId.equals("dimensionless")) {
          UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
          vars.add(ufdb.getKind_of_property_quantity());
        }
      }

      // min & max
      for (Limits lim : limits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }
}


class TwoStepTertiaryModelTemplateCreator extends TertiaryModelTemplateCreator {

  TwoStepTertiaryModelTemplateCreator(TwoStepTertiaryModel model) {
    super(model.getPrimModels().get(0).getModelDoc(), model.getSecDocs());
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class OneStepTertiaryModelTemplateCreator extends TertiaryModelTemplateCreator {

  OneStepTertiaryModelTemplateCreator(OneStepTertiaryModel model) {
    super(model.getTertiaryDoc(), model.getSecDocs());
  }

  @Override
  void setHasData() {
    template.setHasData(true);
  }
}


class ManualTertiaryModelTemplateCreator extends TertiaryModelTemplateCreator {

  ManualTertiaryModelTemplateCreator(ManualTertiaryModel model) {
    super(model.getTertiaryDoc(), model.getSecDocs());
  }

  @Override
  void setHasData() {
    template.setHasData(false);
  }
}
