package de.bund.bfr.knime.pmm.secondarypredictorview;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SecondaryPredictorView" Node.
 * 
 *
 * @author Christian Thoens
 */
public class SecondaryPredictorViewNodeFactory 
        extends NodeFactory<SecondaryPredictorViewNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SecondaryPredictorViewNodeModel createNodeModel() {
        return new SecondaryPredictorViewNodeModel();
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
    public NodeView<SecondaryPredictorViewNodeModel> createNodeView(final int viewIndex,
            final SecondaryPredictorViewNodeModel nodeModel) {
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
        return new SecondaryPredictorViewNodeDialog();
    }

}

