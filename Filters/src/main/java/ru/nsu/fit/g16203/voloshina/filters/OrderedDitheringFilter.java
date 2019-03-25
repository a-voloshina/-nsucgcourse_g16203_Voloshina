package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OrderedDitheringFilter extends DitheringFilter {

    private int matrixSize;
    private ArrayList<ArrayList<Double>> ditherMatrix;

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

        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                System.out.print(ditherMatrix.get(i).get(j) + " ");
            }
            System.out.println();
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
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int rRed = 256 / (1 << redN - 1);
        int rGreen = 256 / (1 << greenN - 1);
        int rBlue = 256 / (1 << blueN - 1);
        for (int i = 0; i < src.getHeight(); ++i) {
            for (int j = 0; j < src.getWidth(); ++j) {
                Color oldColor = new Color(src.getRGB(j, i));
                int newRed = nearestPaletteColor((int) (oldColor.getRed() +
                        rRed * (ditherMatrix.get(i % matrixSize).get(j % matrixSize) - 0.5)), redN);
                int newGreen = nearestPaletteColor((int) (oldColor.getGreen() +
                        rGreen * (ditherMatrix.get(i % matrixSize).get(j % matrixSize) - 0.5)), greenN);
                int newBlue = nearestPaletteColor((int) (oldColor.getBlue() +
                        rBlue * (ditherMatrix.get(i % matrixSize).get(j % matrixSize) - 0.5)), blueN);
                dst.setRGB(j, i, new Color(saturate(newRed), saturate(newGreen), saturate(newBlue)).getRGB());
            }
        }
        return dst;
    }
}
