package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.controller.Controller;
import ru.nsu.fit.g16203.voloshina.general.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class MainWindow extends MainFrame {

    private Controller controller;
    private FieldView fieldView;
    JPanel statusPanel;
    private JLabel statusBar;
    private Component locationComponent;

    private int n = 30;
    private int m = 30;

    private Font font = new Font("TimesNewRoman", Font.PLAIN, 14);

    public MainWindow() {
        super(800, 600, "FIT_16203_Voloshina_Life");
        setTitle("Life Game");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        controller = new Controller(n, m);
        fieldView = new FieldView(controller);
        locationComponent = this;

        customizeMenu();
        customizeToolbar();

        JScrollPane scrollPane = new JScrollPane(fieldView);
        add(scrollPane, BorderLayout.CENTER);

        customizeStatusBar();

        addWindowListener(new MainWindowListener());

        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println("Status Bar " + isSelectedMenuElement("View/Status Bar"));
        System.out.println("Toolbar " + isSelectedMenuElement("View/Toolbar"));
        System.out.println("Impact " + isSelectedMenuElement("View/Impact"));
        System.out.println();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

    private void customizeMenu() {
        addSubMenu("File", font, KeyEvent.VK_F);
        addMenuItem("File/New", "Create new file", KeyEvent.VK_N,
                "new-file.png", font, new NewButtonListener(), false);
        addMenuItem("File/Open", "Open source file", KeyEvent.VK_O,
                "folder.png", font, new OpenButtonListener(), false);
        addMenuItem("File/Save", "Save current state in file", KeyEvent.VK_S,
                "bookmark.png", font, new SaveButtonListener(), false);
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
                null, font, new ExitButtonListener(), false);

        addSubMenu("Edit", font, KeyEvent.VK_E);
        addSubMenu("Edit/Mode", font, KeyEvent.VK_M);
        addMenuItem("Edit/Mode/XOR", "Invert cell state", KeyEvent.VK_R,
                "xor.png", font, new XORButtonListener(), true);
        addMenuItem("Edit/Mode/Replace", "Resurrect cell", KeyEvent.VK_R,
                "replace.png", font, new replaceButtonListener(), true);
        addMenuSeparator("Edit");
        addMenuItem("Edit/Clear", "Clear field", KeyEvent.VK_C,
                "close.png", font, new clearButtonListener(), false);
        addMenuItem("Edit/Settings", "Set parameters", KeyEvent.VK_P,
                "settings.png", font, new SettingsButtonListener(), false);

        addSubMenu("View", font, KeyEvent.VK_V);
        addMenuItem("View/Toolbar", "Show/hide toolbar", KeyEvent.VK_T,
                null, font, new ToolbarButtonListener(), true);
        addMenuItem("View/Status Bar", "Show/hide status bar", KeyEvent.VK_B,
                null, font, new StatusBarButtonListener(), true);
        addMenuItem("View/Impact", "Show/hide impact values", KeyEvent.VK_I,
                null, font, new ImpactButtonListener(), true);

        addSubMenu("Simulation", font, KeyEvent.VK_U);
        addMenuItem("Simulation/Run", "Start simulation", KeyEvent.VK_F10,
                "play.png", font, new runButtonListener(), true);
        addMenuItem("Simulation/Next", "Make one simulation step", KeyEvent.VK_F8,
                "next.png", font, new stepButtonListener(), false);

        addSubMenu("Help", font, KeyEvent.VK_A);
        addMenuItem("Help/About", "Some information about application", KeyEvent.VK_F8,
                "about.png", font, new AboutButtonListener(), false);

        setSelectedMenuElement("View/Toolbar", true);
    }

    private void customizeToolbar() {
        addToolBarButton("File/New");
        addToolBarButton("File/Open");
        addToolBarButton("File/Save");
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

    private void customizeStatusBar() {
        statusPanel = new JPanel();
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusBar = new JLabel("Ready");
        statusBar.setFont(font);
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusBar);
        setSelectedMenuElement("View/Status Bar", true);
    }

    private void setMode() {
        getToolBarButton("Edit/Mode/XOR").setSelected(true);
        setSelectedMenuElement("Edit/Mode/XOR", true);
        fieldView.turnXORModeOn();
    }

    private void fastenChanges(boolean isApplicationClosing) {
        if (fieldView.isFieldConditionChanged()) {
            int ret = JOptionPane.showConfirmDialog(locationComponent, "Сохранить изменения?", "Сохранить изменения",
                    JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.OK_OPTION) {
                if ((new SaveButtonListener()).actionPerformed()) {
                    fieldView.fastenFieldConditionChanges();
                }
            }
        }
        if (isApplicationClosing) {
            System.exit(0);
        }
    }

    class stepButtonListener implements MouseListener {

        private String tooltip = "Make one simulation step";

        void actionPerformed() {
            fieldView.simulationStep();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class runButtonListener implements MouseListener {

        private String tooltip = "Start simulation";
        private boolean isSimulationOn = false;

        private void actionPerformed() {
            if (isSimulationOn) {
                fieldView.stopSimulation();
                isSimulationOn = false;
                getToolBarButton("Edit/Clear").setEnabled(true);
                setEnabledMenuElement("Edit/Clear", true);
                getToolBarButton("Edit/Settings").setEnabled(true);
                setEnabledMenuElement("Edit/Settings", true);
                getToolBarButton("Simulation/Next").setEnabled(true);
                setEnabledMenuElement("Simulation/Next", true);
            } else {
                fieldView.runSimulation();
                isSimulationOn = true;
                getToolBarButton("Edit/Clear").setEnabled(false);
                setEnabledMenuElement("Edit/Clear", false);
                getToolBarButton("Edit/Settings").setEnabled(false);
                setEnabledMenuElement("Edit/Settings", false);
                getToolBarButton("Simulation/Next").setEnabled(false);
                setEnabledMenuElement("Simulation/Next", false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class clearButtonListener implements MouseListener {

        private String tooltip = "Clear field";

        void actionPerformed() {
            fieldView.clearField();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class XORButtonListener implements MouseListener {

        private String tooltip = "Turn invert cells states mode on";

        void actionPerformed() {
            getToolBarButton("Edit/Mode/XOR").setSelected(true);
            setSelectedMenuElement("Edit/Mode/XOR", true);
            getToolBarButton("Edit/Mode/Replace").setSelected(false);
            setSelectedMenuElement("Edit/Mode/Replace", false);
            fieldView.turnXORModeOn();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isSelectedMenuElement("Edit/Mode/XOR")) {
                tooltip = "Turn resurrecting cells mode on";
            } else {
                tooltip = "Turn invert cells states mode on";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class replaceButtonListener implements MouseListener {

        private String tooltip = "Turn resurrecting cells mode on";

        void actionPerformed() {
            getToolBarButton("Edit/Mode/XOR").setSelected(false);
            setSelectedMenuElement("Edit/Mode/XOR", false);
            getToolBarButton("Edit/Mode/Replace").setSelected(true);
            setSelectedMenuElement("Edit/Mode/Replace", true);
            fieldView.turnXORModeOff();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isSelectedMenuElement("Edit/Mode/Replace")) {
                tooltip = "Turn invert cells states mode on";
            } else {
                tooltip = "Turn resurrecting cells mode on";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class SettingsButtonListener implements MouseListener {

        private String tooltip = "Customize game's parameters";
        private SettingsDialog dialog;

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

        protected void actionPerformed() {
            dialog = new SettingsDialog();
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
            dialog.setCheckModeOn();
            dialog.setOkButtonListener(new OKButtonListener());
            dialog.setCancelButtonListener(new CancelButtonListener());

            dialog.pack();
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(locationComponent);
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

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class ImpactButtonListener implements MouseListener {

        private String tooltip = "Show/hide impact values";

        private void actionPerformed() {
            if (!isSelectedMenuElement("View/Impact")) {
                getToolBarButton("View/Impact").setSelected(true);
                fieldView.showImpacts();
            } else {
                getToolBarButton("View/Impact").setSelected(false);
                fieldView.hideImpacts();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isSelectedMenuElement("View/Impact")) {
                tooltip = "Show impact values";
            } else {
                tooltip = "Hide impact values";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class NewButtonListener extends SettingsButtonListener {

        private String tooltip = "Create a new file";

        protected void actionPerformed() {
            fastenChanges(false);

            super.actionPerformed();
            fieldView.clearField();
            fieldView.hideImpacts();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }

    }

    class OpenButtonListener implements MouseListener {

        private String tooltip = "Open source file";

        private void actionPerformed() {
            fastenChanges(false);

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
                    fieldView.fastenFieldConditionChanges();
                } catch (Exception exception) {
                    String messageText = "Не удалось открыть файл";
                    JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }

    }

    class SaveButtonListener implements MouseListener {

        private String tooltip = "Save current state in file";

        private boolean actionPerformed() {
            File saveFile = getSaveFileName("txt", "Text files");
            if (saveFile != null) {
                try {
                    fieldView.fastenFieldConditionChanges();
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
                } catch (IOException ignored) {
                }
                return true;
            }
            return false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class AboutButtonListener implements MouseListener {

        String tooltip = "Some information about application";

        private void actionPerformed() {
            AboutDialog dialog = new AboutDialog();
            dialog.pack();
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(locationComponent);
            dialog.setVisible(true);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class ExitButtonListener implements MouseListener {

        String tooltip = "Exit application";

        private void actionPerformed() {
            fastenChanges(true);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class ToolbarButtonListener implements MouseListener {

        private String tooltip = "Show/hide toolbar";

        private void actionPerformed() {
            if (!isSelectedMenuElement("View/Toolbar")) {
                showToolbar();
            } else {
                hideToolbar();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isSelectedMenuElement("View/Toolbar")) {
                tooltip = "Show toolbar";
            } else {
                tooltip = "Hide toolbar";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class StatusBarButtonListener implements MouseListener {

        String tooltip = "Exit application";

        private void actionPerformed() {
            if (!isSelectedMenuElement("View/Status Bar")) {
                statusPanel.setVisible(true);
            } else {
                statusPanel.setVisible(false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isSelectedMenuElement("View/Status Bar")) {
                tooltip = "Show status bar";
            } else {
                tooltip = "Hide status bar";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class MainWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            fastenChanges(true);
        }

    }

}
