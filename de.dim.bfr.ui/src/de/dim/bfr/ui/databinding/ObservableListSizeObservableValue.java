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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/ObservableListSizeObservableValue.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

/**
 * Observable value that notifies any paries about changes in the list size.
 * No value can be set for this observalble value.
 * @author Mark Hoffmann
 * @since 28.11.2011
 */
public class ObservableListSizeObservableValue extends AbstractObservableValue {
	
	private final IObservableList list;
	private int size = 0;
	private final IListChangeListener listener = new IListChangeListener() {
		
		/* 
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.observable.list.IListChangeListener#handleListChange(org.eclipse.core.databinding.observable.list.ListChangeEvent)
		 */
		@Override
		public void handleListChange(ListChangeEvent event) {
			int old = size;
			size = list.size();
			fireValueChange(Diffs.createValueDiff(old, size));
		}
	};

	/**
	 * Constructor with an {@link IObservableList} as parameter
	 * @param list the list to observe the size
	 */
	public ObservableListSizeObservableValue(IObservableList list) {
		this.list = list;
		size = list.size();
		this.list.addListChangeListener(listener);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
	 */
	@Override
	public Object getValueType() {
		return Integer.TYPE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue()
	 */
	@Override
	protected void doSetValue(Object value) {
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return size;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.observable.AbstractObservable#dispose()
	 */
	@Override
	public synchronized void dispose() {
		list.removeListChangeListener(listener);
		super.dispose();
	}

}
