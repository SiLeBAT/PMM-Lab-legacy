package de.bund.bfr.knime.pmm.jsondecoder;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "JSONDecoder" Node.
 * Turns a PMM Lab table into JSON
 *
 * @author Miguel Alba
 */
public class JSONDecoderNodeFactory 
        extends NodeFactory<JSONDecoderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONDecoderNodeModel createNodeModel() {
        return new JSONDecoderNodeModel();
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
    public NodeView<JSONDecoderNodeModel> createNodeView(final int viewIndex,
            final JSONDecoderNodeModel nodeModel) {
        return new JSONDecoderNodeView(nodeModel);
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
        return new JSONDecoderNodeDialog();
    }

}

