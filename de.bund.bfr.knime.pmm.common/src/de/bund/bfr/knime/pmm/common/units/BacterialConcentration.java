package de.bund.bfr.knime.pmm.common.units;

import java.util.LinkedHashSet;
import java.util.Set;

public class BacterialConcentration implements Category {

	public static final String CFU_PER_GRAMM = "CFU/g";
	public static final String LOG_CFU_PER_GRAMM = "log CFU/g";
	public static final String LN_CFU_PER_GRAMM = "ln CFU/g";
	public static final String CFU_PER_MILLILITER = "CFU/ml";
	public static final String LOG_CFU_PER_MILLILITER = "log CFU/ml";
	public static final String LN_CFU_PER_MILLILITER = "ln CFU/ml";

	public BacterialConcentration() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { CFU_PER_GRAMM, LOG_CFU_PER_GRAMM,
				LN_CFU_PER_GRAMM, CFU_PER_MILLILITER, LOG_CFU_PER_MILLILITER,
				LN_CFU_PER_MILLILITER };
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		if (value == null || !canConvert(fromUnit, toUnit)) {
			return null;
		}

		return fromCFU(toCFU(value, fromUnit), toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		Set<String> weightUnits = new LinkedHashSet<>();
		Set<String> volumeUnits = new LinkedHashSet<>();
		
		weightUnits.add(CFU_PER_GRAMM);
		weightUnits.add(LOG_CFU_PER_GRAMM);
		weightUnits.add(LN_CFU_PER_GRAMM);		
		volumeUnits.add(CFU_PER_MILLILITER);
		volumeUnits.add(LOG_CFU_PER_MILLILITER);
		volumeUnits.add(LN_CFU_PER_MILLILITER);

		return (weightUnits.contains(fromUnit) && weightUnits.contains(toUnit))
				|| (volumeUnits.contains(fromUnit) && volumeUnits
						.contains(toUnit));
	}

	private Double toCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CFU_PER_GRAMM:
		case CFU_PER_MILLILITER:
			return value;
		case LOG_CFU_PER_GRAMM:
		case LOG_CFU_PER_MILLILITER:
			return Math.pow(10.0, value);
		case LN_CFU_PER_GRAMM:
		case LN_CFU_PER_MILLILITER:
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
		case CFU_PER_GRAMM:
		case CFU_PER_MILLILITER:
			return value;
		case LOG_CFU_PER_GRAMM:
		case LOG_CFU_PER_MILLILITER:
			return Math.log10(value);
		case LN_CFU_PER_GRAMM:
		case LN_CFU_PER_MILLILITER:
			return Math.log(value);
		default:
			return null;
		}
	}

}
