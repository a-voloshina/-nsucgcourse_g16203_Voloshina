package ru.nsu.fit.g16203.voloshina.controller;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.general.exception.OutOfFieldRangeException;
import ru.nsu.fit.g16203.voloshina.model.CellStatus;
import ru.nsu.fit.g16203.voloshina.model.CellField;
import ru.nsu.fit.g16203.voloshina.model.ImpactField;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private CellField oldCellField;
    private CellField curCellField;
    private ImpactField oldImpactField;
    private ImpactField curImpactField;

    private Timer timer;
    private Step step;
    private long timePeriod = 1000;
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

    public void setStartCell(int coordinateX, int coordinateY) throws OutOfFieldRangeException {
        oldCellField.addAliveCell(coordinateX, coordinateY);
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
        CellStatus neighbourState = oldCellField.getItem(neighbourCoordinateX, neighbourCoordinateY);
        if (neighbourState == CellStatus.ALIVE) {
            return 1;
        }
        return 0;    //TODO: Integer???????????????
    }

    public void next() {
        countImpacts();
        updateField();
        curCellField.printField();
        System.out.println();
        oldCellField.swap(curCellField);
        oldImpactField.swap(curImpactField);
    }

    public void run() {
        timer = new Timer();
        step = new Step();
        timer.schedule(step, 0, timePeriod);
    }

    public void stop() {
        timer.cancel();
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

    class Step extends TimerTask {

        @Override
        public void run() {
            next();
        }
    }

}
