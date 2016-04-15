package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.UnitList;

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
	private ModelList m_models;
	private UnitList m_units;

	public ModelPlotterViewValue() {
		// Query database
		m_models = new ModelList();
		m_units = new UnitList();
	}

	public ModelList getModels() {
		return m_models;
	}

	public void setModels(ModelList models) {
		m_models = models;
	}
	
	public UnitList getUnits() {
		return m_units;
	}
	
	public void setUnits(UnitList units) {
		m_units = units;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	public boolean equals(Object obj) {
		ModelPlotterViewValue otherView = (ModelPlotterViewValue) obj;
		
		if((this.authors != null && !this.authors.equals(otherView.authors)) ||
			this.authors == null && otherView.authors != null)
			return false;
		if((this.comments != null && !this.comments.equals(otherView.comments)) ||
			this.comments == null && otherView.comments != null)
			return false;
		if((this.reportName != null && !this.reportName.equals(otherView.reportName)) ||
			this.reportName == null && otherView.reportName != null)
			return false;
			
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		if(this.authors != null)
			hash += this.authors.hashCode();
		if(this.comments != null)
			hash += this.comments.hashCode();
		if(this.reportName != null)
			hash += this.reportName.hashCode();
		
		return hash;
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
