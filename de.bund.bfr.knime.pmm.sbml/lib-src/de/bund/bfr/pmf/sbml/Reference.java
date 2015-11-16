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
package de.bund.bfr.pmf.sbml;

/**
 * Interface for reference items.
 *
 * @author Miguel Alba
 */
public interface Reference {

	/** Returns the author of this {@link Reference}. */
	public String getAuthor();
	
	/** Returns the year of this {@link Reference}. */
	public Integer getYear();
	
	/** Returns the title of this {@link Reference}. */
	public String getTitle();

	/** Returns the abstract text of this {@link Reference}. */
	public String getAbstractText();

	/** Returns the journal of this {@link Reference}. */
	public String getJournal();

	/** Returns the volume of this {@link Reference}. */
	public String getVolume();

	/** Returns the issue of this {@link Reference}. */
	public String getIssue();

	/** Returns the page of this {@link Reference}. */
	public Integer getPage();

	/** Returns the approval mode of this {@link Reference}. */
	public Integer getApprovalMode();

	/** Returns the website of this {@link Reference}. */
	public String getWebsite();

	/** Returns the {@link ReferenceType} of this {@link Reference}. */
	public ReferenceType getType();

	/** Returns the comment of this {@link Reference}. */
	public String getComment();

	/** Sets the author value with 'author'. */
	public void setAuthor(String author);

	/** Sets the year value with 'year'. */
	public void setYear(Integer year);

	/** Sets the title value with 'title'. */
	public void setTitle(String title);

	/** Sets the abstract text value with 'abstractText'. */
	public void setAbstractText(String abstractText);

	/** Sets the journal value with 'journal'. */
	public void setJournal(String journal);

	/** Sets the volume value with 'volume'. */
	public void setVolume(String volume);

	/** Sets the issue value with 'issue'. */
	public void setIssue(String issue);

	/** Sets the page value with 'page'. */
	public void setPage(Integer page);

	/** Sets the approval mode value with 'approvalMode'. */
	public void setApprovalMode(Integer approvalMode);

	/** Sets the website value with 'website'. */
	public void setWebsite(String website);

	/** Sets the {@link ReferenceType} value with 'type'. */
	public void setType(ReferenceType type);

	/** Sets the comment value with 'comment'. */
	public void setComment(String comment);

	/** Returns true if the author of this {@link Reference} is set. */
	public boolean isSetAuthor();
	
	/** Returns true if the year of this {@link Reference} is set. */
	public boolean isSetYear();
	
	/** Returns true if the title of this {@link Reference} is set. */
	public boolean isSetTitle();

	/** Returns true if the abstract text of this {@link Reference} is set. */
	public boolean isSetAbstractText();
		
	/** Returns true if the journal of this {@link Reference} is set. */
	public boolean isSetJournal();

	/** Returns true if the volume of this {@link Reference} is set. */
	public boolean isSetVolume();

	/** Returns true if the issue of this {@link Reference} is set. */
	public boolean isSetIssue();

	/** Returns true if the page of this {@link Reference} is set. */
	public boolean isSetPage();
	
	/** Returns true if the approval mode of this {@link Reference} is set. */
	public boolean isSetApprovalMode();

	/** Returns true if the website of this {@link Reference} is set. */
	public boolean isSetWebsite();

	/** Returns true if the {@link ReferenceType} of this {@link Reference} is set. */
	public boolean isSetType();

	/** Returns true if the comment of this {@link Reference} is set. */
	public boolean isSetComment();
}
