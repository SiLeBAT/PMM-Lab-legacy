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
 * File: NotEmptyValidator.java
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import de.dim.bfr.ui.message.Messages;

/**
 * Validates, if the given value is not empty. Otherwise a validation error will be returned.
 * @author Mark Hoffmann
 * @since 05.07.2011
 */
public class NotEmptyValidator implements IValidator {
	
	private final String fieldName;

	/**
	 * Constructor with field name parameter
	 * @param fieldName the field name to validate
	 */
	public NotEmptyValidator(String fieldName) {
		this.fieldName = fieldName;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return value != null ? 
				ValidationStatus.ok() : 
					ValidationStatus.error(Messages.NotEmptyValidator_0 + fieldName + Messages.NotEmptyValidator_1);
	}

}
