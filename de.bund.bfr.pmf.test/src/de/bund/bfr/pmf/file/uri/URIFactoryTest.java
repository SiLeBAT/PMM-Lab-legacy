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
package de.bund.bfr.pmf.file.uri;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

/**
 * @author Miguel Alba
 */
public class URIFactoryTest {

  @Test
  public void test() {
    try {
      assertEquals(new URI("https://raw.githubusercontent.com/NuML/NuML/master/NUMLSchema.xsd"),
          URIFactory.createNuMLURI());
      assertEquals(new URI("http://identifiers.org/combine/specifications/sbml"),
          URIFactory.createSBMLURI());
      assertEquals(new URI("http://sourceforge.net/projects/microbialmodelingexchange/files/"),
          URIFactory.createPMFURI());
    } catch (URISyntaxException e) {
      fail(e.getMessage());
    }
  }

}
