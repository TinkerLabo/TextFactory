package kyPkg.util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class FontTest {
	public static void main(String[] args) throws Exception {
		final float fontsize = 18.0f;
		final String text = ":012Hello����ɂ��̓��C�V�C�ł��˃F;-)";
		final int imageMargin = 10;
		final Font defaultFont = Font.decode("Monospaced").deriveFont(fontsize);

		// ���p�\�Ȃ��ׂẴt�H���g���擾
		GraphicsEnvironment genv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] font = genv.getAllFonts();
		int nameMaxWidth = getFontNameMaxWidth(defaultFont, font);
		int textMaxWidth = getMaxWidth(font, fontsize, text);
		int imageWidth = imageMargin + nameMaxWidth + imageMargin
				+ textMaxWidth + imageMargin;
		BufferedImage image = new BufferedImage(imageWidth, (int) (font.length
				* fontsize * 2), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.WHITE);
		for (int i = 0; i < font.length; i++) {
			// �t�H���g���`��
			g.setFont(defaultFont);
			System.out.println(">>"+getDispFontName(font[i]));
			g.drawString(getDispFontName(font[i]), imageMargin, i * 2
					* fontsize + fontsize);
			// �t�H���g�`��
			g.setFont(font[i].deriveFont(fontsize));
			g.drawString(text, imageMargin + nameMaxWidth + imageMargin, i * 2
					* fontsize + fontsize);
		}
		ImageIO.write(image, "png", new File("fonttest.png"));
	}

	private static int getMaxWidth(Font[] font, float fontsize, String str) {
		BufferedImage image = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		int maxWidth = getMaxWidth(g, font, fontsize, str);
		g.dispose();
		return maxWidth;
	}

	private static int getMaxWidth(Graphics2D g, Font[] font, float fontsize,
			String str) {
		int maxWidth = 0;
		for (int i = 0; i < font.length; i++) {
			Font f = font[i].deriveFont(fontsize);
			FontMetrics fm = g.getFontMetrics(f);
			int w = fm.stringWidth(str);
			maxWidth = Math.max(maxWidth, w);
		}
		return maxWidth;
	}

	private static int getFontNameMaxWidth(Font font, Font[] fonts) {
		BufferedImage image = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		int maxWidth = getFontNameMaxWidth(g, font, fonts);
		g.dispose();
		return maxWidth;
	}

	private static int getFontNameMaxWidth(Graphics2D g, Font font, Font[] fonts) {
		int maxWidth = 0;
		FontMetrics fm = g.getFontMetrics(font);
		for (int i = 0; i < fonts.length; i++) {
			int w = fm.stringWidth(getDispFontName(fonts[i]));
			maxWidth = Math.max(maxWidth, w);
		}
		return maxWidth;
	}

	private static String getDispFontName(Font f) {
		if (f.getName().equals(f.getFontName())) {
			return f.getName();
		} else {
			// �_����(�t�H���g�t�F�[�X��)
			return f.getName() + " (" + f.getFontName() + ")";
		}
	}
}
