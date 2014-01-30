package macroloader;

import com.orekyuu.javatter.view.IJavatterTab;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class LoadWindowView implements IJavatterTab, ActionListener {

    JPanel panelMain = new JPanel();

    private final DefaultListModel<String> listModel;

    private final JList<String> list;

    public LoadWindowView() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
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
        buttonAdd.addActionListener(this);
        buttonReload.addActionListener(this);
        buttonRemove.addActionListener(this);
        buttonRun.addActionListener(this);
        panelButtons.add(buttonAdd);
        panelButtons.add(buttonReload);
        panelButtons.add(buttonRemove);
        panelButtons.add(buttonRun);
        panelMain.add(panelButtons, BorderLayout.EAST);

        return panelMain;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = (Component) e.getSource();
        if (component instanceof JButton) {
            switch (((JButton) component).getText()) {
                case "Add": {
                    JFileChooser chooser = new JFileChooser();
                    chooser.addChoosableFileFilter(
                            new FileNameExtensionFilter("JavaScript", "js"));
                    int result = chooser.showOpenDialog(
                            component.getParent().getParent().getParent());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        if (chooser.getSelectedFiles().length < 1) {
                            try {
                                File file = chooser.getSelectedFile();
                                MacroManager macroManager = Macro.instance.getMacroManager();
                                macroManager.add(file, macroManager.load(file));
                                listModel.addElement(Parser.getPrefix(file.getName()));
                            } catch (FileNotFoundException e1) {
                                Macro.instance.err("Macro not found!");
                            }
                        } else {
                            for (File file : chooser.getSelectedFiles()) {
                                try {
                                    MacroManager macroManager = Macro.instance.getMacroManager();
                                    macroManager.add(file, macroManager.load(file));
                                    listModel.addElement(Parser.getPrefix(file.getName()));
                                } catch (FileNotFoundException e1) {
                                    Macro.instance.err("Macro not found!");
                                }
                            }
                        }
                        Macro.instance.exportMacroList();
                    }
                    break;
                }
                case "Reload": {
                    try {
                        for (int index : list.getSelectedIndices()) {
                            Macro.instance.getMacroManager().reload(index);
                            Macro.instance.accept("Macro \"" + listModel.get(index) +
                                    "\" reload complete!");
                        }
                    } catch (FileNotFoundException e1) {
                        Macro.instance.err("Macro not found!");
                    }
                    break;
                }
                case "Remove": {
                    for (int index : list.getSelectedIndices()) {
                        Macro.instance.getMacroManager().remove(index);
                        listModel.remove(index);
                    }
                    Macro.instance.exportMacroList();
                    break;
                }
                case "Run": {
                    for (int index : list.getSelectedIndices()) {
                        try {
                            Macro.instance.getMacroManager().run(index);
                        } catch (ScriptException e1) {
                            Macro.instance.err("Macro \"" + listModel.get(index) +
                                    "\" is incorrect!");
                        }
                    }
                }
            }
        }
    }

    public JPanel getPanel() {
        return panelMain;
    }

    public void addMacro(String name) {
        listModel.addElement(name);
    }
}
