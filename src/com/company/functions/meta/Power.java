package com.company.functions.meta;

import com.company.functions.Function;

public class Power implements Function{
    private Function function;
    private double powerIndex;

    public Power(Function base, double exponent){
        this.function = base;
        this.powerIndex = exponent;
    }

    @Override
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return function.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(function.getFunctionValue(x),powerIndex);
    }
}
