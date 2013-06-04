package de.bund.bfr.knime.pmm.dataviewandselect;

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
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.BacterialConcentration;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Time;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;

	private List<String> ids;

	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<List<TimeSeriesXml>> data;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table) {
		allIds = new ArrayList<>();
		allTuples = PmmUtilities.getTuples(table,
				SchemaFactory.createDataSchema());
		ids = new ArrayList<>();
		plotables = new LinkedHashMap<>();
		stringColumns = Arrays.asList(AttributeUtilities.DATAID,
				TimeSeriesSchema.ATT_AGENT, TimeSeriesSchema.ATT_MATRIX,
				MdInfoXml.ATT_COMMENT);
		stringColumnValues = new ArrayList<>();
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		conditions = new ArrayList<>();
		conditionValues = new ArrayList<>();
		conditionUnits = new ArrayList<>();
		data = new ArrayList<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		standardVisibleColumns = Arrays.asList(AttributeUtilities.DATAID,
				TimeSeriesSchema.ATT_AGENT, TimeSeriesSchema.ATT_MATRIX,
				MdInfoXml.ATT_COMMENT);

		Set<String> idSet = new LinkedHashSet<String>();
		List<String> miscParams = PmmUtilities.getMiscParams(allTuples);

		for (String param : miscParams) {
			conditions.add(param);
			conditionValues.add(new ArrayList<Double>());
			conditionUnits.add(new ArrayList<String>());
		}

		for (KnimeTuple tuple : allTuples) {
			String id = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			allIds.add(id);

			if (idSet.contains(id)) {
				continue;
			}

			idSet.add(id);
			ids.add(id);

			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			List<Double> timeList = new ArrayList<Double>();
			List<Double> logcList = new ArrayList<Double>();
			List<TimeSeriesXml> dataPoints = new ArrayList<>();
			String timeUnit = new Time().getStandardUnit();
			String concentrationUnit = new BacterialConcentration()
					.getStandardUnit();
			String dataName;
			String agent;
			String matrix;

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;

				timeList.add(element.getTime());
				logcList.add(element.getConcentration());
				dataPoints.add(element);
				timeUnit = element.getTimeUnit();
				concentrationUnit = element.getConcentrationUnit();
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
			stringColumnValues.get(1).add(agent);
			stringColumnValues.get(2).add(matrix);
			stringColumnValues.get(3).add(
					((MdInfoXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO)
							.get(0)).getComment());
			data.add(dataPoints);
			shortLegend.put(id, dataName);
			longLegend.put(id, dataName + " " + agent);

			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (int i = 0; i < miscParams.size(); i++) {
				boolean paramFound = false;

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (miscParams.get(i).equals(element.getName())) {
						conditionValues.get(i).add(element.getValue());
						conditionUnits.get(i).add(element.getUnit());
						paramFound = true;
						break;
					}
				}

				if (!paramFound) {
					conditionValues.get(i).add(null);
					conditionUnits.get(i).add(null);
				}
			}

			Plotable plotable = new Plotable(Plotable.DATASET);
			Map<String, String> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			categories.put(AttributeUtilities.TIME, Categories.TIME);
			categories.put(AttributeUtilities.LOGC,
					Categories.BACTERIAL_CONCENTRATION);
			units.put(AttributeUtilities.TIME, timeUnit);
			units.put(AttributeUtilities.LOGC, concentrationUnit);

			plotable.setCategories(categories);
			plotable.setUnits(units);

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

	public List<List<TimeSeriesXml>> getData() {
		return data;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionValues() {
		return conditionValues;
	}

	public List<List<String>> getConditionUnits() {
		return conditionUnits;
	}

	public List<String> getStandardVisibleColumns() {
		return standardVisibleColumns;
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
