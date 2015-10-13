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
package de.bund.bfr.numl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.base.Strings;

public abstract class NMBase {

	final static String NUML_NS = "http://www.numl.org/numl/level1/version1";
	private static final String META_ID = "metaId";

	protected String metaId;

	protected Element annotation;
	protected Element notes;

	public NMBase() {
		metaId = null;
		annotation = null;
		notes = null;
	}

	protected NMBase(Element node) {
		metaId = Strings.emptyToNull(node.getAttribute(META_ID));
		
		NodeList annotationNodes = node.getElementsByTagName("annotation");
		if (annotationNodes.getLength() == 1) {
			annotation = (Element) annotationNodes.item(0);
		} else {
			annotation = null;
		}
		
		NodeList notesNodes = node.getElementsByTagName("notes");
		if (annotationNodes.getLength() == 1) {
			notes = (Element) notesNodes.item(0);
		} else {
			notes = null;
		}
	}

	public String getMetaId() {
		return metaId;
	}

	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}
	
	public Element getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation(Element annotation) {
		this.annotation = annotation;
	}
	
	public Element getNotes() {
		return notes;
	}
	
	public void setNotes(Element notes) {
		this.notes = notes;
	}

	public abstract Iterable<? extends NMBase> getChildren();

	protected abstract Element toNode(Document doc);

	protected void updateNode(Element node) {
		Utils.setAttributeValue(node, META_ID, metaId);
	}
	
	protected void addAnnotationAndNotes(Document doc, Element node) {
		if (annotation != null) {
			node.appendChild(doc.importNode(annotation, true));
		}
		if (notes != null) {
			node.appendChild(doc.importNode(notes, true));
		}
	}
}
