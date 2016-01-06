/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.jsondecoder;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "JSONDecoder" Node.
 * Turns a PMM Lab table into JSON
 *

 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 

 * 
 * @author Miguel Alba
 */
public class JSONDecoderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring JSONDecoder node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected JSONDecoderNodeDialog() {
        super();
    }
}

