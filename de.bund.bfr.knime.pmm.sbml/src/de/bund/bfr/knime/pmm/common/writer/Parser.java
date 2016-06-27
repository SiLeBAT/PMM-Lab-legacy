package de.bund.bfr.knime.pmm.common.writer;

import java.util.List;

import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.pmfml.sbml.Metadata;

public interface Parser {
	public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata, boolean splitModels,
			String notes, ExecutionContext exec) throws Exception;
}