package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ConvolutionFilter extends Filter {

    protected ArrayList<ArrayList<Double>> convolutionMatrix;

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        double newRedColor = 0;
        double newGreenColor = 0;
        double newBlueColor = 0;
        int n = (convolutionMatrix.size() - 1) / 2;
        for (int u = -n; u <= n; ++u) {
            for (int v = -n; v <= n; ++v) {
                Color color = getColor(src, x + u, y + v);
                double coef = convolutionMatrix.get(v + n).get(u + n);
                newRedColor += coef * (color.getRed());
                newGreenColor += coef * (color.getGreen());
                newBlueColor += coef * (color.getBlue());
            }
        }
        return new Color(saturate(newRedColor), saturate(newGreenColor), saturate(newBlueColor));
    }

    public void setConvolutionMatrix(ArrayList<ArrayList<Double>> convolutionMatrix) {
        this.convolutionMatrix = convolutionMatrix;
    }
}
