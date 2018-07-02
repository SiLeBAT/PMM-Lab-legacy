package de.bund.bfr.knime.pmm.pmf2fsk;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

//import de.bund.bfr.knime.pmm.pmfreader.PMFReaderNodeModel;
//import de.bund.bfr.knime.pmm.pmfwriter.PMFWriterNodeSettings;

import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

import com.toedter.calendar.JDateChooser;

import de.bund.bfr.swing.FilePanel;
import de.bund.bfr.swing.StringTextArea;
import de.bund.bfr.swing.StringTextField;
import de.bund.bfr.swing.UI;


public class PMF2FSKNodeDialog extends DefaultNodeSettingsPane{
	 
	private final PMF2FSKNodeSettings settings = new PMF2FSKNodeSettings();
	// Path fields
		private final FilePanel outputPathField;
		private final StringTextField fileNameField;

		// Metadata fields
		

	
	
	
		DialogComponentFileChooser fileChooser;
		DialogComponentFileChooser output_fileChooser;

	  
	  /**
	   * New pane for configuring SBMLReader node dialog.
	   */
	  public PMF2FSKNodeDialog(boolean isPMFX) {
	    // Set model strings
	    final SettingsModelString fileName =
	        new SettingsModelString(PMF2FSKNodeModel.CFGKEY_FILE, "");
	    fileName.setEnabled(true);

	    // Create fileChooser
	    final String fileExtension = isPMFX ? ".pmfx" : ".pmf";
	    fileChooser =
	        new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG,
	            fileExtension);

	    // Add widgets
	    createNewGroup("Data Source");
	    addDialogComponent(fileChooser);

	    // start showing fileChooser
	    fileChooser.getComponentPanel().setVisible(true);

	    
	    // ---- OUTPUT
	    
	    // Init fields
 		outputPathField = new FilePanel("Ouput path", FilePanel.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY, 30);

 		fileNameField = new StringTextField(false, 30);
	
 		final JPanel fileNamePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("File name")),
				Arrays.asList(fileNameField));
	   
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(UI.createWestPanel(outputPathField));
		panel.add(fileNamePanel);
		//createNewGroup("Data Source");
		
		
		//addTab("Output", UI.createNorthPanel(panel));
	  }
		@Override
		protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {

			// Load settings and update dialog
			try {
				this.settings.load(settings);
			} catch (InvalidSettingsException exception) {
				throw new NotConfigurableException(exception.getMessage(), exception);
			}

			// Path fields
			outputPathField.setFileName(this.settings.outPath);
			fileNameField.setText(this.settings.modelName);

			
		}


	  
	  

}
