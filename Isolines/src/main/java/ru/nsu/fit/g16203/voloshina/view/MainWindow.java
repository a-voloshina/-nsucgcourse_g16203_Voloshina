package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.external.MainFrame;
import ru.nsu.fit.g16203.voloshina.controller.Controller;
import ru.nsu.fit.g16203.voloshina.general.MenuElementMouseListener;
import ru.nsu.fit.g16203.voloshina.general.StatusBar;
import ru.nsu.fit.g16203.voloshina.general.Window;
import ru.nsu.fit.g16203.voloshina.model.HyberbolaFunction;
import ru.nsu.fit.g16203.voloshina.model.LinearFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MainWindow extends Window {

    private StatusBar statusBar;
    private FunctionViewPanel functionViewPanel;
    private FunctionViewPanel legendPanel;
    private JPanel userAreaPanel = new JPanel();

    private int fieldWidth = 500;
    private int fieldHeight = 500;

    public MainWindow() {
        super(800, 650, "FIT_16203_Voloshina_Isolines");

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

        getToolBarButton("Edit/Color map").setSelected(true);
    }

    @Override
    protected void customizeStatusBar() {
        add(statusBar.getStatusPanel(), BorderLayout.PAGE_END);
    }

    private void customizeUserArea(){
        userAreaPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 10));
        Controller controller = new Controller(-10, 10, -10, 10, 10, 10,
                fieldWidth, fieldHeight, new HyberbolaFunction());
        functionViewPanel = new FunctionViewPanel(controller, controller.getFieldWidth(), controller.getFieldHeight());
        functionViewPanel.drawColorFuncMap();
        Controller legendController = new Controller(0, 1, controller.getFunMin(), controller.getFunMax(),
                2, controller.getK()*controller.getM(), 50, fieldHeight, new LinearFunction());
        legendPanel = new FunctionViewPanel(legendController, legendController.getFieldWidth(),
                legendController.getFieldHeight());
        legendPanel.drawColorFuncMap();
        userAreaPanel.add(functionViewPanel);
        userAreaPanel.add(legendPanel);
        add(userAreaPanel);
    }

    class OpenButtonMouseListener extends MenuElementMouseListener {
        OpenButtonMouseListener() {
            super("File/Open", statusBar, (MainFrame) locationComponent);
        }
    }

    class ExitButtonMouseListener extends MenuElementMouseListener{

        ExitButtonMouseListener() {
            super("File/Exit", statusBar, (MainFrame)locationComponent);
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
//            if(isSelectedMenuElement(menuPath)){
//                functionViewPanel.hideGrid();
//            } else {
//                functionViewPanel.drawGrid();
//            }
            if(!gridToolbarButton.isSelected()){
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
            if(!colorMapButton.isSelected()){
                colorMapButton.setSelected(true);
                interpolationToolbarButton.setSelected(false);
                functionViewPanel.drawColorFuncMap();
            } else {
                colorMapButton.setSelected(false);
                functionViewPanel.hideColorMap();
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
                if(!isolinesToolbarButton.isSelected()){
                    isolinesToolbarButton.setSelected(true);
                    functionViewPanel.drawIsolines();
                } else {
                    isolinesToolbarButton.setSelected(false);
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
            JButton isolinesToolbarButton = getToolBarButton("Edit/Isolines");
            try {
                if(!intersectionToolbarButton.isSelected()){
                    intersectionToolbarButton.setSelected(true);
                    isolinesToolbarButton.setSelected(true);
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
            JButton isolinesToolbarButton = getToolBarButton("Edit/Isolines");
            try {
                if(!intersectionToolbarButton.isSelected()){
                    intersectionToolbarButton.setSelected(true);
                    isolinesToolbarButton.setSelected(true);
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
                if(!interpolationToolbarButton.isSelected()){
                    interpolationToolbarButton.setSelected(true);
                    colorMapButton.setSelected(false);
                    functionViewPanel.drawInterpolateColorMap();
                    legendPanel.drawInterpolateColorMap();
                } else {
                    interpolationToolbarButton.setSelected(false);
                    legendPanel.drawColorFuncMap();
                    functionViewPanel.hideColorMap();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
