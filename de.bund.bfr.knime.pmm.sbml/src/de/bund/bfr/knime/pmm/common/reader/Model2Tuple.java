package de.bund.bfr.knime.pmm.common.reader;

import java.util.Map;

import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.Model2Metadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.EMLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.Model2Annotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.bund.bfr.pmf.sbml.SecDep;
import de.bund.bfr.pmf.sbml.SecIndep;

public class Model2Tuple {

	private KnimeTuple tuple;
	private static KnimeSchema schema = SchemaFactory.createM2Schema();

	public Model2Tuple(Model model) {

		Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

		// Parses rule
		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = Util.model2Rule2CatModel(rule);

		// Parses dep
		String depName = rule.getRule().getVariable();

		SecDep secDep = new SecDep(model.getParameter(depName));
		DepXml depXml = new DepXml(secDep.getParam().getId());
		depXml.setDescription(secDep.getDescription());
		if (secDep.getParam().isSetUnits()) {
			// Adds unit
			String unitID = secDep.getParam().getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.setUnit(unitName);

			// Adds unit category
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				UnitsFromDB dbUnit = dbUnits.get(unitName);
				depXml.setCategory(dbUnit.getKind_of_property_quantity());
			}

			// Adds limits
			if (limits.containsKey(secDep.getParam().getId())) {
				Limits depLimits = limits.get(secDep.getParam().getId());
				depXml.setMax(depLimits.getMax());
				depXml.setMin(depLimits.getMin());
			}
		}

		PmmXmlDoc indeps = new PmmXmlDoc();
		PmmXmlDoc consts = new PmmXmlDoc();

		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				ParamXml paramXml = processCoefficient(param, model.getListOfUnitDefinitions(), limits);
				consts.add(paramXml);
			} else if (!param.getId().equals(depName)) {
				IndepXml indepXml = processIndep(param, model.getListOfUnitDefinitions(), limits);
				indeps.add(indepXml);
			}
		}

		// Get model annotations
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		EstModelXml estModel = Util.uncertainties2EstModel(m2Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		Model2Metadata metadata = new Model2Metadata();

		if (model.getListOfSpecies().size() == 1) {
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			Model2AgentXml agentXml = new Model2AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
					species.getDetail(), null);
			metadata.setAgentXml(agentXml);
		}

		if (model.getListOfCompartments().size() == 1) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			Model2MatrixXml matrixXml = new Model2MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
					compartment.getDetail(), null);
			metadata.setMatrixXml(matrixXml);
		}

		// Gets model literature
		PmmXmlDoc mLits = new PmmXmlDoc();
		for (Reference ref : rule.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLits.add(literatureItem);

			MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(mLiteratureItem);
		}

		// Gets estimated model literature
		PmmXmlDoc emLits = new PmmXmlDoc();
		for (Reference ref : m2Annot.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type, comment);
			emLits.add(lit);

			EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(emLiteratureItem);
		}

		tuple = new KnimeTuple(schema);
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, indeps);
		tuple.setValue(Model2Schema.ATT_PARAMETER, consts);
		tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model2Schema.ATT_MLIT, mLits);
		tuple.setValue(Model2Schema.ATT_EMLIT, emLits);
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		tuple.setValue(Model2Schema.ATT_DBUUID, "?");
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Annot.getGlobalModelID());
		tuple.setValue(Model2Schema.ATT_METADATA, metadata);
	}

	private ParamXml processCoefficient(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), null, param.getValue());

		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.setUnit(unitName);
			paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
		if (coefficient.isSetDescription()) {
			paramXml.setDescription(coefficient.getDescription());
		}

		// Adds correlations
		if (coefficient.isSetCorrelations()) {
			for (Correlation corr : coefficient.getCorrelations()) {
				paramXml.addCorrelation(corr.getName(), corr.getValue());
			}
		}

		// Adds limits
		if (limits.containsKey(param.getId())) {
			Limits constLimits = limits.get(param.getId());
			paramXml.setMax(constLimits.getMax());
			paramXml.setMin(constLimits.getMin());
		}

		return paramXml;
	}

	private IndepXml processIndep(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {

		// Adds limits
		Double min = null;
		Double max = null;
		if (limits.containsKey(param.getId())) {
			Limits indepLimits = limits.get(param.getId());
			min = indepLimits.getMin();
			max = indepLimits.getMax();
		}

		IndepXml indepXml = new IndepXml(param.getId(), min, max);

		SecIndep secIndep = new SecIndep(param);
		indepXml.setDescription(secIndep.getDescription());

		// Adds unit and unit category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		return indepXml;
	}

	public KnimeTuple getTuple() {
		return tuple;
	}

	public void setTuple(KnimeTuple tuple) {
		this.tuple = tuple;
	}
}
