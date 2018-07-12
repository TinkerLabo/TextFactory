package kyPkg.util;

import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

// 他の画像フォーマットはどうなっているのか？？
public class JpegE {
	//-------------------------------------------------------------------------
	// JPEGファイルを読み画像imageに変換する
	//	TODO com.sun.image.codec は今後使えなくなるかもしれないので，ImageIO で置き換えた方がいいらしい。
	//	http://opendolphin.motomachi-hifuka.jp/2011/02/comsunimagecode.html
	//-------------------------------------------------------------------------
	//	20160923
	//-------------------------------------------------------------------------
	public static BufferedImage jpeg2image(String iPath) {
		BufferedImage inImage = null;
		try {
			inImage = ImageIO.read(new File(iPath));
		} catch (Exception e) {
			e.printStackTrace();
			inImage = null;
		}
		return inImage;
	}

	//	public static BufferedImage jpeg2image(String iPath){
	//		BufferedImage inImage = null;
	//		try {
	//			FileInputStream in = new FileInputStream(iPath);
	//			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
	//			// 読み込むときに解像度を変更できないだろうか？
	//			inImage = decoder.decodeAsBufferedImage();
	//			in.close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		return inImage;
	//	}
	//-------------------------------------------------------------------------
	// 画像imageからJPEGファイルに出力する
	// 使用例》 JpegE.image2jpeg("test.jpg",image,(float)1.0);
	//-------------------------------------------------------------------------
	public static void image2jpeg(String pPath, BufferedImage pImage) {
		image2jpeg(pPath, pImage, (float) 0.25); // 0.25 低品質
	}

	public static boolean image2jpeg(String pPath, BufferedImage writeImage,
			float quality) {
		boolean result = false;
		try {
			result = ImageIO.write(writeImage, "jpeg", new File(pPath));
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	//	public static void image2jpeg(String pPath, BufferedImage pImage,
	//			float quality) {
	//		FileOutputStream fo = null;
	//		try {
	//			fo = new FileOutputStream(pPath);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//			return;
	//		}
	//		BufferedImage image = pImage;
	//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fo);
	//		//---------------------------------------------------------------------
	//		// JPEG パラメータのセット  他のパラメータも要チェック！！
	//		//---------------------------------------------------------------------
	//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
	//		param.setQuality(quality, false); //画質を設定
	//		encoder.setJPEGEncodeParam(param);
	//		//---------------------------------------------------------------------
	//		//public void setQuality(float quality,boolean forceBs)
	//		//  quality 0.0 ～ 1.0 ( 0.75 高品質 0.5  標準品質 0.25 低品質)
	//		//  forceBs ベースライン量子化テーブルを強制する
	//		//---------------------------------------------------------------------
	//		try {
	//			encoder.encode(image); // データストリームとして符号化する
	//			encoder.getOutputStream().close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
}
