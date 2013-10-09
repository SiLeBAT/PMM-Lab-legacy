package de.bund.bfr.knime.pmm.common;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.hsh.bfr.db.DBKernel;

public class DbIo {

	public static String stripNonValidXMLCharacters(String in) {
	      StringBuffer out = new StringBuffer(); // Used to hold the output.
	      char current; // Used to reference the current character.

	      if (in == null || ("".equals(in))) return ""; // vacancy test.
	      for (int i = 0; i < in.length(); i++) {
	          current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
	          if ((current == 0x9) ||
	              (current == 0xA) ||
	              (current == 0xD) ||
	              ((current >= 0x20) && (current <= 0xD7FF)) ||
	              ((current >= 0xE000) && (current <= 0xFFFD)) ||
	              ((current >= 0x10000) && (current <= 0x10FFFF)))
	              out.append(current);
	      }
	      return out.toString();
	} 
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
    public static PmmXmlDoc convertStringLists2TSXmlDoc(Array t, Array tu, Array l, Array lu, Array lot, Array stddevs, Array wdhs) {
		PmmXmlDoc tsDoc = new PmmXmlDoc();
		if (t != null) {
			try {
				Object[] toksT = (Object[])t.getArray();
				Object[] toksTu = (tu == null) ? null : (Object[])tu.getArray();
				Object[] toksL = (l == null) ? null : (Object[])l.getArray();
				Object[] toksLu = (lu == null) ? null : (Object[])lu.getArray();
				Object[] toksLot = (lot == null) ? null : (Object[])lot.getArray();
				Object[] toksSd = (stddevs == null) ? null : (Object[])stddevs.getArray();
				Object[] toksWdh = (wdhs == null) ? null : (Object[])wdhs.getArray();
				if (toksT.length > 0) {
					int i=0;
					for (Object time : toksT) {
						try {
							TimeSeriesXml tsx = new TimeSeriesXml("t"+i,
									time == null ? null : Double.parseDouble(time.toString()),
											toksTu == null || toksTu[i] == null ? null : toksTu[i].toString(),
											toksL == null || toksL[i] == null ? null : Double.parseDouble(toksL[i].toString()),
											toksLu == null || toksLu[i] == null ? null : toksLu[i].toString(),
											toksSd == null || toksSd[i] == null ? null : Double.parseDouble(toksSd[i].toString()),
											toksWdh == null || toksWdh[i] == null ? null : (int) Double.parseDouble(toksWdh[i].toString()));
							if (toksLot != null && toksLot[i] != null) tsx.setConcentrationUnitObjectType(toksLot[i].toString());
							tsDoc.add(tsx);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					}
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tsDoc;    	
    }
    public static PmmXmlDoc convertArrays2ParamXmlDoc(LinkedHashMap<String, String> varMap, Array name,
    		Array value, Array timeUnit, Array categories, Array units, Array error, Array min, Array max, Array desc) {
		PmmXmlDoc paramDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] va = (value == null) ? null : (Object[])value.getArray();
				//Object[] tu = (timeUnit == null) ? null : (Object[])timeUnit.getArray();
				Object[] cu = (units == null) ? null : (Object[])units.getArray();
				Object[] cc = (categories == null) ? (cu == null ? null : new Object[cu.length]) : (Object[])categories.getArray();
				Object[] er = (error == null) ? null : (Object[])error.getArray();
				Object[] mi = (Object[])min.getArray();
				Object[] ma = (Object[])max.getArray();
				Object[] cd = (Object[])desc.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double vad = (va == null || va[i] == null) ? Double.NaN : Double.parseDouble(va[i].toString());
						/*
						if (!Double.isNaN(vad)) {
							if (tu != null && tu[i] != null) {
								if (tu[i].toString().equalsIgnoreCase("Sekunde")) vad = vad / 3600;
								else if (tu[i].toString().equalsIgnoreCase("Minute")) vad = vad / 60;
								else if (tu[i].toString().equalsIgnoreCase("Stunde")) ;
								else if (tu[i].toString().equalsIgnoreCase("Tag")) vad = vad * 24;
								else if (tu[i].toString().equalsIgnoreCase("Woche")) vad = vad * 24 * 7;
								else if (tu[i].toString().equalsIgnoreCase("Monat")) vad = vad * 24 * 30;
								else if (tu[i].toString().equalsIgnoreCase("Jahr")) vad = vad * 24 * 365;
								else System.err.println("convertArrays2ParamXmlDoc - Unconsidered Time Unit used... Please Check!!!! ->" + tu[i]);
							}
							if (cu != null && cu[i] != null) {
								System.err.println("convertArrays2ParamXmlDoc - Unconsidered concentration Unit used... Please Check!!!! ->" + cu[i]);
							}
						}
						*/
						Double erd = (er == null || er[i] == null) ? Double.NaN : Double.parseDouble(er[i].toString());
						Double mid = (mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
			    		if (cc != null && cc[i] == null && cu[i] != null) {
			    			cc[i] = DBKernel.getValue("Einheiten", "display in GUI as", (String) cu[i], "kind of property / quantity");
			    		}
						ParamXml px = new ParamXml(onas,vad,erd,mid,mad,null,null,cc==null?null:(String) cc[i],cu==null?null:(String) cu[i]);
						px.setName(nas);
						if (cd != null && cd[i] != null) px.setDescription(stripNonValidXMLCharacters(cd[i].toString()));
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
    public static PmmXmlDoc convertArrays2IndepXmlDoc(LinkedHashMap<String, String> varMap, Array name, Array min, Array max, Array categories, Array units, Array desc) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] mi = (min == null) ? null : (Object[])min.getArray();
				Object[] ma = (max == null) ? null : (Object[])max.getArray();
				Object[] cc = (categories == null) ? null : (Object[])categories.getArray();
				Object[] cu = (units == null) ? null : (Object[])units.getArray();
				Object[] cd = (desc == null) ? null : (Object[])desc.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double mid = (mi == null || mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma == null || ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
						IndepXml ix = new IndepXml(onas,mid,mad,cc==null?null:(String) cc[i],cu==null?null:(String) cu[i]);
						ix.setName(nas);
						if (cd != null && cd[i] != null) ix.setDescription(stripNonValidXMLCharacters(cd[i].toString()));
						indepDoc.add(ix);
						if (ix.getUnit() == null || ix.getUnit().isEmpty()) indepDoc.addWarning("\nUnit not defined for independant variable '" + ix.getName() + "'\n");
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return indepDoc;
    }
    public static PmmXmlDoc convertArrays2MiscXmlDoc(Array id, Array param, Array desc, Array value, Array unit) {
		PmmXmlDoc miscDoc = new PmmXmlDoc();
	    if (id != null) {
		    try {
				Object[] sid = (Object[])id.getArray();
				Object[] spa = (param == null) ? null : (Object[])param.getArray();
				Object[] sde = (desc == null) ? null : (Object[])desc.getArray();
				Object[] sv = (value == null) ? null : (Object[])value.getArray();
				Object[] su = (unit == null) ? null : (Object[])unit.getArray();
				if (sid != null && sid.length > 0) {
					for (int i=0;i<sid.length;i++) {
						Integer sidi = (sid[i] == null ? null : Integer.parseInt(sid[i].toString()));
						String spas = (spa == null || spa[i] == null ? null : spa[i].toString());
						String sdes = (sde == null || sde[i] == null ? null : sde[i].toString());
						Double svd = (sv == null || sv[i] == null) ? Double.NaN : Double.parseDouble(sv[i].toString());
						String sus = (su == null || su[i] == null ? null : su[i].toString());
			    		MiscXml mx = new MiscXml(sidi,spas,sdes,svd,null,sus);
						miscDoc.add(mx);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return miscDoc;
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
    	if (varparStr != null) {
    		String[] t1 = varparStr.split(",");

    		for (String map : t1) {
    			String[] t2 = map.split("=");
    			if (t2.length == 2) ret.put(t2[0], t2[1]);
    		}
    	}
		return ret;    	
    }
}
