package de.bund.bfr.knime.pmm.dataviewandselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;

	private List<String> ids;

	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;

	private List<List<String>> infoParameters;
	private List<List<String>> infoParameterValues;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, KnimeSchema schema)
			throws PmmException {
		Set<String> idSet = new HashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);

		allIds = new ArrayList<String>();
		allTuples = new ArrayList<KnimeTuple>();
		ids = new ArrayList<String>();
		plotables = new HashMap<String, Plotable>();
		stringColumns = Arrays.asList("ID");
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		doubleColumns = Arrays.asList(TimeSeriesSchema.ATT_TEMPERATURE,
				TimeSeriesSchema.ATT_PH, TimeSeriesSchema.ATT_WATERACTIVITY);
		doubleColumnValues = new ArrayList<List<Double>>();
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<String>>();
		shortLegend = new HashMap<String, String>();
		longLegend = new HashMap<String, String>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			String id = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			allIds.add(id);
			allTuples.add(tuple);

			if (idSet.contains(id)) {
				continue;
			}

			idSet.add(id);
			ids.add(id);

			Double temperature = tuple
					.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);
			Double ph = tuple.getDouble(TimeSeriesSchema.ATT_PH);
			Double waterActivity = tuple
					.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY);
			List<Double> timeList = tuple
					.getDoubleList(TimeSeriesSchema.ATT_TIME);
			List<Double> logcList = tuple
					.getDoubleList(TimeSeriesSchema.ATT_LOGC);
			String dataName;
			String agent;
			String matrix;

			if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
				dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			} else {
				dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			}

			if (tuple.getString(TimeSeriesSchema.ATT_AGENTNAME) != null) {
				agent = tuple.getString(TimeSeriesSchema.ATT_AGENTNAME) + " ("
						+ tuple.getString(TimeSeriesSchema.ATT_AGENTDETAIL)
						+ ")";
			} else {
				agent = tuple.getString(TimeSeriesSchema.ATT_AGENTDETAIL);
			}

			if (tuple.getString(TimeSeriesSchema.ATT_MATRIXNAME) != null) {
				matrix = tuple.getString(TimeSeriesSchema.ATT_MATRIXNAME)
						+ " ("
						+ tuple.getString(TimeSeriesSchema.ATT_MATRIXDETAIL)
						+ ")";
			} else {
				matrix = tuple.getString(TimeSeriesSchema.ATT_MATRIXDETAIL);
			}

			stringColumnValues.get(0).add(dataName);
			doubleColumnValues.get(0).add(temperature);
			doubleColumnValues.get(1).add(ph);
			doubleColumnValues.get(2).add(waterActivity);
			infoParameters.add(Arrays.asList(TimeSeriesSchema.ATT_AGENTNAME,
					TimeSeriesSchema.ATT_MATRIXNAME, TimeSeriesSchema.ATT_MISC,
					TimeSeriesSchema.ATT_COMMENT));
			infoParameterValues.add(Arrays.asList(agent, matrix,
					tuple.getString(TimeSeriesSchema.ATT_MISC),
					tuple.getString(TimeSeriesSchema.ATT_COMMENT)));
			shortLegend.put(id, dataName);
			longLegend.put(id, dataName + " " + agent);

			Plotable plotable = new Plotable(Plotable.DATASET);

			if (!timeList.isEmpty() && !logcList.isEmpty()) {
				plotable.addValueList(TimeSeriesSchema.ATT_TIME, timeList);
				plotable.addValueList(TimeSeriesSchema.ATT_LOGC, logcList);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getAllIds() {
		return allIds;
	}

	public List<KnimeTuple> getAllTuples() {
		return allTuples;
	}

	public List<String> getIds() {
		return ids;
	}

	public List<String> getStringColumns() {
		return stringColumns;
	}

	public List<List<String>> getStringColumnValues() {
		return stringColumnValues;
	}

	public List<String> getDoubleColumns() {
		return doubleColumns;
	}

	public List<List<Double>> getDoubleColumnValues() {
		return doubleColumnValues;
	}

	public List<List<String>> getInfoParameters() {
		return infoParameters;
	}

	public List<List<String>> getInfoParameterValues() {
		return infoParameterValues;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}

}
