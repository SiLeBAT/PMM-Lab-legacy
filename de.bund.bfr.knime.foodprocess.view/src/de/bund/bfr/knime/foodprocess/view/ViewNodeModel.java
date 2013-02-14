package de.bund.bfr.knime.foodprocess.view;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

/**
 * This is the model implementation of View.
 * 
 * 
 * @author Christian Thoens
 */
public class ViewNodeModel extends NodeModel {

	protected static final String CFG_FILENAME = "FittedParameterView.zip";
	protected static final String CFG_USEDPARAMETERS = "UsedParameters";

	private DataTable table;
	private List<String> usedParameters;

	/**
	 * Constructor for the node model.
	 */
	protected ViewNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { ImagePortObject.TYPE });
		usedParameters = new ArrayList<>();
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		table = (DataTable) inObjects[0];

		return new PortObject[] { new ImagePortObject(
				ChartUtilities
						.convertToPNGImageContent(createChart(), 640, 480),
				new ImagePortObjectSpec(PNGImageContent.TYPE)) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return new PortObjectSpec[] { new ImagePortObjectSpec(
				PNGImageContent.TYPE) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_USEDPARAMETERS,
				XmlConverter.listToXml(usedParameters));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		usedParameters = XmlConverter.xmlToStringList(settings
				.getString(CFG_USEDPARAMETERS));
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
		File f = new File(internDir, CFG_FILENAME);

		table = DataContainer.readFromZip(f);
	}

	protected JFreeChart createChart() {
		List<XYDataset> dataSets = new ArrayList<>();
		DataTableSpec spec = table.getDataTableSpec();
		int timeIndex = spec.findColumnIndex(AttributeUtilities.TIME);
		int processIndex = spec.findColumnIndex("process");

		for (String param : usedParameters) {
			int paramIndex = spec.findColumnIndex(param);
			List<Double> timeList = new ArrayList<>();
			List<Double> paramList = new ArrayList<>();

			for (DataRow row : table) {
				DataCell timeCell = row.getCell(timeIndex);
				DataCell paramCell = row.getCell(paramIndex);

				if (!timeCell.isMissing() && !paramCell.isMissing()) {
					timeList.add(((DoubleCell) timeCell).getDoubleValue());
					paramList.add(((DoubleCell) paramCell).getDoubleValue());
				}
			}

			int n = timeList.size();
			double[][] data = new double[2][n];

			for (int i = 0; i < n; i++) {
				data[0][i] = timeList.get(i);
				data[1][i] = paramList.get(i);
			}

			DefaultXYDataset dataSet = new DefaultXYDataset();

			dataSet.addSeries(param, data);
			dataSets.add(dataSet);
		}

		List<Point2D.Double> ranges = new ArrayList<>();
		String process = null;
		double processStart = Double.NaN;
		double time = Double.NaN;

		for (DataRow row : table) {
			DataCell timeCell = row.getCell(timeIndex);
			DataCell processCell = row.getCell(processIndex);

			if (!timeCell.isMissing() && !processCell.isMissing()) {
				String p = ((StringCell) processCell).getStringValue();

				time = ((DoubleCell) timeCell).getDoubleValue();

				if (!p.equals(process)) {
					if (process != null) {
						ranges.add(new Point2D.Double(processStart, time));
					}

					process = p;
					processStart = time;
				}
			}
		}

		ranges.add(new Point2D.Double(processStart, time));

		XYPlot plot = new XYPlot(null, new MyAxis(AttributeUtilities.TIME,
				ranges), null, null);

		for (int i = 0; i < dataSets.size(); i++) {
			plot.setDataset(i, dataSets.get(i));
			plot.setRenderer(i, new StandardXYItemRenderer());
			plot.setRangeAxis(i, new NumberAxis(usedParameters.get(i)));
			plot.mapDatasetToRangeAxis(i, i);
		}

		return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		File f = new File(internDir, CFG_FILENAME);

		DataContainer.writeToZip(table, f, exec);
	}

}
