package de.bund.bfr.knime.pmm.common.units;

public interface Category {

	public String[] getAllUnits();
	
	public String getStandardUnit();

	public Double convert(Double value, String fromUnit, String toUnit);

	public boolean canConvert(String fromUnit, String toUnit);
}
