package com.company.gui;

import com.company.functions.*;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MainFrame extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu tabulateMenu;
    private JMenuItem newItem;
    private JMenuItem tabulateFunctionItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem exitItem;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JTable tableTabulatedFunction;
    private JLabel labelX;
    private JLabel labelY;
    private JScrollPane scrollPaneTable;
    private JPanel rootPanel;

    private FunctionParameters parametersDialog;
    private TabulatedFunctionDocument functionDocument;
    private JFileChooser fileChooser;
    private FunctionClassLoader functionClassLoader;

    public MainFrame() {
        setContentPane(rootPanel);
        parametersDialog = new FunctionParameters();
        parametersDialog.pack();
        setTitle("Tabulated functions");
        functionDocument = new TabulatedFunctionDocument();
        fileChooser = new JFileChooser();
        functionDocument.newFunction(0, 10, 11);

        TabulatedFunctionTableModel tableModel = new TabulatedFunctionTableModel(functionDocument, this);
        tableTabulatedFunction.setModel(tableModel);

        functionClassLoader = new FunctionClassLoader();

        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNew();
            }
        });
        saveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveAs();
            }
        });
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOpen();
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        tabulateFunctionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTabulate();
            }
        });
    }

    private void onNew() {
        int state = parametersDialog.showDialog();
        if (state == parametersDialog.OK) {
            double leftX = parametersDialog.getLeftDomainBorder();
            double rightX = parametersDialog.getRightDomainBorder();
            int pointCount = parametersDialog.getPointsCount();
            functionDocument.newFunction(leftX, rightX, pointCount);
            tableTabulatedFunction.revalidate();
            tableTabulatedFunction.repaint();
        }
    }

    private void onSaveAs() {
        fileChooser.setDialogTitle("Save file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                functionDocument.saveFunctionAs(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "IOException");
            }
        }
        tableTabulatedFunction.revalidate();
        tableTabulatedFunction.repaint();
    }

    private void onSave() {
        if (functionDocument.isFileNameAssigned()) {
            try {
                functionDocument.saveFunction();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "IOException");
            }
        } else {
            onSaveAs();
        }
    }

    private void onOpen() {
        fileChooser.setDialogTitle("Open file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                functionDocument.loadFunction(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "IOException");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid file, pointsCount must be greater or equal 2");
            }
        }
        tableTabulatedFunction.revalidate();
        tableTabulatedFunction.repaint();
    }

    private void onExit() {
        if (functionDocument.isModified()) {
            if (JOptionPane.showConfirmDialog(this, "Function is modified. Are you sure want to exit?") == 0) {
                dispose();
                System.exit(0);
            } else {
                return;
            }
        } else {
            dispose();
            System.exit(0);
        }
    }

    private void onDelete() {
        try {
            int index = tableTabulatedFunction.getSelectedRow();
            functionDocument.deletePoint(index);
            tableTabulatedFunction.revalidate();
            tableTabulatedFunction.repaint();
        } catch (FunctionPointIndexOutOfBoundException e) {
            JOptionPane.showMessageDialog(this, "Incorrect row to delete");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Table must contain 2 or more points");
        }
    }

    private void onAdd() {
        try {
            double x = Double.parseDouble(textFieldX.getText());
            double y = Double.parseDouble(textFieldY.getText());
            functionDocument.addPoint(new FunctionPoint(x, y));
            tableTabulatedFunction.revalidate();
            tableTabulatedFunction.repaint();

        } catch (NumberFormatException | InappropriateFunctionPointException exception){
            JOptionPane.showMessageDialog(this, "Incorrect x or y");
        }
    }

    private void onTabulate() {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {

            return;
        }
        String fileName = fileChooser.getSelectedFile().getAbsolutePath();

        try {
            Class functionClass = null;
            Function function = null;
            functionClass = functionClassLoader.loadClassFromFile(fileName);
            function = (Function) functionClass.getDeclaredConstructor().newInstance();
            if (parametersDialog.showDialog() == parametersDialog.OK){
                double leftX = parametersDialog.getLeftDomainBorder();
                double rightX = parametersDialog.getRightDomainBorder();
                int pointsCount = parametersDialog.getPointsCount();
                this.functionDocument.tabulateFunction(function, leftX, rightX, pointsCount);
                tableTabulatedFunction.revalidate();
                tableTabulatedFunction.repaint();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "IOException");
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(this, "Illegal access exception");
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(this, "InstantiationException");
        } catch (ClassFormatError e) {
            JOptionPane.showMessageDialog(this, "Incorrect file: file must be .class");
        } catch (NoSuchMethodException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (InvocationTargetException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

}
