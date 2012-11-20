/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
package de.bund.bfr.knime.foodprocess.ui;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.hsh.bfr.db.DBKernel;

public class MyPageOne extends WizardPage {
	private HashMap<Integer, Button> checkBoxes;
	private Composite container;

	public MyPageOne() {
		super("Choose process chain");
		setTitle("Choose process chain");
		setDescription("Process chain chooser");
	}

	@Override
	public void createControl(final Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		/*
		text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
		text1.setText("");
		text1.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent e) {
			}

			@Override
			public void keyReleased(final KeyEvent e) {
				if (!text1.getText().isEmpty()) {
					setPageComplete(true);

				}
			}

		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text1.setLayoutData(gd);
		*/

		checkBoxes = new HashMap<Integer, Button>();

		DBKernel.getLocalConn(true);
		ResultSet rs = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL("ProzessWorkflow"), false);
		try {
			if (rs != null && rs.first()) {
				do {
					if (rs.getObject("Name") != null) {
						Button cb = new Button(container, SWT.CHECK);
						cb.setText(rs.getString("Name"));
						checkBoxes.put(rs.getInt("ID"), cb);						
					}
				} while (rs.next());
			}
		}
		catch (Exception e) {e.printStackTrace();}

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);
		
	}

	public ArrayList<Integer> getPCs() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Integer key : checkBoxes.keySet()) {
		    Button cb = checkBoxes.get(key);
		    if (cb.getSelection()) {
				result.add(key);
			}
		}
		return result;
	}

}