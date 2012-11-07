package de.bund.bfr.knime.pmm.xml2table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.JDOMException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;


/**
 * This is the model implementation of XML2Table.
 * 
 *
 * @author BfR
 */
public class XML2TableNodeModel extends NodeModel {
    
	static final String CFGKEY_COLNAME = "SelectedColumn";
	static final String CFGKEY_APPENDDATA = "AppendDataBool";
	static final String CFGKEY_SELXMLENTRY = "SelectXMLEntry";

    private final SettingsModelString m_col = new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, "");
    private final SettingsModelBoolean m_append = new SettingsModelBoolean(XML2TableNodeModel.CFGKEY_APPENDDATA, true);
    private final SettingsModelStringArray m_xmlsel = new SettingsModelStringArray(XML2TableNodeModel.CFGKEY_SELXMLENTRY, null);
        

    /**
     * Constructor for the node model.
     */
    protected XML2TableNodeModel() {
        super(1, 1);
    }

    private DataTableSpec getOutSpec(BufferedDataTable data, String selColumn) throws IOException, JDOMException {
    	DataTableSpec inSpec = data.getDataTableSpec();
    	DataColumnSpec[] oldColSpecs = new DataColumnSpec[inSpec.getNumColumns()];
    	LinkedHashMap<String, DataColumnSpec> addColSpecs = new LinkedHashMap<String, DataColumnSpec>();
    	for (int i = 0; i < inSpec.getNumColumns(); i++) {
            DataColumnSpec cspec = inSpec.getColumnSpec(i);
    		oldColSpecs[i] = cspec;
            String colName = cspec.getName();
            if (colName.equals(selColumn) && cspec.getType().equals(XMLCell.TYPE)) {
                for (RowIterator it = data.iterator(); it.hasNext();) {
                    DataRow row = it.next();
                    DataCell cell = row.getCell(i);
                    if (!cell.isMissing()) {
                        String xml = ((XMLCell) cell).getStringValue();
                    	PmmXmlDoc doc = new PmmXmlDoc(xml);
                    	for (PmmXmlElementConvertable el : doc.getElementSet()) {
                    		if (el instanceof MiscXml) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                        			MiscXml mx = (MiscXml) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+mx.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+mx.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+mx.getName()+"_"+sarr[j], MiscXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = MiscXml.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, MiscXml.getDataType(element)).createSpec());                    					
                    				}
        		                	break;
                    			}
                    		}
                    		else if (el instanceof ParamXml) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                        			ParamXml px = (ParamXml) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+px.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+px.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+px.getName()+"_"+sarr[j], ParamXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = ParamXml.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, ParamXml.getDataType(element)).createSpec());                    					
                    				}
    		                        break;
                    			}
                    		}
                    		else if (el instanceof IndepXml) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                        			IndepXml ix = (IndepXml) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+ix.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+ix.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+ix.getName()+"_"+sarr[j], IndepXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = IndepXml.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, IndepXml.getDataType(element)).createSpec());                    					
                    				}
    		                        break;
                    			}
                    		}
                    		else if (el instanceof DepXml) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                    				DepXml dx = (DepXml) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+dx.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+dx.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+dx.getName()+"_"+sarr[j], DepXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = DepXml.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, DepXml.getDataType(element)).createSpec());                    					
                    				}
    		                        break;
                    			}
                    		}
                    		else if (el instanceof TimeSeriesXml) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                    				TimeSeriesXml tsx = (TimeSeriesXml) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+tsx.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+tsx.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+tsx.getName()+"_"+sarr[j], TimeSeriesXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = TimeSeriesXml.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, TimeSeriesXml.getDataType(element)).createSpec());                    					
                    				}
    		                        break;
                    			}
                    		}
                    		else if (el instanceof LiteratureItem) {
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                    				LiteratureItem li = (LiteratureItem) el;
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+li.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+li.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+li.getName()+"_"+sarr[j], LiteratureItem.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				List<String> list = LiteratureItem.getElements();
                    				for (String element : list) {
                        				addColSpecs.put(selColumn+"_"+element, new DataColumnSpecCreator(selColumn+"_"+element, LiteratureItem.getDataType(element)).createSpec());                    					
                    				}
    		                        break;
                    			}
                    		}
                    	}
                    }
                }
            }
        }
    	DataColumnSpec[] fullColSpecs;
    	if (m_append.getBooleanValue()) {
        	fullColSpecs = new DataColumnSpec[oldColSpecs.length + addColSpecs.size()];
        	for (int i=0;i<oldColSpecs.length;i++) {
        		fullColSpecs[i] = oldColSpecs[i];
        	}
        	int i=0;
        	for (DataColumnSpec colSpec : addColSpecs.values()) {
        		fullColSpecs[i+oldColSpecs.length] = colSpec;
        		i++;
        	}
    	}
    	else {
        	fullColSpecs = new DataColumnSpec[addColSpecs.size()];
        	int i=0;
        	for (DataColumnSpec colSpec : addColSpecs.values()) {
        		fullColSpecs[i] = colSpec;
        		i++;
        	}
    	}
    	return new DataTableSpec(fullColSpecs);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	BufferedDataTable data = inData[0];
    	if (data != null) {
    		DataTableSpec outSpec = getOutSpec(data, m_col.getStringValue());
            DataTableSpec inSpec = data.getDataTableSpec();
            DataCell[] cells = new DataCell[outSpec.getNumColumns()];
        	int count = 0;
        	BufferedDataContainer container = exec.createDataContainer(outSpec);
            for (RowIterator it = data.iterator(); it.hasNext();count++) {
            	List<LinkedHashMap<String, DataCell>> v = new ArrayList<LinkedHashMap<String, DataCell>>();
                DataRow row = it.next();
            	for (int i = 0; i < inSpec.getNumColumns(); i++) {
                    DataCell cell = row.getCell(i);
                    if (m_append.getBooleanValue()) cells[i] = cell;
	                DataColumnSpec cspec = inSpec.getColumnSpec(i);
	                String colName = cspec.getName();
	                if (colName.equals(m_col.getStringValue()) && cspec.getType().equals(XMLCell.TYPE)) {
	                	if (!cell.isMissing()) {
		                    String xml = ((XMLCell) cell).getStringValue();
		                	PmmXmlDoc doc = new PmmXmlDoc(xml);
		                	for (PmmXmlElementConvertable el : doc.getElementSet()) {
		                		LinkedHashMap<String, DataCell> addCells = new LinkedHashMap<String, DataCell>();
		                		if (el instanceof MiscXml) {
		                			MiscXml mx = (MiscXml) el;
		                			addCells.put("id", mx.getID() == null ? CellIO.createMissingCell() : new IntCell(mx.getID())); 
		                			addCells.put("name", mx.getName() == null ? CellIO.createMissingCell() : new StringCell(mx.getName())); 
		                			addCells.put("description", mx.getDescription() == null ? CellIO.createMissingCell() : new StringCell(mx.getDescription()));
		                			addCells.put("value", mx.getValue() == null ? CellIO.createMissingCell() : new DoubleCell(mx.getValue()));
		                			addCells.put("unit", mx.getUnit() == null ? CellIO.createMissingCell() : new StringCell(mx.getUnit()));
		                			v.add(addCells);
		                		}
		                		else if (el instanceof ParamXml) {
		                			ParamXml px = (ParamXml) el;
		                			addCells.put("name", px.getName() == null ? CellIO.createMissingCell() : new StringCell(px.getName())); 
		                			addCells.put("origname", px.getOrigName() == null ? CellIO.createMissingCell() : new StringCell(px.getOrigName())); 
		                			addCells.put("value", px.getValue() == null ? CellIO.createMissingCell() : new DoubleCell(px.getValue())); 
		                			addCells.put("error", px.getError() == null ? CellIO.createMissingCell() : new DoubleCell(px.getError()));
		                			addCells.put("min", px.getMin() == null ? CellIO.createMissingCell() : new DoubleCell(px.getMin()));
		                			addCells.put("max", px.getMax() == null ? CellIO.createMissingCell() : new DoubleCell(px.getMax()));
		                			addCells.put("p", px.getP() == null ? CellIO.createMissingCell() : new DoubleCell(px.getP()));
		                			addCells.put("t", px.gett() == null ? CellIO.createMissingCell() : new DoubleCell(px.gett()));
		                			v.add(addCells);
		                		}
		                		else if (el instanceof IndepXml) {
		                			IndepXml ix = (IndepXml) el;
		                			addCells.put("name", ix.getName() == null ? CellIO.createMissingCell() : new StringCell(ix.getName())); 
		                			addCells.put("origname", ix.getOrigName() == null ? CellIO.createMissingCell() : new StringCell(ix.getOrigName())); 
		                			addCells.put("min", ix.getMin() == null ? CellIO.createMissingCell() : new DoubleCell(ix.getMin()));
		                			addCells.put("max", ix.getMax() == null ? CellIO.createMissingCell() : new DoubleCell(ix.getMax()));
		                			v.add(addCells);
		                		}
		                		else if (el instanceof DepXml) {
		                			DepXml dx = (DepXml) el;
		                			addCells.put("name", dx.getName() == null ? CellIO.createMissingCell() : new StringCell(dx.getName())); 
		                			addCells.put("origname", dx.getOrigName() == null ? CellIO.createMissingCell() : new StringCell(dx.getOrigName())); 
		                			v.add(addCells);
		                		}
		                		else if (el instanceof TimeSeriesXml) {
		                			TimeSeriesXml tsx = (TimeSeriesXml) el;
		                			addCells.put("name", tsx.getName() == null ? CellIO.createMissingCell() : new StringCell(tsx.getName())); 
		                			addCells.put("time", tsx.getTime() == null ? CellIO.createMissingCell() : new DoubleCell(tsx.getTime()));
		                			addCells.put("log10c", tsx.getLog10C() == null ? CellIO.createMissingCell() : new DoubleCell(tsx.getLog10C()));
		                			v.add(addCells);
		                		}
		                		else if (el instanceof LiteratureItem) {
		                			LiteratureItem li = (LiteratureItem) el;
		                			addCells.put("id", li.getId() == null ? CellIO.createMissingCell() : new IntCell(li.getId()));
		                			addCells.put("author", li.getAuthor() == null ? CellIO.createMissingCell() : new StringCell(li.getAuthor()));
		                			addCells.put("year", li.getYear() == null ? CellIO.createMissingCell() : new IntCell(li.getYear()));
		                			addCells.put("title", li.getTitle() == null ? CellIO.createMissingCell() : new StringCell(li.getTitle()));
		                			addCells.put("abstract", li.getAbstract() == null ? CellIO.createMissingCell() : new StringCell(li.getAbstract()));
		                			v.add(addCells);
		                		}
		                	}
	                	}
                    }
                }
    			String[] sarr = m_xmlsel.getStringArrayValue();
    			if (sarr != null && sarr.length > 0) {
                	if (m_append.getBooleanValue()) {
                		for (int k=0;k<outSpec.getNumColumns()-inSpec.getNumColumns();k++) {
                			cells[k+inSpec.getNumColumns()] = CellIO.createMissingCell();
                		}
                    	for (LinkedHashMap<String, DataCell> addCells : v) {
            				for (int j=0;j<sarr.length;j++) {
            					for (int k=0;k<outSpec.getNumColumns()-inSpec.getNumColumns();k++) {
            						if (outSpec.getColumnNames()[k+inSpec.getNumColumns()].equalsIgnoreCase(
            								m_col.getStringValue()+"_"+addCells.get("name")+"_"+sarr[j])) {
            							cells[k+inSpec.getNumColumns()] = addCells.get(sarr[j].toLowerCase());
            							break;
            						}
            					}
            				}
                    	}
                        container.addRowToTable(new DefaultRow(row.getKey(), cells));
                	}
    			}
    			else {
                	int countResult = 0;
                	for (LinkedHashMap<String, DataCell> addCells : v) {
                		int i=0;
                        for (DataCell dataCell : addCells.values()) {
                        	if (m_append.getBooleanValue()) {
                            	cells[i+inSpec.getNumColumns()] = dataCell;
                        	}
                        	else {
                        		cells[i] = dataCell;
                        	}
                        	i++;
                        }
                        RowKey key = new RowKey(row.getKey().getString() + "." + countResult);
                        container.addRowToTable(new DefaultRow(key, cells));
                        countResult++;
                	}
    			}

                exec.checkCanceled();
                exec.setProgress(count / (double)data.getRowCount(), "Adding row " + count);
            }
            if (container == null) return null;
            container.close();
            BufferedDataTable out = container.getTable();
            return new BufferedDataTable[]{out};
    	}
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	// Spec at output is not known in advance. It depends on the XML.
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_col.saveSettingsTo(settings);
    	m_append.saveSettingsTo(settings);
    	m_xmlsel.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.loadSettingsFrom(settings);
    	m_append.loadSettingsFrom(settings);
    	m_xmlsel.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.validateSettings(settings);
    	m_append.validateSettings(settings);
    	m_xmlsel.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}

