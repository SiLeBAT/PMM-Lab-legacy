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

import java.io.*;

import org.knime.core.node.*;
import org.knime.core.node.port.*;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

/**
 * Fsk meta data editor node model.
 */
public final class FskMetadataEditorNodeModel
		extends AbstractWizardNodeModel<FskMetadataEditorViewRepresentation, FskMetadataEditorViewValue> {

	private FskMetaData m_metadata;

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
			if (val.metadata == null && m_metadata != null) {
				val.metadata = m_metadata;
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
			m_metadata = val.metadata;
		}

		exec.setProgress(1);
		inObj.template = val.metadata;
		return new PortObject[] { inObj };
	}

	@Override
	protected void performReset() {
		m_metadata = null;
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// Unused
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	// internals
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		File file = new File(nodeInternDir, "metadata.bin");
		try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
			m_metadata = (FskMetaData) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		File file = new File(nodeInternDir, "metadata.bin");
		try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(m_metadata);
		}
	}
}