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
package org.eclipse.stem.gis;

import java.io.File;
import java.io.InputStream;

import org.eclipse.stem.gis.shp.ShpConstants;

public class GisUtils 
{
	public static void safeClose(InputStream is)
	{
		try {
			is.close();
		} catch (Throwable t) {
			// nothin' doin'
		}
	}
	
	public static File getDbfFileForShp(File shpFile)
	{
		return GisUtils.getShpMetaFile(shpFile, ShpConstants.SHP_DATABASE_FILE_EXT);
	}
	
	public static File getProjectionFileForShp(File shpFile)
	{
		return GisUtils.getShpMetaFile(shpFile, ShpConstants.SHP_PROJECTION_FILE_EXT);
	}
	
	
	public static File getShpMetaFile(File shpFile, String ext)
	{
		String shpFileNameWithExt = shpFile.getName();
		String shpFileName = shpFileNameWithExt.substring(0, shpFileNameWithExt.lastIndexOf('.'));
	
		if (ext.charAt(0) != '.') {
			ext= "." + ext;
		}
		
		return new File(shpFile.getParentFile(), shpFileName + ext);
	}
	
}
