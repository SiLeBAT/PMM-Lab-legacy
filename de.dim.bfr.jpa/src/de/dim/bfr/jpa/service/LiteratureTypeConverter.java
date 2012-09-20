/**
 * Project: de.dim.bfr.jpa
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.jpa/src/de/dim/bfr/jpa/service/LiteratureTypeConverter.java $
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

import de.dim.bfr.LiteraturTyp;

/**
 * Converter for {@link LiteraturTyp}
 * @author Mark Hoffmann
 * @since 08.12.2011
 */
public class LiteratureTypeConverter extends DirectionConverter {

	public LiteratureTypeConverter(EMFToJPAMapper.MappingDirection direction) {
		super(direction);
	}
	
	public LiteratureTypeConverter() {
		super();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
	 */
	@Override
	public Object getFromType() {
		return EMFToJPAMapper.MappingDirection.JPA2EMF.equals(direction) ? Integer.TYPE : LiteraturTyp.class;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
	 */
	@Override
	public Object getToType() {
		return EMFToJPAMapper.MappingDirection.JPA2EMF.equals(direction) ? LiteraturTyp.class : Integer.TYPE;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.jpa.service.DirectionConverter#convertEMF2JPA(java.lang.Object)
	 */
	@Override
	Object convertEMF2JPA(Object fromObject) {
		return fromObject == null ? null : ((LiteraturTyp)fromObject).getValue();
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.jpa.service.DirectionConverter#convertJPA2EMF(java.lang.Object)
	 */
	@Override
	Object convertJPA2EMF(Object fromObject) {
		int type = 0;
		if (fromObject != null) {
			type = ((Integer)fromObject).intValue();
		}
		return LiteraturTyp.get(type);
	}

}
