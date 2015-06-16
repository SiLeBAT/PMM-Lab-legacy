package de.bund.bfr.knime.pmm.sbmlutil;

import org.json.simple.JSONObject;
import org.knime.core.data.DataTableSpec;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public enum ModelType {
	PRIMARY, SECONDARY, TERTIARY;

	/**
	 * Gets the type of an SBMLDocument
	 * 
	 * @param doc
	 *            SBMLDocument
	 */
	public static ModelType getDocumentType(SBMLDocument doc) {
		CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		// If doc has no model definitions of secondary models => primary model
		if (plugin.getNumModelDefinitions() == 0) {
			return PRIMARY;
		}
		// Doc with no model but secondary models => secondary model
		else if (doc.getModel() == null) {
			return SECONDARY;
		}
		// Doc with model and secondary models => tertiary model
		else {
			return TERTIARY;
		}
	}

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
		} else {
			throw new Exception();
		}
	}

	/**
	 * Gets the type of a JSON object.
	 * 
	 * @param jo JSON object
	 * @throws Exception
	 *             When input JSON object does not match a primary, secondary or
	 *             tertiary model table
	 */
//	public static ModelType getJSONType(JSONObject jo) throws Exception {
//		if (jo.containsKey("TimeSeriesSchema")
//				&& jo.containsKey("Model1Schema")
//				&& jo.containsKey("Model2Schema")) {
//			return TERTIARY;
//		} else if (jo.containsKey("TimeSeriesSchema")
//				&& jo.containsKey("Model1Schema")) {
//			return PRIMARY;
//		} else if (jo.containsKey("Model2Schema")) {
//			return SECONDARY;
//		} else {
//			throw new Exception();
//		}
//	}
};