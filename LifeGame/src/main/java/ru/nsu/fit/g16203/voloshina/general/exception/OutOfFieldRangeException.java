package ru.nsu.fit.g16203.voloshina.general.exception;

public class OutOfFieldRangeException extends Exception {

    private String message;

    private OutOfFieldRangeException() {
    }

    public OutOfFieldRangeException(int coordinateX, int coordinateY, int rangeX, int rangeY) {
        message = "Pair (" + coordinateY + "," + coordinateX + ") is out of field range: " + rangeY + "x" +
                +rangeX;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
