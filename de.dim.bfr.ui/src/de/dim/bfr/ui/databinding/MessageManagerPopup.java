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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/databinding/MessageManagerPopup.java $
 * $LastChangedDate: 2012-01-24 11:03:21 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 652 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.databinding;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Popup dialog for the message managers messages
 * @author Mark Hoffmann
 * @since 20.07.2011
 */
public class MessageManagerPopup extends PopupDialog {
	
	private Point offset = new Point(0, 5);
	private final Point position;
	private final FormToolkit toolkit;
	private final ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
	private final IMessage[] messages;
	private final IHyperlinkListener hyperLinkListener = new HyperlinkAdapter() {
		
		/**
		 * @see IHyperlinkListener#linkActivated(HyperlinkEvent)
		 */
		@Override
		public void linkActivated(HyperlinkEvent e) {
			close();
			if (e.data != null && e.data instanceof Control) {
				((Control)e.data).setFocus();
			}
		}
	};
	
	/**
	 * Constructor with parent shell and title text
	 * @param source the source control to create the pop-up from
	 * @param toolkit the form toolkit
	 * @param titleText the title text
	 * @param messages the messages to display
	 */
	public MessageManagerPopup(Control source, FormToolkit toolkit, String titleText, IMessage[] messages) {
		super(source.getShell(), PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE,	true, false, false, false, false, titleText, null);
		this.position = source.toDisplay(0, 0);
		this.offset.y += source.getSize().y; 
		this.toolkit = toolkit;
		this.messages = messages;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.PopupDialog#getInitialLocation(org.eclipse.swt.graphics.Point)
	 */
	@Override
	protected Point getInitialLocation(Point initialSize) {
		return new Point(position.x + offset.x, position.y + offset.y);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.PopupDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {	
		ScrolledForm scrollForm = toolkit.createScrolledForm(parent);
		scrollForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite = scrollForm.getBody();
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		for (IMessage message : messages){
			Control msgControl = message.getControl();
			
			ImageHyperlink h = toolkit.createImageHyperlink(composite, SWT.LEFT);
			String prefix = message.getPrefix() == null ? "" : message.getPrefix(); //$NON-NLS-1$
			h.setText(prefix + message.getMessage());
			
			Image img = getImage(message);
			h.setImage(img);
			if (msgControl != null) {// general message
				h.setData(msgControl);
			}
			h.addHyperlinkListener(hyperLinkListener);
			int width = Math.min(h.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 600);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).hint(width, SWT.DEFAULT).applyTo(h);
		}						
		return composite;
	}
	
	/**
	 * Returns the image for the corresponding message type
	 * @param message the message to extract the type from
	 * @return the image for the corresponding message type
	 */
	protected Image getImage(IMessage message) {
		Image img = null;
		switch (message.getMessageType()) {
		case IMessageProvider.ERROR:
			img = images.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
			break;
		case IMessageProvider.WARNING:
			img = images.getImage(ISharedImages.IMG_OBJS_WARN_TSK);
			break;
		default:
			img = images.getImage(ISharedImages.IMG_OBJS_INFO_TSK);
			break;
		}
		return img;
	}

	/**
	 * Method that returns the offset
	 * @return the offset of the popup position to the source control
	 */
	public Point getOffset() {
		return offset;
	}

	/**
	 * Sets the offset of the popup position to the source control
	 * @param offset the offset value
	 */
	public void setOffset(Point offset) {
		this.offset = offset;
	}

}
