package com.company;

import com.company.functions.*;
import com.company.functions.basic.Cos;
import com.company.functions.basic.Log;
import com.company.functions.basic.Sin;
import com.company.threads.*;


public class Main {
    public static void main(String[] args) {
        TabulatedFunction func = new LinkedListTabulatedFunction(0,10,11);
        for (FunctionPoint p : func) {
            System.out.println(p);
        }

        Function f = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        TabulatedFunction function;
        function = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(function.getClass());
        System.out.println(function);
        function = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(function.getClass());
        System.out.println(function);
        function = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(function.getClass());
        System.out.println(function);
        function = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(function.getClass());
        System.out.println(function);

    }

    public static void nonThread() {
        Task task = new Task(100);
        for (int i = 0;i<task.getNumberOfTasks();++i){
            task.setFunction(new Log(1 + (Math.random() * 9)));
            task.setLeftX(Math.random()*100);
            task.setRightX(100+Math.random()*100);
            task.setStep(Math.random());

            System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep() );

            double result = Functions.integrate(task.getFunction(),task.getLeftX(),task.getRightX(),task.getStep());

            System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStep() + " " + result);
        }
    }

    public static void simpleThreads(){
        Task task = new Task(100);
        Thread generator = new Thread(new SimpleGenerator(task));
        //generator.setPriority(Thread.MAX_PRIORITY);
        generator.start();

        Thread integrator = new Thread(new SimpleIntegrator(task));
        //integrator.setPriority(Thread.MIN_PRIORITY);
        integrator.start();
    }

    public static void complicatedThreads() {
        Task task = new Task(100);
        Semaphore semaphore = new Semaphore();
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        //integrator.setPriority(8);

        generator.start();
        integrator.start();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
        generator.interrupt();
        integrator.interrupt();


    }
}
