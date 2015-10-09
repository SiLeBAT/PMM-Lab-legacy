package de.bund.bfr.knime.pmm.openfsmr;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class OpenFSMRConverterNodeFactory extends NodeFactory<OpenFSMRConverterNodeModel> {
	
	@Override
	public OpenFSMRConverterNodeModel createNodeModel() {
		return new OpenFSMRConverterNodeModel();
	}
	
	@Override
	public int getNrNodeViews() {
		return 0;
	}
	
	@Override
	public NodeView<OpenFSMRConverterNodeModel> createNodeView(final int viewIndex, final OpenFSMRConverterNodeModel nodeModel) {
        return new OpenFSMRConverterNodeView(nodeModel);
    }
	
	@Override
	public boolean hasDialog() {
		return true;
	}
	
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new OpenFSMRConverterNodeDialog();
	}
}
