package de.bund.bfr.knime.foodprocess.addons;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

/**
 * <code>NodeDialog</code> for the "Ingredients" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author BfR
 */
public class AddonNodeDialog extends NodeDialogPane {

	private AddonC mIc;
	private boolean forMatrices;

    /**
     * New pane for configuring Ingredients node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected AddonNodeDialog(boolean forMatrices) {
        super();
        this.forMatrices = forMatrices;
        mIc = new AddonC(forMatrices);
        this.addTab("Ings", mIc);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		double[] volumeDef = mIc.getVolumeDef();
		if (volumeDef != null) {
	    	settings.addDoubleArray(AddonNodeModel.PARAM_VOLUME, volumeDef);
	    	settings.addStringArray(AddonNodeModel.PARAM_VOLUME_UNIT, mIc.getVolumeUnitDef());
	    	mIc.saveSettings(settings.addConfig(forMatrices ? AddonNodeModel.PARAM_MATRICES : AddonNodeModel.PARAM_AGENTS));
    	}
	}

    @Override
    protected void loadSettingsFrom(final NodeSettingsRO s,
    		final PortObjectSpec[] specs) throws NotConfigurableException {
    	try {
			double[] volumeDef = s.containsKey(AddonNodeModel.PARAM_VOLUME) ? s.getDoubleArray(AddonNodeModel.PARAM_VOLUME) : null;
	    	String[] volumeUnitDef = s.containsKey(AddonNodeModel.PARAM_VOLUME_UNIT) ? s.getStringArray(AddonNodeModel.PARAM_VOLUME_UNIT) : null;
	    	String key = forMatrices ? AddonNodeModel.PARAM_MATRICES : AddonNodeModel.PARAM_AGENTS;
	    	mIc.setSettings(s.containsKey(key) ? s.getConfig(key) : null, volumeDef, volumeUnitDef);
		}
    	catch (InvalidSettingsException e) {
			e.printStackTrace();
		}
    }
}