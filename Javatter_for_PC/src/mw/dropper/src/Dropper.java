package mw.dropper.src;

import com.orekyuu.javatter.plugin.JavatterPlugin;

import java.util.ArrayList;
import java.util.List;


public class Dropper extends JavatterPlugin {
    private List<String> listFilter = new ArrayList<>();
    @Override
    public void init() {
        DropTab dropTab = new DropTab();
        this.addUserStreamTab("Drop", dropTab);
        this.addMenuTab("DropList", new DropListTab(listFilter));
        this.addUserStreamListener(new DropperListener(dropTab, listFilter));
    }

    @Override
    public String getPluginName() {
        return "Dropper";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
