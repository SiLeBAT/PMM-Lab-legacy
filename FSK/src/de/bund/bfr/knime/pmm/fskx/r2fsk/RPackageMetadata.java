package de.bund.bfr.knime.pmm.fskx.r2fsk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

  String m_package;
  String m_type;
  String m_title;
  String m_version;
  Date m_date;

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
            if (field.equals("Title")) {
              metadata.m_title += " " + line.trim();
            }
          }
        }

        // Regular lines
        else {
          String[] tokens = line.split(": "); // (field, value)
          if (tokens.length != 2)
            continue;
          
          field = tokens[0];
          val = tokens[1];

          if (field.equals("Package")) {
            metadata.m_package = val;
          }

          else if (field.equals("Type")) {
            metadata.m_type = val;
          }

          else if (field.equals("Version")) {
            metadata.m_version = val;
          }

          else if (field.equals("Title")) {
            metadata.m_title = val;
          }
          
          else if (field.equals("Date")) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            try {
              metadata.m_date = ft.parse(val);
            } catch (ParseException e) {
              e.printStackTrace();
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
