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
package org.eclipse.stem.gis.shp;

import org.eclipse.stem.gis.dbf.DbfRecord;

public abstract class ShpRecord 
{
	protected int recordNumber;
	protected int contentLength;

	protected DbfRecord dbfRecord;
	
	protected ShpRecord()
	{
	}
	
	public DbfRecord getTableAttributes()
	{
		return dbfRecord;
	}

	public void setTableAttributes(DbfRecord dbfRecord)
	{
		this.dbfRecord = dbfRecord;
	}
	
	public int getRecordNumber() 
	{
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	@Override
	public String toString() {
		return "ShpRecord [recordNumber=" + recordNumber + ", contentLength="
				+ contentLength + ", shpType=" + getType() + ", dbfRecord="
				+ dbfRecord + "]";
	}
	
	
	abstract int getType();

}
