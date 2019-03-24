package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlackWhiteFilter extends Filter {
    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < src.getHeight(); ++i) {
            for (int j = 0; j < src.getWidth(); ++j) {
                Color oldColor = new Color(src.getRGB(j, i));
                int colorValue = (int) Math.round(0.299 * oldColor.getRed() + 0.585 * oldColor.getGreen() + 0.114 * oldColor.getBlue());
                int newColor = new Color(colorValue, colorValue, colorValue).getRGB();
                dst.setRGB(j, i, newColor);
            }
        }
        return dst;
    }
}
