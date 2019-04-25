package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WatercolorFilter extends MedianFilter {
    public WatercolorFilter(int matrixSize) {
        super(matrixSize);
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < src.getHeight(); ++i) {
            for (int j = 0; j < src.getWidth(); ++j) {
                Color color = super.getNewColor(src, j, i);
                dst.setRGB(j, i, color.getRGB());
            }
        }
        BufferedImage dstClone = cloneImage(dst);
        SharpeningFilter sharpeningFilter = new SharpeningFilter();
        for (int i = 0; i < dst.getHeight(); ++i) {
            for (int j = 0; j < dst.getWidth(); ++j) {
                dst.setRGB(j, i, sharpeningFilter.getNewColor(dstClone, j, i).getRGB());
            }
        }
        return dst;
    }
}
