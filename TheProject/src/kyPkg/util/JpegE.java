package kyPkg.util;

import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

// ���̉摜�t�H�[�}�b�g�͂ǂ��Ȃ��Ă���̂��H�H
public class JpegE {
	//-------------------------------------------------------------------------
	// JPEG�t�@�C����ǂ݉摜image�ɕϊ�����
	//	TODO com.sun.image.codec �͍���g���Ȃ��Ȃ邩������Ȃ��̂ŁCImageIO �Œu�����������������炵���B
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
	//			// �ǂݍ��ނƂ��ɉ𑜓x��ύX�ł��Ȃ����낤���H
	//			inImage = decoder.decodeAsBufferedImage();
	//			in.close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		return inImage;
	//	}
	//-------------------------------------------------------------------------
	// �摜image����JPEG�t�@�C���ɏo�͂���
	// �g�p��t JpegE.image2jpeg("test.jpg",image,(float)1.0);
	//-------------------------------------------------------------------------
	public static void image2jpeg(String pPath, BufferedImage pImage) {
		image2jpeg(pPath, pImage, (float) 0.25); // 0.25 ��i��
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
	//		// JPEG �p�����[�^�̃Z�b�g  ���̃p�����[�^���v�`�F�b�N�I�I
	//		//---------------------------------------------------------------------
	//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
	//		param.setQuality(quality, false); //�掿��ݒ�
	//		encoder.setJPEGEncodeParam(param);
	//		//---------------------------------------------------------------------
	//		//public void setQuality(float quality,boolean forceBs)
	//		//  quality 0.0 �` 1.0 ( 0.75 ���i�� 0.5  �W���i�� 0.25 ��i��)
	//		//  forceBs �x�[�X���C���ʎq���e�[�u������������
	//		//---------------------------------------------------------------------
	//		try {
	//			encoder.encode(image); // �f�[�^�X�g���[���Ƃ��ĕ���������
	//			encoder.getOutputStream().close();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
}
