package de.bund.bfr.knime.pmm.xml2table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.util.ColumnFilter;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;

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
public class XML2TableNodeDialog extends DefaultNodeSettingsPane implements ChangeListener {

    /**
     * New pane for configuring XML2Table node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
	private DialogComponentStringListSelection xmlseldialog;
	
    protected XML2TableNodeDialog() {
        super();
        
        xmlseldialog = new DialogComponentStringListSelection(XML2TableNodeModel.m_xmlsel,"Cols from XML 2 add to table", null, false, 4);
        
        XML2TableNodeModel.m_col.addChangeListener(this);
        addDialogComponent(new DialogComponentColumnNameSelection(
        		XML2TableNodeModel.m_col, // TimeSeriesSchema.ATT_MISC
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
        stateChanged(null);

        addDialogComponent(new DialogComponentBoolean(XML2TableNodeModel.m_append, "Append data to table"));
        
        addDialogComponent(xmlseldialog);
    }

	@Override
	public void stateChanged(ChangeEvent arg0) {
        List<String> list = null;
		if (XML2TableNodeModel.m_col.getStringValue().equals("Misc")) {
			list = MiscXml.getElements();
		}
		else if (XML2TableNodeModel.m_col.getStringValue().startsWith("Parameter")) {
			list = ParamXml.getElements();
		}
        xmlseldialog.replaceListItems(list, (String[]) null);
	}
}

