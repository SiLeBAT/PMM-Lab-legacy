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
package de.bund.bfr.knime.gis.regionvisualizer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.eclipse.stem.gis.ShapefileReader;
import org.eclipse.stem.gis.dbf.DbfFieldDef;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "RegionVisualizer" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class RegionVisualizerNodeDialog extends NodeDialogPane implements
		ActionListener, DocumentListener {

	private JTextField fileField;
	private JButton fileButton;
	private JComboBox<String> fileIdBox;
	private JComboBox<String> tableIdBox;

	/**
	 * New pane for configuring the RegionVisualizer node.
	 */
	protected RegionVisualizerNodeDialog() {
		fileField = new JTextField();
		fileField.getDocument().addDocumentListener(this);
		fileButton = new JButton("Browse...");
		fileButton.addActionListener(this);
		fileIdBox = new JComboBox<String>();
		tableIdBox = new JComboBox<String>();

		JPanel fileOpenPanel = new JPanel();

		fileOpenPanel.setLayout(new BorderLayout(5, 5));
		fileOpenPanel.add(fileField, BorderLayout.CENTER);
		fileOpenPanel.add(fileButton, BorderLayout.EAST);

		JPanel fileIdPanel = new JPanel();

		fileIdPanel.setLayout(new BorderLayout(5, 5));
		fileIdPanel.add(new JLabel("Column with IDs:"), BorderLayout.WEST);
		fileIdPanel.add(fileIdBox, BorderLayout.CENTER);

		JPanel innerFilePanel = new JPanel();

		innerFilePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		innerFilePanel.setLayout(new GridLayout(2, 1, 5, 5));
		innerFilePanel.add(fileOpenPanel);
		innerFilePanel.add(fileIdPanel);

		JPanel outerFilePanel = new JPanel();

		outerFilePanel.setBorder(BorderFactory.createTitledBorder("Shapefile"));
		outerFilePanel.setLayout(new BorderLayout());
		outerFilePanel.add(innerFilePanel, BorderLayout.CENTER);

		JPanel leftTablePanel = new JPanel();

		leftTablePanel.setLayout(new GridLayout(1, 1, 5, 5));
		leftTablePanel.add(new JLabel("Column with IDs:"));

		JPanel rightTablePanel = new JPanel();

		rightTablePanel.setLayout(new GridLayout(1, 1, 5, 5));
		rightTablePanel.add(tableIdBox);

		JPanel innerTablePanel = new JPanel();

		innerTablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		innerTablePanel.setLayout(new BorderLayout(5, 5));
		innerTablePanel.add(leftTablePanel, BorderLayout.WEST);
		innerTablePanel.add(rightTablePanel, BorderLayout.CENTER);

		JPanel outerTablePanel = new JPanel();

		outerTablePanel.setBorder(BorderFactory.createTitledBorder("Table"));
		outerTablePanel.setLayout(new BorderLayout());
		outerTablePanel.add(innerTablePanel, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(outerFilePanel);
		mainPanel.add(outerTablePanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(mainPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		String fileName;
		String fileIdColumn;
		String tableIdColumn;

		try {
			fileName = settings
					.getString(RegionVisualizerNodeModel.CFG_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = "";
		}

		try {
			fileIdColumn = settings
					.getString(RegionVisualizerNodeModel.CFG_FILEIDCOLUMN);
		} catch (InvalidSettingsException e) {
			fileIdColumn = "";
		}

		try {
			tableIdColumn = settings
					.getString(RegionVisualizerNodeModel.CFG_TABLEIDCOLUMN);
		} catch (InvalidSettingsException e) {
			tableIdColumn = "";
		}

		fileField.setText(fileName);
		fileIdBox.setSelectedItem(fileIdColumn);

		List<String> tableColumns = Arrays.asList(specs[0].getColumnNames());

		tableIdBox.removeAllItems();

		for (String column : tableColumns) {
			tableIdBox.addItem(column);
		}

		tableIdBox.setSelectedItem(tableIdColumn);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		String fileName = fileField.getText();
		File file = new File(fileName);

		if (!file.exists() || !fileName.toLowerCase().endsWith(".shp")) {
			throw new InvalidSettingsException(
					"Valid Shapefile (*.shp) must be specified!");
		}

		settings.addString(RegionVisualizerNodeModel.CFG_FILENAME, fileName);
		settings.addString(RegionVisualizerNodeModel.CFG_FILEIDCOLUMN,
				(String) fileIdBox.getSelectedItem());
		settings.addString(RegionVisualizerNodeModel.CFG_TABLEIDCOLUMN,
				(String) tableIdBox.getSelectedItem());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fileButton) {
			JFileChooser fileChooser;

			try {
				fileChooser = new JFileChooser(new File(fileField.getText()));
			} catch (Exception ex) {
				fileChooser = new JFileChooser();
			}

			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "Shapefile (*.shp)";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getName().toLowerCase().endsWith(".shp");
				}
			});

			if (fileChooser.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION) {
				fileField.setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged(e);
	}

	private void textChanged(DocumentEvent e) {
		if (e.getDocument() == fileField.getDocument()) {
			String fileName = fileField.getText();
			File file = new File(fileName);

			fileIdBox.removeAllItems();

			if (file.exists() && fileName.toLowerCase().endsWith(".shp")) {
				try {
					ShapefileReader reader = new ShapefileReader(file);
					List<DbfFieldDef> fields = reader.getTableHeader()
							.getFieldDefinitions();

					for (DbfFieldDef f : fields) {
						fileIdBox.addItem(f.getFieldName().trim());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
