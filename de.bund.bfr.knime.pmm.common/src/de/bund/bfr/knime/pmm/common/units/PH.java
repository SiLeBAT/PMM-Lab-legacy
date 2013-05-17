package de.bund.bfr.knime.pmm.common.units;

public class PH implements Category {

	public static final String PH_SCALE = "pH Scale";
	
	public PH() {		
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { PH_SCALE };
	}
	
	@Override
	public String getStandardUnit() {		
		return PH_SCALE;
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
