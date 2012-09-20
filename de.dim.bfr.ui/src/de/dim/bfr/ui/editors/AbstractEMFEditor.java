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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/editors/AbstractEMFEditor.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.editors;

import java.util.EventObject;
import java.util.List;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.dim.bfr.ui.databinding.DIMDatabindingHelper;
import de.dim.bfr.ui.message.Messages;

/**
 * Abstract EMF part that handles dirty state change and change recording
 * @author Mark Hoffmann
 * @since 20.07.2011
 */
public abstract class AbstractEMFEditor extends EditorPart {
	
	protected final EMFDataBindingContext dbc = new EMFDataBindingContext();
	protected boolean dirty = false;
	private final IObservableValue titleObservableValue = new PartNameObservableValue();
	private final IObservableValue titleImageObservableValue = new TitleImageObservableValue();
	private boolean valid = true;
	private ComposedAdapterFactory adapterFactory;
	private AdapterFactoryEditingDomain editingDomain;
	private EObject model;
	private EObject modelCopy;
	private ChangeRecorder recorder;
	
	/**
	 * Observable value for the part name
	 * @author Mark Hoffmann
	 */
	class PartNameObservableValue extends AbstractObservableValue {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue(java.lang.Object)
		 */
		@Override
		protected void doSetValue(Object value) {
			String text = null;
			if (value == null) {
				text = ""; //$NON-NLS-1$
			} else {
				text = (String) value;
			}
			setPartName(text);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
		 */
		@Override
		public Object getValueType() {
			return String.class;
		}

		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
		 */
		@Override
		protected Object doGetValue() {
			return getPartName();
		}
	}
	
	/**
	 * Observable value for the title image
	 * @author Mark Hoffmann
	 */
	class TitleImageObservableValue extends AbstractObservableValue {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue(java.lang.Object)
		 */
		@Override
		protected void doSetValue(Object value) {
			Image image = null;
			if (value instanceof Image) {
				image = (Image) value;
			} 
			setTitleImage(image);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
		 */
		@Override
		public Object getValueType() {
			return Image.class;
		}

		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
		 */
		@Override
		protected Object doGetValue() {
			return getTitleImage();
		}
		
	}


	/**
	 * Default constructor
	 */
	public AbstractEMFEditor() {
		super();
		initEMFStuff();
		final AggregateValidationStatus editorValidationStatus = new AggregateValidationStatus(dbc, AggregateValidationStatus.MAX_SEVERITY);
		editorValidationStatus.addChangeListener(new IChangeListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.core.databinding.observable.IChangeListener#handleChange(org.eclipse.core.databinding.observable.ChangeEvent)
			 */
			@Override
			public void handleChange(ChangeEvent event) {
				IStatus status = (IStatus) editorValidationStatus.getValue();
				valid = status.isOK();
			}
		});
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public final void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		model = (EObject) input.getAdapter(EObject.class);
		if (model == null) {
			throw new IllegalArgumentException(Messages.AbstractEMFEditor_0);
		}
		initWorkingCopy();
		recorder = new ChangeRecorder(model);
		doInit(site, input);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	final public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * Returns <code>true</code>, if the all validations are ok
	 */
	final public boolean isValid() {
		return  valid;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	final public void doSave(IProgressMonitor monitor) {
		if(internalDoSave(monitor)) {
			recorder = new ChangeRecorder(model);
			initWorkingCopy();
			dirty = false;
			firePropertyChange(PROP_DIRTY);
		}
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	final public void doSaveAs() {
		internalDoSaveAs();
	}
	
	/**
	 * Binds the title image
	 * @param model the model {@link IObservableValue} that contains the image
	 * @param m2tStrategy the {@link UpdateValueStrategy}, can be <code>null</code>
	 * @return the {@link Binding}
	 */
	public Binding bindTitleImage(IObservableValue model, UpdateValueStrategy m2tStrategy) {
		if (m2tStrategy == null) {
			return dbc.bindValue(titleImageObservableValue, model, DIMDatabindingHelper.createNeverUpdateValueStrategy(), null);
		} else {
			return dbc.bindValue(titleImageObservableValue, model, DIMDatabindingHelper.createNeverUpdateValueStrategy(), m2tStrategy);
		}
	}
	
	/**
	 * Binds the title name
	 * @param model the model {@link IObservableValue} that contains the title name
	 * @param m2tStrategy the {@link UpdateValueStrategy}, can be <code>null</code>
	 * @return the {@link Binding}
	 */
	public Binding bindTitleName(IObservableValue model, UpdateValueStrategy m2tStrategy) {
		if (m2tStrategy == null) {
			return dbc.bindValue(titleObservableValue, model, DIMDatabindingHelper.createNeverUpdateValueStrategy(), null);
		} else {
			return dbc.bindValue(titleObservableValue, model, DIMDatabindingHelper.createNeverUpdateValueStrategy(), m2tStrategy);
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(EditingDomain.class)) {
			return editingDomain;
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Returns the {@link EditingDomain}
	 * @return
	 */
	protected EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * Delegate method to execute save logic. No dirty state handling is necessary here
	 * in case saving was successful. If saving was not successful, the error handling should happen here.
	 * @param monitor the progress monitor
	 * @return <code>true</code>, if saving was successful. Otherwise <code>false</code> will returned
	 */
	abstract protected boolean internalDoSave(IProgressMonitor monitor);
	
	/**
	 * Delegate method to execute save as logic
	 * @return <code>true</code>, if saving was successful. Otherwise <code>false</code> will returned
	 */
	abstract protected boolean internalDoSaveAs();
	
	/**
	 * Returns the {@link EObject} of the EMF model instance
	 * @return the {@link EObject} of the EMF model instance
	 */
	protected EObject getModel() {
		return model;
	}
	
	/**
	 * Returns the adapter factories from the part
	 * @return the adapter factories from the part
	 */
	abstract protected List<AdapterFactory> getAdapterFactories();
	
	/**
	 * Editor initialization delegation method, called from the {@link IEditorPart#init(IEditorSite, IEditorInput)}. 
	 * @param site the editors site {@link IEditorSite}
	 * @param input the editors input {@link IEditorInput}
	 */
	abstract protected void doInit(IEditorSite site, IEditorInput input);

	/**
	 * Initialize the EMF dirty marker
	 */
	private void initEMFStuff() {
		adapterFactory = new ComposedAdapterFactory(getAdapterFactories());
		BasicCommandStack commands = new BasicCommandStack();
		commands.addCommandStackListener(new CommandStackListener() {
			
			/* 
			 * (non-Javadoc)
			 * @see org.eclipse.emf.common.command.CommandStackListener#commandStackChanged(java.util.EventObject)
			 */
			@Override
			public void commandStackChanged(EventObject event) {
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					
					/* 
					 * (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						dirty = !EcoreUtil.equals(model, modelCopy);
						firePropertyChange(PROP_DIRTY);
					}
				});
				
			}
		});
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commands);
	}
	
	/**
	 * Initializes the working copy
	 */
	protected void initWorkingCopy() {
		modelCopy = EcoreUtil.copy(model);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		recorder.endRecording().apply();
	}
	
}
