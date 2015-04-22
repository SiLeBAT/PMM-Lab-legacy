package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.LinkedList;

import org.nfunk.jep.ParseException;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;

public class PiecewiseFormula {
	
	public static LinkedList<String> parsePieces(String formula) {
		LinkedList<String> pieces = new LinkedList<>();
		int init_pos = 0;
		int level = 0;
		for (int i = 0; i < formula.length(); i++) {
			char currentChar = formula.charAt(i);
			if (currentChar == '(') {
				level++;
			} else if (currentChar == ')') {
				level--;
			} else if (currentChar == '+' && level == 0) {
				pieces.add(formula.substring(init_pos, i));
				init_pos = i + 1;
			}
		}
		// Add last piece
		pieces.add(formula.substring(init_pos));
		
		return pieces;
	}
	
	public static LinkedList<String> parseProducts(String piece) {
		LinkedList<String> products = new LinkedList<>();
		int init_pos = 0;
		int level = 0;
		for (int i = 0; i < piece.length(); i++) {
			char currentChar = piece.charAt(i);
			if (currentChar == '(') {
				level++;
			} else if (currentChar == ')') {
				level--;
			} else if (currentChar == '*' && level == 0) {
				products.add(piece.substring(init_pos, i));
				init_pos = i + 1;
			}
		}
		// Add last product
		products.add(piece.substring(init_pos));
		
		return products;
	}
	
	public static ASTNode[] parsePiecewiseFormula(String formula) throws org.sbml.jsbml.text.parser.ParseException {
		LinkedList<String> pieces = parsePieces(formula);
		ASTNode nodes[] = new ASTNode[pieces.size() * 2];
		int nodeCounter = 0;
		
		for (String piece : pieces) {
			LinkedList<String> products = parseProducts(piece);

			String expr = "";
			String cond = "";

			LinkedList<String> condList = new LinkedList<>();
			for (String product : products) {
				ASTNode node = ASTNode.parseFormula(product);
				if (node.isRelational()) {
					condList.add(product);
				} else {
					expr = product;
				}
			}
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < condList.size() - 1; i++) {
				sb.append(condList.get(i) + "*");
			}
			sb.append(condList.get(condList.size() - 1));
			cond = sb.toString();
			
			nodes[nodeCounter] = ASTNode.parseFormula(expr);
			nodeCounter++;
			nodes[nodeCounter] = ASTNode.parseFormula(cond);
			nodeCounter++;
		}
		
		return nodes;
	}

	public static void main(String[] args) throws ParseException, org.sbml.jsbml.text.parser.ParseException {
		String formula = "Y0*(Time<lag)+"
				+ "(Y0+k*(Time-lag))*(lag<=Time)*(Time<(lag+(Ymax-Y0)/k))+"
				+ "Ymax*(Time>=(lag+(Ymax-Y0)/k))";	

		AssignmentRule ar = new AssignmentRule();
		ar.setVariable("Value");
		ASTNode[] nodes = parsePiecewiseFormula(formula);
		ASTNode math = ASTNode.piecewise(new ASTNode(ar), nodes);
		System.out.println(math.toFormula());
	}
}