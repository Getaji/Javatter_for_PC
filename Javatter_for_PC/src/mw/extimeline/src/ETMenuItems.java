package mw.extimeline.src;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import com.orekyuu.javatter.main.Main;
import com.orekyuu.javatter.util.TwitterUtil;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.orekyuu.javatter.account.AccountManager;
import com.orekyuu.javatter.account.TwitterManager;

/**
 * メニューアイテムを生成するクラス
 */
public class ETMenuItems implements IMenuItemBuilder, ActionListener {
    public static final Font meiryo = new Font("Meiryo", Font.PLAIN, 12);
    private final TwitterUtil twitterUtil = new TwitterUtil();

    /** 推測マップ. */
    //private Map<String, String[]> guessMap = new HashMap<>();

    /*
    public ETMenuItems() {
        guessMap.put("おかえりなさい", new String[] {"帰宅", "帰った", "ただいま"});
        guessMap.put("いってらっしゃい", new String[] {"行ってくる", "出かけ"});
        guessMap.put("おはようございます", new String[] {"起きた", "起床", "おはよう", "むくり"});
    }
    */

    public ETMenuItems() {
        UIManager.put("JMenuItem.font", new FontUIResource(meiryo));
    }

	@Override
	public List<JMenuItem> getMenuItem(final JPanel parent, final Status status) {
		final List<JMenuItem> list = new ArrayList<>();
		if (status.getUser().getId() == 
        		AccountManager.getInstance().getAccessToken().getUserId()
                && !status.isRetweetedByMe()) {
            final JMenuItem delete = new JMenuItem(" 削除", ResourceManager.iconDelete);
            delete.setFont(meiryo);
            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    int i = JOptionPane.showConfirmDialog(parent,
                            "ツイートを削除しますか？", "確認",
                            JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        parent.getParent().getParent().setVisible(false);
                        try {
                            TwitterManager.getInstance().getTwitter()
                                    .destroyStatus(status.getId());
                        } catch (TwitterException e) {
                        	e.printStackTrace();
                        }
                    }
                }
            });
            list.add(delete);
        }
        // ▼ステータス
        final JMenuItem mstatus = new JMenuItem(" ブラウザで開く", ResourceManager.iconWeb);
        mstatus.setFont(meiryo);
        mstatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (status.isRetweet()) {
                        Desktop.getDesktop().browse(
                                new URI("https://twitter.com/"
                                        + status.getRetweetedStatus()
                                                .getUser()
                                                .getScreenName()
                                        + "/status/"
                                        + status.getRetweetedStatus()
                                                .getId()));
                    } else {
                        Desktop.getDesktop().browse(
                                new URI("https://twitter.com/"
                                        + status.getUser()
                                                .getScreenName()
                                        + "/status/" + status.getId()));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        list.add(mstatus);

        // ▼非公式RT
        final JMenuItem unoffical = new JMenuItem(" 非公式RT", ResourceManager.iconRT);
        unoffical.setFont(meiryo);
        unoffical.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ExTimeline.instance.getView().getTweetTextArea().getText()
                        .equals("")) {
                    if (status.isRetweet()) {
                        ExTimeline.instance.getView().getTweetTextArea().setText(
                                status.getText());
                    } else {
                        ExTimeline.instance.getView().getTweetTextArea().setText(
                                "RT @"
                                        + status.getUser()
                                                .getScreenName() + " "
                                        + status.getText());
                    }
                    ExTimeline.instance.getView().getTweetTextArea().requestFocus();
                    ExTimeline.instance.getView().getTweetTextArea()
                            .setCaretPosition(0);
                }
            }
        });
        list.add(unoffical);
        
        final JMenuItem menuFavRt = new JMenuItem(" ふぁぼってRT", ResourceManager.iconFavRt);
        menuFavRt.setFont(meiryo);
        menuFavRt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Twitter tw = TwitterManager.getInstance().getTwitter();
                try {
					tw.createFavorite(status.getId());
					tw.retweetStatus(status.getId());
				} catch (TwitterException e1) {
					e1.printStackTrace();
				}
            }
        });
        list.add(menuFavRt);
        
        // inReplyTo
        final JMenuItem menuInReplyTo = new JMenuItem(" 会話を表示(beta)", ResourceManager.iconInReplyTo);
        menuInReplyTo.setFont(meiryo);
        menuInReplyTo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread() {
					public void run() {
						new InReplyToView(status);
					}
				};
				thread.start();
			}
             });

        list.add(menuInReplyTo);

        final JMenuItem menuBlow = new JMenuItem(" (腹パン)");
        menuBlow.setFont(meiryo);
        menuBlow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tweet("@" + status.getUser().getScreenName() + menuBlow.getText());
            }
        });
        list.add(menuBlow);

        JMenu menuQuickReply = new JMenu(" クイックリプライ");
        menuQuickReply.setFont(meiryo);
        {
            class QuickMenu extends JMenuItem {
                public QuickMenu(String text) {
                    super(" " + text);
                    addActionListener(ETMenuItems.this);
                    setFont(ETMenuItems.meiryo);
                }

                public String getText() {
                    return "@" + status.getUser().getScreenName() + super.getText();
                }
            }

            menuQuickReply.add(new QuickMenu("おはよう"));
            menuQuickReply.add(new QuickMenu("いってらっしゃい"));
            menuQuickReply.add(new QuickMenu("こんにちは"));
            menuQuickReply.add(new QuickMenu("こんばんは"));
            menuQuickReply.add(new QuickMenu("おかえり"));
            menuQuickReply.add(new QuickMenu("おやすみなさい"));
        }
        list.add(menuQuickReply);
        return list;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        //Object menu = item.getParent();
        // 改善の余地あり
        if (true) {
            tweet(item.getText());
        }
    }

    public void tweet(String text) {
        try {
            Main.getMainController().onTweet(text, twitterUtil);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
