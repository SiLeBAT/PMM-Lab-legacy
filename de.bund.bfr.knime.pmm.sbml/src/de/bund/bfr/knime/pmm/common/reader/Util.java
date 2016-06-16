package de.bund.bfr.knime.pmm.common.reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.Uncertainties;

public class Util {

	private Util() {
	}

	/**
	 * Parses a list of constraints and returns a dictionary that maps variables
	 * and their limit values.
	 * 
	 * @param constraints
	 */
	public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
		Map<String, Limits> paramLimits = new HashMap<>();

		for (Constraint currConstraint : constraints) {
			LimitsConstraint lc = new LimitsConstraint(currConstraint);
			Limits lcLimits = lc.getLimits();
			paramLimits.put(lcLimits.getVar(), lcLimits);
		}

		return paramLimits;
	}

	/**
	 * Parses misc items.
	 * 
	 * @param miscs
	 *            . Dictionary that maps miscs names and their values.
	 * @return
	 */
	public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		if (miscs != null) {
			// First misc item has id -1 and the rest of items have negative
			// ints
			int counter = -1;
			for (Entry<String, Double> entry : miscs.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				List<String> categories;
				String description, unit;

				switch (name) {
				case "Temperature":
					categories = Arrays.asList(Categories.getTempCategory().getName());
					description = name;
					unit = Categories.getTempCategory().getStandardUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;

				case "pH":
					categories = Arrays.asList(Categories.getPhCategory().getName());
					description = name;
					unit = Categories.getPhUnit();

					cell.add(new MiscXml(counter, name, description, value, categories, unit));

					counter -= 1;
					break;
				}
			}
		}
		return cell;
	}

	/**
	 * Creates time series
	 */
	public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit, String concUnitObjectType,
			double[][] data) {

		PmmXmlDoc mdData = new PmmXmlDoc();

		Double concStdDev = null;
		Integer numberOfMeasurements = null;

		for (double[] point : data) {
			double conc = point[0];
			double time = point[1];
			String name = "t" + mdData.size();

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
			t.setConcentrationUnitObjectType(concUnitObjectType);
			mdData.add(t);
		}

		return mdData;
	}

	public static EstModelXml uncertainties2EstModel(Uncertainties uncertainties) {
		int estModelId = uncertainties.getID();
		String modelName = uncertainties.getModelName();
		Double sse = uncertainties.getSSE();
		Double rms = uncertainties.getRMS();
		Double r2 = uncertainties.getR2();
		Double aic = uncertainties.getAIC();
		Double bic = uncertainties.getBIC();
		Integer dof = uncertainties.getDOF();
		EstModelXml estModel = new EstModelXml(estModelId, modelName, sse, rms, r2, aic, bic, dof);
		return estModel;
	}

	public static CatalogModelXml model1Rule2CatModel(ModelRule rule) {
		int formulaId = rule.getPmmlabID();
		String formulaName = rule.getFormulaName();
		ModelClass modelClass = rule.getModelClass();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");

		String formula = String.format("Value=%s", formulaString);

		CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
		return catModel;
	}

	public static CatalogModelXml model2Rule2CatModel(ModelRule rule) {
		int formulaId = rule.getPmmlabID();
		String formulaName = rule.getFormulaName();
		ModelClass modelClass = rule.getModelClass();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");

		String formula = String.format("%s=%s", rule.getVariable(), formulaString);

		CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
		return catModel;
	}

	public static KnimeTuple mergeTuples(KnimeTuple dataTuple, KnimeTuple m1Tuple, KnimeTuple m2Tuple) {

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

		// Copies model1 columns
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
		tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

		// Copies model2 columns
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
		tuple.setValue(Model2Schema.ATT_PARAMETER, m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
		tuple.setValue(Model2Schema.ATT_ESTMODEL, m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL));
		tuple.setValue(Model2Schema.ATT_MLIT, m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
		tuple.setValue(Model2Schema.ATT_EMLIT, m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model2Schema.ATT_DBUUID, m2Tuple.getString(Model2Schema.ATT_DBUUID));
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
		tuple.setValue(Model2Schema.ATT_METADATA, m2Tuple.getPmmXml(Model2Schema.ATT_METADATA));

		return tuple;
	}
}