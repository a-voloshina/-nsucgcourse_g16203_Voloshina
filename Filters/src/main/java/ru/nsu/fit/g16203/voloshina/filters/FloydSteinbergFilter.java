package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FloydSteinbergFilter extends DitheringFilter {
    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage srcClone = cloneImage(src);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int width = srcClone.getWidth();
        int height = srcClone.getHeight();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Color oldColor = new Color(srcClone.getRGB(j, i));
                int nearestRedColor = nearestPaletteColor(oldColor.getRed(), redN);
                int errorR = oldColor.getRed() - nearestRedColor;

                int nearestGreenColor = nearestPaletteColor(oldColor.getGreen(), greenN);
                int errorG = oldColor.getGreen() - nearestGreenColor;

                int nearestBlueColor = nearestPaletteColor(oldColor.getBlue(), blueN);
                int errorB = oldColor.getGreen() - nearestBlueColor;
                Color newColor = new Color(nearestRedColor, nearestGreenColor, nearestBlueColor);
                dst.setRGB(j, i, newColor.getRGB());

                if (j + 1 < width) {
                    Color color = new Color(srcClone.getRGB(j + 1, i));
                    Color colorIJ1 = new Color(
                            saturate((int) Math.round(color.getRed() + (7 / (double) 16) * errorR)),
                            saturate((int) Math.round(color.getGreen() + (7 / (double) 16) * errorG)),
                            saturate((int) Math.round(color.getBlue() + (7 / (double) 16) * errorB)));
                    srcClone.setRGB(j + 1, i, colorIJ1.getRGB());
                    if (i + 1 < height) {
                        color = new Color(srcClone.getRGB(j + 1, i + 1));
                        Color colorI1J1 = new Color(
                                saturate((int) Math.round(color.getRed() + (1 / (double) 16) * errorR)),
                                saturate((int) Math.round(color.getGreen() + (1 / (double) 16) * errorG)),
                                saturate((int) Math.round(color.getBlue() + (1 / (double) 16) * errorG)));
                        srcClone.setRGB(j + 1, i + 1, colorI1J1.getRGB());
                    }
                }
                if (i + 1 < height) {
                    Color color = new Color(srcClone.getRGB(j, i + 1));
                    Color colorI1J = new Color(
                            saturate((int) Math.round(color.getRed() + (5 / (double) 16) * errorR)),
                            saturate((int) Math.round(color.getGreen() + (5 / (double) 16) * errorG)),
                            saturate((int) Math.round(color.getBlue() + (5 / (double) 16) * errorB))
                    );
                    srcClone.setRGB(j, i + 1, colorI1J.getRGB());
                    if (j - 1 > 0) {
                        color = new Color(srcClone.getRGB(j - 1, i + 1));
                        Color colorI1J1 = new Color(
                                saturate((int) Math.round(color.getRed() + (3 / (double) 16) * errorR)),
                                saturate((int) Math.round(color.getGreen() + (3 / (double) 16) * errorG)),
                                saturate((int) Math.round(color.getBlue() + (3 / (double) 16) * errorB))
                        );
                        srcClone.setRGB(j - 1, i + 1, colorI1J1.getRGB());
                    }
                }
            }
        }
        return dst;
    }
}
