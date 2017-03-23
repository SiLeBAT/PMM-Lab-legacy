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
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/XMLToTable.png?raw=true) XML To Table
- ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Editors.png?raw=true) Editors
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dbdelete/FittedModelDeleter.png?raw=true) DB Data Deleter
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriescreator/MicrobialDataCreator.png?raw=true) Data Creator
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/microbialdataedit/MicrobialDataEditor.png?raw=true) Data Editor
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/FormulaCreator.png?raw=true) Formula Creator
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelCreator.png?raw=true) Model Creator
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelEditor.png?raw=true) Model Editor
- ![](https://raw.githubusercontent.com/SiLeBAT/PMM-Lab/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Model.png) Model Fitting
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.modelestimation/src/de/bund/bfr/knime/pmm/nodes/ModelFitting.png?raw=true) Model Fitting
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelanddatajoiner/PMMJoiner.png?raw=true) PMM Joiner
- ![](https://raw.githubusercontent.com/SiLeBAT/PMM-Lab/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Reader.png) Readers
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseReader.png?raw=true) ComBase Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriesreader/MicrobialDataReader.png?raw=true) Data Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogreader/ModelFormulaReader.png?raw=true) Formula Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelreader/FittedModelReader.png?raw=true) Model Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/numl/NuMLReader.png?raw=true) NuML Reader
    + OpenFSMR Converter
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfreader/fsk/SBMLReader.png?raw=true) PMFX Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/sbmlreader/SBMLReader.png?raw=true) SBML Reader
    + Variable Data Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlstimeseriesreader/XLSMicrobialDataReader.png?raw=true) XLS Data Reader
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlsmodelreader/XLSPrimaryModelReader.png?raw=true) XLS Model Reader
- ![](https://raw.githubusercontent.com/SiLeBAT/PMM-Lab/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/SelectionAndViews.png) Selectors & Viewers
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dataviewandselect/MicrobialDataSelection.png?raw=true) Data Selection
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/fittedparameterview/FittedParameterView.png?raw=true) Fitted Parameter View
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/predictorview/PredictorView.png?raw=true) Predictor View
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/js/modelplotter/modern/PredictorView.png?raw=true) Predictor View JS (deprecated)
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/primarymodelviewandselect/ModelSelectionPrimary.png?raw=true) Primary Model Selection
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarymodelanddataview/ModelViewSecondary.png?raw=true) Secondary Model View
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarypredictorview/SecondaryPredictorView.png?raw=true) Secondary Predictor View
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelselectiontertiary/ModelSelectionTertiary.png?raw=true) Tertiary Model Selection
- ![](https://raw.githubusercontent.com/SiLeBAT/PMM-Lab/master/de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Writer.png) Writers
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseWriter.png?raw=true) ComBase Writer
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeserieswriter/MicrobialDataWriter.png?raw=true) Data Writer
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogwriter/ModelFormulaWriter.png?raw=true) Formula Writer
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelwriter/FittedModelWriter.png?raw=true) Model Writer
    + ![](https://github.com/SiLeBAT/PMM-Lab/blob/master/de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfwriter/fsk/SBMLWriter.png?raw=true) PMFX Writer
    + SedML Writer

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

