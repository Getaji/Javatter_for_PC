package mw.notifytweet.src;

import mw.notifytweet.src.component.JPopup;
import mw.notifytweet.src.manager.PopupManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * キー操作を受信するクラスです.<br />
 * ショートカットキーでポップアップを閉じるのに利用されます.
 */
public class NTKeyListener implements KeyEventDispatcher {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_P) {
            for (JPopup popup : PopupManager.INSTANCE.getPopups()) {
                popup.setVisible(false);
            }
        }
        return false;
    }
}
