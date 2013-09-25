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
/**
 * 
 */
package org.hsh.bfr.db;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * @author Weiser
 *
 */
public class SendMail {
	
	public static void main(String[] args) {
		 Desktop desktop = null;
		    // Before more Desktop API is used, first check 
		    // whether the API is supported by this particular 
		    // virtual machine (VM) on this particular host.
		    if (Desktop.isDesktopSupported()) {
		        desktop = Desktop.getDesktop();
		        String emailAddress = "";//"armin.weiser@bfr.bund.de"; 
				String subject = "Logfiles-Fehlermeldung"; 
				String body = "(Bitte die letzten 5 Logdateien anhaengen - sie liegen im Ordner '" + DBKernel.HSH_PATH + "LOGs')"; 
				try {
					body = URLEncoder.encode(body, "UTF-8");
				}
				catch (UnsupportedEncodingException e) {
					MyLogger.handleException(e);
				}
				body = body.replace("+", "%20");

		        try {
		            if (emailAddress.length() > 0) {
		            	URI uriMailTo = new URI("mailto", emailAddress, null);
		            	uriMailTo = new URI("mailto:" + emailAddress + "?subject=" + subject + "&body=" + body);
		                desktop.mail(uriMailTo);
		            } else {
		                desktop.mail();
		            }
		        }
		        catch(IOException ioe) {
		        	MyLogger.handleException(ioe);
		        }
		        catch(URISyntaxException use) {
		        	MyLogger.handleException(use);
		        }
		    }
	}
}
