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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.SwappedDataInputStream;

/**
 * Simple implementation of a dBASE v5 and older file reader
 *
 */
public class DbfInputStream extends SwappedDataInputStream
{
	protected DbfHeader header;
	
	public DbfInputStream(InputStream input) throws IOException
	{
		super(input);
		readHeader();
	}

	/**
	 * 
	 * @return The dBASE File Header
	 */
	public DbfHeader getHeader()
	{
		return header;
	}
	
	/**
	 * @return If there are more records to read
	 * @throws IOException
	 */
	public boolean hasMoreRecords() throws IOException
	{
		return !atEOF();
	}

	/**
	 * Reads the next dBASE table record (row) from a file
	 * @return The table record or null if no more records exist
	 * @throws IOException
	 */
	public DbfRecord readNextRecord() throws IOException
	{
		if (!hasMoreRecords()) {
			return null;
		}

		DbfRecord record = new DbfRecord();
		
		// Read the record into memory
		byte[] buf = new byte[header.recordSize];
		readFully(buf);
		
		int bufIdx = 0;
		
		// byte 0 - deleted flag
		record.deletedFlag = (buf[bufIdx++] == 0x2A);
		
		// Loop through the field definitions and partition data
		for (DbfFieldDef def : header.fieldDefinitions) {
			Object dataObject = new String(buf, bufIdx, def.fieldSize).trim();
			try {
				switch (def.fieldType) {
				case 'N': if (def.fieldDecimalCount == 0) {Integer.valueOf((String)dataObject);break;}
				case 'F': dataObject = Float.valueOf((String)dataObject);break;
				default: break;
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			record.data.add(dataObject);
			
			bufIdx += def.fieldSize;
		}
		
		return record;
	}
	
	/**
	 * Reads a dBASE v5 32 byte header
	 * @throws IOException
	 */
	protected void readHeader() throws IOException
	{
		DbfHeader header = new DbfHeader();

		byte versionByte = readByte();
		header.version = (short)(versionByte & 0xf); // 0 / version
		
		header.dateYear = 1900+readUnsignedByte(); // 1 / YY
		header.dateMonth = readUnsignedByte(); // 2 / MM
		header.dateDay = readUnsignedByte(); // 3 / DD
		
		header.records = readInt(); // 4-7
		header.headerSize = readUnsignedShort(); // 8-9
		header.recordSize = readUnsignedShort(); // 10-11
				
		skipBytes(2); // reserved, 12-13
		
		header.incompleteTransactionFlag = readByte(); // 14
		header.encryptionFlag = readByte(); // 15

		skipBytes(12); // reserved, 16-27
		
		header.prodMdxFlag = readByte(); // 28
		header.driverId = readByte(); // 29

		// reserved, 30-31
		skipBytes(2);
		
		// dBASE v7 Fields (bytes 32-67)
//		// 32-63 // driverName
//		read(buf, 0, 32);
//		header.driveName = new String(buf, 0, 32);
//		readInt(); // 64-67 / reserved

		while (!atEOH()) {
			DbfFieldDef fd = readFieldDescriptor();
			header.fieldDefinitions.add(fd);
		}
		
		skipBytes(1); // field def boundary (0x0D)
		
		this.header = header;
	}
	
	/**
	 * Reads a 32-byte dBASE Table Field Descriptor
	 * @return The field descriptor
	 * @throws IOException
	 */
	protected DbfFieldDef readFieldDescriptor() throws IOException
	{
		DbfFieldDef def = new DbfFieldDef();
		
		byte[] buf = new byte[11];
		readFully(buf, 0, 11); // 0 - 10 / fieldName
		def.fieldName = new String(buf, 0, 11);

		def.fieldType = (char)readByte(); // 11 / fieldType
		skipBytes(4); // 12-15 / reserved
		def.fieldSize = (short)readUnsignedByte(); // 16 / fieldSize
		def.fieldDecimalCount = (short)readUnsignedByte(); // 17 / decimalCount
		skipBytes(2);// 18-19 / reserved
		def.workAreaId = readByte(); // 20 / work area id
		skipBytes(10); // 21-30 / reserved
		def.mdxFieldFlag = readByte(); // 31 / MDX Flag

		return def;
	}
	
	/**
	 * Peeks at next byte to see if it equals the value passed
	 * @param value Value to check
	 * @return Whether the next byte to read equals the value passed
	 * @throws IOException
	 */
	private boolean nextByteEquals(byte value) throws IOException
	{
		mark(1);
		boolean isEqual = (readByte() == value);
		reset();
		return isEqual;
	}
	
	/**
	 * Checks if the next byte to read is the end-of-header boundary (0x0d)
	 * @return If stream is at end of header (EOH) boundary
	 * @throws IOException
	 */
	private boolean atEOH() throws IOException
	{
		return nextByteEquals((byte)0x0d);
	}
	
	/**
	 * Checks if the next byte to read is the end-of-file boundary (0x1a)
	 * @return If stream is at end-of-file (EOF) boundary
	 * @throws IOException
	 */
	private boolean atEOF() throws IOException
	{
		return nextByteEquals((byte)0x1a);
	}
}
