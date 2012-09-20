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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;

public class BufferedTableContainer implements PortObject, Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final PortType TYPE = new PortType(BufferedTableContainer.class);

	private static final String CFG_WHOLE_OBJECT = "WHOLE_OBJECT";
	
    private HashMap<String, File> tableFiles;
	
	public BufferedTableContainer() {
		tableFiles = new HashMap<String, File>();
	}
	
	public File getTable(String key){
		return tableFiles.get(key);
	}
	
	@Override
	public String getSummary() {
		return null;
	}

	@Override
	public PortObjectSpec getSpec() {
		return new BufferedTableContainerSpec(tableFiles.keySet().toArray(new String[tableFiles.keySet().size()]));
	}

	@Override
	public JComponent[] getViews() {
		return null;
	}

	public void addTableFile(String key, File dataTableFile) {
		tableFiles.put(key, dataTableFile);
	}

	public List<String> getTableFileNames() {
		return Arrays.asList(tableFiles.keySet().toArray(new String[tableFiles.keySet().size()]));
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		BufferedTableContainer btc = new BufferedTableContainer();
		for(String key : tableFiles.keySet())
			btc.addTableFile(key, tableFiles.get(key));
		
		return btc;
	}
	
    public static PortObjectSerializer<BufferedTableContainer> 
    getPortObjectSerializer() {
    	return new PortObjectSerializer<BufferedTableContainer>() {
		    private static final String FILENAME = "BTspec.xml";		    
		    
			@Override
			public void savePortObject(BufferedTableContainer portObject,
					PortObjectZipOutputStream out, ExecutionMonitor exec)
					throws IOException, CanceledExecutionException {
		        ModelContent cnt = new ModelContent(FILENAME);
		        portObject.save(cnt);
		        out.putNextEntry(new ZipEntry(FILENAME));
		        cnt.saveToXML(out);			
			}

			@Override
			public BufferedTableContainer loadPortObject(
					PortObjectZipInputStream in, PortObjectSpec spec,
					ExecutionMonitor exec) throws IOException,
					CanceledExecutionException {
				ZipEntry entry = in.getNextEntry();
		        if (!FILENAME.equals(entry.getName())) {
		            throw new IOException("Expected '" + FILENAME 
		                    + "' zip entry, got " + entry.getName());
		        }
		        ModelContentRO cnt = ModelContent.loadFromXML(in);
		        try {
		            return BufferedTableContainer.load(cnt);
		        } catch (InvalidSettingsException e) {
		            throw new IOException(e.getMessage(), e);
		        } catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		        
		        return null;
			}
		};      
	}

	protected void save(ConfigWO config) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.close();
		bos.close();
		config.addByteArray(CFG_WHOLE_OBJECT, bos.toByteArray());		
	}

	protected static BufferedTableContainer load(ConfigRO config) throws InvalidSettingsException, IOException, ClassNotFoundException{
		
		ByteArrayInputStream bais = new ByteArrayInputStream(config.getByteArray(CFG_WHOLE_OBJECT));		
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (BufferedTableContainer) ois.readObject();
	}
}
