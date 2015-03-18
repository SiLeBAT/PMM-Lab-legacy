package de.bund.bfr.knime.pmm.js.modelplotter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
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

import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.js.modelplotter.ModelPlotterViewRepresentation.Variable;
import de.bund.bfr.knime.pmm.predictorview.TableReader;

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
	
	private static final String PMM_MODEL_ARG_TIME = "Time";
	private static final String PMM_MODEL_ARG_TEMP = "temp";
	private static final String PMM_MODEL_ARG_AW = "aw";
	private static final String PMM_MODEL_ARG_CO2 = "CO2";
	private static final String PMM_MODEL_ARG_PS = "PhysiologicalState";
	private static final String PMM_MODEL_ARG_PH = "pH";
	
	private final ModelPlotterViewConfig m_config;
	
	
    /**
     * Constructor of {@code ModelPlotterNodeModel}.
     */
    protected ModelPlotterNodeModel() {
		super(new PortType[]{ BufferedDataTable.TYPE }, new PortType[]{ });
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
	public void loadViewValue(ModelPlotterViewValue viewContent, boolean useAsDefault) {

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
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.knime.js.core.node.AbstractWizardNodeModel#performExecute(org.knime.core.node.port.PortObject[], org.knime.core.node.ExecutionContext)
	 */
	@Override
	protected PortObject[] performExecute(PortObject[] inObjects,
			ExecutionContext exec) throws Exception {
		
		BufferedDataTable table = (BufferedDataTable)inObjects[0];
		TableReader reader = new TableReader(
				ModelPlotterNodeModel.getTuples(table),
				new LinkedHashMap<String, String>(),
				new LinkedHashMap<String, String>(), true);
		
		// read all plotables
		Map<String, Plotable> plotables = reader.getPlotables();
		Plotable p;
		
		// warn if more than one plotables
		if (plotables.size() <= 0) {
			setWarningMessage("No model functions to plot in input data table!");
			LOGGER.error("No model functions to plot in input data table!");
			throw new IllegalStateException("No model functions to plot in input data table!");
		} else if (plotables.size() > 1) {
			setWarningMessage("More than one model function to plot in input data table."
					+ " Plotting first function only.");
		}
		
		// get first plotable
		p = plotables.entrySet().iterator().next().getValue();
		
		ModelPlotterViewRepresentation vR = getViewRepresentation();
		if (vR == null) {
			vR = createEmptyViewRepresentation();
			setViewRepresentation(vR);
		}
		
		// CONFIG of JavaScript view
		vR.setChartTitle(m_config.getChartTitle());
		vR.setY0(m_config.getY0());
		
		// DATA: specify function (substring after '=')
		vR.setFunc(p.getFunction().substring(p.getFunction().indexOf("=") + 1));

		// DATA: specify arguments that can be adjusted via sliders in JavaScript view
		List<Variable> variables = new LinkedList<>();	
		Map<String, List<Double>> args = p.getFunctionArguments();
		for (Map.Entry<String, List<Double>> a : args.entrySet()) {
			// ignore time argument
			if (!a.getKey().equals(PMM_MODEL_ARG_TIME)) {
				Variable v = new Variable();
				v.setName(a.getKey());
				
				// set min value
				Double min = p.getMinArguments().get(a.getKey());
				if (min == null) {
					min = 0.0;
				}
				v.setMin(min);
				
				// set max value
				Double max = p.getMaxArguments().get(a.getKey());
				if (max == null) {
					max = 0.0;
				}
				v.setMax(max);
				
				// set default value (different for each argument)
				if (a.getKey().equals(PMM_MODEL_ARG_AW)) {
					v.setDef(0.997);
				}
				else if (a.getKey().equals(PMM_MODEL_ARG_CO2)) {
					v.setDef(0);
				}
				else if (a.getKey().equals(PMM_MODEL_ARG_PH)) {
					v.setDef(7.0);
				}
				else if (a.getKey().equals(PMM_MODEL_ARG_PS)) {
					v.setDef(0.0005);
				}
				else if (a.getKey().equals(PMM_MODEL_ARG_TEMP)) {
					v.setDef(20);
				}
				
				// add variable
				variables.add(v);
			}
		}
		vR.setVariables(variables);
		
		// DATA: specify constants and values
		Map<String, Double> constants = new HashMap<>();	
		Map<String, Double> params = p.getFunctionParameters();
		for (Map.Entry<String, Double> param : params.entrySet()) {
			Double val = param.getValue();
			if (val == null) {
				val = 0.0;
			}
			constants.put(param.getKey(), val);
		}
		vR.setConstants(constants);
		
		setViewRepresentation(vR);
		
		exec.setProgress(1);
		return null;
	}

	@Override
	protected void performReset() {
		// Nothing to do.
	}

	@Override
	protected String getInteractiveViewName() {
		return (new ModelPlotterNodeFactory()).getInteractiveViewName();
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// Nothing to do.
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
