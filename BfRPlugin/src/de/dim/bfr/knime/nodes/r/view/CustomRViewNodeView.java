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
package de.dim.bfr.knime.nodes.r.view;

import java.awt.Image;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.NodeView;

/**
 * The view of the <code>CustomRView</code> which is able to display
 * multiple images created by a certain R command. To display the image
 * {@link org.knime.ext.r.node.RPlotterViewPanel} is used.
 *
 * @author Data In Motion UG
 */
public class CustomRViewNodeView extends NodeView<CustomRViewNodeModel> {

    private final CustomRPlotterViewPanel m_panel;
	private int counter;
	private List<Image> images;
	private JScrollPane jsp;

    /**
     * Creates a new instance of <code>RLocalViewsNodeView</code> which displays
     * a certain image.
     *
     * @param nodeModel the model associated with this view.
     */
    public CustomRViewNodeView(final CustomRViewNodeModel nodeModel) {
        super(nodeModel);
       
        JPanel buttonPanel = new JPanel();
        images = nodeModel.getResultImages();
        if (images.size() > 0) {
	        SpinnerNumberModel spinModel = new SpinnerNumberModel(1, 1, images.size(), 1 );
	        final JSpinner spinner = new JSpinner( spinModel );
	        spinner.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					m_panel.update(getImage((Integer) spinner.getModel().getValue()-1));
				}
			});
	        buttonPanel.add(spinner);
        }
        m_panel = new CustomRPlotterViewPanel();
        
        jsp = new JScrollPane(m_panel);
        JPanel viewPanel = new JPanel();
        viewPanel.add(jsp);

        JPanel main = new JPanel();
        main.add(viewPanel);
        main.add(buttonPanel);
        
        super.setComponent(main);
    }

    protected Image getNextImage() {
    	counter++;
    	if (counter < images.size()) {
    		return images.get(counter);
    	} else {
    		counter = 0;
    		return images.get(counter);
    	}
	}
    
    protected Image getImage(int index) {
    	if (index < images.size()) 
    		return images.get(index);
    	else
    		setShowNODATALabel(true);
    	
    	return null;
    }

	/**
     * Updates the image to display.
     *
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
    	if (images.size()>0)
	        m_panel.update(images.get(0));
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
