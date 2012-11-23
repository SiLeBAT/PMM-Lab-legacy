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

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObjectSpec.PortObjectSpecSerializer;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

public class PCMLPortObjectSpecSerializer extends
		PortObjectSpecSerializer<PCMLPortObjectSpec> {
	private static final String FILENAME = "pcml_spec.xml";

	/**
     * {@inheritDoc}
     */
	@Override
	public void savePortObjectSpec(final PCMLPortObjectSpec spec,
			final PortObjectSpecZipOutputStream out) throws IOException {
		ModelContent cnt = new ModelContent(FILENAME);
        spec.save(cnt);
        out.putNextEntry(new ZipEntry(FILENAME));
        cnt.saveToXML(out);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public PCMLPortObjectSpec loadPortObjectSpec(
			final PortObjectSpecZipInputStream in)
			throws IOException {
        ZipEntry entry = in.getNextEntry();
        if (!FILENAME.equals(entry.getName())) {
            throw new IOException("Expected '" + FILENAME 
                    + "' zip entry, got " + entry.getName());
        }
        ModelContentRO cnt = ModelContent.loadFromXML(in);        
        try {
            return PCMLPortObjectSpec.load(cnt);
        } catch (InvalidSettingsException e) {
            throw new IOException(e.getMessage(), e);
        }
	}

}
