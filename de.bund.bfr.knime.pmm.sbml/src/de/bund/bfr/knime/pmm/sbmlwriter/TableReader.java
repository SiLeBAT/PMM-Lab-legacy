/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AlgebraicRule;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Creator;
import org.sbml.jsbml.History;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.pmm.sbmlwriter.SBMLWriterNodeModel.ModelType;

public class TableReader {

	enum ModelSubject {
		UNKNOWN, GROWTH, INACTIVATION, SURVIVAL, GROWTH_INACTIVATION, INACTIVATION_SURVIVAL, GROWTH_SURVIVAL, GROWTH_INACTIVATION_SURVIVAL, T, PH, AW, T_PH, T_AW, PH_AW, T_PH_AW;

		public static ModelSubject fromInteger(int x) {
			switch (x) {
			case 0:
				return UNKNOWN;
			case 1:
				return GROWTH;
			case 2:
				return INACTIVATION;
			case 3:
				return SURVIVAL;
			case 4:
				return GROWTH_INACTIVATION;
			case 5:
				return INACTIVATION_SURVIVAL;
			case 6:
				return GROWTH_SURVIVAL;
			case 7:
				return GROWTH_INACTIVATION_SURVIVAL;
			case 8:
				return T;
			case 9:
				return PH;
			case 10:
				return AW;
			case 11:
				return T_PH;
			case 12:
				return T_AW;
			case 13:
				return PH_AW;
			case 14:
				return T_PH_AW;
			}
			return null;
		}
	}

	class DCType {
		private String tag;

		public DCType(ModelType modelType) {
			String type = null;
			if (modelType == ModelType.PRIMARY) {
				type = "primary";
			} else if (modelType == ModelType.TERCIARY) {
				type = "terciary";
			}
			tag = "<dc:type> " + type + " </dc:type>";
		}

		public String getTag() {
			return tag;
		}
	}

	class DCSubject {
		private String tag;

		// TODO: DCSubject
		public DCSubject(ModelSubject modelSubject) {
			String subject = null;
			switch (modelSubject) {
			case UNKNOWN:
				subject = "unknown";
				break;
			case GROWTH:
				subject = "growth";
				break;
			case INACTIVATION:
				subject = "inactivation";
				break;
			case SURVIVAL:
				subject = "survival";
				break;
			case GROWTH_INACTIVATION:
				subject = "growth/inactivation";
				break;
			case INACTIVATION_SURVIVAL:
				subject = "inactivation/survival";
				break;
			case GROWTH_SURVIVAL:
				subject = "growth/survival";
				break;
			case GROWTH_INACTIVATION_SURVIVAL:
				subject = "growth/inactivation/survival";
				break;
			case T:
				subject = "T";
				break;
			case AW:
				subject = "aw";
				break;
			case T_PH:
				subject = "T/pH";
				break;
			case T_AW:
				subject = "T/aw";
				break;
			case PH_AW:
				subject = "pH/aw";
				break;
			case T_PH_AW:
				subject = "T/pH/aw";
				break;
			}
			tag = "<dc:subject> " + subject + " </dc:subject>";
		};

		public String getTag() {
			return tag;
		}
	}

	class DCReference {
		private String tag;

		public DCReference(String referenceTitle) {
			tag = "<dc:references> " + referenceTitle + " </dc:references>";
		}

		public String getTag() {
			return tag;
		}
	}

	class DCSource {
		private String tag;

		public DCSource(String source) {
			tag = "<dc:source> " + source + " </dc:source>";
		}

		public String getTag() {
			return tag;
		}
	}

	class DCTitle {
		private String tag;

		public DCTitle(String title) {
			tag = "<dc:title> " + title + " </dc:title>";
		}

		public String getTag() {
			return tag;
		}
	}

	class DCCreator {
		private String tag;

		public DCCreator(String creator) {
			tag = "<dc:creator> " + creator + " </dc:creator>";
		}

		public String getTag() {
			return tag;
		}
	}

	private final static String COMPARTMENT_MISSING = "CompartmentMissing";
	private final static String SPECIES_MISSING = "SpeciesMissing";

	// TableReader now outputs SBML L3v1 documents
	final static int LEVEL = 3;
	final static int VERSION = 1;

	private Map<String, SBMLDocument> documents;

	public TableReader(List<KnimeTuple> tuples, String varParams,
			String modelName, String creatorGivenName,
			String creatorFamilyName, String creatorContact, Date createdDate,
			Date modifiedDate, String reference, ModelType modelType) {

		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		Set<Integer> idSet = new LinkedHashSet<>();
		int index = 1;

		if (isTertiaryModel) {
			tuples = new ArrayList<>(
					new ModelCombiner(tuples, true, null, null)
							.getTupleCombinations().keySet());
		}

		documents = new LinkedHashMap<>(); // list of documents

		// for each document
		for (KnimeTuple tuple : tuples) {
			replaceCelsiusAndFahrenheit(tuple);
			renameLog(tuple);

			// retrieve XML cells
			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			AgentXml organismXml = (AgentXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_AGENT).get(0);
			MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_MATRIX).get(0);
			Integer id = estXml.getId();
			LiteratureItem literatureXml = (LiteratureItem) tuple.getPmmXml(
					Model1Schema.ATT_MLIT).get(0);

			if (!idSet.add(id)) {
				continue;
			}

			// create history
			// History history = createHistory(createdDate, modifiedDate,
			// creatorGivenName, creatorFamilyName, creatorContact);

			String modelID = createId(modelName) + "_" + index;
			SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);

			// enablePackage doesn't work with packageName
			doc.enablePackage(CompConstants.shortLabel);

			Model model = doc.createModel(modelID);

			model.setMetaId("Meta_" + modelID);
			model.setName(modelName);

			// TODO: add annotations
			int modelClass = modelXml.getModelClass().intValue();
			ModelSubject modelSubject = ModelSubject.fromInteger(modelClass);
			Annotation annotation = createModelAnnotation(modelType,
					modelSubject);
			model.setAnnotation(annotation);

			// Create compartment and add it to the model
			Compartment c = createCompartment(matrixXml.getName());
			model.addCompartment(c);

			// Create species and add it to the model
			Species s = createSpecies(organismXml.getName(), c);
			model.addSpecies(s);

			ListOf<Rule> rules = new ListOf<>(LEVEL, VERSION);

			// Create parameter and add it to the model
			Parameter depParam = createDepParam(depXml.getName(),
					depXml.getUnit(), model);
			model.addParameter(depParam);

			// Create rule of the model and add it to the rest of rules
			String modelFormula = modelXml.getFormula();
			String depName = depXml.getName();
			String depUnit = depXml.getUnit();
			Rule modelRule = createModelRule(modelFormula, depName, depUnit);
			rules.add(modelRule);

			// Parse params
			List<PmmXmlElementConvertable> params = tuple.getPmmXml(
					Model1Schema.ATT_PARAMETER).getElementSet();
			parseParams(model, params, varParams);

			// Parse independent params
			List<PmmXmlElementConvertable> indepParams = tuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).getElementSet();
			parseIndepParams(model, indepParams, rules);

			model.setListOfRules(rules);

			documents.put(modelID, doc);
			index++;
		}
	}

	public Map<String, SBMLDocument> getDocuments() {
		return documents;
	}

	private static void renameLog(KnimeTuple tuple) {
		PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		CatalogModelXml model = (CatalogModelXml) modelXml.get(0);

		model.setFormula(MathUtilities.replaceVariable(model.getFormula(),
				"log", "log10"));
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
	}

	private static void replaceCelsiusAndFahrenheit(KnimeTuple tuple) {
		final String CELSIUS = "°C";
		final String FAHRENHEIT = "°F";
		final String KELVIN = "K";

		PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		CatalogModelXml model = (CatalogModelXml) modelXml.get(0);
		Category temp = Categories.getTempCategory();

		for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
			IndepXml indep = (IndepXml) el;

			if (CELSIUS.equals(indep.getUnit())) {
				try {
					String replacement = "("
							+ temp.getConversionString(indep.getName(), KELVIN,
									CELSIUS) + ")";

					model.setFormula(MathUtilities.replaceVariable(
							model.getFormula(), indep.getName(), replacement));
					indep.setUnit(KELVIN);
					indep.setMin(temp.convert(indep.getMin(), CELSIUS, KELVIN));
					indep.setMax(temp.convert(indep.getMax(), CELSIUS, KELVIN));
				} catch (ConvertException e) {
					e.printStackTrace();
				}
			} else if (FAHRENHEIT.equals(indep.getUnit())) {
				try {
					String replacement = "("
							+ temp.getConversionString(indep.getName(), KELVIN,
									FAHRENHEIT) + ")";

					model.setFormula(MathUtilities.replaceVariable(
							model.getFormula(), indep.getName(), replacement));
					indep.setUnit(FAHRENHEIT);
					indep.setMin(temp.convert(indep.getMin(), FAHRENHEIT,
							KELVIN));
					indep.setMax(temp.convert(indep.getMax(), FAHRENHEIT,
							KELVIN));
				} catch (ConvertException e) {
					e.printStackTrace();
				}
			}
		}

		tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
	}

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}

	private static ASTNode parse(String s) throws ParseException {
		return new FormulaParser(new StringReader(s)).parse();
	}

	private static ASTNode and(ASTNode left, ASTNode right) {
		ASTNode relational = new ASTNode(ASTNode.Type.LOGICAL_AND,
				left.getParentSBMLObject());

		relational.addChild(left);
		relational.addChild(right);

		return relational;
	}

	/**
	 * Create an annotation for the model.
	 * 
	 * @param modelType
	 * @param modelSubject
	 * @param literatureTitle
	 * @return
	 */
	private Annotation createModelAnnotation(ModelType modelType,
			ModelSubject modelSubject) {
		Annotation annotation = new Annotation();
		annotation.addDeclaredNamespace("dc",
				"http://purl.org/dc/elements/1.1/");
		
		try {
			annotation.appendNoRDFAnnotation("<PMF-ML>...</PMF-ML>");
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		String tag = "<type> primary </type>";
//		String xmlTag = StringTools.toXMLAnnotationString(tag);
//		XMLNode node = null;
//		try {
//			node = XMLNode.convertStringToXMLNode(xmlTag);
//		} catch (XMLStreamException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		XMLNode noRDFAnnotation = annotation.getNonRDFannotation();
//		noRDFAnnotation.addChild(node);
//		annotation.setNonRDFAnnotation(noRDFAnnotation);
		
//		try {
//			annotation.appendNoRDFAnnotation("<p> primary </p>");
//			System.out.println("annotation appended");
//		} catch (XMLStreamException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("annotation not appended");
//		}

		// // Insert type annotation
		// DCType dcTypeAnnotation = new DCType(modelType);
		// try {
		// annotation.appendNoRDFAnnotation(dcTypeAnnotation.getTag());
		// } catch (XMLStreamException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // annotation.insertNoRDFAnnotation(dcTypeAnnotation.getTag(), 0);
		//
		// // Insert subject annotation
		// DCSubject dcSubject = new DCSubject(modelSubject);
		// try {
		// annotation.appendNoRDFAnnotation(dcSubject.getTag());
		// } catch (XMLStreamException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // annotation.insertNoRDFAnnotation(dcSubject.getTag(), 0);

		return annotation;
	}

	/**
	 * Create a History element with the passed arguments. This History element
	 * is not added to the model.
	 * 
	 * @param createdDate
	 * @param modifiedDate
	 * @param creatorGivenName
	 * @param creatorFamilyName
	 * @param creatorContact
	 * 
	 * @return history
	 */
	private History createHistory(Date createdDate, Date modifiedDate,
			String creatorGivenName, String creatorFamilyName,
			String creatorContact) {
		History history = new History();

		if (createdDate != null)
			history.setCreatedDate(createdDate);

		if (modifiedDate != null)
			history.setModifiedDate(modifiedDate);

		Creator creator = new Creator(creatorGivenName, creatorFamilyName,
				null, creatorContact);
		history.addCreator(creator);

		return history;
	}

	/**
	 * Create a compartment with the name given. This compartment is not added
	 * to the model.
	 * 
	 * @param name
	 *            : Name of the compartment. If the name is null then the will
	 *            be assigned COMPARTMENT_MISSING.
	 * 
	 * @return comparment.
	 */
	private Compartment createCompartment(String name) {
		String compartmentName;
		String compartmentId;

		if (name == null) {
			compartmentId = COMPARTMENT_MISSING;
			compartmentName = COMPARTMENT_MISSING;
		} else {
			compartmentId = createId(name);
			compartmentName = name;
		}

		Compartment compartment = new Compartment(compartmentId);
		compartment.setName(compartmentName);
		return compartment;
	}

	/**
	 * Create a species element with a name and add it to the compartment
	 * passed. If the name is null then the species will be assigned
	 * SPECIES_MISSING. This species element will not be assigned to the model.
	 * 
	 * @param: name
	 * @param: comparment
	 * 
	 * @return: species
	 */
	private Species createSpecies(String name, Compartment compartment) {
		String speciesId;
		String speciesName;

		if (name == null) {
			speciesId = SPECIES_MISSING;
			speciesName = SPECIES_MISSING;
		} else {
			speciesId = createId(name);
			speciesName = name;
		}

		Species species = new Species(speciesId);
		species.setName(speciesName);
		species.setCompartment(compartment);

		return species;
	}

	/**
	 * Create a dependent parameter with a name given. This parameter is
	 * initialized to 0.0 and set as non constant.
	 * 
	 * @param name
	 * 
	 * @return: depParam
	 */
	private Parameter createDepParam(String name, String unit, Model model) {
		Parameter depParam = new Parameter(name);
		depParam.setValue(0.0);
		depParam.setConstant(false);

		String depSbmlUnit = Categories.getCategoryByUnit(unit).getSBML(unit);
		if (depSbmlUnit != null) {
			UnitDefinition unitDefinition = SBMLUtilities.fromXml(depSbmlUnit);
			Unit.Kind kind = SBMLUtilities.simplify(unitDefinition);
			UnitDefinition modelUnit = model.getUnitDefinition(unitDefinition
					.getId());

			if (kind != null) {
				depParam.setUnits(kind);
			} else if (modelUnit != null) {
				depParam.setUnits(modelUnit);
			} else {
				depParam.setUnits(SBMLUtilities.addUnitToModel(model,
						unitDefinition));
			}
		}
		return depParam;
	}

	/**
	 * Create the rule of the model from the formula passed.
	 * 
	 * @param origFormula
	 *            : Original formula from the DepXML cell. It has the following
	 *            format: Value=LOG10N0-Time/D*(((((D>0)))))
	 * @param depName
	 *            : Name of the dependent parameter
	 * @param depUnit
	 *            : Unit of the dependent parameter
	 * 
	 * @return: algebraic rule
	 */
	private AssignmentRule createModelRule(String origFormula, String depName,
			String depUnit) {
		// Get the right hand of the formula (trim value and =)
		int endIndex = origFormula.indexOf("=") + 1;
		String formula = origFormula.substring(endIndex);

		if (depUnit.startsWith("log")) {
			depName = "log10(" + depName + ")";
		} else if (depUnit.startsWith("ln")) {
			depName = "ln(" + depName + ")";
		}

		AssignmentRule rule = null;
		try {
			ASTNode math = parse(formula);
			rule = new AssignmentRule(math, LEVEL, VERSION);
			rule.setVariable(depName);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rule;
	}

	/**
	 * Parse parameters
	 * 
	 * @param model
	 * @param params
	 *            : Parameters.
	 * @param varParams
	 *            : Variable parameters.
	 */
	private void parseParams(Model model,
			List<PmmXmlElementConvertable> params, String varParams) {
		for (PmmXmlElementConvertable item : params) {
			ParamXml paramXml = (ParamXml) item;
			String paramName = paramXml.getName();
			Parameter param = model.createParameter(paramName);

			Boolean isConstant = paramName.equals(varParams);
			param.setConstant(isConstant);

			Double paramValue = paramXml.getValue();
			param.setValue((paramValue == null) ? 0.0 : paramValue);
		}
	}

	/**
	 * Parse independent parameters and add a formula.
	 * 
	 * @param model
	 *            :
	 * @param params
	 *            : Independent parameters.
	 * @param rules
	 *            : List of rules.
	 */
	private void parseIndepParams(Model model,
			List<PmmXmlElementConvertable> params, ListOf<Rule> rules) {
		for (PmmXmlElementConvertable item : params) {
			IndepXml indepXml = (IndepXml) item;

			if (indepXml.getName().equals(AttributeUtilities.TIME)) {
				indepXml.setName("time");
			}

			String indepXmlName = indepXml.getName();
			Parameter param = model.createParameter(indepXmlName);
			String sbmlUnit = Categories.getCategoryByUnit(indepXml.getUnit())
					.getSBML(indepXml.getUnit());

			param.setValue(0.0);
			param.setConstant(false);

			Double min = indepXml.getMin();
			Double max = indepXml.getMax();

			ASTNode mathNode = null;
			if (MathUtilities.isValid(min) && MathUtilities.isValid(max)) {
				try {
					mathNode = and(parse(indepXmlName + ">=" + min),
							parse(indepXmlName + "<=" + max));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (MathUtilities.isValid(min)) {
				try {
					mathNode = parse(indepXmlName + ">=" + min);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (MathUtilities.isValid(max)) {
				try {
					mathNode = parse(indepXmlName + "<=" + max);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (mathNode != null) {
				Rule rule = new AlgebraicRule(mathNode, LEVEL, VERSION);
				rules.add(rule);
			}

			if (sbmlUnit != null) {
				UnitDefinition unit = SBMLUtilities.fromXml(sbmlUnit);
				Unit.Kind kind = SBMLUtilities.simplify(unit);
				UnitDefinition modelUnit = model
						.getUnitDefinition(unit.getId());

				if (kind != null) {
					param.setUnits(kind);
				} else if (modelUnit != null) {
					param.setUnits(modelUnit);
				} else {
					param.setUnits(SBMLUtilities.addUnitToModel(model, unit));
				}

			}
		}

	}
}
