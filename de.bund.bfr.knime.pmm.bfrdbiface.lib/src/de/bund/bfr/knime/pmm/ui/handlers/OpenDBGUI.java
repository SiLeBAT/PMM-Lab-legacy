package de.bund.bfr.knime.pmm.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.hsh.bfr.db.DBKernel;

public class OpenDBGUI extends AbstractHandler {
	public OpenDBGUI() {
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		DBKernel.openDBGUI(true);
		return null;
	}
}
