package mw.bouyomip.src;

import java.text.SimpleDateFormat;

import twitter4j.Status;

import com.orekyuu.javatter.controller.UserStreamController;

/**
 * UserStreamを受信して棒読みちゃんに渡します.
 * @author Getaji
 *
 */
public class BouyomiStreamListener extends UserStreamController {

	private BouyomiSaveData save = BouyomiSaveData.INSTANCE;
	
	/** 棒読みちゃんコネクター. */
	private BouyomiChan4J bouyomi = new BouyomiChan4J();
	
	@Override
	public void onStatus(Status status) {
		if (BouyomiSaveData.INSTANCE.getBoolean("enable")) {
			if (BouyomiSaveData.INSTANCE.getBoolean("default")) {
				bouyomi.talk(save.getInt("port"), format(status));
			} else {
				bouyomi.talk(save.getInt("port"), save.getInt("volume"),
						save.getInt("speed"), save.getInt("tone"),
						save.getInt("voice"), format(status));
			}
		}
	}
	
	/**
	 * Statusを読み上げテキストにフォーマットします.
	 * @param status Status
	 * @return フォーマットしたテキスト
	 */
	public String format(Status status) {
		String res;
		SimpleDateFormat dateFormat = new SimpleDateFormat(save.getString("format_date"));
		if (status.isRetweet()) {
			res = save.getString("format_retweet");
			res = res.replace("$rtuser", status.getUser().getScreenName()); // $rtuser
			res = res.replace("$rtname", status.getUser().getName()); // $rtname
			res = res.replace("$user", status.getRetweetedStatus().getUser().getScreenName()); // $user
			res = res.replace("$name", status.getRetweetedStatus().getUser().getName()); // $name
			res = res.replace("$text", status.getRetweetedStatus().getText()); // $text
			res = res.replace("$date", dateFormat.format(status.getCreatedAt())); // $date
			res = res.replace("$rtvia", status.getSource().replace("<.+>", ""));
		} else {
			res = save.getString("format_normal");
			res = res.replace("$user", status.getUser().getScreenName()); // $user
			res = res.replace("$name", status.getUser().getName()); // $name
			res = res.replace("$text", status.getText()); // $text
			res = res.replace("$date", dateFormat.format(status.getCreatedAt())); // $date
			res = res.replace("$via", status.getSource().replace("<.+>", ""));
		}
		
		return res;
	}

}
