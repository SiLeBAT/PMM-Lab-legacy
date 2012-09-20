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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.viewers.IViewerObservable;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Decorates the underlying controls of the target observables of a
 * {@link ValidationStatusProvider} with {@link ControlDecoration}s mirroring
 * the current validation status. Only those target observables which implement
 * {@link ISWTObservable} or {@link IViewerObservable} are decorated.
 * 
 * @since 1.4
 */
public class DIMControlDecorationSupport {
	/**
	 * Creates a ControlDecorationSupport which observes the validation status
	 * of the specified {@link ValidationStatusProvider}, and displays a
	 * {@link ControlDecoration} over the underlying SWT control of all target
	 * observables that implement {@link ISWTObservable} or
	 * {@link IViewerObservable}.
	 * 
	 * @param validationStatusProvider
	 *            the {@link ValidationStatusProvider} to monitor.
	 * @param position
	 *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
	 *            constructing {@link DIMControlDecorationSupport}
	 * @return a ControlDecorationSupport which observes the validation status
	 *         of the specified {@link ValidationStatusProvider}, and displays a
	 *         {@link ControlDecoration} over the underlying SWT control of all
	 *         target observables that implement {@link ISWTObservable} or
	 *         {@link IViewerObservable}.
	 */
	public static DIMControlDecorationSupport create(
			ValidationStatusProvider validationStatusProvider, int position) {
		return create(validationStatusProvider, position, null,
				new DIMControlDecorationUpdater(), null);
	}

	/**
	 * Creates a ControlDecorationSupport which observes the validation status
	 * of the specified {@link ValidationStatusProvider}, and displays a
	 * {@link ControlDecoration} over the underlying SWT control of all target
	 * observables that implement {@link ISWTObservable} or
	 * {@link IViewerObservable}.
	 * 
	 * @param validationStatusProvider
	 *            the {@link ValidationStatusProvider} to monitor.
	 * @param position
	 *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
	 *            constructing {@link ControlDecoration} instances.
	 * @param control
	 *            the control to use when constructing
	 *            {@link ControlDecoration} instances.
	 * @return a ControlDecorationSupport which observes the validation status
	 *         of the specified {@link ValidationStatusProvider}, and displays a
	 *         {@link ControlDecoration} over the underlying SWT control of all
	 *         target observables that implement {@link ISWTObservable} or
	 *         {@link IViewerObservable}.
	 */
	public static DIMControlDecorationSupport create(
			ValidationStatusProvider validationStatusProvider, int position, Control control) {
		return create(validationStatusProvider, position, null,
				new DIMControlDecorationUpdater(), control);
	}

	/**
	 * Creates a ControlDecorationSupport which observes the validation status
	 * of the specified {@link ValidationStatusProvider}, and displays a
	 * {@link ControlDecoration} over the underlying SWT control of all target
	 * observables that implement {@link ISWTObservable} or
	 * {@link IViewerObservable}.
	 * 
	 * @param validationStatusProvider
	 *            the {@link ValidationStatusProvider} to monitor.
	 * @param position
	 *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
	 *            constructing {@link ControlDecoration} instances.
	 * @param composite
	 *            the composite to use when constructing
	 *            {@link ControlDecoration} instances.
	 * @param updater
	 *            custom strategy for updating the {@link ControlDecoration}(s)
	 *            whenever the validation status changes.
	 * @param control
	 *            the control to use when constructing
	 *            {@link ControlDecoration} instances.
	 * @return a ControlDecorationSupport which observes the validation status
	 *         of the specified {@link ValidationStatusProvider}, and displays a
	 *         {@link ControlDecoration} over the underlying SWT control of all
	 *         target observables that implement {@link ISWTObservable} or
	 *         {@link IViewerObservable}.
	 */
	public static DIMControlDecorationSupport create(
			ValidationStatusProvider validationStatusProvider, int position,
			Composite composite, DIMControlDecorationUpdater updater, Control control) {
		return new DIMControlDecorationSupport(validationStatusProvider, position,
				composite, updater, control);
	}

	private final int position;
	private final Composite composite;
	private final DIMControlDecorationUpdater updater;

	private IObservableValue validationStatus;
	private IObservableList targets;

	private IDisposeListener disposeListener = new IDisposeListener() {
		public void handleDispose(DisposeEvent staleEvent) {
			dispose();
		}
	};

	private IValueChangeListener statusChangeListener = new IValueChangeListener() {
		public void handleValueChange(ValueChangeEvent event) {
			statusChanged((IStatus) validationStatus.getValue());
		}
	};

	private IListChangeListener targetsChangeListener = new IListChangeListener() {
		public void handleListChange(ListChangeEvent event) {
			event.diff.accept(new ListDiffVisitor() {
				public void handleAdd(int index, Object element) {
					targetAdded((IObservable) element);
				}

				public void handleRemove(int index, Object element) {
					targetRemoved((IObservable) element);
				}
			});
			statusChanged((IStatus) validationStatus.getValue());
		}
	};

	private static class TargetDecoration {
		public final IObservable target;
		public final ControlDecoration decoration;

		TargetDecoration(IObservable target, ControlDecoration decoration) {
			this.target = target;
			this.decoration = decoration;
		}
	}

	private List<TargetDecoration> targetDecorations;
	private final Control externalControl;

	@SuppressWarnings("rawtypes")
	private DIMControlDecorationSupport(
			ValidationStatusProvider validationStatusProvider, int position,
			Composite composite, DIMControlDecorationUpdater updater, Control control) {
		this.position = position;
		this.composite = composite;
		this.updater = updater;
		this.externalControl = control;

		this.validationStatus = validationStatusProvider.getValidationStatus();
		Assert.isTrue(!this.validationStatus.isDisposed());

		this.targets = validationStatusProvider.getTargets();
		Assert.isTrue(!this.targets.isDisposed());

		this.targetDecorations = new ArrayList<DIMControlDecorationSupport.TargetDecoration>();

		validationStatus.addDisposeListener(disposeListener);
		validationStatus.addValueChangeListener(statusChangeListener);

		targets.addDisposeListener(disposeListener);
		targets.addListChangeListener(targetsChangeListener);

		for (Iterator it = targets.iterator(); it.hasNext();)
			targetAdded((IObservable) it.next());

		statusChanged((IStatus) validationStatus.getValue());
	}

	private void targetAdded(IObservable target) {
		Control control = findControl(target);
		if (control != null)
			targetDecorations.add(new TargetDecoration(target,
					new ControlDecoration(control, position, composite)));
	}

	private void targetRemoved(IObservable target) {
		for (Iterator<TargetDecoration> it = targetDecorations.iterator(); it.hasNext();) {
			TargetDecoration targetDecoration = (TargetDecoration) it.next();
			if (targetDecoration.target == target) {
				targetDecoration.decoration.dispose();
				it.remove();
			}
		}
	}

	private Control findControl(IObservable target) {
		if (externalControl != null) {
			return externalControl;
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
			IObservable decorated = ((IDecoratingObservable) target)
					.getDecorated();
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

	private void statusChanged(IStatus status) {
		for (Iterator<TargetDecoration> it = targetDecorations.iterator(); it.hasNext();) {
			TargetDecoration targetDecoration = (TargetDecoration) it.next();
			ControlDecoration decoration = targetDecoration.decoration;
			updater.update(decoration, status);
		}
	}

	/**
	 * Disposes this ControlDecorationSupport, including all control decorations
	 * managed by it. A ControlDecorationSupport is automatically disposed when
	 * its target ValidationStatusProvider is disposed.
	 */
	public void dispose() {
		if (validationStatus != null) {
			validationStatus.removeDisposeListener(disposeListener);
			validationStatus.removeValueChangeListener(statusChangeListener);
			validationStatus = null;
		}

		if (targets != null) {
			targets.removeDisposeListener(disposeListener);
			targets.removeListChangeListener(targetsChangeListener);
			targets = null;
		}

		disposeListener = null;
		statusChangeListener = null;
		targetsChangeListener = null;

		if (targetDecorations != null) {
			for (Iterator<TargetDecoration> it = targetDecorations.iterator(); it.hasNext();) {
				TargetDecoration targetDecoration = (TargetDecoration) it
						.next();
				targetDecoration.decoration.dispose();
			}
			targetDecorations.clear();
			targetDecorations = null;
		}
	}
}
