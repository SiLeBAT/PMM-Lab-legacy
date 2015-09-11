package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.pmfreader.MetadataSchema;

public class ReaderUtils {

	/**
	 * Parses a list of constraints and returns a dictionary that maps variables
	 * and their limit values.
	 * 
	 * @param constraints
	 */
	public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
		Map<String, Limits> paramLimits = new HashMap<>();

		for (Constraint currConstraint : constraints) {
			LimitsConstraint lc = new LimitsConstraint(currConstraint);
			Limits lcLimits = lc.getLimits();
			paramLimits.put(lcLimits.getVar(), lcLimits);
		}

		return paramLimits;
	}

	/**
	 * Creates time series
	 */
	public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit, String concUnitObjectType,
			double[][] data) {

		PmmXmlDoc mdData = new PmmXmlDoc();

		Double concStdDev = null;
		Integer numberOfMeasurements = null;

		for (double[] point : data) {
			double time = point[0];
			double conc = point[1];
			String name = "t" + mdData.size();

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
			t.setConcentrationUnitObjectType(concUnitObjectType);
			mdData.add(t);
		}

		return mdData;
	}

	/**
	 * Parses misc items.
	 * 
	 * @param miscs
	 *            . Dictionary that maps miscs names and their values.
	 * @return
	 */
	public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		if (miscs != null) {
			// First misc item has id -1 and the rest of items have negative
			// ints
			int counter = -1;
			for (Entry<String, Double> entry : miscs.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				List<String> categories;
				String description, unit;

				switch (name) {
				case "Temperature":
					categories = Arrays.asList(Categories.getTempCategory().getName());
					description = name;
					unit = Categories.getTempCategory().getStandardUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;

				case "pH":
					categories = Arrays.asList(Categories.getPhCategory().getName());
					description = name;
					unit = Categories.getPhUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;
				}
			}

		}
		return cell;
	}
	
	public static KnimeTuple createMetadataTuple(Metadata metadata) {
		KnimeTuple metadataTuple = new KnimeTuple(new MetadataSchema());
		metadataTuple.setValue(MetadataSchema.ATT_GIVEN_NAME, metadata.getGivenName());
		metadataTuple.setValue(MetadataSchema.ATT_FAMILY_NAME, metadata.getFamilyName());
		metadataTuple.setValue(MetadataSchema.ATT_CONTACT, metadata.getContact());
		metadataTuple.setValue(MetadataSchema.ATT_CREATED_DATE, metadata.getCreatedDate());
		metadataTuple.setValue(MetadataSchema.ATT_MODIFIED_DATE, metadata.getModifiedDate());
		metadataTuple.setValue(MetadataSchema.ATT_TYPE, metadata.getType());
		return metadataTuple;
	}
}