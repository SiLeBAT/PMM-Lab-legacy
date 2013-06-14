package de.bund.bfr.knime.pmm.common.units;

public class MassRatio implements Category {

	public static final String PERCENT_WTWT = "% wt/wt";

	public MassRatio() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { PERCENT_WTWT };
	}

	@Override
	public String getStandardUnit() {
		return PERCENT_WTWT;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return value;
	}
}
