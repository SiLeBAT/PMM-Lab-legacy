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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Meta data from an R package.
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

  public RPackageMetadata(final Map<String, String> props) {
    if (props.containsKey(PACKAGE)) {
      m_package = props.get(PACKAGE);
    }

    if (props.containsKey(TYPE)) {
      m_type = props.get(TYPE);
    }

    if (props.containsKey(VERSION)) {
      m_version = RVersion.numericVersion(props.get(VERSION));
    }

    if (props.containsKey(TITLE)) {
      m_title = props.get(TITLE);
    }

    if (props.containsKey(DATE)) {
      try {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        m_date = new GregorianCalendar();
        m_date.setTime(ft.parse(props.get(DATE)));
      } catch (ParseException e) {
        m_date = null;
      }
    }

    if (props.containsKey(DESCRIPTION)) {
      m_description = props.get(DESCRIPTION);
    }

    if (props.containsKey(DEPENDENCIES)) {
      // Gets dependencies as strings
      // E.g.: val = "R (>= 3.0.2), stats, graphics, zoo, timeDate"
      // depStrings = ["R (>= 3.0.2)", "stats", "graphics", "zoo", "timeDate"]
      String[] depStrings = props.get(DEPENDENCIES).split(",");

      m_dependencies = new ArrayList<>(depStrings.length);

      for (String depString : depStrings) {
        String[] depTokens = depString.split(" ");
        if (depTokens.length == 2) {
          RDependency dep = new RDependency();
          dep.name = depTokens[1];
          m_dependencies.add(dep);
        } else if (depTokens.length == 3) {
          RDependency dep = new RDependency();
          dep.name = depTokens[0];
          String versionString = depTokens[2].substring(0, depTokens[2].length() - 1);
          dep.version = RVersion.numericVersion(versionString);
          m_dependencies.add(dep);
        }
      }
    }
  }
}
