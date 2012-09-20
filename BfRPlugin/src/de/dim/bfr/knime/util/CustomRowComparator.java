/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.dim.bfr.knime.util;

import java.util.Comparator;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataValueComparator;

public class CustomRowComparator implements Comparator<DataRow> {

    private int[] m_indices;
    private DataTableSpec m_spec;
    private boolean[] m_sortAscending;
    
	public CustomRowComparator(DataTableSpec spec, int[] indices, boolean[] sortAscending) 
	{
		this.m_indices 			= indices;
		this.m_sortAscending 	= sortAscending;
		this.m_spec 			= spec;
		
	}
    
    @Override
    public int compare(final DataRow dr1, final DataRow dr2) {

        if (dr1 == dr2) {
            return 0;
        }
        if (dr1 == null) {
            return 1;
        }
        if (dr2 == null) {
            return -1;
        }

        assert (dr1.getNumCells() == dr2.getNumCells());

        for (int i = 0; i < m_indices.length; i++) {

            // only if the cell is in the includeList
            // -1 is RowKey!
            int cellComparison = 0;
            if (m_indices[i] == -1) {
                String k1 = dr1.getKey().getString();
                String k2 = dr2.getKey().getString();
                cellComparison = k1.compareTo(k2);
            } else {
                final DataValueComparator comp =
                        m_spec.getColumnSpec(m_indices[i]).getType()
                                .getComparator();
                // same column means that they have the same type
                cellComparison =
                        comp.compare(dr1.getCell(m_indices[i]), dr2
                                .getCell(m_indices[i]));
            }
            if (cellComparison != 0) {
                return (m_sortAscending[i] ? cellComparison
                        : -cellComparison);
            }
        }
        return 0; // all cells in the DataRow have the same value
    }
}
