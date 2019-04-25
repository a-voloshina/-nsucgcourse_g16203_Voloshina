package ru.nsu.fit.g16203.voloshina.model;

public enum CellStatus {
    ALIVE("*"),
    DEAD("0");

    private final String value;

    CellStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
