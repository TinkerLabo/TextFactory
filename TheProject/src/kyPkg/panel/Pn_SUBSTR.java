package kyPkg.panel;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.converter.SubstrCnv;
import kyPkg.external.ColorControl;
import kyPkg.filter.*;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.TableMouseListener;
import kyPkg.gridPanel.Grid_Panel;
import kyPkg.mySwing.ListMouseAdapter;
import kyPkg.mySwing.ListPanel;
import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.uFile.MatrixUtil;
import kyPkg.util.DnDAdapter;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.File2Matrix;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File; //import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; //import java.util.List;
import java.util.Vector;

//XXX　I/Oのエンコードをそれぞれ指定可能にしたい（ex:　utf8=>sjisなど）
//XXX　パラメータの　”□　インサート”というチェックボックス＆処理がほしい
//-----------------------------------------------------------------------------
//《サブストリングのプロトタイプ》 kyPkg.panel.Pn_SUBSTR
//-----------------------------------------------------------------------------
//※クラスの名前を変更禁止！！（jarファイルが作れなくなる等々影響あり）
public class Pn_SUBSTR extends BorderPanel {
	// -------------------------------------------------------------------------
	private int colWidth = 30;//21;
	private static final String DLM_TAB = "タブ";
	private static final String DLM_COMMA = "カンマ";
	private static final String DLM_SPACE = "スペース";
	private static final String DLM_RETURN = "改行";
	// -------------------------------------------------------------------------
	private static final String AUTO_DETECT = "AutoDetect";
	private static final String EXECUTE = "実行";
	private static final String DISPLAY_CNT = "DisplayCnt";
	private static final String FILE_ENCODING = "file.encoding";
	private static final String OUT_ENCODE = "OutEncode:";
	private static final String LIMIT = "Limit:";
	private static final String ADD = "Add";
	private static final String INSERT = "Insert";
	private static final String REPLACE = "Replace";
	private static final String GENERATE = "Generate";
	private static final String SAVE = "Save";
	private static final String RESET = "Reset";
	private static final String DELIMITER = "←区切文字";
	private static final String COMMENT = "コメント";
	private static final String FIX_STRING = "←固定文字";
	private static final String OTHER = "Other:";
	private static final String TO = "To:";
	private static final String FROM = "From:";
	private static final String COL = "Col:";
	private static final String WELCOME = "Welcome!";
	private static final String PARM = "parm ";
	private static final String INPUT = "input";
	private static final String DEFAULT_MESSAGE = "固定文字列に、@TODAYを指定すると日付、@SEQを指定すると行番号を出力できます。";
	private static final String LINE1_AS_A_HEADER = "一行目をヘッダとして扱う";
	private static final String NEED_HEADER = "要ヘッダ出力";
	private static final String EXT_SCOPE = "←抽出範囲";
	// -------------------------------------------------------------------------
	private int preCol = 0;
	private int colMin = -1;
	private int colMax = -1;
	private static final String escapeComma = "_";
	private static final long serialVersionUID = 8693278004908452341L;
	// -------------------------------------------------------------------------
	// L o c a l 変 数
	// -------------------------------------------------------------------------
	private int jackUp = 1;    // パラメータに履かせるゲタ
	private int depth = 100;   // 検査深度
	private int skipCount = 0; // 読み飛ばし（ヘッダー分をスキップさせる場合）
	private JButton btnGoAhead;
	private JButton btnAutoDetect;
	private JCheckBox flgHeadPrint;
	private JCheckBox colNameHeader;
	private JRadioButton[] rbtOptions;
	private ButtonGroup btnGrp;
	private PnlFile pnlFile_P;
	private PnlFile pnlFile_I;
	private ListPanel plistPanel;
	private Grid_Panel gridPanel1;
	private Grid_Panel gridPanel2;
	private JTextField txtCol;
	private JTextField txtFrom;
	private JTextField txtTo;
	private JTextField txtOther;
	private JTextField txtFixed;
	private JTextField txtComnt;
	private JComboBox cmbDepth;
	private JComboBox cmbEncodeo;
	private JComboBox cmbLimit;
	private JComboBox cmbDlm;
	private JComboBox cmbFlt;
	private JButton btnAdd01;
	private JButton btnAdd02;
	private JButton btnAdd03;
	private JButton btnReset;
	private JButton btnSaveP;
	private JButton btnGen;
	private JLabel labInfo;
	private SubstrCnv converter;
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Pn_SUBSTR() {
		super();
		this.setSize(new Dimension(1000, 520));
		converter = new SubstrCnv();
		createGUI(); // GUI部作成
	}

	private String getComment() {
		String rtn = "\t" + txtComnt.getText();
		txtComnt.setText("");
		return rtn;
	}

	// -------------------------------------------------------------------------
	// パラメータオブジェクト
	// -------------------------------------------------------------------------
	private void ParmsObj(String parm) {
		String[] splited_B = parm.split("\t"); // タブ以降はコメント扱いとする
		String[] splited_A = splited_B[0].split(","); // パラメータはカンマ区切り
		txtComnt.setText("");
		txtCol.setText("");
		txtFrom.setText("");
		txtOther.setText("");
		txtTo.setText("");
		txtFixed.setText("");
		if (splited_B.length > 1)
			txtComnt.setText(splited_B[1]);
		if (splited_A[0].equals("@")) {
			if (splited_A.length > 1)
				txtFixed.setText(splited_A[1]);
		} else {
			if (splited_A[0].equals("D")) {
				if (splited_A[1].equals(",")) {
					cmbDlm.setSelectedItem(DLM_COMMA);
				}
				if (splited_A[1].equals("\t")) {
					cmbDlm.setSelectedItem(DLM_TAB);
				}
			} else {
				if (splited_A[0].matches("\\d+")) {
					txtCol.setText(splited_A[0].trim());
					if (splited_A.length > 1) {
						if (!splited_A[1].equals("*")) {
							if (splited_A.length > 1
									&& splited_A[1].matches("\\d+")) {
								txtFrom.setText(splited_A[1]);
							}
							if (splited_A.length > 2
									&& splited_A[2].matches("\\d+")) {
								txtTo.setText(splited_A[2]);
							}
							if (splited_A.length > 3) {
								cmbFlt.setSelectedItem(splited_A[3]);
							}
							if (splited_A.length > 4) {
								String otherP = splited_A[4];
								otherP = otherP.replaceAll("_", ",");
								txtOther.setText(otherP);
							}
						}
					}
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// 《ＧＵＩ初期化》
	// -------------------------------------------------------------------------
	private void createGUI() {
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, INPUT);
		pmap1.put(PnlFile.OPT49ER, "true");
		pmap1.put(PnlFile.ENCODE, "true");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile_I = new PnlFile(pmap1);

		HashMap pmap2 = new HashMap();
		pmap2.put(PnlFile.CAPTION, PARM);
		pmap2.put(PnlFile.OPT49ER, "false");
		pmap2.put(PnlFile.ENCODE, "false");
		pmap2.put(PnlFile.DELIM, "true");
		pmap2.put(PnlFile.DEFAULT_PATH, "");
		pmap2.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile_P = new PnlFile(pmap2);

		//		pnlFile_I = new PnlFile(INPUT, true, true, true, "", -1);
		//		pnlFile_P = new PnlFile(PARM, false, false, true, "", -1);
		labInfo = new JLabel(WELCOME);
		labInfo.setText(DEFAULT_MESSAGE);
		//		txtComnt = new JTextField("");
		//		txtComnt.setPreferredSize(new Dimension(500, 20));
		// ---------------------------------------------------------------------
		// 入力データが変更された
		// ---------------------------------------------------------------------
		pnlFile_I.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						actionFileIn();
					}
				};
				th1.start();
			}
		});
		// ---------------------------------------------------------------------
		// パラメータが変更された
		// ---------------------------------------------------------------------
		pnlFile_P.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plistPanel.clear();
				Thread th1 = new Thread() {
					@Override
					public void run() {
						parameterRead(pnlFile_P.getPath());
					}
				};
				th1.start();

			}
		});
		// ----------------------------------------------------------------------
		// パラメータリストパネル
		// ----------------------------------------------------------------------
		plistPanel = new ListPanel(false, false);
		plistPanel.addMouseListener(new ListMouseAdapter(true) {
			@Override
			public void clicked(Object obj) {
				if (obj == null)
					return;
				ParmsObj(obj.toString());
			}
		});

		gridPanel1 = new Grid_Panel(new DefaultTableModelMod());
		gridPanel2 = new Grid_Panel(new DefaultTableModelMod());

		JPanel pnlOpt = new JPanel(new FlowLayout(FlowLayout.LEFT));
		colNameHeader = new JCheckBox(LINE1_AS_A_HEADER);
		colNameHeader.setPreferredSize(new Dimension(150, 20));
		pnlOpt.add(colNameHeader);

		flgHeadPrint = new JCheckBox(NEED_HEADER);
		flgHeadPrint.setPreferredSize(new Dimension(150, 20));
		pnlOpt.add(flgHeadPrint);

		cmbDepth = new JComboBox();
		cmbDepth.setPreferredSize(new Dimension(70, 20));
		cmbDepth.addItem("100");
		cmbDepth.addItem("300");
		cmbDepth.addItem("500");
		cmbDepth.addItem("700");
		cmbDepth.addItem("900");
		cmbDepth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					depth = Integer
							.parseInt(cmbDepth.getSelectedItem().toString());
				} catch (NumberFormatException ex) {
				}
			}
		});
		cmbDepth.setSelectedItem("100");
		pnlOpt.add(new JLabel(DISPLAY_CNT));
		pnlOpt.add(cmbDepth);

		btnAutoDetect = new JButton(AUTO_DETECT);
		btnAutoDetect.setPreferredSize(new Dimension(150, 20));
		pnlOpt.add(btnAutoDetect);

		JPanel pnlPos = new JPanel(new FlowLayout(FlowLayout.LEFT));

		txtCol = new JTextField("");
		txtCol.setPreferredSize(new Dimension(30, 20));

		txtFrom = new JTextField();
		txtFrom.setPreferredSize(new Dimension(30, 20));
		txtTo = new JTextField();
		txtTo.setPreferredSize(new Dimension(30, 20));
		txtOther = new JTextField("");
		txtOther.setPreferredSize(new Dimension(190, 20));
		new java.awt.dnd.DropTarget(txtOther, new DnDAdapter(txtOther)); // dnd.DropTarget
		btnAdd01 = new JButton(EXT_SCOPE);
		btnAdd01.setPreferredSize(new Dimension(100, 20));
		cmbFlt = new JComboBox();
		cmbFlt.setPreferredSize(new Dimension(120, 20));
		// ####################################################################
		// # 変換フィルター本体　（SubstrCnv.getFilters();）
		// ####################################################################
		List<String> filterNames = converter.getFilters();
		for (String filterName : filterNames) {
			cmbFlt.addItem(filterName);
		}
		// --------------------------------------------------------------------
		cmbFlt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionFilterSelected();
			}
		});
		// --------------------------------------------------------------------
		pnlPos.add(btnAdd01);
		pnlPos.add(new JLabel(COL));
		pnlPos.add(txtCol);
		pnlPos.add(new JLabel(FROM));
		pnlPos.add(txtFrom);
		pnlPos.add(new JLabel(TO));
		pnlPos.add(txtTo);
		pnlPos.add(cmbFlt);
		pnlPos.add(new JLabel(OTHER));
		pnlPos.add(txtOther);
		JSplitPane splitPane01 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				gridPanel1, gridPanel2);
		splitPane01.setDividerLocation(150);
		// --------------------------------------------------------------------
		// 固定文字
		// --------------------------------------------------------------------
		txtFixed = new JTextField("");
		txtFixed.setPreferredSize(new Dimension(545, 20));
		btnAdd02 = new JButton(FIX_STRING);
		btnAdd02.setPreferredSize(new Dimension(100, 20));
		JPanel pnlFixStr = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlFixStr.add(btnAdd02);
		pnlFixStr.add(txtFixed);
		// --------------------------------------------------------------------
		// コメント
		// --------------------------------------------------------------------
		JPanel pnlComnt = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel labComnt = new JLabel(COMMENT);
		labComnt.setPreferredSize(new Dimension(100, 20));
		pnlComnt.add(labComnt);
		txtComnt = new JTextField("");
		txtComnt.setPreferredSize(new Dimension(545, 20));
		pnlComnt.add(txtComnt);
		// --------------------------------------------------------------------
		// 区切り文字
		// --------------------------------------------------------------------
		JPanel pnlDlmStr = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnAdd03 = new JButton(DELIMITER);
		btnAdd03.setPreferredSize(new Dimension(100, 20));
		pnlDlmStr.add(btnAdd03);
		cmbDlm = new JComboBox();
		cmbDlm.setPreferredSize(new Dimension(120, 20));
		cmbDlm.addItem(DLM_TAB);
		cmbDlm.addItem(DLM_COMMA);
		cmbDlm.addItem(DLM_SPACE);
		cmbDlm.addItem(DLM_RETURN);
		pnlDlmStr.add(cmbDlm);
		// --------------------------------------------------------------------
		// ユーザへの情報提示
		// --------------------------------------------------------------------
		JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlInfo.add(labInfo);

		JPanel pnlSubstr = new JPanel(new BorderLayout());
		pnlSubstr.add(pnlOpt, BorderLayout.NORTH);
		pnlSubstr.add(splitPane01, BorderLayout.CENTER);

		// ----------------------------------------------------------------------
		// 解りづらいので色をつけてみた グレイスケールのほうがいいかも
		// ----------------------------------------------------------------------
		// Color blue1 = new Color(204, 255, 252);
		// Color blue2 = new Color(153, 204, 255);

		pnlPos.setBackground(ColorControl.getBlue1());
		pnlDlmStr.setBackground(ColorControl.getBlue2());
		pnlFixStr.setBackground(ColorControl.getBlue1());
		pnlComnt.setBackground(ColorControl.getBlue2());

		JPanel pnlOther = new JPanel();
		pnlOther.setLayout(new BoxLayout(pnlOther, BoxLayout.Y_AXIS));
		pnlOther.add(pnlSubstr);
		pnlOther.add(pnlComnt);
		pnlOther.add(pnlPos);
		pnlOther.add(pnlDlmStr);
		pnlOther.add(pnlFixStr);
		pnlOther.add(pnlInfo);

		btnReset = new JButton(RESET);
		btnReset.setPreferredSize(new Dimension(80, 20));

		btnSaveP = new JButton(SAVE);
		btnSaveP.setPreferredSize(new Dimension(80, 20));

		btnGen = new JButton(GENERATE);
		btnGen.setPreferredSize(new Dimension(100, 20));

		rbtOptions = new JRadioButton[4];
		btnGrp = new ButtonGroup();
		rbtOptions[0] = new JRadioButton(REPLACE);
		rbtOptions[1] = new JRadioButton(INSERT);
		rbtOptions[2] = new JRadioButton(ADD);
		rbtOptions[0].setPreferredSize(new Dimension(80, 20));
		rbtOptions[1].setPreferredSize(new Dimension(80, 20));
		rbtOptions[2].setPreferredSize(new Dimension(80, 20));
		rbtOptions[2].setSelected(true);
		btnGrp.add(rbtOptions[0]);
		btnGrp.add(rbtOptions[1]);
		btnGrp.add(rbtOptions[2]);

		// chk_Options = new JCheckBox("Replace");
		// chk_Options.setPreferredSize(new Dimension(60, 20));

		JPanel pnlLCtr1 = new JPanel();
		pnlLCtr1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel pnlLCtr2 = new JPanel();
		pnlLCtr2.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlLCtr2.add(btnReset);
		pnlLCtr2.add(btnSaveP);
		pnlLCtr2.add(btnGen);
		pnlLCtr1.add(rbtOptions[0]);
		pnlLCtr1.add(rbtOptions[1]);
		pnlLCtr1.add(rbtOptions[2]);
		// pnlLCtr1.add(chk_Options);

		JPanel pnlSide = new JPanel();
		pnlSide.setLayout(new BorderLayout());
		pnlSide.add(pnlFile_P, BorderLayout.NORTH);
		pnlSide.add(plistPanel, BorderLayout.CENTER);

		JPanel pnlOtherx = new JPanel();
		pnlOtherx.setLayout(new BoxLayout(pnlOtherx, BoxLayout.Y_AXIS));
		pnlOtherx.add(pnlLCtr1);
		pnlOtherx.add(pnlLCtr2);

		pnlSide.add(pnlOtherx, BorderLayout.SOUTH);

		JSplitPane splitPane02 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				pnlSide, pnlOther);
		splitPane02.setDividerLocation(300);

		pnlN.setLayout(new BorderLayout());
		pnlN.add(pnlFile_I, BorderLayout.CENTER);
		pnlC.add(splitPane02);
		pnlS.setLayout(new FlowLayout(FlowLayout.CENTER));

		cmbLimit = new JComboBox();
		cmbLimit.setPreferredSize(new Dimension(100, 20));
		cmbLimit.addItem("∞");
		cmbLimit.addItem("10");
		cmbLimit.addItem("100");
		cmbLimit.addItem("1000");
		pnlS.add(new JLabel(LIMIT));
		pnlS.add(cmbLimit);

		cmbEncodeo = new JComboBox();
		cmbEncodeo.setPreferredSize(new Dimension(100, 20));
		pnlS.add(new JLabel(OUT_ENCODE));
		pnlS.add(cmbEncodeo);

		List<String> charsetNames = FileUtil.getCharsetNames();
		for (String charsetName : charsetNames) {
			cmbEncodeo.addItem(charsetName);
		}
		String charsetName = System.getProperty(FILE_ENCODING);
		cmbEncodeo.setSelectedItem(charsetName);

		btnGoAhead = new JButton(EXECUTE);
		btnGoAhead.setPreferredSize(new Dimension(200, 20));
		pnlS.add(btnGoAhead);
		// --------------------------------------------------------------------
		// AutoDetect
		// XXX 修正しやすいように別のロジックに切り出しておきたい　2013-02-01
		// 　一番最後に拾った行を基準にしているのでこれではいけない・・・総括としての型判定ロジックをさせる（まぁ用はなすんんだけど）
		// --------------------------------------------------------------------
		btnAutoDetect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wDlm = (String) cmbDlm.getSelectedItem();
				// Vector<Vector> matrix = gridPanel1.getDataVector();
				Vector<Vector> matrix = gridPanel1.getDataVector();
				boolean headerOption = colNameHeader.isSelected();
				Guess4Substr guess = new Guess4Substr();
				guess.autoDetect(wDlm, matrix, headerOption);
				List<String> result = guess.getParamList();

				if (result != null && result.size() > 0) {
					plistPanel.setListData(result);
				}

			}
		});
		// --------------------------------------------------------------------
		// 実行ボタン
		// --------------------------------------------------------------------
		btnGoAhead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionGoAhead();
			}
		});
		// --------------------------------------------------------------------
		// TablePanel
		// --------------------------------------------------------------------
		gridPanel1.setTableMouseListener(new TableMouseListener() {
			// ----------------------------------------------------------------
			// マウスボタンを押した
			// ----------------------------------------------------------------
			@Override
			public void mousePressedHandle(MouseEvent evt, int row, int col) {
				preCol = col;
				System.out.println("mousePressed @ 行:" + row + " 列:" + col);
				txtCol.setText(Integer.toString(col + jackUp));
				txtFrom.setText("");
				txtTo.setText("");
				txtOther.setText("");
				Vector<Vector> matrix = gridPanel1.getDataVector();
				if (colNameHeader.isSelected() && matrix != null) {
					skipCount = 1;
					List<String> hVec = matrix.get(0);
					txtComnt.setText(hVec.get(col));
				}
				Vector<Vector> wVec = MatrixUtil.partOfVectorMatrix(matrix, col,
						20, skipCount);
				gridPanel2.setDefModel(new DefaultTableModelMod(wVec));
				gridPanel2.fixFit(colWidth);
			}

			// ----------------------------------------------------------------
			// マウスボタンを放した
			// ----------------------------------------------------------------
			@Override
			public void mouseReleasedHandle(MouseEvent evt, int releasedRow,
					int releasedCol) {
				if (releasedRow >= 0 && releasedCol >= 0) {
					System.out.println("mouseReleased @ 行:" + releasedRow
							+ " 列:" + releasedCol);
					colMin = (releasedCol < preCol) ? releasedCol : preCol;
					colMax = (releasedCol > preCol) ? releasedCol : preCol;
				}
			}

		});
		// --------------------------------------------------------------------
		// TablePanel 2
		// --------------------------------------------------------------------
		gridPanel2.setTableMouseListener(new TableMouseListener() {
			// int pressed_row; // マウスが押された行
			// int pressed_col; // マウスが押された列
			// int released_row; // マウスが離された行
			// int released_col; // マウスが離された列
			// ----------------------------------------------------------------
			// マウスボタンを押した
			// ----------------------------------------------------------------
			@Override
			public void mousePressedHandle(MouseEvent evt, int pressedRow,
					int pressedCol) {
				this.setPressedCol(pressedCol);
				this.setPressedRow(pressedRow);
			}
			// ----------------------------------------------------------------
			// マウスボタンを放した
			// ----------------------------------------------------------------
			@Override
			public void mouseReleasedHandle(MouseEvent evt, int releasedRow,
					int releasedCol) {
				if (releasedRow >= 0 && releasedCol >= 0) {
					int pressedCol = this.getPressedCol();
					System.out.println("mouseReleased @ 行:" + releasedRow
							+ " 列:" + releasedCol);
					int wMin = (releasedCol < pressedCol) ? releasedCol
							: pressedCol;
					int wMax = (releasedCol > pressedCol) ? releasedCol
							: pressedCol;
					// txtPz1.setText( Integer.toString(wMin));
					// txtPz2.setText(Integer.toString(wMax));
					txtFrom.setText(Integer.toString(wMin + jackUp));
					txtTo.setText(Integer.toString(wMax + jackUp));
				}
			}
		});
		// ---------------------------------------------------------------------
		// Saveボタン
		// ---------------------------------------------------------------------
		btnSaveP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// @debug
				actionSave();
			}
		});
		// ---------------------------------------------------------------------
		// Resetボタン
		// ---------------------------------------------------------------------
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plistPanel.clear();
				String curDir = globals.ResControl.getCurDir();
				pnlFile_P.setText(curDir);
				// RuntimeEnv.facade();// なんとなくテスト
			}
		});
		// ---------------------------------------------------------------------
		// リストに追加 <固定文字>
		// ---------------------------------------------------------------------
		btnAdd02.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFixVal();
			}
		});
		// ---------------------------------------------------------------------
		// リストに追加 <区切り文字>
		// ---------------------------------------------------------------------
		btnAdd03.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDelimiter();
			}
		});
		// ---------------------------------------------------------------------
		// リストに追加 <範囲>
		// ---------------------------------------------------------------------
		btnAdd01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFilter();
			}
		});
		// ---------------------------------------------------------------------
		// Generateボタン
		// ---------------------------------------------------------------------
		btnGen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (colMin >= 0) {
					if (colMax == -1 || colMin == colMax)
						colMax = (gridPanel1.getColumnCount() - 1);
					for (int i = (colMin + 1); i <= (colMax + 1); i++) {
						txtCol.setText(String.valueOf(i));
						addFilter();
						addDelimiter();
					}
				}
			}
		});
	};

	private void actionFileIn() {
		String path = pnlFile_I.getPath();
		String charsetName = pnlFile_I.getEncode();
		String delimiter = pnlFile_I.getDelimiter();
		int limit = depth;
		System.out.println("############depth=>" + depth);
		Inf_ArrayCnv cnv = null;
		Vector<Vector> matrix = File2Matrix.extract(path, delimiter, limit,
				charsetName);
		if (matrix != null && matrix.size() > 0) {
			DefaultTableModelMod tModel = new DefaultTableModelMod(matrix);
			tModel.setUseDefaultName(false);
			gridPanel1.setDefModel(tModel);
		}
		gridPanel1.autoFit();

	}

	private void actionFilterSelected() {
		String filter = (String) cmbFlt.getSelectedItem();
		labInfo.setText(converter.getExplain(filter));
		txtOther.setText(converter.getSample(filter));
	}

	// ------------------------------------------------------------------------
	// 実行
	// ------------------------------------------------------------------------
	private void actionGoAhead() {
		boolean withHeader = false;
		String iCharsetName = (String) pnlFile_I.getEncode();
		String oCharsetName = (String) cmbEncodeo.getSelectedItem();
		if (flgHeadPrint.isSelected())
			withHeader = true;
		if (colNameHeader.isSelected()) {
			skipCount = 1;
		} else {
			skipCount = 0;
		}
		long limitCount = -1;
		try {
			limitCount = Long.parseLong(cmbLimit.getSelectedItem().toString());
		} catch (NumberFormatException ex) {
		}
		List paramList = plistPanel.getList();
		// ※パラメータをフィルタに変換する
		converter.parseParam(paramList);
		String oPath = "";
		String iPath = pnlFile_I.getPath();
		if (iPath.contains("*")) {
			System.out.println("iPath contains *!!!:" + iPath);
			String wPath = FileUtil.normarizeIt(iPath);
			int poz = wPath.lastIndexOf("/") + 1;
			String dir = wPath.substring(0, poz);
			int dirLen = dir.length();
			String regex = wPath.substring(poz);
			System.out.println("@debug dir:" + dir);
			System.out.println("@debug regex:" + regex);
			List<String> pathList = DosEmu.pathList2List(dir, regex);
			dir = dir + "/converted/";
			dir = FileUtil.makedir(dir);
			for (String element : pathList) {
				iPath = element;
				oPath = dir + iPath.substring(dirLen);
				System.out.println("iPathx=>" + iPath);
				System.out.println("oPathx=>" + oPath);
				// TODO 開始しました・・・終了しました等のメッセージを表示する
				// ------------------------------------------------------------
				// substrの本体
				// ------------------------------------------------------------
				EzReader reader = new EzReader(iPath, iCharsetName);
				EzWriter writer = new EzWriter(oPath, oCharsetName);
				TextFactory task = new TextFactory(writer, reader, converter);

				// TextFactory task = new TextFactory(oPath, iPath, converter,
				// oCharsetName, iCharsetName);
				task.setRedersDelimiter(pnlFile_I.getDelimiter());
				task.setLimit(limitCount);
				task.setSkip(skipCount);
				task.headerOption(withHeader);
				labInfo.setText(iPath + "を処理中です．．．");
				task.execute();
				labInfo.setText(oPath + "を出力しました。");
			}
		} else {
			//			String currentDirectoryPath = pnlFile_I.getCurrent();
			String currentDirectoryPath = pnlFile_I.getPath();
			JFileChooser fc = new JFileChooser(currentDirectoryPath);
			fc.setDialogTitle("出力するファイル名を指定してください。");
			// XXX BUG うまくいかない
			// ・・・デイレクトリを変更しただけならsetSelectedFileに指定したファイルに吐き出される
			// 　少しでもファイル名に触れば変わる
			fc.setSelectedFile(new File(currentDirectoryPath + "/result.txt"));
			// APPROVE_OPTION:応答ボタン (yes、ok) が選択された場合の戻り値
			if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				oPath = fc.getSelectedFile().toString();
				// ------------------------------------------------------------
				// substrの本体
				// ------------------------------------------------------------
				EzReader reader = new EzReader(iPath, iCharsetName);
				EzWriter writer = new EzWriter(oPath, oCharsetName);
				TextFactory task = new TextFactory(writer, reader, converter);

				// TextFactory task = new TextFactory(oPath, iPath, converter,
				// oCharsetName, iCharsetName);

				task.setRedersDelimiter(pnlFile_I.getDelimiter());
				task.setLimit(limitCount);
				task.setSkip(skipCount);
				task.headerOption(withHeader);
				labInfo.setText(iPath + "を処理中です．．．");
				task.execute();
				labInfo.setText(oPath + "を出力しました。");
			} else {
				return;
			}
		}
	}

	private void actionSave() {
		//		String currentDirectoryPath = pnlFile_P.getCurrent();
		String currentDirectoryPath = pnlFile_P.getPath();
		if (currentDirectoryPath.equals(""))
			currentDirectoryPath = "."; // Home Directory
		JFileChooser fc = new JFileChooser(currentDirectoryPath);
		// JFileChooser fc = new JFileChooser(".");
		fc.setSelectedFile(new File(currentDirectoryPath + "/parameter.txt"));
		fc.setDialogTitle("パラメータファイル名を指定してください。");
		String path = pnlFile_P.getPath();
		if (!path.trim().equals(""))
			fc.setSelectedFile(new File(path));
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String wPath = fc.getSelectedFile().toString();
			List<Object> objList = plistPanel.getList();
			List<String> strList = new ArrayList();
			for (Object obj : objList) {
				strList.add((String) obj);
			}
			ListArrayUtil.list2File(wPath, strList);
			pnlFile_P.setText(wPath);
		} else {
			return;
		}

	}

	// ---------------------------------------------------------------------
	// リストに追加 <範囲>
	// ---------------------------------------------------------------------
	private void addFilter() {
		String col = txtCol.getText().trim();
		String pFrom = txtFrom.getText().trim();
		String pTo = txtTo.getText().trim();
		String pOther = txtOther.getText().trim();
		pOther = pOther.replaceAll(",", escapeComma);
		String flt = (String) cmbFlt.getSelectedItem();
		if (!pFrom.matches("\\d+"))
			pFrom = "";
		if (!pTo.matches("\\d+"))
			pTo = "";
		String wVal = col + "," + pFrom + "," + pTo + "," + flt + "," + pOther
				+ getComment();
		addParameter(wVal);
	}

	// ---------------------------------------------------------------------
	// リストに追加 <固定文字>
	// ---------------------------------------------------------------------
	private void addFixVal() {
		String wVal = "@," + txtFixed.getText() + getComment();
		addParameter(wVal);
	}

	// ---------------------------------------------------------------------
	// リストに追加 <区切り文字>
	// ---------------------------------------------------------------------
	private void addDelimiter() {
		String wVal = "D," + (String) cmbDlm.getSelectedItem() + getComment();
		addParameter(wVal);
	}

	private void addParameter(String wVal) {
		if (rbtOptions[0].isSelected()) {// replace
			plistPanel.setElementAt(wVal, plistPanel.getSelectedIndex());
		} else if (rbtOptions[1].isSelected()) {// insert
			plistPanel.insertElementAt(wVal, plistPanel.getSelectedIndex());
		} else {
			plistPanel.addElement(wVal);
		}
	}
	// ------------------------------------------------------------------------
	// 逆にファイルに書き出すのも作ろう！！
	// ------------------------------------------------------------------------
	public void writeCnvParam() {
		// String wPath = "";
	}

	// ------------------------------------------------------------------------
	// パラメータを読み込む
	// ------------------------------------------------------------------------
	private void parameterRead(String path) {
		List list = File2Matrix.extract(path, (String) null);
		if (list != null && list.size() > 0)
			plistPanel.setListData(list);
	}

	public static void main(String[] argv) {
		if (argv.length == 3) {
			TextFactory.main(argv);
		} else {
			standAlone(new Pn_SUBSTR(), "Substring GUI");
		}
	}
}
