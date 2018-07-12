package kyPkg.panel;
import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;

//-------+---------+---------+---------+---------+---------+---------+---------+
/*
 * @(#)JP_Hex.java	1.11 04/09/15
 *
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//-------+---------+---------+---------+---------+---------+---------+---------+
/**
 * ヘキサダンプ
 *
 * @author  Ken Yuasa
 * @version 1.00 04/09/15
 * @since   1.3
 */
//-------+---------+---------+---------+---------+---------+---------+---------+
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.HashMap;

public class Pn_HexView extends BorderPanel{
	private static final long serialVersionUID = 5246004021556330468L;
	private String fontName = "ＭＳ ゴシック";
	private PnlFile pnlFile;
	private PnlFont pnlFont;
	public static void main(String[] argv) {
		standAlone();
	}
	public static void standAlone() {
		JFrame frame = new JFrame();
		frame.setBounds(50, 50, 800, 350);
		frame.getContentPane().add(new Pn_HexView());
		frame.setVisible(true);
	}
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------
	public Pn_HexView(){
		super();
		createGUI();
	}
	public void createGUI(){

		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "input");
		pmap1.put(PnlFile.OPT49ER, "true");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(pmap1);

//		pnlFile = new PnlFile("input",true, false, true, "", -1);
    	pnlFont = new PnlFont();

    	final Font ft = new Font(fontName,Font.PLAIN,16);
		final JTextArea  jTa = new JTextArea();
		jTa.setTabSize(4);	jTa.setLineWrap(true);	jTa.setFont(ft);
		JScrollPane jSp = new JScrollPane(jTa);		jSp.setBounds(10,80,610,300);	

		final JButton    jBtReadIt  = new JButton("ファイルを読む");	jBtReadIt.setSize(new Dimension(150,20));	
		final JButton    jBtChgFont = new JButton("フォントを変更");	jBtChgFont.setSize(new Dimension(150,20));
		JPanel jPnlBtn = new JPanel();
		jPnlBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
		jPnlBtn.add(jBtReadIt);
		jPnlBtn.add(jBtChgFont);
 		jPnlBtn.add(pnlFont);

    	pnlN.add(pnlFile);
		pnlN.add(jPnlBtn);
//		jPnlN.add(pnlFont);
		pnlC.add(jSp);

		//---------------------------------------------------------------------
		//"ファイルを読む" ボタン動作
		//---------------------------------------------------------------------
		jBtReadIt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				File wFile = new File(pnlFile.getPath());
				try{
			 		System.out.println("読み込み可能か?:" + wFile.canRead());
				}catch(Exception er){
					er.printStackTrace();
				}
				if (wFile.isFile()){
					if (wFile.canRead()){
						String wStr = FileUtil.readAsHex(wFile.toString(),0,0);
						jTa.setText(wStr);
					}else{
						jTa.setText("ファイルが読めません");
					}
				}else{
					jTa.setText("ファイルではありません");
				}
			}
		});
		//---------------------------------------------------------------------
		//"フォントを変更" ボタン動作
		//---------------------------------------------------------------------
		jBtChgFont.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){
				Font wFont = pnlFont.getCurrentFont();
				if ( wFont != null ){ jTa.setFont(wFont); }
				repaint();
			}
		});
		// << Clipbord >> Copy & Paste
	    TextCutPaste tccp = new TextCutPaste();
	    //jTf.addMouseListener(tccp);
	    jTa.addMouseListener(tccp);
	}
	//---------+---------+---------+---------+---------+---------+---------+---------+
	// 描画
	//---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paint(Graphics g){
		super.paint(g);//Super経由でpaintCompをよぶ
	}
	//---------+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		g.setColor(new Color(100,50,50,100));
		g.fillRect(0,0,getWidth(), getHeight());
	}
}
