/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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
	
	public static final String ATT_FORMULA = "FormulaSec";
	public static final String ATT_PARAMNAME = "ParamNameSec";
	public static final String ATT_DEPVAR = "DepVarSec";
	public static final String ATT_INDEPVAR = "IndepVarSec";
	public static final String ATT_VALUE = "ValueSec";
	public static final String ATT_MODELNAME = "ModelNameSec";
	public static final String ATT_MODELID = "ModelCatalogIDSec";
	public static final String ATT_PARAMETER = "ParameterSec";

	public static final String ATT_ESTMODELID = "EstModelIDSec";
	public static final String ATT_VARPARMAP = "VarParMapSec";
	public static final String ATT_RMS = "RMSSec";
	public static final String ATT_RSQUARED = "RsquaredSec";
	public static final String ATT_MINVALUE = "MinValueSec";
	public static final String ATT_MAXVALUE = "MaxValueSec";
	public static final String ATT_MININDEP = "MinIndepSec";
	public static final String ATT_MAXINDEP = "MaxIndepSec";
	public static final String ATT_LITIDM = "MLitIDSec";
	public static final String ATT_LITM = "MLitSec";
	public static final String ATT_LITIDEM = "EstMLitIDSec";
	public static final String ATT_LITEM = "EstMLitSec";
	public static final String ATT_PARAMERR = "StandardErrorSec";
	public static final String ATT_DATABASEWRITABLE = "DatabaseWritableSec";
	public static final String ATT_DBUUID = "M_DB_UIDSec";

	public static final int WRITABLE = 1;
	public static final int NOTWRITABLE = 0;
	
	public static final String ATT_AIC = "AICSec";
	public static final String ATT_BIC = "BICSec";


	public Model2Schema() {
		
		try {
			addIntAttribute( ATT_MODELID );
			addStringAttribute( ATT_MODELNAME );
			addStringAttribute( ATT_FORMULA );
			addStringAttribute( ATT_DEPVAR );
			addStringListAttribute( ATT_INDEPVAR );
			addXmlAttribute( ATT_PARAMETER );
			addStringListAttribute( ATT_PARAMNAME );
			addDoubleListAttribute( ATT_MINVALUE );
			addDoubleListAttribute( ATT_MAXVALUE );
			addIntListAttribute( ATT_LITIDM );
			addStringListAttribute( ATT_LITM );
			
			addIntAttribute( ATT_ESTMODELID );
			addDoubleAttribute( ATT_RMS );
			addDoubleAttribute( ATT_RSQUARED );
			addDoubleAttribute( ATT_AIC );
			addDoubleAttribute( ATT_BIC );
			addDoubleListAttribute( ATT_VALUE );
			addDoubleListAttribute( ATT_PARAMERR );
			addDoubleListAttribute( ATT_MININDEP );
			addDoubleListAttribute( ATT_MAXINDEP );
			addIntListAttribute( ATT_LITIDEM );
			addStringListAttribute( ATT_LITEM );
			
			//addStringListAttribute( ATT_VARPARMAP );
			addMapAttribute(ATT_VARPARMAP);
			addIntAttribute( ATT_DATABASEWRITABLE );
			addStringAttribute( ATT_DBUUID );					
		}
		catch( PmmException ex ) {
			ex.printStackTrace( System.err );
		}
	}
	
	
}
