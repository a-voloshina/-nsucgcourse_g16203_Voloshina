package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ConvolutionFilter extends Filter {

    private ArrayList<ArrayList<Double>> convolutionMatrix;

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        double newRedColor = 0;
        double newGreenColor = 0;
        double newBlueColor = 0;
        int n = (convolutionMatrix.size() - 1) / 2;
        for (int u = -n; u <= n; ++u) {
            for (int v = -n; v <= n; ++v) {
                newRedColor += convolutionMatrix.get(v).get(u) * (getColor(src, x + u, y + v).getRed());
                newGreenColor += convolutionMatrix.get(v).get(u) * (getColor(src, x + u, y + v).getGreen());
                newBlueColor += convolutionMatrix.get(v).get(u) * (getColor(src, x + u, y + v).getBlue());
            }
        }
        return new Color(saturate(newRedColor), saturate(newGreenColor), saturate(newBlueColor));
    }

    public void setConvolutionMatrix(ArrayList<ArrayList<Double>> convolutionMatrix) {
        this.convolutionMatrix = convolutionMatrix;
    }
}
