package mw.tea.src;

/**
 * イベントリスナの抽象インターフェースです.
 * @author Getaji
 * @param <E> イベントで発生する値
 */
public interface Tea_EventListener<E> {
    public void onEvent(E value);
}
