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
				Object[] va = (Object[])value.getArray();
				Object[] er = (Object[])error.getArray();
				Object[] mi = (Object[])min.getArray();
				Object[] ma = (Object[])max.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double vad = va[i] == null ? Double.NaN : Double.parseDouble(va[i].toString());
						Double erd = er[i] == null ? Double.NaN : Double.parseDouble(er[i].toString());
						Double mid = mi[i] == null ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = ma[i] == null ? Double.NaN : Double.parseDouble(ma[i].toString());
						ParamXml px = new ParamXml(null,nas,vad,erd,mid,mad);
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
    
    private static String convertO(Object o) {
		if (o == null) {
			return "?";
		} else {
			return o.toString();
		}
    }

}
