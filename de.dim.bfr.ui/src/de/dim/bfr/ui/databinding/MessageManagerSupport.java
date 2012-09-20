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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/MessageManagerSupport.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDecoratingObservable;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.viewers.IViewerObservable;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.widgets.Form;

/**
 * Message manager support. This code was taken from the bug
 * {@linkplain https://bugs.eclipse.org/bugs/show_bug.cgi?id=219661}
 * and modified an commented by Mark Hoffmann
 * @author Matt Biggs 
 * @since 20.07.2011
 */
public class MessageManagerSupport {
	protected final Form form;
	protected final IMessageManager messageManager;
	protected final DataBindingContext dataBindingContext;
	protected final HashMap<ValidationStatusProvider, IValueChangeListener> valueChangeListeners;
	protected final List<ValidationStatusProvider> excludedProviders;

	// handles changes in the validation status provider list
	protected final IListChangeListener listChangeListener = new IListChangeListener() {		
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.list.IListChangeListener#handleListChange(org.eclipse.core.databinding.observable.list.ListChangeEvent)
		 */
		@Override
		public void handleListChange(ListChangeEvent event) {
			// register visitor for the list changes
			event.diff.accept(new ListDiffVisitor() {								
				/* 
				 * (non-Javadoc)
				 * @see org.eclipse.core.databinding.observable.list.ListDiffVisitor#handleRemove(int, java.lang.Object)
				 */
				@Override
				public void handleRemove(int index, Object element) {
					if( element instanceof ValidationStatusProvider ) {
						final ValidationStatusProvider observable = (ValidationStatusProvider)element;
						removeValueChangeListener(observable);
					}
				}
				
				/* 
				 * (non-Javadoc)
				 * @see org.eclipse.core.databinding.observable.list.ListDiffVisitor#handleAdd(int, java.lang.Object)
				 */
				@Override
				public void handleAdd(int index, Object element) {
					if( element instanceof ValidationStatusProvider ) {
						final ValidationStatusProvider observable = (ValidationStatusProvider)element;
						addValueChangeListener(observable);
					}
				}
			});
		}
	};
	
	protected final IDisposeListener observableDisposeListener = new IDisposeListener() {
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.IDisposeListener#handleDispose(org.eclipse.core.databinding.observable.DisposeEvent)
		 */
		public void handleDispose(DisposeEvent staleEvent) {
			dispose();
		}
	};
	
	/**
	 * Listener that listens to the disposal of the form
	 */
	protected final DisposeListener formDisposeListener = new DisposeListener() {		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
		 */
		@Override
		public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
			dispose();
		}
	};
	
	/**
	 * Implementation of the {@link IValueChangeListener} just as internal helper
	 * @author Mark Hoffmann
	 * @since 20.07.2011
	 */
	protected final class ValueChangeListener implements IValueChangeListener {
		private final ValidationStatusProvider provider;
		
		/**
		 * Constructor
		 * @param provider
		 */
		public ValueChangeListener(ValidationStatusProvider provider) {
			this.provider = provider;
		}		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.IValueChangeListener#handleValueChange(org.eclipse.core.databinding.observable.value.ValueChangeEvent)
		 */
		@Override
		public void handleValueChange(ValueChangeEvent event) {
			handleStatusChanged(provider.getTargets(), (IStatus)event.diff.getOldValue(), (IStatus)event.diff.getNewValue());
		}		
	}
	
	/**
	 * Creates a {@link MessageManagerSupport} for a given {@link Form} and 
	 * {@link DataBindingContext}. With this support you don't need any
	 * {@link ControlDecorationSupport}. This is automatically handled here.
	 * For setting the decoration position use {@link IMessageManager#setDecorationPosition(int)}
	 * @param managedForm the {@link IManagedForm}
	 * @param context the {@link DataBindingContext}
	 * @return the instance of the {@link MessageManagerSupport}
	 */
	public static MessageManagerSupport create(IManagedForm managedForm, DataBindingContext context) {
		return new MessageManagerSupport(managedForm, context);
	}
	
	/**
	 * Constructor for the support
	 * @param managedForm the managed form for which this support is valid
	 * @param dataBindingContext the data binding context
	 */
	protected MessageManagerSupport(IManagedForm managedForm, DataBindingContext dataBindingContext) {
		this.form = managedForm.getForm().getForm();
		this.form.addMessageHyperlinkListener(new MessageManagerHyperlinkListener(managedForm.getToolkit()));
		this.messageManager = form.getMessageManager();
		this.dataBindingContext = dataBindingContext;
		this.valueChangeListeners = new HashMap<ValidationStatusProvider, IValueChangeListener>();
		this.excludedProviders = new LinkedList<ValidationStatusProvider>();
		init();
	}
	
	/**
	 * Initializes this {@link MessageManagerSupport}
	 */
	protected void init() {
		
		// Listen to the Form being disposed
		getForm().addDisposeListener(formDisposeListener);
		
		// Listen to any new ValidationStatusProviders
		getDataBindingContext().getValidationStatusProviders().addDisposeListener(observableDisposeListener);
		getDataBindingContext().getValidationStatusProviders().addListChangeListener(listChangeListener);		
		
		// Listen to changes to any existing ValidationStatusProviders
		for( Object element : getDataBindingContext().getValidationStatusProviders() ) {	
			final ValidationStatusProvider observable = (ValidationStatusProvider)element;
			addValueChangeListener(observable);
		}

	}

	/**
	 * Callback when a status has changed
	 * @param target the target {@link ObservableList}
	 * @param oldStatus the former / old status
	 * @param newStatus the current / new status
	 */
	protected void handleStatusChanged(IObservableList target, final IStatus oldStatus, final IStatus newStatus) {
		final boolean isAutoUpdate = getMessageManager().isAutoUpdate();
		try {
			final Control control = findControl(target);
			
			// Disable automatic update during our operations
			if( isAutoUpdate ) {
				getMessageManager().setAutoUpdate(false);
			}
			
			// Remove the old status
			if( control != null ) {
				getMessageManager().removeMessage(oldStatus, control);
			} else {
				if (oldStatus.isMultiStatus()) {
					for (IStatus status : oldStatus.getChildren()) {
						getMessageManager().removeMessage(status);
					}
				} else {
					getMessageManager().removeMessage(oldStatus);
				}
			}
			
			// Add new status if it is a validation failure
			if( !newStatus.isOK() ) {
				if( control != null ) {
					getMessageManager().addMessage(newStatus, getDescriptionText(newStatus), newStatus, getStatusType(newStatus), control);
				} else {
					if (newStatus.isMultiStatus()) {
						for (IStatus status : newStatus.getChildren()) {
							if (!status.isOK()) {
								getMessageManager().addMessage(status, getDescriptionText(status), status, getStatusType(status));
							}
						}
					} else {
						getMessageManager().addMessage(newStatus, getDescriptionText(newStatus), newStatus, getStatusType(newStatus));
					}
				}
			}
			
		} finally {
			// Re-enable update
			if( isAutoUpdate ) {
				getMessageManager().setAutoUpdate(true);
			}
		}
	}

	/**
	 * Adds a {@link ValidationStatusProvider} to the support
	 * @param observable the {@link ValidationStatusProvider} to add
	 */
	protected void addValueChangeListener(ValidationStatusProvider observable) {
		final IValueChangeListener valueChangeListener = new ValueChangeListener(observable);
		
		final IObservableValue value = observable.getValidationStatus();
		value.addDisposeListener(observableDisposeListener);
		value.addValueChangeListener(valueChangeListener);
		
		valueChangeListeners.put(observable, valueChangeListener);
	}
	
	/**
	 * Removes a {@link ValidationStatusProvider} from the support
	 * @param observable the {@link ValidationStatusProvider} to remove from this support
	 */
	protected void removeValueChangeListener(ValidationStatusProvider observable) {				
		final IValueChangeListener valueChangeListener = valueChangeListeners.get(observable);
		
		final IObservableValue value = observable.getValidationStatus();
		value.removeDisposeListener(observableDisposeListener);
		value.removeValueChangeListener(valueChangeListener);
		
		valueChangeListeners.remove(observable);
	}
	
	/**
	 * Tries to find a control for the given {@link IObservable}
	 * @param target the {@link IObservable} to extract the control from
	 * @return a control for the given {@link IObservable}
	 */
	protected Control findControl(IObservable target) {
		if( target instanceof IObservableList ) {
			IObservableList list = (IObservableList) target;
			for( int i = 0; i < list.size(); i++ ) {
				Control control = findControl((IObservable) list.get(i));
				if( control != null )
					return control;
			}				
		}
		
		if (target instanceof ISWTObservable) {
			Widget widget = ((ISWTObservable) target).getWidget();
			if (widget instanceof Control)
				return (Control) widget;
		}

		if (target instanceof IViewerObservable) {
			Viewer viewer = ((IViewerObservable) target).getViewer();
			return viewer.getControl();
		}

		if (target instanceof IDecoratingObservable) {
			IObservable decorated = ((IDecoratingObservable) target).getDecorated();
			Control control = findControl(decorated);
			if (control != null)
				return control;
		}

		if (target instanceof IObserving) {
			Object observed = ((IObserving) target).getObserved();
			if (observed instanceof IObservable)
				return findControl((IObservable) observed);
		}

		return null;
	}
	
	/**
	 * Disposes the {@link MessageManagerSupport}
	 */
	public void dispose() {
		// Remove Form dispose listener
		getForm().removeDisposeListener(formDisposeListener);
		
		// Remove notifications of changes to the values
		for( Object element : getDataBindingContext().getValidationStatusProviders() ) {
			final ValidationStatusProvider observable = (ValidationStatusProvider)element;
			removeValueChangeListener(observable);
		}
		
		// Remove notification of changes to the ValidationStatusProviders
		getDataBindingContext().getValidationStatusProviders().removeDisposeListener(observableDisposeListener);
		getDataBindingContext().getValidationStatusProviders().removeListChangeListener(listChangeListener);
	}
	
	/**
	 * Removes a {@link ValidationStatusProvider}, that now will be ignored 
	 * by this {@link MessageManagerSupport}
	 * @param provider the {@link ValidationStatusProvider} to ignore
	 */
	public void removeValidationStatusProvider(ValidationStatusProvider provider) {
		removeValueChangeListener(provider);
	}
	
	/**
	 * Adds a {@link ValidationStatusProvider}, that will be under control
	 * of this support.
	 * @param provider the {@link ValidationStatusProvider}
	 */
	public void addValidationStatusProvider(ValidationStatusProvider provider) {
		addValueChangeListener(provider);
	}
	
	/**
	 * Returns the description of a given status
	 * @param status the status to get the description from
	 * @return the description of a given status
	 */
	protected String getDescriptionText(IStatus status) {
		return status == null ? "" : status.getMessage(); //$NON-NLS-1$
	}

	/**
	 * Returns the message type for a given status type
	 * @param status the status type
	 * @return the message type for a given status type
	 */
	protected int getStatusType(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.OK:
			return IMessageProvider.NONE;
		case IStatus.CANCEL:
			return IMessageProvider.NONE;
		case IStatus.INFO:
			return IMessageProvider.INFORMATION;
		case IStatus.WARNING:
			return IMessageProvider.WARNING;
		case IStatus.ERROR:
			return IMessageProvider.ERROR;
		default:                       
			return IMessageProvider.NONE;
		}
	}

	/**
	 * Returns the {@link Form}
	 * @return the {@link Form}
	 */
	protected Form getForm() {
		return form;
	}
	
	/**
	 * Returns the message manager
	 * @return the message manager
	 */
	public IMessageManager getMessageManager() {
		return messageManager;
	}

	/**
	 * Returns the data binding context
	 * @return the data binding context
	 */
	protected DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}
}
