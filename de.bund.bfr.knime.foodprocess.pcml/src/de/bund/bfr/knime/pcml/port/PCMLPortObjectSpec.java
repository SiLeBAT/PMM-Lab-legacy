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
package de.bund.bfr.knime.pcml.port;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import lombok.Getter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;

/**
 * The spec of the process chain port type.
 * @author Heiko Hofer
 *
 */
public class PCMLPortObjectSpec implements PortObjectSpec {
	private static final String MATRIX = "matrix";
	private static final String MATRIX_ID = "matrix id";
	private static final String MATRIX_FRACTION = "matrix fraction";
	private static final String AGENT = "agent";
	private static final String AGENT_ID = "agent id";
	private static final String AGENT_QUANTITY = "agent quantity";
	private static final String VOLUME = "volume";
	private static final String TEMPERATURE = "temperature";
	private static final String PRESSURE = "pressure";
	private static final String PH_VALUE = "pH-value";
	private static final String AW_VALUE = "aw-value";

	/** the matrices. */
	@Getter private final Map<Matrix, Double> matrices;
	/** the agenses. */
	@Getter private final Map<Agent, Double> agents;
	/** the volume or null. */
	@Getter private final Double volume;
	/** the temperature or null. */
	@Getter private final Double temperature;
	/** the pressure or null. */
	@Getter private final Double pressure;
	/** the pH value or null. */
	@Getter private final Double pH_value;
	/** the water activity or null. */
	@Getter private final Double aw_value;
	
	
	/** Static serializer as demanded from {@link PortObjectSpec} framework. */
    private static PortObjectSpecSerializer<PCMLPortObjectSpec> serializer;
    
    /**
     * Returns true when this port is used by this process node, i.e. matrices
     * are sent out of this port.
     * @return true when port is used
     */
    public boolean isUsed() {
    	return matrices.size() > 0;
    }
    
    /**
     * Creates a new port object spec for a port that is not used.
     * 
     */
    @SuppressWarnings("unchecked")
	public PCMLPortObjectSpec() {
		this(Collections.EMPTY_MAP);
	}
    
    
    /**
     * Creates a new port object spec with the given parameters. The matrix
     * at the outport.
     * 
     * @param matrix the matrix
     */
    @SuppressWarnings("unchecked")
	public PCMLPortObjectSpec(final Map<Matrix, Double> matrices) {
		this(matrices, Collections.EMPTY_MAP, null, null, null, null, null);
	}
    
    /**
     * Creates a new port object spec with the given parameters. The matrix 
     * and agent arrays are mandatory but the other parameters are optional. 
     * They describe the physical properties of the matrix.
     * 
     * @param matrices the matrices
     * @param agents the agents
     * @param volume the volume or null
     * @param temperature the temperature of the matrix or null
     * @param pressure the pressure or null
     * @param pH the pH-value of the matrix or null
     * @param aw the the water activity or null
     */
    public PCMLPortObjectSpec(final Map<Matrix, Double> matrices, 
    		final Map<Agent, Double> agents,
    		final Double volume,
			final Double temperature, 
			final Double pressure, 
			final Double pH, 
			final Double aw) {
		if (null == matrices) {
			throw new NullPointerException("The matrix must be defined");
		}
		this.matrices = matrices;
		this.agents = agents;
		this.volume = volume;
		this.temperature = temperature;
		this.pressure = pressure;
		this.pH_value = pH;
		this.aw_value = aw;
	}



	/**
     * Static serializer as demanded from {@link PortObjectSpec} framework.
     * @return serializer for PCML (reads and writes PCML files)
     */
    public static PortObjectSpecSerializer<PCMLPortObjectSpec>
            getPortObjectSpecSerializer() {
        if (serializer == null) {
            serializer = new PCMLPortObjectSpecSerializer();
        }
        return serializer;
    }
    
	@Override
	public JComponent[] getViews() {
		if (isUsed()) {
			return new JComponent[] {new PCMLPortObjectSpecView(this)};
		} else {
			return new JComponent[0];
		}
	}

	/**
	 * Serialize to the given model content.
	 * @param cnt the model content
	 */
	public void save(final ModelContent config) {
		saveMatrices(config);
		saveAgents(config);
        if (null != volume) {
        	config.addDouble(VOLUME, volume);
        }
        if (null != temperature) {
            config.addDouble(TEMPERATURE, temperature);
        }
        if (null != pressure) {
        	config.addDouble(PRESSURE, pressure);
        }
        if (null != pH_value) {
        	config.addDouble(PH_VALUE, pH_value);
        }
        if (null != aw_value) {
        	config.addDouble(AW_VALUE, aw_value);
        }
	}


	private void saveMatrices(final ModelContent config) {
		String[] s = new String[matrices.size()]; 
        int[] id = new int[matrices.size()];
        double[] fraction = new double[matrices.size()];
        int i = 0;
        for (Matrix matrix : matrices.keySet()) {
        	s[i] = matrix.getName();
        	id[i] = matrix.getId();
        	fraction[i] = matrices.get(matrix);
        	i++;
        }
        config.addStringArray(MATRIX, s);
        config.addIntArray(MATRIX_ID, id);
        config.addDoubleArray(MATRIX_FRACTION, fraction);
	}

	private void saveAgents(final ModelContent config) {
		String[] s = new String[agents.size()];
		int[] id = new int[agents.size()];
		double[] quantity = new double[agents.size()];
        int i = 0;
        for (Agent agent : agents.keySet()) {
        	s[i] = agent.getName();
        	id[i] = agent.getId();
        	quantity[i] = matrices.get(agent);
        	i++;
        }		
        config.addStringArray(AGENT, s);
        config.addIntArray(AGENT_ID, id);
        config.addDoubleArray(AGENT_QUANTITY, quantity);
	}

	/**
	 * Deserialize from the given model content 
	 * @param cnt the model content
	 * @return the PCMLPortObjectSpec read from the model content
	 */
	public static PCMLPortObjectSpec load(final ModelContentRO config) throws
		InvalidSettingsException {
		
		Map<Matrix, Double> matrices = loadMatrices(config);
		Map<Agent, Double> agents = loadAgents(config);
		Double volumne = config.containsKey(VOLUME)
			? config.getDouble(VOLUME) : null;
		Double temperature = config.containsKey(TEMPERATURE)
			? config.getDouble(TEMPERATURE) : null;
		Double pressure = config.containsKey(PRESSURE)
			? config.getDouble(PRESSURE) : null;
		Double pH = config.containsKey(PH_VALUE)
			? config.getDouble(PH_VALUE) : null;
		Double aw = config.containsKey(AW_VALUE)
			? config.getDouble(AW_VALUE) : null;
		PCMLPortObjectSpec spec = new PCMLPortObjectSpec(matrices, 
				agents, volumne, 
				temperature, pressure, pH, aw);
		return spec;
	}
	

	private static Map<Matrix, Double> loadMatrices(final ModelContentRO config) {
		String[] s = config.getStringArray(MATRIX, new String[0]);
        int[] id = config.getIntArray(MATRIX_ID, new int[0]);
        double[] percentage = config.getDoubleArray(
        		MATRIX_FRACTION, new double[0]);
        Map<Matrix, Double> matrices = new LinkedHashMap<Matrix, Double>();
        for (int i = 0; i < s.length; i++) {
        	matrices.put(new Matrix(s[i], id[i]), percentage[i]);
        }
        return matrices;
	}	

	private static Map<Agent, Double> loadAgents(final ModelContentRO config) {
		String[] s = config.getStringArray(AGENT, new String[0]);
		int[] id = config.getIntArray(AGENT_ID, new int[0]);
		double[] quantity = config.getDoubleArray(AGENT_QUANTITY, 
				new double[0]);
		Map<Agent, Double> agents = new LinkedHashMap<Agent, Double>();
        for (int i = 0; i < s.length; i++) {
        	agents.put(new Agent(s[i], id[i]), quantity[i]);
        }
        return agents;  
	}

}
