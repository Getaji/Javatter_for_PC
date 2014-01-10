package mw.tea.src;

/**
 * 茶室表示Viewの状況を茶室管理Modelに伝えます.
 * @author Getaji
 */
public class TeaRoomViewModel {
    private TeaRoomView view;
    private TeaRoomModel model;
    public TeaRoomViewModel(TeaRoomView view, TeaRoomModel model) {
        this.view = view;
        this.model = model;
    }
}
