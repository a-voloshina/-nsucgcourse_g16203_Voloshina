package ru.nsu.fit.g16203.voloshina.controller;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;
import ru.nsu.fit.g16203.voloshina.model.CellStatus;
import ru.nsu.fit.g16203.voloshina.model.CellField;
import ru.nsu.fit.g16203.voloshina.model.ImpactField;

import java.util.ArrayList;

public class Controller implements IController {

    private CellField oldCellField;
    private CellField curCellField;
    private ImpactField oldImpactField;
    private ImpactField curImpactField;

    private ArrayList<Pair<Integer, Integer>> evenLineFirstImpact = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(-1, -1));
        add(new Pair<>(-1, 0));
        add(new Pair<>(0, -1));
        add(new Pair<>(0, 1));
        add(new Pair<>(1, -1));
        add(new Pair<>(1, 0));
    }};
    private ArrayList<Pair<Integer, Integer>> oddLineFirstImpact = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(-1, 0));
        add(new Pair<>(-1, 1));
        add(new Pair<>(0, -1));
        add(new Pair<>(0, 1));
        add(new Pair<>(1, 0));
        add(new Pair<>(1, 1));
    }};
    private ArrayList<Pair<Integer, Integer>> evenLineSecondImpact = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(-2, 0));
        add(new Pair<>(-1, -2));
        add(new Pair<>(-1, 1));
        add(new Pair<>(1, -2));
        add(new Pair<>(1, 1));
        add(new Pair<>(2, 0));
    }};
    private ArrayList<Pair<Integer, Integer>> oddLineSecondImpact = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(-2, 0));
        add(new Pair<>(-1, -1));
        add(new Pair<>(-1, 2));
        add(new Pair<>(1, -1));
        add(new Pair<>(1, 2));
        add(new Pair<>(2, 0));
    }};

    private Double LIVE_BEGIN = 2.0;
    private Double LIVE_END = 3.3;
    private Double BIRTH_BEGIN = 2.3;
    private Double BIRTH_END = 2.9;
    private Double FST_IMPACT = 1.0;
    private Double SND_IMPACT = 0.3;

    public Controller(int width, int height) {
        this.oldCellField = new CellField(width, height);
        this.oldImpactField = new ImpactField(width, height);
        this.curImpactField = new ImpactField(width, height);
        this.curCellField = new CellField(width, height);
    }

    public Double getLIVE_BEGIN() {
        return LIVE_BEGIN;
    }

    public void setLIVE_BEGIN(Double LIVE_BEGIN) {
        this.LIVE_BEGIN = LIVE_BEGIN;
    }

    public Double getLIVE_END() {
        return LIVE_END;
    }

    public void setLIVE_END(Double LIVE_END) {
        this.LIVE_END = LIVE_END;
    }

    public Double getBIRTH_BEGIN() {
        return BIRTH_BEGIN;
    }

    public void setBIRTH_BEGIN(Double BIRTH_BEGIN) {
        this.BIRTH_BEGIN = BIRTH_BEGIN;
    }

    public Double getBIRTH_END() {
        return BIRTH_END;
    }

    public void setBIRTH_END(Double BIRTH_END) {
        this.BIRTH_END = BIRTH_END;
    }

    public Double getFST_IMPACT() {
        return FST_IMPACT;
    }

    public void setFST_IMPACT(Double FST_IMPACT) {
        this.FST_IMPACT = FST_IMPACT;
    }

    public Double getSND_IMPACT() {
        return SND_IMPACT;
    }

    public void setSND_IMPACT(Double SND_IMPACT) {
        this.SND_IMPACT = SND_IMPACT;
    }

    private void countImpacts() {
        curImpactField.updateField(this::countCurrentCellImpact);
    }

    private void updateField() {
        curCellField.updateField(this::updateCurCellStatus);
    }

    private CellStatus updateCurCellStatus(int coordinateX, int coordinateY) {
        CellStatus status = oldCellField.getItem(coordinateX, coordinateY);
        double impact = curImpactField.getItem(coordinateX, coordinateY);
        if ((status == CellStatus.DEAD) && (impact >= BIRTH_BEGIN)
                && (impact <= BIRTH_END)) {
            status = CellStatus.ALIVE;
        }
        if ((impact < LIVE_BEGIN) || (impact > LIVE_END)) {
            status = CellStatus.DEAD;
        }
        return status;
    }

    private double countCurrentCellImpact(int coordinateX, int coordinateY) {
        Pair<Integer, Integer> aliveNeighboursCount = new Pair<>(0, 0);
        if (coordinateY % 2 == 0) {
            getAliveNeighboursCount(aliveNeighboursCount,
                    evenLineFirstImpact, evenLineSecondImpact, coordinateX, coordinateY);
        } else {
            getAliveNeighboursCount(aliveNeighboursCount,
                    oddLineFirstImpact, oddLineSecondImpact, coordinateX, coordinateY);
        }

        return FST_IMPACT * aliveNeighboursCount.getKey() + SND_IMPACT * aliveNeighboursCount.getValue();
    }

    private void getAliveNeighboursCount(Pair<Integer, Integer> aliveNeighboursCount,
                                         ArrayList<Pair<Integer, Integer>> firstNeighboursImpact,
                                         ArrayList<Pair<Integer, Integer>> secondNeighboursImpact,
                                         int coordinateX,
                                         int coordinateY) {
        int size = firstNeighboursImpact.size();
        int firstAliveNeighboursCount = 0;
        int secondAliveNeighboursCount = 0;
        for (int i = 0; i < size; i++) {
            firstAliveNeighboursCount += isNeighbourAlive(firstNeighboursImpact, i, coordinateX, coordinateY);
            secondAliveNeighboursCount += isNeighbourAlive(secondNeighboursImpact, i, coordinateX, coordinateY);
        }
        aliveNeighboursCount.setKey(firstAliveNeighboursCount);
        aliveNeighboursCount.setValue(secondAliveNeighboursCount);
    }

    private int isNeighbourAlive(ArrayList<Pair<Integer, Integer>> neighboursImpact,
                                 int i,
                                 int coordinateX,
                                 int coordinateY) {
        int neighbourCoordinateX = neighboursImpact.get(i).getValue() + coordinateX;
        int neighbourCoordinateY = neighboursImpact.get(i).getKey() + coordinateY;
        CellStatus neighbourState = curCellField.getItem(neighbourCoordinateX, neighbourCoordinateY);
        if (neighbourState == CellStatus.ALIVE) {
            return 1;
        }
        return 0;
    }

    private void recountNeighboursImpacts(int coordinateX, int coordinateY) {
        ArrayList<Pair<Integer, Integer>> firstNeighboursImpact = coordinateY % 2 == 0 ? evenLineFirstImpact : oddLineFirstImpact;
        ArrayList<Pair<Integer, Integer>> secondNeighboursImpact = coordinateY % 2 == 0 ? evenLineSecondImpact : oddLineSecondImpact;
        int size = firstNeighboursImpact.size();
        for (int i = 0; i < size; i++) {
            int firstNeighbourCoordinateX = firstNeighboursImpact.get(i).getValue() + coordinateX;
            int firstNeighbourCoordinateY = firstNeighboursImpact.get(i).getKey() + coordinateY;
            int secondNeighbourCoordinateX = secondNeighboursImpact.get(i).getValue() + coordinateX;
            int secondNeighbourCoordinateY = secondNeighboursImpact.get(i).getKey() + coordinateY;
            try {
                curImpactField.setItem(firstNeighbourCoordinateX, firstNeighbourCoordinateY,
                        countCurrentCellImpact(firstNeighbourCoordinateX, firstNeighbourCoordinateY));
            } catch (OutOfFieldRangeException ignored) {
            }
            try {
                curImpactField.setItem(secondNeighbourCoordinateX, secondNeighbourCoordinateY,
                        countCurrentCellImpact(secondNeighbourCoordinateX, secondNeighbourCoordinateY));
            } catch (OutOfFieldRangeException ignored) {
            }
        }
    }

    public void setAliveCell(int coordinateX, int coordinateY) throws OutOfFieldRangeException {
        curCellField.addAliveCell(coordinateX, coordinateY);
        curImpactField.setItem(coordinateX, coordinateY, countCurrentCellImpact(coordinateX, coordinateY));
        countImpacts();
        curCellField.printField();
        //recountNeighboursImpacts(coordinateX, coordinateY);
    }

    @Override
    public void reverseCellState(int coordinateX, int coordinateY) throws OutOfFieldRangeException {
        curCellField.reverseCellState(coordinateX, coordinateY);
        curImpactField.setItem(coordinateX, coordinateY, countCurrentCellImpact(coordinateX, coordinateY));
        countImpacts();
        curCellField.printField();
        //recountNeighboursImpacts(coordinateX, coordinateY);
    }

    @Override
    public CellStatus getCellStatus(int coordinateX, int coordinateY) {
        return curCellField.getItem(coordinateX, coordinateY);
    }

    @Override
    public Double getCellImpact(int coordinateX, int coordinateY) {
        return curImpactField.getItem(coordinateX, coordinateY);
    }

    @Override
    public int getFieldCurWidth(int curHeight) {
        return curCellField.getCurWidth(curHeight);
    }

    @Override
    public int getFieldWidth() {
        return curCellField.getWidth();
    }

    @Override
    public int getFieldHeight() {
        return curCellField.getHeight();
    }

    public void next() {
        //oldImpactField.swap(curImpactField);
        oldCellField.swap(curCellField);
        updateField();
        countImpacts();
        curCellField.printField();
        curImpactField.printField();
        System.out.println();
    }

    public void resizeField(int newWidth, int newHeight) {
        oldImpactField.resizeField(newWidth, newHeight);
        curImpactField.resizeField(newWidth, newHeight);
        oldCellField.resizeField(newWidth, newHeight);
        curCellField.resizeField(newWidth, newHeight);
    }

    public void clearField() {
        oldCellField.clearField();
        curCellField.clearField();
        oldImpactField.clearField();
        curImpactField.clearField();
    }

    public void printField() {
        curCellField.printField();
        System.out.println();
    }

}
