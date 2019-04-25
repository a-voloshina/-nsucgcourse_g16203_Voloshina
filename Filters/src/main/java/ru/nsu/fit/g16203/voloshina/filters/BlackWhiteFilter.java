package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlackWhiteFilter extends Filter {
    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color oldColor = new Color(src.getRGB(x, y));
        int colorValue = (int) Math.round(0.299 * oldColor.getRed() + 0.585 * oldColor.getGreen() + 0.114 * oldColor.getBlue());
        return new Color(colorValue, colorValue, colorValue);
    }
}
