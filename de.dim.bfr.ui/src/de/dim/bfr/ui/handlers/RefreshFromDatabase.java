/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.dim.bfr.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.dim.bfr.ui.editors.EstimatedStatisticModelEditor;
import de.dim.bfr.ui.editors.LiteraturEditor;
import de.dim.bfr.ui.editors.StatisticModelEditor;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;

public class RefreshFromDatabase extends AbstractHandler {
	
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public RefreshFromDatabase() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		window = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell activeShell = HandlerUtil.getActiveShell(event);
		// is dirty editor open ...
		if (isDirtyEditorOpen()) {
			// ... break refreshing, if question is answered with 'no'
			if (!MessageDialog.openQuestion(activeShell, Messages.RefreshFromDatabase_0, Messages.RefreshFromDatabase_1)) {
				return null;
			}
		}
		// ... otherwise close all editors
		closeOpenEditors();
		BFRUIActivator.getBFRService().refreshServiceContent();
		return null;
	}

	/**
	 * Returns <code>true</code>, if a dirty Literature-, StatisticModel- or EstimatedStatisticModelEditor is open.
	 * @return <code>true</code>, if a dirty Literature-, StatisticModel- or EstimatedStatisticModelEditor is open.
	 */
	private boolean isDirtyEditorOpen() {
		for (IEditorReference editor : window.getActivePage().getEditorReferences()) {
			if ((editor.getId().equals(LiteraturEditor.ID) || 
					editor.getId().equals(StatisticModelEditor.ID) || 
					editor.getId().equals(EstimatedStatisticModelEditor.ID)) && editor.isDirty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Closes all editors of the given editor id
	 */
	private void closeOpenEditors() {
		List<IEditorReference> closeEditorList = new ArrayList<IEditorReference>(window.getActivePage().getEditorReferences().length);
		for (IEditorReference editor : window.getActivePage().getEditorReferences()) {
			if (editor.getId().equals(LiteraturEditor.ID) || 
					editor.getId().equals(StatisticModelEditor.ID) || 
					editor.getId().equals(EstimatedStatisticModelEditor.ID)) {
				closeEditorList.add(editor);
			}
		}
		window.getActivePage().closeEditors(closeEditorList.toArray(new IEditorReference[closeEditorList.size()]), false);
	}
}
