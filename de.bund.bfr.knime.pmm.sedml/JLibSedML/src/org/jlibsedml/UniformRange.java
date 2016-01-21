package org.jlibsedml;

public class UniformRange extends Range {

    public enum UniformType {
        Linear("Linear"), Log("Log");
        private String text;
        UniformType(String text) {
            this.text = text;
        }
        public String getText() {
            return this.text;
        }
        public static UniformType fromString(String text) {
            if (text != null) {
                for (UniformType b : UniformType.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
        return null;
        }
    }

    private double start;
    private double end;
    private int numberOfPoints;
    private UniformType type;
    
    public UniformRange(String id, double start, double end, int numberOfPoints) {
        super(id);
        this.start = start;
        this.end = end;
        this.numberOfPoints = numberOfPoints;
        this.type = UniformType.Linear;
    }
    public UniformRange(String id, double start, double end, int numberOfPoints, UniformType type) {
        super(id);
        this.start = start;
        this.end = end;
        this.numberOfPoints = numberOfPoints;
        if(type == null) {
            this.type = UniformType.Linear;
        } else if(type == UniformType.Linear) {
            this.type = type;
        } else if(type == UniformType.Log) {
            this.type = type;
        } else {
            throw new RuntimeException("Unsupported type: " + type);
        }
    }
    
    public double getStart() {
        return start;
    }
    public double getEnd() {
        return end;
    }
    public int getNumberOfPoints() {
        return numberOfPoints;
    }
    public UniformType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Uniform Range ["
        + "getId()=" + getId()
        + ", getStart()=" + getStart()
        + ", getEnd()=" + getEnd()
        + ", getNumberOfPoints()=" + getNumberOfPoints()
        + ", getType()=" + getType()
        + "]";
    }

    @Override
    public int getNumElements() {
        return numberOfPoints;
    }

    @Override
    public double getElementAt(int index) {
        if(type == UniformType.Linear) {
            return ((end - start)/(numberOfPoints-1))*((double)index);
        } else {
            return start * Math.pow(end/start, ((double)index)/(numberOfPoints - 1));
        }
    }

    @Override
    public String getElementName() {
        return SEDMLTags.UNIFORM_RANGE_TAG;
    }

    @Override
    public boolean accept(SEDMLVisitor visitor) {
        return visitor.visit(this);
    }

}
