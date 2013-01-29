package de.bund.bfr.knime.simplexlsreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SimpleXLSReader" Node.
 * 
 * 
 * @author Christian Thöns
 */
public class SimpleXLSReaderNodeFactory extends
		NodeFactory<SimpleXLSReaderNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimpleXLSReaderNodeModel createNodeModel() {
		return new SimpleXLSReaderNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<SimpleXLSReaderNodeModel> createNodeView(
			final int viewIndex, final SimpleXLSReaderNodeModel nodeModel) {
		return new SimpleXLSReaderNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new SimpleXLSReaderNodeDialog();
	}

}
