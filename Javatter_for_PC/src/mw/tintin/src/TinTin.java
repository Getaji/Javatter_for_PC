package mw.tintin.src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.view.IJavatterTab;

/**
 * 何も言うことはない.
 * @author Getaji
 *
 */
public class TinTin extends JavatterPlugin implements IJavatterTab {
	private String penis = "おちんぽ";
	private int count = 1;
	private int max = 10000;
	private StringBuilder b = new StringBuilder();
	private String temp = "おぉぉおﾞおﾞ～っ！！イグゥウ！！イッグゥウウ！！$n私は$c回目で果てました...";
	private String tempnot = "$m回やってみましたがイキませんでした...";

	@Override
	public void init() {
		this.addMenuTab("TinTin", this);
	}

	@Override
	public String getPluginName() {
		return "TinTin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public Component getComponent() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		final JTextField field = new JTextField(penis + "@@" + temp + "@@" + tempnot + "@@" + max);
		panel.add(field, BorderLayout.NORTH);
		
		final JTextArea area = new JTextArea();
		final JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(area);
		area.setLineWrap(true);
		area.setEditable(false);
		
		panel.add(scroll, BorderLayout.CENTER);
		
		final JPanel panelButton = new JPanel();
		final JButton buttonGo = new JButton("Go");
		final JButton buttonSet = new JButton("ツイート欄にセット");
		buttonGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (field.getText().split("@@").length < 4) {
					area.setText("フォーマットちゃうで");
					return;
				}
				final Thread thread = new Thread() {
					public void run() {
						buttonGo.setEnabled(false);
						String[] sp = field.getText().split("@@", 4);
						penis = sp[0]; temp = sp[1]; tempnot = sp[2];
						max = Integer.parseInt(sp[3]);
						area.setText("");
						count = 1;
						while (true) {
							Character c = go();
							area.append(c.toString());
							if (area.getText().endsWith(penis) || max <= count) break;
							++count;
						}
						scroll.getViewport().scrollRectToVisible(new Rectangle(0, Integer.MAX_VALUE - 1, 1, 1));
						area.append("\n\n" + limit());
						buttonGo.setEnabled(true);
					}
				};
				thread.start();
			}
		});
		buttonSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getMainView().getTweetTextArea().setText(limit());
			}
		});
		
		panelButton.add(buttonGo);
		panelButton.add(buttonSet);
		panel.add(panelButton, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public char go() {
		return penis.toCharArray()[(int)Math.floor(Math.random()*(penis.length()))];
	}
	
	public String limit() {
		return count < max ? replace(temp) : replace(tempnot);
	}
	
	public String replace(String str) {
		return str.replace("$c", String.valueOf(count))
				.replace("$n", "\n")
				.replace("$m", String.valueOf(max));
	}

}
