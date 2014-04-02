/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.combaseio.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class CombaseReader implements Enumeration<PmmTimeSeries> {

	private BufferedReader reader;
	private PmmTimeSeries next;
	private Map<String, Integer> newAgentIDs = new LinkedHashMap<String, Integer>();
	private Map<String, Integer> newMatrixIDs = new LinkedHashMap<String, Integer>();
	private Map<String, MiscXml> newMiscs = new LinkedHashMap<String, MiscXml>();

	public CombaseReader(final String filename) throws FileNotFoundException,
			IOException, Exception {
		InputStreamReader isr = null;
		File file = new File(filename);
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-16LE");
		} else {
			try {
				URL url = new URL(filename);
				isr = new InputStreamReader(url.openStream(), "UTF-16LE");
				isr.read();
			} catch (Exception e) {
				throw new FileNotFoundException("File not found");
			}
		}
		if (isr != null) {
			reader = new BufferedReader(isr);
			step();
		}
	}

	public void close() throws IOException {
		reader.close();
	}

	public PmmTimeSeries nextElement() {

		PmmTimeSeries ret;

		ret = next;

		try {
			step();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public boolean hasMoreElements() {
		return next != null;
	}

	private void step() throws IOException, Exception {
		// initialize next time series
		next = new PmmTimeSeries();

		while (true) {
			String line = reader.readLine();

			if (line == null) {
				next = null;
				return;
			}

			// split up token
			String[] token = line.split("\t");

			if (token.length < 2)
				continue;

			if (token[0].isEmpty())
				continue;

			for (int i = 0; i < token.length; i++) {
				// token[i] =
				// token[i].replaceAll("[^a-zA-Z0-9° \\.\\(\\)_/\\+\\-\\*,:]",
				// "");
				token[i] = token[i].replaceAll("\"", "");
			}
			String key = token[0].toLowerCase().trim();
			// utf16lemessage[0] = (byte)0xFF; utf16lemessage[1] = (byte)0xFE;
			if (key.length() > 1 && key.charAt(0) == 65279)
				key = key.substring(1);

			// fetch record id
			if (key.equals("recordid")) {
				next.setCombaseId(token[1]);
				continue;
			}

			// fetch organism
			if (key.equals("organism")) {
				// next.setAgentDetail( token[ 1 ] );
				setAgent(next, token[1]);
				continue;
			}

			// fetch environment
			if (key.equals("environment")) {
				// next.setMatrixDetail(token[1]);
				setMatrix(next, token[1]);
				continue;
			}

			// fetch temperature
			if (key.equals("temperature")) {
				int pos = token[1].indexOf(" ");
				if (!token[1].endsWith(" °C"))
					throw new PmmException("Temperature unit must be [°C]");
				Double value = parse(token[1].substring(0, pos));
				// next.setTemperature(value);
				next.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,
						AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_TEMPERATURE, value,
						Arrays.asList(Categories.getTempCategory().getName()),
						Categories.getTempCategory().getStandardUnit());
				continue;
			}

			// fetch pH
			if (key.equals("ph")) {
				Double value = parse(token[1]);
				// next.setPh(value);
				next.addMisc(AttributeUtilities.ATT_PH_ID,
						AttributeUtilities.ATT_PH, AttributeUtilities.ATT_PH,
						value,
						Arrays.asList(Categories.getPhCategory().getName()),
						Categories.getPhUnit());
				continue;
			}

			// fetch water activity
			if (key.equals("water activity")) {
				Double value = parse(token[1]);
				// next.setWaterActivity(value);
				next.addMisc(AttributeUtilities.ATT_AW_ID,
						AttributeUtilities.ATT_AW, AttributeUtilities.ATT_AW,
						value,
						Arrays.asList(Categories.getAwCategory().getName()),
						Categories.getAwUnit());
				continue;
			}

			// fetch conditions
			if (key.equals("conditions")) {
				PmmXmlDoc xml = combase2XML(token[1]);
				next.addMiscs(xml);
				continue;
			}

			if (key.equals("maximum rate")) {
				next.setMaximumRate(parse(token[1]));
				continue;
			}

			if (key.startsWith("time") && token[1].equals("logc")) {
				if (!key.endsWith("(h)"))
					throw new Exception("Time unit must be [h].");
				while (true) {
					line = reader.readLine();
					if (line == null)
						return;
					if (line.replaceAll("\\t\"", "").isEmpty())
						break;
					token = line.split("\t");
					for (int i = 0; i < token.length; i++) {
						token[i] = token[i].replaceAll(
								"[^a-zA-Z0-9° \\.\\(\\)/,]", "");
					}
					if (token.length < 2) {
						break;
					}
					double t = parse(token[0]);
					double logc = parse(token[1]);
					if (Double.isNaN(t) || Double.isNaN(logc)) {
						continue;
					}
					next.add(t, Categories.getTimeCategory().getStandardUnit(),
							logc, Categories.getConcentrationCategories()
									.get(0).getStandardUnit());
				}
				break;
			}
		}
	}

	private static double parse(String num) {
		double n = Double.NaN;

		num = num.toLowerCase();
		num = num.trim();
		if (num.equals("no growth"))
			return 0;

		try {
			num = num.replaceAll("[a-zA-Z\\(\\)\\s]", "");
			num = num.replaceAll(",", ".");
			n = Double.valueOf(num);
		} catch (Exception e) {
		}

		return n;
	}

	private PmmXmlDoc combase2XML(String misc) {
		PmmXmlDoc result = null;
		if (misc != null) {
			result = new PmmXmlDoc();
			List<String> conds = condSplit(misc);
			for (int i = 0; i < conds.size(); i++) {
				String val = conds.get(i).trim();
				int index = val.indexOf(':');
				int index2 = 0;
				// String unit = null;
				Double dbl = null;
				if (index >= 0) {
					try {
						dbl = Double.parseDouble(val.substring(index + 1));
						if (val.charAt(index - 1) == ')') {
							for (index2 = index - 1; index2 >= 0
									&& val.charAt(index2) != '('; index2--) {
								;
							}
							// unit = val.substring(index2 + 1, index - 1);
							val = val.substring(0, index2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					dbl = 1.0;
				}
				// ersetzen mehrerer Spaces im Text durch lediglich eines, Bsp.:
				// "was    ist los?" -> "was ist los?"
				String description = val.trim().replaceAll(" +", " ");
				MiscXml mx = getMiscXml(description, dbl);
				// new MiscXml(newIDs.get(description),
				// getCombaseName(description), description, dbl, unit);
				result.add(mx);
			}
		}
		return result;
	}

	private void setMatrix(PmmTimeSeries next, String matrixname) {
		Integer id = null;
		String matrixdetail = null;
		int index = matrixname.indexOf("(");
		if (index > 0) {
			matrixdetail = matrixname.substring(index).trim();
			matrixname = matrixname.substring(0, index).trim();
		}
		if (!newMatrixIDs.containsKey(matrixname)) {
			id = DBKernel.getID("Matrices", "Matrixname", matrixname);
			if (id == null) {
				System.err.println(matrixname + "... unknown Matrix ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newMatrixIDs.put(matrixname, id);
		} else
			id = newMatrixIDs.get(matrixname);
		matrixdetail = id < 0 ? matrixname + " (" + matrixdetail + ")"
				: matrixdetail;
		next.setMatrix(id, id < 0 ? null : matrixname, matrixdetail);
	}

	private void setAgent(PmmTimeSeries next, String agentsname) {
		Integer id = null;
		if (!newAgentIDs.containsKey(agentsname)) {
			id = DBKernel.getID("Agenzien", "Agensname", agentsname);
			if (id == null) {
				System.err.println(agentsname + "... unknown Agens ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newAgentIDs.put(agentsname, id);
		} else
			id = newAgentIDs.get(agentsname);
		next.setAgent(id, id < 0 ? null : agentsname, id < 0 ? agentsname
				: null);
	}

	private MiscXml getMiscXml(String description, Double dbl) {
		if (!newMiscs.containsKey(description)) {
			MiscXml m = getCombaseName(description);
			Integer id = (Integer) DBKernel.getValue(null, "SonstigeParameter",
					"Parameter", m.getName(), "ID");

			m.setId(id);
			newMiscs.put(description, m);
		}

		MiscXml misc = new MiscXml(newMiscs.get(description));

		misc.setValue(dbl);

		return misc;
	}

	private MiscXml getCombaseName(String des) {
		if (des.equalsIgnoreCase("alta fermentation product in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "ALTA",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des
				.equalsIgnoreCase("acetic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"acetic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("anaerobic environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"anaerobic", "", null, Arrays.asList("True/False Value"),
					"True/False");
		else if (des
				.equalsIgnoreCase("ascorbic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"ascorbic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des
				.equalsIgnoreCase("benzoic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"benzoic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des
				.equalsIgnoreCase("citric acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"citric_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("carbon-dioxide in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "CO_2",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("other species in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"competition", "", null, Arrays.asList("True/False Value"),
					"True/False");
		else if (des.equalsIgnoreCase("cut (minced, chopped, ground, etc)"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "cut", "",
					null, Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("dried food"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "dried",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des
				.equalsIgnoreCase("ethylenenediaminetetraacetic acid in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "EDTA",
					"", null, Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("ethanol in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "ethanol",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("fat in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "fat", "",
					null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("frozen food"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "frozen",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("fructose in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"fructose", "", null, Arrays.asList("Arbitrary Fraction"),
					"%");
		else if (des.equalsIgnoreCase("glucose in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "glucose",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("glycerol in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"glycerol", "", null, Arrays.asList("Arbitrary Fraction"),
					"%");
		else if (des.equalsIgnoreCase("hydrochloric acid in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "HCl", "",
					null, Arrays.asList("Mass Concentration"), "g/L");
		else if (des
				.equalsIgnoreCase("inoculation in/on previously heated (cooked, baked, pasteurized, etc) but not sterilised food/medium"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "heated",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des
				.equalsIgnoreCase("in an environment that has been irradiated"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"irradiated", "", null, Arrays.asList("True/False Value"),
					"True/False");
		else if (des
				.equalsIgnoreCase("irradiation at constant rate during the observation time"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"irradiation", "", null, Arrays.asList("Energy Content"),
					"kGy");
		else if (des
				.equalsIgnoreCase("lactic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"lactic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("modified atmosphere environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"Modified_Atmosphere", "", null,
					Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("malic acid in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"malic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("moisture in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"moisture", "", null, Arrays.asList("Arbitrary Fraction"),
					"%");
		else if (des
				.equalsIgnoreCase("glycerol monolaurate (emulsifier) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"monolaurin", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("nitrogen in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "N_2", "",
					null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("sodium chloride in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "NaCl",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("nisin in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "nisin",
					"", null, null, null);
		else if (des
				.equalsIgnoreCase("sodium or potassium nitrite in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "nitrite",
					"", null, Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des
				.equalsIgnoreCase("oxygen (aerobic conditions) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "O_2", "",
					null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des
				.equalsIgnoreCase("propionic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"propionic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("raw"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "raw", "",
					null, Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("shaken (agitated, stirred)"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "shaken",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("smoked food"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "smoked",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des
				.equalsIgnoreCase("sorbic acid (possibly as salt) in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"sorbic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("sterilised before inoculation"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "sterile",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des.equalsIgnoreCase("sucrose in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "sucrose",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("sugar in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "sugar",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("vacuum-packed"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "vacuum",
					"", null, Arrays.asList("True/False Value"), "True/False");
		else if (des
				.equalsIgnoreCase("oregano essential oil in the environment"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "oregano",
					"", null, Arrays.asList("Arbitrary Fraction"), "%");
		else if (des.equalsIgnoreCase("pressure controlled"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"pressure", "", null, Arrays.asList("Pressure"), "MPa");
		else if (des
				.equalsIgnoreCase("in presence of diacetic acid (possibly as salt)"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(),
					"diacetic_acid", "", null,
					Arrays.asList("Arbitrary Fraction"), "ppm");
		else if (des.equalsIgnoreCase("in presence of betaine"))
			return new MiscXml(MathUtilities.getRandomNegativeInt(), "betaine",
					"", null, Arrays.asList("Arbitrary Fraction"), "ppm");

		throw new RuntimeException("Unknown Condition: " + des);
	}

	private List<String> condSplit(final String misc) {
		if (misc == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(misc, ",");
		int openParenthesis = 0;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (openParenthesis > 0) {
				nextToken = result.get(result.size() - 1) + "," + nextToken;
				result.remove(result.size() - 1);
			}
			result.add(nextToken);
			openParenthesis = 0;
			int index = -1;
			while ((index = nextToken.indexOf("(", index + 1)) >= 0) {
				openParenthesis++;
			}
			while ((index = nextToken.indexOf(")", index + 1)) >= 0) {
				openParenthesis--;
			}
		}
		return result;
	}
}
