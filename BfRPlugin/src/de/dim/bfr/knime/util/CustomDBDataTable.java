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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;

public class CustomDBDataTable implements DataTable
{
	private ResultSet m_result;
	private DataTableSpec m_spec;
	
	public CustomDBDataTable(ResultSet result) 
	{
		try {
			this.m_spec = PluginUtils.createTableSpec(result.getMetaData());
			this.m_result = result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public DataTableSpec getDataTableSpec() {
		return m_spec;
	}

	@Override
	public RowIterator iterator() {
		return new CustomDBRowIterator(m_spec, m_result);
	}

}
