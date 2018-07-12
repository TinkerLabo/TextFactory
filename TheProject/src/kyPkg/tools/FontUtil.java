package kyPkg.tools;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

public class FontUtil {
	public static final String PLAIN = "PLAIN";
	public static final String BOLD = "BOLD";
	public static final String ITALIC = "ITALIC";
	public static final String BOLD_ITALIC = "BOLD + ITALIC";

	public static String[] getFontStyles() {
		String[] fontStyles = new String[] { PLAIN, BOLD, ITALIC, BOLD_ITALIC };
		return fontStyles;
	}

	public static String[] getFontPoints() {
		String fontPoints[] = new String[40];
		for (int i = 0; i < 40; i++) {
			fontPoints[i] = Integer.toString((i + 1) * 2);
		}
		return fontPoints;
	}

	public static String[] getFontNames() {

		try {
			return GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getAvailableFontFamilyNames();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static void debugAllowFont() {
		try {
			Font[] allowFonts = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getAllFonts();
			for (int i = 0; i < allowFonts.length; i++) {
				System.out.println(allowFonts[i].getName());
				// 論理フォント名取得時はgetName、フェース名はgetFontNameを使用
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void drawString(Graphics g, Font font, Point point,
			String message) {
		drawString(g, font, (int) point.getX(), (int) point.getY(), message);
	}

	public static void drawString(Graphics g, Font font, int x, int y,
			String message) {
		if (font != null)
			g.setFont(font);
		g.drawString(message, x, y);
	}

	public static Font createFont(String fontName, String style, String point) {
		if (fontName == null)
			fontName = "ＭＳ ゴシック";
		int iStyle;
		if (style.equals(PLAIN))
			iStyle = Font.PLAIN;
		else if (style.equals(BOLD))
			iStyle = Font.BOLD;
		else if (style.equals(ITALIC))
			iStyle = Font.ITALIC;
		else
			iStyle = Font.BOLD + Font.ITALIC;
		int iSize = Integer.parseInt(point);
		// Font ft = new Font("ＭＳ ゴシック",Font.PLAIN,12);

		System.out.println("Font font = new Font(" + fontName + ",Font.PLAIN,"
				+ point + ");");
		return new Font(fontName, iStyle, iSize);
	}

	public static String createScript(String fontName, String style,
			String point) {
		if (fontName == null)
			fontName = "ＭＳ ゴシック";
		return "Font font = new Font(\"" + fontName + "\",Font.PLAIN," + point
				+ ");";
	}
}
