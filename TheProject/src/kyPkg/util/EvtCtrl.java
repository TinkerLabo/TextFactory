package kyPkg.util;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EvtCtrl {
	Component me;
	public int Pressed_X; // マウスが押されたＸ位置
	public int Pressed_Y; // マウスが押されたＹ位置
	public int Released_X; // マウスが離されたＸ位置
	public int Released_Y; // マウスが離されたＹ位置
	public int xPozX, xPozY; // keyによるコントロール位置
	public String xEvtName;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public EvtCtrl(Component pComponent) {
		xEvtName = "";
		xPozX = xPozY = 0;
		Pressed_X = Pressed_Y = 0;
		Released_X = Released_Y = 0;
		me = pComponent;
	}

	public void requestKeyF() {
		JComponent jcmp;
		jcmp = (JComponent) me;
		jcmp.requestFocus(true); // ※これをやらないとキーイベントを拾ってくれない！
	}

	// --------------------------------------------------------
	// evtInfo
	// --------------------------------------------------------
	// public void evtInfo(MouseEvent evt){
	// Graphics gr;
	// gr = offImg.getGraphics();
	// // int r = (int)(Math.random() * 255);
	// // int g = (int)(Math.random() * 255);
	// // int b = (int)(Math.random() * 255);
	// //gr.setColor(Color.white);
	// //gr.fillRect(0,0,600,600);
	// //gr.setColor(Color.black);
	// // int ix = (evt.getX()/10)*10;
	// // int iy = (evt.getY()/10)*10;
	// //gr.setColor(new Color(r,g,b));
	// gr.drawString(evt.toString(),ix+10,iy+10);
	// gr.fillRect(ix,iy,10,10);
	// me.repaint();
	// }
	public void compAction() {
		Component pThis;
		pThis = me;
		pThis.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent evt) { // キーボードフォーカスを取得した
				System.out.println("focusGained");
			}
		});
		// ---------------------------------------------------------------------
		// KeyListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		pThis.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) { // キーを押している時に呼び出される
				// System.out.println("KeyPressed:");
				// System.out.println("	KeyChar         :" + evt.getKeyChar());
				// System.out.println("	KeyCode         :" + evt.getKeyCode());
				// System.out.println("	KeyLocation     :" +
				// evt.getKeyLocation());
				// System.out.println("	isActionKey()   :" + evt.isActionKey()
				// );
				// System.out.println("	KeyModifiersText:" +
				// evt.getKeyModifiersText());
				// System.out.println("	KeyText         :" + evt.getKeyText());
				// 数値入力キーパッドの下矢印キー用の定数です。
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					xPozX -= 10;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					xPozX += 10;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					xPozY -= 10;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					xPozY += 10;
					break;
				case KeyEvent.VK_ENTER:
					xPozX = 100;
					xPozY = 100;
					break;
				case KeyEvent.VK_ESCAPE:
					xPozX = 0;
					xPozY = 0;
					break;
				// default;
				}
				me.repaint();
			}
			@Override
			public void keyReleased(KeyEvent evt) { // キーを離した
				// System.out.println("keyReleased:");
			}
			@Override
			public void keyTyped(KeyEvent evt) { // キーをタイプした ※アクションキーでは発生しない
				// System.out.println("keyTyped:");
			}
		});
		// ---------------------------------------------------------------------
		// MouseListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		pThis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) { // マウスがクリックされました
				System.out.println("MouseClicked :" + evt.getX() + ","
						+ evt.getY());
				requestKeyF();
				System.out.println("MouseClicked");
				System.out.println("evt.toString:" + evt.toString());
				System.out.println("  :getWhen       :" + evt.getWhen());
				System.out.println("  :isAltDown     :" + evt.isAltDown());
				// System.out.println("  :isAltGraphDown:" +
				// evt.isAltGraphDown());
				// System.out.println("  :isControlDown :" +
				// evt.isControlDown());
				// System.out.println("  :isShiftDown   :" + evt.isShiftDown());
				// System.out.println("  :getButton     :" + evt.getButton());
				// // from ver1.4
				// System.out.println("  :getClickCount :" +
				// evt.getClickCount());
				// System.out.println("  :getPoint      :" + evt.getPoint());
				// System.out.println("  :getX,getY     :" + evt.getX() + "," +
				// evt.getY());
			}

			// public void mouseEntered(MouseEvent evt){ //マウスが入った
			// System.out.println("MouseEntered");
			// }
			// public void mouseExited(MouseEvent evt){ //マウスが出た
			// System.out.println("MouseExited");
			// }
			@Override
			public void mousePressed(MouseEvent evt) { // マウスのボタンが押された
				Pressed_X = evt.getX();
				Pressed_Y = evt.getY();
				xEvtName = "MousePressed";
			}

			@Override
			public void mouseReleased(MouseEvent evt) { // マウスのボタンが離された
				Released_X = evt.getX();
				Released_Y = evt.getY();
				xEvtName = "MouseReleased";
				me.repaint();
			}
		});
		// ---------------------------------------------------------------------
		// MouseMotionListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		// pThis.addMouseMotionListener(new MouseMotionAdapter() {
		// public void mouseDragged(MouseEvent evt){ //マウスがドラッグされた
		// System.out.println("mouseDragged");
		// me.repaint();
		// }
		// public void mouseMoved(MouseEvent evt){ //マウスが移動された
		// System.out.println("mouseMoved x:" + evt.getX() + " y:" +
		// evt.getY());
		// me.repaint();
		// }
		// });
		// ---------------------------------------------------------------------
		// MouseWheelListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		// pThis.addMouseWheelListener(new MouseWheelListener() {
		// public void mouseWheelMoved(MouseWheelEvent e) { //ホイールが回転すると呼ばれる
		// // int e.getScrollAmount() //このイベントに応答してスクロールされるユニット数を返します。
		// // int e.getScrollType() //このイベントに応答して発生するスクロールのタイプを返します。
		// // int e.getUnitsToScroll()
		// // int e.getWheelRotation() //マウスホイールを回転させた数
		// // String e.paramString() //このイベントを特定するパラメータの文字列
		// }
		// });
	}

	// -------------------------------------------------------------------------
	// buttonAction
	// -------------------------------------------------------------------------
	public void buttonAction(Button pThis) {
		// ---------------------------------------------------------------------
		// ActionListener 以下のオブジェクトが持つメソッド
		// MenuItem,TextField,List,Button
		// JFileChooser,JComboBox,Timer,DefaultButtonModel,JTextField
		// AbstractButton,BasicComboBoxEditor,ComboBoxEditorButtonModel
		// ---------------------------------------------------------------------
		pThis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) { // アクションが発生しました
				System.out.println("actionPerformed");
			}
		});
	}

	public void checkboxAction(Checkbox pThis) {
		// ---------------------------------------------------------------------
		// addItemListener
		// List、Choice、CheckboxMenuItem、Checkbox、JComboBox、
		// DefaultButtonModel、ButtonModel、AbstractButton、ItemSelectable のメソッド
		// ---------------------------------------------------------------------
		pThis.addItemListener(new ItemListener() {
			// ユーザによって項目が選択または選択解除されたときに呼び出されます。
			@Override
			public void itemStateChanged(ItemEvent e) {
				// Object e.getItem()
				// イベントによって影響を受けた項目を返します。
				// ItemSelectable e.getItemSelectable()
				// イベントの発生元を返します。
				// int e.getStateChange()
				// 状態変更のタイプ (選択、または選択解除) を返します 。
				// String e.paramString()
				// この項目イベントを特定するパラメータの文字列を返します。
			}
		});
		// addAdjustmentListener(AdjustmentListener) Scrollbar のメソッド
		// addChangeListener(ChangeListener) JSlider,MenuSelectionManager のメソッド
	}

	public void windowAction(Window pThis) {
		// ---------------------------------------------------------------------
		// WindowListener java.awt.Window のメソッド
		// ---------------------------------------------------------------------
		pThis.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) { // アクティブに設定された
				System.out.println("windowActivated");
			}

			@Override
			public void windowClosed(WindowEvent e) { // ウィンドウがクローズされた
				System.out.println("windowClosed");
			}

			@Override
			public void windowClosing(WindowEvent e) { // システムメニューで閉じようとした
				System.out.println("windowClosing");
			}

			@Override
			public void windowDeactivated(WindowEvent e) { // アクティブ Window
															// でなくなった
				System.out.println("windowDeactivated");
			}

			@Override
			public void windowDeiconified(WindowEvent e) { // 最小化から通常の状態
				System.out.println("windowDeiconified");
			}

			@Override
			public void windowIconified(WindowEvent e) { // 通常から最小化された
				System.out.println("windowIconified");
			}

			@Override
			public void windowOpened(WindowEvent e) { // 最初に可視になった
				System.out.println("windowOpened");
			}
		});
	}
}
