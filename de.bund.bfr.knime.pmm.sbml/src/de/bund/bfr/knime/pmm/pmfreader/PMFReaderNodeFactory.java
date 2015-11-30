package de.bund.bfr.knime.pmm.pmfreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SBMLReader" Node.
 * 
 * Author: Miguel de Alba Aparicio
 */
public class PMFReaderNodeFactory 
        extends NodeFactory<PMFReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PMFReaderNodeModel createNodeModel() {
        return new PMFReaderNodeModel();
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
    public NodeView<PMFReaderNodeModel> createNodeView(final int viewIndex,
            final PMFReaderNodeModel nodeModel) {
        return new PMFReaderNodeView(nodeModel);
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
        return new PMFReaderNodeDialog();
    }

}

