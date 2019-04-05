package ru.nsu.fit.g16203.voloshina.view.zones;

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
    private int xorRectWidth = 50;
    private int xorRectHeight = 50;
    private double k;

    private SourceZoneMouseMotionListener mouseListener = new SourceZoneMouseMotionListener();

    public SourceZone() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setSize(new Dimension(panelWidth, panelHeight));
        setBorder(BorderFactory.createDashedBorder(Color.black, 4, 2));

        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        rectImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(rectImage, 1, 1, rectImage.getWidth() - 2, rectImage.getHeight() - 2, null);
    }

    private void setImage() throws IOException {
        image = ImageIO.read(imageFile);
        if (image.getColorModel().getPixelSize() != 24) {
            throw new IOException();
        }
        double kx = (double) image.getWidth() / panelWidth;
        double ky = (double) image.getHeight() / panelHeight;
        k = kx > ky ? kx : ky;
        if (k > 1F) {
            rectImage = new BufferedImage((int) Math.round(image.getWidth() / k),
                    (int) Math.round(image.getHeight() / k), BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale((double) 1 / k, (double) 1 / k);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            scaleOp.filter(image, rectImage);
        } else {
            rectImage = image;
        }
        xorRectWidth = (int) (panelWidth / k < rectImage.getWidth() - 1 ? panelWidth / k : rectImage.getWidth() - 1);
        xorRectHeight = (int) (panelHeight / k < rectImage.getHeight() - 1 ? panelHeight / k : rectImage.getHeight() - 1);
        repaint();
    }

    private BufferedImage getImagePart(int xorRectX, int xorRectY) {
        return image.getSubimage((int) Math.round(xorRectX * k), (int) Math.round(xorRectY * k),
                panelWidth < image.getWidth() ? panelWidth : image.getWidth(),
                panelHeight < image.getHeight() ? panelHeight : image.getHeight());
    }

    private void drawXorRect(int x, int y, int width, int height) {
        Graphics2D g2d = rectImage.createGraphics();
        g2d.setXORMode(Color.white);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1F, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 3F, new float[]{2F, 2F}, 0F));
        g2d.drawRect(x, y, width, height);
    }

    public void addImage(File imageFile) throws IOException {
        this.imageFile = imageFile;
        setImage();
    }

    public void selectImagePart(ISelect fn) {
        mouseListener.fun = fn;
        addMouseMotionListener(mouseListener);
        repaint();
    }

    public void stopSelectImagePart() {
        try {
            setImage();
        } catch (IOException e) {
            return;
        }
        removeMouseMotionListener(mouseListener);
    }

    public void clear() {
        rectImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public boolean isImageAdded() {
        return (imageFile != null);
    }

    private class SourceZoneMouseMotionListener implements MouseMotionListener {

        ISelect fun;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (imageFile != null) {
                int x = e.getX() - 1;
                int y = e.getY() - 1;
                try {
                    setImage();
                } catch (IOException e1) {
                    return;
                }
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
                drawXorRect(nextXorRectX, nextXorRectY, xorRectWidth, xorRectHeight);
                repaint();
                fun.getSelectedImagePart(getImagePart(nextXorRectX, nextXorRectY));
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public interface ISelect {
        void getSelectedImagePart(BufferedImage imagePart);
    }
}
