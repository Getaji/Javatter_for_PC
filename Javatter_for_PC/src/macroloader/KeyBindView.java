package macroloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class KeyBindView extends JDialog implements KeyListener, ActionListener {

    private final JTextField field;

    private final DefaultListModel<String> listModel;

    private final JList<String> keyList;

    private final JButton buttonBind;

    private final JButton buttonUpdate;

    private final JButton buttonRemove;

    private KeyBind lastKeyBind;

    private final int index;

    public KeyBindView(int index) {
        super((JFrame) null, "KeyBind");

        this.index = index;

        setModal(true);
        setSize(320, 320);

        field = new JTextField();
        listModel = new DefaultListModel<>();
        keyList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel panel = new JPanel();
        buttonBind = new JButton("Bind");
        buttonUpdate = new JButton("Update");
        buttonRemove = new JButton("Remove");

        java.util.List<KeyBind> eventList = Macro.instance.getMacroManager().getBindList(index);
        if (eventList != null) {
            for (KeyBind keyBind : Macro.instance.getMacroManager().getBindList(index)) {
                listModel.addElement(Parser.keyEventToStr(keyBind));
            }
        }
        scrollPane.setViewportView(keyList);

        buttonBind.addActionListener(this);
        buttonUpdate.addActionListener(this);
        buttonRemove.addActionListener(this);

        panel.setLayout(new GridLayout(1, 3));
        panel.add(buttonBind);
        panel.add(buttonUpdate);
        panel.add(buttonRemove);

        field.addKeyListener(this);

        add(field, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        lastKeyBind = Parser.parseKeyBind(e);
        if (lastKeyBind.getKeyCode() == KeyEvent.VK_ESCAPE) {
            field.setText("");
            return;
        }
        String text = Parser.keyEventToStr(Parser.parseKeyBind(e));
        field.setText(text);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (lastKeyBind.getKeyCode() == KeyEvent.VK_ESCAPE) {
            field.setText("");
            return;
        }
        String text = Parser.keyEventToStr(lastKeyBind);
        field.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JButton) e.getSource()).getText()) {
            case "Bind": {
                if (field.getText().isEmpty()) {
                    listModel.addElement("**ALL BIND**");
                    Macro.instance.getMacroManager().addKeyBind(index, new KeyBind(-1, -1, '*'));
                } else {
                    listModel.addElement(field.getText());
                    Macro.instance.getMacroManager().addKeyBind(
                            index, lastKeyBind);
                    field.setText("");
                }
                break;
            }
            case "Update": {
                int select = keyList.getSelectedIndex();
                if (field.getText().isEmpty()) {
                    listModel.addElement("**ALL BIND**");
                    Macro.instance.getMacroManager().setKeyBind(index, select, new KeyBind(-1, -1, '*'));
                } else {
                    listModel.set(select, field.getText());
                    Macro.instance.getMacroManager().setKeyBind(index, select, lastKeyBind);
                }
                break;
            }
            case "Remove": {
                int select = keyList.getSelectedIndex();
                listModel.remove(select);
                Macro.instance.getMacroManager().removeKeyBind(index, select);
                break;
            }
        }
        field.requestFocus();
    }
}
