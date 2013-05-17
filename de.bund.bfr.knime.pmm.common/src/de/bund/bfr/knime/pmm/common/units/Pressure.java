package de.bund.bfr.knime.pmm.common.units;

import java.util.LinkedHashMap;
import java.util.Map;

public class Pressure implements Category {

	public static final String PASCAL = "Pa";
	public static final String KILO_PASCAL = "kPa";
	public static final String MEGA_PASCAL = "MPa";
	public static final String BAR = "bar";
	public static final String MILLI_BAR = "mbar";
	public static final String KILO_BAR = "kbar";

	public Pressure() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { PASCAL, KILO_PASCAL, MEGA_PASCAL, BAR, MILLI_BAR,
				KILO_BAR };
	}
	
	@Override
	public String getStandardUnit() {		
		return PASCAL;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		Map<String, Double> factors = new LinkedHashMap<>();
		
		factors.put(PASCAL, 1.0);
		factors.put(KILO_PASCAL, 1e3);
		factors.put(MEGA_PASCAL, 1e6);
		factors.put(BAR, 1e5);
		factors.put(MILLI_BAR, 1e2);
		factors.put(KILO_BAR, 1e8);

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
