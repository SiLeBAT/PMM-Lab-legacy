package de.bund.bfr.knime.pmm.sbmlutil;

import java.io.StringReader;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

public abstract class ModelRule {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected AssignmentRule rule;

	protected ASTNode parseMath(String math) throws ParseException {
		return new FormulaParser(new StringReader(math)).parse();
	}

	public AssignmentRule getRule() {
		return rule;
	}

	public abstract void parse(String formula);
}