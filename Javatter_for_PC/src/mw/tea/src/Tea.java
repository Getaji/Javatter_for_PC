package mw.tea.src;

import com.orekyuu.javatter.plugin.JavatterPlugin;

/**
 * お茶ウマー.
 * @author Getaji
 */
public class Tea extends JavatterPlugin {
    private TeaRoomView view = new TeaRoomView();
    private TeaRoomModel model = new TeaRoomModel();
    private TeaRoomViewModel viewModel = new TeaRoomViewModel(view, model);
    @Override
    public void init() {
        addMenuTab("茶室", view);
    }

    @Override
    public String getPluginName() {
        return "お茶が入りましたよ";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }
}
