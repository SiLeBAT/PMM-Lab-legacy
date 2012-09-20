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
package de.dim.bfr.knime.ports;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

public class BufferedTableContainerSpec implements PortObjectSpec {

	private static final String CFG_TABLE_NAMES = "BufferedTableContainerSpec_table_names";
	String[] tableNames;
	
	public BufferedTableContainerSpec(String[] names) {
		this.tableNames = names;
	}

	public String[] getTableNames() {
		return tableNames;
	}
	
	@Override
	public JComponent[] getViews() {
		return null;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		BufferedTableContainerSpec spec = new BufferedTableContainerSpec(Arrays.copyOf(tableNames,tableNames.length));		
		return spec;
	}
	
    /** Method required by the interface {@link PortObjectSpec}. Not meant
     * for public use.
     * @return A new serializer responsible for loading/saving.
     */
    public static PortObjectSpecSerializer<BufferedTableContainerSpec> 
            getPortObjectSpecSerializer() {
        return new PortObjectSpecSerializer<BufferedTableContainerSpec>() {
            private static final String FILENAME = "spec.xml";
            
            /** {@inheritDoc} */
            @Override
            public BufferedTableContainerSpec loadPortObjectSpec(
                    final PortObjectSpecZipInputStream in)
                throws IOException {
                ZipEntry entry = in.getNextEntry();
                if (!FILENAME.equals(entry.getName())) {
                    throw new IOException("Expected '" + FILENAME 
                            + "' zip entry, got " + entry.getName());
                }
                ModelContentRO cnt = ModelContent.loadFromXML(in);
                try {
                    return BufferedTableContainerSpec.load(cnt);
                } catch (InvalidSettingsException e) {
                    throw new IOException(e.getMessage(), e);
                }
            }
            
            /** {@inheritDoc} */
            @Override
            public void savePortObjectSpec(final BufferedTableContainerSpec spec,
                    final PortObjectSpecZipOutputStream out) 
                throws IOException {
                ModelContent cnt = new ModelContent(FILENAME);
                spec.save(cnt);
                out.putNextEntry(new ZipEntry(FILENAME));
                cnt.saveToXML(out);
            }
        };      
    }
    
    public static BufferedTableContainerSpec load(final ConfigRO config)
            throws InvalidSettingsException {
        String[] names = config.getStringArray(CFG_TABLE_NAMES);
        return new BufferedTableContainerSpec(names);
    }
    
    public void save(final ConfigWO config) {
        config.addStringArray(CFG_TABLE_NAMES,tableNames);
    }
}
