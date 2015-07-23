package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * XML data source node. It holds a reference to a data file. E.g.
 * <pmf:dataSource id="source1" href="foo_0.numl"/>
 * 
 * @author Miguel Alba
 */
public class DataSourceNode extends SBMLNodeBase {

	/**
	 * Builds a DataSourceNode from an existing XMLNode.
	 * 
	 * @param node
	 *            XMLNode
	 */
	public DataSourceNode(XMLNode node) {
		this.node = node;
	}

	/**
	 * Builds a DataSourceNode with id "source1".
	 * 
	 * @param dataName
	 *            Reference to data file.
	 */
	public DataSourceNode(String dataName) {
		XMLTriple triple = new XMLTriple("dataSource", null, "pmf");
		XMLAttributes attrs = new XMLAttributes();
		attrs.add("id", "source1");
		attrs.add("href", dataName);
		node = new XMLNode(triple, attrs);
	}

	// Return data file name
	public String getFile() {
		return node.getAttrValue("href");
	}
}
