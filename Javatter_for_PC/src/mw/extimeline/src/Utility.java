package mw.extimeline.src;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

public class Utility {
	
	public static double getImageWidth(ImageIcon icon, double maximum) {
		return (double) icon.getIconWidth() / ((double)icon.getIconHeight() / maximum);
	}
	
	public static double getImageHeight(ImageIcon icon, double maximum) {
		return (double) icon.getIconHeight() / ((double)icon.getIconWidth() / maximum);
	}

    public static boolean finder(String reg, String text) {
        return Pattern.compile(reg).matcher(text).find();
    }
    
    public static String getMatchString(String reg, String text) {
    	Matcher matcher = Pattern.compile(reg).matcher(text);
    	matcher.find();
    	return matcher.group();
    }
	
	public static String getMatchServiceUrl(String url) {
		String returnUrl;
		if (finder("http://twitpic.com/\\w+(\\w|/)$", url)) {
			returnUrl = url.replace("http://twitpic.com/", "http://twitpic.com/show/full/");
		} else if (finder("http://instagram.com/p/\\w+(\\w|/)$", url)) {
			returnUrl = url.concat("/media/?size=l");
		} else if (finder("http://twitter.yfrog.com/\\w+(\\w|/)$", url)) {
			returnUrl = url.replace("http://twitter.yfrog.com/", "http://yfrog.com/").concat(":medium");
		} else if (finder("http://p.twipple.jp/\\w+(\\w|/)", url)) {
			returnUrl = url.replace("http://p.twipple.jp/", "http://p.twpl.jp/show/orig/");
		} else {
			returnUrl = "NULL";
		}
		return returnUrl.replaceAll("//", "/").replaceFirst(":/", "://");
	}
	
	public static void downloadFile(String path, String to) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(to);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		/* URLを構築します。引数にダウンロード先のURLを指定します。*/
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}

		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}

		int num;
		byte buf[] = new byte[256];	  

		/* DataInputStreamを使用してファイルに書き出します。*/
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(urlConnection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		long start = System.currentTimeMillis();
		try {
			while((num = dis.read(buf)) != -1) {
				fos.write(buf,0,num);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - start);
		try {
			fos.close();
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 正規表現に一致したテキストのリストを返します.<br />
	 * オーバーフロー防止のためリスト項目は100個までとなります。
	 * @param reg
	 * @param txt
	 * @return
	 */
	public static List<String> getFindStrings(String reg, String txt) {
		List<String> list = new ArrayList<String>();
		Matcher matcher = Pattern.compile(reg).matcher(txt);
		while (matcher.find()) {
			list.add(matcher.group());
			if (list.size() == 100) {
				break;
			}
		}
		return list;
	}
}
