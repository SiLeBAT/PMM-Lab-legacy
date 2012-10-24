package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class ParamXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAM = "param";

	private String name = null;
	private Double value = null;
	private Double error = null;
	private Double min = null;
	private Double max = null;
	private Double P = null;
	private Double t = null;
	
	public ParamXml(String name, Double value, Double error, Double min, Double max, Double P, Double t) {
		setName(name);
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
			String strDbl = xmlElement.getAttribute("value").getValue();
			setValue(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("error").getValue();
			setError(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("min").getValue();
			setMin(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("max").getValue();
			setMax(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("P-value").getValue();
			setP(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("t-value").getValue();
			sett(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getName() {return name;}
	public Double getValue() {return value;}
	public Double getError() {return error;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	public Double getP() {return P;}
	public Double gett() {return t;}
	
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setValue(Double value) {this.value = (value == null) ? Double.NaN : value;}
	public void setError(Double error) {this.error = (error == null) ? Double.NaN : error;}
	public void setMin(Double min) {this.min = (min == null) ? Double.NaN : min;}
	public void setMax(Double max) {this.max = (max == null) ? Double.NaN : max;}
	public void setP(Double P) {this.P = (P == null) ? Double.NaN : P;}
	public void sett(Double t) {this.t = (t == null) ? Double.NaN : t;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_PARAM);
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("error", "" + (error == null || Double.isNaN(error) ? "" : error));
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		modelElement.setAttribute("P-value", "" + (P == null || Double.isNaN(P) ? "" : P));
		modelElement.setAttribute("t-value", "" + (t == null || Double.isNaN(t) ? "" : t));
		return modelElement;
	}
}
