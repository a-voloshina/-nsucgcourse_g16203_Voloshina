package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EdgesFilter extends ConvolutionFilter {

    protected int threshold;

    public EdgesFilter(int c) {
        threshold = c;
    }

    public int getThreshold() {
        return threshold;
    }

    protected int binarize(int color) {
        if (color > threshold) {
            color = 255;
        } else {
            color = 0;
        }
        return color;
    }

    private boolean majorize(int redEdge, int greenEdge, int blueEdge) {
        return (redEdge == 255 && greenEdge == 255) ||
                (redEdge == 255 && blueEdge == 255) ||
                (greenEdge == 255 && blueEdge == 255);
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        Color color = super.getNewColor(src, x, y);
//        int redEdge = binarize(color.getRed());
//        int greenEdge = binarize(color.getGreen());
//        int blueEdge = binarize(color.getBlue());
//        if(majorize(redEdge, greenEdge, blueEdge)){
//            return new Color(255, 255, 255);
//        } else {
//            return new Color(0, 0, 0);
//        }
        return new Color(binarize(color.getRed()), binarize(color.getGreen()), binarize(color.getBlue()));
    }
}
