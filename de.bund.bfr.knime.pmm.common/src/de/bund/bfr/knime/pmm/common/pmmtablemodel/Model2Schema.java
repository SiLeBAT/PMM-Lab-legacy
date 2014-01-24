/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class Model2Schema extends KnimeSchema {
	
	public static final String ATT_MODELCATALOG = getAttribute(Model1Schema.ATT_MODELCATALOG, 2);
	public static final String ATT_ESTMODEL = getAttribute(Model1Schema.ATT_ESTMODEL, 2);

	public static final String ATT_DEPENDENT = getAttribute(Model1Schema.ATT_DEPENDENT, 2);

	public static final String ATT_PARAMETER = getAttribute(Model1Schema.ATT_PARAMETER, 2);
	public static final String ATT_INDEPENDENT = getAttribute(Model1Schema.ATT_INDEPENDENT, 2);

	public static final String ATT_MLIT = getAttribute(Model1Schema.ATT_MLIT, 2);
	public static final String ATT_EMLIT = getAttribute(Model1Schema.ATT_EMLIT, 2);

	public static final String ATT_DATABASEWRITABLE = getAttribute(Model1Schema.ATT_DATABASEWRITABLE, 2);
	public static final String ATT_DBUUID = getAttribute(Model1Schema.ATT_DBUUID, 2);
	
	public static final String ATT_GLOBAL_MODEL_ID = "GlobalModelID";

	public static final int WRITABLE = 1;
	public static final int NOTWRITABLE = 0;
	public static final String MODELNAME = "ModelNameSec";
	public static final String FORMULA = "FormulaSec";
	public static final String SSE = "SSE";
	public static final String MSE = "MSE";
	public static final String RMSE = "RMSE";
	public static final String RSQUARED = "Rsquared";
	public static final String AIC = "AIC";	
	
	public Model2Schema() {
		
		try {
			addXmlAttribute( ATT_MODELCATALOG );

			addXmlAttribute( ATT_DEPENDENT );
			addXmlAttribute( ATT_INDEPENDENT );

			addXmlAttribute( ATT_PARAMETER );

			addXmlAttribute( ATT_ESTMODEL );

			addXmlAttribute(ATT_MLIT);
			
			addXmlAttribute(ATT_EMLIT);
			
			addIntAttribute( ATT_DATABASEWRITABLE );
			addStringAttribute( ATT_DBUUID );	
			addIntAttribute(ATT_GLOBAL_MODEL_ID);
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
	}
	
	
}
