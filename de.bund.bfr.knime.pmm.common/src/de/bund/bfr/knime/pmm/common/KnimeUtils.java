package de.bund.bfr.knime.pmm.common;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;

import org.knime.core.util.FileUtil;

public class KnimeUtils {
	
	private KnimeUtils() {		
	}

	public static File getFile(String fileName) throws InvalidPathException, MalformedURLException {
		return FileUtil.getFileFromURL(FileUtil.toURL(fileName));
	}
}
