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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.io.IOUtils;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.knime.base.data.aggregation.dialogutil.BooleanCellRenderer;
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
import de.bund.bfr.knime.pmm.fskx.rsnippet.RSnippet;
import de.bund.bfr.knime.pmm.fskx.ui.RSnippetTextArea;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;

import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

  /**
   * Convenience access member for <code>new PortType(FSKPortObject.class)</code>
   */
  public static final PortType TYPE =
      PortTypeRegistry.getInstance().getPortType(FskPortObject.class);

  /** Model script. */
  private String m_model;

  /** Parameters script. */
  private String m_param;

  /** Visualization script. */
  private String m_viz;

  /** Model meta data. */
  private FSMRTemplate m_template;

  /** R workspace file. */
  private File m_workspace;

  /** R library files. */
  private Set<File> m_libs;

  public FskPortObject(final String model, final String param, final String viz,
      final FSMRTemplate template, final File workspace, final Set<File> libs) {
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

  public void setModelScript(final String model) {
    m_model = model;
  }

  /** @return the parameters script. */
  public String getParamScript() {
    return m_param;
  }

  public void setParamScript(final String param) {
    m_param = param;
  }

  /** @return the visualization script. */
  public String getVizScript() {
    return m_viz;
  }

  public void setVizScript(final String viz) {
    m_viz = viz;
  }

  /** @return the template. */
  public FSMRTemplate getTemplate() {
    return m_template;
  }

  public void setTemplate(final FSMRTemplate template) {
    m_template = template;
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
    public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
        ExecutionMonitor exec) throws IOException, CanceledExecutionException {

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
            // Adds to libs the Paths of the libraries converted to Files
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
    JPanel modelScriptPanel = new ScriptPanel("Model script", m_model);
    JPanel paramScriptPanel = new ScriptPanel("Param script", m_param);
    JPanel vizScriptPanel = new ScriptPanel("Visualization script", m_viz);

    return new JComponent[] {modelScriptPanel, paramScriptPanel, vizScriptPanel,
        new MetaDataPanel(), new LibrariesPanel()};
  }

  /** JPanel with an R script */
  class ScriptPanel extends JPanel {

    private static final long serialVersionUID = -2150198208821903469L;

    ScriptPanel(final String title, final String script) {
      super(new BorderLayout());
      setName(title);

      RSnippetTextArea textArea = new RSnippetTextArea(new RSnippet());
      textArea.setLineWrap(true);
      textArea.setText(script);
      textArea.setEditable(false);
      add(new RTextScrollPane(textArea));
    }
  }

  /** JPanel with a JTable populated with data from an FSMRTemplate. */
  class MetaDataPanel extends JPanel {

    private static final long serialVersionUID = 7056855986937773639L;

    MetaDataPanel() {
      super(new BorderLayout());
      setName("Meta data");
      add(new JScrollPane(new MetaDataTable(m_template)));
    }

    class MetaDataTable extends JTable {

      private static final long serialVersionUID = -4683197224648521120L;

      MetaDataTable(final FSMRTemplate template) {
        super(new MetaDataTableModel(template));
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      }

      @Override
      public TableCellRenderer getCellRenderer(int row, int column) {
        if (row == 28 && column == 1) {
          // Create renderer for boolean without tooltip
          BooleanCellRenderer booleanCellRenderer = new BooleanCellRenderer(null);
          booleanCellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
          return booleanCellRenderer;
        }
        return super.getCellRenderer(row, column);
      }
    }

    class MetaDataTableModel extends AbstractTableModel {

      private static final long serialVersionUID = 2006463867849567074L;
      String[] names = new String[] {"Field", "Value"};
      Object[][] data = new Object[29][2];

      MetaDataTableModel(final FSMRTemplate template) {
        // model name
        data[0][0] = OpenFSMRSchema.ATT_MODEL_NAME;
        data[0][1] = template.isSetModelName() ? template.getModelName() : "";

        // model id
        data[1][0] = OpenFSMRSchema.ATT_MODEL_ID;
        data[1][1] = template.isSetModelId() ? template.getModelId() : "";

        // model link
        data[2][0] = OpenFSMRSchema.ATT_MODEL_LINK;
        data[2][1] = template.isSetModelLink() ? template.getModelLink() : null;

        // organism name
        data[3][0] = OpenFSMRSchema.ATT_ORGANISM_NAME;
        data[3][1] = template.isSetOrganismName() ? template.getOrganismName() : "";

        // organism detail
        data[4][0] = OpenFSMRSchema.ATT_ORGANISM_DETAIL;
        data[4][1] = template.isSetOrganismDetails() ? template.getOrganismDetails() : "";

        // matrix name
        data[5][0] = OpenFSMRSchema.ATT_ENVIRONMENT_NAME;
        data[5][1] = template.isSetMatrixName() ? template.getMatrixName() : "";

        // matrix detail
        data[6][0] = OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL;
        data[6][1] = template.isSetMatrixDetails() ? template.getMatrixDetails() : "";

        // creator
        data[7][0] = OpenFSMRSchema.ATT_MODEL_CREATOR;
        data[7][1] = template.isSetCreator() ? template.getCreator() : "";

        // family name
        data[8][0] = OpenFSMRSchema.ATT_MODEL_FAMILY_NAME;
        data[8][1] = template.isSetFamilyName() ? template.getFamilyName() : "";

        // contact
        data[9][0] = OpenFSMRSchema.ATT_MODEL_CONTACT;
        data[9][1] = template.isSetContact() ? template.getContact() : "";

        // reference description
        data[10][0] = OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION;
        data[10][1] =
            template.isSetReferenceDescription() ? template.getReferenceDescription() : "";

        // reference description link
        data[11][0] = OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK;
        data[11][1] = template.isSetReferenceDescriptionLink()
            ? template.getReferenceDescriptionLink() : null;

        // created date
        data[12][0] = OpenFSMRSchema.ATT_MODEL_CREATED_DATE;
        data[12][1] = template.isSetCreatedDate() ? template.getCreatedDate() : null;

        // modified date
        data[13][0] = OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE;
        data[13][1] = template.isSetModifiedDate() ? template.getModifiedDate() : null;

        // rights
        data[14][0] = OpenFSMRSchema.ATT_MODEL_RIGHTS;
        data[14][1] = template.isSetRights() ? template.getRights() : "";

        // notes
        data[15][0] = OpenFSMRSchema.ATT_MODEL_NOTES;
        data[15][1] = template.isSetNotes() ? template.getNotes() : "";

        // curation status
        data[16][0] = OpenFSMRSchema.ATT_MODEL_CURATION_STATUS;
        data[16][1] = template.isSetModelType() ? template.getCurationStatus() : null;

        // model type
        data[17][0] = OpenFSMRSchema.ATT_MODEL_TYPE;
        data[17][1] = template.isSetModelType() ? template.getModelType() : null;

        // model subject
        data[18][0] = OpenFSMRSchema.ATT_MODEL_SUBJECT;
        data[18][1] = template.isSetModelSubject() ? template.getModelSubject() : null;

        // food process
        data[19][0] = OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS;
        data[19][1] = template.isSetFoodProcess() ? template.getFoodProcess() : null;

        // dependent variable
        data[20][0] = OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE;
        data[20][1] = template.isSetDependentVariable() ? template.getDependentVariable() : null;

        // dependent variable unit
        data[21][0] = OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT;
        data[21][1] =
            template.isSetDependentVariableUnit() ? template.getDependentVariableUnit() : null;

        // dependent variable min
        data[22][0] = OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN;
        data[22][1] =
            template.isSetDependentVariableMin() ? template.getDependentVariableMin() : null;

        // dependent variable max
        data[23][0] = OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX;
        data[23][1] =
            template.isSetDependentVariableMax() ? template.getDependentVariableMax() : null;

        // independent variable
        data[24][0] = OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE;
        data[24][1] = template.isSetIndependentVariables()
            ? String.join(",", template.getIndependentVariables()) : null;

        // independent variable units
        data[25][0] = OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS;
        data[25][1] = template.isSetIndependentVariablesUnits()
            ? String.join(",", template.getIndependentVariablesUnits()) : null;

        // independent variable minimum values
        data[26][0] = OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS;
        if (template.isSetIndependentVariablesMins()) {
          data[26][1] = Arrays.stream(template.getIndependentVariablesMins())
              .map(d -> ((Double) d).toString()).collect(Collectors.joining(","));
        } else {
          data[26][1] = null;
        }

        // independent variable maximum values
        data[27][0] = OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS;
        data[27][1] = data[27][1] = Arrays.stream(template.getIndependentVariablesMaxs())
            .map(d -> ((Double) d).toString()).collect(Collectors.joining(","));

        // has data
        data[28][0] = OpenFSMRSchema.ATT_HAS_DATA;
        data[28][1] = template.isSetHasData() ? template.getHasData() : null;
      }

      @Override
      public int getColumnCount() {
        return names.length;
      }

      @Override
      public int getRowCount() {
        return data.length;
      }

      @Override
      public String getColumnName(int column) {
        return names[column];
      }

      @Override
      public Object getValueAt(int row, int col) {
        return data[row][col];
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
      }
    }
  }

  /** JPanel with list of R libraries. */
  class LibrariesPanel extends JPanel {

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


