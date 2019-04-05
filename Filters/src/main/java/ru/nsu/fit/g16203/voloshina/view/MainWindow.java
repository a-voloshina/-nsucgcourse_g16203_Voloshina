package ru.nsu.fit.g16203.voloshina.view;

import ru.nsu.fit.external.MainFrame;
import ru.nsu.fit.g16203.voloshina.controller.VolumeRenderingController;
import ru.nsu.fit.g16203.voloshina.filters.*;
import ru.nsu.fit.g16203.voloshina.view.dialog.AboutDialog;
import ru.nsu.fit.g16203.voloshina.view.dialog.DitheringDialog;
import ru.nsu.fit.g16203.voloshina.view.dialog.ParametersDialog;
import ru.nsu.fit.g16203.voloshina.view.dialog.RenderingDialog;
import ru.nsu.fit.g16203.voloshina.view.zones.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainWindow extends MainFrame {

    private Font font = new Font("TimesNewRoman", Font.PLAIN, 14);
    private String defaultTooltip = "Ready";

    private JPanel zonesPanel = new JPanel();
    private SourceZone sourceZone;
    private PartZone partZone;
    private ResultZone resultZone;
    private JPanel renderingPanel = new JPanel();
    private EmissionZone emissionZone = new EmissionZone();
    private AbsorbtionZone absorbtionZone = new AbsorbtionZone();
    private JPanel statusPanel;
    private JLabel statusBar;
    private Component locationComponent;

    private boolean conditionChanges = false;
    private VolumeRenderingController volumeRenderingController;

    public MainWindow() throws HeadlessException {
        super(1105, 700, "FIT_16203_Voloshina_Filters");
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
                "save.png", font, new SaveButtonListener(), false);
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
                null, font, new ScalingFilterButtonMouseListener(), false);
        addMenuItem("Edit/Blur", "Smooth image", KeyEvent.VK_Z,
                null, font, new BlurFilterButtonMouseListener(), false);
        addMenuItem("Edit/Sharpness", "Sharpen image", KeyEvent.VK_Z,
                null, font, new SharpnessFilterButtonMouseListener(), false);
        addMenuItem("Edit/Emboss", "Emboss", KeyEvent.VK_Z,
                null, font, new EmbossFilterButtonMouseListener(), false);
        addMenuItem("Edit/Watercolor", "Watercolor", KeyEvent.VK_A,
                null, font, new WatercolorFilterButtonMouseListener(), false);
        addMenuItem("Edit/Rotate", "Rotate image", KeyEvent.VK_A,
                null, font, new RotationFilterButtonMouseListener(), false);
        addSubMenu("Edit/Canal", font, KeyEvent.VK_E);
        addMenuItem("Edit/Canal/Black and white", "Translate color image to black and white(greyscale)", KeyEvent.VK_X,
                null, font, new BlackWhiteButtonMouseListener(), false);
        addMenuItem("Edit/Canal/Negative", "Convert image to negative", KeyEvent.VK_P,
                null, font, new NegativeButtonMouseListener(), false);
        addMenuItem("Edit/Canal/Gamma correction", "Lighten or darken the image using gamma correction", KeyEvent.VK_G,
                null, font, new GammaCorrectionFilterButtonMouseListener(), false);
        addSubMenu("Edit/Volume rendering", font, KeyEvent.VK_V);
        addMenuItem("Edit/Volume rendering/Config", "Open volume rendering config file", KeyEvent.VK_C,
                null, font, new ConfigButtonMouseListener(), false);
        addMenuItem("Edit/Volume rendering/Emission", "Turn on/off emission for volume rendering", KeyEvent.VK_E,
                null, font, new EmissionButtonMouseListener(), true);
        addMenuItem("Edit/Volume rendering/Absorbtion", "Turn on/off absorbtion for volume rendering", KeyEvent.VK_A,
                null, font, new AbsorbtionButtonMouseListener(), false);
        addMenuItem("Edit/Volume rendering/Volume rendering", "Visualizate volume rendering", KeyEvent.VK_R,
                null, font, new VolumeRenderingButtonMouseListener(), false);
//        addMenuItem("Edit/Canal/Delete R-chanel", "Set to zero the red component of the image", KeyEvent.VK_P,
//                null, font, null, false);
//        addMenuItem("Edit/Canal/Delete G-chanel", "Set to zero the green component of the image", KeyEvent.VK_P,
//                null, font, null, false);
//        addMenuItem("Edit/Canal/Delete B-chanel", "Set to zero the blue component of the image", KeyEvent.VK_P,
//                null, font, null, false);
//        addMenuItem("Edit/Canal/Brightness", "Increase the image brightness", KeyEvent.VK_P,
//                null, font, null, false);
//        addMenuItem("Edit/Canal/Contrast", "Increase the image contrast", KeyEvent.VK_P,
//                null, font, null, false);
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
        addToolBarButton("Edit/Rotate", "rotate.png");
        addToolBarButton("Edit/Zoom", "resize.png");
        addToolBarSeparator();
        addToolBarButton("Edit/Volume rendering/Config", "manual.png");
        addToolBarButton("Edit/Volume rendering/Emission", "emission.png");
        addToolBarButton("Edit/Volume rendering/Absorbtion", "absorbtion.png");
        addToolBarButton("Edit/Volume rendering/Volume rendering", "volume.png");
        addToolBarSeparator();
        addToolBarButton("Help/About");

        setSelectedMenuElement("View/Toolbar", true);
        setSelectedMenuElement("View/Status Bar", true);

        getToolBarButton("Edit/Volume rendering/Emission").setEnabled(false);
        getToolBarButton("Edit/Volume rendering/Absorbtion").setEnabled(false);
        getToolBarButton("Edit/Volume rendering/Volume rendering").setEnabled(false);
    }

    private void customizeStatusBar() {
        statusPanel = new JPanel();
        add(statusPanel, BorderLayout.PAGE_END);
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
        zonesPanel.add(absorbtionZone);
        zonesPanel.add(emissionZone);
        add(zonesPanel);
    }

    class NewButtonListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            sourceZone.clear();
            partZone.clear();
            resultZone.clear();
            absorbtionZone.clear();
            emissionZone.clear();
            getToolBarButton("Edit/Volume rendering/Emission").setEnabled(false);
            getToolBarButton("Edit/Volume rendering/Absorbtion").setEnabled(false);
            getToolBarButton("Edit/Volume rendering/Volume rendering").setEnabled(false);
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
            //File openFile = getOpenFileName(new String[]{"png", "bmp"}, "portable network graphics images");
            File openFile = null;
            try {
                openFile = getOpenFileName(new String[]{"png", "bmp"}, "portable network graphics images");
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
                }
            } catch (IOException e) {
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

    class SaveButtonListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            File saveDir = getSaveFileName();
            if (saveDir != null && saveDir.isDirectory()) {
                String result = JOptionPane.showInputDialog(locationComponent, "Введите имя файла");
                String saveFileName;
                if (result != null) {
                    saveFileName = result + ".png";
                } else {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH!mm!ss");
                    Date date = new Date();
                    saveFileName = "example" + dateFormat.format(date) + ".png";
                }
                try {
                    File file = new File(saveDir.getAbsoluteFile() + "\\" + saveFileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    ImageIO.write(resultZone.getImage(), "png", file);
                    System.out.println("success: " + saveDir.getPath() + "\\" + saveFileName);
                } catch (IOException ex) {
                    String messageText = "Не удалось сохранить изображение";
                    JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
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

        private int Nr = 3;
        private int Ng = 3;
        private int Nb = 2;

        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                DitheringDialog dialog = new DitheringDialog();
                dialog.setNr(Nr);
                dialog.setNg(Ng);
                dialog.setNb(Nb);
                dialog.setOnOkListener(new OKOrderedDitheringButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OKOrderedDitheringButtonListener implements ActionListener {
            DitheringDialog dialog;

            public OKOrderedDitheringButtonListener(DitheringDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                OrderedDitheringFilter filter = new OrderedDitheringFilter(4);
                if (dialog.getNr() != null) {
                    Nr = dialog.getNr();
                    filter.setRedN(Nr);
                }
                if (dialog.getNg() != null) {
                    Ng = dialog.getNg();
                    filter.setGreenN(Ng);
                }
                if (dialog.getNb() != null) {
                    Nb = dialog.getNb();
                    filter.setBlueN(Nb);
                }
                resultZone.setImage(filter.apply(partZone.getImage()));
                dialog.dispose();
            }
        }
    }

    class FloydSteinbergButtonMouseListener extends MouseAdapter {

        private int Nr = 3;
        private int Ng = 3;
        private int Nb = 2;

        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                DitheringDialog dialog = new DitheringDialog();
                dialog.setNr(Nr);
                dialog.setNg(Ng);
                dialog.setNb(Nb);
                dialog.setOnOkListener(new OKDitheringButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OKDitheringButtonListener implements ActionListener {
            DitheringDialog dialog;

            public OKDitheringButtonListener(DitheringDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                FloydSteinbergFilter filter = new FloydSteinbergFilter();
                if (dialog.getNr() != null) {
                    Nr = dialog.getNr();
                    filter.setRedN(Nr);
                }
                if (dialog.getNg() != null) {
                    Ng = dialog.getNg();
                    filter.setGreenN(Ng);
                }
                if (dialog.getNb() != null) {
                    Nb = dialog.getNb();
                    filter.setBlueN(Nb);
                }
                resultZone.setImage(filter.apply(partZone.getImage()));
                dialog.dispose();
            }
        }

    }

    class RobertsButtonMouseListener extends MouseAdapter {

        private int edge = 45;

        @Override
        public void mousePressed(MouseEvent e) {
            ParametersDialog dialog = new ParametersDialog(false);
            dialog.setLabelText("Пороговое значение:");
            if (partZone.isImageAdded()) {
                dialog.setEdge(edge);
                dialog.setOnOkListener(new OnOkRobertsButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OnOkRobertsButtonListener implements ActionListener {

            private ParametersDialog dialog;

            public OnOkRobertsButtonListener(ParametersDialog dialog) {
                this.dialog = dialog;
                this.dialog.setLabelText("Пороговое значение:");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (partZone.isImageAdded()) {
                    if (dialog.getEdge() != null) {
                        RobertsFilter filter = new RobertsFilter(dialog.getEdge());
                        edge = filter.getThreshold();
                        resultZone.setImage(filter.apply(partZone.getImage()));
                    } else {
                        String messageText = "Введенное значение некорректно";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        }
    }

    class SobelFilterButtonMouseListener extends MouseAdapter {

        private int edge = 200;

        @Override
        public void mousePressed(MouseEvent e) {
            ParametersDialog dialog = new ParametersDialog(false);
            dialog.setLabelText("Пороговое значение:");
            if (partZone.isImageAdded()) {
                dialog.setEdge(edge);
                dialog.setOnOkListener(new OnOkButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OnOkButtonListener implements ActionListener {

            private ParametersDialog dialog;

            public OnOkButtonListener(ParametersDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (partZone.isImageAdded()) {
                    if (dialog.getEdge() != null) {
                        SobelFilter filter = new SobelFilter(dialog.getEdge());
                        edge = filter.getThreshold();
                        resultZone.setImage(filter.apply(partZone.getImage()));
                    } else {
                        String messageText = "Введенное значение некорректно";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        }
    }

    class MatrixEdgesFilterButtonMouseListener extends MouseAdapter {

        private int edge = 170;

        @Override
        public void mousePressed(MouseEvent e) {
            ParametersDialog dialog = new ParametersDialog(false);
            dialog.setLabelText("Пороговое значение:");
            if (partZone.isImageAdded()) {
                dialog.setEdge(edge);
                dialog.setOnOkListener(new OnOkMatrixButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OnOkMatrixButtonListener implements ActionListener {

            private ParametersDialog dialog;

            public OnOkMatrixButtonListener(ParametersDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (partZone.isImageAdded()) {
                    if (dialog.getEdge() != null) {
                        MatrixEdgesFilter filter = new MatrixEdgesFilter(dialog.getEdge());
                        edge = filter.getThreshold();
                        resultZone.setImage(filter.apply(partZone.getImage()));
                    } else {
                        String messageText = "Введенное значение некорректно";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
            }
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

        private double gamma = 1.5;

        @Override
        public void mousePressed(MouseEvent e) {
            ParametersDialog dialog = new ParametersDialog(true);
            dialog.setLabelText("Параметр гамма:");
            if (partZone.isImageAdded()) {
                dialog.setDoubleParameter(gamma);
                dialog.setOnOkListener(new OnOkGammaButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
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

        class OnOkGammaButtonListener implements ActionListener {

            private ParametersDialog dialog;

            public OnOkGammaButtonListener(ParametersDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (partZone.isImageAdded()) {
                    if (dialog.getDoubleParameter() != null) {
                        GammaCorrectionFilter filter = new GammaCorrectionFilter(dialog.getDoubleParameter());
                        gamma = filter.getGamma();
                        resultZone.setImage(filter.apply(partZone.getImage()));
                    } else {
                        String messageText = "Введенное значение некорректно";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        }
    }

    class RotationFilterButtonMouseListener extends MouseAdapter {

        private int angle = 45;
        @Override
        public void mousePressed(MouseEvent e) {
            ParametersDialog dialog = new ParametersDialog(false);
            dialog.setLabelText("Угол поворота:");
            dialog.setMaximum(180);
            dialog.setMinimum(-180);
            if (partZone.isImageAdded()) {
                dialog.setEdge(angle);
                dialog.setOnOkListener(new OnOkRotationButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Rotate").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }

        class OnOkRotationButtonListener implements ActionListener {

            private ParametersDialog dialog;

            public OnOkRotationButtonListener(ParametersDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (partZone.isImageAdded()) {
                    if (dialog.getEdge() != null) {
                        RotationFilter filter = new RotationFilter(dialog.getEdge());
                        angle = filter.getAngle();
                        resultZone.setImage(filter.apply(partZone.getImage()));
                    } else {
                        String messageText = "Введенное значение некорректно";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                dialog.dispose();
            }
        }
    }

    class ScalingFilterButtonMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded()) {
                resultZone.setImage(new ScalingFilter(2).apply(partZone.getImage()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Zoom").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class ConfigButtonMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                File openFile = getOpenFileName(null, null);
                if (openFile != null) {
                    try {
                        volumeRenderingController = new VolumeRenderingController(openFile,
                                absorbtionZone::drawAbsorbtionGraphic, emissionZone::drawEmissionGraphic);

                        getToolBarButton("Edit/Volume rendering/Emission").setEnabled(true);
                        getToolBarButton("Edit/Volume rendering/Emission").setSelected(true);
                        getToolBarButton("Edit/Volume rendering/Absorbtion").setEnabled(true);
                        getToolBarButton("Edit/Volume rendering/Absorbtion").setSelected(true);
                        getToolBarButton("Edit/Volume rendering/Volume rendering").setEnabled(true);

                    } catch (IOException ex) {
                        String messageText = "Формат конфигурационного файла не поддерживается";
                        JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (IOException ex) {
                String messageText = "Не удалось открыть файл";
                JOptionPane.showMessageDialog(null, messageText, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Volume rendering/Volume rendering").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class EmissionButtonMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JButton emissionToolbarButton = getToolBarButton("Edit/Volume rendering/Emission");
            if (!emissionToolbarButton.isSelected()) {
                emissionToolbarButton.setSelected(true);
            } else {
                emissionToolbarButton.setSelected(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Volume rendering/Emission").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class AbsorbtionButtonMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JButton absorbtionToolbarButton = getToolBarButton("Edit/Volume rendering/Absorbtion");
            if (!absorbtionToolbarButton.isSelected()) {
                absorbtionToolbarButton.setSelected(true);
            } else {
                absorbtionToolbarButton.setSelected(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Volume rendering/Absorbtion").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }
    }

    class VolumeRenderingButtonMouseListener extends MouseAdapter {

        private int max = 350;
        private int Nx = 350;
        private int Ny = 350;
        private int Nz = 350;

        @Override
        public void mousePressed(MouseEvent e) {
            if (partZone.isImageAdded() && volumeRenderingController != null) {
                RenderingDialog dialog = new RenderingDialog(max);
                dialog.setNx(Nx);
                dialog.setNy(Ny);
                dialog.setNz(Nz);
                dialog.setOnOkListener(new OKButtonListener(dialog));

                dialog.pack();
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(locationComponent);
                dialog.setVisible(true);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            statusBar.setText(getToolBarButton("Edit/Volume rendering/Volume rendering").getToolTipText());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            statusBar.setText(defaultTooltip);
        }

        class OKButtonListener implements ActionListener {
            RenderingDialog dialog;

            OKButtonListener(RenderingDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialog.getNx() != null || dialog.getNy() != null || dialog.getNz() != null) {
                    if (dialog.getNx() != null) {
                        Nx = dialog.getNx();
                    }
                    if (dialog.getNy() != null) {
                        Ny = dialog.getNy();
                    }
                    if (dialog.getNz() != null) {
                        Nz = dialog.getNz();
                    }
                    dialog.dispose();
                    boolean isAbsorbtionOn = getToolBarButton("Edit/Volume rendering/Absorbtion").isSelected();
                    System.out.println("isAbsorbtionOn = " + isAbsorbtionOn);
                    boolean isEmissionOn = getToolBarButton("Edit/Volume rendering/Emission").isSelected();
                    System.out.println("isEmissionOn = " + isEmissionOn);
                    resultZone.setImage(volumeRenderingController.render(partZone.getImage(),
                            Nx, Ny, Nz, isAbsorbtionOn, isEmissionOn));
                }
            }
        }
    }

}
