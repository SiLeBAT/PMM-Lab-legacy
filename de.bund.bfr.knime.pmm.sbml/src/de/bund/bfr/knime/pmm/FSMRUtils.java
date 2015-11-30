package de.bund.bfr.knime.pmm;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;

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
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.SBMLFactory;

public class FSMRUtils {

	public static FSMRTemplate processData(NuMLDocument doc) {
		FSMRTemplate template = new FSMRTemplateImpl();

		ConcentrationOntology concOntology = doc.getConcentrationOntologyTerm();

		// PMF organism
		PMFSpecies species = concOntology.getSpecies();
		template.setOrganismName(species.getName());
		if (species.isSetDetail()) {
			String organismDetail = species.getDetail();
			template.setOrganismDetails(organismDetail);
		}

		// PMF environment
		PMFCompartment compartment = concOntology.getCompartment();
		template.setMatrixName(compartment.getName());
		if (compartment.isSetDetail()) {
			String matrixDetail = compartment.getDetail();
			template.setMatrixDetails(matrixDetail);
		}

		ResultComponent rc = doc.getResultComponent();
		// Sets creator
		if (rc.isSetCreatorGivenName()) {
			String creatorGivenName = rc.getCreatorGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		// Sets created date
		if (rc.isSetCreatedDate()) {
			String createdDateAsString = rc.getCreatedDate();
			try {
				Date createdDate = dateFormat.parse(createdDateAsString);
				template.setCreatedDate(createdDate);
			} catch (ParseException e) {
				System.err.println(createdDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets modified date
		if (rc.isSetModifiedDate()) {
			String modifiedDateAsString = rc.getModifiedDate();
			try {
				Date modifiedDate = dateFormat.parse(modifiedDateAsString);
				template.setModifiedDate(modifiedDate);
			} catch (ParseException e) {
				System.err.println(modifiedDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets model rights
		if (rc.isSetRights()) {
			String rights = rc.getRights();
			template.setRights(rights);
		}

		// Sets model type
		template.setModelType(ModelType.EXPERIMENTAL_DATA);

		// Sets model subject as ModelClass.UNKNOWN since the data files do not
		// keep the subject of the model
		template.setModelSubject(ModelClass.UNKNOWN);

		return template;
	}

	public static FSMRTemplate processPrimaryModelWithMicrobialData(SBMLDocument doc) {
		FSMRTemplate template = new FSMRTemplateImpl();
		Model model = doc.getModel();

		template.setModelId(model.getId());

		if (model.isSetName()) {
			String modelName = model.getName();
			template.setModelName(modelName);
		}

		// PMF organism
		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		String speciesName = species.getName();
		template.setOrganismName(speciesName);
		if (species.isSetDetail()) {
			String speciesDetail = species.getDetail();
			template.setOrganismDetails(speciesDetail);
		}

		// PMF environment
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		String compartmentName = compartment.getName();
		template.setMatrixName(compartmentName);
		if (compartment.isSetDetail()) {
			String matrixDetail = compartment.getDetail();
			template.setMatrixDetails(matrixDetail);
		}

		Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

		// Sets creator
		if (metadata.isSetGivenName()) {
			String creatorGivenName = metadata.getGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		// Sets created date
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

		// Sets modified date
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

		// Sets model rights
		if (metadata.isSetRights()) {
			String rights = metadata.getRights();
			template.setRights(rights);
		}

		if (model.isSetNotes()) {
			try {
				String notes = model.getNotesString();
				template.setNotes(notes);
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		// Sets model type
		if (metadata.isSetType()) {
			template.setModelType(metadata.getType());
		}

		// Sets model subject
		ModelRule modelRule = new ModelRule((AssignmentRule) model.getRule(0));
		ModelClass modelSubject = modelRule.getModelClass();
		template.setModelSubject(modelSubject);

		if (species.getSpecies().isSetUnits()) {
			String depUnitID = species.getSpecies().getUnits();

			// Sets dependent variable unit
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			template.setDependentVariableUnit(depUnitName);

			// Sets dependent variable
			if (!depUnitID.equals("dimensionless")) {
				String depUnitCategory = DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
				template.setDependentVariable(depUnitCategory);
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
					if (max != null) {
						template.setDependentVariableMax(max);
					}

					break;
				}
			}
		}

		List<String> independentVariableCategories = new LinkedList<>();
		for (Parameter parameter : model.getListOfParameters()) {
			String unitID = parameter.getUnits();

			// Sets independent variable unit
			String unitName = model.getUnitDefinition(unitID).getName();

			if (!unitID.equals("dimensionless")) {
				String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
				if (!independentVariableCategories.contains(unitCategory)) {
					independentVariableCategories.add(unitCategory);
				}
			}
		}
		String[] independentVariableArray = independentVariableCategories
				.toArray(new String[independentVariableCategories.size()]);
		template.setIndependentVariables(independentVariableArray);

		return template;
	}

	public static FSMRTemplate processPrimaryModelWithoutMicrobialData(SBMLDocument doc) {

		FSMRTemplate template = new FSMRTemplateImpl();

		Model model = doc.getModel();

		template.setModelId(model.getId());

		if (model.isSetName()) {
			String modelName = model.getName();
			template.setModelName(modelName);
		}

		Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

		// Sets creator
		if (metadata.isSetGivenName()) {
			String creatorGivenName = metadata.getGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		// Sets created date
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

		// Sets modified date
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

		// Sets model rights
		if (metadata.isSetRights()) {
			String rights = metadata.getRights();
			template.setRights(rights);
		}

		// Sets model type
		if (metadata.isSetType()) {
			template.setModelType(metadata.getType());
		}

		// Sets model subject
		ModelRule modelRule = new ModelRule((AssignmentRule) model.getRule(0));
		ModelClass modelSubject = modelRule.getModelClass();
		template.setModelSubject(modelSubject);

		// Sets model notes
		if (model.isSetNotes()) {
			try {
				String notes = model.getNotesString();
				template.setNotes(notes);
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		String depName = rule.getRule().getVariable();
		Parameter depParam = model.getParameter(depName);

		if (depParam.isSetUnits()) {
			// Sets dependent variable unit
			String unitID = depParam.getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			template.setDependentVariableUnit(unitName);

			// Sets dependent variable
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				String unitCategory = dbUnits.get(unitName).getKind_of_property_quantity();
				template.setDependentVariable(unitCategory);
			}

			// Sets dependent variable min & max
			for (Constraint contraint : model.getListOfConstraints()) {
				LimitsConstraint lc = new LimitsConstraint(contraint);
				Limits lcLimits = lc.getLimits();
				if (lcLimits.getVar().equals(depParam.getId())) {
					Double min = lcLimits.getMin();
					if (min != null) {
						template.setDependentVariableMin(min);
					}
					Double max = lcLimits.getMax();
					if (max != null) {
						template.setDependentVariableMax(max);
					}
					break;
				}
			}
		}

		return template;
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

		if (template.isSetReferenceDescription()) {
			String referenceDescription = template.getReferenceDescription();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION, referenceDescription);
		}

		if (template.isSetReferenceDescriptionLink()) {
			URL referenceDescriptionLink = template.getReferenceDescriptionLink();
			String referenceDescriptionLinkAsString = referenceDescriptionLink.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK, referenceDescriptionLinkAsString);
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
			String formattedIndepentVariables = Joiner.on(",").join(template.getIndependentVariables());
			tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE, formattedIndepentVariables);
		}

		return tuple;
	}
}