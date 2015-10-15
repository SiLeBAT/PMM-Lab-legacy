package de.bund.bfr.knime.pmm.sbmlutil;

import com.google.common.base.Strings;

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
	}

	public Metadata(String givenName, String familyName, String contact, String createdDate, String modifiedDate,
			String type, String rights, String referenceLink) {
		this.givenName = Strings.emptyToNull(givenName);
		this.familyName = Strings.emptyToNull(familyName);
		this.contact = Strings.emptyToNull(contact);
		this.createdDate = Strings.emptyToNull(createdDate);
		this.modifiedDate = Strings.emptyToNull(modifiedDate);
		this.type = Strings.emptyToNull(type);
		this.rights = Strings.emptyToNull(rights);
		this.referenceLink = Strings.emptyToNull(referenceLink);
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	public boolean isGivenNameSet() {
		return givenName != null;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public boolean isFamilyNameSet() {
		return familyName != null;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public boolean isContactSet() {
		return contact != null;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public boolean isCreatedDateSet() {
		return createdDate != null;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public boolean isModifiedDateSet() {
		return modifiedDate != null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isTypeSet() {
		return type != null;
	}

	public String getRights() {
		return rights;
	}
	
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public boolean areRightsSet() {
		return rights != null;
	}
	
	public String getReferenceLink() {
		return referenceLink;
	}
	
	public void setReferenceLink(String referenceLink) {
		this.referenceLink = referenceLink;
	}
	
	public boolean isReferenceLinkSet() {
		return referenceLink != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
		result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
		result = prime * result + ((referenceLink == null) ? 0 : referenceLink.hashCode());
		result = prime * result + ((rights == null) ? 0 : rights.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Metadata other = (Metadata) obj;
		if (contact == null && other.contact != null) {
			return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (createdDate == null && other.createdDate != null) {
			return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (familyName == null && other.familyName != null) {
			return false;
		} else if (!familyName.equals(other.familyName))
			return false;
		if (givenName == null && other.givenName != null) {
			return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		if (modifiedDate == null && other.modifiedDate != null) {
			return false;
		} else if (!modifiedDate.equals(other.modifiedDate))
			return false;
		if (referenceLink == null && other.referenceLink != null) {
			return false;
		} else if (!referenceLink.equals(other.referenceLink))
			return false;
		if (rights == null && other.rights != null) {
			return false;
		} else if (!rights.equals(other.rights))
			return false;
		if (type == null && other.type != null) {
			return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
