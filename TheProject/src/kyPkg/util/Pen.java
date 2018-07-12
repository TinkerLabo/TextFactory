package kyPkg.util;

/** 線を描く時の情報を記憶する Pen */
import java.awt.*;

import kyPkg.panelMini.PaintCanvas;

public class Pen {
	public static final String BUTT = "BUTT";
	public static final String SQUARE = "SQUARE";
	public static final String BEVEL = "BEVEL";
	public static final String MITER = "MITER";
	public static final String ROUND = "ROUND";
	// どんな描画をするか？
	private String shape;

	public String getShape() {
		return shape;
	}

	public void setShape(String pShape) {
		this.shape = pShape;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// 線のプロパティ
	// -----+---------+---------+---------+---------+---------+---------+---------+
	private Color color;
	/** 線の色 */
	private BasicStroke stroke;

	// public static final int ROUND = 0;
	// public static final int SQUARE = 1;

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// 線の色にアクセスするメソッド */
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// 線の太さ、線の端の形状にアクセスするメソッド */
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public BasicStroke getBasicStroke() {
		return stroke;
	}

	public void setBasicStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public float getLineWidth() {
		return stroke.getLineWidth();
	}

	// public int getLineCap() {
	// return stroke.getEndCap();
	// }
	// public int getLineJoin() {
	// return stroke.getLineJoin();
	// }
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// CAP_ROUND ペン幅の半分の長さを半径とした丸い装飾を付けて、閉じられていない部分輪郭線および破線セグメントを終了します。
	// CAP_BUTT 装飾を付けずに、閉じられていない部分輪郭線および破線セグメントを終了します。
	// CAP_SQUARE
	// ライン幅の半分の長さに等しい距離だけセグメントの先端を延長する正方形を付けて、閉じられていない部分輪郭線および破線セグメントを終了します。
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// JOIN_ROUND ライン幅の半分の長さを半径として、角を丸く切り落として輪郭線セグメントを接合します。
	// JOIN_BEVEL 幅の広い輪郭線の外側の角を直線セグメントに接合するようにして輪郭線セグメントを接合します。
	// JOIN_MITER ラインセグメントの外側の端が重なるまで延長して輪郭線セグメントを接合します。
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public void setLineWidth(float width) {
		int cap = stroke.getEndCap();
		int join = stroke.getLineJoin();
		stroke = new BasicStroke(width, cap, join);
	}

	public void setLineCap(String capTyp) {
		int cap;
		if (capTyp.equals(BUTT)) {
			cap = BasicStroke.CAP_BUTT;
		} else if (capTyp.equals(SQUARE)) {
			cap = BasicStroke.CAP_SQUARE;
		} else if (capTyp.equals(ROUND)) {
			cap = BasicStroke.CAP_ROUND;
		} else {
			cap = BasicStroke.CAP_BUTT;
		}
		int join = stroke.getLineJoin();
		float width = stroke.getLineWidth();
		stroke = new BasicStroke(width, cap, join);
	}

	public void setLineJoin(String joinTyp) {
		int join;
		if (joinTyp.equals(BEVEL)) {
			join = BasicStroke.JOIN_BEVEL;
		} else if (joinTyp.equals(MITER)) {
			join = BasicStroke.JOIN_MITER;
		} else if (joinTyp.equals(ROUND)) {
			join = BasicStroke.JOIN_ROUND;
		} else {
			join = BasicStroke.JOIN_BEVEL;
		}
		int cap = stroke.getEndCap();
		float width = stroke.getLineWidth();
		stroke = new BasicStroke(width, cap, join);
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// コンストラクタ & default
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public Pen() {
		color = Color.black;
		shape = PaintCanvas.RECTANGLE;// "四角形"モードがデフォルト
		stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND);
	}
}
