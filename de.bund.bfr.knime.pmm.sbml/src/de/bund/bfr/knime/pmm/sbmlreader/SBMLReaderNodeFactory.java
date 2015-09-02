package de.bund.bfr.knime.pmm.sbmlreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class SBMLReaderNodeFactory extends NodeFactory<SBMLReaderNodeModel> {
	
	@Override
	public SBMLReaderNodeModel createNodeModel() {
		return new SBMLReaderNodeModel();
	}
	
	@Override
	public int getNrNodeViews() {
		return 0;
	}
	
	@Override
	public NodeView<SBMLReaderNodeModel> createNodeView(final int viewIndex, final SBMLReaderNodeModel nodeModel) {
        return new SBMLReaderNodeView(nodeModel);
    }
	
	@Override
	public boolean hasDialog() {
		return true;
	}
	
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new SBMLReaderNodeDialog();
	}
}
