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
package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCellFactory;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

public class CellIO {

	public static long tttxcmldoc = 0;

	public static String getString(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		String s = ((StringCell) cell).getStringValue();

		if (s.trim().isEmpty()) {
			return null;
		}

		return s;
	}

	public static Integer getInt(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		return ((IntCell) cell).getIntValue();
	}

	public static PmmXmlDoc getPmmXml(DataCell cell) {

		if (!(cell instanceof DataCell))
			return new PmmXmlDoc();

		if (((DataCell) cell).isMissing())
			return new PmmXmlDoc();

		try {
			return new PmmXmlDoc(((StringValue) cell).getStringValue());
		} catch (Exception e) {
			return new PmmXmlDoc();
		}
	}

	public static Double getDouble(DataCell cell) {
		if (cell.isMissing()) {
			return null;
		}

		return ((DoubleCell) cell).getDoubleValue();
	}

	public static List<String> getStringList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<String>();
		}

		String[] toks = ((StringCell) cell).getStringValue().split(",");

		return new ArrayList<String>(Arrays.asList(toks));
	}

	public static List<Double> getDoubleList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<Double>();
		}

		List<Double> list = new ArrayList<Double>();
		String[] toks = ((StringCell) cell).getStringValue().split(",");

		for (String t : toks) {
			if (t.equals("?")) {
				list.add(null);
			} else {
				try {
					list.add(Double.parseDouble(t));
				} catch (NumberFormatException e) {
					return new ArrayList<Double>();
				}
			}
		}

		return list;
	}

	public static List<Integer> getIntList(DataCell cell) {
		if (cell.isMissing()) {
			return new ArrayList<Integer>();
		}

		List<Integer> list = new ArrayList<Integer>();
		String[] toks = ((StringCell) cell).getStringValue().split(",");

		for (String t : toks) {
			if (t.equals("?")) {
				list.add(null);
			} else {
				try {
					list.add(Integer.parseInt(t));
				} catch (NumberFormatException e) {
					return new ArrayList<Integer>();
				}
			}
		}

		return list;
	}

	public static DataCell createCell(Object o) {
		if (o instanceof String && !((String) o).isEmpty()) {
			return new StringCell((String) o);
		} else if (o instanceof Integer) {
			return new IntCell((Integer) o);
		} else if (o instanceof Double) {
			return new DoubleCell((Double) o);
		}

		return DataType.getMissingCell();
	}

	public static DataCell createXmlCell(PmmXmlDoc xmlDoc) {
		if (xmlDoc == null)
			return null;
		DataCell xmlCell = null;
		long ttt = System.currentTimeMillis();
		org.w3c.dom.Document doc = xmlDoc.getW3C();
		xmlCell = XMLCellFactory.create(doc);
		tttxcmldoc += (System.currentTimeMillis() - ttt);
		return xmlCell;
	}

	public static DataCell createMissingCell() {
		return DataType.getMissingCell();
	}

	public static DataCell createDoubleCell(String d) {
		if (d == null) {
			return createMissingCell();
		}

		try {
			return createCell(Double.parseDouble(d));
		} catch (NumberFormatException e) {
			return createMissingCell();
		}
	}

	public static DataCell createIntCell(String d) {
		if (d == null) {
			return createMissingCell();
		}

		try {
			return createCell(Integer.parseInt(d));
		} catch (NumberFormatException e) {
			return createMissingCell();
		}
	}

	public static Map<String, String> getMap(DataCell dataCell)
			throws PmmException {

		String[] t1, t2;
		Map<String, String> ret;

		ret = new LinkedHashMap<String, String>();

		if (dataCell.isMissing())
			return ret;

		if (!(dataCell instanceof StringCell))
			throw new PmmException("Only String cell can return map.");

		t1 = ((StringCell) dataCell).getStringValue().split(",");

		for (String map : t1) {

			t2 = map.split("=");
			if (t2.length != 2)
				throw new PmmException("Map string contains malformed item.");

			ret.put(t2[0], t2[1]);
		}

		return ret;
	}

	public static List<String> getNameList(PmmXmlDoc xml) {
		List<String> names = new ArrayList<String>();

		for (PmmXmlElementConvertable element : xml.getElementSet()) {
			if (element instanceof MiscXml) {
				names.add(((MiscXml) element).getName());
			} else if (element instanceof AgentXml) {
				names.add(((AgentXml) element).getName());
			} else if (element instanceof MatrixXml) {
				names.add(((MatrixXml) element).getName());
			} else if (element instanceof TimeSeriesXml) {
				names.add(((TimeSeriesXml) element).getName());
			} else if (element instanceof MdInfoXml) {
				names.add(((MdInfoXml) element).getName());
			} else if (element instanceof ParamXml) {
				names.add(((ParamXml) element).getName());
			} else if (element instanceof EstModelXml) {
				names.add(((EstModelXml) element).getName());
			} else if (element instanceof CatalogModelXml) {
				names.add(((CatalogModelXml) element).getName());
			} else if (element instanceof IndepXml) {
				names.add(((IndepXml) element).getName());
			} else if (element instanceof DepXml) {
				names.add(((DepXml) element).getName());
			} else {
				throw new RuntimeException();
			}
		}

		return names;
	}

	public static HashMap<Integer, Integer> setMIDs(boolean before,
			String attr, String dbTablename,
			HashMap<Integer, Integer> foreignDbIdsTable, KnimeTuple row,
			ParametricModel pm) throws PmmException {
		if (dbTablename.equals("Literatur")) {
			PmmXmlDoc lili = row.getPmmXml(attr);
			if (lili != null) {
				PmmXmlDoc fromToXmlDB = attr.startsWith(Model1Schema.ATT_EMLIT) ? pm
						.getEstModelLit() : pm.getModelLit();
				int i = 0;
				for (PmmXmlElementConvertable el : lili.getElementSet()) {
					if (el instanceof LiteratureItem) {
						LiteratureItem li = (LiteratureItem) el;
						LiteratureItem liDB = ((LiteratureItem) fromToXmlDB
								.get(i));
						Integer key = li.getId();
						if (key != null && foreignDbIdsTable.containsKey(key)) {
							if (before) {
								liDB.setId(foreignDbIdsTable.get(key));
								fromToXmlDB.set(i, liDB);
							} else if (foreignDbIdsTable.get(key).intValue() != liDB
									.getId().intValue()) {
								System.err
										.println("checkIDs, LiteratureItem ... shouldn't happen - "
												+ foreignDbIdsTable.get(key)
														.intValue()
												+ "\t"
												+ liDB.getId().intValue());
							}
						} else {
							if (before) {
								liDB.setId(MathUtilities.getRandomNegativeInt());
								fromToXmlDB.set(i, liDB);
							} else
								foreignDbIdsTable.put(key, liDB.getId());
						}
					}
					i++;
				}
				if (attr.startsWith(Model1Schema.ATT_EMLIT))
					pm.setEstLit(fromToXmlDB);
				else
					pm.setMLit(fromToXmlDB);
			}
		} else if (attr.startsWith(Model1Schema.ATT_MODELCATALOG)) { // Modellkatalog
			PmmXmlDoc modelCat = row.getPmmXml(attr);
			if (modelCat != null) {
				PmmXmlDoc fromToXmlDB = pm.getCatModel();
				int i = 0;
				for (PmmXmlElementConvertable el : modelCat.getElementSet()) {
					if (el instanceof CatalogModelXml) {
						CatalogModelXml cmx = (CatalogModelXml) el;
						CatalogModelXml cmxDB = ((CatalogModelXml) fromToXmlDB
								.get(i));
						Integer key = cmx.getId();
						if (key != null && foreignDbIdsTable.containsKey(key)) {
							if (before) {
								pm.setModelId(foreignDbIdsTable.get(key));
								cmxDB.setId(foreignDbIdsTable.get(key));
								fromToXmlDB.set(i, cmxDB);
							} else if (foreignDbIdsTable.get(key).intValue() != cmxDB
									.getId().intValue()) {
								System.err
										.println("checkIDs, CatalogModelXml ... shouldn't happen - "
												+ foreignDbIdsTable.get(key)
														.intValue()
												+ "\t"
												+ cmxDB.getId().intValue());
							}
						} else {
							if (before) {
								int rn = MathUtilities.getRandomNegativeInt();
								pm.setModelId(rn);
								cmxDB.setId(rn);
								fromToXmlDB.set(i, cmxDB);
							} else
								foreignDbIdsTable.put(key, cmxDB.getId());
						}
					}
					i++;
				}
			}
		} else if (attr.startsWith(Model1Schema.ATT_ESTMODEL)) { // GeschaetzteModelle
			PmmXmlDoc estModel = row.getPmmXml(attr);
			if (estModel != null) {
				PmmXmlDoc fromToXmlDB = pm.getEstModel();
				int i = 0;
				for (PmmXmlElementConvertable el : estModel.getElementSet()) {
					if (el instanceof EstModelXml) {
						EstModelXml emx = (EstModelXml) el;
						EstModelXml emxDB = ((EstModelXml) fromToXmlDB.get(i));
						Integer key = emx.getID();
						if (key != null && foreignDbIdsTable.containsKey(key)) {
							if (before) {
								pm.setEstModelId(foreignDbIdsTable.get(key));
								emxDB.setID(foreignDbIdsTable.get(key));
								fromToXmlDB.set(i, emxDB);
							} else if (foreignDbIdsTable.get(key).intValue() != emxDB
									.getID().intValue()) {
								System.err
										.println("checkIDs, EstModelXml ... shouldn't happen - "
												+ foreignDbIdsTable.get(key)
														.intValue()
												+ "\t"
												+ emxDB.getID().intValue());
							}
						} else {
							if (before) {
								int rn = MathUtilities.getRandomNegativeInt();
								pm.setEstModelId(rn);
								emxDB.setID(rn);
								fromToXmlDB.set(i, emxDB);
							} else
								foreignDbIdsTable.put(key, emxDB.getID());
						}
					}
					i++;
				}
			}
		}
		return foreignDbIdsTable;
	}

	public static HashMap<Integer, Integer> setTsIDs(boolean before,
			String attr, HashMap<Integer, Integer> foreignDbIds,
			KnimeTuple row, KnimeTuple schemaTuple) throws PmmException {
		int type = schemaTuple.getSchema().getType(row.getIndex(attr));
		if (type == KnimeAttribute.TYPE_XML) {
			PmmXmlDoc x = row.getPmmXml(attr);
			if (x != null) {
				PmmXmlDoc fromToXmlDB = schemaTuple.getPmmXml(attr);
				// if (before) schemaTuple.setCell(attr,
				// CellIO.createMissingCell());
				int i = 0;
				for (PmmXmlElementConvertable el : x.getElementSet()) {
					if (el instanceof MiscXml) {
						MiscXml mx = (MiscXml) el;
						MiscXml mx2DB = ((MiscXml) fromToXmlDB.get(i));
						Integer key = mx.getID();
						if (key != null && key <= -1 && key >= -3)
							continue; // ATT_TEMPERATURE_ID, ATT_PH_ID or
										// ATT_AW_ID
						if (key != null && foreignDbIds.containsKey(key)) {
							if (before) {
								mx2DB.setID(foreignDbIds.get(key)); // schemaTuple.addValue(attr,
																	// foreignDbIds.get(key));
								fromToXmlDB.set(i, mx2DB);
							} else if (foreignDbIds.get(key).intValue() != mx2DB
									.getID().intValue()) {
								System.err
										.println("fillNewIDsIntoForeign ... shouldn't happen...MiscXml");
							}
						} else {
							if (before) {
								mx2DB.setID(MathUtilities
										.getRandomNegativeInt()); // schemaTuple.addValue(attr,
																	// MathUtilities.getRandomNegativeInt());
								fromToXmlDB.set(i, mx2DB);
							} else
								foreignDbIds.put(key, mx2DB.getID()); // schemaTuple.getIntList(attr).get(i));
						}
					} else if (el instanceof MatrixXml) {
						MatrixXml matx = (MatrixXml) el;
						MatrixXml matxDB = ((MatrixXml) fromToXmlDB.get(i));
						Integer key = matx.getID();
						if (key != null && foreignDbIds.containsKey(key)) {
							if (before) {
								matxDB.setID(foreignDbIds.get(key)); // schemaTuple.addValue(attr,
																		// foreignDbIds.get(key));
								fromToXmlDB.set(i, matxDB);
							} else if (foreignDbIds.get(key).intValue() != matxDB
									.getID()) {
								System.err
										.println("fillNewIDsIntoForeign ... shouldn't happen...MatrixXml");
							}
						} else {
							if (before) {
								matxDB.setID(MathUtilities
										.getRandomNegativeInt()); // schemaTuple.addValue(attr,
																	// MathUtilities.getRandomNegativeInt());
								fromToXmlDB.set(i, matxDB);
							} else
								foreignDbIds.put(key, matxDB.getID()); // schemaTuple.getIntList(attr).get(i));
						}
					} else if (el instanceof AgentXml) {
						AgentXml ax = (AgentXml) el;
						AgentXml axDB = ((AgentXml) fromToXmlDB.get(i));
						Integer key = ax.getID();
						if (key != null && foreignDbIds.containsKey(key)) {
							if (before) {
								axDB.setID(foreignDbIds.get(key)); // schemaTuple.addValue(attr,
																	// foreignDbIds.get(key));
								fromToXmlDB.set(i, ax);
							} else if (foreignDbIds.get(key).intValue() != axDB
									.getID()) {
								System.err
										.println("fillNewIDsIntoForeign ... shouldn't happen...AgentXml");
							}
						} else {
							if (before) {
								axDB.setID(MathUtilities.getRandomNegativeInt()); // schemaTuple.addValue(attr,
																					// MathUtilities.getRandomNegativeInt());
								fromToXmlDB.set(i, ax);
							} else
								foreignDbIds.put(key, axDB.getID()); // schemaTuple.getIntList(attr).get(i));
						}
					} else if (el instanceof LiteratureItem) {
						LiteratureItem li = (LiteratureItem) el;
						LiteratureItem liDB = ((LiteratureItem) fromToXmlDB
								.get(i));
						Integer key = li.getId();
						if (key != null && foreignDbIds.containsKey(key)) {
							if (before) {
								liDB.setId(foreignDbIds.get(key)); // schemaTuple.addValue(attr,
																	// foreignDbIds.get(key));
								fromToXmlDB.set(i, liDB);
							} else if (foreignDbIds.get(key).intValue() != liDB
									.getId().intValue()) {
								System.err
										.println("fillNewIDsIntoForeign ... shouldn't happen...LiteratureItem");
							}
						} else {
							if (before) {
								liDB.setId(MathUtilities.getRandomNegativeInt()); // schemaTuple.addValue(attr,
																					// MathUtilities.getRandomNegativeInt());
								fromToXmlDB.set(i, liDB);
							} else
								foreignDbIds.put(key, liDB.getId()); // schemaTuple.getIntList(attr).get(i));
						}
					}
					i++;
				}
				schemaTuple.setValue(attr, fromToXmlDB);
			}
		} else {
			Integer key = row.getInt(attr);
			if (key != null) {
				if (foreignDbIds.containsKey(key)) {
					if (before)
						schemaTuple.setValue(attr, foreignDbIds.get(key));
					else if (foreignDbIds.get(key).intValue() != schemaTuple
							.getInt(attr).intValue()) {
						System.err
								.println("fillNewIDsIntoForeign ... shouldn't happen");
					}
				} else {
					if (before)
						schemaTuple.setValue(attr,
								MathUtilities.getRandomNegativeInt());
					else
						foreignDbIds.put(key, schemaTuple.getInt(attr));
				}
			}
		}
		return foreignDbIds;
	}

}
