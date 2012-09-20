package de.dim.bfr.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.services.BFRUIService;

public class OpenDBGUI extends AbstractHandler {
	public OpenDBGUI() {
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		//BFRNodeServiceImpl nodeService = new BFRNodeServiceImpl();
		BFRUIService service = BFRUIActivator.getBFRService();
		service.openDBGUI(true);
		//
		return null;
	}
}
