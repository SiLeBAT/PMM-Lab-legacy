package de.bund.bfr.knime.pmm.pmfreader;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class MetadataSchema extends KnimeSchema {
	
	public static final String ATT_GIVEN_NAME = "GivenName";
	public static final String ATT_FAMILY_NAME = "FamilyName";
	public static final String ATT_CONTACT = "Contact";
	public static final String ATT_CREATED_DATE = "CreatedDate";
	public static final String ATT_MODIFIED_DATE = "ModifiedDate";
	public static final String ATT_TYPE = "Type";
	
	public MetadataSchema() {
		addStringAttribute(ATT_GIVEN_NAME);
		addStringAttribute(ATT_FAMILY_NAME);
		addStringAttribute(ATT_CONTACT);
		addStringAttribute(ATT_CREATED_DATE);
		addStringAttribute(ATT_MODIFIED_DATE);
		addStringAttribute(ATT_TYPE);
	}
}