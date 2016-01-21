package org.jlibsedml.validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jlibsedml.SedMLError;
import org.jlibsedml.XMLException;
import org.jlibsedml.SedMLError.ERROR_SEVERITY;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class SchemaValidatorImpl {
    
    static final String SEDML_SCHEMA = "schema.xsd";
    static final String SBML_MATHML_SCHEMA = "sedml-mathml.xsd";
    public void validateSedMLString(final List<SedMLError> errors,
            String xmlAsString) throws XMLException {
       
        SchemaFactory factory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        File math = loadSchema(SBML_MATHML_SCHEMA);
        File sed = loadSchema(SEDML_SCHEMA);

        StreamSource s1 = new StreamSource(math);
        StreamSource s2 = new StreamSource(sed);

        Schema schema = null;
        try {
            schema = factory.newSchema(new Source[] {s1, s2 });
        } catch (SAXException e1) {
            System.err.println(e1.getMessage());
            throw new XMLException("Error parsing schema files", e1);
        }
        Validator xmlValidator = schema.newValidator();

        xmlValidator.setErrorHandler(new DefaultXMLParsingErrorHandler(errors));
        

        // at last perform validation:
        try {
            xmlValidator.validate(new StreamSource(new StringReader(xmlAsString)));
        } catch (SAXException e) {
            throw new XMLException("Error parsing XML",e);
        } catch (IOException e) {
            throw new XMLException("Error parsing XML",e);
        }
    }

    class DefaultXMLParsingErrorHandler implements ErrorHandler {
        private List<SedMLError> errors;

        public DefaultXMLParsingErrorHandler(List<SedMLError> errors) {
            this.errors = errors;
        }

        public void warning(SAXParseException exception) throws SAXException {
            
            errors.add(new SedMLError(exception.getLineNumber(), exception
                    .getMessage(), ERROR_SEVERITY.WARNING));

        }

        public void fatalError(SAXParseException exception) throws SAXException {
        
            errors.add(new SedMLError(exception.getLineNumber(), exception
                    .getMessage(), ERROR_SEVERITY.FATAL));

        }

        public void error(SAXParseException exception) throws SAXException {
            errors.add(new SedMLError(exception.getLineNumber(), exception
                    .getMessage(), ERROR_SEVERITY.ERROR));
        }
    }

    private  File loadSchema(final String schema) {
         
       InputStream is2 = SchemaValidatorImpl.class.getClassLoader().getResourceAsStream(schema);
        

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        FileOutputStream fos2 = null;
        File sed = null;
        try {
            byte[] buf = new byte[1024];

            int read = 0;
            while ((read = is2.read(buf)) != -1) {
                baos2.write(buf, 0, read);
            }
            sed = File.createTempFile("xsd", "xsd");
            fos2 = new FileOutputStream(sed);
            fos2.write(baos2.toByteArray());
            fos2.flush();

        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        } finally {
            try {
                if (fos2 != null) {
                    fos2.close();
                }
                baos2.close();
                // is2.close();
            } catch (IOException e) {
            }

        }
        return sed;
    }


}
