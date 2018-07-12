package kyPkg.panel;

//import kyPkg.*;
import kyPkg.util.*;

//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)JPanel_?.java	1.11 04/09/15
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//package ken.temp;
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/**
 * ん～なんでしょう
 * @author  Ken Yuasa
 * @version 1.00 04/09/15
 * @since   1.3
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/******************************************************************************
 * Swing Panel Template !!
 *******************************************************************************/
public class JP_Eye extends JP_Ancestor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3624341540172925239L;
	public static final int PORT = 10001;
	/** 通信に使用するポート番号 */
	Object myParent;
	Component me;
	int xWidth, xHeight;
	String name = "JP_Eye";

	int xPozX, xPozY;
	Image wkImg; // for ImageLoading
	Image offImg; // ダブルバッファリング用
	Graphics offGrf; // ダブルバッファリング用

	public static String messages[] = { "********************************",
			"****  Gabba Gabba Hey!!     ****",
			"********************************", };

	public JP_Eye(Object pParent) {
		this(640, 480, pParent, null);
	}

	public JP_Eye(int pWidth, int pHeight, Object pParent, LayoutManager layout) {
		super("JP_EYE");
		myParent = pParent;
		me = this;
		resizeMe(pWidth, pHeight);
		this.setLayout(layout);
		initMe();
	}

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public JP_Eye() {
		super("JP_EYE");
	}

	// -------------------------------------------------------------------------
	// xxInit《 JP_Ancestor 》
	// -------------------------------------------------------------------------
	@Override
	public void initGui(int pWidth, int pHeight, Object pParent,
			LayoutManager layout) {
		myParent = pParent;
		me = this;
		resizeMe(pWidth, pHeight);
		this.setLayout(layout);
		initMe();
	}

	public void initMe() {
		// xPane= this.getContentPane();
		xPozX = xPozY = 0;
		JButton jbt;
		jbt = new JButton("StealYourFace!");
		jbt.setBounds(0, 10, 200, 20);
		add(jbt);

		Icon icon = ImageCtrl.getImageIcon(this, "images/DEAD.GIF");
		JLabel imageLabel = new JLabel(icon);
		add(imageLabel);

		JTextField jtf1 = new JTextField("");
		jtf1.setBounds(0, 40, 200, 20);
		add(jtf1);
		jtf1.setText(hostInfo("Address"));

		JTextField jtf2 = new JTextField("");
		jtf2.setBounds(0, 60, 200, 20);
		add(jtf2);
		jtf2.setText(hostInfo("Name"));

		jbt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JOptionPane jop = new JOptionPane();
				// jop.showMessageDialog((Component)null,"le Freak!!");
				wkImg = ImageCtrl.imageLoader(this, "images/DEAD.GIF");
				mesServe();
				requestKeyF();
				repaint();
			}
		});
		act003(this);
	}

	public void requestKeyF() {
		this.requestFocus(true); // ※これをやらないとキーイベントを拾ってくれない！
	}

	public static String hostInfo(String pType) {
		String rtnStr = null;
		try {
			InetAddress myHost = InetAddress.getLocalHost();
			System.out.println("Machine Name:" + myHost.getHostName());
			System.out.println("IP Address  :" + myHost.getHostAddress());
			if (pType.equals("Name")) {
				rtnStr = myHost.getHostName();
			} else if (pType.equals("Address")) {
				rtnStr = myHost.getHostAddress();
			} else {
				rtnStr = "?pType=>" + pType;
			}
		} catch (UnknownHostException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		return rtnStr;
	}

	public static void mesServe() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				// 通信記録表示
				String remoteName = socket.getInetAddress().getHostName();
				Date date = new Date();
				String logMessage = remoteName + " connected at "
						+ date.toString();
				System.out.println(logMessage);

				// メッセージ転送
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream()));
				for (int i = 0; i < messages.length; i++) {
					bw.write(messages[i], 0, messages[i].length());
					bw.newLine();
				}
				bw.close();
				socket.close();
				serverSocket.close();//20160509 
			}
		} catch (SocketException e) {
			System.err.println("Socket Error");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("IO Error");
			System.exit(-1);
		}
	}

	public static void mesClient(String pIP) {
		try {
			Socket socket = new Socket(pIP, PORT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
				System.out.println(line);
			reader.close();
			socket.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Usage:java HostAddress hostname");
			System.exit(-1);
		} catch (UnknownHostException e) {
			System.err.println("Host not found");
			System.exit(-1);
		} catch (SocketException e) {
			System.err.println("Socket Error");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("IO Error");
			System.exit(-1);
		}
	}

	// -------------------------------------------------------------------------
	// xxResize
	// -------------------------------------------------------------------------
	public void resizeMe(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;

		offImg = createImage(xWidth, xHeight); // ???????????????????????
	}

	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paint(Graphics g) {
		super.paint(g);// Super経由でpaintCompをよぶ

		if (offImg == null)
			offImg = createImage(xWidth, xHeight);
		if (offImg == null) {
			super.paint(g);
			return;
		}
		offGrf = offImg.getGraphics();
		Ruler.drawRuler(offGrf, xWidth, xHeight, new Color(155, 187, 244));
		// g.drawImage(offImg, 0, 0, this); // 作業イメージをアプレットに描画
		if (wkImg != null)
			g.drawImage(wkImg, xPozX, xPozY, this);
	}

	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paintComponent(Graphics g) {
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(155, 187, 244));
		// g.setColor(Color.blue);
		// g.drawString(name,0,10);
		// g.drawString(new Date().toString(),100,10);
	}

	public void act003(Component pThis) {
		// ---------------------------------------------------------------------
		// MouseListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		pThis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				// マウスがクリックされました
				System.out.println("MouseClicked :" + evt.getX() + ","
						+ evt.getY());
				// System.out.println("MouseClicked");
				// System.out.println("evt.toString:" + evt.toString());
				// System.out.println("            :getWhen       :" +
				// evt.getWhen());
				// System.out.println("            :isAltDown     :" +
				// evt.isAltDown());
				// System.out.println("            :isAltGraphDown:" +
				// evt.isAltGraphDown());
				// System.out.println("            :isControlDown :" +
				// evt.isControlDown());
				// System.out.println("            :isShiftDown   :" +
				// evt.isShiftDown());
				// // System.out.println("            :getButton     :" +
				// evt.getButton()); // from ver1.4
				// System.out.println("            :getClickCount :" +
				// evt.getClickCount());
				// // System.out.println("            :getPoint      :" +
				// evt.getPoint());
				// System.out.println("            :getX,getY     :" +
				// evt.getX() + "," + evt.getY());

				requestKeyF();
			}
			// public void mouseEntered(MouseEvent evt){
			// //コンポーネントにマウスが入りました
			// System.out.println("MouseEntered");
			// evtInfo(evt);
			// }
			// public void mouseExited(MouseEvent evt){
			// //コンポーネントからマウスが出ました
			// System.out.println("MouseExited");
			// evtInfo(evt);
			// }
			// public void mousePressed(MouseEvent evt){
			// //マウスのボタンが押されました
			// System.out.println("MousePressed");
			// evtInfo(evt);
			// }
			// public void mouseReleased(MouseEvent evt){
			// //マウスのボタンが離されました
			// System.out.println("MouseReleased");
			// evtInfo(evt);
			// }
		});
		// ---------------------------------------------------------------------
		// MouseMotionListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		pThis.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {
				// マウスがドラッグされました
				System.out.println("mouseDragged");
				repaint();
				// evtInfo(evt);
			}

			@Override
			public void mouseMoved(MouseEvent evt) {
				// コンポーネント上でマウスが移動されました
				System.out.print("mouseMoved");
				System.out.println(" x:" + evt.getX() + " y:" + evt.getY());
				double wX = getParent().getParent().getLocation().getX();
				double wY = getParent().getParent().getLocation().getY();

				System.out.println("getRootPane.getLocation():"
						+ getRootPane().getLocation());
				System.out.println("Location x:" + wX + " y:" + wY);
				repaint();
				// evtInfo(evt);
			}
		});
		// ---------------------------------------------------------------------
		// MouseWheelListener java.awt.Component のメソッド
		// ---------------------------------------------------------------------
		// pThis.addMouseWheelListener(new MouseWheelListener() {
		// public void mouseWheelMoved(MouseWheelEvent e) {
		// //マウスホイールが回転すると呼び出されます。
		// //int e.getScrollAmount()
		// //このイベントに応答してスクロールされるユニット数を返します。
		// // int e.getScrollType()
		// //このイベントに応答して発生するスクロールのタイプを返します。
		// // int e.getUnitsToScroll()
		// //プラットフォームの設定に適合する範囲で、
		// //ScrollPane または JScrollPane をスクロールする際に使用する一般的な
		// //MouseWheelListener の実装に役立つ便利なメソッドです。
		// // int e.getWheelRotation()
		// //マウスホイールを回転させた「クリック」数を返します。
		// // String e.paramString()
		// //このイベントを特定するパラメータの文字列を返します。
		// }
		// });
	}

}
