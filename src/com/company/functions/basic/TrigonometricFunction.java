package com.company.functions.basic;

import com.company.functions.Function;

public abstract class TrigonometricFunction implements Function {
    @Override
    public double getLeftDomainBorder(){
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }
}
