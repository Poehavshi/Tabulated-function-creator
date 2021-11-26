package com.company.threads;

import com.company.functions.Functions;

public class Integrator extends Thread{
    private Task task;
    private Semaphore semaphore;
    private boolean isRun = false;

    public Integrator(Task task, Semaphore semaphore){
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        isRun = true;
        for (int i = 0; i < task.getNumberOfTasks(); ++i){
            double result;
            try {
                semaphore.beginRead();
                result = Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStep());

                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep() + " integrate " + result);
                semaphore.endRead();
            } catch (InterruptedException e){
                System.err.println("InterruptedException");
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isRun = false;
    }
}
