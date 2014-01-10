package mw.tea.src;

import com.orekyuu.javatter.view.IJavatterTab;

import javax.swing.*;
import java.awt.*;

/**
 * 茶室の外観を実装します.
 * @author Getaji
 */
public class TeaRoomView implements IJavatterTab {
    private JTextArea area = new JTextArea();
    private JLabel label = new JLabel();
    @Override
    public Component getComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        area.setFont(new Font("MS PGothic", Font.PLAIN, 13));
        area.setFont(new Font("MS PGochic", Font.PLAIN, 12));
        return panel;
    }

    public void setAA(String aa) {
        area.setText(aa);
    }

    public void setStatus(String status) {
        label.setText(status);
    }
}
