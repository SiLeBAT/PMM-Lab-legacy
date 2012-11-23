/*
 * An XML document type.
 * Localname: AgentIncredient
 * Namespace: http://www.bfr.bund.de/PCML-1_0
 * Java type: de.bund.bfr.pcml10.AgentIncredientDocument
 *
 * Automatically generated - do not modify.
 */
package de.bund.bfr.pcml10.impl;
/**
 * A document containing one AgentIncredient(@http://www.bfr.bund.de/PCML-1_0) element.
 *
 * This is a complex type.
 */
public class AgentIncredientDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentIncredientDocument
{
    private static final long serialVersionUID = 1L;
    
    public AgentIncredientDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AGENTINCREDIENT$0 = 
        new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "AgentIncredient");
    
    
    /**
     * Gets the "AgentIncredient" element
     */
    public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient getAgentIncredient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
            target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().find_element_user(AGENTINCREDIENT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AgentIncredient" element
     */
    public void setAgentIncredient(de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient agentIncredient)
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
            target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().find_element_user(AGENTINCREDIENT$0, 0);
            if (target == null)
            {
                target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().add_element_user(AGENTINCREDIENT$0);
            }
            target.set(agentIncredient);
        }
    }
    
    /**
     * Appends and returns a new empty "AgentIncredient" element
     */
    public de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient addNewAgentIncredient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient target = null;
            target = (de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient)get_store().add_element_user(AGENTINCREDIENT$0);
            return target;
        }
    }
    /**
     * An XML AgentIncredient(@http://www.bfr.bund.de/PCML-1_0).
     *
     * This is a complex type.
     */
    public static class AgentIncredientImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient
    {
        private static final long serialVersionUID = 1L;
        
        public AgentIncredientImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName AGENT$0 = 
            new javax.xml.namespace.QName("http://www.bfr.bund.de/PCML-1_0", "Agent");
        private static final javax.xml.namespace.QName QUANTITY$2 = 
            new javax.xml.namespace.QName("", "quantity");
        
        
        /**
         * Gets the "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId getAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(AGENT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "Agent" element
         */
        public void setAgent(de.bund.bfr.pcml10.NameAndDatabaseId agent)
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().find_element_user(AGENT$0, 0);
                if (target == null)
                {
                    target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(AGENT$0);
                }
                target.set(agent);
            }
        }
        
        /**
         * Appends and returns a new empty "Agent" element
         */
        public de.bund.bfr.pcml10.NameAndDatabaseId addNewAgent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                de.bund.bfr.pcml10.NameAndDatabaseId target = null;
                target = (de.bund.bfr.pcml10.NameAndDatabaseId)get_store().add_element_user(AGENT$0);
                return target;
            }
        }
        
        /**
         * Gets the "quantity" attribute
         */
        public double getQuantity()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUANTITY$2);
                if (target == null)
                {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }
        
        /**
         * Gets (as xml) the "quantity" attribute
         */
        public org.apache.xmlbeans.XmlDouble xgetQuantity()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(QUANTITY$2);
                return target;
            }
        }
        
        /**
         * True if has "quantity" attribute
         */
        public boolean isSetQuantity()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(QUANTITY$2) != null;
            }
        }
        
        /**
         * Sets the "quantity" attribute
         */
        public void setQuantity(double quantity)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(QUANTITY$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(QUANTITY$2);
                }
                target.setDoubleValue(quantity);
            }
        }
        
        /**
         * Sets (as xml) the "quantity" attribute
         */
        public void xsetQuantity(org.apache.xmlbeans.XmlDouble quantity)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(QUANTITY$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(QUANTITY$2);
                }
                target.set(quantity);
            }
        }
        
        /**
         * Unsets the "quantity" attribute
         */
        public void unsetQuantity()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(QUANTITY$2);
            }
        }
    }
}
