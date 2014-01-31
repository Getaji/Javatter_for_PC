package macroloader;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class KeyBind {

    private int keyCode;

    private int modifiers;

    private char keyChar;

    public KeyBind(int keyCode, int modifiers, char keyChar) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
        this.keyChar = keyChar;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public char getKeyChar() {
        return keyChar;
    }
}
