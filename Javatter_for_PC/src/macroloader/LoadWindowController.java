package macroloader;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class LoadWindowController implements ActionListener {

    private final LoadWindowView view;

    public LoadWindowController(LoadWindowView view) {
        this.view = view;
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
                            File file = chooser.getSelectedFile();
                            loadMacro(file);
                        } else {
                            for (File file : chooser.getSelectedFiles()) {
                                loadMacro(file);
                            }
                        }
                        Macro.instance.exportMacroList();
                    }
                    break;
                }
                case "Reload": {
                    try {
                        for (int index : view.getSelectedIndices()) {
                            Macro.instance.getMacroManager().reload(index);
                            Macro.instance.accept("Macro \"" + view.getElement(index) +
                                    "\" reload complete!");
                        }
                    } catch (FileNotFoundException e1) {
                        Macro.instance.err("Macro not found!");
                    }
                    break;
                }
                case "Remove": {
                    for (int index : view.getSelectedIndices()) {
                        Macro.instance.getMacroManager().remove(index);
                        view.removeElement(index);
                    }
                    Macro.instance.exportMacroList();
                    break;
                }
                case "Folder": {
                    for (int index : view.getSelectedIndices()) {
                        File file = Macro.instance.getMacroManager().getFile(index).getParentFile();
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    break;
                }
                case "KeyBind": {
                    new KeyBindView(view.getSelectedIndex()).setVisible(true);
                    break;
                }
                case "Run": {
                    for (int index : view.getSelectedIndices()) {
                        try {
                            Macro.instance.getMacroManager().run(index);
                        } catch (ScriptException e1) {
                            Macro.instance.err("Macro \"" + view.getElement(index) +
                                    "\" is incorrect!");
                        } catch (FileNotFoundException e1) {
                            Macro.instance.err("Macro \"" + view.getElement(index) +
                                    "\" not found!");
                        }
                    }
                    break;
                }
            }
        } else if (component instanceof JCheckBox) {
            switch (((JCheckBox) component).getText()) {
                case "Original console": {
                    Macro.instance.getMacroManager().setUseConsole(view.isUseConsole());
                    break;
                }
                case "Dynamic load": {
                    break;
                }
            }
        }
    }

    public void loadMacro(File file) {
        try {
            MacroManager macroManager = Macro.instance.getMacroManager();
            macroManager.add(file, macroManager.load(file));
            view.addMacro(Parser.getPrefix(file.getName()));
        } catch (FileNotFoundException e1) {
            Macro.instance.err("Macro not found!");
        }
    }
}
