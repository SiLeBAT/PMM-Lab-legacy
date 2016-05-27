package de.bund.bfr.knime.pmm.fskx.ui;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import de.bund.bfr.knime.pmm.fskx.ui.MetaDataTable.Row;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

public class MetaDataTable extends JTable {

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
		Model_Dependent_Variable,
		Model_Dependent_Variable_Unit,
		Model_Dependent_Variable_Min,
		Model_Dependent_Variable_Max,
		Independent_Variable,
		Independent_Variable_Units,
		Independent_Variable_Mins,
		Independent_Variable_Maxs,
		Has_Data
	};

	private static final long serialVersionUID = -4683197224648521120L;

	public MetaDataTable(final FSMRTemplate template, final boolean editable) {
		super(new MetaDataTableModel(template, editable));

		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		getModel().addTableModelListener(new TableModelListener() {
//			@Override
//			public void tableChanged(TableModelEvent tableModelEvent) {
//				System.out.println(tableModelEvent);
//			}
//		});
	}

	
}

class MetaDataTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 5174052168162089904L;
	private String[] names = new String[] { "Field", "Value" };

	private FSMRTemplate template;
	private boolean editable;
	
	public MetaDataTableModel(FSMRTemplate template, boolean editable) {
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
			return mrow.name().replace("\\_", " ");
		} else {
			switch (mrow) {
			case Model_Name:
				return template.isSetModelName() ? template.getModelName() : "";
			case Model_Id:
				return template.isSetModelId() ? template.getModelId() : "";
			case Model_Link:
				return template.isSetModelLink() ? template.getModelLink() : "";
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
				return template.isSetReferenceDescriptionLink() ? template.getReferenceDescriptionLink() : null;
			case Model_Created_Date:
				return template.isSetCreatedDate() ? template.getCreatedDate() : null;
			case Model_Modified_Date:
				return template.isSetModifiedDate() ? template.getModifiedDate() : null;
			case Model_Rights:
				return template.isSetRights() ? template.getRights() : "";
			case Model_Notes:
				return template.isSetNotes() ? template.getNotes() : "";
			case Model_Curation_Status:
				return template.isSetCurationStatus() ? template.getCurationStatus() : "";
			case Model_Type:
				return template.isSetModelType() ? template.getModelType() : null;
			case Model_Subject:
				return template.isSetModelSubject() ? template.getModelSubject() : null;
			case Model_Food_Process:
				return template.isSetFoodProcess() ? template.getFoodProcess() : "";
			case Model_Dependent_Variable:
				return template.isSetDependentVariable() ? template.getDependentVariable() : null;
			case Model_Dependent_Variable_Unit:
				return template.isSetDependentVariableUnit() ? template.getDependentVariableUnit() : null;
			case Model_Dependent_Variable_Min:
				return template.isSetDependentVariableMin() ? template.getDependentVariableMin() : null;
			case Model_Dependent_Variable_Max:
				return template.isSetDependentVariableMax() ? template.getDependentVariableMax() : null;
			case Independent_Variable:
				return template.isSetIndependentVariables() ? template.getIndependentVariables() : null;
			case Independent_Variable_Units:
				return template.isSetIndependentVariablesUnits() ? template.getIndependentVariablesUnits() : null;
			case Independent_Variable_Mins:
				return template.isSetIndependentVariablesMins()
						? Arrays.stream(template.getIndependentVariablesMins()).map(d -> ((Double) d).toString())
								.collect(Collectors.joining(","))
						: null;
			case Independent_Variable_Maxs:
				return template.isSetIndependentVariablesMaxs()
						? Arrays.stream(template.getIndependentVariablesMaxs()).map(d -> ((Double) d).toString())
								.collect(Collectors.joining(","))
						: null;
			case Has_Data:
				return template.isSetHasData() ? template.getHasData() : null;
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
		Object newValue = getValueAt(rowIndex, columnIndex);

		switch (row) {
		case Model_Name:
			template.setModelName((String) newValue);
			break;
		case Model_Id:
			template.setModelId((String) newValue);
			break;
		case Model_Link:
			template.setModelLink((URL) newValue);
			break;
		case Organism_Name:
			template.setOrganismName((String) newValue);
			break;
		case Organism_Detail:
			template.setOrganismDetails((String) newValue);
			break;
		case Environment_Name:
			template.setMatrixName((String) newValue);
			break;
		case Environment_Detail:
			template.setMatrixDetails((String) newValue);
			break;
		case Model_Creator:
			template.setCreator((String) newValue);
			break;
		case Model_Family_Name:
			template.setFamilyName((String) newValue);
			break;
		case Model_Contact:
			template.setContact((String) newValue);
			break;
		case Model_Reference_Description:
			template.setReferenceDescription((String) newValue);
			break;
		case Model_Reference_Description_Link:
			template.setReferenceDescriptionLink((URL) newValue);
			break;
		case Model_Created_Date:
			template.setCreatedDate((Date) newValue);
			break;
		case Model_Modified_Date:
			template.setModifiedDate((Date) newValue);
			break;
		case Model_Rights:
			template.setModifiedDate((Date) newValue);
			break;
		case Model_Notes:
			template.setNotes((String) newValue);
			break;
		case Model_Curation_Status:
			template.setCurationStatus((String) newValue);
			break;
		case Model_Type:
			template.setModelType((ModelType) newValue);
			break;
		case Model_Subject:
			template.setModelSubject((ModelClass) newValue);
			break;
		case Model_Food_Process:
			template.setFoodProcess((String) newValue);
			break;
		case Model_Dependent_Variable:
			template.setDependentVariable((String) newValue);
			break;
		case Model_Dependent_Variable_Unit:
			template.setDependentVariableUnit((String) newValue);
			break;
		case Model_Dependent_Variable_Min:
			template.setDependentVariableMin((double) newValue);
			break;
		case Model_Dependent_Variable_Max:
			template.setDependentVariableMax((double) newValue);
			break;
		case Independent_Variable:
			template.setIndependentVariables((String[]) newValue);
			break;
		case Independent_Variable_Units:
			template.setIndependentVariablesUnits((String[]) newValue);
			break;
		case Independent_Variable_Mins:
			template.setIndependentVariablesMins((Double[]) newValue);
			break;
		case Independent_Variable_Maxs:
			template.setIndependentVariablesMaxs((Double[]) newValue);
			break;
		case Has_Data:
			template.setHasData((boolean) newValue);
			break;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable;
	}
}
