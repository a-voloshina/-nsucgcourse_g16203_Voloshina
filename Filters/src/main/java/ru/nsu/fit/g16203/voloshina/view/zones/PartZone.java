package ru.nsu.fit.g16203.voloshina.view.zones;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PartZone extends JPanel {

    private BufferedImage image;
    private int panelWidth = 350;
    private int panelHeight = 350;

    private boolean isImageAdded = false;

    public PartZone() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setSize(new Dimension(panelWidth, panelHeight));
        setBorder(BorderFactory.createDashedBorder(Color.black, 4, 2));
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 1, 1, image.getWidth() - 2, image.getHeight() - 2, null);
    }

    public void clear(){
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        isImageAdded = false;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage bufferedImage) {
        image = bufferedImage;
        isImageAdded = true;
        repaint();
    }

    public boolean isImageAdded() {
        return isImageAdded;
    }

}
