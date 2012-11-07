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
package de.bund.bfr.knime.pmm.bfrdbiface.lib;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class Bfrdb extends Hsqldbiface {

	private static final String REL_ESTMODEL = "GeschaetzteModelle";
	private static final String REL_PARAM = "ModellkatalogParameter";
	private static final String REL_ESTPARAM = "GeschaetzteParameter";
	private static final String REL_CONDITION = "Versuchsbedingungen";
	private static final String REL_DATA = "Messwerte";
	private static final String REL_DOUBLE = "DoubleKennzahlen";
	private static final String REL_MODEL = "Modellkatalog";
	private static final String REL_AGENT = "Agenzien";
	private static final String REL_MATRIX = "Matrices";
	private static final String REL_LITERATURE = "Literatur";
	private static final String REL_MODEL_LITERATURE = "Modell_Referenz";
	
	private static final String ATT_NAMESHORT = "Kurzbezeichnung";
	private static final String ATT_LOG10NUNIT = "Konz_Einheit";
	private static final String ATT_TIMEUNIT = "ZeitEinheit";
	private static final String ATT_VALUETYPE = "Wert_typ";
	public static final String ATT_RSS = "RSS";
	public static final String ATT_RSQUARED = "Rsquared";
	public static final String ATT_CONDITIONID = "Versuchsbedingung";
	public static final String ATT_MODELID = "Modell";
	public static final String ATT_ID = "ID";
	public static final String ATT_KEY = "Parametername";
	public static final String ATT_ESTMODELID = "GeschaetztesModell";
	public static final String ATT_PARAMID = "Parameter";
	public static final String ATT_VALUE = "Wert";	
	public static final String ATT_TEMPERATURE = "Temperatur";
	public static final String ATT_PH = "pH";
	public static final String ATT_AW = "aw";
	public static final String ATT_TIME = "Zeit";
	public static final String ATT_AGENTNAME = "Agensname";
	public static final String ATT_MATRIXNAME = "Matrixname";
	public static final String ATT_LOG10N = "Konzentration";
	public static final String ATT_FORMULA = "Formel";
	public static final String ATT_LEVEL = "Level";
	public static final String ATT_AGENTID = "Agens";
	public static final String ATT_MATRIXID = "Matrix";
	public static final String ATT_PARAMTYPE = "Parametertyp";
	public static final String ATT_DEP = "Dependent";
	public static final String ATT_INDEP = "Independent";
	public static final String ATT_NAME = "Name";
	public static final String ATT_PARAMNAME = "Parametername";
	public static final String ATT_LITERATUREID = "Literatur";
	public static final String ATT_FIRSTAUTHOR = "Erstautor";
	public static final String ATT_YEAR = "Jahr";
	public static final String ATT_RECORDID = "ID_CB";
	public static final String ATT_COMBASEID = "CombaseID";
	//private static final String ATT_INPUTDATE = "Eingabedatum";
	public static final String ATT_CONDITIONS = "b_f_details_CB";
	public static final String ATT_MAX = "max";
	public static final String ATT_MIN = "min";
	public static final String ATT_AGENTDETAIL = "AgensDetail";
	public static final String ATT_MATRIXDETAIL = "MatrixDetail";
	public static final String ATT_MISC = "Sonstiges";
	private static final String REL_COMBASE = "ImportedCombaseData";
	public static final String ATT_COMMENT = "Kommentar";
	private static final String REL_MISCPARAM = "SonstigeParameter";
	private static final String ATT_CONDITION_MISCPARAM = "Versuchsbedingungen_Sonstiges";
	private static final String REL_UNIT = "Einheiten";
	public static final String ATT_MISCID = "SonstigesID";
	private static final String ATT_DESCRIPTION = "Beschreibung";
	private static final String ATT_UNIT = "Einheit";
	public static final String ATT_LITERATURETEXT = "ReferenzText";
	public static final String ATT_MININDEP = "minIndep";
	public static final String ATT_MINVALUE = "minValue";
	public static final String ATT_MAXINDEP = "maxIndep";
	public static final String ATT_MAXVALUE = "maxValue";
	public static final String ATT_RMS = "RMS";
	private static final String ATT_STANDDARDERROR = "StandardError";
	private static final String REL_VARMAP = "VarParMaps";
	private static final String ATT_VARMAPFROM = "VarPar";
	public static final String ATT_VARMAPTO = "VarParMap";
	
	public static final int PARAMTYPE_INDEP = 1;
	public static final int PARAMTYPE_PARAM = 2;
	public static final int PARAMTYPE_DEP = 3;
	
	private static final String queryEstModelPrim3 = "SELECT\n"
		+"    \""+ATT_FORMULA+"\",\n"
		+"    \""+ATT_DEP+"\",\n"
		+"    \""+ATT_INDEP+"\",\n"
		+"    \""+ATT_PARAMNAME+"\",\n"
		+"    \""+ATT_VALUE+"\",\n"
		+"    \""+ATT_NAME+"\",\n"
		+"    \""+REL_MODEL+"\".\"ID\" AS \""+ATT_MODELID+"\",\n"
		+"    \""+REL_ESTMODEL+"\".\"ID\" AS \""+ATT_ESTMODELID+"\",\n"
		+"    \"RMS\",\n"
		+"    \""+ATT_RSQUARED+"\",\n"
		+"    \"AIC\",\n"
		+"    \"BIC\",\n"
		+"    \""+ATT_MIN+"\",\n"
		+"    \""+ATT_MAX+"\",\n"
		+"    \""+ATT_MININDEP+"\",\n"
		+"    \""+ATT_MAXINDEP+"\",\n"
		+"    \"LitMID\",\n"
		+"    \"LitM\",\n"
		+"    \"LitEmID\",\n"
		+"    \"LitEm\",\n"
		+"    \""+ATT_CONDITIONID+"\",\n"
		+"    \""+ATT_STANDDARDERROR+"\",\n"
		+"    \""+ATT_VARMAPTO+"\"\n"
		+"\n"
		+"\n"
		+"FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_MODEL+"\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\""+REL_ESTMODEL+"\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_DEP+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \""+REL_MODEL+"\"\n"
		+"    ON \""+REL_MODEL+"\".\"ID\"=\""+REL_ESTMODEL+"\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_PARAM+"\".\""+ATT_MODELID+"\"=\""+REL_MODEL+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=3\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"DepVarView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"DepVarView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        ARRAY_AGG(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_INDEP+"\",\n"
		+"        ARRAY_AGG( \"Gueltig_von\" )AS \""+ATT_MININDEP+"\",\n"
		+"        ARRAY_AGG( \"Gueltig_bis\" )AS \""+ATT_MAXINDEP+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \"GueltigkeitsBereiche\"\n"
		+"    ON \""+REL_ESTMODEL+"\".\"ID\"=\"GueltigkeitsBereiche\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \"GueltigkeitsBereiche\".\""+ATT_PARAMID+"\"=\""+REL_PARAM+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=1\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"IndepVarView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"IndepVarView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        ARRAY_AGG(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_PARAMNAME+"\",\n"
		+"        ARRAY_AGG( \""+ATT_VALUE+"\" )AS \""+ATT_VALUE+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MIN+"\" )AS \""+ATT_MIN+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MAX+"\" )AS \""+ATT_MAX+"\",\n"
		+"        ARRAY_AGG( \""+ATT_STANDDARDERROR+"\" )AS \""+ATT_STANDDARDERROR+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \""+REL_ESTPARAM+"\"\n"
		+"    ON \""+REL_ESTMODEL+"\".\"ID\"=\""+REL_ESTPARAM+"\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_ESTPARAM+"\".\""+ATT_PARAMID+"\"=\""+REL_PARAM+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=2\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"ParamView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"ParamView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN (\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_MODELID+"\",\n"
		+"        GROUP_CONCAT( CONCAT( \""+ATT_FIRSTAUTHOR+"\", '_', \"Jahr\" ) )AS \"LitM\",\n"
		+"        GROUP_CONCAT( \""+ATT_LITERATUREID+"\".\"ID\" )AS \"LitMID\"\n"
		+"    FROM \""+REL_MODEL_LITERATURE+"\"\n"
		+"    JOIN \""+ATT_LITERATUREID+"\"\n"
		+"    ON \""+REL_MODEL_LITERATURE+"\".\""+ATT_LITERATUREID+"\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+ATT_MODELID+"\"\n"
		+"\n"
		+")\"LitMView\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"LitMView\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN (\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT( CONCAT( \""+ATT_FIRSTAUTHOR+"\", '_', \"Jahr\" ) )AS \"LitEm\",\n"
		+"        GROUP_CONCAT( \""+ATT_LITERATUREID+"\".\"ID\" )AS \"LitEmID\"\n"
		+"\n"
		+"    FROM \""+ATT_ESTMODELID+"_Referenz\"\n"
		+"\n"
		+"    JOIN \""+ATT_LITERATUREID+"\"\n"
		+"    ON \""+ATT_ESTMODELID+"_Referenz\".\""+ATT_LITERATUREID+"\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+")\"LitEmView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"LitEmView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"\n"
		+"        \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT(\n"
		+"            CONCAT(\n"
		+"                \""+REL_VARMAP+"\".\""+ATT_VARMAPTO+"\",\n"
		+"                '=',\n"
		+"                \""+REL_PARAM+"\".\""+ATT_PARAMNAME+"\" ) )AS \""+ATT_VARMAPTO+"\"\n"
		+"\n"
		+"    FROM \""+REL_VARMAP+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_PARAM+"\".\"ID\"=\""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\" \n"
		+"\n"
		+"    GROUP BY \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\""+ATT_VARMAPTO+"View\"\n"
		+"ON \""+ATT_VARMAPTO+"View\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"WHERE \""+ATT_LEVEL+"\"=1\n";
	
	private static final String queryEstModelSec3 = "SELECT\n"
		+"    \""+ATT_FORMULA+"\" AS \""+ATT_FORMULA+"2\",\n"
		+"    \""+ATT_DEP+"\" AS \""+ATT_DEP+"2\",\n"
		+"    \""+ATT_INDEP+"\" AS \""+ATT_INDEP+"2\",\n"
		+"    \""+ATT_PARAMNAME+"\" AS \""+ATT_PARAMNAME+"2\",\n"
		+"    \""+ATT_VALUE+"\" AS \""+ATT_VALUE+"2\",\n"
		+"    \""+ATT_NAME+"\" AS \""+ATT_NAME+"2\",\n"
		+"    \""+REL_MODEL+"\".\"ID\" AS \""+ATT_MODELID+"2\",\n"
		+"    \""+REL_ESTMODEL+"\".\"ID\" AS \""+ATT_ESTMODELID+"2\",\n"
		+"    \"RMS\" AS \"RMS2\",\n"
		+"    \""+ATT_RSQUARED+"\" AS \""+ATT_RSQUARED+"2\",\n"
		+"    \"AIC\" AS \"AIC2\",\n"
		+"    \"BIC\" AS \"BIC2\",\n"
		+"    \""+ATT_MIN+"\" AS \""+ATT_MIN+"2\",\n"
		+"    \""+ATT_MAX+"\" AS \""+ATT_MAX+"2\",\n"
		+"    \""+ATT_MININDEP+"\" AS \""+ATT_MININDEP+"2\",\n"
		+"    \""+ATT_MAXINDEP+"\" AS \""+ATT_MAXINDEP+"2\",\n"
		+"    \"LitMID\" AS \"LitMID2\",\n"
		+"    \"LitM\" AS \"LitM2\",\n"
		+"    \"LitEmID\" AS \"LitEmID2\",\n"
		+"    \"LitEm\" AS \"LitEm2\",\n"
		+"    \""+ATT_CONDITIONID+"\" AS \""+ATT_CONDITIONID+"2\",\n"
		+"    \""+ATT_STANDDARDERROR+"\" AS \""+ATT_STANDDARDERROR+"2\",\n"
		+"    \""+ATT_VARMAPTO+"\" AS \""+ATT_VARMAPTO+"2\"\n"
		+"\n"
		+"FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_MODEL+"\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\""+REL_ESTMODEL+"\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_DEP+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \""+REL_MODEL+"\"\n"
		+"    ON \""+REL_MODEL+"\".\"ID\"=\""+REL_ESTMODEL+"\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_PARAM+"\".\""+ATT_MODELID+"\"=\""+REL_MODEL+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=3\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"DepVarView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"DepVarView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        ARRAY_AGG(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_INDEP+"\",\n"
		+"        ARRAY_AGG( \"Gueltig_von\" )AS \""+ATT_MININDEP+"\",\n"
		+"        ARRAY_AGG( \"Gueltig_bis\" )AS \""+ATT_MAXINDEP+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \"GueltigkeitsBereiche\"\n"
		+"    ON \""+REL_ESTMODEL+"\".\"ID\"=\"GueltigkeitsBereiche\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \"GueltigkeitsBereiche\".\""+ATT_PARAMID+"\"=\""+REL_PARAM+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=1\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"IndepVarView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"IndepVarView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        ARRAY_AGG(\n"
		+"            CASE\n"
		+"                WHEN \""+ATT_VARMAPTO+"\" IS NOT NULL\n"
		+"                THEN \""+ATT_VARMAPTO+"\"\n"
		+"                ELSE \""+ATT_PARAMNAME+"\"\n"
		+"            END\n"
		+"        )AS \""+ATT_PARAMNAME+"\",\n"
		+"        ARRAY_AGG( \""+ATT_VALUE+"\" )AS \""+ATT_VALUE+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MIN+"\" )AS \""+ATT_MIN+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MAX+"\" )AS \""+ATT_MAX+"\",\n"
		+"        ARRAY_AGG( \""+ATT_STANDDARDERROR+"\" )AS \""+ATT_STANDDARDERROR+"\"\n"
		+"\n"
		+"    FROM \""+REL_ESTMODEL+"\"\n"
		+"\n"
		+"    JOIN \""+REL_ESTPARAM+"\"\n"
		+"    ON \""+REL_ESTMODEL+"\".\"ID\"=\""+REL_ESTPARAM+"\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_ESTPARAM+"\".\""+ATT_PARAMID+"\"=\""+REL_PARAM+"\".\"ID\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_VARMAP+"\"\n"
		+"    ON \""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"=\""+REL_PARAM+"\".\"ID\" AND \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=2\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\"ParamView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"ParamView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN (\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_MODELID+"\",\n"
		+"        GROUP_CONCAT( CONCAT( \""+ATT_FIRSTAUTHOR+"\", '_', \"Jahr\" ) )AS \"LitM\",\n"
		+"        GROUP_CONCAT( \""+ATT_LITERATUREID+"\".\"ID\" )AS \"LitMID\"\n"
		+"    FROM \""+REL_MODEL_LITERATURE+"\"\n"
		+"    JOIN \""+ATT_LITERATUREID+"\"\n"
		+"    ON \""+REL_MODEL_LITERATURE+"\".\""+ATT_LITERATUREID+"\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+ATT_MODELID+"\"\n"
		+"\n"
		+")\"LitMView\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"LitMView\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN (\n"
		+"\n"
		+"    SELECT\n"
		+"        \""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT( CONCAT( \""+ATT_FIRSTAUTHOR+"\", '_', \"Jahr\" ) )AS \"LitEm\",\n"
		+"        GROUP_CONCAT( \""+ATT_LITERATUREID+"\".\"ID\" )AS \"LitEmID\"\n"
		+"\n"
		+"    FROM \""+ATT_ESTMODELID+"_Referenz\"\n"
		+"\n"
		+"    JOIN \""+ATT_LITERATUREID+"\"\n"
		+"    ON \""+ATT_ESTMODELID+"_Referenz\".\""+ATT_LITERATUREID+"\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+ATT_ESTMODELID+"\"\n"
		+")\"LitEmView\"\n"
		+"ON \""+REL_ESTMODEL+"\".\"ID\"=\"LitEmView\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"\n"
		+"        \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\",\n"
		+"        GROUP_CONCAT(\n"
		+"            CONCAT(\n"
		+"                \""+REL_VARMAP+"\".\""+ATT_VARMAPTO+"\",\n"
		+"                '=',\n"
		+"                \""+REL_PARAM+"\".\""+ATT_PARAMNAME+"\" ) )AS \""+ATT_VARMAPTO+"\"\n"
		+"\n"
		+"    FROM \""+REL_VARMAP+"\"\n"
		+"\n"
		+"    JOIN \""+REL_PARAM+"\"\n"
		+"    ON \""+REL_PARAM+"\".\"ID\"=\""+REL_VARMAP+"\".\""+ATT_VARMAPFROM+"\"\n"
		+"\n"
		+"    GROUP BY \""+REL_VARMAP+"\".\""+ATT_ESTMODELID+"\"\n"
		+"\n"
		+")\""+ATT_VARMAPTO+"View\"\n"
		+"ON \""+ATT_VARMAPTO+"View\".\""+ATT_ESTMODELID+"\"=\""+REL_ESTMODEL+"\".\"ID\"\n"
		+"\n"
		+"WHERE \""+ATT_LEVEL+"\"=2\n";
	
	private static final String queryTimeSeries5 = "SELECT\n"
		+"\n"
		+"\""+REL_CONDITION+"\".\"ID\" AS \""+ATT_CONDITIONID+"\",\n"
		+"\""+REL_COMBASE+"\".\""+ATT_COMBASEID+"\",\n"
		+"\"MiscView\".\""+ATT_MISCID+"\",\n"
		+"\"MiscView\".\""+ATT_MISC+"\",\n"
		+"\"D3\".\""+ATT_VALUE+"\" AS \""+ATT_TEMPERATURE+"\",\n"
		+"\"D4\".\""+ATT_VALUE+"\" AS \"pH\",\n"
		+"\"D5\".\""+ATT_VALUE+"\" AS \""+ATT_AW+"\",\n"
		+"\""+REL_CONDITION+"\".\""+ATT_AGENTID+"\" AS \""+ATT_AGENTID+"\",\n"
		+"\""+ATT_AGENTNAME+"\",\n"
		+"\""+REL_CONDITION+"\".\""+ATT_AGENTDETAIL+"\",\n"
		+"\""+REL_CONDITION+"\".\""+ATT_MATRIXID+"\" AS \""+ATT_MATRIXID+"\",\n"
		+"\""+REL_MATRIX+"\".\""+ATT_MATRIXNAME+"\",\n"
		+"\""+REL_CONDITION+"\".\""+ATT_MATRIXDETAIL+"\",\n"
		+"\"DataView\".\""+ATT_TIME+"\",\n"
		+"\"DataView\".\""+ATT_LOG10N+"\",\n"
		+"\""+REL_CONDITION+"\".\""+ATT_COMMENT+"\",\n"
		+"\""+REL_CONDITION+"\".\"Referenz\" AS \""+ATT_LITERATUREID+"\",\n"
		+"CONCAT( \""+ATT_LITERATUREID+"\".\""+ATT_FIRSTAUTHOR+"\", '_', \""+ATT_LITERATUREID+"\".\"Jahr\" )AS "+ATT_LITERATURETEXT+"\n"
		+"\n"
		+"FROM \""+REL_CONDITION+"\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_COMBASE+"\"\n"
		+"ON \""+REL_CONDITION+"\".\"ID\"=\""+REL_COMBASE+"\".\""+ATT_CONDITIONID+"\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_DOUBLE+"\" AS \"D3\"\n"
		+"ON \""+REL_CONDITION+"\".\""+ATT_TEMPERATURE+"\"=\"D3\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_DOUBLE+"\" AS \"D4\"\n"
		+"ON \""+REL_CONDITION+"\".\"pH\"=\"D4\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_DOUBLE+"\" AS \"D5\"\n"
		+"ON \""+REL_CONDITION+"\".\""+ATT_AW+"\"=\"D5\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_AGENT+"\"\n"
		+"ON \""+REL_CONDITION+"\".\""+ATT_AGENTID+"\"=\""+REL_AGENT+"\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN \""+REL_MATRIX+"\"\n"
		+"ON \""+REL_CONDITION+"\".\""+ATT_MATRIXID+"\"=\""+REL_MATRIX+"\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN \""+ATT_LITERATUREID+"\"\n"
		+"ON \""+REL_CONDITION+"\".\"Referenz\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"\n"
		+"    \""+ATT_CONDITION_MISCPARAM+"\".\""+REL_CONDITION+"\",\n"
		+"    GROUP_CONCAT( \""+REL_MISCPARAM+"\".\"ID\" )AS \""+ATT_MISCID+"\",\n"
		+"\n"
		+"    GROUP_CONCAT(\n"
		+"        CONCAT(\n"
		+"            \""+REL_MISCPARAM+"\".\""+ATT_DESCRIPTION+"\",\n"
		+"            CASE \""+ATT_CONDITION_MISCPARAM+"\".\""+ATT_UNIT+"\"\n"
		+"                WHEN IS NULL THEN ''\n"
		+"                ELSE CONCAT( '(', \""+REL_UNIT+"\".\""+ATT_UNIT+"\", ')' )\n"
		+"            END,\n"
		+"            CASE \""+REL_DOUBLE+"\".\""+ATT_VALUE+"\"\n"
		+"                WHEN IS NULL THEN ''\n"
		+"                ELSE CONCAT( ':', \""+REL_DOUBLE+"\".\""+ATT_VALUE+"\" )\n"
		+"            END\n"
		+"        )\n"
		+"    )AS \""+ATT_MISC+"\"\n"
		+"\n"
		+"    FROM \""+ATT_CONDITION_MISCPARAM+"\"\n"
		+"\n"
		+"    LEFT JOIN \""+REL_UNIT+"\"\n"
		+"    ON \""+ATT_CONDITION_MISCPARAM+"\".\""+ATT_UNIT+"\"=\""+REL_UNIT+"\".\"ID\"\n"
		+"\n"
		+"    JOIN \""+REL_MISCPARAM+"\"\n"
		+"    ON \""+ATT_CONDITION_MISCPARAM+"\".\""+REL_MISCPARAM+"\"=\""+REL_MISCPARAM+"\".\"ID\"\n"
		+"\n"
		+"		LEFT JOIN \""+REL_DOUBLE+"\"\n"
		+"		ON \""+ATT_CONDITION_MISCPARAM+"\".\""+ATT_VALUE+"\"=\""+REL_DOUBLE+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+ATT_CONDITION_MISCPARAM+"\".\""+REL_CONDITION+"\"\n"
		+"\n"
		+")\"MiscView\"\n"
		+"ON \""+REL_CONDITION+"\".\"ID\"=\"MiscView\".\""+REL_CONDITION+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"\n"
		+"    SELECT\n"
		+"\n"
		+"        \""+REL_CONDITION+"\",\n"
		+"        GROUP_CONCAT(\n"
		+"\n"
		+"            CASE\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Stunde'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Minute'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"/60\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Sekunde'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"/3600\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Tag'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"*24\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Woche'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"*168\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Monat'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"*730.5\n"
		+"                WHEN \""+REL_DATA+"\".\""+ATT_TIMEUNIT+"\" LIKE 'Jahr'\n"
		+"                    THEN \"T\".\""+ATT_VALUE+"\"*8766\n"
		+"                ELSE\n"
		+"                    NULL\n"
		+"            END\n"
		+"\n"
		+"        )AS \""+ATT_TIME+"\",\n"
		+"\n"
		+"        GROUP_CONCAT(\n"
		+"\n"
		+"            CASE\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", 'log .* pro 25.*' )\n"
		+"                    THEN \"K\".\""+ATT_VALUE+"\"-LOG10( 25 )\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", 'log .* pro 100.*' )\n"
		+"                    THEN \"K\".\""+ATT_VALUE+"\"-LOG10( 100 )\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", 'log .* pro .*' )\n"
		+"                    THEN \"K\".\""+ATT_VALUE+"\"\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", '.* pro 25.*' )\n"
		+"                    THEN LOG10( \"K\".\""+ATT_VALUE+"\"/25 )\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", '.* pro 100.*' )\n"
		+"                    THEN LOG10( \"K\".\""+ATT_VALUE+"\"/100 )\n"
		+"                WHEN REGEXP_MATCHES( \""+REL_UNIT+"\".\""+ATT_UNIT+"\", '.* pro .*' )\n"
		+"                    THEN LOG10( \"K\".\""+ATT_VALUE+"\" )\n"
		+"                ELSE\n"
		+"                    NULL\n"
		+"            END\n"
		+"\n"
		+"        )AS \""+ATT_LOG10N+"\"\n"
		+"\n"
		+"    FROM \""+REL_DATA+"\"\n"
		+"\n"
		+"    JOIN \""+REL_DOUBLE+"\" AS \"T\"\n"
		+"    ON \""+REL_DATA+"\".\""+ATT_TIME+"\"=\"T\".\"ID\"\n"
		+"\n"
		+"    JOIN \""+REL_DOUBLE+"\" AS \"K\"\n"
		+"    ON \""+REL_DATA+"\".\""+ATT_LOG10N+"\"=\"K\".\"ID\"\n"
		+"\n"
		+"    JOIN \""+REL_UNIT+"\"\n"
		+"    ON \""+REL_DATA+"\".\""+ATT_LOG10NUNIT+"\"=\""+REL_UNIT+"\".\"ID\"\n"
		+"\n"
		+"    GROUP BY \""+REL_CONDITION+"\"\n"
		+"\n"
		+")\"DataView\"\n"
		+"ON \""+REL_CONDITION+"\".\"ID\"=\"DataView\".\""+REL_CONDITION+"\"\n";
	
	
	public Bfrdb( final Connection conn ) { super( conn ); }
	
	public Bfrdb( final String filename, final String login, final String pw )
	throws ClassNotFoundException, SQLException {
		super( filename, login, pw );
	}
	
	public void close() throws SQLException {		
		//super.close();
	}
	
	public static String queryEstPei() throws SQLException {
		
		
		String q;
		
		q = "WITH \"Em1View\" AS( "+queryEstModelPrim3+" ), "
		+"\"Em2View\" AS( "+queryEstModelSec3+" ), "
		+"\"TsView\" AS( "+queryTimeSeries5+" )"
		+"SELECT * "
		+"FROM \"TsView\" "
		+"RIGHT JOIN \"Em1View\" "
		+"ON \"Em1View\".\""+ATT_CONDITIONID+"\"=\"TsView\".\""+ATT_CONDITIONID+"\"";

		return q;
		
	}
	
	public String queryEstSei() throws SQLException {
		
		String q;
		
		q = queryEstPei();
		
		q += " JOIN \"Sekundaermodelle_Primaermodelle\" "
			+"ON \"Sekundaermodelle_Primaermodelle\".\"GeschaetztesPrimaermodell\"=\"Em1View\".\"GeschaetztesModell\""
			
			+" JOIN \"Em2View\" "
			+"ON \"Sekundaermodelle_Primaermodelle\".\"GeschaetztesSekundaermodell\"=\"Em2View\".\"GeschaetztesModell2\"";
		
		return q;
	}
	
	public ResultSet selectModel( final int level ) throws SQLException {
		String sql = "SELECT\n"
		+"\n"
		+"\""+ATT_FORMULA+"\",\n"
		+"\"P\".\""+ATT_PARAMNAME+"\",\n"
		+"\"D\".\""+ATT_PARAMNAME+"\" AS \""+ATT_DEP+"\",\n"
		+"\"I\".\""+ATT_PARAMNAME+"\" AS \""+ATT_INDEP+"\",\n"
		+"\""+REL_MODEL+"\".\""+ATT_NAME+"\",\n"
		+"\""+REL_MODEL+"\".\"ID\" AS \""+ATT_MODELID+"\",\n"
		+"\""+ATT_MINVALUE+"\",\n"
		+"\""+ATT_MAXVALUE+"\",\n"
		+"\""+ATT_MININDEP+"\",\n"
		+"\""+ATT_MAXINDEP+"\",\n"
		+"\""+ATT_LITERATUREID+"\",\n"
		+"\""+ATT_LITERATURETEXT+"\",\n"
		+"\""+ATT_LEVEL+"\"\n"
		+"\n"
		+"FROM \""+REL_MODEL+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"    SELECT\n"
		+"        \""+ATT_MODELID+"\",\n"
		+"        GROUP_CONCAT( \""+ATT_LITERATUREID+"\".\"ID\" )AS \""+ATT_LITERATUREID+"\",\n"
		+"        GROUP_CONCAT( CONCAT( \""+ATT_FIRSTAUTHOR+"\", '_', \"Jahr\" ) )AS \""+ATT_LITERATURETEXT+"\"\n"
		+"    FROM \""+REL_MODEL_LITERATURE+"\"\n"
		+"    JOIN \""+ATT_LITERATUREID+"\"\n"
		+"    ON \""+REL_MODEL_LITERATURE+"\".\""+ATT_LITERATUREID+"\"=\""+ATT_LITERATUREID+"\".\"ID\"\n"
		+"    GROUP BY \""+ATT_MODELID+"\"\n"
		+")AS \"LitView\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"LitView\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"    SELECT \""+ATT_MODELID+"\", \""+ATT_PARAMNAME+"\"\n"
		+"    FROM \""+REL_MODEL+""+ATT_PARAMID+"\"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=3 )AS \"D\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"D\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"    SELECT\n"
		+"        \""+ATT_MODELID+"\",\n"
		+"        ARRAY_AGG( \""+ATT_PARAMNAME+"\" )AS \""+ATT_PARAMNAME+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MIN+"\" )AS \""+ATT_MININDEP+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MAX+"\" )AS \""+ATT_MAXINDEP+"\"\n"
		+"    FROM \""+REL_MODEL+""+ATT_PARAMID+"\"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=1\n"
		+"    GROUP BY \""+ATT_MODELID+"\" )AS \"I\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"I\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"LEFT JOIN(\n"
		+"    SELECT\n"
		+"        \""+ATT_MODELID+"\",\n"
		+"        ARRAY_AGG( \""+ATT_PARAMNAME+"\" )AS \""+ATT_PARAMNAME+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MIN+"\" )AS \""+ATT_MINVALUE+"\",\n"
		+"        ARRAY_AGG( \""+ATT_MAX+"\" )AS \""+ATT_MAXVALUE+"\"\n"
		+"    FROM \""+REL_MODEL+""+ATT_PARAMID+"\"\n"
		+"    WHERE \""+ATT_PARAMTYPE+"\"=2\n"
		+"    GROUP BY \""+ATT_MODELID+"\" )AS \"P\"\n"
		+"ON \""+REL_MODEL+"\".\"ID\"=\"P\".\""+ATT_MODELID+"\"\n"
		+"\n"
		+"WHERE \""+ATT_LEVEL+"\"=?\n";
		
		PreparedStatement ps = conn.prepareStatement( sql );
		ps.setInt( 1, level );
		
		return ps.executeQuery();
	}
	
	public PmmXmlDoc getMiscXmlDoc(Integer tsID) throws SQLException {
		PmmXmlDoc miscDoc = new PmmXmlDoc();
		String sql = "SELECT " + "\"SonstigeParameter\".\"ID\" AS \"ID\"," +
			"\"SonstigeParameter\".\"Parameter\" AS \"Parameter\"," +
			"\"SonstigeParameter\".\"Beschreibung\" AS \"Beschreibung\"," +
			"\"DoubleKennzahlen\".\"Wert\" AS \"Wert\"," +
			"\"Einheiten\".\"Einheit\" AS \"Einheit\" FROM \"Versuchsbedingungen_Sonstiges\"" +		 
			" left join \"SonstigeParameter\" on \"Versuchsbedingungen_Sonstiges\".\"SonstigeParameter\" = \"SonstigeParameter\".\"ID\"" +
			" left join \"DoubleKennzahlen\" on \"Versuchsbedingungen_Sonstiges\".\"Wert\" = \"DoubleKennzahlen\".\"ID\"" +
			" left join \"Einheiten\" on \"Versuchsbedingungen_Sonstiges\".\"Einheit\" = \"Einheiten\".\"ID\"" +
			" where \"Versuchsbedingungen_Sonstiges\".\"Versuchsbedingungen\" = " + tsID;
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			MiscXml mx = new MiscXml(rs.getInt("ID"),rs.getString("Parameter"),rs.getString("Beschreibung"),rs.getDouble("Wert"),rs.getString("Einheit"));
			miscDoc.add(mx);
		}
		return miscDoc;
	}
	public ResultSet selectEstModel( final int level ) throws SQLException {
		String q;
		if( level == 1 ) {
			q = queryEstPei();
		} else {
			q = queryEstSei();
		}		
		//System.out.println( q );
		PreparedStatement ps = conn.prepareStatement( q );
		return ps.executeQuery();
	}
	
	public ResultSet selectTs() throws SQLException {	
		//System.err.println(queryTimeSeries5);
		return pushQuery( queryTimeSeries5 );
	}
	
	public ResultSet selectParam( final int paramtype ) throws SQLException {
		
		String q;
		
		q = "SELECT \""+ATT_MODELID+"\", \""+ATT_PARAMNAME+"\" FROM \""+
				REL_PARAM+"\" WHERE \""+ATT_PARAMTYPE+"\"="+paramtype;
		
		return pushQuery( q );
	}
	
	public ResultSet selectRelatedLiterature( final String modelName )
	throws SQLException {
		
		
		String q;
		PreparedStatement psSelectRelLit;
		
		q = "SELECT \""+REL_LITERATURE+"\".\""+ATT_FIRSTAUTHOR+"\", \""+REL_LITERATURE
		+"\".\""+ATT_YEAR+"\" FROM \""+REL_MODEL+"\" JOIN \""+REL_MODEL_LITERATURE
		+"\" ON \""+REL_MODEL+"\".\""+ATT_ID+"\"=\""+REL_MODEL_LITERATURE
		+"\".\""+ATT_MODELID+"\" JOIN \""+REL_LITERATURE+"\" ON \""+REL_LITERATURE+"\".\""
		+ATT_ID+"\"=\""+REL_MODEL_LITERATURE+"\".\""+ATT_LITERATUREID+"\" WHERE \""+ATT_NAME
		+"\"=?";
		
		//System.out.println( q );
		
		psSelectRelLit = conn.prepareStatement( q );
		
		psSelectRelLit.setString( 1, modelName );
		
		return psSelectRelLit.executeQuery();
	}
	
	/* public ResultSet selectChosenLiterature( final String modelName, final String form )
	throws SQLException {
		
		PreparedStatement psSelectPossLit;
		
		psSelectPossLit = conn.prepareStatement(
				
			"SELECT \""+REL_LITERATURE+"\".\""+ATT_FIRSTAUTHOR+"\", \""+REL_LITERATURE+"\".\""+ATT_YEAR
			+"\", \""+REL_MODEL+"\".\""+ATT_ID+"\" FROM \""+REL_LITERATURE+"\" LEFT JOIN \""
			+REL_MODEL_LITERATURE+"\" ON \""+REL_LITERATURE+"\".\""+ATT_ID+"\"=\""+REL_MODEL_LITERATURE
			+"\".\""+ATT_LITERATUREID+"\" LEFT JOIN \""+REL_MODEL+"\" ON \""+REL_MODEL+"\".\""+ATT_ID
			+"\"=\""+REL_MODEL_LITERATURE+"\".\""+ATT_MODELID+"\" WHERE( \""+REL_MODEL+"\".\""+ATT_NAME
			+"\"=? AND \""+REL_MODEL+"\".\""+ATT_FORMULA+"\"=? )OR \""+REL_MODEL+"\".\""+ATT_ID
			+"\" IS NULL"
		);
		
		return psSelectPossLit.executeQuery();
	} */
	
	public String getRelatedLiterature( final String modelName ) {
		
		ResultSet result;
		String ret;
		
		ret = "";
		
		try {
			
			result = selectRelatedLiterature( modelName );
			
			while( result.next() ) {
				
				if( !ret.isEmpty() ) {
					ret += ", ";
				}
				
				ret += result.getString( 1 )+" et al. "+result.getString( 2 );
			}
			
			result.getStatement().close();
			result.close();
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		return ret;
	}
	
	public HashMap<Integer, String> getPossibleLiterature() {
		
		HashMap<Integer, String> ret;
		ResultSet result;
		
		ret = new HashMap<Integer, String>();
		
		try {
			
			PreparedStatement psSelectPossLit;
			
			psSelectPossLit = conn.prepareStatement(
					
				"SELECT \"ID\",\""+ATT_FIRSTAUTHOR+"\", \""+ATT_YEAR+"\" FROM \""+REL_LITERATURE+"\""
			);
			
			result = psSelectPossLit.executeQuery();
			
			while( result.next() ) {
				
				ret.put(result.getInt("ID"), result.getString(2)+" et al. "+result.getInt(3) );
			}
			
			result.getStatement().close();
			result.close();
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		return ret;
	}
	
	public int getNumLitEntry() {
		
		int n;
		PreparedStatement psNumLitEntry;
		ResultSet result;
		
		n = -1;
		
		try {
		
			psNumLitEntry = conn.prepareStatement( "SELECT COUNT( * )FROM \""+REL_LITERATURE+"\"" );
			
			result = psNumLitEntry.executeQuery();
			
			result.next();
			return result.getInt( 1 );
			
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		
		return n;
	}
	
	public int getNumModel() {
		
		int n;
		PreparedStatement psNumLitEntry;
		ResultSet result;
		
		n = -1;
		
		try {
		
			psNumLitEntry = conn.prepareStatement( "SELECT COUNT( * )FROM \""+REL_MODEL+"\"" );
			
			result = psNumLitEntry.executeQuery();
			
			result.next();
			return result.getInt( 1 );
			
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		
		return n;
	}
	
	public LinkedList<String> getPossibleModelName() {
		
		LinkedList<String> ret;
		String q;
		PreparedStatement psPossibleModelName;
		ResultSet result;
		
		ret = new LinkedList<String>();
		
		q = "SELECT \""+ATT_NAME+"\" FROM \""+REL_MODEL+"\"";
		
		try {
		
			psPossibleModelName = conn.prepareStatement( q );
			
			result = psPossibleModelName.executeQuery();
			
			while( result.next() ) {
				ret.add( result.getString( 1 ) );
			}
			
			result.getStatement().close();
			result.close();
			
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		return ret;                                    
	}
	
	public HashMap<Integer, List<Integer>> getModLitMatrix() {
		
		HashMap<Integer, List<Integer>> resultt = new HashMap<Integer, List<Integer>>();
		//boolean[][] mat;
		//int n, m, i, j;
		//HashMap<Integer, String> literature;
		//LinkedList<String> model;
		String q;
		PreparedStatement psLitMat;
		ResultSet result;
		
		//n = getNumLitEntry();
		//m = getNumModel();
		
		//mat = new boolean[ n ][ m ];
		
		//literature = getPossibleLiterature();
		//model = getPossibleModelName();
		
		q = "SELECT \"Modell\",\"Literatur\" FROM \""+REL_MODEL_LITERATURE+"\"";
		
		
		try {
			
			psLitMat = conn.prepareStatement( q );
			
			result = psLitMat.executeQuery();
			
			while( result.next() ) {
				
				//i = indexOf( literature, result.getString( 2 ) );
				//j = indexOf( model, result.getString( 3 ) );
				
				//mat[ i ][ j ] = true;
				List<Integer> li = resultt.get(result.getInt(1));
				if (li == null) {
					li = new ArrayList<Integer>();
				}
				li.add(result.getInt(2));
				resultt.put(result.getInt(1), li);
			}
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		return resultt;
	}
	
	public void insertEm2(final Integer secID, final List<Integer> primIDs) {
		try {
			PreparedStatement ps = conn.prepareStatement( "INSERT INTO \"Sekundaermodelle_Primaermodelle\"(\"GeschaetztesPrimaermodell\", \"GeschaetztesSekundaermodell\")VALUES(?,?)");
			for (Integer id : primIDs) {
				if (id != null && id >= 0) {
					ps.setInt( 1, id);
					ps.setInt( 2, secID);
					ps.executeUpdate();			
				}
			}
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
	}
	public Integer insertEm(final ParametricModel pm) { // , HashMap<String, String> hm
		Integer estModelId = null;

		Double rms = pm.getRms();
		Double r2 = pm.getRsquared();
		//if (!Double.isNaN(rms)) {
		/*
			LinkedList<String> paramNameSet = new LinkedList<String>();
			paramNameSet.addAll(pm.getParamNameSet());
			int numParams = paramNameSet.size();
			double[] valueSet = new double[numParams];
			double[] paramErrSet = new double[numParams];
			for (int i = 0; i < numParams; i++) {
				valueSet[ i ] = pm.getParamValue( paramNameSet.get(i) );
				paramErrSet[ i ] = pm.getParamError(paramNameSet.get(i));
			}		
			*/
			estModelId = pm.getEstModelId();
			int condId = pm.getCondId();
			int modelId = pm.getModelId();
			
			HashMap<String, Integer> hmi = new HashMap<String, Integer>(); 
			int responseId = queryParamId(modelId, pm.getDepXml().getOrigName(), PARAMTYPE_DEP);
			if (!pm.getDepXml().getOrigName().equals(pm.getDepXml().getName())) hmi.put(pm.getDepXml().getName(), responseId);
			/*
			if (hm != null && hm.get(pm.getDepVar()) != null) {
				hmi.put(hm.get(pm.getDepVar()), responseId);
			}
			*/
			if (responseId < 0) {
				System.err.println("responseId < 0..." + pm.getDepVar() + "\t" + pm.getDepVar());
			}

			if (isObjectPresent(REL_ESTMODEL, estModelId)) {
				updateEstModel(estModelId, condId, modelId, rms, r2, pm.getAic(), pm.getBic(), responseId);
			} else {
				estModelId = insertEstModel(condId, modelId, rms, r2, pm.getAic(), pm.getBic(), responseId);
				pm.setEstModelId(estModelId);
			}
			
			deleteFrom("GeschaetzteParameter", "GeschaetztesModell", estModelId);
			for (PmmXmlElementConvertable el : pm.getParameter().getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					int paramId = queryParamId(modelId, px.getOrigName(), PARAMTYPE_PARAM);
					if (paramId < 0) {
						System.err.println("paramId < 0... " + px.getOrigName());
					}
					if (!px.getOrigName().equals(px.getName())) hmi.put(px.getName(), paramId);
					insertEstParam(estModelId, paramId, px.getValue(), px.getError());
				}
			}
			/*
			for (int i = 0; i < numParams; i++ ) {			
				int paramId = queryParamId(modelId, paramNameSet.get(i), PARAMTYPE_PARAM);
				if (paramId < 0) {
					System.err.println("paramId < 0... " + paramNameSet.get(i) + "\t" + paramNameSet.get(i));
				}
				if (hm != null && hm.get(paramNameSet.get(i)) != null) {
					hmi.put(hm.get(paramNameSet.get(i)), paramId);
				}
				insertEstParam(estModelId, paramId, valueSet[i], paramErrSet[i]);
			}
			*/
			insertModLit(estModelId, pm.getEstModelLit(), true, pm);
			
			deleteFrom("GueltigkeitsBereiche", "GeschaetztesModell", estModelId);
			for (PmmXmlElementConvertable el : pm.getIndependent().getElementSet()) {
				if (el instanceof IndepXml) {
					IndepXml ix = (IndepXml) el;
					int indepId = queryParamId(modelId, ix.getOrigName(), PARAMTYPE_INDEP);
					if (indepId >= 0) {
						insertMinMaxIndep(estModelId, indepId, ix.getMin(), ix.getMax());	
						if (!ix.getOrigName().equals(ix.getName())) hmi.put(ix.getName(), indepId);
					}
					else {
						System.err.println("insertEm:\t" + ix.getOrigName() + "\t" + modelId);
					}
				}
			}
			/*
			for (String name : pm.getIndepVarSet()) {
				int indepId = queryParamId(modelId, name, PARAMTYPE_INDEP);
				if (indepId >= 0) {
					insertMinMaxIndep(estModelId, indepId, pm.getIndepMin(name), pm.getIndepMax(name));					
					if (hm != null && hm.get(name) != null) {
						hmi.put(hm.get(name), indepId);
					}
				}
				else {
					System.err.println("insertEm:\t" + name + "\t" + modelId + "\t" + name);
				}
			}
			*/
			// insert mapping of parameters and variables of this estimation
			deleteFrom("VarParMaps", "GeschaetztesModell", estModelId);			
			for (String newName : hmi.keySet()) {
				insertVarParMaps(estModelId, hmi.get(newName), newName);					
			}			
			
		return estModelId;
	}
	private void insertVarParMaps(final int estModelId, final int paramId, final String newVarPar) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"VarParMaps\"(\"GeschaetztesModell\", \"VarPar\", \"VarParMap\") VALUES (?,?,?)");
			ps.setInt(1, estModelId);
			ps.setInt(2, paramId);
			if (newVarPar == null) {
				ps.setNull(3, java.sql.Types.VARCHAR);
			} else {
				ps.setString(3, newVarPar);
			}
			ps.executeUpdate();			
			ps.close();
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}		
	}
	private void insertMinMaxIndep(final int estModelId, final int paramId, final Double min, final Double max) {
		try {
			PreparedStatement ps = conn.prepareStatement( "INSERT INTO \"GueltigkeitsBereiche\"(\"GeschaetztesModell\", \"Parameter\", \"Gueltig_von\", \"Gueltig_bis\")VALUES(?,?,?,?)");
			ps.setInt( 1, estModelId);
			ps.setInt( 2, paramId);
			if (min == null) {
				ps.setNull(3, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble( 3, min);
			}
			if (max == null) {
				ps.setNull(4, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble( 4, max);
			}
			ps.executeUpdate();			
			ps.close();
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}		
	}
	
	private Integer insertCondition(Integer condId, final Integer tempId, final Integer phId, final Integer awId, final String organism,
			final String environment, final String combaseId,
			Integer matrixId, Integer agentId, final String agentDetail, final String matrixDetail, PmmXmlDoc misc, final String comment,
			PmmXmlDoc lit, PmmTimeSeries ts) {
			
			boolean doUpdate = isObjectPresent("Versuchsbedingungen", condId);
			Integer cdai = combaseDataAlreadyIn(combaseId);
			if (!doUpdate && cdai != null) {
				condId = cdai;//return null;
				doUpdate = true;
			}
			Integer resultID = null;
			PreparedStatement ps;

			try {
				if (agentId == null || agentId < 0) {
					agentId = queryAgentId( organism == null ? agentDetail : organism );
				}
				if (matrixId == null || matrixId < 0) {
					matrixId = queryMatrixId( environment == null ? matrixDetail : environment );
				}
				
				if (doUpdate) {
					ps = conn.prepareStatement( "UPDATE \"Versuchsbedingungen\" SET \""+ATT_TEMPERATURE+"\"=?, \""+ATT_PH+"\"=?, \""+ATT_AW+"\"=?, \""+ATT_AGENTID+"\"=?, \"AgensDetail\"=?, \""+ATT_MATRIXID+"\"=?, \"MatrixDetail\"=?, \"b_f_details_CB\"=?, \"Kommentar\"=?, \"Referenz\"=? WHERE \"ID\"=?" );
				} else {
					ps = conn.prepareStatement( "INSERT INTO \"Versuchsbedingungen\" ( \""+ATT_TEMPERATURE+"\", \""+ATT_PH+"\", \""+ATT_AW+"\", \""+ATT_AGENTID+"\", \"AgensDetail\", \""+ATT_MATRIXID+"\", \"MatrixDetail\", \"b_f_details_CB\", \"Kommentar\", \"Referenz\" ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS );
				}
				
				if( tempId >= 0 ) {
					ps.setInt( 1, tempId );
				} else {
					ps.setNull( 1, Types.INTEGER );
				}
				
				if( phId >= 0 ) {
					ps.setInt( 2, phId );
				} else {
					ps.setNull( 2, Types.INTEGER );
				}
				
				if( awId >= 0) {
					ps.setInt( 3, awId );
				} else {
					ps.setNull( 3, Types.INTEGER );
				}
				
				if (agentId == null || agentId <= 0) {
					ps.setNull( 4, Types.INTEGER );
				} else {
					ps.setInt(4, agentId );
					try {ts.setAgentId(agentId);} catch (PmmException e) {e.printStackTrace();}
				}
				if( agentDetail == null) {
					ps.setNull( 5, Types.VARCHAR );
				} else {
					ps.setString( 5, agentDetail );
				}
				if (matrixId == null || matrixId <= 0) {
					ps.setNull( 6, Types.INTEGER );
				} else {
					ps.setInt(6, matrixId );
					try {ts.setMatrixId(matrixId);} catch (PmmException e) {e.printStackTrace();}
				}
				if( matrixDetail == null) {
					ps.setNull( 7, Types.VARCHAR );
				} else {
					ps.setString( 7, matrixDetail);
				}
				/*
				if( misc == null ) {
					ps.setNull( 8, Types.VARCHAR );
				} else {
					ps.setString( 8, misc );
				}
				*/
				ps.setNull(8, Types.VARCHAR);
				if( comment == null ) {
					ps.setNull( 9, Types.VARCHAR );
				} else {
					ps.setString( 9, comment );
				}
				List<PmmXmlElementConvertable> l = lit.getElementSet();
				if (l.size() > 0) {
					LiteratureItem li = (LiteratureItem) l.get(0);
					ps.setInt(10, li.getId());
				}
				else {
					ps.setNull(10, Types.INTEGER);					
				}
				if (doUpdate) {
					ps.setInt( 11, condId );
					
					ps.executeUpdate();
					resultID = condId;
				}
				else {
					if( ps.executeUpdate() > 0 ) {
						ResultSet result = ps.getGeneratedKeys();
						result.next();
						resultID = result.getInt( 1 );
						
						result.close();
					}
					
				}
				ps.close();
			}
			catch( SQLException ex ) { ex.printStackTrace(); }
			
			if( cdai == null && resultID != null && combaseId != null && !combaseId.isEmpty()) {
				insertCondComb(resultID, combaseId);
			}
			ts.setWarning(handleConditions(resultID, misc, ts));

			return resultID;
		}
	private String handleConditions(final Integer condId, final PmmXmlDoc misc, PmmTimeSeries ts) {
		String result = "";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("DELETE FROM \"Versuchsbedingungen_Sonstiges\" WHERE \"Versuchsbedingungen\" = " + condId);
			ps.executeUpdate();
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (condId != null && condId >= 0 && misc != null) {
        	for (PmmXmlElementConvertable el : misc.getElementSet()) {
        		if (el instanceof MiscXml) {		
        			MiscXml mx = (MiscXml) el;
    				Integer paramID = getID("SonstigeParameter", "Beschreibung", mx.getDescription().toLowerCase()); // Parameter Beschreibung
    				if (paramID != null) {
    					//System.err.println("handleConditions:\t" + after + "\t" + dbl + "\t" + unit + "\t" + paramID + "\t" + (condIDs == null ? condIDs : condIDs.get(i)));
    					try {
    						ps = conn.prepareStatement("INSERT INTO \"Versuchsbedingungen_Sonstiges\" (\"Versuchsbedingungen\", \"SonstigeParameter\", \"Wert\", \"Einheit\", \"Ja_Nein\") VALUES (?,?,?,?,?)");
    						ps.setInt(1, condId);
    						ps.setInt(2, paramID);
    						if (mx.getValue() == null || Double.isNaN(mx.getValue())) {
    							ps.setNull(3, java.sql.Types.DOUBLE);
    							ps.setNull(4, java.sql.Types.INTEGER);
    							ps.setBoolean(5, true);
    						}
    						else {
    							int did = insertDouble(mx.getValue());
    							ps.setDouble(3, did);							
    							Integer eid = getID("Einheiten", "Einheit", mx.getUnit());
    							if (eid == null) {
    								ps.setNull(4, java.sql.Types.INTEGER);
    							} else {
    								ps.setInt(4, eid);
    							}
    							ps.setBoolean(5, false);
    						}
    						ps.executeUpdate();
    						//try {ts.addValue(TimeSeriesSchema.ATT_MISCID, paramID);} catch (PmmException e) {e.printStackTrace();}
    					}
    					catch (Exception e) {e.printStackTrace();}
    				}
    				else {
    					//System.err.println("handleConditions, paramID not known:\t" + val + "\t" + after);
    					result += "Insert of Misc failed:\t" + mx.getDescription() + "\n";
    				}
        		}
        	}
		}
		return result;
	}
		
	private void deleteFrom(String tablename, String fieldname, int id) {
		try {			
			PreparedStatement ps = conn.prepareStatement( "DELETE FROM \""+tablename+"\" WHERE \""+fieldname+"\"="+id );			
			ps.executeUpdate();
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
	}
		
	public Integer insertTs(final PmmTimeSeries ts) throws PmmException {		
		Integer condId = ts.getCondId();
		Double ph = ts.getPh();
		Double temp = ts.getTemperature();
		Double aw = ts.getWaterActivity();
		String organism = ts.getAgentName();
		String environment = ts.getMatrixName();
		String combaseId = ts.getCombaseId();
		Integer matrixId = ts.getMatrixId();
		Integer agentId = ts.getAgentId();
		String agentDetail = ts.getAgentDetail();
		String matrixDetail = ts.getMatrixDetail();
		String comment = ts.getComment();
		PmmXmlDoc misc = ts.getMisc();
		PmmXmlDoc lit = ts.getLiterature();

		List<Double> time = ts.getDoubleList(TimeSeriesSchema.ATT_TIME);
		List<Double> logc = ts.getDoubleList(TimeSeriesSchema.ATT_LOGC);
		
		
		int tempId = insertDouble( temp );
		int phId = insertDouble( ph );
		int awId = insertDouble( aw );

		condId = insertCondition(condId, tempId, phId, awId, organism, environment, combaseId,
				matrixId, agentId, agentDetail, matrixDetail, misc, comment,
				lit, ts);
			
		ts.setCondId(condId);
		if( condId == null || condId < 0 ) {
			return null;
		}
		
		if (time != null && logc != null) {
			// delete old data
			deleteFrom("Messwerte", "Versuchsbedingungen", condId);
			
			for (int i = 0; i < time.size(); i++) {				
				int timeId = insertDouble( time.get(i) );				
				int lognId = insertDouble( logc.get(i) );				
				insertData(condId, timeId, lognId);
			}	
		}
		return condId;
	}
	
	public Integer insertM(final ParametricModel m) {		
		int modelId = m.getModelId();

		if (isObjectPresent("Modellkatalog", modelId)) {
			//Date date = new Date( System.currentTimeMillis() );		
			
			try {
				PreparedStatement ps = conn.prepareStatement( "UPDATE \"Modellkatalog\" SET \"Name\"=?, \"Level\"=?, \"Formel\"=? WHERE \"ID\"=?" );
				ps.setString(1, m.getModelName());
				ps.setInt(2, m.getLevel());
				//ps.setDate( 3, date );
				ps.setString(3, m.getFormula());
				ps.setInt(4, modelId);

				ps.executeUpdate();
				ps.close();
			}
			catch( SQLException ex ) { ex.printStackTrace(); }		
		}
		else {
			Date date = new Date( System.currentTimeMillis() );		
			
			try {				
				PreparedStatement ps = conn.prepareStatement( "INSERT INTO \"Modellkatalog\" ( \"Name\", \"Level\", \"Eingabedatum\", \"Formel\", \"Notation\", \"Klasse\" ) VALUES( ?, ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS );
				ps.setString( 1, m.getModelName() + "_" + (-modelId) );
				ps.setInt( 2, m.getLevel() );
				ps.setDate( 3, date );
				ps.setString( 4, m.getFormula() );
				ps.setString( 5, m.getModelName().toLowerCase().replaceAll( "\\s", "_") );
				ps.setInt(6, 0); // erstmal Unknown!
				
				modelId = -1;
				if( ps.executeUpdate() > 0) {
					ResultSet result = ps.getGeneratedKeys();
					result.next();
					modelId = result.getInt( 1 );					
					m.setModelId(modelId);
					result.close();
				}				
				ps.close();
			}
			catch(SQLException ex) {ex.printStackTrace();}

			if( modelId < 0 ) {
				return null;
			}			
		}
		
		// insert parameter set
		LinkedList<Integer> paramIdSet = new LinkedList<Integer>();
		
		// insert dependent variable
		int paramId = insertParam(modelId, m.getDepXml().getOrigName(), PARAMTYPE_DEP, null, null);
		paramIdSet.add(paramId);
		
		// insert independent variable set
		for (PmmXmlElementConvertable el : m.getIndependent().getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				paramId = insertParam(modelId, ix.getOrigName(), PARAMTYPE_INDEP, ix.getMin(), ix.getMax());
				paramIdSet.add(paramId);
			}
		}
		/*
		for (String name : indepVar) {
			paramId = insertParam(modelId, name, PARAMTYPE_INDEP, m.getParamMin(name), m.getParamMax(name));
			paramIdSet.add(paramId);
		}
		*/
		// insert parameters
		for (PmmXmlElementConvertable el : m.getParameter().getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				paramId = insertParam(modelId, px.getOrigName(), PARAMTYPE_PARAM, px.getMin(), px.getMax());
				paramIdSet.add(paramId);
			}
		}
		/*
		for (String name : paramNameSet) {			
			paramId = insertParam(modelId, name, PARAMTYPE_PARAM, m.getParamMin(name), m.getParamMax(name));
			paramIdSet.add(paramId);
		}
		*/
		insertModLit(modelId, m.getModelLit(), false, m);
		
		// delete dangling parameters
		// deleteParamNotIn kann man eigentlich nicht machen!!! Sonst sind irgendwann die Response-Verknüpfungen weg....
		// andererseits hat man das Problem, dass sich die Parameter sammeln...
		deleteParamNotIn( modelId, paramIdSet );

		return modelId;
	}
	private void insertModLit(final int modelId, PmmXmlDoc modelLit, final boolean estimatedModels, final ParametricModel m) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM " + (estimatedModels ? "\"GeschaetztesModell_Referenz\" WHERE \"GeschaetztesModell\"" : "\"Modell_Referenz\"WHERE \"Modell\"") + " = " + modelId);
			ps.executeUpdate();
			ps.close();
			PreparedStatement psm = conn.prepareStatement("INSERT INTO \"Modell_Referenz\"(\"Modell\", \"Literatur\") VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			PreparedStatement psgm = conn.prepareStatement("INSERT INTO \"GeschaetztesModell_Referenz\"(\"GeschaetztesModell\", \"Literatur\") VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			for (PmmXmlElementConvertable el : modelLit.getElementSet()) {
				if (el instanceof LiteratureItem) {
					LiteratureItem li = (LiteratureItem) el;
					if (li.getId() >= 0) { // neue Literatur evtl. später hinzufügen, aber Achtung: DB Gleichheit checken!!!
						// Ausserdem: hier die neu insertierten IDs updaten im ParametricModel!!!!!
						if (!estimatedModels) {
							psm.setInt(1, modelId);
							psm.setInt(2, li.getId());
							psm.executeUpdate();
							//m.addModelLit(lid); nach einem realiseirten INSERT: überprüfen, ob korrekt
						}
						else {
							psgm.setInt(1, modelId);
							psgm.setInt(2, li.getId());
							psgm.executeUpdate();
							//m.addEstModelLit(lid); nach einem realiseirten INSERT: überprüfen, ob korrekt
						}
					}
				}
			}
			psm.close();
			psgm.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
	}
	private void insertCondComb(final Integer resultID, final String combaseId) {
		try {
			PreparedStatement ps = conn.prepareStatement( "INSERT INTO \"ImportedCombaseData\"(\"CombaseID\", \"Versuchsbedingung\")VALUES(?,?)" );
			ps.setString( 1, combaseId );
			ps.setInt( 2, resultID);
			ps.executeUpdate();			
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
	}
	private Integer combaseDataAlreadyIn(final String combaseId) {
		Integer res = null;
		try {
			ResultSet result = getResultSet("SELECT \"Versuchsbedingung\" FROM \"ImportedCombaseData\" WHERE \"CombaseID\" LIKE '" + combaseId + "' AND \"Versuchsbedingung\" IS NOT NULL", false);
			
			if(result != null && result.first()) {
				res = result.getInt(1);
				result.close();
			}
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		return res;
	}
	
	private int insertDouble( final Double value ) {
		
		int doubleId;
		PreparedStatement psInsertDouble;
		ResultSet result;
		
		doubleId = -1;
		try {
			psInsertDouble = conn.prepareStatement( "INSERT INTO \""+REL_DOUBLE+"\"( \""+ATT_VALUE+"\", \""+ATT_VALUETYPE+"\" )VALUES( ?, 1 )", Statement.RETURN_GENERATED_KEYS );
			if( value == null || Double.isNaN( value ) || Double.isInfinite( value ) ) {
				// psInsertDouble.setNull( 1, Types.DOUBLE );
				return -1;
			} else {
				psInsertDouble.setDouble( 1, value );
			}
			
			if( psInsertDouble.executeUpdate() < 1 ) {
				return doubleId;
			}
			
			result = psInsertDouble.getGeneratedKeys();
			result.next();
			doubleId = result.getInt( 1 );
			
			result.close();
			psInsertDouble.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		
		return doubleId;
	}
	
	private Integer queryAgentId( final String agentName ) {
		
		Integer agentId = null;
		PreparedStatement psQueryAgentId;
		ResultSet result;
		
		try {
			psQueryAgentId = conn.prepareStatement( "SELECT \"ID\" FROM \""+REL_AGENT+"\" WHERE \""+ATT_AGENTNAME+"\" LIKE ? OR \""+ATT_NAMESHORT+"\" LIKE ?" );
			psQueryAgentId.setString( 1, agentName );
			psQueryAgentId.setString( 2, agentName );
			
			result = psQueryAgentId.executeQuery();
			
			if( !result.next() ) {
				return agentId;
			}
			
			agentId = result.getInt( 1 );
						
			result.close();
			psQueryAgentId.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		
		return agentId;
	}
	
	
	private Integer queryMatrixId( final String matrixName ) {
		
		Integer matrixId = null;
		
		try {
			PreparedStatement ps = conn.prepareStatement( "SELECT \"ID\" FROM \""+REL_MATRIX+"\" WHERE \""+ATT_MATRIXNAME+"\" LIKE ?" );
			ps.setString( 1, matrixName );
			
			ResultSet result = ps.executeQuery();
			
			if( !result.next() ) {
				return matrixId;
			}
			
			matrixId = result.getInt( 1 );
						
			result.close();
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		
		return matrixId;
	}
	
	private ResultSet getResultSet(final String sql, final boolean suppressWarnings) {
		    ResultSet ergebnis = null;
		    try {
		      Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		      ergebnis = anfrage.executeQuery(sql);
		      ergebnis.first();
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		    return ergebnis;
	}
	private Integer getID(final String tablename, final String feldname, final String feldVal) {
		  Integer result = null;
		  String sql = "SELECT \"ID\" FROM \"" + tablename + "\" WHERE \"" + feldname + "\"";
		  if (feldVal == null) {
			sql += " IS NULL";
		} else {
			sql += " = '" + feldVal.replace("'", "''") + "'";
		}
			ResultSet rs = getResultSet(sql, true);
			try {
				if (rs != null && rs.last()) {
					result = rs.getInt(1);
				}
			}
			catch (Exception e) {e.printStackTrace();}
			return result;
	}

	private void insertData( final int condId, final int timeId, final int lognId ) {
		
		PreparedStatement ps;
		
		try {
			
			ps = conn.prepareStatement( "INSERT INTO \""+REL_DATA+"\"( \""+REL_CONDITION+"\", \""+ATT_TIME+"\", \""+ATT_TIMEUNIT+"\", \""+ATT_LOG10N+"\", \""+ATT_LOG10NUNIT+"\" )VALUES( ?, ?, 'Stunde', ?, '1' )" );
			ps.setInt( 1, condId );
			if (timeId >= 0) {
				ps.setInt(2, timeId);
			}
			else {
				ps.setNull(2, Types.INTEGER);
			}
			if (lognId >= 0) {
				ps.setInt(3, lognId);
			}
			else {
				ps.setNull(3, Types.INTEGER);
			}
			
			ps.executeUpdate();
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
	}
	
	private int insertParam(final int modelId, final String paramName, final int paramType, final Double min, final Double max) {
		
		PreparedStatement ps;
				
		int id = -1;
		try {
			int paramId = queryParamId(modelId, paramName, paramType);
			if( paramId <= 0 ) {
				ps = conn.prepareStatement( "INSERT INTO \"ModellkatalogParameter\" ( \"Modell\", \"Parametername\", \"Parametertyp\", \"min\",\"max\" ) VALUES( ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS );				
			}
			else {
				ps = conn.prepareStatement( "UPDATE \"ModellkatalogParameter\" SET \"Modell\" = ?, \"Parametername\" = ?, \"Parametertyp\" = ?, \"min\"= ?, \"max\" = ? WHERE \"ID\"=" + paramId, Statement.RETURN_GENERATED_KEYS );								
			}
				
			ps.setInt( 1, modelId );
			ps.setString( 2, paramName );
			ps.setInt( 3, paramType );
			if (min == null) {
				ps.setNull(4, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(4, min);
			}
			if (max == null) {
				ps.setNull(5, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(5, max);
			}
			
			if( ps.executeUpdate() < 1 ) {
				return id;
			}
			if (paramId > 0) {
				return paramId;
			}
			
			ResultSet result = ps.getGeneratedKeys();
			
			if( !result.next() ) {
				return id;
			}

			id = result.getInt(1);
			
			result.close();
			ps.close();

		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		
		return id;
	}
	
	private boolean isObjectPresent( final String tablename, final int id ) {
		
		if( id <= 0 ) {
			return false;
		}
		
		int cnt = 0;
		try {
			PreparedStatement ps = conn.prepareStatement( "SELECT COUNT( * )FROM \""+tablename+"\" WHERE \"ID\"=?" );
			ps.setInt( 1, id );
			
			ResultSet result = ps.executeQuery();
			
			result.next();
				
			
			cnt = result.getInt( 1 );
						
			result.close();
			ps.close();
		}
		catch( SQLException ex ) { ex.printStackTrace(); }
		
		if( cnt > 0 ) {
			return true;
		}
		
		return false;
	}
	
	private void insertEstParam( final int estModelId, final int paramId, final double value, final double paramErr ) {
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement( "INSERT INTO \"GeschaetzteParameter\" ( \"GeschaetztesModell\", \"Parameter\", \"Wert\", \"StandardError\" ) VALUES( ?, ?, ?, ? )" );
			ps.setInt(1, estModelId);
			ps.setInt(2, paramId);
			if(Double.isNaN(value)) {
				ps.setNull(3, Types.DOUBLE);
			} else {
				ps.setDouble(3, value);
			}
			if(Double.isNaN(paramErr)) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, paramErr);
			}
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException ex) {
			System.out.println(ps);
			ex.printStackTrace();
		}
	}
	
	private int queryParamId( final int modelId, final String paramName, final int paramType ) {
		int ret = -1;
		try {			
			PreparedStatement ps = conn.prepareStatement( "SELECT \"ID\" FROM \""+REL_PARAM+"\"  WHERE \""+ATT_MODELID+"\"=? AND \""+ATT_PARAMNAME+"\" LIKE ? AND \""+ATT_PARAMTYPE+"\"=?" );
			ps.setInt( 1, modelId );
			ps.setString( 2, paramName );
			ps.setInt( 3, paramType );
			
			//System.out.println( ps );
			
			ResultSet result = ps.executeQuery();
			if( !result.next() ) {
				return ret;
			}
			ret = result.getInt( 1 );
			
			result.close();
			ps.close();
		}
		catch( SQLException ex ) {ex.printStackTrace();}
		
		return ret;
	}
	
	private void updateEstModel( final int estModelId, final int condId, final int modelId,
		final double rms, final double rsquared, final double aic, final double bic, final int responseId ) {
		try {
			
			PreparedStatement ps = conn.prepareStatement("UPDATE \"GeschaetzteModelle\" SET \"Versuchsbedingung\"=?, \"Modell\"=?, \"RMS\"=?, \"Rsquared\"=?, \"AIC\"=?, \"BIC\"=?, \"Response\"=? WHERE \"ID\"=?");
			if (condId > 0) {
				ps.setInt(1, condId);
			} else {
				ps.setNull(1, Types.INTEGER);
			}
			ps.setInt(2, modelId);
			if(Double.isNaN(rms)) {
				ps.setNull(3, Types.DOUBLE);
			} else {
				ps.setDouble(3, rms);
			}
			if( Double.isNaN( rsquared ) ) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, rsquared);
			}
			if (Double.isNaN(aic)) {
				ps.setNull(5, Types.DOUBLE);
			} else {
				ps.setDouble(5, aic);
			}
			if (Double.isNaN(bic)) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, bic);
			}
			if( responseId > 0 ) {
				ps.setInt(7, responseId);
			} else {
				ps.setNull(7, Types.INTEGER);
			}
			ps.setInt(8, estModelId);
			
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException ex) {ex.printStackTrace();}
	}
	
	private int insertEstModel( final int condId, final int modelId, final double rms,
		final double rsquared, final double aic, final double bic, final int responseId ) {		
		int ret = -1;
		try {
			
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"GeschaetzteModelle\" ( \"Versuchsbedingung\", \"Modell\", \"RMS\", \"Rsquared\", \"AIC\", \"BIC\", \"Response\" ) VALUES( ?, ?, ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);
			if( condId > 0 ) {
				ps.setInt( 1, condId );
			} else {
				ps.setNull( 1, Types.INTEGER );
			}
			ps.setInt( 2, modelId );
			if( Double.isNaN( rms ) ) {
				ps.setNull( 3, Types.DOUBLE );
			} else {
				ps.setDouble( 3, rms );
			}
			if( Double.isNaN( rsquared ) ) {
				ps.setNull( 4, Types.DOUBLE );
			} else {
				ps.setDouble( 4, rsquared );
			}
			if (Double.isNaN(aic)) {
				ps.setNull(5, Types.DOUBLE);
			} else {
				ps.setDouble(5, aic);
			}
			if (Double.isNaN(bic)) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, bic);
			}
			if( responseId > 0 ) {
				ps.setInt( 7, responseId );
			} else {
				ps.setNull( 7, Types.INTEGER );
			}

			ps.executeUpdate();
			ResultSet result = ps.getGeneratedKeys();
			result.next();			
			ret = result.getInt( 1 );
			
			result.close();
			ps.close();
			
		}
		catch( SQLException ex ) {
			ex.printStackTrace();
		}
		
		return ret;
	}
		
	private void deleteParamNotIn( final int modelId, final LinkedList<Integer> paramIdSet ) {
		
		PreparedStatement ps;
		String q, r;
		
		
		r = "( ";
		for( Integer i : paramIdSet ) {
			
			if( !r.equals( "( " ) ) {
				r +=", ";
			}
			
			r += i;
		}
		r += ")";
		
		q = "DELETE FROM \""+REL_PARAM+"\" WHERE \"Modell\"=" + modelId + " AND \"ID\" NOT IN " + r;
		
		try {
			
			ps = conn.prepareStatement( q );
			ps.executeUpdate();
			ps.close();
		}
		catch( SQLException ex ) {
			// ex.printStackTrace();
		}
	}
}
