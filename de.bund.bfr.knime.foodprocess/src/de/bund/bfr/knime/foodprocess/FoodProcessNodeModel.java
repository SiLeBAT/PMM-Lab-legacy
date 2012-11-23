/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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
package de.bund.bfr.knime.foodprocess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.jfree.data.xy.XYSeries;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.lib.OutPortSetting;
import de.bund.bfr.knime.foodprocess.lib.ParametersSetting;
import de.bund.bfr.knime.foodprocess.ui.MyChartDialog;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pcml.port.PCMLUtil;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.pcml10.ColumnDocument.Column;
import de.bund.bfr.pcml10.ColumnListDocument.ColumnList;
import de.bund.bfr.pcml10.DataTableDocument.DataTable;
import de.bund.bfr.pcml10.InlineTableDocument.InlineTable;
import de.bund.bfr.pcml10.InportDocument.Inport;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.OutportDocument.Outport;
import de.bund.bfr.pcml10.OutportRefDocument.OutportRef;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.PCMLDocument.PCML;
import de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.ProcessNodeType;
import de.bund.bfr.pcml10.ProcessParameters;
import de.bund.bfr.pcml10.RowDocument.Row;

/**
 * This is the model implementation of FoodProcess.
 * 
 *
 * @author Jorgen Brandt
 */
public class FoodProcessNodeModel extends NodeModel {
	public static final int N_PORT_IN = 4;
	public static final int N_PORT_OUT = 4;
	private FoodProcessNodeSettings settings;
	
	/**
     * Constructor for the node model.
     */
    protected FoodProcessNodeModel() {    	
        super(new PortType[] {
        	new PortType(PCMLPortObject.class, false),
        	new PortType(PCMLPortObject.class, true),
        	new PortType(PCMLPortObject.class, true),
        	new PortType(PCMLPortObject.class, true),
        }, new PortType[] {
        	new PortType(PCMLPortObject.class, false),
        	new PortType(PCMLPortObject.class, false),
        	new PortType(PCMLPortObject.class, false),
        	new PortType(PCMLPortObject.class, false),
        });
        settings = new FoodProcessNodeSettings();
    }


    /**
     * {@inheritDoc}
     */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
        PortObjectSpec[] outSpecs = new PortObjectSpec[N_PORT_OUT];
        for (int i = 0; i < N_PORT_OUT; i++) {
            outSpecs[i] = createPCMLSpec(i, inSpecs);
        }
		FoodProcessSetting fps = settings.getFoodProcessSetting();
		if (fps.getStepWidth() == null || fps.getDuration() == null) {
			setWarningMessage("step-width and duration are essential parameters for meaningful computations.");			
		}
        return outSpecs;
    }    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, 
    		final ExecutionContext exec) {

        PCMLDocument pcmlDoc = PCMLUtil.merge(inObjects);

        PortObjectSpec[] inSpecs = new PortObjectSpec[N_PORT_IN];
    	PCMLPortObjectSpec[] outSpecs =  new PCMLPortObjectSpec[N_PORT_OUT];
    	for (int i = 0; i < N_PORT_IN; i++) {
    		inSpecs[i] = inObjects[i] == null ? null : inObjects[i].getSpec();
    	}
    	for (int i = 0; i < N_PORT_OUT; i++) {
    		outSpecs[i] = createPCMLSpec(i, inSpecs);
    	}
    	
        // Create a processNode
    	PCML pcml = pcmlDoc.getPCML();
        ProcessNode processNode = pcml.getProcessChain().addNewProcessNode();
        String processNodeID = UUID.randomUUID().toString();
        processNode.setId(processNodeID);
        processNode.setType(ProcessNodeType.PROCESSING);
		FoodProcessSetting fps = settings.getFoodProcessSetting();
		ParametersSetting ps = fps.getParametersSetting();
		NameAndDatabaseId process = processNode.addNewProcess();
		process.setName(fps.getProcessName());
		// parameters of the process
		// firstly assume: same Unit!
		ProcessParameters param = processNode.addNewParameters();
		if (fps.getCapacity() != null) {
			param.setCapacity(fps.getCapacity());
		}
		if (fps.getDuration() != null) {
			param.setDuration(fps.getDuration());
		}
		if (fps.getStepWidth() != null) {
			param.setStepWidth(fps.getStepWidth());
		}
		param.setTemperature(ps.getTemperature());
		param.setAw(ps.getAw());
		param.setPH(ps.getPh());
		param.setPressure(ps.getPressure());
        
        // Set inports
        for (int i = 0; i < inObjects.length; i++) {
        	PortObject inObject = inObjects[i];
            if (null != inObject) {
                PCMLPortObject in = (PCMLPortObject)inObject;               
                Inport inport = processNode.addNewInport();
                OutportRef outRef = inport.addNewOutportRef();
                outRef.setRef(in.getParentId());
                outRef.setOutIndex(i);
            }
        }
        
        // calculate the universal time for each ProcessNode relative to this node!
		Map<String, ProcessNode> processNodes = createProcessNodeMap(pcmlDoc);		
		Map<String, ProcessData> processChainData = createProcessChainDataMap(pcmlDoc);		
		calculateNewTimes(processNodes, processChainData, processNode, 0.0);
        
		// the process chain data
		ProcessChainData pcData = pcml.getProcessChainData();//.addNewProcessChainData();
		ProcessData p1Data = pcData.addNewProcessData();
		p1Data.setRef(processNodeID);

		p1Data.setTime(0.0); // absolute time at process start is always zero. Previous nodes have negative timestamps
		DataTable table = p1Data.addNewDataTable();
		ColumnList columnList = table.addNewColumnList();
		// Column Temperature
		Column temperature = columnList.addNewColumn();
		temperature.addNewColumnId().setName(TimeSeriesSchema.ATT_TEMPERATURE);
		QName tempCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c1");
		temperature.setName(tempCol.getLocalPart());
		// Column pH
		Column ph = columnList.addNewColumn();
		ph.addNewColumnId().setName(TimeSeriesSchema.ATT_PH);
		QName phCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c2");
		ph.setName(phCol.getLocalPart());
		// Column aw
		Column aw = columnList.addNewColumn();
		aw.addNewColumnId().setName(TimeSeriesSchema.ATT_WATERACTIVITY);
		QName awCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c3");
		aw.setName(awCol.getLocalPart());
		// Column pressure
		Column pressure = columnList.addNewColumn();
		pressure.addNewColumnId().setName("pressure");
		QName pressureCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c4");
		pressure.setName(pressureCol.getLocalPart());
		// Columns Matrices
		Map<Matrix, Double> matrices = outSpecs[0].getMatrices();
		Map<QName, Double> matrixCols = new HashMap<QName, Double>();
		int index = 5;
        for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
			Column matrix = columnList.addNewColumn();
			NameAndDatabaseId nadbid = matrix.addNewMatrix();
			nadbid.setName(entry.getKey().getName());
			nadbid.setDbId(entry.getKey().getId());
			QName matrixCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c" + index);
			matrix.setName(matrixCol.getLocalPart());
			matrixCols.put(matrixCol, entry.getValue());
			index++;
        }
		// Columns Agents
		Map<Agent, Double> agents = outSpecs[0].getAgents();
		Map<QName, Double> agentCols = new HashMap<QName, Double>();
        for (Map.Entry<Agent, Double> entry : agents.entrySet()) {
			Column agent = columnList.addNewColumn();
			NameAndDatabaseId nadbid = agent.addNewAgent();
			nadbid.setName(entry.getKey().getName());
			nadbid.setDbId(entry.getKey().getId());
			QName agentCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c" + index);
			agent.setName(agentCol.getLocalPart());
			agentCols.put(agentCol, entry.getValue());
			index++;
        }
		
        
        InlineTable inlineTable = table.addNewInlineTable();
        
        // exec Table
		MyChartDialog mcd = new MyChartDialog();
		if (fps.getStepWidth() != null && fps.getDuration() != null) {
			XYSeries xy_T = mcd.getSerie(ps.getTemperature(), fps.getStepWidth(), fps.getDuration(), fps.getStepWidth());
			XYSeries xy_ph = mcd.getSerie(ps.getPh(), fps.getStepWidth(), fps.getDuration(), fps.getStepWidth());
			XYSeries xy_aw = mcd.getSerie(ps.getAw(), fps.getStepWidth(), fps.getDuration(), fps.getStepWidth());
			XYSeries xy_pres = mcd.getSerie(ps.getPressure(), fps.getStepWidth(), fps.getDuration(), fps.getStepWidth());
			/*
			StringBuffer outTable = new StringBuffer("t\tTemp\tpH\taw\tPressure");
			Map<Matrix, Double> matrices = outSpecs[0].getMatrices();
	        for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
				outTable.append("\t" + entry.getKey().getName());
	        }
			Map<Agent, Double> agents = outSpecs[0].getAgents();
	        for (Map.Entry<Agent, Double> entry : agents.entrySet()) {
				outTable.append("\t" + entry.getKey().getName());
	        }
*/
			for (double t=fps.getStepWidth();t<=fps.getDuration();t+=fps.getStepWidth()) {
				Row row = inlineTable.addNewRow();
				XmlCursor cur = row.newCursor();
				cur.toFirstContentToken();
				Double val = getYVal(xy_T, t);
				if (val == null) {
					cur.insertElement(tempCol);
				} else {
					cur.insertElementWithText(tempCol, val.toString());
				}
				val = getYVal(xy_ph, t);
				if (val == null) {
					cur.insertElement(phCol);
				} else {
					cur.insertElementWithText(phCol, val.toString());
				}
				val = getYVal(xy_aw, t);
				if (val == null) {
					cur.insertElement(awCol);
				} else {
					cur.insertElementWithText(awCol, val.toString());
				}
				val = getYVal(xy_pres, t);
				if (val == null) {
					cur.insertElement(pressureCol);
				} else {
					cur.insertElementWithText(pressureCol, val.toString());
				}
				/*
				outTable.append("\n" + t);
				outTable.append("\t" + getYVal(xy_T, t)); // °C
				outTable.append("\t" + getYVal(xy_ph, t)); // pH
				outTable.append("\t" + getYVal(xy_aw, t)); // aw
				outTable.append("\t" + getYVal(xy_pres, t)); // Pressure
				*/
		        for (Map.Entry<QName, Double> entry : matrixCols.entrySet()) {
					cur.insertElementWithText(entry.getKey(), "" + (outSpecs[0].getVolume() * entry.getValue()));
					//outTable.append("\t" + (outSpecs[0].getVolume() * entry.getValue()));
		        }
		        for (Map.Entry<QName, Double> entry : agentCols.entrySet()) {
					cur.insertElementWithText(entry.getKey(), "" + entry.getValue());
					//outTable.append("\t" + entry.getValue());
		        }
				cur.dispose();
			}
			//System.out.println(outTable);			
		}
		else {
			// Invalid configuration
		}
		
        // Set outports
        PortObject[] out = new PCMLPortObject[N_PORT_OUT];
        for (int i = 0; i < N_PORT_OUT; i++) {
            Outport outport = PCMLUtil.addOutport(processNode, outSpecs[i]);
            if (outSpecs[i].isUsed()) {
                outport.setVolume(outSpecs[i].getVolume().toString());
                /*
        		Map<Matrix, Double> matrices = outSpecs[0].getMatrices();
                for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
            		MatrixRecipe mRecipe = outport.addNewMatrixRecipe();
            		MatrixIncredient mIncredient = mRecipe.addNewMatrixIncredient();
            		NameAndDatabaseId matrix = mIncredient.addNewMatrix();
            		matrix.setName(entry.getKey().getName());
            		matrix.setDbId(entry.getKey().getId());  
            		mIncredient.setFraction(entry.getValue());
                }
        		Map<Agent, Double> agents = outSpecs[0].getAgents();
                for (Map.Entry<Agent, Double> entry : agents.entrySet()) {
            		AgentRecipe mRecipe = outport.addNewAgentRecipe();
            		AgentIncredient mIncredient = mRecipe.addNewAgentIncredient();
            		NameAndDatabaseId agent = mIncredient.addNewAgent();
            		agent.setName(entry.getKey().getName());
            		agent.setDbId(entry.getKey().getId());  
            		mIncredient.setQuantity()(entry.getValue());
                }
                */
            }
            else {
                outport.setVolume("0.0");
            }
            //out[i] = PCMLPortObject.create(pcmlDoc.toString());
            out[i] = new PCMLPortObject(pcmlDoc, processNodeID, i, outSpecs[i]);
        }
/*        
        // Set outports     
        for (int i = 0; i < N_PORT_OUT; i++) {
            out[i] = new PCMLPortObject(pcmlDoc, processNodeID, i, outSpecs[i]);
        }
 */   	

        
        return out;
    }
	/** Create a list of all process nodes for fast access. This is map of the
	 * process nodes id to the object itself.
	 */
	private Map<String, ProcessNode> createProcessNodeMap(final PCMLDocument pcmlDoc) {
		Map<String, ProcessNode> processNodes = new HashMap<String, ProcessNode>();
		
		for (ProcessNode processNode : pcmlDoc.getPCML().getProcessChain().getProcessNodeArray()) {
			processNodes.put(processNode.getId(), processNode);
		}
		
		return processNodes;
	}
	private void calculateNewTimes(final Map<String, ProcessNode> processNodes, final Map<String, ProcessData> processChainData,
			final ProcessNode processNode, final Double time) {
		Inport[] in = processNode.getInportArray();
		for (int i = 0; i < in.length; i++) {
			OutportRef opr = in[i].getOutportRef();
			ProcessNode pn = processNodes.get(opr.getRef());
			ProcessData pd = processChainData.get(opr.getRef());
			if (pd == null) {
				System.err.println("WW");
			}
			else {
				Double newTime = time  - pn.getParameters().getDuration();
				pd.setTime(newTime);
				calculateNewTimes(processNodes, processChainData, pn, newTime);
			}
		}		
	}
	/** Create a list of all process nodes for fast access. This is map of the
	 * process nodes id to the object itself.
	 */
	private Map<String, ProcessData> createProcessChainDataMap(final PCMLDocument pcmlDoc) {
		Map<String, ProcessData> processChainData = new HashMap<String, ProcessData>();
		
		for (ProcessData pcd : pcmlDoc.getPCML().getProcessChainData().getProcessDataArray()) {
			processChainData.put(pcd.getRef(), pcd);
		}
		
		return processChainData;
	}
    private Double getYVal(final XYSeries xy, final double xVal) {
    	Double result = null;
    	if (xy != null) {
        	for (int i=0;i<xy.getItemCount();i++) {
        		if (xy.getDataItem(i).getXValue() == xVal) {
        			result = xy.getDataItem(i).getYValue();
        				break;
        		}
        	}    		
    	}
    	return result;
    }

    private PCMLPortObjectSpec createPCMLSpec(final int outIndex, final PortObjectSpec[] inSpecs) {
		FoodProcessSetting fps = settings.getFoodProcessSetting();
		OutPortSetting[] ops = fps.getOutPortSetting();
		// erst einmal erhalten alle OutPorts denselben Mix - im Nachgang für jeden OutPORT INDIVIDUELL MACHEN, "Expert mode"

		HashMap<Matrix, Double> newMatrixMix = new HashMap<Matrix, Double>();
		HashMap<Agent, Double> newAgentMix = new HashMap<Agent, Double>();
		Double newVolume = null, newTemperature = null, newpH = null, newAw = null, newPressure = null;
		Double tVol;
		
		// firstly: calculate the new volume
		for (int i=0;i<N_PORT_IN;i++) {
			if (inSpecs[i] != null) {
				PCMLPortObjectSpec inSpec = (PCMLPortObjectSpec) inSpecs[i];
				tVol = inSpec.getVolume();
				if (tVol != null) {
					if (newVolume == null) {
						newVolume = 0.0;
					}
					newVolume += tVol;
				}
			}
		}
		
		// secondly: calculate the other parameters
		for (int i=0;i<N_PORT_IN;i++) {
			if (inSpecs[i] != null) {
				PCMLPortObjectSpec inSpec = (PCMLPortObjectSpec) inSpecs[i];
				// Volume is obligatory!
				if (inSpec.getVolume() != null) {
					
					// calculate matrices, take care of recurrences
					Map<Matrix, Double> matrices = inSpec.getMatrices();
			        for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
						double newFraction = entry.getValue() * inSpec.getVolume() / newVolume;
						if (newMatrixMix.containsKey(entry.getKey())) {
							newFraction += newMatrixMix.get(entry.getKey());
							newMatrixMix.remove(entry.getKey());
						}
						newMatrixMix.put(entry.getKey(), newFraction);
			        }
					
					// calculate agents, take care of recurrences
					Map<Agent, Double> agents = inSpec.getAgents();
			        for (Map.Entry<Agent, Double> entry : agents.entrySet()) {
						double newQuantity = entry.getValue() * inSpec.getVolume() / newVolume;
						if (newAgentMix.containsKey(entry.getKey())) {
							newQuantity += newAgentMix.get(entry.getKey());
							newAgentMix.remove(entry.getKey());
						}
						newAgentMix.put(entry.getKey(), newQuantity);
			        }
					
					// calculate temperature -> simply calculate the mean value, weighted by the volume
					newAw = calculateWeightedMean(
							newTemperature,
							inSpec.getTemperature(),
							inSpec.getVolume(),
							newVolume);
					
					// calculate pH -> simply calculate the mean value, weighted by the volume
					newAw = calculateWeightedMean(
							newpH,
							inSpec.getPH_value(),
							inSpec.getVolume(),
							newVolume);
					
					// calculate aw -> simply calculate the mean value, weighted by the volume
					newAw = calculateWeightedMean(
							newAw,
							inSpec.getAw_value(),
							inSpec.getVolume(),
							newVolume);
					
					// calculate pressure -> simply calculate the mean value, weighted by the volume
					newPressure = calculateWeightedMean(
							newPressure,
							inSpec.getPressure(),
							inSpec.getVolume(),
							newVolume);
				}
			}
		}
		
		PCMLPortObjectSpec outSpec;
		// take care of unused Outports (OutFlux = 0 or undefined)
		if (ops[outIndex].getOutFlux() == null || ops[outIndex].getOutFlux() == 0) {
			outSpec = new PCMLPortObjectSpec();
		}
		else {
			double outFluxSum = 0;
			for (int i=0;i<N_PORT_OUT;i++) {
				outFluxSum += ops[i].getOutFlux() == null ? 0 : ops[i].getOutFlux();
			}
			outSpec = new PCMLPortObjectSpec(newMatrixMix,
					newAgentMix,
					newVolume == null ? null : newVolume * ops[outIndex].getOutFlux() / outFluxSum,
					newTemperature,
					newPressure,
					newpH,
					newAw);
		}
		return outSpec;
    }
    private Double calculateWeightedMean(Double sum, final Double newVal, final Double partVol, final Double totalVol) {
		if (newVal != null) {
			if (sum == null) {
				sum = 0.0;
			}
			sum += newVal * partVol / totalVol;
		}    	
		return sum;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	// no internals, nothing to reset
    }

	public void setSetting( final FoodProcessNodeSettings settings ) {		
		this.settings = settings;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         this.settings.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.settings.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	FoodProcessNodeSettings s = new FoodProcessNodeSettings();
        s.loadSettings(settings); // kann InvalidSettingsException werfen
        // TODO: Check settings if not already done by the settings object in the previous line.
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to load
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to save
    }

}

