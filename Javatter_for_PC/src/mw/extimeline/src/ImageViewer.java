package mw.extimeline.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

/**
 * 画像を表示するウィンドウ.<br />
 * このクラスは画像サービスの画像表示をJavatter内で完結させるために作られたクラスです.<br />
 * そのため,このクラスは画像提供元のURLも要求します.
 * @author Getaji
 *
 */
public class ImageViewer extends JDialog implements MouseListener {

    /** 画像表示ラベル. */
    private JLabel label;

    /** 画像パス. */
    private String imageUrl;

    /** 画像提供元URL. */
    private String serviceUrl;

    /** ウィンドウの最大横幅. */
    private static final int MAX_WIDTH = 800;

    /** ウィンドウの最大縦幅. */
    private static final int MAX_HEIGHT = 600;

    /** 画像のサイズ. */
    private int width = 0, height = 0;

    public ImageViewer(final String imgurl, final String svurl) {
        super(ExTimeline.instance.getView().getMainFrame(), "インナービューワー");

        imageUrl = imgurl;
        serviceUrl = svurl;

        label = new JLabel("Loading...", JLabel.CENTER);
        label.setFont(new Font("Meiryo UI", Font.BOLD, 30));
        label.addMouseListener(this);

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(label, "Center");
		
		/* ******** 画像を別スレッドで読み込みます. ******** */
        Thread thread = new Thread() {
            @Override
            public void run() {
                ImageIcon icon;
                Image image;
                int retryCount = 0;
                while(true) {
                    try {
                        // 受け取ったURLから画像を読み込み一旦ImageIconへ
                        icon = new ImageIcon(ImageIO.read(new URL(imgurl)));

                        if (MAX_WIDTH < icon.getIconWidth()
                                && icon.getIconHeight() < icon.getIconWidth()) {
                            width = MAX_WIDTH;
                            height = (int) Utility.getImageHeight(icon, MAX_WIDTH);
                            // 画像を高画質モードで縮小
                            image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

                        } else if (MAX_HEIGHT < icon.getIconHeight()
                                && icon.getIconWidth() < icon.getIconHeight()) {
                            width = (int) Utility.getImageWidth(icon, MAX_HEIGHT);
                            height = MAX_HEIGHT;
                            // 画像を高画質モードで縮小
                            image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

                        } else {
                            // 画像がウィンドウの最大サイズより小さい場合
                            width = icon.getIconWidth();
                            height = icon.getIconHeight();
                            image = icon.getImage();
                        }

                        // 画像が読み込めた場合,テキストをクリアして画像をセット
                        label.setText("");
                        label.setIcon(new ImageIcon(image));

                    } catch (MalformedURLException e) {
                        if (retryCount++ < 5) {
                            label.setText("Retrying... : " + retryCount);
                            continue;
                        }
                        label.setText("Malformed url!\nURLの形式が不正です！");
                    } catch (IOException e) {
                        if (retryCount++ < 5) {
                            label.setText("Retrying... : " + retryCount);
                            continue;
                        }
                        label.setText("Can't read image!\n画像が読み込めません！");
                        label.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
                    }
                    label.setSize(width, height);
                    ImageViewer.this.pack();
                    break;
                }
            }
        };
        thread.start();
        setSize(MAX_WIDTH, MAX_HEIGHT);
        setVisible(true);
    }

    /**
     * 右クリックメニューを表示します.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Font meiryo = new Font("Meiryo UI", Font.PLAIN, 12);
            JPopupMenu menu = new JPopupMenu();

			/* ******** 画像の提供元ページを開くメニュー. ******** */
            JMenuItem itemOpenService = new JMenuItem("画像の提供元ページを開く");
            itemOpenService.setFont(meiryo);
            itemOpenService.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 提供元ページをブラウザで開く
                        Desktop.getDesktop().browse(new URI(serviceUrl));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }

            });

			/* ******** 画像をブラウザで開くメニュー. ******** */
            JMenuItem itemOpenUrl = new JMenuItem("画像をブラウザで開く");
            itemOpenUrl.setFont(meiryo);
            itemOpenUrl.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(imageUrl));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }

            });

			/* ******** 画像を保存するメニュー. ******** */
            JMenuItem itemSaveImage = new JMenuItem("画像を保存");
            itemSaveImage.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.addChoosableFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            if(file.isDirectory()) {
                                return true;
                            }
                            return file.getName().toLowerCase().endsWith(".png");
                        }
                        @Override
                        public String getDescription() {
                            return "PNGファイル(*.png)";
                        }
                    });
                    int choose = chooser.showSaveDialog(
                            ExTimeline.instance.getView().getMainFrame());
                    if (choose == JFileChooser.APPROVE_OPTION) {
                        Utility.downloadFile(
                                imageUrl,
                                chooser.getSelectedFile().getAbsolutePath());
                    }

                }

            });
            itemSaveImage.setFont(meiryo);

			/* ******** 項目を追加し、表示. ******** */
            menu.add(itemOpenService);
            menu.add(itemOpenUrl);
            menu.add(itemSaveImage);

            menu.show(e.getComponent(), e.getPoint().x, e.getPoint().y);
        }
    }
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
