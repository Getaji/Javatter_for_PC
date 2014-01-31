package macroloader;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.util.ImageManager;
import com.orekyuu.javatter.view.IJavatterTab;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class Macro extends JavatterPlugin {

    public static Macro instance;

    private LoadWindowView view;

    private final MacroManager macroManager;

    private final Console console;

    private final File export;

    public Macro() {
        instance = this;
        console = new Console();
        macroManager = new MacroManager();
        export = new File("./macrolist.txt");
        if (!export.exists()) {
            try {
                export.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void init() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(export);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner != null) {
            while (scanner.hasNext()) {
                File file = new File(scanner.nextLine());
                try {
                    macroManager.add(file, macroManager.load(file));
                    view.addMacro(Parser.getPrefix(file.getName()));
                    System.out.println("[Macro] Loaded " + file.getName());
                } catch (FileNotFoundException e) {
                    Macro.instance.err("Macro \"" + file.getAbsolutePath() + "\" not found!");
                }
            }
        }
    }

    @Override
    public String getPluginName() {
        return "MacroLoader";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public IJavatterTab getPluginConfigViewObserver() {
        if (view == null) {
            view = new LoadWindowView();
        }
        return view;
    }

    public void exportMacroList() {
        this.exportMacroList(macroManager.getFileList());
    }

    public void exportMacroList(List<File> files) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(export));
            for (File file : files) {
                writer.append(file.getAbsolutePath());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MacroManager getMacroManager() {
        return macroManager;
    }

    public Console getConsole() {
        return console;
    }

    public LoadWindowView getLoadWindowView() {
        return view;
    }

    public void accept(String status) {
        getMainView().setStatus(new ImageIcon(
                ImageManager.getInstance().getImage(ImageManager.STATUS_NORMAL)),
                status);
    }

    public void err(String status) {
        getMainView().setStatus(new ImageIcon(
                ImageManager.getInstance().getImage(ImageManager.STATUS_ERROR)),
                status);
    }
}
