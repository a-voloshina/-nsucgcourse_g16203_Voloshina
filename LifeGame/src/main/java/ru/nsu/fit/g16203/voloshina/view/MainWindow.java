package ru.nsu.fit.g16203.voloshina.view;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends MainFrame {

    public MainWindow() {
        super(800, 600, "FIT_16203_Voloshina_Life");
        customizeMenu();
        customizeToolbar();
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }

    private void customizeMenu() {
        Font font = new Font("TimesNewRoman", Font.PLAIN, 14);

        addSubMenu("File", font, KeyEvent.VK_F);
        addMenuItem("File/New", "Create new file", KeyEvent.VK_N,
                "new-file.png", font, null, false);
        addMenuItem("File/Open", "Open source file", KeyEvent.VK_O,
                "folder.png", font, null, false);
        addMenuItem("File/Save", "Save current state in file", KeyEvent.VK_S,
                "bookmark.png", font, null, false);
        //addMenuSeparator("File");
        //addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "exit.gif", font, null);

        addSubMenu("Edit", font, KeyEvent.VK_E);
        addSubMenu("Edit/Mode", font, KeyEvent.VK_M);
        addMenuItem("Edit/Mode/XOR", "Invert cell state", KeyEvent.VK_R,
                "xor.png", font, null, true);
        addMenuItem("Edit/Mode/Replace", "Resurrect cell", KeyEvent.VK_R,
                "replace.png", font, null, true);
        addMenuSeparator("Edit");
        addMenuItem("Edit/Clear", "Clear field", KeyEvent.VK_C,
                "close.png", font, null, false);
        addMenuItem("Edit/Settings", "Set parameters",
                KeyEvent.VK_P, "settings.png", font, null, false);

        addSubMenu("View", font, KeyEvent.VK_V);
        addMenuItem("View/Toolbar", "Show/hide toolbar", KeyEvent.VK_T,
                null, font, null, true);
        addMenuItem("View/Status Bar", "Show/hide status bar", KeyEvent.VK_B,
                null, font, null, true);
        addMenuItem("View/Impact", "Show/hide impact values",
                KeyEvent.VK_I, null, font, null, true);

        addSubMenu("Simulation", font, KeyEvent.VK_U);
        addMenuItem("Simulation/Run", "Start simulation", KeyEvent.VK_F10,
                "play.png", font, null, true);
        addMenuItem("Simulation/Next", "Make one simulation step", KeyEvent.VK_F8,
                "next.png", font, null, false);

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
    }

}
