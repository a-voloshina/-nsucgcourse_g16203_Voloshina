package ru.nsu.fit.g16203.voloshina.filters;

import java.util.ArrayList;

public class SharpeningFilter extends ConvolutionFilter {

    public SharpeningFilter() {
        setConvolutionMatrix(new ArrayList<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(0.0);
                add(-1.0);
                add(0.0);
            }});
            add(new ArrayList<Double>() {{
                add(-1.0);
                add(5.0);
                add(-1.0);
            }});
            add(new ArrayList<Double>() {{
                add(0.0);
                add(-1.0);
                add(0.0);
            }});
        }});
    }

}
