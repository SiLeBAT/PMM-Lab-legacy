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

import java.beans.PropertyChangeEvent;
import java.io.InputStream;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.TreeNodeRemovedEvent;

/**
 * @author Miguel Alba
 */
public class SBMLReader {
	
	private static final org.sbml.jsbml.xml.stax.SBMLReader sbmlReader = new org.sbml.jsbml.xml.stax.SBMLReader();
	
	private SBMLReader() {
	}
	
	public static SBMLDocument readSBMLFromStream(InputStream stream) throws XMLStreamException {
		return sbmlReader.readSBMLFromStream(stream, new NoLogging());
	}
}

/**
 * @author Christian Thoens
 */
class NoLogging implements TreeNodeChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent evt) { }

	@Override
	public void nodeRemoved(TreeNodeRemovedEvent event) { }

	@Override
	public void nodeAdded(TreeNode node) { }
}