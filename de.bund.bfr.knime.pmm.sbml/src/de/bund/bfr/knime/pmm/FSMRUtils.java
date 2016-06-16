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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;

import com.google.common.base.Joiner;

import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.numl.ConcentrationOntology;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.ResultComponent;
import de.bund.bfr.pmf.numl.TimeOntology;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.SBMLFactory;

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
      String modelName = template.getModelName();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_NAME, modelName);
    }

    if (template.isSetModelId()) {
      String modelId = template.getModelId();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_ID, modelId);
    }

    if (template.isSetModelLink()) {
      URL modelLink = template.getModelLink();
      String modelLinkAsString = modelLink.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_LINK, modelLinkAsString);
    }

    if (template.isSetOrganismName()) {
      String organismName = template.getOrganismName();
      tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_NAME, organismName);
    }

    if (template.isSetOrganismDetails()) {
      String organismDetails = template.getOrganismDetails();
      tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL, organismDetails);
    }

    if (template.isSetMatrixName()) {
      String matrixName = template.getMatrixName();
      tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_NAME, matrixName);
    }

    if (template.isSetMatrixDetails()) {
      String matrixDetail = template.getMatrixDetails();
      tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL, matrixDetail);
    }

    if (template.isSetCreator()) {
      String creator = template.getCreator();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATOR, creator);
    }

    if (template.isSetFamilyName()) {
      String familyName = template.getFamilyName();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_FAMILY_NAME, familyName);
    }

    if (template.isSetContact()) {
      String contact = template.getContact();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CONTACT, contact);
    }

    if (template.isSetReferenceDescription()) {
      String referenceDescription = template.getReferenceDescription();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION, referenceDescription);
    }

    if (template.isSetReferenceDescriptionLink()) {
      URL referenceDescriptionLink = template.getReferenceDescriptionLink();
      String referenceDescriptionLinkAsString = referenceDescriptionLink.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK,
          referenceDescriptionLinkAsString);
    }

    if (template.isSetCreatedDate()) {
      Date createdDate = template.getCreatedDate();
      String createdDateAsString = createdDate.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATED_DATE, createdDateAsString);
    }

    if (template.isSetModifiedDate()) {
      Date modifiedDate = template.getModifiedDate();
      String modifiedDateAsString = modifiedDate.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE, modifiedDateAsString);
    }

    if (template.isSetRights()) {
      String rights = template.getRights();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_RIGHTS, rights);
    }

    if (template.isSetNotes()) {
      String notes = template.getNotes();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_NOTES, notes);
    }

    if (template.isSetCurationStatus()) {
      String curationStatus = template.getCurationStatus();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS, curationStatus);
    }

    if (template.isSetModelType()) {
      ModelType modelType = template.getModelType();
      String modelTypeAsString = modelType.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_TYPE, modelTypeAsString);
    }

    if (template.isSetModelSubject()) {
      ModelClass modelSubject = template.getModelSubject();
      String modelSubjectAsString = modelSubject.toString();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_SUBJECT, modelSubjectAsString);
    }

    if (template.isSetFoodProcess()) {
      String foodProcess = template.getFoodProcess();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS, foodProcess);
    }

    if (template.isSetDependentVariable()) {
      String dependentVariable = template.getDependentVariable();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE, dependentVariable);
    }

    if (template.isSetDependentVariableUnit()) {
      String dependentVariableUnit = template.getDependentVariableUnit();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT, dependentVariableUnit);
    }

    if (template.isSetDependentVariableMin()) {
      double dependentVariableMin = template.getDependentVariableMin();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN, dependentVariableMin);
    }

    if (template.isSetDependentVariableMax()) {
      double dependentVariableMax = template.getDependentVariableMax();
      tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX, dependentVariableMax);
    }

    if (template.isSetIndependentVariables()) {
      // Join independent variables with commas. E.g. "time,temperature"
      String formattedIndepentVariables = Joiner.on("||").join(template.getIndependentVariables());
      tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE, formattedIndepentVariables);
    }

    if (template.isSetIndependentVariablesUnits()) {
      String formattedUnits = Joiner.on("||").join(template.getIndependentVariablesUnits());
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
      int hasDataAsInt = template.getHasData() ? 1 : 0;
      tuple.setValue(OpenFSMRSchema.ATT_HAS_DATA, hasDataAsInt);
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

    template
        .setReferenceDescription(tuple.getString(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION));

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
    template.setDependentVariableUnit(
        tuple.getString(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT));
    template
        .setDependentVariableMin(tuple.getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN));
    template
        .setDependentVariableMax(tuple.getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX));

    template.setIndependentVariables(
        tuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE).split("\\|\\|"));
    template.setIndependentVariablesUnits(
        tuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS).split("\\|\\|"));

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
      String speciesDetail = species.getDetail();
      template.setOrganismDetails(speciesDetail);
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
      String creatorGivenName = rc.getCreatorGivenName();
      template.setCreator(creatorGivenName);
    }
  }

  @Override
  public void setFamilyName() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatorFamilyName()) {
      String creatorFamilyName = rc.getCreatorFamilyName();
      template.setFamilyName(creatorFamilyName);
    }
  }

  @Override
  public void setContact() {
    ResultComponent rc = doc.getResultComponent();
    if (rc.isSetCreatorContact()) {
      String creatorContact = rc.getCreatorContact();
      template.setContact(creatorContact);
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
      String rights = rc.getRights();
      template.setRights(rights);
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
    Model model = doc.getModel();
    String modelId = model.getId();
    template.setModelId(modelId);
  }

  @Override
  public void setModelName() {
    Model model = doc.getModel();
    if (model.isSetName()) {
      String modelName = model.getName();
      template.setModelName(modelName);
    }
  }

  @Override
  public void setCreator() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetGivenName()) {
      String creatorGivenName = metadata.getGivenName();
      template.setCreator(creatorGivenName);
    }
  }

  @Override
  public void setFamilyName() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetFamilyName()) {
      String creatorFamilyName = metadata.getFamilyName();
      template.setFamilyName(creatorFamilyName);
    }
  }

  @Override
  public void setContact() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetContact()) {
      String creatorContact = metadata.getContact();
      template.setContact(creatorContact);
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
      String rights = metadata.getRights();
      template.setRights(rights);
    }
  }

  @Override
  public void setModelType() {
    Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
    if (metadata.isSetType()) {
      ModelType modelType = metadata.getType();
      template.setModelType(modelType);
    }
  }

  @Override
  public void setModelSubject() {
    Model model = doc.getModel();
    AssignmentRule sbmlRule = (AssignmentRule) model.getRule(0);
    ModelRule pmfRule = new ModelRule(sbmlRule);
    ModelClass subject = pmfRule.getModelClass();
    template.setModelSubject(subject);
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

  public ModelWithMicrobialDataTemplateCreator(SBMLDocument doc) {
    super(doc);
  }

  @Override
  public void setOrganismData() {
    Model model = doc.getModel();
    Species sbmlSpecies = model.getSpecies(0);
    PMFSpecies pmfSpecies = SBMLFactory.createPMFSpecies(sbmlSpecies);

    String speciesName = pmfSpecies.getName();
    template.setOrganismName(speciesName);

    if (pmfSpecies.isSetDetail()) {
      String speciesDetail = pmfSpecies.getDetail();
      template.setOrganismDetails(speciesDetail);
    }
  }

  @Override
  public void setMatrixData() {
    Model model = doc.getModel();
    Compartment sbmlCompartment = model.getCompartment(0);
    PMFCompartment pmfCompartment = SBMLFactory.createPMFCompartment(sbmlCompartment);

    String compartmentName = pmfCompartment.getName();
    template.setMatrixName(compartmentName);

    if (pmfCompartment.isSetDetail()) {
      String compartmentDetail = pmfCompartment.getDetail();
      template.setMatrixDetails(compartmentDetail);
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
      for (Constraint constraint : model.getListOfConstraints()) {
        LimitsConstraint lc = new LimitsConstraint(constraint);
        Limits lcLimits = lc.getLimits();
        if (lcLimits.getVar().equals(depUnitID)) {
          Double min = lcLimits.getMin();
          if (min != null) {
            template.setDependentVariableMin(min);
          }
          Double max = lcLimits.getMax();
          if (min != null) {
            template.setDependentVariableMax(max);
          }
          break;
        }
      }
    }
  }

  @Override
  public void setIndependentVariableData() {
    Model model = doc.getModel();

    LinkedList<String> indepVarCategories = new LinkedList<>();
    for (Parameter parameter : model.getListOfParameters()) {
      String unitID = parameter.getUnits();

      // Sets independent variable unit
      String unitName = model.getUnitDefinition(unitID).getName();

      if (!unitID.equals("dimensionless")) {
        if (DBUnits.getDBUnits().containsKey(unitName)) {
          String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
          if (!indepVarCategories.contains(unitCategory)) {
            indepVarCategories.add(unitCategory);
          }
        }
      }
    }

    String[] indepVarArray = indepVarCategories.toArray(new String[indepVarCategories.size()]);
    template.setIndependentVariables(indepVarArray);
  }

  @Override
  public void setHasData() {
    template.setHasData(true);
  }
}


class PrevalenceModelTemplateCreator extends ModelWithMicrobialDataTemplateCreator {

  /**
   * @param doc
   */
  public PrevalenceModelTemplateCreator(SBMLDocument doc) {
    super(doc);
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
    for (Constraint constraint : model.getListOfConstraints()) {
      LimitsConstraint lc = new LimitsConstraint(constraint);
      Limits lcLimits = lc.getLimits();
      if (lcLimits.getVar().equals(depId)) {
        if (lcLimits.getMin() != null) {
          template.setDependentVariableMin(lcLimits.getMin());
        }
        if (lcLimits.getMax() != null) {
          template.setDependentVariableMax(lcLimits.getMax());
        }
        break;
      }
    }
  }

  @Override
  public void setIndependentVariableData() {
    Model model = doc.getModel();
    AssignmentRule rule = (AssignmentRule) model.getRule(0);
    String depId = rule.getVariable();

    List<String> indepNames = new LinkedList<>();
    List<String> indepUnits = new LinkedList<>();
    List<Double> indepMins = new LinkedList<>();
    List<Double> indepMaxs = new LinkedList<>();

    for (Parameter parameter : model.getListOfParameters()) {
      // Skips parameter for the dependent variable
      if (parameter.getId().equals(depId)) {
        continue;
      }

      indepNames.add(parameter.getName());

      // Gets and sets independent variable unit
      String unitId = parameter.getUnits();
      String unitName = "";
      if (!unitId.equals("dimensionless")) {
        UnitDefinition unitDef = model.getUnitDefinition(unitId);
        if (unitDef != null) {
          unitName = unitDef.getName();
        }
      }
      indepUnits.add(unitName);

      // Sets dependent variable min & max
      for (Constraint constraint : model.getListOfConstraints()) {
        LimitsConstraint lc = new LimitsConstraint(constraint);
        Limits lcLimits = lc.getLimits();
        if (lcLimits.getVar().equals(parameter.getId())) {
          indepMins.add(lcLimits.getMin());
          indepMaxs.add(lcLimits.getMax());
          break;
        }
      }
    }

    String[] indepNamesArray = indepNames.toArray(new String[indepNames.size()]);
    template.setIndependentVariables(indepNamesArray);

    String[] indepUnitsArray = indepUnits.toArray(new String[indepUnits.size()]);
    template.setIndependentVariablesUnits(indepUnitsArray);

    double[] indepMinsArray = indepMins.stream().mapToDouble(Double::doubleValue).toArray();
    template.setIndependentVariablesMins(indepMinsArray);

    double[] indepMaxArray = indepMaxs.stream().mapToDouble(Double::doubleValue).toArray();
    template.setIndependentVariablesMaxs(indepMaxArray);
  }
}


class ModelWithoutMicrobialDataTemplateCreator extends ModelTemplateCreator {

  public ModelWithoutMicrobialDataTemplateCreator(SBMLDocument doc) {
    super(doc);
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
      Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
      if (dbUnits.containsKey(unitName)) {
        UnitsFromDB ufdb = dbUnits.get(unitName);
        String unitCategory = ufdb.getKind_of_property_quantity();
        template.setDependentVariable(unitCategory);
      }
    }
  }

  @Override
  public void setIndependentVariableData() {
    Model model = doc.getModel();
    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    String depName = rule.getRule().getVariable();

    LinkedList<String> unitCategories = new LinkedList<>();
    for (Parameter param : model.getListOfParameters()) {
      if (!param.isConstant() && !param.getId().equals(depName)) {
        String unitID = param.getUnits();

        // Sets independent variable unit
        String unitName = model.getUnitDefinition(unitID).getName();

        if (!unitID.equals("dimensionless")) {
          UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
          String unitCategory = ufdb.getKind_of_property_quantity();
          unitCategories.add(unitCategory);
        }
      }
    }

    String[] unitCategoryArray = unitCategories.toArray(new String[unitCategories.size()]);
    template.setIndependentVariables(unitCategoryArray);
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
    double[] indepVarMaxs = Arrays.stream(indepVarMaxTokens).mapToDouble(Double::parseDouble).toArray();
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
