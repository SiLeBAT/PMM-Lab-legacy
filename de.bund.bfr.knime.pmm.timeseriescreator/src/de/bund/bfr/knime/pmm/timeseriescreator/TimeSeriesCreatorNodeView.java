package de.bund.bfr.knime.pmm.timeseriescreator;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "TimeSeriesCreator" Node.
 * 
 *
 * @author Christian Thoens
 */
public class TimeSeriesCreatorNodeView extends NodeView<TimeSeriesCreatorNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link TimeSeriesCreatorNodeModel})
     */
    protected TimeSeriesCreatorNodeView(final TimeSeriesCreatorNodeModel nodeModel) {
        super(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
    }

}

