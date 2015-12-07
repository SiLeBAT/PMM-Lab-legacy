package de.bund.bfr.knime.pmm.extendedtable.items;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlHelper;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class Model1AgentXml implements AgentXmlI, PmmXmlElementConvertable {

	public static final String ELEMENT_AGENT = "model1Agent";

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_DETAIL = "detail";
	private static final String ATT_DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String detail;
	private String dbuuid;

	public Model1AgentXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null);
	}

	public Model1AgentXml(Integer id, String name, String detail) {
		this(id, name, detail, null);
	}

	public Model1AgentXml(AgentXmlI agent) {
		this(agent.getId(), agent.getName(), agent.getDetail(), agent.getDbuuid());
	}

	public Model1AgentXml(Integer id, String name, String detail, String dbuuid) {
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.dbuuid = dbuuid;
	}

	public Model1AgentXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME), XmlHelper.getString(el, ATT_DETAIL),
				XmlHelper.getString(el, ATT_DBUUID));
	}

	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_AGENT);

		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_DETAIL, XmlHelper.getNonNull(detail));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return ret;
	}

	public String getElementName() {
		return "modelLiterature";
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}