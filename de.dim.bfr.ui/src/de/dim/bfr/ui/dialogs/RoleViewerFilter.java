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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/dialogs/RoleViewerFilter.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.dialogs;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModellParameter;

/**
 * Filter that only allows roles of type parameter 
 * @author Mark Hoffmann
 * @since 14.12.2011
 */
public class RoleViewerFilter extends ViewerFilter {
	
	private final IObservableList values;

	public RoleViewerFilter() {
		this(null);
	}
	
	/**
	 * Constructor
	 * @param values parameter list
	 */
	public RoleViewerFilter(IObservableList values) {
		this.values = values;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		StatistikModellParameter parameter = (StatistikModellParameter) element;
		boolean filter = false;
		if (values != null) {
			filter = parameter.getRole().equals(ParameterRoleType.PARAMETER) && parameterListContains(parameter);
		} else {
			filter = parameter.getRole().equals(ParameterRoleType.PARAMETER);
		}
		return filter;
	}

	/**
	 * Return <code>true</code>, if the given parameter is already in the list of estimated parameters
	 * @param parameter statistic model parameter
	 * @return <code>true</code>, if the given parameter is already in the list of estimated parameters
	 */
	private boolean parameterListContains(StatistikModellParameter parameter) {
		for (int i = 0; i < values.size(); i++) {
			Object o = values.get(i);
			GeschModellParameter estParameter = (GeschModellParameter) o;
			if (estParameter.getModelParameter().equals(parameter)) {
				return false;
			}
		}
		return true;
	}

}
