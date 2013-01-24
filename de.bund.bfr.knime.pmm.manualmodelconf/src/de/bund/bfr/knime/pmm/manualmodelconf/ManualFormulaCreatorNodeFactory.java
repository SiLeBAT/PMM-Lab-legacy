package de.bund.bfr.knime.pmm.manualmodelconf;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ManualModelEditor" Node.
 * 
 *
 * @author Armin Weiser
 */
public class ManualFormulaCreatorNodeFactory extends NodeFactory<ManualModelConfNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ManualModelConfNodeModel createNodeModel() {
        return new ManualModelConfNodeModel(false, true);
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
    public NodeView<ManualModelConfNodeModel> createNodeView(final int viewIndex,
            final ManualModelConfNodeModel nodeModel) {
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
        return new ManualModelConfNodeDialog(true);
    }

}

