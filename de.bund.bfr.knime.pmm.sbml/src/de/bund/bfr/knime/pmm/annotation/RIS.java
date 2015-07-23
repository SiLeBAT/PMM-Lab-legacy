package de.bund.bfr.knime.pmm.annotation;

public class RIS implements LiteratureSpecification {

	public String getAuthor() {
		return "AU";
	}
	
	public String getYear() {
		return "PY";
	}
	
	public String getTitle() {
		return "TI";
	}
	
	public String getAbstract() {
		return "AB";
	}
	
	public String getJournal() {
		return "T2";
	}
	
	public String getVolume() {
		return "VL";
	}

	public String getIssue() {
		return "IS";
	}
	
	public String getPage() {
		return "SP";
	}

	public String getApproval() {
		return "LB";
	}

	public String getWebsite() {
		return "UR";
	}

	public String getType() {
		return "M3";
	}

	public String getComment() {
		return "N1";
	}
}
