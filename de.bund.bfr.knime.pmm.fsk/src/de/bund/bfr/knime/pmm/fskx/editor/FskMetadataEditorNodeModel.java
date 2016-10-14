package de.bund.bfr.knime.pmm.fskx.editor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractSVGWizardNodeModel;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.editor.FskMetadataEditorViewValue.Variable;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/

/**
 * Fsk meta data editor node model.
 */
public final class FskMetadataEditorNodeModel
		extends AbstractSVGWizardNodeModel<FskMetadataEditorViewRepresentation, FskMetadataEditorViewValue> {

	/**
	 * Original meta data from the input FskPortObject. Null before run.
	 */
	private FskMetaData originalMetaData;

	protected FskMetadataEditorNodeModel() {
		super(new PortType[] { FskPortObject.TYPE }, new PortType[] { FskPortObject.TYPE },
				(new FskMetadataEditorNodeFactory()).getInteractiveViewName());
	}

	@Override
	public FskMetadataEditorViewRepresentation createEmptyViewRepresentation() {
		return new FskMetadataEditorViewRepresentation();
	}

	@Override
	public FskMetadataEditorViewValue createEmptyViewValue() {
		return new FskMetadataEditorViewValue();
	}

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.fskx.editor";
	}

	@Override
	public boolean isHideInWizard() {
		return false;
	}

	@Override
	public ValidationError validateViewValue(FskMetadataEditorViewValue viewContent) {
		synchronized (getLock()) {
			// nothing to do.
		}
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
		getViewValue().saveToNodeSettings(content);
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		// nothing
		return inSpecs;
	}

	@Override
	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		// Unused
	}

	@Override
	protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView, PortObject[] inObjects,
			ExecutionContext exec) throws Exception {

		FskPortObject inObj = (FskPortObject) inObjects[0];

		FskMetadataEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (originalMetaData == null) {
			originalMetaData = inObj.template;
			performReset();
		}

		// Create new FskMetaData with changes
		viewValue = getViewValue();
		inObj.template.modelName = viewValue.modelName;
		inObj.template.modelId = viewValue.modelId;
		inObj.template.modelLink = Strings.isNullOrEmpty(viewValue.modelLink) ? null : new URL(viewValue.modelLink);
		inObj.template.organism = viewValue.organism;
		inObj.template.organismDetails = viewValue.organismDetails;
		inObj.template.matrix = viewValue.matrix;
		inObj.template.matrixDetails = viewValue.matrixDetails;
		inObj.template.contact = viewValue.contact;
		inObj.template.software = Strings.isNullOrEmpty(viewValue.software) ? null
				: FskMetaData.Software.valueOf(viewValue.software);
		inObj.template.referenceDescription = viewValue.referenceDescription;
		inObj.template.referenceDescriptionLink = Strings.isNullOrEmpty(viewValue.referenceDescriptionLink) ? null
				: new URL(viewValue.referenceDescriptionLink);
		inObj.template.createdDate = FskMetaData.dateFormat.parse(viewValue.createdDate);
		inObj.template.modifiedDate = FskMetaData.dateFormat.parse(viewValue.modifiedDate);
		inObj.template.rights = viewValue.rights;
		inObj.template.notes = viewValue.notes;
		inObj.template.curated = viewValue.curated;
		inObj.template.type = Strings.isNullOrEmpty(viewValue.modelType) ? null
				: ModelType.valueOf(viewValue.modelType);
		inObj.template.subject = Strings.isNullOrEmpty(viewValue.modelSubject) ? null
				: ModelClass.valueOf(viewValue.modelSubject);
		inObj.template.foodProcess = viewValue.foodProcess;

		Variable depVar = viewValue.variables.stream().filter(v -> v.isDependent == true).findAny().get();
		List<Variable> indepVars = viewValue.variables.stream().filter(v -> !v.isDependent)
				.collect(Collectors.toList());

		inObj.template.dependentVariable = depVar.name;
		inObj.template.dependentVariableUnit = depVar.unit;
		inObj.template.dependentVariableMin = depVar.min;
		inObj.template.dependentVariableMax = depVar.max;

		inObj.template.independentVariables = new String[indepVars.size()];
		inObj.template.independentVariableUnits = new String[indepVars.size()];
		inObj.template.independentVariableMins = new double[indepVars.size()];
		inObj.template.independentVariableMaxs = new double[indepVars.size()];
		inObj.template.independentVariableValues = new double[indepVars.size()];

		for (int i = 0; i < indepVars.size(); i++) {
			inObj.template.independentVariables[i] = indepVars.get(i).name;
			inObj.template.independentVariableUnits[i] = indepVars.get(i).unit;
			inObj.template.independentVariableMins[i] = indepVars.get(i).min;
			inObj.template.independentVariableMaxs[i] = indepVars.get(i).max;
			inObj.template.independentVariableValues[i] = indepVars.get(i).value;
		}
		inObj.template.hasData = viewValue.hasData;

		return new PortObject[] { inObj };
	}

	@Override
	protected boolean generateImage() {
		return false;
	}

	@Override
	protected void performReset() {
		if (originalMetaData != null) {
			FskMetadataEditorViewValue viewValue = getViewValue();

			viewValue.modelName = originalMetaData.modelName;
			viewValue.modelId = originalMetaData.modelId;
			viewValue.modelLink = originalMetaData.modelLink == null ? "" : originalMetaData.modelLink.toString();
			viewValue.organism = originalMetaData.organism;
			viewValue.organismDetails = originalMetaData.organismDetails;
			viewValue.matrix = originalMetaData.matrix;
			viewValue.matrixDetails = originalMetaData.matrixDetails;
			viewValue.contact = originalMetaData.contact;
			viewValue.referenceDescription = originalMetaData.referenceDescription;
			viewValue.referenceDescriptionLink = originalMetaData.referenceDescriptionLink == null ? ""
					: originalMetaData.referenceDescriptionLink.toString();
			viewValue.createdDate = originalMetaData.createdDate == null ? ""
					: FskMetaData.dateFormat.format(originalMetaData.createdDate);
			viewValue.modifiedDate = originalMetaData.modifiedDate == null ? ""
					: FskMetaData.dateFormat.format(originalMetaData.modifiedDate);
			viewValue.rights = originalMetaData.rights;
			viewValue.notes = originalMetaData.notes;
			viewValue.curated = originalMetaData.curated;
			viewValue.modelType = originalMetaData.type == null ? "" : originalMetaData.type.toString();
			viewValue.modelSubject = originalMetaData.subject.toString();
			viewValue.foodProcess = originalMetaData.foodProcess;

			viewValue.variables = new ArrayList<>();

			Variable depVar = new Variable();
			depVar.isDependent = true;
			depVar.name = originalMetaData.dependentVariable;
			depVar.unit = originalMetaData.dependentVariableUnit;
			depVar.min = originalMetaData.dependentVariableMin;
			depVar.max = originalMetaData.dependentVariableMax;
			viewValue.variables.add(depVar);

			for (int i = 0; i < originalMetaData.independentVariables.length; i++) {
				Variable indepVar = new Variable();
				if (originalMetaData.independentVariables != null)
					indepVar.name = originalMetaData.independentVariables[i];
				if (originalMetaData.independentVariableUnits != null)
					indepVar.unit = originalMetaData.independentVariableUnits[i];
				if (originalMetaData.independentVariableMins != null)
					indepVar.min = originalMetaData.independentVariableMins[i];
				if (originalMetaData.independentVariableMaxs != null)
					indepVar.max = originalMetaData.independentVariableMaxs[i];
				if (originalMetaData.independentVariableValues != null)
					indepVar.value = originalMetaData.independentVariableValues[i];
				viewValue.variables.add(indepVar);
			}

			setViewValue(viewValue);
		}
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// Unused
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		FskMetadataEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
		}
		viewValue.saveToNodeSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		FskMetadataEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
		}
		viewValue.loadFromNodeSettings(settings);
		setViewValue(viewValue);
	}
}