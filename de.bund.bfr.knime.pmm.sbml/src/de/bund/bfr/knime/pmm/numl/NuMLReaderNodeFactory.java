package de.bund.bfr.knime.pmm.numl;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the NuMLReader node.
 * 
 * @author Miguel de Alba
 */
public class NuMLReaderNodeFactory extends NodeFactory<NuMLReaderNodeModel> {
	
	@Override
	public NuMLReaderNodeModel createNodeModel() {
		return new NuMLReaderNodeModel();
	}
	
	@Override
	public int getNrNodeViews() {
		return 0;
	}
	
	@Override
	public NodeView<NuMLReaderNodeModel> createNodeView(final int viewIndex, final NuMLReaderNodeModel nodeModel) {
		return new NuMLReaderNodeView(nodeModel);
	}
	
	@Override
	public boolean hasDialog() {
		return true;
	}
	
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new NuMLReaderNodeDialog();
	}
}
