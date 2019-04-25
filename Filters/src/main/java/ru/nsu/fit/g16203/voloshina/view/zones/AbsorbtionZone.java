package ru.nsu.fit.g16203.voloshina.view.zones;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.view.GraphicsDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class AbsorbtionZone extends GraphicsDrawer {

    private int indent = 2;

    public void drawAbsorbtionGraphic(ArrayList<Pair<Integer, Double>> absorbtionArray) {
        clear();
        if (absorbtionArray.size() > 1) {
            Iterator<Pair<Integer, Double>> iterator = absorbtionArray.iterator();
            Pair<Integer, Double> first = iterator.next();
            Pair<Integer, Double> second;
            while (iterator.hasNext()) {
                second = iterator.next();
                drawLineInCoordinateGrid(first.getKey(), first.getValue(), second.getKey(), second.getValue(),
                        0, indent, Color.black);
                first = second;
            }
        }
    }

    public void clear() {
        clearGrid(0, 100, 1);
    }

}
