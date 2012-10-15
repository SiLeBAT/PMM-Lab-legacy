package de.bund.bfr.knime.pmm.xml2table;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "XML2Table" Node.
 * 
 *
 * @author BfR
 */
public class XML2TableNodeFactory 
        extends NodeFactory<XML2TableNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public XML2TableNodeModel createNodeModel() {
        return new XML2TableNodeModel();
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
    public NodeView<XML2TableNodeModel> createNodeView(final int viewIndex,
            final XML2TableNodeModel nodeModel) {
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
        return new XML2TableNodeDialog();
    }

}

