package ru.nsu.fit.g16203.voloshina.model;

public class HyberbolaFunction implements Function {
    @Override
    public double getFunctionValue(double x, double y) {
        return Math.sin(x*y/5) + Math.cos(x*y/5);
    }
}
