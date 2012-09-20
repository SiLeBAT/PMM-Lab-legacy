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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class TabListener implements ComponentListener {
	
	private ComponentEvent event;

	public ComponentEvent getEvent() {
		return event;
	}

	public void setEvent(ComponentEvent event) {
		this.event = event;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		this.event = e;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.event = e;
	}

	@Override
	public void componentShown(ComponentEvent e) {
		this.event = e;
	}

}
