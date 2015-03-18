package de.bund.bfr.knime.pmm.js.modelplotter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;

/**
 * Factory that bundles model, dialog and view creation.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeFactory extends NodeFactory<ModelPlotterNodeModel>
		implements
		WizardNodeFactoryExtension<ModelPlotterNodeModel, ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	@Override
	public ModelPlotterNodeModel createNodeModel() {
		return new ModelPlotterNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<ModelPlotterNodeModel> createNodeView(int viewIndex,
			ModelPlotterNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new ModelPlotterNodeDialogPane();
	}

}
