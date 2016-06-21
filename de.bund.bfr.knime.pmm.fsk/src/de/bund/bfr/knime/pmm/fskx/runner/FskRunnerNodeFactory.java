package de.bund.bfr.knime.pmm.fskx.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FskRunnerNodeFactory extends NodeFactory<FskRunnerNodeModel> {

  @Override
  public FskRunnerNodeModel createNodeModel() {
    return new FskRunnerNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 1;
  }

  @Override
  public NodeView<FskRunnerNodeModel> createNodeView(int viewIndex,
      FskRunnerNodeModel nodeModel) {
    return new FskRunnerNodeView(nodeModel);
  }

  @Override
  protected boolean hasDialog() {
    return true;
  }

  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return null;
  }
}
