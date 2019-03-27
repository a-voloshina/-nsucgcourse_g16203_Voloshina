package ru.nsu.fit.g16203.voloshina.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SobelFilter extends EdgesFilter {

    ArrayList<ArrayList<Double>> convolutionXMatrix;

    ArrayList<ArrayList<Double>> convolutionYMatrix;

    public SobelFilter(int threshold) {
        super(threshold);
        convolutionXMatrix = new ArrayList<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(-1.0);
                add(-2.0);
                add(-1.0);
            }});
            add(new ArrayList<Double>() {{
                add(0.0);
                add(0.0);
                add(0.0);
            }});
            add(new ArrayList<Double>() {{
                add(1.0);
                add(2.0);
                add(1.0);
            }});
        }};

        convolutionYMatrix = new ArrayList<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(-1.0);
                add(0.0);
                add(1.0);
            }});
            add(new ArrayList<Double>() {{
                add(-2.0);
                add(0.0);
                add(2.0);
            }});
            add(new ArrayList<Double>() {{
                add(-1.0);
                add(0.0);
                add(1.0);
            }});
        }};
    }

    @Override
    protected Color getNewColor(BufferedImage src, int x, int y) {
        double sXRedColor = 0;
        double sXGreenColor = 0;
        double sXBlueColor = 0;
        double sYRedColor = 0;
        double sYGreenColor = 0;
        double sYBlueColor = 0;
        int n = (convolutionXMatrix.size() - 1) / 2;
        for (int u = -n; u <= n; ++u) {
            for (int v = -n; v <= n; ++v) {
                double coefX = convolutionXMatrix.get(v + n).get(u + n);
                double coefY = convolutionYMatrix.get(v + n).get(u + n);
                Color color = getColor(src, x + u, y + v);
                sXRedColor += coefX * color.getRed();
                sXGreenColor += coefX * color.getGreen();
                sXBlueColor += coefX * color.getBlue();
                sYRedColor += coefY * color.getRed();
                sYGreenColor += coefY * color.getGreen();
                sYBlueColor += coefY * color.getBlue();
            }
        }

        return new Color(binarize(saturate(Math.abs(sXRedColor) + Math.abs(sYRedColor))),
                binarize(saturate(Math.abs(sXGreenColor) + Math.abs(sYGreenColor))),
                binarize(saturate(Math.abs(sXBlueColor) + Math.abs(sYBlueColor))));
    }
}
