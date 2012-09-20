package de.dim.bfr.knime.nodes.loadrelateddata;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.knime.core.node.InvalidSettingsException;

public class SelectionHandler {
	
	private SelectionHandler() {
	}

	public static int[] getSelection(JTable table) throws InvalidSettingsException {
		if (hasNullModel(table) || hasNoDataMark(table) && table.getSelectedRow() == 0)
				throw new InvalidSettingsException("There is no data available to perform this operation");
		else if (table != null) {
			int[] rows = table.getSelectedRows();
			int[] selectedIds = new int[table.getSelectedRowCount()];
			for (int i = 0; i < rows.length; i++)
				selectedIds[i] = Integer.parseInt(table.getModel()
						.getValueAt(rows[i], 0).toString());
			return selectedIds;
		}else 
			return null;
	}
	
	public static void setSelection(JTable table, int[] selection) {
		List<Integer> selectionList = new ArrayList<Integer>();
		for (int i = 0; i < table.getModel().getRowCount(); i++) {
			for (int j : selection)
				if (Integer.parseInt(table.getModel().getValueAt(i, 0).toString()) == j)
					selectionList.add(i);
		}
		int[] selectables = new int[selectionList.size()];
		for (int i = 0; i <selectionList.size(); i++) 
			selectables[i] = selectionList.get(i);
		loadSelection(table, selectables);
	}
	
	private static void loadSelection(JTable table, int[] selectables) {
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		for (int i : selectables)
			table.changeSelection(i, 0, true, false);
	}
	
	private static boolean hasNullModel(JTable table) {
		return table.getModel().getColumnCount() == 0;
	}

	private static boolean hasNoDataMark(JTable table) {
		return table.getModel().getValueAt(0, 0).equals("no data");
	}
	
}
