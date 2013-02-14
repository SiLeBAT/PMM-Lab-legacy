package de.bund.bfr.knime.foodprocess.view;

import org.jfree.chart.ChartPanel;
import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "View" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class ViewNodeView extends NodeView<ViewNodeModel> {

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ViewNodeModel})
	 */
	protected ViewNodeView(final ViewNodeModel nodeModel) {
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
		setComponent(new ChartPanel(getNodeModel().createChart()));
	}

}
