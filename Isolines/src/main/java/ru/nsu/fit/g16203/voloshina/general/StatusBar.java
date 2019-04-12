package ru.nsu.fit.g16203.voloshina.general;

import javax.swing.*;
import java.awt.*;

public class StatusBar {
    private JPanel statusPanel;
    private JLabel statusBar;

    private String defaultTooltip ="Ready";

    public StatusBar(Font font, int parentWidth) {
        statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(parentWidth, 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusBar = new JLabel(defaultTooltip);
        statusBar.setFont(font);
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusBar);
    }

    public void setText(String text){
        statusBar.setText(text);
    }

    public void setDefaultTooltip() {
        statusBar.setText(defaultTooltip);
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }
}
