package kyPkg.gridPanel;



import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import kyPkg.uFile.FileUtil;
import kyPkg.util.*;
import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import static kyPkg.uFile.FileUtil.*;

//------------------------------------------------------------------------------
/*
 * @(#)JP_IOtest.java	1.11 04/09/15
 * Copyright 2004 Ken Yuasa. All rights reserved.
 */
//------------------------------------------------------------------------------
/**
 * 入出力雛形
 * 
 * @author Ken Yuasa
 * @version 1.00 04/09/15
 * @since 1.3
 */
// ------------------------------------------------------------------------------
public class JP_IOtest extends BorderPanel {
	private static final long serialVersionUID = 4739730453970507482L;
	private Grid grid;
	private PnlFile pnlFile;
	private JTabbedPane jTp;
	private JComboBox cmbEncode;
	private JTextArea jTArea;
	private JTextArea jTinfo;
	private JButton btnRd2Texar;
	private JButton btnRd2Table;
	private JButton bntRdUniCD;
	private JButton btnWrite;
	private JButton btnWrtEnc;
	private JButton btnDeComp;
	private JButton btnDisAsm;
	private JButton btnExpand;
	private String fontName = "ＭＳ ゴシック";

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public JP_IOtest() {
		super();
		setSize(750, 500);
		this.setOpaque(false);
		createGUI();
	}

	// -------------------------------------------------------------------------
	// createGUI
	// -------------------------------------------------------------------------
	public void createGUI() {

		java.awt.Font ft = new java.awt.Font(fontName, Font.PLAIN, 12);

		HashMap hmap1 = new HashMap();
		hmap1.put(PnlFile.CAPTION, "input");
		hmap1.put(PnlFile.OPT49ER, "true");
		hmap1.put(PnlFile.ENCODE, "false");
		hmap1.put(PnlFile.DELIM, "true");
		hmap1.put(PnlFile.DEFAULT_PATH, "");
		hmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(hmap1);
//		pnlFile = new PnlFile("input", true, false, true, "", -1);
		// --------------------------------------------------------------------
		btnRd2Texar = new JButton("Read to TextArea");
		btnRd2Texar.setSize(150, 20);
		btnRd2Table = new JButton("Read to Table   ");
		btnRd2Table.setSize(150, 20);
		bntRdUniCD = new JButton("Unicode to TextArea");
		bntRdUniCD.setSize(150, 20);
		btnWrite = new JButton("Write It");
		btnWrite.setSize(100, 20);
		btnWrtEnc = new JButton("Write as Encoded");
		btnWrtEnc.setSize(100, 20);
		btnDeComp = new JButton("Decompile");
		btnDeComp.setSize(100, 20);
		btnDisAsm = new JButton("disAssemble");
		btnDisAsm.setSize(100, 20);
		btnExpand = new JButton("Expand");
		btnExpand.setSize(100, 20);

		JPanel pnlBtn01 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlBtn01.add(btnRd2Texar);
		pnlBtn01.add(btnRd2Table);
		pnlBtn01.add(bntRdUniCD);
		pnlBtn01.add(btnDeComp);
		pnlBtn01.add(btnDisAsm);
		pnlBtn01.add(btnExpand);
		// --------------------------------------------------------------------
		// あらかじめファイルのエンコードを調べる方法がないだろうか？
		// --------------------------------------------------------------------
		cmbEncode = new JComboBox();
		cmbEncode.setSize(100, 20);
		java.util.SortedMap cmap = java.nio.charset.Charset.availableCharsets();
		System.out.println("debug availableCharsets:" + cmap.toString());
		// --------------------------------------------------------------------
		// 　使用可能なもののみに限定をかけるように変更したい・・・
		// --------------------------------------------------------------------
		cmbEncode.addItem(US_ASCII);
		cmbEncode.addItem(SHIFT_JIS);
		cmbEncode.addItem(MS932);
		cmbEncode.addItem(EUC_JP);
		cmbEncode.addItem(UTF_16);
		cmbEncode.addItem(UTF_8);
		// --------------------------------------------------------------------
		JPanel pnlBtn02 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlBtn02.add(btnWrite);
		pnlBtn02.add(btnWrtEnc);
		pnlBtn02.add(cmbEncode);
		// --------------------------------------------------------------------
		// Tab Pane
		// --------------------------------------------------------------------
		jTArea = new JTextArea();
		jTArea.setFont(ft);
		jTArea.setTabSize(4);
		jTArea.setLineWrap(true);
		// --------------------------------------------------------------------
		grid = new Grid(false);
//		grid.setCellSelectionEnabled(true);
		grid.setAutoResizeMode(Grid.AUTO_RESIZE_OFF);
		// --------------------------------------------------------------------
		jTp = new JTabbedPane();
		jTp.setBounds(10, 100, 500, 200);
		jTp.addTab("01 TextArea ", new JScrollPane(jTArea));
		jTp.addTab("02 Table    ", new JScrollPane(grid));

		jTinfo = new JTextArea();
		jTinfo.setFont(ft);
		jTinfo.setTabSize(4);
		jTinfo.setLineWrap(true);

		pnlN.add(pnlFile);
		pnlN.add(pnlBtn01);
		pnlN.add(pnlBtn02);
		pnlC.add(jTp);
		pnlS.add(jTinfo);

		// --------------------------------------------------------------------
		// Action
		// --------------------------------------------------------------------
		btnWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String wPath = fc.getSelectedFile().toString();
					FileUtil.string2file_(wPath, jTArea.getText());
				}
			}
		});
		// ---------------------------------------------------------------------
		// xxx ボタン
		// ---------------------------------------------------------------------
		btnWrtEnc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wEncode = (cmbEncode.getSelectedItem()).toString();
				JFileChooser fc = new JFileChooser(wEncode + ".txt");
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String wPath = fc.getSelectedFile().toString();
					FileUtil.string2file_(wPath, jTArea.getText(), wEncode);
				}
			}
		});
		// ---------------------------------------------------------------------
		// xxx ボタン
		// ---------------------------------------------------------------------
		btnRd2Texar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTArea.setText(FileUtil.file2String(pnlFile.getPath()));
				jTinfo.setText(FileUtil.fileInfo(pnlFile.getPath()));
			}
		});
		// ---------------------------------------------------------------------
		// xxx ボタン
		// ---------------------------------------------------------------------
		btnRd2Table.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTinfo.setText(FileUtil.fileInfo(pnlFile.getPath()));
				// File=> Grid
//				file2Grid(pnlFile.getPath());
				 grid.assign(null,pnlFile.getPath(),null);

			}
		});
		// ---------------------------------------------------------------------
		// xxx ボタン
		// ---------------------------------------------------------------------
		bntRdUniCD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTArea.setText(new FileUtil().readUnicode(pnlFile.getPath()));
				jTinfo.setText(FileUtil.fileInfo(pnlFile.getPath()));
			}
		});
		// ---------------------------------------------------------------------
		// Decompile ボタン
		// ---------------------------------------------------------------------
		btnDeComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wPath = pnlFile.getPath(); // 処理対象パス、
				jTinfo.setText(Archiver.deCompileIt(wPath));
			}
		});
		// ---------------------------------------------------------------------
		// disAssemble ボタン
		// ---------------------------------------------------------------------
		btnDisAsm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wPath = pnlFile.getPath(); // 処理対象パス、
				jTinfo.setText(Archiver.disAssemble(wPath));
			}
		});
		// ---------------------------------------------------------------------
		// Expandボタン
		// ---------------------------------------------------------------------
		btnExpand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wPath = pnlFile.getPath(); // 処理対象パス、
				System.out.println("jar Expand:" + wPath);
				// Archiver.ManiInfo(wPath);
				if (wPath.endsWith(".jar")) {
					Archiver.deCompZipAll(wPath);
				} else if (wPath.endsWith(".zip")) {
					Archiver.deCompZipAll(wPath);
				} else {
					Archiver.enCompZip(wPath); // それ以外だったら圧縮しょり
				}
			}
		});

	}

//	private void file2Grid(String path) {
//		// new FileUtil().file2DtmX(wPath,tModel,20);// ◆ file2DtmX ◆
//		// new VectorUtil();
//		Vector fVec = VectorUtil.file2VectorByExt(pnlFile.getPath());
//		List<List> matrix = MatrixUtil.partOfMatrix(fVec, 0, 20);
//		// -----------------------------------------------------------------
//		tModel.setRowCount(0);
//		for (int i = 0; i < matrix.size(); i++) {
//			List qVec = matrix.get(i);
//			if (i == 0) {
//				for (int j = 0; j < qVec.size(); j++) {
//					tModel.addColumn("<" + j + ">");
//				}
//			}
//			System.out.println("qVec:" + qVec);
//			tModel.addRow(new Vector(qVec));
//		}
//
//		// カラム幅の調節 TableColumnModelが前提・・・・
//		DefaultTableColumnModel columnModel = (DefaultTableColumnModel) grid
//				.getColumnModel();
//		TableColumn column = null;
//		for (int i = 0; i < columnModel.getColumnCount(); i++) {
//			column = columnModel.getColumn(i);
//			column.setPreferredWidth(20);
//		}
//
//	}

	public static void main(String[] argv) {
		standAlone(new JP_IOtest(), "Cross");
	}
}
