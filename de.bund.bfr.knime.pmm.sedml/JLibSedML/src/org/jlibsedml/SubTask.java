package org.jlibsedml;

import java.util.HashMap;
import java.util.Map;

public class SubTask {
/*
<listOfSubTasks>
    <subTask task="task1" >
        <listOfDependTasks>
            <dependendTask task="task2" />
        </listOfDependTasks>
    </subTask>
    <subTask task="task2" />
</listOfSubTasks>
*/

    private String order;
    private String taskId = new String();     // id of the Task which is a SubTask of the current RepeatedTask
    private Map<String, SubTask> dependentTasks = new HashMap<String, SubTask> ();
    private RepeatedTask ownerTask = null;
    
    private SubTask() {}
    public SubTask(String taskId) {
//        if(taskId == null) {
//            throw new IllegalArgumentException("SId cannot be null");
//        }
        this.order = null;
        this.taskId = taskId;
    }
    public SubTask(String order, String taskId) {
        this(taskId);
        if(order == null) {
            return;
        }
        try {
            Integer i = Integer.parseInt(order);    // we just check whether can be parsed to an int
            this.order = order;
        } catch (NumberFormatException e) {
            System.err.println("SubTask: order is not an Integer: " + order);
            this.order = null;
        }
    }
    
    @Override
    public String toString() {
        return "SubTask ["
        + "getTaskId()=" + getTaskId()
        + ", getOrder()=" + getOrder()
        + ", dependentTasks.size()=" + dependentTasks.size()
        + "]";
    }

    public String getTaskId() {
        return taskId;
    }
    public String getOrder() {
        return order;
    }
    public void addDependentTask(SubTask dependentTask) {
        if(dependentTask == null || dependentTask.getTaskId() == null || dependentTask.getTaskId().equals("")) {
            System.err.println("dependentTask cant't be null, key can't be null, key can't be empty string");
            System.err.println("   ...dependent task not added to list");
            return;     // dependentTask cant't be null, key can't be null, key can't be ""
        }
        if(this.getTaskId().equals(dependentTask.getTaskId())) {
            System.err.println("'this' subTask cannot be a dependentTask for itself");
            System.err.println("   ...dependent task " + dependentTask.getTaskId() + " not added to list");
            return;     // "this" subTask cannot be a dependentTask for itself
        }
        if(ownerTask != null && ownerTask.getId().equals(dependentTask.getTaskId())) {
            System.err.println("the RepeatedTask which owns this subTask cannot be a dependentTask for itself");
            System.err.println("   ...dependent task " + dependentTask.getTaskId() + " not added to list");
            return;     // the RepeatedTask which owns this subTask cannot be a dependentTask for itself
        }
        if(!dependentTasks.containsKey(dependentTask.getTaskId())) {  // no duplicates
            dependentTasks.put(dependentTask.getTaskId(), dependentTask);
        } else {
            System.err.println("dependent task already in dependent task list");
            System.err.println("   ...dependent task " + dependentTask.getTaskId() + " not added to list");
            return;
        }
    }
    public Map<String, SubTask> getDependentTasks() {
        return dependentTasks;
    }
    public void removeOwnerFromDependentTasksList(RepeatedTask repeatedTask) {
        this.ownerTask = repeatedTask;
        if(dependentTasks != null && !dependentTasks.isEmpty()) {
            for(SubTask dt : dependentTasks.values()) {
                if(ownerTask.getId().equals(dt.getTaskId())) {
                    dependentTasks.remove(dt.getTaskId());
                    System.err.println("the RepeatedTask which owns this subTask cannot be a dependentTask for itself");
                    System.err.println("   ...dependent task " + dt.getTaskId() + " removed from list");
                    return;
                }
            }
        }
    }
}
