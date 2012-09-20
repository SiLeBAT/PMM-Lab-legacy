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
package de.bund.bfr.knime.pmm.common;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DbConfigurationUi extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 20120622;
	
	private JCheckBox overrideBox;
	private JTextField connField;
	private JTextField loginField;
	private JPasswordField passwdField;
	private JButton chooseButton;
	private JButton applyButton;

	public static final String PARAM_FILENAME = "filename";
	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_PASSWD = "passwd";
	public static final String PARAM_OVERRIDE = "override";
	
	public DbConfigurationUi() { this( false ); }

	public DbConfigurationUi( boolean hasApplyButton ) {
		
		JPanel panel, panel2, panel0;
		
		setLayout( new BorderLayout() );
		// setPreferredSize( new Dimension( 330, 130 ) );
		
		panel0 = new JPanel();
		panel0.setLayout( new BorderLayout() );
		
		add( panel0, BorderLayout.SOUTH );
				
		panel = new JPanel();
		panel.setLayout( new GridLayout( 0, 1 ) );
		panel0.add( panel, BorderLayout.WEST );
		// panel.add( new JLabel() );
		panel.add( new JLabel( "Database : " ) );
		panel.add( new JLabel( "Login : " ) );
		panel.add( new JLabel( "Password : " ) );
		panel.add( new JLabel() );
		
		panel = new JPanel();
		panel.setLayout( new GridLayout( 0, 1 ) );
		panel0.add( panel, BorderLayout.CENTER );
		
		
		overrideBox = new JCheckBox( "Use external database" );
		overrideBox.addActionListener( this );
		add( overrideBox, BorderLayout.CENTER );
		
		panel2 = new JPanel();
		panel2.setLayout( new BorderLayout() );
		panel.add( panel2 );
		
		connField = new JTextField();
		connField.setEditable( false );
		panel2.add( connField, BorderLayout.CENTER );
		
		chooseButton = new JButton( "..." );
		chooseButton.addActionListener( this );
		panel2.add( chooseButton, BorderLayout.EAST );
		panel2.add( new JLabel( "jdbc:hsqldb:file:" ), BorderLayout.WEST );
		
		loginField = new JTextField();
		loginField.setEditable( false );
		panel.add( loginField );
		
		passwdField = new JPasswordField();
		passwdField.setEditable( false );
		panel.add( passwdField );
		
		if( hasApplyButton ) {
			
			panel2 = new JPanel();
			panel2.setLayout( new BoxLayout( panel2, BoxLayout.X_AXIS ) );
			panel.add( panel2 );
			
			applyButton = new JButton( "Apply" );
			panel2.add( Box.createHorizontalGlue() );
			panel2.add( applyButton );
		}
		else
			panel.add( new JLabel() );
		
	}
	
	@Override
	public void actionPerformed( ActionEvent e ) {
		
		JFileChooser chooser;
		int retValue;
		String candidate;
		String suffix;
		int i;
		
		if( e.getSource() == overrideBox ) {
			
			// the override box has been altered
			System.out.println( "the override box has been altered" );
			
			if( overrideBox.isSelected() ) {
				connField.setEditable( true );
				loginField.setEditable( true );
				passwdField.setEditable( true );
			}
			else {
				connField.setEditable( false );
				loginField.setEditable( false );
				passwdField.setEditable( false );
			}
		}
		
		if( e.getSource() == chooseButton ) {
			
			// the choose button has been clicked
			System.out.println( "the choose button has been clicked" );
			
			if( overrideBox.isSelected() ) {
				
				System.out.println( "open file chooser" );
				
				chooser = new JFileChooser();
				retValue = chooser.showOpenDialog( this );
				
				if( retValue == JFileChooser.APPROVE_OPTION ) {
					
					System.out.println( "The user approved." );
					
					candidate = chooser.getSelectedFile().getAbsolutePath();
					System.out.println( candidate );
					i = candidate.lastIndexOf( '\\' );
					
					if( i < 0 )
						i = candidate.lastIndexOf( '/' );
					
					if( i < 0 )
						suffix = candidate;
					else
						suffix = candidate.substring( i );
					
					System.out.println( suffix );
					
					i = suffix.indexOf( '.' );
					if( i < 0 )
						suffix = "";
					else
						suffix = suffix.substring( i );

					System.out.println( suffix );
					
					candidate = candidate.substring( 0,
						candidate.length()-suffix.length() );
					
					System.out.println( candidate );
					
					connField.setText( candidate );
				}
			}
			
		}
	}
	
	public boolean getOverride() {
		return overrideBox.isSelected();
	}
	
	public void setOverride( boolean override ) {
		
		if( override != overrideBox.isSelected() )
			overrideBox.doClick();
	}
	
	public void setFilename( String filename ) {
		connField.setText( filename );
	}
	
	public void setLogin( String login ) {
		loginField.setText( login );
	}
	
	public void setPasswd( String passwd ) {
		passwdField.setText( passwd );
	}
	
	public boolean isOverride() { return overrideBox.isSelected(); }
	public String getFilename() { return connField.getText(); }
	public String getLogin() { return loginField.getText(); }
	public String getPasswd() { return String.valueOf( passwdField.getPassword() ); }
	
	public JButton getApplyButton() { return applyButton; }
	
	
}
