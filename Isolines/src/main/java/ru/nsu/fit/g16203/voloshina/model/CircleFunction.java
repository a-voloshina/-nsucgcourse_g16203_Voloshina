package ru.nsu.fit.g16203.voloshina.model;

public class CircleFunction implements Function {

    @Override
    public double getFunctionValue(double x, double y) {
        return (Math.pow(x, 2) + Math.pow(y, 2));
    }
}
