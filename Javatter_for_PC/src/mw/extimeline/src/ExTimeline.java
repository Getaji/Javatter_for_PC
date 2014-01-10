package mw.extimeline.src;

import java.util.ArrayList;
import java.util.List;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.util.SaveData;
import com.orekyuu.javatter.view.IJavatterTab;
import com.orekyuu.javatter.view.MainWindowView;

/**
 * @author Getaji / Margherita Works
 *
 */

public class ExTimeline extends JavatterPlugin {
    private boolean deleteTweet;
    private boolean isViewImage;
    private boolean isAutoGuess;
    
    public static ExTimeline instance;

    private String username;

    private SaveData saveData;
    private MainWindowView view;

    private List<IMenuItemBuilder> listMenuItemBuilder;

    public ExTimeline() {
    	instance = this;
        listMenuItemBuilder = new ArrayList<IMenuItemBuilder>();
        this.addMenuItemBuilder(new ETMenuItems());
    }

    @Override
    public String getPluginName() {
        return "ExTimeline";
    }

    @Override
    public String getVersion() {
        return "build131221:next1.4";
    }

    @Override
    public void init() {
        getSaveData().setDefaultValue("deleteTweet", true);
        getSaveData().setDefaultValue("viewimage", true);
        getSaveData().setDefaultValue("guess", true);

        deleteTweet = getSaveData().getBoolean("deleteTweet");
        isViewImage = getSaveData().getBoolean("viewimage");
        isAutoGuess = getSaveData().getBoolean("guess");

        username = userName;
        saveData = getSaveData();
        view = this.getMainView();
        ResourceManager.load();
        addTweetObjectBuider(new ExTimelineBuilder());
    }

    protected IJavatterTab getPluginConfigViewObserver() {
        return new ETConfigView();
    }

    public static void send(Object text) {
        System.out.println(text);
    }

    public List<IMenuItemBuilder> getMenuItemList() {
        return listMenuItemBuilder;
    }
    
    public void addMenuItemBuilder(IMenuItemBuilder builder) {
    	listMenuItemBuilder.add(builder);
    }
    
    public MainWindowView getView() {
    	return getMainView();
    }
    
    public SaveData getData() {
    	return getSaveData();
    }
    
    public boolean isAddButton() {
    	return deleteTweet;
    }
    
    public boolean isViewImage() {
    	return isViewImage;
    }

    public boolean isAutoGuess() {
        return isAutoGuess;
    }
    
    public void setAddButton(boolean add) {
    	deleteTweet = add;
    }
    
    public void setViewImage(boolean view) {
    	isViewImage = view;
    }
}
