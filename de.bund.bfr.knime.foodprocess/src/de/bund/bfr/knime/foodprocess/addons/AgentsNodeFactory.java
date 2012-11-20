package de.bund.bfr.knime.foodprocess.addons;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Ingredients" Node.
 * 
 *
 * @author BfR
 */
public class AgentsNodeFactory 
        extends NodeFactory<AddonNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AddonNodeModel createNodeModel() {
        return new AddonNodeModel(false);
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
    public NodeView<AddonNodeModel> createNodeView(final int viewIndex,
            final AddonNodeModel nodeModel) {
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
        return new AddonNodeDialog(false);
    }

}

