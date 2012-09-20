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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/MessageManagerHyperlinkListener.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dim.bfr.ui.message.Messages;

/**
 * {@link IHyperlinkListener} opens a popup with a
 * links to the messages from the {@link IMessageManager}
 * @author Sebastian Fuchs
 * @since 05.04.2007
 */
public class MessageManagerHyperlinkListener extends HyperlinkAdapter {

	private final FormToolkit toolkit;

	/**
	 * Constructor with toolkit as parameter
	 * @param toolkit the {@link FormToolkit} 
	 */
	public MessageManagerHyperlinkListener(FormToolkit toolkit){
		this.toolkit = toolkit;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.events.HyperlinkAdapter#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
	 */
	@Override
	public void linkActivated(HyperlinkEvent e) {
		PopupDialog pd = new MessageManagerPopup((Control) e.widget, toolkit, Messages.MessageManagerHyperlinkListener_0, (IMessage[]) e.getHref());				
		pd.open();
	}
	
}
