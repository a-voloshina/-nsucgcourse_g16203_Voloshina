package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StampingFilter extends ConvolutionFilter {

    public StampingFilter() {
        setConvolutionMatrix(new ArrayList<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(0.0);
                add(-1.0);
                add(0.0);
            }});
            add(new ArrayList<Double>() {{
                add(1.0);
                add(0.0);
                add(-1.0);
            }});
            add(new ArrayList<Double>() {{
                add(0.0);
                add(1.0);
                add(0.0);
            }});
        }});
    }

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
        return new Color(saturate(newRedColor + 128), saturate(newGreenColor + 128), saturate(newBlueColor + 128));
    }
}
