package de.bund.bfr.knime.pmm.common;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class CatalogModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_CATALOGMODEL = "catalogmodelxml";

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_FORMULA = "formula";
	private static final String ATT_MODEL_CLASS = "modelClass";
	private static final String ATT_DBUUID = "dbuuid";

	private Integer id;
	private String name = null;
	private String formula = null;
	private Integer modelClass = null;
	private String dbuuid = null;

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass) {
		this(id, name, formula, modelClass, null);
	}

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass, String dbuuid) {
		setId(id == null ? MathUtilities.getRandomNegativeInt() : id);
		setName(name);
		setFormula(formula);
		setModelClass(modelClass);
		setDbuuid(dbuuid);
	}

	public CatalogModelXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getString(el, ATT_FORMULA), XmlHelper.getInt(el,
						ATT_MODEL_CLASS), XmlHelper.getString(el, ATT_DBUUID));
	}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_CATALOGMODEL);

		modelElement.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		modelElement.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		modelElement.setAttribute(ATT_FORMULA, XmlHelper.getNonNull(formula));
		modelElement.setAttribute(ATT_MODEL_CLASS,
				XmlHelper.getNonNull(modelClass));
		modelElement.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

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

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Integer getModelClass() {
		return modelClass;
	}

	public void setModelClass(Integer modelClass) {
		this.modelClass = modelClass;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
