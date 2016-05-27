package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FskxEditorNodeFactory extends NodeFactory<FskxEditorNodeModel> {
  
  /** {@inheritDoc} */
  @Override
  public FskxEditorNodeModel createNodeModel() {
    return new FskxEditorNodeModel();
  }
  
  /** {@inheritDoc} */
  @Override
  protected int getNrNodeViews() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public NodeView<FskxEditorNodeModel> createNodeView(int viewIndex,
      FskxEditorNodeModel nodeModel) {
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  protected boolean hasDialog() {
    return true;
  }
  
  /** {@inheritDoc} */
  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return new FskxEditorNodeDialog();
  }
}
