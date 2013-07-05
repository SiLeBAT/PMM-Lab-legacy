package de.bund.bfr.knime.pmm.common.units;

public class Bridge2DB {

	private int id;
	private String unit;
	private String description;
	private String name;
	private String kind_of_property_quantity;
	private String notation_case_sensitive;
	private String convert_to;
	private String conversion_function_factor;
	private String inverse_conversion_function_factor;
	private String object_type;
	private String display_in_GUI_as;
	private String MathML_string;
	private String Priority_for_display_in_GUI;
	
	public Bridge2DB() {
		//DBKernel.getResultSet("", true);
	}

	public int getId() {
		return id;
	}

	public String getUnit() {
		return unit;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getKind_of_property_quantity() {
		return kind_of_property_quantity;
	}

	public String getNotation_case_sensitive() {
		return notation_case_sensitive;
	}

	public String getConvert_to() {
		return convert_to;
	}

	public String getConversion_function_factor() {
		return conversion_function_factor;
	}

	public String getInverse_conversion_function_factor() {
		return inverse_conversion_function_factor;
	}

	public String getObject_type() {
		return object_type;
	}

	public String getDisplay_in_GUI_as() {
		return display_in_GUI_as;
	}

	public String getMathML_string() {
		return MathML_string;
	}

	public String getPriority_for_display_in_GUI() {
		return Priority_for_display_in_GUI;
	}
}
