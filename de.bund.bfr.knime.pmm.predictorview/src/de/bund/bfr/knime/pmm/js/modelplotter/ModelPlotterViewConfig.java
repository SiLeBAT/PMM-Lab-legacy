package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * Node configuration data, such as chart title and Y0.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
final class ModelPlotterViewConfig {

	static final String CHART_TITLE = "chartTitle";

	static final String Y0 = "Y0";
	
	static final String HIDE_IN_WIZARD = "hideInWizard";

	static final double DEF_Y0 = 4.169;
	static final double MIN_Y0 = 0;
	static final double MAX_Y0 = 10;

	private static final String DEF_CHART_TITLE = "PMM Model Plot";

	private String m_chartTitle;

	private boolean m_isHideInWizard;

	private double m_y0;

	/**
	 * @return
	 */
	public double getY0() {
		return m_y0;
	}

	/**
	 * @param y0
	 */
	public void setY0(double y0) {
		this.m_y0 = y0;
	}

	/**
	 * @return
	 */
	public boolean getHideInwizard() {
		return m_isHideInWizard;
	}

	/**
	 * @param hideInWizard
	 */
	public void setHideInWizard(boolean hideInWizard) {
		this.m_isHideInWizard = hideInWizard;
	}

	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return m_chartTitle;
	}

	/**
	 * @param chartTitle
	 *            the chartTitle to set
	 */
	public void setChartTitle(final String chartTitle) {
		m_chartTitle = chartTitle;
	}

	/**
	 * Loads parameters in NodeModel.
	 * 
	 * @param settings
	 *            To load from.
	 * @throws InvalidSettingsException
	 *             If incomplete or wrong.
	 */
	public void loadSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_chartTitle = settings.getString(CHART_TITLE);
		m_isHideInWizard = settings.getBoolean(HIDE_IN_WIZARD);
		m_y0 = settings.getDouble(Y0);
	}

	/**
	 * Loads parameters in Dialog.
	 * 
	 * @param settings
	 *            To load from.
	 */
	public void loadSettingsForDialog(final NodeSettingsRO settings) {
		m_chartTitle = settings.getString(CHART_TITLE, DEF_CHART_TITLE);
		m_isHideInWizard = settings.getBoolean(HIDE_IN_WIZARD, false);
		m_y0 = settings.getDouble(Y0, DEF_Y0);
	}

	/**
	 * Saves current parameters to settings object.
	 * 
	 * @param settings
	 *            To save to.
	 */
	public void saveSettings(final NodeSettingsWO settings) {
		settings.addString(CHART_TITLE, m_chartTitle);
		settings.addBoolean(HIDE_IN_WIZARD, m_isHideInWizard);
		settings.addDouble(Y0, m_y0);
	}
}
