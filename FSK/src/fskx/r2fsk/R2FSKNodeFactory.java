package fskx.r2fsk;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class R2FSKNodeFactory extends NodeFactory<R2FSKNodeModel> {
  
  /** {@inheritDoc} */
  @Override
  public R2FSKNodeModel createNodeModel() {
    return new R2FSKNodeModel();
  }
  
  /** {@inheritDoc} */
  @Override
  public int getNrNodeViews() {
    return 0;
  }
  
  /** {@inheritDoc} */
  @Override
  public NodeView<R2FSKNodeModel> createNodeView(final int viewIndex, final R2FSKNodeModel nodeModel) {
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  public boolean hasDialog() {
    return true;
  }
  
  /** {@inheritDoc} */
  @Override
  public NodeDialogPane createNodeDialogPane() {
    return new R2FSKNodeDialog();
  }
}
