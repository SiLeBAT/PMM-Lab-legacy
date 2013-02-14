package de.bund.bfr.knime.foodprocess.view;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "View" Node.
 * 
 *
 * @author Christian Thoens
 */
public class ViewNodeFactory 
        extends NodeFactory<ViewNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewNodeModel createNodeModel() {
        return new ViewNodeModel();
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
    public NodeView<ViewNodeModel> createNodeView(final int viewIndex,
            final ViewNodeModel nodeModel) {
        return new ViewNodeView(nodeModel);
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
        return new ViewNodeDialog();
    }

}

