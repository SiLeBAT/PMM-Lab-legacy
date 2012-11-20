/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
package de.bund.bfr.knime.pcml.port;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeView;
import org.knime.core.node.port.pmml.XMLTreeCreator;
import org.xml.sax.InputSource;

/**
 * The view for the PCML Port Object.
 * 
 * @author Heiko Hofer
 */
@SuppressWarnings("serial")
public class PCMLPortObjectView extends JComponent {
	private static final NodeLogger LOGGER = NodeLogger.getLogger(
            PCMLPortObjectView.class);
	
	public PCMLPortObjectView(final PCMLPortObject portObject) {
	    setLayout(new BorderLayout());
	    setBackground(NodeView.COLOR_BACKGROUND);
	    
	    setName("PCML model");
	    JTree tree = new JTree();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PCMLFormatter.save(portObject.getPcmlDoc(), out);
	        out.close();
			SAXParserFactory saxFac = SAXParserFactory.newInstance();
			SAXParser parser = saxFac.newSAXParser();
			XMLTreeCreator treeCreator = new XMLTreeCreator();
			parser.parse(
					new InputSource(new ByteArrayInputStream(out
							.toByteArray())), treeCreator);
			tree.setModel(new DefaultTreeModel(treeCreator.getTreeNode()));
			add(new JScrollPane(tree));
			revalidate();

		} catch (Exception e) {
			// log and return a "error during saving" component
			LOGGER.error("PMML contains errors", e);
			PCMLPortObjectView.this.add(new JLabel("PCML contains errors: "
					+ e.getMessage()));
		}
	}
}
