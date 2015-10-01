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
package de.bund.bfr.numl2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NuMLWriter {

	public NuMLWriter() {
	}

	public void write(NuMLDocument doc, StreamResult out) throws SAXException, IOException,
			ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		xmlDoc.appendChild(doc.toNode(xmlDoc));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(new DOMSource(xmlDoc), out);
	}

	public String toString(NuMLDocument doc) throws IOException, SAXException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			new NuMLWriter().write(doc, new StreamResult(stream));

			return stream.toString(StandardCharsets.UTF_8.name());
		}
	}
}
