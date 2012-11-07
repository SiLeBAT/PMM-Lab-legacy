package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;

public class ParamXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAM = "param";

	private String name = null;
	private String urName = null;
	private Double value = null;
	private Double error = null;
	private Double min = null;
	private Double max = null;
	private Double P = null;
	private Double t = null;
	private Double minGuess = null;
	private Double maxGuess = null;
	
	public ParamXml() {
		
	}
	public ParamXml(String name, Double value, Double error, Double min, Double max, Double P, Double t) {
		setName(name);
		setUrName(name);
		setValue(value);
		setError(error);
		setMin(min);
		setMax(max);
		setP(P);
		sett(t);		
	}
	public ParamXml(Element xmlElement) {
		try {
			setName(xmlElement.getAttribute("name").getValue());
			setUrName(xmlElement.getAttribute("urname").getValue());
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public String getUrName() {return urName;}
	public Double getValue() {return value;}
	public Double getError() {return error;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	public Double getP() {return P;}
	public Double gett() {return t;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setUrName(String urName) {this.urName = (urName == null) ? "" : urName;}
	public void setValue(Double value) {this.value = (value == null) ? null : value;}
	public void setError(Double error) {this.error = (error == null) ? null : error;}
	public void setMin(Double min) {this.min = (min == null) ? null : min;}
	public void setMax(Double max) {this.max = (max == null) ? null : max;}
	public void setP(Double P) {this.P = (P == null) ? null : P;}
	public void sett(Double t) {this.t = (t == null) ? null : t;}	

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
		modelElement.setAttribute("urname", urName);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("error", "" + (error == null || Double.isNaN(error) ? "" : error));
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		modelElement.setAttribute("P", "" + (P == null || Double.isNaN(P) ? "" : P));
		modelElement.setAttribute("t", "" + (t == null || Double.isNaN(t) ? "" : t));
		modelElement.setAttribute("minGuess", "" + (minGuess == null || Double.isNaN(minGuess) ? "" : minGuess));
		modelElement.setAttribute("maxGuess", "" + (maxGuess == null || Double.isNaN(maxGuess) ? "" : maxGuess));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("Name");
        list.add("Urname");
        list.add("Value");
        list.add("Error");
        list.add("Min");
        list.add("Max");
        list.add("P");
        list.add("t");
        list.add("MinGuess");
        list.add("MaxGuess");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("urname")) {
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
		return null;
	}
}
