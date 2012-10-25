package de.bund.bfr.knime.pmm.xml2table;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;

/**
 * <code>NodeDialog</code> for the "XML2Table" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author BfR
 */
public class XML2TableNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring XML2Table node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected XML2TableNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        	    new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, ""), // TimeSeriesSchema.ATT_MISC
        	    "Select a column",
        	    0,
        	    true,
        	    new ColumnFilter() {
        	        public boolean includeColumn(final DataColumnSpec colSpec) {
        	            return colSpec.getType().equals(XMLCell.TYPE);
        	        }
        	        public String allFilteredMsg() {
        	            return "No xml columns in input table";
        	        }
        	    }));

        addDialogComponent(new DialogComponentBoolean(new SettingsModelBoolean(
        		XML2TableNodeModel.CFGKEY_APPENDDATA, true), "Append data to table"));
    }
}

