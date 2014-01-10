package mw.extimeline.src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.orekyuu.javatter.view.IJavatterTab;

public class ETConfigView implements IJavatterTab, ActionListener {
    private JPanel panelTop;
    private JCheckBox checkDeleteTweet;
    private JCheckBox checkViewImage;
    private JButton buttonOK;
    private JButton buttonCancel;

    @Override
    public Component getComponent() {
        panelTop = new JPanel();
        panelTop.setLayout(new BorderLayout());

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        checkDeleteTweet = new JCheckBox("その他ボタンを追加");
        checkDeleteTweet.setSelected(ExTimeline.instance.isAddButton());
        panelCenter.add(checkDeleteTweet);

        checkViewImage = new JCheckBox("画像をTLに表示");
        checkViewImage.setSelected(ExTimeline.instance.isViewImage());
        panelCenter.add(checkViewImage);

        panelTop.add(panelCenter, "Center");

        JPanel panelLast = new JPanel();
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("キャンセル");
        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);
        panelLast.add(buttonOK);
        panelLast.add(buttonCancel);

        panelTop.add(panelLast, "Last");

        return panelTop;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource().equals(buttonOK)) {
            JDialog d = (JDialog)SwingUtilities.getWindowAncestor(panelTop);

            ExTimeline.instance.getData().setBoolean("deleteTweet", checkDeleteTweet.isSelected());
            ExTimeline.instance.setAddButton(checkDeleteTweet.isSelected());

            ExTimeline.instance.getData().setBoolean("viewimage", checkViewImage.isSelected());
            ExTimeline.instance.setViewImage(checkViewImage.isSelected());

            System.out.println("[ExTimeline]設定の保存に成功");
            d.setVisible(false);
        } else if (ev.getSource().equals(buttonCancel)){
            JDialog d = (JDialog)SwingUtilities.getWindowAncestor(panelTop);
            d.setVisible(false);
        }
    }

}
