package com.company.functions.meta;

import com.company.functions.Function;

public class Composition implements Function {

    private Function first, second;

    public Composition(Function firstFunction, Function secondFunction) {
        this.first = firstFunction;
        this.second = secondFunction;
    }

    @Override
    public double getLeftDomainBorder() {
        return first.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return second.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return first.getFunctionValue(second.getFunctionValue(x));
    }

}