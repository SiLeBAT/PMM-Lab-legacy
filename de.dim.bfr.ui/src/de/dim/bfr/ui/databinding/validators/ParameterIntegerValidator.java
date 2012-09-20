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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/validators/ParameterIntegerValidator.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding.validators;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import de.dim.bfr.ui.message.Messages;

/**
 * Validator based upon the meta data from the statistic parameter
 * @author Mark Hoffmann
 * @since 25.11.2011
 */
public class ParameterIntegerValidator extends DoubleValidator {
	
	private final IObservableValue integerOV;

	public ParameterIntegerValidator(IObservableValue integerOV) {
		this.integerOV = integerOV;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		if (integerOV.getValue() == null) {
			return ValidationStatus.error(Messages.ParameterIntegerValidator_0);
		}
		Boolean isInteger = (Boolean) integerOV.getValue();
		IStatus status = super.validate(value);
		if (!status.isOK()) {
			return status;
		}
		if (isInteger && doubleValue != null) {
			if (isInt(doubleValue)) {
				return ValidationStatus.ok();
			} else {
				return ValidationStatus.error(Messages.ParameterIntegerValidator_1);
			}
		}
		return ValidationStatus.ok();
	}
	
	private static boolean isInt(double x) {
	    return x == (int) x;
	}


}
