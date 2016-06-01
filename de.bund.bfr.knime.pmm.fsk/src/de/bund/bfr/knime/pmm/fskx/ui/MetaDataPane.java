package de.bund.bfr.knime.pmm.fskx.ui;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.knime.core.node.NodeLogger;

import com.google.common.base.Joiner;

import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

public class MetaDataPane extends JScrollPane {

	private static final long serialVersionUID = -3455056721681075796L;

	private static final NodeLogger LOGGER = NodeLogger.getLogger(MetaDataPane.class);

	static enum Row {
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

	FSMRTemplate template;

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
			getColumnModel().getColumn(0).setCellRenderer(new PropertyRenderer());
		}
		
	}
	private static class PropertyRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -7250065472402093670L;

		public void setValue(Object value) {
			setText((String) value);
			setBackground(Color.LIGHT_GRAY);
		}
	}

	private static class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 5174052168162089904L;
		private String[] names = new String[] { "Field", "Value" };

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
			return Row.values().length;
		}

		@Override
		public String getColumnName(int column) {
			return names[column];
		}

		@Override
		public Object getValueAt(int row, int col) {
			Row mrow = Row.values()[row];
			if (col == 0) {
				switch (mrow) {
				case Model_Name:
					return "Model name";
				case Model_Id:
					return "Model id";
				case Model_Link:
					return "Model link";
				case Organism_Name:
					return "Organism name";
				case Organism_Detail:
					return "Organism detail";
				case Environment_Name:
					return "Environment name";
				case Environment_Detail:
					return "Environment detail";
				case Model_Creator:
					return "Model creator";
				case Model_Family_Name:
					return "Model family name";
				case Model_Contact:
					return "Model contact";
				case Model_Reference_Description:
					return "Model reference description";
				case Model_Reference_Description_Link:
					return "Model reference description link";
				case Model_Created_Date:
					return "Creation date";
				case Model_Modified_Date:
					return "Last modification date";
				case Model_Rights:
					return "Model rights";
				case Model_Notes:
					return "Model notes";
				case Model_Curation_Status:
					return "Curation status";
				case Model_Type:
					return "Model type";
				case Model_Subject:
					return "Model subject";
				case Model_Food_Process:
					return "Food process";
				case Dependent_Variable:
					return "Dependent variable";
				case Dependent_Variable_Unit:
					return "Unit of the dependent variable";
				case Dependent_Variable_Min:
					return "Minimum value of the dependent variable";
				case Dependent_Variable_Max:
					return "Maximum value of the dependent variable";
				case Independent_Variable:
					return "Independent variables";
				case Independent_Variable_Units:
					return "Units of the independent variables";
				case Independent_Variable_Mins:
					return "Minimum values of the independent variables";
				case Independent_Variable_Maxs:
					return "Maximum values of the independent variables";
				case Has_Data:
					return "Has data?";
				}
			} else {
				switch (mrow) {
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
					return template.isSetModelType() ? template.getModelType().name() : "";
				case Model_Subject:
					return template.isSetModelSubject() ? template.getModelSubject().fullName() : "";
				case Model_Food_Process:
					return template.isSetFoodProcess() ? template.getFoodProcess() : "";
				case Dependent_Variable:
					return template.isSetDependentVariable() ? template.getDependentVariable().toString() : "";
				case Dependent_Variable_Unit:
					return template.isSetDependentVariableUnit() ? template.getDependentVariableUnit().toString() : "";
				case Dependent_Variable_Min:
					return template.isSetDependentVariableMin() ? Double.toString(template.getDependentVariableMin())
							: "";
				case Dependent_Variable_Max:
					return template.isSetDependentVariableMax() ? Double.toString(template.getDependentVariableMax())
							: "";
				case Independent_Variable:
					return template.isSetIndependentVariables() ? template.getIndependentVariables().toString() : "";
				case Independent_Variable_Units:
					return template.isSetIndependentVariables() ? joiner.join(template.getIndependentVariables()) : "";
				case Independent_Variable_Mins:
					return template.isSetIndependentVariablesMins()
							? Arrays.stream(template.getIndependentVariablesMins()).mapToObj(Double::toString)
									.collect(Collectors.joining("||"))
							: "";
				case Independent_Variable_Maxs:
					return template.isSetIndependentVariablesMaxs()
							? Arrays.stream(template.getIndependentVariablesMaxs()).mapToObj(Double::toString)
									.collect(Collectors.joining("||"))
							: "";
				case Has_Data:
					return template.isSetHasData() ? Boolean.toString(template.getHasData()) : "";
				}
			}
			throw new RuntimeException("Invalid row & col" + row + " " + col);
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

			if (columnIndex != 1) {
				return;
			}
			Row row = Row.values()[rowIndex];
			String stringValue = (String) aValue;

			switch (row) {
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
				try {
					template.setModelType(ModelType.valueOf(stringValue));
				} catch (IllegalArgumentException e) {
					LOGGER.warn("Invalid model type\n" + e.getMessage());
				}
				break;
			case Model_Subject:
				try {
					template.setModelSubject(ModelClass.valueOf(stringValue));
				} catch (IllegalArgumentException e) {
					LOGGER.warn("Invalid model class\n" + e.getMessage());
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
