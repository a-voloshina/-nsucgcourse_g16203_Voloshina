package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GammaCorrectionFilter extends Filter {

    private double gamma;

    public GammaCorrectionFilter(double gamma) {
        this.gamma = gamma;
    }

    private int correct(int color) {
        double func = Math.pow((double) color / 255, gamma);
        return saturate(func * 255);
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color color = getColor(src, x, y);
        return new Color(correct(color.getRed()), correct(color.getGreen()), correct(color.getBlue()));
    }
}
