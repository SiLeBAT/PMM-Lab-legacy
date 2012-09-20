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
/**
 * Project: de.dim.bfr.ui
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/handlers/BFRDeleteHandler.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.ui.editors.EstimatedModellEditorInput;
import de.dim.bfr.ui.editors.LiteraturEditorInput;
import de.dim.bfr.ui.editors.ModellEditorInput;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;

/**
 * Delete handler that handles all delete commands
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public class BFRDeleteHandler extends AbstractHandler {

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Object o = ((IStructuredSelection)selection).getFirstElement();
		boolean delSuccess = false;
		IEditorInput input = null;
		if (o instanceof Literatur) {
			Literatur literatur = (Literatur) o;
			input = new LiteraturEditorInput(literatur);
			if (MessageDialog.openQuestion(window.getShell(), Messages.BFRDeleteHandler_0, Messages.BFRDeleteHandler_1)) {
				delSuccess = BFRUIActivator.getBFRService().deleteLiterature(literatur);
			}
		}
		if (o instanceof StatistikModell) {
			StatistikModell model = (StatistikModell) o;
			input = new ModellEditorInput(model);
			if (MessageDialog.openQuestion(window.getShell(), Messages.BFRDeleteHandler_2, Messages.BFRDeleteHandler_3)) {
				delSuccess = BFRUIActivator.getBFRService().deleteStatisticModel(model);
			}
		}
		if (o instanceof GeschaetztStatistikModell) {
			GeschaetztStatistikModell model = (GeschaetztStatistikModell) o;
			input = new EstimatedModellEditorInput(model);
			if (MessageDialog.openQuestion(window.getShell(), Messages.BFRDeleteHandler_4, Messages.BFRDeleteHandler_5)) {
				delSuccess = BFRUIActivator.getBFRService().deleteEstimatedStatisticModel(model);
			}
		}
		if (delSuccess && input != null) {
			IEditorPart editor = window.getActivePage().findEditor(input);
			window.getActivePage().closeEditor(editor, false);
		}
		return null;
	}

}
