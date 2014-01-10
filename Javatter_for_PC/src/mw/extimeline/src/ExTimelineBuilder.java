package mw.extimeline.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

import com.orekyuu.javatter.account.AccountManager;
import com.orekyuu.javatter.plugin.TweetObjectBuilder;

public class ExTimelineBuilder implements TweetObjectBuilder {
	private static EmptyBorder border;
	private static Color colorMe;
	private static Color colorRT;
	private static Color colorReply;
	private boolean line;

	static {
		border = new EmptyBorder(3, 3, 3, 3);
		colorMe = new Color(218, 236, 251);
		colorRT = new Color(218, 250, 204);
		colorReply = new Color(255, 205, 204);
	}

	@Override
	public void createdButtonPanel(final JPanel panel, final Status status) {
		/*
		 * JButton buttonReply = (JButton) panel.getComponent(0);
		 * buttonReply.setIcon(ResourceManager.iconReply);
		 * buttonReply.setPressedIcon(ResourceManager.iconReplyHover);
		 * buttonReply.setText("");
		 * 
		 * JToggleButton buttonRetweet = (JToggleButton) panel.getComponent(1);
		 * buttonRetweet.setIcon(ResourceManager.iconRetweet);
		 * buttonRetweet.setPressedIcon(ResourceManager.iconRetweetPress);
		 * buttonRetweet.setText("");
		 * 
		 * JToggleButton buttonFavorite = (JToggleButton) panel.getComponent(2);
		 * buttonFavorite.setIcon(ResourceManager.iconFavorite);
		 * buttonFavorite.setPressedIcon(ResourceManager.iconFavoritePress);
		 * buttonFavorite.setText("");
		 */
		if (!ExTimeline.instance.isAddButton()) {
			return;
		}
		final JButton delButton = new JButton("...");
		delButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x < e.getComponent().getWidth() && x >= 0
						&& y < e.getComponent().getHeight() && y >= 0) {
					createPopupMenu(status, panel).show(delButton, x, y);
				}
			}
		});
		panel.add(delButton, 3);
		try {
			((JTextPane) panel.getComponent(4)).setOpaque(false);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	@Override
	public void createdTextAreaPanel(final JPanel panel, final Status status) {
		if (ExTimeline.instance.isViewImage()) {
			for (MediaEntity entity : status.getMediaEntities()) {
				panel.add(Box.createRigidArea(new Dimension(1, 3)));
				JLabel label = new GLabel(entity.getMediaURL(),
						entity.getExpandedURL());
				panel.add(label);
			}
			for (URLEntity entity : status.getURLEntities()) {
				panel.add(Box.createRigidArea(new Dimension(1, 3)));
				String url = Utility
						.getMatchServiceUrl(entity.getExpandedURL());
				if (!url.equals("NULL")) {
					JLabel label = new GLabel(url, entity.getExpandedURL());
					panel.add(label);
				}
			}
			JLabel name = (JLabel) panel.getComponent(0);
			name.setOpaque(true);
			if (status.isRetweet())
				name.setBackground(colorRT);
			else if (status.getUser().getId() == AccountManager.getInstance().getAccessToken().getUserId())
				name.setBackground(colorMe);
			else if (status.getText().indexOf(
					"@" + AccountManager.getInstance()
					.getAccessToken().getScreenName()) != -1)
				name.setBackground(colorReply);
			else
				name.setBackground(Color.WHITE);
			/*
			if (status.getUser().getId() == AccountManager.getInstance().getAccessToken().getUserId()) {
				name.setHorizontalAlignment(JLabel.RIGHT);
				SimpleAttributeSet attribs = new SimpleAttributeSet();
				StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_RIGHT);
				((JTextPane) panel.getComponent(1)).setParagraphAttributes(attribs, true);
			}
			*/
			name.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
	}

	@Override
	public void createdImagePanel(JPanel panel, final Status status) {
		((JLabel) panel.getComponent(0)).setBorder(border);
		try {
			((JLabel) panel.getComponent(1)).setBorder(border);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		if (status.isRetweet())
			panel.setBackground(colorRT);
		else if (status.getUser().getId() == AccountManager.getInstance().getAccessToken().getUserId())
			panel.setBackground(colorMe);
		else if (status.getText().indexOf(
				"@" + AccountManager.getInstance()
				.getAccessToken().getScreenName()) != -1)
			panel.setBackground(colorReply);
		else
			panel.setBackground(Color.WHITE);
		panel.setOpaque(true);
	}

	@Override
	public void createdObjectPanel(final JPanel panel, final Status status) {
		// panel.setBorder(new LineBorder(Color.BLACK));
		panel.setMaximumSize(new Dimension(417, Integer.MAX_VALUE));
		//panel.setBackground(colorZero);

		if (status.isRetweet())
			panel.setBackground(colorRT);
		else if (status.getText().indexOf(
				"@" + AccountManager.getInstance().getAccessToken().getScreenName()) != -1)
			panel.setBackground(colorReply);
		else
			panel.setBackground(Color.WHITE);
		line = !line;
		panel.setOpaque(true);
		
		/*
		if (status.getUser().getId() == AccountManager.getInstance().getAccessToken().getUserId()) {
			JPanel panelIcon = (JPanel) panel.getComponent(0);
			panel.remove(0);
			panel.add(panelIcon, BorderLayout.EAST);
		}
		*/
		
		panel.repaint();
	}

	private JPopupMenu createPopupMenu(final Status status, final JPanel parent) {
    	final JPopupMenu menu = new JPopupMenu();
        

        // ▼プラグイン
        for (IMenuItemBuilder i : ExTimeline.instance.getMenuItemList()) {
            for (JMenuItem j : i.getMenuItem(parent, status)) {
                menu.add(j);
            }
        }
        return menu;
    }

	/**
	 * 正規表現に一致した文字列をリストに代入して返す.
	 * 
	 * @param str
	 *            原文
	 * @param reg
	 *            正規表現
	 * @return 一致リスト
	 */
	public List<String> extraction(String str, String reg) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	public boolean isMatch(String reg, String str) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
}
