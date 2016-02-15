/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.pmf.sbml;

/**
 * Interface for reference items. Holds:
 * <ul>
 * <li>Author</li>
 * <li>Year</li>
 * <li>Title</li>
 * <li>Abstract text</li>
 * <li>Journal</li>
 * <li>Volume</li>
 * <li>Issue</li>
 * <li>Page</li>
 * <li>Approval mode</li>
 * <li>Website</li>
 * <li>Type</li>
 * <li>Comment</li>
 *
 * @author Miguel Alba
 */
public interface Reference {

  /** Returns the author of this {@link Reference}. If author is not set returns null. */
  public String getAuthor();

  /** Returns the year of this {@link Reference}. If year is not set returns null. */
  public Integer getYear();

  /** Returns the title of this {@link Reference}. If title is not returns null. */
  public String getTitle();

  /**
   * Returns the abstract text of this {@link Reference}. If abstract text is not set returns null.
   */
  public String getAbstractText();

  /** Returns the journal of this {@link Reference}. If journal is not set returns null. */
  public String getJournal();

  /** Returns the volume of this {@link Reference}. If volume is not set returns null. */
  public String getVolume();

  /** Returns the issue of this {@link Reference}. If issue is not set returns null. */
  public String getIssue();

  /** Returns the page of this {@link Reference}. If page is not set returns null. */
  public Integer getPage();

  /**
   * Returns the approval mode of this {@link Reference}. If approval mode is not set returns null.
   */
  public Integer getApprovalMode();

  /** Returns the website of this {@link Reference}. If website is not set returns null. */
  public String getWebsite();

  /**
   * Returns the {@link ReferenceType} of this {@link Reference}. If type is not set returns null.
   */
  public ReferenceType getType();

  /** Returns the comment of this {@link Reference}. If comment is not set returns null. */
  public String getComment();

  /** Sets the author value with 'author'. If 'author' is null or empty it will do nothing. */
  public void setAuthor(final String author);

  /** Sets the year value with 'year'. */
  public void setYear(final int year);

  /** Sets the title value with 'title'. If 'title' is null or empty it will do nothing. */
  public void setTitle(final String title);

  /**
   * Sets the abstract text value with 'abstractText'. If 'abstractText' is null or empty it will do
   * nothing.
   */
  public void setAbstractText(final String abstractText);

  /** Sets the journal value with 'journal'. If 'journal' is null or empty it will do nothing. */
  public void setJournal(final String journal);

  /** Sets the volume value with 'volume'. If 'volume' is null or empty it will do nothing. */
  public void setVolume(final String volume);

  /** Sets the issue value with 'issue'. If 'issue' is null or empty it will do nothing. */
  public void setIssue(final String issue);

  /** Sets the page value with 'page'. */
  public void setPage(final int page);

  /**
   * Sets the approval mode value with 'approvalMode'. If 'approvalMode' is null or empty it will do
   * nothing.
   */
  public void setApprovalMode(final Integer approvalMode);

  /** Sets the website value with 'website'. If 'website' is null or empty it will do nothing. */
  public void setWebsite(final String website);

  /** Sets the {@link ReferenceType} value with 'type'. If 'type' is null it will do nothing. */
  public void setType(final ReferenceType type);

  /** Sets the comment value with 'comment'. If 'comment' is null or empty it will do nothing. */
  public void setComment(final String comment);

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
