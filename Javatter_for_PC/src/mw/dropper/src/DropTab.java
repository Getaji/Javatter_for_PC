package mw.dropper.src;

import com.orekyuu.javatter.plugin.JavatterPluginLoader;
import com.orekyuu.javatter.util.TweetObjectFactory;
import com.orekyuu.javatter.view.IJavatterTab;
import mw.extimeline.src.ExTimeline;
import twitter4j.Status;

import javax.swing.*;
import java.awt.*;


public class DropTab implements IJavatterTab {
    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);

    public DropTab() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    @Override
    public Component getComponent() {
        return scrollPane;
    }

    public void addStatus(Status status) {
        panel.add(createObject(status), 0);
        panel.repaint();
    }

    /**
     * ツイートオブジェクトを作成して返す.
     * @param status 対象ステータス
     * @return 作成されたオブジェクト
     */
    private JPanel createObject(Status status){
        TweetObjectFactory factory = new TweetObjectFactory(status, JavatterPluginLoader.getTweetObjectBuilder());
        return (JPanel) factory.createTweetObject(ExTimeline.instance.getView()).getComponent();
    }
}
