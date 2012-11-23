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
package de.bund.bfr.knime.pcml;

import lombok.Getter;

/**
 * A wrapper class for a agent.
 * @author Heiko Hofer
 *
 */
public class Agent {
	@Getter private String name;
	@Getter private int id;
	@Getter private double quantity;
	
	/**
	 * Create a agent.
	 * @param name the name of the agent
	 */
	public Agent(final String name) {
		this(name, -1, 1.0);
	}
	
	/**
	 * Creates a new agent.
	 * @param name the agent
	 * @param id the id of the agent in the database use -1 if matrix does
	 * not have an id. 
	 * @param quantity the quantity of this matrix.
	 */
	public Agent(final String name, final int id, final double quantity) {
		this.name = name;
		this.id = id;
		this.quantity = quantity;
	}	
}
