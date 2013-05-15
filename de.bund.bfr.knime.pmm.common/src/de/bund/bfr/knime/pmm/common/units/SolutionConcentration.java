package de.bund.bfr.knime.pmm.common.units;

public class SolutionConcentration implements Category {

	public static final String PERCENT_WTVOL = "% wt/vol";
	public static final String PERCENT_WTWT = "% wt/wt";

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		if (fromUnit == toUnit) {
			return value;			
		}
		
		return null;
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return false;
	}

}
