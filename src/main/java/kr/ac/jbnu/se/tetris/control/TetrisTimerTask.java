package kr.ac.jbnu.se.tetris.control;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public abstract class TetrisTimerTask extends TimerTask {
    private boolean isPaused = false;
    private long delay = 0;
    @Override
    public void run() {
        // 특정 조건에 따라 작업 수행
        if (!isPaused) {
            // 작업이 일시 중지 상태가 아닌 경우에만 실행
            try {
                if(delay != 0){
                    pauseTask();
                    synchronized (this){
                        wait(delay);
                    }
                    resumeTask();
                }
                doLogic();
            } catch (ExecutionException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //태스크가 행해야할 메소드 -> 재정의하여 사용
    public abstract void doLogic() throws ExecutionException, InterruptedException, IOException;
    // 태스크를 일시 중지하는 메소드
    public synchronized void pauseTask() {
        isPaused = true;
    }
    // 태스크를 재개하는 메소드
    public synchronized void resumeTask() {
        isPaused = false;
        delay = 0;
    }
    public synchronized void pauseTask(long delay) {
        isPaused = true;
        this.delay = delay;
    }
    // 태스크를 취소하는 메소드
    public synchronized void cancelTask() {
        cancel(); // TimerTask의 내장 메소드로 태스크를 취소
    }
    public boolean isPaused() { return isPaused; }
}
