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

import java.util.HashMap;
import java.util.Map;

import de.bund.bfr.pmf.ModelType;

/**
 * @author Miguel Alba
 */
public class MetadataImpl implements Metadata {

  private static final String GIVEN_NAME = "givenName";
  private static final String FAMILY_NAME = "familyName";
  private static final String CONTACT = "contact";
  private static final String CREATED_DATE = "createdDate";
  private static final String MODIFIED_DATE = "modifiedDate";
  private static final String TYPE = "type";
  private static final String RIGHTS = "rights";
  private static final String REFERENCE_LINK = "referenceLink";

  private Map<String, String> props;

  public MetadataImpl() {
    props = new HashMap<>(8);
  }

  public MetadataImpl(final String givenName, final String familyName, final String contact,
      final String createdDate, final String modifiedDate, final ModelType type,
      final String rights, final String referenceLink) {
    props = new HashMap<>(8);

    setGivenName(givenName);
    setFamilyName(familyName);
    setContact(contact);
    setCreatedDate(createdDate);
    setModifiedDate(modifiedDate);
    setType(type);
    setRights(rights);
    setReferenceLink(referenceLink);
  }

  /** {@inheritDoc} */
  public String getGivenName() {
    return props.get(GIVEN_NAME);
  }

  /** {@inheritDoc} */
  public String getFamilyName() {
    return props.get(FAMILY_NAME);
  }

  /** {@inheritDoc} */
  public String getContact() {
    return props.get(CONTACT);
  }

  /** {@inheritDoc} */
  public String getCreatedDate() {
    return props.get(CREATED_DATE);
  }

  /** {@inheritDoc} */
  public String getModifiedDate() {
    return props.get(MODIFIED_DATE);
  }

  /** {@inheritDoc} */
  public ModelType getType() {
    return props.containsKey(TYPE) ? ModelType.valueOf(props.get(TYPE)) : null;
  }

  /** {@inheritDoc} */
  public String getRights() {
    return props.get(RIGHTS);
  }

  /** {@inheritDoc} */
  public String getReferenceLink() {
    return props.get(REFERENCE_LINK);
  }

  /** {@inheritDoc} */
  public void setGivenName(final String givenName) {
    if (givenName != null && !givenName.isEmpty()) {
      props.put(GIVEN_NAME, givenName);
    }
  }

  /** {@inheritDoc} */
  public void setFamilyName(final String familyName) {
    if (familyName != null && !familyName.isEmpty()) {
      props.put(FAMILY_NAME, familyName);
    }
  }

  /** {@inheritDoc} */
  public void setContact(final String contact) {
    if (contact != null && !contact.isEmpty()) {
      props.put(CONTACT, contact);
    }
  }

  /** {@inheritDoc} */
  public void setCreatedDate(final String createdDate) {
    if (createdDate != null && !createdDate.isEmpty()) {
      props.put(CREATED_DATE, createdDate);
    }
  }

  /** {@inheritDoc} */
  public void setModifiedDate(final String modifiedDate) {
    if (modifiedDate != null && !modifiedDate.isEmpty()) {
      props.put(MODIFIED_DATE, modifiedDate);
    }
  }

  /** {@inheritDoc} */
  public void setType(final ModelType type) {
    if (type != null) {
      props.put(TYPE, type.name());
    }
  }

  /** {@inheritDoc} */
  public void setRights(final String rights) {
    if (rights != null && !rights.isEmpty()) {
      props.put(RIGHTS, rights);
    }
  }

  /** {@inheritDoc} */
  public void setReferenceLink(final String referenceLink) {
    if (referenceLink != null && !referenceLink.isEmpty()) {
      props.put(REFERENCE_LINK, referenceLink);
    }
  }

  /** {@inheritDoc} */
  public boolean isSetGivenName() {
    return props.containsKey(GIVEN_NAME);
  }

  /** {@inheritDoc} */
  public boolean isSetFamilyName() {
    return props.containsKey(FAMILY_NAME);
  }

  /** {@inheritDoc} */
  public boolean isSetContact() {
    return props.containsKey(CONTACT);
  }

  /** {@inheritDoc} */
  public boolean isSetCreatedDate() {
    return props.containsKey(CREATED_DATE);
  }

  /** {@inheritDoc} */
  public boolean isSetModifiedDate() {
    return props.containsKey(MODIFIED_DATE);
  }

  /** {@inheritDoc} */
  public boolean isSetType() {
    return props.containsKey(TYPE);
  }

  /** {@inheritDoc} */
  public boolean isSetRights() {
    return props.containsKey(RIGHTS);
  }

  /** {@inheritDoc} */
  public boolean isSetReferenceLink() {
    return props.containsKey(REFERENCE_LINK);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int result = 31 * 1 + ((props == null) ? 0 : props.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MetadataImpl other = (MetadataImpl) obj;
    if (props == null) {
      if (other.props != null)
        return false;
    } else if (!props.equals(other.props))
      return false;
    return true;
  }
}
