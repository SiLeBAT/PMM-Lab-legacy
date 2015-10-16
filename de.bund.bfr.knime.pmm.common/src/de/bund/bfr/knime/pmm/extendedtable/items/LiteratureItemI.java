package de.bund.bfr.knime.pmm.extendedtable.items;

import org.jdom2.Element;

public interface LiteratureItemI {

	public String getElementName();
	
	public Element toXmlElement();
	public String toString();
	
	public Integer getId();
	public void setId(Integer id);
	
	public String getAuthor();
	public void setAuthor(String author);
	
	public String getTitle();
	public void setTitle(String title);
	
	public String getAbstractText();
	public void setAbstractText(String abstractText);
	
	public Integer getYear();
	public void setYear(Integer year);
	
	public String getJournal();
	public void setJournal(String journal);
	
	public String getVolume();
	public void setVolume(String volume);
	
	public String getIssue();
	public void setIssue(String issue);
	
	public Integer getPage();
	public void setPage(Integer page);
	
	public Integer getApprovalMode();
	public void setApprovalMode(Integer approvalMode);
	
	public String getWebsite();
	public void setWebsite(String website);
	
	public Integer getType();
	public void setType(Integer type);
	
	public String getComment();
	public void setComment(String comment);
	
	public String getDbuuid();
	public void setDbuuid(String dbuuid);
}