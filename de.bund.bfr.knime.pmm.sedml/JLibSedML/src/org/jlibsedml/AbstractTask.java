package org.jlibsedml;

public abstract class AbstractTask extends AbstractIdentifiableElement {

    public AbstractTask(String id, String name) {
        super(id, name);
    }
    public String getModelReference() {
        return "";
    }
    public String getSimulationReference() {
        return "";
    }
}
