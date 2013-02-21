package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.knime.core.data.DataType;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class EstModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_ESTMODEL = "estmodelxml";

	private Integer id;
	private String name = null;
	private Double rms = null;
	private Double r2 = null;
	private Double aic = null;
	private Double bic = null;
	private Integer dof = null;
	private Integer qualityScore = null;
	private Boolean checked = null;
	
	public EstModelXml(Integer id, String name, Double rms, Double r2, Double aic, Double bic, Integer dof) {
		setID(id);
		setName(name);
		setRMS(rms);
		setR2(r2);
		setAIC(aic);
		setBIC(bic);
		setDOF(dof);
	}
	public EstModelXml(Element xmlElement) {
		try {
			String strInt = xmlElement.getAttribute("id").getValue();
			setID(strInt.trim().isEmpty() ? null : Integer.parseInt(strInt));
			setName(xmlElement.getAttribute("name").getValue());
			String strDbl = xmlElement.getAttribute("rms").getValue();
			setRMS(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("r2").getValue();
			setR2(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("aic").getValue();
			setAIC(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strDbl = xmlElement.getAttribute("bic").getValue();
			setBIC(strDbl.trim().isEmpty() ? null : Double.parseDouble(strDbl));
			strInt = xmlElement.getAttribute("dof").getValue();
			setDOF(strInt.trim().isEmpty() ? null : Integer.parseInt(strInt));
			if (xmlElement.getAttribute("qualityScore") != null) {
				strInt = xmlElement.getAttribute("qualityScore").getValue();
				setQualityScore(strInt.trim().isEmpty() ? null : Integer.parseInt(strInt));				
			}
			if (xmlElement.getAttribute("checked") != null) {
				String strBool = xmlElement.getAttribute("checked").getValue();
				setChecked(strBool.trim().isEmpty() ? null : Boolean.parseBoolean(strBool));				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Integer getID() {return id;}
	public String getName() {return name;}
	public Double getRMS() {return rms;}
	public Double getR2() {return r2;}
	public Double getAIC() {return aic;}
	public Double getBIC() {return bic;}
	public Integer getDOF() {return dof;}
	public Integer getQualityScore() {return qualityScore;}
	public Boolean getChecked() {return checked;}
	
	public void setID(Integer id) {this.id = (id == null) ? null : id;} // MathUtilities.getRandomNegativeInt()
	public void setName(String name) {this.name = (name == null) ? "" : name;}
	public void setRMS(Double rms) {this.rms = (rms == null) ? null : rms;}
	public void setR2(Double r2) {this.r2 = (r2 == null) ? null : r2;}
	public void setAIC(Double aic) {this.aic = (aic == null) ? null : aic;}
	public void setBIC(Double bic) {this.bic = (bic == null) ? null : bic;}
	public void setDOF(Integer dof) {this.dof = dof;}
	public void setQualityScore(Integer qualityScore) {this.qualityScore = qualityScore;}
	public void setChecked(Boolean checked) {this.checked = checked;}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_ESTMODEL);
		modelElement.setAttribute("id", (id == null ? "" : id.toString()));
		modelElement.setAttribute("name", name);
		modelElement.setAttribute("rms", "" + (rms == null || Double.isNaN(rms) ? "" : rms));
		modelElement.setAttribute("r2", "" + (r2 == null || Double.isNaN(r2) ? "" : r2));
		modelElement.setAttribute("aic", "" + (aic == null || Double.isNaN(aic) ? "" : aic));
		modelElement.setAttribute("bic", "" + (bic == null || Double.isNaN(bic) ? "" : bic));
		modelElement.setAttribute("dof", "" + (dof == null ? "" : dof));
		modelElement.setAttribute("qualityScore", "" + (qualityScore == null ? "" : qualityScore));
		modelElement.setAttribute("checked", "" + (checked == null ? "" : checked));
		return modelElement;
	}

	public static List<String> getElements() {
        List<String> list = new ArrayList<String>();
        list.add("ID");
        list.add("Name");
        list.add("RMS");
        list.add("R2");
        list.add("AIC");
        list.add("BIC");
        list.add("DOF");
        list.add("QualityScore");
        list.add("Checked");
        return list;
	}
	public static DataType getDataType(String element) {
		if (element.equalsIgnoreCase("id")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("name")) {
			return StringCell.TYPE;
		}
		else if (element.equalsIgnoreCase("rms")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("r2")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("aic")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("bic")) {
			return DoubleCell.TYPE;
		}
		else if (element.equalsIgnoreCase("dof")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("qualityscore")) {
			return IntCell.TYPE;
		}
		else if (element.equalsIgnoreCase("checked")) {
			return BooleanCell.TYPE;
		}
		return null;
	}
}
