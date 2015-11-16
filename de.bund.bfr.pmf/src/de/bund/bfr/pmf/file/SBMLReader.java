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
package de.bund.bfr.pmf.file;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;

/**
 * @author Miguel Alba
 */
public class SBMLReader {
	
	private static final org.sbml.jsbml.xml.stax.SBMLReader sbmlReader = new org.sbml.jsbml.xml.stax.SBMLReader();
	
	private SBMLReader() {
	}
	
	public static SBMLDocument readSBMLFromStream(InputStream stream) throws XMLStreamException {
		return sbmlReader.readSBMLFromStream(stream);
	}
}
