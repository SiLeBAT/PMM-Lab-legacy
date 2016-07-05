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

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
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
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
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
    DataTemplateCreator templateCreator = new DataTemplateCreator(doc);
    return templateCreator.createTemplate();
  }

  public static FSMRTemplate processModelWithMicrobialData(SBMLDocument doc) {
    ModelWithMicrobialDataTemplateCreator templateCreator =
        new ModelWithMicrobialDataTemplateCreator(doc);
    return templateCreator.createTemplate();
  }

  public static FSMRTemplate processModelWithoutMicrobialData(SBMLDocument doc) {
    ModelWithoutMicrobialDataTemplateCreator templateCreator =
        new ModelWithoutMicrobialDataTemplateCreator(doc);
    return templateCreator.createTemplate();
  }

  public static FSMRTemplate processPrevalenceModel(SBMLDocument doc) {
    return new PrevalenceModelTemplateCreator(doc).createTemplate();
  }

  public static FSMRTemplate processSpreadsheet(XSSFWorkbook workbook) {
    return new SpreadsheetTemplateCreator(workbook.getSheetAt(0)).createTemplate();
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
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK, template
          .getReferenceDescriptionLink().toString());
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
      String formattedMins =
          Arrays.stream(template.getIndependentVariablesMins()).mapToObj(Double::toString)
              .collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS, formattedMins);
    }

    if (template.isSetIndependentVariablesMaxs()) {
      String formattedMaxs =
          Arrays.stream(template.getIndependentVariablesMaxs()).mapToObj(Double::toString)
              .collect(Collectors.joining("||"));
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS, formattedMaxs);
    }

    if (template.isSetHasData()) {
      tuple.setValue(OpenFSMRSchema.ATT_HAS_DATA, template.getHasData() ? 1 : 0);
    }

    return tuple;
  }

  public static FSMRTemplate createTemplateFromTuple(
      de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple tuple) {
    FSMRTemplate template = new FSMRTemplateImpl();

    template.setModelName(tuple.getString(OpenFSMRSchema.ATT_MODEL_NAME));
    template.setModelId(tuple.getString(OpenFSMRSchema.ATT_MODEL_ID));
    try {
      URL url = new URL(tuple.getString(OpenFSMRSchema.ATT_MODEL_LINK));
      template.setModelLink(url);
    } catch (MalformedURLException e) {
    }

    template.setOrganismName(tuple.getString(OpenFSMRSchema.ATT_ORGANISM_NAME));
    template.setOrganismDetails(tuple.getString(OpenFSMRSchema.ATT_ORGANISM_DETAIL));

    template.setMatrixName(tuple.getString(OpenFSMRSchema.ATT_ENVIRONMENT_NAME));
    template.setMatrixDetails(tuple.getString(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL));

    template.setCreator(tuple.getString(OpenFSMRSchema.ATT_MODEL_CREATOR));
    template.setFamilyName(tuple.getString(OpenFSMRSchema.ATT_MODEL_FAMILY_NAME));
    template.setContact(tuple.getString(OpenFSMRSchema.ATT_MODEL_CONTACT));

    template.setReferenceDescription(tuple
        .getString(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION));

    if (template.isSetReferenceDescriptionLink()) {
      try {
        URL url = new URL(tuple.getString(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK));
        template.setReferenceDescriptionLink(url);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }

    SimpleDateFormat dateFormat =
        new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

    if (template.isSetCreatedDate()) {
      String createdDateAsString = tuple.getString(OpenFSMRSchema.ATT_MODEL_CREATED_DATE);
      try {
        Date createdDate = dateFormat.parse(createdDateAsString);
        template.setCreatedDate(createdDate);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    if (template.isSetModifiedDate()) {
      String modifiedDateAsString = tuple.getString(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE);
      try {
        Date modifiedDate = dateFormat.parse(modifiedDateAsString);
        template.setModifiedDate(modifiedDate);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    template.setRights(tuple.getString(OpenFSMRSchema.ATT_MODEL_RIGHTS));
    template.setNotes(tuple.getString(OpenFSMRSchema.ATT_MODEL_NOTES));
    template.setCurationStatus(tuple.getString(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS));

    if (template.isSetModelType()) {
      template.setModelType(ModelType.valueOf(tuple.getString(OpenFSMRSchema.ATT_MODEL_TYPE)));
    }

    template.setModelSubject(ModelClass.valueOf(tuple.getString(OpenFSMRSchema.ATT_MODEL_SUBJECT)));
    template.setFoodProcess(tuple.getString(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS));
    template.setDependentVariable(tuple.getString(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE));
    template.setDependentVariableUnit(tuple
        .getString(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT));
    template.setDependentVariableMin(tuple
        .getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN));
    template.setDependentVariableMax(tuple
        .getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX));

    template.setIndependentVariables(tuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE)
        .split("\\|\\|"));
    template.setIndependentVariablesUnits(tuple.getString(
        OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS).split("\\|\\|"));

    double[] indepVarMins =
        Arrays.stream(tuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS).split("||"))
            .mapToDouble(Double::parseDouble).toArray();
    template.setIndependentVariablesMins(indepVarMins);

    double[] indepVarMaxs =
        Arrays.stream(tuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS).split("||"))
            .mapToDouble(Double::parseDouble).toArray();
    template.setIndependentVariablesMaxs(indepVarMaxs);

    return template;
  }
}


abstract class TemplateCreator {

  protected FSMRTemplate template = new FSMRTemplateImpl();

  public final FSMRTemplate createTemplate() {
    setModelId();
    setModelName();
    setOrganismData();
    setMatrixData();
    setCreator();
    setFamilyName();
    setContact();
    setReferenceDescriptionLink();
    setCreatedDate();
    setModifiedDate();
    setModelRights();
    setModelType();
    setModelSubject();
    setModelNotes();
    setDependentVariableData();
    setIndependentVariableData();
    setHasData();
    return template;
  }

  abstract public void setModelId();

  abstract public void setModelName();

  abstract public void setOrganismData();

  abstract public void setMatrixData();

  abstract public void setCreator();

  abstract public void setFamilyName();

  abstract public void setContact();

  abstract public void setReferenceDescriptionLink();

  abstract public void setCreatedDate();

  abstract public void setModifiedDate();

  abstract public void setModelRights();

  abstract public void setModelType();

  abstract public void setModelSubject();

  abstract public void setModelNotes();

  abstract public void setDependentVariableData();

  abstract public void setIndependentVariableData();

  abstract public void setHasData();
}


class DataTemplateCreator extends TemplateCreator {

  private NuMLDocument doc;

  public DataTemplateCreator(NuMLDocument doc) {
    this.doc = doc;
  }

  // TODO: setModelId
  @Override
  public void setModelId() {}

  // TODO: setModelName
  @Override
  public void setModelName() {}

  @Override
  public void setOrganismData() {
    ConcentrationOntology concOntology = doc.getConcentrationOntologyTerm();
    PMFSpecies species = concOntology.getSpecies();

    template.setOrganismName(species.getName());

    if (species.isSetDetail()) {
      template.setOrganismDetails(species.getDetail());
    }
  }

  @Override
  public void setMatrixData() {
    ConcentrationOntology concOntology = doc.getConcentrationOntologyTerm();
    PMFCompartment compartment = concOntology.getCompartment();

    template.setMatrixName(compartment.getName());

    if (compartment.isSetDetail()) {
      String matrixDetail = compartment.getDetail();
      template.setMatrixDetails(matrixDetail);
    }
  }

  @Override
  public void setCreator() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatorGivenName()) {
      template.setCreator(rc.getCreatorGivenName());
    }
  }

  @Override
  public void setFamilyName() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatorFamilyName()) {
      template.setFamilyName(rc.getCreatorFamilyName());
    }
  }

  @Override
  public void setContact() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatorContact()) {
      template.setContact(rc.getCreatorContact());
    }
  }

  // TODO: setReferenceDescriptionLink
  @Override
  public void setReferenceDescriptionLink() {}

  @Override
  public void setCreatedDate() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatedDate()) {
      SimpleDateFormat dateFormat =
          new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
      String createdDateAsString = rc.getCreatedDate();
      try {
        Date createdDate = dateFormat.parse(createdDateAsString);
        template.setCreatedDate(createdDate);
      } catch (ParseException e) {
        System.err.println(createdDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setModifiedDate() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetModifiedDate()) {
      SimpleDateFormat dateFormat =
          new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
      String modifiedDateAsString = rc.getModifiedDate();
      try {
        Date modifiedDate = dateFormat.parse(modifiedDateAsString);
        template.setModifiedDate(modifiedDate);
      } catch (ParseException e) {
        System.err.println(modifiedDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setModelRights() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetRights()) {
      template.setRights(rc.getRights());
    }
  }

  @Override
  public void setModelType() {
    template.setModelType(ModelType.EXPERIMENTAL_DATA);
  }

  /**
   * Sets model subject as ModelClass.UNKNOWN since the data files do not keep the subject of the
   * model.
   */
  @Override
  public void setModelSubject() {
    template.setModelSubject(ModelClass.UNKNOWN);
  }

  // TODO: setModelNotes
  @Override
  public void setModelNotes() {}

  @Override
  public void setDependentVariableData() {
    ConcentrationOntology concOntology = doc.getConcentrationOntologyTerm();
    PMFUnitDefinition unitDef = concOntology.getUnitDefinition();
    String unitName = unitDef.getName();

    // Sets dependent variable unit
    template.setDependentVariableUnit(unitName);

    // Sets dependent variable
    if (!unitDef.getId().equals("dimensionless")) {
      UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
      String unitCategory = ufdb.getKind_of_property_quantity();
      template.setDependentVariable(unitCategory);
    }

    /**
     * NuMLDocument do not keep the limits of the variables so it is not possible to retrieve the
     * min & max values of the dependent variable.
     */
  }

  @Override
  public void setIndependentVariableData() {
    TimeOntology timeOntology = doc.getTimeOntologyTerm();
    PMFUnitDefinition unitDef = timeOntology.getUnitDefinition();
    String unitName = unitDef.getName();

    UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
    String unitCategory = ufdb.getKind_of_property_quantity();

    template.setIndependentVariables(new String[] {unitCategory});
  }

  @Override
  public void setHasData() {
    template.setHasData(true);
  }
}


abstract class ModelTemplateCreator extends TemplateCreator {

  protected SBMLDocument doc;

  public ModelTemplateCreator(SBMLDocument doc) {
    this.doc = doc;
  }

  @Override
  public void setModelId() {
    template.setModelId(doc.getModel().getId());
  }

  @Override
  public void setModelName() {
    Model model = doc.getModel();
    if (model.isSetName()) {
      template.setModelName(model.getName());
    }
  }

  @Override
  public void setCreator() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetGivenName()) {
      template.setCreator(metadata.getGivenName());
    }
  }

  @Override
  public void setFamilyName() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetFamilyName()) {
      template.setFamilyName(metadata.getFamilyName());
    }
  }

  @Override
  public void setContact() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetContact()) {
      template.setContact(metadata.getContact());
    }
  }

  @Override
  public void setReferenceDescriptionLink() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
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
  }

  @Override
  public void setCreatedDate() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetCreatedDate()) {
      String createdDateAsString = metadata.getCreatedDate();

      SimpleDateFormat dateFormat =
          new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
      try {
        Date createdDate = dateFormat.parse(createdDateAsString);
        template.setCreatedDate(createdDate);
      } catch (ParseException e) {
        System.err.println(createdDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setModifiedDate() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetModifiedDate()) {
      String modifiedDateAsString = metadata.getModifiedDate();

      SimpleDateFormat dateFormat =
          new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
      try {
        Date modifiedDate = dateFormat.parse(modifiedDateAsString);
        template.setModifiedDate(modifiedDate);
      } catch (ParseException e) {
        System.err.println(modifiedDateAsString + " is not a valid date");
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setModelRights() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetRights()) {
      template.setRights(metadata.getRights());
    }
  }

  @Override
  public void setModelType() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetType()) {
      template.setModelType(metadata.getType());
    }
  }

  @Override
  public void setModelSubject() {
    Model model = doc.getModel();
    AssignmentRule sbmlRule = (AssignmentRule) model.getRule(0);
    ModelRule pmfRule = new ModelRule(sbmlRule);
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  public void setModelNotes() {
    Model model = doc.getModel();
    if (model.isSetNotes()) {
      try {
        String notes = model.getNotesString();
        template.setNotes(notes);
      } catch (XMLStreamException e) {
        System.err.println("Error accesing the notes of " + model);
        e.printStackTrace();
      }
    }
  }
}


class ModelWithMicrobialDataTemplateCreator extends ModelTemplateCreator {

  private final List<Limits> limits;

  public ModelWithMicrobialDataTemplateCreator(SBMLDocument doc) {
    super(doc);

    // Caches limits
    limits =
        doc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setOrganismData() {
    Model model = doc.getModel();
    Species sbmlSpecies = model.getSpecies(0);
    PMFSpecies pmfSpecies = SBMLFactory.createPMFSpecies(sbmlSpecies);

    template.setOrganismName(pmfSpecies.getName());

    if (pmfSpecies.isSetDetail()) {
      template.setOrganismDetails(pmfSpecies.getDetail());
    }
  }

  @Override
  public void setMatrixData() {
    Model model = doc.getModel();
    Compartment sbmlCompartment = model.getCompartment(0);
    PMFCompartment pmfCompartment = SBMLFactory.createPMFCompartment(sbmlCompartment);

    template.setMatrixName(pmfCompartment.getName());

    if (pmfCompartment.isSetDetail()) {
      template.setMatrixDetails(pmfCompartment.getDetail());
    }
  }

  @Override
  public void setDependentVariableData() {
    Model model = doc.getModel();
    Species species = model.getSpecies(0);
    if (species.isSetUnits()) {
      String depUnitID = species.getUnits();

      // Sets dependent variable unit
      String depUnitName = model.getUnitDefinition(depUnitID).getName();
      template.setDependentVariableUnit(depUnitName);

      // Sets dependent variable
      if (!depUnitID.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          String depUnitCategory =
              DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
          template.setDependentVariable(depUnitCategory);
        }
      }

      // Sets dependent variable min & max
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

  @Override
  public void setIndependentVariableData() {
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
  public void setHasData() {
    template.setHasData(true);
  }
}


class PrevalenceModelTemplateCreator extends ModelWithMicrobialDataTemplateCreator {

  private final List<Limits> limits;

  /**
   * @param doc
   */
  public PrevalenceModelTemplateCreator(SBMLDocument doc) {
    super(doc);

    // Caches limits
    limits =
        doc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setDependentVariableData() {
    // Gets dependent variable id
    Model model = doc.getModel();
    AssignmentRule rule = (AssignmentRule) model.getRule(0);
    String depId = rule.getVariable();

    // Gets parameter for the dependent variable and sets it
    Parameter param = model.getParameter(depId);
    template.setDependentVariable(param.getName());

    // Gets and sets dependent variable unit
    String unitId = param.getUnits();
    if (!unitId.equals("dimensionless")) {
      UnitDefinition unitDef = model.getUnitDefinition(unitId);
      if (unitDef != null) {
        template.setDependentVariableUnit(unitDef.getName());
      }
    }

    // Sets dependent variable min & max
    for (Limits lim : limits) {
      if (lim.getVar().equals(depId)) {
        if (lim.getMin() != null)
          template.setDependentVariableMin(lim.getMin());
        if (lim.getMax() != null)
          template.setDependentVariableMax(lim.getMax());
        break;
      }
    }
  }

  @Override
  public void setIndependentVariableData() {
    Model model = doc.getModel();
    AssignmentRule rule = (AssignmentRule) model.getRule(0);
    String depId = rule.getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        return !((Parameter) o).getId().equals(depId);
      }
    });

    final int numParams = indepParams.size();
    String[] units = new String[numParams];
    String[] names = new String[numParams];
    double[] mins = new double[numParams];
    double[] maxs = new double[numParams];

    for (int i = 0; i < numParams; i++) {
      Parameter param = indepParams.get(i);

      // variable
      names[i] = param.getName();

      // unit
      String unitId = param.getUnits();
      units[i] = "";
      if (!unitId.equals("dimensionless")) {
        UnitDefinition unitDef = model.getUnitDefinition(unitId);
        if (unitDef != null) {
          units[i] = unitDef.getName();
        }
      }

      for (Limits lim : limits) {
        if (lim.getVar().equals(param.getId())) {
          mins[i] = lim.getMin();
          maxs[i] = lim.getMax();
          break;
        }
      }
    }

    template.setIndependentVariables(names);
    template.setIndependentVariablesUnits(units);
    template.setIndependentVariablesMins(mins);
    template.setIndependentVariablesMaxs(maxs);
  }
}


class ModelWithoutMicrobialDataTemplateCreator extends ModelTemplateCreator {

  private final List<Limits> limits;

  public ModelWithoutMicrobialDataTemplateCreator(SBMLDocument doc) {
    super(doc);

    // Caches limits
    limits =
        doc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setOrganismData() {}

  @Override
  public void setMatrixData() {}

  @Override
  public void setHasData() {
    template.setHasData(false);
  }

  @Override
  public void setDependentVariableData() {
    Model model = doc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    String depName = rule.getRule().getVariable();
    Parameter param = model.getParameter(depName);

    if (param.isSetUnits()) {
      // Adds unit
      String unitID = param.getUnits();
      String unitName = model.getUnitDefinition(unitID).getName();
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
  public void setIndependentVariableData() {
    Model model = doc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    String depName = rule.getRule().getVariable();

    List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
      @Override
      public boolean accepts(Object o) {
        Parameter param = (Parameter) o;
        return !param.isConstant() && !param.getId().equals(depName);
      }
    });

    final int numParams = indepParams.size();
    String[] units = new String[numParams];
    String[] categories = new String[numParams];
    double[] mins = new double[numParams];
    double[] maxs = new double[numParams];

    for (int i = 0; i < numParams; i++) {
      Parameter param = indepParams.get(i);

      final String unitId = param.getUnits();

      // unit
      units[i] = unitId;

      // category
      String unitName = model.getUnitDefinition(unitId).getName();
      if (!unitId.equals("dimensionless")) {
        UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
        categories[i] = ufdb.getKind_of_property_quantity();
      }

      // min & max
      for (Limits lim : limits) {
        if (lim.getVar().equals(param.getId())) {
          mins[i] = lim.getMin();
          maxs[i] = lim.getMax();
        }
      }
    }

    template.setIndependentVariables(categories);
    template.setIndependentVariablesUnits(units);
    template.setIndependentVariablesMins(mins);
    template.setIndependentVariablesMaxs(maxs);
  }
}


/** Creates a OpenFSMR template from a XLSX spreadsheet. */
class SpreadsheetTemplateCreator extends TemplateCreator {

  private XSSFSheet sheet;

  public SpreadsheetTemplateCreator(final XSSFSheet sheet) {
    this.sheet = sheet;
  }

  @Override
  public void setModelId() {
    template.setModelId(getStringVal(2));
  }

  @Override
  public void setModelName() {
    template.setModelName(getStringVal(1));
  }

  @Override
  public void setOrganismData() {
    template.setOrganismName(getStringVal(3));
    template.setOrganismDetails(getStringVal(4));
  }

  @Override
  public void setMatrixData() {
    template.setMatrixName(getStringVal(5));
    template.setMatrixDetails(getStringVal(6));
  }

  @Override
  public void setCreator() {
    template.setCreator(getStringVal(7));
  }

  @Override
  public void setFamilyName() {
    // No family name in the spreadsheet
  }

  @Override
  public void setContact() {
    // No contact in the spreadsheet
  }

  @Override
  public void setReferenceDescriptionLink() {
    template.setReferenceDescription(getStringVal(8));
  }

  @Override
  public void setCreatedDate() {
    SimpleDateFormat dateFormat =
        new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
    Double createdDateAsDouble = sheet.getRow(9).getCell(5).getNumericCellValue();
    String createdDateAsString = createdDateAsDouble.toString();
    try {
      Date createdDate = dateFormat.parse(createdDateAsString);
      template.setCreatedDate(createdDate);
    } catch (ParseException e) {
      System.err.println(createdDateAsString + " is not a valid date");
      e.printStackTrace();
    }
  }

  @Override
  public void setModifiedDate() {
    SimpleDateFormat dateFormat =
        new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
    Double modifiedDateAsDouble = sheet.getRow(10).getCell(5).getNumericCellValue();
    String modifiedDateAsString = modifiedDateAsDouble.toString();
    try {
      Date modifiedDate = dateFormat.parse(modifiedDateAsString);
      template.setModifiedDate(modifiedDate);
    } catch (ParseException e) {
      System.err.println(modifiedDateAsString + " is not a valid date");
      e.printStackTrace();
    }
  }

  @Override
  public void setModelRights() {
    template.setRights(getStringVal(11));
  }

  @Override
  public void setModelType() {
    String modelTypeAsString = getStringVal(13);
    try {
      template.setModelType(ModelType.valueOf(modelTypeAsString));
    }
    // if modelTypeAsString is not a valid ModelType
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setModelSubject() {
    String modelSubject = getStringVal(14);
    try {
      template.setModelSubject(ModelClass.valueOf(modelSubject));
    }
    // if modelSubject is not a valid ModelClass
    catch (IllegalArgumentException e) {
      template.setModelSubject(ModelClass.UNKNOWN);
      e.printStackTrace();
    }
  }

  @Override
  public void setModelNotes() {
    template.setNotes(getStringVal(12));
  }

  @Override
  public void setDependentVariableData() {
    template.setDependentVariable(getStringVal(21));
    template.setDependentVariableUnit(getStringVal(22));
    template.setDependentVariableMin(sheet.getRow(23).getCell(5).getNumericCellValue());
    template.setDependentVariableMax(sheet.getRow(24).getCell(5).getNumericCellValue());
  }

  @Override
  public void setIndependentVariableData() {

    String[] indepVars = getStringVal(25).split("\\|\\|");
    for (int i = 0; i < indepVars.length; i++) {
      indepVars[i] = indepVars[i].trim();
    }
    template.setIndependentVariables(indepVars);

    template.setIndependentVariablesUnits(getStringVal(26).split("\\|\\|"));

    // Sets minimum values
    String[] indepVarMinTokens = getStringVal(27).split("\\|\\|");
    double[] indepVarMins =
        Arrays.stream(indepVarMinTokens).mapToDouble(Double::parseDouble).toArray();
    template.setIndependentVariablesMins(indepVarMins);


    // Sets maximum values
    String[] indepVarMaxTokens = getStringVal(28).split("\\|\\|");
    double[] indepVarMaxs =
        Arrays.stream(indepVarMaxTokens).mapToDouble(Double::parseDouble).toArray();
    template.setIndependentVariablesMaxs(indepVarMaxs);
  }

  @Override
  public void setHasData() {
    template.setHasData(false);
  }

  /** Gets the string value of the fifth column which holds the value for that row. */
  private String getStringVal(int rownum) {
    return sheet.getRow(rownum).getCell(5).getStringCellValue();
  }
}


abstract class BetterTemplateCreator {
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

  abstract public void setModelId();

  abstract public void setModelName();

  abstract public void setOrganismData();

  abstract public void setMatrixData();

  abstract public void setMetadata();

  abstract public void setModelSubject();

  abstract public void setModelNotes();

  abstract public void setDependentVariableData();

  abstract public void setIndependentVariableData();

  abstract public void setHasData();
}


class TwoStepSecondaryModelTemplateCreator extends BetterTemplateCreator {

  private final SBMLDocument secModelDoc;
  private final SBMLDocument primModelDoc;

  private final List<Limits> secModelLimits;
  private final List<Limits> primModelLimits;

  public TwoStepSecondaryModelTemplateCreator(TwoStepSecondaryModel model) {
    secModelDoc = model.getSecDoc();
    primModelDoc = model.getPrimModels().get(0).getModelDoc();

    // Caches limits
    primModelLimits =
        primModelDoc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
    secModelLimits =
        secModelDoc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setModelId() {
    if (secModelDoc.getModel().isSetId())
      template.setModelId(secModelDoc.getModel().getId());
  }

  @Override
  public void setModelName() {
    if (secModelDoc.getModel().isSetName())
      template.setModelName(secModelDoc.getModel().getName());
  }

  @Override
  public void setOrganismData() {
    PMFSpecies species = SBMLFactory.createPMFSpecies(secModelDoc.getModel().getSpecies(0));

    if (species.getSpecies().isSetName())
      template.setOrganismName(species.getSpecies().getName());

    if (species.isSetDetail())
      template.setOrganismDetails(species.getDetail());
  }

  @Override
  public void setMatrixData() {
    PMFCompartment matrix =
        SBMLFactory.createPMFCompartment(secModelDoc.getModel().getCompartment(0));

    if (matrix.getCompartment().isSetName())
      template.setMatrixName(matrix.getCompartment().getName());

    if (matrix.isSetDetail())
      template.setMatrixDetails(matrix.getDetail());
  }

  @Override
  public void setMetadata() {
    Metadata metadata = new MetadataAnnotation(secModelDoc.getAnnotation()).getMetadata();

    if (metadata.isSetGivenName())
      template.setCreator(metadata.getGivenName());

    if (metadata.isSetFamilyName())
      template.setCreator(metadata.getFamilyName());

    if (metadata.isSetContact())
      template.setCreator(metadata.getContact());

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

  @Override
  public void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) secModelDoc.getModel().getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  public void setModelNotes() {
    if (secModelDoc.getModel().isSetNotes()) {
      try {
        template.setNotes(secModelDoc.getModel().getNotesString());
      } catch (XMLStreamException error) {
        System.err.println("error accessing the notes of " + secModelDoc.getModel());
        error.printStackTrace();
      }
    }
  }

  @Override
  public void setDependentVariableData() {
    // Gets data of the dependent variable in the primary model
    Model model = primModelDoc.getModel();
    Species species = model.getSpecies(0);

    if (species.isSetUnits()) {
      String depUnitId = species.getUnits();

      // Sets dependent variable unit
      String depUnitName = model.getUnitDefinition(depUnitId).getName();
      template.setDependentVariable(depUnitName);

      // Sets dependent variable
      if (!depUnitId.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          String depUnitCategory =
              DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
          template.setDependentVariable(depUnitCategory);
        }
      }

      // Sets dependent variable min & max
      for (Limits lim : primModelLimits) {
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

  @Override
  public void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    // Gets independent variables from primary model
    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    // Gets independent variables from secondary model
    addIndepsFromSecondaryModel(vars, units, mins, maxs);

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = vars.toArray(new String[units.size()]);
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
  public void setHasData() {
    template.setHasData(true);
  }
}


class OneStepSecondaryModelTemplateCreator extends BetterTemplateCreator {

  private final SBMLDocument doc;
  private final Model primModel;
  private final ModelDefinition secModel;

  private final List<Limits> secModelLimits;
  private final List<Limits> primModelLimits;

  public OneStepSecondaryModelTemplateCreator(OneStepSecondaryModel model) {
    doc = model.getModelDoc();
    primModel = model.getModelDoc().getModel();

    CompSBMLDocumentPlugin plugin =
        (CompSBMLDocumentPlugin) model.getModelDoc().getPlugin(CompConstants.shortLabel);
    secModel = plugin.getModelDefinition(0);

    // Caches limits
    primModelLimits =
        primModel.getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
    secModelLimits =
        secModel.getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setModelId() {
    if (primModel.isSetId())
      template.setModelId(primModel.getId());
  }

  @Override
  public void setModelName() {
    if (primModel.isSetName())
      template.setModelName(primModel.getName());
  }

  @Override
  public void setOrganismData() {
    PMFSpecies species = SBMLFactory.createPMFSpecies(primModel.getSpecies(0));

    if (species.getSpecies().isSetName())
      template.setOrganismName(species.getSpecies().getName());

    if (species.isSetDetail())
      template.setOrganismDetails(species.getDetail());
  }

  @Override
  public void setMatrixData() {
    PMFCompartment matrix = SBMLFactory.createPMFCompartment(primModel.getCompartment(0));

    if (matrix.getCompartment().isSetName())
      template.setMatrixName(matrix.getCompartment().getName());

    if (matrix.isSetDetail())
      template.setMatrixDetails(matrix.getDetail());
  }

  @Override
  public void setMetadata() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

    if (metadata.isSetGivenName())
      template.setCreator(metadata.getGivenName());

    if (metadata.isSetFamilyName())
      template.setCreator(metadata.getFamilyName());

    if (metadata.isSetContact())
      template.setCreator(metadata.getContact());

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

  @Override
  public void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) primModel.getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  public void setModelNotes() {
    if (primModel.isSetNotes()) {
      try {
        template.setNotes(primModel.getNotesString());
      } catch (XMLStreamException error) {
        System.err.println("error accessing the notes of " + primModel);
        error.printStackTrace();
      }
    }
  }

  @Override
  public void setDependentVariableData() {
    // Gets data of the dependent variable in the primary model
    Species species = primModel.getSpecies(0);

    if (species.isSetUnits()) {
      String depUnitId = species.getUnits();

      // Sets dependent variable unit
      String depUnitName = primModel.getUnitDefinition(depUnitId).getName();
      template.setDependentVariable(depUnitName);

      // Sets dependent variable
      if (!depUnitId.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          String depUnitCategory =
              DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
          template.setDependentVariable(depUnitCategory);
        }
      }

      // Sets dependent variable min & max
      for (Limits lim : primModelLimits) {
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

  @Override
  public void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    // Gets independent variables from primary model
    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    // Gets independent variables from secondary model
    addIndepsFromSecondaryModel(vars, units, mins, maxs);

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = vars.toArray(new String[units.size()]);
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
  public void setHasData() {
    template.setHasData(true);
  }
}


class ManualSecondaryModelTemplateCreator extends BetterTemplateCreator {

  private final SBMLDocument doc;
  private final List<Limits> limits;

  public ManualSecondaryModelTemplateCreator(ManualSecondaryModel model) {
    doc = model.getDoc();

    // Caches limits
    limits =
        doc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setModelId() {
    if (doc.getModel().isSetId())
      template.setModelId(doc.getModel().getId());
  }

  @Override
  public void setModelName() {
    if (doc.getModel().isSetName())
      template.setModelName(doc.getModel().getName());
  }

  @Override
  public void setOrganismData() {
    // Does nothing - manual tertiary models has no associated microbial data
  }

  @Override
  public void setMatrixData() {
    // Does nothing - manual tertiary models have no associated microbial data
  }

  @Override
  public void setMetadata() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

    if (metadata.isSetGivenName())
      template.setCreator(metadata.getGivenName());

    if (metadata.isSetFamilyName())
      template.setCreator(metadata.getFamilyName());

    if (metadata.isSetContact())
      template.setCreator(metadata.getContact());

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

  @Override
  public void setModelSubject() {
    ModelRule pmfRule = new ModelRule((AssignmentRule) doc.getModel().getRule(0));
    template.setModelSubject(pmfRule.getModelClass());
  }

  @Override
  public void setModelNotes() {
    if (doc.getModel().isSetNotes()) {
      try {
        template.setNotes(doc.getModel().getNotesString());
      } catch (XMLStreamException error) {
        System.err.println("error accessing the notes of " + doc.getModel());
        error.printStackTrace();
      }
    }
  }

  @Override
  public void setDependentVariableData() {
    // Gets data of the dependent variable in the primary model
    Species species = doc.getModel().getSpecies(0);

    if (species.isSetUnits()) {
      String depUnitId = species.getUnits();

      // Sets dependent variable unit
      String depUnitName = doc.getModel().getUnitDefinition(depUnitId).getName();
      template.setDependentVariable(depUnitName);

      // Sets dependent variable
      if (!depUnitId.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          String depUnitCategory =
              DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
          template.setDependentVariable(depUnitCategory);
        }
      }

      // Sets dependent variable min & max
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

  @Override
  public void setIndependentVariableData() {
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
    String[] unitsArray = vars.toArray(new String[units.size()]);
    double[] minsArray = mins.stream().mapToDouble(Double::doubleValue).toArray();
    double[] maxsArray = maxs.stream().mapToDouble(Double::doubleValue).toArray();

    template.setIndependentVariables(varsArray);
    template.setIndependentVariablesUnits(unitsArray);
    template.setIndependentVariablesMins(minsArray);
    template.setIndependentVariablesMaxs(maxsArray);
  }

  @Override
  public void setHasData() {
    template.setHasData(false);
  }
}


class TwoStepTertiaryModelTemplateCreator extends BetterTemplateCreator {

  private final SBMLDocument primDoc;
  private final List<SBMLDocument> secDocs;

  private final List<Limits> primModelLimits;

  public TwoStepTertiaryModelTemplateCreator(TwoStepTertiaryModel model) {
    primDoc = model.getPrimModels().get(0).getModelDoc();
    secDocs = model.getSecDocs();

    // Caches limits
    primModelLimits =
        primDoc.getModel().getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());
  }

  @Override
  public void setModelId() {
    if (primDoc.getModel().isSetId())
      template.setModelId(primDoc.getModel().getId());
  }

  @Override
  public void setModelName() {
    if (primDoc.getModel().isSetName())
      template.setModelName(primDoc.getModel().getName());
  }

  @Override
  public void setOrganismData() {
    PMFSpecies species = SBMLFactory.createPMFSpecies(primDoc.getModel().getSpecies(0));

    if (species.getSpecies().isSetName())
      template.setOrganismName(species.getSpecies().getName());

    if (species.isSetDetail())
      template.setOrganismDetails(species.getDetail());
  }

  @Override
  public void setMatrixData() {
    PMFCompartment matrix = SBMLFactory.createPMFCompartment(primDoc.getModel().getCompartment(0));

    if (matrix.getCompartment().isSetName())
      template.setMatrixName(matrix.getCompartment().getName());

    if (matrix.isSetDetail())
      template.setMatrixDetails(matrix.getDetail());
  }

  @Override
  public void setMetadata() {
    Metadata metadata = new MetadataAnnotation(primDoc.getAnnotation()).getMetadata();

    if (metadata.isSetGivenName())
      template.setCreator(metadata.getGivenName());

    if (metadata.isSetFamilyName())
      template.setCreator(metadata.getFamilyName());

    if (metadata.isSetContact())
      template.setCreator(metadata.getContact());

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

  @Override
  public void setModelSubject() {
    ModelRule rule = new ModelRule((AssignmentRule) primDoc.getModel().getRule(0));
    template.setModelSubject(rule.getModelClass());
  }

  @Override
  public void setModelNotes() {
    if (primDoc.getModel().isSetNotes()) {
      try {
        template.setNotes(primDoc.getModel().getNotesString());
      } catch (XMLStreamException error) {
        System.err.println("error accessing the notes of " + primDoc.getModel());
        error.printStackTrace();
      }
    }
  }

  @Override
  public void setDependentVariableData() {
    // Gets data of the dependent variable in the primary model
    Species species = primDoc.getModel().getSpecies(0);

    if (species.isSetUnits()) {
      String unitId = species.getUnits();

      // Sets dependent variable unit
      String unitName = primDoc.getModel().getUnitDefinition(unitId).getName();
      template.setDependentVariable(unitName);

      // Sets dependent variable
      if (!unitId.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(unitName)) {
          String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
          template.setDependentVariable(unitCategory);
        }
      }

      // Sets dependent variable min & max
      for (Limits lim : primModelLimits) {
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

  @Override
  public void setIndependentVariableData() {
    final Set<String> vars = new LinkedHashSet<>();
    final Set<String> units = new LinkedHashSet<>();
    final Set<Double> mins = new LinkedHashSet<>();
    final Set<Double> maxs = new LinkedHashSet<>();

    addIndepsFromPrimaryModel(vars, units, mins, maxs);
    for (SBMLDocument doc : secDocs) {
      addIndepsFromSecondaryModel(vars, units, mins, maxs, doc.getModel());
    }

    String[] varsArray = vars.toArray(new String[vars.size()]);
    String[] unitsArray = vars.toArray(new String[units.size()]);
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

    List<Limits> limits =
        secModel.getListOfConstraints().stream().map(LimitsConstraint::new)
            .map(LimitsConstraint::getLimits).collect(Collectors.toList());

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
      for (Limits lim : limits) {
        if (lim.getVar().equals(param.getId())) {
          mins.add(lim.getMin());
          maxs.add(lim.getMax());
        }
      }
    }
  }

  @Override
  public void setHasData() {
    template.setHasData(true);
  }
}
