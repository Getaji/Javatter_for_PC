package mw.bouyomip.src;

import java.util.HashMap;
import java.util.Map;

import com.orekyuu.javatter.util.SaveData;

/**
 * 設定を管理するクラスです.<br />
 * <code>com.orekyuu.javatter.util.SaveData</code>のラッパーとなっています.
 * @author Getaji
 *
 */
public enum BouyomiSaveData {
	
	/** インスタンス. */
	INSTANCE;
	
	/** SaveData. */
	private SaveData saveData;
	
	/** 設定を保管してあるマップ. */
	private Map<String, Object> map = new HashMap<>();

	/** デフォルト設定を保管してあるマップ. */
	private Map<String, Object> mapDefault = new HashMap<>();
	
	/**
	 * 初期化処理.
	 * @param saveData SaveData
	 */
	public void init(SaveData saveData) {
		this.saveData = saveData;
		setDefault();
		load();
	}
	
	/**
	 * 設定値をint型で返します.
	 * @param key キー
	 * @return 値
	 */
	public int getInt(String key) {
		return (int) map.get(key);
	}
	
	/**
	 * 設定値をString型で返します.
	 * @param key キー
	 * @return 値
	 */
	public String getString(String key) {
		return (String) map.get(key);
	}
	
	/**
	 * 設定値をboolean型で返します.
	 * @param key キー
	 * @return 値
	 */
	public boolean getBoolean(String key) {
		return (boolean) map.get(key);
	}
	
	/**
	 * 初期値をint型で返します.
	 * @param key キー
	 * @return 値
	 */
	public int getDefaultInt(String key) {
		return (int) map.get(key);
	}
	
	/**
	 * 初期値をString型で返します.
	 * @param key キー
	 * @return 値
	 */
	public String getDefaultString(String key) {
		return (String) map.get(key);
	}
	
	/**
	 * 初期値をboolean型で返します.
	 * @param key キー
	 * @return 値
	 */
	public boolean getDefaultBoolean(String key) {
		return (boolean) map.get(key);
	}
	
	/**
	 * int型の値を設定します.
	 * @param key キー
	 * @param value 値
	 */
	public void setInt(String key, int value) {
		map.put(key, value);
		saveData.setInt(key, value);
	}
	
	/**
	 * String型の値を設定します.
	 * @param key キー
	 * @param value 値
	 */
	public void setString(String key, String value) {
		map.put(key, value);
		saveData.setString(key, value);
	}
	
	/**
	 * boolean型の値を設定します.
	 * @param key キー
	 * @param value 値
	 */
	public void setBoolean(String key, boolean value) {
		map.put(key, value);
		saveData.setBoolean(key, value);
	}
	
	@Deprecated
	public void save(Map<String, Object> map) {
		for(Map.Entry<String, Object> entry : map.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		save();
	}
	
	/**
	 * デフォルト値を設定します.<br />
	 * <code>mapDefault</code>への格納は一度<code>mapDefault</code>をクリアしてから行われます.
	 */
	private void setDefault() {
		saveData.setDefaultValue("enable", true);
		saveData.setDefaultValue("default", false);
		saveData.setDefaultValue("format_normal", "$nameさんのツイート $text");
		saveData.setDefaultValue("format_retweet", "$rtnameさんの$nameさんからのリツイート $text");
		saveData.setDefaultValue("format_date", "HHじmmふん");
		saveData.setDefaultValue("volume", 60);
		saveData.setDefaultValue("speed", 100);
		saveData.setDefaultValue("tone", 100);
		saveData.setDefaultValue("voice", 1);
		saveData.setDefaultValue("port", 50001);
		
		mapDefault.clear();
		mapDefault.put("enable", true);
		mapDefault.put("default", false);
		mapDefault.put("format_normal", "$nameさんのツイート $text");
		mapDefault.put("format_retweet", "$rtnameさんの$nameさんからのリツイート $text");
		mapDefault.put("format_date", "HHじmmふん");
		mapDefault.put("volume", -1);
		mapDefault.put("speed", -1);
		mapDefault.put("tone", -1);
		mapDefault.put("voice", 0);
		mapDefault.put("port", 50001);
	}
	
	/**
	 * 設定を読み込みます.<br />
	 * <code>map</code>への格納は一度<code>map</code>をクリアしてから行われます.
	 */
	private void load() {
		map.clear();
		map.put("enable", saveData.getBoolean("enable"));
		map.put("default", saveData.getBoolean("default"));
		map.put("format_normal", saveData.getString("format_normal"));
		map.put("format_retweet", saveData.getString("format_retweet"));
		map.put("format_date", saveData.getString("format_date"));
		map.put("volume", saveData.getInt("volume"));
		map.put("speed", saveData.getInt("speed"));
		map.put("tone", saveData.getInt("tone"));
		map.put("voice", saveData.getInt("voice"));
		map.put("port", saveData.getInt("port"));
	}
	
	@Deprecated
	private void save() {
		saveData.setBoolean("enable", getBoolean("enable"));
		saveData.setBoolean("default", getBoolean("default"));
		saveData.setString("format_normal", getString("format_normal"));
		saveData.setString("format_retweet", getString("format_retweet"));
		saveData.setString("format_date", getString("format_date"));
		saveData.setInt("volume", getInt("volume"));
		saveData.setInt("speed", getInt("speed"));
		saveData.setInt("tone", getInt("tone"));
		saveData.setInt("voice", getInt("voice"));
	}
}
