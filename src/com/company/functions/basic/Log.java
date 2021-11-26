package com.company.functions.basic;

import com.company.functions.Function;

public class Log implements Function {
    private double base;

    public Log(double base) {
        if (base > 0) {
            this.base = base;
        } else {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public double getLeftDomainBorder(){
        return 0;
    }
    @Override
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public double getFunctionValue(double x){
        if (x > 0) {
            return Math.log(x) / Math.log(base);
        } else {
            return Double.NaN;
        }
    }
}
