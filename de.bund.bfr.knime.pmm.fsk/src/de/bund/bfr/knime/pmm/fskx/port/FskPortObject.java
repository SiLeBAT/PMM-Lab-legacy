/*
 ***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.pmm.fskx.port;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.apache.commons.io.IOUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import de.bund.bfr.knime.pmm.fskx.FSKNodePlugin;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.ui.MetaDataPane;
import de.bund.bfr.knime.pmm.fskx.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

	/**
	 * Convenience access member for
	 * <code>new PortType(FSKPortObject.class)</code>
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject.class);

	/** Model script. */
	private final String m_model;

	/** Parameters script. */
	private final String m_param;

	/** Visualization script. */
	private final String m_viz;

	/** Model meta data. */
	private final FSMRTemplate m_template;

	/** R workspace file. */
	private File m_workspace;

	/** R library files. */
	private final Set<File> m_libs;

	public FskPortObject(final String model, final String param, final String viz, final FSMRTemplate template,
			final File workspace, final Set<File> libs) {
		m_model = model;
		m_param = param;
		m_viz = viz;
		m_template = template;
		m_workspace = workspace;
		m_libs = libs;
	}

	@Override
	public FskPortObjectSpec getSpec() {
		return FskPortObjectSpec.INSTANCE;
	}

	@Override
	public String getSummary() {
		return "FSK Object";
	}

	/** @return the model script. */
	public String getModelScript() {
		return m_model;
	}

	/** @return the parameters script. */
	public String getParamScript() {
		return m_param;
	}

	/** @return the visualization script. */
	public String getVizScript() {
		return m_viz;
	}

	/** @return the template. */
	public FSMRTemplate getTemplate() {
		return m_template;
	}

	/** @return the R workspace file. */
	public File getWorkspaceFile() {
		return m_workspace;
	}

	public void setWorkspaceFile(final File workspace) {
		m_workspace = workspace;
	}

	/** @return the R library files. */
	public Set<File> getLibraries() {
		return m_libs;
	}

	/**
	 * Serializer used to save this port object.
	 * 
	 * @return a {@link FskPortObject}.
	 */
	public static final class Serializer extends PortObjectSerializer<FskPortObject> {

		private static final String MODEL = "model.R";
		private static final String PARAM = "param.R";
		private static final String VIZ = "viz.R";
		private static final String META_DATA = "metaData";
		private static final String WORKSPACE = "workspace";

		/** {@inheritDoc} */
		@Override
		public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
			// model entry (file with model script)
			out.putNextEntry(new ZipEntry(MODEL));
			IOUtils.write(portObject.m_model, out);
			out.closeEntry();

			// param entry (file with param script)
			out.putNextEntry(new ZipEntry(PARAM));
			IOUtils.write(portObject.m_param, out);
			out.closeEntry();

			// viz entry (file with visualization script)
			out.putNextEntry(new ZipEntry(VIZ));
			IOUtils.write(portObject.m_viz, out);
			out.closeEntry();

			// template entry (file with model meta data)
			if (portObject.m_template != null) {
				out.putNextEntry(new ZipEntry(META_DATA));
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(portObject.m_template);
				out.closeEntry();
			}

			// workspace entry
			if (portObject.m_workspace != null) {
				out.putNextEntry(new ZipEntry(WORKSPACE));
				try (FileInputStream fis = new FileInputStream(portObject.m_workspace)) {
					FileUtil.copy(fis, out);
				}
				out.closeEntry();
			}

			if (!portObject.m_libs.isEmpty()) {
				out.putNextEntry(new ZipEntry("library.list"));
				List<String> libNames = portObject.m_libs.stream().map(f -> f.getName().split("\\_")[0])
						.collect(Collectors.toList());
				IOUtils.writeLines(libNames, "\n", out, "UTF-8");
				out.closeEntry();
			}

			out.close();
		}

		@Override
		public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
				throws IOException, CanceledExecutionException {
			
			String model = "";
			String param = "";
			String viz = "";
			FSMRTemplate template = null;
			File workspaceFile = null;
			Set<File> libs = new HashSet<>();

			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();

				if (entryName.equals(MODEL)) {
					model = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(PARAM)) {
					param = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(VIZ)) {
					viz = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(META_DATA)) {
					ObjectInputStream ois = new ObjectInputStream(in);
					try {
						template = (FSMRTemplateImpl) ois.readObject();
					} catch (ClassNotFoundException e) {
					}
				} else if (entryName.equals(WORKSPACE)) {
					workspaceFile = FileUtil.createTempFile("workspace", ".r");
					FileOutputStream fos = new FileOutputStream(workspaceFile);
					FileUtil.copy(in, fos);
					fos.close();
				} else if (entryName.equals("library.list")) {
					List<String> libNames = IOUtils.readLines(in, "UTF-8");

					FSKNodePlugin plugin = FSKNodePlugin.getDefault();
					// Install missing libraries
					List<String> missing = libNames.stream().filter(lib -> !plugin.isInstalled(lib))
							.collect(Collectors.toList());

					try {
						if (!missing.isEmpty()) {
							plugin.installLibs(missing);
						}
						// Adds to libs the Paths of the libraries converted to
						// Files
						plugin.getPaths(libNames).stream().forEach(p -> libs.add(p.toFile()));
					} catch (RException | REXPMismatchException error) {
						throw new IOException(error.getMessage());
					}
				}
			}

			in.close();

			return new FskPortObject(model, param, viz, template, workspaceFile, libs);
		}
	}

	/** {Override} */
	@Override
	public JComponent[] getViews() {
		JPanel modelScriptPanel = new ScriptPanel("Model script", m_model, false);
		JPanel paramScriptPanel = new ScriptPanel("Param script", m_param, false);
		JPanel vizScriptPanel = new ScriptPanel("Visualization script", m_viz, false);

		return new JComponent[] { modelScriptPanel, paramScriptPanel, vizScriptPanel, new MetaDataPanel(),
				new LibrariesPanel() };
	}

	/** JPanel with a JTable populated with data from an FSMRTemplate. */
	private class MetaDataPanel extends JPanel {

		private static final long serialVersionUID = 7056855986937773639L;

		MetaDataPanel() {
			super(new BorderLayout());
			setName("Meta data");
			add(new MetaDataPane(m_template, false));
		}
	}

	/** JPanel with list of R libraries. */
	private class LibrariesPanel extends JPanel {

		private static final long serialVersionUID = -5084804515050256443L;

		LibrariesPanel() {
			super(new BorderLayout());
			setName("Libraries list");

			String[] libNames = new String[m_libs.size()];
			int i = 0;
			for (File lib : m_libs) {
				libNames[i] = lib.getName();
				i++;
			}

			JList<String> list = new JList<>(libNames);
			list.setLayoutOrientation(JList.VERTICAL);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			add(new JScrollPane(list));
		}
	}
}
