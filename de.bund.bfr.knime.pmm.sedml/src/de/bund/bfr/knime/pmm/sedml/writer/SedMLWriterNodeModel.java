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
package de.bund.bfr.knime.pmm.sedml.writer;

import java.io.File;
import java.io.IOException;

import org.jlibsedml.Algorithm;
import org.jlibsedml.Curve;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.Plot2D;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.Simulation;
import org.jlibsedml.Task;
import org.jlibsedml.UniformTimeCourse;
import org.jlibsedml.Variable;
import org.jlibsedml.VariableSymbol;
import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;
import org.jmathml.ASTNode;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 */
public class SedMLWriterNodeModel extends NodeModel {

	protected static final String CFG_FILE = "file";

	private SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

	protected SedMLWriterNodeModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		KnimeSchema schema = SchemaFactory.createM1DataSchema();
		KnimeTuple tuple = PmmUtilities.getTuples(inData[0], schema).get(0);

		SEDMLDocument doc = createDoc(filePath.getStringValue(), tuple);
		doc.writeDocument(new File(filePath.getStringValue()));

		return new BufferedDataTable[] {};
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filePath.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	private SEDMLDocument createDoc(final String path, final KnimeTuple tuple) {

		// Creates an SedML document object
		final SEDMLDocument doc = new SEDMLDocument();
		final SedML sedml = doc.getSedMLModel();

		final Simulation simulation = createSimulation(tuple);
		final Model model = createModel(tuple);
		final Task task = createTask(model.getId(), simulation.getId());
		final DataGenerator dgx = createXDataGenerator(task.getId());
		final DataGenerator dgy = createYDataGenerator(task.getId());
		final Plot2D plot = createOutput(dgx.getId(), dgy.getId());

		// Add SedML components
		sedml.addSimulation(simulation);
		sedml.addModel(model);
		sedml.addTask(task);
		sedml.addDataGenerator(dgx);
		sedml.addDataGenerator(dgy);
		sedml.addOutput(plot);
		
		return doc;
	}

	private Simulation createSimulation(final KnimeTuple tuple) {
		
		final PmmXmlDoc indepDoc = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		final IndepXml indepXml = (IndepXml) indepDoc.get(0);

		final String id = "simulation1";
		final String name = null;
		final double initialTime = 0;
		final double outputStartTime = indepXml.getMin() == null ? 0 : indepXml.getMin();
		final double outputEndTime = indepXml.getMax() == null ? 0 : indepXml.getMax();
		final int numberOfPoints = 1000;
		final Algorithm algorithm = new Algorithm("KISAO:0000032");

		final Simulation simulation = new UniformTimeCourse(id, name, initialTime, outputStartTime, outputEndTime,
				numberOfPoints, algorithm);

		return simulation;
	}

	private Model createModel(final KnimeTuple tuple) {
		final PmmXmlDoc estModelDoc = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
		final EstModelXml estModelXml = (EstModelXml) estModelDoc.get(0);

		final String id = "model1";
		final String name = estModelXml.getName();
		final String language = SUPPORTED_LANGUAGE.SBML_GENERIC.getURN();
		final String source = "model.xml";

		final Model model = new Model(id, name, language, source);

		return model;
	}

	private Task createTask(final String modelId, final String simulationId) {

		final String id = "task1";
		final String name = null;

		final Task task = new Task(id, name, modelId, simulationId);

		return task;
	}

	private DataGenerator createXDataGenerator(final String taskReference) {

		// Creates variable
		final String variableId = "concVar";
		final String variableName = null;
		final String variableTarget = "/sbml:sbml/sbml:model/sbml:listOfSpecies/sbml:species[0]";
		final Variable variable = new Variable(variableId, variableName, taskReference, variableTarget);
		
		// Creates MathML
		final ASTNode root = Libsedml.parseFormulaString(variableId);
		
		// Creates DataGenerator
		DataGenerator dgx = new DataGenerator("conc", null, root);
		dgx.addVariable(variable);
		
		return dgx;
	}
	
	private DataGenerator createYDataGenerator(final String taskReference) {
		
		// Creates variable
		final Variable variable = new Variable("t", null, taskReference, VariableSymbol.TIME);
		
		// Creates MathML
		final ASTNode root = Libsedml.parseFormulaString("t");
		
		// Creates DataGenerator
		DataGenerator dgy = new DataGenerator("time", null, root);
		dgy.addVariable(variable);
		
		return dgy;
	}
	
	private Plot2D createOutput(final String xDataRef, final String yDataRef) {
		
		// Creates curve
		final String curveId = "curve1";
		final String curveName = null;
		final boolean isLogX = false;
		final boolean isLogY = false;
		
		final Curve curve = new Curve(curveId, curveName, isLogX, isLogY, xDataRef, yDataRef);
		
		// Creates Plot2D and adds curve
		Plot2D plot = new Plot2D("plot1", null);
		plot.addCurve(curve);
		
		return plot;
	}
	
}
