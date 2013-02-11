package de.bund.bfr.knime.pmm.dataviewandselect;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
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
	private List<List<?>> infoParameterValues;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, KnimeSchema schema)
			throws PmmException {
		Set<String> idSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<String> miscParams = getAllMiscParams(table);

		allIds = new ArrayList<String>();
		allTuples = new ArrayList<KnimeTuple>();
		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		stringColumns = Arrays.asList(AttributeUtilities.DATAID);
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		doubleColumns = new ArrayList<String>();
		doubleColumnValues = new ArrayList<List<Double>>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();

		for (String param : miscParams) {
			doubleColumns.add(param);
			doubleColumnValues.add(new ArrayList<Double>());
		}

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

			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			List<Double> timeList = new ArrayList<Double>();
			List<Double> logcList = new ArrayList<Double>();
			List<Point2D.Double> dataPoints = new ArrayList<Point2D.Double>();
			String dataName;
			String agent;
			String matrix;

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;
				double time = Double.NaN;
				double logc = Double.NaN;

				if (element.getTime() != null) {
					time = element.getTime();
				}

				if (element.getLog10C() != null) {
					logc = element.getLog10C();
				}

				timeList.add(element.getTime());
				logcList.add(element.getLog10C());
				dataPoints.add(new Point2D.Double(time, logc));
			}

			if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
				dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			} else {
				dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			}

			PmmXmlDoc agentXml = tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT);
			String agentName = ((AgentXml) agentXml.get(0)).getName();
			String agentDetail = ((AgentXml) agentXml.get(0)).getDetail();
			PmmXmlDoc matrixXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX);
			String matrixName = ((MatrixXml) matrixXml.get(0)).getName();
			String matrixDetail = ((MatrixXml) matrixXml.get(0)).getDetail();

			if (agentName != null) {
				agent = agentName + " (" + agentDetail + ")";
			} else {
				agent = agentDetail;
			}

			if (matrixName != null) {
				matrix = matrixName + " (" + matrixDetail + ")";
			} else {
				matrix = matrixDetail;
			}

			stringColumnValues.get(0).add(dataName);
			infoParameters.add(Arrays.asList(AttributeUtilities.DATAPOINTS,
					TimeSeriesSchema.ATT_AGENT, TimeSeriesSchema.ATT_MATRIX,
					TimeSeriesSchema.ATT_COMMENT));
			infoParameterValues.add(Arrays.asList(dataPoints, agent, matrix,
					tuple.getString(TimeSeriesSchema.ATT_COMMENT)));
			shortLegend.put(id, dataName);
			longLegend.put(id, dataName + " " + agent);

			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (int i = 0; i < miscParams.size(); i++) {
				boolean paramFound = false;

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (miscParams.get(i).equals(element.getName())) {
						doubleColumnValues.get(i).add(element.getValue());
						paramFound = true;
						break;
					}
				}

				if (!paramFound) {
					doubleColumnValues.get(i).add(null);
				}
			}

			Plotable plotable = new Plotable(Plotable.DATASET);

			if (!timeList.isEmpty() && !logcList.isEmpty()) {
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.LOGC, logcList);
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

	public List<List<?>> getInfoParameterValues() {
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

	private List<String> getAllMiscParams(BufferedDataTable table)
			throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new TimeSeriesSchema(), table);
		Set<String> paramSet = new LinkedHashSet<String>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
	}

}
