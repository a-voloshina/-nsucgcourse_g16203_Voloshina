package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JPanel Field;
    private JLabel rowsLabel;
    private JLabel columnsLabel;
    private JLabel cellSizeLabel;
    private JLabel gridWidthLabel;
    private JLabel timerLabel;
    private JTextField rowsTextField;
    private JTextField columnsTextField;
    private JTextField cellSizeTextField;
    private JTextField timerPeriodTextField;
    private JSlider rowsSlider;
    private JSlider columnsSlider;
    private JSlider cellSizeSlider;
    private JSlider gridWidthSlider;
    private JSlider timerSlider;
    private JTextField gridWidthTextField;
    private JPanel Properties;
    private JLabel liveBeginLabel;
    private JLabel liveEndLabel;
    private JTextField LIVE_BEGINTextField;
    private JTextField LIVE_ENDTextField;
    private JTextField BIRTH_BEGINTextField;
    private JTextField BIRTH_ENDTextField;
    private JPanel Mode;
    private JRadioButton XORRadioButton;
    private JRadioButton replaceRadioButton;
    private ButtonGroup modeButtonGroup = new ButtonGroup();
    private JTextField fstImpactTextField;
    private JTextField scndImpactTextField;
    private JButton buttonOK;
    private JButton buttonCancel;
    private int rowsMax = 100;
    private int columnsMax = 100;
    private int cellSizeMax = 50;
    private int gridWidthMax = 10;
    private int timerMax = 10000;

    public SettingsDialog(ActionListener okButtonListener, ActionListener cancelButtonListener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        rowsSlider.addChangeListener(event -> {
            int currentRowsCount = ((JSlider) event.getSource()).getValue();
            rowsTextField.setText(Integer.toString(currentRowsCount));
        });

        rowsTextField.addActionListener(event -> {
            Integer currentCellSize = getInteger((JTextField) event.getSource(), rowsMax);
            if (currentCellSize != null) {
                rowsSlider.setValue(currentCellSize);
            }
        });

        columnsSlider.addChangeListener(event -> {
            int currentColumnsCount = ((JSlider) event.getSource()).getValue();
            columnsTextField.setText(Integer.toString(currentColumnsCount));
        });

        columnsTextField.addActionListener(event -> {
            Integer currentColumnsCount = getInteger((JTextField) event.getSource(), columnsMax);
            if (currentColumnsCount != null) {
                columnsSlider.setValue(currentColumnsCount);
            }
        });

        cellSizeSlider.addChangeListener(event -> {
            int currentCellSize = ((JSlider) event.getSource()).getValue();
            cellSizeTextField.setText(Integer.toString(currentCellSize));
        });


        cellSizeTextField.addActionListener(event -> {
            Integer currentCellSize = getInteger((JTextField) event.getSource(), cellSizeMax);
            if (currentCellSize != null) {
                cellSizeSlider.setValue(currentCellSize);
            }
        });

        gridWidthSlider.addChangeListener(event -> {
            int currentGridWidth = ((JSlider) event.getSource()).getValue();
            gridWidthTextField.setText(Integer.toString(currentGridWidth));
        });

        gridWidthTextField.addActionListener(event -> {
            Integer currentGridWidth = getInteger((JTextField) event.getSource(), gridWidthMax);
            if (currentGridWidth != null) {
                gridWidthSlider.setValue(currentGridWidth);
            }
        });

        timerSlider.addChangeListener(event -> {
            int currentTimerPeriod = ((JSlider) event.getSource()).getValue();
            timerPeriodTextField.setText(Integer.toString(currentTimerPeriod));
        });

        timerPeriodTextField.addActionListener(event -> {
            Integer currentTimerPeriod = getInteger((JTextField) event.getSource(), timerMax);
            if (currentTimerPeriod != null) {
                timerSlider.setValue(currentTimerPeriod);
            }
        });

        modeButtonGroup.add(XORRadioButton);
        modeButtonGroup.add(replaceRadioButton);

        buttonOK.addActionListener(okButtonListener);
        buttonCancel.addActionListener(cancelButtonListener);

    }

    private boolean setTextFieldValue(int value, int valueMax, JTextField textField, JSlider valueSlider) {
        if (value <= valueMax) {
            textField.setText(Integer.toString(value));
            valueSlider.setValue(value);
            return true;
        }
        return false;
    }

    public boolean setRows(int rowsCount) {
        return setTextFieldValue(rowsCount, rowsMax, rowsTextField, rowsSlider);
    }

    public boolean setColumns(int columnsCount) {
        return setTextFieldValue(columnsCount, columnsMax, columnsTextField, columnsSlider);
    }

    public boolean setCellSize(int cellSize) {
        return setTextFieldValue(cellSize, cellSizeMax, cellSizeTextField, cellSizeSlider);
    }

    public boolean setGridWidth(int gridWidth) {
        return setTextFieldValue(gridWidth, gridWidthMax, gridWidthTextField, gridWidthSlider);
    }

    public boolean setTimerPeriod(int timerPeriod) {
        return setTextFieldValue(timerPeriod, timerMax, timerPeriodTextField, timerSlider);
    }

    public Integer getRowsCount() {
        return getInteger(rowsTextField, rowsMax);
    }

    public Integer getColumnsCount() {
        return getInteger(columnsTextField, columnsMax);
    }

    public Integer getCellSize() {
        return getInteger(cellSizeTextField, cellSizeMax);
    }

    public Integer getGridWidth() {
        return getInteger(gridWidthTextField, gridWidthMax);
    }

    public Integer getTimerPeriod() {
        return getInteger(timerPeriodTextField, timerMax);
    }

    private Integer getInteger(JTextField valueTextField, int valueMax) {
        try {
            int value = Integer.parseInt(valueTextField.getText());
            if (value <= valueMax) {
                return value;
            } else {
                return valueMax;
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Double getLIVE_BEGIN() {
        try {
            double liveBegin = Double.parseDouble(LIVE_BEGINTextField.getText());
            return new BigDecimal(liveBegin).setScale(1, RoundingMode.UP).doubleValue();
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setLIVE_BEGIN(Double LIVE_BEGIN) {
        LIVE_BEGINTextField.setText(Double.toString(LIVE_BEGIN));
    }

    public Double getLIVE_END() {
        try {
            return Double.parseDouble(LIVE_ENDTextField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setLIVE_END(Double LIVE_END) {
        LIVE_ENDTextField.setText(Double.toString(LIVE_END));
    }

    public Double getBIRTH_BEGIN() {
        try {
            double d = Double.parseDouble(BIRTH_BEGINTextField.getText());
            return d;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setBIRTH_BEGIN(Double BIRTH_BEGIN) {
        BIRTH_BEGINTextField.setText(Double.toString(BIRTH_BEGIN));
    }

    public Double getBIRTH_END() {
        try {
            return Double.parseDouble(BIRTH_ENDTextField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setBIRTH_END(Double BIRTH_END) {
        BIRTH_ENDTextField.setText(Double.toString(BIRTH_END));
    }

    public Double getFST_IMPACT() {
        try {
            return Double.parseDouble(fstImpactTextField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setFST_IMPACT(Double FST_IMPACT) {
        fstImpactTextField.setText(Double.toString(FST_IMPACT));
    }

    public Double getSND_IMPACT() {
        try {
            return Double.parseDouble(scndImpactTextField.getText());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void setSND_IMPACT(Double SND_IMPACT) {
        scndImpactTextField.setText(Double.toString(SND_IMPACT));
    }

    public boolean getMode() throws Exception {
        for (Enumeration<AbstractButton> buttons = modeButtonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText().equals("XOR");
            }
        }
        throw new Exception("Unknown mode");
    }

    public void setMode(boolean isXORModeOn) {
        if (isXORModeOn) {
            modeButtonGroup.setSelected(XORRadioButton.getModel(), true);
        } else {
            modeButtonGroup.setSelected(replaceRadioButton.getModel(), true);
        }
    }
}
