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

import de.bund.bfr.pmf.ModelType;

/**
 * Holds meta data related to a model.
 * 
 * @author Miguel Alba
 */
public interface Metadata {

  /** Returns the given name of this {@link Metadata}. If not set returns null. */
  public String getGivenName();

  /** Returns the family name of this {@link Metadata}. If not set returns null. */
  public String getFamilyName();

  /** Returns the contact of this {@link Metadata}. If not set returns null. */
  public String getContact();

  /** Returns the created date of this {@link Metadata}. If not set returns null. */
  public String getCreatedDate();

  /** Returns the modified date of this {@link Metadata}. If not set returns null. */
  public String getModifiedDate();

  /** Returns the {@link ModelType} of this {@link Metadata}. If not set returns null. */
  public ModelType getType();

  /** Returns the rights of this {@link Metadata}. If not set returns null. */
  public String getRights();

  /** Returns the reference link of this {@link Metadata}. If not set returns null. */
  public String getReferenceLink();

  /**
   * Sets the given name value with 'givenName'. If 'givenName' is null or empty it will do nothing.
   */
  public void setGivenName(final String givenName);

  /**
   * Sets the family name value with 'familyName'. If 'familyName' is null or empty it will do
   * nothing
   */
  public void setFamilyName(final String familyName);

  /** Sets the contact value with 'contact'. If 'contact' is null or empty it will do nothing. */
  public void setContact(final String contact);

  /**
   * Sets the created date value with 'createdDate'. If 'createdDate' is null or empty it will do
   * nothing.
   */
  public void setCreatedDate(final String createdDate);

  /**
   * Sets the modified date value with 'modifiedDate'. If 'modifiedDate' is null or empty it will do
   * nothing.
   */
  public void setModifiedDate(final String modifiedDate);

  /** Sets the {@link ModelType} value with 'type'. If 'modelType' is null it will do nothing. */
  public void setType(final ModelType type);

  /** Sets the rights value with 'rights'. If 'rights' is null or empty it will do nothing. */
  public void setRights(final String rights);

  /**
   * Sets the reference link value with 'referenceLink'. If 'referenceLink' is null or empty it will
   * do nothing.
   */
  public void setReferenceLink(final String referenceLink);

  /** Returns true if the given name of this {@link Metadata} is set. */
  public boolean isSetGivenName();

  /** Returns true if the family name of this {@link Metadata} is set. */
  public boolean isSetFamilyName();

  /** Returns true if the contact of this {@link Metadata} is set. */
  public boolean isSetContact();

  /** Returns true if the created date of this {@link Metadata} is set. */
  public boolean isSetCreatedDate();

  /** Returns true if the modified date of this {@link Metadata} is set. */
  public boolean isSetModifiedDate();

  /** Returns true if the {@link ModelType} of this {@link Metadata} is set. */
  public boolean isSetType();

  /** Returns true if the rights of this {@link Metadata} are set. */
  public boolean isSetRights();

  /** Returns true if the reference link of this {@link Metadata} is set. */
  public boolean isSetReferenceLink();
}
