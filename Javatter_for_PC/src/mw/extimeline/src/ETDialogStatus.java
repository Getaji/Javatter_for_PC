package mw.extimeline.src;

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import twitter4j.Status;

public class ETDialogStatus extends JDialog {
    public ETDialogStatus(Status status) {
        setTitle("ツイート情報");
        setSize(380, 200);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel icon = new JLabel("Loading...");
        icon.setBounds(5, 5, 48, 48);
        icon.setBorder(new LineBorder(Color.DARK_GRAY));
        try {
            icon.setIcon(new ImageIcon(ImageIO.read(new URL(
                    status.getUser().getProfileImageURL()))));
            icon.setText(null);
        } catch (MalformedURLException e) {} catch (IOException e) {}

        JTextPane text = new JTextPane();
        text.setText(status.getText());
        text.setBackground(Color.white);
        text.setBorder(new LineBorder(Color.DARK_GRAY));
        text.setBounds(58, 5, 300, 150);
        text.setEditable(false);
        text.setMargin(new Insets(10, 10, 10, 10));
        text.setOpaque(true);
        JScrollPane scroll = new JScrollPane(text);
        scroll.setBounds(58, 5, 300, 150);
        scroll.setBorder(new LineBorder(Color.DARK_GRAY));
        
        JLabel rt = new JLabel();
        rt.setText(
        		"<html><font size=\"3\">" + status.getRetweetCount() + "</font> " +
        		"retweet");
        rt.setBounds(5, 55, 50, 50);

        panel.add(icon);
        panel.add(scroll);
        panel.add(rt);
        add(panel);
    }
}
