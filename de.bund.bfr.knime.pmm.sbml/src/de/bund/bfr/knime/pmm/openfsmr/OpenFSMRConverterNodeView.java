package de.bund.bfr.knime.pmm.openfsmr;

import org.knime.core.node.NodeView;

public class OpenFSMRConverterNodeView extends NodeView<OpenFSMRConverterNodeModel> {
	
	/**
	 * Creates a new view
	 * @param nodeModel The model (class: {@link OpenFSMRConverterNodeModel})
	 */
	protected OpenFSMRConverterNodeView(final OpenFSMRConverterNodeModel nodeModel) {
		super(nodeModel);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		OpenFSMRConverterNodeModel nodeModel = (OpenFSMRConverterNodeModel) getNodeModel();
		assert nodeModel != null;
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
