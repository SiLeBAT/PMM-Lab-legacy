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
