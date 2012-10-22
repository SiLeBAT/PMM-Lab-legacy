/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import javax.swing.JProgressBar;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.gui.dbtable.editoren.CB_ConditionsEditor;

/**
 * @author Weiser
 *
 */
public class MySQLImporter implements MyImporter {

	private LinkedHashMap<String, Integer> alreadyLoggedIDs = new LinkedHashMap<String, Integer>();
	private int maxID2Insert = 100;
	private boolean checkCombaseImportTable = false;
	private boolean forFirstDB = true;
	private boolean compareWithCatalogue = false;

	public MySQLImporter() {
		
	}
	public MySQLImporter(final int maxID2Insert, final boolean checkCombaseImportTable, final boolean forFirstDB) {
		this(maxID2Insert, checkCombaseImportTable, forFirstDB, false);
	}
	public MySQLImporter(final int maxID2Insert, final boolean checkCombaseImportTable, final boolean forFirstDB, final boolean compareWithCatalogue) {
		this.maxID2Insert = maxID2Insert;
		this.checkCombaseImportTable = checkCombaseImportTable;
		this.compareWithCatalogue = compareWithCatalogue;
		this.forFirstDB = forFirstDB;
	}
	@Override
	public void doImport(final String filename, final JProgressBar progress, final boolean showResults) {
	  	Runnable runnable = new Runnable() {
	        @Override
			public void run() {
	  		    try {
	        		if (progress != null) {
	        			progress.setVisible(true);
			              progress.setStringPainted(true);
			              progress.setString("Importiere MySQL Data...");
	        			progress.setMinimum(0);
	        			progress.setMaximum(320000);
	        			progress.setValue(0);
	        		}

					Class.forName( "com.mysql.jdbc.Driver" );
					Connection conn = DriverManager.getConnection("jdbc:mysql://vm-mysql:3306/pmmdb", "pmm_read", "pmmdbread"); //  "jdbc:mysql://vm-mysql:3306/pmmdb", "pmm_read", "pmmdbread" // "", "", ""
					conn.setAutoCommit(false);
			        Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			        st.setFetchSize(10000);			        
			        
			        ResultSet rs = st.executeQuery( "select * from sources" );
			        if (rs != null) {
					      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Literatur") +
						      		" (" + DBKernel.delimitL("Erstautor") + ", " + DBKernel.delimitL("Jahr") + ", " + DBKernel.delimitL("Titel") + ", " +
						      		 DBKernel.delimitL("Journal") + ", " +  DBKernel.delimitL("Volume") + ", " +  DBKernel.delimitL("Issue") + ", " +
						      		DBKernel.delimitL("Seite") + ", " + DBKernel.delimitL("Kommentar") + ", " + DBKernel.delimitL("Literaturtyp") + ") VALUES (?,?,?,?,?,?,?,?,1)",
						      		Statement.RETURN_GENERATED_KEYS);
					      int lfd = 0;
					      while (rs.next()) {
					    	  if (forFirstDB && rs.getObject("source_id") != null && rs.getString("source_id").equals("Frank_77b")) {
					    		  break;
					    	  }
					    	  String[] lit = splitLiteratur(rs.getObject("source"));
					    	  try {
						          for (int ii=0; ii<lit.length;ii++) {
							          if (lit[ii] == null) {
										ps.setNull(ii+1, java.sql.Types.VARCHAR);
									} else {
										ps.setString(ii+1, lit[ii]);
									}					        	  
						          }
					    	  }
					    	  catch (Exception e) {
					    		  MyLogger.handleMessage(rs.getObject("source").toString());
					    		  MyLogger.handleException(e);
					    	  }
					          if (rs.getObject("source_id") == null) {
								ps.setNull(lit.length+1, java.sql.Types.VARCHAR);
							} else {
								ps.setString(lit.length+1, rs.getString("source_id"));
							}
				          try {
				        	  Object foreignID = null;
					          String cb_src = getCBSource(rs.getString("source_id"));					          
					          if (cb_src == null) {
					        	  System.err.println("source_id is null???\t" + lit);
					          }
					          else {
						          //foreignID = DBKernel.getID("Literatur", "Kommentar", cb_src);					        	  
						          foreignID = DBKernel.getValue("ImportedCombaseData", "CombaseID", cb_src, "Literatur");					        	  
					          }
					          if (foreignID != null) {
								System.err.println(rs.getObject("source_id") + " gibts schon???");
							} else if (ps.executeUpdate() > 0) {
					        	  foreignID = DBKernel.getLastInsertedID(ps);
						          if (foreignID != null) {
								      PreparedStatement ps2 = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("ImportedCombaseData") +
									      		" (" + DBKernel.delimitL("CombaseID") + "," + DBKernel.delimitL("Literatur") +
									      		") VALUES (?,?)");
								      ps2.setString(1, rs.getString("source_id"));
								      ps2.setInt(2, (Integer) foreignID);
								      ps2.executeUpdate();
						          }
					          }
				          }
				          catch(SQLException e1) {
				        	  //MyLogger.handleMessage(ps.toString());
				        	  System.err.println(rs.getObject("source_id") + " hat nicht geklappt!!!...\n" + e1.getMessage());
				        	  //MyLogger.handleException(e1);
				          } // MyLogger.handleException(e1);
				          lfd++; if (lfd % 1000 == 0) {
							MyLogger.handleMessage("select * from sources\t" + lfd);
						}
				          if (progress != null) {progress.setValue(lfd);}
			        	}
			        	rs.close();
			        	/*
				          try {
					            ps.executeBatch();
				          }
				          catch(SQLException e1) {
				        	  MyLogger.handleException(e1);
				          } // MyLogger.handleException(e1);
				          */
			        }

			        LinkedHashMap<String, Integer> hash = new LinkedHashMap<String, Integer>();
			        rs = st.executeQuery( "select * from raw_data WHERE data_source = 'ComBase'" );  // AND id='AeroMayo_4C_40pH_1'
			        Integer myLastID = 0;
			        if (rs != null) {
					      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Versuchsbedingungen") +
						      		" (" + DBKernel.delimitL("ID_CB") + ", " + DBKernel.delimitL("Referenz") + ", " +
						      		 DBKernel.delimitL("Organismus_CB") + ", " + DBKernel.delimitL("Agens") + ", " + DBKernel.delimitL("environment_CB") + ", " +
						      		 DBKernel.delimitL("b_f_CB") + ", " + DBKernel.delimitL("b_f_details_CB") + ", " + DBKernel.delimitL("Matrix") + ", " +
						      		 DBKernel.delimitL("in_on") +
						      		 ") VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
					      
					      int lfd = 0;
					      String id_str;
					      while (rs.next()) {
					          id_str = rs.getString("id");
					          Object vID = DBKernel.getValue("ImportedCombaseData", "CombaseID", id_str, "Versuchsbedingung");
					          if (vID != null) {
								System.err.println(id_str + " gibbbbbbet schon?!?");
							} else {
						          if (rs.getObject("id") == null) {
									ps.setNull(1, java.sql.Types.VARCHAR);
								} else {
									ps.setString(1, id_str);
								}
						          Integer foreignID = null;
						          String cb_src = getCBSource(rs.getString("source_id"));					          
						          if (cb_src == null) {
						        	  System.err.println("source_id is null???\t" + rs.getObject("organism") + "\t" + rs.getObject("b_f_details"));
						          }
						          else {
							          foreignID = DBKernel.getID("Literatur", "Kommentar", cb_src);					        	  
						          }
						      	  if (foreignID != null) {
									ps.setInt(2, foreignID);
								} else {
									ps.setNull(2, java.sql.Types.INTEGER);
								}
						          if (rs.getObject("organism") == null) {
									ps.setNull(3, java.sql.Types.VARCHAR);
								} else {
									ps.setString(3, rs.getString("organism"));
								}
						      	  Integer erregerID = getTheID(foreignID, rs.getString("organism"), "Agenzien", "Agensname");
						          if (erregerID == null) {
									ps.setNull(4, java.sql.Types.INTEGER);
								} else {
									ps.setInt(4, erregerID);
								}
						          if (rs.getObject("environment") == null) {
									ps.setNull(5, java.sql.Types.VARCHAR);
								} else {
									ps.setString(5, rs.getString("environment"));
								}
						          if (rs.getObject("b_f") == null) {
									ps.setNull(6, java.sql.Types.VARCHAR);
								} else {
									ps.setString(6, rs.getString("b_f"));
								}
						          if (rs.getObject("b_f_details") == null) {
									ps.setNull(7, java.sql.Types.VARCHAR);
								} else {
									ps.setString(7, rs.getString("b_f_details"));
								}
						          Integer matrixID = getTheID(foreignID, rs.getString("b_f_details"), "Matrices", "Matrixname", rs.getString("b_f"));
						          if (matrixID == null || erregerID == null) {
									ps.setNull(8, java.sql.Types.INTEGER);
								} else {
						        	  ps.setInt(8, matrixID);
						        	  /*
						        	  ps3.setInt(1, matrixID);
							          try {
						      				if (ps3.executeUpdate() > 0) {
						      					Integer lastID = DBKernel.getLastInsertedID(ps3);
						      					if (lastID != null) {
						      						ps.setInt(8, lastID);
						      					}
						      				}							      
								          }
								          catch(SQLException e1) {} // MyLogger.handleException(e1);
								          */
						        	  
						          }
						          if (rs.getObject("in_on") == null) {
									ps.setNull(9, java.sql.Types.VARCHAR);
								} else {
									ps.setString(9, rs.getString("in_on"));
								}
						          try {
						        	  /*
						        	  if (erregerID != null || matrixID != null) {
						        		  System.err.println(erregerID + "\t" + matrixID + "\t" + rs.getString("organism") + "\t" + rs.getString("b_f_details") + "\t" + rs.getString("b_f"));
						        	  }
						        	  */
					      				if (erregerID != null && matrixID != null && myLastID < maxID2Insert && ps.executeUpdate() > 0) {// execute()
					      					Integer lastID = DBKernel.getLastInsertedID(ps);
					      					if (lastID != null) {
					      						if (rs.getObject("temperature_C") != null) {
					      							Object kzID = DBKernel.insertDBL("Versuchsbedingungen", "Temperatur", lastID, null, "Wert", getDouble(rs, "temperature_C"));
					      							if (kzID != null) {
						      							DBKernel.insertDBL("Versuchsbedingungen", "Temperatur", lastID, kzID, "Wert_typ", 1);				      								
						      							if (rs.getObject("temperature_assumed") != null && rs.getBoolean("temperature_assumed")) {
						      								DBKernel.insertDBL("Versuchsbedingungen", "Temperatur", lastID, kzID, "Wert_g", "TRUE");				      								
						      							}
					      							}
					      						}
					      						if (rs.getObject("pH") != null) {
					      							Object kzID = DBKernel.insertDBL("Versuchsbedingungen", "pH", lastID, null, "Wert", getDouble(rs, "pH"));
					      							if (kzID != null) {
						      							DBKernel.insertDBL("Versuchsbedingungen", "pH", lastID, kzID, "Wert_typ", 1);				      								
						      							if (rs.getObject("pH_assumed") != null && rs.getBoolean("pH_assumed")) {
						      								DBKernel.insertDBL("Versuchsbedingungen", "pH", lastID, kzID, "Wert_g", "TRUE");				      								
						      							}
					      							}
					      						}
					      						if (rs.getObject("aw") != null) {
					      							Object kzID = DBKernel.insertDBL("Versuchsbedingungen", "aw", lastID, null, "Wert", getDouble(rs, "aw"));
					      							if (kzID != null) {
						      							DBKernel.insertDBL("Versuchsbedingungen", "aw", lastID, kzID, "Wert_typ", 1);				      								
						      							if (rs.getObject("aw_assumed") != null && rs.getBoolean("aw_assumed")) {
						      								DBKernel.insertDBL("Versuchsbedingungen", "aw", lastID, kzID, "Wert_g", "TRUE");				      								
						      							}
					      							}
					      						}
					      						if (rs.getObject("co_2") != null) {
					      							Object kzID = DBKernel.insertDBL("Versuchsbedingungen", "CO2", lastID, null, "Wert", getDouble(rs, "co_2"));
					      							if (kzID != null) {
						      							DBKernel.insertDBL("Versuchsbedingungen", "CO2", lastID, kzID, "Wert_typ", 1);				      												      								
					      							}
					      						}
										        if (rs.getObject("conditions") != null) {
										        	  String cond = rs.getString("conditions");
										        	  CB_ConditionsEditor.getMyString(cond, lastID, "Versuchsbedingungen");
										        	  //String condRes = CB_ConditionsEditor.getMyString(cond, lastID, "Versuchsbedingungen");
										        	  //ps.setString(10, condRes);
										          }
					      						hash.put(id_str, lastID);
											      PreparedStatement ps2 = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("ImportedCombaseData") +
												      		" (" + DBKernel.delimitL("CombaseID") + "," + DBKernel.delimitL("Versuchsbedingung") +
												      		") VALUES (?,?)");
											      ps2.setString(1, id_str);
											      ps2.setInt(2, lastID);
											      ps2.executeUpdate();
					      						myLastID = lastID;
					      					}
					      				} 
					      				else {
					      					//MyLogger.handleMessage("W\t" + psexup + "\t" + erregerID + "\t" + matrixID + "\t" + myLastID);
					      				}
						          }
						          catch(SQLException e1) {
						        	  //MyLogger.handleException(e1);
						        	  System.err.println(rs.getString("id") + " gibbet schon?!? Oder was los????");
						          } // MyLogger.handleException(e1);					        	  
					          }
					          lfd++; if (lfd % 1000 == 0) {
								MyLogger.handleMessage("select * from raw_data\t" + lfd);
							}
					          if (progress != null) {progress.setValue(lfd);}
			        	} 
			        	rs.close();
			        }

				      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Messwerte") +
					      		" (" + DBKernel.delimitL("Versuchsbedingungen") + ", " + DBKernel.delimitL("ZeitEinheit") + ", " +
					      		DBKernel.delimitL("Konz_Einheit") +
					      		 ") VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			        //for (int i=0;i<20;i++) {
			        	//System.out.println(i);
				        //rs = st.executeQuery( "select * from measured_values LIMIT " + (i*100) + ",100" );
				        rs = st.executeQuery( "select * from measured_values" );
				        if (rs != null) {
						      int lfd = 0;
						      Integer foreignID = 0;
						      while (rs.next()) {
						    	  foreignID = hash.get(rs.getString("raw_data_key"));//DBKernel.getID("Versuchsbedingungen", "id_str", rs.getString("raw_data_key"));
						    	  if (foreignID != null) {
							      		ps.setInt(1, foreignID);
							      		/*
							      		if (rs.getObject("time_h") == null) ps.setNull(2, java.sql.Types.DOUBLE);
								        else ps.setDouble(2, rs.getDouble("time_h"));
								        */
							          ps.setString(2, "Stunde");
							          ps.setInt(3, 1); // "log Anzahl pro g"
							          //if (rs.getObject("weights") == null) ps.setNull(6, java.sql.Types.DOUBLE);
								      //else ps.setDouble(6, rs.getDouble("weights"));
							          try {
							            //ps.addBatch();
					      				if (ps.executeUpdate() > 0) {
					      					Integer lastID = DBKernel.getLastInsertedID(ps);
					      					if (lastID != null) {
					      						Double Konzentration = new Double(0);
					      						if (rs.getObject("logc") != null) {
													Konzentration = getDouble(rs, "logc");
												}
					      						else {
													System.err.println("logc is null: " + rs.getString("raw_data_key"));
												}
				      							Object kzID = DBKernel.insertDBL("Messwerte", "Konzentration", lastID, null, "Wert", Konzentration);
				      							if (kzID != null) {
					      							DBKernel.insertDBL("Messwerte", "Konzentration", lastID, kzID, "Wert_typ", 1);				      												      								
				      							}
					      						Double Zeit = new Double(0);
					      						if (rs.getObject("time_h") != null) {
													Zeit = getDouble(rs, "time_h");
												} else {
													System.err.println("time_h is null: " + rs.getString("raw_data_key"));
												}
				      							kzID = DBKernel.insertDBL("Messwerte", "Zeit", lastID, null, "Wert", Zeit);
				      							if (kzID != null) {
					      							DBKernel.insertDBL("Messwerte", "Zeit", lastID, kzID, "Wert_typ", 1);				      												      								
				      							}
					      					}
					      				}							      
							          }
							          catch(SQLException e1) {} // MyLogger.handleException(e1);
						    	  }
					          lfd++; if (lfd % 1000 == 0) {
								MyLogger.handleMessage("select * from measured_values\t" + lfd);
							}
					          if (progress != null) {progress.setValue(lfd);}
				        	}
				        	rs.close();
				        	/*
					          try {
						            ps.executeBatch();
					          }
					          catch(SQLException e1) {} // MyLogger.handleException(e1);
					          */
				        }
			        //}
				        DBKernel.doMNs(DBKernel.myList.getTable("Versuchsbedingungen"));
				        DBKernel.doMNs(DBKernel.myList.getTable("Messwerte"));

			        if (progress != null) {
	    				progress.setVisible(false);
	    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
	    				if (myDB.getActualTable() != null) {
		    				String tablename = myDB.getActualTable().getTablename();
		    				if (tablename.equals("Literatur") || tablename.equals("Versuchsbedingungen") || tablename.equals("Messwerte")) {
		    					myDB.setTable(myDB.getActualTable());
		    				}
	    				}
	    			}
	  				else {
	  					MyLogger.handleMessage("MySQL Data Importer - Fin");
	  				}
	  		    }
			    catch (Exception e) {MyLogger.handleException(e);}
	      }
	    };
	    
	    Thread thread = new Thread(runnable);
	    thread.start();
	    try {
				thread.join();
			}
	    catch (InterruptedException e) {
				MyLogger.handleException(e);
			}

	}

	private Double getDouble(final ResultSet rs, final String fieldname) {
		Double result = new Double(0); // N/D
		try {
			result = rs.getDouble(fieldname);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Integer getTheID(final Integer literaturID, final String bezeichner, final String foreignTable, final String foreignField) {
		return getTheID(literaturID, bezeichner, foreignTable, foreignField, null);
	}
	private Integer getTheID(final Integer literaturID, final String bezeichner, final String foreignTable, final String foreignField, final String b_f) {
		if (bezeichner == null) {
			return null;
		}
		Integer result = null;
		String betterBezeichner = bezeichner.replaceAll("'", "");
		String myBezeichner = bezeichner + "_" + foreignTable + "_" + foreignField;
		if (b_f != null) {
			myBezeichner = b_f + "_" + myBezeichner;
		}
		if (alreadyLoggedIDs.containsKey(literaturID + "_" + myBezeichner)) {
			result = alreadyLoggedIDs.get(literaturID + "_" + myBezeichner);
		}
		else {
		    try {
	    		if (foreignField.equals("Agensname") || foreignField.equals("Matrixname")) {
	    			String feldname = foreignField.equals("Agensname") ? "Agenskatalog" : "Matrixkatalog";
	    			String sql = "SELECT " + DBKernel.delimitL(feldname) + " FROM " + DBKernel.delimitL("ComBaseImport") +
						" WHERE " + DBKernel.delimitL(foreignField) + "='" + betterBezeichner + "'" +
						((b_f == null) ? "" : " AND " + DBKernel.delimitL("b_f") + "='"+b_f+"'") +
						" AND (" + DBKernel.delimitL("Referenz") + "="+literaturID + " OR " + DBKernel.delimitL("Referenz") + " IS NULL)" +
						// die Feldnamen, die nicht NULL sind oben listen!
						" ORDER BY (" + DBKernel.delimitL(feldname) + " IS NULL) ASC,"+
						// die Referenz, die nicht NULL sind listen und die dann in die DB aufnehmen!!!
						"(" + DBKernel.delimitL("Referenz") + " IS NULL) ASC"; 
	    			ResultSet rs = DBKernel.getResultSet(sql, false);
	    		    if (rs != null && rs.first()) {
	    		    	if (checkCombaseImportTable && rs.getObject(feldname) != null) {
	    		    		result = rs.getInt(feldname);
	    		    	}
	    		    	else if (compareWithCatalogue) {
		    		    	sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL(foreignField) +
		    		        " FROM " + DBKernel.delimitL(foreignTable) + " WHERE LCASE(" + DBKernel.delimitL(foreignField) + ") = '" + betterBezeichner.toLowerCase() + "'";
		    			    rs = DBKernel.getResultSet(sql, false);
		    			    try {
		    				    if (rs != null && rs.first()) {
		    				    	if (betterBezeichner.equals(rs.getString(foreignField))) {
										result = rs.getInt("ID");
									}
		    				    }
		    				    else {
		    				    	if (DBKernel.debug) {
										MyLogger.handleMessage(foreignField + "\t" + bezeichner);
									} 		    	
		    				    }
		    				}
		    				catch(Exception e) {MyLogger.handleException(e); System.err.println("getTheID:\t" + foreignField + "\t" + bezeichner);}
	    		    	}
	    		    }
	    		    else {
	    		    	insert2CBImport(literaturID, foreignField, betterBezeichner, b_f);

	    		    }
	    		}
	    		//if (DBKernel.debug) System.out.println(foreignField + "\t" + bezeichner + "\t" + rs.getString(foreignField) + "\t" + result); 
			}
			catch(Exception e) {MyLogger.handleException(e); System.err.println("getTheID:\t" + foreignField + "\t" + bezeichner);}	
						
			alreadyLoggedIDs.put(literaturID + "_" + myBezeichner, result);
		}
		
		return result;
	}
	private void insert2CBImport(final Integer literaturID, final String foreignField, final String betterBezeichner, final String b_f) {
    	System.out.println("neu in ComBaseImport:\t" + literaturID + "\t" + foreignField + "\t" + betterBezeichner + "\t" + b_f);
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ComBaseImport") +
				" (" + DBKernel.delimitL("Referenz") + ", " + DBKernel.delimitL(foreignField) + ((b_f == null) ? "" : ","+DBKernel.delimitL("b_f")) +
				") VALUES (" + literaturID + ",'" + betterBezeichner + "'" + ((b_f == null) ? "" : ",'"+b_f+"'") + ")", false);			    						    		    	
	}
	private String[] splitLiteratur(final Object literatur) {
		// Abram (et al.), 1984: Survival of Campylobacter jejuni at different temperatures in broth, beef chicken and cod supplemented with sodium chloride. Journal of Food Protection 47: 795-800
		// Frank (et al.), 1977: Inhibition of enteropathogenic Escherichia coli by homofermentative lactic acid bacteria in skimmilk. 2 - Comparison of lactic acid bacteria and enumeration methods. Journal of Food Protection   40(11): 754 - 759
		String[] result = new String[7];
		if (literatur != null) {
			try {
				String lit = literatur.toString();
				int index = lit.indexOf("(et al.)");
				if (lit.startsWith("Frank (et al.), 1977: Inhibition of enteropathogenic Escherichia coli by homofermentative")) {
					result[0] = "Frank";
					result[1] = "1977";
					if (lit.indexOf("1 - Comparison of strains of Escherichia coli") > 0) {
						result[2] = "Inhibition of enteropathogenic Escherichia coli by homofermentative lactic acid bacteria in skimmilk. 1 - Comparison of strains of Escherichia coli";
						result[6] = "749";
					}
					else {
						result[2] = "Inhibition of enteropathogenic Escherichia coli by homofermentative lactic acid bacteria in skimmilk. 2 - Comparison of lactic acid bacteria and enumeration methods";
						result[6] = "754";
					}
					result[3] = "Journal of Food Protection";
					result[4] = "40"; //    40(11): 754 - 759
				}
				else if (index >= 0) {
					result[0] = lit.substring(0, index).trim(); // Autor
					if (result[0].startsWith("Skandamis")) { // Skandamis (et al.). 2000: Ecophysiological attributes of Sa
						System.out.print("");
					}
					int index2 = lit.indexOf(": ", index);
					if (index2 < 0 || index2 - index > 20) {index2 = lit.indexOf(", ", index); if (index2 >= 0) {
						index2 = lit.indexOf(", ", index2 + 1);
					}}
					if (index2 < 0 || index2 - index > 20) {
						index2 = lit.indexOf(". ", index);
					}
					if (index2 < 0 || index2 - index > 20) {
						index2 = lit.indexOf(":", index);
					}
					if (index2 < 0) {
						result[2] = lit.substring(lit.indexOf(",", index) + 1).trim();
					}
					else {
						int index1 = lit.indexOf(",", index) + 1;
						if (index1 <= 0 || index1 > index2) {
							index1 = lit.indexOf(".)", index) + 3;
						}
						result[1] = lit.substring(index1, index2).trim(); // Jahr
						if (result[1].startsWith("(")) {
							result[1] = result[1].substring(1);
						}
						if (result[1].endsWith(")")) {
							result[1] = result[1].substring(0, result[1].length() - 1);
						}
						index = lit.indexOf(". ", index2 + 1);
						if (index < 0) {
							index = lit.indexOf(", Journal ", index2);
						}
						if (index < 0) {
							index = lit.indexOf(", Food ", index2);
						}
						if (index < 0) {
							index = lit.indexOf("  ", index2);
						}
						if (index < 0) {
							index = lit.indexOf(" Journal ", index2);
						}
						if (index < 0 || index - index2 < 5) {
							result[2] = lit.substring(index2 + 1).trim();
						}
						else {
							result[2] = lit.substring(index2 + 1, index + 1).trim(); // Title
							result[2] = result[2].substring(0, result[2].length() - 1); // Den Punkt bzw. das Komma wegmachen, sieht blöd aus, vor allem das Komma!
							result[3] = lit.substring(index + 1).trim(); // Journal, Volume, Issue, Seite						
						}
					}
				}
				else if (lit.startsWith("Adams, D.M., 1973: Inacti")) {
					result[0] = "Adams, D.M.";
					result[1] = "1973";
					result[2] = "Inactivation of Clostridium perfringens type A spores at ultrahigh temperatures";
					result[3] = "Applied Microbiology";
					result[4] = "26";
					result[6] = "282";
				}
				else {
					result[2] = lit;
				}
			}
			catch (Exception e) {
				System.err.println(literatur); MyLogger.handleException(e);
			}
		}
		return result;
	}
	private String getCBSource(final String cb_src) {
		if (cb_src != null && cb_src.equals("Oscar_99b")) {
			return "Oscar_99";
		} else {
			return cb_src;
		}
	}
}
