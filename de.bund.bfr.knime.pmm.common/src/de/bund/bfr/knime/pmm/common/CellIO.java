/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class CellIO {

	
	public static String getString(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		String s = ((StringCell) cell).getStringValue();

		if (s.trim().isEmpty()) {
			return null;
		}

		return s;
	}

	public static Integer getInt(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		return ((IntCell) cell).getIntValue();
	}

	public static Double getDouble(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		return ((DoubleCell) cell).getDoubleValue();
	}

	public static List<String> getStringList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<String>();
		}

		String[] toks = ((StringCell) cell).getStringValue().split(",");

		return new ArrayList<String>(Arrays.asList(toks));
	}

	public static List<Double> getDoubleList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<Double>();
		}

		List<Double> list = new ArrayList<Double>();
		String[] toks = ((StringCell) cell).getStringValue().split(",");

		for (String t : toks) {
			if (t.equals("?")) {
				list.add(null);
			} else {
				try {
					list.add(Double.parseDouble(t));
				} catch (NumberFormatException e) {
					return new ArrayList<Double>();
				}
			}
		}

		return list;
	}

	public static List<Integer> getIntList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<Integer>();
		}

		List<Integer> list = new ArrayList<Integer>();
		String[] toks = ((StringCell) cell).getStringValue().split(",");

		for (String t : toks) {
			if (t.equals("?")) {
				list.add(null);
			} else {
				try {
					list.add(Integer.parseInt(t));
				} catch (NumberFormatException e) {
					return new ArrayList<Integer>();
				}
			}
		}

		return list;
	}

	public static DataCell createCell(String s) {
		if (s == null || s.isEmpty()) {
			return DataType.getMissingCell();
		}

		return new StringCell(s);
	}

	public static DataCell createCell(Integer i) {
		if (i == null) {
			return DataType.getMissingCell();
		}

		return new IntCell(i);
	}

	public static DataCell createCell(Double d) {
		if (d == null || d.isNaN() || d.isInfinite()) {
			return DataType.getMissingCell();
		}

		return new DoubleCell(d);
	}

	public static DataCell createCell(List<?> list) {
		if (list == null || list.isEmpty()) {
			return DataType.getMissingCell();
		}

		String s = "";

		for (Object o : list) {
			if (o == null) {
				s += "?,";
			} else if (o instanceof Double) {
				Double d = (Double) o;

				if (d.isNaN() || d.isInfinite()) {
					s += "?,";
				} else {
					s += d + ",";
				}
			} else {
				s += o + ",";
			}
		}

		return new StringCell(s.substring(0, s.length() - 1));
	}
	
	public static DataCell createCell( Map<?,?> map ) throws PmmException {
		
		String ret, key, value;
		Object prevalue;
		
		ret = "";
		for( Object prekey : map.keySet() ) {
			
			if( prekey == null )
				continue;
			
			prevalue = map.get( prekey );
			if( prevalue == null )
				continue;
			
			key = prekey.toString();
			if( key == null )
				continue;
			
			value = prevalue.toString();
			if( value == null )
				continue;
			
			if( key.contains( "=" ) )
				throw new PmmException( "No '=' symbol allowed for in key '"
					+key+"'" );
			
			if( key.contains( "," ) )
				throw new PmmException( "No ',' symbol allowed for in key '"
					+key+"'" );
			
			if( key.contains( "=" ) )
				throw new PmmException( "No '=' symbol allowed for in value '"
					+value+"'" );
			
			if( key.contains( "," ) )
				throw new PmmException( "No ',' symbol allowed for in value '"
					+value+"'" );
			
			if( !ret.isEmpty() )
				ret += ",";
			
			ret += key+"="+value;	
		}
		
		return new StringCell( ret );
	}

	public static DataCell createMissingCell() {
		return DataType.getMissingCell();
	}

	public static DataCell createDoubleCell(String d) {
		if (d == null) {
			return createMissingCell();
		}

		try {
			return createCell(Double.parseDouble(d));
		} catch (NumberFormatException e) {
			return createMissingCell();
		}
	}

	public static DataCell createIntCell(String d) {
		if (d == null) {
			return createMissingCell();
		}

		try {
			return createCell(Integer.parseInt(d));
		} catch (NumberFormatException e) {
			return createMissingCell();
		}
	}

	public static Map<String, String> getMap( DataCell dataCell )
	throws PmmException {

		String[] t1, t2;
		HashMap<String,String> ret;

		if( !( dataCell instanceof StringCell ) )
			throw new PmmException( "Only String cell can return map." );
		
		t1 = ( ( StringCell)dataCell ).getStringValue().split( "," );
		
		ret = new HashMap<String,String>();
		
		for( String map : t1 ) {
			
			t2 = map.split( "=" );
			if( t2.length != 2 )
				throw new PmmException( "Map string contains malformed item." );
			
			ret.put( t2[ 0 ], t2[ 1 ] );
		}
		
		return ret;
	}

}
