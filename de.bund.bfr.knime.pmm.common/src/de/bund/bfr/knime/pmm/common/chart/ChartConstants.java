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
package de.bund.bfr.knime.pmm.common.chart;

public interface ChartConstants {

	public static final String IS_FITTED = "Fitted";
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String WARNING = "Warning";

	public static final String NO_TRANSFORM = "";
	public static final String SQRT_TRANSFORM = "sqrt";
	public static final String LOG_TRANSFORM = "ln";
	public static final String LOG10_TRANSFORM = "log";
	public static final String EXP_TRANSFORM = "exp";
	public static final String EXP10_TRANSFORM = "10^";
	public static final String DIVX_TRANSFORM = "1/x";
	public static final String DIVX2_TRANSFORM = "1/x^2";

	public static final String[] TRANSFORMS = { NO_TRANSFORM, SQRT_TRANSFORM,
			LOG_TRANSFORM, LOG10_TRANSFORM, EXP_TRANSFORM, EXP10_TRANSFORM,
			DIVX_TRANSFORM, DIVX2_TRANSFORM };

}
