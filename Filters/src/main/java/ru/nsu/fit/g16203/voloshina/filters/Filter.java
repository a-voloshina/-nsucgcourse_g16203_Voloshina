package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class Filter {

    public BufferedImage apply(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < src.getHeight(); ++i) {
            for (int j = 0; j < src.getWidth(); ++j) {
                dst.setRGB(j, i, getNewColor(src, j, i).getRGB());
            }
        }
        return dst;
    }

    protected int saturate(int color) {
        if (color > 255) {
            return 255;
        }
        if (color < 0) {
            return 0;
        }
        return color;
    }

    protected int saturate(double color) {
        if (color > 255.0) {
            return 255;
        }
        if (color < 0.0) {
            return 0;
        }
        return (int) color;
    }

    protected BufferedImage cloneImage(BufferedImage src) {
        ColorModel colorModel = src.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = src.copyData(src.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    protected Color getColor(BufferedImage src, int x, int y) {
        int j = x;
        int i = y;
        if (x < 0) {
            j = 0;
        } else if (x >= src.getWidth()) {
            j = src.getWidth() - 1;
        }
        if (y < 0) {
            i = 0;
        } else if (y >= src.getHeight()) {
            i = src.getHeight() - 1;
        }
        return new Color(src.getRGB(j, i));
    }

    protected Color getNewColor(BufferedImage src, int x, int y) {
        return new Color(src.getRGB(x, y));
    }

}
