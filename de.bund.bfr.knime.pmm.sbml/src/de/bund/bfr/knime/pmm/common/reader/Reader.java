package de.bund.bfr.knime.pmm.common.reader;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;

/**
 * Reader interface
 * 
 * @author Miguel Alba
 */
public interface Reader {
	/**
	 * Read models from a CombineArchive and returns a Knime table with them
	 * 
	 * @param isPMFX. If true the reads PMFX file. Else then read PMF file.
	 * @throws Exception
	 */
	public BufferedDataContainer[] read(String filepath, boolean isPMFX, ExecutionContext exec) throws Exception;
}