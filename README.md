![logo](https://foodrisklabs.bfr.bund.de/wp-content/uploads/2015/02/PMM-Lab-Logo_3001.png "FSK-Lab")

Predictive Microbial Modeling Lab (PMM-Lab) is an open source extension to the Konstanz Information Miner (KNIME). PMM-Lab aims to ease and standardize the statistical analysis of experimental microbial data and the development of predictive microbial models (PMM).

It consists of three components:
- a library of KNIME nodes (called PMM-Lab)
- a library of "standard" workflows
- a HSQL databaase to store experimental data and microbial models

## Installation
FSK-Lab may be installed throught the update site [https://dl.bintray.com/silebat/pmmlab]. More information about the installation can be found at the [Food Risk Labs website](https://foodrisklabs.bfr.bund.de/index.php/pmm-lab-installation/)

## Extension nodes
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/XMLToTable.png) Converters
    + ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/XMLToTable.png) XML To Table
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Editors.png) Editors
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dbdelete/FittedModelDeleter.png) DB Data Deleter
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriescreator/MicrobialDataCreator.png) Data Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/microbialdataedit/MicrobialDataEditor.png) Data Editor
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/FormulaCreator.png) Formula Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelCreator.png) Model Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelEditor.png) Model Editor
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Model.png) Model Fitting
    + ![](de.bund.bfr.knime.pmm.modelestimation/src/de/bund/bfr/knime/pmm/nodes/ModelFitting.png) Model Fitting
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelanddatajoiner/PMMJoiner.png) PMM Joiner
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Reader.png) Readers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseReader.png) ComBase Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriesreader/MicrobialDataReader.png) Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogreader/ModelFormulaReader.png) Formula Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelreader/FittedModelReader.png) Model Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/numl/NuMLReader.png) NuML Reader
    + OpenFSMR Converter
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfreader/fsk/SBMLReader.png) PMFX Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/sbmlreader/SBMLReader.png) SBML Reader
    + Variable Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlstimeseriesreader/XLSMicrobialDataReader.png) XLS Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlsmodelreader/XLSPrimaryModelReader.png) XLS Model Reader
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/SelectionAndViews.png) Selectors & Viewers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dataviewandselect/MicrobialDataSelection.png?raw=true) Data Selection
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/fittedparameterview/FittedParameterView.png?raw=true) Fitted Parameter View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/predictorview/PredictorView.png?raw=true) Predictor View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/js/modelplotter/modern/PredictorView.png?raw=true) Predictor View JS (deprecated)
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/primarymodelviewandselect/ModelSelectionPrimary.png?raw=true) Primary Model Selection
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarymodelanddataview/ModelViewSecondary.png?raw=true) Secondary Model View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarypredictorview/SecondaryPredictorView.png?raw=true) Secondary Predictor View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelselectiontertiary/ModelSelectionTertiary.png?raw=true) Tertiary Model Selection
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Writer.png) Writers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseWriter.png?raw=true) ComBase Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeserieswriter/MicrobialDataWriter.png?raw=true) Data Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogwriter/ModelFormulaWriter.png?raw=true) Formula Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelwriter/FittedModelWriter.png?raw=true) Model Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfwriter/fsk/SBMLWriter.png?raw=true) PMFX Writer

## Eclipse plugin projects:
- *com.jgoodies*
- *com.thoughtworks.xstream*
- *de.bund.bfr.formats*
- *de.bund.bfr.knime.pmm.bfrdbiface.lib*
- *de.bund.bfr.knime.pmm.common*
- *de.bund.bfr.knime.pmm.nodes*
- *de.bund.bfr.knime.pmm.sbml.test*
- *de.bund.bfr.knime.pmm.target*
- *de.bund.bfr.knime.pmm.tests*
- *de.bund.bfr.knime.pmmlab.update.p2*
- *de.bund.bfr.knime.pmmlab.update.p2.deploy*
- *JSBML_PMF*
- *net.sf.jabref*
- *org.freixas.jcalendar*
- *org.javers*
- *org.jdom2*
- *PMM_FEAT*
- *quick.dbtable*

