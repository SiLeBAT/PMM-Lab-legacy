/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
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