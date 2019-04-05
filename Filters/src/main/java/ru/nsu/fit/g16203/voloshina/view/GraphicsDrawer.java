package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.general.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsDrawer extends JPanel {

    protected int gridHeight = 100;
    protected int gridWidth = 350;
    protected Pair<Integer, Integer> xyMinPosition;
    protected Pair<Integer, Integer> xMaxPosition;
    protected Pair<Integer, Integer> yMaxPosition;
    private int indent = 30;
    private int textSize = 12;
    private int xyMinValue = 0;
    private int xMaxValue = 1;
    private int yMaxValue = 1;

    private BufferedImage image = new BufferedImage(450, 150, BufferedImage.TYPE_INT_ARGB);
    private Color borderColor = Color.black;

    public GraphicsDrawer() {
        setPreferredSize(new Dimension(gridWidth + indent * 2, gridHeight + indent * 2));
        setSize(new Dimension(gridWidth + indent * 2, gridHeight + indent * 2));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    private void drawBresenhamLine(int x0, int y0, int x1, int y1, Color color) {
        int diry = Integer.compare(y1, y0);
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int y = y0;
        int x = x0;
        boolean swap = false;

        if (dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
            swap = true;
        }

        int err = 0;
        int derr = 2 * dy;
        for (int i = 0; i <= dx; i++) {
            image.setRGB(x, y, color.getRGB());
            if (swap) {
                y += diry;
            } else {
                x++;
            }

            err += derr;
            if (err > dx) {
                err -= 2 * dx;
                if (swap) {
                    x++;
                } else {
                    y += diry;
                }
            }
        }
    }

    public void drawCoordinateLines() {
        xyMinPosition = new Pair<>(indent, image.getHeight() - indent);
        xMaxPosition = new Pair<>(indent + gridWidth, image.getHeight() - indent);
        yMaxPosition = new Pair<>(indent, image.getHeight() - (indent + gridHeight));
        drawBresenhamLine(xyMinPosition.getKey(), xyMinPosition.getValue(),
                xMaxPosition.getKey(), xMaxPosition.getValue(), borderColor);
        drawBresenhamLine(xyMinPosition.getKey(), xyMinPosition.getValue() + 3,
                yMaxPosition.getKey(), yMaxPosition.getValue(), borderColor);
    }

    private void printLimits(int xyMin, int xMax, int yMax) {
        Graphics2D g2d = (Graphics2D) image.getGraphics().create();
        g2d.setColor(borderColor);
        g2d.setFont(new Font("TimesNewRoman", Font.PLAIN, textSize));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(String.valueOf(xyMin), indent + fm.stringWidth(String.valueOf(xyMin)) / 2,
                image.getHeight() - indent + fm.getHeight());
        g2d.drawString(String.valueOf(xMax), indent - fm.stringWidth(String.valueOf(xMax)) / 2 + gridWidth,
                image.getHeight() - indent + fm.getHeight());
        g2d.drawString(String.valueOf(yMax), indent - fm.stringWidth(String.valueOf(yMax)) - 2,
                image.getHeight() - indent - gridHeight + fm.getHeight() / 2);
        g2d.dispose();
    }

    public void drawCoordinateGrid(int xyMin, int xMax, int yMax) {
        xyMinValue = xyMin;
        xMaxValue = xMax;
        yMaxValue = yMax;
        drawCoordinateLines();
        printLimits(xyMin, xMax, yMax);
        repaint();
    }

    protected void drawLine(int x0, int y0, int x1, int y1, Color color) {
        if (x1 < x0) {
            int tmpx = x0;
            x0 = x1;
            x1 = tmpx;

            int tmpy = y0;
            y0 = y1;
            y1 = tmpy;
        }
        drawBresenhamLine(x0, y0, x1, y1, color);
    }

    protected void drawLineInCoordinateGrid(double x0, double y0, double x1, double y1, int indentX, int indentY, Color color) {
        int xCoordinate0 = (int) (x0 * gridWidth / xMaxValue) + indentX + xyMinPosition.getKey();
        int xCoordinate1 = (int) (x1 * gridWidth / xMaxValue) + indentX + xyMinPosition.getKey();
        int yCoordinate0 = xyMinPosition.getValue() - indentY - (int) (y0 * gridHeight / yMaxValue);
        int yCoordinate1 = xyMinPosition.getValue() - indentY - (int) (y1 * gridHeight / yMaxValue);
        drawLine(xCoordinate0, yCoordinate0, xCoordinate1, yCoordinate1, color);
        repaint();
    }

    protected void clearGrid(int xyMin, int xMax, int yMax) {
        image = new BufferedImage(450, 150, BufferedImage.TYPE_INT_ARGB);
        drawCoordinateGrid(xyMin, xMax, yMax);
        repaint();
    }
}
