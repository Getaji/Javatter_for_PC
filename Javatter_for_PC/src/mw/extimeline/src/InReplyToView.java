package mw.extimeline.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import twitter4j.Status;
import twitter4j.TwitterException;

import com.orekyuu.javatter.account.TwitterManager;
import com.orekyuu.javatter.plugin.JavatterPluginLoader;
import com.orekyuu.javatter.util.TweetObjectFactory;

/**
 * In_Reply_Toを表示するダイアログ.
 * @author Getaji
 *
 */
public class InReplyToView extends JDialog {
	
	/** ステータスIDのリスト. */
	private List<Long> statusList = new ArrayList<Long>();
	
	/** メインのステータスのID. */
	private long mainStatus;
	
	/** 定数. */
	private enum Data {
		/** ダイアログのサイズ. */
		WIDTH(200), HEIGHT(600);
		
		/** データ. */
		private int value;
		
		/** データを取得して設定.
		 * @param val データ. 
		 */
		private Data(int val) { value = val; }
		
		/**
		 *  データを返す.
		 *  @return データ 
		 */
		@SuppressWarnings("unused")
		public int get() { return value; }
	}
	
	/**
	 * 初期化.
	 * @param status メインステータス
	 */
	public InReplyToView(Status status) {
		super(ExTimeline.instance.getView().getMainFrame(), "会話を表示");
		setLayout(new BorderLayout());
		mainStatus = status.getId();
		setMaximumSize(new Dimension(Data.WIDTH.value, Data.WIDTH.value));
		getStatuses(status);
		view();
		setVisible(true);
	}
	
	/**
	 * 関連ステータスを取得してIDをステータスリストに格納する.
	 * @param status メインステータス
	 */
	private void getStatuses(Status status) {
		statusList.add(status.getId());
		long bufStatus = status.getInReplyToStatusId();
		
		while (bufStatus != -1) {
			statusList.add(bufStatus);
			try {
				bufStatus = TwitterManager.getInstance()
						.getTwitter().showStatus(bufStatus)
						.getInReplyToStatusId();
			} catch (TwitterException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	/**
	 * 表示.
	 */
	private void view() {
		Collections.reverse(statusList);
		JTabbedPane tab = new JTabbedPane();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(panel);
		tab.addTab("InReplyTo", scroll);
		for (long id : statusList) {
			try {
				Status status = TwitterManager.getInstance().getTwitter().showStatus(id);
				if (status.isRetweet()) {
					status = status.getRetweetedStatus();
				}
				JPanel panelTweet = createObject(status);
				panel.add(panelTweet);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		add(tab);
		pack();
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
