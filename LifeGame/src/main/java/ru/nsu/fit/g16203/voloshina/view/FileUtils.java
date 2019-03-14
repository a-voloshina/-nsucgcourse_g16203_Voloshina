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
                String path = URLDecoder.decode(MainFrame.class.getProtectionDomain().getCodeSource().
                        getLocation().getFile(), Charset.defaultCharset().toString());
                dataDirectory = new File(path).getParentFile();
            } catch (UnsupportedEncodingException e) {
                dataDirectory = new File(".");
            }
            if (dataDirectory == null || !dataDirectory.exists()) dataDirectory = new File(".");
            for (File f : dataDirectory.listFiles()) {
                if (f.isDirectory() && f.getName().endsWith("_Data")) {
                    dataDirectory = f;
                    break;
                }
            }
        }
        return dataDirectory;
    }

    public static File getOpenFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = initFileChooser(extension, description);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return getFile(extension, fileChooser);
        }
        return null;
    }

    public static File getSaveFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = initFileChooser(extension, description);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return getFile(extension, fileChooser);
        }
        return null;
    }

}
