package de.bund.bfr.knime.pmm.file;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

public class PMFMetadataNode {

	private final static String MODEL_TYPE_TAG = "modeltype";
	private final static String MASTER_FILE_TAG = "masterFile";

	String modelType;
	Set<String> masterFiles;
	Element node;

	public PMFMetadataNode(String modelType, Set<String> masterFiles) {
		this.modelType = modelType;
		this.masterFiles = masterFiles;
		
		node = new Element("metaParent");
		
		Element modelTypeElement = new Element(MODEL_TYPE_TAG);
		modelTypeElement.addContent(modelType);
		node.addContent(modelTypeElement);
		
		for (String masterFile : masterFiles) {
			Element masterFileElement = new Element("masterFile");
			masterFileElement.addContent(masterFile);
			node.addContent(masterFileElement);
		}
	}
	
	public PMFMetadataNode(Element node) {
		this.node = node;
		modelType = node.getChild(MODEL_TYPE_TAG).getText();
		
		List<Element> masterFileElements = node.getChildren(MASTER_FILE_TAG);
		masterFiles = new HashSet<>(masterFileElements.size());
		for (Element masterFileElement : masterFileElements) {
			masterFiles.add(masterFileElement.getText());
		}
	}

	public String getModelType() {
		return modelType;
	}
	
	public Set<String> getMasterFiles() {
		return masterFiles;
	}
	
	public Element getNode() {
		return node;
	}
}
