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
package de.dim.bfr.ui.databinding;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * Controls the appearance of a ControlDecoration managed by a
 * ControlDecorationSupport.
 * 
 * @since 1.4
 */
public class DIMControlDecorationUpdater extends ControlDecorationUpdater {

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater#update(org.eclipse.jface.fieldassist.ControlDecoration, org.eclipse.core.runtime.IStatus)
	 */
	public void update(ControlDecoration decoration, IStatus status) {
		super.update(decoration, status);
	}

}
