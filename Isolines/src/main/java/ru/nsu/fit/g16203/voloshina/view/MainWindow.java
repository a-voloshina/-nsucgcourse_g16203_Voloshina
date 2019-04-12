package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.external.MainFrame;
import ru.nsu.fit.g16203.voloshina.OpenFileExeption;
import ru.nsu.fit.g16203.voloshina.controller.Controller;
import ru.nsu.fit.g16203.voloshina.controller.FileController;
import ru.nsu.fit.g16203.voloshina.general.MenuElementMouseListener;
import ru.nsu.fit.g16203.voloshina.general.StatusBar;
import ru.nsu.fit.g16203.voloshina.general.Window;
import ru.nsu.fit.g16203.voloshina.model.CircleFunction;
import ru.nsu.fit.g16203.voloshina.model.LinearFunction;
import ru.nsu.fit.g16203.voloshina.view.dialog.SettingsDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainWindow extends Window {

    private StatusBar statusBar;
    private FunctionViewPanel functionViewPanel;
    private FunctionViewPanel legendPanel;
    private JPanel userAreaPanel = new JPanel();

    private int fieldWidth = 500;
    private int fieldHeight = 500;

    private Controller controller;
    private Controller legendController;

    public MainWindow() {
        super(630, 650, "FIT_16203_Voloshina_Isolines");

        controller = new Controller(-10, 10, -10, 10, 10, 10,
                fieldWidth, fieldHeight, new CircleFunction());
        legendController = new Controller(0, 1, controller.getFunMin(), controller.getFunMax(),
                2, controller.getLevelsCount(), 50, fieldHeight, new LinearFunction());

        statusBar = new StatusBar(font, getWidth());
        customizeMenu();
        customizeToolbar();
        customizeUserArea();
        customizeStatusBar();

        locationComponent = this;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    protected void customizeMenu() {
        addSubMenu("File", font, KeyEvent.VK_F);
        addMenuItem("File/Open", "Open source file", KeyEvent.VK_O,
                "open.png", font, new OpenButtonMouseListener(), false);
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
                "logout.png", font, new ExitButtonMouseListener(), false);
        addSubMenu("Edit", font, KeyEvent.VK_E);
        addMenuItem("Edit/Grid", "Show grid", KeyEvent.VK_G,
                "grid.png", font, new GridButtonMouseListener(), true);
        addMenuItem("Edit/Color map", "Show color map of function", KeyEvent.VK_M,
                "colormap.png", font, new ColorMapButtonMouseListener(), true);
        addMenuItem("Edit/Isolines", "Show isolines", KeyEvent.VK_I,
                "topography.png", font, new IsolinesButtonMouseListener(), true);
        addMenuItem("Edit/Rectangle intersection", "Show intersection isolines with grid cells", KeyEvent.VK_R,
                "circleshapered.png", font, new RectangleIntersectionButtonMouseListener(), true);
        addMenuItem("Edit/Triangle intersection", "Show intersection isolines with grid diagonals", KeyEvent.VK_T,
                "circleshapegreen.png", font, new TriangleIntersectionButtonMouseListener(), true);
        addMenuItem("Edit/Interpolate", "Show interpolated color map of function", KeyEvent.VK_N,
                "interpolation.png", font, new InterpolateButtonMouseListener(), true);
        addMenuItem("Edit/Settings", "Show settings window", KeyEvent.VK_S,
                "settings.png", font, new SettingsButtonMouseListener(), false);
        addMenuItem("Edit/Delete user isolines", "Delete user isolines", KeyEvent.VK_D,
                "eraser.png", font, new EraserButtonMouseListener(), false);
    }

    @Override
    protected void customizeToolbar() {
        addToolBarButton("File/Open");
        addToolBarSeparator();
        addToolBarButton("Edit/Grid");
        addToolBarButton("Edit/Color map");
        addToolBarButton("Edit/Isolines");
        addToolBarButton("Edit/Rectangle intersection");
        addToolBarButton("Edit/Triangle intersection");
        addToolBarButton("Edit/Interpolate");
        addToolBarButton("Edit/Settings");
        addToolBarButton("Edit/Delete user isolines");

        getToolBarButton("Edit/Color map").setSelected(true);
    }

    @Override
    protected void customizeStatusBar() {
        add(statusBar.getStatusPanel(), BorderLayout.PAGE_END);
    }

    private void customizeUserArea() {
        userAreaPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 10));
        functionViewPanel = new FunctionViewPanel(controller, controller.getFieldWidth(), controller.getFieldHeight(),
                new FunctionViewPanelMouseMovingListener());
        functionViewPanel.drawColorFuncMap();
        legendPanel = new FunctionViewPanel(legendController, legendController.getFieldWidth(),
                legendController.getFieldHeight(), null);
        legendPanel.drawColorFuncMap();
        userAreaPanel.add(functionViewPanel);
        userAreaPanel.add(legendPanel);
        add(userAreaPanel);
    }

    private void updateCondition() {
        if (getToolBarButton("Edit/Grid").isSelected()) {
            functionViewPanel.drawGrid();
        }
        if (getToolBarButton("Edit/Color map").isSelected()) {
            functionViewPanel.drawColorFuncMap();
            legendPanel.drawColorFuncMap();
        }
        if (getToolBarButton("Edit/Isolines").isSelected()) {
            try {
                functionViewPanel.drawIsolines();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (getToolBarButton("Edit/Triangle intersection").isSelected()) {
            functionViewPanel.setIntersectionTrianPointModeOn(true);
            try {
                functionViewPanel.drawIsolines();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (getToolBarButton("Edit/Rectangle intersection").isSelected()) {
            functionViewPanel.setIntersectionRectPointModeOn(true);
            try {
                functionViewPanel.drawIsolines();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (getToolBarButton("Edit/Interpolate").isSelected()) {
            functionViewPanel.drawInterpolateColorMap();
            legendPanel.drawInterpolateColorMap();
        }
    }

    class FunctionViewPanelMouseMovingListener implements FunctionViewPanel.MouseMovingListener {

        @Override
        public void mouseMoved(double trueFuncValue, double interpolatedFuncValue, int x, int y) {
            statusBar.setText(x + ", " + y + " | " + new BigDecimal(trueFuncValue)
                    .setScale(3, RoundingMode.UP).doubleValue() +
                    " | " + new BigDecimal(interpolatedFuncValue).setScale(3, RoundingMode.UP).doubleValue());
        }
    }

    class OpenButtonMouseListener extends MenuElementMouseListener {
        OpenButtonMouseListener() {
            super("File/Open", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                File configFile = getOpenFileName(new String[]{"txt"}, "Text config format");
                if (configFile != null) {
                    FileController fileController = new FileController();
                    fileController.parseFile(configFile);
                    controller.setGridParams(fileController.getK(), fileController.getM());
                    int n = fileController.getN();
                    controller.setLevelsCount(n);
                    legendController.setLevelsCount(n);
                    legendController.setGridParams(2, controller.getLevelsCount());
                    ArrayList<Color> colorsList = fileController.getColorsList();
                    controller.setColorsList(colorsList);
                    legendController.setColorsList(colorsList);
                    functionViewPanel.setIsolinesColor(fileController.getIsolinesColor());
                    updateCondition();
                }
            } catch (IOException e1) {
                String messageText = "Неверный формат файла";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (OpenFileExeption openFileExeption) {
                String messageText = "Не удалось открыть файл";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ExitButtonMouseListener extends MenuElementMouseListener {

        ExitButtonMouseListener() {
            super("File/Exit", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.exit(0);
        }
    }

    class GridButtonMouseListener extends MenuElementMouseListener {
        private String menuPath;

        GridButtonMouseListener() {
            super("Edit/Grid", statusBar, (MainFrame) locationComponent);
            menuPath = "Edit/Grid";
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton gridToolbarButton = getToolBarButton(menuPath);
            if (!gridToolbarButton.isSelected()) {
                gridToolbarButton.setSelected(true);
                functionViewPanel.drawGrid();
            } else {
                gridToolbarButton.setSelected(false);
                functionViewPanel.hideGrid();
            }
        }
    }

    class ColorMapButtonMouseListener extends MenuElementMouseListener {
        ColorMapButtonMouseListener() {
            super("Edit/Color map", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton colorMapButton = getToolBarButton("Edit/Color map");
            JButton interpolationToolbarButton = getToolBarButton("Edit/Interpolate");
            if (!colorMapButton.isSelected()) {
                colorMapButton.setSelected(true);
                interpolationToolbarButton.setSelected(false);
                functionViewPanel.drawColorFuncMap();
                legendPanel.drawColorFuncMap();
            } else {
                colorMapButton.setSelected(false);
                interpolationToolbarButton.setSelected(true);
                legendPanel.drawInterpolateColorMap();
                functionViewPanel.drawInterpolateColorMap();
            }
        }
    }

    class IsolinesButtonMouseListener extends MenuElementMouseListener {
        IsolinesButtonMouseListener() {
            super("Edit/Isolines", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton isolinesToolbarButton = getToolBarButton("Edit/Isolines");
            try {
                if (!isolinesToolbarButton.isSelected()) {
                    isolinesToolbarButton.setSelected(true);
                    functionViewPanel.setIsolinesModeOn(true);
                    functionViewPanel.drawIsolines();
                } else {
                    isolinesToolbarButton.setSelected(false);
                    functionViewPanel.setIsolinesModeOn(false);
                    functionViewPanel.hideIsolines();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class TriangleIntersectionButtonMouseListener extends MenuElementMouseListener {
        TriangleIntersectionButtonMouseListener() {
            super("Edit/Triangle intersection", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton intersectionToolbarButton = getToolBarButton("Edit/Triangle intersection");
            try {
                if (!intersectionToolbarButton.isSelected()) {
                    intersectionToolbarButton.setSelected(true);
                    functionViewPanel.setIntersectionTrianPointModeOn(true);
                    functionViewPanel.drawIsolines();
                } else {
                    intersectionToolbarButton.setSelected(false);
                    functionViewPanel.setIntersectionTrianPointModeOn(false);
                    functionViewPanel.drawIsolines();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class RectangleIntersectionButtonMouseListener extends MenuElementMouseListener {
        RectangleIntersectionButtonMouseListener() {
            super("Edit/Rectangle intersection", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton intersectionToolbarButton = getToolBarButton("Edit/Rectangle intersection");
            try {
                if (!intersectionToolbarButton.isSelected()) {
                    intersectionToolbarButton.setSelected(true);
                    functionViewPanel.setIntersectionRectPointModeOn(true);
                    functionViewPanel.drawIsolines();
                } else {
                    intersectionToolbarButton.setSelected(false);
                    functionViewPanel.setIntersectionRectPointModeOn(false);
                    functionViewPanel.drawIsolines();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class InterpolateButtonMouseListener extends MenuElementMouseListener {
        InterpolateButtonMouseListener() {
            super("Edit/Interpolate", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton interpolationToolbarButton = getToolBarButton("Edit/Interpolate");
            JButton colorMapButton = getToolBarButton("Edit/Color map");
            try {
                if (!interpolationToolbarButton.isSelected()) {
                    interpolationToolbarButton.setSelected(true);
                    colorMapButton.setSelected(false);
                    functionViewPanel.drawInterpolateColorMap();
                    legendPanel.drawInterpolateColorMap();
                } else {
                    interpolationToolbarButton.setSelected(false);
                    colorMapButton.setSelected(true);
                    functionViewPanel.drawColorFuncMap();
                    legendPanel.drawColorFuncMap();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class SettingsButtonMouseListener extends MenuElementMouseListener {

        private int curK = controller.getK();
        private int curM = controller.getM();
        private double curA = controller.getA();
        private double curB = controller.getB();
        private double curC = controller.getC();
        private double curD = controller.getD();

        SettingsButtonMouseListener() {
            super("Edit/Interpolate", statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.setK(curK);
            settingsDialog.setM(curM);
            settingsDialog.setA(curA);
            settingsDialog.setB(curB);
            settingsDialog.setC(curC);
            settingsDialog.setD(curD);
            settingsDialog.setOnOkListener(new OnOkListener(settingsDialog));

            settingsDialog.pack();
            settingsDialog.setResizable(false);
            settingsDialog.setLocationRelativeTo(locationComponent);
            settingsDialog.setVisible(true);
        }

        class OnOkListener implements ActionListener {

            private SettingsDialog dialog;

            OnOkListener(SettingsDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer k = dialog.getK();
                Integer m = dialog.getM();
                Double a = dialog.getA();
                Double b = dialog.getB();
                Double c = dialog.getC();
                Double d = dialog.getD();
                if (k != null && m != null && a != null && b != null && c != null && d != null) {
                    curK = k;
                    curM = m;
                    curA = a;
                    curB = b;
                    curC = c;
                    curD = d;
                    controller.setGridParams(k, m);
                    controller.setDomain(a, b, c, d);
                    legendController.setDomain(legendController.getA(), legendController.getB(), controller.getFunMin(),
                            controller.getFunMax());
                    updateCondition();
                }
            }
        }
    }

    class EraserButtonMouseListener extends MenuElementMouseListener {
        EraserButtonMouseListener() {
            super("Edit/Delete user isolines",
                    statusBar, (MainFrame) locationComponent);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                functionViewPanel.deleteUserIsolines();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
