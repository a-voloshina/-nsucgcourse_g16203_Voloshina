package ru.nsu.fit.g16203.voloshina.view.dialog;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField kTextField;
    private JTextField mTextField;
    private JTextField aTextField;
    private JTextField bTextField;
    private JTextField cTextField;
    private JTextField dTextField;

    private int kMax = 100;
    private int mMax = 100;
    private double bMax = 100;
    private double dMax = 100;

    public SettingsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Set parameters");

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        SettingsDialog dialog = new SettingsDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private Integer getInteger(JTextField valueTextField, int valueMax) {
        try {
            int value = Integer.parseInt(valueTextField.getText());
            if (Math.abs(value) <= valueMax) {
                return value;
            } else {
                String messageText = "Введенное значение больше максимально возможного. " +
                        "Будет установлено максимально возможное значение";
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

    private Double getDoubleParameter(JTextField parameterTextField, double max) {
        try {
            double param = Double.parseDouble(parameterTextField.getText());
            if (Math.abs(param) <= max) {
                return new BigDecimal(param).setScale(2, RoundingMode.UP).doubleValue();
            } else {
                String messageText = "Введенное значение больше максимально возможного. " +
                        "Будет установлено максимально возможное значение";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return max;
            }
        } catch (NumberFormatException ex) {
            String messageText = "Необходимо ввести число!";
            JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void setOnOkListener(ActionListener listener) {
        buttonOK.addActionListener(listener);
    }

    public Integer getK() {
        return getInteger(kTextField, kMax);
    }

    public void setK(Integer k) {
        kTextField.setText(k.toString());
    }

    public Integer getM() {
        return getInteger(mTextField, mMax);
    }

    public void setM(Integer m) {
        mTextField.setText(m.toString());
    }

    public Double getA() {
        return getDoubleParameter(aTextField, bMax);
    }

    public void setA(Double a) {
        aTextField.setText(a.toString());
    }

    public Double getB() {
        return getDoubleParameter(bTextField, bMax);
    }

    public void setB(Double b) {
        bTextField.setText(b.toString());
    }

    public Double getC() {
        return getDoubleParameter(cTextField, dMax);
    }

    public void setC(Double c) {
        cTextField.setText(c.toString());
    }

    public Double getD() {
        return getDoubleParameter(dTextField, dMax);
    }

    public void setD(Double d) {
        dTextField.setText(d.toString());
    }
}
