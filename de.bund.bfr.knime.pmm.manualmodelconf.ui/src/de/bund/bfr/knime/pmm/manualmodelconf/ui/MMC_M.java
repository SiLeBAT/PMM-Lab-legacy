/*
 * Created by JFormDesigner on Sat Sep 29 12:27:04 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.event.*;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.*;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.dim.bfr.external.service.BFRNodeService;

/**
 * @author Armin Weiser
 */
public class MMC_M extends JPanel {

	private static final String LABEL_OWNMODEL = "Manually defined model";

	private HashMap<String,ParametricModel> modelCatalog;
	private HashMap<String,ParametricModel> estModelMap;

	public MMC_M() {
		initComponents();
		modelCatalog = new HashMap<String,ParametricModel>();	
		estModelMap = new HashMap<String,ParametricModel>();
	}
	
	public void setDb(final BFRNodeService service) {		
		Bfrdb db;
		try {			
			db = new Bfrdb(service);
			getFromDB(db);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void getFromDB(final Bfrdb db) {		
		modelCatalog.clear();
		ParametricModel pm = new ParametricModel(LABEL_OWNMODEL, "", "", 1);
		modelNameBox.addItem(LABEL_OWNMODEL);
		modelCatalog.put(LABEL_OWNMODEL, pm);
		try {			
			ResultSet result = db.selectModel(Bfrdb.LEVEL_PRIMARY);			
			while(result.next()) {				
				String modelName = result.getString(Bfrdb.ATT_NAME);
				String formula = result.getString(Bfrdb.ATT_FORMULA);
				int modelID = result.getInt(Bfrdb.ATT_MODELID);

				pm = new ParametricModel(modelName, formula, result.getString(Bfrdb.ATT_DEP), 1, modelID);
				manageDBMinMax(result, pm);
				manageIndep(pm, result.getArray( Bfrdb.ATT_INDEP));
				
				modelNameBox.addItem(modelName);
				modelCatalog.put(modelName, pm);
			}			
			result.getStatement().close();
			result.close();
			
			/*
			result = db.selectModel( Bfrdb.LEVEL_SECONDARY );
			secondaryModel.resetModelItems();
			pm = new ParametricModel(LABEL_OWNMODEL, "", "", 1);
			secondaryModel.getModelCatalog().put(LABEL_OWNMODEL, pm);
			while( result.next() ) {
				
				k = result.getString( Bfrdb.ATT_NAME );
				f = result.getString( Bfrdb.ATT_FORMULA );
				//t = convertArray2String(result.getArray( Bfrdb.ATT_INDEP ));
				i = result.getInt( Bfrdb.ATT_MODELID );
				// initialize modelcatalog instance
				pm = new ParametricModel( k, f, result.getString( Bfrdb.ATT_DEP ), 1, i );
				manageMinMax(result, pm);
				manageIndep(pm, result.getArray( Bfrdb.ATT_INDEP ));
				secondaryModel.getModelCatalog().put(k, pm);
				
				secondaryModel.addModelItem(k);
			}
			
			result.getStatement().close();
			result.close();
			*/			
			db.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}		
	}
	private void manageDBMinMax(ResultSet result, ParametricModel pm) throws SQLException {
		Array array = result.getArray(Bfrdb.ATT_PARAMNAME);
		Array arrayMin = result.getArray(Bfrdb.ATT_MINVALUE);
		Array arrayMax = result.getArray(Bfrdb.ATT_MAXVALUE);
	    if (array != null && arrayMin != null && arrayMax != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				Object[] oMin = (Object[])arrayMin.getArray();
				Object[] oMax = (Object[])arrayMax.getArray();
				if (o != null && o.length > 0) {
					for (int ii=0;ii<o.length;ii++) {
						pm.addParam(o[ii].toString(), Double.NaN, Double.NaN);
						if (oMin != null && oMin.length > ii && oMin[ii] != null) {
							pm.setParamMin(o[ii].toString(), Double.parseDouble(oMin[ii].toString()));
						}
						if (oMax != null && oMax.length > ii && oMax[ii] != null) {
							pm.setParamMax(o[ii].toString(), Double.parseDouble(oMax[ii].toString()));
						}
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}
    private String manageIndep(ParametricModel pm, Array array) {
    	String result = null;
	    if (array != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				if (o != null && o.length > 0) {
					for (int i=0;i<o.length;i++) {
						pm.addIndepVar(o[i].toString());
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
    	return result;
    }

	private void modelNameBoxActionPerformed(ActionEvent e) {
		table.clearTable();
		table.revalidate();
		formulaArea.setText("");
		
		if (modelNameBox.getSelectedIndex() == 0) { // Manually defined model
			return;
		}

		ParametricModel pm = modelCatalog.get(modelNameBox.getSelectedItem());
		if (pm != null) {
			formulaArea.setText(pm.getFormula());
			formulaUpdate();
			table.setValueAt(Boolean.TRUE, pm.getIndepVarSet().getFirst(), "Indep");
			for (String param : pm.getParamNameSet()) {
				table.setValueAt(pm.getParamMin(param), param, "Min");				
				table.setValueAt(pm.getParamMax(param), param, "Max");				
			}
			table.revalidate();
			//paramUpdate(pm.getIndepVarSet().size() > 0 ? pm.getIndepVarSet().getFirst() : null);				
		}
		else {
			System.err.println("pm = null???");
		}
	}
	private void formulaUpdate() {
		String formula = formulaArea.getText();
		formula = formula.replaceAll( "\n", "" );
		formula = formula.replaceAll( "\\s", "" );
		formula = formula.replace("~", "=").trim();
		
		int index = formula.indexOf("=");
		if (index < 0) {
			return;
		}
		
		formulaArea.setText(formula);
		String depVar = formula.substring(0, index).trim();

		table.clearTable();
		DJep parser = MathUtilities.createParser();
		try {
			parser.parse(formula);
			SymbolTable st = parser.getSymbolTable();
		    for (Object o : st.keySet()) {
		    	String os = o.toString();
		        if (!os.equals(depVar)) {
					table.addValue(os, 0);
		        }		        
		    }
		}
		catch (ParseException e) {
			if (!e.getErrorInfo().startsWith("Unexpected \"<EOF>\"") &&
					!e.getErrorInfo().startsWith("Encountered \"-\" at")) {
				e.printStackTrace();
			}
		}
		table.revalidate();
	}

	private void formulaAreaFocusLost(FocusEvent e) {
		modelNameBox.setSelectedIndex(0);
		formulaUpdate();
	}

	private void formulaAreaKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) formulaUpdate();
	}

	/*
	public String toXmlString() {		
		PmmXmlDoc doc = new PmmXmlDoc();

		ParametricModel pm = new ParametricModel( modelNameBox.getSelectedItem(), formulaArea.getText(), getDepVar(), 1, getModelId() );
		
		// set independent variable
		pm.addIndepVar(getIndepVar(), indepMinVal.getValue(), indepMaxVal.getValue());
		
		// add parameter values
		boolean hasParamValues = false;
		int n = getNumParam();
		for( int i = 0; i < n; i++ ) {
			if( i == indepBox.getSelectedIndex() ) {
				continue;
			}
			double dbl = getParamValue( i );
			if (!Double.isNaN(dbl)) {
				hasParamValues = true;
			}
			pm.addParam( getParamName( i ), dbl, Double.NaN, getMinValue(i), getMaxValue(i) );
		}
		
		// add literature items
		//if (modelNameBox.getSelectedIndex() >= 1) {
			for (Map.Entry<Integer, String> entry : possLiterature.entrySet()) {
			    Integer key = entry.getKey();
			    String value = entry.getValue();
				ParametricModel pmc = modelCatalog.get( modelNameBox.getSelectedItem() );
				List<Integer> li = modLitMat.get(pmc.getModelId());
			    if (li != null && li.contains(key)) {
					int q = value.indexOf( " et al. " );
					
					if (hasParamValues && getLevel() == 1) {
						pm.addEstModelLit(value.substring( 0, q ), Integer.valueOf( value.substring( q+8 ) ), key);
					} else {
						pm.addModelLit(value.substring( 0, q ), Integer.valueOf( value.substring( q+8 ) ), key);
					}
			    }
			}
		//}
		
		doc.add( pm );
		
		if( getLevel() == 2 ) {
			for( ParametricModel m : secondaryModel.getModelSet() ) {
				doc.add( m );
			}
		}
		
		return doc.toXmlString();
	}
	
	public void setFromXmlString( final String xmlString ) {
		isLoading = true;
		
		PmmXmlDoc doc;
		int i, j, n;
			
		ParametricModel primModel, pm;
		PmmXmlElementConvertable el;
		LinkedList<ParametricModel> secModel;
		
		secModel = new LinkedList<ParametricModel>();
		
		try {
			
			doc = new PmmXmlDoc( xmlString );
			
			
			n = doc.size();
			
			// fetch model set
			pm = null;
			primModel = null;
			for( i = 0; i < n; i++ ) {
				
				el = doc.get( i );
				
				if( !( el instanceof ParametricModel ) ) {
					continue;
				}
				
				pm = (ParametricModel) el;
				
				if( pm.getLevel() == 1 ) {
					primModel = pm;
				} else {
					secModel.add( pm );
				}
			}
			
			if( primModel == null ) {
				isLoading = false;
				return;
			}

			// set primary model
			setModelName( primModel.getModelName() );
			setLevel( primModel.getLevel() );
			setFormula( primModel.getFormula() );
			if (primModel.getIndepVarSet().size() > 0) {
				String indepVar = primModel.getIndepVarSet().getFirst();
				setIndepVar(indepVar, primModel.getIndepMin(indepVar), primModel.getIndepMax(indepVar));
			} else {
				setIndepVar(null);
			}
			for( String paramName : primModel.getParamNameSet() ) {
				setParam( paramName, primModel.getParamValue( paramName ), primModel.getParamMin(paramName), primModel.getParamMax(paramName));
			}
			paramUpdate();

			List<Integer> li = new ArrayList<Integer>();
			for( LiteratureItem item : primModel.getModelLit()) {
				li.add(item.getId());
			}
			for( LiteratureItem item : primModel.getEstModelLit()) {
				li.add(item.getId());
			}
			ParametricModel pmc = modelCatalog.get( modelNameBox.getSelectedItem() );
			modLitMat.put(pmc.getModelId(), li);
			
			updatePossibleLiterature();
			updateLiterature();
			
			
			if( secModel.isEmpty() ) {
				isLoading = false;
				return;
			}
			
			setLevel( 2 );
			
			// set secondary models
			for( ParametricModel m : secModel ) {
				secondaryModel.setModel( m );
			}
		}
		catch( Exception e ) {
			e.printStackTrace( System.err );
		}
		isLoading = false;
	}
*/
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		modelNameLabel = new JLabel();
		modelNameBox = new JComboBox();
		label2 = new JLabel();
		formulaArea = new JTextField();
		tableLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		table = new ModelTableModel();
		literatureLabel = new JLabel();

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Model Properties"),
			Borders.DLU2_BORDER));
		setLayout(new FormLayout(
			"default, 3*($lcgap, default:grow)",
			"3*(default, $lgap), default"));

		//---- modelNameLabel ----
		modelNameLabel.setText("Model from DB:");
		add(modelNameLabel, CC.xy(1, 1));

		//---- modelNameBox ----
		modelNameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelNameBoxActionPerformed(e);
			}
		});
		add(modelNameBox, CC.xywh(3, 1, 5, 1));

		//---- label2 ----
		label2.setText("Model formula:");
		add(label2, CC.xy(1, 3));

		//---- formulaArea ----
		formulaArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				formulaAreaFocusLost(e);
			}
		});
		formulaArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				formulaAreaKeyReleased(e);
			}
		});
		add(formulaArea, CC.xywh(3, 3, 5, 1));

		//---- tableLabel ----
		tableLabel.setText("Parameter definition:");
		add(tableLabel, CC.xy(1, 5));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(table);
		}
		add(scrollPane1, CC.xywh(3, 5, 5, 1));

		//---- literatureLabel ----
		literatureLabel.setText("Literatur:");
		add(literatureLabel, CC.xy(1, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel modelNameLabel;
	private JComboBox modelNameBox;
	private JLabel label2;
	private JTextField formulaArea;
	private JLabel tableLabel;
	private JScrollPane scrollPane1;
	private ModelTableModel table;
	private JLabel literatureLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
