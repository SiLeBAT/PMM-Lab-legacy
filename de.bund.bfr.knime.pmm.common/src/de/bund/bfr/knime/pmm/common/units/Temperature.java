package de.bund.bfr.knime.pmm.common.units;

public class Temperature implements Category {

	public static final String CELSIUS = "°C";
	public static final String FAHRENHEIT = "°F";

	public Temperature() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { CELSIUS, FAHRENHEIT };
	}
	
	@Override
	public String getStandardUnit() {		
		return CELSIUS;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return fromCelsius(toCelsius(value, fromUnit), toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return true;
	}

	private Double toCelsius(Double value, String unit) {
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

	private Double fromCelsius(Double value, String unit) {
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
