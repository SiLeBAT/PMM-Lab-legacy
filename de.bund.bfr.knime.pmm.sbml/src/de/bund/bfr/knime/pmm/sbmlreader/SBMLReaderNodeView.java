package de.bund.bfr.knime.pmm.sbmlreader;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SBMLReader" Node.
 * 
 * Author: Miguel de Alba Aparicio
 * Contact: malba@optimumquality.es
 */
public class SBMLReaderNodeView extends NodeView<SBMLReaderNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SBMLReaderNodeModel})
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
		// TODO retrieve the new model from your nodemodel and
		// update the view.
		SBMLReaderNodeModel nodeModel = (SBMLReaderNodeModel) getNodeModel();
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