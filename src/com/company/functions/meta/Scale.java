package com.company.functions.meta;

import com.company.functions.Function;

public class Scale implements Function {
    private final double coefX, coefY;
    private Function function;

    public Scale(Function functionToScale, double x, double y) {
        this.function = functionToScale;
        this.coefX = x;
        this.coefY = y;
    }

    @Override
    public double getLeftDomainBorder() {
        if (coefX >= 0) {
            return coefX * function.getLeftDomainBorder();
        } else {
            return function.getLeftDomainBorder() / coefX;
        }
    }

    @Override
    public double getRightDomainBorder() {
        if (coefX >= 0) {
            return coefX * function.getRightDomainBorder();
        } else {
            return function.getRightDomainBorder() / coefX;
        }
    }

    @Override
    public double getFunctionValue(double x) {
        if (coefY >= 0) {
            return coefY * function.getFunctionValue(x/coefX);
        } else {
            return function.getFunctionValue(x/coefX) / coefY;
        }
    }

}
