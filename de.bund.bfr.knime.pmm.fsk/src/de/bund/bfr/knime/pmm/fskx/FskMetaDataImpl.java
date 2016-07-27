package de.bund.bfr.knime.pmm.fskx;

import java.net.URL;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;

import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

/**
 * TODO: docstring
 *
 * @author Miguel de Alba, BfR, Berlin
 */
public class FskMetaDataImpl implements FskMetaData {

	private String modelName;
	private String modelId;
	private URL modelLink;
	private String organism;
	private String organismDetails;
	private String matrix;
	private String matrixDetails;
	private String creator;
	private String familyName;
	private String contact;
	private String referenceDescription;
	private URL referenceDescriptionLink;
	private Date createdDate;
	private Date modifiedDate;
	private String rights;
	private String notes;
	private boolean isCurated;
	private ModelType modelType;
	private ModelClass modelSubject;
	private String foodProcess;
	private String depvar;
	private String depvarUnit;
	private Double depvarMin;
	private Double depvarMax;
	private List<String> indepvars;
	private List<String> indepvarUnits;
	private List<Double> indepvarMins;
	private List<Double> indepvarMaxs;
	private List<Double> indepvarValues;
	private boolean hasData;

	// --- model name ---
	public String getModelName() {
		if (modelName == null)
			throw new RuntimeException("Model name is not set");
		return modelName;
	}

	public boolean isSetModelName() {
		return modelName != null;
	}

	public void setModelName(final String name) {
		if (!Strings.isNullOrEmpty(name))
			modelName = name;
	}

	public void unsetModelName() {
		modelName = null;
	}

	// --- model id ---
	public String getModelId() {
		if (modelId == null)
			throw new RuntimeException("Model id is not set");
		return modelId;
	}

	public boolean isSetModelId() {
		return modelId != null;
	}

	public void setModelId(final String id) {
		if (!Strings.isNullOrEmpty(id))
			modelId = id;
	}

	public void unsetModelId() {
		modelId = null;
	}

	// --- model link ---
	public URL getModelLink() {
		if (modelLink == null)
			throw new RuntimeException("Model link is not set");
		return modelLink;
	}

	public boolean isSetModelLink() {
		return modelLink != null;
	}

	public void setModelLink(final URL link) {
		if (link != null)
			modelLink = link;
	}

	public void unsetModelLink() {
		modelLink = null;
	}

	// --- organism name ---
	public String getOrganism() {
		if (organism == null)
			throw new RuntimeException("Organism is not set");
		return organism;
	}

	public boolean isSetOrganism() {
		return organism != null;
	}

	public void setOrganism(final String organism) {
		if (!Strings.isNullOrEmpty(organism))
			this.organism = organism;
	}

	public void unsetOrganism() {
		organism = null;
	}

	// --- organism details ---
	public String getOrganismDetails() {
		if (organismDetails == null)
			throw new RuntimeException("Organism details are not set");
		return organismDetails;
	}

	public boolean isSetOrganismDetails() {
		return organismDetails != null;
	}

	public void setOrganismDetails(final String details) {
		if (!Strings.isNullOrEmpty(details))
			organismDetails = details;
	}

	public void unsetOrganismDetails() {
		organismDetails = null;
	}

	// --- matrix name ---
	public String getMatrix() {
		if (matrix == null)
			throw new RuntimeException("Matrix is not set");
		return matrix;
	}

	public boolean isSetMatrix() {
		return matrix != null;
	}

	public void setMatrix(final String name) {
		if (!Strings.isNullOrEmpty(name))
			matrix = name;
	}

	public void unsetMatrix() {
		matrix = null;
	}

	// --- matrix details ---
	public String getMatrixDetails() {
		if (matrixDetails == null)
			throw new RuntimeException("Matrix details are not set");
		return matrixDetails;
	}

	public boolean isSetMatrixDetails() {
		return matrixDetails != null;
	}

	public void setMatrixDetails(final String details) {
		if (!Strings.isNullOrEmpty(details))
			matrixDetails = details;
	}

	public void unsetMatrixDetails() {
		matrixDetails = null;
	}

	// --- creator ---
	public String getCreator() {
		if (creator == null)
			throw new RuntimeException("Creator is not set");
		return creator;
	}

	public boolean isSetCreator() {
		return creator != null;
	}

	public void setCreator(final String creator) {
		if (!Strings.isNullOrEmpty(creator))
			this.creator = creator;
	}

	public void unsetCreator() {
		creator = null;
	}

	// -- family name ---
	public String getFamilyName() {
		if (familyName == null)
			throw new RuntimeException("Family name is not set");
		return familyName;
	}

	public boolean isSetFamilyName() {
		return familyName != null;
	}

	public void setFamilyName(final String name) {
		if (!Strings.isNullOrEmpty(name))
			familyName = name;
	}

	public void unsetFamilyName() {
		familyName = null;
	}

	// --- contact ---
	public String getContact() {
		if (contact == null)
			throw new RuntimeException("Contact is not set");
		return contact;
	}

	public boolean isSetContact() {
		return contact != null;
	}

	public void setContact(final String contact) {
		if (!Strings.isNullOrEmpty(contact))
			this.contact = contact;
	}

	public void unsetContact() {
		contact = null;
	}

	// --- reference description ---
	public String getReferenceDescription() {
		if (referenceDescription == null)
			throw new RuntimeException("Reference description is not set");
		return referenceDescription;
	}

	public boolean isSetReferenceDescription() {
		return referenceDescription != null;
	}

	public void setReferenceDescription(final String description) {
		if (!Strings.isNullOrEmpty(description))
			referenceDescription = description;
	}

	public void unsetReferenceDescription() {
		referenceDescription = null;
	}

	// --- link to the reference description ---
	public URL getReferenceDescriptionLink() {
		if (referenceDescriptionLink == null)
			throw new RuntimeException("Reference description link is not set");
		return referenceDescriptionLink;
	}

	public boolean isSetReferenceDescriptionLink() {
		return referenceDescriptionLink != null;
	}

	public void setReferenceDescriptionLink(final URL link) {
		if (link != null)
			referenceDescriptionLink = link;
	}

	public void unsetReferenceDescriptionLink() {
		referenceDescriptionLink = null;
	}

	// --- creation date ---
	public Date getCreatedDate() {
		if (createdDate == null)
			throw new RuntimeException("Creation date is not set");
		return createdDate;
	}

	public boolean isSetCreatedDate() {
		return createdDate != null;
	}

	public void setCreatedDate(final Date date) {
		if (date != null)
			createdDate = date;
	}

	public void unsetCreatedDate() {
		createdDate = null;
	}

	// --- last modification date ---
	public Date getModifiedDate() {
		if (modifiedDate == null)
			throw new RuntimeException("Date of last modification is not set");
		return modifiedDate;
	}

	public boolean isSetModifiedDate() {
		return modifiedDate != null;
	}

	public void setModifiedDate(final Date date) {
		if (date != null)
			modifiedDate = date;
	}

	public void unsetModifiedDate() {
		modifiedDate = null;
	}

	// --- rights ---
	public String getRights() {
		if (rights == null)
			throw new RuntimeException("Rights are not set");
		return rights;
	}

	public boolean isSetRights() {
		return rights != null;
	}

	public void setRights(final String rights) {
		if (!Strings.isNullOrEmpty(rights))
			this.rights = rights;
	}

	public void unsetRights() {
		rights = null;
	}

	// --- notes ---
	public String getNotes() {
		if (notes == null)
			throw new RuntimeException("Notes are not set");
		return notes;
	}

	public boolean isSetNotes() {
		return notes != null;
	}

	public void setNotes(final String notes) {
		if (!Strings.isNullOrEmpty(notes))
			this.notes = notes;
	}

	public void unsetNotes() {
		notes = null;
	}

	// --- cur status ---
	public boolean isCurated() {
		return isCurated;
	}

	public void setCurated(final boolean isCurated) {
		this.isCurated = isCurated;
	}

	// --- model type ---
	public ModelType getModelType() {
		if (modelType == null)
			throw new RuntimeException("Model type is not set");
		return modelType;
	}

	public boolean isSetModelType() {
		return modelType != null;
	}

	public void setModelType(final ModelType type) {
		if (type != null)
			modelType = type;
	}

	public void unsetModelType() {
		modelType = null;
	}

	// --- model subject ---
	public ModelClass getModelSubject() {
		if (modelSubject == null)
			throw new RuntimeException("Model subject is not set");
		return modelSubject;
	}

	public boolean isSetModelSubject() {
		return modelSubject != null;
	}

	public void setModelSubject(final ModelClass subject) {
		if (subject != null)
			modelSubject = subject;
	}

	public void unsetModelSubject() {
		modelSubject = null;
	}

	// --- food process ---
	public String getFoodProcess() {
		if (foodProcess == null)
			throw new RuntimeException("Food process is not set");
		return foodProcess;
	}

	public boolean isSetFoodProcess() {
		return foodProcess != null;
	}

	public void setFoodProcess(final String process) {
		if (!Strings.isNullOrEmpty(process))
			foodProcess = process;
	}

	public void unsetFoodProcess() {
		foodProcess = null;
	}

	// --- dependent variable ---
	public String getDependentVariable() {
		if (depvar == null)
			throw new RuntimeException("Dependent variable is not set");
		return depvar;
	}

	public boolean isSetDependentVariable() {
		return depvar != null;
	}

	public void setDependentVariable(final String var) {
		if (!Strings.isNullOrEmpty(var))
			depvar = var;
	}

	public void unsetDependentVariable() {
		depvar = null;
	}

	// --- dependent variable unit ---
	public String getDependentVariableUnit() {
		if (depvarUnit == null)
			throw new RuntimeException("Dependent variable unit is not set");
		return depvarUnit;
	}

	public boolean isSetDependentVariableUnit() {
		return depvarUnit != null;
	}

	public void setDependentVariableUnit(final String unit) {
		if (!Strings.isNullOrEmpty(unit))
			depvarUnit = unit;
	}

	public void unsetDependentVariableUnit() {
		depvarUnit = null;
	}

	// --- dependent variable min ---
	public double getDependentVariableMin() {
		if (depvarMin == null)
			throw new RuntimeException("Minimum value of dependent variable is not set");
		return depvarMin;
	}

	public boolean isSetDependentVariableMin() {
		return depvarMin != null;
	}

	public void setDependentVariableMin(final double min) {
		depvarMin = min;
	}

	public void unsetDependentVariableMin() {
		depvarMin = null;
	}

	// --- dependent variable max ---
	public double getDependentVariableMax() {
		if (depvarMax == null)
			throw new RuntimeException("Maximum value of dependent varaible is not set");
		return depvarMax;
	}

	public boolean isSetDependentVariableMax() {
		return depvarMax != null;
	}

	public void setDependentVariableMax(final double max) {
		depvarMax = max;
	}

	public void unsetDependentVariableMax() {
		depvarMax = null;
	}

	// --- independent variables ---
	public List<String> getIndependentVariables() {
		if (indepvars == null)
			throw new RuntimeException("Independent variables are not set");
		return indepvars;
	}

	public boolean isSetIndependentVariables() {
		return indepvars != null;
	}

	public void setIndependentVariables(final List<String> vars) {
		if (vars != null && !vars.isEmpty())
			indepvars = vars;
	}

	public void unsetIndependentVariables() {
		indepvars = null;
	}

	// --- units of the independent variables ---
	public List<String> getIndependentVariableUnits() {
		if (indepvarUnits == null)
			throw new RuntimeException("Independent variables are not set");
		return indepvarUnits;
	}

	public boolean isSetIndependentVariableUnits() {
		return indepvarUnits != null;
	}

	public void setIndependentVariableUnits(final List<String> units) {
		if (units != null && !units.isEmpty())
			indepvarUnits = units;
	}

	public void unsetIndependentVariableUnits() {
		indepvarUnits = null;
	}

	// --- minimum values of the independent variables ---
	public List<Double> getIndependentVariableMins() {
		if (indepvarMins == null)
			throw new RuntimeException("Minimum values of independent variables are not set");
		return indepvarMins;
	}

	public boolean isSetIndependentVariableMins() {
		return indepvarMins != null;
	}

	public void setIndependentVariableMins(List<Double> mins) {
		if (mins != null && !mins.isEmpty())
			indepvarMins = mins;
	}

	public void unsetIndependentVariableMins() {
		indepvarMins = null;
	}

	// --- maximum values of the independent variables ---
	public List<Double> getIndependentVariableMaxs() {
		if (indepvarMaxs == null)
			throw new RuntimeException("Maximum values of independent variables are not set");
		return indepvarMaxs;
	}

	public boolean isSetIndependentVariableMaxs() {
		return indepvarMaxs != null;
	}

	public void setIndependentVariableMaxs(final List<Double> maxs) {
		if (maxs != null && !maxs.isEmpty())
			indepvarMaxs = maxs;
	}

	public void unsetIndependentVariableMaxs() {
		indepvarMaxs = null;
	}

	// --- values of the independent variables ---
	public List<Double> getIndependentVariableValues() {
		if (indepvarValues == null)
			throw new RuntimeException("Values of independent variables are not set");
		return indepvarValues;
	}

	public boolean isSetIndependentVariableValues() {
		return indepvarValues != null;
	}

	public void setIndependentVariableValues(final List<Double> values) {
		if (values != null && !values.isEmpty())
			indepvarValues = values;
	}

	public void unsetIndependentVariableValues() {
		indepvarValues = null;
	}

	// --- has data ---
	public boolean hasData() {
		return hasData;
	}

	public void setHasData(final boolean hasData) {
		this.hasData = hasData;
	}
}
