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
package de.bund.bfr.knime.pmm.js.modelplotter;

import java.util.List;
import java.util.Random;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.editor.ModelEditorNodeModel;
import de.bund.bfr.knime.pmm.js.common.Model1DataTuple;
import de.bund.bfr.knime.pmm.js.common.ModelList;

/**
 * Model Plotter node model.
 * Reading all plotables functions of input table and preparing the first plotable for 
 * JavaScript view. 
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeModel extends AbstractWizardNodeModel<ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(ModelPlotterNodeModel.class);
	
	static final String FLOWVAR_FUNCTION_ORIG = "Original Function";
	static final String FLOWVAR_FUNCTION_FULL = "Full Function";
	static final String FLOWVAR_FUNCTION_APPLIED = "Applied Function";
	/*
	 * deprecated
	private static final String PMM_MODEL_ARG_TIME = "Time";
	private static final String PMM_MODEL_ARG_TEMP = "temp";
	private static final String PMM_MODEL_ARG_AW = "aw";
	private static final String PMM_MODEL_ARG_CO2 = "CO2";
	private static final String PMM_MODEL_ARG_PS = "PhysiologicalState";
	private static final String PMM_MODEL_ARG_PH = "pH";
	 */
	
	private final ModelPlotterViewConfig m_config;
	
	private boolean m_executed = false;
	
    /**
     * Constructor of {@code ModelPlotterNodeModel}.
     */
    protected ModelPlotterNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, 
			  new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE }, 
			  (new ModelPlotterNodeFactory()).getInteractiveViewName());
        m_config = new ModelPlotterViewConfig();
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public ModelPlotterViewRepresentation createEmptyViewRepresentation() {
        return new ModelPlotterViewRepresentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelPlotterViewValue createEmptyViewValue() {
        return new ModelPlotterViewValue();
    }

	@Override
	public String getJavascriptObjectID() {
		return "org.bund.bfr.knime.pmm.js.modelplotter";
	}

	@Override
	public boolean isHideInWizard() {
		return m_config.getHideInwizard();
	}

	@Override
	public ValidationError validateViewValue(ModelPlotterViewValue viewContent) {
        synchronized (getLock()) {
            // Nothing to do.
        }
        return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
		// Nothing to do.
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (!SchemaFactory.createM1Schema()
				.conforms((DataTableSpec) inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input!");
		}
		
		return createOutputDataTableSpecs();
	}
	
	/* (non-Javadoc)
	 * @see org.knime.js.core.node.AbstractWizardNodeModel#performExecute(org.knime.core.node.port.PortObject[], org.knime.core.node.ExecutionContext)
	 */
	@Override
	protected PortObject[] performExecute(PortObject[] inObjects,
			ExecutionContext exec) throws Exception {
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		List<KnimeTuple> tuples = PmmUtilities.getTuples(table, SchemaFactory.createM1DataSchema());

		ModelPlotterViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (!m_executed) {
			// Config of JavaScript view
			// viewValue.setModels(m_config.getModels());
			viewValue.setY0(m_config.getY0());
			viewValue.setMinXAxis(m_config.getMinXAxis());
			viewValue.setMinYAxis(m_config.getMinYAxis());
			viewValue.setMaxXAxis(m_config.getMaxXAxis());
			viewValue.setMaxYAxis(m_config.getMaxYAxis());
	
			// Convert KNIME tuples to Model1DataTuple
			Model1DataTuple[] m1DataTuples = new Model1DataTuple[tuples.size()];
			for (int i = 0; i < tuples.size(); i++) {
				m1DataTuples[i] = ModelEditorNodeModel.codeTuple(tuples.get(i));
				
				// as long as there is no dbuuid, we generate one
				if(m1DataTuples[i].getDbuuid().isEmpty() || m1DataTuples[i].getDbuuid().equals("?"))
				{
					LOGGER.warn("DATA PROBLEM: No dbuuid given. Random ID will be generated.");
					int seed = m1DataTuples[i].getCatModel().getFormula().hashCode();
					String id = "g" + String.valueOf((new Random(seed)).nextInt(999999)); // "g" for "generated", max 6 digits
					m1DataTuples[i].setDbuuid(id);
				}
			}
			ModelList modelList = new ModelList();
			modelList.setModels(m1DataTuples);
			viewValue.setModels(modelList);

			setViewValue(viewValue);
			m_executed = true;
		}

		exec.setProgress(1);

		// return edited table
		BufferedDataContainer container = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
		ModelList outModelList = getViewValue().getModels();
		for (Model1DataTuple m1DataTuple : outModelList.getModels()) {
			KnimeTuple outTuple = ModelEditorNodeModel.decodeTuple(m1DataTuple);
			container.addRowToTable(outTuple);
		}
		container.close();
		// TODO: finish output
		return new BufferedDataTable[] { container.getTable(), container.getTable() };
	}

	private DataTableSpec[] createOutputDataTableSpecs() {
		return new DataTableSpec[]{ 
			SchemaFactory.createM1DataSchema().createSpec(), 
			SchemaFactory.createM1DataSchema().createSpec() 
		};		
	}
	
	/*
	 * private BufferedDataTable[] createOutputDataTables(final ExecutionContext exec) {
		ModelPlotterViewValue value = getViewValue();
		DataTableSpec[] outSpces = createOutputDataTableSpecs();
		
		BufferedDataContainer constBC = exec.createDataContainer(outSpces[0]);
		long i = 0;
		if (value.getConstants() != null) {
			for (Entry<String, Double> e : value.getConstants().entrySet()) {
				RowKey key = RowKey.createRowKey(i);
				constBC.addRowToTable(new DefaultRow(key, new StringCell(e
						.getKey()), new DoubleCell(e.getValue())));
				i++;
			}
		}
		constBC.close();
		
		BufferedDataContainer varBC = exec.createDataContainer(outSpces[1]);
		i = 0;
		if (value.getVariables() != null) {
			for (Variable v : value.getVariables()) {
				RowKey key = RowKey.createRowKey(i);
				varBC.addRowToTable(new DefaultRow(key, new StringCell(v
						.getName()), new DoubleCell(v.getDef()),
						new DoubleCell(v.getMin()), new DoubleCell(v.getMax())));
				i++;
			}
		}
		varBC.close();
		
		return new BufferedDataTable[]{ constBC.getTable(), varBC.getTable() };
	}
	*/
	
	@Override
	protected void performReset() {
		m_executed = false;
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// save value #getViewValue() as config for default values after restart 
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		new ModelPlotterViewConfig().loadSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}
	
	private static List<KnimeTuple> getTuples(DataTable table) {
		boolean isTertiaryModel = SchemaFactory.createM12Schema().conforms(
				table);
		boolean containsData = SchemaFactory.createDataSchema().conforms(table);

		if (isTertiaryModel) {
			if (containsData) {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM12DataSchema());
			} else {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM12Schema());
			}
		} else {
			if (containsData) {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM1DataSchema());
			} else {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM1Schema());
			}
		}
	}   	
}
