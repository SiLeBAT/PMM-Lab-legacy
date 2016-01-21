package org.jlibsedml;

public class SId {

    private final String string;
    
    public SId(String string) {
        if(!SId.isValidSId(string)) {
            throw new IllegalArgumentException("Invalid SId: " + string);
        }
        this.string = string;
    }
    
    public String getString() {
        return string;
    }
    public static String unwrap(SId sid) {
        if(sid == null) {
            return null;
        }
        return sid.getString();
    }
    public int length() {
        return string.length();
    }
    /*
     *      letter ::= 'a'..'z','A'..'Z'
     *      digit  ::= '0'..'9'
     *      idChar ::= letter | digit | '_'
     *      SId    ::= ( letter | '_' ) idChar*
     */
    public static boolean isValidSId(String string) {
        if(string == null) {
            return false;
        }
        if(string.equals("")) {
            return false;
        }
        if(!string.substring(0,1).matches("[a-zA-Z_]")) {
            return false;
        }
        if(string.length() > 1) {
            if(!string.substring(1).matches("[\\da-zA-Z_]*")) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "SId ["
        + "getString()=" + getString()
        + "]";
    }
    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        if(other instanceof SId) {
            return getString().equals(((SId) other).getString());
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return getString().hashCode();
    }
    
    public static void main(String[] args) {
        
        try {
            SId sid = new SId("");          // invalid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId(null);        // invalid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("1abcaaaAA"); // invalid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("abca/aaAA"); // invalid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("-test123");  // invalid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("_");         // valid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("_abZx_3xC"); // valid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            SId sid = new SId("abZx_3xC");  // valid
            System.out.println("Valid SId: " + sid.getString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
