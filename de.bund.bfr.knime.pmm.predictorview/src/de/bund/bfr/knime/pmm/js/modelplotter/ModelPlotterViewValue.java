package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
* JavaScript view value, is empty since the view does not produce any output ports. 
* 
* @author Kilian Thiel, KNIME.com GmbH, Berlin, Germany
*/
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ModelPlotterViewValue extends JSONViewContent {
	
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		//settings.addString(ModelPlotterViewConfig.CHART_TITLE, getChartTitle());
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		//setChartTitle(settings.getString(ModelPlotterViewConfig.CHART_TITLE));
	}
}
