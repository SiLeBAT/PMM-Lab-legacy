package de.bund.bfr.knime.simplexlsreader;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SimpleXLSReader" Node.
 * 
 * 
 * @author Christian Thöns
 */
public class SimpleXLSReaderNodeView extends NodeView<SimpleXLSReaderNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SimpleXLSReaderNodeModel})
	 */
	protected SimpleXLSReaderNodeView(final SimpleXLSReaderNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
	}

}
