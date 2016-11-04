package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;

public class FskJSEditorNodeFactory extends NodeFactory<FskJSEditorNodeModel> implements
		WizardNodeFactoryExtension<FskJSEditorNodeModel, FskJSEditorNodeRepresentation, FskJSEditorNodeValue> {

	@Override
	public FskJSEditorNodeModel createNodeModel() {
		return new FskJSEditorNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FskJSEditorNodeModel> createNodeView(int viewIndex, FskJSEditorNodeModel nodeModel) {
		return null;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}
}
