/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2011
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, version 2, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ------------------------------------------------------------------------
 *
 * History
 *   19.09.2007 (thiel): created
 */
package de.dim.bfr.knime.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 *
 * @author Kilian Thiel, University of Konstanz
 */
public class CustomRPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage 
{	
	
	private boolean somethingChanged = false;
	
	/**
     * Creates a new preference page.
     */
    public CustomRPreferencePage() {
        super(GRID);        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createFieldEditors() 
    {
    	Composite parent = getFieldEditorParent();
    	
    	
    	//addField(new FileFieldEditor(BfRNodePluginActivator.R_PATH, "Path to R executable", parent));
    	addField(new ComboFieldEditor(BfRNodePluginActivator.DB_LOCATION, "Database location", getLabelsAndValues(), parent));
        addField(new StringFieldEditor(BfRNodePluginActivator.DB_PATH, "Path to Database", parent));
        addField(new StringFieldEditor(BfRNodePluginActivator.USERNAME, "Username",  parent));
        addField(new PasswordFieldEditor(BfRNodePluginActivator.PASSWORD, "Password",  parent));

    }
    
    private String[][] getLabelsAndValues() {
    	String[][] labelsValues = new String[3][2];
    	labelsValues[0][0] = "File";
    	labelsValues[0][1] = "jdbc:hsqldb:file:";
    	labelsValues[1][0] = "Server";
    	labelsValues[1][1] = "jdbc:hsqldb:hsql:";
    	labelsValues[2][0] = "In Memory";
    	labelsValues[2][1] = "jdbc:hsqldb:mem:";
    	return labelsValues;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final IWorkbench workbench) 
    {
        setPreferenceStore(BfRNodePluginActivator.getDefault().getPreferenceStore());
        setDescription("PmmLab-DB preferences");
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event) {
    	// TODO Auto-generated method stub
    	super.propertyChange(event);
    	somethingChanged = true;
    }
    
    public boolean performOk() {
    	if(super.performOk() && somethingChanged){
    		BfRNodePluginActivator.getDefault().getBfRService().refreshServiceContent();
    		somethingChanged = false;
    	}
    	return true;
    }
}
