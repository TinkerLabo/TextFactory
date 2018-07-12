package kyPkg.panelsc;

import java.io.*;
import java.util.HashMap;

import kyPkg.uFile.FileUtil;
import kyPkg.panel.JP_Ancestor;
import kyPkg.panelMini.PnlFile;
import kyPkg.task.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// ============================================================================
/**《プログレスバーの利用雛形》
 * ※以下の二つのクラスに依存しています
 *   kyPkg.util.TskXXX
 *   kyPkg.util.SwingWorker
 */
public class Pn_Scaffold3 extends JP_Ancestor {
	public static void main(String[] argv) {
		standAlone(new Pn_Scaffold3(), "scaffold3");
	}

	private static final long serialVersionUID = -1775170390761227057L;
	private Inf_ProgressTask task; // 実際に処理を行うクラス
	//-------------------------------------------------------------------------
	// Local変数
	//-------------------------------------------------------------------------
	private JTabbedPane tabbedPane;
	protected PnlFile pnlFile1;
	private JButton btn_V; // Paste
	private JButton btn_R; // Clear
	private JButton btn_C; // Copy
	private JButton btn_X; // Cut
	protected JTextArea jTa1;
	protected JTextArea jTa2;
	private JButton btn_Start; // 実行ボタン
	private JButton jBtWrite;
	protected BorderPanel pnlInput;
	protected BorderPanel pnlOutput;
	protected BorderPanel pnlSouth;

	//-------------------------------------------------------------------------
	/** コンストラクタ は無論無し！！（単体テストのときのみ）*/
	//-------------------------------------------------------------------------
	// 新しいタブを追加する
	//-------------------------------------------------------------------------
	public void addTab(String title, Component component) {
		tabbedPane.addTab(title, component);
	}

	//-------------------------------------------------------------------------
	//↓■これを変更する■
	//-------------------------------------------------------------------------
	/**《ＧＵＩ関連》 ここと初期化部分を修正する！  */
	void createGUI() {
		this.setPreferredSize(new Dimension(940, 520));

		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "Read Data");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile1 = new PnlFile(pmap1);

		//		pnlFile1 = new PnlFile("Read Data", false, false, true, "", -1);
		//---------------------------------------------------------------------
		// ACTION
		//---------------------------------------------------------------------
		pnlFile1.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String wPath = pnlFile1.getPath();
						jTa1.setText(FileUtil.file2String(wPath));
					}
				};
				th1.start();
			}
		});
		jTa1 = new JTextArea("");
		jTa2 = new JTextArea("");
		JScrollPane jSp1 = new JScrollPane(jTa1);
		jSp1.setBounds(0, 50, 600, 150);
		JScrollPane jSp2 = new JScrollPane(jTa2);
		jSp2.setBounds(0, 260, 600, 140);
		btn_V = new JButton("Paste");
		btn_V.setBounds(0, 200, 100, 20);
		btn_R = new JButton("Clear");
		btn_R.setBounds(100, 200, 100, 20);
		btn_C = new JButton("Copy");
		btn_C.setBounds(100, 400, 100, 20);
		btn_X = new JButton("Cut");
		btn_X.setBounds(200, 400, 100, 20);
		jBtWrite = new JButton("Write It");
		jBtWrite.setBounds(0, 400, 100, 20);
		//---------------------------------------------------------------------
		pnlInput = new BorderPanel();
		//pnlInput.jPnlN.setLayout(new FlowLayout(FlowLayout.LEFT));
		//		add(pnlFile1,BorderLayout.NORTH);
		pnlInput.pnlS.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlInput.pnlN.add(pnlFile1);
		pnlInput.pnlS.add(btn_V);
		pnlInput.pnlS.add(btn_R);
		pnlInput.pnlC.add(jSp1);
		//---------------------------------------------------------------------
		pnlOutput = new BorderPanel();
		pnlOutput.pnlN.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlOutput.pnlS.setLayout(new FlowLayout());
		pnlOutput.pnlN.add(btn_C);
		pnlOutput.pnlN.add(btn_X);
		pnlOutput.pnlN.add(jBtWrite);
		pnlOutput.pnlC.add(jSp2);
		//---------------------------------------------------------------------
		pnlSouth = new BorderPanel();
		pnlSouth.pnlN.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlSouth.pnlS.setLayout(new FlowLayout());
		//---------------------------------------------------------------------
		//		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,pnlXB1, pnlXB2);
		//	    splitPane.setDividerLocation(200);
		//		add(splitPane,BorderLayout.CENTER);			
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("input", pnlInput);
		tabbedPane.addTab("output", pnlOutput);
		add(tabbedPane, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
		//		this.setOpaque(true);
		//---------------------------------------------------------------------
		// Write It処理
		//---------------------------------------------------------------------
		jBtWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
					return;
				String wPath = fc.getSelectedFile().toString();
				if (new File(wPath).exists()) {
					int result = JOptionPane.showConfirmDialog((Component) null,
							"すでに同名のファイルが存在します、上書きしますか？", "確認!",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return;
				}
				if (wPath.indexOf(".") == 0) {
					wPath = wPath.trim() + ".txt";
				}
				FileUtil.string2file_(wPath, jTa2.getText());
			}
		});
		//---------------------------------------------------------------------
		// テキストエリア１編集
		//---------------------------------------------------------------------
		btn_V.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa1.selectAll();
				jTa1.paste();
			}
		});
		//---------------------------------------------------------------------
		btn_R.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa1.selectAll();
				jTa1.setText("");
			}
		});
		//---------------------------------------------------------------------
		// テキストエリア２編集
		//---------------------------------------------------------------------
		btn_C.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa2.selectAll();
				jTa2.copy();
			}
		});
		//---------------------------------------------------------------------
		btn_X.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa2.selectAll();
				jTa2.cut();
			}
		});
	}

	//-------------------------------------------------------------------------
	/** ActionListenerインターフェースのメソッドを定義 */
	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		if (button == btn_Start) {
			task = new TskXXX(new String[] { ".", pnlFile1.getPath(), "" });
			task.execute(); // 実処理を起動する
		}
	}

	//-------------------------------------------------------------------------
	/** 実行ボタンが押されたら
	* プログレスモニターをセットアップ＆ 実処理のスレッドを開始
	*/
	//-------------------------------------------------------------------------
	// ルーラー表示
	//-------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//		Ruler.drawRuler(g,this.getSize().width,this.getSize().height,
		//			new Color(250,187,244,128));
	}
}
