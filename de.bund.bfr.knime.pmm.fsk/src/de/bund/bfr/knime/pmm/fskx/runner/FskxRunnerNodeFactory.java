package de.bund.bfr.knime.pmm.fskx.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FskxRunnerNodeFactory extends NodeFactory<FskxRunnerNodeModel> {

  @Override
  public FskxRunnerNodeModel createNodeModel() {
    return new FskxRunnerNodeModel();
  }

  @Override
  protected int getNrNodeViews() {
    return 1;
  }

  @Override
  public NodeView<FskxRunnerNodeModel> createNodeView(int viewIndex,
      FskxRunnerNodeModel nodeModel) {
    return new FskxRunnerNodeView(nodeModel);
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
