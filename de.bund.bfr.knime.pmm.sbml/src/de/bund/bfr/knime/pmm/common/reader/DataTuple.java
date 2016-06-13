package de.bund.bfr.knime.pmm.common.reader;

import java.util.HashMap;
import java.util.Map;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.TimeSeriesMetadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.MDAgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDMatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.numl.Tuple;
import de.bund.bfr.pmfml.sbml.Model1Annotation;
import de.bund.bfr.pmfml.sbml.ModelVariable;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceType;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class DataTuple {

	private KnimeTuple tuple;
	// time series schema
	private static KnimeSchema schema = SchemaFactory.createDataSchema();

	public DataTuple(NuMLDocument doc) {

		int condID = doc.getResultComponent().getCondID();
		String combaseID = (doc.getResultComponent().isSetCombaseID()) ? doc.getResultComponent().getCombaseID() : "?";

		String timeUnit = doc.getTimeOntologyTerm().getUnitDefinition().getName();
		String concUnit = doc.getConcentrationOntologyTerm().getUnitDefinition().getName();

		// Gets concentration unit object type from DB
		String concUnitObjectType;
		if (DBUnits.getDBUnits().containsKey(concUnit)) {
			UnitsFromDB ufdb = DBUnits.getDBUnits().get(concUnit);
			concUnitObjectType = ufdb.getObject_type();
		} else {
			concUnitObjectType = "";
		}

		PMFSpecies species = doc.getConcentrationOntologyTerm().getSpecies();
		AgentXml originalAgentXml = new AgentXml();
		originalAgentXml.setName(species.getName());
		MDAgentXml agentXml = new MDAgentXml();
		agentXml.setName(species.getName());
		if (species.isSetDetail()) {
			originalAgentXml.setDetail(species.getDetail());
			agentXml.setDetail(species.getDetail());
		}

		PMFCompartment compartment = doc.getConcentrationOntologyTerm().getCompartment();
		MatrixXml originalMatrixXml = new MatrixXml();
		MDMatrixXml matrixXml = new MDMatrixXml();
		originalMatrixXml.setName(compartment.getName());
		matrixXml.setName(compartment.getName());
		if (compartment.isSetDetail()) {
			originalMatrixXml.setDetail(compartment.getDetail());
			matrixXml.setDetail(compartment.getDetail());
		}

		// Gets time series
		Tuple[] dimensions = doc.getResultComponent().getDimensions();
		double[][] data = new double[dimensions.length][2];
		for (int i = 0; i < dimensions.length; i++) {
			Tuple tuple = dimensions[i];
			data[i] = new double[] { tuple.getConcValue().getValue(), tuple.getTimeValue().getValue() };
		}
		PmmXmlDoc mdData = Util.createTimeSeries(timeUnit, concUnit, concUnitObjectType, data);

		// Gets model variables
		ModelVariable[] modelVariables = compartment.getModelVariables();
		Map<String, Double> miscs = new HashMap<>(modelVariables.length);
		for (ModelVariable modelVariable : modelVariables) {
			miscs.put(modelVariable.getName(), modelVariable.getValue());
		}
		PmmXmlDoc miscDoc = Util.parseMiscs(miscs);

		// Creates empty model info
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		// Gets literature items
		PmmXmlDoc litDoc = new PmmXmlDoc();
		for (Reference reference : doc.getResultComponent().getReferences()) {
			String author = (reference.isSetAuthor()) ? reference.getAuthor() : null;
			Integer year = (reference.isSetYear()) ? reference.getYear() : null;
			String title = (reference.isSetTitle()) ? reference.getTitle() : null;
			String abstractText = (reference.isSetAbstractText()) ? reference.getAbstractText() : null;
			String journal = (reference.isSetJournal()) ? reference.getJournal() : null;
			String volume = (reference.isSetVolume()) ? reference.getVolume() : null;
			String issue = (reference.isSetIssue()) ? reference.getIssue() : null;
			Integer page = (reference.isSetPage()) ? reference.getPage() : null;
			Integer approvalMode = (reference.isSetApprovalMode()) ? reference.getApprovalMode() : null;
			String website = (reference.isSetWebsite()) ? reference.getWebsite() : null;
			ReferenceType type = (reference.isSetType()) ? reference.getType() : null;
			String comment = (reference.isSetComment()) ? reference.getComment() : null;

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type.value(), comment);
			litDoc.add(lit);

			MDLiteratureItem mdLit = new MDLiteratureItem(author, year, title, abstractText, journal, volume, issue,
					page, approvalMode, website, type.value(), comment);
			metadata.addLiteratureItem(mdLit);
		}

		// Creates and fills tuple
		tuple = new KnimeTuple(schema);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, litDoc);
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}

	public DataTuple(SBMLDocument sbmlDoc) {

		Model model = sbmlDoc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		AgentXml originalAgentXml = new AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);
		MDAgentXml agentXml = new MDAgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		MatrixXml originalMatrixXml = new MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);
		MDMatrixXml matrixXml = new MDMatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		PmmXmlDoc miscCell = new PmmXmlDoc();
		if (compartment.isSetModelVariables()) {
			Map<String, Double> miscs = new HashMap<String, Double>(compartment.getModelVariables().length);
			for (ModelVariable modelVariable : compartment.getModelVariables()) {
				miscs.put(modelVariable.getName(), modelVariable.getValue());
			}
			miscCell = Util.parseMiscs(miscs);
		}

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		tuple = new KnimeTuple(schema);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}

	public KnimeTuple getTuple() {
		return tuple;
	}

	public void setTuple(KnimeTuple tuple) {
		this.tuple = tuple;
	}
}