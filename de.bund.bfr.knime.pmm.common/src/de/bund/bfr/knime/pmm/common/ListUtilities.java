package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

public class ListUtilities {

	private ListUtilities() {
	}

	public static List<String> getStringListFromString(String s) {
		List<String> list = new ArrayList<String>();

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

	public static List<Double> getDoubleListFromString(String s) {
		List<Double> list = new ArrayList<Double>();

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

	public static String getStringFromList(List<?> list) {
		StringBuilder s = new StringBuilder();

		for (Object o : list) {
			if (o == null) {
				s.append("?;");
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
