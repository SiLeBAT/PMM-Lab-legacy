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
package de.bund.bfr.knime.pmm.sbmlreader;

import org.knime.core.node.NodeView;

public class SBMLReaderNodeView extends NodeView<SBMLReaderNodeModel> {
	
	/**
	 * Creates a new view
	 * @param nodeModel The model (class: {@link SBMLReaderNodeModel})
	 */
	protected SBMLReaderNodeView(final SBMLReaderNodeModel nodeModel) {
		super(nodeModel);
		// SBMLReaderNodeView has no components
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		SBMLReaderNodeModel nodeModel = (SBMLReaderNodeModel) getNodeModel();
		assert nodeModel != null;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
		// TODO things to do when closing the view
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		// TODO things to do when opening the view
	}
}
