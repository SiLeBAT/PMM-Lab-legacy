package org.jlibsedml;

public class OneStep extends Simulation {

    
    private double step;
    
    public OneStep(String id, String name, Algorithm algorithm, double step) {
        super(id, name, algorithm);
        this.setStep(step);

    }

    @Override
    public String toString() {
        return "OneStep [" + getAlgorithm()
            + ", name=" + getName()
            + ", getId()=" + getId() 
            + ", getStep()=" + getStep() 
            + "]";
    }

    @Override
    public String getSimulationKind() {
        return SEDMLTags.SIMUL_OS_KIND;
    }

    @Override
    public String getElementName() {
        return SEDMLTags.SIM_OS;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getStep() {
        return step;
    }

}
