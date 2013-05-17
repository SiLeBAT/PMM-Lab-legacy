package de.bund.bfr.knime.pmm.common.units;

public class WaterActivity implements Category {

	public static final String WATER_ACTIVITY = "Water Activity";

	public WaterActivity() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { WATER_ACTIVITY };
	}

	@Override
	public String getStandardUnit() {
		return WATER_ACTIVITY;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return value;
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return true;
	}

}
