package de.bund.bfr.knime.pmm.common.units;

public class Categories {

	public static final String TIME = "Time";
	public static final String CFU = "CFU";
	public static final String TEMPERATURE = "Temperature";

	public static final String[] CATEGORIES = { TIME, CFU, TEMPERATURE };

	public static Category getCategory(String id) {
		switch (id) {
		case TIME:
			return new Time();
		case CFU:
			return new CFU();
		case TEMPERATURE:
			return new Temperature();
		default:
			return null;
		}
	}
}
