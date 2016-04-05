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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Metadata from an R package.
 * 
 * The meta data from the DESCRIPTION file in an R package is stored in a DCF database.
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
public class RPackageMetadata {

  public String m_package;
  public String m_type;
  public String m_title;
  public RVersion m_version;
  public Calendar m_date;
  public String m_description;
  public List<RDependency> m_dependencies;

  public static final String PACKAGE = "Package";
  public static final String TYPE = "Type";
  public static final String TITLE = "Title";
  public static final String VERSION = "Version";
  public static final String DATE = "Date";
  public static final String DESCRIPTION = "Description";
  public static final String DEPENDENCIES = "Depends";


  public static RPackageMetadata parseDescription(InputStream stream) {
    RPackageMetadata metadata = new RPackageMetadata();

    BufferedReader br = new BufferedReader(new InputStreamReader(stream));

    String line;
    String field = null, val = null;
    try {
      while ((line = br.readLine()) != null) {

        // Continuation lines
        if (line.startsWith(" ")) {
          if (field != null) {
            if (field.equals(TITLE)) {
              metadata.m_title += " " + line.trim();
            }

            else if (field.equals(DESCRIPTION)) {
              metadata.m_description += " " + line.trim();
            }
          }
        }

        // Regular lines
        else {
          String[] tokens = line.split(": ", 2); // (field, value)
          if (tokens.length != 2)
            continue;

          field = tokens[0];
          val = tokens[1];

          if (field.equals(PACKAGE)) {
            metadata.m_package = val;
          }

          else if (field.equals(TYPE)) {
            metadata.m_type = val;
          }

          else if (field.equals(VERSION)) {
            metadata.m_version = RVersion.numericVersion(val);
          }

          else if (field.equals(TITLE)) {
            metadata.m_title = val;
          }

          else if (field.equals(DATE)) {
            metadata.m_date = new GregorianCalendar();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            try {
              metadata.m_date.setTime(ft.parse(val));
            } catch (ParseException e) {
              e.printStackTrace();
            }
          }

          else if (field.equals(DESCRIPTION)) {
            metadata.m_description = val;
          }

          else if (field.equals(DEPENDENCIES)) {
            // Gets dependencies as strings
            // E.g.: val = "R (>= 3.0.2), stats, graphics, zoo, timeDate"
            // depStrings = ["R (>= 3.0.2)", "stats", "graphics", "zoo", "timeDate"]
            String[] depStrings = val.split(",");

            metadata.m_dependencies = new ArrayList<>(depStrings.length);

            for (String depString : depStrings) {
              String[] depTokens = depString.split(" ");
              if (depTokens.length == 2) {
                RDependency dep = new RDependency();
                dep.name = depTokens[1];
                metadata.m_dependencies.add(dep);
              } else if (depTokens.length == 3) {
                RDependency dep = new RDependency();
                dep.name = depTokens[0];
                String versionString = depTokens[2].substring(0, depTokens[2].length() - 1);
                dep.version = RVersion.numericVersion(versionString);
                metadata.m_dependencies.add(dep);
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return metadata;
  }
}
