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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/EstimatedModellEditorInput.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.ui.internal.BFRUIActivator;
import de.dim.bfr.ui.message.Messages;

/**
 * Editor input for the model
 * @author Mark Hoffmann
 * @since 13.11.2011
 */
public class EstimatedModellEditorInput implements IEditorInput {

	private final GeschaetztStatistikModell estimatedStatisticModel;
	
	/**
	 * Constructor
	 */
	public EstimatedModellEditorInput() {
		estimatedStatisticModel = BfrFactory.eINSTANCE.createGeschaetztStatistikModell();
		estimatedStatisticModel.setManuellEingetragen(true);
	}
	
	/**
	 * Constructor with instance parameter
	 * @param model the estimated statistic model instance
	 */
	public EstimatedModellEditorInput(GeschaetztStatistikModell model) {
		estimatedStatisticModel = model;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(GeschaetztStatistikModell.class)) {
			return estimatedStatisticModel;
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return BFRUIActivator.getImageDescriptor("icons/obj16/estimated_model.png"); //$NON-NLS-1$
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	@Override
	public String getName() {
		String name = estimatedStatisticModel.getStatistikModel() == null ? Messages.EstimatedModellEditorInput_1 : estimatedStatisticModel.getStatistikModel().getName() + Messages.EstimatedModellEditorInput_2;
		return (name == null || name.length() == 0) ? Messages.EstimatedModellEditorInput_3 : name;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return getName();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EstimatedModellEditorInput) {
			EstimatedModellEditorInput input = (EstimatedModellEditorInput) obj;
			return input.estimatedStatisticModel.getId() == estimatedStatisticModel.getId();
		}
		if (obj instanceof StatistikModell) {
			StatistikModell model = (StatistikModell) obj;
			return model.getId() == estimatedStatisticModel.getId();
		}
		return super.equals(obj);
	}

}
