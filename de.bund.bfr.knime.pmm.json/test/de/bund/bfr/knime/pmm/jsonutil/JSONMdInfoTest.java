package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

public class JSONMdInfoTest {

	@Test
	public void test() {
		
		Integer id = 1;
		String name = "i1";
		String comment = "";
		Integer qualityScore = null;
		Boolean checked = null;
		
		MdInfoXml mdInfo = new MdInfoXml(id, name, comment, qualityScore, checked);
		JSONMdInfo jsonMdInfo = new JSONMdInfo(mdInfo);
		mdInfo = jsonMdInfo.toMdInfoXml();
		
		// Tests mdInfo
		assertEquals(id, mdInfo.getId());
		assertEquals(name, mdInfo.getName());
		assertEquals(comment, mdInfo.getComment());
		assertEquals(qualityScore, mdInfo.getQualityScore());
		assertEquals(checked, mdInfo.getChecked());
	}
}
