package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
* JavaScript view representation, contains all data of the plotable function, such as function 
* expression, argumants, and constants. 
*
* @author Kilian Thiel, KNIME.com GmbH, Berlin, Germany
*/
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public final class ModelPlotterViewRepresentation extends JSONViewContent {

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		// Nothing to do.
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// Nothing to do.
	}
}
