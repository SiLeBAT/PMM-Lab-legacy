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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/views/EstimatedStatisticModelView.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.views;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
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
import de.dim.bfr.GeschModelList;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;
import de.dim.bfr.ui.services.BFRUIService;

/**
 * View to show all statistic model instances
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public class EstimatedStatisticModelView extends ViewPart {
	
	public static final String ID = "de.dim.bfr.ui.view.EstimatedStatisticModel"; //$NON-NLS-1$
	private GeschModelList inputList;
	private BFRUIService service;
	private Table statModelTable;
	private TableViewer statModelViewer;
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite)
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		service = BFRUIActivator.getBFRService();
		inputList = service.getAllEstimatedStatisticModels();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		statModelViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		statModelTable = statModelViewer.getTable();
		statModelTable.setLinesVisible(true);
		statModelTable.setHeaderVisible(true);
		
		TableViewerColumn idVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn idCol = idVC.getColumn();
		idCol.setWidth(50);
		idCol.setText(Messages.EstimatedStatisticModelView_1);
		
		TableViewerColumn titelVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn titleCol = titelVC.getColumn();
		titleCol.setWidth(200);
		titleCol.setText(Messages.EstimatedStatisticModelView_2);
		
		TableViewerColumn manualVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn manualCol = manualVC.getColumn();
		manualCol.setWidth(70);
		manualCol.setText(Messages.EstimatedStatisticModelView_0);
		
		TableViewerColumn scoreVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn scoreCol = scoreVC.getColumn();
		scoreCol.setWidth(75);
		scoreCol.setText(Messages.EstimatedStatisticModelView_3);
		
		TableViewerColumn rsquareVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn rsquareCol = rsquareVC.getColumn();
		rsquareCol.setWidth(75);
		rsquareCol.setText(Messages.EstimatedStatisticModelView_4);
		
		TableViewerColumn rssVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn rssCol = rssVC.getColumn();
		rssCol.setWidth(75);
		rssCol.setText(Messages.EstimatedStatisticModelView_5);
		
		TableViewerColumn commentVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn commentCol = commentVC.getColumn();
		commentCol.setWidth(200);
		commentCol.setText(Messages.EstimatedStatisticModelView_6);
		
		TableViewerColumn condVC = new TableViewerColumn(statModelViewer, SWT.NONE);
		TableColumn condCol = condVC.getColumn();
		condCol.setWidth(50);
		condCol.setText(Messages.EstimatedStatisticModelView_7);
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		IObservableSet knownElements = contentProvider.getKnownElements();
		IObservableMap[] attributeMap = new IObservableMap [8];
		attributeMap[0] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__ID).observeDetail(knownElements);
		attributeMap[1] = EMFProperties.value(FeaturePath.fromList(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, BfrPackage.Literals.STATISTIK_MODELL__NAME)).observeDetail(knownElements);
		attributeMap[2] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN).observeDetail(knownElements);
		attributeMap[3] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__SCORE).observeDetail(knownElements);
		attributeMap[4] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__RSQUARED).observeDetail(knownElements);
		attributeMap[5] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__RSS).observeDetail(knownElements);
		attributeMap[6] = EMFProperties.value(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR).observeDetail(knownElements);
		attributeMap[7] = EMFProperties.value(FeaturePath.fromList(BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG, BfrPackage.Literals.VERSUCHS_BEDINGUNG__ID)).observeDetail(knownElements);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(attributeMap);
		
		statModelViewer.setContentProvider(contentProvider);
		statModelViewer.setLabelProvider(labelProvider);
		statModelViewer.setSorter(new EstimatedStatisticModelSorter());
		statModelViewer.setInput(EMFProperties.list(BfrPackage.Literals.GESCH_MODEL_LIST__MODELS).observe(inputList));
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
