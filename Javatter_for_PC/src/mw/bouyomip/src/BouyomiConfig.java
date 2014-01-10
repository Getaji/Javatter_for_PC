package mw.bouyomip.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import com.orekyuu.javatter.view.IJavatterTab;

/**
 * 設定画面表示クラスです.
 * @author Getaji
 *
 */
public class BouyomiConfig implements IJavatterTab {
	
	/** セーブデータ. */
	private BouyomiSaveData save = BouyomiSaveData.INSTANCE;
	
	private final Font meiryo = new Font("Meiryo", Font.PLAIN, 13);
	
	private final String[] voice = 
		{"女性1", "女性2", "男性1", "男性2", "中性", "ロボット", "機械1", "機械2"};
	
	/** 棒読みちゃんコネクター. */
	private BouyomiChan4J bouyomi = new BouyomiChan4J();
	
	
	@Override
	public Component getComponent() {
		final JPanel panel = new JPanel();
		final JPanel panelCenter = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		panelCenter.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final Dimension size = new Dimension(370, 22);

		final JCheckBox checkEnable = new JCheckBox("読み上げを有効にする");
		final JCheckBox checkDefault = new JCheckBox("デフォルト設定を使う");
		final JTextField fieldFormatNormal = new JTextField(save.getString("format_normal"));
		final JTextField fieldFormatRetweet = new JTextField(save.getString("format_retweet"));
		final JTextField fieldFormatDate = new JTextField(save.getString("format_date"));
		final JSlider sliderVolume = new JSlider();
		final JSlider sliderSpeed = new JSlider();
		final JSlider sliderTone = new JSlider();
		final JComboBox<String> comboVoice = new JComboBox<>(voice);
		final JTextField fieldPort = new JTextField(String.valueOf(save.getInt("port")));

		final Dimension sizeSlider = new Dimension(370, 40);
		final Dimension sizeBox = new Dimension(100, 28);
		final boolean isDefault = !save.getBoolean("default");

		checkEnable.setSelected(save.getBoolean("enable"));
		checkEnable.setFont(meiryo);
		checkDefault.setSelected(save.getBoolean("default"));
		checkDefault.setFont(meiryo);
		checkDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				boolean def = !checkDefault.isSelected();
				fieldFormatNormal.setEnabled(def);
				fieldFormatRetweet.setEnabled(def);
				fieldFormatDate.setEnabled(def);
				sliderVolume.setEnabled(def);
				sliderSpeed.setEnabled(def);
				sliderTone.setEnabled(def);
				comboVoice.setEnabled(def);
				fieldPort.setEnabled(def);
			}
		});
		fieldFormatNormal.setPreferredSize(size);
		fieldFormatRetweet.setPreferredSize(size);
		fieldFormatDate.setPreferredSize(size);
		sliderVolume.setPreferredSize(sizeSlider);
		sliderVolume.setMaximum(100);
		sliderVolume.setMinimum(0);
		sliderVolume.setValue(save.getInt("volume"));
		sliderVolume.setLabelTable(sliderVolume.createStandardLabels(20));
		sliderVolume.setPaintLabels(true);
		sliderSpeed.setPreferredSize(sizeSlider);
		sliderSpeed.setMaximum(300);
		sliderSpeed.setMinimum(50);
		sliderSpeed.setValue(save.getInt("speed"));
		sliderSpeed.setLabelTable(sliderSpeed.createStandardLabels(50, 50));
		sliderSpeed.setPaintLabels(true);
		sliderTone.setPreferredSize(sizeSlider);
		sliderTone.setMaximum(300);
		sliderTone.setMinimum(50);
		sliderTone.setValue(save.getInt("tone"));
		sliderTone.setLabelTable(sliderTone.createStandardLabels(50, 50));
		sliderTone.setPaintLabels(true);
		comboVoice.setFont(meiryo);
		comboVoice.setPreferredSize(sizeBox);
		fieldPort.setPreferredSize(sizeBox);
		
		fieldFormatNormal.setEnabled(isDefault);
		fieldFormatRetweet.setEnabled(isDefault);
		fieldFormatDate.setEnabled(isDefault);
		sliderVolume.setEnabled(isDefault);
		sliderSpeed.setEnabled(isDefault);
		sliderTone.setEnabled(isDefault);
		comboVoice.setEnabled(isDefault);
		fieldPort.setEnabled(isDefault);

		panelCenter.add(checkEnable);
		panelCenter.add(checkDefault);
		panelCenter.add(Box.createRigidArea(new Dimension(400, 7)));
		panelCenter.add(createLabel("通常ツイートの発音フォーマット :"));
		panelCenter.add(fieldFormatNormal);
		panelCenter.add(Box.createRigidArea(new Dimension(400, 3)));
		panelCenter.add(createLabel("リツイートの発音フォーマット :"));
		panelCenter.add(fieldFormatRetweet);
		panelCenter.add(Box.createRigidArea(new Dimension(400, 3)));
		panelCenter.add(createLabel("日時の発音フォーマット :"));
		panelCenter.add(fieldFormatDate);
		panelCenter.add(Box.createRigidArea(new Dimension(400, 10)));
		panelCenter.add(createLabel("音量 :"));
		panelCenter.add(sliderVolume);
		panelCenter.add(createLabel("速度 :"));
		panelCenter.add(sliderSpeed);
		panelCenter.add(createLabel("音程 :"));
		panelCenter.add(sliderTone);
		panelCenter.add(Box.createRigidArea(new Dimension(400, 10)));
		panelCenter.add(createLabel("声質 :"));
		panelCenter.add(Box.createRigidArea(new Dimension(6, 10)));
		panelCenter.add(comboVoice);
		panelCenter.add(Box.createRigidArea(new Dimension(12, 10)));
		panelCenter.add(createLabel("ポート番号 :"));
		panelCenter.add(fieldPort);
		
		panel.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelButton = new JPanel();
		JButton buttonOK = new JButton("OK");
		JButton buttonCancel = new JButton("Cancel");
		JButton buttonDefault = new JButton("Default");
		JButton buttonTest = new JButton("Test");
		setBackground(panelButton, Color.LIGHT_GRAY);
		buttonOK.setBackground(Color.WHITE);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				save.setBoolean("enable", checkEnable.isSelected());
				save.setBoolean("default", checkDefault.isSelected());
				save.setString("format_normal", fieldFormatNormal.getText());
				save.setString("format_retweet", fieldFormatRetweet.getText());
				save.setString("format_date", fieldFormatDate.getText());
				save.setInt("volume", sliderVolume.getValue());
				save.setInt("speed", sliderSpeed.getValue());
				save.setInt("tone", sliderTone.getValue());
				save.setInt("voice", comboVoice.getSelectedIndex() + 1);
				save.setInt("port", Integer.parseInt(fieldPort.getText()));
				panel.getParent().getParent().getParent().getParent().setVisible(false);
			}
		});
		buttonCancel.setBackground(Color.WHITE);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				panel.getParent().getParent().getParent().getParent().setVisible(false);
			}
		});
		buttonDefault.setBackground(Color.WHITE);
		buttonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int result = JOptionPane.showConfirmDialog(panel, "デフォルト設定に復元しますか？",
						"デフォルトの復元", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					checkEnable.setSelected(save.getDefaultBoolean("enable"));
					checkDefault.setSelected(save.getDefaultBoolean("default"));
					fieldFormatNormal.setText(save.getDefaultString("format_normal"));
					fieldFormatRetweet.setText(save.getDefaultString("format_retweet"));
					fieldFormatDate.setText(save.getDefaultString("format_date"));
					sliderVolume.setValue(save.getDefaultInt("volume"));
					sliderSpeed.setValue(save.getDefaultInt("speed"));
					sliderTone.setValue(save.getDefaultInt("tone"));
					comboVoice.setSelectedIndex(save.getDefaultInt("voice") - 1);
					fieldPort.setText(save.getDefaultString("port"));
				}
			}
		});
		buttonTest.setBackground(Color.WHITE);
		buttonTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				bouyomi.talk(Integer.parseInt(fieldPort.getText()), sliderVolume.getValue(), sliderSpeed.getValue(),
						sliderTone.getValue(), comboVoice.getSelectedIndex() + 1,
						"こんなかんじで発音します。");
			}
		});
		
		panelButton.add(buttonOK);
		panelButton.add(buttonCancel);
		panelButton.add(buttonDefault);
		panelButton.add(buttonTest);
		
		panel.add(panelButton, BorderLayout.SOUTH);
		
		final Timer timer = new Timer(1, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    JDialog dialog = (JDialog)
                            panel.getParent().getParent()
                            .getParent().getParent();
                    dialog.setBounds(0, 0, 410, 600);
                } catch (NullPointerException e) {
                    timer.restart();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
		
		return panel;
	}
	
	private void setBackground(JPanel panel, Color color) {
		panel.setBackground(color);
		panel.setOpaque(true);
	}
	
	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(meiryo);
		return label;
	}
}
