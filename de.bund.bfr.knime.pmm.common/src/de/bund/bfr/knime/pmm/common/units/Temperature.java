package de.bund.bfr.knime.pmm.common.units;

public class Temperature implements Category {

	public static final String CELSIUS = "°C";
	public static final String FAHRENHEIT = "°F";

	public static final String[] UNITS = { CELSIUS, FAHRENHEIT };

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return fromStandardUnit(toStandardUnit(value, fromUnit), toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return true;
	}

	private Double toStandardUnit(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CELSIUS:
			return value;
		case FAHRENHEIT:
			return (value - 32.0) * 5.0 / 9.0;
		default:
			return null;
		}
	}

	private Double fromStandardUnit(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CELSIUS:
			return value;
		case FAHRENHEIT:
			return value * 9.0 / 5.0 + 32.0;
		default:
			return null;
		}
	}

}
