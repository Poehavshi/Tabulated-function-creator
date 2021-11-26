package com.company.functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint table[];
    private int count;

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX,rightX,values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX,rightX,pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] array) {
            return new ArrayTabulatedFunction(array);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int current;
            @Override
            public boolean hasNext() {
                return current < count;
            }

            @Override
            public FunctionPoint next() {
                if (current >= count){
                    throw new NoSuchElementException();
                }
                return new FunctionPoint(table[current++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be greater or equal 2");
        }
        this.table = new FunctionPoint[2 * pointsCount];
        double currentX = leftX;
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            this.table[i] = new FunctionPoint(currentX, 0);
            currentX += interval;
        }
        this.count = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        int pointsCount = values.length;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be greater or equal 2");
        }
        this.table = new FunctionPoint[2 * pointsCount];
        double currentX = leftX;
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            this.table[i] = new FunctionPoint(currentX, values[i]);
            currentX += interval;
        }
        this.count = pointsCount;
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        int pointsCount = points.length;

        for (int i = 0; i < points.length-1; i++){
            if (points[i].getX() > points[i+1].getX()){
                throw new IllegalArgumentException("x must be increasing");
            }
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be greater or equal 2");
        }
        this.table = new FunctionPoint[2 * pointsCount];

        for (int i = 0; i < pointsCount; ++i) {
            this.table[i] = points[i];
        }
        this.count = pointsCount;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0;i<count; ++i){
            result.append(this.getPoint(i).toString());
            if (i != count-1) {
                result.append(", ");
            }
        }
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        else if (o instanceof  ArrayTabulatedFunction){
            ArrayTabulatedFunction tabulatedFunction = (ArrayTabulatedFunction) o;
            if (tabulatedFunction.count != this.count){
                return false;
            }
            for (int i = 0;i<this.count; ++i){
                if (!(this.table[i].equals(tabulatedFunction.table[i]))) return false;
            }
            return true;
        }
        else if (o instanceof TabulatedFunction){
            TabulatedFunction tabulatedFunction = (TabulatedFunction) o;
            if (tabulatedFunction.getPointsCount() != this.count){
                return false;
            }
            for (int i = 0;i<this.count; ++i){
                if (!(this.getPoint(i).equals(tabulatedFunction.getPoint(i)))) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 10;
        for (int i = 0;i<count;++i){
            result=result ^ table[i].hashCode();
        }
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint points[] = new FunctionPoint[count];
        for (int i = 0; i<count; ++i){
            points[i] = (FunctionPoint) table[i].clone();
        }
        return new ArrayTabulatedFunction(points);
    }

    @Override
    public double getLeftDomainBorder() {
        return table[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return table[this.count - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        // Если точка в границах области определения, то мы можем получить её значение
        if (this.getLeftDomainBorder() <= x && x <= this.getRightDomainBorder()) {
            // Перебираем все точки в поисках нужного места
            for (int i = 0; i < table.length - 1; ++i) {
                // Если точка между двумя другими, интерполируем
                if (table[i].getX() < x && x < table[i + 1].getX()) {
                    FunctionPoint point1 = table[i];
                    FunctionPoint point2 = table[i + 1];
                    double b = point1.getY();
                    double k = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
                    return k * (x - point1.getX()) + b;
                    // Иначе либо продолжаем, либо выводим найденное значение
                } else if (table[i].getX() == x) {
                    return table[i].getY();
                } else if (table[i + 1].getX() == x) {
                    return table[i + 1].getY();
                }
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount() {
        return count;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (0 <= index && index < this.count) {
            return new FunctionPoint(this.table[index]);
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (index == 0) {
            if (point.getX() < table[index + 1].getX()) {
                this.table[index] = point;
            } else {
                throw new InappropriateFunctionPointException();
            }
        } else if (index == table.length - 1) {
            if (table[index - 1].getX() < point.getX()) {
                this.table[index] = point;
            } else {
                throw new InappropriateFunctionPointException();
            }
        } else if (0 < index && index < table.length - 1) {
            if (table[index - 1].getX() < point.getX() && point.getX() < table[index + 1].getX()) {
                this.table[index] = point;
            }else {
                throw new InappropriateFunctionPointException();
            }
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public double getPointX(int index) {
        if (0 <= index && index < this.count) {
            return table[index].getX();
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index == 0) {
            if (x < table[index + 1].getX()) {
                this.table[index] = new FunctionPoint(x, table[index].getY());
            } else {
                throw new InappropriateFunctionPointException();
            }
        } else if (index == table.length - 1) {
            if (table[index - 1].getX() < x) {
                this.table[index] = new FunctionPoint(x, table[index].getY());
            } else {
                throw new InappropriateFunctionPointException();
            }
        } else if (0 < index && index < table.length - 1) {
            if (table[index - 1].getX() < x && x < table[index + 1].getX()) {
                this.table[index] = new FunctionPoint(x, table[index].getY());
            }
        }else if (this.getLeftDomainBorder() > x && x > this.getRightDomainBorder()) {
            throw new InappropriateFunctionPointException();
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public double getPointY(int index) {
        if (0<=index && index<this.count) {
            return table[index].getY();
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPointY(int index, double y) {
        if (0 <= index && index < this.count) {
            this.table[index] = new FunctionPoint(this.table[index].getX(), y);
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void deletePoint(int index) {
        if (this.getPointsCount() < 3) {
            throw new IllegalStateException("Quantity of points must be greater then 2");
        }
        else if (0 <= index && index < this.getPointsCount()) {
            // Для удаление сдвигаем элементы после удаляемого на одну позицию назад
            System.arraycopy(this.table, index + 1, this.table, index, this.getPointsCount() - index);
            this.count--;
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        // Если места в массиве мало, добавляем его
        if (this.count+2 >= table.length){
            FunctionPoint newTable[] = new FunctionPoint[table.length*2];
            System.arraycopy(this.table, 0, newTable, 0, this.count);
            this.table = newTable;
        }

        // Если точка вне области определения, то сразу добавляем её
        if (point.getX() < this.getLeftDomainBorder()) {
            System.arraycopy(this.table, 0, this.table, 1, this.getPointsCount());
            this.table[0] = point;
        }
        else if (point.getX() > this.getRightDomainBorder()) {
            this.table[this.getPointsCount()] = point;
        }
        // Иначе ищем для неё подходящее место
        else if (this.getLeftDomainBorder() < point.getX() && point.getX() < this.getRightDomainBorder()) {
            for (int i = 0; i < this.getPointsCount() - 1; ++i) {
                if (table[i].getX() < point.getX() && point.getX() < table[i + 1].getX()) {
                    System.arraycopy(this.table, i+1, this.table, i+2, this.getPointsCount());
                    this.table[i+1] = point;
                }
                else if (point.getX() + 0.000001 <table[i].getX() && table[i].getX() < point.getX() + 0.000001 ||
                        point.getX() + 0.000001 <table[i+1].getX() && table[i+1].getX() < point.getX() + 0.000001){
                    throw new InappropriateFunctionPointException();
                }
            }
        }
        this.count++;
    }
}
