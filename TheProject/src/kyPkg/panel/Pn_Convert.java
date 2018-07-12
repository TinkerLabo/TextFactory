package kyPkg.panel;

import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import kyPkg.task.*;
import kyPkg.uFile.ListArrayUtil;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

//-----------------------------------------------------------------------------
/**
 * 《プログレスバーの利用雛形》 ※以下の二つのクラスに依存しています kyPkg.util.TskXXX kyPkg.util.SwingWorker
 * 
 * 大量データを読み込んだ場合ハングアップする恐れがあるが、予期できないか・・
 */
public class Pn_Convert extends BorderPanel implements ActionListener {

	private static final String PREFIX = ".*\\.";

	private static final String PARAMETER = "外部パラメータ";

	private static final String CONVERT = "変換処理実行";

	private static final long serialVersionUID = 8693278004908452341L;

	// -------------------------------------------------------------------------
	// プログレスバーのタイトル
	// -------------------------------------------------------------------------
	private String gParmPath;

	private TskXXX task; // 実際に処理を行うクラス

	// -------------------------------------------------------------------------
	// Local変数
	// -------------------------------------------------------------------------
	private PnlFile pnlFile;

	private CnvPanel wCvP[];

	private String wBef[];

	private String wAft[];

	private String wPath;

	private JButton wBtFuji; // 藤居ボタン

	private JButton wBtKick; // 実行ボタン

	private JComboBox comboExt; // 対象ファイルの拡張子

	private JCheckBox checkSubDir; // サブディレクトリ以下も検索するかどうか

	private JTextArea jTxaMsg; // 結果メッセージ表示用テキストエリア

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Pn_Convert() {
		super();
		wCvP = new CnvPanel[50];
		wBef = new String[50];
		wAft = new String[50];
		createGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// createGUI
	// -------------------------------------------------------------------------
	private void createGUI() {

		HashMap pmap = new HashMap();
		pmap.put(PnlFile.CAPTION, "targetPath:");
		pmap.put(PnlFile.OPT49ER, "true");
		pmap.put(PnlFile.ENCODE, "false");
		pmap.put(PnlFile.DELIM, "true");
		pmap.put(PnlFile.DEFAULT_PATH, "");
		pmap.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(pmap);

		//		pnlFile = new PnlFile("targetPath:", true, false, true, "", -1);
		// ---------------------------------------------------------------------
		// 拡張子コンボボックス
		// ---------------------------------------------------------------------
		JLabel wLabel01 = new JLabel("対象拡張子：");
		wLabel01.setSize(new Dimension(120, 20));
		comboExt = new JComboBox();
		comboExt.setSize(new Dimension(100, 20));
		comboExt.setEditable(true);
		comboExt.addItem(PREFIX + "csv");
		comboExt.addItem(PREFIX + "txt");
		comboExt.addItem(PREFIX + "sql");
		comboExt.addItem(PREFIX + "prn");
		comboExt.addItem(PREFIX + "htm");
		comboExt.addItem(PREFIX + "html");
		comboExt.addItem(PREFIX + "java");
		comboExt.addItem(PREFIX + "bas");
		comboExt.addItem(PREFIX + "frm");
		comboExt.addItem(PREFIX + "c");
		comboExt.addItem(PREFIX + "cpp");
		comboExt.addItem(PREFIX + ".*");
		// ---------------------------------------------------------------------
		// チェックボックス
		// ---------------------------------------------------------------------
		checkSubDir = new JCheckBox("サブフォルダも検索する", true);
		checkSubDir.setSize(new Dimension(200, 20));
		checkSubDir.setOpaque(false);
		wBtKick = new JButton(CONVERT);
		wBtKick.setSize(new Dimension(200, 20));
		wBtFuji = new JButton(PARAMETER);
		wBtFuji.setSize(new Dimension(200, 20));
		// ---------------------------------------------------------------------
		// 変換条件 Ｂｅｆｏｒｅ ＆ Ａｆｔｅｒ
		// ---------------------------------------------------------------------
		JPanel innerPanel = new JPanel();
		for (int i = 0; i < wCvP.length; i++) {
			wCvP[i] = new CnvPanel();
			wCvP[i].setBounds(10, (20 * i), 690, 20);
			innerPanel.add(wCvP[i]);
		}
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(innerPanel);
		// ---------------------------------------------------------------------
		// メッセージエリア
		// ---------------------------------------------------------------------
		jTxaMsg = new JTextArea();
		jTxaMsg.setSize(new Dimension(100, 20));
		pnlN.add(pnlFile);
		JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBtn.add(wLabel01);
		panelBtn.add(comboExt);
		panelBtn.add(checkSubDir);
		panelBtn.add(wBtKick);
		panelBtn.add(wBtFuji);
		pnlN.add(panelBtn);
		pnlC.add(scrollPane);
		pnlS.add(jTxaMsg);
		// ---------------------------------------------------------------------
		// 実行ボタン
		// ---------------------------------------------------------------------
		wBtKick.addActionListener(this);
		// ---------------------------------------------------------------------
		// 藤居ボタン
		// ---------------------------------------------------------------------
		wBtFuji.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String curDir = globals.ResControl.getCurDir();
				readCnvParam(curDir);
			}
		});
	}

	// -------------------------------------------------------------------------
	// コンバート表をファイルから読み込む（やっつけ版）
	// -------------------------------------------------------------------------
	private void readCnvParam(String pParm) {
		JFileChooser fc = new JFileChooser(pParm);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			gParmPath = fc.getSelectedFile().toString();
		}
		System.out.println("fujiipath:" + gParmPath);
		Thread th1 = new Thread() {
			@Override
			public void run() {
				int k = 0;
				String[] array = ListArrayUtil.file2Array(gParmPath);
				for (int i = 0; i < array.length; i++) {
					String[] zArray = array[i].split("\t");
					if (zArray.length >= 2) {
						wCvP[k].setBef(zArray[0]);
						wCvP[k].setAft(zArray[1]);
						k++;
					}
				}
			}
		};
		th1.start();
	};

	// -------------------------------------------------------------------------
	// 実行ボタンの処理
	// プログレスモニターをセットアップ＆ 実処理のスレッドを開始
	// -------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		// ■実処理ルーチンの初期化部分 実行前に条件に基づいて初期化する！！！
		// ↓※以下を変更する（実装により引数は異なる）
		// ---------------------------------------------------------------------
		jTxaMsg.setText("");
		boolean wFlg = checkSubDir.isSelected(); // サブディレクトリ以下も検索するかどうか
		String wRgx = (String) (comboExt.getSelectedItem());
		// wRgx = ".*\\."+wRgx;

		for (int k = 0; k < wCvP.length; k++) {
			wBef[k] = wCvP[k].getBef();
			wAft[k] = wCvP[k].getAft();
		}
		wPath = pnlFile.getPath(); // 処理対象パス、
		// 処理対象パス、元文字列、変換先文字列、対象ファイルタイプ、サブディレクトリ検索
		task = new TskXXX(wPath, wBef, wAft, wRgx, wFlg); // ←■これを変更する

		task.initProgressKit(Pn_Convert.this, wBtKick, "処理中・・・", 100);
		task.execute(); // 実処理を起動する
	}

	public static void main(String[] argv) {
		standAlone();
	}

	public static void standAlone() {
		JFrame frame = new JFrame();
		frame.setBounds(50, 50, 800, 350);
		frame.getContentPane().add(new Pn_Convert());
		frame.setVisible(true);
	}

}
