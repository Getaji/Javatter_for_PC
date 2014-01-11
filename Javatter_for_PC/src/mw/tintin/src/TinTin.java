package mw.tintin.src;

import com.orekyuu.javatter.plugin.JavatterPlugin;
import com.orekyuu.javatter.view.IJavatterTab;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        final JPanel panelForm = new JPanel();
        final GroupLayout groupLayout = new GroupLayout(panelForm);
        final JLabel labelWord = new JLabel("Word");
        final JLabel labelOrgasm = new JLabel("Orgasm");
        final JLabel labelNotOrgasm = new JLabel("Not orgasm");
        final JLabel labelTryCount = new JLabel("Try");
        final JTextField fieldWord = new JTextField(penis);
        final JTextField fieldOrgasm = new JTextField(temp);
        final JTextField fieldNotOrgasm = new JTextField(tempnot);
        final JSpinner spinnerTryCount = new JSpinner();
        panelForm.setLayout(groupLayout);
        spinnerTryCount.setValue(max);
        panelForm.setBorder(new EmptyBorder(4, 4, 4, 4));
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(labelWord)
                                .addComponent(labelOrgasm)
                                .addComponent(labelNotOrgasm)
                                .addComponent(labelTryCount))
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(fieldWord)
                                .addComponent(fieldOrgasm)
                                .addComponent(fieldNotOrgasm)
                                .addComponent(spinnerTryCount))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(labelWord)
                                .addComponent(fieldWord))
                        .addGroup(groupLayout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(labelOrgasm)
                                .addComponent(fieldOrgasm))
                        .addGroup(groupLayout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(labelNotOrgasm)
                                .addComponent(fieldNotOrgasm))
                        .addGroup(groupLayout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(labelTryCount)
                                .addComponent(spinnerTryCount))
        );
		panel.add(panelForm, BorderLayout.NORTH);
		
		final JTextArea area = new JTextArea();
		final JScrollPane scroll = new JScrollPane();
        final JScrollBar scrollBar = scroll.getVerticalScrollBar();
		scroll.setViewportView(area);
		area.setLineWrap(true);
		area.setEditable(false);
		
		panel.add(scroll, BorderLayout.CENTER);
		
		final JPanel panelButton = new JPanel();
		final JButton buttonGo = new JButton("Go");
		final JButton buttonSet = new JButton("Set tweet area");
		buttonGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Thread thread = new Thread() {
					public void run() {
						buttonGo.setEnabled(false);
						penis = fieldWord.getText();
                        temp = fieldOrgasm.getText();
                        tempnot = fieldNotOrgasm.getText();
						max = Integer.parseInt(spinnerTryCount.getValue().toString());
						area.setText("");
						count = 1;
						while (true) {
							Character c = go();
							area.append(c.toString());
							if (area.getText().endsWith(penis) || max <= count) break;
							++count;
                            try {
                                sleep(10);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            scrollBar.setValue(scrollBar.getMaximum());
                        }
						area.append("\n\n" + limit());
                        scrollBar.setValue(scrollBar.getMaximum());
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
