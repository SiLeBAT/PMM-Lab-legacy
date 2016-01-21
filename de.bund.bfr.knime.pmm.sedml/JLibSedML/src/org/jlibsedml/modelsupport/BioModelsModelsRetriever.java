package org.jlibsedml.modelsupport;

import java.net.URI;

import org.jlibsedml.execution.IModelResolver;

/**
 * Class for retreiving SBML models from Biomodels, using a MIRIAM compliant URN
 * @author radams
 *
 */
public class BioModelsModelsRetriever extends AbstractResourceRetriever implements IModelResolver{
    
    public final static String BIOMODELS_DOWNLOAD_URL="http://www.ebi.ac.uk/biomodels-main/download?mid=";
	public final static String MIRIAM_BMDELS="urn:miriam:biomodels.db:";
	
	
	



	/**
	 * Gets model based on a MIRIAM URN of the form: <ul>
	 * <li> urn:miriam:biomodels.db:BIOMD0000000XXX.xml or
	 * <li>  urn:miriam:biomodels.db:BIOMD0000000XXX
	 * </ul>
	 * @return A <code>String</code> of the BioModels model in SBML, or <code>null</code> if could
	 *  not be found, <code>modelURI</code> is null, the network was unavailable or an internal exception was thrown.
	 */
    public String getModelXMLFor(final URI modelURI) {
        if(modelURI == null){
            return null;
        }
        String uriAsStr=modelURI.toString().trim();
        if(!uriAsStr.startsWith(MIRIAM_BMDELS)){
            return null;
        }
        if (uriAsStr.substring(uriAsStr.lastIndexOf(":"), uriAsStr.length()).length() != 16 &&
                !uriAsStr.endsWith(".xml") ||
                uriAsStr.substring(uriAsStr.lastIndexOf(":"), uriAsStr.length()).length() != 20 &&
                uriAsStr.endsWith(".xml")    ){
            return null;
        }
        String suffix = uriAsStr.substring(uriAsStr.lastIndexOf(":")+1);
        if(suffix.lastIndexOf(".")!=-1){
            suffix=suffix.substring(0,suffix.lastIndexOf("."));
        }
        
      return getResource(BIOMODELS_DOWNLOAD_URL + suffix);
        
    }

}
