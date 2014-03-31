package org.hsh.bfr.db.db;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyDBI;
import org.hsh.bfr.db.MyTable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XmlLoader {

	private static XStream xstream = getXStream();
	
	public static XStream getXStream() {
		XStream xstream = new XStream(null, new XppDriver(),new ClassLoaderReference(MyDBI.class.getClassLoader()));
		xstream.omitField(MyTable.class, "rowHeights");
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
	public static String getXml() {
		String xml = xstream.toXML(DBKernel.myDBi);		
		return xml;
	}
	public static Object getObject(String xml) {
		return xstream.fromXML(xml);
	}
	public static void doTest() {
		/*
		try {
	  		String xml = XmlLoader.getXml();
	  		System.err.println(xml);
	  		Object o = XmlLoader.getObject(xml);
	  		System.err.println(o instanceof MyDBI);		
		}
		catch (Exception e) {e.printStackTrace();}
		*/
	}
}
