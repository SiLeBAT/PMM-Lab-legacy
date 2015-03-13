package de.bund.bfr.knime.pmm.sbmlutil;

import java.io.StringReader;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.ModelClassNode;
import de.bund.bfr.knime.pmm.annotation.ModelNameNode;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;

/**
 * Base class for model rules.
 * 
 * @author malba
 */
public abstract class ModelRule {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected AssignmentRule rule;

	public AssignmentRule getRule() {
		return rule;
	}

	protected static ASTNode parseMath(String math) throws ParseException {
		return new FormulaParser(new StringReader(math)).parse();
	}

	/**
	 * Add annotation to the rule.
	 * 
	 * @param name
	 *            : Model name. Mandatory.
	 * @param type
	 *            : Model class.
	 */
	protected void addAnnotation(String name, String type) {
		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dc");
		pmfNS.add("http://www.dmg.org/PMML-4.2", "pmml");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

		// Add model name to pmfNode
		ModelNameNode nameNode = new ModelNameNode(name);
		pmfNode.addChild(nameNode.getNode());

		// Add model class to pmfNode
		ModelClassNode typeNode = new ModelClassNode(type);
		pmfNode.addChild(typeNode.getNode());

		// add non rdf annotation
		Annotation annot = new Annotation();
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		rule.setAnnotation(annot);
	}

	/** Get CatalogModelXml from ModelRule */
	public abstract CatalogModelXml toCatModel();
}