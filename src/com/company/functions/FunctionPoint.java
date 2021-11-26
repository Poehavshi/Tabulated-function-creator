package com.company.functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x, y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public String toString() {
        return new String("("+this.x+"; "+this.y+")");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FunctionPoint){
            // Преобразуем в FunctionPoint
            FunctionPoint point = (FunctionPoint) o;
            double epsilonX = Math.ulp(point.x);
            double epsilonY = Math.ulp(point.y);
            return  (point.x -  epsilonX <= this.x && this.x <= point.x + epsilonX) &&
                    (point.y -  epsilonY <= this.y && this.y <= point.y + epsilonY);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    protected Object clone() {
        return new FunctionPoint(this.x, this.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
