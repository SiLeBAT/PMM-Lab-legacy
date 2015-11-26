/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.sbml;

import java.util.List;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.pmf.ModelClass;

/**
 * Model rule. It has the properties:
 * <ul>
 * <li>Formula (mandatory)</li>
 * <li>Variable (mandatory)</li>
 * <li>Formula name (mandatory)</li>
 * <li>Model class (mandatory)</li>
 * <li>PmmLab id (mandatory)</li>
 * <li>References (optional)</li>
 * </ul>
 * 
 * @author Miguel Alba
 */
public class ModelRule {

	private AssignmentRule rule;
	private String formulaName;
	private ModelClass modelClass;
	private int pmmlabID;
	private Reference[] references;

	public ModelRule(String variable, String formula, String formulaName, ModelClass modelClass, int pmmlabID,
			Reference[] references) {
		this.formulaName = formulaName;
		this.modelClass = modelClass;
		this.pmmlabID = pmmlabID;
		this.references = references;

		ASTNode math = null;
		try {
			math = JSBML.parseFormula(formula);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rule = new AssignmentRule(math, 3, 1);
		rule.setVariable(variable);
		ModelRuleAnnotation annot = new ModelRuleAnnotation(formulaName, modelClass, pmmlabID, references);
		rule.setAnnotation(annot.annotation);
	}

	public ModelRule(AssignmentRule rule) {
		this.rule = rule;
		if (rule.isSetAnnotation()) {
			ModelRuleAnnotation annot = new ModelRuleAnnotation(rule.getAnnotation());
			pmmlabID = annot.pmmlabID;
			formulaName = annot.formulaName;
			modelClass = annot.modelClass;
			references = annot.references;
		}
	}

	public AssignmentRule getRule() {
		return rule;
	}

	/** Returns the formula of this {@link ModelRule}. */
	public String getFormula() {
		return rule.getMath().toFormula();
	}

	/** Returns the variable of this {@link ModelRule}. */
	public String getVariable() {
		return rule.getVariable();
	}

	/** Returns the formula name of this {@link ModelRule}. */
	public String getFormulaName() {
		return formulaName;
	}

	/** Returns the {@link ModelClass} of this {@link ModelRule}. */
	public ModelClass getModelClass() {
		return modelClass;
	}

	/** Returns the PmmLab id of this {@link ModelRule}. */
	public int getPmmlabID() {
		return pmmlabID;
	}

	/**
	 * Returns the {@link Reference}(s) of this {@link ModelRule}. If not set,
	 * returns null.
	 */
	public Reference[] getReferences() {
		return references;
	}

	/**
	 * Sets the formula name value of this {@link ModelRule} with 'formulaName'.
	 * Ignores null or empty strings.
	 */
	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}

	/**
	 * Sets the {@link ModelClass} value of this {@link ModelRule} with
	 * 'modelClass'. Ignores null.
	 */
	public void setModelClass(ModelClass modelClass) {
		this.modelClass = modelClass;
	}

	/** Sets the PmmLab id value of this {@link ModelRule} with 'id'. */
	public void setPmmlabID(int id) {
		this.pmmlabID = id;
	}

	/** Sets the references of this {@link ModelRule} with 'references'. Ignores null. */
	public void setReferences(Reference[] references) {
		this.references = references;
	}

	/** Returns true if the references of this {@link ModelRule} are set. */
	public boolean isSetReferences() {
		return references == null;
	}
}

class ModelRuleAnnotation {

	String formulaName;
	ModelClass modelClass;
	Reference[] references;
	int pmmlabID;
	Annotation annotation;

	private static final String FORMULA_TAG = "formulaName";
	private static final String SUBJECT_TAG = "subject";
	private static final String REFERENCE_TAG = "reference";
	private static final String PMMLAB_ID = "pmmlabID";

	public ModelRuleAnnotation(Annotation annotation) {
		this.annotation = annotation;

		XMLNode metadata = annotation.getNonRDFannotation().getChildElement("metadata", "");

		// Gets formula name
		XMLNode nameNode = metadata.getChildElement(FORMULA_TAG, "");
		formulaName = nameNode.getChild(0).getCharacters();

		// Gets formula subject
		XMLNode modelclassNode = metadata.getChildElement(SUBJECT_TAG, "");
		if (modelclassNode == null) {
			modelClass = ModelClass.UNKNOWN;
		} else {
			modelClass = ModelClass.fromName(modelclassNode.getChild(0).getCharacters());
		}

		// Get PmmLab ID
		XMLNode idNode = metadata.getChildElement(PMMLAB_ID, "");
		if (idNode == null) {
			pmmlabID = -1;
		} else {
			pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
		}

		// Gets references
		List<XMLNode> refNodes = metadata.getChildElements(REFERENCE_TAG, "");
		int numRefNodes = refNodes.size();
		references = new ReferenceImpl[numRefNodes];
		for (int i = 0; i < numRefNodes; i++) {
			XMLNode refNode = refNodes.get(i);
			references[i] = new ReferenceSBMLNode(refNode).toReference();
		}
	}

	public ModelRuleAnnotation(String formulaName, ModelClass modelClass, int pmmlabID, Reference[] references) {
		// Builds metadata node
		XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
		annotation = new Annotation();
		annotation.setNonRDFAnnotation(metadataNode);

		// Creates annotation for formula name
		XMLNode nameNode = new XMLNode(new XMLTriple(FORMULA_TAG, null, "pmmlab"));
		nameNode.addChild(new XMLNode(formulaName));
		metadataNode.addChild(nameNode);

		// Creates annotation for modelClass
		XMLNode modelClassNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null, "pmmlab"));
		modelClassNode.addChild(new XMLNode(modelClass.fullName()));
		metadataNode.addChild(modelClassNode);

		// Create annotation for pmmlabID
		XMLNode idNode = new XMLNode(new XMLTriple(PMMLAB_ID, null, "pmmlab"));
		idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
		metadataNode.addChild(idNode);

		// Builds reference nodes
		for (Reference ref : references) {
			metadataNode.addChild(new ReferenceSBMLNode(ref).node);
		}

		// Saves formulaName, subject and model literature
		this.formulaName = formulaName;
		this.modelClass = modelClass;
		this.pmmlabID = pmmlabID;
		this.references = references;
	}
}
