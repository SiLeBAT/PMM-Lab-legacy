/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

public class ModelReaderUi extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 20120828;

	public static final String PARAM_LEVEL = "level";
	public static final String PARAM_MODELCLASS = "modelClass";
	public static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	public static final String PARAM_MODELLIST = "modelList";

	protected static final String LABEL_UNSPEC = "Unspecified only";
	private static final String LABEL_PRIM = "Primary";
	private static final String LABEL_SEC = "Secondary";
	private static final String LABEL_TERT = "Combined Primary/Secondary";
	private static final int LEVEL_PRIM = 1;
	private static final int LEVEL_SEC = 2;

	private JCheckBox modelNameSwitch;
	private JComboBox<String> levelBox, classBox;
	private JPanel modelPanel;
	private JPanel panel;

	private HashMap<String, Integer> modelIdPrim;
	private HashMap<String, Integer> modelIdSec;
	private LinkedHashMap<JCheckBox, String> modelBoxSetPrim;
	private LinkedHashMap<JCheckBox, String> modelBoxSetSec;

	public ModelReaderUi() {
		this(false);
	}
	public ModelReaderUi(boolean fitted) {

		JPanel panel0;

		clearModelSet();

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 200));

		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Level"));
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(250, 45));
		add(panel, BorderLayout.NORTH);

		panel0 = new JPanel();
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
		panel.add(panel0);

		panel0.add(new JLabel("Level   "));

		if (fitted) levelBox = new JComboBox<String>(new String[] { LABEL_PRIM, LABEL_TERT });
		else levelBox = new JComboBox<String>(new String[] { LABEL_PRIM, LABEL_SEC });
		levelBox.addActionListener(this);
		levelBox.setPreferredSize(new Dimension(50, 25));
		panel0.add(levelBox);

		panel0.add(new JLabel("Model class   "));

		classBox = new JComboBox<String>(new String[] {"All","growth","inactivation","survival"});
		classBox.addActionListener(this);
		classBox.setPreferredSize(new Dimension(50, 25));
		panel0.add(classBox);

		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Model"));
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(250, 150));
		add(panel, BorderLayout.CENTER);

		modelNameSwitch = new JCheckBox("Filter by formula");
		modelNameSwitch.addActionListener(this);
		panel.add(modelNameSwitch, BorderLayout.NORTH);

		/*
		 * modelNameBox = new JComboBox(); modelNameBox.setEnabled( false );
		 * panel.add( modelNameBox, BorderLayout.CENTER );
		 */

		modelPanel = new JPanel();
		modelPanel.setLayout(new GridLayout(0, 1));
		// modelPanel.setPreferredSize( new Dimension( 150, 200 ) );
		JScrollPane pane;

		pane = new JScrollPane(modelPanel);
		// pane.setPreferredSize( new Dimension( 200, 10 ) );
		panel.add(pane);
	}

	public void clearModelSet() {
		modelIdPrim = new HashMap<String, Integer>();
		modelIdSec = new HashMap<String, Integer>();
		modelBoxSetPrim = new LinkedHashMap<JCheckBox, String>();
		modelBoxSetSec = new LinkedHashMap<JCheckBox, String>();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == modelNameSwitch) {
			updateModelNameEnabled();
			return;
		}
		if (arg0.getSource() == levelBox) {
			updateModelName();
			return;
		}
		if (arg0.getSource() == classBox) {
			updateModelName();
			return;
		}
	}

	public void addModelPrim(final int id, final String name, final String modelType) throws PmmException {
		if (name == null) throw new PmmException("Model name must not be null.");

		modelIdPrim.put(name + " (" + modelType + ")", id);
		modelBoxSetPrim.put(new JCheckBox(name + " (" + modelType + ")"), modelType);
		updateModelName();
	}

	public void addModelSec(final int id, final String name, final String modelType) throws PmmException {
		if (name == null) throw new PmmException("Model name must not be null.");

		modelIdSec.put(name + " (" + modelType + ")", id);
		modelBoxSetSec.put(new JCheckBox(name + " (" + modelType + ")"), modelType);
		updateModelName();
	}

	public int getLevel() {
		return levelBox.getSelectedIndex() + 1;
	}
	public String getModelClass() {
		return classBox.getSelectedItem().toString();
	}

	public boolean isPrim() {
		return getLevel() == LEVEL_PRIM;
	}

	public boolean isSec() {
		return getLevel() == LEVEL_SEC;
	}

	public boolean isModelFilterEnabled() {
		return modelNameSwitch.isSelected();
	}

	public boolean modelNameEnabled(final String name) {
		for (JCheckBox box : modelBoxSetPrim.keySet()) {
			if (box.getText().equals(name)) return true;		
			if (box.getText().startsWith(name) && box.getText().lastIndexOf(" (") == name.length())  return true;
		}

		for (JCheckBox box : modelBoxSetSec.keySet()) {
			if (box.getText().equals(name)) return true;			
			if (box.getText().startsWith(name) && box.getText().lastIndexOf(" (") == name.length())  return true;
		}

		return false;
	}

	public void setLevel(int level) throws PmmException {
		//if (!(level == 1 || level == 2)) throw new PmmException("Level must be in {1, 2}");

		if (level == 1 || level == 2) levelBox.setSelectedIndex(level - 1);
	}
	public void setModelClass(String modelClass) throws PmmException {
		classBox.setSelectedItem(modelClass == null || modelClass.isEmpty() ? "All" : modelClass);
	}

	private void updateModelName() {
		modelPanel.removeAll();
		modelPanel.setVisible(false);

		if (isPrim()) addBoxes2Panel(modelBoxSetPrim, modelPanel);
		else addBoxes2Panel(modelBoxSetSec, modelPanel);

		updateModelNameEnabled();

		modelPanel.setVisible(true);
		panel.validate();
	}
	private void addBoxes2Panel(LinkedHashMap<JCheckBox, String> modelBox, JPanel modelPanel) {
		for (JCheckBox box : modelBox.keySet()) {
			Object o = classBox.getSelectedItem();
			int indexKlammer = 0;
			int indexKeyword = 1;
			if (box.getText() != null && !box.getText().isEmpty() && o != null) {
				indexKlammer = box.getText().lastIndexOf(" (");
				indexKeyword = box.getText().toLowerCase().lastIndexOf(o.toString().toLowerCase());				
			}
			if (o == null || o.toString().equals("All") ||
					indexKeyword > indexKlammer) {
				modelPanel.add(box);			
			}
		}
	}

	private void updateModelNameEnabled() {
		if (modelNameSwitch.isSelected()) {
			for (JCheckBox box : modelBoxSetPrim.keySet()) box.setEnabled(true);
			for (JCheckBox box : modelBoxSetSec.keySet()) box.setEnabled(true);
		} else {
			for (JCheckBox box : modelBoxSetPrim.keySet()) box.setEnabled(false);
			for (JCheckBox box : modelBoxSetSec.keySet()) box.setEnabled(false);
		}
	}

	public boolean complies(KnimeTuple tuple) throws PmmException {

		if( isModelFilterEnabled() ) {
			PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MODELCATALOG, tuple.getSchema().conforms(new Model1Schema()) ? 1 : 2));
			if (x != null) {
				for (PmmXmlElementConvertable el : x.getElementSet()) {
					if (el instanceof CatalogModelXml) {
						CatalogModelXml cmx = (CatalogModelXml) el;
						if (modelNameEnabled(cmx.getName())) return true;
						break;
					}
				}
			}
		}
		else
			return true;
		/*
			if (modelNameEnabled(tuple.getString(Model1Schema.ATT_MODELNAME)))
				return true;
*/
		return false;
	}

	public String getModelList() {
		String ret = "";

		for (JCheckBox box : modelBoxSetPrim.keySet()) {
			if (box.isSelected()) {
				if (!ret.isEmpty()) ret += ",";
				ret += modelIdPrim.get(box.getText());
			}
		}

		for (JCheckBox box : modelBoxSetSec.keySet()) {
			if (box.isSelected()) {
				if (!ret.isEmpty()) ret += ",";
				ret += modelIdSec.get(box.getText());
			}			
		}
		
		return ret;
	}

	public void enableModelList(final String idlist) {
		if (idlist == null || idlist.isEmpty()) return;
		// disable everything
		for (JCheckBox box : modelBoxSetPrim.keySet()) {
			box.setSelected(false);
		}
		for (JCheckBox box : modelBoxSetSec.keySet()) {
			box.setSelected(false);
		}

		String[] token = idlist.split(",");
		// enable model if appropriate
		for (JCheckBox box : modelBoxSetPrim.keySet()) {
			for (String id : token) {
				if (Integer.valueOf(id) == modelIdPrim.get(box.getText())) {
					box.setSelected(true);
					break;
				}
			}
		}

		for (JCheckBox box : modelBoxSetSec.keySet()) {
			for (String id : token) {
				if (Integer.valueOf(id) == modelIdSec.get(box.getText())) {
					box.setSelected(true);
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return getModelList();
	}

	public void setModelFilterEnabled(final boolean en) {

		if (en != isModelFilterEnabled())
			modelNameSwitch.doClick();
	}

	public static boolean passesFilter(final String modelList, final KnimeTuple tuple, final int level) throws PmmException {

		if (modelList.isEmpty())
			return false;

		Integer id = null;
		PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MODELCATALOG, level));
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof CatalogModelXml) {
					CatalogModelXml cmx = (CatalogModelXml) el;
					id = cmx.getId();
					break;
				}
			}
		}
		/*
		if (tuple.getSchema().conforms(new Model1Schema()))
			id = tuple.getInt(Model1Schema.ATT_MODELID);
		else
			id = tuple.getInt(Model2Schema.ATT_MODELID);
		 */
		
		String[] token = modelList.split(",");
		for (String candidate : token)
			if (Integer.valueOf(candidate) == id)
				return true;

		return false;
	}

	public void addLevelListener(ActionListener listener) {
		levelBox.addActionListener(listener);
	}

    public void saveSettingsTo(Config c) {
    	c.addInt( ModelReaderUi.PARAM_LEVEL, getLevel() );
    	c.addString(ModelReaderUi.PARAM_MODELCLASS, getModelClass());
    	c.addBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED, isModelFilterEnabled() );
    	c.addString( ModelReaderUi.PARAM_MODELLIST, getModelList() );
    }	
	public void setSettings(Config c) throws InvalidSettingsException {		
		setLevel(c.getInt(ModelReaderUi.PARAM_LEVEL, 1));
		setModelClass(c.getString(ModelReaderUi.PARAM_MODELCLASS));
		setModelFilterEnabled(c.getBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED ));
		enableModelList(c.getString( ModelReaderUi.PARAM_MODELLIST ));
	}
}
