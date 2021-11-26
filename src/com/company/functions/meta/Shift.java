package com.company.functions.meta;

import com.company.functions.Function;

public class Shift implements Function {
    Function function;
    double shiftX, shiftY;

    public Shift(Function functionToShift, double x, double y){
        this.function = functionToShift;
        this.shiftX = x;
        this.shiftY = y;
    }

    @Override
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder()+shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return function.getRightDomainBorder()+shiftX;
    }

    @Override
    public double getFunctionValue(double x) {
        return function.getFunctionValue(x+shiftX)+shiftY;
    }
}
