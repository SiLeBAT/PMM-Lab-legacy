package de.bund.bfr.knime.pmm.common.units;

import java.util.Arrays;
import java.util.List;

public class MassRatio implements Category {

	public static final String PERCENT_WTWT = "% wt/wt";

	public MassRatio() {
	}

	@Override
	public String getName() {
		return Categories.MASS_RATIO;
	}

	@Override
	public List<String> getAllUnits() {
		return Arrays.asList(PERCENT_WTWT);
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
