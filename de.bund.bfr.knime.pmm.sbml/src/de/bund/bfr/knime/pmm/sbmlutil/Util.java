package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitary methods.
 * 
 * @author Miguel Alba
 */
public class Util {
	
	private Util() {
	}

	// Dictionary that maps model classes to integers
	public static final Map<String, Integer> MODELCLASS_NUMS;

	static {
		Map<String, Integer> tempMap = new HashMap<>();
		tempMap.put("unknown", 0);
		tempMap.put("growth", 1);
		tempMap.put("inactivation", 2);
		tempMap.put("survival", 3);
		tempMap.put("growth/inactivation", 4);
		tempMap.put("inactivation/survival", 5);
		tempMap.put("growth/survival", 6);
		tempMap.put("growth/inactivation/survival", 7);
		tempMap.put("T", 8);
		tempMap.put("pH", 9);
		tempMap.put("aw", 10);
		tempMap.put("T/pH", 11);
		tempMap.put("T/aw", 12);
		tempMap.put("pH/aw", 13);
		tempMap.put("T/pH/aw", 14);

		MODELCLASS_NUMS = Collections.unmodifiableMap(tempMap);
	}

	// Dictionary that maps integers to model classes
	public static final Map<Integer, String> MODELCLASS_STRS;

	static {
		Map<Integer, String> tempMap = new HashMap<>();
		tempMap.put(0, "unknown");
		tempMap.put(1, "growth");
		tempMap.put(2, "inactivation");
		tempMap.put(3, "survival");
		tempMap.put(4, "growth/inactivation");
		tempMap.put(5, "inactivation/survival");
		tempMap.put(6, "growth/survival");
		tempMap.put(7, "growth/inactivation/survival");
		tempMap.put(8, "T");
		tempMap.put(9, "pH");
		tempMap.put(10, "aw");
		tempMap.put(11, "T/pH");
		tempMap.put(12, "T/aw");
		tempMap.put(13, "pH/aw");
		tempMap.put(14, "T/pH/aw");

		MODELCLASS_STRS = Collections.unmodifiableMap(tempMap);
	}

	public static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
}