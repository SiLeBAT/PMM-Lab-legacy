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

public class DbfFieldDef
{
	String fieldName;
	char fieldType;
	short fieldSize;
	short fieldDecimalCount;
	byte workAreaId;
	byte mdxFieldFlag;
	

	public String getFieldName() {
		return fieldName;
	}

	public char getFieldType() {
		return fieldType;
	}

	public short getFieldSize() {
		return fieldSize;
	}

	public short getFieldDecimalCount() {
		return fieldDecimalCount;
	}

	public byte getWorkAreaId() {
		return workAreaId;
	}

	public byte getMdxFieldFlag() {
		return mdxFieldFlag;
	}

	@Override
	public String toString() {
		return "DbfFieldDef [fieldName=" + fieldName + ", fieldType="
				+ fieldType + ", fieldSize=" + fieldSize
				+ ", fieldDecimalCount=" + fieldDecimalCount
				+ ", workAreaId=" + workAreaId + ", mdxFieldFlag="
				+ mdxFieldFlag + "]";
	}
	
	
}