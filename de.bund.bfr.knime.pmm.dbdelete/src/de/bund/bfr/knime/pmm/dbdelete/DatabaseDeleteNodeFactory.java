package de.bund.bfr.knime.pmm.dbdelete;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "DatabaseDelete" Node.
 * 
 *
 * @author Armin A. Weiser
 */
public class DatabaseDeleteNodeFactory 
        extends NodeFactory<DatabaseDeleteNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseDeleteNodeModel createNodeModel() {
        return new DatabaseDeleteNodeModel();
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
    public NodeView<DatabaseDeleteNodeModel> createNodeView(final int viewIndex,
            final DatabaseDeleteNodeModel nodeModel) {
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
        return new DatabaseDeleteNodeDialog();
    }

}

