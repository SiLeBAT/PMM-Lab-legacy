package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.bund.bfr.numl.AtomicDescription;
import de.bund.bfr.numl.CompositeDescription;
import de.bund.bfr.numl.DataType;
import de.bund.bfr.numl.Description;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLWriter;
import de.bund.bfr.numl.OntologyTerm;
import de.bund.bfr.numl.ResultComponent;
import de.bund.bfr.numl.TupleDescription;

public class NuMLWriterTest {

	public static void main(String[] args) throws IOException,
			URISyntaxException {
		OntologyTerm time = new OntologyTerm();
		OntologyTerm mass = new OntologyTerm();
		OntologyTerm concentration = new OntologyTerm();

		time.setTerm("time");
		time.setSourceTermId("SBO:0000345");
		time.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		mass.setTerm("mass");
		mass.setSourceTermId("SBO:0000345");
		mass.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		concentration.setTerm("concentration");
		concentration.setSourceTermId("SBO:0000196");
		concentration.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		AtomicDescription massDescription = new AtomicDescription();
		AtomicDescription concentrationDescription = new AtomicDescription();
		TupleDescription tupleDescription = new TupleDescription();

		massDescription.setName("mass");
		massDescription.setOntologyTerm(mass);
		massDescription.setValueType(DataType.Double);

		concentrationDescription.setName("concentration");
		concentrationDescription.setOntologyTerm(concentration);
		concentrationDescription.setValueType(DataType.Double);

		tupleDescription.setDescriptions(Arrays.asList(
				(Description) massDescription,
				(Description) concentrationDescription));

		CompositeDescription description = new CompositeDescription();

		description.setName("Time");
		description.setIndexType(DataType.Integer);
		description.setOntologyTerm(time);
		description.setDescription(tupleDescription);

		Map<Integer, List<Double>> dimension = new LinkedHashMap<>();

		dimension.put(0, Arrays.asList(0.11, 0.12));
		dimension.put(1, Arrays.asList(0.13, 0.11));
		dimension.put(2, Arrays.asList(0.14, 0.10));
		dimension.put(3, Arrays.asList(0.15, 0.11));

		ResultComponent resultComponent = new ResultComponent();

		resultComponent.setId("exp1");
		resultComponent.setDimensionDescription(description);
		resultComponent.setDimension(dimension);

		NuMLDocument doc = new NuMLDocument();

		doc.setResultComponents(Arrays.asList(resultComponent));

		NuMLWriter writer = new NuMLWriter();

//		writer.write(doc, new File("test.xml"));
		writer.write(doc, new File("C:/Users/Malba/Desktop/test.xml"));
	}
}
