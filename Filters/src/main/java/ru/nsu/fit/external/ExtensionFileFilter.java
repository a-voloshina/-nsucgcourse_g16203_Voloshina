package ru.nsu.fit.external;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * File filter which leaves only directories and files with specific extension
 *
 * @author Tagir F. Valeev
 * @coauthor Anastasia A. Voloshina
 */

class ExtensionFileFilter extends FileFilter {

    private String extension, description;

    /**
     * Constructs filter
     *
     * @param extension   - extension (without point), for example, "txt"
     * @param description - file type description, for example, "Text files"
     */
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
