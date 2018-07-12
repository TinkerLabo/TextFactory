package kyPkg.frame;

import kyPkg.panel.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//-----------------------------------------------------------------------------
/**
 * インターナルフレームの雛形
 * <p>
 * 他のコンポーネントへの組み込みが簡単であるはず
 * <p>
 * 
 * @(#)IF_Tmp.java 1.11 04/11/16 Copyright 2004 Ken Yuasa. All rights reserved.
 * @author Ken Yuasa
 * @version 1.00 04/11/16
 * @since 1.3
 */
// -----------------------------------------------------------------------------
// インターナルフレーム Template !!
// -----------------------------------------------------------------------------
public class JInternalFrameMod extends JInternalFrame {
	private static final long serialVersionUID = 8711592840546862133L;
	private JP_Ancestor child;
	private int xWidth = 640;
	private int xHeight = 300;
	// -------------------------------------------------------------------------
	// コンストラクタ
	public JInternalFrameMod(String title, JP_Ancestor pPnl) {
	// -------------------------------------------------------------------------
		this(title, pPnl, 640, 300);
	}
	public JInternalFrameMod(String title, JP_Ancestor pannel, int pWidth, int pHeight) {
		super();
		child = pannel; // 内包するパネル
		initMe(title);
		xxResize(pWidth, pHeight);
	}
	// -------------------------------------------------------------------------
	public void initMe(String title) {
		this.setClosable(true);
		this.setResizable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setFrameIcon(null);
		this.setTitle(title);
		// this.setOpaque(false);
		this.setVisible(true);
		// ---------------------------------------------------------------------
		// 内包するパネルの初期化
		// ---------------------------------------------------------------------
		final Container pane = this.getContentPane();
		child.initGui(xWidth, xHeight, this, null);
//		child.initGui(xWidth, xHeight,  null);
		pane.add(child);
		// ---------------------------------------------------------------------
		// コンポーネントリスナー設定
		// ---------------------------------------------------------------------
		this.addComponentListener(new ComponentAdapter() {
			// -----------------------------------------------------------------
			// リサイズされたら → 子パネルのリサイズを呼ぶ
			// -----------------------------------------------------------------
			@Override
			public void componentResized(ComponentEvent e) {
				// System.out.println("◆componentResized");
//				child.resizeMe(pane.getWidth(), pane.getHeight(),true);
			}
			// -----------------------------------------------------------------
			// void componentHidden (ComponentEvent e) 隠されたら・・・
			// void componentMoved (ComponentEvent e) 位置が変わったら・・・
			// void componentResized(ComponentEvent e) サイズが変わったら・・・
			// void componentShown (ComponentEvent e) 可視になったら・・・
			// -----------------------------------------------------------------
		});
		/**********************************************************************
		 * this.addInternalFrameListener( new InternalFrameAdapter(){ //
		 * 起動されたときに呼び出されます。 public void
		 * internalFrameActivated(InternalFrameEvent e) {} // クローズされたときに呼び出されます。
		 * public void internalFrameClosed(InternalFrameEvent e) {} //
		 * クローズ処理中のときに呼び出されます。 public void
		 * internalFrameClosing(InternalFrameEvent e) {} // 終了されたときに呼び出されます。
		 * public void internalFrameDeactivated(InternalFrameEvent e) {} //
		 * アイコン化解除されたときに呼び出されます。 public void
		 * internalFrameDeiconified(InternalFrameEvent e) {} //
		 * アイコン化されたときに呼び出されます。 public void
		 * internalFrameIconified(InternalFrameEvent e) {} // オープンされたときに呼び出されます。
		 * public void internalFrameOpened(InternalFrameEvent e) {} });
		 ************************************************************************/
	}
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// xxResize
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	public void xxResize(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;
		this.setSize(xWidth, xHeight);
	}
	// //---------+---------+---------+---------+---------+---------+---------+---------+
	// // 描画
	// //---------+---------+---------+---------+---------+---------+---------+---------+
	// public void paint(Graphics g){
	// super.paint(g); // Super経由でpaintCompをよぶ
	// }
	// //-------------------------------------------------------------------------
	// // 描画
	// //-------------------------------------------------------------------------
	// public void paintComponent(Graphics g){
	// kyPkg.util.Ruler.drawRuler(g,this.getSize().width,this.getSize().height,new
	// Color(255,187,244,80));
	// }
}
