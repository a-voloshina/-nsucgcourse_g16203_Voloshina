package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.filechooser.FileFilter;
import java.io.File;

class ExtensionFileFilter extends FileFilter {

    private String extension, description;

    ExtensionFileFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith("." + extension.toLowerCase());
    }

    @Override
    public String getDescription() {
        return description + " (*." + extension + ")";
    }
}
