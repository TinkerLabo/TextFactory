package kyPkg.cross;

import kyPkg.mySwing.*;
import kyPkg.panelsc.Pn_Scaffold;
import kyPkg.sql.IsamConnector;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.FontUtility;
import kyPkg.util.SwingUtil;

import static kyPkg.util.SwingUtil.load2Combo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import globals.ResControl;

//============================================================================
// 汎用クロス集計　java version
//============================================================================
public class Pn_CrossAp_BK extends Pn_Scaffold {
	protected List<List> ODBC_SQL2VECTOR(String xSql, String iPath) {
		List<List> matrix = null;
		if (FileUtil.isExists(iPath)) {
			System.out.println("xSql:" + xSql);
			matrix = new IsamConnector(iPath).query2Matrix(xSql);
		}
		return matrix;
	}

	public static void main(String[] argv) {
		standAlone(new Pn_CrossAp_BK(), "Cross");
	}

	private static final String VTAB = "" + '\u000b'; // 垂直タブ
	private static final String vbTab = "\t";
	private static final String vbCrLf = "\n";
	private boolean gDirty1 = false;
	private boolean gDirty2 = false;
	private String gPath_Sys = "";
	private String gPath_Cls = "";
	private String gPath_Idx = "";
	private String gCndPath_Idx = "";
	private String gCndPath_Cls = "";

	private static final long serialVersionUID = -3066425890995680687L;
	private MyComboBox cmbRepos;
	private MyComboBox comboBox1;
	private MyComboBox comboBox2;

	private MyButton ComQuit;
	private MyButton ComResAll;
	private MyButton comGoGo;
	private MyButton CommandButton1;

	private MyPanel frame1;
	private MyLabel LabInfo2;
	private JTabbedPane multiPage1;
	private MyTextField textTitle;
	private ListPanelW list1;
	private ListPanelW list3;

	private MyComboBox comboBox3;
	private MyComboBox comboBox4;
	private MyComboBox CondCAL;
	private MyButton CommAdd;
	private JTabbedPane CondePages;
	private MyButton CommandButton5;
	private MyButton cmdSaveAsCond;
	private MyButton ComSaveAs;
	private MyButton CommandButton3;
	private MyButton ComJamin;

	private MyCheckBox checkCom1;
	private MyCheckBox checkCom2;
	private MyCheckBox checkCom3;
	private MyTextField textCom1;
	private MyTextField textCom2;
	private MyTextField textCom3;
	private MyTextField TextCond;
	private ListPanel listQtb1;
	private ListPanel listQtb2;
	private MyPanel frame2;
	private MyPanel frame0;
	private MyLabel lPath;
	private MyLabel lTable;
	private MyLabel lKey;
	private MyLabel lTyp;
	private MyLabel lCom;
	// private ListPanel listQtb3b;
	private CondPanel3 condPanel3;
	private CondPanel1 condPanel1;
	private CondPanel1 condPanel2;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_CrossAp_BK() {
		super(750, 500);
		// super(RWPanel.READDATA, 0);
		createGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// create GUI
	// -------------------------------------------------------------------------
	void createGUI() {
		FontUtility.setFont(new Font("Monospaced", Font.PLAIN, 10),false);
		frame0 = new MyPanel(null);

		frame0.add(new MyLabel(0, 0, 700, 20, "種類、調査、および時期を選択してください。"));
		frame0.add(new MyLabel(0, 20, 100, 20, "種類："));
		frame0.add(new MyLabel(0, 40, 100, 20, "調査："));
		frame0.add(new MyLabel(350, 40, 100, 20, "時期："));
		cmbRepos = new MyComboBox(50, 20, 250, 20);
		comboBox1 = new MyComboBox(50, 40, 250, 20);
		comboBox2 = new MyComboBox(400, 40, 250, 20);
		frame0.add(cmbRepos);
		frame0.add(comboBox1);
		frame0.add(comboBox2);
		comboBox2.setVisible(false);
		// ---------------------------------------------------------------------
		// タブペーﾝ
		// ---------------------------------------------------------------------
		multiPage1 = new JTabbedPane();
		multiPage1.setBounds(0, 60, 705, 335);
		// ---------------------------------------------------------------------
		// 表頭表側の選択
		// ---------------------------------------------------------------------
		frame1 = new MyPanel(null);
		frame1.setBounds(0, 0, 1150, 400);
		frame1.setGrid(true);
		// ---------------------------------------------------------------------
		frame1.add(new MyLabel(0, 0, 100, 20, "表頭の選択"));
		checkCom1 = new MyCheckBox(100, 0, 100, 20, "コメント");
		textCom1 = new MyTextField(200, 0, 350, 20, "");
		MyButton ComClr1 = new MyButton(600, 0, 90, 20, "Clear");
		MyButton CommChk1 = new MyButton(700, 0, 90, 20, "check");
		MyButton CommRes1 = new MyButton(800, 0, 90, 20, "unCheck");
		list1 = new ListPanelW(0, 20, 700, 130, false); // + List2
		list1.setRightClear(false);
		// ListDst1
		frame1.add(checkCom1);
		frame1.add(textCom1);
		frame1.add(ComClr1);
		frame1.add(CommChk1);
		frame1.add(CommRes1);
		frame1.add(list1);
		// ---------------------------------------------------------------------
		int geta = 150;
		frame1.add(new MyLabel(0, geta, 100, 20, "表側の選択"));
		checkCom2 = new MyCheckBox(100, geta, 100, 20, "コメント");
		textCom2 = new MyTextField(200, geta, 350, 20, "");
		MyButton ComClr2 = new MyButton(600, geta, 90, 20, "Clear");
		MyButton CommChk2 = new MyButton(700, geta, 90, 20, "check");
		MyButton CommRes2 = new MyButton(800, geta, 90, 20, "unCheck");
		list3 = new ListPanelW(0, geta + 20, 700, 130, false); // +List4?
		list3.setRightClear(false);
		// ListDst2
		frame1.add(checkCom2);
		frame1.add(textCom2);
		frame1.add(ComClr2);
		frame1.add(CommChk2);
		frame1.add(CommRes2);
		frame1.add(list3);

		// ---------------------------------------------------------------------
		// 条件設定画面
		// ---------------------------------------------------------------------
		frame2 = new MyPanel(null);
		frame2.setGrid(true);
		comboBox3 = new MyComboBox(0, 0, 250, 20);
		frame2.add(comboBox3);
		comboBox4 = new MyComboBox(250, 0, 150, 20);
		frame2.add(comboBox4);
		CondCAL = new MyComboBox(400, 0, 150, 20);
		frame2.add(CondCAL);
		CommAdd = new MyButton(550, 0, 150, 20, "条件を追加する");
		frame2.add(CommAdd);
		checkCom3 = new MyCheckBox(0, 20, 100, 20, "コメント");
		textCom3 = new MyTextField(100, 20, 300, 20, "");
		TextCond = new MyTextField(400, 20, 300, 20, "");
		listQtb1 = new ListPanel(new Rectangle(0, 40, 400, 100), false, false);
		listQtb2 = new ListPanel(new Rectangle(400, 40, 300, 100), false,
				false);
		frame2.add(listQtb1);
		frame2.add(listQtb2);
		frame2.add(checkCom3);
		frame2.add(textCom3);
		frame2.add(TextCond);
		// ---------------------------------------------------------------------
		CondePages = new JTabbedPane();
		// CondPage1(l)
		CondePages.setBounds(0, 150, 650, 125);
		condPanel1 = new CondPanel1(0, 0, 400, 100, null);
		CondePages.add("条件Ａ", condPanel1);

		condPanel2 = new CondPanel1(0, 0, 400, 100, null);
		CondePages.add("条件Ｂ", condPanel2);

		condPanel3 = new CondPanel3(0, 0, 400, 100, null);
		CondePages.add("ＡＢの関連", condPanel3);
		// ---------------------------------------------------------------------
		CondePages.setSelectedIndex(0); // タブページの選択
		frame2.add(CondePages);
		int stab1 = 275;
		lPath = new MyLabel(0, stab1, 200, 20, "LPath  ");
		lTable = new MyLabel(200, stab1, 100, 20, "LTable ");
		lKey = new MyLabel(300, stab1, 50, 20, "LKey   ");
		lTyp = new MyLabel(350, stab1, 50, 20, "LTyp   ");
		lCom = new MyLabel(400, stab1, 200, 20, "LCom   ");
		frame2.add(lPath);
		frame2.add(lTable);
		frame2.add(lKey);
		frame2.add(lTyp);
		frame2.add(lCom);
		// ---------------------------------------------------------------------
		CommandButton5 = new MyButton(0, 290, 150, 20, "HMLよりMyData作成");
		cmdSaveAsCond = new MyButton(150, 290, 150, 20, "モニター抽出として保存");
		ComSaveAs = new MyButton(300, 290, 150, 20, "該当ＩＤ保存");
		CommandButton3 = new MyButton(450, 290, 150, 20, "条件をクリア");
		ComJamin = new MyButton(600, 290, 150, 20, "件数確認");
		frame2.add(CommandButton5);
		frame2.add(cmdSaveAsCond);
		frame2.add(ComSaveAs);
		frame2.add(CommandButton3);
		frame2.add(ComJamin);
		// ---------------------------------------------------------------------
		// 集計表タイトルと出力条件
		// ---------------------------------------------------------------------
		MyPanel frameX = new MyPanel(null);
		frameX.add(new MyLabel(0, 0, 700, 20, "集計票タイトル　(編集可能)"));
		textTitle = new MyTextField(0, 20, 400, 20, "");
		frameX.add(textTitle);
		// ---------------------------------------------------------------------
		// タブペーﾝ(タブ追加)
		// ---------------------------------------------------------------------
		MyPanel tab1 = new MyPanel(null);
		tab1.add(frame1);
		multiPage1.add("表頭表側の選択", tab1);
		multiPage1.add("抽出条件", frame2);
		multiPage1.add("集計表タイトルと出力条件", frameX);
		// ---------------------------------------------------------------------
		// ボタンパネル
		// ---------------------------------------------------------------------
		MyPanel btnPanel = new MyPanel(0, 400, 700, 50, null);
		ComQuit = new MyButton(0, 0, 100, 20, "Quit");
		ComResAll = new MyButton(100, 0, 100, 20, "全リセット");
		comGoGo = new MyButton(250, 0, 150, 20, "作表実行");
		CommandButton1 = new MyButton(400, 0, 300, 20, "選択項目の確認→作表へ");
		btnPanel.add(ComQuit);
		btnPanel.add(ComResAll);
		btnPanel.add(comGoGo);
		btnPanel.add(CommandButton1);

		frame0.add(multiPage1);
		frame0.add(btnPanel);
		add(frame0, BorderLayout.CENTER);

		LabInfo2 = new MyLabel(0, 0, 720, 20, "種類を選択してください");
		pnlSouth.pnlS.add(LabInfo2);
		initComboBox0();

		cmbRepos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initComboBox1();
				repaint();
			}
		});
		comboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox1_Click();
				repaint();
			}
		});
		comboBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox2_Click();
				repaint();
			}
		});
		comboBox3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox3_Click();
				repaint();
			}
		});
		comboBox4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboBox4_Click();
				repaint();
			}
		});

		listQtb1.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					listQtb1_DblClick(listQtb2);
					repaint();
				}
			}
		});

		listQtb2.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					listQtb2_DblClick();
					repaint();
				}
			}
		});
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		comGoGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				repaint();
			}
		});
	}

	private void initComboBox0() {
		List listRepos = ListArrayUtil.file2List(ResControl.REPOSITORY);
		SwingUtil.load2Combo(cmbRepos, listRepos);
	}

	private void initComboBox1() {
		String val = (String) cmbRepos.getSelectedItem();
		String[] array = val.split("\t");
		gPath_Sys = array[1];
		System.out.println("gPath_Sys>>" + gPath_Sys);
		String regex = "";
		List listNext = kyPkg.uFile.ListArrayUtil.dir2List_asc(gPath_Sys, regex,
				false);
		if (listNext != null) {
			SwingUtil.load2Combo(comboBox1, listNext);
			SwingUtil.load2Combo(comboBox3, listNext);
		}

	}

	private void ComboBox1_Click() {
		// If Me.Frame1.Left <> 0 Then Call CommandButton1_Click
		textTitle.setText("");
		textCom1.setText("");
		textCom2.setText("");
		boolean flag = false;
		if (flag) {
			// If Me.ListQtb3.ListCount <> 0 Then
			String msg = "抽出条件をクリアしますか？";
			int option = JOptionPane.showConfirmDialog((Component) null, "確認",
					msg, JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				// Call ComResAll_Click
			}
			// End If
		}
		list1.clear();
		list3.clear();

		// XXX path を正規化しておく

		gPath_Cls = gPath_Sys + "/" + comboBox1.getSelectedItem(); // '■cls■
		gPath_Cls = FileUtil.normarizeIt(gPath_Cls);

		// すでに条件設定が使われていたら、同期させる
		// Me.ComboBox3 = Me.ComboBox1
		System.out.println("gPath_Cls>>" + gPath_Cls);
		String regex = "";
		List listNext = kyPkg.uFile.ListArrayUtil.dir2List_dsc(gPath_Cls, regex,
				false);// 降順ソート
		if (listNext != null) {
			load2Combo(comboBox2, listNext);
			load2Combo(comboBox4, listNext);
		}
		// '条件設定側のコンボも同期させる ※注意！コンボ４はコンボ３とも同期しています
		listQtb1.clear();
		listQtb2.clear();
		// If Me.ListQtb3.ListCount <> 0 Then
		//  
		// End If
		comboBox2.setVisible(true);
	}

	public boolean msgBox(String title, String msg) {
		int option = JOptionPane.showConfirmDialog((Component) this, title, msg,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	// private void ComResAll_Click() {
	//
	// }

	private void ComboBox2_Click() {
		// If Me.Frame1.Left <> 0 Then Call CommandButton1_Click
		// if (msgBox("確認","抽出条件をクリアしますか？")){
		// ComResAll_Click();
		// }
		list1.clear();
		list3.clear();
		// XXX path　を正規化する
		gPath_Idx = gPath_Cls + "/" + comboBox2.getSelectedItem(); // ■term■
		gPath_Idx = FileUtil.normarizeIt(gPath_Idx);
		// -----------------------------------------------------------
		// '同じアンケートを使っている場合のみ連動
		// -----------------------------------------------------------
		if (comboBox1.getSelectedItem() == comboBox3.getSelectedItem()) {
			// Me.ComboBox4 = Me.ComboBox2
		}
		// -----------------------------------------------------------
		LabInfo2.setText("しばらくお待ちください（パラメータを読み込んでいます）");
		frame1.setVisible(false);
		setHourglass(true);
		lPath.setText(gPath_Idx);
		list1.setVisible(false);
		list3.setVisible(false);
		Dimension_Load2(gPath_Idx, list1, "", "");
		Dimension_Load2(gPath_Idx, list3, "", "");
		list1.setVisible(true);
		list3.setVisible(true);
		frame1.setVisible(true);
		LabInfo2.setText("パラメータ読み込み完了しました。");
		setHourglass(false);

		multiPage1.setSelectedIndex(0); // tabPane の操作

		textTitle.setText("ＱＰＲアンケートクロス");
		textCom1.setText(comboBox1.getSelectedItem() + "_"
				+ comboBox2.getSelectedItem() + "_");
		textCom2.setText(comboBox1.getSelectedItem() + "_"
				+ comboBox2.getSelectedItem() + "_");
		// CommQuick.setVisible(false);
		// If Dir(gPath_Idx + "\qtb2.txt") = "" Then Me.CommQuick.Visible = True
		// if (!FileUtil.isExists(gPath_Idx + "/qtb2.txt"))
		// CommQuick.setVisible(true);

	}

	protected void Dimension_Load2(String pPath, InfListPanel outlet,
			String pLimit, String pComm) {
		outlet.clear();
		String xNam;
		String xKey;
		String xTbl;
		String xTyp;
		String xFld;
		String[] wOpts;
		String wOpt;
		String wProxy;
		xTbl = "";
		if (FileUtil.isExists(pPath + "/ALIAS.TXT")) {
			xTbl = "ALIAS";
			wProxy = "$"; // $$$
		} else {
			wProxy = "";
			if (pLimit.equals("M01")) {
				xTbl = "RCL";
			} else if (pLimit.equals("Q01")) {
				xTbl = "RCL";
			} else {
				xTbl = "ASM";
			}
		}
		String xComm;
		String xParm;
		// ---------------------------------------------------------------------
		int xmax;
		// String xCnn;
		String xSql;
		if (pLimit.equals("M01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else if (pLimit.equals("Q01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else {
			xSql = "SELECT Nam,Key,Typ,Opt FROM QTB1#TXT WHERE (Typ = 'SINGLE' OR Typ = 'MULTI') AND Mot = 'ROOT' ORDER BY Srt";
		}
		List xVector = ODBC_SQL2VECTOR(xSql, pPath);
		if (xVector != null && xVector.size() > 0) {
			xmax = xVector.size();
		} else {
			System.out.println("#error @SQL");
			return;
		}
		List listData = new ArrayList();

		if (xmax > 0) {
			for (Iterator iterator = xVector.iterator(); iterator.hasNext();) {
				List list = (List) iterator.next();
				String val = (String) list.get(0);
				if (val.equals("")) {
				} else if (val.equals("ID")) {
				} else if (val.equals("SAMPLENUMBER")) {
				} else {
					xFld = wProxy;
					xNam = (String) list.get(0);
					xKey = (String) list.get(1);
					xTyp = (String) list.get(2);
					wOpt = (String) list.get(3);
					if (wOpt != null) {
						wOpts = wOpt.split(":");
						if (wOpts.length > 0) {
							if (!wOpts[0].trim().equals(""))
								xFld = wOpts[0];
						}
					}
					xParm = vbTab + pPath + vbTab + xTbl + vbTab + xKey + vbTab
							+ xFld + vbTab + xTyp;
					// comm 設定
					if (!pLimit.equals("")) {
						if (xKey.indexOf("@") > 0) {
							if (xKey.indexOf(pLimit) > 0) {
								xComm = pComm.trim() + " " + xNam.trim();
							} else {
								xComm = "";
							}
						} else {
							xComm = xNam.trim() + " ";
						}
					} else {
						xComm = xNam.trim() + " ";
					}
					if (!xComm.equals("")) {
						// System.out.println("xComm:" + xComm+ " xParm:" +
						// xParm);
						listData.add(xComm + VTAB + xParm);
						// outlet.AddItem
						// outlet.List(outlet.ListCount - 1, 0) = xComm
						// outlet.List(outlet.ListCount - 1, 1) = xParm
					}
				}
			}
			outlet.setListData(listData);
		}
	}

	private void ComboBox3_Click() {
		textCom3.setText("");
		gCndPath_Cls = gPath_Sys + "/" + comboBox3.getSelectedItem(); // ■cls■
		gCndPath_Cls = FileUtil.normarizeIt(gCndPath_Cls);
		listQtb1.clear();
		listQtb2.clear();
		String regex = "";
		List listNext = ListArrayUtil.dir2List_asc(gCndPath_Cls, regex, false);
		if (listNext != null) {
			load2Combo(comboBox4, listNext);
		}

	}

	private void ComboBox4_Click() {
		gCndPath_Idx = gCndPath_Cls + "/" + comboBox4.getSelectedItem();// '■term■
		// String xStr = (String)comboBox4.getSelectedItem();
		// int xPoz = xStr.indexOf(vbTab);
		// ---------------------------------------------------------------------
		// 条件設定用の設問＆選択肢ロード
		// ---------------------------------------------------------------------
		setHourglass(true);
		LabInfo2.setText("設問項目検索中・・・");
		frame2.setEnabled(true);
		frame2.setVisible(true);
		listQtb1.clear();
		listQtb2.clear();
		listQtb1.setVisible(false);
		Dimension_Load2(gCndPath_Idx, listQtb1, "", "");
		listQtb1.setVisible(true);
		setHourglass(false);
		frame0.setVisible(true);
		LabInfo2.setText("ＯＫ！");
		textCom3.setText(
				comboBox3.getSelectedItem() + "" + comboBox4.getSelectedItem());
	}

	// private String NameCheck(String zStr, int xLim, boolean pNarrow) {
	// return "";
	// }

	// Function NameCheck(zStr As String, xLim As Integer, Optional pNarrow) As
	// String
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' 概要 : オブジェクト名称をチェック＆正規化します
	// ' 引数 : zStr 検査する文字列 xLim:最大文字列長
	// ' 戻り値 : 変換された文字列
	// ' 使用例 : wStr = NameCheck(Sorce,30)
	// 'Print NameCheck("HML (Aグループご主人)", 30)
	// '--------------------------------------------------------------------------------------
	// 1997 YUASA --
	// ' ＜ACCESS OBJECTの名前を付けるためのガイドライン＞
	// ' 長さは半角で ６４ 文字以下です。(全角３２文字)
	// ' 1234567890123456789012345678901234567890123456789012345678901234<この位の幅
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' ＜TRANSACT-SQL＞
	// ' 長さは半角で１～３０バイト（全角１５文字）
	// ' SQL:最初の文字は漢字(Space以外)、英字、下線_@#になります
	// ' ＠で始まる識別子は変数、＃で始まる識別子は一時テーブルです
	// ' ２文字以降は、漢字、英字、数字、または記号$#_になります
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' ＜回避すべき文字＞
	// ' 演算子
	// ' 区切り文字 ピリオド (.)、感嘆符 (!)、バック クォーテーション (')など
	// ' 括弧 角かっこ ([ ]) など
	// ' 制御文字制御文字 (ASCII 値 0 ～ 31) を含めることはできません。
	// ' Spaceも避けておく
	// ' キーワード（IF、THEN、ELSE）は不可
	// ' "'`/*-+=<>()[]{}!?|:;.,\&%~^
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// ' xx 1.全角＞半角変換
	// ' 2.トークンを除去
	// ' 3.先頭の文字チェック
	// ' 4.文字列長を調整
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// 'wStr = Translate(Sorce, "（）＜＞［］", "()<>[]") '文字列中の括弧を全角文字に変換します
	// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
	// Dim xStr As String
	// Dim wPos As Integer
	// Dim wNXT As Integer
	// Dim wStr As String
	// Dim xIdx As Integer
	// If IsMissing(pNarrow) Then pNarrow = False
	// xStr = " ""'`/*-+=<>()[]{}!?|:;.,\&%~^" '除去対象文字
	// If pNarrow = True Then zStr = Trim(StrConv(zStr, vbNarrow)) '一旦すべてを半角化する
	// zStr = Translate(zStr, "【】「」『』【】「」『』", "()<>[]（）＜＞［］")
	// '文字列中の括弧を全角文字に変換します
	// For xIdx = 1 To Len(xStr) Step 1
	// wStr = ""
	// wNXT = 1
	// wPos = InStr(wNXT, zStr, Mid(xStr, xIdx, 1))
	// While wPos > 0
	// Debug.Print "wPos:" & wPos
	// If wPos > wNXT Then wStr = wStr & Mid(zStr, wNXT, wPos - wNXT)
	// wNXT = wPos + 1
	// wPos = InStr(wNXT, zStr, Mid(xStr, xIdx, 1))
	// Wend
	// zStr = wStr & Mid(zStr, wNXT, (Len(zStr) - wNXT + 1))
	// Next xIdx
	// wStr = zStr
	// If Len(wStr) > xLim Then wStr = Mid(wStr, 1, xLim)
	// NameCheck = wStr
	// End Function

	private void listQtb2_DblClick() {
		String xPfx = "";
		if (checkCom3.isSelected()) {
			xPfx = textCom3.getText();
			// xPfx = NameCheck(xPfx, 20) //TODO 後で実装しておく
			textCom3.setText(xPfx);
		}
		String pPrm1 = "";
		String pPrm2 = "";
		int xLdx = listQtb2.getSelectedIndex();
		pPrm1 = (String) listQtb2.getValueX(xLdx, 0);
		pPrm2 = (String) listQtb2.getValueX(xLdx, 1);
		System.out.println("pPrm1:" + pPrm1);
		System.out.println("pPrm2:" + pPrm2);
		// pPrm1 = listQtb2.List(xLdx, 0)
		// pPrm2 = listQtb2.List(xLdx, 1)
		if (CondePages.getSelectedIndex() > 1)
			CondePages.setSelectedIndex(0);
		if (CondePages.getSelectedIndex() == 0) {
			Add2pCtr(pPrm1, pPrm2, condPanel1.getListPanel(),
					CondCAL.getSelectedItem(), xPfx + lCom.getText());
		} else if (CondePages.getSelectedIndex() == 1) {
			Add2pCtr(pPrm1, pPrm2, condPanel2.getListPanel(),
					CondCAL.getSelectedItem(), xPfx + lCom.getText());
		}
	}

	private boolean IsNumeric(String val) {
		// TODO ヤッツケ版
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected int MsgBox(String msg) {
		// TODO ヤッツケ版
		return 0;
	}

	protected int MsgBox(String msg, int style, String caption) {
		// TODO ヤッツケ版
		return 0;
	}

	private String StripChar(String src, String strip) {
		// TODO ヤッツケ版
		return src;
	}

	private void Add2pCtr(String pPrm1, String pPrm2, InfListPanel pCtr,
			String pCLQ, String pComm) {
		String[] wChunk = null;
		String xPth = "";
		String xTbl = "";
		String xDat = "";
		String xKey = "";
		String xVal = "";
		String xTyp = "";
		String xCol = "";
		String xLen = "";
		String xCm1 = "";
		String xCm2 = "";
		xCm1 = "『" + pComm.trim() + "』";
		xCm2 = "『" + pPrm1.trim() + "』";
		wChunk = pPrm2.split(vbTab);
		if (wChunk == null || wChunk.length == 0)
			return;
		xPth = wChunk[0];
		xTbl = wChunk[1];
		xDat = wChunk[2];
		xKey = wChunk[3];
		xVal = wChunk[4];
		xTyp = wChunk[5];
		xCol = wChunk[6];
		xLen = wChunk[7];
		System.out.println(
				"debug 20101128 ########################################");
		System.out.println("xCm1:" + xCm1);
		System.out.println("xCm2:" + xCm2);

		System.out.println("xPth:" + xPth);
		System.out.println("xTbl:" + xTbl);
		System.out.println("xDat:" + xDat);
		System.out.println("xKey:" + xKey);
		System.out.println("xVal:" + xVal);
		System.out.println("xTyp:" + xTyp);
		System.out.println("xCol:" + xCol);
		System.out.println("xLen:" + xLen);

		String xComm = "";
		String xParm = "";
		String xCnd = "";
		String Msg = "";
		int vbOKOnly = 2;
		int vbCritical = 4;
		int Style = 0;
		if (xTyp.equals("INTEGER") || xTyp.equals("REAL")
				|| xTyp.equals("COUNT")) {
			// 数値の場合
			if (IsNumeric(xVal) == false) {
				Msg = "比較する値が数値ではありません！\n数値を指定して下さい。";
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "警告!");
				return;
			}
			if (pCLQ.equals("Like") || pCLQ.equals("Like XXX?")
					|| pCLQ.equals("Like ?XXX")) {
				Msg = "比較する値が数値なのでLike演算子は使えません！\n他の演算子を指定して下さい。";
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "警告!");
				return;
			}
		} else if (xTyp.equals("MULTI") || xTyp.equals("SINGLE")
				|| xTyp.equals("ALPHA")) {
			if (pCLQ.equals("Like")) {
				xVal = "'*" + StripChar(xVal, "'").trim() + "*'";
			} else if (pCLQ.equals("Like XXX?")) {
				xVal = "'" + StripChar(xVal, "'").trim() + "*'";
			} else if (pCLQ.equals("Like ?XXX")) {
				xVal = "'*" + StripChar(xVal, "'").trim() + "'";
			} else {
				if (xVal.equals(""))
					xVal = "NULL";
				if (xVal.toUpperCase().equals("NULL")) {
					xVal = "NULL";
				} else {
					xVal = "'" + StripChar(xVal, "'") + "'";
				}
			}
		} else {
			return;
		}
		xCnd = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+;
		if (pCLQ.equals("Like")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "に" + xCm2 + "という文字が含まれるもの";
		} else if (pCLQ.equals("Like XXX?")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "という文字で始まるもの";
		} else if (pCLQ.equals("Like ?XXX")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "という文字で終わるもの";
		} else if (pCLQ.equals("IN")) {
			xCnd = " IN (" + xVal + ") ";
			xComm = xCm1 + "が、" + xCm2 + " のうちどれか";
		} else if (pCLQ.equals("NOT IN")) {
			xCnd = " NOT IN (" + xVal + ") ";
			xComm = xCm1 + "が、" + xCm2 + "のうちどれでもないもの";
		} else if (pCLQ.equals("=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "であるもの";
		} else if (pCLQ.equals("<>")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "ではないもの";
		} else if (pCLQ.equals(">")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "より大きいもの";
		} else if (pCLQ.equals(">=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "以上であるもの";
		} else if (pCLQ.equals("<")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " が、" + xCm2 + "より小さいもの";
		} else if (pCLQ.equals("<=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " が、" + xCm2 + "以下であるもの";
		} else {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " " + pCLQ + " " + xCm2 + "であるもの";
		}
		xParm = pPrm2 + vbTab + xCnd;
		// '-----+---------+---------+---------+---------+---------+---------+---------+---------+---------+;
		// int i ;
		boolean xFlg = false;
		// 同じものが追加されないようになっている・・・のかなたぶん
		List list = pCtr.getList();
		System.out.println("forDEBUG xComm ==> " + xComm);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String object = (String) iterator.next();
			System.out.println("forDEBUG selected ==> " + object);
		}
		// For i = 0 To pCtr.ListCount - 1;
		// if pCtr.List(i, 0) = xComm Then;
		// xFlg = True;
		// Exit For;
		// End if;
		// Next i;
		if (xFlg == false) {
			pCtr.addElement(xComm + VTAB + xParm); // どうだろう？？＠2010-11-28
			// pCtr.AddItem //wStr;
			// pCtr.List(pCtr.ListCount - 1, 0) = xComm;
			// pCtr.List(pCtr.ListCount - 1, 1) = xParm;
		}
	}

	private void listQtb1_DblClick(InfListPanel outlet) {
		// String xCnn="";
		String xSql = "";
		List xVector;
		int xmax;
		// ---------+-------
		String xCom;
		String xComm;
		String xParm;
		// ---------+-------
		String mKey;
		String mTyp;
		// ---------+-------
		String wChunk[];
		String xPrm;
		int xLdx;
		// ---------+-------
		String mPth = "";
		String mTbl = "";
		String xFld = "";
		String xKey = "";
		String xVal = "";
		String xTyp = "";
		String xCol = "";
		String xLen = "";
		// ---------+-------
		String wProxy;
		String wOpts[];
		String wOpt;
		xLdx = listQtb1.getSelectedIndex();
		xCom = (String) listQtb1.getElementAt(xLdx);
		xPrm = (String) listQtb1.getValueX(xLdx);
		// 例＞
		// xCom:年代×性別
		// xPrm:年代×性別  Z:/s2/rx/Enquetes/NQ/ＴＡＧ飲料調査/2009 ASM CELL SINGLE
		System.out.println("xCom:" + xCom);
		System.out.println("xPrm:" + xPrm);
		wChunk = xPrm.split(vbTab);
		if (wChunk.length == 0)
			return;
		mPth = wChunk[1]; // インデックスのパスと同じ
		mTbl = wChunk[2]; // ALIAS ASMなど
		mKey = wChunk[3]; // AQA <Mother KEY>
		wProxy = wChunk[4]; // $
		mTyp = wChunk[5]; // SINGLE
		System.out.println("mPth   :" + mPth);
		System.out.println("mTbl   :" + mTbl);
		System.out.println("mKey   :" + mKey);
		System.out.println("wProxy :" + wProxy);
		System.out.println("mTyp   :" + mTyp);
		String wQtb = "";
		if (FileUtil.isExists(mPth + "/QTB1.TXT"))
			wQtb = "QTB1#TXT";
		if (FileUtil.isExists(mPth + "/QTB2.TXT"))
			wQtb = "QTB2#TXT";
		if (wQtb.equals("")) {
			// MsgBox "インデックスファイルが見つかりませんでした。"
			System.out.println("#ERROR! インデックスファイルが見つかりませんでした。:" + mPth);
			return;
		}
		// -----------------------------------------------------------
		lCom.setText(xCom);
		lKey.setText(mKey);
		lTyp.setText(mTyp);
		// -----------------------------------------------------------
		listQtb2.clear();
		// -----------------------------------------------------------
		if (!FileUtil.isExists(mPth + "/Schema.ini")) {
			String msg = "カテゴリーデータが存在しませんでした" + vbCrLf + mPth + "/Schema.ini"
					+ vbCrLf + "他のカテゴリーを選んでください。";
			// MsgBox msg
			System.out.println("#ERROR! " + msg);
			frame0.setVisible(true);
			return;
		}
		listQtb1.setVisible(false);
		listQtb2.setVisible(false);
		TextCond.setVisible(false);
		setHourglass(true);
		// -----------------------------------------------------------
		// 選択された項目タイプが、
		// -----------------------------------------------------------
		if (mTyp.equals("SINGLE") || mTyp.equals("MULTI")) {
			CondCAL.setVisible(true);
			CommAdd.setVisible(false);
			TextCond.setVisible(false);
			listQtb2.setVisible(true);
			TextCond.setText("");
			CondCAL.setSelectedItem("=");
			// -------------------------------------------------------
			xSql = "SELECT Nam,Key,Val,Typ,Col,Len,Opt FROM " + wQtb
					+ " WHERE Mot = '" + mKey + "' ORDER BY Srt";
			// xCnn =
			// "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=" +
			// mPth +
			// ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
			xVector = ODBC_SQL2VECTOR(xSql, mPth);
			xmax = xVector.size();
			listQtb2.clear();
			if (xmax > 0) {
				for (Iterator iterator = xVector.iterator(); iterator
						.hasNext();) {
					List xVlist = (List) iterator.next();
					xKey = (String) xVlist.get(1);
					xVal = (String) xVlist.get(2);
					xTyp = (String) xVlist.get(3);
					xCol = ((Integer) xVlist.get(4)).toString();
					xLen = ((Integer) xVlist.get(5)).toString();
					wOpt = (String) xVlist.get(6);
					if (wOpt != null) {
						wOpts = wOpt.split(":");
						if (wOpts.length >= 0) {
							if (!wOpts[0].trim().equals(""))
								xFld = wOpts[0];
						}
					}
					xFld = wProxy;
					xComm = xVlist.get(0) + "                             ";
					xParm = mPth + vbTab + mTbl + vbTab + xFld + vbTab + xKey
							+ vbTab + xVal + vbTab + xTyp + vbTab + xCol + vbTab
							+ xLen;
					if (!xComm.equals("")) {
						outlet.addElement(xComm + VTAB + xParm);
					}
				}
				// -------------------------------------------------------
				// Formula for 条件抽出
				// -------------------------------------------------------
				CondCAL.removeAll();
				CondCAL.addItem("=");
				CondCAL.addItem("<>");
				CondCAL.setSelectedIndex(0);
			}
		} else if (mTyp.equals("COUNT") || mTyp.equals("INTEGER")
				|| mTyp.equals("REAL")) {
			CondCAL.setVisible(true);
			CommAdd.setVisible(true);
			TextCond.setVisible(true);
			listQtb2.setVisible(false);
			TextCond.setText("0");
			// -------------------------------------------------------
			// Formula for 条件抽出
			// -------------------------------------------------------
			CondCAL.removeAll();
			CondCAL.addItem("=");
			CondCAL.addItem("<>");
			CondCAL.addItem(">");
			CondCAL.addItem(">=");
			CondCAL.addItem("<");
			CondCAL.addItem("<=");
			CondCAL.addItem("IN");
			CondCAL.addItem("NOT IN");
			CondCAL.setSelectedIndex(0);
		} else if (mTyp.equals("ALPHA")) {
			// あとで考えよう！！このままでは駄目！
			String qCnn = ""; // アンケートデータへの接続パラメータ
			String qTbl = ""; // アンケートデータ、テーブル
			String qDat = ""; // アンケートデータ、データフィールド
			String qKey = ""; // アンケートデータ、キー
			String qCnd = ""; // アンケートデータ、コンディション
			if (mTbl.toUpperCase().equals("ALIAS")) {
				// ParseAlias エイリアスファイルを読み込む
				if (!FileUtil.isExists(mPth + "/ALIAS.TXT")) {
					String msg = "エイリアスが見つかりませんでした：" + mPth;
					// MsgBox msg
					System.out.println("#ERROR! " + msg);
					return;
				}
				ParseAlias(mPth + "/ALIAS.TXT", qCnn, qTbl, qDat, qKey, qCnd);
			}
			CondCAL.setVisible(true);
			CommAdd.setVisible(true);
			TextCond.setVisible(true);
			listQtb2.setVisible(true);
			TextCond.setText("");
			// -------------------------------------------------------
			if (qCnn.equals("")) {
				qCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
						+ mPth
						+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
				qTbl = "[" + mTbl + "#TXT]";
			}
			xSql = "SELECT [" + mKey + "] FROM " + qTbl + " " + " GROUP BY ["
					+ mKey + "] ORDER BY [" + mKey + "] ;";
			System.out.println("xSql:" + xSql);
			// -------------------------------------------------------
			xVector = ODBC_SQL2VECTOR(xSql, mPth);
			xmax = xVector.size();

			listQtb2.setVisible(false);
			listQtb2.clear();
			if (xmax > 0) {
				for (Iterator iterator = xVector.iterator(); iterator
						.hasNext();) {
					List<String> xVlist = (List) iterator.next();
					xComm = xVlist.get(0) + "                             ";
					xKey = mKey;
					xVal = xVlist.get(0);
					xTyp = "ALPHA";
					// xCol = "???"
					// xLen = "???"
					// //xParm = xComm + vbTab + mKey + vbTab + xVal
					xParm = mPth + vbTab + mTbl + vbTab + xFld + vbTab + xKey
							+ vbTab + xVal + vbTab + xTyp + vbTab + xCol + vbTab
							+ xLen;
					if (!xComm.equals("")) {
						outlet.addElement(xComm + VTAB + xParm);
					}
				}
			}
			listQtb2.setVisible(true);
			// -------------------------------------------------------
			// Formula for 条件抽出
			// -------------------------------------------------------
			CondCAL.removeAll();
			CondCAL.addItem("=");
			CondCAL.addItem("<>");
			CondCAL.addItem(">");
			CondCAL.addItem(">=");
			CondCAL.addItem("<");
			CondCAL.addItem("<=");
			CondCAL.addItem("Like");
			CondCAL.addItem("Like XXX?");
			CondCAL.addItem("Like ?XXX");
			// CondCAL.addItem("IN");
			// CondCAL.addItem("NOT IN");
			CondCAL.setSelectedIndex(0);
		} else {
			// MsgBox "type error!!!" & mTyp
		}
		listQtb1.setVisible(true);
		setHourglass(false);
		repaint();
	}

	private void ParseAlias(String pFile, String gd_Cnn, String gd_Tbl,
			String gd_Fld, String gd_Key, String gd_Cnd) {

	}

	// Private Sub CommandButton1_Click()
	// If Me.Frame1.Left = 0 Then

	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// Dim wChunk() As String
	// Dim wStr As String
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// If (outlet1.ListCount = 0) And (outlet2.ListCount > 0) Then
	// wStr = outlet2.List(0, 1)
	// wChunk = Split(wStr, vbTab)
	// wChunk(3) = "FIXX"
	// wChunk(4) = "1"
	// wChunk(5) = "SINGLE"
	// wChunk(6) = "'1'"
	// wChunk(7) = "Char     Width 1"
	// wStr = Join(wChunk, vbTab)
	// outlet1.AddItem
	// outlet1.List(0, 0) = "【Ｇ／Ｔ】" & vbTab & "全体" 'outlet2.List(0, 0)
	// outlet1.List(0, 1) = wStr 'outlet2.List(0, 1)
	// Call setSelectAll(outlet1)
	// End If
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// If (outlet2.ListCount = 0) And (outlet1.ListCount > 0) Then
	// wStr = outlet1.List(0, 1)
	// wChunk = Split(wStr, vbTab)
	// wChunk(3) = "FIXX"
	// wChunk(4) = "1"
	// wChunk(5) = "SINGLE"
	// wChunk(6) = "'1'"
	// wChunk(7) = "Char     Width 1"
	// wStr = Join(wChunk, vbTab)
	// outlet2.AddItem
	// outlet2.List(0, 0) = "【Ｇ／Ｔ】" & vbTab & "全体" 'outlet1.List(0, 0)
	// outlet2.List(0, 1) = wStr 'outlet1.List(0, 1)
	// Call setSelectAll(outlet2)
	// End If
	// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
	// outlet1.Height = 92.05
	// outlet2.Height = 92.05
	// DoEvents
	// ChkHeadSum = False
	// ChkSideSum = False
	// If outlet1.ListCount = 1 Then
	// ChkFunk6 = True
	// ChkFunk1 = False
	// ChkFunk2 = True
	// ElseIf outlet2.ListCount = 1 Then
	// ChkFunk6 = True
	// ChkFunk1 = True
	// ChkFunk2 = False
	// Else
	// ChkFunk1 = True
	// ChkFunk2 = True
	// End If
	// If outlet1.ListCount > 0 And outlet2.ListCount > 0 Then
	// Me.ComGoGo.Visible = True
	// Else
	// Me.ComGoGo.Visible = False
	// End If
	// Else
	// gDirty1 = True '2008/11/10
	// gDirty2 = True '2008/11/10
	// inlet1.Enabled = True
	// inlet2.Enabled = True
	// For i = 1 To 480 Step 10
	// Me.Frame1.Left = -480 + i
	// DoEvents
	// Next i
	// Me.Frame1.Left = 0
	// CommandButton1.Caption = "選択項目の確認→作表へ"
	// Me.LabInfo2 = ""
	// End If
	// SetHourglass False
	// DoEvents
	// End Sub

	public void testCommandButton1_Click() {
		setHourglass(true);
		comGoGo.setVisible(false);
		// DoEvents
		multiPage1.setSelectedIndex(0);
		// Me.ScrollBar1.value = 0
		// Me.ScrollBar2.value = 0

		// Dim inlet1 As control
		// Dim outlet1 As control
		// Dim inlet2 As control
		// Dim outlet2 As control
		// Set inlet1 = Me.List2
		// Set outlet1 = Me.ListDst1
		// Set inlet2 = Me.List4
		// Set outlet2 = Me.ListDst2
		if (frame1.getLocation().x == 0) {
			for (int i = 0; i < 350; i += 2) {
				frame1.setLeft(0 - i);
				try {
					// Thread.sleep(1);
				} catch (Exception e) {
				}
				frame1.repaint();
			}
			repaint();

			CommandButton1.setText("表頭表側の設定に戻る");
			frame1.setLeft(-252);
			frame1.setLeft(-350);
			// inlet1.Enabled = False
			// inlet2.Enabled = False
			// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
			if (gDirty1) {
				gDirty1 = false;
				LabInfo2.setText("表頭要素を検索中・・・");
				// DoEvents
				// outlet1.Clear
				// For i = 0 To inlet1.ListCount - 1
				// xStr = inlet1.List(i, 0) & inlet1.List(i, 1)
				// Call ListDetails(xStr, outlet1)
				// Next i
				// Call setSelectAll(outlet1)
				LabInfo2.setText("不要な項目はチェックマークを外してください");
				LabInfo2.setText("集計票タイトルと出力条件を指定してください");
				// DoEvents
			}
			// '-------+---------+---------+---------+---------+---------+---------+---------+---------+
			if (gDirty2) {
				gDirty2 = false;
				LabInfo2.setText("表側要素を検索中・・・");
				// DoEvents
				// outlet2.Clear
				// For i = 0 To inlet2.ListCount - 1
				// xStr = inlet2.List(i, 0) & inlet2.List(i, 1)
				// Call ListDetails(xStr, outlet2)
				// Next i
				// Call setSelectAll(outlet2)
				LabInfo2.setText("不要な項目はチェックマークを外してください");
				LabInfo2.setText("集計票タイトルと出力条件を指定してください");
				// DoEvents

			}

		} else {
			for (int i = 350; i >= 0; i -= 2) {
				frame1.setLeft(i);
				try {
					// Thread.sleep(1);
				} catch (Exception e) {
				}
				frame1.repaint();
			}
		}
		repaint();
		setHourglass(false);
		comGoGo.setVisible(true);
	}

}