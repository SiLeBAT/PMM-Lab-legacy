package org.jlibsedml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jlibsedml.UniformRange.UniformType;
import org.jmathml.ASTCi;
import org.jmathml.ASTNode;
import org.jmathml.ASTOtherwise;
import org.jmathml.ASTPlus;

public class RepeatedTask extends AbstractTask {
    
    private boolean resetModel = false;
    private String range = new String();
    
    private Map<String, Range> ranges = new HashMap<String, Range> ();
    private List<SetValue> changes = new ArrayList<SetValue> ();
    private Map<String, SubTask> subTasks = new HashMap<String, SubTask> ();
    
    public boolean getResetModel() {
        return resetModel;
    }
    public void setResetModel(boolean resetModel) {
        this.resetModel = resetModel;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public Range getRange(String rangeId) {
        return ranges.get(rangeId);
    }
    public void addRange(Range range) {
        if(!ranges.containsKey(range.getId())) {
            ranges.put(range.getId(), range);
        } else {
            System.err.println("range already in ranges list");
            System.err.println("   ...range " + range.getId() + " not added to list");
        }
    }
    
    public Map<String, Range> getRanges() {
        return ranges;
    }
    public void addChange(SetValue change) {
        changes.add(change);
    }
    public List<SetValue> getChanges() {
        return changes;
    }
    public void addSubtask(SubTask subTask) {
        if(subTask == null || subTask.getTaskId() == null || subTask.getTaskId().equals("")) {
            System.err.println("subtask cant't be null, key can't be null, key can't be empty string");
            System.err.println("   ...subtask " + subTask.getTaskId() + " not added to list");
            return;     // subtask cant't be null, key can't be null, key can't be ""
        }
        if(this.getId().equals(subTask.getTaskId())) {
            System.err.println("'this' repeated task cannot be a subtask for itself");
            System.err.println("   ...subtask " + subTask.getTaskId() + " not added to list");
            return;     // "this" repeated task cannot be a subtask for itself
        }
        if(!subTasks.containsKey(subTask.getTaskId())) {        // no duplicates
            subTasks.put(subTask.getTaskId(), subTask);
            subTask.removeOwnerFromDependentTasksList(this);    // this repeated task cannot depend on itself
        } else {
            System.err.println("subtask already in subtasks list");
            System.err.println("   ...subtask " + subTask.getTaskId() + " not added to list");
            return;
        }
    }
    public Map<String, SubTask> getSubTasks() {
        return subTasks;
    }

    public RepeatedTask(String id, String name, boolean resetModel, String range) {
        super(id, name);
        this.resetModel = resetModel;
        this.range = range;
    }
    
    @Override
    public String toString() {
        return "Repeated Task ["
        + "name=" + getName()
        + ", getId()=" + getId()
        + ", resetModel=" + resetModel
        + ", ranges.size()=" + ranges.size()
        + ", changes.size()=" + changes.size()
        + ", subTasks.size()=" + subTasks.size()
        + "]";
    }
    
     @Override
        public String getElementName() {
            return SEDMLTags.REPEATED_TASK_TAG;
        }

     @Override
     public boolean accept(SEDMLVisitor visitor) {
         return visitor.visit(this);
     }
    
     
     
     
    // =============== quick functionality test
    public static void main(String[] args) throws JDOMException, IOException, XMLException {
        
        SEDMLDocument sedmlDocument = new SEDMLDocument();
        SedML sedmlModel = sedmlDocument.getSedMLModel();
        
        Model model = new Model("model1", "Model 1 from VCell", "urn:sedml:language:sbml", "urn:miriam:biomodels.db:BIOMD0000000021");
        sedmlModel.addModel(model);
        
        Algorithm a1 = new Algorithm("KISAO:0000030");
        Simulation s1 = new UniformTimeCourse("simul1", "simul1", 0, 5, 7, 3, a1);
        AlgorithmParameter ap1 = new AlgorithmParameter("KISAO:0000211", "23");
        AlgorithmParameter ap2 = new AlgorithmParameter("KISAO:0000212", "25");
        Algorithm a2 = new Algorithm("KISAO:0000032");
        a2.addAlgorithmParameter(ap1);
        a2.addAlgorithmParameter(ap2);
        Simulation s2 = new OneStep("simul2", "simul2", a2, 5);
        Algorithm a3 = new Algorithm("KISAO:0000034");
        Simulation s3 = new SteadyState("simul3", "", a3);
        Simulation s4 = new SteadyState("simul4", null, a3);
        sedmlModel.addSimulation(s1);
        sedmlModel.addSimulation(s2);
        sedmlModel.addSimulation(s3);
        sedmlModel.addSimulation(s4);
       
        List<Simulation> simulations = sedmlModel.getSimulations();
        for(Simulation simulation : simulations) {
            System.out.println(simulation.toString());
        }

// <task id="task1" modelReference="model1" simulationReference="simulation1" />
        Task task = new Task("task1", "Classic task", "model1", "simul1");
        sedmlModel.addTask(task);
        
/*
    <listOfRanges>
        <vectorRange id="current">
            <value> 1 </value>
            <value> 4 </value>
            <value> 10 </value>
        </vectorRange>
    </listOfRanges>
*/
       ArrayList<Double> values = new ArrayList<Double> (Arrays.asList(2.0, 3.0, 10.0));
       VectorRange r1 = new VectorRange("current", values);
       UniformRange r2 = new UniformRange("other", 3.006, 11.111, 27, UniformType.Linear);
       Variable var0 = new Variable("val0", "current range value", "model1", "#current");
       Variable var1 = new Variable("val1", "current range value", "model1", "#current");
       ASTNode math1 = new ASTPlus();
       ASTNode var0_Node = new ASTCi(var0.getId());
       ASTNode var1_Node = new ASTCi(var1.getId());
       math1.addChildNode(var0_Node);
       math1.addChildNode(var1_Node);

       Parameter p = new Parameter("Kf", "Kf", 0.5);
       Map<String, AbstractIdentifiableElement> variables1 = new HashMap<String, AbstractIdentifiableElement> ();
       Map<String, AbstractIdentifiableElement> parameters1 = new HashMap<String, AbstractIdentifiableElement> ();
       variables1.put(var0.getId(), var0);
       variables1.put(var1.getId(), var1);
       parameters1.put(p.getId(), p);
       FunctionalRange r3 = new FunctionalRange("frange3", "index", variables1, parameters1, math1);
       UniformRange r4 = new UniformRange("other", 5.432, 7.89, 11, UniformType.Log);

/*
<repeatedTask id="task3" modelReference="model1" resetModel="false" range="current">
 */
       RepeatedTask t = new RepeatedTask("task3", "Repeated Task", false, "current");
       t.addRange(r1);
       t.addRange(r2);
       t.addRange(r3);
       t.addRange(r4);  // should fail, range id "other" already in use
/*
<listOfChanges>
    <setValue target="/sbml/model/listOfParameters/parameter[@id='w']" modelReference="model1" >
        <math>
            <ci> current </ci>
        </math>
    </setValue>
</listOfChanges>
*/ 
       Parameter p2 = new Parameter("Kr", "Kr", 0.4);
       Variable var2 = new Variable("val2", "current range value", "model1", "#current");
       Variable var3 = new Variable("val3", "current range value", "model1", "#current");
       ASTNode math2 = new ASTPlus();
       ASTNode var2_Node = new ASTCi(var2.getId());
       ASTNode var3_Node = new ASTCi(var3.getId());
       math2.addChildNode(var2_Node);
       math2.addChildNode(var3_Node);
       SetValue setValue = new SetValue(new XPathTarget("/sbml/model/listOfParameters/parameter[@id='sum']"), r3.getId(), "model1");
       setValue.addParameter(p2);
       setValue.addVariable(var2);
       setValue.addVariable(var3);
       setValue.setMath(math2);
       t.addChange(setValue);
/*
    <listOfSubTasks>
        <subTask task="task1" />
    </listOfSubTasks>
    
    <listOfSubTasks>
        <subTask order="1" task="task2" />
    </listOfSubTasks>
    
</repeatedTask>
*/
       t.addSubtask(new SubTask("1", "task0"));         // order + id attributes
       t.addSubtask(new SubTask(task.getId()));         // just id
       t.addSubtask(new SubTask("task2"));
       // the next 4 subtasks should not be added to subtask list (should even raise RuntimeException??)
       t.addSubtask(new SubTask("task2"));      // duplicate, will not be added again
       t.addSubtask(new SubTask(""));           // not a valid name
       t.addSubtask(new SubTask(null));         // not valid
       t.addSubtask(new SubTask("third", "task9")); // invalid order, must be string representation of an integer
       t.addSubtask(new SubTask(t.getId()));    // "this" task cannot be in the list of subtasks
       // and now a subtask with dependent tasks
       SubTask st = new SubTask("task7");
       st.addDependentTask(new SubTask("task2"));
       st.addDependentTask(new SubTask("task4"));
       // the next 2 dependent tasks should not show (should even raise RuntimeException??)
       st.addDependentTask(new SubTask(t.getId()));         // repeated task should not depend on itself
       st.addDependentTask(new SubTask(st.getTaskId()));    // subtask should not depend on itself
       t.addSubtask(st);
       
       sedmlModel.addTask(t);
       List<AbstractTask> taskss = sedmlModel.getTasks();
       for(AbstractTask tsk : taskss) {
           System.out.println(tsk.toString());
       }
        
       sedmlDocument.writeDocument(new File("c:\\TEMP\\repeatedTaskDemo.xml"));
       
       System.out.println("");
       System.out.println(" ------------------------------------------------------------------");
       System.out.println("");

       String fileName = "c:\\TEMP\\repeatedTaskDemo.xml";
       sedmlModel = SEDMLReader.readFile(new File(fileName));
       Namespace namespace = sedmlModel.getNamespace();
       List<Model> mmm = sedmlModel.getBaseModels();
       for(Model mm : mmm) {
           System.out.println(mm.toString());
       }
       List<Simulation> sss = sedmlModel.getSimulations();
       for(Simulation ss : sss) {
           System.out.println(ss.toString());
       }
       List<AbstractTask> ttt = sedmlModel.getTasks();
       for(AbstractTask tt : ttt) {
           System.out.println(tt.toString());
       }
       List<DataGenerator> ddd = sedmlModel.getDataGenerators();
       for(DataGenerator dd : ddd) {
           System.out.println(dd.toString());
       }
       List<Output> ooo = sedmlModel.getOutputs();
       for(Output oo : ooo) {
           System.out.println(oo.toString());
       }
       System.out.println("");
    }
}
