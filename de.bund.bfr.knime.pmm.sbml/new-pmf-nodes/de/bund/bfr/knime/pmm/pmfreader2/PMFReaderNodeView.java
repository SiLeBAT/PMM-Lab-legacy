package de.bund.bfr.knime.pmm.pmfreader2;

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