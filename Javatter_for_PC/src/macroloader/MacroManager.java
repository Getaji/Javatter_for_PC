package macroloader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.List;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class MacroManager implements KeyEventDispatcher {

    private final List<File> fileList;

    private final List<String> scriptList;

    private final Map<Integer, List<KeyBind>> keyBindMap;

    private final ScriptEngine engine;

    private final StringBuilder builder;

    private final Writer consoleWriter;

    private final Writer systemWriter;

    private KeyEvent lastKeyEvent;

    public MacroManager() {
        fileList = new LinkedList<>();
        scriptList = new LinkedList<>();
        keyBindMap = new HashMap<>();
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        consoleWriter = Macro.instance.getConsole().getWriter();
        systemWriter = engine.getContext().getWriter();
        builder = new StringBuilder();
    }

    public void add(File file, String script) {
        fileList.add(file);
        scriptList.add(script);
    }

    public String load(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            builder.append(scanner.nextLine());
            builder.append("\n");
        }
        scanner.close();
        String script = builder.toString();
        builder.setLength(0);
        return script;
    }

    public void reload(int index) throws IndexOutOfBoundsException, FileNotFoundException {
        scriptList.set(index, load(fileList.get(index)));

    }

    public void run(int index) throws IndexOutOfBoundsException, ScriptException, FileNotFoundException {
        if (Macro.instance.getLoadWindowView().isUseConsole() &&
                !Macro.instance.getConsole().isVisible()) {
            Macro.instance.getConsole().setVisible(true);
        }
        if (Macro.instance.getLoadWindowView().isDynamicLoad()) {
            reload(index);
        }
        engine.eval(scriptList.get(index));
    }

    public void remove(int index) {
        fileList.remove(index);
        scriptList.remove(index);
        keyBindMap.remove(index);
    }

    public File getFile(int index) {
        return fileList.get(index);
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setUseConsole(boolean useConsole) {
        engine.getContext().setWriter(useConsole ? consoleWriter : systemWriter);
    }

    public void addKeyBind(int index, KeyBind keyBind) {
        List<KeyBind> list = keyBindMap.get(index);
        if (list == null) {
            list = new LinkedList<>();
            keyBindMap.put(index, list);
        }
        list.add(keyBind);
        Macro.instance.exportKeyBind(keyBindMap);
    }

    public void setKeyBind(int macroIndex, int keyIndex, KeyBind keyBind) {
        List<KeyBind> list = keyBindMap.get(macroIndex);
        if (list != null) {
            list.set(keyIndex, keyBind);
        }
        Macro.instance.exportKeyBind(keyBindMap);
    }

    public void removeKeyBind(int macroIndex, int keyIndex) {
        List<KeyBind> list = keyBindMap.get(macroIndex);
        if (list != null) {
            list.remove(keyIndex);
        }
        Macro.instance.exportKeyBind(keyBindMap);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        lastKeyEvent = e;
        Iterator<List<KeyBind>> iterator = keyBindMap.values().iterator();
        for (int i = 0; iterator.hasNext(); ++i)//List<KeyEvent> list : keyBindMap.values())
            for (KeyBind reg : iterator.next()) {
                if (reg.getKeyCode() == -1 ||
                        (e.getKeyCode() == reg.getKeyCode() &&
                         e.getModifiers() == reg.getModifiers())) {
                    try {
                        run(i);
                    } catch (ScriptException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        return false;
    }

    public void print(String string) {
        setUseConsole(true);
        try {
            engine.getContext().getWriter().append(string);
            engine.getContext().getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUseConsole(false);
    }

    public List<KeyBind> getBindList(int index) {
        return keyBindMap.get(index);
    }

    public KeyEvent getLastKeyEvent() {
        return lastKeyEvent;
    }
}
