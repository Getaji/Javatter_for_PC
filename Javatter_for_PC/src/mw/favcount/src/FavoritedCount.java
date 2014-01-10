package mw.favcount.src;

import com.orekyuu.javatter.controller.UserStreamController;
import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.view.IJavatterTab;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * ふぁぼりとふぁぼられをカウントして上位10人をリストアップします.
 * @author Getaji
 */
public class FavoritedCount extends JavatterPlugin implements IJavatterTab {

    /** GUIの描画に用いるメイリオフォント. */
    public static final Font FONT_MEIRYO = new Font("Meiryo UI", Font.PLAIN, 12);

    /** 全ふぁぼりユーザーのMap. */
    private final Map<User, Integer> favedUsers = new HashMap<>();
    /** 全ふぁぼられユーザーのMap. */
    private final Map<User, Integer> favSufedUsers = new HashMap<>();
    /** 上位10人のふぁぼりユーザーのMap. */
    private final Map<User, Integer> favedTopUsers = new HashMap<>();
    /** 上位10人のふぁぼられユーザーのMap. */
    private final Map<User, Integer> favSufedTopUsers = new HashMap<>();

    /** ふぁぼりユーザーのリストモデル. */
    private final DefaultListModel<Map.Entry<User, Integer>> favedListModel = new DefaultListModel<>();
    /** ふぁぼられユーザーのリストモデル. */
    private final DefaultListModel<Map.Entry<User, Integer>> favSufedListModel = new DefaultListModel<>();
    /** ふぁぼりユーザーのリストコンポーネント. */
    private final JList<Map.Entry<User, Integer>>favedList = new JList<>(favedListModel);
    /** ふぁぼられユーザーのリストコンポーネント. */
    private final JList<Map.Entry<User, Integer>> favSufedList = new JList<>(favSufedListModel);

    /** 最大人数. */
    private final int maxUser = 10;

    @Override
    public void init() {
        addMenuTab("ふぁぼカウンタ", this);
        addUserStreamListener(new UserStreamController() {
            @Override
            public void onFavorite(User source, User target, Status favoritedStatus) {
                try {
                    boolean is_self = (source.getId() == twitter.getId());
                    User user = (is_self ? target : source);
                    Integer count = (is_self ? favedUsers : favSufedUsers).get(user);
                    count = (count == null ? 1 : count + 1);
                    update(user, count, is_self);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatus(Status status) { }
        });
    }

    @Override
    public String getPluginName() {
        return "ふぁぼカウンタ";
    }

    @Override
    public String getVersion() {
        return "1.1ふぁぼ";
    }

    @Override
    public Component getComponent() {
        JPanel panelTop = new JPanel();
        JPanel panelFaved = new JPanel();
        JPanel panelFavSufed = new JPanel();
        JScrollPane favedScrollPane = new JScrollPane(favedList);
        JScrollPane favSufedScrollPane = new JScrollPane(favSufedList);
        JLabel labelFaved = new JLabel("ふぁぼり");
        JLabel labelFavSufed = new JLabel("ふぁぼられ");

        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);

        panelTop.setLayout(new GridLayout(2, 1));
        panelTop.add(panelFaved);
        panelTop.add(panelFavSufed);
        panelFaved.setBorder(emptyBorder);
        panelFaved.setLayout(new BorderLayout());
        panelFaved.add(labelFaved, BorderLayout.NORTH);
        panelFaved.add(favedScrollPane, BorderLayout.CENTER);
        panelFavSufed.setBorder(emptyBorder);
        panelFavSufed.setLayout(new BorderLayout());
        panelFavSufed.add(labelFavSufed, BorderLayout.NORTH);
        panelFavSufed.add(favSufedScrollPane, BorderLayout.CENTER);
        favedList.setCellRenderer(new FC_CellRender());
        favSufedList.setCellRenderer(new FC_CellRender());
        labelFaved.setFont(FONT_MEIRYO);
        labelFavSufed.setFont(FONT_MEIRYO);

        return panelTop;
    }

    /**
     * ふぁぼを追加してリストを更新する.
     * @param user ふぁぼったユーザー
     * @param count ふぁぼ数
     * @param isSelf ふぁぼったかふぁぼられたか
     */
    private void update(User user, int count, boolean isSelf) {
        Map<User, Integer> top_user = (isSelf ? favedTopUsers : favSufedTopUsers);

        (isSelf ? favedListModel : favSufedListModel).removeAllElements();
        (isSelf ? favedUsers : favSufedUsers).put(user, count);
        top_user.put(user, count);

        int loop = (maxUser < top_user.size() ? maxUser : top_user.size());
        List<Map.Entry<User, Integer>> entries = valueSort(top_user);

        if (maxUser < entries.size())
            entries.remove(maxUser);

        for (int i = 0; i < loop; ++i)
            (isSelf ? favedListModel : favSufedListModel).addElement(entries.get(i));
    }

    private List<Map.Entry<User, Integer>> valueSort(Map map) {
        List<Map.Entry<User, Integer>> entries = new LinkedList<Map.Entry<User, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<User, Integer>>() {
            public int compare(Map.Entry<User, Integer> o1,
                               Map.Entry<User, Integer> o2) {
                Map.Entry<User, Integer> entry1 = (Map.Entry<User, Integer>) o1;
                Map.Entry<User, Integer> entry2 = (Map.Entry<User, Integer>) o2;
                Integer int1 = (Integer) entry1.getValue();
                Integer int2 = (Integer) entry2.getValue();
                return int2 - int1;
            }
        });
        return entries;
    }
}

class FC_CellRender implements ListCellRenderer<Map.Entry<User, Integer>> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Map.Entry<User, Integer>> list, Map.Entry<User, Integer> value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = new JLabel();
        User user = value.getKey();
            int count = value.getValue();
            try {
                label.setIcon(new ImageIcon(ImageIO.read(new URL(user.getMiniProfileImageURL()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            label.setText("@" + user.getScreenName() + " " + count + "ふぁぼ");
            label.setFont(FavoritedCount.FONT_MEIRYO);
        return label;
    }
}
