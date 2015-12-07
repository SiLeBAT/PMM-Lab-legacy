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
package de.bund.bfr.knime.pmm.jsonutil;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class JSONLiteratureItemTest {

	@Test
	public void test() {

		Integer id = 265;
		String author = "Huang, L.";
		String title = "USDA Integrated Pathogen Modeling Program";
		String abstractText = null;
		Integer year = 2013;
		String journal = null;
		String volume = null;
		String issue = null;
		Integer page = null;
		Integer approvalMode = null;
		String website = "http://www.ars.usda.gov/Main/docs.htm?docid=23355";
		Integer type = null;
		String comment = null;
		String dbuuid = "c6582d2c-7cd6-4ee3-b425-68662b2558d9";

		LiteratureItem literatureItem = new LiteratureItem(author, year, title,
				abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment, id, dbuuid);
		JSONLiteratureItem jsonLiteratureItem = new JSONLiteratureItem(literatureItem);
		literatureItem = jsonLiteratureItem.toLiteratureItem();
		
		// Tests jsonLiteratureItem
		assertEquals(id, literatureItem.getId());
		assertEquals(author, literatureItem.getAuthor());
		assertEquals(title, literatureItem.getTitle());
		assertEquals(abstractText, literatureItem.getAbstractText());
		assertEquals(year, literatureItem.getYear());
		assertEquals(journal, literatureItem.getJournal());
		assertEquals(volume, literatureItem.getVolume());
		assertEquals(issue, literatureItem.getIssue());
		assertEquals(approvalMode, literatureItem.getApprovalMode());
		assertEquals(website, literatureItem.getWebsite());
		assertEquals(type, literatureItem.getType());
		assertEquals(comment, literatureItem.getComment());
		assertEquals(dbuuid, literatureItem.getDbuuid());
	}
}
