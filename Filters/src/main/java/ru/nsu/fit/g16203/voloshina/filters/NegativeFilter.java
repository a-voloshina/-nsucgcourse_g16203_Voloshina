package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NegativeFilter extends Filter {
    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < src.getHeight(); ++i) {
            for (int j = 0; j < src.getWidth(); ++j) {
                Color color = new Color(src.getRGB(j, i));
                dst.setRGB(j, i, new Color(255 - color.getRed(),
                        255 - color.getGreen(), 255 - color.getBlue()).getRGB());
            }
        }
        return dst;
    }
}
