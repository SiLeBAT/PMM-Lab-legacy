package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractSVGWizardNodeModel;

import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

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
		inObj.template = viewValue.metadata;

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
			viewValue.metadata = originalMetaData;
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