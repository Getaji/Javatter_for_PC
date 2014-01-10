package mw.charcounter.src;

import com.orekyuu.javatter.plugin.JavatterPlugin;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * JTextAreaでキー入力されるたびに文字数をカウントするプラグイン.
 *
 * @author Getaji
 */
public class CharCounter extends JavatterPlugin implements KeyListener {

    private String title;
    @Override
    public void init() {
        this.getMainView().getTweetTextArea().addKeyListener(this);
        title = this.getMainView().getMainFrame().getTitle();
    }

    @Override
    public String getPluginName() {
        return "文字数カウンタ";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String text = this.getMainView().getTweetTextArea().getText();
        this.getMainView().getMainFrame().setTitle(title + " char:" + text.length());
    }
}
