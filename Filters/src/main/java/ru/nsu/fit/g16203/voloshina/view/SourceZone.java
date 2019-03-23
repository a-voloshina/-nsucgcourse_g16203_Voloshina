package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.general.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SourceZone extends JPanel {

    private BufferedImage image;
    private BufferedImage rectImage;
    private File imageFile;
    private int panelWidth = 350;
    private int panelHeight = 350;

    private Pair<Integer, Integer> curXorRectCenter = new Pair<>(-1, -1);
    private int xorRectWidth = 50;
    private int xorRectHeight = 50;

    public SourceZone() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setSize(new Dimension(panelWidth, panelHeight));
        setBorder(BorderFactory.createDashedBorder(Color.black, 4, 2));

        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        rectImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (imageFile != null) {
                    int x = e.getX() - 1;
                    int y = e.getY() - 1;
                    setImage();
                    int nextXorRectX;
                    int nextXorRectY;
                    if (x < xorRectWidth / 2) {
                        nextXorRectX = 0;
                    } else if (x >= rectImage.getWidth() - 2 - xorRectWidth / 2) {
                        nextXorRectX = rectImage.getWidth() - xorRectWidth - 1;
                    } else {
                        nextXorRectX = x - xorRectWidth / 2;
                    }
                    if (y < xorRectHeight / 2) {
                        nextXorRectY = 0;
                    } else if (y >= rectImage.getHeight() - 2 - xorRectHeight / 2) {
                        nextXorRectY = rectImage.getHeight() - xorRectHeight - 1;
                    } else {
                        nextXorRectY = y - xorRectHeight / 2;
                    }
                    System.out.println("Rect:");
                    System.out.println("A = " + nextXorRectX + "," + nextXorRectY);
                    System.out.println("B = " + (nextXorRectX+xorRectWidth) + "," + nextXorRectY);
                    System.out.println("C = " + (nextXorRectX+xorRectWidth) + "," + (nextXorRectY+xorRectHeight));
                    System.out.println("D = " + nextXorRectX + "," + (nextXorRectY+xorRectHeight));
                    drawXorRect(nextXorRectX, nextXorRectY, xorRectWidth, xorRectHeight);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rectImage, 1, 1, rectImage.getWidth() - 2, rectImage.getHeight() - 2, null);
    }

    public void addImage(File imageFile) {
        //System.out.println("image path = " + imagePath);
        curXorRectCenter = new Pair<>(-1, -1);
        this.imageFile = imageFile;
        setImage();
    }

    private void setImage() {
        try {
            image = ImageIO.read(imageFile);
            double kx = (double) image.getWidth() / panelWidth;
            double ky = (double) image.getHeight() / panelHeight;
            double k = kx > ky ? kx : ky;
            if (k > 1F) {
                rectImage = new BufferedImage((int)Math.round(image.getWidth()/k),
                        (int)Math.round(image.getHeight()/k), BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale((double) 1 / k, (double) 1 / k);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                scaleOp.filter(image, rectImage);
            } else {
                rectImage = image;
            }
            xorRectWidth = (int)(panelWidth/kx < rectImage.getWidth() ? panelWidth/kx : rectImage.getWidth()-1);
            xorRectHeight = (int)(panelHeight/ky < rectImage.getHeight() ? panelHeight/ky : rectImage.getHeight()-1);
            System.out.println("rectImage width = " + rectImage.getWidth());
            System.out.println("rectImage height = " + rectImage.getHeight());
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawXorRect(int x, int y, int width, int height) {
        Graphics2D g2d = rectImage.createGraphics();
        g2d.setXORMode(Color.white);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1F, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 3F, new float[]{2F, 2F}, 0F));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRect(x, y, width, height);
    }

}
