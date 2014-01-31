package macroloader;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.util.ImageManager;
import com.orekyuu.javatter.view.IJavatterTab;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    private final File macroFile;

    private final File keyBindFile;

    public Macro() {
        instance = this;
        console = new Console();
        macroManager = new MacroManager();
        macroFile = new File("./macrolist.txt");
        if (!macroFile.exists()) {
            try {
                macroFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        keyBindFile = new File("./macro_keybind.cfg");
        if (!keyBindFile.exists()) {
            try {
                keyBindFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(macroManager);
    }
    @Override
    public void init() {
        Scanner scanner;
        try {
            scanner = new Scanner(macroFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int macroLine = 0;
        while (scanner.hasNext()) {
            ++macroLine;
            File file = new File(scanner.nextLine());
            try {
                macroManager.add(file, macroManager.load(file));
                view.addMacro(Parser.getPrefix(file.getName()));
                System.out.println("[Macro] Loaded " + file.getName());
            } catch (FileNotFoundException e) {
                Macro.instance.err("Macro \"" + file.getAbsolutePath() + "\" not found!");
            }
        }
        try {
            scanner = new Scanner(keyBindFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int keyBindLine = 0;
        while (scanner.hasNext()) {
            ++keyBindLine;
            String str = scanner.nextLine();
            if (str.isEmpty()) {
                continue;
            }
            String[] values = str.split("\\|\\|");
            if (values.length < 4) {
                err("[MacroLoader] macro_keybind.cfg キーバインドの値が足りません.:" +
                        keyBindLine + "行目\n");
            } else {
                int index;
                try {
                    index = Integer.parseInt(values[0]);
                } catch (NumberFormatException e) {
                    err("[MacroLoader] macro_keybind.cfg インデックス値が不正です.:" +
                            keyBindLine + "行目\n");
                    continue;
                }
                if (macroLine < index) {
                    err("[MacroManager] キーバインドのインデックスがマクロファイルの数を超過しています.");
                    break;
                }
                int keyCode;
                try {
                    keyCode = Integer.parseInt(values[1]);
                } catch (NumberFormatException e) {
                    err("[MacroLoader] macro_keybind.cfg キーコード値が不正です.:" +
                            keyBindLine + "行目\n");
                    continue;
                }
                int modifies;
                try {
                    modifies = Integer.parseInt(values[2]);
                } catch (NumberFormatException e) {
                    err("[MacroLoader] macro_keybind.cfg 修飾キー値が不正です.:" +
                            keyBindLine + "行目\n");
                    continue;
                }
                char keyChar = values[3].toCharArray()[0];
                macroManager.addKeyBind(index, new KeyBind(keyCode, modifies, keyChar));
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(macroFile));
            for (File file : files) {
                writer.append(file.getAbsolutePath());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportKeyBind(Map<Integer, List<KeyBind>> map) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(keyBindFile));
            Iterator<List<KeyBind>> iterator = map.values().iterator();
            for (int i = 0; iterator.hasNext(); ++i) {
                for (KeyBind keyBind : iterator.next()) {
                    writer.append(String.valueOf(i));
                    writer.append("||");
                    writer.append(String.valueOf(keyBind.getKeyCode()));
                    writer.append("||");
                    writer.append(String.valueOf(keyBind.getModifiers()));
                    writer.append("||");
                    writer.append(String.valueOf(keyBind.getKeyChar()));
                    writer.newLine();
                }
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
