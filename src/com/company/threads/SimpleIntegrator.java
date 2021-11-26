package com.company.threads;

import com.company.functions.Functions;

public class SimpleIntegrator implements Runnable{
    private Task task;

    public SimpleIntegrator(Task task){
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getNumberOfTasks(); ++i){
            double result;
            if (task.getFunction() == null){
                continue;
            }
            synchronized (task){
                result = Functions.integrate(task.getFunction(), task.getLeftX(),task.getRightX(),task.getStep());
                System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep() + " integrate " + result);
            }
        }
    }
}
