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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/views/LiteraturView.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.views;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * View to show all literature instances
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public class LiteraturView extends ViewPart {
	
	public static final String ID = "de.dim.bfr.ui.view.Literature"; //$NON-NLS-1$
	private LiteraturListe inputList;
	private BFRUIService service;
	private Table literaturTable;
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		service = BFRUIActivator.getBFRService();
		inputList = service.getAllLiterature();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		TableViewer literaturViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		literaturTable = literaturViewer.getTable();
		literaturTable.setLinesVisible(true);
		literaturTable.setHeaderVisible(true);
		
		TableViewerColumn idVC = new TableViewerColumn(literaturViewer, SWT.NONE);
		TableColumn idCol = idVC.getColumn();
		idCol.setWidth(50);
		idCol.setText(Messages.LiteraturView_1);
		
		TableViewerColumn titelVC = new TableViewerColumn(literaturViewer, SWT.NONE);
		TableColumn titleCol = titelVC.getColumn();
		titleCol.setWidth(200);
		titleCol.setText(Messages.LiteraturView_2);
		
		TableViewerColumn authorVC = new TableViewerColumn(literaturViewer, SWT.NONE);
		TableColumn authorCol = authorVC.getColumn();
		authorCol.setWidth(200);
		authorCol.setText(Messages.LiteraturView_3);
		
		TableViewerColumn yearVC = new TableViewerColumn(literaturViewer, SWT.NONE);
		TableColumn yearCol = yearVC.getColumn();
		yearCol.setWidth(50);
		yearCol.setText(Messages.LiteraturView_4);
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		IObservableSet knownElements = contentProvider.getKnownElements();
		IObservableMap[] attributeMap = new IObservableMap [4];
		attributeMap[0] = EMFProperties.value(BfrPackage.Literals.LITERATUR__ID).observeDetail(knownElements);
		attributeMap[1] = EMFProperties.value(BfrPackage.Literals.LITERATUR__TITEL).observeDetail(knownElements);
		attributeMap[2] = EMFProperties.value(BfrPackage.Literals.LITERATUR__ERSTAUTOR).observeDetail(knownElements);
		attributeMap[3] = EMFProperties.value(BfrPackage.Literals.LITERATUR__JAHR).observeDetail(knownElements);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(attributeMap);
		
		literaturViewer.setContentProvider(contentProvider);
		literaturViewer.setLabelProvider(labelProvider);
		literaturViewer.setSorter(new LiteraturSorter());
		literaturViewer.setInput(EMFProperties.list(BfrPackage.Literals.LITERATUR_LISTE__LITERATUR).observe(inputList));
		getSite().setSelectionProvider(literaturViewer);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		literaturTable.setFocus();
	}

}
