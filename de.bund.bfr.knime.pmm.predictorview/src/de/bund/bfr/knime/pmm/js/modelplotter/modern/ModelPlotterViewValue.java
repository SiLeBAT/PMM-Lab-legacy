package de.bund.bfr.knime.pmm.js.modelplotter.modern;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.UnitList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM12DataSchemaList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM12SchemaList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM1DataSchemaList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM1SchemaList;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ModelPlotterViewValue extends JSONViewContent {
	
	private int minXAxis;
	private int minYAxis;
	private int maxXAxis;
	private int maxYAxis;
	private String mAuthors;
	private String mComments;
	private String mReportName;
	private String mSVGPlot;
	private double y0;
	
	static final String AUTHORS = "authors";
	static final String COMMENTS = "comments";
	static final String REPORT_NAME = "reportName";
	static final String SVG_PLOT = "svgPlot";

	// Configuration keys
	private ModelList m_models;
	private JsM1DataSchemaList m1_models;
	private JsM12DataSchemaList m12_models;
	private UnitList m_units;

	public ModelPlotterViewValue() {
		// Query database
		m_models = new ModelList();
		m1_models = new JsM1DataSchemaList();
		m12_models = new JsM12DataSchemaList();
		m_units = new UnitList();
	}

	public ModelList getModels() {
		return m_models;
	}

	public void setModels(ModelList models) {
		m_models = models;
	}
	
	public JsM1DataSchemaList getM1Models() {
		return m1_models;
	}
	
	public void setModels(JsM1DataSchemaList models) {
		m1_models = models;
	}
	
	public JsM12DataSchemaList getM12Models() {
		return m12_models;
	}
	
	public void setModels(JsM12DataSchemaList models) {
		m12_models = models;
	}
	
	public UnitList getUnits() {
		return m_units;
	}
	
	public void setUnits(UnitList units) {
		m_units = units;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addString(AUTHORS, getAuthors());
		settings.addString(COMMENTS, getComments());
		settings.addString(REPORT_NAME, getReportName());
		settings.addString(SVG_PLOT, getSVGPlot());
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		setAuthors(settings.getString(AUTHORS));
		setComments(settings.getString(COMMENTS));
		setReportName(settings.getString(REPORT_NAME));
		setSVGPlot(settings.getString(SVG_PLOT));
	}

	@Override
	public boolean equals(Object obj) {
		ModelPlotterViewValue otherView = (ModelPlotterViewValue) obj;
		
		if((this.mAuthors != null && !this.mAuthors.equals(otherView.mAuthors)) ||
			this.mAuthors == null && otherView.mAuthors != null)
			return false;
		if((this.mComments != null && !this.mComments.equals(otherView.mComments)) ||
			this.mComments == null && otherView.mComments != null)
			return false;
		if((this.mReportName != null && !this.mReportName.equals(otherView.mReportName)) ||
			this.mReportName == null && otherView.mReportName != null)
			return false;
			
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		if(this.mAuthors != null)
			hash += this.mAuthors.hashCode();
		if(this.mComments != null)
			hash += this.mComments.hashCode();
		if(this.mReportName != null)
			hash += this.mReportName.hashCode();
		
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
		return mAuthors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		this.mAuthors = authors;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return mComments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.mComments = comments;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return mReportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.mReportName = reportName;
	}

	public String getSVGPlot() {
		return mSVGPlot;
	}

	public void setSVGPlot(String mSVGPlot) {
		this.mSVGPlot = mSVGPlot;
	}
}
