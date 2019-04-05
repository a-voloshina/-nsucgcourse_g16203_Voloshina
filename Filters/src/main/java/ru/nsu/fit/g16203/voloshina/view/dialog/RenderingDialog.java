package ru.nsu.fit.g16203.voloshina.view.dialog;

import javax.swing.*;
import java.awt.event.*;

public class RenderingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nXtextField;
    private JTextField nYtextField;
    private JTextField nZtextField;

    private int max;

    public RenderingDialog(int maxValue) {
        max = maxValue;
        setTitle("Config rendering");
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
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private Integer getInteger(JTextField valueTextField, int valueMax) {
        try {
            int value = Integer.parseInt(valueTextField.getText());
            if (value <= valueMax) {
                return value;
            } else {
                String messageText = "Введенное значение больше максимально возможного : " + valueMax;
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

    public Integer getNx() {
        return getInteger(nXtextField, max);
    }

    public void setNx(Integer Nr) {
        nXtextField.setText(Nr.toString());
    }

    public Integer getNy() {
        return getInteger(nYtextField, max);
    }

    public void setNy(Integer Ng) {
        nYtextField.setText(Ng.toString());
    }

    public Integer getNz() {
        return getInteger(nZtextField, max);
    }

    public void setNz(Integer Nb) {
        nZtextField.setText(Nb.toString());
    }

    public void setOnOkListener(ActionListener listener) {
        buttonOK.addActionListener(listener);
    }
}
