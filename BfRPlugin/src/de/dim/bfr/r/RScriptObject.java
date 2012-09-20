/**
 * 
 */
package de.dim.bfr.r;


/**
 * @author Administrator
 *
 */
public class RScriptObject 
{
	private static final String R_NULL = "NULL";
	private StringBuilder code;
	
	public RScriptObject() {
		code = new StringBuilder();
	}
	
	public void loadCSVIntoVariable(String varName, String csvPath)
	{
        String path = csvPath.replace('\\','/');
        code.append(varName + " <- read.csv(\"" + path + "\", header = TRUE, row.names = 1);\n"); 	
	}
	
	public void setWorkspaceDirectory(String workspace){
		code.append("setwd(\"" + workspace + "\");\n");
	}
	
	public void setVariable(String varName, String value){
		code.append(varName + " <- "+value+"\n");
	}

	public void setVariableNull(String varName) {
		setVariable(varName, R_NULL);	
	}

	public void append(String str) {
		code.append(str);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return code.toString();
	}
}
