package de.bund.bfr.knime.pmm.xml2table;

import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.util.ColumnFilter;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
	private final DialogComponentStringListSelection xmlseldialog;
    private final SettingsModelString m_col = new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, "");
    private final SettingsModelBoolean m_append = new SettingsModelBoolean(XML2TableNodeModel.CFGKEY_APPENDDATA, true);
    private final SettingsModelStringArray m_xmlsel = new SettingsModelStringArray(XML2TableNodeModel.CFGKEY_SELXMLENTRY, null);
	
    protected XML2TableNodeDialog() {
        super();
        
        xmlseldialog = new DialogComponentStringListSelection(m_xmlsel,"Cols from XML 2 add to table", null, false, 4);
        
        m_col.addChangeListener(this);
        addDialogComponent(new DialogComponentColumnNameSelection(
        		m_col, // TimeSeriesSchema.ATT_MISC
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

        addDialogComponent(new DialogComponentBoolean(m_append, "Append data to table"));
        
        addDialogComponent(xmlseldialog);
    }

	@Override
	public void stateChanged(ChangeEvent arg0) {
        List<String> list = null;
		if (m_col.getStringValue().equals(TimeSeriesSchema.ATT_MISC)) {
			list = MiscXml.getElements();
		}
		else if (m_col.getStringValue().equals(TimeSeriesSchema.ATT_TIMESERIES)) {
			list = TimeSeriesXml.getElements();
		}
		else if (m_col.getStringValue().startsWith(Model1Schema.ATT_PARAMETER)) {
			list = ParamXml.getElements();
		}
		else if (m_col.getStringValue().startsWith(Model1Schema.ATT_INDEPENDENT)) {
			list = IndepXml.getElements();
		}
		else if (m_col.getStringValue().startsWith(Model1Schema.ATT_DEPENDENT)) {
			list = DepXml.getElements();
		}
		else if (m_col.getStringValue().startsWith(Model1Schema.ATT_MODELCATALOG)) {
			list = CatalogModelXml.getElements();
		}
		else if (m_col.getStringValue().startsWith(Model1Schema.ATT_ESTMODEL)) {
			list = EstModelXml.getElements();
		}
        if (list != null) xmlseldialog.replaceListItems(list, (String[]) null);
	}
}

