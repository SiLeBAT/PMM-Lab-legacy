package de.bund.bfr.knime.pmm.common;

public class CollectionUtilities {

	// public static final String LIST_DIVIDER = ";";
	// public static final String LISTLIST_DIVIDER = ",";
	// public static final String MAP_DIVIDER = ":";
	// public static final String MAPMAP_DIVIDER = "=";
	// public static final String POINT_DIVIDER = "/";
	// public static final String MISSING_VALUE = "?";
	//
	// private CollectionUtilities() {
	// }
	//
	// public static List<String> getStringListFromString(String s) {
	// List<String> list = new ArrayList<>();
	//
	// if (s != null && !s.isEmpty()) {
	// for (String tok : s.split(LIST_DIVIDER)) {
	// if (tok.equals(MISSING_VALUE)) {
	// list.add(null);
	// } else {
	// list.add(tok);
	// }
	// }
	// }
	//
	// return list;
	// }
	//
	// public static List<Integer> getIntListFromString(String s) {
	// List<Integer> list = new ArrayList<>();
	//
	// if (s != null && !s.isEmpty()) {
	// for (String tok : s.split(LIST_DIVIDER)) {
	// try {
	// list.add(Integer.parseInt(tok));
	// } catch (NumberFormatException e) {
	// list.add(null);
	// }
	// }
	// }
	//
	// return list;
	// }
	//
	// public static List<Double> getDoubleListFromString(String s) {
	// List<Double> list = new ArrayList<>();
	//
	// if (s != null && !s.isEmpty()) {
	// for (String tok : s.split(LIST_DIVIDER)) {
	// try {
	// list.add(Double.parseDouble(tok));
	// } catch (NumberFormatException e) {
	// list.add(null);
	// }
	// }
	// }
	//
	// return list;
	// }
	//
	// public static List<Point2D.Double> getPointDoubleListFromString(String s)
	// {
	// List<Point2D.Double> list = new ArrayList<>();
	//
	// if (s != null && !s.isEmpty()) {
	// for (String tok : s.split(LIST_DIVIDER)) {
	// String[] toks = tok.split(POINT_DIVIDER);
	// double x = Double.NaN;
	// double y = Double.NaN;
	//
	// try {
	// x = Double.parseDouble(toks[0]);
	// } catch (Exception e) {
	// }
	//
	// try {
	// y = Double.parseDouble(toks[1]);
	// } catch (Exception e) {
	// }
	//
	// list.add(new Point2D.Double(x, y));
	// }
	// }
	//
	// return list;
	// }
	//
	// public static String getStringFromList(List<?> list) {
	// StringBuilder s = new StringBuilder();
	//
	// for (Object o : list) {
	// if (o == null) {
	// s.append(MISSING_VALUE + LIST_DIVIDER);
	// } else if (o instanceof Point2D.Double) {
	// Point2D.Double p = (Point2D.Double) o;
	// String x = MISSING_VALUE;
	// String y = MISSING_VALUE;
	//
	// if (!Double.isNaN(p.x) && !Double.isInfinite(p.x)) {
	// x = p.x + "";
	// }
	//
	// if (!Double.isNaN(p.y) && !Double.isInfinite(p.y)) {
	// y = p.y + "";
	// }
	//
	// s.append(x + POINT_DIVIDER + y + LIST_DIVIDER);
	// } else {
	// s.append(o + LIST_DIVIDER);
	// }
	// }
	//
	// if (s.length() > 0) {
	// s.deleteCharAt(s.length() - 1);
	// }
	//
	// return s.toString();
	// }
	//
	// public static Map<String, String> getStringMapFromString(String s) {
	// Map<String, String> map = new LinkedHashMap<>();
	//
	// for (String mapping : getStringListFromString(s)) {
	// String[] toks = mapping.split(MAP_DIVIDER);
	//
	// try {
	// map.put(toks[0], toks[1]);
	// } catch (ArrayIndexOutOfBoundsException e) {
	// }
	// }
	//
	// return map;
	// }
	//
	// public static Map<Integer, Double> getIntDoubleMapFromString(String s) {
	// Map<Integer, Double> map = new LinkedHashMap<>();
	//
	// for (String mapping : getStringListFromString(s)) {
	// String[] toks = mapping.split(MAP_DIVIDER);
	//
	// try {
	// map.put(Integer.parseInt(toks[0]), Double.parseDouble(toks[1]));
	// } catch (Exception e) {
	// }
	// }
	//
	// return map;
	// }
	//
	// public static Map<String, Color> getColorMapFromString(String s) {
	// Map<String, Color> map = new LinkedHashMap<>();
	//
	// for (String mapping : getStringListFromString(s)) {
	// String[] toks = mapping.split(MAP_DIVIDER);
	//
	// try {
	// map.put(toks[0], Color.decode(toks[1]));
	// } catch (ArrayIndexOutOfBoundsException e) {
	// }
	// }
	//
	// return map;
	// }
	//
	// public static Map<String, Shape> getShapeMapFromString(String s) {
	// Map<String, Shape> map = new LinkedHashMap<>();
	// Map<String, Shape> shapeMap = (new ColorAndShapeCreator(0))
	// .getShapeByNameMap();
	//
	// for (String mapping : getStringListFromString(s)) {
	// String[] toks = mapping.split(MAP_DIVIDER);
	//
	// try {
	// map.put(toks[0], shapeMap.get(toks[1]));
	// } catch (ArrayIndexOutOfBoundsException e) {
	// }
	// }
	//
	// return map;
	// }
	//
	// public static String getStringFromMap(Map<?, ?> map) {
	// List<String> list = new ArrayList<>();
	// Map<Shape, String> shapeMap = (new ColorAndShapeCreator(0))
	// .getNameByShapeMap();
	//
	// for (Map.Entry<?, ?> entry : map.entrySet()) {
	// String key = MISSING_VALUE;
	// String value = MISSING_VALUE;
	//
	// if (entry.getKey() != null) {
	// key = entry.getKey() + "";
	// }
	//
	// if (entry.getValue() != null) {
	// Object o = entry.getValue();
	//
	// if (o instanceof Color) {
	// value = "#"
	// + Integer.toHexString(((Color) o).getRGB())
	// .substring(2);
	// } else if (o instanceof Shape) {
	// value = shapeMap.get((Shape) o);
	// } else {
	// value = o + "";
	// }
	// }
	//
	// list.add(key + MAP_DIVIDER + value);
	// }
	//
	// return getStringFromList(list);
	// }
	//
	// public static Map<String, Map<String, Point2D.Double>>
	// getPointMapMapFromString(
	// String s) {
	// Map<String, Map<String, Point2D.Double>> map1 = new LinkedHashMap<>();
	//
	// for (String mapping1 : CollectionUtilities.getStringListFromString(s)) {
	// String[] mapToks1 = mapping1.split(MAP_DIVIDER);
	//
	// if (mapToks1.length != 2) {
	// continue;
	// }
	//
	// String mapKey1 = mapToks1[0];
	// Map<String, Point2D.Double> map2 = new LinkedHashMap<>();
	//
	// for (String mapping2 : mapToks1[1].split(LISTLIST_DIVIDER)) {
	// String[] mapToks2 = mapping2.split(MAPMAP_DIVIDER);
	//
	// if (mapToks2.length != 2) {
	// continue;
	// }
	//
	// String mapKey2 = mapToks2[0];
	// String[] toks = mapToks2[1].split(POINT_DIVIDER);
	// double x = Double.NaN;
	// double y = Double.NaN;
	//
	// try {
	// x = Double.parseDouble(toks[0]);
	// } catch (Exception e) {
	// }
	//
	// try {
	// y = Double.parseDouble(toks[1]);
	// } catch (Exception e) {
	// }
	//
	// map2.put(mapKey2, new Point2D.Double(x, y));
	// }
	//
	// map1.put(mapKey1, map2);
	// }
	//
	// return map1;
	// }
	//
	// public static String getStringFromPointMapMap(
	// Map<String, Map<String, Point2D.Double>> map1) {
	// List<String> list = new ArrayList<String>();
	//
	// for (String mapKey1 : map1.keySet()) {
	// Map<String, Point2D.Double> map2 = map1.get(mapKey1);
	// StringBuilder map2String = new StringBuilder(mapKey1 + MAP_DIVIDER);
	//
	// for (String paramName : map2.keySet()) {
	// Point2D.Double p = map2.get(paramName);
	// String x = MISSING_VALUE;
	// String y = MISSING_VALUE;
	//
	// if (!Double.isNaN(p.x) && !Double.isInfinite(p.x)) {
	// x = p.x + "";
	// }
	//
	// if (!Double.isNaN(p.y) && !Double.isInfinite(p.y)) {
	// y = p.y + "";
	// }
	//
	// map2String.append(paramName + MAPMAP_DIVIDER + x
	// + POINT_DIVIDER + y + LISTLIST_DIVIDER);
	// }
	//
	// if (map2String.toString().endsWith(LISTLIST_DIVIDER)) {
	// map2String.delete(
	// map2String.length() - LISTLIST_DIVIDER.length(),
	// map2String.length());
	// }
	//
	// list.add(map2String.toString());
	// }
	//
	// return CollectionUtilities.getStringFromList(list);
	// }
	//
	// public static String getStringFromMapMap(
	// Map<String, Map<String, String>> map1) {
	// List<String> list = new ArrayList<String>();
	//
	// for (String mapKey1 : map1.keySet()) {
	// Map<String, String> map2 = map1.get(mapKey1);
	// StringBuilder map2String = new StringBuilder(mapKey1 + MAP_DIVIDER);
	//
	// for (String mapKey2 : map2.keySet()) {
	// String s = map2.get(mapKey2);
	//
	// if (s == null) {
	// map2String.append(mapKey2 + MAPMAP_DIVIDER + MISSING_VALUE
	// + LISTLIST_DIVIDER);
	// } else {
	// map2String.append(mapKey2 + MAPMAP_DIVIDER + s
	// + LISTLIST_DIVIDER);
	// }
	// }
	//
	// if (map2String.toString().endsWith(LISTLIST_DIVIDER)) {
	// map2String.delete(
	// map2String.length() - LISTLIST_DIVIDER.length(),
	// map2String.length());
	// }
	//
	// list.add(map2String.toString());
	// }
	//
	// return CollectionUtilities.getStringFromList(list);
	// }
	//
	// public static Map<String, Map<String, String>> getMapMapFromString(String
	// s) {
	// Map<String, Map<String, String>> map1 = new LinkedHashMap<>();
	//
	// for (String mapping1 : CollectionUtilities.getStringListFromString(s)) {
	// String[] mapToks1 = mapping1.split(MAP_DIVIDER);
	//
	// if (mapToks1.length != 2) {
	// continue;
	// }
	//
	// String mapKey1 = mapToks1[0];
	// Map<String, String> map2 = new LinkedHashMap<>();
	//
	// for (String mapping2 : mapToks1[1].split(LISTLIST_DIVIDER)) {
	// String[] mapToks2 = mapping2.split(MAPMAP_DIVIDER);
	//
	// try {
	// map2.put(mapToks2[0], mapToks2[1]);
	// } catch (ArrayIndexOutOfBoundsException e) {
	// }
	// }
	//
	// map1.put(mapKey1, map2);
	// }
	//
	// return map1;
	// }
	//
	// public static String getStringFromMapListMap(
	// Map<String, List<Map<String, String>>> map1) {
	// List<String> list = new ArrayList<String>();
	//
	// for (String key1 : map1.keySet()) {
	// for (Map<String, String> assign : map1.get(key1)) {
	// StringBuilder s = new StringBuilder(key1 + MAP_DIVIDER);
	//
	// for (String key2 : assign.keySet()) {
	// String value2 = assign.get(key2);
	//
	// s.append(key2 + MAPMAP_DIVIDER + value2 + LISTLIST_DIVIDER);
	// }
	//
	// if (s.toString().endsWith(LISTLIST_DIVIDER)) {
	// s.delete(s.length() - LISTLIST_DIVIDER.length(), s.length());
	// }
	//
	// list.add(s.toString());
	// }
	// }
	//
	// return CollectionUtilities.getStringFromList(list);
	// }
	//
	// public static Map<String, List<Map<String, String>>>
	// getMapListMapFromString(
	// String s1) {
	// Map<String, List<Map<String, String>>> map = new LinkedHashMap<>();
	//
	// for (String s : CollectionUtilities.getStringListFromString(s1)) {
	// String[] toks = s.split(":");
	//
	// if (toks.length == 2) {
	// String model = toks[0].trim();
	// Map<String, String> modelReplacements = new LinkedHashMap<String,
	// String>();
	//
	// for (String assignment : toks[1].split(",")) {
	// String[] elements = assignment.split("=");
	//
	// if (elements.length == 2) {
	// String variable = elements[0].trim();
	// String parameter = elements[1].trim();
	//
	// modelReplacements.put(variable, parameter);
	// }
	// }
	//
	// if (!map.containsKey(model)) {
	// map.put(model, new ArrayList<Map<String, String>>());
	// }
	//
	// map.get(model).add(modelReplacements);
	// }
	// }
	//
	// return map;
	// }

}
