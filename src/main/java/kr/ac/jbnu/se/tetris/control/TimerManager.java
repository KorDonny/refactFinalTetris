package kr.ac.jbnu.se.tetris.control;

import java.util.*;

public class TimerManager {
    private Timer timer;
    private HashMap<String, TimerTask> timerTasks;
    public TimerManager() {
        timer = new Timer();
        timerTasks = new HashMap<>();
    }
    /** 딜레이 만큼 지연후 고정된 주기만큼 task수행 */
    public void scheduleTask(TimerTask task, long delay, long period) {
        timer.scheduleAtFixedRate(task, delay, period);
    }
    /** 키값 미존재시 null 반환 */
    public TimerTask getTimerTask(String taskID) {
        return timerTasks.get(taskID);
    }
    /** 이미 taskID가 존재한다면 해당 task에 대한 새로운 값으로 재할당 */
    public void setTimerTask(String taskID, TimerTask task, long delay, long period){
        timerTasks.put(taskID,task);
        scheduleTask(task,delay,period);
    }
    /** 이미 taskID가 존재한다면 task 작업을 거부함 */
    public boolean addTimerTask(String taskID, TimerTask task, long delay, long period){
        if(getTimerTask(taskID)==null){
            timerTasks.put(taskID,task);
            scheduleTask(task,delay,period);
            return true;
        }
        return false;
    }
}

