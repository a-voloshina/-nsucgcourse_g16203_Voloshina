package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OrderedDitheringFilter extends DitheringFilter {

    private int matrixSize;
    private ArrayList<ArrayList<Double>> ditherMatrix;
//    private int rRed = 256 / (1 << redN - 1);
//    private int rGreen = 256 / (1 << greenN - 1);
//    private int rBlue = 256 / (1 << blueN - 1);

    private int rRed = 256 / redN;
    private int rGreen = 256 / greenN;
    private int rBlue = 256 / blueN;

    public OrderedDitheringFilter(int n) {
        matrixSize = n;
        int M = getTwoDegreeOf(matrixSize);
        ditherMatrix = new ArrayList<>(matrixSize);
        for (int i = 0; i < matrixSize; ++i) {
            ArrayList<Double> inner = new ArrayList<>();
            for (int j = 0; j < matrixSize; j++) {
                int v = 0, mask = M - 1, xc = j ^ i, yc = i;
                for (int bit = 0; bit < 2 * M; --mask) {
                    v |= ((yc >> mask) & 1) << bit++;
                    v |= ((xc >> mask) & 1) << bit++;
                }
                inner.add(j, (double) v / matrixSize / matrixSize);
            }
            ditherMatrix.add(inner);
        }
    }

    private int getTwoDegreeOf(int num) {
        int degree = 0;
        while (num != 1 && num % 2 == 0) {
            num /= 2;
            ++degree;
        }
        return degree;
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color oldColor = new Color(src.getRGB(x, y));
//        int newRed = nearestPaletteColor((int) (oldColor.getRed() +
//                rRed * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), redN);
//        int newGreen = nearestPaletteColor((int) (oldColor.getGreen() +
//                rGreen * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), greenN);
//        int newBlue = nearestPaletteColor((int) (oldColor.getBlue() +
//                rBlue * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), blueN);
        int newRed = nearestPaletteColor((int) (oldColor.getRed() +
                rRed * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), rRed);
        int newGreen = nearestPaletteColor((int) (oldColor.getGreen() +
                rGreen * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), rGreen);
        int newBlue = nearestPaletteColor((int) (oldColor.getBlue() +
                rBlue * (ditherMatrix.get(y % matrixSize).get(x % matrixSize) - 0.5)), rBlue);
        return new Color(saturate(newRed), saturate(newGreen), saturate(newBlue));
    }
}
