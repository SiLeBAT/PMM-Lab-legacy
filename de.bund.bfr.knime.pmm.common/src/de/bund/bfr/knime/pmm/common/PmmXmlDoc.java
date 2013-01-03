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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class PmmXmlDoc {

	private static final String ELEMENT_PMMDOC = "PmmDoc";
	
	private List<PmmXmlElementConvertable> elementSet;
	
	public PmmXmlDoc() {
		elementSet = new ArrayList<PmmXmlElementConvertable>();
	}
	
	public PmmXmlDoc(Element rootElement) throws IOException, JDOMException {		
		parseElement(rootElement);
	}
	public PmmXmlDoc(String xmlString) throws IOException, JDOMException {		
		this();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlString));
		
		Element rootElement = doc.getRootElement();
		parseElement(rootElement);
	}
	private void parseElement(Element rootElement) {
		for (Element el : rootElement.getChildren()) {
			/*
			if (el instanceof PmmXmlElementConvertable) {
				elementSet.add(new ParametricModel(el));
			}
			*/
			if (el.getName().equals(ParametricModel.ELEMENT_PARAMETRICMODEL)) {
				elementSet.add(new ParametricModel(el));
			}	
			else if (el.getName().equals(MiscXml.ELEMENT_MISC)) {
				elementSet.add(new MiscXml(el));
			}	
			else if (el.getName().equals(ParamXml.ELEMENT_PARAM)) {
				elementSet.add(new ParamXml(el));
			}	
			else if (el.getName().equals(IndepXml.ELEMENT_INDEP)) {
				elementSet.add(new IndepXml(el));
			}	
			else if (el.getName().equals(DepXml.ELEMENT_DEPENDENT)) {
				elementSet.add(new DepXml(el));
			}	
			else if (el.getName().equals(TimeSeriesXml.ELEMENT_TIMESERIES)) {
				elementSet.add(new TimeSeriesXml(el));
			}	
			else if (el.getName().equals(LiteratureItem.ELEMENT_LITERATURE)) {
				elementSet.add(new LiteratureItem(el));
			}	
			else if (el.getName().equals(CatalogModelXml.ELEMENT_CATALOGMODEL)) {
				elementSet.add(new CatalogModelXml(el));
			}	
			else if (el.getName().equals(EstModelXml.ELEMENT_ESTMODEL)) {
				elementSet.add(new EstModelXml(el));
			}	
		}					
	}
	public void add(PmmXmlElementConvertable el) {
		elementSet.add(el);
	}
	
	public Document toXmlDocument() {		
		Document doc = new Document();		
		Element rootElement = new Element(ELEMENT_PMMDOC);
		doc.setRootElement(rootElement);
		
		for (PmmXmlElementConvertable element : elementSet) {
			rootElement.addContent(element.toXmlElement());			
		}		
		return doc;
	}
	
	public String toXmlString() {		
		Document doc = toXmlDocument();		
		XMLOutputter xmlo = new XMLOutputter();
		return xmlo.outputString(doc);
	}
	
	public int size() {return elementSet.size();}
	public PmmXmlElementConvertable get(int i) {return elementSet.get(i);}
	
	public List<PmmXmlElementConvertable> getElementSet() {return elementSet;}
	
}
