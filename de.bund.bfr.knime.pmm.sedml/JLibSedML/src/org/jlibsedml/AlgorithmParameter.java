package org.jlibsedml;

public class AlgorithmParameter {

    private String kisaoID;
    private String value;
    
    public AlgorithmParameter(String kisaoID, String value) {
        super();
        Assert.checkNoNullArgs(kisaoID);
        Assert.stringsNotEmpty(kisaoID);
        this.kisaoID = kisaoID;
        this.setValue(value);
    }
    
    public void setKisaoID(String kisaoID) {
        this.kisaoID = kisaoID;
    }
    public String getKisaoID() {
        return kisaoID;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public String toString() {
        String s = "AlgorithmParameter [";
        s += "kisaoID=" + kisaoID; 
        s += " value=" + value; 
        s += "]";
        return s;
    }

}
