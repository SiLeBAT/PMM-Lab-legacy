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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.dim.bfr.external.service.BFRNodeService;


public class ManualModelConfUi extends JPanel implements KeyListener, ActionListener, MouseListener, TableModelListener {

	private static final String LABEL_OWNMODEL = "Manually defined model";
	protected static final int MAX_PARAM = 10;
	
	private static final long serialVersionUID = 20120503;
	private boolean isLoading = false;
	
	private TableWithOverwrite table;
	private JComboBox modelNameBox;
	private JComboBox modelTypeBox;
	//private JComboBox modelLangBox;
	private JTextField formulaArea;
	private JTextField depField;
	
	// private ArrayList<String> paramKey;
	// private ArrayList<Double> paramVal;
	
	private List<String> keySet;
	private HashMap<String, Double> valSet;
	private HashMap<String, Double> minSet;
	private HashMap<String, Double> maxSet;
	
	private JComboBox indepBox;
	private Hashtable<String,ParametricModel> modelCatalog;
	//private JTextField literatureField;
	private SecondaryUi2 secondaryModel;
	
	private LinkedList<String> literature;
	private HashMap<Integer, String> possLiterature;
	private JPanel literaturePanel;
	private JPanel[] timeseriesPanel = new JPanel[6];
	private StringTextField agentField;
	private StringTextField matrixField;
	private StringTextField commentField;
	private DoubleTextField temperatureField;
	private DoubleTextField phField;
	private DoubleTextField waterActivityField;
	private DoubleTextField indepMinVal;
	private DoubleTextField indepMaxVal;

	//private Hashtable<String,Integer> modelId;
	
	//private boolean[][] litMat;
	private HashMap<Integer, List<Integer>> modLitMat;// = new HashMap<String, List<Integer>>();
	private HashMap<Integer, CheckBoxWithID> litBox;
	
	public ManualModelConfUi(Frame frame) throws ClassNotFoundException {
						
		secondaryModel = new SecondaryUi2(frame);
	
		// paramVal = new ArrayList<Double>();
		// paramKey = new ArrayList<String>();
		
		keySet = new ArrayList<String>();
		valSet = new HashMap<String, Double>();
		minSet = new HashMap<String, Double>();
		maxSet = new HashMap<String, Double>();
		
		Object[][] paramReal = new Object[ MAX_PARAM ][ 5 ];
		String[] colnames = new String[] { "Parameter", "Indep", "Value", "Min", "Max" };
		table = new TableWithOverwrite( paramReal, colnames ) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return column != 0;
			}
		};
		table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

		this.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		this.setPreferredSize( new Dimension( 500, 600 ) );
		
		List<JLabel> labelList = new ArrayList<JLabel>();
		
		JPanel tsPanel = new JPanel();
		tsPanel.setLayout( new BoxLayout( tsPanel, BoxLayout.Y_AXIS ) );
		tsPanel.setBorder(new TitledBorder("Microbial Data Properties"));
		// TimeSeries Definition
		timeseriesPanel[0] = new JPanel();
		timeseriesPanel[0].setLayout(new BorderLayout());
		timeseriesPanel[0].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		agentField = new StringTextField(true);
		JLabel agentLabel = new JLabel(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_AGENTNAME) + ":");
		timeseriesPanel[0].add(agentLabel, BorderLayout.WEST);
		timeseriesPanel[0].add(agentField, BorderLayout.CENTER);
		labelList.add(agentLabel);
		
		timeseriesPanel[1] = new JPanel();
		timeseriesPanel[1].setLayout(new BorderLayout());
		timeseriesPanel[1].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		matrixField = new StringTextField(true);
		JLabel matrixLabel = new JLabel(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_MATRIXNAME) + ":");
		timeseriesPanel[1].add(matrixLabel, BorderLayout.WEST);
		timeseriesPanel[1].add(matrixField, BorderLayout.CENTER);
		labelList.add(matrixLabel);
		
		timeseriesPanel[2] = new JPanel();
		timeseriesPanel[2].setLayout(new BorderLayout());
		timeseriesPanel[2].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		commentField = new StringTextField(true);
		JLabel commentLabel = new JLabel(TimeSeriesSchema.ATT_COMMENT + ":");
		timeseriesPanel[2].add(commentLabel, BorderLayout.WEST);
		timeseriesPanel[2].add(commentField, BorderLayout.CENTER);
		labelList.add(commentLabel);
		
		timeseriesPanel[3] = new JPanel();
		timeseriesPanel[3].setLayout(new BorderLayout());
		timeseriesPanel[3].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		temperatureField = new DoubleTextField(true);
		JLabel tempLabel = new JLabel(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":");
		timeseriesPanel[3].add(tempLabel, BorderLayout.WEST);
		timeseriesPanel[3].add(temperatureField, BorderLayout.CENTER);
		labelList.add(tempLabel);
		
		timeseriesPanel[4] = new JPanel();
		timeseriesPanel[4].setLayout(new BorderLayout());
		timeseriesPanel[4].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH, true);
		JLabel phLabel = new JLabel(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_PH) + ":");
		timeseriesPanel[4].add(phLabel, BorderLayout.WEST);
		timeseriesPanel[4].add(phField, BorderLayout.CENTER);
		labelList.add(phLabel);
		
		timeseriesPanel[5] = new JPanel();
		timeseriesPanel[5].setLayout(new BorderLayout());
		timeseriesPanel[5].setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));		
		waterActivityField = new DoubleTextField(PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY, true);
		JLabel awLabel = new JLabel(AttributeUtilities.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":");
		timeseriesPanel[5].add(awLabel, BorderLayout.WEST);
		timeseriesPanel[5].add(waterActivityField, BorderLayout.CENTER);
		labelList.add(awLabel);

		for (int i=0;i<6;i++) {
			tsPanel.add(timeseriesPanel[i]);
		}
		
		this.add(tsPanel);
		
		JPanel m1Panel = new JPanel();
		m1Panel.setLayout( new BoxLayout( m1Panel, BoxLayout.Y_AXIS ) );
		m1Panel.setBorder(new TitledBorder("Primary Model Properties"));

		JPanel modelNamePanel = new JPanel();
		JLabel modelNameLabel = new JLabel("Primary model :");
		
		modelNamePanel.setLayout(new BorderLayout());
		modelNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		modelNameBox = new JComboBox( new String[] { LABEL_OWNMODEL } );
		modelNameBox.addActionListener( this );
		modelNamePanel.add(modelNameLabel, BorderLayout.WEST);
		modelNamePanel.add(modelNameBox, BorderLayout.CENTER);
		m1Panel.add(modelNamePanel);
		labelList.add(modelNameLabel);
		
		/*
		JPanel literatureFieldPanel = new JPanel();
		JLabel literatureFieldLabel = new JLabel("Literatur :");
		
		literatureFieldPanel.setLayout(new BorderLayout());
		literatureFieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		literatureField = new JTextField();
		literatureField.setEditable( false );
		literatureFieldPanel.add(literatureFieldLabel, BorderLayout.WEST);
		literatureFieldPanel.add(literatureField, BorderLayout.CENTER);
		m1Panel.add(literatureFieldPanel);
		labelList.add(literatureFieldLabel);
		*/
		
		/*
		JPanel modelLangPanel = new JPanel();
		JLabel modelLangLabel = new JLabel("Formula notation :");
		
		modelLangPanel.setLayout(new BorderLayout());
		modelLangPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		modelLangBox = new JComboBox( new String[] { "R", "Java", "Matlab" } );
		modelLangPanel.add(modelLangLabel, BorderLayout.WEST);
		modelLangPanel.add(modelLangBox, BorderLayout.CENTER);
		add(modelLangPanel);
		labelList.add(modelLangLabel);
		*/
		JPanel formulaPanel = new JPanel();
		JLabel formulaLabel = new JLabel("Primary model formula :");
		JScrollPane formulaScrollPane = new JScrollPane();
		
		formulaPanel.setLayout(new BorderLayout());
		formulaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));					
		formulaArea = new JTextField();
		formulaArea.addKeyListener( this );
		formulaScrollPane.setViewportView(formulaArea);		
		formulaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		formulaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		formulaScrollPane.setMinimumSize(new Dimension(-1, formulaArea.getPreferredSize().height*2));
		formulaPanel.add(formulaLabel, BorderLayout.WEST);
		formulaPanel.add(formulaScrollPane, BorderLayout.CENTER);		
		m1Panel.add(formulaPanel);
		labelList.add(formulaLabel);
		
		JPanel indepPanel = new JPanel();
		JPanel indepPanelMM = new JPanel();
		JPanel indepPanelNorth = new JPanel();
		JLabel indepLabel = new JLabel("Independent variable :");
		indepMinVal = new DoubleTextField(true);
		indepMaxVal = new DoubleTextField(true);
		indepMinVal.setColumns(10);
		indepMaxVal.setColumns(10);
		
		indepPanel.setLayout(new BorderLayout());
		indepPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		indepPanelMM.setLayout(new BorderLayout());
		indepPanelNorth.setLayout(new BorderLayout());
		indepBox = new JComboBox();
		indepBox.addActionListener( this );
		indepPanelMM.add(indepMinVal, BorderLayout.WEST);
		indepPanelMM.add(indepMaxVal, BorderLayout.EAST);
		indepPanelNorth.add(new JLabel("Min", SwingConstants.CENTER), BorderLayout.WEST);
		indepPanelNorth.add(new JLabel("Max", SwingConstants.CENTER), BorderLayout.EAST);
		indepPanelMM.add(indepPanelNorth, BorderLayout.NORTH);
		indepPanel.add(indepLabel, BorderLayout.WEST);
		indepPanel.add(indepBox, BorderLayout.CENTER);
		indepPanel.add(indepPanelMM, BorderLayout.EAST);
		m1Panel.add(indepPanel);
		labelList.add(indepLabel);
		
		JPanel depPanel = new JPanel();
		JLabel depLabel = new JLabel("Dependent variable :" ); 
		
		depPanel.setLayout(new BorderLayout());
		depPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		depField = new JTextField();
		depField.setEditable( false );
		depPanel.add(depLabel, BorderLayout.WEST);
		depPanel.add(depField, BorderLayout.CENTER);
		m1Panel.add(depPanel);
		labelList.add(depLabel);
		
		JPanel modelTypePanel = new JPanel();
		JLabel modelTypeLabel = new JLabel("Model type :");
		
		modelTypePanel.setLayout(new BorderLayout());
		modelTypePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		modelTypeBox = new JComboBox( new String[] { "Primary only", "Primary and Secondary" } );
		modelTypeBox.addActionListener( this );
		modelTypePanel.add(modelTypeLabel, BorderLayout.WEST);
		modelTypePanel.add(modelTypeBox, BorderLayout.CENTER);
		m1Panel.add(modelTypePanel);
		labelList.add(modelTypeLabel);
		
		JPanel tablePanel = new JPanel();
		JPanel innerPanel = new JPanel();
		JLabel tableLabel = new JLabel("Parameter definition :");
		
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		innerPanel.setLayout(new BorderLayout());
		innerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		table.addMouseListener( this );
		table.addKeyListener( this );
		table.getModel().addTableModelListener( this );
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		innerPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(table);
		innerPanel.add(scroll, BorderLayout.CENTER);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePanel.add(tableLabel, BorderLayout.WEST);
		tablePanel.add(innerPanel, BorderLayout.CENTER);
		tablePanel.setPreferredSize(new Dimension(modelTypeBox.getWidth(),3500));
		m1Panel.add(tablePanel);
		labelList.add(tableLabel);
		
		modelCatalog = new Hashtable<String,ParametricModel>();		
		literature = new LinkedList<String>();
		possLiterature = new HashMap<Integer, String>();
		
		JPanel literaturePanelPanel = new JPanel();
		JLabel literatureLabel = new JLabel("Reference :");
						
		literaturePanelPanel.setLayout(new BorderLayout());
		literaturePanelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));		
		literaturePanel = new JPanel();
		literaturePanel.setLayout( new GridLayout( 0, 1 ) );
		literaturePanelPanel.add(literatureLabel, BorderLayout.WEST);
		literaturePanelPanel.add(new JScrollPane(literaturePanel), BorderLayout.CENTER);
		m1Panel.add(literaturePanelPanel);
		labelList.add(literatureLabel);
		
		this.add(m1Panel);

		modLitMat = new HashMap<Integer, List<Integer>>();//boolean[ 0 ][ 0 ];
		
		litBox = new HashMap<Integer, CheckBoxWithID>();
		
		// setDb( "bfrdb\\DB", "SA", "" );
		
		int width = 0;
		
		for (JLabel label : labelList) {
			width = Math.max(width, label.getPreferredSize().width);
		}
		
		width += 10;
		
		for (JLabel label : labelList) {
			label.setMinimumSize(new Dimension(width, label.getMinimumSize().height));
			label.setPreferredSize(new Dimension(width, label.getMinimumSize().height));
		}
		table.getColumn("Indep").setMinWidth(0);
		table.getColumn("Indep").setMaxWidth(0);
	}
	
	@Override
	public void tableChanged( final TableModelEvent te ) {
		
		if (isLoading) {
			return;
		}
		
		int changedRow = te.getFirstRow();
		int changedCol = te.getColumn();
		String changedKey = table.getValueAt(changedRow, 0).toString();
		if (!changedKey.isEmpty()) {
			//System.out.println( "table changed:" + changedKey + "\t" + changedRow + "\t" + changedCol + "\t" +
				//	table.getValueAt( changedRow, changedCol) + "\t" + valSet.get(changedKey));
			if (changedCol > 1) {
				Double dbl = Double.NaN;
				try {
					dbl = Double.valueOf( ( String )table.getValueAt( changedRow, changedCol) );
				}
				catch( Exception e ) {}
				if (changedCol == 2) {
					valSet.put(changedKey, dbl);				
				}
				else if (changedCol == 3) {
					minSet.put(changedKey, dbl);
				}
				else if (changedCol == 4) {
					maxSet.put(changedKey, dbl);
				}
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
	private void setDbAux( final Bfrdb db ) {
		
		ResultSet result;
		String k, f;
		int i;
		
		literature.clear();
		modelCatalog.clear();
		ParametricModel pm = new ParametricModel(LABEL_OWNMODEL, "", "", 1);
		modelCatalog.put(LABEL_OWNMODEL, pm);
		//modelId = new Hashtable<String,Integer>();

		try {
			
			result = db.selectModel(1);
			
			while( result.next() ) {
				
				k = result.getString( Bfrdb.ATT_NAME );
				f = result.getString( Bfrdb.ATT_FORMULA );
				i = result.getInt( Bfrdb.ATT_MODELID );
				// initialize modelcatalog instance
				pm = new ParametricModel( k, f, result.getString( Bfrdb.ATT_DEP ), 1, i );
				manageMinMax(result, pm);
				manageIndep(pm, result.getArray( Bfrdb.ATT_INDEP ));
				
				modelNameBox.addItem( k );
				modelCatalog.put(k, pm);
				//model.put( k, new String[] { t, f } );
				//modelId.put(k, i);
				literature.add( db.getRelatedLiterature( k ) );
			}
			
			result.getStatement().close();
			result.close();
			
			possLiterature = db.getPossibleLiterature();
			modLitMat = db.getModLitMatrix();
			createPossibleLiterature();
			
			result = db.selectModel(2);
			
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
			
			
			db.close();
		}
		catch( Exception e ) {
			e.printStackTrace( System.err );
		}		
	}
	private void manageMinMax(ResultSet result, ParametricModel pm) throws SQLException {
		Array array = result.getArray( Bfrdb.ATT_PARAMNAME );
		Array arrayMin = result.getArray( Bfrdb.ATT_MINVALUE );
		Array arrayMax = result.getArray( Bfrdb.ATT_MAXVALUE );
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
		
	public void setDb( final String filename, final String login, final String passwd )
	throws ClassNotFoundException {
		
		Bfrdb db;
		
		try {
			
			db = new Bfrdb( filename, login, passwd );
			setDbAux( db );
		}
		catch( Exception e ) {
			e.printStackTrace( System.err );
		}
	}
	
	public void setDb( final Connection conn ) {
		
		Bfrdb db;
		try {
			
			db = new Bfrdb( conn );
			setDbAux( db );
		}
		catch( Exception e ) {
			e.printStackTrace( System.err );
		}
	}
	
	public void setDb( final BFRNodeService service ) {
		
		Bfrdb db;
		try {
			
			db = new Bfrdb( service );
			setDbAux( db );
		}
		catch( Exception e ) {
			e.printStackTrace( System.err );
		}
	}
	
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
	public void keyPressed( final KeyEvent ke ) {}
	
	@Override
	public void keyReleased( final KeyEvent ke ) {
				
		// this method gets called when the user types something in the formula text area
		if (ke.getSource().equals(formulaArea)) {
			isLoading = true;
			modelNameBox.setSelectedIndex(0);
			formulaUpdate();
			paramUpdate();
			isLoading = false;
		}
		
	}
	
	@Override
	public void actionPerformed( final ActionEvent e ) {
		
		// this method gets called when the user changes the modelNameBox item.
		
		if( e.getSource() == modelNameBox ) {
			
			if( modelNameBox.getSelectedIndex() == 0 ) { // Manually defined model
				return;
			}
			// System.out.println( "modelNameBox is the source." );
			clearTable();

			updatePossibleLiterature();
			updateLiterature();
			
/*			
			// System.out.println( "You changed the model." );
			if( modelNameBox.getSelectedIndex() == 0 ) { // Manually defined model
				formulaArea.setText( "" );
				formulaUpdate();
				paramUpdate();
				return;
			}
	*/		
			ParametricModel pm = modelCatalog.get( modelNameBox.getSelectedItem() );
			if (pm != null) {
				formulaArea.setText(pm.getFormula());
				formulaUpdate();
				paramUpdate(pm.getIndepVarSet().size() > 0 ? pm.getIndepVarSet().getFirst() : null);				
			}
			else {
				formulaArea.setText("");
				formulaUpdate();
				paramUpdate();
			}
			return;
		}
		
		if( e.getSource() == table ) {
			// System.out.println( "The table was triggered." );
		}
		if (e.getSource() == modelTypeBox) {
			try {
				if( getLevel() == 2 ) {
					table.getColumn("Value").setMinWidth(0);
					table.getColumn("Value").setMaxWidth(0);
				}
				else {
					table.getColumn("Value").setMinWidth(0);
					table.getColumn("Value").setMaxWidth(100);
					table.getColumn("Value").setPreferredWidth(60);
				}
			}
			catch (Exception e2) {}
			return;
		}
		
		if( e.getSource() instanceof CheckBoxWithID ) {
			CheckBoxWithID box = (CheckBoxWithID) e.getSource();
			ParametricModel pm = modelCatalog.get( modelNameBox.getSelectedItem() );
			List<Integer> li = modLitMat.get(pm.getModelId());
			if (li == null) {
				li = new ArrayList<Integer>();
			}
			if (box.isSelected()) {
				li.add(box.getID());
			} else {
				li.remove(box.getID());
			}
			modLitMat.put(pm.getModelId(), li);
			
			updateLiterature();
		}
		
		if (e.getSource() == indepBox) {
			if (e.getModifiers() == 16) {
				paramUpdate(indepBox.getSelectedItem() == null ? null : indepBox.getSelectedItem().toString());
			}
		}
		
		//paramUpdate();
		//secondaryUpdate();
	}
	
	@Override
	public void mouseClicked( final MouseEvent e ) {
		
		// this method gets called when the user clicks into the parameter table
		
		if( getLevel() != 2 ) {
			return;
		}
		
		if( table.getSelectedRow() >= getNumParam()-1 ) {
			return;
		}
		
		secondaryModel.setSelectedVar( table.getSelectedRow() );
		
		if (!secondaryModel.isVisible()) {
			secondaryModel.setLocationRelativeTo(this);
			secondaryModel.setAlwaysOnTop(true);
			secondaryModel.setVisible( true );			
		}
	}
	
	@Override
	public void mouseExited( final MouseEvent e ) {}
	@Override
	public void mousePressed( final MouseEvent e ) {}
	@Override
	public void mouseReleased( final MouseEvent e ) {}
	@Override
	public void mouseEntered( final MouseEvent e ) {}

	private void formulaUpdate() {
		// fetch formula string
		String formula = formulaArea.getText();
		formula = formula.replaceAll( "\n", "" );
		formula = formula.replaceAll( "\\s", "" );
		formula = formula.replace("~", "=");
		
		int index = formula.indexOf("=");
		if( index < 0 ) {
			depField.setText(formula.trim());
			return;
		}
		
		String depVar = formula.substring(0, index).trim();
		depField.setText(depVar);

		DJep parser = MathUtilities.createParser();
		//parser.removeVariable("x");
		try {
			parser.parse(formula);
			SymbolTable st = parser.getSymbolTable();
			keySet.clear();
		    for (Object o : st.keySet()) {
		    	String os = o.toString();
		        if (!keySet.contains(os) && !os.equals(depVar)) {
					keySet.add(os);
		        }		        
		    }
		}
		catch (ParseException e) {
			if (!e.getErrorInfo().startsWith("Unexpected \"<EOF>\"") &&
					!e.getErrorInfo().startsWith("Encountered \"-\" at")) {
				e.printStackTrace();
			}
		}

		//formula = formula.substring(index+1 );
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

	}
	
	private void clearTable() {
		// clear unused key and value pairs
		for(int i=0 ; i < MAX_PARAM; i++ ) {
			table.setValueAt( "", i, 0 );
			table.setValueAt( Boolean.FALSE, i, 1 );			
			table.setValueAt( "", i, 2 );			
			table.setValueAt( "", i, 3 );			
			table.setValueAt( "", i, 4 );			
		}
		table.revalidate();
	}
	private void paramUpdate() {	
		paramUpdate(null); 
	}
	private void paramUpdate(final String indepVar) {		

		String tIndepVar = indepVar;
		if (tIndepVar == null && indepBox.getSelectedItem() != null) {
			tIndepVar = indepBox.getSelectedItem().toString();
		}
				
		// add indep var candidates
		indepBox.removeAllItems();		
		for( int i = 0; i < MAX_PARAM; i++ ) {
			if (keySet.size() <= i || keySet.get(i) == null ) {
				break;
			}
			indepBox.addItem(keySet.get(i));
		}
		
		indepBox.setSelectedItem(tIndepVar);
		// wenn das alte indepVar gelöscht wurde, dann ist hier automatisch eine andere neue indepvar ausgewählt,
		// diese Info wird benötigt beim Füllen der Tabelle (indepVar soll da nicht auftauchen), siehe unten
		if (indepBox.getSelectedItem() != null) {
			tIndepVar = indepBox.getSelectedItem().toString();
		}

		int i = 0;
		for (int j = 0; j < getNumParam(); j++ ) {
			
			if (!keySet.get(j).equals(tIndepVar)) {
				table.setValueAt(keySet.get(j), i, 0 );
	//			table.setValueAt( valSet.get(keySet[ j ]), i, 1 );
				table.setValueAt( valSet.get(keySet.get(j)) == null || Double.isNaN(valSet.get(keySet.get(j))) ? "" : valSet.get(keySet.get(j)), i, 2 );
				Double min = minSet.get(keySet.get(j));
				if (min == null || Double.isNaN(min)) {
					min = modelCatalog.get(getModelName()).getParamMin(keySet.get(j));
				}
				table.setValueAt( min == null || Double.isNaN(min) ? "" : min, i, 3 );
				Double max = maxSet.get(keySet.get(j));
				if (max == null || Double.isNaN(max)) {
					max = modelCatalog.get(getModelName()).getParamMax(keySet.get(j));
				}
				table.setValueAt( max == null || Double.isNaN(max) ? "" : max, i, 4 );

				i++;
			}
		}
		
		// clear unused key and value pairs
		for( ; i < MAX_PARAM; i++ ) {
			table.setValueAt( "", i, 0 );
			table.setValueAt( Boolean.FALSE, i, 1 );			
			table.setValueAt( "", i, 2 );			
			table.setValueAt( "", i, 3 );			
			table.setValueAt( "", i, 4 );			
		}
		table.revalidate();
			
		secondaryUpdate();
	}
	
	protected void secondaryUpdate() {		
		secondaryModel.setParamList( getParamNameSet() );
	}
	
	
	public String getModelName() { return ( String )( modelNameBox.getSelectedItem() ); }
	public int getModelId() {
		
		Random rand;
		int i;

		if( modelNameBox.getSelectedIndex() == 0 ) {
			
			rand = new Random();
			i = rand.nextInt();
			if( i > 0 ) {
				i = -i;
			}
			
			return i;
		}
		
		ParametricModel pm = modelCatalog.get( modelNameBox.getSelectedItem() );
		return pm.getModelId();
	}
	public String getFormula() { return formulaArea.getText().replaceAll( "\n", "" ); }
	public String getIndepVar() { return ( String )( indepBox.getSelectedItem() ); }
	public String getDepVar() { return depField.getText(); }
	public int getLevel() { return modelTypeBox.getSelectedIndex()+1; }
	
	public int getNumParam() {
		/*
		int i;
		
		for( i = 0; i < MAX_PARAM; i++ ) {
			
			if(keySet.get(i) == null ) {
				return i;
			}
		}
		*/
		return keySet.size();
	}
	
	public LinkedList<String> getParamNameSet() {
		
		LinkedList<String> param;
		int i;
		
		param = new LinkedList<String>();
		
		for( i = 0; i < MAX_PARAM; i++ ) {
			
			if( i == indepBox.getSelectedIndex() ) {
				continue;
			}
			
			if (keySet.size() <= i || keySet.get(i) == null ) {
				break;
			}
			
			param.add(keySet.get(i));
		}

		return param;
	}
	
	public String getParamName( final int i ) {
		return keySet.get(i);
	}
	
	public double getParamValue( final int i ) {	
		if (valSet.get(keySet.get(i)) == null) {
			return Double.NaN;
		} else {
			return valSet.get(keySet.get(i));
		}
	}
	private double getMinValue( final int i ) {	
		if (minSet.get(keySet.get(i)) == null) {
			return Double.NaN;
		} else {
			return minSet.get(keySet.get(i));
		}
	}
	private double getMaxValue( final int i ) {	
		if (maxSet.get(keySet.get(i)) == null) {
			return Double.NaN;
		} else {
			return maxSet.get(keySet.get(i));
		}
	}
	
	public void setTS(String agent, String matrix, String comment,
			double temp, double ph, double aw) throws PmmException {
		agentField.setText(agent);
		matrixField.setText(matrix);
		commentField.setText(comment);
		temperatureField.setValue(temp);
		phField.setValue(ph);
		waterActivityField.setValue(aw);
	}
	public PmmTimeSeries getTS() throws PmmException {
		if (!temperatureField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		PmmTimeSeries tuple = new PmmTimeSeries();

		tuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, agentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, matrixField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_COMMENT, commentField.getText());
		tuple.setValue(TimeSeriesSchema.ATT_TEMPERATURE, temperatureField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_PH, phField.getValue());
		tuple.setValue(TimeSeriesSchema.ATT_WATERACTIVITY, waterActivityField.getValue());
		
		return tuple;
	}
	public String toXmlString() {
		
		PmmXmlDoc doc = new PmmXmlDoc();
		
		// initialize primary model
		ParametricModel pm = new ParametricModel( getModelName(), getFormula(), getDepVar(), 1, getModelId() );
		
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
		int i, n;
			
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
/*
			j = modelNameBox.getSelectedIndex()-1;
			if( j < 0 ) {
				isLoading = false;
				return;
			}
	*/		
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
	
	public void setModelName( final String name ) { modelNameBox.setSelectedItem( name ); }
	
	public void setFormula( final String formula ) {
		formulaArea.setText( formula );
		formulaUpdate();
		paramUpdate();
	}
	
	public void setIndepVar(final String indep) {
		setIndepVar(indep, null, null);
	}
	public void setIndepVar(final String indep, Double min, Double max) {
		if (min != null && !min.isNaN()) {
			indepMinVal.setText(min.toString());
		} else {
			indepMinVal.setText("");
		}
		if (max != null && !max.isNaN()) {
			indepMaxVal.setText(max.toString());
		} else {
			indepMaxVal.setText("");
		}
		indepBox.setSelectedItem(indep);
		formulaUpdate();
		paramUpdate();
	}
	public void setLevel( final int i ) { modelTypeBox.setSelectedIndex( i-1 ); }
	private void setParam( final String name, final double value, final double min, final double max ) {
		
		int i;
		
		for( i = 0; i < MAX_PARAM; i++ ) {
			if( keySet.size() > i && keySet.get(i).equals( name ) ) {
				valSet.put(name, value);
				minSet.put(name, min);
				maxSet.put(name, max);
				break;
			}
		}
			
		
		
		/* i = paramKey.indexOf( name );
		if( i < 0 )
			return;
		paramVal.set( i, value ); */
	}

	public void stopCellEditing() {
		if (table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
	}
	
	private void updateLiterature() {
		
		//System.out.print( "Updating literature line ..." );
		
		if(modelNameBox.getSelectedIndex() < 1) {
			return;
		}
		
		/*
		literatureField.setText( "" );
		for (Map.Entry<Integer, String> entry : possLiterature.entrySet()) {
		    Integer key = entry.getKey();
		    String value = entry.getValue();
			List<Integer> li = modLitMat.get(modelId.get(modelNameBox.getSelectedItem()));
		    if (li != null && li.contains(key)) {
				if( !literatureField.getText().isEmpty() ) {
					literatureField.setText( literatureField.getText() + ", " );
				}
				literatureField.setText( literatureField.getText() + value);
		    }
		}
		*/
		
		//System.out.println( " OK" );
	}
	
	private void updatePossibleLiterature() {
		
		//System.out.print( "Updating possible literature ..." );
		
		literaturePanel.setVisible(false);
		for (Map.Entry<Integer, CheckBoxWithID> entry : litBox.entrySet()) {
			CheckBoxWithID value = entry.getValue();
			value.setSelected(false);
		}
		//if (modelNameBox.getSelectedIndex() >= 1) {
		ParametricModel pmc = modelCatalog.get( modelNameBox.getSelectedItem() );
			for (Map.Entry<Integer, String> entry : possLiterature.entrySet()) {
			    Integer key = entry.getKey();
			    //String value = entry.getValue();
				List<Integer> li = modLitMat.get(pmc.getModelId());
			    if (li != null) {
					litBox.get(key).setSelected(li.contains(key));
				}
			}
		//}
		literaturePanel.validate();
		literaturePanel.setVisible(true);
		
		//System.out.println( " OK" );
	}
	
	private void createPossibleLiterature() {
		
		//System.out.print( "Creating literature list ..." );
		
		literaturePanel.removeAll();
		for (Map.Entry<Integer, String> entry : possLiterature.entrySet()) {
		    Integer key = entry.getKey();
		    String value = entry.getValue();
		    CheckBoxWithID box = new CheckBoxWithID(value);
		    box.setID(key);
			box.addActionListener( this );
			literaturePanel.add( box );
			litBox.put(key, box);
		}		
		
		//System.out.println( " OK");
	}
}
