package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FunctionViewPanel extends JPanel {
    private BufferedImage functionMapImage;
    private BufferedImage gridImage;
    private BufferedImage isolinesImage;
    private BufferedImage pointsImage;
    private Controller controller;

    private Color borderColor = Color.black;
    private Color isolinesColor = Color.white;

    public interface LinePainter {
        void drawLine(int x0, int y0, int x1, int y1);
    }

    public interface PointPainter {
        void drawPoint(int x, int y, Color fillColor);
    }

    public FunctionViewPanel(Controller controller, int width, int height) {
        this.controller = controller;
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createDashedBorder(Color.black, 4, 2));
        functionMapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        isolinesImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pointsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(functionMapImage, 0, 0, functionMapImage.getWidth(), functionMapImage.getHeight(), null);
        g.drawImage(gridImage, 0, 0, gridImage.getWidth(), gridImage.getHeight(), null);
        g.drawImage(isolinesImage, 0, 0, isolinesImage.getWidth(), isolinesImage.getHeight(), null);
        g.drawImage(pointsImage, 0, 0, pointsImage.getWidth(), pointsImage.getHeight(), null);
    }

    private void drawBresenhamLine(int x0, int y0, int x1, int y1, Color color, BufferedImage image) {
        int diry = Integer.compare(y1, y0);
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int y = y0;
        int x = x0;
        boolean swap = false;

        if (dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
            swap = true;
        }

        int err = 0;
        int derr = 2 * dy;
        for (int i = 0; i <= dx; i++) {
            if(x < image.getWidth() && y < image.getHeight() && x >= 0 && y >= 0){
                image.setRGB(x, y, color.getRGB());
            }
            if (swap) {
                y += diry;
            } else {
                x++;
            }

            err += derr;
            if (err > dx) {
                err -= 2 * dx;
                if (swap) {
                    x++;
                } else {
                    y += diry;
                }
            }
        }
    }

    private void drawLine(int x0, int y0, int x1, int y1, Color color, BufferedImage image) {
        if (x1 < x0) {
            int tmpx = x0;
            x0 = x1;
            x1 = tmpx;

            int tmpy = y0;
            y0 = y1;
            y1 = tmpy;
        }
        drawBresenhamLine(x0, y0, x1, y1, color, image);
    }

    private void drawIsoine(int x0, int y0, int x1, int y1) {
        drawLine(x0, y0, x1, y1, isolinesColor, isolinesImage);
    }

    private void drawGridLine(int x0, int y0, int x1, int y1) {
        drawLine(x0, y0, x1, y1, borderColor, gridImage);
    }

    private void drawPoint(int x, int y, Color color){
        Graphics2D g2d = pointsImage.createGraphics();
        int radius = 7;
        g2d.setPaint(color);
        g2d.fillOval(x - radius/2, y - radius/2, radius, radius);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x - radius/2, y - radius/2, radius, radius);
        g2d.dispose();
    }

    public void drawColorFuncMap(){
        functionMapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < getHeight(); ++i){
            for(int j = 0; j < getWidth(); ++j){
                functionMapImage.setRGB(j, i, controller.getPixelColor(j, i).getRGB());
            }
        }
        System.out.println("draw color map");
        repaint();
    }

    public void drawGrid(){
        gridImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        controller.drawGrid(this::drawGridLine);
        repaint();
    }

    public void drawInterpolateColorMap(){
        functionMapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < getHeight(); ++i){
            for(int j = 0; j < getWidth(); ++j){
                functionMapImage.setRGB(j, i, controller.getInterpolatedPixelColor(j, i).getRGB());
            }
        }
        System.out.println("draw interpolated color map");
        repaint();
    }

    public void drawIsolines() throws Exception {
        isolinesImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        pointsImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        controller.drawIsolines(this::drawIsoine, this::drawPoint);
        System.out.println("draw isolines");
        repaint();
    }

    public void hideColorMap(){
        functionMapImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public void hideGrid(){
        gridImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public void hideIsolines(){
        isolinesImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public void setIntersectionRectPointModeOn(boolean intersectionRectPointModeOn) {
        controller.setIntersectionRectPointModeOn(intersectionRectPointModeOn);
    }

    public void setIntersectionTrianPointModeOn(boolean intersectionTrianPointModeOn) {
        controller.setIntersectionTrianPointModeOn(intersectionTrianPointModeOn);
    }

    public void setIsolinesModeOn(boolean isolinesModeOn) {
        controller.setIsolinesModeOn(isolinesModeOn);
    }

}
