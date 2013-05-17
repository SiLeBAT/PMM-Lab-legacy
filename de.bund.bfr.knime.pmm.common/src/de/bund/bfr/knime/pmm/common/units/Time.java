package de.bund.bfr.knime.pmm.common.units;

import java.util.LinkedHashMap;
import java.util.Map;

public class Time implements Category {

	public static final String SECOND = "s";
	public static final String MINUTE = "min";
	public static final String HOUR = "h";
	public static final String DAY = "day";
	public static final String WEEK = "week";

	public Time() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { SECOND, MINUTE, HOUR, DAY, WEEK };
	}
	
	@Override
	public String getStandardUnit() {		
		return HOUR;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		Map<String, Double> factors = new LinkedHashMap<>();
		
		factors.put(SECOND, 1.0);
		factors.put(MINUTE, 60.0);
		factors.put(HOUR, 60 * 60.0);
		factors.put(DAY, 24.0 * 60.0 * 60.0);
		factors.put(WEEK, 7.0 * 24.0 * 60.0 * 60.0);

		if (value == null || factors.get(fromUnit) == null
				|| factors.get(toUnit) == null) {
			return null;
		}

		return value * factors.get(fromUnit) / factors.get(toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return true;
	}

}
