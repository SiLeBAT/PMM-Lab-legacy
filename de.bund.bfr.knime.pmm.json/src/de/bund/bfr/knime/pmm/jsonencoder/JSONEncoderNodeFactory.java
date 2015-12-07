package de.bund.bfr.knime.pmm.jsonencoder;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "JSONEncoder" Node.
 * Turns a PMM Lab table into JSON
 *
 * @author JSON Encoder
 */
public class JSONEncoderNodeFactory 
        extends NodeFactory<JSONEncoderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONEncoderNodeModel createNodeModel() {
        return new JSONEncoderNodeModel();
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
    public NodeView<JSONEncoderNodeModel> createNodeView(final int viewIndex,
            final JSONEncoderNodeModel nodeModel) {
        return new JSONEncoderNodeView(nodeModel);
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
        return new JSONEncoderNodeDialog();
    }

}

