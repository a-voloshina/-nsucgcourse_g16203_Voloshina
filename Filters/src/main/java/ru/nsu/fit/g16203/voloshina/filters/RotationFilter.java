package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.image.BufferedImage;

public class RotationFilter extends TransformationFilter {

    private int angle;

    public RotationFilter(int angle) {
        this.angle = angle;
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int halfWidth = dst.getWidth() / 2;
        int halfHeight = dst.getHeight() / 2;
        double sin = Math.sin(angle * Math.PI / 180);
        double cos = Math.cos(angle * Math.PI / 180);
        for (int i = 0; i < dst.getHeight(); ++i) {
            for (int j = 0; j < dst.getWidth(); ++j) {
                int x2 = j - halfWidth;
                int y2 = i - halfHeight;
                double x1 = cos * x2 + sin * y2 + halfWidth;
                double y1 = -sin * x2 + cos * y2 + halfHeight;
                dst.setRGB(j, i, interpolate(src, x1, y1));
            }
        }
        return dst;
    }
}
