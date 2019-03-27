package ru.nsu.fit.g16203.voloshina.filters;

public class EdgesFilter extends ConvolutionFilter {

    protected int threshold;

    public EdgesFilter(int c) {
        threshold = c;
    }

    protected int binarize(int color) {
        if (color > threshold) {
            color = 255;
        } else {
            color = 0;
        }
        return color;
    }

}
