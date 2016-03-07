/**
 * 
 */
package de.bund.bfr.knime.pmm.fskx;

/**
 * @author Miguel Alba
 *
 */
public class MissingValueError extends Exception {
  private static final long serialVersionUID = 4475779784977781461L;
  
  public MissingValueError(String msg) {
    super(msg);
  }
}