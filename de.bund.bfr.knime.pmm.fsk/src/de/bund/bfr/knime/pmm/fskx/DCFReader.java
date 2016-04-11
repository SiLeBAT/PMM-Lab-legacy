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
package de.bund.bfr.knime.pmm.fskx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads a DCF database.
 * 
 * The DCF databases used in R follow the rules:
 * <ol>
 * <li>A database consists of one or more records, each with one or more name fields. Not every
 * record must contain each field. Fields may appear more than once in a record.</li>
 * <li>Regular lines start with a non-whitespace character.</li>
 * <li>Regular lines are of form tag:value.</li>
 * <li>Lines starting with whitespace are continuation lines (to the preceding field) if at least
 * one character in the line is non-whitespace.</li>
 * <li>Records are separated by one or more empty lines.</li>
 * </ol>
 * 
 * @author Miguel Alba
 * @see <a href="http://www.inside-r.org/r-doc/base/read.dcf"> http://www.inside-r.org/r-doc/base/
 *      read.dcf</a>
 * @see <a href="http://r-pkgs.had.co.nz/description.html">http://r-pkgs.had.co.nz/description.html
 *      </a>
 */
public class DCFReader {

  private DCFReader() {}
  
  /** @return map with properties and values from the DCF database. */
  public static Map<String, String> read(final InputStream stream) {
    
    Map<String, String> props = new HashMap<>();
    
    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
    
    String line;  // current line
    String field = null;  // holds property
    
    try {
      while ((line = br.readLine()) != null) {
        // Continuation lines
        if (field != null && line.startsWith(" ")) {
          props.put(field, props.get(field) + " " + line.trim());
        }
        // Regular lines
        else {
          String[] tokens = line.split(": ", 2);  // (field, value)
          if (tokens.length != 2) {
            continue;
          }
          field = tokens[0];
          String val = tokens[1];
          props.put(field, val);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return props;
  }
}
