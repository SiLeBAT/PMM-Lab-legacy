package de.bund.bfr.knime.pmm.js.modelplotter;

import java.util.List;
import java.util.Map;

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
/**
 * @author Kilian
 *
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public final class ModelPlotterViewRepresentation extends JSONViewContent {

	private String chartTitle;
	
	private double y0;
	
	private String function;
	
	private Map<String, Double> constants;

	private List<Variable> variables;
	
	private String xUnit;
	
	private String yUnit;

	
	/**
	 * @return the unit of the x axis.
	 */
	public String getxUnit() {
		return xUnit;
	}

	/**
	 * @param xUnit the unit of the x axis to set
	 */
	public void setxUnit(String xUnit) {
		this.xUnit = xUnit;
	}

	/**
	 * @return the unit of the y axis.
	 */
	public String getyUnit() {
		return yUnit;
	}

	/**
	 * @param yUnit the unit of the y axis to set.
	 */
	public void setyUnit(String yUnit) {
		this.yUnit = yUnit;
	}

	/**
	 * @return the value of Y0.
	 */
	public double getY0() {
		return y0;
	}

	/**
	 * @param y0 value to set for Y0.
	 */
	public void setY0(double y0) {
		this.y0 = y0;
	}

	/**
	 * @return the chart title.
	 */
	public String getChartTitle() {
		return chartTitle;
	}
	
	/**
	 * @param chartTitle the title to set.
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}
	
	/**
	 * @return the expression of the plotable function as string.
	 */
	public String getFunc() {
		return function;
	}

	/**
	 * @param f the function expression to set. 
	 */
	public void setFunc(String f) {
		this.function = f;
	}
	
	/**
	 * @return the constants of the function. 
	 */
	public Map<String, Double> getConstants() {
		return constants;
	}
    
	/**
	 * @param constants the constants of the function to set.
	 */
	public void setConstants(Map<String, Double> constants) {
		this.constants = constants;
	}
	
	/**
	 * @return the arguments of the function.
	 */
	public List<Variable> getVariables() {
		return variables;
	}
	
	/**
	 * @param variables the variables of the function.
	 */
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
	
	@JsonAutoDetect
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	public static class Variable {
		
		private String name;
		
		private double min;
		
		private double max;
		
		private double def;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getMin() {
			return min;
		}

		public void setMin(double min) {
			this.min = min;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}

		public double getDef() {
			return def;
		}

		public void setDef(double def) {
			this.def = def;
		}
	}

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
