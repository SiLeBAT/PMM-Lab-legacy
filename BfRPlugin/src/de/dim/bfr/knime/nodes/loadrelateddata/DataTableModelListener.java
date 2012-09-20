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
package de.dim.bfr.knime.nodes.loadrelateddata;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class DataTableModelListener implements TableModelListener{
	
	private TableModelEvent event;

	
	public DataTableModelListener() {
		this.event = null;
	}
	
	public TableModelEvent getEvent() {
		return this.event;
	}
	
	public void setEvent(TableModelEvent event) {
		this.event = event;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		this.event = e;
	}

}
