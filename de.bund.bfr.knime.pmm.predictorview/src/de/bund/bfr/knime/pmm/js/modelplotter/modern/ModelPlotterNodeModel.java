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
package de.bund.bfr.knime.pmm.js.modelplotter.modern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractSVGWizardNodeModel;

import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.editor.ModelEditorNodeModel;
import de.bund.bfr.knime.pmm.js.common.Model1DataTuple;
import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.Unit;
import de.bund.bfr.knime.pmm.js.common.UnitList;

/**
 * Model Plotter node model.
 * Reading all plotables functions of input table and preparing the first plotable for 
 * JavaScript view. 
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 * @author Markus Freitag, EITCO GmbH, Berlin, Germany
 *
 */
public final class ModelPlotterNodeModel extends AbstractSVGWizardNodeModel<ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(ModelPlotterNodeModel.class);
	
	static final String FLOWVAR_FUNCTION_ORIG = "Original Function";
	static final String FLOWVAR_FUNCTION_FULL = "Full Function";
	static final String FLOWVAR_FUNCTION_APPLIED = "Applied Function";
	static final String AUTHORS = "authors";
	static final String REPORT_NAME = "reportName";
	static final String COMMENT = "comments";
	static final String SVG_PLOT = "svgPlot";
	
	private final ModelPlotterViewConfig m_config;
	private boolean m_executed = false;
	
    /**
     * Constructor of {@code ModelPlotterNodeModel}.
     */
    protected ModelPlotterNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, 
			  new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE, ImagePortObject.TYPE }, 
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
		return "de.bund.bfr.knime.pmm.js.modelplotter.modern";
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
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
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
	protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView, PortObject[] inObjects,
			ExecutionContext exec) throws Exception {
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		List<KnimeTuple> tuples = getTuples(table);

		ModelPlotterViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (!m_executed) {
			// Config of JavaScript view
			viewValue.setY0(m_config.getY0());
			viewValue.setMinXAxis(m_config.getMinXAxis());
			viewValue.setMinYAxis(m_config.getMinYAxis());
			viewValue.setMaxXAxis(m_config.getMaxXAxis());
			viewValue.setMaxYAxis(m_config.getMaxYAxis());
	
			// Convert KNIME tuples to Model1DataTuple
			Model1DataTuple[] dataTuples = new Model1DataTuple[tuples.size()];
			for (int i = 0; i < tuples.size(); i++) {
				dataTuples[i] = ModelEditorNodeModel.codeTuple(tuples.get(i));
				// as long as there is no dbuuid, we generate one
				if(dataTuples[i].getCondId() == null)
				{
					LOGGER.warn("DATA PROBLEM: No dbuuid given. Random ID will be generated.");
					int seed;
					if(dataTuples[i].getCatModel() != null)
						seed = dataTuples[i].hashCode();
					else
						seed = dataTuples[i].getCatModel().getFormula().hashCode();
					String globalId = "g" + String.valueOf((new Random(seed)).nextInt(999999)); // "g" for "generated", max 6 digits
					dataTuples[i].setDbuuid(globalId);
				}
			}
			ModelList modelList = new ModelList();
			modelList.setModels(dataTuples);
			viewValue.setModels(modelList);
			
			// create UnitList from DBUnits
			// (this way we can use the units known to the DB and do not have to implement extra JSONType declarations)
			ArrayList<Unit> tempUnitList = new ArrayList<Unit>();
			for (UnitsFromDB dbUnit : DBUnits.getDBUnits().values()) {
				Unit newUnit = new Unit();
				// only copy attributes that are used
				newUnit.setDisplayInGuiAs(dbUnit.getDisplay_in_GUI_as());
				newUnit.setConversionFunctionFactor(dbUnit.getConversion_function_factor());
				newUnit.setInverseConversionFunctionFactor(dbUnit.getInverse_conversion_function_factor());
				newUnit.setName(dbUnit.getName());
				newUnit.setUnit(dbUnit.getUnit());
				
				tempUnitList.add(newUnit);
			}
			UnitList unitList = new UnitList();
			unitList.setUnits((Unit[])tempUnitList.toArray(new Unit[0]));
			// make list available to view
			viewValue.setUnits(unitList);

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
		
		BufferedDataContainer userContainer = exec.createDataContainer(getUserSpec());
		String reportName = getViewValue().getReportName();
		String authors = getViewValue().getAuthors();
		String comment = getViewValue().getComments();
		String svgPlot = getViewValue().getSVGPlot();
		
		KnimeSchema userSchema = new KnimeSchema();
		userSchema.addStringAttribute(REPORT_NAME);
		userSchema.addStringAttribute(AUTHORS);
		userSchema.addStringAttribute(COMMENT);
		userSchema.addStringAttribute(SVG_PLOT);
		
		KnimeTuple userTuple = new KnimeTuple(userSchema);
		userTuple.setValue(REPORT_NAME, reportName);
		userTuple.setValue(AUTHORS, authors);
		userTuple.setValue(COMMENT, comment);
		userTuple.setValue(SVG_PLOT, svgPlot);
		
		userContainer.addRowToTable(userTuple);
		userContainer.close();
		
		// TODO: finish output
		return new PortObject[] { container.getTable(), userContainer.getTable(), svgImageFromView };
	}

	private PortObjectSpec[] createOutputDataTableSpecs() {

		return new PortObjectSpec[] {
			SchemaFactory.createM1DataSchema().createSpec(), 
			getUserSpec(),
			ChartUtilities.getImageSpec(true)
		};		
	}
	
	private DataTableSpec getUserSpec(){
		String[] fields = {AUTHORS, REPORT_NAME, COMMENT, SVG_PLOT};
		DataType[] types = {StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE};
		DataTableSpec userDataSpec = new DataTableSpec(fields, types);
		return userDataSpec;
	}
	
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

	@Override
	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean generateImage() {
		// always generate image
		return true;
	}   	
}
