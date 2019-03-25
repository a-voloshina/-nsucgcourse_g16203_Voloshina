package ru.nsu.fit.g16203.voloshina.filters;

public abstract class DitheringFilter extends Filter {

    protected int redN = 1;
    protected int greenN = 1;
    protected int blueN = 1;

    protected int nearestPaletteColor(int color, int bitPerColor) {
        double step = (256 / (double) ((1 << bitPerColor) - 1));
        return saturate((int) Math.round(Math.round(color / step) * step));
    }

}
