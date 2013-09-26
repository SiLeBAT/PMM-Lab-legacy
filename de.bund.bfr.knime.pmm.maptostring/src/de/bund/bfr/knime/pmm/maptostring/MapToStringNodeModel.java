package de.bund.bfr.knime.pmm.maptostring;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.XmlConverter;

/**
 * This is the model implementation of MapToString.
 * 
 * 
 * @author Christian Thoens
 */
public class MapToStringNodeModel extends NodeModel {

	private MapToStringSettings set;

	/**
	 * Constructor for the node model.
	 */
	protected MapToStringNodeModel() {
		super(0, 1);
		set = new MapToStringSettings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Map<String, Double> map = new LinkedHashMap<>();

		for (int i = 0; i < 5; i++) {
			String key = set.getKeys().get(i);
			Double value = set.getValues().get(i);

			if (key != null && value != null) {
				map.put(key, value);
			}
		}

		BufferedDataContainer container = exec
				.createDataContainer(createSpec(set.getVariableName()));
		StringCell cell = new StringCell(XmlConverter.objectToXml(map));

		container.addRowToTable(new DefaultRow("1", cell));
		container.close();

		return new BufferedDataTable[] { container.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (set.getVariableName() == null) {
			throw new InvalidSettingsException("Node has to be configured");
		}

		return new DataTableSpec[] { createSpec(set.getVariableName()) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.save(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.load(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	private static DataTableSpec createSpec(String variableName) {
		return new DataTableSpec(new DataColumnSpecCreator(variableName,
				StringCell.TYPE).createSpec());
	}

}
