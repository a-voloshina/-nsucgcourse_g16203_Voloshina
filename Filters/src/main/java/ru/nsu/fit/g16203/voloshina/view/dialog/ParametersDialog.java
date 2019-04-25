package ru.nsu.fit.g16203.voloshina.view.dialog;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ParametersDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField parameterTextField;
    private JSlider parameterSlider;
    private JLabel parameterLabel;

    private int max = 255;
    private boolean doubleMode = false;

    public ParametersDialog(boolean doubleMode) {
        this.doubleMode = doubleMode;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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


        if (doubleMode) {
            parameterSlider.setMaximum(500);
            parameterSlider.setMinimum(10);
        }

        parameterSlider.addChangeListener(event -> {
            if (doubleMode) {
                double param = ((JSlider) event.getSource()).getValue() / (double) 100;
                parameterTextField.setText(Double.toString(param));
            } else {
                int param = ((JSlider) event.getSource()).getValue();
                parameterTextField.setText(Integer.toString(param));
            }
        });

        parameterTextField.addActionListener(event -> {
            if (doubleMode) {
                Double param = getDoubleParameter();
                if (param != null) {
                    parameterSlider.setValue((int) (param * 100));
                }
            } else {
                Integer param = getInteger((JTextField) event.getSource(), max);
                if (param != null) {
                    parameterSlider.setValue(param);
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
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

    public void setOnOkListener(ActionListener listener) {
        buttonOK.addActionListener(listener);
    }

    public Integer getEdge() {
        return getInteger(parameterTextField, max);
    }

    public void setEdge(Integer edge) {
        parameterTextField.setText(edge.toString());
    }

    public Double getDoubleParameter() {
        try {
            double param = Double.parseDouble(parameterTextField.getText());
            return new BigDecimal(param).setScale(1, RoundingMode.UP).doubleValue();
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setDoubleParameter(Double parameter) {
        parameterTextField.setText(parameter.toString());
    }

    public void setLabelText(String labelText) {
        parameterLabel.setText(labelText);
    }

    public void setMaximum(int max) {
        parameterSlider.setMaximum(max);
        this.max = max;
    }

    public void setMinimum(int min) {
        parameterSlider.setMinimum(min);
    }
}
