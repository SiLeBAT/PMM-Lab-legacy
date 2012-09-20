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
 * File: ValidationStatusObservableValue.java
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * This observable value returns <code>true</code>, if the validation status is OK.
 * Otherwise <code>false</code> will be returned.
 * @author mark
 *
 */
public class ValidationStatusObservableValue extends AbstractObservableValue {
	
	private final AggregateValidationStatus validationStatus;
	private boolean okStatus = true;
	private final IValueChangeListener changeListener = new IValueChangeListener() {
		
		@Override
		public void handleValueChange(ValueChangeEvent event) {
			IStatus status = (IStatus)event.diff.getNewValue();
			checkValueStatus(status);
		}
	};

	public ValidationStatusObservableValue(AggregateValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
		validationStatus.addValueChangeListener(changeListener);
		IStatus status = (IStatus)this.validationStatus.getValue();
		checkValueStatus(status);
	}
	
	/**
	 * Checks the status for OK status and fires the value change
	 * @param status the {@link IStatus}
	 */
	private void checkValueStatus(IStatus status) {
		boolean old = okStatus;
		okStatus = ValidationStatus.ok().equals(status);
		fireValueChange(Diffs.createValueDiff(old, okStatus));
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.AbstractObservable#dispose()
	 */
	@Override
	public synchronized void dispose() {
		validationStatus.removeValueChangeListener(changeListener);
		super.dispose();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
	 */
	@Override
	public Object getValueType() {
		return Boolean.TYPE;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return okStatus;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		// Nothing to set here
	}

}
