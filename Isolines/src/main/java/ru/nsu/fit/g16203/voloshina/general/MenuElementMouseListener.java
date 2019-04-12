package ru.nsu.fit.g16203.voloshina.general;

import ru.nsu.fit.external.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuElementMouseListener extends MouseAdapter {

    private String menuPath;
    private StatusBar statusBar;
    private MainFrame mainFrame;

    public MenuElementMouseListener(String menuPath, StatusBar statusBar, MainFrame mainFrame) {
        this.menuPath = menuPath;
        this.statusBar = statusBar;
        this.mainFrame = mainFrame;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        statusBar.setText(mainFrame.getMenuItem(menuPath).getToolTipText());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        statusBar.setDefaultTooltip();
    }
}
