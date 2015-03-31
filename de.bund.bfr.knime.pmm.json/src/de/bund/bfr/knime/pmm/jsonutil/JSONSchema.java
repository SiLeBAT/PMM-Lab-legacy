package de.bund.bfr.knime.pmm.jsonutil;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class JSONSchema extends KnimeSchema {

	public JSONSchema() {
		try {
			addStringAttribute("Model");
		} catch (PmmException ex) {
			ex.printStackTrace();
		}
	}
}
