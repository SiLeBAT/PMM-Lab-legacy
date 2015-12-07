package de.bund.bfr.knime.pmm.xml2table;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SettingsHelper {
	
	private static final String CFG_COLUMN = "Column";
	private static final String CFG_XML_ELEMENTS = "XmlElements";
	
	private String column;
	private String[] xmlElements;
	
	public SettingsHelper() {
		column = null;
		xmlElements = new String[0];
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			column = settings.getString(CFG_COLUMN);
		} catch (InvalidSettingsException e) {
		}
		
		try {
			xmlElements = settings.getStringArray(CFG_XML_ELEMENTS);
		} catch (InvalidSettingsException e) {
		}
	}
	
	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFG_COLUMN, column);
		settings.addStringArray(CFG_XML_ELEMENTS, xmlElements);
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String[] getXmlElements() {
		return xmlElements;
	}

	public void setXmlElements(String[] xmlElements) {
		this.xmlElements = xmlElements;
	}
}
