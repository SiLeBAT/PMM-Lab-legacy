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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/validators/ActiveNotEmptyValidator.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding.validators;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * Not emtpy validator that checks an additional activity state
 * @author Mark Hoffmann
 * @since 14.12.2011
 */
public class ActiveNotEmptyValidator extends NotEmptyValidator {

	private final IObservableValue activityOV;

	/**
	 * Constructor
	 * @param fieldName the field name which should not be empty
	 * @param activityOV the {@link IObservableValue} with a boolean activity state
	 */
	public ActiveNotEmptyValidator(String fieldName, IObservableValue activityOV) {
		super(fieldName);
		this.activityOV = activityOV;
	}
	
	@Override
	public IStatus validate(Object value) {
		Object activity = activityOV.getValue();
		if (activity != null && activity instanceof Boolean) {
			// if active is false, no further validation is neccessary
			if (Boolean.FALSE.equals(activity)) {
				return ValidationStatus.ok();
			}
		}
		return super.validate(value);
	}

}
