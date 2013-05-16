package de.bund.bfr.knime.pmm.common.units;

public class TrueFalseValue implements Category {

	public static final String TRUE_FALSE = "True/False";

	public TrueFalseValue() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { TRUE_FALSE };
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
