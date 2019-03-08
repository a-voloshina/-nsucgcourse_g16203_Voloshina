package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.controller.IController;
import ru.nsu.fit.g16203.voloshina.general.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

public class FieldView extends JPanel {

    private int cellSize = 30;
    private double r = Math.sqrt(3) * ((double) cellSize) / 2;
    private int fieldHeight;
    private int fieldWidth;

    private int indent = 10;

    private int pixelFieldWidth = (int) Math.round(2 * r * 10) + 1;
    private int pixelFieldHeight = (int) Math.round(cellSize * (0.5 + 1.5 * 10)) + 1;
    private BufferedImage image = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);

    private Color backgroundColor = new Color(0, 0, 0, 0);
    private Color borderColor = new Color(11, 5, 37, 255);
    private Color hexagonAliveColor = new Color(40, 138, 204, 255);//(63,81,181);//(5,166,5);
    private Color hexagonDeadColor = new Color(187, 222, 251, 255);//(255,240,197);

    public FieldView(int n, int m, IController controller) {

        fieldHeight = n;
        fieldWidth = m;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() - indent;
                int y = e.getY() - indent;
                //System.out.println("Pressed pixel: (" + x + "," + y + ")");
                if (x < 0 || x > pixelFieldWidth || y < 0 || y > pixelFieldHeight) {
                    return;
                }
                if (image.getRGB(x, y) == borderColor.getRGB() || image.getRGB(x, y) == backgroundColor.getRGB()) {
                    return;
                }
                Pair<Integer, Integer> position = getHexagonPosition(x, y);
                if (position.getKey() != null && position.getValue() != null
                        && (position.getKey() >= 0 && position.getValue() >= 0)) {
                    //System.out.println("Hexagon position: " + position.getKey() + "," + position.getValue());
                    Pair<Double, Double> hexagonCenter = countHexagonCenter(position.getValue(), position.getKey());
                    fillHexagon((int) Math.round(hexagonCenter.getKey()), (int) Math.round(hexagonCenter.getValue()),
                            hexagonDeadColor.getRGB(), hexagonAliveColor.getRGB());
                    repaint();
                }
            }
        });

        //image.getGraphics().setColor(new Color(0, 0, 0, 0));
        //image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
        printField();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, indent, indent, pixelFieldWidth, pixelFieldHeight, null);
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    private void drawLine(int x0, int y0, int x1, int y1) {
        if (x1 < x0) {
            int tmpx = x0;
            x0 = x1;
            x1 = tmpx;

            int tmpy = y0;
            y0 = y1;
            y1 = tmpy;
        }
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
            image.setRGB(x, y, borderColor.getRGB());
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

    private void drawHexagon(double centerX, double centerY) {
        int x0 = (int) Math.round(centerX);
        int y0 = (int) Math.round(centerY - cellSize);
        int x1 = (int) Math.round(centerX + r);
        int y1 = (int) Math.round(centerY - (float) cellSize / 2);
        drawLine(x0, y0, x1, y1);
        int y2 = (int) Math.round(centerY + (float) cellSize / 2);
        drawLine(x1, y1, x1, y2);
        int y3 = (int) Math.round(centerY + cellSize);
        drawLine(x1, y2, x0, y3);
        int x4 = (int) Math.round(centerX - r);
        drawLine(x0, y3, x4, y2);
        drawLine(x4, y2, x4, y1);
        drawLine(x4, y1, x0, y0);
    }

    private Pair<Double, Double> countHexagonCenter(int x, int y) {
        double centerX = (y % 2 == 0) ? (2 * x + 1) * r : 2 * (x + 1) * r;
        double centerY = cellSize * (1 + 1.5 * y);
        return new Pair<>(centerX, centerY);
    }

    public void printField() {
        for (int i = 0; i < fieldHeight; i++) {
            int end = (i % 2 == 0) ? fieldWidth : fieldWidth - 1;
            for (int j = 0; j < end; j++) {
                Pair<Double, Double> hexagonCenter = countHexagonCenter(j, i);
                int centerX = (int) Math.round(hexagonCenter.getKey());
                int centerY = (int) Math.round(hexagonCenter.getValue());
                drawHexagon(hexagonCenter.getKey(), hexagonCenter.getValue());
                fillHexagon(centerX, centerY, image.getRGB(centerX, centerY), hexagonDeadColor.getRGB());
            }
        }
    }

    private void fillHexagon(int startX, int startY, int curColor, int fillColor) {
        ArrayDeque<Pair<Integer, Integer>> spanStack = new ArrayDeque<>();
        spanStack.push(new Pair<>(startX, startY));
        spanFill(spanStack, curColor, fillColor);
    }

    private void spanFill(ArrayDeque<Pair<Integer, Integer>> spanStack, int oldColor, int fillColor) {
        while (!spanStack.isEmpty()) {
            Pair<Integer, Integer> curSpan = spanStack.pop();
            int curSpanX = curSpan.getKey();
            int curSpanY = curSpan.getValue();
            while (image.getRGB(curSpanX + 1, curSpanY) == oldColor) {
                if (curSpanX < 0 || curSpanX > pixelFieldWidth || curSpanY < 0 || curSpanY > pixelFieldHeight) {
                    return;
                }
                curSpanX++;
            }
            boolean findUpSpan = false;
            boolean findDownSpan = false;
            while (image.getRGB(curSpanX, curSpanY) == oldColor) {
                if (curSpanX < 0 || curSpanX > pixelFieldWidth || curSpanY < 0 || curSpanY > pixelFieldHeight) {
                    return;
                }
                if (image.getRGB(curSpanX, curSpanY - 1) == oldColor && !findUpSpan) {
                    spanStack.add(new Pair<>(curSpanX, curSpanY - 1));
                    findUpSpan = true;
                }
                if (image.getRGB(curSpanX, curSpanY - 1) != oldColor && findUpSpan) {
                    findUpSpan = false;
                }
                if (image.getRGB(curSpanX, curSpanY + 1) == oldColor && !findDownSpan) {
                    spanStack.add(new Pair<>(curSpanX, curSpanY + 1));
                    findDownSpan = true;
                }
                if (image.getRGB(curSpanX, curSpanY + 1) != oldColor && findDownSpan) {
                    findDownSpan = false;
                }
                image.setRGB(curSpanX, curSpanY, fillColor);
                curSpanX--;
            }
        }
    }

    private Pair<Integer, Integer> getHexagonPosition(int x, int y) {
        int sectionY = y / cellSize;
        int sectionX = (int) (x / r);
        int modX = (int) (x - sectionX * r);
        int modY = (y - sectionY * cellSize);
        System.out.println(sectionX + " " + sectionY);
        System.out.println(modX + " " + modY);
        double kx = cellSize * modX / (r * 2);
        switch (sectionY % 3) {
            case 2:
                return new Pair<>(sectionY * 2 / 3, (sectionX - 1) / 2);    //checked
            case 1:
                if (sectionX % 2 == 0) {
                    int lineY = (int) (kx + cellSize / 2);
                    if (modY > lineY) {
                        return new Pair<>((sectionY * 2 + 1) / 3, sectionX / 2 - 1);
                    } else {
                        return new Pair<>(sectionY * 2 / 3, sectionX / 2);
                    }
                } else {
                    int lineY = (int) (-kx + cellSize);
                    if (modY > lineY) {
                        return new Pair<>((sectionY * 2 + 1) / 3, sectionX / 2);
                    } else {
                        return new Pair<>(sectionY * 2 / 3, sectionX / 2);
                    }
                }
            case 0:
                if (sectionX % 2 == 0) {
                    int lineY = (int) (-kx + cellSize / 2);
                    if (modY > lineY) {
                        return new Pair<>(sectionY * 2 / 3, sectionX / 2);
                    } else {
                        return new Pair<>(sectionY * 2 / 3 - 1, sectionX / 2 - 1);
                    }
                } else {
                    int lineY = (int) (kx);
                    if (modY > lineY) {
                        return new Pair<>(sectionY * 2 / 3, sectionX / 2);
                    } else {
                        return new Pair<>(sectionY * 2 / 3 - 1, sectionX / 2);
                    }
                }
            default:
                return new Pair<>(null, null);
        }
    }

    public void simulationStep() {

    }

}
