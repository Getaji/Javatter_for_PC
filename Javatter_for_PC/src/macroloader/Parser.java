package macroloader;

import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class Parser {

    private static final String[] MODIFIES = {"Alt", "Ctrl", "Shift"};
    public static String getPrefix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(0, point);
        }
        return fileName;
    }

    public static String keyEventToStr(KeyBind e) {
        if (e.getKeyCode() == -1) {
            return "**ALL BIND**";
        }
        
        String key;

        if (e.getModifiers() == 0) {
            key = String.valueOf(e.getKeyChar()).toUpperCase();
        } else {
            key = KeyEvent.getKeyText(e.getKeyCode());
            int result = Arrays.binarySearch(MODIFIES, key);
            if (-1 < result) {
                key = "";
            } else {
                key = "+" + key;
            }
        }
        String modifies = "";
        if (e.getModifiers() != 0) {
            modifies = KeyEvent.getKeyModifiersText(e.getModifiers());
        }
        return modifies + key;
    }

    public static KeyBind parseKeyBind(KeyEvent keyEvent) {
        return new KeyBind(
                keyEvent.getKeyCode(), keyEvent.getModifiers(), keyEvent.getKeyChar());
    }
}
