package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.g16203.voloshina.filters.*;
import ru.nsu.fit.g16203.voloshina.view.zones.PartZone;
import ru.nsu.fit.g16203.voloshina.view.zones.ResultZone;
import ru.nsu.fit.g16203.voloshina.view.zones.SourceZone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MainWindow extends MainFrame {

    private Font font = new Font("TimesNewRoman", Font.PLAIN, 14);
    private String defaultTooltip = "Ready";

    private JPanel zonesPanel = new JPanel();
    private SourceZone sourceZone;
    private PartZone partZone;
    private ResultZone resultZone;
    private JPanel statusPanel;
    private JLabel statusBar;
    private Component locationComponent;

    public MainWindow() throws HeadlessException {
        super(1105, 500, "FIT_16203_Voloshina_Filters");
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        customizeMenu();
        customizeToolbar();
        customizeZones();
        customizeStatusBar();

        locationComponent = this;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void customizeMenu() {
        addSubMenu("File", font, KeyEvent.VK_I);
        addMenuItem("File/New", "Create new file", KeyEvent.VK_N,
                "new.png", font, new NewButtonListener(), false);
        addMenuItem("File/Open", "Open source file", KeyEvent.VK_O,
                "open.png", font, new OpenButtonListener(), false);
        addMenuItem("File/Save", "Save current state in file", KeyEvent.VK_S,
                "save.png", font, null, false);
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
                "logout.png", font, new ExitButtonListener(), false);

        addSubMenu("View", font, KeyEvent.VK_V);
        addMenuItem("View/Toolbar", "Show/hide toolbar", KeyEvent.VK_T,
                null, font, new ToolbarButtonListener(), true);
        addMenuItem("View/Status Bar", "Show/hide status bar", KeyEvent.VK_B,
                null, font, new StatusBarButtonListener(), true);

        addSubMenu("Edit", font, KeyEvent.VK_E);
        addMenuItem("Edit/Select", "Select a part of image to see int zone B", KeyEvent.VK_S,
                null, font, new SelectMouseListener(), true);
        addMenuItem("Edit/Copy right", "Copy image from zone B to zone C", KeyEvent.VK_RIGHT,
                null, font, new CopyRightMouseListener(), false);
        addMenuItem("Edit/Copy left", "Copy image from zone C to zone B", KeyEvent.VK_LEFT,
                null, font, new CopyLeftMouseListener(), false);
        addMenuSeparator("Edit");
        addMenuItem("Edit/Zoom", "Double image", KeyEvent.VK_Z,
                null, font, null, false);
        addMenuItem("Edit/Blur", "Smooth image", KeyEvent.VK_Z,
                null, font, new BlurFilterButtonMouseListener(), false);
        addMenuItem("Edit/Sharpness", "Sharpen image", KeyEvent.VK_Z,
                null, font, new SharpnessFilterButtonMouseListener(), false);
        addMenuItem("Edit/Emboss", "Emboss", KeyEvent.VK_Z,
                null, font, new EmbossFilterButtonMouseListener(), false);
        addMenuItem("Edit/Watercolor", "Watercolor", KeyEvent.VK_A,
                null, font, new WatercolorFilterButtonMouseListener(), false);
        addMenuItem("Edit/Rotate", "Rotate image", KeyEvent.VK_A,
                null, font, null, false);
        addSubMenu("Edit/Canal", font, KeyEvent.VK_E);
        addMenuItem("Edit/Canal/Black and white", "Translate color image to black and white(greyscale)", KeyEvent.VK_X,
                null, font, new BlackWhiteButtonMouseListener(), false);
        addMenuItem("Edit/Canal/Negative", "Convert image to negative", KeyEvent.VK_P,
                null, font, new NegativeButtonMouseListener(), false);
        addMenuItem("Edit/Canal/Gamma correction", "Lighten or darken the image", KeyEvent.VK_G,
                null, font, new GammaCorrectionFilterButtonMouseListener(), false);
        addMenuItem("Edit/Canal/Delete R-chanel", "Set to zero the red component of the image", KeyEvent.VK_P,
                null, font, null, false);
        addMenuItem("Edit/Canal/Delete G-chanel", "Set to zero the green component of the image", KeyEvent.VK_P,
                null, font, null, false);
        addMenuItem("Edit/Canal/Delete B-chanel", "Set to zero the blue component of the image", KeyEvent.VK_P,
                null, font, null, false);
        addMenuItem("Edit/Canal/Brightness", "Increase the image brightness", KeyEvent.VK_P,
                null, font, null, false);
        addMenuItem("Edit/Canal/Contrast", "Increase the image contrast", KeyEvent.VK_P,
                null, font, null, false);
        addSubMenu("Edit/Dithering", font, KeyEvent.VK_E);
        addMenuItem("Edit/Dithering/Floyd-Steinberg", "Apply Floyd-Steinberg dithering algorithm", KeyEvent.VK_F,
                null, font, new FloydSteinbergButtonMouseListener(), false);
        addMenuItem("Edit/Dithering/Ordered dither", "Apply ordered dither algorithm", KeyEvent.VK_P,
                null, font, new OrderedDitheringButtonMouseListener(), false);
        addSubMenu("Edit/Edges", font, KeyEvent.VK_E);
        addMenuItem("Edit/Edges/Robert's edges", "Robert's edges selection", KeyEvent.VK_P,
                null, font, new RobertsButtonMouseListener(), false);
        addMenuItem("Edit/Edges/Sobel's edges", "Sobel's edges selection", KeyEvent.VK_P,
                null, font, new SobelFilterButtonMouseListener(), false);
        addMenuItem("Edit/Edges/Matrix's edges", "Matrix's edges selection", KeyEvent.VK_P,
                null, font, new MatrixEdgesFilterButtonMouseListener(), false);

        addSubMenu("Help", font, KeyEvent.VK_H);
        addMenuItem("Help/About", "Some information about application", KeyEvent.VK_F8,
                "about.png", font, new AboutButtonListener(), false);
    }

    private void customizeToolbar() {
        addToolBarButton("File/New");
        addToolBarButton("File/Open");
        addToolBarButton("File/Save");
        addToolBarSeparator();
        addToolBarButton("Edit/Select", "select.png");
        addToolBarButton("Edit/Copy right", "right-arrow.png");
        addToolBarButton("Edit/Copy left", "left-arrow.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Canal/Black and white", "blackwhite.png");
        addToolBarButton("Edit/Canal/Negative", "exposure.png");
        addToolBarButton("Edit/Canal/Gamma correction", "gamma.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Dithering/Ordered dither", "ordered.png");
        addToolBarButton("Edit/Dithering/Floyd-Steinberg", "floyd.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Edges/Robert's edges", "roberts.png");
        addToolBarButton("Edit/Edges/Sobel's edges", "sobel.png");
        addToolBarButton("Edit/Edges/Matrix's edges", "matrix.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Blur", "blur.png");
        addToolBarButton("Edit/Sharpness", "sharpness.png");
        addToolBarButton("Edit/Emboss", "stamp.png");
        addToolBarButton("Edit/Watercolor", "watercolor.png");
        addToolBarSeparator();
        addToolBarButton("Help/About");

        setSelectedMenuElement("View/Toolbar", true);
    }

    private void customizeStatusBar() {
        statusPanel = new JPanel();
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        statusBar = new JLabel("Ready");
        statusBar.setFont(font);
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusBar);
        setSelectedMenuElement("View/Status Bar", true);
    }

    private void customizeZones() {
        zonesPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        partZone = new PartZone();
        sourceZone = new SourceZone();
        resultZone = new ResultZone();
        zonesPanel.add(sourceZone);
        zonesPanel.add(partZone);
        zonesPanel.add(resultZone);
        add(zonesPanel);
    }

    class NewButtonListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            sourceZone.clear();
            partZone.clear();
            resultZone.clear();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("File/New").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class OpenButtonListener implements MouseListener {

        String tooltip = "Open source file";

        private void actionPerformed() {
            //File openFile = getOpenFileName("png", "portable network graphics images");
            File openFile = getOpenFileName(null, null);
            if (openFile != null) {
                try {
                    sourceZone.addImage(openFile);
                    partZone.clear();
                    resultZone.clear();
                } catch (IOException e) {
                    String messageText = "Формат файла не поддерживается. Поодерживаемый формат изображения -  BMP/PNG, " +
                            "24 бита/пиксель";
                    JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                String messageText = "Не удалось открыть файл";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class ExitButtonListener implements MouseListener {

        String tooltip = "Exit application";

        private void actionPerformed() {
            System.exit(0);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class ToolbarButtonListener implements MouseListener {

        private String tooltip = "Show/hide toolbar";

        private void actionPerformed() {
            if (!isSelectedMenuElement("View/Toolbar")) {
                showToolbar();
            } else {
                hideToolbar();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isSelectedMenuElement("View/Toolbar")) {
                tooltip = "Show toolbar";
            } else {
                tooltip = "Hide toolbar";
            }
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class StatusBarButtonListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isSelectedMenuElement("View/Status Bar")) {
                statusPanel.setVisible(true);
            } else {
                statusPanel.setVisible(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isSelectedMenuElement("View/Status Bar")) {
                statusBar.setText("Show status bar");
            } else {
                statusBar.setText("Hide status bar");
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class AboutButtonListener implements MouseListener {

        String tooltip = "Some information about application";

        private void actionPerformed() {
            AboutDialog dialog = new AboutDialog();
            dialog.pack();
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(locationComponent);
            dialog.setVisible(true);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            actionPerformed();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText("Ready");
        }
    }

    class SelectMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (sourceZone.isImageAdded()) {
                if (isSelectedMenuElement("Edit/Select")) {
                    getToolBarButton("Edit/Select").setSelected(false);
                    setSelectedMenuElement("Edit/Select", false);
                    sourceZone.stopSelectImagePart();
                } else {
                    getToolBarButton("Edit/Select").setSelected(true);
                    setSelectedMenuElement("Edit/Select", true);
                    sourceZone.selectImagePart(partZone::setImage);
                }
            } else {
                setSelectedMenuElement("Edit/Select", false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Select").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class CopyRightMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(partZone.getImage());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Copy right").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class CopyLeftMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (resultZone.isImageAdded()) {
                partZone.setImage(resultZone.getImage());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Copy left").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class NegativeButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new NegativeFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Canal/Negative").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class BlackWhiteButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new BlackWhiteFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Canal/Black and white").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class OrderedDitheringButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new OrderedDitheringFilter(4).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Dithering/Ordered dither").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class FloydSteinbergButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new FloydSteinbergFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Dithering/Floyd-Steinberg").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class RobertsButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new RobertsFilter(50).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Edges/Robert's edges").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class SobelFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new SobelFilter(200).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Edges/Sobel's edges").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class MatrixEdgesFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new MatrixEdgesFilter(10).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Edges/Matrix's edges").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class BlurFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new BlurFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Blur").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class SharpnessFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new SharpeningFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Sharpness").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class EmbossFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new StampingFilter().apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Emboss").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class WatercolorFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new WatercolorFilter(5).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Watercolor").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class GammaCorrectionFilterButtonMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new GammaCorrectionFilter(1.5).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Canal/Gamma correction").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

}
