package de.bund.bfr.knime.pmm.jsonencoder;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "JSONEncoder" Node.
 * Turns a PMM Lab table into JSON
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author JSON Encoder
 */
public class JSONEncoderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring JSONEncoder node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected JSONEncoderNodeDialog() {
        super();
    }
}

