package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JToolBar toolBar;
    private HashMap<String, JButton> toolbarButtons = new HashMap<>();

    public MainFrame() throws HeadlessException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ignored) {
        }

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        toolBar = new JToolBar("Main toolbar");
        toolBar.setRollover(false);
        add(toolBar, BorderLayout.PAGE_START);
    }

    public MainFrame(int widthWindow, int heightWindow, String title) {
        this();
        setSize(widthWindow, heightWindow);
        setTitle(title);

    }

    public Icon findIcon(String icon) {
        return new ImageIcon(getClass().getResource("/" + icon));
    }

    public JMenuItem createMenuItem(String title,
                                    String tooltip,
                                    int mnemonic,
                                    String icon,
                                    Font font,
                                    MouseListener actionMethod,
                                    boolean checkBox) {
        JMenuItem item;
        if (checkBox) {
            item = new JCheckBoxMenuItem(title);
        } else {
            item = new JMenuItem(title);
        }
        item.setMnemonic(mnemonic);
        item.setToolTipText(tooltip);
        item.setFont(font);
        if (icon != null) {
            item.setIcon(new ImageIcon(getClass().getResource("/" + icon), title));
        }
        if (actionMethod != null) {
            item.addMouseListener(actionMethod);
        }
        return item;
    }

    private JMenu createSubMenu(String title, Font font, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.setFont(font);
        return menu;
    }

    private String getMenuPathName(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return menuPath.substring(pos + 1);
        else
            return menuPath;
    }

    private MenuElement getMenuElement(String menuPath) {
        MenuElement element = menuBar;
        for (String pathElement : menuPath.split("/")) {
            MenuElement newElement = null;
            for (MenuElement subElement : element.getSubElements()) {
                if ((subElement instanceof JMenu && ((JMenu) subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem) subElement).getText().equals(pathElement))) {
                    if (subElement.getSubElements().length == 1 && subElement.getSubElements()[0] instanceof JPopupMenu) {
                        newElement = subElement.getSubElements()[0];
                    } else {
                        newElement = subElement;
                    }
                    break;
                }
            }
            if (newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    private MenuElement getParentMenuElement(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menuBar;
    }

    public void addSubMenu(String title, Font font, int mnemonic) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), font, mnemonic);
        if (element instanceof JMenuBar)
            ((JMenuBar) element).add(subMenu);
        else if (element instanceof JMenu)
            ((JMenu) element).add(subMenu);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuItem(String title,
                            String tooltip,
                            int mnemonic,
                            String icon,
                            Font font,
                            MouseListener actionMethod,
                            boolean checkBox) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenuItem item = createMenuItem(getMenuPathName(title), tooltip, mnemonic, icon, font, actionMethod, checkBox);
        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public void addMenuSeparator(String title) {
        MenuElement element = getMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        if (element instanceof JMenu)
            ((JMenu) element).addSeparator();
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).addSeparator();
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    private JButton createToolBarButton(JMenuItem item, String icon) {
        JButton button;
        if (icon != null) {
            button = new JButton(findIcon(icon));
        } else {
            button = new JButton(item.getIcon());
        }
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setMargin(new Insets(0, 0, 0, 0));
        for (MouseListener listener : item.getMouseListeners())
            button.addMouseListener(listener);
        button.setToolTipText(item.getToolTipText());
        return button;
    }

    public JButton createToolBarButton(String menuPath, String icon) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToolBarButton(item, icon);
    }

    public void addToolBarButton(String menuPath) {
        JButton button = createToolBarButton(menuPath, null);
        toolBar.add(button);
        toolbarButtons.put(menuPath, button);
    }

    public void addToolBarButton(String menuPath, String icon) {
        JButton button = createToolBarButton(menuPath, icon);
        toolBar.add(button);
        toolbarButtons.put(menuPath, button);
    }

    public JButton getToolBarButton(String menuPath) {
        return toolbarButtons.get(menuPath);
    }

    public void addToolBarSeparator() {
        toolBar.addSeparator();
    }

    public void hideToolbar() {
        toolBar.setVisible(false);
    }

    public void showToolbar() {
        toolBar.setVisible(true);
    }

    public File getSaveFileName(String extension, String description) {
        return FileUtils.getSaveFileName(this, extension, description);
    }

    public File getOpenFileName(String extension, String description) {
        return FileUtils.getOpenFileName(this, extension, description);
    }

    public void setSelectedMenuElement(String path, boolean isSelected) {
        MenuElement element = getMenuElement(path);
        if (element instanceof JCheckBoxMenuItem) {
            ((JCheckBoxMenuItem) element).setSelected(isSelected);
        }
    }

    public Boolean isSelectedMenuElement(String path) {
        MenuElement element = getMenuElement(path);
        if (element instanceof JCheckBoxMenuItem) {
            return ((JCheckBoxMenuItem) element).isSelected();
        }
        return null;
    }

    public void setEnabledMenuElement(String path, boolean isEnabled) {
        MenuElement element = getMenuElement(path);
        if (element instanceof JMenuItem) {
            ((JMenuItem) element).setEnabled(isEnabled);
        }
    }

}
