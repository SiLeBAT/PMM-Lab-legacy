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
		// TODO Auto-generated method stub
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
		FskMetaData metadata = new FskMetaData();
		metadata.modelName = viewValue.modelName;
		metadata.modelId = viewValue.modelId;
		metadata.modelLink = Strings.isNullOrEmpty(viewValue.modelLink) ? null : new URL(viewValue.modelLink);
		metadata.organism = viewValue.organism;
		metadata.organismDetails = viewValue.organismDetails;
		metadata.matrix = viewValue.matrix;
		metadata.matrixDetails = viewValue.matrixDetails;
		metadata.contact = viewValue.contact;
		metadata.referenceDescription = viewValue.referenceDescription;
		metadata.referenceDescriptionLink = Strings.isNullOrEmpty(viewValue.referenceDescriptionLink) ? null
				: new URL(viewValue.referenceDescriptionLink);
		metadata.createdDate = FskMetaData.dateFormat.parse(viewValue.createdDate);
		metadata.modifiedDate = FskMetaData.dateFormat.parse(viewValue.modifiedDate);
		metadata.rights = viewValue.rights;
		metadata.notes = viewValue.notes;
		metadata.curated = viewValue.curated;
		metadata.type = Strings.isNullOrEmpty(viewValue.modelType) ? null : ModelType.valueOf(viewValue.modelType);
		metadata.subject = Strings.isNullOrEmpty(viewValue.modelSubject) ? null
				: ModelClass.valueOf(viewValue.modelSubject);
		metadata.foodProcess = viewValue.foodProcess;

		Variable depVar = viewValue.variables.stream().filter(v -> v.isDependent == true).findAny().get();
		List<Variable> indepVars = viewValue.variables.stream().filter(v -> !v.isDependent)
				.collect(Collectors.toList());

		metadata.dependentVariable = depVar.name;
		metadata.dependentVariableUnit = depVar.unit;
		metadata.dependentVariableMin = depVar.min;
		metadata.dependentVariableMax = depVar.max;

		metadata.independentVariables = new String[indepVars.size()];
		metadata.independentVariableUnits = new String[indepVars.size()];
		metadata.independentVariableMins = new double[indepVars.size()];
		metadata.independentVariableMaxs = new double[indepVars.size()];
		metadata.independentVariableValues = new double[indepVars.size()];

		for (int i = 0; i < indepVars.size(); i++) {
			metadata.independentVariables[i] = indepVars.get(i).name;
			metadata.independentVariableUnits[i] = indepVars.get(i).unit;
			metadata.independentVariableMins[i] = indepVars.get(i).min;
			metadata.independentVariableMaxs[i] = indepVars.get(i).max;
			metadata.independentVariableValues[i] = indepVars.get(i).value;
		}
		metadata.hasData = viewValue.hasData;

		inObj.template = metadata;
		return new PortObject[] { inObj };
	}

	@Override
	protected boolean generateImage() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
//		new FskMetadataEditorViewValue().loadFromNodeSettings(settings);
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