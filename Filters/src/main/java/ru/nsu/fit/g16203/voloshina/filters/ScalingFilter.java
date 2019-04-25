package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.image.BufferedImage;

public class ScalingFilter extends TransformationFilter {

    private int scale;

    public ScalingFilter(int scale) {
        this.scale = scale;
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int halfWidth = dst.getWidth() / 2;
        int halfHeight = dst.getHeight() / 2;
        for (int i = 0; i < dst.getHeight(); ++i) {
            for (int j = 0; j < dst.getWidth(); ++j) {
                int x2 = j - halfWidth;
                int y2 = i - halfHeight;
                double x1 = (double) x2 / scale + halfWidth;
                double y1 = (double) y2 / scale + halfHeight;
                dst.setRGB(j, i, interpolate(src, x1, y1));
            }
        }
        return dst;
    }

}
