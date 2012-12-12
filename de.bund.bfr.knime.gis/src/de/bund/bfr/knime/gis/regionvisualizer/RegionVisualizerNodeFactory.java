package de.bund.bfr.knime.gis.regionvisualizer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RegionVisualizer" Node.
 * 
 *
 * @author Christian Thoens
 */
public class RegionVisualizerNodeFactory 
        extends NodeFactory<RegionVisualizerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RegionVisualizerNodeModel createNodeModel() {
        return new RegionVisualizerNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<RegionVisualizerNodeModel> createNodeView(final int viewIndex,
            final RegionVisualizerNodeModel nodeModel) {
        return new RegionVisualizerNodeView(nodeModel);
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
        return new RegionVisualizerNodeDialog();
    }

}

