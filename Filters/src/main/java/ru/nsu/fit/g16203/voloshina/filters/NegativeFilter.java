package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NegativeFilter extends Filter {
    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color color = new Color(src.getRGB(x, y));
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }
}
