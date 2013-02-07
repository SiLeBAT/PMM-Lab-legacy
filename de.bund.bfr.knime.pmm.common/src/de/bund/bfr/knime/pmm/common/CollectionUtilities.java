package de.bund.bfr.knime.pmm.common;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtilities {

	public static final String LIST_DIVIDER = ";";
	public static final String MAP_DIVIDER = "=";
	public static final String POINT_DIVIDER = "/";
	public static final String MISSING_VALUE = "?";

	private CollectionUtilities() {
	}

	public static List<String> getStringListFromString(String s) {
		List<String> list = new ArrayList<>();

		if (!s.isEmpty()) {
			for (String tok : s.split(LIST_DIVIDER)) {
				if (tok.equals(MISSING_VALUE)) {
					list.add(null);
				} else {
					list.add(tok);
				}
			}
		}

		return list;
	}

	public static List<Integer> getIntListFromString(String s) {
		List<Integer> list = new ArrayList<>();

		if (!s.isEmpty()) {
			for (String tok : s.split(LIST_DIVIDER)) {
				try {
					list.add(Integer.parseInt(tok));
				} catch (NumberFormatException e) {
					list.add(null);
				}
			}
		}

		return list;
	}

	public static List<Double> getDoubleListFromString(String s) {
		List<Double> list = new ArrayList<>();

		if (!s.isEmpty()) {
			for (String tok : s.split(LIST_DIVIDER)) {
				try {
					list.add(Double.parseDouble(tok));
				} catch (NumberFormatException e) {
					list.add(null);
				}
			}
		}

		return list;
	}

	public static List<Point2D.Double> getPointDoubleListFromString(String s) {
		List<Point2D.Double> list = new ArrayList<>();

		if (!s.isEmpty()) {
			for (String tok : s.split(LIST_DIVIDER)) {
				String[] toks = tok.split(POINT_DIVIDER);
				double x = Double.NaN;
				double y = Double.NaN;

				try {
					x = Double.parseDouble(toks[0]);
				} catch (Exception e) {
				}

				try {
					y = Double.parseDouble(toks[1]);
				} catch (Exception e) {
				}

				list.add(new Point2D.Double(x, y));
			}
		}

		return list;
	}

	public static String getStringFromList(List<?> list) {
		StringBuilder s = new StringBuilder();

		for (Object o : list) {
			if (o == null) {
				s.append(MISSING_VALUE + LIST_DIVIDER);
			} else if (o instanceof Point2D.Double) {
				Point2D.Double p = (Point2D.Double) o;
				String x = MISSING_VALUE;
				String y = MISSING_VALUE;

				if (!Double.isNaN(p.x) && !Double.isInfinite(p.x)) {
					x = p.x + "";
				}

				if (!Double.isNaN(p.y) && !Double.isInfinite(p.y)) {
					y = p.y + "";
				}

				s.append(x + POINT_DIVIDER + y + LIST_DIVIDER);
			} else {
				s.append(o + LIST_DIVIDER);
			}
		}

		if (s.length() > 0) {
			s.deleteCharAt(s.length() - 1);
		}

		return s.toString();
	}

	public static Map<String, String> getStringMapFromString(String s) {
		Map<String, String> map = new LinkedHashMap<>();

		for (String mapping : getStringListFromString(s)) {
			String[] toks = mapping.split(MAP_DIVIDER);

			try {
				map.put(toks[0], toks[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		return map;
	}

	public static String getStringFromMap(Map<?, ?> map) {
		List<String> list = new ArrayList<>();

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String key = MISSING_VALUE;
			String value = MISSING_VALUE;

			if (entry.getKey() != null) {
				key = entry.getKey() + "";
			}

			if (entry.getValue() != null) {
				value = entry.getValue() + "";
			}

			list.add(key + MAP_DIVIDER + value);
		}

		return getStringFromList(list);
	}

}
