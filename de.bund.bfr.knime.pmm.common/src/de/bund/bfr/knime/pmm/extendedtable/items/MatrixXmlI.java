package de.bund.bfr.knime.pmm.extendedtable.items;

public interface MatrixXmlI {

	public String getElementName();
	
	public Integer getId();
	public void setId(Integer id);
	
	public String getName();
	public void setName(String name);
	
	public String getDetail();
	public void setDetail(String detail);
	
	public String getDbuuid();
	public void setDbuuid(String dbuuid);
}
