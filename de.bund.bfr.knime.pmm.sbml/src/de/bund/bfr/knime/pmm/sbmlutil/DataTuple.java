package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.annotation.Model1Annotation;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.numl.NuMLDocument;

public class DataTuple {

	static KnimeSchema schema = SchemaFactory.createDataSchema(); // time series schema
	KnimeTuple tuple;

	public DataTuple(NuMLDocument numlDocument) {

		DataFile df = new DataFile(numlDocument);

		String timeUnit = df.getTimeUnit();
		String concUnit = df.getConcUnit();

		// Gets concentration unit object type from DB
		UnitsFromDB ufdb = DBUnits.getDBUnits().get(concUnit);
		String concUnitObjectType = ufdb.getObject_type();

		// Gets time series
		PmmXmlDoc mdData = ReaderUtils.createTimeSeries(timeUnit, concUnit, concUnitObjectType, df.getData());

		// Gets model variables
		PmmXmlDoc miscDoc = ReaderUtils.parseMiscs(df.getMiscs());

		// Gets literature items
		PmmXmlDoc litDoc = new PmmXmlDoc();
		for (LiteratureItem literatureItem : df.getLits()) {
			litDoc.add(literatureItem);
		}

		// Creates empty model info
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		// Creates and fills tuple
		tuple = new KnimeTuple(schema);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, df.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, df.getCombaseID());
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(df.getAgent()));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(df.getMatrix()));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, litDoc);
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
	}
	
	public DataTuple(SBMLDocument sbmlDoc) {

		Model model = sbmlDoc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());
		
		Agent agent = new Agent(model.getSpecies(0));
		Matrix matrix = new Matrix(model.getCompartment(0));
		
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);
		
		tuple = new KnimeTuple(SchemaFactory.createDataSchema());
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agent.toAgentXml()));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrix.toMatrixXml()));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
	}

	public KnimeTuple getTuple() {
		return tuple;
	}
}