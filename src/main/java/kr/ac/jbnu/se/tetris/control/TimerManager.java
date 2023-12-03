package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.entity.WorkFlow;

import java.io.IOException;
import java.util.*;

public class TimerManager {
    private static TimerManager instance = null;
    static Timer timer;
    static HashMap<Integer, TimerTask> timerMap;
    static {
        timer = new Timer("Game Timer");
        timerMap = new HashMap<>();
    }
    /*
     * Timer 중앙화, Task에 period만 전달하여 수행. 단, delay는 빠짐.
     * 문제점 : 동기화 문제(기술 이해도 부족), obj의 주소값을 전달하여 인덱싱 하려 했으나
     * 같은 종류의 클래스가 들어가면 같이 취소되버림.
     * -------------최종 코드 제출시 삭제 예정(다이어그램 도시 과정때 삭제후 작업 요망)------------------
     * */
    public TimerManager(){

    }
    /** Task 삽입, 그러나 꼭 Task의 해시코드를 삽입하거나, 한 클래스에 고유 Task를 원할 시, 클래스의 hashCode()를 넣어주세요.*/
    public static void addTask(WorkFlow works, TimerTask task, long period){
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
    public static TimerTask getTask(WorkFlow works){
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
    /** 특정 타이머 태스크만 삭제 */
    public static void removeTask(WorkFlow works){
        TimerTask task = getTask(works);
        if(task != null){
            task.cancel();
            timerMap.remove(works.getHashCode());
        }
    }
    /** wait상태의 스레드를 정지 */
    public synchronized static void stopTask(WorkFlow works) throws InterruptedException {
        TimerTask task = getTask(works);
        if (task != null) {
            task.wait();
            works.tracingLog();
            System.out.println("Task will wait until notify or notifyAll");
            task.notifyAll();
        }
        else System.out.println("No such task exist : " + works.getHashCode());
    }
    /** 스레드를 일정 시간동안 멈추기 */
    public synchronized static void stopTask(WorkFlow works, long duration) throws InterruptedException {
        TimerTask task = getTask(works);
        if (task != null) {
            task.wait(duration);
            works.tracingLog();
            System.out.println("Task will be delayed in "+duration);
            task.notifyAll();
        }
        else {
            works.tracingLog();
            System.out.println("No such task exist");
        }
    }
    /** 특정 스레드만 wait 상태에서 재시작 */
    public synchronized static void resumeTask(WorkFlow works){
        TimerTask task = getTask(works);
        if(task != null){
            task.notifyAll();
            works.tracingLog();
            System.out.println("Task Resumed");
        }
    }
    /** 모든 스레드 재시작  */
    public synchronized static void resumeAllTask(WorkFlow works){
        if(works!=null){
            final Object obj = new Object();
            synchronized (obj){
                obj.notifyAll();
                works.tracingLog();
                System.out.println("All Tasks Resumed");
            }
        }
    }
    public static Timer getTimer(){ return timer; }
    public static TimerManager getInstance() throws IOException {
        if(instance==null){
            synchronized (TimerManager.class){
                instance = new TimerManager();
            }
        }
        return instance;
    }
}

