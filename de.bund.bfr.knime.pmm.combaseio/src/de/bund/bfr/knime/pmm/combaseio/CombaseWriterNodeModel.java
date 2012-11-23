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
package de.bund.bfr.knime.pmm.combaseio;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.combaseio.lib.CombaseWriter;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of CombaseWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class CombaseWriterNodeModel extends NodeModel {
	
	protected static final String PARAM_FILENAME = "filename";
	protected static final String DEFAULT_FILENAME = "";	
	
	private String filename;

    /**
     * Constructor for the node model.
     */
    protected CombaseWriterNodeModel() {
    
        super( 1, 0 );
        
        filename = DEFAULT_FILENAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	int n = inData[ 0 ].getRowCount();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);

		int j = 0;
		CombaseWriter cbw = new CombaseWriter(filename);
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();

			PmmTimeSeries ts = new PmmTimeSeries(row);
			cbw.add( ts );
		}
    	cbw.flush();
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure( final DataTableSpec[] inSpecs ) throws InvalidSettingsException {   	
    	if( filename.isEmpty() )
    		throw new InvalidSettingsException( "Filename must be specified." );
    	getInSchema(inSpecs[0]);    	
    	return null;//new DataTableSpec[]{};
    }
    
    private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
    	KnimeSchema result = null;
    	String errorMsg = "Expected format: TS";
    	KnimeSchema inSchema = new TimeSeriesSchema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			result = inSchema;
    		}
    	}
    	catch (PmmException e) {
    	}
    	if (result == null) {
    		throw new InvalidSettingsException(errorMsg);
    	}
    	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString(PARAM_FILENAME, filename);    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	filename = settings.getString(PARAM_FILENAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}
    

    

    


}

