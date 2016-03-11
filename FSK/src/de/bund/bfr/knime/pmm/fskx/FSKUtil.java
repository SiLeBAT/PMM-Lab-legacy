package de.bund.bfr.knime.pmm.fskx;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;

public class FSKUtil {

  private FSKUtil() {}

  public static DataTableSpec createFSKTableSpec() {
    final String[] columnNames = {"model", "params", "visualization", "RLibraries", "RSources"};
    final DataType[] columnTypes =
        {StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE};
    final DataColumnSpec[] columnSpecs = DataTableSpec.createColumnSpecs(columnNames, columnTypes);

    return new DataTableSpec(columnSpecs);
  }
}
