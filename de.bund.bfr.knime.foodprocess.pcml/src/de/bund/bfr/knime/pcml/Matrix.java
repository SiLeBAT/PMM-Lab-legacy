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
 * A matrix is defined by either a string or a string with the corresponding 
 * id in a matrix database.
 * 
 * @author Heiko Hofer
 */
public class Matrix {
	@Getter private final String name;
	@Getter private final int id;
	@Getter private double percentage;
	
	/**
	 * Creates a new matrix.
	 * @param name the matrix 
	 */
	public Matrix(final String name) {
		this(name, -1, 1.0);
	}
	
	/**
	 * Creates a new matrix.
	 * @param name the matrix
	 * @param id the id of the matrix in the database use -1 if matrix does
	 * not have an id. 
	 * @param percentage the percentage of this matrix from the mixture of 
	 * matrices.
	 */
	public Matrix(final String name, final int id, final double percentage) {
		this.name = name;
		this.id = id;
		this.percentage = percentage;
	}
}
