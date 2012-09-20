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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/views/StatistikModellView.java $
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
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * View to show all statistic model instances
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public class StatistikModellView extends ViewPart {
	
	public static final String ID = "de.dim.bfr.ui.view.StatisticModel"; //$NON-NLS-1$
	private StatistikModellKatalog inputList;
	private BFRUIService service;
	private Table statModelTable;
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		service = BFRUIActivator.getBFRService();
		inputList = service.getAllStatisticModels();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		TableViewer statModelViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		statModelTable = statModelViewer.getTable();
		statModelTable.setLinesVisible(true);
		statModelTable.setHeaderVisible(true);
		
		TableViewerColumn idVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn idCol = idVC.getColumn();
		idCol.setWidth(50);
		idCol.setText(Messages.StatistikModellView_1);
		
		TableViewerColumn titelVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn titleCol = titelVC.getColumn();
		titleCol.setWidth(200);
		titleCol.setText(Messages.StatistikModellView_2);
		
		TableViewerColumn authorVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn authorCol = authorVC.getColumn();
		authorCol.setWidth(50);
		authorCol.setText(Messages.StatistikModellView_3);
		
		TableViewerColumn yearVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn yearCol = yearVC.getColumn();
		yearCol.setWidth(200);
		yearCol.setText(Messages.StatistikModellView_4);
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		IObservableSet knownElements = contentProvider.getKnownElements();
		IObservableMap[] attributeMap = new IObservableMap [4];
		attributeMap[0] = EMFProperties.value(BfrPackage.Literals.STATISTIK_MODELL__ID).observeDetail(knownElements);
		attributeMap[1] = EMFProperties.value(BfrPackage.Literals.STATISTIK_MODELL__NAME).observeDetail(knownElements);
		attributeMap[2] = EMFProperties.value(BfrPackage.Literals.STATISTIK_MODELL__EINGABEDATUM).observeDetail(knownElements);
		attributeMap[3] = EMFProperties.value(BfrPackage.Literals.STATISTIK_MODELL__FORMEL).observeDetail(knownElements);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(attributeMap);
		
		statModelViewer.setContentProvider(contentProvider);
		statModelViewer.setLabelProvider(labelProvider);
		statModelViewer.setSorter(new StatistikModellSorter());
		statModelViewer.setInput(EMFProperties.list(BfrPackage.Literals.STATISTIK_MODELL_KATALOG__MODELLE).observe(inputList));
		getSite().setSelectionProvider(statModelViewer);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		statModelTable.setFocus();
	}

}
