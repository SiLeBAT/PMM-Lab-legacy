package de.bund.bfr.knime.feeddistributionestimator;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FeedDistributionEstimator" Node.
 * 
 *
 * @author Christian Thoens
 */
public class FeedDistributionEstimatorNodeFactory 
        extends NodeFactory<FeedDistributionEstimatorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FeedDistributionEstimatorNodeModel createNodeModel() {
        return new FeedDistributionEstimatorNodeModel();
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
    public NodeView<FeedDistributionEstimatorNodeModel> createNodeView(final int viewIndex,
            final FeedDistributionEstimatorNodeModel nodeModel) {
        return new FeedDistributionEstimatorNodeView(nodeModel);
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
        return new FeedDistributionEstimatorNodeDialog();
    }

}

