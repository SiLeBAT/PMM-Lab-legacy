package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * XML data source node. It holds a reference to a data file.
 * E.g. <pmf:dataSource id="source1" href="foo_0.numl"/>
 * 
 * @author Miguel Alba
 */
public class DataSourceNode {
	
	private XMLNode node;
	
	public DataSourceNode(XMLNode node) {
		this.node = node;
	}

	public DataSourceNode(String dataName) {
		XMLTriple triple = new XMLTriple("dataSource", null, "pmf");
		XMLAttributes attrs = new XMLAttributes();
		attrs.add("id", "source1");
		attrs.add("href", dataName);
		node = new XMLNode(triple, attrs);
	}
	
	public XMLNode getNode() {
		return node;
	}
	
	// Return data file name
	public String getFile() {
		return node.getAttrValue("href");
	}
}
