package de.bund.bfr.knime.pmm.common;

import java.sql.Array;
import java.sql.SQLException;

public class DbIo {

    public static String convertArray2String(Array array) {
    	String result = null;
	    if (array != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				if (o != null && o.length > 0) {
					result = convertO(o[0]);
					for (int i=1;i<o.length;i++) {
						result += "," + convertO(o[i]);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
    	return result;
    }
    public static PmmXmlDoc convertArrays2ParamXmlDoc(Array name, Array value, Array error, Array min, Array max) {
		PmmXmlDoc paramDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] va = (value == null) ? null : (Object[])value.getArray();
				Object[] er = (error == null) ? null : (Object[])error.getArray();
				Object[] mi = (Object[])min.getArray();
				Object[] ma = (Object[])max.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double vad = (va == null || va[i] == null) ? Double.NaN : Double.parseDouble(va[i].toString());
						Double erd = (er == null || er[i] == null) ? Double.NaN : Double.parseDouble(er[i].toString());
						Double mid = (mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						ParamXml px = new ParamXml(nas,vad,erd,mid,mad,null,null);
						paramDoc.add(px);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return paramDoc;
    }
    public static PmmXmlDoc convertArrays2IndepXmlDoc(Array name, Array min, Array max) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] mi = (min == null) ? null : (Object[])min.getArray();
				Object[] ma = (max == null) ? null : (Object[])max.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double mid = (mi == null || mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma == null || ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						IndepXml ix = new IndepXml(nas,mid,mad);
						indepDoc.add(ix);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return indepDoc;
    }
    
    private static String convertO(Object o) {
		if (o == null) {
			return "?";
		} else {
			return o.toString();
		}
    }

}
