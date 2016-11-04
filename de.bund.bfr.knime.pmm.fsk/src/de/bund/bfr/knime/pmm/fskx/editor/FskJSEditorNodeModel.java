package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

/**
 * JS version of FSK Editor.
 *
 * Allows the edition of scripts in an FSK model.
 */
public final class FskJSEditorNodeModel
 extends AbstractWizardNodeModel<FskJSEditorNodeRepresentation, FskJSEditorNodeValue> {
	
	SettingsModelString model = new SettingsModelString("model", null);
	SettingsModelString param = new SettingsModelString("param", null);
	SettingsModelString viz = new SettingsModelString("viz", null);
	
	protected FskJSEditorNodeModel() {
		super(new PortType[] { FskPortObject.TYPE},
				new PortType[] { FskPortObject.TYPE },
				(new FskJSEditorNodeFactory().getInteractiveViewName()));
	}
	
	@Override
	public FskJSEditorNodeRepresentation createEmptyViewRepresentation() {
		return new FskJSEditorNodeRepresentation();
	}
	
	@Override
	public FskJSEditorNodeValue createEmptyViewValue() {
		return new FskJSEditorNodeValue();
	}
	
	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.fskx.jseditor";
	}
	
	@Override
	public boolean isHideInWizard() {
		return false;
	}
	
	@Override
	public ValidationError validateViewValue(FskJSEditorNodeValue viewContent) {
		return null;
	}
	
	@Override
	public void saveCurrentValue(NodeSettingsWO content) {	
	}
	
	@Override
	public FskJSEditorNodeValue getViewValue() {
		FskJSEditorNodeValue val = super.getViewValue();
		synchronized (getLock()) {
			if (val == null) {
				val = createEmptyViewValue();
			}
			if (val.modelScript == null && model.getStringValue() != null) {
				val.modelScript = model.getStringValue();
			}
			if (val.paramScript == null && param.getStringValue() != null) {
				val.paramScript = param.getStringValue();
			}
			if (val.vizScript == null && viz.getStringValue() != null) {
				val.vizScript = viz.getStringValue();
			}
		}
		return val;
	}
	
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) {
		return inSpecs;
	}
	
	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
	
		FskPortObject inObj = (FskPortObject) inObjects[0];
		
		FskJSEditorNodeValue val = getViewValue();
		synchronized (getLock()) {
			// If not executed
			if (val.modelScript == null && val.paramScript == null && val.vizScript == null) {
				val.modelScript = inObj.model;
				val.paramScript = inObj.param;
				val.vizScript = inObj.viz;
			}
			
			model.setStringValue(val.modelScript);
			param.setStringValue(val.paramScript);
			viz.setStringValue(val.vizScript);
		}
		
		exec.setProgress(1);
		inObj.model = model.getStringValue();
		inObj.param = param.getStringValue();
		inObj.viz = viz.getStringValue();
		
		return new PortObject[] { inObj };
	}
	
	@Override
	protected void performReset() {
	}
	
	@Override
	protected void useCurrentValueAsDefault() {
		// Unused
	}
	
	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		model.validateSettings(settings);
		param.validateSettings(settings);
		viz.validateSettings(settings);
	}
	
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		model.saveSettingsTo(settings);
		param.saveSettingsTo(settings);
		viz.saveSettingsTo(settings);
	}
	
	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		model.loadSettingsFrom(settings);
		param.loadSettingsFrom(settings);
		viz.loadSettingsFrom(settings);
	}
	
	// TODO: continue ...
}