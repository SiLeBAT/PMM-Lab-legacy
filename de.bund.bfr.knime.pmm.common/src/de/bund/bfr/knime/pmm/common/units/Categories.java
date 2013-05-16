package de.bund.bfr.knime.pmm.common.units;

public class Categories {

	public static final String CFU = "CFU";
	public static final String PH = "pH";
	public static final String PRESSURE = "Pressure";
	public static final String SOLUTION_CONCENTRATION = "Solution Concentration";
	public static final String TEMPERATURE = "Temperature";
	public static final String TIME = "Time";
	public static final String TRUE_FALSE_VALUE = "True/False Value";
	public static final String WATER_ACTIVITY = "Water Activity";

	public static String[] getAllCategories() {
		return new String[] { CFU, PH, PRESSURE, SOLUTION_CONCENTRATION,
				TEMPERATURE, TIME, TRUE_FALSE_VALUE, WATER_ACTIVITY };
	}

	public static Category getCategory(String id) {
		switch (id) {
		case CFU:
			return new CFU();
		case PH:
			return new PH();
		case PRESSURE:
			return new Pressure();
		case SOLUTION_CONCENTRATION:
			return new SolutionConcentration();
		case TEMPERATURE:
			return new Temperature();
		case TIME:
			return new Time();
		case TRUE_FALSE_VALUE:
			return new TrueFalseValue();
		case WATER_ACTIVITY:
			return new WaterActivity();
		default:
			return null;
		}
	}
}
