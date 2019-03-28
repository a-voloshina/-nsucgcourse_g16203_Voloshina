package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TransformationFilter extends Filter {

    protected int interpolate(BufferedImage src, double x, double y) {
        int j = (int) x;
        int i = (int) y;
        double ax = x - j;
        double ay = y - i;
        double c1Red = getColor(src, j, i).getRed() * (1 - ax) + getColor(src, j + 1, i).getRed() * ax;
        double c2Red = getColor(src, j, i + 1).getRed() * (1 - ax) + getColor(src, j + 1, i + 1).getRed() * ax;
        double crRed = c1Red * (1 - ay) + c2Red * ay;
        double c1Green = getColor(src, j, i).getGreen() * (1 - ax) + getColor(src, j + 1, i).getGreen() * ax;
        double c2Green = getColor(src, j, i + 1).getGreen() * (1 - ax) + getColor(src, j + 1, i + 1).getGreen() * ax;
        double crGreen = c1Green * (1 - ay) + c2Green * ay;
        double c1Blue = getColor(src, j, i).getBlue() * (1 - ax) + getColor(src, j + 1, i).getBlue() * ax;
        double c2Blue = getColor(src, j, i + 1).getBlue() * (1 - ax) + getColor(src, j + 1, i + 1).getBlue() * ax;
        double crBlue = c1Blue * (1 - ay) + c2Blue * ay;
        return new Color(saturate(crRed), saturate(crGreen), saturate(crBlue)).getRGB();
    }

    @Override
    protected Color getColor(BufferedImage src, int x, int y) {
        if (x < 0 || x >= src.getWidth() || y < 0 || y >= src.getHeight()) {
            return Color.white;
        }
        return new Color(src.getRGB(x, y));
    }

    public void affineTransformations(BufferedImage src, BufferedImage dst) {

    }
}
