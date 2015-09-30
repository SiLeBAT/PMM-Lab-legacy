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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NuMLReader {

	public static void main(String[] args) throws FileNotFoundException, SAXException, IOException,
			ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		NuMLDocument doc = new NuMLReader().read(new InputSource(new FileReader("C:/Users/thoens/Downloads/numl.xml")));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		new NuMLWriter().write(doc, new StreamResult(stream));

		System.out.println(stream.toString(StandardCharsets.UTF_8.name()));
	}

	public NuMLReader() {
	}

	public NuMLDocument read(InputSource in) throws SAXException, IOException, ParserConfigurationException {
		Document xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		NuMLDocument doc = new NuMLDocument(xmlDoc.getDocumentElement());

		return doc;
	}
}
