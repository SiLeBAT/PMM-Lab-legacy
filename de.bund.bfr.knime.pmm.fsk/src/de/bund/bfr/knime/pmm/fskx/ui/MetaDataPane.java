package de.bund.bfr.knime.pmm.fskx.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.knime.core.node.NodeLogger;

import com.google.common.base.Joiner;

import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class MetaDataPane extends JScrollPane {

	private static final long serialVersionUID = -3455056721681075796L;

	private static final NodeLogger LOGGER = NodeLogger.getLogger(MetaDataPane.class);
	
	private final static Map<ModelType, String> modelTypeStrings;

	static {
		// model type strings
		modelTypeStrings = new LinkedHashMap<>();
		modelTypeStrings.put(ModelType.EXPERIMENTAL_DATA, "Experimental data");
		modelTypeStrings.put(ModelType.PRIMARY_MODEL_WDATA, "Primary model with data");
		modelTypeStrings.put(ModelType.PRIMARY_MODEL_WODATA, "Primary model without data");
		modelTypeStrings.put(ModelType.TWO_STEP_SECONDARY_MODEL, "Two step secondary model");
		modelTypeStrings.put(ModelType.ONE_STEP_SECONDARY_MODEL, "One step secondary model");
		modelTypeStrings.put(ModelType.MANUAL_SECONDARY_MODEL, "Manual secondary model");
		modelTypeStrings.put(ModelType.TWO_STEP_TERTIARY_MODEL, "Two step tertiary model");
		modelTypeStrings.put(ModelType.ONE_STEP_TERTIARY_MODEL, "One step tertiary model");
		modelTypeStrings.put(ModelType.MANUAL_TERTIARY_MODEL, "Manual tertiary model");	
	}

	private static enum Col {
		Model_Name,
		Model_Id,
		Model_Link,
		Organism_Name,
		Organism_Detail,
		Environment_Name,
		Environment_Detail,
		Model_Creator,
		Model_Family_Name,
		Model_Contact,
		Model_Reference_Description,
		Model_Reference_Description_Link,
		Model_Created_Date,
		Model_Modified_Date,
		Model_Rights,
		Model_Notes,
		Model_Curation_Status,
		Model_Type,
		Model_Subject,
		Model_Food_Process,
		Dependent_Variable,
		Dependent_Variable_Unit,
		Dependent_Variable_Min,
		Dependent_Variable_Max,
		Independent_Variable,
		Independent_Variable_Units,
		Independent_Variable_Mins,
		Independent_Variable_Maxs,
		Has_Data
	};

	private final FSMRTemplate template;

	public MetaDataPane(FSMRTemplate template, boolean editable) {
		super(new Table(template, editable));
		this.template = template;
	}

	public FSMRTemplate getMetaData() {
		return template;
	}

	private static class Table extends JTable {
		private static final long serialVersionUID = 8776004658791577404L;

		public Table(FSMRTemplate template, boolean editable) {
			super(new TableModel(template, editable));

			TableColumnModel columnModel = getColumnModel();
			
			// Set columns witdth
			for (int ncol = 0; ncol < getColumnCount(); ncol++) {
				columnModel.getColumn(ncol).setPreferredWidth(150);
			}
			setAutoResizeMode(AUTO_RESIZE_OFF);
			
			// Set special editors
			columnModel.getColumn(Col.Model_Type.ordinal()).setCellEditor(new ModelTypeEditor());
			columnModel.getColumn(Col.Model_Subject.ordinal()).setCellEditor(new ModelSubjectEditor());
		}
	}
	
	private static class ModelTypeEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 2923508881330612951L;
		private static JComboBox<String> comboBox;

		static {
			comboBox = new JComboBox<>();
			modelTypeStrings.values().forEach(modelType -> comboBox.addItem(modelType));
			comboBox.addItem("");  // empty string for non defined model types
		}

		public ModelTypeEditor() {
			super(comboBox);
		}
	}
	
	private static class ModelSubjectEditor extends DefaultCellEditor {
		
		private static final long serialVersionUID = -3451495357854026436L;
		private static JComboBox<String> comboBox;
		
		static {
			comboBox =  new JComboBox<>();
			Arrays.stream(ModelClass.values()).forEach(modelClass -> comboBox.addItem(modelClass.fullName()));
			comboBox.addItem("");  // empty string for non defined model class
		}
		
		public ModelSubjectEditor() {
			super(comboBox);
		}
	}

	private static class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 5174052168162089904L;
		private static String[] names;

		static {
			// Populate column names
			names = new String[Col.values().length];
			names[Col.Model_Name.ordinal()] = "Model name";
			names[Col.Model_Id.ordinal()] = "Model id";
			names[Col.Model_Link.ordinal()] = "Model link";
			names[Col.Organism_Name.ordinal()] = "Organism name";
			names[Col.Organism_Detail.ordinal()] = "Organism detail";
			names[Col.Environment_Name.ordinal()] = "Environment name";
			names[Col.Environment_Detail.ordinal()] = "Environment detail";
			names[Col.Model_Creator.ordinal()] = "Model creator";
			names[Col.Model_Family_Name.ordinal()] = "Model family name";
			names[Col.Model_Contact.ordinal()] = "Model contact";
			names[Col.Model_Reference_Description.ordinal()] = "Reference description";
			names[Col.Model_Reference_Description_Link.ordinal()] = "Reference description link";
			names[Col.Model_Created_Date.ordinal()] = "Created date";
			names[Col.Model_Modified_Date.ordinal()] = "Modified date";
			names[Col.Model_Rights.ordinal()] = "Rights";
			names[Col.Model_Notes.ordinal()] = "Notes";
			names[Col.Model_Curation_Status.ordinal()] = "Curation status";
			names[Col.Model_Type.ordinal()] = "Model type";
			names[Col.Model_Subject.ordinal()] = "Model subject";
			names[Col.Model_Food_Process.ordinal()] = "Food process";
			names[Col.Dependent_Variable.ordinal()] = "Dependent variable";
			names[Col.Dependent_Variable_Unit.ordinal()] = "Dependet variable unit";
			names[Col.Dependent_Variable_Min.ordinal()] = "Dependent variable minimum value";
			names[Col.Dependent_Variable_Max.ordinal()] = "Dependent variable maximum value";
			names[Col.Independent_Variable.ordinal()] = "Independent variables";
			names[Col.Independent_Variable_Units.ordinal()] = "Independent variable units";
			names[Col.Independent_Variable_Mins.ordinal()] = "Independent variable minimum values";
			names[Col.Independent_Variable_Maxs.ordinal()] = "Independent variable maximum values";
			names[Col.Has_Data.ordinal()] = "Has data?";
		}

		private FSMRTemplate template;
		private boolean editable;

		private static final Joiner joiner = Joiner.on("||"); // utility joiner
		private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

		public TableModel(FSMRTemplate template, boolean editable) {
			this.template = template;
			this.editable = editable;
		}

		@Override
		public int getColumnCount() {
			return names.length;
		}

		@Override
		public int getRowCount() {
			return 1; // so far only one template is supported
		}

		@Override
		public String getColumnName(int column) {
			return names[column];
		}

		@Override
		public Object getValueAt(int row, int col) {
			switch (Col.values()[col]) {
			case Model_Name:
				return template.isSetModelName() ? template.getModelName() : "";
			case Model_Id:
				return template.isSetModelId() ? template.getModelId() : "";
			case Model_Link:
				return template.isSetModelLink() ? template.getModelLink().toString() : "";
			case Organism_Name:
				return template.isSetOrganismName() ? template.getOrganismName() : "";
			case Organism_Detail:
				return template.isSetOrganismDetails() ? template.getOrganismDetails() : "";
			case Environment_Name:
				return template.isSetMatrixName() ? template.getMatrixName() : "";
			case Environment_Detail:
				return template.isSetMatrixDetails() ? template.getMatrixDetails() : "";
			case Model_Creator:
				return template.isSetCreator() ? template.getCreator() : "";
			case Model_Family_Name:
				return template.isSetFamilyName() ? template.getFamilyName() : "";
			case Model_Contact:
				return template.isSetContact() ? template.getContact() : "";
			case Model_Reference_Description:
				return template.isSetReferenceDescription() ? template.getReferenceDescription() : "";
			case Model_Reference_Description_Link:
				return template.isSetReferenceDescriptionLink() ? template.getReferenceDescriptionLink().toString()
						: "";
			case Model_Created_Date:
				return template.isSetCreatedDate() ? dateFormat.format(template.getCreatedDate()) : "";
			case Model_Modified_Date:
				return template.isSetModifiedDate() ? template.getModifiedDate().toString() : "";
			case Model_Rights:
				return template.isSetRights() ? template.getRights() : "";
			case Model_Notes:
				return template.isSetNotes() ? template.getNotes() : "";
			case Model_Curation_Status:
				return template.isSetCurationStatus() ? template.getCurationStatus() : "";
			case Model_Type:
				return template.isSetModelType() ? modelTypeStrings.get(template.getModelType()) : "";
			case Model_Subject:
				return template.isSetModelSubject() ? template.getModelSubject().fullName() : "";
			case Model_Food_Process:
				return template.isSetFoodProcess() ? template.getFoodProcess() : "";
			case Dependent_Variable:
				return template.isSetDependentVariable() ? template.getDependentVariable().toString() : "";
			case Dependent_Variable_Unit:
				return template.isSetDependentVariableUnit() ? template.getDependentVariableUnit().toString() : "";
			case Dependent_Variable_Min:
				return template.isSetDependentVariableMin() ? Double.toString(template.getDependentVariableMin()) : "";
			case Dependent_Variable_Max:
				return template.isSetDependentVariableMax() ? Double.toString(template.getDependentVariableMax()) : "";
			case Independent_Variable:
				return template.isSetIndependentVariables() ? template.getIndependentVariables().toString() : "";
			case Independent_Variable_Units:
				return template.isSetIndependentVariables() ? joiner.join(template.getIndependentVariables()) : "";
			case Independent_Variable_Mins:
				return template.isSetIndependentVariablesMins() ? Arrays.stream(template.getIndependentVariablesMins())
						.mapToObj(Double::toString).collect(Collectors.joining("||")) : "";
			case Independent_Variable_Maxs:
				return template.isSetIndependentVariablesMaxs() ? Arrays.stream(template.getIndependentVariablesMaxs())
						.mapToObj(Double::toString).collect(Collectors.joining("||")) : "";
			case Has_Data:
				return template.isSetHasData() ? Boolean.toString(template.getHasData()) : "";
			}
			throw new RuntimeException("Invalid row & col" + row + " " + col);
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

			String stringValue = (String) aValue;

			switch (Col.values()[columnIndex]) {
			case Model_Name:
				template.setModelName(stringValue);
				break;
			case Model_Id:
				template.setModelId(stringValue);
				break;
			case Model_Link:
				try {
					template.setModelLink(new URL(stringValue));
				} catch (MalformedURLException e) {
					LOGGER.warnWithFormat("Invalid url: %s", e);
				}
				break;
			case Organism_Name:
				template.setOrganismName(stringValue);
				break;
			case Organism_Detail:
				template.setOrganismDetails(stringValue);
				break;
			case Environment_Name:
				template.setMatrixName(stringValue);
				break;
			case Environment_Detail:
				template.setMatrixDetails(stringValue);
				break;
			case Model_Creator:
				template.setCreator(stringValue);
				break;
			case Model_Family_Name:
				template.setFamilyName(stringValue);
				break;
			case Model_Contact:
				template.setContact(stringValue);
				break;
			case Model_Reference_Description:
				template.setReferenceDescription(stringValue);
				break;
			case Model_Reference_Description_Link:
				try {
					URL descriptionLink = new URL(stringValue);
					template.setReferenceDescriptionLink(descriptionLink);
				} catch (MalformedURLException e) {
					LOGGER.warn("Invalid url\n" + e.getMessage());
				}
				break;
			case Model_Created_Date:
				try {
					template.setCreatedDate(dateFormat.parse(stringValue));
				} catch (ParseException e) {
					LOGGER.warn("Invalid date");
				}
				break;
			case Model_Modified_Date:
				try {
					template.setModifiedDate(dateFormat.parse(stringValue));
				} catch (ParseException e) {
					LOGGER.warn("Invalid date");
				}
				break;
			case Model_Rights:
				template.setRights(stringValue);
				break;
			case Model_Notes:
				template.setNotes(stringValue);
				break;
			case Model_Curation_Status:
				template.setCurationStatus(stringValue);
				break;
			case Model_Type:
				if (stringValue.isEmpty()) {
					template.unsetModelType();
				} else {
					for (Map.Entry<ModelType, String> entry : modelTypeStrings.entrySet()) {
						if (stringValue.equals(entry.getValue())) {
							template.setModelType(entry.getKey());
							break;
						}
					}
				}
				break;
			case Model_Subject:
				if (stringValue.isEmpty()) {
					template.unsetModelSubject();
				} else {
					template.setModelSubject(ModelClass.fromName(stringValue));
				}
				break;
			case Model_Food_Process:
				template.setFoodProcess(stringValue);
				break;
			case Dependent_Variable:
				template.setDependentVariable(stringValue);
				break;
			case Dependent_Variable_Unit:
				template.setDependentVariableUnit(stringValue);
				break;
			case Dependent_Variable_Min:
				try {
					template.setDependentVariableMin(Double.parseDouble(stringValue));
				} catch (NullPointerException | NumberFormatException e) {
					LOGGER.warn("NaN");
				}
				break;
			case Dependent_Variable_Max:
				try {
					template.setDependentVariableMax(Double.parseDouble(stringValue));
				} catch (NullPointerException | NumberFormatException e) {
					LOGGER.warn("NaN");
				}
				break;
			case Independent_Variable:
				template.setIndependentVariables(stringValue.split("||"));
				break;
			case Independent_Variable_Units:
				template.setIndependentVariablesUnits(stringValue.split("||"));
				break;
			case Independent_Variable_Mins:
				try {
					double[] mins = Arrays.stream(stringValue.split("||")).mapToDouble(Double::parseDouble).toArray();
					template.setIndependentVariablesMins(mins);
				} catch (NumberFormatException e) {
					LOGGER.warn("NaN");
				}
				break;
			case Independent_Variable_Maxs:
				try {
					double[] maxs = Arrays.stream(stringValue.split("||")).mapToDouble(Double::parseDouble).toArray();
					template.setIndependentVariablesMaxs(maxs);
				} catch (NumberFormatException e) {
					LOGGER.warn("NaN");
				}
				break;
			case Has_Data:
				template.setHasData(Boolean.parseBoolean(stringValue));
				break;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return editable;
		}
	}
}
