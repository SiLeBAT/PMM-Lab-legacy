package org.sbml.jsbml.ext.pmf;


import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 */
public class PMFReference extends AbstractSBase {

  private static final long serialVersionUID = -4222919106019745845L;
  
  private String  author;
  private Integer year;
  private String  title;
  private String  abstractText;
  private String  journal;
  private String  volume;
  private String  issue;
  private Integer page;
  private Integer approvalMode;
  private String  website;
  private Integer referenceType; // TODO: should use a ReferenceType enum
  private String  comment;


  /** Creates a {@link PMFReference} instance. */
  public PMFReference() {
    super();
    this.packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link PMFReference} instance with a level and version. */
  public PMFReference(int level, int version) {
    super(level, version);
    this.packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public PMFReference(PMFReference reference) {
    super(reference);
  }


  /** Clones this class. */
  @Override
  public PMFReference clone() {
    return new PMFReference(this);
  }


  // *** author ***
  /**
   * Returns author.
   *
   * @return author.
   */
  public String getAuthor() {
    return this.author;
  }


  /**
   * Returns whether author is set.
   *
   * @return whether author is set.
   */
  public boolean isSetAuthor() {
    return this.author != null;
  }


  /**
   * Sets author.
   *
   * @param author
   */
  public void setAuthor(String author) {
    String oldAuthor = this.author;
    this.author = author;
    firePropertyChange("author", oldAuthor, this.author);
  }


  /**
   * Unsets the variable author.
   *
   * @return {@code true}, if author was set before, otherwise {@code false}.
   */
  public boolean unsetAuthor() {
    if (isSetAuthor()) {
      String oldAuthor = this.author;
      this.author = null;
      firePropertyChange("author", oldAuthor, this.author);
      return true;
    }
    return false;
  }


  // *** year ***
  /**
   * Returns year.
   *
   * @return year.
   */
  public int getYear() {
    if (isSetYear()) {
      return this.year.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("year", this);
  }


  /**
   * Returns whether year is set.
   *
   * @return whether year is set.
   */
  public boolean isSetYear() {
    return this.year != null;
  }


  /**
   * Sets year.
   *
   * @param year.
   */
  public void setYear(int year) {
    Integer oldYear = this.year;
    this.year = Integer.valueOf(year);
    firePropertyChange("year", oldYear, this.year);
  }


  /**
   * Unsets year.
   *
   * @return {@code true}, if year was set before, otherwise {@code false}.
   */
  public boolean unsetYear() {
    if (isSetYear()) {
      Integer oldYear = this.year;
      this.year = null;
      firePropertyChange("year", oldYear, this.year);
      return true;
    }
    return false;
  }


  // *** title ***
  /**
   * Returns title.
   *
   * @return title.
   */
  public String getTitle() {
    return this.title;
  }


  /**
   * Returns whether title is set.
   *
   * @return whether title is set.
   */
  public boolean isSetTitle() {
    return this.title != null;
  }


  /**
   * Sets title.
   *
   * @param title.
   */
  public void setTitle(String title) {
    String oldTitle = this.title;
    this.title = title;
    firePropertyChange("title", oldTitle, this.title);
  }


  /**
   * Unsets the variable title.
   *
   * @return {@code true}, if title was set before, otherwise {@code false}.
   */
  public boolean unsetTitle() {
    if (isSetTitle()) {
      String oldTitle = this.title;
      this.title = null;
      firePropertyChange("title", oldTitle, this.title);
      return true;
    }
    return false;
  }


  // *** abstract ***
  /**
   * Returns abstract.
   *
   * @return abstract.
   */
  public String getAbstractText() {
    return this.abstractText;
  }


  /**
   * Returns whether abstractText is set.
   *
   * @return whether abstractText is set.
   */
  public boolean isSetAbstractText() {
    return this.abstractText != null;
  }


  /**
   * Sets abstractText.
   *
   * @param abstractText.
   */
  public void setAbstractText(String abstractText) {
    String oldAbstractText = this.abstractText;
    this.abstractText = abstractText;
    firePropertyChange("abstractText", oldAbstractText, this.abstractText);
  }


  /**
   * Unsets the abstractText.
   *
   * @return {@code true}, if abstractText was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetAbstractText() {
    if (isSetAbstractText()) {
      String oldAbstractText = this.abstractText;
      this.abstractText = null;
      firePropertyChange("abstractText", oldAbstractText, this.abstractText);
      return true;
    }
    return false;
  }


  // *** journal ***
  /**
   * Returns journal.
   *
   * @return journal.
   */
  public String getJournal() {
    return this.journal;
  }


  /**
   * Returns whether journal is set.
   *
   * @return whether journal is set.
   */
  public boolean isSetJournal() {
    return this.journal != null;
  }


  /**
   * Sets journal.
   *
   * @param journal.
   */
  public void setJournal(String journal) {
    String oldJournal = this.journal;
    this.journal = journal;
    firePropertyChange("journal", oldJournal, this.journal);
  }


  /**
   * Unsets the journal.
   *
   * @return {@code true}, if journal was set before, otherwise {@code false}.
   */
  public boolean unsetJournal() {
    if (isSetJournal()) {
      String oldJournal = this.journal;
      this.journal = null;
      firePropertyChange("journal", oldJournal, this.journal);
      return true;
    }
    return false;
  }


  // *** volume ***
  /**
   * Returns volume.
   *
   * @return volume.
   */
  public String getVolume() {
    return this.volume;
  }


  /**
   * Returns whether volume is set.
   *
   * @return whether volume is set.
   */
  public boolean isSetVolume() {
    return this.volume != null;
  }


  /**
   * Sets volume.
   *
   * @param volume.
   */
  public void setVolume(String volume) {
    String oldVolume = this.volume;
    this.volume = volume;
    firePropertyChange("volume", oldVolume, this.volume);
  }


  /**
   * Unsets the volume.
   *
   * @return {@code true}, if volume was set before, otherwise {@code false}.
   */
  public boolean unsetVolume() {
    if (isSetVolume()) {
      String oldVolume = this.volume;
      this.volume = null;
      firePropertyChange("volume", oldVolume, this.volume);
      return true;
    }
    return false;
  }


  // *** issue ***
  /**
   * Returns issue.
   *
   * @return issue.
   */
  public String getIssue() {
    return this.issue;
  }


  /**
   * Returns whether issue is set.
   *
   * @return whether issue is set.
   */
  public boolean isSetIssue() {
    return this.issue != null;
  }


  /**
   * Sets issue.
   *
   * @param issue.
   */
  public void setIssue(String issue) {
    String oldIssue = this.issue;
    this.issue = issue;
    firePropertyChange("issue", oldIssue, this.issue);
  }


  /**
   * Unsets the issue.
   *
   * @return {@code true}, if issue was set before, otherwise {@code false}.
   */
  public boolean unsetIssue() {
    if (isSetIssue()) {
      String oldIssue = this.issue;
      this.issue = null;
      firePropertyChange("issue", oldIssue, this.issue);
      return true;
    }
    return false;
  }


  // *** page ***
  /**
   * Returns page.
   *
   * @return page.
   */
  public int getPage() {
    if (isSetPage()) {
      return this.page.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("page", this);
  }


  /**
   * Returns whether page is set.
   *
   * @return whether page is set.
   */
  public boolean isSetPage() {
    return this.page != null;
  }


  /**
   * Sets year.
   *
   * @param year.
   */
  public void setPage(int page) {
    Integer oldPage = this.page;
    this.page = Integer.valueOf(page);
    firePropertyChange("page", oldPage, this.page);
  }


  /**
   * Unsets page.
   *
   * @return {@code true}, if page was set before, otherwise {@code false}.
   */
  public boolean unsetPage() {
    if (isSetPage()) {
      Integer oldPage = this.page;
      this.page = null;
      firePropertyChange("page", oldPage, this.page);
      return true;
    }
    return false;
  }


  // *** approvalMode ***
  /**
   * Returns approval mode.
   *
   * @return approval mode.
   */
  public int getApprovalMode() {
    if (isSetApprovalMode()) {
      return this.approvalMode.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("approvalMode", this);
  }


  /**
   * Returns whether approvalMode is set.
   *
   * @return whether approvalMode is set.
   */
  public boolean isSetApprovalMode() {
    return this.approvalMode != null;
  }


  /**
   * Sets approval mode.
   *
   * @param approval
   *        mode.
   */
  public void setApprovalMode(int approvalMode) {
    Integer oldApprovalMode = this.approvalMode;
    this.approvalMode = Integer.valueOf(approvalMode);
    firePropertyChange("approvalMode", oldApprovalMode, this.approvalMode);
  }


  /**
   * Unsets approval mode.
   *
   * @return {@code true}, if approval mode was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetApprovalMode() {
    if (isSetApprovalMode()) {
      Integer oldApprovalMode = this.approvalMode;
      this.page = null;
      firePropertyChange("approvalMode", oldApprovalMode, this.approvalMode);
      return true;
    }
    return false;
  }


  // *** website ***
  /**
   * Returns website.
   *
   * @return website.
   */
  public String getWebsite() {
    return this.website;
  }


  /**
   * Returns whether website is set.
   *
   * @return whether website is set.
   */
  public boolean isSetWebsite() {
    return this.website != null;
  }


  /**
   * Sets website.
   *
   * @param website.
   */
  public void setWebsite(String website) {
    String oldWebsite = this.website;
    this.website = website;
    firePropertyChange("website", oldWebsite, this.website);
  }


  /**
   * Unsets the website.
   *
   * @return {@code true}, if website was set before, otherwise {@code false}.
   */
  public boolean unsetWebsite() {
    if (isSetWebsite()) {
      String oldWebsite = this.website;
      this.website = null;
      firePropertyChange("website", oldWebsite, this.website);
      return true;
    }
    return false;
  }


  // *** reference type ***
  /**
   * Returns reference type.
   *
   * @return reference type.
   */
  public int getReferenceType() {
    if (isSetReferenceType()) {
      return this.referenceType.intValue();
    }
    // This is necesssary because we cannot return null here.
    throw new PropertyUndefinedError("referenceType", this);
  }


  /**
   * Returns whether reference type is set.
   *
   * @return whether reference type is set.
   */
  public boolean isSetReferenceType() {
    return this.referenceType != null;
  }


  /**
   * Sets reference type.
   *
   * @param referenceType
   */
  public void setReferenceType(int referenceType) {
    Integer oldReferenceType = this.referenceType;
    this.referenceType = Integer.valueOf(referenceType);
    firePropertyChange("referenceType", oldReferenceType, this.referenceType);
  }


  /**
   * Unsets reference type.
   *
   * @return {@code true}, if reference type was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetReferenceType() {
    if (isSetReferenceType()) {
      Integer oldReferenceType = this.referenceType;
      this.referenceType = null;
      firePropertyChange("referenceType", oldReferenceType, this.referenceType);
      return true;
    }
    return false;
  }


  // *** comment ***
  /**
   * Returns comment.
   *
   * @return comment.
   */
  public String getComment() {
    return this.comment;
  }


  /**
   * Returns whether comment is set.
   *
   * @return whether comment is set.
   */
  public boolean isSetComment() {
    return this.comment != null;
  }


  /**
   * Sets comment.
   *
   * @param comment.
   */
  public void setComment(String comment) {
    String oldComment = this.comment;
    this.comment = comment;
    firePropertyChange("comment", oldComment, this.comment);
  }


  /**
   * Unsets the comment.
   *
   * @return {@code true}, if comment was set before, otherwise {@code false}.
   */
  public boolean unsetComment() {
    if (isSetComment()) {
      String oldComment = this.comment;
      this.comment = null;
      firePropertyChange("comment", oldComment, this.comment);
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * javal.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    switch (attributeName) {
      case "AU": setAuthor(value); return true;
      case "PY": setYear(StringTools.parseSBMLInt(value)); return true;
      case "TI": setTitle(value); return true;
      case "AB": setAbstractText(value); return true;
      case "T2": setJournal(value); return true;
      case "VL": setVolume(value); return true;
      case "IS": setIssue(value); return true;
      case "SP": setPage(StringTools.parseSBMLInt(value)); return true;
      case "LB": setApprovalMode(StringTools.parseSBMLInt(value)); return true;
      case "UR": setWebsite(value); return true;
      case "M3": setReferenceType(StringTools.parseSBMLInt(value)); return true;
      case "N1": setComment(value); return true;
      default: return false;
    }
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();

    if (isSetAuthor()) {
      attributes.put("AU", this.author);
    }
    if (isSetYear()) {
      attributes.put("PY", StringTools.toString(Locale.ENGLISH, this.year.intValue()));
    }
    if (isSetTitle()) {
      attributes.put("TI", this.title);
    }
    if (isSetAbstractText()) {
      attributes.put("AB", this.abstractText);
    }
    if (isSetJournal()) {
      attributes.put("T2", this.journal);
    }
    if (isSetVolume()) {
      attributes.put("VL", this.volume);
    }
    if (isSetIssue()) {
      attributes.put("IS", this.issue);
    }
    if (isSetPage()) {
      attributes.put("SP", StringTools.toString(Locale.ENGLISH, this.page.intValue()));
    }
    if (isSetApprovalMode()) {
      attributes.put("LB", StringTools.toString(Locale.ENGLISH, this.approvalMode.intValue()));
    }
    if (isSetWebsite()) {
      attributes.put("UR", this.website);
    }
    if (isSetReferenceType()) {
      attributes.put("M3", StringTools.toString(Locale.ENGLISH, this.referenceType.intValue()));
    }
    if (isSetComment()) {
      attributes.put("UR", this.comment);
    }

    return attributes;
  }


  @Override
  public String toString() {
    return String.format("%s_%s_%s", this.author, this.year, this.title);
  }
}
