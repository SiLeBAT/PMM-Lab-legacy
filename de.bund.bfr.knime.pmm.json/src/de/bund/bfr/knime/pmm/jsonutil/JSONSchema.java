/**
 * JSON table schema with one single JSON string.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class JSONSchema extends KnimeSchema {
	
	public static final String ATT_MODEL = "Model";

	public JSONSchema() {
		try {
			addStringAttribute(ATT_MODEL);
		} catch (PmmException ex) {
			ex.printStackTrace();
		}
	}
}
