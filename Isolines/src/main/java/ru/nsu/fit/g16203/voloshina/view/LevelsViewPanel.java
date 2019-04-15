package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class LevelsViewPanel extends JPanel {
    private BufferedImage levelsImage;
    private Color levelsColor = Color.black;
    private Font font;

    public LevelsViewPanel(int width, int height, Font font) {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        this.font = font;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(levelsImage, 0, 0, levelsImage.getWidth(), levelsImage.getHeight(), null);
    }

    public void printLevels(ArrayList<Double> levelsList) {
        levelsImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        int n = levelsList.size();
        int step = getHeight() / (n + 1);
        Graphics2D g2d = levelsImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(levelsColor);
        FontMetrics fm = g2d.getFontMetrics();
        int curHeight = 0;
        for (Double aDouble : levelsList) {
            curHeight += step;
            curHeight = curHeight > getHeight() ? getHeight() - fm.getHeight() : curHeight;
            double level = new BigDecimal(aDouble).setScale(2, RoundingMode.UP).doubleValue();
            g2d.drawString(String.valueOf(level), 0, curHeight);
        }
        g2d.dispose();
        repaint();
    }
}
