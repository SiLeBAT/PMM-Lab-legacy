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

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Constraint;
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
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
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

public class TableReader {

	private final static String COMPARTMENT_MISSING = "CompartmentMissing";
	private final static String SPECIES_MISSING = "SpeciesMissing";

	private Map<String, SBMLDocument> documents;

	public TableReader(List<KnimeTuple> tuples, String varParams,
			String creatorGivenName, String creatorFamilyName,
			String creatorContact, Date createdDate, Date modifiedDate,
			String reference) throws IOException, XMLStreamException {
		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		Set<Integer> idSet = new LinkedHashSet<>();
		Map<KnimeTuple, List<KnimeTuple>> tupleMap;

		if (isTertiaryModel) {
			tupleMap = new ModelCombiner(tuples, true, null, null)
					.getTupleCombinations();
		} else {
			tupleMap = new LinkedHashMap<>();

			for (KnimeTuple tuple : tuples) {
				tupleMap.put(tuple, Arrays.asList(tuple));
			}
		}

		documents = new LinkedHashMap<>();

		for (KnimeTuple tuple : tupleMap.keySet()) {
			replaceCelsiusAndFahrenheit(tuple);
			renameLog(tuple);

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

			if (!idSet.add(id)) {
				continue;
			}

			History history = new History();

			if (createdDate != null) {
				history.setCreatedDate(createdDate);
			}

			if (modifiedDate != null) {
				history.setModifiedDate(modifiedDate);
			}

			history.addCreator(new Creator(creatorGivenName, creatorFamilyName,
					null, creatorContact));

			String modelName = ((EstModelXml) tupleMap.get(tuple).get(0)
					.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0)).getName();

			if (modelName == null || modelName.trim().isEmpty()) {
				throw new IOException("Model Name missing");
			}

			String modelID = createId(modelName);
			SBMLDocument doc = new SBMLDocument(2, 4);
			Model model = doc.createModel(modelID);

			model.setMetaId("Meta_" + modelID);
			model.setName(modelID);
			model.setHistory(history);
			// model.setNotes(XMLNode.convertStringToXMLNode("<notes>" +
			// reference
			// + "</notes>"));

			Compartment c;
			Species s;

			if (matrixXml.getName() != null) {
				c = model.createCompartment(createId(matrixXml.getName()));
				c.setName(matrixXml.getName());
			} else {
				c = model.createCompartment(COMPARTMENT_MISSING);
				c.setName(COMPARTMENT_MISSING);
			}

			if (organismXml.getName() != null) {
				s = model.createSpecies(createId(organismXml.getName()), c);
				s.setName(organismXml.getName());
			} else {
				s = model.createSpecies(SPECIES_MISSING, c);
				s.setName(SPECIES_MISSING);
			}

			ListOf<Rule> rules = new ListOf<>(2, 4);
			ListOf<Constraint> constraints = new ListOf<>(2, 4);
			Parameter depParam = model.createParameter(depXml.getName());
			String depSbmlUnit = Categories.getCategoryByUnit(depXml.getUnit())
					.getSBML(depXml.getUnit());

			depParam.setValue(0.0);
			depParam.setConstant(false);

			if (depSbmlUnit != null) {
				UnitDefinition unit = SBMLUtilities.fromXml(depSbmlUnit);
				Unit.Kind kind = SBMLUtilities.simplify(unit);
				UnitDefinition modelUnit = model
						.getUnitDefinition(unit.getId());

				if (kind != null) {
					depParam.setUnits(kind);
				} else if (modelUnit != null) {
					depParam.setUnits(modelUnit);
				} else {
					depParam.setUnits(SBMLUtilities.addUnitToModel(model, unit));
				}
			}

			String formula = modelXml.getFormula().substring(
					modelXml.getFormula().indexOf("=") + 1);

			try {
				rules.add(new AssignmentRule(parse(formula), depParam));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml paramXml = (ParamXml) el;
				Parameter param = model.createParameter(paramXml.getName());

				if (paramXml.getName().equals(varParams)) {
					param.setConstant(false);
				} else {
					param.setConstant(true);
				}

				if (paramXml.getValue() != null) {
					param.setValue(paramXml.getValue());
				} else {
					param.setValue(0.0);
				}
			}

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indepXml = (IndepXml) el;

				if (indepXml.getName().equals(AttributeUtilities.TIME)) {
					indepXml.setName("time");
				}

				String name = indepXml.getName();
				Parameter param = model.createParameter(name);
				String sbmlUnit = Categories.getCategoryByUnit(
						indepXml.getUnit()).getSBML(indepXml.getUnit());

				param.setValue(0.0);
				param.setConstant(false);

				Double min = indepXml.getMin();
				Double max = indepXml.getMax();

				if (MathUtilities.isValid(min)) {
					try {
						constraints.add(new Constraint(
								parse(name + ">=" + min), 2, 4));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (MathUtilities.isValid(max)) {
					try {
						constraints.add(new Constraint(
								parse(name + "<=" + max), 2, 4));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (sbmlUnit != null) {
					UnitDefinition unit = SBMLUtilities.fromXml(sbmlUnit);
					Unit.Kind kind = SBMLUtilities.simplify(unit);
					UnitDefinition modelUnit = model.getUnitDefinition(unit
							.getId());

					if (kind != null) {
						param.setUnits(kind);
					} else if (modelUnit != null) {
						param.setUnits(modelUnit);
					} else {
						param.setUnits(SBMLUtilities
								.addUnitToModel(model, unit));
					}
				}
			}

			model.setListOfRules(rules);
			model.setListOfConstraints(constraints);

			if (documents.containsKey(modelID)) {
				throw new IOException("Duplicate model name: " + modelID);
			}

			documents.put(modelID, doc);
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
}
