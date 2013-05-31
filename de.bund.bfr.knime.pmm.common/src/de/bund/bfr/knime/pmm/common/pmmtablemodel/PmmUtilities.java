package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import java.util.ArrayList;
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

	public static List<String> getAllMiscParams(List<KnimeTuple> tuples) {
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

	public static Map<String, String> getAllMiscCategories(
			List<KnimeTuple> tuples) {
		Map<String, String> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.getName(), element.getCategory());
			}
		}

		return map;
	}

	public static Map<String, String> getAllMiscUnits(List<KnimeTuple> tuples) {
		Map<String, String> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.getName(), element.getUnit());
			}
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
