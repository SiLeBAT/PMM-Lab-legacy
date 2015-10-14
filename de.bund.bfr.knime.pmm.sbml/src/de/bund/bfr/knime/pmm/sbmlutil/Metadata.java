package de.bund.bfr.knime.pmm.sbmlutil;

// Holds metadata info related to a model
public class Metadata {

	String givenName;
	String familyName;
	String contact;
	String createdDate;
	String modifiedDate;
	String type;
	String rights;
	String referenceLink;

	public Metadata() {
		givenName = "";
		familyName = "";
		contact = "";
		createdDate = "";
		modifiedDate = "";
		type = "";
		rights = "";
		referenceLink = "";
	}

	public Metadata(String givenName, String familyName, String contact, String createdDate, String modifiedDate,
			String type, String rights, String referenceLink) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.contact = contact;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.type = type;
		this.rights = rights;
		this.referenceLink = referenceLink;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getRights() {
		return rights;
	}
	
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public String getReferenceLink() {
		return referenceLink;
	}
	
	public void setReferenceLink(String referenceLink) {
		this.referenceLink = referenceLink;
	}

	@Override
	public boolean equals(Object obj) {
		Metadata other = (Metadata) obj;

		if (!givenName.equals(other.givenName))
			return false;
		if (!familyName.equals(other.familyName))
			return false;
		if (!contact.equals(other.contact))
			return false;
		if (!createdDate.equals(other.createdDate))
			return false;
		if (!modifiedDate.equals(other.modifiedDate))
			return false;
		if (!type.equals(other.type))
			return false;
		if (!rights.equals(other.rights))
			return false;
		if (!referenceLink.equals(other.referenceLink))
			return false;
		return true;

	}
}
