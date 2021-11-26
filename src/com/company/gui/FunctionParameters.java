package com.company.gui;

import javax.swing.*;
import java.awt.event.*;

public class FunctionParameters extends JDialog {
    private JPanel functionParameters;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLeftBorder;
    private JLabel labelLeftDomain;
    private JLabel labelRightDomain;
    private JTextField textFieldRightBorder;
    private JSpinner spinnerPointsCount;
    private JLabel labelPointsCount;

    public final int OK=0;
    public final int CANCEL=1;
    int  buttonThatUserClicked;

    private double leftDomainBorder, rightDomainBorder;
    private int pointsCount;

    public FunctionParameters() {
        setContentPane(functionParameters);
        setModal(true);
        setTitle("Function parameters");
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        spinnerPointsCount.setModel(new SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2),null, Integer.valueOf(1)));



        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // set state variable to CANCEL, while user click cross
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                buttonThatUserClicked = CANCEL;
            }
        });

        // call onCancel() on ESCAPE
        functionParameters.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            rightDomainBorder = Double.parseDouble(textFieldRightBorder.getText());
            leftDomainBorder = Double.parseDouble(textFieldLeftBorder.getText());
            pointsCount = (int) spinnerPointsCount.getValue();
            this.setVisible(false);
            buttonThatUserClicked = OK;
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(FunctionParameters.this,"Domain borders should be numbers" );
        }
    }

    private void onCancel() {
        buttonThatUserClicked = CANCEL;
        this.setVisible(false);
    }

    public int showDialog() {
        this.setVisible(true);
        return buttonThatUserClicked;
    }

    public double getLeftDomainBorder(){
        return leftDomainBorder;
    }

    public double getRightDomainBorder(){
        return rightDomainBorder;
    }

    public int getPointsCount(){
        return pointsCount;
    }
}
