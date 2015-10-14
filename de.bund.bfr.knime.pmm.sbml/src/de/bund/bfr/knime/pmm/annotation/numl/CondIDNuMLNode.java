package de.bund.bfr.knime.pmm.annotation.numl;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CondIDNuMLNode extends NuMLNodeBase {
	
	public final static String TAG = "pmmlab:condID";
	
	public CondIDNuMLNode(final int id) throws ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		node = doc.createElement(TAG);
		node.setTextContent(Integer.toString(id));
	}
	
	public CondIDNuMLNode(final Element node) {
		this.node = node;
	}

	public int getCondId() {
		return Integer.parseInt(node.getTextContent());
	}
}
