package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

public abstract class Filter {

    public abstract BufferedImage apply(BufferedImage src);

    public int saturate(int color) {
        if (color > 255) {
            return 255;
        }
        if (color < 0) {
            return 0;
        }
        return color;
    }

    public int saturate(double color) {
        if (color > 255) {
            return 255;
        }
        if (color < 0) {
            return 0;
        }
        return (int) color;
    }

    public BufferedImage cloneImage(BufferedImage src) {
        ColorModel colorModel = src.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = src.copyData(src.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
        //.getSubimage(0, 0, src.getWidth(), src.getHeight());
    }

}
