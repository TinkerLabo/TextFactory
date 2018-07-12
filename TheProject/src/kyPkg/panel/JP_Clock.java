package kyPkg.panel;

import java.awt.*;
import java.util.*;
import java.lang.Math; // sin, cos
//-----------------------------------------------------------------------------

import kyPkg.frame.BaseFrame;
import kyPkg.util.Ruler;

public class JP_Clock extends JP_Ancestor {
	private static final long serialVersionUID = 2243916145937323016L;
	private int xWidth, xHeight;
	// -------------------------------------------------------------------------
	// Local Values
	// -------------------------------------------------------------------------
	private Image offImg; // ダブルバッファリング用
	private int CenterX, CenterY; // 時計の中心
	private int Radius; // 時計の半径
	private Thread gThread; // タイマー用スレッド
	private Color wBackColor = Color.BLACK; // 文字盤の色
	private Color wScaleColor = Color.WHITE; // 目盛＆針の色
	private Color wHandColor = Color.RED; // 秒針の色

	// -------------------------------------------------------------------------
	// 文字盤の色
	// -------------------------------------------------------------------------
	public void setBackColor(Color newColor) {
		if (newColor != null) {
			wBackColor = newColor;
		} else {
			// nullが指定された場合ランダムな色
			int iR, iG, iB;
			iR = (int) Math.floor(Math.random() * 256);
			iG = (int) Math.floor(Math.random() * 256);
			iB = (int) Math.floor(Math.random() * 256);
			wBackColor = new Color(iR, iG, iB);
			System.out.println("● R:" + iR + " G:" + iG + " B:" + iB);
		}
	}

	// --------------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------------
	public JP_Clock() {
		super();
		resizeMe(300, 300);
		gThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					repaint(); // → paint
					try {
						Thread.sleep(1000); // 一秒おき！
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		};
		gThread.start();
	}

	// #########################################################################
	// -------------------------------------------------------------------------
	// xxInit《 JP_Ancestor 》親から呼ばれる
	// -------------------------------------------------------------------------
	// public void xxInit(int pWidth,int pHeight,Object pParent,LayoutManager
	// layout){
	// //myParent = pParent;
	// xxResize(pWidth,pHeight);
	// this.setLayout(layout);
	// }
	// //-------------------------------------------------------------------------
	// // xxInit《 JP_Ancestor 》親から呼ばれる
	// //-------------------------------------------------------------------------
	// public void xxInit(int pWidth,int pHeight,Object pParent,LayoutManager
	// layout){
	// myParent = pParent;
	// //xxResize(pWidth,pHeight);
	// gThread = new Thread(){
	// public void run(){
	// while(true){
	// repaint(); // → paint
	// try{
	// Thread.sleep(1000); //一秒おき！
	// }catch (InterruptedException e){
	// e.printStackTrace();
	// System.exit(1);
	// }
	// }
	// }
	// };
	// gThread.start();
	// }
	// -------------------------------------------------------------------------
	// initGui《 InfChilePanel 》
	// -------------------------------------------------------------------------
	@Override
	public void initGui(int pWidth, int pHeight, Object pParent,
			LayoutManager layout) {
		resizeMe(pWidth, pHeight);

	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// xxResize
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public void resizeMe(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;
		// ---------------------------------------------------------------------
		CenterX = xWidth / 2; // 時計の位置と半径
		CenterY = xHeight / 2;
		if (xWidth < xHeight) // 縦横，短い方を時計の半径
			Radius = (int) (xWidth / 2 * 0.9);
		else
			Radius = (int) (xHeight / 2 * 0.9);
	}

	@Override
	public void paintComponent(Graphics g) {
		// Ruler.drawRuler(g,this.getSize().width,this.getSize().height,Color.white);

		// g.setColor(new Color(50,50,50,100));
		// g.fillRect(10,10, xWidth, xHeight);
		// g.setColor(wBackColor);
		// g.fillOval( (xWidth / 2 - Radius),
		// (xHeight / 2 - Radius),
		// (Radius * 2 ),
		// (Radius * 2 ));

		if (xWidth > 0 && xHeight > 0) {
			offImg = createImage(xWidth / 2, xHeight / 2); // イメージ作成
			if (offImg != null) {
				DispTime(offImg); // オフスクリーンに描画
				g.drawImage(offImg, 0, 0, this); // イメージを描画
			}
		}
		Ruler.drawRuler(g, this.getSize().width,
				this.getSize().height, new Color(128, 255, 128, 250));

	}

	// -------------------------------------------------------------------------
	// public void xxpaintComponent(Graphics g) {
	// super.paintComponent(g);
	// g.setColor(new Color(100,50,50,100));
	// g.fillRect(0,0,getWidth(), getHeight());
	// }
	// //-----+---------+---------+---------+---------+---------+---------+---------+
	// // 描画
	// //-----+---------+---------+---------+---------+---------+---------+---------+
	// public void paint(Graphics g){
	// super.paint(g);//Super経由でpaintCompをよぶ
	// }
	// -------------------------------------------------------------------------
	// paint
	// -------------------------------------------------------------------------
	@Override
	public void paint(Graphics g) {
		super.paint(g);// Super経由でpaintCompをよぶ
		if (xWidth > 0 && xHeight > 0) {
			offImg = createImage(xWidth, xHeight); // イメージ作成
			if (offImg != null) {
				DispTime(offImg); // オフスクリーンに描画
				g.drawImage(offImg, 0, 0, this); // イメージを描画
			}
		}
	}

	// -------------------------------------------------------------------------
	// update
	// -------------------------------------------------------------------------
	// public void update(Graphics g){
	// paint(g);
	// }
	// -------------------------------------------------------------------------
	// dispose
	// -------------------------------------------------------------------------
	// public void dispose(){
	// gThread = null;
	// }
	// -------------------------------------------------------------------------
	// 時計をオフスクリーンに描画
	// -------------------------------------------------------------------------
	void DispTime(Image pOffImg) {
		Graphics g = pOffImg.getGraphics(); // オフスクリーン
		g.setColor(new Color(50, 50, 50, 100));
		g.fillRect(0, 0, xWidth, xHeight);

		g.setColor(wBackColor);
		g.fillOval((xWidth / 2 - Radius), (xHeight / 2 - Radius), (Radius * 2),
				(Radius * 2));
		// ---------------------------------------------------------------------
		// 目盛り線を描画（1分は6度，60分で360度)
		// ---------------------------------------------------------------------
		for (int angle = 0; angle < 360; angle += 6) {
			double RD = angle * Math.PI / 180; // 角度をラジアンに変換
			int x1 = CenterX + (int) (Math.sin(RD) * Radius); // 目盛りの外側の点の位置
			int y1 = CenterY - (int) (Math.cos(RD) * Radius);
			int radius2;
			if (angle % 30 == 0) // 5分刻み(5*6=30)
				radius2 = Radius - 8; // 長さ8の目盛り
			else
				radius2 = Radius; // 長さ0の目盛り（つまり点)
			int x2 = CenterX + (int) (Math.sin(RD) * radius2); // 目盛りの内側の点の位置
			int y2 = CenterY - (int) (Math.cos(RD) * radius2);
			g.setColor(wScaleColor);
			g.drawLine(x1, y1, x2, y2); // 目盛りラインを引く
		}
		// ---------------------------------------------------------------------
		// 現在の時刻
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		int hour = date.get(Calendar.HOUR); // 時間 取得
		int minute = date.get(Calendar.MINUTE); // 分 取得
		int second = date.get(Calendar.SECOND); // 秒 取得
		// ---------------------------------------------------------------------
		// 時針 （一時間は360/12=30度）
		double RD = ((hour + minute / 60.0) * 30) * Math.PI / 180; // 角度をラジアンに変換
		int hx = CenterX + (int) (Math.sin(RD) * Radius * 0.6); // 針の先端の位置
		int hy = CenterY - (int) (Math.cos(RD) * Radius * 0.6);
		g.setColor(wScaleColor);
		g.drawLine(CenterX, CenterY, hx, hy); // 中心から針の先端まで
		// ---------------------------------------------------------------------
		// 分針 （一分は360/60=6度）
		RD = (minute * 6) * Math.PI / 180; // 角度をラジアンに変換
		int mx = CenterX + (int) (Math.sin(RD) * Radius * 0.8); // 針の先端の位置
		int my = CenterY - (int) (Math.cos(RD) * Radius * 0.8);
		g.setColor(wScaleColor);
		g.drawLine(CenterX, CenterY, mx, my);
		// ---------------------------------------------------------------------
		// 秒針 （一秒は360/60 =6度）
		RD = (second * 6) * Math.PI / 180; // 角度をラジアンに変換
		int sx = CenterX + (int) (Math.sin(RD) * Radius * 0.9); // 針の先端の位置
		int sy = CenterY - (int) (Math.cos(RD) * Radius * 0.9);
		g.setColor(wHandColor);
		g.drawLine(CenterX, CenterY, sx, sy);

	}

	// ----+---------+---------+---------+---------+---------+---------+---------+
	// forTest
	// ----+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv) {
		asAPart();
	}
	public static void standAlone() {
		new JP_Clock().showDialog("CLOCK");// 品目マスタ検索画面表示
	}
	public static void asAPart() {
		// ---------------------------------------------------------------------
		// 時計パネル 飾り
		// ---------------------------------------------------------------------
		JP_Clock objClock = new JP_Clock();
		Color xColor = new Color(128, 150, 161);
		objClock.setBackColor(xColor);
		objClock.initGui(100, 100, null, null);
		// objClock.initGui(100, 100, null);
		objClock.resizeMe(150, 150); // これをしないとバグる！！
		objClock.setBounds(0, 0, 150, 150);
		new BaseFrame(objClock, 640, 480);
	}
	
	
	
	
}
