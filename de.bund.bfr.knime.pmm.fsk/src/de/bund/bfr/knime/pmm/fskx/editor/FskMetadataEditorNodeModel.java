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
package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

/**
 * Fsk meta data editor node model.
 */
public final class FskMetadataEditorNodeModel
		extends AbstractWizardNodeModel<FskMetadataEditorViewRepresentation, FskMetadataEditorViewValue> {

	private final FskMetaDataSettings m_config;

	protected FskMetadataEditorNodeModel() {
		super(new PortType[] { FskPortObject.TYPE }, new PortType[] { FskPortObject.TYPE },
				(new FskMetadataEditorNodeFactory()).getInteractiveViewName());
		
		m_config = new FskMetaDataSettings();
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
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
	}

	@Override
	public FskMetadataEditorViewValue getViewValue() {
		FskMetadataEditorViewValue val = super.getViewValue();
		synchronized (getLock()) {
			if (val == null) {
				val = createEmptyViewValue();
			}
			if (val.metadata == null && m_config != null && m_config.metaData != null) {
				val.metadata = m_config.metaData;
			}
		}
		return val;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return inSpecs;
	}

	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) {

		FskPortObject inObj = (FskPortObject) inObjects[0];
		
		FskMetadataEditorViewValue val = getViewValue();
		synchronized (getLock()) {
			// If not executed
			if (val.metadata == null) {
				val.metadata = inObj.template;
			}
			m_config.metaData = val.metadata;
		}
		
		inObj.template = val.metadata;
		
		exec.setProgress(1);
		return new PortObject[] { inObj };
	}

	@Override
	protected void performReset() {
		m_config.metaData = null;
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// Unused
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}
}