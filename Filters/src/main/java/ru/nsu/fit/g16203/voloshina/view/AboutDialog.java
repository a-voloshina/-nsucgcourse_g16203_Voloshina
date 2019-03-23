package ru.nsu.fit.g16203.voloshina.view;

import javax.swing.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel aboutImage;
    private JLabel authorNameLabel;
    private JLabel gameNameLabel;
    private JLabel copyrightLabel;

    public AboutDialog() {
        setTitle("About");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    public static void main(String[] args) {
        AboutDialog dialog = new AboutDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        dispose();
    }
}
