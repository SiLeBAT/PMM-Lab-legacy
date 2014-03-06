package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class XmlHelper {

	private XmlHelper() {
	}

	public static String getString(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return el.getAttributeValue(attr);
	}

	public static Integer getInt(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Integer.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double getDouble(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Double.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Boolean getBoolean(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return Boolean.valueOf(el.getAttributeValue(attr));
	}

	public static String getNonNull(Object o) {
		if (o == null) {
			return "";
		}

		return o.toString();
	}

	public static String removeDirt(String toClean) {
		String cleaned = (toClean == null ? "" : toClean);
		cleaned = cleaned.toString().replace("&amp;", "&"); // .replace("\n",
															// " ");
															// //.replaceAll("[^A-Za-zäöüßÄÖÜ0-9+-.,;': ()°%?&=<>/]",
															// "");
		cleaned = cleanInvalidXmlChars(cleaned);
		/*
		 * if (toClean != null && !toClean.equals(cleaned)) {
		 * System.err.println(toClean); System.err.println(cleaned); }
		 */
		return cleaned;
	}

	public static String cleanInvalidXmlChars(String text) {
		String re = "[^^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD]";
		return text.replaceAll(re, " ");
	}
}
