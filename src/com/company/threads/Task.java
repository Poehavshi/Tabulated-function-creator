package com.company.threads;

import com.company.functions.Function;

public class Task {
    private Function function;
    private double leftX, rightX;
    private double step;
    private int numberOfTasks;

    public Task(int numberOfTasks){
        if (numberOfTasks <= 0){
            throw new IllegalArgumentException();
        }
        this.numberOfTasks = numberOfTasks;
    }

    public int getNumberOfTasks(){
        return numberOfTasks;
    }

    public double getLeftX(){
        return leftX;
    }

    public double getRightX(){
        return rightX;
    }

    public double getStep(){
        return step;
    }

    public Function getFunction(){
        return function;
    }

    public void setFunction(Function function){
        this.function = function;
    }

    public void setLeftX(double leftX){
        this.leftX = leftX;
    }

    public void setRightX(double rightX){
        if (rightX < leftX){
            throw new IllegalArgumentException();
        }
        this.rightX = rightX;
    }

    public void setStep(double step){
        this.step = step;
    }



}
