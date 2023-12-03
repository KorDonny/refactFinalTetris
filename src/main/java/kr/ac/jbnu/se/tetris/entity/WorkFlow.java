package kr.ac.jbnu.se.tetris.entity;

public class WorkFlow {
    private int hashCode;
    private String className;
    public WorkFlow(Object object){
        hashCode = object.hashCode();
        className = object.getClass().getSimpleName();
    }
    public int getHashCode() { return hashCode; }
    public void tracingLog(){ System.out.println("Class Name : "+className+" / hashCode : "+getHashCode()); }
}
