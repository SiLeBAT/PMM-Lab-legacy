/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.stem.gis.dbf;

import java.util.ArrayList;
import java.util.List;

public class DbfRecord
{
	protected boolean deletedFlag = false;
	protected List<Object> data = new ArrayList<Object>();

	public boolean isDeletedFlag() {
		return deletedFlag;
	}

	public List<Object> getData() {
		return data;
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "DbfRecord [data="
				+ (data != null ? data.subList(0,
						Math.min(data.size(), maxLen)) : null) + "]";
	}
	
}