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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/validators/ParameterValidator.java $
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
public class ParameterValidator extends ParameterIntegerValidator {
	
	private final IObservableValue minOV;
	private final IObservableValue maxOV;

	public ParameterValidator(IObservableValue integerOV, IObservableValue minOV, IObservableValue maxOV) {
		super(integerOV);
		this.minOV = minOV;
		this.maxOV = maxOV;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		IStatus status = super.validate(value);
		if (!status.isOK()) {
			return status;
		}
		Double min = (Double) minOV.getValue();
		Double max = (Double) maxOV.getValue();
		if (min == null && max == null) {
			if (doubleValue == null) {
				return ValidationStatus.error(Messages.ParameterValidator_7);
			} else {
				return ValidationStatus.ok();
			}
		} else if (min == null && max != null) {
			return (doubleValue !=null && doubleValue <= max) ? ValidationStatus.ok() : ValidationStatus.error(Messages.ParameterValidator_0 + max + Messages.ParameterValidator_1);
		} else if (min != null && max == null) {
			return (doubleValue != null && doubleValue >= min) ? ValidationStatus.ok() : ValidationStatus.error(Messages.ParameterValidator_2 + min + Messages.ParameterValidator_3);
		} else {
			return (doubleValue != null && doubleValue >= min && doubleValue <= max) ? ValidationStatus.ok() : ValidationStatus.error(Messages.ParameterValidator_4 + min + Messages.ParameterValidator_5 + max + Messages.ParameterValidator_6);
		}
	}

}
