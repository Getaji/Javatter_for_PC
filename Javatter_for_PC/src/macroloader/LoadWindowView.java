package macroloader;

import com.orekyuu.javatter.view.IJavatterTab;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    private final JCheckBox checkUseConsole;

    private final JCheckBox checkDynamicLoad;

    private final JCheckBox checkCompile;

    public LoadWindowView() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        controller = new LoadWindowController(this);
        checkUseConsole = new JCheckBox("Original console");
        checkDynamicLoad = new JCheckBox("Dynamic load");
        checkCompile = new JCheckBox("Compile");
        init();
    }

    public void init() {
        panelMain.setLayout(new BorderLayout());

        list.setDropTarget(new DropTarget() {
            @Override public void drop(DropTargetDropEvent e) {
                try {
                    Transferable transfer = e.getTransferable();
                    // ファイルリストの転送を受け付ける
                    if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        // copyとmoveを受け付ける
                        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        // ドラッグ＆ドロップされたファイルのリストを取得
                        java.util.List<File> fileList =
                                (java.util.List<File>) transfer.getTransferData(
                                        DataFlavor.javaFileListFlavor);
                        // 取得したファイルの名称を表示
                        for (File file : fileList) {
                            try {
                                MacroManager macroManager = Macro.instance.getMacroManager();
                                macroManager.add(file, macroManager.load(file));
                                Macro.instance.getLoadWindowView()
                                        .addMacro(Parser.getPrefix(file.getName()));
                            } catch (FileNotFoundException e1) {
                                Macro.instance.err("Macro not found!");
                            } catch (ScriptException e1) {
                                Macro.instance.err("Macro \"" + file.getName() +
                                        "\" is incorrect!");
                            }
                            Macro.instance.exportMacroList();
                        }
                    }
                } catch (UnsupportedFlavorException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(list);
        scrollPane.setBorder(new CompoundBorder(new EmptyBorder(7, 7, 7, 7),
                new LineBorder(Color.GRAY)));
        panelMain.add(scrollPane, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setBorder(new EmptyBorder(7, 7, 7, 7));
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
        JButton buttonAdd = new JButton("Add");
        JButton buttonReload = new JButton("Reload");
        JButton buttonRemove = new JButton("Remove");
        JButton buttonOpen = new JButton("Folder");
        JButton buttonKeyBind = new JButton("KeyBind");
        JButton buttonRun = new JButton("Run");

        buttonAdd.addActionListener(controller);
        buttonReload.addActionListener(controller);
        buttonRemove.addActionListener(controller);
        buttonOpen.addActionListener(controller);
        buttonKeyBind.addActionListener(controller);
        buttonRun.addActionListener(controller);

        buttonAdd.setToolTipText("Load and add Javascript file.");
        buttonReload.setToolTipText("Reload script.");
        buttonRemove.setToolTipText("Remove script.");
        buttonOpen.setToolTipText("Open script folder.");
        buttonKeyBind.setToolTipText("Setting key binding.");
        buttonRun.setToolTipText("Run script.");

        checkUseConsole.addActionListener(controller);
        checkDynamicLoad.addActionListener(controller);

        checkUseConsole.setToolTipText("Output to original console.");
        checkDynamicLoad.setToolTipText("Reload script when run.");
        checkCompile.setToolTipText("Compile script when add and reload.");

        panelButtons.add(buttonAdd);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonRun);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonReload);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonRemove);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonOpen);
        panelButtons.add(Box.createVerticalStrut(5));
        panelButtons.add(buttonKeyBind);
        panelButtons.add(Box.createVerticalStrut(10));
        panelButtons.add(checkUseConsole);
        panelButtons.add(checkDynamicLoad);
        panelButtons.add(checkCompile);
        panelMain.add(panelButtons, BorderLayout.EAST);
        panelButtons.updateUI();
    }

    @Override
    public Component getComponent() {
        return panelMain;
    }

    public JPanel getPanel() {
        return panelMain;
    }

    public void addMacro(String name) {
        listModel.addElement(name);
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
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
        return checkUseConsole.isSelected();
    }

    public boolean isDynamicLoad() {
        return checkDynamicLoad.isSelected();
    }

    public boolean isCompile() {
        return checkCompile.isSelected();
    }
}
