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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class JSONLiteratureListTest {

	@Test
	public void test() {
		// Florent Baty literature item
		String fbatyAuthor = "Florent Baty";
		Integer fbatyYear = 2012;
		String fbatyTitle = "Package `nlstools`";
		String fbatyAbstract = "";
		String fbatyJournal = "";
		String fbatyVolume = "";
		String fbatyIssue = "";
		Integer fbatyPage = null;
		Integer fbatyApprovalMode = null;
		String fbatyWebsite = "http://cran.r-project.org/web/packages/nlstools/";
		Integer fbatyType = null;
		String fbatyComment = "";
		Integer fbatyId = 240;

		LiteratureItem fbaty = new LiteratureItem(fbatyAuthor, fbatyYear,
				fbatyTitle, fbatyAbstract, fbatyJournal, fbatyVolume,
				fbatyIssue, fbatyPage, fbatyApprovalMode, fbatyWebsite,
				fbatyType, fbatyComment, fbatyId);

		// Jagannath literature item
		String jagAuthor = "Jagannath";
		Integer jagYear = 2005;
		String jagTitle = "Comparison of the thermal inactivation of Bacillus subtilis spores in&#xa;foods using the modified Weibull and Bigelow equations";
		String jagAbstract = "";
		String jagJournal = "Food Microbiology";
		String jagVolume = "22";
		String jagIssue = "";
		Integer jagPage = 233;
		Integer jagApprovalMode = null;
		String jagWebsite = "";
		Integer jagType = null;
		String jagComment = "";
		Integer jagId = 242;

		LiteratureItem jag = new LiteratureItem(jagAuthor, jagYear, jagTitle,
				jagAbstract, jagJournal, jagVolume, jagIssue, jagPage,
				jagApprovalMode, jagWebsite, jagType, jagComment, jagId);
		
		
		List<LiteratureItem> expectedLits = Arrays.asList(fbaty, jag);
		JSONLiteratureList jsonLiteratureList = new JSONLiteratureList(expectedLits);
		List<LiteratureItem> obtainedLits = jsonLiteratureList.toLiteratureItem();
		
		for (int i = 0; i < obtainedLits.size(); i++) {
			LiteratureItem expectedLit = expectedLits.get(i);
			LiteratureItem obtainedLit = obtainedLits.get(i);
			
			assertEquals(expectedLit.getAuthor(), obtainedLit.getAuthor());
			assertEquals(expectedLit.getYear(), obtainedLit.getYear());
			assertEquals(expectedLit.getTitle(), obtainedLit.getTitle());
			assertEquals(expectedLit.getAbstractText(), obtainedLit.getAbstractText());
			assertEquals(expectedLit.getJournal(), obtainedLit.getJournal());
			assertEquals(expectedLit.getVolume(), obtainedLit.getVolume());
			assertEquals(expectedLit.getIssue(), obtainedLit.getIssue());
			assertEquals(expectedLit.getPage(), obtainedLit.getPage());
			assertEquals(expectedLit.getApprovalMode(), obtainedLit.getApprovalMode());
			assertEquals(expectedLit.getWebsite(), obtainedLit.getWebsite());
			assertEquals(expectedLit.getType(), obtainedLit.getType());
			assertEquals(expectedLit.getComment(), obtainedLit.getComment());
			assertEquals(expectedLit.getId(), obtainedLit.getId());
		}
	}

}
