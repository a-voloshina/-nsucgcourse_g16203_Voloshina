package ru.nsu.fit.g16203.voloshina.model;

import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;

public class CellField extends Field<CellStatus> {

    public CellField(int width, int height) {
        super(width, height, CellStatus.DEAD);
    }

    public void addAliveCell(int coordinateX, int coordinateY) throws OutOfFieldRangeException {
        setItem(coordinateX, coordinateY, CellStatus.ALIVE);
    }

    @Override
    public void printField() {
        for (int i = 0; i < height; i++) {
            int curWidth = checkWidth(width, i);
            if (i % 2 == 1) {
                System.out.print(" ");
            }
            for (int j = 0; j < curWidth; j++) {
                System.out.print(field.get(i).get(j).value() + " ");
            }
            System.out.println();
        }
    }
}
