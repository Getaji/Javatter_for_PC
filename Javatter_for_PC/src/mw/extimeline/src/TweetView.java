package mw.extimeline.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import twitter4j.Status;
import twitter4j.User;

import com.orekyuu.javatter.util.IconCache;

public class TweetView extends JDialog {
	private JLabel icon;
	private JLabel labelName;
	private JTextPane textPane;
	private JPanel panelRT;
	private JPanel panelFav;
	
	public TweetView(Status status) {
		if(status.isRetweet()) {
			status = status.getRetweetedStatus();
		}
		User user = status.getUser();
		
		Font meiryo = new Font("Meiryo UI", Font.PLAIN, 12);
		Font meiryoB = new Font("Meiryo UI", Font.BOLD, 12);
		CompoundBorder border = new CompoundBorder(
				new EmptyBorder(5, 5, 5, 5), new LineBorder(Color.BLACK, 1, true));
		CompoundBorder borderIcon = new CompoundBorder(
				new EmptyBorder(5, 5, 5, 5), new LineBorder(Color.BLACK));
		
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		
		JPanel panelIcon = new JPanel();
		panelIcon.setBackground(Color.WHITE);
		panelIcon.setOpaque(true);
		
		{ // アイコンのURLを取得してキャッシュからゲット
			try {
				icon = new JLabel(IconCache.getInstance().getIcon(
						new URL(status.getUser().getProfileImageURL())));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			System.out.println(status.getUser().getProfileImageURL());
			//icon.setBorder(borderIcon);
			icon.setPreferredSize(new Dimension(48, 48));
			icon.setVerticalAlignment(JLabel.TOP);
			panelIcon.add(icon);
		}
		panelMain.add(panelIcon, BorderLayout.WEST);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setAlignmentX(LEFT_ALIGNMENT);
		panelCenter.setBackground(Color.WHITE);
		panelCenter.setBorder(border);
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		panelCenter.setOpaque(true);
		panelCenter.setPreferredSize(new Dimension(400, 100));
		
		{ // ユーザー名を取得してフォーマットし、ラベルに設定
			labelName = new JLabel();
			labelName.setHorizontalAlignment(JLabel.LEFT);
			labelName.setText("@" + user.getScreenName() + " " + user.getName());
			labelName.setFont(meiryoB);
			panelCenter.add(labelName);
		}
		
		{ // テキストを取得してテキストペインに設定
			textPane = new JTextPane();
			textPane.setText(status.getText());
			textPane.setEditable(false);
			textPane.setFont(meiryo);
			textPane.setOpaque(false);
			panelCenter.add(textPane);
		}
		
		JPanel panelBottom = new JPanel();
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
		
		{
			panelRT = new JPanel();
			JLabel labelRTName = new JLabel("RT");
			panelRT.add(labelRTName);
			panelBottom.add(panelRT);
		}
		
		{
			panelFav = new JPanel();
			JLabel labelFavName = new JLabel("Fav");
			panelFav.add(labelFavName);
			panelBottom.add(panelFav);
		}
		
		panelMain.add(panelBottom, BorderLayout.SOUTH);
		
		panelMain.add(panelCenter, BorderLayout.CENTER);
		
		add(panelMain);
		
		
		pack();
		setVisible(true);
	}

}
