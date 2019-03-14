package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.controller.Controller;
import ru.nsu.fit.g16203.voloshina.general.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class MainWindow extends MainFrame {

    private Controller controller;
    private FieldView fieldView;

    private int n = 10;
    private int m = 10;

    public MainWindow() {
        super(800, 600, "FIT_16203_Voloshina_Life");
        controller = new Controller(n, m);
        fieldView = new FieldView(controller);

        customizeMenu();
        customizeToolbar();

        JScrollPane scrollPane = new JScrollPane(fieldView, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.revalidate();
        add(scrollPane);

        setVisible(true);
        setMaximumSize(new Dimension(n, m));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

    private void customizeMenu() {
        Font font = new Font("TimesNewRoman", Font.PLAIN, 14);

        addSubMenu("File", font, KeyEvent.VK_F);
        addMenuItem("File/New", "Create new file", KeyEvent.VK_N,
                "new-file.png", font, new NewButtonListener(), false);
        addMenuItem("File/Open", "Open source file", KeyEvent.VK_O,
                "folder.png", font, new OpenButtonListener(), false);
        addMenuItem("File/Save", "Save current state in file", KeyEvent.VK_S,
                "bookmark.png", font, new SaveButtonListener(), false);
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
                null, font, null, false);

        addSubMenu("Edit", font, KeyEvent.VK_E);
        addSubMenu("Edit/Mode", font, KeyEvent.VK_M);
        addMenuItem("Edit/Mode/XOR", "Invert cell state", KeyEvent.VK_R,
                "xor.png", font, new XORButtonListener(), true);
        addMenuItem("Edit/Mode/Replace", "Resurrect cell", KeyEvent.VK_R,
                "replace.png", font, new replaceButtonListener(), true);
        addMenuSeparator("Edit");
        addMenuItem("Edit/Clear", "Clear field", KeyEvent.VK_C,
                "close.png", font, new clearButtonListener(), false);
        addMenuItem("Edit/Settings", "Set parameters",
                KeyEvent.VK_P, "settings.png", font, new SettingsButtonListener(), false);

        addSubMenu("View", font, KeyEvent.VK_V);
        addMenuItem("View/Toolbar", "Show/hide toolbar", KeyEvent.VK_T,
                null, font, null, true);
        addMenuItem("View/Status Bar", "Show/hide status bar", KeyEvent.VK_B,
                null, font, null, true);
        addMenuItem("View/Impact", "Show/hide impact values",
                KeyEvent.VK_I, null, font, new ImpactButtonListener(), true);

        addSubMenu("Simulation", font, KeyEvent.VK_U);
        addMenuItem("Simulation/Run", "Start simulation", KeyEvent.VK_F10,
                "play.png", font, new runButtonListener(), true);
        addMenuItem("Simulation/Next", "Make one simulation step", KeyEvent.VK_F8,
                "next.png", font, new stepButtonListener(), false);

        addSubMenu("Help", font, KeyEvent.VK_A);
        addMenuItem("Help/About", "Some information about application", KeyEvent.VK_F8,
                "about.png", font, null, false);
    }

    private void customizeToolbar() {
        addToolBarButton("File/New");
        addToolBarButton("File/Open");
        addToolBarButton("File/Save");
        //addToolBarButton("File/Exit");
        addToolBarSeparator();
        addToolBarButton("Edit/Mode/XOR");
        addToolBarButton("Edit/Mode/Replace");
        addToolBarButton("View/Impact", "visible.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Clear");
        addToolBarButton("Edit/Settings");
        addToolBarSeparator();
        addToolBarButton("Simulation/Run");
        addToolBarButton("Simulation/Next");
        addToolBarSeparator();
        addToolBarButton("Help/About");

        setMode();
    }

    private void setMode() {
        getToolBarButton("Edit/Mode/XOR").setSelected(true);
        fieldView.turnXORModeOn();
    }

    class stepButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fieldView.simulationStep();
        }
    }

    class runButtonListener implements ActionListener {

        private boolean isSimulationOn = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isSimulationOn) {
                fieldView.stopSimulation();
                isSimulationOn = false;
                getToolBarButton("Edit/Clear").setEnabled(true);
                getToolBarButton("Edit/Settings").setEnabled(true);
                getToolBarButton("Simulation/Next").setEnabled(true);
            } else {
                fieldView.runSimulation();
                isSimulationOn = true;
                getToolBarButton("Edit/Clear").setEnabled(false);
                getToolBarButton("Edit/Settings").setEnabled(false);
                getToolBarButton("Simulation/Next").setEnabled(false);
            }
        }
    }

    class clearButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fieldView.clearField();
        }
    }

    class XORButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getToolBarButton("Edit/Mode/XOR").setSelected(true);
            getToolBarButton("Edit/Mode/Replace").setSelected(false);
            fieldView.turnXORModeOn();
        }
    }

    class replaceButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getToolBarButton("Edit/Mode/XOR").setSelected(false);
            getToolBarButton("Edit/Mode/Replace").setSelected(true);
            fieldView.turnXORModeOff();
        }
    }

    class SettingsButtonListener implements ActionListener {

        SettingsDialog dialog = new SettingsDialog(new OKButtonListener(), new CancelButtonListener());
        ;

        Double curBIRTH_BEGIN;
        Double curBIRTH_END;
        Double curLIVE_BEGIN;
        Double curLIVE_END;
        int curCellSize;
        int curGridWidth;
        int curTimerPeriod;
        Double curFST_IMPACT;
        Double curSND_IMPACT;
        boolean curXORMode;
        int curRowsCount;
        int curColumnsCount;

        @Override
        public void actionPerformed(ActionEvent e) {
            curBIRTH_BEGIN = controller.getBIRTH_BEGIN();
            curBIRTH_END = controller.getBIRTH_END();
            curLIVE_BEGIN = controller.getLIVE_BEGIN();
            curLIVE_END = controller.getLIVE_END();
            curCellSize = fieldView.getCellSize();
            curGridWidth = fieldView.getGridWidth();
            curTimerPeriod = fieldView.getTimePeriod();
            curFST_IMPACT = controller.getFST_IMPACT();
            curSND_IMPACT = controller.getSND_IMPACT();
            curXORMode = fieldView.isXORModeOn();
            curRowsCount = controller.getFieldHeight();
            curColumnsCount = controller.getFieldWidth();

            dialog.setBIRTH_BEGIN(curBIRTH_BEGIN);
            dialog.setBIRTH_END(curBIRTH_END);
            dialog.setLIVE_BEGIN(curLIVE_BEGIN);
            dialog.setLIVE_END(curLIVE_END);
            dialog.setFST_IMPACT(curFST_IMPACT);
            dialog.setSND_IMPACT(curSND_IMPACT);
            dialog.setCellSize(curCellSize);
            dialog.setRows(curRowsCount);
            dialog.setColumns(curColumnsCount);
            dialog.setGridWidth(curGridWidth);
            dialog.setTimerPeriod(curTimerPeriod);
            dialog.setMode(curXORMode);
            dialog.pack();
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(fieldView);
            dialog.setVisible(true);
        }

        class OKButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Double BIRTH_BEGIN = dialog.getBIRTH_BEGIN();
                if (BIRTH_BEGIN != null && !BIRTH_BEGIN.equals(curBIRTH_BEGIN)) {
                    controller.setBIRTH_BEGIN(BIRTH_BEGIN);
                }
                Double BIRTH_END = dialog.getBIRTH_END();
                if (BIRTH_END != null && !BIRTH_END.equals(curBIRTH_END)) {
                    controller.setBIRTH_END(BIRTH_END);
                }
                Double LIVE_BEGIN = dialog.getLIVE_BEGIN();
                if (LIVE_BEGIN != null && !LIVE_BEGIN.equals(curLIVE_BEGIN)) {
                    controller.setLIVE_BEGIN(LIVE_BEGIN);
                }
                Double LIVE_END = dialog.getLIVE_END();
                if (LIVE_END != null && !LIVE_END.equals(curLIVE_END)) {
                    controller.setLIVE_END(LIVE_END);
                }
                Double FST_IMPACT = dialog.getFST_IMPACT();
                if (FST_IMPACT != null && !FST_IMPACT.equals(curFST_IMPACT)) {
                    controller.setFST_IMPACT(FST_IMPACT);
                }
                Double SND_IMPACT = dialog.getSND_IMPACT();
                if (SND_IMPACT != null && !SND_IMPACT.equals(curSND_IMPACT)) {
                    controller.setSND_IMPACT(SND_IMPACT);
                }
                Integer rows = dialog.getRowsCount();
                Integer columns = dialog.getColumnsCount();
                if (rows != null && columns != null && (rows != n || columns != m)) {
                    fieldView.resizeField(columns, rows);
                }
                Integer cellSize = dialog.getCellSize();
                if (cellSize != null && cellSize != curCellSize) {
                    fieldView.setCellSize(cellSize);
                }
                Integer gridWidth = dialog.getGridWidth();
                if (gridWidth != null && gridWidth != curGridWidth) {
                    fieldView.setGridWidth(gridWidth);
                }
                Integer timerPeriod = dialog.getTimerPeriod();
                if (timerPeriod != curTimerPeriod) {
                    fieldView.setTimePeriod(timerPeriod);
                }
                try {
                    boolean XORMode = dialog.getMode();
                    if (XORMode) {
                        fieldView.turnXORModeOn();
                    } else {
                        fieldView.turnXORModeOff();
                    }
                } catch (Exception ignored) {
                }
                dialog.dispose();
            }
        }

        class CancelButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }
    }

    class ImpactButtonListener implements ActionListener {

        private boolean isSelected = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isSelected) {
                getToolBarButton("View/Impact").setSelected(true);
                fieldView.showImpacts();
                isSelected = true;
            } else {
                getToolBarButton("View/Impact").setSelected(false);
                fieldView.hideImpacts();
                isSelected = false;
            }
        }
    }

    class NewButtonListener extends SettingsButtonListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            fieldView.clearField();
            fieldView.hideImpacts();
        }

    }

    class OpenButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File openFile = getOpenFileName(null, null);
            if (openFile != null) {
                fieldView.clearField();
                try {
                    BufferedReader buf = new BufferedReader(new FileReader(openFile));
                    String line;
                    int lineCount = 1;
                    while ((line = buf.readLine()) != null) {
                        int commentStartIndex = line.indexOf("//");
                        if (commentStartIndex > 0) {
                            line = line.substring(0, commentStartIndex);
                        }
                        String[] numbers = line.split(" ");
                        switch (lineCount) {
                            case 1:
                                fieldView.resizeField(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
                                break;
                            case 2:
                                fieldView.setGridWidth(Integer.parseInt(numbers[0]));
                                break;
                            case 3:
                                fieldView.setCellSize(Integer.parseInt(numbers[0]));
                                break;
                            case 4:
                                break;
                            default:
                                controller.setAliveCell(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
                        }
                        lineCount++;
                    }
                    fieldView.updateField();
                } catch (Exception exception) {
                    fieldView.clearField();
                    System.err.println("Wrong input file format");              //TODO!!
                }
            }
        }

    }

    class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File saveFile = getSaveFileName("txt", "Text files");
            if (saveFile != null) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                    writer.write(controller.getFieldHeight() + " " + controller.getFieldWidth() + "\n");
                    writer.write(fieldView.getGridWidth() + "\n");
                    writer.write(fieldView.getCellSize() + "\n");
                    ArrayList<Pair<Integer, Integer>> aliveCells = controller.getAliveCells();
                    int aliveCellsCount = aliveCells.size();
                    writer.write(aliveCellsCount + "\n");
                    for (Pair<Integer, Integer> curAliveCell : aliveCells) {
                        writer.write(curAliveCell.getKey() + " " + curAliveCell.getValue() + "\n");
                    }
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }

    }

}
