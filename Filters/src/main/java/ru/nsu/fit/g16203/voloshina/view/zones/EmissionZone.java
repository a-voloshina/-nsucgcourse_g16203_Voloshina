package ru.nsu.fit.g16203.voloshina.view.zones;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.view.GraphicsDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class EmissionZone extends GraphicsDrawer {

    private int indent = 2;

    public void drawEmissionGraphic(ArrayList<Pair<Integer, Color>> emissionsArray) {
        clear();
        if (emissionsArray.size() > 1) {
            Iterator<Pair<Integer, Color>> iterator = emissionsArray.iterator();
            Pair<Integer, Color> first = iterator.next();
            Pair<Integer, Color> second;
            while (iterator.hasNext()) {
                second = iterator.next();
                drawLineInCoordinateGrid(first.getKey(), first.getValue().getRed(),
                        second.getKey(), second.getValue().getRed(), 0, indent, new Color(233, 30, 99));
                drawLineInCoordinateGrid(first.getKey(), first.getValue().getGreen(),
                        second.getKey(), second.getValue().getGreen(), indent, indent * 2, new Color(99, 195, 48));
                drawLineInCoordinateGrid(first.getKey(), first.getValue().getBlue(),
                        second.getKey(), second.getValue().getBlue(), indent * 2, indent * 3, new Color(3, 177, 254));
                first = second;
            }
        }
    }

    public void clear() {
        clearGrid(0, 100, 255);
    }


}
