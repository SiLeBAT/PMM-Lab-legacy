package de.bund.bfr.knime.pmm.common;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedHashMap;

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
    public static PmmXmlDoc convertStringLists2TSXmlDoc(String t, String l) {
		PmmXmlDoc tsDoc = new PmmXmlDoc();
		if (t != null && l != null && !t.isEmpty() && !l.isEmpty()) {
			String[] toksT = t.split(",");
			String[] toksL = l.split(",");
			if (toksT.length > 0) {
				int i=0;
				for (String time : toksT) {
					try {
						TimeSeriesXml tsx = new TimeSeriesXml("t"+i,
								time.equals("?") ? null : Double.parseDouble(time),
										toksL[i].equals("?") ? null : Double.parseDouble(toksL[i]));
						tsDoc.add(tsx);
					}
					catch (Exception e) {
					}
					i++;
				}
			}
		}
		return tsDoc;    	
    }
    public static PmmXmlDoc convertArrays2ParamXmlDoc(LinkedHashMap<String, String> varMap, Array name, Array value, Array error, Array min, Array max) {
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
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
						ParamXml px = new ParamXml(onas,vad,erd,mid,mad,null,null);
						px.setName(nas);
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
    public static PmmXmlDoc convertArrays2IndepXmlDoc(LinkedHashMap<String, String> varMap, Array name, Array min, Array max) {
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
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
						IndepXml ix = new IndepXml(onas,mid,mad);
						ix.setName(nas);
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

    public static LinkedHashMap<String, String> getVarParMap(String varparStr) {
    	LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>();
		String[] t1 = varparStr.split(",");

		for (String map : t1) {
			String[] t2 = map.split("=");
			if (t2.length == 2) ret.put(t2[0], t2[1]);
		}

		return ret;    	
    }
}
