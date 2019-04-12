package ru.nsu.fit.g16203.voloshina.general;

import ru.nsu.fit.external.MainFrame;

import java.awt.*;

public abstract class Window extends MainFrame {

    public Window(int widthWindow, int heightWindow, String title) {
        super(widthWindow, heightWindow, title);
    }

    protected Font font = new Font("TimesNewRoman", Font.PLAIN, 14);
    protected Component locationComponent = this;

    protected abstract void customizeMenu();

    protected abstract void customizeToolbar();

    protected abstract void customizeStatusBar();
}
