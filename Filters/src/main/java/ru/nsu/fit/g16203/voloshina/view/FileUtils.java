package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class FileUtils {

    private static File dataDirectory = null;

    private static JFileChooser initFileChooser(String extension, String description) {
        JFileChooser fileChooser = new JFileChooser();
        if (extension != null && description != null) {
            FileFilter filter = new ExtensionFileFilter(extension, description);
            fileChooser.addChoosableFileFilter(filter);
        }
        fileChooser.setCurrentDirectory(getDataDirectory());
        return fileChooser;
    }

    private static File getFile(String extension, JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        if (!file.getName().contains("."))
            file = new File(file.getParent(), file.getName() + "." + extension);
        return file;
    }

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

    public static File getOpenFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = initFileChooser(extension, description);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = getFile(extension, fileChooser);
            dataDirectory = file.getParentFile();
            return file;
        }
        return null;
    }

    public static File getSaveFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = initFileChooser(extension, description);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = getFile(extension, fileChooser);
            dataDirectory = file.getParentFile();
            return file;
        }
        return null;
    }

}
