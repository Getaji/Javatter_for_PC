package mw.tea.src;

import java.util.Timer;

/**
 * アスキーアートです.
 */
public class AsciiArt {
    private String aa;
    private final Timer timer = new Timer();
    public AsciiArt(final String aa) {
        this.aa = aa;
    }
}
