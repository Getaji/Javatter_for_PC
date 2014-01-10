package mw.dropper.src;

import com.orekyuu.javatter.controller.UserStreamController;
import com.orekyuu.javatter.view.IJavatterTab;
import twitter4j.Status;

import java.util.List;


public class DropperListener extends UserStreamController {
    private DropTab dropTab;
    private List<String> filterList;

    public DropperListener(DropTab dropTab, List filterList) {
        this.dropTab = dropTab;
        this.filterList = filterList;
    }

    @Override
    public void onStatus(Status status) {
        for (String filter: filterList)
            if (status.getText().indexOf(filter) != -1) {
                dropTab.addStatus(status);
                break;
            }
    }
}
