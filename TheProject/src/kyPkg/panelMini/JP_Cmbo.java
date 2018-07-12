package kyPkg.panelMini;

//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)JP_Cmbo.java	1.11 04/09/15
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//package ken.temp;
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
/**
 * コンボボックスの雛形
 *
 * @author  Ken Yuasa
 * @version 1.00 04/09/15
 * @since   1.3
 */
//---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

import kyPkg.util.Ruler;

/******************************************************************************
 * swing template SwingDrv!!
 *******************************************************************************/
public class JP_Cmbo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1878475980324522147L;
	Object myParent;
	Component me;
	int xWidth, xHeight;
	String name = "JP_Cmbo";

	// -------------------------------------------------------------------------
	// Constructor #1
	// -------------------------------------------------------------------------
	public JP_Cmbo(Object pParent) {
		this(640, 480, pParent, null);
	}

	// -------------------------------------------------------------------------
	// Constructor #2
	// -------------------------------------------------------------------------
	public JP_Cmbo(int pWidth, int pHeight, Object pParent, LayoutManager layout) {
		super();
		myParent = pParent;
		me = this;
		xxResize(pWidth, pHeight);
		this.setLayout(layout);
		initMe();
	}

	// -------------------------------------------------------------------------
	// Init
	// -------------------------------------------------------------------------
	public void initMe() {
		String sList[] = { "Windows", "Machintosh", "Linux" };

		final JComboBox jCmb01 = new JComboBox(sList);
		jCmb01.setBounds(new Rectangle(0, 10, 300, 20));
		this.add(jCmb01, null);

		JButton jBtRes = new JButton("Reset The Combobox");
		jBtRes.setBounds(new Rectangle(0, 30, 150, 20));
		this.add(jBtRes, null);

		JButton jBtAdd = new JButton("AddElements       ");
		jBtAdd.setBounds(new Rectangle(0, 50, 150, 20));
		this.add(jBtAdd, null);

		jCmb01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wMsg = "";
				wMsg = wMsg + "getSelectedItem:" + jCmb01.getSelectedItem()
						+ "\n";
				wMsg = wMsg + "getSelectedIndex:" + jCmb01.getSelectedIndex()
						+ "\n";
				JOptionPane.showMessageDialog((Component) null, wMsg);
			}
		});
		jBtRes.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jCmb01.removeAllItems();
			}
		});

		jBtAdd.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jCmb01.addItem("リンゴ");
				jCmb01.addItem("ごりら");
				jCmb01.addItem("らっぱ");
				jCmb01.addItem("ぱんだ");
			}
		});
	}

	// -------------------------------------------------------------------------
	// xxResize
	// -------------------------------------------------------------------------
	public void xxResize(int pWidth, int pHeight) {
		xWidth = pWidth;
		xHeight = pHeight;
		if (xWidth <= 0)
			xWidth = 300;
		if (xHeight <= 0)
			xHeight = 300;
	}

	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paint(Graphics g) {
		super.paint(g);// Super経由でpaintCompをよぶ
	}

	// ---------+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	// ---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paintComponent(Graphics g) {
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(0, 255, 128));
		// g.setColor(Color.blue);
		// g.drawString(name,0,10);
		// g.drawString(new Date().toString(),100,10);
	}
	// jCmb01.addMouseListener(new MouseAdapter() {
	// public void mouseReleased(MouseEvent e) {
	// JOptionPane jop = new JOptionPane();
	// jop.showMessageDialog((Component)null,wMsg);
	// }
	// });
}
