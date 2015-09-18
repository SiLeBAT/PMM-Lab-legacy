package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import groovy.util.Node;
import groovy.util.NodeList;

public class MatrixNuMLNode extends NuMLNodeBase {
	
	public static final String TAG = "compartment";
	public static final String NS = "sbml";
	
	public MatrixNuMLNode(Matrix matrix) {

		Map<String, String> attrs = matrix.getCompartment().writeXMLAttributes();
		attrs.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		attrs.put("xmlns:pmmlab", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		
		String name = String.format("%s:%s", NS, TAG);
		
		node = new Node(null, name, attrs);
		
		if (matrix.getCode() != null) {
			node.appendNode("dc:source", matrix.getDetails());
		}
		
		if (matrix.getDetails() != null) {
			node.appendNode("pmmlab:detail", matrix.getDetails());
		}
		
		for (Entry<String, Double> entry : matrix.getMiscs().entrySet()) {
			Map<String, String> modelVariable = new HashMap<>();
			modelVariable.put("name", entry.getKey());
			modelVariable.put("value", entry.getValue().toString());
			node.appendNode("pmmlab:modelVariable", modelVariable);
		}
	}
	
	public MatrixNuMLNode(Node node) {
		this.node = node;
	}
	
	public MatrixXml toMatrixXml() {
		MatrixXml matrixXml = new MatrixXml();
		matrixXml.setName((String) node.attribute("name"));
		
		// Gets and sets matrix detail
		NodeList detailNodes = (NodeList) node.get("detail");
		if (detailNodes.size() == 1) {
			Node detailNode = (Node) detailNodes.get(0);
			matrixXml.setDetail(detailNode.text());
		}
		
		return matrixXml;
	}

}
