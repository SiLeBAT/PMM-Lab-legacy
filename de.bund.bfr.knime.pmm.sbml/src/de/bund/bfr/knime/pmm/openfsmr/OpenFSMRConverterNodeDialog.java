package de.bund.bfr.knime.pmm.openfsmr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

/**
 * <code>NodeDialog</code> for the OpenFSMR Converter node.
 * 
 * @author Miguel Alba
 */
public class OpenFSMRConverterNodeDialog extends NodeDialogPane implements ChangeListener {

	// models
	private SettingsModelString m_selectedDirectory;
	private final SettingsModelStringArray m_selectedFiles;
	private final DefaultListModel<String> m_included;
	private final DefaultListModel<String> m_excluded;

	// gui elements
	private final DialogComponentFileChooser fileChooser;
	private final JList<String> m_includeList;
	private final JList<String> m_excludeList;

	/** New pane for configuring the OpenFSMR Converter node. */
	protected OpenFSMRConverterNodeDialog() {
		super();

		m_selectedDirectory = new SettingsModelString(OpenFSMRConverterNodeModel.CFGKEY_DIR,
				OpenFSMRConverterNodeModel.DEFAULT_DIR);
		m_selectedDirectory.setEnabled(true);
		m_selectedFiles = new SettingsModelStringArray(OpenFSMRConverterNodeModel.CFGKEY_FILES,
				OpenFSMRConverterNodeModel.DEFAULT_FILES);
		m_selectedFiles.setEnabled(true);

		// Inits models
		m_included = new DefaultListModel<String>();
		m_excluded = new DefaultListModel<String>();

		// Inits gui elements
		m_includeList = new JList<>(m_included);
		m_excludeList = new JList<>(m_excluded);

		// Creates file chooser
		final SettingsModelString selectedDirectory = new SettingsModelString(OpenFSMRConverterNodeModel.CFGKEY_DIR,
				"");
		fileChooser = new DialogComponentFileChooser(selectedDirectory, "directory-history", JFileChooser.OPEN_DIALOG,
				true);
		// Adds listener to file chooser to update the exclude list
		fileChooser.addChangeListener(this);

		// creates the GUI
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(fileChooser.getComponentPanel(), BorderLayout.NORTH);
		panel.add(createFileSelectionLists(), BorderLayout.CENTER);
		addTab("Selection:", new JScrollPane(panel));
		removeTab("Options");
	}

	private Box createFileSelectionLists() {
		Box overall = Box.createHorizontalBox();
		overall.setBorder(new TitledBorder("PMF files selection:"));
		overall.add(new LeftBox()); // left box with excluded items
		overall.add(new ButtonBox());
		overall.add(new RightBox()); // right box with included items

		return overall;
	}

	/**
	 * If the selected directory changes, include and exclude lists are cleared
	 * and all the files in the selected directory are added to the exclude
	 * list.
	 */
	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		m_included.clear();
		m_excluded.clear();

		// Add all the files in the selected directory to m_excluded
		SettingsModelString fileChooserModel = (SettingsModelString) fileChooser.getModel();
		String selectedDirectory = fileChooserModel.getStringValue();
		
		// Updates 'm_excluded' with all the PMF files in 'selectedDirectory'
		File directory = new File(selectedDirectory);
		for (File fileInDirectory : directory.listFiles()) {
			if (fileInDirectory.isFile() && fileInDirectory.getName().endsWith(".pmf")) {
				m_excluded.addElement(fileInDirectory.getName());
			}
		}

		// Updates models
		m_selectedDirectory.setStringValue(selectedDirectory);
		m_selectedFiles.setStringArrayValue(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.knime.core.node.NodeDialogPane#loadSettingsFrom(org.knime.core.node.
	 * NodeSettingsRO, org.knime.core.data.DataTableSpec[])
	 */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) {
		try {
			m_selectedDirectory.loadSettingsFrom(settings);
			try {
				fileChooser.loadSettingsFrom(settings, specs);
			} catch (NotConfigurableException e) {
				e.printStackTrace();
			}
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
			m_selectedDirectory.setStringValue(OpenFSMRConverterNodeModel.DEFAULT_DIR);
		}
		try {
			m_selectedFiles.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
			m_selectedFiles.setStringArrayValue(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.knime.core.node.NodeDialogPane#saveSettingsTo(org.knime.core.node.
	 * NodeSettingsWO)
	 */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		m_selectedDirectory.saveSettingsTo(settings);
		m_selectedFiles.saveSettingsTo(settings);
	}

	private void updateSelectedFiles() {
		String[] includedFilesArray = new String[m_included.size()];
		for (int i = 0; i < m_included.size(); i++) {
			includedFilesArray[i] = m_included.get(i);
		}
		m_selectedFiles.setStringArrayValue(includedFilesArray);
	}

	/** Sort alphabetically the elements in 'm_included'. */
	private void sortIncludedList() {
		TreeSet<String> orderedList = new TreeSet<>();
		for (int i = 0; i < m_included.size(); i++) {
			orderedList.add(m_included.get(i));
		}

		m_included.clear();
		for (String element : orderedList) {
			m_included.addElement(element);
		}
		m_includeList.setModel(m_included);
	}

	/** Sort alphabetically the elements in 'm_excluded'. */
	private void sortExcludedList() {
		TreeSet<String> orderedList = new TreeSet<>();
		for (int i = 0; i < m_excluded.size(); i++) {
			orderedList.add(m_excluded.get(i));
		}

		m_excluded.clear();
		for (String element : orderedList) {
			m_excluded.addElement(element);
		}
		m_excludeList.setModel(m_excluded);
	}

	/**
	 * Left box with excluded items. An excluded item may be included double
	 * clicking it in the {@link LeftBox}. Then it will left the {@link LeftBox}
	 * and join the {@link RightBox} with the included items.
	 */
	class LeftBox extends Box {

		private static final long serialVersionUID = 9000457181654600045L;

		public LeftBox() {
			super(BoxLayout.PAGE_AXIS);
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Excluded"));

			m_excludeList.setMinimumSize(new Dimension(200, 200));
			m_excludeList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent mouseEvent) {
					// Include file on double click
					if (mouseEvent.getClickCount() == 2) {
						final String selectedFile = m_excludeList.getSelectedValue();
						m_excluded.removeElement(selectedFile);
						sortExcludedList();
						m_included.addElement(selectedFile);
						sortIncludedList();
						updateSelectedFiles();
					}
				}
			});
			// force fixed with for list
			m_excludeList.setFixedCellWidth(200);

			JScrollPane excludedScroller = new JScrollPane(m_excludeList);
			excludedScroller.setMinimumSize(new Dimension(200, 200));
			add(excludedScroller);
		}
	}

	/**
	 * Button to move a selection of excluded items from the {@link LeftBox}
	 * (excluded items) to the {@link RightBox} (included items).
	 */
	class AddButton extends JButton {

		private static final long serialVersionUID = -476357270294636249L;

		public AddButton() {
			super("Add >");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					final List<String> selectedFiles = m_excludeList.getSelectedValuesList();
					for (String selectedFile : selectedFiles) {
						m_included.addElement(selectedFile);
						sortIncludedList();
						m_excluded.removeElement(selectedFile);
						sortExcludedList();
					}
					updateSelectedFiles();
				}
			});
		}
	}

	/**
	 * Button to move all the excluded items from the {@link LeftBox} (excluded
	 * items) to the {@link RightBox} (included items).
	 */
	class AddAllButton extends JButton {

		private static final long serialVersionUID = 8768291057679750883L;

		public AddAllButton() {
			super("Add all >> ");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					for (int i = 0; i < m_excluded.size(); i++) {
						m_included.addElement(m_excluded.get(i));
					}
					sortIncludedList();
					// There is not need to order 'm_excluded' since it is empty
					m_excluded.clear();

					updateSelectedFiles();
				}
			});
		}
	}

	/**
	 * Button to move a selection of items from the {@link RightBox} (included
	 * items) to the {@link LeftBox} (excluded items).
	 */
	class RemoveButton extends JButton {

		private static final long serialVersionUID = -7715462009327823823L;

		public RemoveButton() {
			super("< Remove");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					final List<String> selectedElements = m_includeList.getSelectedValuesList();
					for (String selectedElement : selectedElements) {
						m_excluded.addElement(selectedElement);
						sortExcludedList();
						m_included.removeElement(selectedElement);
						sortIncludedList();
					}
					updateSelectedFiles();
				}
			});
		}
	}

	/**
	 * Button to move a selection of items from the {@link RightBox} (included
	 * items) to the {@link LeftBox} (excluded items).
	 */
	class RemoveAllButton extends JButton {

		private static final long serialVersionUID = 4377915230615328383L;

		public RemoveAllButton() {
			super("<< Remove all");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					for (int i = 0; i < m_included.getSize(); i++) {
						m_excluded.addElement(m_included.getElementAt(i));
					}
					sortExcludedList();
					// There is no need to sort 'm_included' since it is emtpy
					m_included.clear();
					updateSelectedFiles();
				}
			});
		}
	}

	/**
	 * Vertical box with buttons to handle the inclusion/exclusion of items.
	 * 
	 * <ul>
	 * <li>{@link AddButton}</li>
	 * <li>{@link AddAllButton}</li>
	 * <li>{@link RemoveButton}</li>
	 * <li>{@link RemoveAllButton}</li>
	 * </ul>
	 */
	class ButtonBox extends Box {

		private static final long serialVersionUID = -5530856056354362424L;

		public ButtonBox() {
			super(BoxLayout.PAGE_AXIS);
			setBorder(BorderFactory.createTitledBorder("Select:"));
			setMinimumSize(new Dimension(200, 300));
			add(Box.createVerticalGlue());
			add(Box.createVerticalStrut(20));
			add(new AddButton());
			add(Box.createVerticalStrut(20));
			add(new AddAllButton());
			add(Box.createVerticalStrut(20));
			add(new RemoveButton());
			add(Box.createVerticalStrut(20));
			add(new RemoveAllButton());
			add(Box.createVerticalStrut(20));
			add(Box.createVerticalGlue());
		}
	}

	/**
	 * Right box with included items. An included item may be excluded double
	 * clicking it in the {@link RightBox}. Then it will left the
	 * {@link RightBox} and join the {@link LeftBox} with the included items.
	 */
	class RightBox extends Box {

		private static final long serialVersionUID = 1866857729449632053L;

		public RightBox() {
			super(BoxLayout.PAGE_AXIS);
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN), "Included:"));
			m_includeList.setMinimumSize(new Dimension(200, 200));
			m_includeList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent me) {
					// Exclude on double click
					if (me.getClickCount() == 2) {
						final String selectedFile = m_includeList.getSelectedValue();
						m_excluded.addElement(selectedFile);
						sortExcludedList();
						m_included.removeElement(selectedFile);
						sortIncludedList();
						updateSelectedFiles();
					}
				}
			});
			// force list to have fixed with
			m_includeList.setFixedCellWidth(200);

			JScrollPane includedScroller = new JScrollPane(m_includeList);
			add(includedScroller);
		}
	}

}