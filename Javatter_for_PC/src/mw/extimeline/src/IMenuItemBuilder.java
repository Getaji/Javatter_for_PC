package mw.extimeline.src;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import twitter4j.Status;

/**
 * ExTimelineで追加させるメニューを取得するためのインターフェース
 *
 */
public interface IMenuItemBuilder {
	/**
	 * ExTimeline側でTweetObjectBuilderが呼ばれた際、<br>
	 * メニュー追加の最後に追加するJMenuItemのリストがここで取得されます。
	 * @param panel JPanel
	 * @param status Status
	 * @return JMenuItemのリスト
	 */
	public List<JMenuItem> getMenuItem(JPanel panel, Status status);
}
