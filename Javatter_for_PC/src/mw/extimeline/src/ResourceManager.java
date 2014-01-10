package mw.extimeline.src;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceManager {
	public static ImageIcon iconWeb;
	public static ImageIcon iconRT;
	public static ImageIcon iconFavRt;
	public static ImageIcon iconInReplyTo;
	public static ImageIcon iconDelete;
	/*
	public static ImageIcon iconReply;
	public static ImageIcon iconReplyHover;
	public static ImageIcon iconRetweet;
	public static ImageIcon iconRetweetPress;
	public static ImageIcon iconFavorite;
	public static ImageIcon iconFavoritePress;
	*/
	
	private static ImageIcon getImageIcon(String url) {
		ImageIcon icon = null;
		try {
			
			icon = new ImageIcon(ImageIO.read(ResourceManager.class.getResourceAsStream(url)));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return icon;
	}
	
	public static void load() {
		iconWeb = getImageIcon("web.png");
		iconRT = getImageIcon("rt.png");
		iconInReplyTo = getImageIcon("inreplyto.png");
		iconDelete = getImageIcon("delete.png");
		iconFavRt = getImageIcon("favrt.png");
		/*
		iconReply = getImageIcon("timeline/reply.png");
		iconReplyHover = getImageIcon("timeline/reply_hover.png");
		iconRetweet = getImageIcon("timeline/retweet.png");
		iconRetweetPress = getImageIcon("timeline/retweet_on.png");
		iconFavorite = getImageIcon("timeline/favorite.png");
		iconFavoritePress = getImageIcon("timeline/favorite_on.png");
		*/
	}
}
