package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.controller.IController;
import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;
import ru.nsu.fit.g16203.voloshina.model.CellStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

public class FieldView extends JPanel {

    private int cellSize = 30;
    private double r = countInnerHexagonRadius();
    private int fieldHeight;
    private int fieldWidth;
    private int gridWidth = 1;

    private int indent = 10;

    private int pixelFieldWidth;
    private int pixelFieldHeight;
    private BufferedImage image;
    private BufferedImage impactsImage;

    private Color backgroundColor = new Color(0, 0, 0, 0);
    private Color borderColor = new Color(11, 5, 37, 255);
    private Color hexagonAliveColor = new Color(40, 138, 204, 255);//(63,81,181);//(5,166,5);
    private Color hexagonDeadColor = new Color(187, 222, 251, 255);//(255,240,197);
    private Color textColor = Color.BLACK;

    private Timer timer;
    private Step step;
    private int timePeriod = 1000;

    private boolean isXORModeOn = false;
    private boolean isImpactsShown = false;
    private Pair<Integer, Integer> curHexagon = new Pair<>(-1, -1);

    private int rowsMax = 100;
    private int columnsMax = 100;
    private int cellSizeMax = 50;
    private int gridWidthMax = 10;
    private int timerMax = 10000;

    private IController controller;

    public FieldView(IController iController) {

        controller = iController;

        fieldHeight = controller.getFieldHeight();
        fieldWidth = controller.getFieldWidth();
        pixelFieldWidth = countPixelFieldWidth();
        pixelFieldHeight = countPixelFieldHeight();
        image = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);
        impactsImage = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                hexagonUpdate(x, y, false);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                hexagonUpdate(x, y, true);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        printField();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.drawImage(impactsImage, 0, 0, impactsImage.getWidth(), impactsImage.getHeight(), null);
    }

    private double countInnerHexagonRadius() {
        return Math.sqrt(3) * ((double) cellSize) / 2;
    }

    private int countPixelFieldWidth() {
        return (int) Math.round(2 * r * fieldWidth) + indent * 2;
    }

    private int countPixelFieldHeight() {
        return (int) Math.round(cellSize * (0.5 + 1.5 * fieldHeight)) + indent * 2;
    }

    private void hexagonUpdate(int x, int y, boolean isDragged) {
        x = x - indent;
        y = y - indent;
        if (x < 0 || x > pixelFieldWidth - indent * 2 || y < 0 || y > pixelFieldHeight - indent * 2) {
            return;
        }
        if (image.getRGB(x, y) == borderColor.getRGB() || image.getRGB(x, y) == backgroundColor.getRGB()) {
            return;
        }
        Pair<Integer, Integer> position = getHexagonPosition(x, y);
        if (position.getKey() != null && position.getValue() != null
                && (position.getKey() >= 0 && position.getValue() >= 0)) {
            int oldColor;
            int newColor;
            try {
                if (isXORModeOn) {
                    if (isDragged && position.getKey().equals(curHexagon.getKey()) && position.getValue().equals(curHexagon.getValue())) {
                        return;
                    }
                    curHexagon = position;
                    CellStatus cellStatus = controller.getCellStatus(position.getValue(), position.getKey());
                    Pair<Integer, Integer> hexagonColors = getFillHexagonColors(cellStatus);
                    oldColor = hexagonColors.getKey();
                    newColor = hexagonColors.getValue();
                    controller.reverseCellState(position.getValue(), position.getKey());
                } else {
                    controller.setAliveCell(position.getValue(), position.getKey());
                    oldColor = hexagonDeadColor.getRGB();
                    newColor = hexagonAliveColor.getRGB();
                }
            } catch (OutOfFieldRangeException ex) {
                return;
            }
            Pair<Double, Double> hexagonCenter = countHexagonCenter(position.getValue(), position.getKey());
            fillHexagon((int) Math.round(hexagonCenter.getKey()), (int) Math.round(hexagonCenter.getValue()),
                    oldColor, newColor);
            if (isImpactsShown) {
                repaintImpacts();
            } else {
                repaint();
            }
        }
    }

    private Pair<Integer, Integer> getFillHexagonColors(CellStatus cellStatus) {
        if (cellStatus == CellStatus.ALIVE) {
            return new Pair<>(hexagonAliveColor.getRGB(), hexagonDeadColor.getRGB());
        } else {
            return new Pair<>(hexagonDeadColor.getRGB(), hexagonAliveColor.getRGB());
        }
    }

    private void drawBresenhamLine(int x0, int y0, int x1, int y1) {
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

    private void drawLine(Graphics2D g, int x0, int y0, int x1, int y1) {
        if (gridWidth == 1) {
            drawBresenhamLine(x0, y0, x1, y1);
        } else {
            g.setStroke(new BasicStroke(gridWidth));
            g.drawLine(x0, y0, x1, y1);
        }
    }

    private void drawHexagon(Graphics2D g, double centerX, double centerY) {
        int x0 = (int) Math.round(centerX);
        int y0 = (int) Math.round(centerY - cellSize);
        int x1 = (int) Math.round(centerX + r);
        int y1 = (int) Math.round(centerY - (float) cellSize / 2);
        drawLine(g, x0, y0, x1, y1);
        int y2 = (int) Math.round(centerY + (float) cellSize / 2);
        drawLine(g, x1, y1, x1, y2);
        int y3 = (int) Math.round(centerY + cellSize);
        drawLine(g, x1, y2, x0, y3);
        int x4 = (int) Math.round(centerX - r);
        drawLine(g, x0, y3, x4, y2);
        drawLine(g, x4, y2, x4, y1);
        drawLine(g, x4, y1, x0, y0);
    }

    private Pair<Double, Double> countHexagonCenter(int x, int y) {
        double centerX = (y % 2 == 0) ? (2 * x + 1) * r : 2 * (x + 1) * r;
        double centerY = cellSize * (1 + 1.5 * y);
        return new Pair<>(centerX + indent, centerY + indent);
    }

    private void printField() {
        Graphics2D g = (Graphics2D) image.getGraphics().create();
        g.setColor(borderColor);
        for (int i = 0; i < fieldHeight; i++) {
            int end = (i % 2 == 0) ? fieldWidth : fieldWidth - 1;
            for (int j = 0; j < end; j++) {
                Pair<Double, Double> hexagonCenter = countHexagonCenter(j, i);
                int centerX = (int) Math.round(hexagonCenter.getKey());
                int centerY = (int) Math.round(hexagonCenter.getValue());
                drawHexagon(g, hexagonCenter.getKey(), hexagonCenter.getValue());
                fillHexagon(centerX, centerY, image.getRGB(centerX, centerY), hexagonDeadColor.getRGB());
            }
        }
        g.dispose();
    }

    private void printImpacts(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < fieldHeight; i++) {
            int end = controller.getFieldCurWidth(i);
            for (int j = 0; j < end; j++) {
                Pair<Double, Double> hexagonCenter = countHexagonCenter(j, i);
                int centerX = (int) Math.round(hexagonCenter.getKey());
                int centerY = (int) Math.round(hexagonCenter.getValue());
                String curTextImpact;
                double curImpact = controller.getCellImpact(j, i);
                if ((int) ((curImpact % 1) * 10) != 0) {
                    curTextImpact = String.valueOf(Math.round(curImpact * 10) / 10.0);
                } else {
                    curTextImpact = String.valueOf((int) curImpact);
                }
                int x = centerX - fm.stringWidth(curTextImpact) / 2;
                int y = centerY + cellSize / 2 - fm.getHeight() / 4;
                g2d.drawString(curTextImpact, x, y);
            }
        }
    }

    private void fillHexagon(int startX, int startY, int curColor, int fillColor) {
        if (curColor == fillColor) return;
        ArrayDeque<Pair<Integer, Integer>> spanStack = new ArrayDeque<>();
        spanStack.push(new Pair<>(startX, startY));
        spanFill(spanStack, curColor, fillColor);
    }

    private void makeHexagonAlive(int centerX, int centerY) {
        fillHexagon(centerX, centerY, image.getRGB(centerX, centerY), hexagonAliveColor.getRGB());
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

    private void makeHexagonDead(int centerX, int centerY) {
        fillHexagon(centerX, centerY, image.getRGB(centerX, centerY), hexagonDeadColor.getRGB());
    }

    private void updateFieldContent() {
        for (int i = 0; i < fieldHeight; i++) {
            int end = controller.getFieldCurWidth(i);
            for (int j = 0; j < end; j++) {
                CellStatus cellStatus = controller.getCellStatus(j, i);
                Pair<Double, Double> hexagonCenter = countHexagonCenter(j, i);
                int centerX = (int) Math.round(hexagonCenter.getKey());
                int centerY = (int) Math.round(hexagonCenter.getValue());
                if (cellStatus == CellStatus.ALIVE) {
                    makeHexagonAlive(centerX, centerY);
                } else if (cellStatus == CellStatus.DEAD) {
                    makeHexagonDead(centerX, centerY);
                }

            }
        }
    }

    private void updateFieldCondition() {
        image = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);
        printField();
        updateFieldContent();
    }

    private int calculateFontSize() {
        return cellSize * 3 / 4;
    }

    private void repaintImpacts() {
        impactsImage = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);
        showImpacts();
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        if (cellSize <= cellSizeMax) {
            this.cellSize = cellSize;
            r = countInnerHexagonRadius();
            pixelFieldWidth = countPixelFieldWidth();
            pixelFieldHeight = countPixelFieldHeight();
            updateFieldCondition();
            if (isImpactsShown) {
                repaintImpacts();
            } else {
                repaint();
            }
        }
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        if (gridWidth <= gridWidthMax) {
            this.gridWidth = gridWidth;
            pixelFieldWidth = countPixelFieldWidth();
            pixelFieldHeight = countPixelFieldHeight();
            updateFieldCondition();
            if (isImpactsShown) {
                repaintImpacts();
            } else {
                repaint();
            }
        }
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        if (timePeriod < timerMax) {
            this.timePeriod = timePeriod;
        }
    }

    public boolean isXORModeOn() {
        return isXORModeOn;
    }

    public void turnXORModeOn() {
        isXORModeOn = true;
    }

    public void turnXORModeOff() {
        isXORModeOn = false;
    }

    public void simulationStep() {
        controller.next();
        updateFieldContent();
        if (isImpactsShown) {
            repaintImpacts();
        } else {
            repaint();
        }
    }

    public void runSimulation() {
        timer = new Timer();
        step = new Step();
        timer.schedule(step, 0, timePeriod);
    }

    public void stopSimulation() {
        timer.cancel();
    }

    public void clearField() {
        controller.clearField();
        updateFieldContent();
        if (isImpactsShown) {
            repaintImpacts();
        } else {
            repaint();
        }
    }

    public void resizeField(int newWidth, int newHeight) {
        if (newWidth != fieldWidth || newHeight != fieldHeight) {
            if (newWidth != fieldWidth && newWidth <= columnsMax) {
                fieldWidth = newWidth;
                pixelFieldWidth = countPixelFieldWidth();
            }
            if (newHeight != fieldHeight && newHeight <= rowsMax) {
                fieldHeight = newHeight;
                pixelFieldHeight = countPixelFieldHeight();
            }
            controller.resizeField(newWidth, newHeight);
            updateFieldCondition();
            if (isImpactsShown) {
                repaintImpacts();
            } else {
                repaint();
            }
        }
    }

    public void showImpacts() {
        isImpactsShown = true;
        Graphics2D g2d = (Graphics2D) impactsImage.getGraphics().create();
        g2d.setColor(textColor);
        g2d.setFont(new Font("TimesNewRoman", Font.PLAIN, calculateFontSize()));
        printImpacts(g2d);
        g2d.dispose();
        repaint();
    }

    public void hideImpacts() {
        isImpactsShown = false;
        impactsImage = new BufferedImage(pixelFieldWidth, pixelFieldHeight, BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public void updateField() {
        updateFieldCondition();
        if (isImpactsShown) {
            repaintImpacts();
        } else {
            repaint();
        }
    }

    class Step extends TimerTask {
        @Override
        public void run() {
            simulationStep();
        }
    }

}
