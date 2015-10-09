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
package de.bund.bfr.knime.pmm.openfsmr;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

/**
 * Schema for the OpenFSMR template.
 * @author Miguel Alba
 */
public class OpenFSMRSchema extends KnimeSchema {
	
	public static final String ATT_NAME = "Model_Name";
	public static final String ATT_ID = "Model_ID";
	public static final String ATT_LINK = "Model_Link";
	public static final String ATT_ORGANISM = "PMF_Organism";
	public static final String ATT_ORGANISM_DETAIL = "PMF_Organism_Detail";
	public static final String ATT_ENVIRONMENT = "PMF_Environment";
	public static final String ATT_ENVIRONMENT_DETAIL = "PMF_Environment_Detail";
	public static final String ATT_CREATOR = "Model_Creator";
	public static final String ATT_REF_DESC = "Model_Reference_Description";
	public static final String ATT_REF_DESC_LINK = "Model_Reference_Description_Link";
	public static final String ATT_CREATED = "Model_Created";
	public static final String ATT_MODIFIED = "Model_Modified";
	public static final String ATT_RIGHTS = "Model_Rights";
	public static final String ATT_NOTES = "Model_Notes";
	public static final String ATT_CURATION_STATUS = "Model_Curation_Status";
	public static final String ATT_TYPE = "Model_Type";
	public static final String ATT_SUBJECT = "Model_Subject";
	public static final String ATT_FOOD_PROCESS = "Model_Food_Process";
	public static final String ATT_DEP = "Model_Dependent_Variable";
	public static final String ATT_DEP_UNITS = "Model_Dependent_Variable_Unit";
	public static final String ATT_DEP_MIN = "Model_Dependent_Variable_Min";
	public static final String ATT_DEP_MAX = "Model_Dependent_Variable_Max";
	public static final String ATT_INDEP = "Model_Independent_Variable";
	public static final String ATT_SOFTWARE = "Software";
	public static final String ATT_SOFTWARE_LINK = "Software_Link";
	public static final String ATT_ACCESIBILITY = "Software_Accesibility";
	public static final String ATT_STOCHASTIC_MODELING = "Software_Stochastic_Modeling?";
	public static final String ATT_PREDICTION_CONDITIONS = "Software_Prediction_Conditions";

	public OpenFSMRSchema() {
		addStringAttribute(ATT_NAME);
		addStringAttribute(ATT_ID);
		addStringAttribute(ATT_LINK);
		addStringAttribute(ATT_ORGANISM);
		addStringAttribute(ATT_ORGANISM_DETAIL);
		addStringAttribute(ATT_ENVIRONMENT);
		addStringAttribute(ATT_ENVIRONMENT_DETAIL);
		addStringAttribute(ATT_CREATOR);
		addStringAttribute(ATT_REF_DESC);
		addStringAttribute(ATT_REF_DESC_LINK);
		addStringAttribute(ATT_CREATED);
		addStringAttribute(ATT_MODIFIED);
		addStringAttribute(ATT_RIGHTS);
		addStringAttribute(ATT_NOTES);
		addStringAttribute(ATT_CURATION_STATUS);
		addStringAttribute(ATT_TYPE);
		addStringAttribute(ATT_SUBJECT);
		addStringAttribute(ATT_FOOD_PROCESS);
		addStringAttribute(ATT_DEP);
		addStringAttribute(ATT_DEP_UNITS);
		addStringAttribute(ATT_DEP_MIN);
		addStringAttribute(ATT_DEP_MAX);
		addStringAttribute(ATT_INDEP);
		addStringAttribute(ATT_SOFTWARE);
		addStringAttribute(ATT_SOFTWARE_LINK);
		addStringAttribute(ATT_ACCESIBILITY);
		addIntAttribute(ATT_STOCHASTIC_MODELING);
		addStringAttribute(ATT_PREDICTION_CONDITIONS);
	}
}
