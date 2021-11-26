package com.company.functions.meta;

import com.company.functions.Function;

public class Sum implements Function {

    private Function first, second;

    public Sum(Function firstFunction, Function secondFunction){
        this.first = firstFunction;
        this.second = secondFunction;
    }

    @Override
    public double getLeftDomainBorder() {
        return Math.max(first.getLeftDomainBorder(),second.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(first.getRightDomainBorder(),second.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return (first.getFunctionValue(x)+second.getFunctionValue(x));
    }
}
