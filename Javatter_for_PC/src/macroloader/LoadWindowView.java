package macroloader;

import com.orekyuu.javatter.view.IJavatterTab;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class LoadWindowView implements IJavatterTab {

    JPanel panelMain = new JPanel();

    private final DefaultListModel<String> listModel;

    private final JList<String> list;

    private final LoadWindowController controller;

    private final JCheckBox useConsole;

    public LoadWindowView() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        controller = new LoadWindowController(this);
        useConsole = new JCheckBox("Original console");
    }

    @Override
    public Component getComponent() {
        panelMain.setLayout(new BorderLayout());

        list.setBorder(new CompoundBorder(new EmptyBorder(7, 7, 7, 7),
                                          new LineBorder(Color.GRAY)));
        panelMain.add(list, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setBorder(new EmptyBorder(7, 7, 7, 7));
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
        JButton buttonAdd = new JButton("Add");
        JButton buttonReload = new JButton("Reload");
        JButton buttonRemove = new JButton("Remove");
        JButton buttonRun = new JButton("Run");
        buttonAdd.addActionListener(controller);
        buttonReload.addActionListener(controller);
        buttonRemove.addActionListener(controller);
        buttonRun.addActionListener(controller);
        useConsole.addActionListener(controller);
        panelButtons.add(buttonAdd);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonReload);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonRemove);
        panelButtons.add(Box.createVerticalStrut(10));
        panelButtons.add(buttonRun);
        panelButtons.add(Box.createVerticalStrut(10));
        panelButtons.add(useConsole);
        panelMain.add(panelButtons, BorderLayout.EAST);

        return panelMain;
    }

    public JPanel getPanel() {
        return panelMain;
    }

    public void addMacro(String name) {
        listModel.addElement(name);
    }

    public int[] getSelectedIndices() {
        return list.getSelectedIndices();
    }

    public String getElement(int index) {
        return listModel.get(index);
    }

    public void removeElement(int index) {
        listModel.remove(index);
    }

    public boolean isUseConsole() {
        return useConsole.isSelected();
    }
}
