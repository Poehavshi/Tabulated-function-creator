package com.company.gui;

import com.company.functions.InappropriateFunctionPointException;
import com.company.functions.TabulatedFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TabulatedFunctionTableModel extends DefaultTableModel {
    private TabulatedFunction function;
    private Component parent;

    public TabulatedFunctionTableModel(TabulatedFunction function,Component parent){
        this.function = function;
        this.parent = parent;
    }

    @Override
    public int getRowCount(){
        if (function == null) return 0;
        return function.getPointsCount();
    }

    @Override
    public int getColumnCount(){
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "x";
            case 1:
                return "y";
            default:
                return "Not exist";
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return Double.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return row <= this.getRowCount() && column <= this.getColumnCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return function.getPointX(row);
            case 1:
                return function.getPointY(row);
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch (column) {
            case 0:
                try {
                    function.setPointX(row, (Double) aValue);
                } catch (InappropriateFunctionPointException e) {
                    JOptionPane.showMessageDialog(parent, "Incorrect X, try again!");
                }
                break;
            case 1:
                function.setPointY(row, (Double) aValue);
        }
    }
}
