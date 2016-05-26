package de.bund.bfr.knime.pmm.common.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import com.google.common.base.Joiner;

import de.bund.bfr.knime.pmm.common.KnimeUtils;

public class BoxFileSelection extends Box implements ChangeListener {

	private static final long serialVersionUID = 1686534988633504356L;

	// models
	private SettingsModelString m_dir;
	private List<String> m_extensions;
	private SettingsModelStringArray m_selectedFiles;
	private DefaultListModel<String> m_included; // Included files
	private DefaultListModel<String> m_excluded; // Excluded files

	// gui elements
	private DialogComponentFileChooser dirChooser;
	private JList<String> m_includedList; // JList of included files
	private JList<String> m_excludedList; // JList of excluded files

	public BoxFileSelection(SettingsModelString dir, SettingsModelStringArray selectedFiles, String extensions,
			String title) {
		super(BoxLayout.PAGE_AXIS);

		m_dir = dir;

		// Split on \ and skip dot. extensions are formatted with dots:
		// .gif|.jpeg|...
		m_extensions = Arrays.stream(extensions.split("\\|")).map(ext -> ext.substring(1)).collect(Collectors.toList());

		m_selectedFiles = selectedFiles;

		// Inits models
		m_included = new DefaultListModel<>();
		m_excluded = new DefaultListModel<>();

		// Inits gui elements
		m_includedList = new JList<>(m_included);
		m_excludedList = new JList<>(m_excluded);

		dirChooser = new DialogComponentFileChooser(m_dir, "file-directory", JFileChooser.OPEN_DIALOG, true,
				extensions);
		dirChooser.addChangeListener(this);

		Box innerBox = Box.createHorizontalBox();
		innerBox.add(new LeftBox()); // left box with excluded items
		innerBox.add(new ButtonBox()); //
		innerBox.add(new RightBox()); // right box with included items

		setBorder(BorderFactory.createTitledBorder(title));
		add(dirChooser.getComponentPanel());
		add(innerBox);
	}

	/**
	 * If m_dir changes, m_included and m_excluded are cleared and all the files
	 * in m_dir are added to m_excluded.
	 */
	@Override
	public void stateChanged(ChangeEvent event) {
		m_included.clear();
		m_excluded.clear();

		// Add all the files in the selected directory to m_excluded
		SettingsModelString fileChooserModel = (SettingsModelString) dirChooser.getModel();
		String selectedDirectory = fileChooserModel.getStringValue();

		// Updates m_excluded with all the files in selectedDirectory
		File directory;
		try {
			directory = KnimeUtils.getFile(selectedDirectory);

			for (File fileInDirectory : directory.listFiles()) {
				// Filters: only files (not directories) and zip files
				// Not sure if it is necessary since dirChooser already has
				// filters
				String regex = "([^\\s]+(\\.(?i)(" + Joiner.on('|').join(m_extensions) + "))$)";
				if (fileInDirectory.isFile() && fileInDirectory.getName().matches(regex)) {
					m_excluded.addElement(fileInDirectory.getName());
				}
			}
		} catch (InvalidPathException | MalformedURLException e) {
			e.printStackTrace();
		}

		// Update models
		m_dir.setStringValue(selectedDirectory);
		m_selectedFiles.setStringArrayValue(null);
	}

	private void updateSelectedFiles() {
		String[] includedArray = new String[m_included.size()];
		for (int i = 0; i < m_included.size(); i++) {
			includedArray[i] = m_included.get(i);
		}
		m_selectedFiles.setStringArrayValue(includedArray);
	}

	/** Sort alphabetically the elements in {@link #m_included}. */
	private void sortIncludedList() {
		TreeSet<String> orderedList = new TreeSet<>();
		for (int i = 0; i < m_included.size(); i++) {
			orderedList.add(m_included.get(i));
		}

		m_included.clear();
		for (String element : orderedList) {
			m_included.addElement(element);
		}
	}

	/** Sort alphabetically the elements in {@link #m_excluded}. */
	private void sortExcludedList() {
		TreeSet<String> orderedList = new TreeSet<>();
		for (int i = 0; i < m_excluded.size(); i++) {
			orderedList.add(m_excluded.get(i));
		}

		m_excluded.clear();
		for (String element : orderedList) {
			m_excluded.addElement(element);
		}
	}

	/**
	 * Left box with excluded items. An excluded item may be included double
	 * clicking it in the {@link LeftBox}. Then it will left the {@link LeftBox}
	 * and join the {@link RightBox} with the included items.
	 */
	class LeftBox extends Box {
		private static final long serialVersionUID = 3134747434282456042L;

		public LeftBox() {
			super(BoxLayout.PAGE_AXIS);

			Border innerBorder = BorderFactory.createLineBorder(Color.RED);
			Border outterBorder = BorderFactory.createTitledBorder(innerBorder, "Excluded");
			setBorder(outterBorder);

			m_excludedList.setMinimumSize(new Dimension(200, 200));

			// force fixed width for list
			m_excludedList.setFixedCellWidth(200);

			JScrollPane excludedScroller = new JScrollPane(m_excludedList);
			excludedScroller.setMinimumSize(new Dimension(200, 200));
			add(excludedScroller);
		}
	}

	/**
	 * Button to move a selection of excluded files from the {@link LeftBox}
	 * (excluded files) to the {@link RightBox} (included items).
	 */
	class AddButton extends JButton {
		private static final long serialVersionUID = -3023789698578646374L;

		public AddButton() {
			super("Add >");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					final List<String> selectedFiles = m_excludedList.getSelectedValuesList();
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
	 * Button to move all the excluded files from the {@link LeftBox} (excluded
	 * files) to the {@link RightBox} (included items).
	 */
	class AddAllButton extends JButton {
		private static final long serialVersionUID = 1477600311045718096L;

		public AddAllButton() {
			super("Add all >>");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent arg0) {
					for (int i = 0; i < m_excluded.size(); i++) {
						m_included.addElement(m_excluded.get(i));
					}
					sortIncludedList();
					// There is no need to order 'm_excluded' since it is empty
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

		private static final long serialVersionUID = -5831069004661066441L;

		public RemoveButton() {
			super("< Remove");
			setMaximumSize(new Dimension(200, 20));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					final List<String> selectedElements = m_includedList.getSelectedValuesList();
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

		private static final long serialVersionUID = 8386678280862200669L;

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
					// There is no need to sort 'm_included' since it is empty
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
	 * clicking it in the {@link RightBox}. Then it will leave the
	 * {@link RightBox} and join the {@link LeftBox} with the included items.
	 */
	class RightBox extends Box {

		private static final long serialVersionUID = -3243460843724539504L;

		public RightBox() {
			super(BoxLayout.PAGE_AXIS);

			Border innerBorder = BorderFactory.createLineBorder(Color.GREEN);
			Border outerBorder = BorderFactory.createTitledBorder(innerBorder, "Included:");
			setBorder(outerBorder);

			m_includedList.setMinimumSize(new Dimension(200, 20));

			// force list to have fixed width
			m_includedList.setFixedCellWidth(200);

			add(new JScrollPane(m_includedList));
		}
	}
}
