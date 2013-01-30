package de.bund.bfr.knime.pmm.common;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ListUtilities {

	private ListUtilities() {
	}

	public static List<String> getStringListFromString(String s) {
		List<String> list = new ArrayList<>();

		if (!s.isEmpty()) {
			for (String tok : s.split(";")) {
				if (tok.equals("?")) {
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
			for (String tok : s.split(";")) {
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
			for (String tok : s.split(";")) {
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
			for (String tok : s.split(";")) {
				String[] toks = tok.split("/");
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
				s.append("?;");
			} else if (o instanceof Point2D.Double) {
				Point2D.Double p = (Point2D.Double) o;

				s.append(p.x + "/" + p.y + ";");
			} else {
				s.append(o + ";");
			}
		}

		if (s.length() > 0) {
			s.deleteCharAt(s.length() - 1);
		}

		return s.toString();
	}
}
