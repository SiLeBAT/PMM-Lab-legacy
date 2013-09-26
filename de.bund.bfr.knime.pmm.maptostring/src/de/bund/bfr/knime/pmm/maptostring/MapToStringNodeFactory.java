package de.bund.bfr.knime.pmm.maptostring;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MapToString" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class MapToStringNodeFactory extends NodeFactory<MapToStringNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapToStringNodeModel createNodeModel() {
		return new MapToStringNodeModel();
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
	public NodeView<MapToStringNodeModel> createNodeView(final int viewIndex,
			final MapToStringNodeModel nodeModel) {
		return null;
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
		return new MapToStringNodeDialog();
	}

}
