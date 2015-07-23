package de.bund.bfr.knime.pmm.sbmlutil;

import org.knime.core.data.DataTableSpec;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;


public enum ModelType {
	EXPERIMENTAL_DATA, // Experimental data

	PRIMARY_MODEL_WDATA, // Primary models generated from data records
	PRIMARY_MODEL_WODATA, // Primary models generated without data records

	TWO_STEP_SECONDARY_MODEL, // Secondary models generated with the classical
								// 2-step approach from primary models
	ONE_STEP_SECONDARY_MODEL, // Secondary models generated implicitely during
								// 1-step fitting of tertiary models
	MANUAL_SECONDARY_MODEL, // Manually generated secondary model

	TWO_STEP_TERTIARY_MODEL, // Tertiary model generated with 2-step fit
								// approach
	ONE_STEP_TERTIARY_MODEL, // Tertiary model generated with 1-step fit
								// approach
	MANUAL_TERTIARY_MODEL,  // Tertiary models generated manually
	
	PRIMARY, SECONDARY, TERTIARY;  // Types used by JSON nodes

	/**
	 * Gets the type of a KNIME table spec.
	 * 
	 * @param spec
	 *            KNIME DataTableSpec.
	 * @throws Exception
	 *             When input table spec does not match a primary, secondary or
	 *             tertiary model table
	 */
	public static ModelType getTableType(DataTableSpec spec) throws Exception {

		if (SchemaFactory.conformsM12DataSchema(spec)) {
			return TERTIARY;
		} else if (SchemaFactory.conformsM1DataSchema(spec)) {
			return PRIMARY;
		} else if (SchemaFactory.conformsM2Schema(spec)) {
			return SECONDARY;
		} else if (SchemaFactory.conformsDataSchema(spec)) {
			return EXPERIMENTAL_DATA;
		} else {
			throw new Exception();
		}
	}
};