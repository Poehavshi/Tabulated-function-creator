package com.company.threads;

import com.company.functions.basic.Log;

public class SimpleGenerator implements Runnable{
    private Task task;

    public SimpleGenerator(Task task){
        this.task = task;
    }

    @Override
    public void run(){
        for (int i = 0; i < task.getNumberOfTasks(); i++) {
            Log log = new Log(1 + (Math.random() * 9));
            synchronized (task) {
                task.setFunction(log);
                task.setLeftX(Math.random()*100);
                task.setRightX(100+Math.random()*100);
                task.setStep(Math.random());

                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep());
            }
        }
    }
}
