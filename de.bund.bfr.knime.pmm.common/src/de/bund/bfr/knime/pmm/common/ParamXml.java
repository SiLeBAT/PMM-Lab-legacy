package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

public class ParamXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAM = "param";

	private String name = null;
	private String origName = null;
	private Double value = null;
	private Double error = null;
	private Double min = null;
	private Double max = null;
	private Double P = null;
	private Double t = null;
	private Double minGuess = null;
	private Double maxGuess = null;
	private String category = null;
	private String unit = null;
	private String description = null;
	
	private HashMap<String, Double> correlations = new HashMap<String, Double>();
	
	public ParamXml(String name, Double value) {
		this(name, value, null, null, null, null, null);
	}
	public ParamXml(String name, Double value, Double error, Double min, Double max, Double P, Double t) {
		this(name, value, error, min, max, P, t, null, null);
	}
	public ParamXml(String name, Double value, Double error, Double min, Double max, Double P, Double t, String category, String unit) {
		setName(name);
		setOrigName(name);
		setValue(value);
		setError(error);
		setMin(min);
		setMax(max);
		setP(P);
		sett(t);
		setCategory(category);
		setUnit(unit);
	}
	public ParamXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			setOrigName(xmlElement.getAttribute("origname").getValue());
			String strDbl = xmlElement.getAttribute("value").getValue();
			setValue(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("error").getValue();
			setError(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("min").getValue();
			setMin(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("max").getValue();
			setMax(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("P").getValue();
			setP(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("t").getValue();
			sett(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("minGuess").getValue();
			setMinGuess(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("maxGuess").getValue();
			setMaxGuess(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("category") != null ? xmlElement.getAttribute("category").getValue().trim() : "";
			setCategory(strDbl.isEmpty() ? null : strDbl);
			strDbl = xmlElement.getAttribute("unit") != null ? xmlElement.getAttribute("unit").getValue().trim() : "";
			setUnit(strDbl.isEmpty() ? null : strDbl);
			strDbl = xmlElement.getAttribute("description") != null ? xmlElement.getAttribute("description").getValue().trim() : "";
			setDescription(strDbl.isEmpty() ? null : strDbl);
			
			for (Element el : xmlElement.getChildren()) {
				if (el.getName().equals("correlation")) {
					String n = el.getAttributeValue("origname");
					strDbl = el.getAttributeValue("value");
					Double d = strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl);
					correlations.put(n, d);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addCorrelation(String otherOrigname, Double value) {
		correlations.put(otherOrigname, value);
	}
	public Double getCorrelation(String otherOrigname) {
		if (correlations.containsKey(otherOrigname)) return correlations.get(otherOrigname);
		else return null;
	}
	public HashMap<String, Double> getAllCorrelations() {
		return correlations;
	}
	public String getName() {return name;}
	public String getOrigName() {return origName;}
	public Double getValue() {return value;}
	public Double getError() {return error;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	public Double getP() {return P;}
	public Double gett() {return t;}
	public String getCategory() {return category;}
	public String getUnit() {return unit;}
	public String getDescription() {return description;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	private void setOrigName(String origName) {this.origName = (origName == null) ? "" : origName;}
	public void setValue(Double value) {this.value = (value == null) ? null : value;}
	public void setError(Double error) {this.error = (error == null) ? null : error;}
	public void setMin(Double min) {this.min = (min == null) ? null : min;}
	public void setMax(Double max) {this.max = (max == null) ? null : max;}
	public void setP(Double P) {this.P = (P == null) ? null : P;}
	public void sett(Double t) {this.t = (t == null) ? null : t;}	
	public void setCategory(String category) {this.category = category;}
	public void setUnit(String unit) {this.unit = unit;}
	public void setDescription(String description) {this.description = description;}

	public Double getMinGuess() {
		return minGuess;
	}
	public void setMinGuess(Double minGuess) {
		this.minGuess = minGuess;
	}
	public Double getMaxGuess() {
		return maxGuess;
	}
	public void setMaxGuess(Double maxGuess) {
		this.maxGuess = maxGuess;
	}
	
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_PARAM);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("origname", origName);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("error", "" + (error == null || Double.isNaN(error) ? "" : error));
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		modelElement.setAttribute("P", "" + (P == null || Double.isNaN(P) ? "" : P));
		modelElement.setAttribute("t", "" + (t == null || Double.isNaN(t) ? "" : t));
		modelElement.setAttribute("minGuess", "" + (minGuess == null || Double.isNaN(minGuess) ? "" : minGuess));
		modelElement.setAttribute("maxGuess", "" + (maxGuess == null || Double.isNaN(maxGuess) ? "" : maxGuess));
		modelElement.setAttribute("category", category == null ? "" : category);
		modelElement.setAttribute("unit", unit == null ? "" : unit);
		modelElement.setAttribute("description", description == null ? "" : description);

		for (String origname : correlations.keySet()) {
			Element element = new Element("correlation");
			element.setAttribute("origname", origname);
			Double d = correlations.get(origname);
			element.setAttribute("value", "" + (d == null || Double.isNaN(d) ? "" : d));
			modelElement.addContent(element);			
		}

		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Origname");
        list.add("Value");
        list.add("Error");
        list.add("Min");
        list.add("Max");
        list.add("P");
        list.add("t");
        list.add("MinGuess");
        list.add("MaxGuess");
        list.add("Category");
        list.add("Unit");
        list.add("Description");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("origname")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("value")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("error")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("min")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("max")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("p")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("t")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("minGuess")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("maxGuess")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("category")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("unit")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("description")) {
			return StringCell.TYPE;
		}
		return null;
	}
}
