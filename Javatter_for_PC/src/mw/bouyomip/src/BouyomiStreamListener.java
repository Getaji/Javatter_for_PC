package mw.bouyomip.src;

import com.orekyuu.javatter.controller.UserStreamController;
import gutil.CustomFormat;
import twitter4j.Status;

import java.text.SimpleDateFormat;

/**
 * UserStreamを受信して棒読みちゃんに渡します.
 * @author Getaji
 *
 */
public class BouyomiStreamListener extends UserStreamController {

	private final BouyomiSaveData save = BouyomiSaveData.INSTANCE;
	
	/** 棒読みちゃんコネクター. */
	private final BouyomiChan4J bouyomi = new BouyomiChan4J();

    private final CustomFormat normalFormat = new CustomFormat();

    private final CustomFormat retweetFotmat = new CustomFormat();

    public BouyomiStreamListener() {
        retweetFotmat.addPatterns("$rtuser", "$rtname", "$user", "$name",
                                  "$text", "$rtdate", "$date", "$rtvia", "$via");
        normalFormat.addPatterns("$user", "$name", "$text", "$date", "$via");

    }
	
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
            retweetFotmat.addObjects(true, status.getUser().getScreenName(),
                    status.getUser().getName(),
                    status.getRetweetedStatus().getUser().getScreenName(),
                    status.getRetweetedStatus().getUser().getName(),
                    status.getRetweetedStatus().getText(),
                    dateFormat.format(status.getCreatedAt()),
                    dateFormat.format(status.getRetweetedStatus().getCreatedAt()),
                    status.getSource().replace("<.+>", ""),
                    status.getRetweetedStatus().getSource().replace("<.+>", ""));
            res = retweetFotmat.format(save.getString("format-retweet"), true);
		} else {
            normalFormat.addObjects(true, status.getUser().getScreenName(),
                    status.getUser().getName(),
                    status.getText(),
                    dateFormat.format(status.getCreatedAt()),
                    status.getSource().replace("<.+>", ""));
            res = normalFormat.format(save.getString("format_normal"), true);
		}
		
		return res;
	}

}
