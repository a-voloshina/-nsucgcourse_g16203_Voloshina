package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MedianFilter extends Filter {

    private int matrixSize;

    public MedianFilter(int matrixSize) {
        this.matrixSize = matrixSize;
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        ArrayList<Color> neighboursList = new ArrayList<>();
        int n = (matrixSize - 1) / 2;
        for (int i = -n; i <= n; ++i) {
            for (int j = -n; j <= n; ++j) {
                neighboursList.add(getColor(src, x + j, y + i));
            }
        }
        neighboursList.sort((firstColor, secondColor) -> {
            int firstColorValue = (int) Math.round(0.299 * firstColor.getRed() +
                    0.585 * firstColor.getGreen() + 0.114 * firstColor.getBlue());
            int secondColorValue = (int) Math.round(0.299 * secondColor.getRed() +
                    0.585 * secondColor.getGreen() + 0.114 * secondColor.getBlue());
            return Integer.compare(firstColorValue, secondColorValue);
        });
        return neighboursList.get((matrixSize * matrixSize - 1) / 2);
    }
}
