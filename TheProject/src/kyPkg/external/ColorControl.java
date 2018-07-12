package kyPkg.external;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartColor;

//システム全体の色を手中管理しようと思った(2013-11-13)
public class ColorControl {
	private static final Color NEGATIVE = Color.LIGHT_GRAY;
	private static final Color blue1 = new Color(204, 255, 252);// みずいろ
	public static final Color blue2 = new Color(153, 204, 255);// ブルー
	// Color blue3 = new Color(102,153,255);
	// Color blue4 = new Color( 51,102,255);
	// Color blue5 = new Color( 0, 51,255);
	public static final Color evenColor = new Color(200, 227, 227);// 暗いいみずいろ
	// public static final Color evenColor = new Color(221,250,250);//明るいみずいろ
	// public static final Color evenColor = new Color(220, 220, 255);　original
	private static final Color activeBack = Color.PINK;
	private static List<java.awt.Color> colorList;

	// private static final Color activeBack = Color.LIGHT_GRAY;
	public static Color getActivebackColor() {
		return activeBack;
	}
	public static Color getNegative() {
		return NEGATIVE;
	}

	public static Color getBlue1() {
		return blue1;
	}

	public static Color getBlue2() {
		return blue2;
	}

	public static Color getEvenColor() {
		return evenColor;
	}

	// 参考　http://www.jfree.org/jfreechart/
	// http://www.jfree.org/jfreechart/api/javadoc/index.html?org/jfree/chart/ChartColor.html
	public static List<java.awt.Color> getColorList() {
		colorList = new ArrayList();
		// like key note1(暗め)
		colorList.add(ChartColor.VERY_DARK_CYAN); // 0
		colorList.add(ChartColor.VERY_DARK_GREEN); // 1
		colorList.add(ChartColor.ORANGE); // 2
		colorList.add(ChartColor.VERY_DARK_RED); // 3
		colorList.add(ChartColor.VERY_DARK_MAGENTA); // 4
		colorList.add(ChartColor.VERY_DARK_BLUE); // 5
		colorList.add(ChartColor.LIGHT_RED); // 6
		colorList.add(ChartColor.LIGHT_BLUE); // 7
		colorList.add(ChartColor.LIGHT_GREEN); // 8
		colorList.add(ChartColor.LIGHT_YELLOW); // 9
		colorList.add(ChartColor.LIGHT_MAGENTA); // 10
		colorList.add(ChartColor.LIGHT_CYAN); // 11
		return colorList;
	}

	public static Color getColor(int seq) {
		if (colorList == null)
			colorList = getColorList();
		int remainder = seq % 10;
		return colorList.get(remainder);

	}

	public static Color getDarkColor(int seq) {
		return darken(getColor(seq));
	}
	public static Color getLightColor(int seq) {
		return lighten(getColor(seq));
	}

	public static Color XorColor(Color color) {
		return new Color(color.getRed() ^ 0xff, color.getGreen() ^ 0xff,
				color.getBlue() ^ 0xff);
	}

	final static int magic=0x16;
	
	public static Color lighten(Color color) {
		int rr = color.getRed() + magic;
		int gg = color.getGreen() + magic;
		int bb = color.getBlue() + magic;
		if(rr>255)rr=255;
		if(gg>255)gg=255;
		if(bb>255)bb=255;
		return new Color(rr, gg, bb);
	}
	public static Color darken(Color color) {
		int rr = color.getRed() - magic;
		int gg = color.getGreen() - magic;
		int bb = color.getBlue() - magic;
		if(rr<0)rr=5;
		if(gg<0)gg=5;
		if(bb<0)bb=5;
		return new Color(rr, gg, bb);
	}

	// ColorControl.getEvenColor()

	// private List<Color> colors = new ArrayList();
	// private void setColors() {
	// colors.add(new Color(153, 255, 255));//0
	// colors.add(new Color(153, 153, 255));//1
	// colors.add(new Color(204, 153, 255));//2
	// colors.add(new Color(255, 153, 153));//3
	// colors.add(new Color(255, 204, 153));//4
	// colors.add(new Color(255, 255, 153));//5
	// colors.add(new Color(204, 255, 153));//6
	// colors.add(new Color(153, 255, 153));//7
	// }

	// List<java.awt.Color> colorList = new ArrayList();
	// // like key note1(暗め)
	// colorList.add(ChartColor.VERY_DARK_CYAN);
	// colorList.add(ChartColor.VERY_DARK_GREEN);
	// colorList.add(ChartColor.ORANGE);
	// colorList.add(ChartColor.VERY_DARK_RED);
	// colorList.add(ChartColor.VERY_DARK_MAGENTA);
	// colorList.add(ChartColor.VERY_DARK_BLUE);

	// like key note1(明るめ)
	// colorList.add(ChartColor.DARK_CYAN);
	// colorList.add(ChartColor.DARK_GREEN);
	// colorList.add(ChartColor.ORANGE);
	// colorList.add(ChartColor.LIGHT_RED);
	// colorList.add(ChartColor.VERY_DARK_MAGENTA);

	// colorList.add(ChartColor.RED);
	// colorList.add(ChartColor.ORANGE);
	// colorList.add(ChartColor.LIGHT_GREEN);
	// colorList.add(ChartColor.BLUE);

	// colorList.add(ChartColor.RED);
	// colorList.add(ChartColor.LIGHT_YELLOW);
	// colorList.add(ChartColor.DARK_GREEN);
	// colorList.add(ChartColor.DARK_MAGENTA);
	// colorList.add(ChartColor.VERY_LIGHT_CYAN);
	// colorList.add(ChartColor.VERY_LIGHT_BLUE);

}
