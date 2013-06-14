package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class PmmUtilities {

	public static List<KnimeTuple> getTuples(DataTable table, KnimeSchema schema) {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		return tuples;
	}

	public static List<String> getMiscParams(List<KnimeTuple> tuples) {
		Set<String> paramSet = new LinkedHashSet<String>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
	}

	public static Map<String, List<String>> getMiscCategories(
			List<KnimeTuple> tuples) {
		Map<String, List<String>> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.getName(), Arrays.asList(element.getCategory()));
			}
		}

		return map;
	}

	public static Map<String, String> getMiscUnits(List<KnimeTuple> tuples) {
		Map<String, Map<String, Integer>> occurences = new LinkedHashMap<>();
		Map<String, String> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				if (!occurences.containsKey(element.getName())) {
					occurences.put(element.getName(),
							new LinkedHashMap<String, Integer>());
				}

				Integer value = occurences.get(element.getName()).get(
						element.getUnit());

				if (value != null) {
					occurences.get(element.getName()).put(element.getUnit(),
							value + 1);
				} else {
					occurences.get(element.getName()).put(element.getUnit(), 1);
				}
			}
		}

		for (String name : occurences.keySet()) {
			String maxUnit = null;
			int maxN = 0;

			for (String unit : occurences.get(name).keySet()) {
				int n = occurences.get(name).get(unit);

				if (n > maxN) {
					maxUnit = unit;
					maxN = n;
				}
			}

			map.put(name, maxUnit);
		}

		return map;
	}

	public static boolean isOutOfRange(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			ParamXml element = (ParamXml) el;

			if (element.getValue() != null) {
				if (element.getMin() != null
						&& element.getValue() < element.getMin()) {
					return true;
				}

				if (element.getMax() != null
						&& element.getValue() > element.getMax()) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean covarianceMatrixMissing(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			if (((ParamXml) el).getError() == null) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNotSignificant(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			Double p = ((ParamXml) el).getP();

			if (p != null && p > 0.95) {
				return true;
			}
		}

		return false;
	}

}
