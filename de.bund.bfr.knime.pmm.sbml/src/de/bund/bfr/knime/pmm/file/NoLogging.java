/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package de.bund.bfr.knime.pmm.file;

import java.beans.PropertyChangeEvent;

import javax.swing.tree.TreeNode;

import org.sbml.jsbml.util.TreeNodeChangeListener;
import org.sbml.jsbml.util.TreeNodeRemovedEvent;

public class NoLogging implements TreeNodeChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}

	@Override
	public void nodeRemoved(TreeNodeRemovedEvent event) {
	}

	@Override
	public void nodeAdded(TreeNode node) {
	}
}
