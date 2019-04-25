package ru.nsu.fit.g16203.voloshina.filters;

import java.util.ArrayList;

public class BlurFilter extends ConvolutionFilter {

    public BlurFilter() {
        setConvolutionMatrix(new ArrayList<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(1.0 / 16);
                add(2.0 / 16);
                add(1.0 / 16);
            }});
            add(new ArrayList<Double>() {{
                add(2.0 / 16);
                add(4.0 / 16);
                add(2.0 / 16);
            }});
            add(new ArrayList<Double>() {{
                add(1.0 / 16);
                add(2.0 / 16);
                add(1.0 / 16);
            }});
        }});
    }
}
