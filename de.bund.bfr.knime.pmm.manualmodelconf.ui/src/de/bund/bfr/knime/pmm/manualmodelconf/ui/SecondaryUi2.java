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
package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.resources.Resources;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

public class SecondaryUi2 extends JDialog implements KeyListener, ActionListener, TableModelListener {
	
	private static final long serialVersionUID = 20120529;
	private static final String LABEL_OWNSECMODEL = "Manually defined model";
	private static final String LABEL_UNDEFINEDMODEL = "Undefined model";

	private JComboBox depBox;
	private JComboBox secBox;
	//private Hashtable<String,String[]> secModel;
	private Hashtable<String,Integer> secModelId;
	private JTextArea formulaArea;
	private Hashtable<String,String> formulaSet;
	private Hashtable<String,String> globalModelSet;
	private HashMap<String, HashMap<String, Boolean>> indepKeys;
	private HashMap<String, HashMap<String, DoubleTextField[]>> indepMinMax;
	//private boolean[][] isIndepKey;
	//private String[][] keySet;
	private HashMap<String, List<String>> keySet;
	private HashMap<String, HashMap<String, Double>> paramNameMap;
	private HashMap<String, HashMap<String, Double>> paramMinMap;
	private HashMap<String, HashMap<String, Double>> paramMaxMap;
	private Hashtable<String,ParametricModel> modelCatalog;
	private JPanel indepPanel;
	private TableWithOverwrite table;
	private JButton okButton;
	private boolean ignoreTableChanged = false;
	
	private static final int MAX_PARAM = 10;
	
	public SecondaryUi2(Frame frame) {
		super(frame,true);
		
		Object[][] paramReal;
		String[] colnames;
		
		this.setSize( 400, 600 );
		this.setPreferredSize( new Dimension( 400, 600 ) );
		this.setIconImage(Resources.getInstance().getDefaultIcon());
		this.setLayout( new GridLayout( 0, 1 ) );
		
		JPanel parentPanel = new JPanel();
		
		parentPanel.setLayout( new BoxLayout( parentPanel, BoxLayout.Y_AXIS ) );
		List<JLabel> labelList = new ArrayList<JLabel>();

		JPanel ppPanel = new JPanel();
		JLabel ppLabel = new JLabel("Primary parameter:");
		depBox = new JComboBox();
		depBox.addActionListener( this );
		depBox.setEnabled(false);
		fillPanel(parentPanel, ppPanel, ppLabel, depBox, labelList);
						
		JPanel smPanel = new JPanel();
		JLabel smLabel = new JLabel("Secondary model:");
		secBox = new JComboBox(new String[] {LABEL_OWNSECMODEL});
		secBox.addActionListener( this );
		fillPanel(parentPanel, smPanel, smLabel, secBox, labelList);
		
		JPanel sfPanel = new JPanel();
		JLabel sfLabel = new JLabel("Secondary formula:");
		formulaArea = new JTextArea();
		formulaArea.addKeyListener( this );
		fillPanel(parentPanel, sfPanel, sfLabel, formulaArea, labelList);
		
		JPanel icPanel = new JPanel();
		JLabel icLabel = new JLabel("Independent conditions:");
		indepPanel = new JPanel();
		indepPanel.setLayout( new GridLayout(0, 3));
		//indepPanel.setPreferredSize(new Dimension(400, 3500));
		fillPanel(parentPanel, icPanel, icLabel, indepPanel, labelList);
		
		//isIndepKey = new boolean[ MAX_PARAM ][ MAX_PARAM ];
		indepKeys = new HashMap<String, HashMap<String, Boolean>>();
		indepMinMax = new HashMap<String, HashMap<String, DoubleTextField[]>>();
		formulaSet = new Hashtable<String, String>();
		globalModelSet = new Hashtable<String, String>();
		keySet = new HashMap<String, List<String>>();//new String[ MAX_PARAM ][ MAX_PARAM ];
		paramNameMap = new HashMap<String, HashMap<String, Double>>();
		paramMinMap = new HashMap<String, HashMap<String, Double>>();
		paramMaxMap = new HashMap<String, HashMap<String, Double>>();
		modelCatalog = new Hashtable<String,ParametricModel>();
		
		// initialize table
		paramReal = new Object[ MAX_PARAM ][ 4 ];
		colnames = new String[] { "Parameter", "Value", "Min", "Max" };
		table = new TableWithOverwrite( paramReal, colnames ) {
			private static final long serialVersionUID = -2690118183110287184L;

			@Override
			   public boolean isCellEditable(final int row, final int column) {
			       return column != 0;
			   }
		};
		table.addKeyListener( this );
		table.getModel().addTableModelListener( this );
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		JPanel pdPanel = new JPanel();
		JLabel pdLabel = new JLabel("Parameter definition:");
		JScrollPane sp = new JScrollPane(table);
		sp.setMinimumSize(new Dimension(400, 150));
		//sp.setBorder(BorderFactory.createEmptyBorder());
		fillPanel(parentPanel, pdPanel, pdLabel, sp, labelList);
		
		okButton = new JButton( "OK" );
		okButton.addActionListener( this );
		parentPanel.add(okButton);

		this.add(parentPanel);
				
		int width = 0;		
		for (JLabel label : labelList) {
			width = Math.max(width, label.getPreferredSize().width);
		}		
		width += 10;		
		for (JLabel label : labelList) {
			label.setMinimumSize(new Dimension(width, label.getMinimumSize().height));
			label.setPreferredSize(new Dimension(width, label.getMinimumSize().height));
		}

		formulaChanged();
	}
	private void fillPanel(JPanel parent, JPanel panel, JLabel label, JComponent comp, List<JLabel> labelList) {
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(label, BorderLayout.WEST);
		panel.add(comp, BorderLayout.CENTER);
		parent.add(panel);
		labelList.add(label);
	}
	
	public Hashtable<String,ParametricModel> getModelCatalog() {
		return modelCatalog;
	}
	public String getModelName() { return ( String )( secBox.getSelectedItem() ); }
	public int getModelId() {
		return getModelId(secBox.getSelectedItem().toString(), null);
	}
	public int getModelId(final String modelname, final Object depVar) {
		
		if (modelname.equals(LABEL_OWNSECMODEL)) { // Manually defined model			
			return getNegRand();
		}
		
		Integer res = null;
		ParametricModel pm = modelCatalog.get(modelname);
		if (pm != null) {
			res = pm.getModelId();
		} else { // LABEL_UNDEFINEDMODEL
			res = secModelId.get(depVar + "_" + modelname);
		}
		if (res == null) {
			res = getNegRand();
		}
		return res;
	}
	private int getNegRand() {
		Random rand = new Random();
		int i = rand.nextInt();
		if( i > 0 ) {
			i = -i;
		}
		return i;
	}
	public void resetModelItems() {
		secBox.removeAllItems();
		secBox.addItem(LABEL_OWNSECMODEL);
		modelCatalog.clear();
		secModelId = new Hashtable<String,Integer>();
		//secModel = new Hashtable<String,String[]>();
	}
	public void addModelItem(final String k) {
		secBox.addItem( k );
		//secModel.put( k, new String[] { t, f } );
		//secModelId.put( k, i );		
	}
	public void setSelectedVar( final int i ) {
		depBox.setSelectedIndex( i );
		String modelName = globalModelSet.get(depBox.getSelectedItem());
		if (modelName == null) {
			secBox.setSelectedItem(LABEL_OWNSECMODEL);
		}
		else {
			secBox.setSelectedItem(modelName);
			formulaArea.setText(formulaSet.get(depBox.getSelectedItem()));			
		}
	}
	
	@Override
	public void tableChanged( final TableModelEvent te ) {
		if (!ignoreTableChanged) {
			
			String selItem = (String) depBox.getSelectedItem();
			if (te != null) {
				int changedRow = te.getFirstRow();
				int changedCol = te.getColumn();
				String changedKey = table.getValueAt(changedRow, 0).toString();
				if (!changedKey.isEmpty()) {
					Double dbl = Double.NaN;
					try {
						dbl = Double.valueOf( ( String )table.getValueAt( changedRow, changedCol) );
					}
					catch( Exception e ) {}
					if (changedCol == 1) {
						HashMap<String, Double> valMap = paramNameMap.get(selItem);
						handleValMap(paramNameMap, selItem, valMap, changedKey, dbl);						
					}
					else if (changedCol == 2) {
						HashMap<String, Double> valMap = paramMinMap.get(selItem);
						handleValMap(paramMinMap, selItem, valMap, changedKey, dbl);						
					}
					else if (changedCol == 3) {
						HashMap<String, Double> valMap = paramMaxMap.get(selItem);
						handleValMap(paramMaxMap, selItem, valMap, changedKey, dbl);						
					}
				}
			}
			else {
				System.err.println("te == null ???? Sollte eigentlich nicht sein...");
				int j, k;
				//i = depBox.getSelectedIndex();
				String depVar = depBox.getSelectedItem().toString();
				List<String> kl = keySet.get(depVar);
				
				k = 0;
				for( j = 0; j < MAX_PARAM; j++ ) {
					
					//if( isIndepKey[ i ][ j ] ) {
					if( getIndepKey(selItem, kl.get(j) )) {
						continue;
					}
					
					try {
						HashMap<String, Double> valMap = paramNameMap.get(selItem);
						Double dbl = null;
						try {
							dbl = Double.valueOf( ( String )table.getModel().getValueAt( k, 1 ) );
						}
						catch( Exception e ) {}
						handleValMap(paramNameMap, selItem, valMap, kl.get(j), dbl);						

						valMap = paramMinMap.get(selItem);
						dbl = null;
						try {
							dbl = Double.valueOf( ( String )table.getModel().getValueAt( k, 2) );
						}
						catch( Exception e ) {}
						handleValMap(paramMinMap, selItem, valMap, kl.get(j), dbl);						

						valMap = paramMaxMap.get(selItem);
						dbl = null;
						try {
							dbl = Double.valueOf( ( String )table.getModel().getValueAt( k, 3) );
						}
						catch( Exception e ) {}
						handleValMap(paramMaxMap, selItem, valMap, kl.get(j), dbl);						
					}
					catch( Exception e ) {}
					k++;
				}			
			}
		}		
	}
	
	public void setParamList( final LinkedList<String> paramNameSet ) {

		int i;
		
		depBox.removeAllItems();
		for( String s : paramNameSet ) {
			depBox.addItem( s );
		}
		
		i = 0;
		for( String s : paramNameSet ) {
			if( i >= MAX_PARAM ) {
				break;
			} else {
				formulaSet.put(s, s+"=");
			}
		}
		
		depCorrect();
	}
	
	@Override
	public void keyPressed( final KeyEvent ke ) {}
	@Override
	public void keyTyped( final KeyEvent ke ) {
		if (ke.getSource() instanceof TableWithOverwrite) {
		  	char ch = ke.getKeyChar();
			  			if (ch == ',') {
			  				ch = '.';
			  				ke.setKeyChar('.');
			  			}
		}
	}

	@Override
	public void keyReleased( final KeyEvent ke ) {
		
		if( ke.getSource() == formulaArea ) {
			secBox.setSelectedIndex(0);
			// find out the current index
			//int i = depBox.getSelectedIndex();
			
			depCorrect();
			formulaSet.put(depBox.getSelectedItem().toString(), formulaArea.getText());
			formulaChanged();
		}
				
	}
	
	@Override
	public void actionPerformed( final ActionEvent ae ) {
		
		int i, j;
		String id;
		
		// System.out.print( "." );
		
		// find out the current index
		i = depBox.getSelectedIndex();

		if( ae.getSource() == depBox ) {
			
			// the dependent variable was updated			
			if(i < 0) {
				return;
			}
			
			// load the formula that was selected
			formulaArea.setText( formulaSet.get(depBox.getSelectedItem()));
			// depCorrect();
			formulaChanged();
			
			return;
		}
		
		if( ae.getSource() == secBox ) {
			
			if (i < 0) {
				return;
			}
			if( secBox.getSelectedIndex() == 0) { // Manually defined model
				globalModelSet.put(depBox.getSelectedItem().toString(), secBox.getSelectedItem().toString());
				//formulaArea.setText(formulaSet[ i ]);
				//formulaChanged();
			}
			else if (secBox.getSelectedIndex() > 0) {
				//String[] ss = secModel.get(secBox.getSelectedItem());
				ParametricModel pm = modelCatalog.get(secBox.getSelectedItem());
				if (pm != null) {
					String formula = pm.getFormula();
					formula = formula.replaceAll( "\n", "" );
					formula = formula.replaceAll( "\\s", "" );
					formula = formula.replace("~", "=");
					
					int index = formula.indexOf("=");
					if (index >= 0) {
						formula = formula.substring(index + 1);
					}
					//formulaArea.setText(formulaSet[ i ]);
					formulaArea.setText(depBox.getSelectedItem() + "=" + formula);
				}				
				formulaSet.put(depBox.getSelectedItem().toString(), formulaArea.getText());
				globalModelSet.put(depBox.getSelectedItem().toString(), secBox.getSelectedItem().toString());
				formulaChanged();
				
				if (pm != null) {
					for (Component c : indepPanel.getComponents()) {
					    if (c instanceof JCheckBox) { 
					    	JCheckBox cb = (JCheckBox) c;
					    	String cbStr = cb.getText();					    	
					    	if (pm.getIndepVarSet().contains(cbStr)) {
					    		cb.setSelected(true);
					    		manageCheckboxChange(cb);
					    	}
					    }
					}
					indepChanged();
				}
			}
			return;
		}
		
		if( ae.getSource() instanceof JCheckBox ) {
			
			// some checkbox was changed
			
			id = ae.getActionCommand();
			String depVar = depBox.getSelectedItem().toString();
			List<String> kl = keySet.get(depVar);
			for( j = 0; j < MAX_PARAM; j++ ) {
				if(j < kl.size() && kl.get(j) != null ) {
					if( id.equals( kl.get(j) ) ) {
						//isIndepKey[ i ][ j ] = ( ( JCheckBox )ae.getSource() ).isSelected();
						JCheckBox cb = (JCheckBox) ae.getSource();
						manageCheckboxChange(cb);
						break;
					}
				}
			}
		}
		
		if( ae.getSource() == okButton ) {
			
			// The OK button was pressed
			setVisible( false );
		}
		
		indepChanged();
	}
	private void manageCheckboxChange(JCheckBox cb) {
		String selItem = (String) depBox.getSelectedItem();
		HashMap<String, Boolean> valMap = indepKeys.get(selItem);
		if (valMap == null) {
			valMap = new HashMap<String, Boolean>();
		}
		valMap.put(cb.getText(), cb.isSelected());
		indepKeys.put(selItem, valMap);

		DoubleTextField[] dtf;
		HashMap<String, DoubleTextField[]> valDTF = indepMinMax.get(selItem);
		if (valDTF != null && valDTF.containsKey(cb.getText())) {
			dtf = valDTF.get(cb.getText());
		}
		else {
			dtf = new DoubleTextField[2];
			dtf[0] = new DoubleTextField(true);
			dtf[1] = new DoubleTextField(true);
			if (valDTF != null) {
				valDTF.put(cb.getText(), dtf);
			}
		}
		dtf[0].setEnabled(cb.isSelected());
		dtf[1].setEnabled(cb.isSelected());
		indepMinMax.put(selItem, valDTF);		
	}
	
	
	private void depCorrect() {
		
		String[] token;
		String formula;
		String dep;
		
		if( depBox.getSelectedIndex() < 0 ) {
			return;
		}
		
		// find out selected variable
		dep = ( String )depBox.getSelectedItem();
		
		
		// fetch formula string
		formula = formulaArea.getText();		
		token = formula.split( "[~=]" );
		
		// there can be only one equality sign
		if( token.length > 2 ) {
			return;
		}
		
		// in case no equality sign is present
		if( token.length < 2 ) {
			
			formulaArea.setText( dep+"=" );
			return;
		}
		
		// in case there is exactly one equality sign
		assert token.length == 2;
		
		if( formula.startsWith( dep+"=" ) ) {
			return;
		}
		
		formulaArea.setText( dep+"="+token[ 1 ] );
	}

	private void formulaChanged() {	
		int depIndex = depBox.getSelectedIndex();
		if (depIndex < 0 ) {
			return;
		}
		String depVar = depBox.getSelectedItem().toString();
		String formula = formulaArea.getText();
		formula = formula.replaceAll( "\n", "" );
		formula = formula.replaceAll( "\\s", "" );
		formula = formula.replace("~", "=").trim();
		formula = formula.substring(formula.indexOf("=") + 1);
		
		List<String> kl = new ArrayList<String>();
		if (!formula.isEmpty()) {
			// remove part left of equality sign
			
			DJep parser = MathUtilities.createParser();
			//parser.removeVariable("x");
			try {
				parser.parse(formula);
				SymbolTable st = parser.getSymbolTable();
			    for (Object o : st.keySet()) {
			    	String os = o.toString();
			        if (!kl.contains(os)) {
			        	kl.add(os);
			        }	
			    }
			}
			catch (ParseException e) {
				if (!e.getErrorInfo().startsWith("Unexpected \"<EOF>\"")) {
					e.printStackTrace();
				}
			}
		}
	    keySet.put(depVar, kl);

		
		//// identify independent variable and parameters
		//token = formula.split( "[\\+\\-\\*/^\\(\\)><=]" );
		/*
		key = new ArrayList<String>();
		for( String s : token ) {
			
			// skip item if it is a number
			if( s.matches( "\\d*(\\.\\d*)?" ) ) {
				continue;
			}
			
			t = s.toLowerCase();
			
			// skip the exponential function
			if( t.equals( "exp" ) ) {
				continue;
			}
			
			// skip the natural logarithm
			if( t.equals( "log" ) ) {
				continue;
			}
			
			// skip the logarithm base 10
			if( t.equals( "log10" ) ) {
				continue;
			}
			
			if( t.equals( "sqrt" ) ) {
				continue;
			}
			
			// skip if already processed
			if( key.contains( s ) ) {
				continue;
			}
			
			key.add( s );
		}
		*/
		
		keySetChanged();
	}
	
	private void keySetChanged() {
		
		String depVar = depBox.getSelectedItem().toString();
		
		// clear all items
		indepPanel.removeAll();
		indepPanel.setVisible(false);
		indepPanel.add(new JLabel("IndepVar", SwingConstants.CENTER));
		indepPanel.add(new JLabel("Min", SwingConstants.CENTER));
		indepPanel.add(new JLabel("Max", SwingConstants.CENTER));
		
		for( String s : keySet.get(depVar)) {
			
			if( s == null ) {
				break;
			}

			JCheckBox box = new JCheckBox(s);
			box.setHorizontalAlignment(SwingConstants.CENTER);
			
			String selItem = (String) depBox.getSelectedItem();
			HashMap<String, Boolean> valMap = indepKeys.get(selItem);
			if (valMap != null) {
				if (valMap.containsKey(s)) {
					box.setSelected(valMap.get(s));
				}
			}
			else {
				box.setSelected(false);
			}

			box.addActionListener( this );
			indepPanel.add(box);
			
			DoubleTextField[] dtf;
			HashMap<String, DoubleTextField[]> valDTF = indepMinMax.get(selItem);
			if (valDTF != null && valDTF.containsKey(s)) {
				dtf = valDTF.get(s);
			}
			else {
				dtf = new DoubleTextField[2];
				dtf[0] = new DoubleTextField(true);
				dtf[1] = new DoubleTextField(true);
				if (valDTF == null) {
					valDTF = new HashMap<String, DoubleTextField[]>();
				}
				valDTF.put(s, dtf);
			}
			dtf[0].setEnabled(box.isSelected());
			dtf[1].setEnabled(box.isSelected());
			indepMinMax.put(selItem, valDTF);
			
			indepPanel.add(dtf[0]);
			indepPanel.add(dtf[1]);
		}
		indepPanel.validate();
		indepPanel.setVisible(true);
		
		indepChanged();
	}
	
	private void indepChanged() {
		
		ignoreTableChanged = true;

		String depVar = depBox.getSelectedItem().toString();
		List<String> kl = keySet.get(depVar);
		int k = 0;
		for (int j = 0; j < MAX_PARAM && j < kl.size(); j++ ) {
			
			if( kl.get(j) == null ) {
				break;
			}
			
			//if( isIndepKey[ i ][ j ] ) {
			String selItem = (String) depBox.getSelectedItem();
			if( getIndepKey(selItem, kl.get(j) )) {
				continue;
			}

			table.setValueAt( kl.get(j), k, 0 );
			
			Double val = null;
			HashMap<String, Double> valMap = paramNameMap.get(selItem);
			if (valMap != null && valMap.get(kl.get(j)) != null) {
				val = valMap.get(kl.get(j));
			}
			table.setValueAt( val == null || Double.isNaN(val) ? "" : val, k, 1 );
			
			Double min = null;
			valMap = paramMinMap.get(selItem);
			if (valMap != null && valMap.get(kl.get(j)) != null) {
				min = valMap.get(kl.get(j));
			}
			if (min == null || Double.isNaN(min)) {
				min = modelCatalog.get(getModelName()).getParamMin(kl.get(j));
				handleValMap(paramMinMap, selItem, valMap, kl.get(j), min);
			}
			table.setValueAt( min == null || Double.isNaN(min) ? "" : min, k, 2 );

			Double max = null;
			valMap = paramMaxMap.get(selItem);
			if (valMap != null && valMap.get(kl.get(j)) != null) {
				max = valMap.get(kl.get(j));
			}
			if (max == null || Double.isNaN(max)) {
				max = modelCatalog.get(getModelName()).getParamMax(kl.get(j));
				handleValMap(paramMaxMap, selItem, valMap, kl.get(j), max);
			}
			table.setValueAt( max == null || Double.isNaN(max) ? "" : max, k, 3 );

			k++;
		}
		
		for( ; k < MAX_PARAM; k++ ) {
			table.setValueAt( "", k, 0 );
			table.setValueAt( "", k, 1 );
			table.setValueAt( "", k, 2 );
			table.setValueAt( "", k, 3 );
		}
				
		ignoreTableChanged = false;
	}
	private void handleValMap(HashMap<String, HashMap<String, Double>> paramMap, String selItem, HashMap<String, Double> valMap, String key, Double value) {
		if (valMap == null) {
			valMap = new HashMap<String, Double>();
		}
		valMap.put(key, value);
		paramMap.put(selItem, valMap);
	}
	
	public LinkedList<ParametricModel> getModelSet() {
		
		LinkedList<ParametricModel> modelSet = new LinkedList<ParametricModel>();
		
		int n = depBox.getItemCount();
		
		for (int i = 0; i < n; i++ ) {
			
			ParametricModel model;
			Object depVar = depBox.getItemAt(i);
			String modelName = globalModelSet.get(depVar);
			if (modelName == null) {
				int j = getModelId(LABEL_OWNSECMODEL, depVar);
				// initialize new secondary model instance
				model = new ParametricModel(LABEL_UNDEFINEDMODEL , formulaSet.get(depBox.getItemAt(i)),
					( String )depBox.getItemAt( i ), 2, j );
			}
			else {
				int j = getModelId(modelName, depVar);
				// initialize new secondary model instance
				String selItem = (String) depBox.getItemAt(i);
				model = new ParametricModel( modelName, formulaSet.get(selItem), selItem, 2, j );
				
				List<String> kl = keySet.get(depVar);
				// collect keys
				for( j = 0; j < MAX_PARAM && j < kl.size(); j++ ) {
					String kls = kl.get(j);
					if( kls == null ) {
						break;
					}
						if( getIndepKey(selItem, kls)) {
							HashMap<String, DoubleTextField[]> valDTF = indepMinMax.get(selItem);
							if (valDTF != null && valDTF.containsKey(kls)) {
								DoubleTextField[] dtf = valDTF.get(kls);
								model.addIndepVar(kls, dtf[0].getValue(), dtf[1].getValue());
							}
							else {
								model.addIndepVar(kls);
							}
						}
						else {
							Double val = Double.NaN;
							Double min = Double.NaN;
							Double max = Double.NaN;
							HashMap<String, Double> valMap = paramNameMap.get(selItem);
							if (valMap != null && valMap.get(kls) != null) {
								val = valMap.get(kls);
							}
							valMap = paramMinMap.get(selItem);
							if (valMap != null && valMap.get(kls) != null) {
								min = valMap.get(kls);
							}
							valMap = paramMinMap.get(selItem);
							if (valMap != null && valMap.get(kls) != null) {
								max = valMap.get(kls);
							}
							model.addParam(kls, val, Double.NaN, min, max); //  selItem + ";" + 
						}					
				}				
			}
			// add model
			modelSet.add( model );
		}
		
		return modelSet;
	}
	
	public void setModel( final ParametricModel model ) {
		
		int depIndex;
		LinkedList<String> indepVarSet;
		
		String depVar = model.getDepVar();
				
		for( depIndex = 0; depIndex < depBox.getItemCount(); depIndex++ ) {
			if( depBox.getItemAt(depIndex).equals( depVar ) ) {
				break;
			}
		}
		
		formulaSet.put(depVar, model.getFormula());
		globalModelSet.put(depVar, model.getModelName());
		if (model.getModelName().equals(LABEL_UNDEFINEDMODEL)) {
			secModelId.put(depVar + "_" + model.getModelName(), model.getModelId());
		}
		formulaArea.setText( model.getFormula() );
		depBox.setSelectedIndex(depIndex);
		formulaChanged();
		
		indepVarSet = model.getIndepVarSet();
		List<String> kl = keySet.get(depVar);
		for(int j = 0; j < MAX_PARAM && j < kl.size(); j++ ) {
			if( indepVarSet.contains(kl.get(j)) ) {
				//isIndepKey[ i ][ j ] = true;
				String selItem = (String) depBox.getSelectedItem();
				HashMap<String, Boolean> valMap = indepKeys.get(selItem);
				if (valMap == null) {
					valMap = new HashMap<String, Boolean>();
				}
				valMap.put(kl.get(j), true);
				indepKeys.put(selItem, valMap);
				
				Double min = model.getIndepMin(kl.get(j));
				Double max = model.getIndepMax(kl.get(j));
				HashMap<String, DoubleTextField[]> valDTF = indepMinMax.get(selItem);
				if (valDTF == null) {
					valDTF = new HashMap<String, DoubleTextField[]>();
				}
				DoubleTextField[] dtf = new DoubleTextField[2];
				dtf[0] = new DoubleTextField(true);
				dtf[1] = new DoubleTextField(true);
				if (min != null && !min.isNaN()) {
					dtf[0].setText(min.toString());
				} else {
					dtf[0].setText("");
				}
				if (max != null && !max.isNaN()) {
					dtf[1].setText(max.toString());
				} else {
					dtf[1].setText("");
				}
				valDTF.put(kl.get(j), dtf);
				indepMinMax.put(selItem, valDTF);
			}
		}
		
		String selItem = (String) depBox.getSelectedItem();
		for( String key : model.getParamNameSet() ) {
			
			for(int j = 0; j < MAX_PARAM && j < kl.size(); j++ ) {
				if( (kl.get(j)).equals( key ) ) { // selItem + ";" + 
					break;
				}
			}
			HashMap<String, Double> valMap = paramNameMap.get(selItem);
			Double dbl = model.getParamValue( key );
			if (Double.isNaN(dbl)) {
				dbl = null;
			}
			handleValMap(paramNameMap, selItem, valMap, key, dbl);

			valMap = paramMinMap.get(selItem);
			dbl = model.getParamMin( key );
			if (Double.isNaN(dbl)) {
				dbl = null;
			}
			handleValMap(paramMinMap, selItem, valMap, key, dbl);

			valMap = paramMaxMap.get(selItem);
			dbl = model.getParamMax( key );
			if (Double.isNaN(dbl)) {
				dbl = null;
			}
			handleValMap(paramMaxMap, selItem, valMap, key, dbl);
		}
		keySetChanged();
		
	}
	private boolean getIndepKey(final String selItem, final String param) {
		boolean result = false;
		HashMap<String, Boolean> valMap = indepKeys.get(selItem);
		if (valMap != null) {
			if (valMap.containsKey(param)) {
				result = valMap.get(param);
			}
		}
		return result;
	}
}
