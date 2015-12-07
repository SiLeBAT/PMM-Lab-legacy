package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

public class MdInfoXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MDINFO = "mdinfoxml";

	public static final String ATT_ID = "ID";
	public static final String ATT_NAME = "Name";
	public static final String ATT_COMMENT = "Comment";
	public static final String ATT_QUALITYSCORE = "QualityScore";
	public static final String ATT_CHECKED = "Checked";

	private Integer id;
	private String name;
	private String comment;
	private Integer qualityScore;
	private Boolean checked;

	public MdInfoXml(Integer id, String name, String comment,
			Integer qualityScore, Boolean checked) {
		this.id = id;
		this.name = name;
		this.comment = comment;
		this.qualityScore = qualityScore;
		this.checked = checked;
	}

	public MdInfoXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getString(el, ATT_COMMENT), XmlHelper.getInt(el,
						ATT_QUALITYSCORE), XmlHelper
						.getBoolean(el, ATT_CHECKED));
	}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_MDINFO);

		modelElement.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		modelElement.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		modelElement.setAttribute(ATT_COMMENT, XmlHelper.getNonNull(comment));
		modelElement.setAttribute(ATT_QUALITYSCORE,
				XmlHelper.getNonNull(qualityScore));
		modelElement.setAttribute(ATT_CHECKED, XmlHelper.getNonNull(checked));

		return modelElement;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
}
