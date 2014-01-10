package mw.bouyomip.src;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.view.IJavatterTab;

/**
 * 初期・主要処理を行います.
 * @author Getaji
 *
 */
public class BouyomiPlugin extends JavatterPlugin {
	
	@Override
	public void init() {
		BouyomiSaveData.INSTANCE.init(getSaveData());
		addUserStreamListener(new BouyomiStreamListener());
	}

	@Override
	public String getPluginName() {
		// TODO 自動生成されたメソッド・スタブ
		return "棒読みちゃん読み上げプラグイン";
	}

	@Override
	public String getVersion() {
		// TODO 自動生成されたメソッド・スタブ
		return "1.0";
	}
	
	@Override
	protected IJavatterTab getPluginConfigViewObserver(){
		return new BouyomiConfig();
	}
}
