package ru.nsu.fit.external;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * @author Tagir F. Valeev
 * @coauthor Anastasia A. Voloshina
 */

public class FileUtils {

    private static File dataDirectory = null;


    private static JFileChooser initFileChooser(String[] extensions, String description) {
        JFileChooser fileChooser = new JFileChooser();
        if (extensions != null && description != null) {
            FileFilter filter = new FileNameExtensionFilter(description, extensions);
            fileChooser.addChoosableFileFilter(filter);
        }
        fileChooser.setCurrentDirectory(getDataDirectory());
        return fileChooser;
    }

    private static File getFile(String[] extensions, JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        if (!file.getName().contains("."))
            file = new File(file.getParent(), file.getName() + "." + extensions[0]);
        return file;
    }

    /**
     * Returns File pointing to Data directory of current project. If Data directory is not found, returns current directory.
     *
     * @return File object.
     */
    public static File getDataDirectory() {
        if (dataDirectory == null) {
            try {
                dataDirectory = new File(System.getProperty("user.dir")+"/FIT_16203_Voloshina_Filters_Data");

            } catch (NullPointerException e) {
                dataDirectory = new File(System.getProperty("user.dir"));
            }
        }
        return dataDirectory;
    }

    /**
     * Prompts user for file name to save and returns it
     *
     * @param parent      - parent frame for file selection dialog
     * @param extensions  - preferred file extension (example: "txt")
     * @param description - description of specified file type (example: "Text files")
     * @return File specified by user or null if user canceled operation
     */
    public static File getOpenFileName(JFrame parent, String[] extensions, String description) throws IOException {
        JFileChooser fileChooser = initFileChooser(extensions, description);

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = getFile(extensions, fileChooser);
            dataDirectory = file.getParentFile();
            return file;
        }
        if (result == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        throw new IOException("Can't open file");
    }

    /**
     * Prompts user for file name to open and returns it
     * @param parent - parent frame for file selection dialog
     * @return File specified by user or null if user canceled operation
     */
    public static File getSaveFileName(JFrame parent) {
        JFileChooser fileChooser = initFileChooser(null, null);
        fileChooser.setDialogTitle("Выберите директорию для сохранения");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            dataDirectory = fileChooser.getSelectedFile();
            return dataDirectory;
        }
        return null;
    }

}
