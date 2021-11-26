package com.company.functions;

import javax.xml.crypto.NoSuchMechanismException;
import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {

    // Создаем приватный конструктор, чтобы было невозможно создать
    // объект Functions вне этого файла
    private TabulatedFunctions() {
    }

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f){
        factory = f;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] array) {
        return factory.createTabulatedFunction(array);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,double leftX, double rightX, double[] values) {
        Constructor constructors[] = functionClass.getConstructors();
        for (Constructor constructor:constructors){
            Class types[] = constructor.getParameterTypes();
            if (types.length == 3 && types[0].equals(Double.TYPE) && types[1].equals(Double.TYPE) && types[2].equals(values.getClass())){
                try {
                    return (TabulatedFunction) constructor.newInstance(leftX,rightX,values);
                } catch (Exception e){
                    throw new IllegalArgumentException(e);
                }
            }
        }
        throw new NoSuchMechanismException();
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,double leftX, double rightX, int pointsCount) {
        Constructor constructors[] = functionClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class types[] = constructor.getParameterTypes();
            if (types.length == 3 && types[0].equals(Double.TYPE) && types[1].equals(Double.TYPE) && types[2].equals(Integer.TYPE)) {
                try {
                    return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        throw new NoSuchMechanismException();
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,FunctionPoint[] array) {
        Constructor constructors[] = functionClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class types[] = constructor.getParameterTypes();
            if (types[0].equals(array.getClass())) {
                try {
                    return (TabulatedFunction) constructor.newInstance(new Object[]{array});
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        throw new NoSuchMechanismException();
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be greater or equal 2");
        }


        FunctionPoint[] table = new FunctionPoint[pointsCount];
        double currentX = leftX;
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            table[i] = new FunctionPoint(currentX, function.getFunctionValue(currentX));
            currentX += interval;
        }

        //return new LinkedListTabulatedFunction(table);
        return factory.createTabulatedFunction(table);
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX, int pointsCount) {

        if (leftX >= rightX) {
            throw new IllegalArgumentException("rightX must be greater leftX");
        } else if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be greater or equal 2");
        }


        FunctionPoint[] table = new FunctionPoint[pointsCount];
        double currentX = leftX;
        double interval = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            table[i] = new FunctionPoint(currentX, function.getFunctionValue(currentX));
            currentX += interval;
        }

        return createTabulatedFunction(functionClass, table);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{
        int pointsCount = function.getPointsCount();

        // DataOutputStream позволяет использовать выведенные данные для
        // обратного считывания данных
        DataOutputStream stream = new DataOutputStream(out);

        stream.writeInt(pointsCount);
        for (int i = 0; i<pointsCount;++i){
            stream.writeDouble(function.getPointX(i));
            stream.writeDouble(function.getPointY(i));
        }
        stream.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException{
        DataInputStream stream = new DataInputStream(in);
        int pointsCount = stream.readInt();

        FunctionPoint points[] = new FunctionPoint[pointsCount];

        for (int i = 0; i<pointsCount; i++) {
            points[i] = new FunctionPoint(stream.readDouble(), stream.readDouble());
        }

        return factory.createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        int pointsCount = function.getPointsCount();

        // DataOutputStream позволяет использовать выведенные данные для
        // обратного считывания данных
        PrintWriter stream = new PrintWriter(out);

        stream.print(pointsCount);
        stream.print(" ");
        for (int i = 0; i<pointsCount;++i){
            stream.print(function.getPointX(i));
            stream.print(" ");
            stream.print(function.getPointY(i));
            stream.print(" ");
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();

        int pointCount = (int) tokenizer.nval;

        FunctionPoint points[] = new FunctionPoint[pointCount];
        double x, y;
        for (int i = 0; i < pointCount; ++i) {
            tokenizer.nextToken();
            x = tokenizer.nval;
            tokenizer.nextToken();
            y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        return factory.createTabulatedFunction(points);
    }
}
