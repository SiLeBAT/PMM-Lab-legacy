/**
 * 
 */
package de.bund.bfr.knime.pmm.fskx;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;

import com.google.common.io.Files;

public class RScript {

  private String script;
  private List<String> libraries = new LinkedList<>();
  private List<String> sources = new LinkedList<>();

  /**
   * Process R script.
   * 
   * @param file.
   * @throws IOException if the file specified by path cannot be read.
   */
  public RScript(final File file) throws IOException {
    String script = Files.toString(file, Charsets.UTF_8);  // throws IOException

    // If no errors are thrown, proceed to extract libraries and sources
    final String[] lines = script.split("\\r?\\n");
    
    final Pattern libPattern = Pattern.compile("^\\s*\\b(library|require)\\((\"?.+\"?)\\)");
    final Pattern srcPattern = Pattern.compile("^\\s*\\b(source)\\((\"?.+\"?)\\)");

    StringBuilder sb = new StringBuilder();
    for (final String line : lines) {

      if (line.startsWith("#"))
        continue;
      
      sb.append(line + '\n');

      final Matcher libMatcher = libPattern.matcher(line);
      final Matcher srcMatcher = srcPattern.matcher(line);

      if (libMatcher.find()) {
        final String libName = libMatcher.group(2).replace("\"", "");
        getLibraries().add(libName);
      } else if (srcMatcher.find()) {
        final String srcName = srcMatcher.group(2).replace("\"", "");
        getSources().add(srcName);
      }
    }
    
    this.script = sb.toString();
  }

  public String getScript() {
    return this.script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public List<String> getSources() {
    return this.sources;
  }

  public void setSources(List<String> sources) {
    this.sources = sources;
  }

  public List<String> getLibraries() {
    return this.libraries;
  }

  public void setLibraries(List<String> libraries) {
    this.libraries = libraries;
  }
}