package de.bund.bfr.knime.pmm.common;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;

public class XmlConverter {

	private XmlConverter() {
	}

	public static String listToXml(List<?> list) {
		return new XStream().toXML(list);
	}

	public static String mapToXml(Map<?, ?> map) {
		return new XStream().toXML(map);
	}

	public static String colorMapToXml(Map<String, Color> map) {
		return new XStream().toXML(colorMapToStringMap(map));
	}

	public static String shapeMapToXml(Map<String, Shape> map) {
		return new XStream().toXML(shapeMapToStringMap(map));
	}

	@SuppressWarnings("unchecked")
	public static List<String> xmlToStringList(String xml) {
		try {
			return (List<String>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Integer> xmlToIntList(String xml) {
		try {
			return (List<Integer>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Double> xmlToDoubleList(String xml) {
		try {
			return (List<Double>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Point2D.Double> xmlToPointDoubleList(String xml) {
		try {
			return (List<Point2D.Double>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToStringMap(String xml) {
		try {
			return (Map<String, String>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Color> xmlToColorMap(String xml) {
		try {
			return stringMapToColorMap((Map<String, String>) new XStream()
					.fromXML(xml));
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Shape> xmlToShapeMap(String xml) {
		try {
			return stringMapToShapeMap((Map<String, String>) new XStream()
					.fromXML(xml));
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<Integer, Double> xmlToIntDoubleMap(String xml) {
		try {
			return (Map<Integer, Double>) new XStream().fromXML(xml);
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, String>> xmlToStringMapMap(String xml) {
		try {
			return (Map<String, Map<String, String>>) new XStream()
					.fromXML(xml);
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Point2D.Double>> xmlToPointDoubleMapMap(
			String xml) {
		try {
			return (Map<String, Map<String, Point2D.Double>>) new XStream()
					.fromXML(xml);
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, List<Map<String, String>>> xmlToStringMapListMap(
			String xml) {
		try {
			return (Map<String, List<Map<String, String>>>) new XStream()
					.fromXML(xml);
		} catch (Exception e) {
			return new LinkedHashMap<>();
		}
	}

	private static Map<String, String> colorMapToStringMap(
			Map<String, Color> map) {
		Map<String, String> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, Color> entry : map.entrySet()) {
			String colorString = "#"
					+ Integer.toHexString(entry.getValue().getRGB()).substring(
							2);

			newMap.put(entry.getKey(), colorString);
		}

		return newMap;
	}

	private static Map<String, Color> stringMapToColorMap(
			Map<String, String> map) {
		Map<String, Color> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			newMap.put(entry.getKey(), Color.decode(entry.getValue()));
		}

		return newMap;
	}

	private static Map<String, String> shapeMapToStringMap(
			Map<String, Shape> map) {
		Map<String, String> newMap = new LinkedHashMap<>();
		Map<Shape, String> shapeMap = (new ColorAndShapeCreator(0))
				.getNameByShapeMap();

		for (Map.Entry<String, Shape> entry : map.entrySet()) {
			newMap.put(entry.getKey(), shapeMap.get(entry.getValue()));
		}

		return newMap;
	}

	private static Map<String, Shape> stringMapToShapeMap(
			Map<String, String> map) {
		Map<String, Shape> newMap = new LinkedHashMap<>();
		Map<String, Shape> shapeMap = (new ColorAndShapeCreator(0))
				.getShapeByNameMap();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			newMap.put(entry.getKey(), shapeMap.get(entry.getValue()));
		}

		return newMap;
	}

}
