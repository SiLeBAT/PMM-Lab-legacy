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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/dialogs/LiteratureSelectionDialog.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;

/**
 * Dialog to select the literature in the statistic model editor
 * @author Mark Hoffmann
 * @since 17.11.2011
 */
public class LiteratureSelectionDialog extends TitleAreaDialog {
	
	private final StatistikModell statisticModel;
	private final GeschaetztStatistikModell estimatedStatisticModel;
	private CheckboxTableViewer literatureViewer;
	private static final Literatur EMPTY;
	private final EditingDomain domain;
	
	static {
		EMPTY = BfrFactory.eINSTANCE.createLiteratur();
		EMPTY.setTitel(Messages.LiteratureSelectionDialog_0);
	}

	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model
	 * @param domain the editing domain
	 */
	public LiteratureSelectionDialog(Shell parentShell, StatistikModell model, EditingDomain domain) {
		super(parentShell);
		this.statisticModel = model;
		this.domain = domain;
		this.estimatedStatisticModel = null;
	}
	/**
	 * Create the dialog.
	 * @param parentShell the under lying shell
	 * @param model the statistic model
	 * @param domain the editing domain
	 */
	public LiteratureSelectionDialog(Shell parentShell, GeschaetztStatistikModell model, EditingDomain domain) {
		super(parentShell);
		this.domain = domain;
		this.statisticModel = null;
		this.estimatedStatisticModel = model;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent the parent composite
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.LiteratureSelectionDialog_1);
		setMessage(Messages.LiteratureSelectionDialog_2);
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		literatureViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table tableLiterature = literatureViewer.getTable();
		tableLiterature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		literatureViewer.setContentProvider(new ArrayContentProvider());
		literatureViewer.setLabelProvider(new LabelProvider(){
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element.equals(EMPTY)) {
					return EMPTY.getTitel();
				}
				if (element instanceof Literatur){
					Literatur l = (Literatur) element;
					return l.getTitel() + " - " + l.getErstautor(); //$NON-NLS-1$
				}
				return super.getText(element);
			}
		});
		List<Literatur> inputList = new ArrayList<Literatur>(BFRUIActivator.getBFRService().getAllLiterature().getLiteratur());
		if (inputList.size() == 0) {
			inputList.add(EMPTY);
		}
		literatureViewer.setInput(inputList);
		Object[] checkedElements = new Object[0];
		if (statisticModel != null) {
			checkedElements = statisticModel.getLiteratur().toArray();
		} else if (estimatedStatisticModel != null) {
			checkedElements = estimatedStatisticModel.getLiteratur().toArray();
		}
		literatureViewer.setCheckedElements(checkedElements);
		
		Button buttonAll = new Button(container, SWT.NONE);
		GridData gd_buttonAll = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_buttonAll.widthHint = 120;
		buttonAll.setLayoutData(gd_buttonAll);
		buttonAll.setText(Messages.LiteratureSelectionDialog_4);
		buttonAll.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				literatureViewer.setAllChecked(true);
			}
		});
		
		Button buttonNone = new Button(container, SWT.NONE);
		GridData gd_buttonNone = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_buttonNone.widthHint = 120;
		buttonNone.setLayoutData(gd_buttonNone);
		buttonNone.setText(Messages.LiteratureSelectionDialog_5);
		buttonNone.addSelectionListener(new SelectionAdapter() {
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				literatureViewer.setAllChecked(false);
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent the parent button bar composite
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		Object[] selectionArray = literatureViewer.getCheckedElements();
		List<Literatur> literatureList = new ArrayList<Literatur>(selectionArray.length);
		for (Object o : selectionArray) {
			literatureList.add((Literatur) o);
		}
		Command addCommand = null;
		if (!literatureList.contains(EMPTY)) {
			if (statisticModel != null) {
				statisticModel.getLiteratur().clear();
				addCommand = AddCommand.create(domain, statisticModel, BfrPackage.Literals.STATISTIK_MODELL__LITERATUR, literatureList);
			} else if (estimatedStatisticModel != null) {
				estimatedStatisticModel.getLiteratur().clear();
				addCommand = AddCommand.create(domain, estimatedStatisticModel, BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__LITERATUR, literatureList);
			}
		}
		if (addCommand != null) {
			domain.getCommandStack().execute(addCommand);
		}
		super.okPressed();
	}
	
}
