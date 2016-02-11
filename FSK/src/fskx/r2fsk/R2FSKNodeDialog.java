package fskx.r2fsk;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class R2FSKNodeDialog extends DefaultNodeSettingsPane {

  private static final int dialogType = JFileChooser.OPEN_DIALOG; // type of the dialogs
  private static final String rFilters = ".r|.R"; // Extension filters for the R scripts

  protected R2FSKNodeDialog() {


    // File chooser with the path to the model script
    final SettingsModelString modelScriptSettingsModel =
        new SettingsModelString(R2FSKNodeModel.CFGKEY_MODEL_SCRIPT, null);
    final DialogComponentFileChooser modelScriptChooser = new DialogComponentFileChooser(
        modelScriptSettingsModel, "modelScript-history", dialogType, rFilters);
    modelScriptChooser.setBorderTitle("Selected model script");
    addDialogComponent(modelScriptChooser);

    // File chooser with the path to the parameters script
    final SettingsModelString paramScriptSettingsModel =
        new SettingsModelString(R2FSKNodeModel.CFGKEY_PARAM_SCRIPT, null);
    final DialogComponentFileChooser paramScriptChooser = new DialogComponentFileChooser(
        paramScriptSettingsModel, "paramScript-history", dialogType, rFilters);
    paramScriptChooser.setBorderTitle("Selected parameters script");
    addDialogComponent(paramScriptChooser);

    // File chooser with the path to the visualization script
    final SettingsModelString visualizationScriptSettingsModel =
        new SettingsModelString(R2FSKNodeModel.CFGKEY_VISUALIZATION_SCRIPT, null);
    final DialogComponentFileChooser visualizationScriptChooser = new DialogComponentFileChooser(
        visualizationScriptSettingsModel, "visualizationScript-history", dialogType, rFilters);
    visualizationScriptChooser.setBorderTitle("Selected visualization script");
    addDialogComponent(visualizationScriptChooser);

    // File chooser with the path to the meta data document
    final SettingsModelString metaDataDocSettingsModel =
        new SettingsModelString(R2FSKNodeModel.CFGKEY_META_DATA_DOC, null);
    final DialogComponentFileChooser metaDataDocChooser = new DialogComponentFileChooser(
        metaDataDocSettingsModel, "metaDataDoc-history", dialogType, ".sbml|.SBML");
    metaDataDocChooser.setBorderTitle("Selected meta data document");
    addDialogComponent(metaDataDocChooser);
  }
}
