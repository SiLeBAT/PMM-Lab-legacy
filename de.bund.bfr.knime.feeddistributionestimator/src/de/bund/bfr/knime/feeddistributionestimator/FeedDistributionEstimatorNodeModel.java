package de.bund.bfr.knime.feeddistributionestimator;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
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

/**
 * This is the model implementation of FeedDistributionEstimator.
 * 
 * 
 * @author Christian Thoens
 */
public class FeedDistributionEstimatorNodeModel extends NodeModel {

	protected static final String CFG_ID = "ID";
	protected static final String CFG_PRODUCTION = "Production";
	protected static final String CFG_CONSUMPTION = "Consumption";
	protected static final String CFG_X = "X";
	protected static final String CFG_Y = "Y";

	private static final double LAMBDA = 1e-1;
	private static final double START_VALUE = 0.0;
	private static final int ITERATIONS = 10000;
	private static final double LEARNING_RATE = 1e-2;

	private String id;
	private String production;
	private String consumption;
	private String x;
	private String y;

	/**
	 * Constructor for the node model.
	 */
	protected FeedDistributionEstimatorNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		BufferedDataTable table = inData[0];
		DataTableSpec spec = table.getDataTableSpec();
		int idIndex = spec.findColumnIndex(id);
		int productionIndex = spec.findColumnIndex(production);
		int consumptionIndex = spec.findColumnIndex(consumption);
		int xIndex = spec.findColumnIndex(x);
		int yIndex = spec.findColumnIndex(y);
		List<Consumer> consumers = new ArrayList<Consumer>();
		List<Producer> producers = new ArrayList<Producer>();
		List<List<Double>> probability = new ArrayList<List<Double>>();
		RowIterator it = table.iterator();

		while (it.hasNext()) {
			DataRow row = it.next();
			String id = ((StringCell) row.getCell(idIndex)).getStringValue();
			double consumption = ((DoubleCell) row.getCell(productionIndex))
					.getDoubleValue();
			double production = ((DoubleCell) row.getCell(consumptionIndex))
					.getDoubleValue();
			double x = ((DoubleCell) row.getCell(xIndex)).getDoubleValue();
			double y = ((DoubleCell) row.getCell(yIndex)).getDoubleValue();
			Point2D.Double position = new Point2D.Double(x, y);

			if (production > consumption) {
				producers.add(new Producer(id, position, production
						- consumption));
			} else if (consumption > production) {
				consumers.add(new Consumer(id, position, consumption
						- production));
			}
		}

		for (Consumer c : consumers) {
			List<Double> prob = new ArrayList<Double>();
			double probSum = 0.0;

			for (Producer p : producers) {
				probSum += Math.pow(c.getPosition().distance(p.getPosition()),
						-3.0);
			}

			for (Producer p : producers) {
				prob.add(Math.pow(c.getPosition().distance(p.getPosition()),
						-3.0) / probSum);
			}

			probability.add(prob);
		}

		double[] point = new double[consumers.size() * producers.size()];

		for (int i = 0; i < consumers.size(); i++) {
			for (int j = 0; j < producers.size(); j++) {
				point[i * producers.size() + j] = START_VALUE;
			}
		}

		long startTime = System.currentTimeMillis();
		CostFunction function = new CostFunction(consumers, producers,
				probability);

		for (int n = 0; n < ITERATIONS; n++) {
			double[] deriv = function.derivative(point);

			for (int index = 0; index < consumers.size() * producers.size(); index++) {
				point[index] = point[index] - LEARNING_RATE * deriv[index];
				point[index] = Math.max(point[index], 0.0);
			}

			if (n % 100 == 0) {
				System.out.println(n + "\tTime:"
						+ (System.currentTimeMillis() - startTime) + "\tError:"
						+ function.value(point));
			}
		}

		DataTableSpec outSpec = configure(null)[0];
		BufferedDataContainer container = exec.createDataContainer(outSpec);

		for (int i = 0; i < consumers.size(); i++) {
			for (int j = 0; j < producers.size(); j++) {
				DataCell[] cells = new DataCell[3];

				cells[outSpec.findColumnIndex("To")] = new StringCell(consumers
						.get(i).getId());
				cells[outSpec.findColumnIndex("From")] = new StringCell(
						producers.get(j).getId());
				cells[outSpec.findColumnIndex("Amount")] = new DoubleCell(
						point[i * producers.size() + j]);

				container.addRowToTable(new DefaultRow(
						(i * producers.size() + j) + "", cells));
			}
		}

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
		DataColumnSpec fromSpec = new DataColumnSpecCreator("From",
				StringCell.TYPE).createSpec();
		DataColumnSpec toSpec = new DataColumnSpecCreator("To", StringCell.TYPE)
				.createSpec();
		DataColumnSpec amountSpec = new DataColumnSpecCreator("Amount",
				DoubleCell.TYPE).createSpec();

		return new DataTableSpec[] { new DataTableSpec(fromSpec, toSpec,
				amountSpec) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_ID, id);
		settings.addString(CFG_PRODUCTION, production);
		settings.addString(CFG_CONSUMPTION, consumption);
		settings.addString(CFG_X, x);
		settings.addString(CFG_Y, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			id = settings.getString(CFG_ID);
		} catch (InvalidSettingsException e) {
			id = "";
		}

		try {
			production = settings.getString(CFG_PRODUCTION);
		} catch (InvalidSettingsException e) {
			production = "";
		}

		try {
			consumption = settings.getString(CFG_CONSUMPTION);
		} catch (InvalidSettingsException e) {
			consumption = "";
		}

		try {
			x = settings.getString(CFG_X);
		} catch (InvalidSettingsException e) {
			x = "";
		}

		try {
			y = settings.getString(CFG_Y);
		} catch (InvalidSettingsException e) {
			y = "";
		}
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

	private class Player {

		private String id;
		private Point2D.Double position;
		private double amount;

		public Player(String id, Point2D.Double position, double amount) {
			this.id = id;
			this.position = position;
			this.amount = amount;
		}

		public String getId() {
			return id;
		}

		public Point2D.Double getPosition() {
			return position;
		}

		public double getAmount() {
			return amount;
		}
	}

	private class Producer extends Player {

		public Producer(String id, Point2D.Double position, double amount) {
			super(id, position, amount);
		}
	}

	private class Consumer extends Player {

		public Consumer(String id, Point2D.Double position, double amount) {
			super(id, position, amount);
		}
	}

	private class CostFunction {

		private double[] prod;
		private double[] cons;
		private double[][] prob;

		public CostFunction(List<Consumer> consumers, List<Producer> producers,
				List<List<Double>> probability) {
			cons = new double[consumers.size()];
			prod = new double[producers.size()];
			prob = new double[consumers.size()][producers.size()];

			for (int i = 0; i < consumers.size(); i++) {
				cons[i] = consumers.get(i).getAmount();
			}

			for (int j = 0; j < producers.size(); j++) {
				prod[j] = producers.get(j).getAmount();
			}

			for (int i = 0; i < consumers.size(); i++) {
				for (int j = 0; j < producers.size(); j++) {
					prob[i][j] = probability.get(i).get(j);
				}
			}
		}

		public double value(double[] point) {
			double cost = 0.0;

			for (int i = 0; i < cons.length; i++) {
				for (int j = 0; j < prod.length; j++) {
					cost += 1.0 / prob[i][j] * point[i * prod.length + j];
				}
			}

			double error = 0.0;

			for (int i = 0; i < cons.length; i++) {
				double e = cons[i];

				for (int j = 0; j < prod.length; j++) {
					e -= point[i * prod.length + j];
				}

				error += e * e;
			}

			for (int j = 0; j < prod.length; j++) {
				double e = prod[j];

				for (int i = 0; i < cons.length; i++) {
					e -= point[i * prod.length + j];
				}

				error += e * e;
			}

			return cost + LAMBDA * error;
		}

		public double[] derivative(double[] point) {
			double[] deriv = new double[cons.length * prod.length];

			for (int i0 = 0; i0 < cons.length; i0++) {
				for (int j0 = 0; j0 < prod.length; j0++) {
					double cost = 1.0 / prob[i0][j0];
					double error = -cons[i0] - prod[j0];

					for (int j = 0; j < prod.length; j++) {
						error += point[i0 * prod.length + j];
					}

					for (int i = 0; i < cons.length; i++) {
						error += point[i * prod.length + j0];
					}

					error *= 2.0;

					deriv[i0 * prod.length + j0] = cost + LAMBDA * error;
				}
			}

			return deriv;
		}
	}

}
