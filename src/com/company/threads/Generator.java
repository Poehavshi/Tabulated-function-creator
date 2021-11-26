package com.company.threads;

import com.company.functions.basic.Log;

public class Generator extends Thread{
    private Task task;
    private Semaphore semaphore;
    private boolean isRun = false;

    public Generator(Task task, Semaphore semaphore){
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        for (int i = 0; i < task.getNumberOfTasks(); i++) {
            Log log = new Log(1 + (Math.random() * 9));
            try {
                semaphore.beginWrite();
                task.setFunction(log);
                task.setLeftX(Math.random() * 100);
                task.setRightX(100 + Math.random() * 100);
                task.setStep(Math.random());
                semaphore.endWrite();

                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());
            } catch (InterruptedException e){
                System.err.println("Interrupted exception");
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isRun = false;
    }
}
