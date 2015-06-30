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
	 * Gets the type of an SBMLDocument
	 * 
	 * @param doc
	 *            SBMLDocument
	 */
//	public static ModelType getDocumentType(SBMLDocument doc) {
//		CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) doc
//				.getPlugin(CompConstants.shortLabel);
//
//		// If doc has no model definitions of secondary models => primary model
//		if (plugin.getNumModelDefinitions() == 0) {
//			return PRIMARY;
//		}
//		// Doc with no model but secondary models => secondary model
//		else if (doc.getModel() == null) {
//			return SECONDARY;
//		}
//		// Doc with model and secondary models => tertiary model
//		else {
//			return TERTIARY;
//		}
//	}

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

		if (SchemaFactory.createM12DataSchema().conforms(spec)) {
			return TERTIARY;
		} else if (SchemaFactory.createM1DataSchema().conforms(spec)) {
			return PRIMARY;
		} else if (SchemaFactory.createM2Schema().conforms(spec)) {
			return SECONDARY;
		} else if (SchemaFactory.createDataSchema().conforms(spec)) {
			return EXPERIMENTAL_DATA;
		} else {
			throw new Exception();
		}
	}

//	public static ModelType getPMFType(CombineArchive ca) throws Exception {
//		MetaDataObject mdo = ca.getDescriptions().get(0);
//		Element parentElement = mdo.getXmlDescription();
//		Element metadataElement = parentElement.getChild("modeltype");
//		String modelType = metadataElement.getText();
//
//		if (modelType.equals("Primary model")) {
//			return PRIMARY;
//		} else {
//			throw new Exception();
//		}
//	}
};