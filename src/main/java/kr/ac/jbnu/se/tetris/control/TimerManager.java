package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.entity.WorkFlow;

import java.util.*;

public class TimerManager {
    private static TimerManager instance = null;
    private static Timer timer = new Timer("Game Timer");
    static HashMap<Integer, TetrisTimerTask> timerMap = new HashMap<>();
    /*
     * Timer 중앙화, Task에 period만 전달하여 수행. 단, delay는 빠짐.
     * 문제점 : 동기화 문제(기술 이해도 부족), obj의 주소값을 전달하여 인덱싱 하려 했으나
     * 같은 종류의 클래스가 들어가면 같이 취소되버림.
     * -------------최종 코드 제출시 삭제 예정(다이어그램 도시 과정때 삭제후 작업 요망)------------------
     * */
    TimerManager(){
        if(timer==null)timer = new Timer("Game Timer");
        if(timerMap==null) timerMap = new HashMap<>();
    }
    /** Task 삽입, 그러나 꼭 Task의 해시코드를 삽입하거나, 한 클래스에 고유 Task를 원할 시, 클래스의 hashCode()를 넣어주세요.*/
    public static void addTask(WorkFlow works, TetrisTimerTask task, long period){
        if(timerMap.containsKey(works.getHashCode()) && getTask(works)!=null){
            works.tracingLog();
            System.out.println("Already exist, task will be overwritten");
            timerMap.put(works.getHashCode(),task);
            timer.scheduleAtFixedRate(task,0,period);
        }
        else {
            works.tracingLog();
            System.out.println("Successfully inserted");
            timerMap.put(works.getHashCode(),task);
            timer.scheduleAtFixedRate(task,0,period);
        }
    }
    /** Task Getter */
    public static TetrisTimerTask getTask(WorkFlow works){
        if(timerMap.containsKey(works.getHashCode())){
            if(timerMap.get(works.getHashCode())!=null){
                System.out.println("Task Getter success : "+works.getHashCode());
                return timerMap.get(works.getHashCode());
            }
            works.tracingLog();
            System.out.println("Task key exist, but Task is null");
            return null;
        }
        works.tracingLog();
        System.out.println("No such Task exist");
        return null;
    }
    /** 해당 스레드 정지 */
    public static void pauseTask(WorkFlow works){
        if(works!=null && getTask(works) != null) getTask(works).pauseTask();
    }
    /** 해당 스레드 정지, delay 시간동안 */
    public static void pauseTask(WorkFlow works,long delay){
        if(works!=null && getTask(works) != null) getTask(works).pauseTask(delay);
    }
    /** 해당 스레드 정지 */
    public static void resumeTask(WorkFlow works){
        if(works!=null && getTask(works) != null) getTask(works).resumeTask();
    }
    /** 특정 타이머 태스크만 삭제 */
    public static void removeTask(WorkFlow works){
        TetrisTimerTask task = getTask(works);
        if(task != null){
            task.cancelTask();
            timerMap.remove(works.getHashCode());
        }
    }
    /** 모든 스레드 정지 */
    public static void pauseAllTasks(){
        for (TetrisTimerTask task : timerMap.values()) {
            task.pauseTask();
        }
        System.out.println("All tasks will wait until notify or notifyAll");
    }
    /** 모든 스레드 정지, delay 시간동안 */
    public static void pauseAllTasks(long delay){
        for (TetrisTimerTask task : timerMap.values()) {
            task.pauseTask(delay);
        }
        System.out.println("All tasks will wait until notify or notifyAll");
    }
    /** 모든 스레드 재시작  */
    public static void resumeAllTasks(){
        for (TetrisTimerTask task : timerMap.values()) {
            task.resumeTask();
        }
    }
    public static TimerManager getInstance(){
        if(instance==null){
            synchronized (TimerManager.class){
                instance = new TimerManager();
            }
        }
        return instance;
    }
    public static synchronized void exitProg(){
        timerMap = null;
        timer = null;
        instance = null;
    }
}

