package de.bund.bfr.knime.pmm.annotation.numl;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;

public class MatrixNuMLNode extends NuMLNodeBase {

	public static final String TAG = "sbml:compartment";
	
	private final static String SOURCE_TAG = "dc:source";
	private final static String DETAIL_TAG = "pmmlab:detail";
	private final static String VARIABLE_TAG = "pmmlab:modelVariable";
	
	public MatrixNuMLNode(Matrix matrix) throws ParserConfigurationException {
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		node = doc.createElement(TAG);
		
		for (Map.Entry<String, String> entry : matrix.getCompartment().writeXMLAttributes().entrySet()) {
			node.setAttribute(entry.getKey(), entry.getValue());
		}
		
		if (matrix.getCode() != null) {
			Element codeNode = doc.createElement(SOURCE_TAG);
			codeNode.setTextContent(matrix.getCode());
			node.appendChild(codeNode);
		}
		
		if (matrix.getDetails() != null) {
			Element detailsNode = doc.createElement(DETAIL_TAG);
			detailsNode.setTextContent(matrix.getDetails());
			node.appendChild(detailsNode);
		}
		
		for (Entry<String, Double> entry : matrix.getMiscs().entrySet()) {
			Element modelVarNode = doc.createElement(VARIABLE_TAG);
			modelVarNode.setAttribute("name", entry.getKey());
			modelVarNode.setAttribute("value", entry.getValue().toString());
			node.appendChild(modelVarNode);
		}
	}
	
	public MatrixNuMLNode(Element node) {
		this.node = node;
	}
	
	public MatrixXml toMatrixXml() {
		MatrixXml matrixXml = new MatrixXml();
		matrixXml.setName(node.getAttribute("name"));
		
		// Gets and sets matrix detail
		NodeList detailNodes = node.getElementsByTagName(DETAIL_TAG);
		if (detailNodes.getLength() == 1) {
			Element detailNode = (Element) detailNodes.item(0);
			matrixXml.setDetail(detailNode.getTextContent());
		}
		
		return matrixXml;
	}
}
