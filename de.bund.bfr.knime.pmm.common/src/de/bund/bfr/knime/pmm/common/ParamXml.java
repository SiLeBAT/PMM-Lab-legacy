package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class ParamXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAM = "param";

	private Integer id;
	private String name = null;
	private Double value = null;
	private Double error = null;
	private Double min = null;
	private Double max = null;
	
	public ParamXml() {
		id = MathUtilities.getRandomNegativeInt();
	}
	public ParamXml(Integer id, String name, Double value, Double error, Double min, Double max) {
		setID(id);
		setName(name);
		setValue(value);
		setError(error);
		setMin(min);
		setMax(max);
	}
	public ParamXml(Element xmlElement) {
		try {
			setID(Integer.parseInt(xmlElement.getAttribute("id").getValue()));
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("value").getValue();
			setValue(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("error").getValue();
			setError(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("min").getValue();
			setMin(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("max").getValue();
			setMax(strDbl.trim().isEmpty() ? Double.NaN : Double.parseDouble(strDbl));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Integer getID() {return id;}
	public String getName() {return name;}
	public Double getValue() {return value;}
	public Double getError() {return error;}
	public Double getMin() {return min;}
	public Double getMax() {return max;}
	
	public void setID(Integer id) {this.id = (id == null) ? MathUtilities.getRandomNegativeInt() : id;}
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setValue(Double value) {this.value = (value == null) ? Double.NaN : value;}
	public void setError(Double error) {this.error = (error == null) ? Double.NaN : error;}
	public void setMin(Double min) {this.min = (min == null) ? Double.NaN : min;}
	public void setMax(Double max) {this.max = (max == null) ? Double.NaN : max;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_PARAM);
		modelElement.setAttribute("id", id.toString());
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("value", "" + (value == null || Double.isNaN(value) ? "" : value));
		modelElement.setAttribute("error", "" + (error == null || Double.isNaN(error) ? "" : error));
		modelElement.setAttribute("min", "" + (min == null || Double.isNaN(min) ? "" : min));
		modelElement.setAttribute("max", "" + (max == null || Double.isNaN(max) ? "" : max));
		return modelElement;
	}
}
