package de.bund.bfr.knime.pmm.ui.handlers;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class Settings extends AbstractHandler {
	public Settings() {
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		SettingsDialog sd = new SettingsDialog(JOptionPane.getRootFrame());
		//sd.setModal(true);
		sd.setVisible(true);
		return null;
	}
}
