package ru.nsu.fit.g16203.voloshina.controller;

import ru.nsu.fit.g16203.voloshina.general.Pair;
import ru.nsu.fit.g16203.voloshina.general.Triple;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class VolumeRenderingController {
    private ArrayList<Pair<Integer, Double>> absorbtionArray;
    private ArrayList<Pair<Integer, Color>> emissionsArray;
    private ArrayList<Pair<Triple<Double>, Integer>> chargesArray;

    private double fMin;
    private double fMax;
    private int Nx;
    private int Ny;
    private int Nz;

    public VolumeRenderingController(File configFile, IAbsorbtionCreate ab, IEmissionCreate em) throws IOException {
        BufferedReader buf = new BufferedReader(new FileReader(configFile));
        absorbtionArray = configAbsorbtion(buf);
        emissionsArray = configEmission(buf);
        chargesArray = configCharges(buf);
        ab.absorbtionCreated(absorbtionArray);
        em.emissionCreated(emissionsArray);
    }

    private ArrayList<Pair<Integer, Double>> configAbsorbtion(BufferedReader buf) throws IOException {
        ArrayList<Pair<Integer, Double>> absorbtionArray = new ArrayList<>();
        String line;
        while ((line = parseComments(buf.readLine())).equals("")) {
            continue;
        }
        int absorbtionTopNumber = Integer.parseInt(line);
        for (int i = 0; i < absorbtionTopNumber; ++i) {
            if ((line = buf.readLine()) == null) {
                throw new IOException();
            }
            if (line.equals("")) {
                continue;
            }
            String[] numbers = line.split(" ");
            if (numbers.length != 2) {
                throw new IOException("Illegal file format");
            }
            absorbtionArray.add(new Pair<>(Integer.parseInt(numbers[0]), Double.parseDouble(numbers[1])));
        }
        return absorbtionArray;
    }

    private ArrayList<Pair<Integer, Color>> configEmission(BufferedReader buf) throws IOException {
        ArrayList<Pair<Integer, Color>> emissionsArray = new ArrayList<>();
        String line;
        while ((line = parseComments(buf.readLine())).equals("")) {
            continue;
        }
        int emissionTopNumber = Integer.parseInt(line);
        for (int i = 0; i < emissionTopNumber; ++i) {
            if ((line = parseComments(buf.readLine())) == null) {
                throw new IOException();
            }
            if (line.equals("")) {
                continue;
            }
            String[] numbers = line.split(" ");
            if (numbers.length != 4) {
                throw new IOException("Illegal file format");
            }
            int red = Integer.parseInt(numbers[1]);
            int green = Integer.parseInt(numbers[2]);
            int blue = Integer.parseInt(numbers[3]);
            emissionsArray.add(new Pair<>(Integer.parseInt(numbers[0]), new Color(red, green, blue)));
        }
        return emissionsArray;
    }

    private ArrayList<Pair<Triple<Double>, Integer>> configCharges(BufferedReader buf) throws IOException {
        ArrayList<Pair<Triple<Double>, Integer>> chargesArray = new ArrayList<>();
        String line;
        while ((line = parseComments(buf.readLine())).equals("")) {
            continue;
        }
        int chargesNumber = Integer.parseInt(line);
        for (int i = 0; i < chargesNumber; ++i) {
            if ((line = parseComments(buf.readLine())) == null) {
                throw new IOException();
            }
            if (line.equals("")) {
                continue;
            }
            String[] numbers = line.split(" ");
            if (numbers.length != 4) {
                throw new IOException("Illegal file format");
            }
            chargesArray.add(new Pair<>(new Triple<>(Double.parseDouble(numbers[0]), Double.parseDouble(numbers[1]),
                    Double.parseDouble(numbers[2])), Integer.parseInt(numbers[3])));
        }
        return chargesArray;
    }

    private String parseComments(String line) {
        int commentStartIndex = line.indexOf("//");
        if (commentStartIndex > 0) {
            line = line.substring(0, commentStartIndex);
        }
        return line;
    }

    public BufferedImage render(BufferedImage src, int Nx, int Ny, int Nz, boolean isAbsorbtionOn, boolean isEmissionOn) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        if (Nx > src.getWidth() * 2 / 3) {
            this.Nx = src.getWidth();
        } else {
            this.Nx = Nx;
        }
        if (Ny > src.getHeight() * 2 / 3) {
            this.Ny = src.getHeight();
        } else {
            this.Ny = Ny;
        }
        this.Nz = Nz;
        findFMinMax();
        for (int i = 0; i < Nx; ++i) {
            for (int j = 0; j < Ny; ++j) {
                double xc = getVoxelCenter(i, Nx), yc = getVoxelCenter(j, Ny);
                for (double x = i * ((double) src.getWidth() / Nx); x < (i + 1) * ((double) src.getWidth() / Nx); ++x) {
                    int curX = (int) Math.round(x);
                    if (curX < 0 || curX >= src.getWidth()) continue;
                    for (double y = j * ((double) src.getHeight() / Ny); y < (j + 1) * ((double) src.getHeight() / Ny); ++y) {
                        int curY = (int) Math.round(y);
                        if (curY < 0 || curY >= src.getWidth()) continue;
                        Color oldColor = new Color(src.getRGB(curX, curY));
                        double dz = 1 / (double) Nz;
                        double redI = oldColor.getRed();
                        double greenI = oldColor.getGreen();
                        double blueI = oldColor.getBlue();
                        Color newColor = oldColor;
                        if (isAbsorbtionOn && isEmissionOn) {
                            newColor = absorbtionAndEmission(xc, yc, dz, redI, greenI, blueI);
                        } else if (isAbsorbtionOn) {
                            newColor = absorbtionOnly(xc, yc, dz, redI, greenI, blueI);
                        } else if (isEmissionOn) {
                            newColor = emissionOnly(xc, yc, dz, redI, greenI, blueI);
                        }
                        dst.setRGB(curX, curY, newColor.getRGB());
                    }
                }
            }
        }
        return dst;
    }

    private Color absorbtionAndEmission(double xc, double yc, double dz, double redI, double greenI, double blueI) {
        int k = 0;
        while (k < Nz) {
            double curF = getF(xc, yc, getVoxelCenter(k, Nz));
            Color color = getEmissionFromFunctionValue(curF);
            redI = redI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz) + color.getRed() * dz;
            greenI = greenI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz) + color.getGreen() * dz;
            blueI = blueI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz) + color.getBlue() * dz;
            ++k;
        }
        return new Color(saturate(redI), saturate(greenI), saturate(blueI));
    }

    private Color absorbtionOnly(double xc, double yc, double dz, double redI, double greenI, double blueI) {
        int k = 0;
        while (k < Nz) {
            double curF = getF(xc, yc, getVoxelCenter(k, Nz));
            redI = redI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz);
            greenI = greenI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz);
            blueI = blueI * Math.exp(-getAbsorbtionFromFunctionValue(curF) * dz);
            ++k;
        }
        return new Color(saturate(redI), saturate(greenI), saturate(blueI));
    }

    private Color emissionOnly(double xc, double yc, double dz, double redI, double greenI, double blueI) {
        int k = 0;
        while (k < Nz) {
            double curF = getF(xc, yc, getVoxelCenter(k, Nz));
            Color color = getEmissionFromFunctionValue(curF);
            redI = redI + color.getRed() * dz;
            greenI = greenI + color.getGreen() * dz;
            blueI = blueI + color.getBlue() * dz;
            ++k;
        }
        return new Color(saturate(redI), saturate(greenI), saturate(blueI));
    }

    private int saturate(double color) {
        if (color > 255.0) {
            return 255;
        }
        if (color < 0.0) {
            return 0;
        }
        return (int) color;
    }

    private double getF(double x, double y, double z) {
        double result = 0;
        for (Pair<Triple<Double>, Integer> tripleIntegerPair : chargesArray) {
            result += tripleIntegerPair.getValue() / getDistance(x, y, z, tripleIntegerPair.getKey().getFirst(),
                    tripleIntegerPair.getKey().getSecond(), tripleIntegerPair.getKey().getThird());
        }
        return result;
    }

    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dist = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
        return dist < 0.1 ? 0.1 : dist;
    }

    private void findFMinMax() {
        fMin = getF(getVoxelCenter(0, Nx), getVoxelCenter(0, Ny), getVoxelCenter(0, Nz));
        fMax = getF(getVoxelCenter(0, Nx), getVoxelCenter(0, Ny), getVoxelCenter(0, Nz));
        for (int i = 0; i < Nx; ++i) {
            for (int j = 0; j < Ny; ++j) {
                for (int k = 0; k < Nz; ++k) {
                    double f = getF(getVoxelCenter(i, Nx), getVoxelCenter(j, Ny), getVoxelCenter(k, Nz));
                    if (f < fMin) {
                        fMin = f;
                    }
                    if (f > fMax) {
                        fMax = f;
                    }
                }
            }
        }
        int a = 0;
    }

    private double getPercentFunctionValue(double f) {
        return (f - fMin) / (fMax - fMin) * 100;
    }

    private double getVoxelCenter(int i, int n) {
        return i * (1 / (double) n) + 1 / (2 * (double) n);
    }

    private double getAbsorbtionFromFunctionValue(double f) {
        int absorbtionF = (int) Math.round(getPercentFunctionValue(f));
        int end = absorbtionArray.size() - 1;
        for (int i = 1; i < absorbtionArray.size(); ++i) {
            if (absorbtionF == absorbtionArray.get(i).getKey()) {
                return absorbtionArray.get(i).getValue();
            }
            if (absorbtionF < absorbtionArray.get(i).getKey()) {
                end = i;
                break;
            }
        }
        int start = end - 1;
        double alpha = (double) (absorbtionF - absorbtionArray.get(start).getKey()) /
                (absorbtionArray.get(end).getKey() - absorbtionArray.get(start).getKey());
        return absorbtionArray.get(start).getValue() * (1 - alpha) + absorbtionArray.get(end).getValue() * alpha;
    }

    private Color getEmissionFromFunctionValue(double f) {
        int emissionF = (int) Math.round(getPercentFunctionValue(f));

        int end = emissionsArray.size() - 1;
        for (int i = 1; i < emissionsArray.size(); ++i) {
            if (emissionF == emissionsArray.get(i).getKey()) {
                return emissionsArray.get(i).getValue();
            }
            if (emissionF < emissionsArray.get(i).getKey()) {
                end = i;
                break;
            }
        }
        int start = end - 1;
        double alpha = (double) (emissionF - emissionsArray.get(start).getKey()) /
                (emissionsArray.get(end).getKey() - emissionsArray.get(start).getKey());
        double newRed = emissionsArray.get(start).getValue().getRed() * (1 - alpha) +
                emissionsArray.get(end).getValue().getRed() * alpha;
        double newGreen = emissionsArray.get(start).getValue().getGreen() * (1 - alpha) +
                emissionsArray.get(end).getValue().getGreen() * alpha;
        double newBlue = emissionsArray.get(start).getValue().getBlue() * (1 - alpha) +
                emissionsArray.get(end).getValue().getBlue() * alpha;
        return new Color(saturate(newRed), saturate(newGreen), saturate(newBlue));
    }

    public interface IAbsorbtionCreate {
        void absorbtionCreated(ArrayList<Pair<Integer, Double>> absorbtionArray);
    }

    public interface IEmissionCreate {
        void emissionCreated(ArrayList<Pair<Integer, Color>> emissionsArray);
    }

}
