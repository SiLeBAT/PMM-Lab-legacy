package de.bund.bfr.knime.pmm.numl;

import org.knime.core.node.NodeView;

public class NuMLReaderNodeView extends NodeView<NuMLReaderNodeModel> {

	/**
	 * Creates a new view
	 * @param nodeModel The model (class: {@link NuMLReaderNodeModel})
	 */
	protected NuMLReaderNodeView(final NuMLReaderNodeModel nodeModel) {
		super(nodeModel);
		// NuMLReaderNodeView has no components
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		NuMLReaderNodeModel nodeModel = (NuMLReaderNodeModel) getNodeModel();
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
