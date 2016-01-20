/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Miguel Alba
 */
public class ModelClassTest {
	
	private ModelClass unknown = ModelClass.UNKNOWN;
	private ModelClass growth = ModelClass.GROWTH;
	private ModelClass inactivation = ModelClass.INACTIVATION;
	private ModelClass survival = ModelClass.SURVIVAL;
	private ModelClass growthInactivation = ModelClass.GROWTH_INACTIVATION;
	private ModelClass inactivationSurvival = ModelClass.INACTIVATION_SURVIVAL;
	private ModelClass growthSurvival = ModelClass.GROWTH_SURVIVAL;
	private ModelClass growthInactivationSurvival = ModelClass.GROWTH_INACTIVATION_SURVIVAL;
	private ModelClass t = ModelClass.T;
	private ModelClass pH = ModelClass.PH;
	private ModelClass aw = ModelClass.AW;
	private ModelClass t_ph = ModelClass.T_PH;
	private ModelClass t_aw = ModelClass.T_AW;
	private ModelClass ph_aw = ModelClass.PH_AW;
	private ModelClass t_ph_aw = ModelClass.T_PH_AW;
	
	@Test
	public void testFromName() {
		assertEquals(unknown, ModelClass.fromName("unknown"));
		assertEquals(growth, ModelClass.fromName("growth"));
		assertEquals(inactivation, ModelClass.fromName("inactivation"));
		assertEquals(survival, ModelClass.fromName("survival"));
		assertEquals(growthInactivation, ModelClass.fromName("growth/inactivation"));
		assertEquals(inactivationSurvival, ModelClass.fromName("inactivation/survival"));
		assertEquals(growthSurvival, ModelClass.fromName("growth/survival"));
		assertEquals(growthInactivationSurvival, ModelClass.fromName("growth/inactivation/survival"));
		assertEquals(t, ModelClass.fromName("T"));
		assertEquals(pH, ModelClass.fromName("pH"));
		assertEquals(aw, ModelClass.fromName("aw"));
		assertEquals(t_ph, ModelClass.fromName("T/pH"));
		assertEquals(t_aw, ModelClass.fromName("T/aw"));
		assertEquals(ph_aw, ModelClass.fromName("pH/aw"));
		assertEquals(t_ph_aw, ModelClass.fromName("T/pH/aw"));
		
		assertEquals(unknown, ModelClass.fromName("???"));
	}

	@Test
	public void testFromValue() {
		assertEquals(unknown, ModelClass.fromValue(0));
		assertEquals(growth, ModelClass.fromValue(1));
		assertEquals(inactivation, ModelClass.fromValue(2));
		assertEquals(survival, ModelClass.fromValue(3));
		assertEquals(growthInactivation, ModelClass.fromValue(4));
		assertEquals(inactivationSurvival, ModelClass.fromValue(5));
		assertEquals(growthSurvival, ModelClass.fromValue(6));
		assertEquals(growthInactivationSurvival, ModelClass.fromValue(7));
		assertEquals(t, ModelClass.fromValue(8));
		assertEquals(pH, ModelClass.fromValue(9));
		assertEquals(aw, ModelClass.fromValue(10));
		assertEquals(t_ph, ModelClass.fromValue(11));
		assertEquals(t_aw, ModelClass.fromValue(12));
		assertEquals(ph_aw, ModelClass.fromValue(13));
		assertEquals(t_ph_aw, ModelClass.fromValue(14));
		
		assertEquals(unknown, ModelClass.fromValue(Integer.MAX_VALUE));
	}
	
	@Test
	public void testFullName() {
		assertEquals(unknown.fullName(), "unknown");
		assertEquals(growth.fullName(), "growth");
		assertEquals(inactivation.fullName(), "inactivation");
		assertEquals(survival.fullName(), "survival");
		assertEquals(growthInactivation.fullName(), "growth/inactivation");
		assertEquals(inactivationSurvival.fullName(), "inactivation/survival");
		assertEquals(growthSurvival.fullName(), "growth/survival");
		assertEquals(growthInactivationSurvival.fullName(), "growth/inactivation/survival");
		assertEquals(t.fullName(), "T");
		assertEquals(pH.fullName(), "pH");
		assertEquals(aw.fullName(), "aw");
		assertEquals(t_ph.fullName(), "T/pH");
		assertEquals(t_aw.fullName(), "T/aw");
		assertEquals(ph_aw.fullName(), "pH/aw");
		assertEquals(t_ph_aw.fullName(), "T/pH/aw");
	}
}
