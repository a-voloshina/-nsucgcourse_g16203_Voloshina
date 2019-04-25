package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RobertsFilter extends EdgesFilter {

    public RobertsFilter(int threshold) {
        super(threshold);
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color colorIJ = getColor(src, x, y);
        Color colorI1J1 = getColor(src, x + 1, y + 1);
        Color colorI1J = getColor(src, x, y + 1);
        Color colorIJ1 = getColor(src, x + 1, y);
        int fRed = Math.abs(colorIJ.getRed() - colorI1J1.getRed()) + Math.abs(colorIJ1.getRed() - colorI1J.getRed());
        int fGreen = Math.abs(colorIJ.getGreen() - colorI1J1.getGreen()) + Math.abs(colorIJ1.getGreen() - colorI1J.getGreen());
        int fBlue = Math.abs(colorIJ.getBlue() - colorI1J1.getBlue()) + Math.abs(colorIJ1.getBlue() - colorI1J.getBlue());

        return new Color(binarize(fRed), binarize(fGreen), binarize(fBlue));
    }
}
