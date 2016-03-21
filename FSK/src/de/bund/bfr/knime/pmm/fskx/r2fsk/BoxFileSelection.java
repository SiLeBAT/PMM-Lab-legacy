package de.bund.bfr.knime.pmm.fskx.r2fsk;

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
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

class BoxFileSelection extends Box {

  private static final long serialVersionUID = 1686534988633504356L;
  private SettingsModelString m_dir;
  private SettingsModelStringArray m_selectedFiles;

  private DefaultListModel<String> m_includedFiles = new DefaultListModel<>();
  private DefaultListModel<String> m_excludedFiles = new DefaultListModel<>();

  private JList<String> m_includedFilesList = new JList<>(m_includedFiles);
  private JList<String> m_excludedFilesList = new JList<>(m_excludedFiles);

  protected BoxFileSelection(SettingsModelString dir, SettingsModelStringArray selectedFiles,
      String extensions, String title) {
    super(BoxLayout.PAGE_AXIS);

    m_dir = dir;
    m_selectedFiles = selectedFiles;

    DialogComponentFileChooser dirChooser = new DialogComponentFileChooser(m_dir, "file-directory",
        JFileChooser.OPEN_DIALOG, true, ".zip");
    // adds listener to update the exclude list
    dirChooser.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent event) {
        m_includedFiles.clear();
        m_excludedFiles.clear();

        // Add all the files in the selected directory to m_excludedLibs
        SettingsModelString fileChooserModel = (SettingsModelString) dirChooser.getModel();
        String selectedDirectory = fileChooserModel.getStringValue();

        // Updates m_excludedLibs with all the libraries in selectedDirectory
        File directory = new File(selectedDirectory);
        for (File fileInDirectory : directory.listFiles()) {
          // Filters: only files (not directories) and zip files
          // Not sure if it is necessary since libDirectoryChooser already has filters
          if (fileInDirectory.isFile() && fileInDirectory.getName().endsWith(extensions)) {
            m_excludedFiles.addElement(fileInDirectory.getName());
          }
        }

        // Update models
        m_dir.setStringValue(selectedDirectory);
        m_selectedFiles.setStringArrayValue(null);
      }
    });

    Box innerBox = Box.createHorizontalBox();
    innerBox.add(new LeftBox()); // left box with excluded items
    innerBox.add(new ButtonBox()); //
    innerBox.add(new RightBox()); // right box with included items

    setBorder(BorderFactory.createTitledBorder(title));
    add(dirChooser.getComponentPanel());
    add(innerBox);
  }

  private void updateSelectedLibs() {
    String[] includedLibsArray = new String[m_includedFiles.size()];
    for (int i = 0; i < m_includedFiles.size(); i++) {
      includedLibsArray[i] = m_includedFiles.get(i);
    }
    m_selectedFiles.setStringArrayValue(includedLibsArray);
  }

  /** Sort alphabetically the elements in {@link #m_includedLibs}. */
  private void sortIncludedList() {
    TreeSet<String> orderedList = new TreeSet<>();
    for (int i = 0; i < m_includedFiles.size(); i++) {
      orderedList.add(m_includedFiles.get(i));
    }

    m_includedFiles.clear();
    for (String element : orderedList) {
      m_includedFiles.addElement(element);
    }
    m_includedFilesList.setModel(m_includedFiles);
  }

  /** Sort alphabetically the elements in {@link #m_excludedLibs}. */
  private void sortExcludedList() {
    TreeSet<String> orderedList = new TreeSet<>();
    for (int i = 0; i < m_excludedFiles.size(); i++) {
      orderedList.add(m_excludedFiles.get(i));
    }

    m_excludedFiles.clear();
    for (String element : orderedList) {
      m_excludedFiles.addElement(element);
    }
    m_excludedFilesList.setModel(m_excludedFiles);
  }

  /**
   * Left box with excluded items. An excluded item may be included double clicking it in the
   * {@link LeftBox}. Then it will left the {@link LeftBox} and join the {@link RightBox} with the
   * included items.
   */
  class LeftBox extends Box {
    private static final long serialVersionUID = 3134747434282456042L;

    public LeftBox() {
      super(BoxLayout.PAGE_AXIS);

      Border innerBorder = BorderFactory.createLineBorder(Color.RED);
      Border outterBorder = BorderFactory.createTitledBorder(innerBorder, "Excluded");
      setBorder(outterBorder);

      m_excludedFilesList.setMinimumSize(new Dimension(200, 200));
      m_excludedFilesList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(final MouseEvent mouseEvent) {
          // Include lib on double click
          if (mouseEvent.getClickCount() == 2) {
            final String selectedLib = m_excludedFilesList.getSelectedValue();
            m_excludedFiles.removeElement(selectedLib);
            sortExcludedList();
            m_includedFiles.addElement(selectedLib);
            sortIncludedList();
            updateSelectedLibs();
          }
        }
      });

      // force fixed width for list
      m_excludedFilesList.setFixedCellWidth(200);

      JScrollPane excludedScroller = new JScrollPane(m_excludedFilesList);
      excludedScroller.setMinimumSize(new Dimension(200, 200));
      add(excludedScroller);
    }
  }

  /**
   * Button to move a selection of excluded libraries from the {@link LeftBox} (excluded libraries)
   * to the {@link RightBox} (included items).
   */
  class AddButton extends JButton {
    private static final long serialVersionUID = -3023789698578646374L;

    public AddButton() {
      super("Add >");
      setMaximumSize(new Dimension(200, 20));
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
          final List<String> selectedLibraries = m_excludedFilesList.getSelectedValuesList();
          for (String selectedLibrary : selectedLibraries) {
            m_includedFiles.addElement(selectedLibrary);
            sortIncludedList();
            m_excludedFiles.removeElement(selectedLibrary);
            sortExcludedList();
          }
          updateSelectedLibs();
        }
      });
    }
  }

  /**
   * Button to move all the excluded libraries from the {@link LeftBox} (excluded libraries) to the
   * {@link RightBox} (included items).
   */
  class AddAllButton extends JButton {
    private static final long serialVersionUID = 1477600311045718096L;

    public AddAllButton() {
      super("Add all >>");
      setMaximumSize(new Dimension(200, 20));
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
          for (int i = 0; i < m_excludedFiles.size(); i++) {
            m_includedFiles.addElement(m_excludedFiles.get(i));
          }
          sortIncludedList();
          // There is no need to order 'm_excludedLibs' since it is empty
          m_excludedFiles.clear();

          updateSelectedLibs();
        }
      });
    }
  }

  /**
   * Button to move a selection of items from the {@link RightBox} (included items) to the
   * {@link LeftBox} (excluded items).
   */
  class RemoveButton extends JButton {

    private static final long serialVersionUID = -5831069004661066441L;

    public RemoveButton() {
      super("< Remove");
      setMaximumSize(new Dimension(200, 20));
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          final List<String> selectedElements = m_includedFilesList.getSelectedValuesList();
          for (String selectedElement : selectedElements) {
            m_excludedFiles.addElement(selectedElement);
            sortExcludedList();
            m_includedFiles.removeElement(selectedElement);
            sortIncludedList();
          }
          updateSelectedLibs();
        }
      });
    }
  }

  /**
   * Button to move a selection of items from the {@link RightBox} (included items) to the
   * {@link LeftBox} (excluded items).
   */
  class RemoveAllButton extends JButton {

    private static final long serialVersionUID = 8386678280862200669L;

    public RemoveAllButton() {
      super("<< Remove all");
      setMaximumSize(new Dimension(200, 20));
      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
          for (int i = 0; i < m_includedFiles.getSize(); i++) {
            m_excludedFiles.addElement(m_includedFiles.getElementAt(i));
          }
          sortExcludedList();
          // There is no need to sort 'm_included' since it is empty
          m_includedFiles.clear();
          updateSelectedLibs();
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
   * Right box with included items. An included item may be excluded double clicking it in the
   * {@link RightBox}. Then it will leave the {@link RightBox} and join the {@link LeftBox} with the
   * included items.
   */
  class RightBox extends Box {

    private static final long serialVersionUID = -3243460843724539504L;

    public RightBox() {
      super(BoxLayout.PAGE_AXIS);

      Border innerBorder = BorderFactory.createLineBorder(Color.GREEN);
      Border outerBorder = BorderFactory.createTitledBorder(innerBorder, "Included:");
      setBorder(outerBorder);

      m_includedFilesList.setMinimumSize(new Dimension(200, 20));
      m_includedFilesList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(final MouseEvent me) {
          final String selectedLib = m_includedFilesList.getSelectedValue();
          m_excludedFiles.addElement(selectedLib);
          sortExcludedList();
          m_includedFiles.removeElement(selectedLib);
          sortIncludedList();
          updateSelectedLibs();
        }
      });

      // force list to have fixed width
      m_includedFilesList.setFixedCellWidth(200);

      add(new JScrollPane(m_includedFilesList));
    }
  }
}