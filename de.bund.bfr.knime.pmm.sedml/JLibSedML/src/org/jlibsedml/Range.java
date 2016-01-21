package org.jlibsedml;

public abstract class Range extends AbstractIdentifiableElement{

    public Range(String id) {
        super(id, "");
    }
    
    private int numElements;
    public abstract int getNumElements();
    public abstract double getElementAt(int index);
}
