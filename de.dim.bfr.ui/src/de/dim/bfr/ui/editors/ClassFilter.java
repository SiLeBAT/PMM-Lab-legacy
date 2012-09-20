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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/ClassFilter.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.dim.bfr.KlasseTyp;
import de.dim.bfr.LevelTyp;

/**
 * Viewer filter for the class depending on the level selection
 * @author Mark Hoffmann
 * @since 09.12.2011
 */
public class ClassFilter extends ViewerFilter {
	
	private final IObservableValue selection;
	private final IChangeListener selectionChange = new IChangeListener() {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
		 */
		@Override
		public void handleChange(ChangeEvent event) {
			Object value = selection.getValue();
			if (value != null && value instanceof LevelTyp) {
				levelType = (LevelTyp)value;
				if (viewer != null) {
					viewer.refresh();
				}
			}
			
		}
	};
	private LevelTyp levelType = LevelTyp.OTHER;
	private Viewer viewer;

	/**
	 * Constructor with selection observable value 
	 * @param selection the selection observable value
	 */
	public ClassFilter(IObservableValue selection) {
		Assert.isNotNull(selection);
		this.selection = selection;
		this.selection.addChangeListener(selectionChange);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		this.viewer = viewer;
		KlasseTyp classType = (KlasseTyp) element;
		switch (levelType) {
		case PRIMARY:
			return classType.getValue() < 8 && classType.getValue() > 0;
		case SECONDARY:
			return classType.getValue() >= 8;
		default:
			return false;
		}
	}
	
}
