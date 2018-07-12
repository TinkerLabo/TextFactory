package kyPkg.util;

import java.awt.*;
import java.net.*;
import java.util.List;

import javax.swing.*;

public class ImageCtrl {
	// 拡大
	public static Image getImage(String path) {
		return new ImageIcon(path).getImage();
	}
	public static Image getScaledImage(int iWidth, int iHeight, String path) {
		Image image = new ImageIcon(path).getImage();
		return image.getScaledInstance(iWidth, iHeight, Image.SCALE_SMOOTH);
	}

	public static void useTracker(Component component, List<Image> images) {
		MediaTracker tracker = new MediaTracker(component);
		for (Image image : images) {
			tracker.addImage(image, 1);
		}
		try {
			tracker.waitForAll();
		} catch (InterruptedException L01) {
		}
	}

	// public void paintComponent(Graphics gr) {
	// super.paintComponent(gr);
	// //gr.drawString("原寸", 10, 20);
	// gr.drawImage(imgCtrl.getIcon(), 10, 25, this);
	//
	// //gr.drawString("単純に拡大(" + iScale + "%)", 10, 180);
	// gr.drawImage(icon, 10, 185, iWidth * iScale / 100, iHeight * iScale /
	// 100, this);
	//
	// //gr.drawString("スムーズに拡大(" + iScale + "%)", 200, 180);
	// gr.drawImage(scaled01, 200, 185, this);
	// }

	// ------------------------------------------------------------------------
	// pass がクラスのフォルダ内のオブジェクトになっているようだ・・・
	// ex DEAD.GIF=> T:\workspace\QPRweb\WEB-INF\classes\DEAD.GIF
	// ------------------------------------------------------------------------
	// <<クラスパスを通せばよいのだろうか？>>
	// Eclipseでクラスパスを設定するにはプロジェクトのプロパティ（パッケージエクスプローラのプロジェクト名のところで右クリック）から設定する。
	// Javaのビルドパス→ライブラリー→外部クラス・フォルダーの追加
	// ここで「T:\workspace\QPRweb\images」を指定した。これでオッケーかな
	// 　実行時のパスはどうなんだろう？？@20110531
	// ------------------------------------------------------------------------
	private static URL getURL(Object thisObject, String path) {
		URL location = null;
		if (path == null || path.equals("")) {
			return null;
		}
		try {
			ClassLoader loader = thisObject.getClass().getClassLoader();
			location = loader.getResource(path);
			if (location == null) {
				// クラスパスの設定がおかしいのかもしれない
				// JOptionPane.showMessageDialog((Component) null,
				// "resource couldn't found:" + path+
				// "\nClassLoader:" + loader.toString()
				// );
				System.out.println("#ERROR! resource couldn't found:" + path);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	public static ImageIcon getImageIcon(Object thisObject, String path) {
		URL location = getURL(thisObject, path);
		if (location == null)
			return null;
		return new ImageIcon(location);
	}

	// ----------------------------------------------------------------
	// Image Loading ... 実行形式により方式が違う
	// ----------------------------------------------------------------
	public static Image imageLoader(Object thisObject, String path) {
		URL location = getURL(thisObject, path);
		if (location == null)
			return null;
		java.awt.Image image = null;
		if (thisObject instanceof java.applet.Applet) {
			java.applet.Applet applet = (java.applet.Applet) thisObject;
			System.out.print("基底 URL :" + applet.getCodeBase());
			image = applet.getImage(location); // Image Loading On Web
			// img1 = apl.getImage(apl.getCodeBase(),pFile); // Image
			// Loading On Web
			// img = createImage() dummyimage Create!!
		} else {
			try {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				image = toolkit.getImage(location);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}

}
