package de.bund.bfr.knime.pmm.common.units;

public class NumberConcentration implements Category {

	public static final String COUNT_PER_MILLILITER = "count/ml";
	public static final String LOG_COUNT_PER_MILLILITER = "log(count/ml)";
	public static final String LN_COUNT_PER_MILLILITER = "ln(count/ml)";

	public NumberConcentration() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { COUNT_PER_MILLILITER, LOG_COUNT_PER_MILLILITER,
				LN_COUNT_PER_MILLILITER };
	}

	@Override
	public String getStandardUnit() {
		return LOG_COUNT_PER_MILLILITER;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return fromCFU(toCFU(value, fromUnit), toUnit);
	}

	private Double toCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case COUNT_PER_MILLILITER:
			return value;
		case LOG_COUNT_PER_MILLILITER:
			return Math.pow(10.0, value);
		case LN_COUNT_PER_MILLILITER:
			return Math.exp(value);
		default:
			return null;
		}
	}

	private Double fromCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case COUNT_PER_MILLILITER:
			return value;
		case LOG_COUNT_PER_MILLILITER:
			return Math.log10(value);
		case LN_COUNT_PER_MILLILITER:
			return Math.log(value);
		default:
			return null;
		}
	}
}
