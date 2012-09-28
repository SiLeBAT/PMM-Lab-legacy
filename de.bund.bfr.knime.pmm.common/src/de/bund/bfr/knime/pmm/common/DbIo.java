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
    
    private static String convertO(Object o) {
		if (o == null) {
			return "?";
		} else {
			return o.toString();
		}
    }

}
