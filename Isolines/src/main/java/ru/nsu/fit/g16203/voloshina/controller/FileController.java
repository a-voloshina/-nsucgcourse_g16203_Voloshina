package ru.nsu.fit.g16203.voloshina.controller;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FileController implements IFileController {

    private int k;
    private int m;
    private int n;
    private ArrayList<Color> colorsList;
    private Color isolinesColor;

    @Override
    public void parseFile(File configFile) throws IOException {
        BufferedReader buf = new BufferedReader(new FileReader(configFile));
        getKM(buf);
        getN(buf);
        getColors(buf);
        getIsolinesColor(buf);
    }

    private void getKM(BufferedReader buf) throws IOException {
        String line = buf.readLine();
        System.out.println(line);
        String[] splitLine = line.split(" ");
        k = Integer.parseInt(splitLine[0]);
        m = Integer.parseInt(splitLine[1]);
    }

    private void getN(BufferedReader buf) throws IOException {
        n = getInt(buf);
    }

    private void getColors(BufferedReader buf) throws IOException {
        String line;
        colorsList = new ArrayList<>();
        for (int i = 0; i <= n; ++i) {
            if ((line = parseComments(buf.readLine())) == null) {
                throw new IOException();
            }
            if (line.equals("")) {
                continue;
            }
            System.out.println(line);
            String[] colors = line.split(" ");
            if (colors.length < 3) {
                throw new IOException("Illegal file format");
            }
            int red = Integer.parseInt(colors[0]);
            int green = Integer.parseInt(colors[1]);
            int blue = Integer.parseInt(colors[2]);
            colorsList.add(new Color(red, green, blue));
        }
    }

    private void getIsolinesColor(BufferedReader buf) throws IOException {
        String line = parseComments(buf.readLine());
        System.out.println(line);
        String[] colors = line.split(" ");
        if (colors.length < 3) {
            throw new IOException("Illegal file format");
        }
        int red = Integer.parseInt(colors[0]);
        int green = Integer.parseInt(colors[1]);
        int blue = Integer.parseInt(colors[2]);
        isolinesColor = new Color(red, green, blue);
    }

    private int getInt(BufferedReader buf) throws IOException {
        String line = buf.readLine();
        System.out.println(line);
        int spacesStartIndex = line.indexOf(" ");
        if (spacesStartIndex > 0) {
            line = line.substring(0, spacesStartIndex);
        }
        return Integer.parseInt(parseComments(line));
    }

    private String parseComments(String line) {
        int commentStartIndex = line.indexOf("//");
        if (commentStartIndex > 0) {
            line = line.substring(0, commentStartIndex);
        }
        return line;
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public ArrayList<Color> getColorsList() {
        return colorsList;
    }

    public Color getIsolinesColor() {
        return isolinesColor;
    }
}
