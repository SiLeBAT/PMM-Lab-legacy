/*******************************************************************************
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

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * Converter that supports directions
 * @author Mark Hoffmann
 * @since 08.12.2011
 */
public abstract class DirectionConverter implements IConverter {

	protected final EMFToJPAMapper.MappingDirection direction;

	/**
	 * Constructor with direction parameter
	 * @param direction the sync direction
	 */
	public DirectionConverter(EMFToJPAMapper.MappingDirection direction) {
		this.direction = direction;
	}
	
	/**
	 * Constructor with default sync direction EMF to JPA
	 */
	public DirectionConverter() {
		this(EMFToJPAMapper.MappingDirection.EMF2JPA);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	@Override
	public final Object convert(Object fromObject) {
		return EMFToJPAMapper.MappingDirection.EMF2JPA.equals(direction) ? convertEMF2JPA(fromObject) : convertJPA2EMF(fromObject);
	}

	/**
	 * Convert method that will be called if the sync direction is EMF 2 JPA
	 * @param fromObject the source object EMF
	 * @return the converted object JPA
	 */
	abstract Object convertEMF2JPA(Object fromObject);

	/**
	 * Convert method that will be called if the sync direction is JPA 2 EMF
	 * @param fromObject the source object JPA
	 * @return the converted object EMF
	 */
	abstract Object convertJPA2EMF(Object fromObject);

}
