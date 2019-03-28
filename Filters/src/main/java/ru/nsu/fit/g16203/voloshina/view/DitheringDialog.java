package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import java.awt.event.*;

public class DitheringDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTextField NrTextField;
    private JTextField NgTextField;
    private JTextField NbTextField;
    private JLabel NrLabel;
    private JButton buttonOK;
    private JLabel NgLabel;
    private JLabel NbLabel;

    private int max = 8;

    public DitheringDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setOnOkListener(ActionListener listener) {
        buttonOK.addActionListener(listener);
    }

    public void setOnCancelListener(ActionListener listener) {
        buttonCancel.addActionListener(listener);
    }

    private Integer getInteger(JTextField valueTextField, int valueMax) {
        try {
            int value = Integer.parseInt(valueTextField.getText());
            if (value <= valueMax) {
                return value;
            } else {
                String messageText = "Введенное значение больше максимально возможного ):";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return valueMax;
            }
        } catch (NumberFormatException ex) {
            String messageText = "Необходимо ввести число!";
            JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Integer getNr() {
        return getInteger(NrTextField, max);
    }

    public void setNr(Integer Nr) {
        NrTextField.setText(Nr.toString());
    }

    public Integer getNg() {
        return getInteger(NgTextField, max);
    }

    public void setNg(Integer Ng) {
        NgTextField.setText(Ng.toString());
    }

    public Integer getNb() {
        return getInteger(NbTextField, max);
    }

    public void setNb(Integer Nb) {
        NbTextField.setText(Nb.toString());
    }
}
