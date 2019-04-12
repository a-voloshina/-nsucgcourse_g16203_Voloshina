package ru.nsu.fit.g16203.voloshina.controller;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.model.Function;
import ru.nsu.fit.g16203.voloshina.view.FunctionViewPanel;

import java.awt.*;
import java.util.ArrayList;

public class Controller {

    private double funMin;
    private double funMax;
    private double a;
    private double b;
    private double c;
    private double d;
    private int n = 3;
    private ArrayList<Color> colorsList = new ArrayList<Color>() {{
        add(new Color(255, 0, 0));
        add(new Color(0, 255, 0));
        add(new Color(0, 0, 255));
        add(new Color(255, 255, 0));
    }};
    private ArrayList<Double> levelsList;
    private int k;
    private int m;
    private int gridXSize;
    private int gridYSize;
    private int fieldWidth;
    private int fieldHeight;

    private Function function;

    private double eps = 0.0005;

    private boolean isIntersectionRectPointModeOn = false;
    private boolean isIntersectionTrianPointModeOn = false;

    public Controller(double a, double b, double c, double d, int k, int m, int width, int height, Function function) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.k = k;
        this.m = m;
        this.fieldWidth = width;
        this.fieldHeight = height;
        gridXSize = fieldWidth / k;
        gridYSize = fieldHeight / m;
        this.function = function;

        countFunctionMinMax();
        initializeLevelsList();
    }

    private void countFunctionMinMax() {
        double min = function.getFunctionValue(getXFromU(0), getYFromV(0));
        double max = min;
        for (int i = 0; i <= k; ++i) {
            for (int j = 0; j <= m; ++j) {
                double curValue = function.getFunctionValue(getXFromU(i * gridXSize), getYFromV(j * gridYSize));
                if (curValue > max) {
                    max = curValue;
                }
                if (curValue < min) {
                    min = curValue;
                }
            }
        }
        funMin = min;
        funMax = max;
    }

    private void initializeLevelsList() {
        levelsList = new ArrayList<>();
        double step = (funMax - funMin) / (n + 1);
        for (int i = 1; i <= n; i++) {
            levelsList.add(i * step + funMin);
        }
    }

    private Color getAppropriateColor(double z) {
        for (int i = 0; i < n; ++i) {
            if (z < levelsList.get(i)) {
                return colorsList.get(i);
            }
        }
        return colorsList.get(n);
    }

    private int getUFromX(double x) {
        return (int) (fieldWidth * (x - a) / (b - a) + 0.5);
    }

    private int getVFromY(double y) {
        return (int) (fieldHeight * (y - c) / (d - c) + 0.5);
    }

    private double getXFromU(int u) {
        return (b - a) * u / fieldWidth + a;
    }

    private double getYFromV(int v) {
        return (d - c) * v / fieldHeight + c;
    }

    private double getInterpolateFuncValue(int u, int v) {
        double x = getXFromU(u);
        double y = getYFromV(v);
        int gridCellXNumber = u / gridXSize;
        int gridCellYNumber = v / gridYSize;
        double x13 = getXFromU(gridXSize * gridCellXNumber);
        double x24 = getXFromU(gridXSize * (gridCellXNumber + 1));
        double y12 = getYFromV(gridYSize * gridCellYNumber);
        double y34 = getYFromV(gridYSize * (gridCellYNumber + 1));
        double f1 = function.getFunctionValue(x13, y12);
        double f2 = function.getFunctionValue(x24, y12);
        double f3 = function.getFunctionValue(x13, y34);
        double f4 = function.getFunctionValue(x24, y34);
        return bilinearlInterpolation(f1, f2, f3, f4, x13, x24, y12, y34, x, y);
    }

    private double bilinearlInterpolation(double f1, double f2,
                                          double f3, double f4,
                                          double x13, double x24,
                                          double y12, double y34,
                                          double x, double y){
        double alphaX = (x - x13) / (x24 - x13);
        double f34 = alphaX * f4 + (1 - alphaX) * f3;
        double f12 = alphaX * f2 + (1 - alphaX) * f1;
        double alphaY = (y - y12) / (y34 - y12);
        return alphaY * f34 + (1 - alphaY) * f12;
    }

    private Pair<Double, Double> getIntersectionCoords(double f1, double f2, double z, double x1, double x2, double y1, double y2) {
        if (z == f1 || z == f2) {
            z += eps;
        }
        if ((z > f1 && z < f2) || (z < f1 && z > f2)) {
            double xStart = x1 < x2 ? x1 : x2;
            double dx = Math.abs(x2 - x1);
            double alphaX = Math.abs(z - f1) / Math.abs(f2 - f1);
            alphaX = x2 < x1 ? 1 - alphaX : alphaX;
            double x = xStart + dx * alphaX;

            double yStart = y1 < y2 ? y1 : y2;
            double dy = Math.abs(y2 - y1);
            double alphaY = y2 < y1 ? 1 - alphaX : alphaX;
            double y = yStart + dy * alphaY;

            return new Pair<>(x, y);
        } else {
            return null;
        }
    }


    protected int saturate(double color) {
        if (color > 255.0) {
            return 255;
        }
        if (color < 0.0) {
            return 0;
        }
        return (int) color;
    }

    public Color getPixelColor(int u, int v) {
        return getAppropriateColor(getInterpolateFuncValue(u, v));
    }

    public void drawIsolines(FunctionViewPanel.LinePainter painter,
                             FunctionViewPanel.PointPainter pointPainter) throws Exception {
        for (int l = 0; l < n; ++l) {
            double z = levelsList.get(l);
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < k; ++j) {
                    double x13 = getXFromU(gridXSize * j);
                    double x24 = getXFromU(gridXSize * (j + 1));
                    double y12 = getYFromV(gridYSize * i);
                    double y34 = getYFromV(gridYSize * (i + 1));
                    double f1 = function.getFunctionValue(x13, y12);
                    double f2 = function.getFunctionValue(x24, y12);
                    double f3 = function.getFunctionValue(x13, y34);
                    double f4 = function.getFunctionValue(x24, y34);
                    double xCenter = getXFromU(gridXSize * j + gridXSize / 2);
                    double yCenter = getYFromV(gridYSize * i + gridYSize / 2);
                    double fCenter = (f1 + f2 + f3 + f4) / 4;

                    int intersectionCount = 0;

                    Pair<Double, Double> top = getIntersectionCoords(f1, f2, z, x13, x24, y12, y12);
                    Pair<Double, Double> bottom = getIntersectionCoords(f3, f4, z, x13, x24, y34, y34);
                    Pair<Double, Double> left = getIntersectionCoords(f1, f3, z, x13, x13, y12, y34);
                    Pair<Double, Double> right = getIntersectionCoords(f2, f4, z, x24, x24, y12, y34);

                    Pair<Double, Double> topLeft = getIntersectionCoords(f1, fCenter, z, x13, xCenter, y12, yCenter);
                    Pair<Double, Double> topRight = getIntersectionCoords(fCenter, f2, z, xCenter, x24, yCenter, y12);
                    Pair<Double, Double> bottomLeft = getIntersectionCoords(f3, fCenter, z, x13, xCenter, y34, yCenter);
                    Pair<Double, Double> bottomRight = getIntersectionCoords(fCenter, f4, z, xCenter, x24, yCenter, y34);

                    if (top != null) {
                        ++intersectionCount;
                    }
                    if (bottom != null) {
                        ++intersectionCount;
                    }
                    if (left != null) {
                        ++intersectionCount;
                    }
                    if (right != null) {
                        ++intersectionCount;
                    }

                    if (intersectionCount == 0) {
                        continue;
                    }
                    if (intersectionCount == 2 || intersectionCount == 4) {
                        if (topLeft != null) {
                            if (top != null) {
                                painter.drawLine(getUFromX(topLeft.getKey()), getVFromY(topLeft.getValue()),
                                        getUFromX(top.getKey()), getVFromY(top.getValue()));
                            }
                            if (left != null) {
                                painter.drawLine(getUFromX(topLeft.getKey()), getVFromY(topLeft.getValue()),
                                        getUFromX(left.getKey()), getVFromY(left.getValue()));
                            }
                            if (bottomLeft != null) {
                                painter.drawLine(getUFromX(topLeft.getKey()), getVFromY(topLeft.getValue()),
                                        getUFromX(bottomLeft.getKey()), getVFromY(bottomLeft.getValue()));
                            }
                            if (topRight != null) {
                                painter.drawLine(getUFromX(topLeft.getKey()), getVFromY(topLeft.getValue()),
                                        getUFromX(topRight.getKey()), getVFromY(topRight.getValue()));
                            }
                        }
                        if (topRight != null) {
                            if (top != null) {
                                painter.drawLine(getUFromX(topRight.getKey()), getVFromY(topRight.getValue()),
                                        getUFromX(top.getKey()), getVFromY(top.getValue()));
                            }
                            if (right != null) {
                                painter.drawLine(getUFromX(topRight.getKey()), getVFromY(topRight.getValue()),
                                        getUFromX(right.getKey()), getVFromY(right.getValue()));
                            }
                            if (bottomRight != null) {
                                painter.drawLine(getUFromX(topRight.getKey()), getVFromY(topRight.getValue()),
                                        getUFromX(bottomRight.getKey()), getVFromY(bottomRight.getValue()));
                            }
                        }
                        if (bottomLeft != null) {
                            if (left != null) {
                                painter.drawLine(getUFromX(bottomLeft.getKey()), getVFromY(bottomLeft.getValue()),
                                        getUFromX(left.getKey()), getVFromY(left.getValue()));
                            }
                            if (bottom != null) {
                                painter.drawLine(getUFromX(bottomLeft.getKey()), getVFromY(bottomLeft.getValue()),
                                        getUFromX(bottom.getKey()), getVFromY(bottom.getValue()));
                            }
                            if (bottomRight != null) {
                                painter.drawLine(getUFromX(bottomLeft.getKey()), getVFromY(bottomLeft.getValue()),
                                        getUFromX(bottomRight.getKey()), getVFromY(bottomRight.getValue()));
                            }
                        }
                        if (bottomRight != null) {
                            if (right != null) {
                                painter.drawLine(getUFromX(bottomRight.getKey()), getVFromY(bottomRight.getValue()),
                                        getUFromX(right.getKey()), getVFromY(right.getValue()));
                            }
                            if (bottom != null) {
                                painter.drawLine(getUFromX(bottomRight.getKey()), getVFromY(bottomRight.getValue()),
                                        getUFromX(bottom.getKey()), getVFromY(bottom.getValue()));
                            }
                        }
                    } else {
                        throw new Exception();
                    }

                    if (isIntersectionRectPointModeOn) {
                        if (top != null) {
                            pointPainter.drawPoint(getUFromX(top.getKey()), getVFromY(top.getValue()), Color.red);
                        }
                        if (bottom != null) {
                            pointPainter.drawPoint(getUFromX(bottom.getKey()), getVFromY(bottom.getValue()), Color.red);
                        }
                        if (left != null) {
                            pointPainter.drawPoint(getUFromX(left.getKey()), getVFromY(left.getValue()), Color.red);
                        }
                        if (right != null) {
                            pointPainter.drawPoint(getUFromX(right.getKey()), getVFromY(right.getValue()), Color.red);
                        }
                    }

                    if (isIntersectionTrianPointModeOn) {
                        if (topLeft != null) {
                            pointPainter.drawPoint(getUFromX(topLeft.getKey()), getVFromY(topLeft.getValue()), Color.green);
                        }
                        if (topRight != null) {
                            pointPainter.drawPoint(getUFromX(topRight.getKey()), getVFromY(topRight.getValue()), Color.green);
                        }
                        if (bottomLeft != null) {
                            pointPainter.drawPoint(getUFromX(bottomLeft.getKey()), getVFromY(bottomLeft.getValue()), Color.green);
                        }
                        if (bottomRight != null) {
                            pointPainter.drawPoint(getUFromX(bottomRight.getKey()), getVFromY(bottomRight.getValue()), Color.green);
                        }
                    }
                }
            }
        }
    }

    public Color getInterpolatedPixelColor(int u, int v) {
        double x = getXFromU(u);
        double y = getYFromV(v);
        int gridCellXNumber = u / gridXSize;
        int gridCellYNumber = v / gridYSize;
        double x13 = getXFromU(gridXSize * gridCellXNumber);
        double x24 = getXFromU(gridXSize * (gridCellXNumber + 1));
        double y12 = getYFromV(gridYSize * gridCellYNumber);
        double y34 = getYFromV(gridYSize * (gridCellYNumber + 1));
        double f1 = function.getFunctionValue(x13, y12);
        double f2 = function.getFunctionValue(x24, y12);
        double f3 = function.getFunctionValue(x13, y34);
        double f4 = function.getFunctionValue(x24, y34);
        Color color1 = getAppropriateColor(f1);
        Color color2 = getAppropriateColor(f2);
        Color color3 = getAppropriateColor(f3);
        Color color4 = getAppropriateColor(f4);
        int newRed = saturate(bilinearlInterpolation(color1.getRed(), color2.getRed(), color3.getRed(),
                color4.getRed(), x13, x24, y12, y34, x, y));
        int newGreen = saturate(bilinearlInterpolation(color1.getGreen(), color2.getGreen(), color3.getGreen(),
                color4.getGreen(), x13, x24, y12, y34, x, y));
        int newBlue = saturate(bilinearlInterpolation(color1.getBlue(), color2.getBlue(), color3.getBlue(),
                color4.getBlue(), x13, x24, y12, y34, x, y));
        return new Color(newRed, newGreen, newBlue);
    }

    public int getGridXSize() {
        return gridXSize;
    }

    public int getGridYSize() {
        return gridYSize;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public void setDomain(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        countFunctionMinMax();
        initializeLevelsList();
    }

    public void setGridParams(int k, int m) {
        this.k = k;
        this.m = m;
        countFunctionMinMax();
        initializeLevelsList();
    }

    public double getFunMin() {
        return funMin;
    }

    public double getFunMax() {
        return funMax;
    }

    public void setIntersectionRectPointModeOn(boolean intersectionRectPointModeOn) {
        isIntersectionRectPointModeOn = intersectionRectPointModeOn;
    }

    public void setIntersectionTrianPointModeOn(boolean intersectionTrianPointModeOn) {
        isIntersectionTrianPointModeOn = intersectionTrianPointModeOn;
    }
}
