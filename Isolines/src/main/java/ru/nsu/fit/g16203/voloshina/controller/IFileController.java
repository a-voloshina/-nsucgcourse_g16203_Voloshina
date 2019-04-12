package ru.nsu.fit.g16203.voloshina.controller;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface IFileController {
    void parseFile(File configFile) throws IOException;

    int getK();

    int getM();

    int getN();

    ArrayList<Color> getColorsList();

    Color getIsolinesColor();
}
