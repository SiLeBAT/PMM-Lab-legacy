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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/validators/DoubleValidator.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import de.dim.bfr.ui.message.Messages;

/**
 * Validates a double value. <code>null</code> values are allowed.
 * @author Mark Hoffmann
 * @since 28.11.2011
 */
public class DoubleValidator implements IValidator {
	
	protected Double doubleValue = null;
	private final IStatus errorStatus = ValidationStatus.error(Messages.DoubleValidator_0);

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		if (value == null || value.toString().length() == 0) {
			return ValidationStatus.ok();
		}
		if (value != null && value instanceof String) {
			try {
				doubleValue = Double.valueOf(value.toString());
			} catch (Exception e) {
				return errorStatus;
			}
		} else if (!(value instanceof Double)) {
			return errorStatus;
		} 
		return ValidationStatus.ok();
	}

}
