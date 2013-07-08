/**
 * 
 */
package org.hsh.bfr.db.imports;

import javax.swing.JProgressBar;

/**
 * @author Armin
 *
 */
public interface MyImporter {
	public void doImport(final String filename, final JProgressBar progress, final boolean showResults);
}
