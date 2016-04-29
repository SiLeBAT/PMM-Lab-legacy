package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.knime.core.node.NodeSettings;

//<?xml version="1.0" encoding="UTF-8"?>
//<PmmDoc>
//    <param P="2.220446049250313E-16" category="" description="specific growth rate related to ln() transformed data - min/max selected to improve fitting" error="9.922557266695599E-4" isStart="false" max="5.0" maxGuess="" min="0.0" minGuess="" name="mu_max" origname="mu_max" t="34.39444922664667" unit="" value="0.03412808921078558">
//        <correlation origname="mu_max" value="9.845714271085363E-7">
//        </correlation>
//        <correlation origname="Ymax" value="-6.137961639952814E-5">
//        </correlation>
//        <correlation origname="Y0" value="7.843710581862156E-5">
//        </correlation>
//        <correlation origname="h0" value="3.2387019240707804E-4">
//        </correlation>
//    </param>
//    <param P="0.006868797484142597" category="" description="physiological state of the microorganism - product of maximum specific growth rate and the lag" error="0.4708716463769545" isStart="false" max="" maxGuess="" min="" minGuess="" name="h0" origname="h0" t="3.1007696498455273" unit="" value="1.4600645100584562">
//        <correlation origname="mu_max" value="3.2387019240707874E-4">
//        </correlation>
//        <correlation origname="Ymax" value="-0.014839269963920113">
//        </correlation>
//        <correlation origname="Y0" value="0.1240873232218696">
//        </correlation>
//        <correlation origname="h0" value="0.2217201073617437">
//        </correlation>
//    </param>
//    <param P="1.964872708981602E-12" category="Number Content (count/mass)" description="initial bacterial population at time t -ln() transformed; min/max selected to improve fitting" error="0.31463779837402434" isStart="false" max="15.0" maxGuess="" min="-5.0" minGuess="" name="Y0" origname="Y0" t="19.08656033645524" unit="ln(count/g)" value="6.005353322795253">
//        <correlation origname="mu_max" value="7.843710581862224E-5">
//        </correlation>
//        <correlation origname="Ymax" value="-0.0031365063220899683">
//        </correlation>
//        <correlation origname="Y0" value="0.09899694416565318">
//        </correlation>
//        <correlation origname="h0" value="0.12408732322186981">
//        </correlation>
//    </param>
//    <param P="0.0" category="Number Content (count/mass)" description="maximal bacterial population at time t -ln() transformed; min/max selected to improve fitting" error="0.19103395503698417" isStart="false" max="25.0" maxGuess="" min="5.0" minGuess="" name="Ymax" origname="Ymax" t="103.60356286681522" unit="ln(count/g)" value="19.791798370370543">
//        <correlation origname="mu_max" value="-6.137961639952812E-5">
//        </correlation>
//        <correlation origname="Ymax" value="0.03649397197707249">
//        </correlation>
//        <correlation origname="Y0" value="-0.003136506322089929">
//        </correlation>
//        <correlation origname="h0" value="-0.01483926996392007">
//        </correlation>
//    </param>
//</PmmDoc>

public class DepTest {

	static String name = "Value";
	static String origname = "Value";
	static double min = 0.0;
	static double max = 10.0;
	static String category = "Number Content (count/mass)";
	static String unit = "ln(count/g)";
	static String description = "bacterial population at time t -ln() transformed";

	@Test
	public void testName() {
		Dep dep = new Dep();
		assertNull(dep.getName());

		dep.setName(name);
		assertEquals(name, dep.getName());
	}

	@Test
	public void testOrigname() {
		Dep dep = new Dep();
		assertNull(dep.getOrigname());

		dep.setOrigname(origname);
		assertEquals(origname, dep.getOrigname());
	}

	@Test
	public void testMin() {
		Dep dep = new Dep();
		assertNull(dep.getMin());

		dep.setMin(min);
		assertTrue(Double.compare(min, dep.getMin()) == 0);
	}

	@Test
	public void testMax() {
		Dep dep = new Dep();
		assertNull(dep.getMax());

		dep.setMax(max);
		assertTrue(Double.compare(max, dep.getMax()) == 0);
	}

	@Test
	public void testCategory() {
		Dep dep = new Dep();
		assertNull(dep.getCategory());
		
		dep.setCategory(category);
		assertEquals(category, dep.getCategory());
	}

	@Test
	public void testUnit() {
		Dep dep = new Dep();
		assertNull(dep.getUnit());
		
		dep.setUnit(unit);
		assertEquals(unit, dep.getUnit());
	}

	@Test
	public void testDescription() {
		Dep dep = new Dep();
		assertNull(dep.getDescription());
		
		dep.setDescription(description);
		assertEquals(description, dep.getDescription());
	}

	@Test
	public void testSaveToNodeSettings() throws Exception {
		Dep dep = new Dep();
		dep.setName(name);
		dep.setOrigname(origname);
		dep.setMin(min);
		dep.setMax(max);
		dep.setCategory(category);
		dep.setUnit(unit);
		dep.setDescription(description);
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		dep.saveToNodeSettings(settings);
		
		assertEquals(name, settings.getString(Dep.NAME));
		assertEquals(origname, settings.getString(Dep.ORIGNAME));
		assertTrue(Double.compare(min, settings.getDouble(Dep.MIN)) == 0);
		assertTrue(Double.compare(max, settings.getDouble(Dep.MAX)) == 0);
		assertEquals(category, settings.getString(Dep.CATEGORY));
		assertEquals(unit, settings.getString(Dep.UNIT));
		assertEquals(description, settings.getString(Dep.DESCRIPTION));
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addString(Dep.NAME, name);
		settings.addString(Dep.ORIGNAME, origname);
		settings.addDouble(Dep.MIN, min);
		settings.addDouble(Dep.MAX, max);
		settings.addString(Dep.CATEGORY, category);
		settings.addString(Dep.UNIT, unit);
		settings.addString(Dep.DESCRIPTION, description);
		
		Dep dep = new Dep();
		dep.loadFromNodeSettings(settings);
		
		assertEquals(name, dep.getName());
		assertEquals(origname, dep.getOrigname());
		assertTrue(Double.compare(min, dep.getMin()) == 0);
		assertTrue(Double.compare(max, dep.getMax()) == 0);
		assertEquals(category, dep.getCategory());
		assertEquals(unit, dep.getUnit());
		assertEquals(description, dep.getDescription());
	}
}
