package de.bund.bfr.knime.pmm.pmf2fsk;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;

import org.knime.core.node.NodeFactory;




public class PMF2FSKNodeFactory 
		extends NodeFactory<PMF2FSKNodeModel> {

	
	@Override
	public  PMF2FSKNodeModel createNodeModel() {
		return new PMF2FSKNodeModel(false);
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<PMF2FSKNodeModel> createNodeView(int viewIndex, PMF2FSKNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new PMF2FSKNodeDialog(false);
	}
	
}
