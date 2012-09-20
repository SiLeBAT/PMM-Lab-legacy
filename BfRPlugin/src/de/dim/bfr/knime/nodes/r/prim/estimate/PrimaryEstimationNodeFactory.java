/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.dim.bfr.knime.nodes.r.prim.estimate;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

import de.dim.bfr.knime.nodes.r.PredictorNodeModel;
import de.dim.bfr.knime.util.PluginUtils;

/**
 * <code>NodeFactory</code> for the "Predictor" Node.
 * 
 *
 * @author 
 */
public class PrimaryEstimationNodeFactory 
        extends NodeFactory<PredictorNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PredictorNodeModel createNodeModel() 
    {
        return new PredictorNodeModel(this.getClass(), PluginUtils.PRIMARY_ESTIMATION);
        
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
    public NodeView<PredictorNodeModel> createNodeView(final int viewIndex,
            final PredictorNodeModel nodeModel) {
//        return new PredictorNodeView(nodeModel);
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
//        return true;
    	return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
//        return new PredictorNodeDialog(this.getNodeName());
    	return null;
    }

}

