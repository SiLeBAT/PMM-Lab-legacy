package de.bund.bfr.knime.pmm.pdfkeeper;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PdfKeeper" Node.
 * 
 *
 * @author BfR
 */
public class PdfKeeperNodeFactory 
        extends NodeFactory<PdfKeeperNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfKeeperNodeModel createNodeModel() {
        return new PdfKeeperNodeModel();
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
    public NodeView<PdfKeeperNodeModel> createNodeView(final int viewIndex,
            final PdfKeeperNodeModel nodeModel) {
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
        return new PdfKeeperNodeDialog();
    }

}

