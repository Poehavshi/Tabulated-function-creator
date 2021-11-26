package com.company.functions;


import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
    private class FunctionNode{
        private FunctionPoint point = null;
        private FunctionNode prev=null,next = null;
    }

    private int length = 0;
    private FunctionNode head = new FunctionNode(), tail, current;
    private int currentIndex = 0;

    {
        // Голова ссылается сама на себя
        head.next = head;
        head.prev = head;
        tail = head;
        current = head;
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX,rightX,values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX,rightX,pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] array) {
            return new LinkedListTabulatedFunction(array);
        }
    }

    public Iterator<FunctionPoint> iterator(){
        return new Iterator<FunctionPoint>() {
            private FunctionNode node = head.next;

            @Override
            public boolean hasNext() {
                return node != head;
            }

            @Override
            public FunctionPoint next() {
                if (node == head){
                    throw new NoSuchElementException();
                }
                FunctionNode result = node;
                node = node.next;
                return (FunctionPoint) result.point.clone();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private FunctionNode getNodeByIndex(int index){
        /**
         * Этот метод осуществляет получение элемента связного списка по индексу.
         * В основе лежит поиск наименьшего расстояния
         * (от чего ближе идти к элементу: от хвоста, головы или текущего элемента?)
         * С последующим выбором направления движения и движения к необходимому элементу
         * @exception FunctionPointIndexOutOfBoundException при неверном index.
         * @param index индекс элемента в списке
         * @return FunctionNode возвращает ссылку на элемент
         */
        if (index < 0 || index > length){
            throw new FunctionPointIndexOutOfBoundException();
        }

        int fromHead = index;
        int fromTail = length - 1 - index;
        int fromCurrent = Math.abs(currentIndex - index);
        int fromNear = Math.min(Math.min(fromHead, fromCurrent), fromTail);
        if (fromNear == fromTail){
            current = tail;
            currentIndex = length - 1;
        }
        else if (fromNear == fromHead){
            current = head.next;
            currentIndex = 0;
        }
        if (index < currentIndex){
            while (currentIndex != index){
                current = current.prev;
                currentIndex--;
            }
        }
        else {
            while (currentIndex != index){
                current = current.next;
                currentIndex++;
            }
        }
        return current;
    }

    private FunctionNode addNodeToTail(){
        /**
         * Добавляет элемент к хвосту (в заднюю часть списка)
         * @return FunctionNode возвращает ссылку на добавленный элемент
         */
        tail.next = new FunctionNode();
        tail.next.prev = tail;
        tail.next.next = head;
        tail = tail.next;
        length++;
        head.prev = tail;
        return tail;
    }

    private FunctionNode addNodeByIndex(int index){
        /**
         * Добавляет новый элемент в указанную позицию списка
         * Для вставки используется создание нового элемента
         * С последующим изменением ссылок соседних к нему элементов
         * @exception FunctionPointIndexOutOfBoundException при неверном index.
         * @param index позицию, куда нужно добавить элемент
         * @return FunctionNode возвращает ссылку на объект нового элемента.
         */
        if (index < 0 || index > length){
            throw new FunctionPointIndexOutOfBoundException();
        }
        else if (index == length){
            return addNodeToTail();
        }
        // Переходим к элементу с номером index
        // (меняем current на элемент с номером index)
        getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode();
        newNode.next = current;
        newNode.prev = current.prev;
        current.prev.next = newNode;
        current.prev = newNode;

        current = newNode;
        length++;

        return current;
    }

    private FunctionNode deleteNodeByIndex(int index){
        /**
         * Удаляет элемент списка по номеру
         * @exception FunctionPointIndexOutOfBoundException при неверном index.
         * @param index индекс элемента в списке
         * @return FunctionNode возвращает ссылку на объект удаленного элемента
         */
        if (index < 0 || index > length){
            throw new FunctionPointIndexOutOfBoundException();
        }

        getNodeByIndex(index);
        current.prev.next = current.next;
        current.next.prev = current.prev;
        FunctionNode node = current;
        current = current.prev;
        length--;
        currentIndex--;

        return node;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (pointsCount < 2) {
            throw new IllegalArgumentException("values.length must be greater or equal 2");
        }

        FunctionNode node = addNodeToTail();
        node.point = new FunctionPoint(leftX, 0);

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 1; i < pointsCount; i++) {
            node = addNodeToTail();
            node.point = new FunctionPoint(node.prev.point.getX()+step, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (values.length < 2) {
            throw new IllegalArgumentException("values.length must be greater or equal 2");
        }

        FunctionNode node = addNodeToTail();
        node.point = new FunctionPoint(leftX, values[0]);

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 1; i < values.length; i++) {
            node = addNodeToTail();
            node.point = new FunctionPoint(node.prev.point.getX()+step, values[i]);
        }

    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {

        for (int i = 0; i < points.length-1; i++){
            if (points[i].getX() > points[i+1].getX()){
                throw new IllegalArgumentException("x must be increasing");
            }
        }

        if (points.length < 2) {
            throw new IllegalArgumentException("values.length must be greater or equal 2");
        }

        FunctionNode node = addNodeToTail();
        node.point = points[0];

        for (int i = 1; i < points.length; i++) {
            node = addNodeToTail();
            node.point = points[i];
        }

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0;i<length; ++i){
            result.append(this.getPoint(i).toString());
            if (i != length-1) {
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
        else if (o instanceof LinkedListTabulatedFunction){
            LinkedListTabulatedFunction tabulatedFunction = (LinkedListTabulatedFunction) o;
            if (tabulatedFunction.length != this.length){
                return false;
            }
            for (int i = 0;i<this.length; ++i){
                if (!(this.getPoint(i).equals(tabulatedFunction.getPoint(i)))) {
                    return false;
                }
            }
            return true;
        }
        else if (o instanceof TabulatedFunction){
            TabulatedFunction tabulatedFunction = (TabulatedFunction) o;
            if (tabulatedFunction.getPointsCount() != this.length){
                return false;
            }
            for (int i = 0;i<this.length; ++i){
                if (!(this.getPoint(i).equals(tabulatedFunction.getPoint(i)))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 10;
        for (int i = 0;i<length;++i){
            result=result ^ this.getPoint(i).hashCode();
        }
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint points[] = new FunctionPoint[length];
        for (int i = 0; i<length; ++i){
            points[i] = (FunctionPoint) this.getPoint(i).clone();
        }
        return new LinkedListTabulatedFunction(points);
    }

    @Override
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    @Override
    public double getRightDomainBorder() { return tail.point.getX(); }

    @Override
    public double getFunctionValue(double x) {
        if (length == 0) {
            throw new IllegalStateException();
        }
        if (x < head.next.point.getX() || x > tail.point.getX()) {
            throw new FunctionPointIndexOutOfBoundException();
        }

        current = head.next;
        currentIndex = 0;
        while (current.point.getX() < x) {
            current = current.next;
            currentIndex++;
        }

        if (current.point.getX() == x) {
            return current.point.getY();
        }

        FunctionPoint point1 = current.prev.point;
        FunctionPoint point2 = current.point;
        double b = point1.getY();
        double k = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        return k * (x - point1.getX()) + b;

    }

    @Override
    public int getPointsCount() {
        return length;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (0 <= index && index < length) {
            return new FunctionPoint(getNodeByIndex(index).point);
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{

        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundException();
        }

        double left = Double.MIN_VALUE;
        double right = Double.MAX_VALUE;

        FunctionNode node = getNodeByIndex(index);

        if (node.prev != null) {
            left = node.prev.point.getX();
        }
        if (node.next != null) {
            right = node.next.point.getX();
        }

        if (left > point.getX() || right < point.getX()) {
            throw new InappropriateFunctionPointException();
        }

        node.point = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (0 <= index && index < length) {
            return getNodeByIndex(index).point.getX();
        } else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundException();
        }

        double left = Double.MIN_VALUE;
        double right = Double.MAX_VALUE;

        FunctionNode node = getNodeByIndex(index);

        if (node.prev != null) {
            left = node.prev.point.getX();
        }
        if (node.next != null) {
            right = node.next.point.getX();
        }

        if (left > x || right < x) {
            throw new InappropriateFunctionPointException();
        }

        node.point = new FunctionPoint(x, node.point.getY());
    }

    @Override
    public double getPointY(int index) {
        if (0<=index && index<length) {
            return getNodeByIndex(index).point.getY();
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void setPointY(int index, double y) {
        if (0 <= index && index < length) {
            getNodeByIndex(index).point = new FunctionPoint(getNodeByIndex(index).point.getX(), y);
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void deletePoint(int index) {
        if (length < 3) {
            throw new IllegalStateException("Quantity of points must be greater then 2");
        }
        else if (0 <= index && index < length) {
            deleteNodeByIndex(index);
        }
        else {
            throw new FunctionPointIndexOutOfBoundException();
        }
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        if (length != 0 && (point.getX() < head.next.point.getX() || point.getX() > tail.point.getX())) {
            throw new InappropriateFunctionPointException();
        }

        if (length == 0){
            FunctionNode node = addNodeToTail();
            node.point = point;
            return;
        }

        current = head.next;
        currentIndex = 0;
        while (current.point.getX() < point.getX()){
            current = current.next;
            currentIndex++;
        }

        if (current.point.getX() == point.getX()){
            throw new InappropriateFunctionPointException();
        }

        addNodeByIndex(currentIndex).point = point;

    }

}
