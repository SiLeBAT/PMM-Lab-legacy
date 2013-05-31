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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class PmmUtilities {
	
	public static List<String> getAllMiscParams(DataTable table) {
		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), table);
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

	public static Map<String, String> getAllMiscCategories(DataTable table) {
		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), table);
		Map<String, String> map = new LinkedHashMap<>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.getName(), element.getCategory());
			}
		}

		return map;
	}

	public static Map<String, String> getAllMiscUnits(DataTable table) {
		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), table);
		Map<String, String> map = new LinkedHashMap<>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.getName(), element.getUnit());
			}
		}

		return map;
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
