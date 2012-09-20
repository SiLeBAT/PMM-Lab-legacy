/**
 * Project: de.dim.bfr.jpa
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.jpa/src/de/dim/bfr/jpa/service/EMFToJPAMapper.java $
 * $LastChangedDate: 2012-01-24 10:37:40 +0100 (Di, 24 Jan 2012) $
 * $lastChangedBy$
 * $Revision: 650 $
 * /*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.jpa.service;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Maps EMF objects to JPA Pojo using data binding
 * @author Mark Hoffmann
 * @since 08.12.2011
 */
public class EMFToJPAMapper {
	
	private final EObject emfObject;
	private final Object jpaObject;
	private final MappingDirection direction;
	private final DataBindingContext dbc = new DataBindingContext();

	public static enum MappingDirection{
		EMF2JPA,
		JPA2EMF
	}
	
	/**
	 * Constructor that creates a mapper with direction EMF2JPA
	 * @param emfObject the EMF object
	 * @param jpaObject the JPA object
	 */
	public EMFToJPAMapper(EObject emfObject, Object jpaObject) {
		this(emfObject, jpaObject, MappingDirection.EMF2JPA);
	}
	
	/**
	 * Constructor that creates a mapper with direction EMF2JPA
	 * @param emfObject the EMF object
	 * @param jpaObject the JPA object
	 * @param direction the direction
	 */
	public EMFToJPAMapper(EObject emfObject, Object jpaObject, MappingDirection direction) {
		this.emfObject = emfObject;
		this.jpaObject = jpaObject;
		this.direction = direction;
	}
	
	/**
	 * Bind a value using a converter
	 * @param feature the EMF feature
	 * @param pojoProperty the field name of the POJO property
	 */
	public void bindValue(EStructuralFeature feature, String pojoProperty) {
		bindValue(feature, pojoProperty, null);
	}
	
	/**
	 * Bind a value using a converter
	 * @param feature the EMF feature
	 * @param pojoProperty the field name of the POJO property
	 * @param converter the {@link IConverter}
	 */
	public void bindValue(EStructuralFeature feature, String pojoProperty, IConverter converter) {
		UpdateValueStrategy t2mUVS = getT2MStrategy(converter);
		UpdateValueStrategy m2tUVS = getM2TStrategy(converter);
		dbc.bindValue(EMFObservables.observeValue(emfObject, feature), PojoObservables.observeValue(jpaObject, pojoProperty), t2mUVS, m2tUVS);
	}
	
	/**
	 * Executes the update
	 */
	public void update() {
		switch (direction) {
		case EMF2JPA:
			dbc.updateModels();
			break;
		default:
			dbc.updateTargets();
			break;
		}
	}
	
	/**
	 * Dispose this mapper
	 */
	public void dispose() {
		dbc.dispose();
	}
	
	/**
	 * Creates the target to model {@link UpdateValueStrategy} depending on the sync direction
	 * @param converter the converter, can be <code>null</code>
	 * @return the {@link UpdateValueStrategy}
	 */
	private UpdateValueStrategy getT2MStrategy(IConverter converter) {
		switch (direction) {
		case EMF2JPA:
			return createManualStategy(converter);
		default:
			return createNeverStategy();
		}
	}
	
	/**
	 * Creates the model to target {@link UpdateValueStrategy} depending on the sync direction
	 * @param converter the converter, can be <code>null</code>
	 * @return the {@link UpdateValueStrategy}
	 */
	private UpdateValueStrategy getM2TStrategy(IConverter converter) {
		switch (direction) {
		case EMF2JPA:
			return createNeverStategy();
		default:
			return createManualStategy(converter);
		}
	}
	
	/**
	 * Create a {@link UpdateValueStrategy} with the police never, which means no update
	 * @return the {@link UpdateValueStrategy}
	 */
	public static UpdateValueStrategy createNeverStategy() {
		return new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
	}
	
	/**
	 * Create a {@link UpdateValueStrategy} with the police on request, which means only manually triggered updates
	 * via {@link DataBindingContext#updateModels()}
	 * @param converter the converter, can be <code>null</code>
	 * @return the {@link UpdateValueStrategy}
	 */
	public static UpdateValueStrategy createManualStategy(IConverter converter) {
		UpdateValueStrategy uvs = new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST);
		if (converter != null) {
			uvs.setConverter(converter);
		}
		return uvs;
	}
	
	/**
	 * Create a {@link UpdateValueStrategy} with the police on request, which means only manually triggered updates
	 * via {@link DataBindingContext#updateModels()}
	 * @return the {@link UpdateValueStrategy}
	 */
	public static UpdateValueStrategy createManualStategy() {
		return createManualStategy(null);
	}

}
