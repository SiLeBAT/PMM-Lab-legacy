package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class EstModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_ESTMODEL = "estmodelxml";

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_RMS = "rms";
	private static final String ATT_R2 = "r2";
	private static final String ATT_AIC = "aic";
	private static final String ATT_BIC = "bic";
	private static final String ATT_DOF = "dof";
	private static final String ATT_QUALITYSCORE = "qualityScore";
	private static final String ATT_CHECKED = "checked";
	private static final String ATT_DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private Double rms;
	private Double r2;
	private Double aic;
	private Double bic;
	private Integer dof;
	private Integer qualityScore;
	private Boolean checked;
	private String dbuuid;

	public EstModelXml(Integer id, String name, Double rms, Double r2,
			Double aic, Double bic, Integer dof) {
		this(id, name, rms, r2, aic, bic, dof, null, null, null);
	}

	public EstModelXml(Integer id, String name, Double rms, Double r2,
			Double aic, Double bic, Integer dof, Boolean checked,
			Integer qualityScore) {
		this(id, name, rms, r2, aic, bic, dof, checked, qualityScore, null);
	}

	public EstModelXml(Integer id, String name, Double rms, Double r2,
			Double aic, Double bic, Integer dof, Boolean checked,
			Integer qualityScore, String dbuuid) {
		this.id = id;
		this.name = name;
		this.rms = rms;
		this.r2 = r2;
		this.aic = aic;
		this.bic = bic;
		this.dof = dof;
		this.qualityScore = qualityScore;
		this.checked = checked;
		this.dbuuid = dbuuid;
	}

	public EstModelXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getDouble(el, ATT_RMS), XmlHelper.getDouble(el,
						ATT_R2), XmlHelper.getDouble(el, ATT_AIC), XmlHelper
						.getDouble(el, ATT_BIC), XmlHelper.getInt(el, ATT_DOF),
				XmlHelper.getBoolean(el, ATT_CHECKED), XmlHelper.getInt(el,
						ATT_QUALITYSCORE), XmlHelper.getString(el, ATT_DBUUID));
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_ESTMODEL);

		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_RMS, XmlHelper.getNonNull(rms));
		ret.setAttribute(ATT_R2, XmlHelper.getNonNull(r2));
		ret.setAttribute(ATT_AIC, XmlHelper.getNonNull(aic));
		ret.setAttribute(ATT_BIC, XmlHelper.getNonNull(bic));
		ret.setAttribute(ATT_DOF, XmlHelper.getNonNull(dof));
		ret.setAttribute(ATT_QUALITYSCORE, XmlHelper.getNonNull(qualityScore));
		ret.setAttribute(ATT_CHECKED, XmlHelper.getNonNull(checked));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return ret;
	}

	public Integer getID() {
		return id;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRMS() {
		return rms;
	}

	public void setRMS(Double rms) {
		this.rms = rms;
	}

	public Double getR2() {
		return r2;
	}

	public void setR2(Double r2) {
		this.r2 = r2;
	}

	public Double getAIC() {
		return aic;
	}

	public void setAIC(Double aic) {
		this.aic = aic;
	}

	public Double getBIC() {
		return bic;
	}

	public void setBIC(Double bic) {
		this.bic = bic;
	}

	public Integer getDOF() {
		return dof;
	}

	public void setDOF(Integer dof) {
		this.dof = dof;
	}

	public Integer getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
