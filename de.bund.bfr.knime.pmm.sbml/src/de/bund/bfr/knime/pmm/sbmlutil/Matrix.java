package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Compartment;

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class Matrix {
	private Compartment compartment;
	
	public Matrix(Compartment compartment) {
		this.compartment = compartment;
	}
	
	public Compartment getCompartment() {
		return compartment;
	}
	
	public MatrixXml toMatrixXml() {
		String name = compartment.getName();
		MatrixXml matrix = new MatrixXml();
		matrix.setName(name);
		
		// Process annotation
//		Annotation annot = compartment.getAnnotation();
		// if (annot != null) {
		// XMLNode nonRDFAnnot = annot.getNonRDFannotation();
		// // PMF compartment id obtained from the DB
		// String casNumber =
		// ReaderUtils.parseComparmentAnnotation(nonRDFAnnot);
		// compartmentName = (String) DBKernel.getValue("Matrices",
		// "CAS_Nummer", casNumber, "Matrixname");
		// }
		
		return matrix;
	}
	
	public static Matrix convertMatrixXmlToMatrix(MatrixXml matrixXml) {
		final String COMPARTMENT_MISSING = "ComparmentMissing";
		String name;
		String id;
		
		if (matrixXml.getName().isEmpty()) {
			id = name = COMPARTMENT_MISSING;
		} else {
			id = createId(matrixXml.getName());
			name = matrixXml.getName();
		}
		
		Compartment compartment = new Compartment(id);
		compartment.setName(name);
		compartment.setConstant(true);
		Matrix matrix = new Matrix(compartment);
		return matrix;
	}
	
	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
	
//	public static String parseComparmentAnnotation(XMLNode annot) {
//		// Search metadata container
//		XMLNode metadata = null;
//		for (int nChild = 0; nChild < annot.getChildCount(); nChild++) {
//			XMLNode currNode = annot.getChildAt(nChild);
//			String nodeName = currNode.getName();
//			if (nodeName.equals("metadata")) {
//				metadata = currNode;
//				break;
//			}
//		}
//
//		String casNumber = null;
//		// Parse metadata container
//		if (metadata != null) {
//			for (int nTag = 0; nTag < metadata.getChildCount(); nTag++) {
//				XMLNode currNode = metadata.getChildAt(nTag);
//				String nodeName = currNode.getName();
//				if (nodeName.equals("source")) {
//					casNumber = currNode.getChildAt(0).getCharacters();
//					int pos = casNumber.lastIndexOf("/");
//					casNumber = casNumber.substring(pos + 1);
//				}
//			}
//		}
//
//		return casNumber;
//	}
}
