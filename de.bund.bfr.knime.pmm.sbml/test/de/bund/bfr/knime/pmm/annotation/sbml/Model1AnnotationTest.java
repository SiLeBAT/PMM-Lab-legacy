package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.Model1Annotation;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

public class Model1AnnotationTest {

	@Test
	public void test() {

		EstModelXml estModel = new EstModelXml(1, "myexp", 0.5, 0.8, 0.2, 0.3, 0.2, 2);
		estModel.setComment("old experiment");
		Uncertainties uncertainties = new Uncertainties(estModel);

		LiteratureItem lit = new LiteratureItem("Baranyi, J.", 1994,
				"A dynamic approach to predicting bacterial growth in food",
				"A new member of the family of growth models described by Baranyi et al. (1993a)...",
				"International Journal of Food Microbiology", "23", "3", 277, 1,
				"http://www.sciencedirect.com/science/article/pii/0168160594901570", 1, "");

		int condID = -1968201666;
		
		Model1Annotation ma = new Model1Annotation(uncertainties, Arrays.asList(lit), condID);
		Model1Annotation ma2 = new Model1Annotation(ma.getAnnotation());

		assertEquals(uncertainties, ma2.getUncertainties());

		LiteratureItem lit2 = ma2.getLits().get(0);
		assertEquals(lit.getAuthor(), lit2.getAuthor());
		assertEquals(lit.getTitle(), lit2.getTitle());
		assertEquals(lit.getAbstractText(), lit2.getAbstractText());
		assertEquals(lit.getYear(), lit2.getYear());
		assertEquals(lit.getJournal(), lit2.getJournal());
		assertEquals(lit.getVolume(), lit2.getVolume());
		assertEquals(lit.getIssue(), lit2.getIssue());
		assertEquals(lit.getPage(), lit2.getPage());
		assertEquals(lit.getApprovalMode(), lit2.getApprovalMode());
		assertEquals(lit.getWebsite(), lit2.getWebsite());
		assertEquals(lit.getType(), lit2.getType());
		assertEquals(lit.getComment(), lit2.getComment());
		assertEquals(lit.getDbuuid(), lit2.getDbuuid());

		assertEquals(condID, ma2.getCondID());
	}
}
