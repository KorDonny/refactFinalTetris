package kr.ac.jbnu.se.tetris.control;

import kr.ac.jbnu.se.tetris.entity.WorkFlow;

import java.util.TimerTask;

public abstract class TetrisTimerTask extends TimerTask {
    private boolean isWait = true;
    private final Object lock = new Object();
    private final WorkFlow works;
    public TetrisTimerTask(WorkFlow works){
        this.works = works;
    }
    public void run(){
        synchronized (lock) {
            while (isWait) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            doLogic();
        }
    }
    public abstract void doLogic();
    public void notifyThread() {
        synchronized (lock) {
            isWait = false;
            lock.notify();
        }
    }
}
