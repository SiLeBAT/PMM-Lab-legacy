package org.hsh.bfr.db.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.hsh.bfr.db.MyDBI;
import org.hsh.bfr.db.MyDBTablesNew;
import org.hsh.bfr.db.MyTable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XmlLoader {

	private static XStream xstream = getXStream();
	
	private static XStream getXStream() {
		XStream xstream = new XStream(null, new XppDriver(),new ClassLoaderReference(MyDBI.class.getClassLoader()));
		xstream.omitField(MyDBI.class, "conn");
		xstream.omitField(MyDBI.class, "dbUsername");
		xstream.omitField(MyDBI.class, "dbPassword");
		xstream.omitField(MyDBI.class, "dbPath");
		xstream.omitField(MyDBI.class, "path2XmlFile");
		xstream.omitField(MyDBI.class, "isServerConnection");
		xstream.omitField(MyDBI.class, "passFalse");
		xstream.omitField(MyDBI.class, "filledHashtables");
		xstream.omitField(MyDBTablesNew.class, "isPmm");
		xstream.omitField(MyDBTablesNew.class, "isKrise");
		xstream.omitField(MyDBTablesNew.class, "isSiLeBAT");
		//xstream.omitField(MyTable.class, "rowHeights");
		xstream.omitField(MyTable.class, "colWidths");
		xstream.omitField(MyTable.class, "sortKeyList");
		xstream.omitField(MyTable.class, "searchString");
		xstream.omitField(MyTable.class, "selectedRow");
		xstream.omitField(MyTable.class, "selectedCol");
		xstream.omitField(MyTable.class, "verticalScrollerPosition");
		xstream.omitField(MyTable.class, "horizontalScrollerPosition");
		xstream.omitField(MyTable.class, "form_SelectedID");
		xstream.omitField(MyTable.class, "caller4Trigger");
		xstream.omitField(MyTable.class, "mnSQL");
		return xstream;
	}
	private static String getXml(MyDBI myDBi) {
		String xml = xstream.toXML(myDBi);		
		return xml;
	}
	public static void save2File(String xmlFile, MyDBI myDBi) {
		try {
			File file = new File(xmlFile);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(getXml(myDBi));
			output.close();
	    }
		catch (IOException e) {
			e.printStackTrace();
	    }
	}
	public static Object getObjectFromFile(String xmlFile) {
		Object result = null;
		BufferedReader br = null;
	    try {
			br = new BufferedReader(new FileReader(xmlFile));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        result = getObject(sb.toString());
	    }
	    catch (Exception e) {e.printStackTrace();}
	    finally {
	        if (br != null) {
				try {
					br.close();
				}
		        catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	    return result;
	}
	private static Object getObject(String xml) {
		return xstream.fromXML(xml);
	}
}
