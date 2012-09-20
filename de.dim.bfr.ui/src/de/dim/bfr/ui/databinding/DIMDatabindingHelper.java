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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/DIMDatabindingHelper.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import de.dim.bfr.ui.databinding.validators.LevelValidator;
import de.dim.bfr.ui.databinding.validators.NotEmptyStringValidator;

/**
 * Helper class for data binding stuff
 * @author Mark Hoffmann
 * @since 15.06.2011
 */
public class DIMDatabindingHelper {
	
	/**
	 * Returns the observable value for watching the validation state 'OK' of an {@link AggregateValidationStatus}.
	 * @param validationStatus the {@link AggregateValidationStatus}
	 * @return the observable value for watching the validation state 'OK'
	 */
	public static IObservableValue observeValidationStatusOK(AggregateValidationStatus validationStatus) {
		return new ValidationStatusObservableValue(validationStatus);
	}
	
	/**
	 * Creates a {@link UpdateValueStrategy} with the policy NEVER
	 * @return {@link UpdateValueStrategy} with the policy NEVER
	 */
	public static UpdateValueStrategy createNeverUpdateValueStrategy() {
		return new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
	}
	
	/**
	 * Returns an update value strategy with an not emtpy string validator
	 * @param fieldName the name of the field
	 * @return an update value strategy with an not emtpy string validator
	 */
	public static UpdateValueStrategy createNotEmptyUpdateStrategy(final String fieldName) {
		return new UpdateValueStrategy().setAfterGetValidator(new NotEmptyStringValidator(fieldName));
	}
	
	/**
	 * Returns an update value strategy with an not emtpy string validator
	 * @param fieldName the name of the field
	 * @return an update value strategy with an not emtpy string validator
	 */
	public static UpdateValueStrategy createLevelUpdateStrategy() {
		return new UpdateValueStrategy().setAfterGetValidator(new LevelValidator());
	}

}
