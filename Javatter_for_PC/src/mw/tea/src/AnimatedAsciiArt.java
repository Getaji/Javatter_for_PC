package mw.tea.src;

import java.util.LinkedList;
import java.util.List;

public class AnimatedAsciiArt {
    private List<String> aaList;
    private int interval;
    private final List<Tea_EventListener<String>> eventListenerList = new LinkedList<>();
    public AnimatedAsciiArt(final List<String> aaList, final int interval) {
        this.aaList = aaList;
        this.interval = interval;
    }

    public List<String> getAAList() {
        return aaList;
    }

    public int getInterval() {
        return interval;
    }

    public List<Tea_EventListener<String>> getEventListener() {
        return eventListenerList;
    }
}
