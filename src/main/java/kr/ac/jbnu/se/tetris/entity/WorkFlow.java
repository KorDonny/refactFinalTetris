package kr.ac.jbnu.se.tetris.entity;

public class WorkFlow {
    private int hashCode;
    private String className;
    private Object target;
    public WorkFlow(Object target){
        this.target = target;
        hashCode = target.hashCode();
        className = target.getClass().getSimpleName();
    }
    public int getHashCode() { return hashCode; }
    public Object getTarget(){ return target; }
    public void tracingLog(){ System.out.println("Class Name : "+className+" / hashCode : "+getHashCode()); }
}
