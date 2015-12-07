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
package de.bund.bfr.knime.pmm.pmfreader;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SBMLReader" Node.
 * 
 * Author: Miguel de Alba Aparicio
 * Contact: malba@optimumquality.es
 */
public class PMFReaderNodeView extends NodeView<PMFReaderNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link PMFReaderNodeModel})
	 */
	protected PMFReaderNodeView(final PMFReaderNodeModel nodeModel) {
		super(nodeModel);
		// PMFReaderNodeView has no components
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		// TODO retrieve the new model from your nodemodel and
		// update the view.
		PMFReaderNodeModel nodeModel = (PMFReaderNodeModel) getNodeModel();
		assert nodeModel != null;
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
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