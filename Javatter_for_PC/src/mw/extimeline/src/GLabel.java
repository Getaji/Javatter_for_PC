package mw.extimeline.src;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class GLabel extends JLabel implements MouseListener {
	private String imageUrl;
	private String serviceUrl;
	public GLabel(final String url, final String url2) {
		super("loading...", JLabel.CENTER);
		imageUrl = url;
		serviceUrl = url2;
		addMouseListener(this);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK, 2));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);
		setPreferredSize(new Dimension(320, 70));
		
		/* ******** 画像読み込み ******** */
		Thread thread = new Thread() {
			@Override
			public void run() {
				ImageIcon icon;
                int retryCount = 0;
                while (true) {
                    try {
                        icon = new ImageIcon(ImageIO.read(new URL(url)));

                        if (200 < icon.getIconWidth()) {
                            Image image = icon.getImage().getScaledInstance(320,
                                    (int) Utility.getImageHeight(icon, 320.0), Image.SCALE_DEFAULT);
                            setIcon(new ImageIcon(image));
                        } else {
                            setIcon(icon);
                        }
                        setText("");
                    } catch (MalformedURLException e) {
                        if (retryCount++ < 5) {
                            setText("Retrying... : " + retryCount);
                            continue;
                        }
                        setText("Malformed URL!");
                    } catch (IOException e) {
                        if (retryCount++ < 5) {
                            setText("Retrying... : " + retryCount);
                            continue;
                        }
                        setText("Can't read image!");
                    }
                    break;
                }
			}
		};
		thread.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			new ImageViewer(imageUrl, serviceUrl);
		}
	}
	
	/* ******** Unused! ******** */

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
