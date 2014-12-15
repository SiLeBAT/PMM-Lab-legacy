package de.bund.bfr.knime.pmm.sbmlreader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SBMLReader" Node.
 * 
 *
 * Author: Miguel de Alba Aparicio
 * Contact: malba@optimumquality.es
 */
public class SBMLReaderNodeFactory 
        extends NodeFactory<SBMLReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SBMLReaderNodeModel createNodeModel() {
        return new SBMLReaderNodeModel();
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
    public NodeView<SBMLReaderNodeModel> createNodeView(final int viewIndex,
            final SBMLReaderNodeModel nodeModel) {
        return new SBMLReaderNodeView(nodeModel);
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
        return new SBMLReaderNodeDialog();
    }

}

