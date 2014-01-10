package mw.dropper.src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: TAISEI
 * Date: 13/12/12
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
public class FilterItemEditor extends JDialog implements ActionListener {
    private JPanel panelTop = new JPanel();
    private JPanel panelFields = new JPanel();
    private JPanel panelButtons = new JPanel();
    private JTextField textField = new JTextField();
    private JButton buttonApply = new JButton("OK");
    private JButton buttonCancel = new JButton("Cancel");
    private String type;
    private int index;

    private DropListTab dropListTab;

    public static final String NEW = "NEW", EDIT = "EDIT";

    public FilterItemEditor(String text, String type, DropListTab dropListTab, int itemIndex) {
        this(type, dropListTab);
        index = itemIndex;
        textField.setText(text);
    }

    public FilterItemEditor(String type, DropListTab dropListTab) {
        super((Window) null);
        this.setTitle("フィルターの" + (type.equals(NEW) ? "新規作成": "編集"));
        this.setSize(320, 140);
        this.setResizable(false);
        this.setModal(true);
        this.type = type;

        panelTop.setLayout(new BorderLayout());
        panelTop.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel labelType = new JLabel(type);
        labelType.setFont(new Font("Arial", Font.ITALIC, 26));
        buttonApply.addActionListener(this);
        buttonCancel.addActionListener(this);

        panelFields.add(textField);
        panelButtons.add(buttonApply);
        panelButtons.add(buttonCancel);

        panelTop.add(labelType, BorderLayout.NORTH);
        panelTop.add(textField, BorderLayout.CENTER);
        panelTop.add(panelButtons, BorderLayout.SOUTH);

        add(panelTop);

        this.dropListTab = dropListTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttonApply))
            if (type.equals(NEW))
                dropListTab.addFilter(textField.getText());
            else
                dropListTab.editFilter(index, textField.getText());
        this.setVisible(false);
    }
}
