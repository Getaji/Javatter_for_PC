package macroloader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class MacroManager {

    private final List<File> fileList;

    private final List<String> scriptList;

    private final ScriptEngine engine;

    private final StringBuilder builder;

    public MacroManager() {
        fileList = new LinkedList<>();
        scriptList = new LinkedList<>();
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.getContext().setWriter(Macro.instance.getConsole().getWriter());
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

    public void run(int index) throws IndexOutOfBoundsException, ScriptException {
        if (!Macro.instance.getConsole().isVisible()) {
            Macro.instance.getConsole().setVisible(true);
        }
        engine.eval(scriptList.get(index));
    }

    public void remove(int index) {
        fileList.remove(index);
        scriptList.remove(index);
    }

    public List<File> getFileList() {
        return fileList;
    }
}
