package de.bund.bfr.knime.gis.regiontoregionvisualizer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RegionToRegionVisualizer" Node.
 * 
 *
 * @author Christian Thoens
 */
public class RegionToRegionVisualizerNodeFactory 
        extends NodeFactory<RegionToRegionVisualizerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RegionToRegionVisualizerNodeModel createNodeModel() {
        return new RegionToRegionVisualizerNodeModel();
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
    public NodeView<RegionToRegionVisualizerNodeModel> createNodeView(final int viewIndex,
            final RegionToRegionVisualizerNodeModel nodeModel) {
        return new RegionToRegionVisualizerNodeView(nodeModel);
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
        return new RegionToRegionVisualizerNodeDialog();
    }

}

