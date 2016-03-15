package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.js.common.ModelList;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ModelPlotterViewValue extends JSONViewContent {
	
	private int minXAxis;
	private int minYAxis;
	private int maxXAxis;
	private int maxYAxis;
	private String authors;
	private String comments;
	private String reportName;
	private double y0;

	// Configuration keys
	private static final String MODELS = "models";
	private ModelList m_models;

	public ModelPlotterViewValue() {
		// Query database
		m_models = new ModelList();
	}

	public ModelList getModels() {
		return m_models;
	}

	public void setModels(ModelList models) {
		m_models = models;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		m_models.saveToNodeSettings(settings.addNodeSettings(MODELS));
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_models.loadFromNodeSettings(settings.getNodeSettings(MODELS));
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinXAxis() {
		return minXAxis;
	}

	public void setMinXAxis(int minXAxis) {
		this.minXAxis = minXAxis;
	}

	public int getMinYAxis() {
		return minYAxis;
	}

	public void setMinYAxis(int minYAxis) {
		this.minYAxis = minYAxis;
	}

	public int getMaxXAxis() {
		return maxXAxis;
	}

	public void setMaxXAxis(int maxXAxis) {
		this.maxXAxis = maxXAxis;
	}

	public int getMaxYAxis() {
		return maxYAxis;
	}

	public void setMaxYAxis(int maxYAxis) {
		this.maxYAxis = maxYAxis;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
