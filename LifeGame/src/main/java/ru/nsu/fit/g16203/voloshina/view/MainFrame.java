package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.createMainFrame();
    }

    public void createMainFrame() {
        JFrame frame = new JFrame("FIT_16203_Voloshina_Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("TimesNewRoman", Font.PLAIN, 14);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = createMenu(font, " File ");
        createMenuItem(font, "    New  ", fileMenu);
        createMenuItem(font, "    Open  ", fileMenu);
        createMenuItem(font, "    Save  ", fileMenu);
        createMenuItem(font, "    Save as...  ", fileMenu);
        fileMenu.addSeparator();
        createMenuItem(font, "    Exit ", fileMenu);
        menuBar.add(fileMenu);

        JMenu editMenu = createMenu(font, "Edit");
        JMenu modeEditMenu = createMenu(font, "    Mode  ");
        editMenu.add(modeEditMenu);
        createMenuItem(font, "    XOR  ", modeEditMenu);
        createMenuItem(font, "    Replace ", modeEditMenu);
        editMenu.addSeparator();
        createMenuItem(font, "    Clear  ", editMenu);
        editMenu.addSeparator();
        createMenuItem(font, "    Settings  ", editMenu);
        menuBar.add(editMenu);

        JMenu viewMenu = createMenu(font, "View");
        createMenuItem(font, "   Toolbar ", viewMenu);
        createMenuItem(font, "   Status Bar ", viewMenu);
        createMenuItem(font, "   Display Impact Values", viewMenu);
        menuBar.add(viewMenu);

        JMenu runMenu = createMenu(font, "Run");
        createMenuItem(font, "    Run ", runMenu);
        createMenuItem(font, "    Step ", runMenu);
        menuBar.add(runMenu);

        JMenu helpMenu = createMenu(font, "Help");
        createMenuItem(font, "   About ", runMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
        createToolbar(frame);


        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private JMenu createMenu(Font font, String name) {
        JMenu menu = new JMenu(name);
        menu.setFont(font);
        return menu;
    }

    private void createMenuItem(Font font, String itemName, JMenu menu) {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.setFont(font);
        menu.add(menuItem);
    }

    private void createToolbar(JFrame frame) {
        JToolBar toolbar = new JToolBar();

        JButton newButton = new JButton(new ImageIcon(this.getClass().getResource("/new-file.png")));
        toolbar.add(newButton);
        JButton openButton = new JButton(new ImageIcon(this.getClass().getResource("/open-file.png")));
        toolbar.add(openButton);
        JButton saveButton = new JButton(new ImageIcon(this.getClass().getResource("/save-file.png")));
        toolbar.add(saveButton);
        JButton settingsButton = new JButton(new ImageIcon(this.getClass().getResource("/settings.png")));
        toolbar.add(settingsButton);
        JButton XORButton = new JButton(new ImageIcon(this.getClass().getResource("/change.png")));
        toolbar.add(XORButton);
        JButton impactButton = new JButton(new ImageIcon(this.getClass().getResource("/impact.png")));
        toolbar.add(impactButton);
        JButton clearButton = new JButton(new ImageIcon(this.getClass().getResource("/cancel.png")));
        toolbar.add(clearButton);
        JButton nextButton = new JButton(new ImageIcon(this.getClass().getResource("/last.png")));
        toolbar.add(nextButton);
        JButton playButton = new JButton(new ImageIcon(this.getClass().getResource("/play.png")));
        toolbar.add(playButton);
        JButton helpButton = new JButton(new ImageIcon(this.getClass().getResource("/help.png")));
        toolbar.add(helpButton);

        frame.add(toolbar, BorderLayout.NORTH);
    }

}
