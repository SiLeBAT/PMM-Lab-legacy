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
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.knime.base.node.util.DoubleFormat;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;

/**
 * The view for the PCML Port Object.
 * 
 * @author Heiko Hofer
 */
@SuppressWarnings("serial")
public class PCMLPortObjectSpecView extends JComponent {
	//private static final NodeLogger LOGGER = NodeLogger.getLogger(PCMLPortObjectSpecView.class);
	
	public PCMLPortObjectSpecView(final PCMLPortObjectSpec spec) {
	    setLayout(new BorderLayout());
	    setBackground(NodeView.COLOR_BACKGROUND);
	    setName("Process Chain Port");
	    
	    JEditorPane editor = new JEditorPane("text/html", "");
        editor.setEditable(false);
        JScrollPane scroller = new JScrollPane(editor);
        add(scroller, BorderLayout.CENTER);
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html>\n");
        buffer.append("<head>\n");
        buffer.append("<style type=\"text/css\">\n");
        buffer.append("body {color:#333333;}");
        buffer.append("table {width: 70%;margin: 7px 0 7px 0;text-align:center;}");
        buffer.append("th {font-weight: bold;background-color: #aaccff;}");
        buffer.append("td,th {padding: 4px 5px; }");
        buffer.append(".numeric {text-align: right}");
        buffer.append(".odd {background-color:#ddeeff;}");
        buffer.append(".even {background-color:#ffffff;}");
        buffer.append("</style>\n");
        buffer.append("</head>\n");
        buffer.append("<body>\n");
        buffer.append("<h2>Properties of the Outport</h2>");

        buffer.append("<table>\n");
        
//        buffer.append("<tr>");
//        buffer.append("<th>Property</th>");
//        buffer.append("<th>Value</th>");
//        buffer.append("</tr>");
        
        int row = 1;
        // the matrices
	    if (spec.getMatrices().size() == 0) {
	        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
	        buffer.append("<td>");
	        buffer.append("<b>");
	        buffer.append("Matrix:");
	        buffer.append("</b>");
	        buffer.append("</td>\n<td>");	       
	        buffer.append("No matrix defined. The outport is not used!");
	        buffer.append("</td>");
	        buffer.append("</tr>");
	        row++;
	    } else {
	    	Iterator<Entry<Matrix, Double>> iter = spec.getMatrices().entrySet().iterator();
	        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
	        buffer.append("<td>");
	        buffer.append("<b>");
	        buffer.append("Matrix:");
	        buffer.append("</b>");
	        buffer.append("</td>\n<td>");
	        renderMatrix(buffer, iter.next());
	        buffer.append("</td>");
	        buffer.append("</tr>");
	        row++;
	        while(iter.hasNext()) {
		        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
		        buffer.append("<td>");
		        buffer.append("</td>\n<td>");
		        renderMatrix(buffer, iter.next());
		        buffer.append("</td>");
		        buffer.append("</tr>");
		        row++;
	        }
	    }
	    
        // the agents
	    if (spec.getAgents().size() == 0) {
	        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
	        buffer.append("<td>");
	        buffer.append("<b>");
	        buffer.append("Agent:");
	        buffer.append("</b>");
	        buffer.append("</td>\n<td>");	       
	        buffer.append("No agents defined.");
	        buffer.append("</td>");
	        buffer.append("</tr>");
	        row++;
	    } else {
	    	Iterator<Entry<Agent, Double>> iter = spec.getAgents().entrySet().iterator();
	        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
	        buffer.append("<td>");
	        buffer.append("<b>");
	        buffer.append("Agent:");
	        buffer.append("</b>");
	        buffer.append("</td>\n<td>");
	        renderAgent(buffer, iter.next());
	        buffer.append("</td>");
	        buffer.append("</tr>");
	        row++;
	        while(iter.hasNext()) {
		        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
		        buffer.append("<td>");
		        buffer.append("</td>\n<td>");
		        renderAgent(buffer, iter.next());
		        buffer.append("</td>");
		        buffer.append("</tr>");
		        row++;
	        }
	    }
	    
        // the volume
        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
        buffer.append("<td>");
        buffer.append("Volume:");
        buffer.append("</td>\n<td class=\"numeric\">");
        String value = null != spec.getVolume()
        	? DoubleFormat.formatDouble(spec.getVolume())
        	: "n/a";
        buffer.append(value);
        buffer.append("</td>");
        buffer.append("</tr>");
        row++;
	    
        // the temperature
        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
        buffer.append("<td>");
        buffer.append("Temperature:");
        buffer.append("</td>\n<td class=\"numeric\">");
        value = null != spec.getTemperature()
	    	? DoubleFormat.formatDouble(spec.getTemperature())
	    	: "n/a";
        buffer.append(value);
        buffer.append("</td>");
        buffer.append("</tr>");
        row++;
        
        // the pressure
        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
        buffer.append("<td>");
        buffer.append("Pressure:");;
        buffer.append("</td>\n<td class=\"numeric\">");
        value = null != spec.getPressure()
	    	? DoubleFormat.formatDouble(spec.getPressure())
	    	: "n/a";
	    buffer.append(value);
        buffer.append("</td>");
        buffer.append("</tr>");
        row++;
        
        // the pH value
        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
        buffer.append("<td>");
        buffer.append("pH value:");
        buffer.append("</td>\n<td class=\"numeric\">");
        value = null != spec.getPH_value()
	    	? DoubleFormat.formatDouble(spec.getPH_value())
	    	: "n/a";
	    buffer.append(value);
        buffer.append("</td>");
        buffer.append("</tr>");
        row++;
        
        // the aw value
        buffer.append("<tr class=\"" + getRowClass(row) + "\">\n");
        buffer.append("<td>");
        buffer.append("aw value:");
        buffer.append("</td>\n<td class=\"numeric\">");
        value = null != spec.getAw_value()
	    	? DoubleFormat.formatDouble(spec.getAw_value())
	    	: "n/a";
	    buffer.append(value);
        buffer.append("</td>");
        buffer.append("</tr>");
        row++;
        
        buffer.append("</table>\n");
        buffer.append("</body>\n");
        buffer.append("</html>\n");
        editor.setText(buffer.toString());
        editor.revalidate();
	}

	private String getRowClass(final int row) {
		return row % 2 == 0 ? "even" : "odd";
	}

	private void renderMatrix(final StringBuilder buffer, final Entry<Matrix, Double> entry) {
		Matrix matrix = entry.getKey();
		Double fraction = entry.getValue();
        buffer.append("<b>");
        buffer.append(matrix.getName());
        buffer.append("</b>");
        if (matrix.getId() > -1) {
	        buffer.append(" (");
	        buffer.append(matrix.getId());
	        buffer.append(")");
        }
        buffer.append(" : ");
        buffer.append(DoubleFormat.formatDouble(
        		fraction * 100));
        buffer.append("%");        
	}
	

	private void renderAgent(final StringBuilder buffer, final Entry<Agent, Double> entry) {
		Agent agent = entry.getKey();
		Double quantity = entry.getValue();
        buffer.append(agent.getName());
        if (agent.getId() > -1) {
	        buffer.append(" (");
	        buffer.append(agent.getId());
	        buffer.append(")");
        }
        buffer.append(" : ");
        buffer.append(DoubleFormat.formatDouble(
        		quantity));        
	}
	
}
