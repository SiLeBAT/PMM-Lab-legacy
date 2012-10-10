/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;

import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Messwerte;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

public class EMFUtils 
{
	//private static DataCellFactory dataCellFactory = new DataCellFactory();
	
	public static DataTableSpec getDataTableSpecVersuchsbedingung(){
		return new DataTableSpec(new String[]{"ID","ID_CB"}, new DataType[]{StringCell.TYPE,StringCell.TYPE});		
	}
	
	public static DataTableSpec getDataTableSpecEstimatedModel(){
		return new DataTableSpec(new String[]{"ID","Versuchsbedingung","Modell"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE});		
	}
	
	public static DataTableSpec getDataTableSpecStatistikModellParameter(){
		return new DataTableSpec(new String[]{"Parametername", "min", "max"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE});
	}
	
	/**
	 * 
	 * @return a new DataTableSpec for <tt>StatistikModel</tt>
	 */
	public static DataTableSpec getDataTableSpecStatistikModel(){
		return new DataTableSpec(new String[]{"ID", "Name", "Formel"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE});
	}
	
	public static BufferedDataTable createBufferedDataTableFromVersuchsbedingung(List<VersuchsBedingung> bedingungen, ExecutionContext exec) throws CanceledExecutionException
	{		
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		for(int i=0; i<bedingungen.size(); i++)
		{
			VersuchsBedingung bedingung = bedingungen.get(i);
			CustomDataRow row = new CustomDataRow(i);
			row.addCell(String.valueOf(bedingung.getId()));
			row.addCell(String.valueOf(bedingung.getIdCB()));
			rows.add(row);
		}

		CustomDataTable cdt = new CustomDataTable(getDataTableSpecVersuchsbedingung(), rows.toArray(new CustomDataRow[rows.size()]));
		
		return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
	}

	public static BufferedDataTable createBufferedDataTableFromVersuchsbedingung(
			VersuchsBedingung vBedingung, ExecutionContext exec) throws CanceledExecutionException {

		ArrayList<VersuchsBedingung> vbList = new ArrayList<VersuchsBedingung>();
    	vbList.add(vBedingung);
    	
		return createBufferedDataTableFromVersuchsbedingung(vbList, exec);
		
	}

	public static BufferedDataTable createBufferedDataTableFromEstimatedModels(
			List<GeschaetztStatistikModell> gModels,
			ExecutionContext exec) throws CanceledExecutionException {
	    String[] colnames = "ID|Versuchsbedingung|Modell|Response|manuellEingetragen|Rsquared|RSS|Score|Kommentar".split("[|]");	
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		for(int i=0; i<gModels.size(); i++)
		{
			GeschaetztStatistikModell model = gModels.get(i);
			
			CustomDataRow row = new CustomDataRow(i);
			row.addCell(String.valueOf(model.getId()));
			row.addCell((model.getBedingung()!=null) ? String.valueOf(model.getBedingung().getId()) : "");
			row.addCell((model.getStatistikModel()!=null) ? String.valueOf(model.getStatistikModel().getId()) : "");
            row.addCell((model.getResponse()!=null) ? String.valueOf(model.getResponse()) : "");
            row.addCell(String.valueOf(model.isManuellEingetragen()));
            row.addCell(String.valueOf(model.getRSquared()));
            row.addCell(String.valueOf(model.getRss()));
            row.addCell(String.valueOf(model.getScore()));
            row.addCell(String.valueOf(model.getKommentar()));
			rows.add(row);
		}
		
		CustomDataTable cdt = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(new CustomDataRow[rows.size()]));
				
		return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
	}
	
	/**
	 * 
	 * @param models a list of estimated statistic models 
	 * @param exec the Execution Context
	 * @return StatistikModell
	 * @throws CanceledExecutionException 
	 */
	public static BufferedDataTable createBufferedDataTableFromStatistikModell(
			List<GeschaetztStatistikModell> models, ExecutionContext exec) throws CanceledExecutionException {
		
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		CustomDataRow row = new CustomDataRow(1);
		for (int i = 0; i < models.size(); i++){
			StatistikModell model = models.get(i).getStatistikModel();
			row.addCell(String.valueOf(model.getId()));
			row.addCell(String.valueOf(model.getName()));
			row.addCell(String.valueOf(model.getFormel()));
		
			rows.add(row);
		}
		CustomDataTable table = new CustomDataTable(
				getDataTableSpecStatistikModel(), rows.toArray(new CustomDataRow[rows.size()]));
		return exec.createBufferedDataTable(table, new ExecutionMonitor());
	}
   public static BufferedDataTable createBufferedDataTableFromParameters(
            List<GeschaetztStatistikModell> models,
            ExecutionContext exec) throws CanceledExecutionException {
        int counter = 0;
        ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
        for (int i = 0; i < models.size(); i++) {
            EList<StatistikModellParameter> params = models.get(i).getStatistikModel().getParameter();
            for (int j = 0; j < params.size(); j++) {
                CustomDataRow row = new CustomDataRow(counter++);
                row.addCell(String.valueOf(params.get(j).getName()));
                row.addCell(String.valueOf(params.get(j).getMin()));
                row.addCell(String.valueOf(params.get(j).getMax()));
                rows.add(row);
            }
        }
        CustomDataTable table = new CustomDataTable(
                getDataTableSpecStatistikModellParameter(), 
                rows.toArray(new CustomDataRow[rows.size()]));
        return exec.createBufferedDataTable(table, new ExecutionMonitor());
    }
	   
	public static BufferedDataTable createBufferedDataTableFromEstimatedParameters(List<GeschaetztStatistikModell> models, ExecutionContext exec) throws CanceledExecutionException 
	{
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		int counter=0;
		String[] colnames = new String[]{"ID", "KI.oben", "KI.unten","GeschaetztesModell","Parameter","p","SD","t","Wert"};
		for (int i = 0; i < models.size(); i++){
			EList<GeschModellParameter> estParams = models.get(i).getParameter();
			for (int j = 0; j < estParams.size(); j++) {
				CustomDataRow row = new CustomDataRow(counter++);
				row.addCell(String.valueOf(estParams.get(j).getId())); //ID				
                row.addCell(String.valueOf(estParams.get(j).getKiOben()));   //"KI.unten"           
                row.addCell(String.valueOf(estParams.get(j).getKiUnten()));              
                row.addCell((estParams.get(j).getGeschaetztesModell() != null) ? String.valueOf(estParams.get(j).getGeschaetztesModell().getId()) : ""); //"GeschaetztesModell"              
                row.addCell(String.valueOf(estParams.get(j).getModelParameter().getId()));       //Parameter       
                row.addCell(String.valueOf(estParams.get(j).getP())); // p               
                row.addCell(String.valueOf(estParams.get(j).getSd())); // SD             
                row.addCell(String.valueOf(estParams.get(j).getT())); // t              
                row.addCell(String.valueOf(estParams.get(j).getWert())); // Wert              
                rows.add(row);
			}
		}
		
		CustomDataTable table = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(
				new CustomDataRow[rows.size()]));
		
		return exec.createBufferedDataTable(table, new ExecutionMonitor());
	}
	
	public static BufferedDataTable createBufferedDataTableFromEstimatedParameters(List<GeschaetztStatistikModell> models, int[] paramIds, ExecutionContext exec) throws CanceledExecutionException 
	{
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		int counter=0;
		String[] colnames = new String[]{"ID", "KI.oben", "KI.unten","GeschaetztesModell","Parameter","p","SD","t","Wert"};
		for (int i = 0; i < models.size(); i++){
			EList<GeschModellParameter> estParams = models.get(i).getParameter();
			for (int j = 0; j < estParams.size(); j++) {
				for (int p : paramIds) {
					if (p == estParams.get(i).getId()) {
						CustomDataRow row = new CustomDataRow(counter++);
						row.addCell(String.valueOf(estParams.get(j).getId())); //ID				
		                row.addCell(String.valueOf(estParams.get(j).getKiOben()));   //"KI.unten"           
		                row.addCell(String.valueOf(estParams.get(j).getKiUnten()));              
		                row.addCell(String.valueOf(estParams.get(j).getGeschaetztesModell().getId())); //"GeschaetztesModell"              
		                row.addCell(String.valueOf(estParams.get(j).getModelParameter().getId()));       //Parameter       
		                row.addCell(String.valueOf(estParams.get(j).getP())); // p               
		                row.addCell(String.valueOf(estParams.get(j).getSd())); // SD             
		                row.addCell(String.valueOf(estParams.get(j).getT())); // t              
		                row.addCell(String.valueOf(estParams.get(j).getWert())); // Wert              
		                rows.add(row);
					}
				}
			}
		}
		
		CustomDataTable table = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(
				new CustomDataRow[rows.size()]));
		
		return exec.createBufferedDataTable(table, new ExecutionMonitor());
	}
	
	public static BufferedDataTable createBufferedDataTableFromEstimatedParamsById(int[] paramIds, ExecutionContext exec) throws CanceledExecutionException {
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		int counter=0;
		String[] colnames = new String[]{"ID", "KI.oben", "KI.unten","GeschaetztesModell","Parameter","p","SD","t","Wert"};
		for (int i = 0; i < paramIds.length; i++) {
			GeschModellParameter param =  BfRNodePluginActivator.getBfRService().getGeschParameterByParamId(paramIds[i]);
			if (param!=null) {
				CustomDataRow row = new CustomDataRow(counter++);
				row.addCell(String.valueOf(param.getId()));
				row.addCell(String.valueOf(param.getKiOben()));
				row.addCell(String.valueOf(param.getKiUnten()));              
	            row.addCell(String.valueOf(param.getGeschaetztesModell().getId())); //"GeschaetztesModell"              
	            row.addCell(String.valueOf(param.getModelParameter().getId()));       //Parameter       
	            row.addCell(String.valueOf(param.getP())); // p               
	            row.addCell(String.valueOf(param.getSd())); // SD             
	            row.addCell(String.valueOf(param.getT())); // t              
	            row.addCell(String.valueOf(param.getWert())); // Wert              
	            rows.add(row);
			}
		}
		
		CustomDataTable table = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(
				new CustomDataRow[rows.size()]));
		return exec.createBufferedDataTable(table, new ExecutionMonitor());
	}
	
	
	public static BufferedDataTable createBufferedDataTableFromParameterCovCor(List<GeschaetztStatistikModell> models, ExecutionContext exec) throws CanceledExecutionException {
		ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
		int counter = 0;
		String[] colNames = new String[] {"ID", "param1", "param2", "GeschaetztesModell", "cor", "Wert"};
		for (int i = 0; i < models.size(); i++) {
			EList<ParameterCovCor> paramCovCor = models.get(i).getParameterCovCor();
			for (int j = 0; j < paramCovCor.size(); j++) {
				CustomDataRow row = new CustomDataRow(counter++);
				row.addCell(String.valueOf(paramCovCor.get(j).getId()));
				row.addCell(String.valueOf(paramCovCor.get(j).getParameter1().getId()));
				row.addCell(String.valueOf(paramCovCor.get(j).getParameter2().getId()));
				row.addCell(String.valueOf(paramCovCor.get(j).getParameter1().getGeschaetztesModell().getId()));
				row.addCell(String.valueOf(paramCovCor.get(j).isCor()));
				row.addCell(String.valueOf(paramCovCor.get(j).getValue()));
				rows.add(row);
			}
		}
		CustomDataTable table = new CustomDataTable(getDataTableSpecCustom(colNames), rows.toArray(new CustomDataRow[rows.size()]));
		
		return exec.createBufferedDataTable(table, new ExecutionMonitor());
	}
	
	public static DataTableSpec getDataTableSpecCustom(String[] colnames ){
	    List<DataType> dataTypes = new ArrayList<DataType>();
	    for(int i=0; i<colnames.length; i++){
	        dataTypes.add(StringCell.TYPE);
	    }
        return new DataTableSpec(colnames,dataTypes.toArray(new DataType[dataTypes.size()]));
	}
	
    public static BufferedDataTable createBufferedDataTableFromModel(
            List<StatistikModell> sModels, ExecutionContext exec) throws CanceledExecutionException {
        String[] colnames = "ID|Name|Notation|Level|Klasse|Typ|Eingabedatum|eingegeben_von|Beschreibung|Formel|Software|Kommentar".split("[|]");
        ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
        for(int i=0; i<sModels.size(); i++)
        {
            StatistikModell model = sModels.get(i);
            CustomDataRow row = new CustomDataRow(i);
            row.addCell(String.valueOf(model.getId()));
            row.addCell(String.valueOf(model.getName()));
            row.addCell(String.valueOf(model.getNotation()));
            row.addCell(String.valueOf(model.getLevel().getValue()));
            row.addCell(String.valueOf(model.getKlasse().getValue()));
            row.addCell(String.valueOf(model.getTyp()));
            row.addCell(new java.sql.Date(model.getEingabedatum().getTime()).toString());
            row.addCell(String.valueOf(model.getBenutzer())); // eingegeben_von
            row.addCell(String.valueOf(model.getBeschreibung()));
            row.addCell(String.valueOf(model.getFormel()));
            row.addCell(String.valueOf(model.getSoftware().name()));
            row.addCell(String.valueOf(model.getKommentar()));
            rows.add(row);
        }
        CustomDataTable cdt = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(new CustomDataRow[rows.size()]));
                
        return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
    }

    public static DataTableSpec getDataTableSpecModel() {
        return new DataTableSpec(new String[]{"ID", "Benutzer", "Beschreibung", "Eingabedatum", "Formel", "Klasse", "Kommentar", "Level", "Name", "Notation", "Software", "Typ" }, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE});        
    }

    public static BufferedDataTable createBufferedDataTableFromModelParameter(
            List<StatistikModell> sModels,
            ExecutionContext exec) throws CanceledExecutionException {
        String[] colnames = "ID|Modell|Parametername|Parametertyp|ganzzahl|min|max|Beschreibung".split("[|]");
        ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
        int rowcounter = 0;
        for(int i=0; i<sModels.size(); i++)
        {
            for(int j=0; j<sModels.get(i).getParameter().size(); j++)
            {
                StatistikModellParameter parameter = sModels.get(i).getParameter().get(j);
                
                CustomDataRow row = new CustomDataRow(rowcounter++);
                row.addCell(String.valueOf(parameter.getId()));      
                row.addCell(String.valueOf(sModels.get(i).getId()));
                row.addCell(String.valueOf(parameter.getName()));      
                row.addCell(String.valueOf(parameter.getRole().getValue()));      
                row.addCell(String.valueOf(parameter.isInteger()));      
                row.addCell(String.valueOf(parameter.getMax()));      
                row.addCell(String.valueOf(parameter.getMin()));      
                row.addCell(String.valueOf(parameter.getBeschreibung()));      
                rows.add(row);
            }
        }
        CustomDataTable cdt = new CustomDataTable(getDataTableSpecCustom(colnames), rows.toArray(new CustomDataRow[rows.size()]));
                
        return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
    }
    
    public static DataTableSpec getDataTableSpecModelParameter() {
        return new DataTableSpec(new String[]{"ID", "Beschreibung", "Max", "Min", "Name", "Role","Modell"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE});        
    }

    public static BufferedDataTable createBufferedDataTableFromMesswerte(
            List<Messwerte> messwerte, ExecutionContext exec) throws CanceledExecutionException {
        ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
        for(int i=0; i<messwerte.size(); i++)
        {
            Messwerte model = messwerte.get(i);

			CustomDataRow row = new CustomDataRow(i);
			row.addCell(String.valueOf(model.getId()));
			row.addCell((model.getZeit() != null) ? String.valueOf(model.getZeit().getId()) : "");
			row.addCell((model.getZeitEinheit() != null) ? String.valueOf(model.getZeitEinheit()) : "");
			row.addCell((model.getKonzentration() != null) ? String.valueOf(model.getKonzentration().getId()) : "");
			row.addCell((model.getKonzEinheit() != null) ? String.valueOf(model.getKonzEinheit().getId()) : "");
//			row.addCell(String.valueOf(model.getKonzEinheit().getId()));
			row.addCell((model.getAw() != null) ? String.valueOf(model.getAw().getId()) : "");
			row.addCell((model.getPH() != null) ? String.valueOf(model.getPH().getId()) : "");
			row.addCell((model.getTemperatur() != null) ? String.valueOf(model.getTemperatur().getId()) : "");
			row.addCell((model.getCo2() != null) ? String.valueOf(model.getCo2().getId()) : "");
			row.addCell((model.getDruck() != null) ? String.valueOf(model.getDruck().getId()) : "");
			row.addCell(String.valueOf(model.getVersuchsbedingungen().getId()));
			rows.add(row);
		}
		CustomDataTable cdt = new CustomDataTable(getDataTableSpecMesswerte(), rows.toArray(new CustomDataRow[rows.size()]));
                
        return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
    }
    
    public static DataTableSpec getDataTableSpecMesswerte() {
        return new DataTableSpec(new String[]{"ID", "Zeit", "ZeitEinheit", "Konzentration", "Konz_Einheit", "aw", "pH", "Temperatur", "CO2", "Druck","Versuchsbedingungen"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE,StringCell.TYPE,StringCell.TYPE,StringCell.TYPE, StringCell.TYPE});        
    }
    
    public static BufferedDataTable createBufferedDataTableFromEinheiten(List<Einheiten> einheiten, ExecutionContext exec) throws CanceledExecutionException {
    	
//        String[] colnames = "ID|Einheit|Beschreibung".split("[|]");
        ArrayList<CustomDataRow> rows = new ArrayList<CustomDataRow>();
        for(int i=0; i < einheiten.size(); i++) {
            Einheiten model = einheiten.get(i);
			CustomDataRow row = new CustomDataRow(i);
			row.addCell(String.valueOf(model.getId()));
			row.addCell(model.getEinheit());
			row.addCell(model.getBeschreibung());
			rows.add(row);
        }
        CustomDataTable cdt = new CustomDataTable(getDataTableSpecEinheiten(), rows.toArray(new CustomDataRow[rows.size()]));
		return exec.createBufferedDataTable(cdt, new ExecutionMonitor());
    }
    
    public static DataTableSpec getDataTableSpecEinheiten() {
        return new DataTableSpec(new String[]{"ID", "Einheit", "Beschreibung"}, new DataType[]{StringCell.TYPE, StringCell.TYPE, StringCell.TYPE });        
    }
    
}
