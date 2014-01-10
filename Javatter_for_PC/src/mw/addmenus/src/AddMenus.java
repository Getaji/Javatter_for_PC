package mw.addmenus.src;

import com.orekyuu.javatter.account.TwitterManager;
import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.util.ImageManager;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * いろいろ特殊なメニューを追加するプラグインです.
 * @author Getaji
 */
public class AddMenus extends JavatterPlugin implements mw.extimeline.src.IMenuItemBuilder {
    private final ImageIcon iconTweet = loadImageIcon("tweet.png");
    private final ImageIcon iconAddText = loadImageIcon("addtext.png");

    @Override
    public void init() { }

    @Override
    public  void postInit() {
        if (isPluginLoaded("ExTimeline")) {
            mw.extimeline.src.ExTimeline.instance.addMenuItemBuilder(this);
        }
    }

    @Override
    public String getPluginName() {
        return "[ExTimeline]AddMenus";
    }

    @Override
    public String getVersion() {
        return "1.1";
    }

    @Override
    public List<JMenuItem> getMenuItem(JPanel panel, final Status status) {
        final List<JMenuItem> list = new ArrayList<>();
        final Status rtStatus = (status.isRetweet() ? status.getRetweetedStatus() : status);
        final Font meiryo = new Font("Meiryo UI", Font.PLAIN, 12);
        final JTextArea area = AddMenus.this.getMainView().getTweetTextArea();

        final JMenuItem itemPakuTwi = new JMenuItem(" パクツイ", iconTweet);
        itemPakuTwi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTweet(status.isRetweet() ? status.getRetweetedStatus().getText() : status.getText());
            }
        });
        final JMenuItem itemUNKakoi = new JMenuItem(" 囲い式update_name", iconAddText);
        itemUNKakoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.append("(@" + status.getUser().getScreenName() + ")");
                area.requestFocus();
                area.setCaretPosition(0);
            }
        });
        final JMenuItem itemUNReply = new JMenuItem(" リプ式update_name", iconAddText);
        itemUNReply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.setText("@" + status.getUser().getScreenName() + " update_name " + area.getText());
                area.requestFocus();
            }
        });
        final JMenuItem itemAirReply = new JMenuItem(" 空リプ", iconTweet);
        itemAirReply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatusUpdate su = new StatusUpdate("@" + status.getUser().getScreenName());
                su.setInReplyToStatusId(status.getId());
                onTweet(su);
            }
        });

        final JMenu menuService = new JMenu(" ブラウザで開く");
        final JMenuItem itemWebUser = new JMenuItem(" ユーザーを公式webで開く");
        itemWebUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("https://twitter.com/" + rtStatus.getUser().getScreenName());
            }
        });
        final JMenuItem itemWebIcon = new JMenuItem(" アイコン画像の場所を開く");
        itemWebIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI(rtStatus.getUser().getOriginalProfileImageURL());
            }
        });
        final JMenuItem itemUserFavstar = new JMenuItem(" ユーザーをFavstarで開く");
        itemUserFavstar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("http://ja.favstar.fm/users/" + rtStatus.getUser().getScreenName());
            }
        });
        final JMenuItem itemUserAclog = new JMenuItem(" ユーザーをaclogで開く");
        itemUserAclog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("http://aclog.koba789.com/" + rtStatus.getUser().getScreenName());
            }
        });
        final JMenuItem itemUserTwilog = new JMenuItem(" ユーザーをTwilogで開く");
        itemUserTwilog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("http://twilog.org/" + rtStatus.getUser().getScreenName());
            }
        });
        final JMenuItem itemTweetFavstar = new JMenuItem(" ツイートをFavstarで開く");
        itemTweetFavstar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("http://ja.favstar.fm/users/" + rtStatus.getUser().getScreenName() +
                        "/status/" + rtStatus.getId());
            }
        });
        JMenuItem itemTweetAclog = new JMenuItem(" ツイートをaclogで開く");
        itemTweetAclog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openURI("http://aclog.koba789.com/i/" + rtStatus.getId());
            }
        });
        menuService.add(itemWebUser);
        menuService.add(itemWebIcon);
        menuService.add(itemUserFavstar);
        menuService.add(itemUserAclog);
        menuService.add(itemUserTwilog);
        menuService.add(itemTweetFavstar);
        menuService.add(itemTweetAclog);

        final JMenu menuCopy = new JMenu(" クリップボードにコピー");
        final JMenuItem itemCopyScreenName = new JMenuItem(" スクリーンネーム");
        itemCopyScreenName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getScreenName());
            }
        });
        final JMenuItem itemCopyUserName = new JMenuItem(" ユーザーネーム");
        itemCopyUserName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getName());
            }
        });
        final JMenu menuCopyIconUrl = new JMenu(" アイコンURL");
        final JMenuItem itemCIU = new JMenuItem(" 原寸");
        itemCIU.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getOriginalProfileImageURL());
            }
        });
        final JMenuItem itemCIUBigger = new JMenuItem(" 大");
        itemCIUBigger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getBiggerProfileImageURL());
            }
        });
        final JMenuItem itemCIUNormal = new JMenuItem(" 中");
        itemCIUNormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getProfileImageURL());
            }
        });
        final JMenuItem itemCIUMini = new JMenuItem(" 小");
        itemCIUMini.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getUser().getMiniProfileImageURL());
            }
        });
        final JMenuItem itemCopyTweet = new JMenuItem(" ツイート");
        itemCopyTweet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(rtStatus.getText());
            }
        });
        menuCopyIconUrl.add(itemCIU);
        menuCopyIconUrl.add(itemCIUBigger);
        menuCopyIconUrl.add(itemCIUNormal);
        menuCopyIconUrl.add(itemCIUMini);
        menuCopy.add(itemCopyScreenName);
        menuCopy.add(itemCopyUserName);
        menuCopy.add(menuCopyIconUrl);
        menuCopy.add(itemCopyTweet);

        final JMenuItem itemWarotaRt = new JMenuItem(" ワロタ式RT", iconAddText);
        itemWarotaRt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.append("ワロタｗ RT@" + rtStatus.getUser().getScreenName() +
                        " " + rtStatus.getText());
                area.requestFocus();
            }
        });
        final JMenuItem itemCuko = new JMenuItem(" 覚えましたし", iconAddText);
        itemCuko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.append("（「" + status.getText() + "」………" + status.getText() + "………覚えましたし)");
                area.requestFocus();
                area.setCaretPosition(2);
            }
        });

        final JMenu menuUserOperation = new JMenu(" ユーザーへの操作");
        final JMenuItem itemUserFollow = new JMenuItem(" フォロー");

        list.add(itemPakuTwi);
        list.add(itemUNKakoi);
        list.add(itemUNReply);
        list.add(itemAirReply);
        list.add(menuService);
        list.add(menuCopy);
        list.add(itemWarotaRt);
        list.add(itemCuko);

        for (JMenuItem menuItem : list )
            menuItem.setFont(meiryo);
        for (Component menuItem : menuService.getMenuComponents())
            menuItem.setFont(meiryo);
        for (Component menuItem : menuCopy.getMenuComponents())
            menuItem.setFont(meiryo);
        for (Component menuItem : menuCopyIconUrl.getMenuComponents())
            menuItem.setFont(meiryo);
        return list;
    }

    private ImageIcon loadImageIcon(String url) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(AddMenus.class.getResourceAsStream(url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    private void openURI(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    private void copy(String str) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), null);
    }

    /**
     * ツイートします.<br />
     * StatusUpdateが渡された場合StatusUpdateとして,<br />
     * その他オブジェクトが渡された場合toStringされた文字列としてツイートされます.
     * @param tweet ツイート内容
     */
    public void onTweet(Object tweet) {
        ImageIcon icon = null;
        try {
            if (tweet instanceof StatusUpdate)
                TwitterManager.getInstance().getTwitter().updateStatus((StatusUpdate) tweet);
            else
                TwitterManager.getInstance().getTwitter().updateStatus(tweet.toString());
            icon = new ImageIcon(ImageManager.getInstance().getImage(ImageManager.STATUS_NORMAL));
        } catch (TwitterException e1) {
            icon = new ImageIcon(ImageManager.getInstance().getImage(ImageManager.STATUS_ERROR));
        } finally {
            AddMenus.this.getMainView().setStatus(icon, "[AddMenus] Failed:" + tweet);
        }
    }
}
