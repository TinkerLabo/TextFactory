package kyPkg.panel;

import static kyPkg.util.Joint.join;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kyPkg.panelsc.BorderPanel;
//import kyPkg.panelsc.Pn_Venn;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.DnDAdapter;
import kyPkg.util.Ruler;

//XXX 件数が違う場合に誤動作しているのかもしれない　2013/07/22
public class Pn_Compare_org extends BorderPanel {

	private String fontName = "ＭＳ ゴシック";
	private static final long serialVersionUID = 6931970310127973516L;
	private JTextField txtFile1;
	private JTextField txtFile2;
	private JTextArea txtRight;
	private JTextArea txtLeft;
	private JCheckBox chkHex;
	private JCheckBox chkHir;
	private JCheckBox chkTrim;
	private JTextField txtLimit;
	private JTextField txtRegex;
	private boolean trim = false;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public Pn_Compare_org() {
		super();
		setSize(940, 520);
		this.setOpaque(false);
		createGUI();
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
				new Color(155, 187, 244));
	}

	public void createGUI() {
		Font ft = new Font(fontName, Font.PLAIN, 12);
		txtFile1 = new JTextField(30);
		txtFile1.setSize(new Dimension(500, 20));

		txtFile2 = new JTextField(30);
		txtFile2.setSize(new Dimension(500, 20));

		txtRight = new JTextArea();
		txtLeft = new JTextArea();

		txtFile1.setFont(ft);
		txtFile2.setFont(ft);
		txtRight.setFont(ft);
		txtLeft.setFont(ft);

		chkHex = new JCheckBox("HexCompare?");
		chkHex.setSize(new Dimension(120, 20));

		chkHir = new JCheckBox("HirCompare?");
		chkHir.setSize(new Dimension(120, 20));

		chkTrim = new JCheckBox("Trim?");
		chkTrim.setSize(new Dimension(120, 20));

		txtLimit = new JTextField("2048");
		// jtxLimit.setMinimumSize(new Dimension(100, 20));
		txtLimit.setPreferredSize(new Dimension(100, 20));
		txtLimit.setSize(new Dimension(120, 20));

		txtRegex = new JTextField("*.bas");
		// jtxRegex.setMinimumSize(new Dimension(120, 20));
		txtRegex.setPreferredSize(new Dimension(100, 20));
		txtRegex.setSize(new Dimension(120, 20));

		JButton btnCompare = new JButton("Compare");
		btnCompare.setSize(new Dimension(110, 20));

		JButton btnReset = new JButton("Reset");
		btnReset.setSize(new Dimension(110, 20));

		JButton btnReadIt = new JButton("Read It");
		btnReadIt.setSize(new Dimension(110, 20));

		JButton btnWriteIt = new JButton("Write It");
		btnWriteIt.setSize(new Dimension(100, 20));

		JScrollPane scpLeft = new JScrollPane(txtLeft);
		scpLeft.setSize(new Dimension(610, 220));
		JScrollPane scpRight = new JScrollPane(txtRight);
		scpRight.setSize(new Dimension(610, 100));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				scpLeft, scpRight);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
                
		JPanel pnlBtn = new JPanel();
		pnlBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlBtn.add(btnCompare);
		pnlBtn.add(btnReset);
		pnlBtn.add(btnReadIt);
		pnlBtn.add(btnWriteIt);
		pnlBtn.add(txtLimit);
		pnlBtn.add(chkHex);
		pnlBtn.add(txtRegex);
		pnlBtn.add(chkHir);
		pnlBtn.add(chkTrim);
		txtRight.setLineWrap(true);
		txtRight.setTabSize(4); // TABの大きさを設定
		txtLeft.setLineWrap(true);
		txtLeft.setTabSize(4); // TABの大きさを設定
		new java.awt.dnd.DropTarget(txtFile1, new DnDAdapter(txtFile1)); // dnd.DropTarget
		new java.awt.dnd.DropTarget(txtFile2, new DnDAdapter(txtFile2)); // dnd.DropTarget
		pnlN.add(txtFile1);
		pnlN.add(txtFile2);
		pnlN.add(pnlBtn);
		pnlC.add(splitPane);
		// ---------------------------------------------------------------------
		// Compare処理
		// ---------------------------------------------------------------------
		btnCompare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compareIt();
			}
		});
		// ---------------------------------------------------------------------
		// reset処理
		// ---------------------------------------------------------------------
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtFile1.setText("");
				txtFile2.setText("");
				txtLeft.setText("");
				txtRight.setText("");
			}
		});
		// ---------------------------------------------------------------------
		// Read It処理
		// ---------------------------------------------------------------------
		btnReadIt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readIt();
			}
		});
		// ---------------------------------------------------------------------
		// Write It処理
		// ---------------------------------------------------------------------
		btnWriteIt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeIt();
			}
		});
	}

	// -------------------------------------------------------------------------
	// original
	// -------------------------------------------------------------------------
	// private void compareIt_old() {
	// String rStr = "";
	// String wPath1 = txtFile1.getText();
	// String wPath2 = txtFile2.getText();
	// int limit = Integer.parseInt(txtLimit.getText());
	// if (chkHex.isSelected()) {
	// rStr = new kyPkg.tools.HexCompare(wPath1, wPath2)
	// .compareAndGetStat();
	// } else {
	// boolean trim = false;
	// rStr = new kyPkg.tools.Compare(wPath1, wPath2, limit, trim)
	// .compareAndGetStatRez();
	// }
	// txtLeft.setText(rStr);
	// txtRight.setText("");
	// }

	private void compareIt() {
		String rStr = "";
		String wPath1 = txtFile1.getText();
		String wPath2 = txtFile2.getText();
		trim = chkTrim.isSelected();
		if (wPath1.trim().equals("")) {
			wPath1 = "./left.txt";
			txtFile1.setText(wPath1);
			FileUtil.string2file_(wPath1, txtLeft.getText());
		}
		if (wPath2.trim().equals("")) {
			wPath2 = "./right.txt";
			txtFile2.setText(wPath2);
			FileUtil.string2file_(wPath2, txtRight.getText());
		}

		int limit = Integer.parseInt(txtLimit.getText());
		String regex = txtRegex.getText();
		List<String> statLog = new ArrayList();
		// 異なるディレクトリにある同名ファイルどうしの比較
		if (chkHir.isSelected()) {
			List<String> list = DosEmu.getFileList(wPath1, regex);
			for (String srcPath : list) {
				// 親パスを書き換える
				String dstPath = FileUtil.cnvParent(wPath2,
						srcPath);
				// 拡張子を書き換える
				String difPath = FileUtil.cnvExt(srcPath, "dif");
				System.out.println("srcPath>" + srcPath);
				System.out.println("dstPath>" + dstPath);
				System.out.println("difPath>" + difPath);

				// 比較した結果違いが一見以上あればdiffを出力する
				// 比較結果を画面にも返したい　ファイルｘｘｘはマッチしませんでした
				kyPkg.tools.Compare ins = new kyPkg.tools.Compare(dstPath,
						srcPath, limit, trim);
				if (ins.compare() == 0) {
					// すべて一致した（違いが無ければ何もしない）
				} else {
					// 一致しなかった
					List<String> diffList = ins.getDuffList();
					ListArrayUtil.list2File(difPath, diffList);
					statLog.add(ins.getStatus());
				}
			}
			rStr = join(statLog, "\n") + "\n完了・・・";
			txtLeft.setText(rStr);
			txtRight.setText("");

		} else {
			if (chkHex.isSelected()) {
				rStr = new kyPkg.tools.HexCompare(wPath1, wPath2)
						.compareAndGetStat();
			} else {
				rStr = new kyPkg.tools.Compare(wPath1, wPath2, limit, trim)
						.compareAndGetStatRez();
			}
			txtLeft.setText(rStr);
			txtRight.setText("");

		}

	}

	private void readIt() {
		String wPath = txtFile1.getText().trim();
		if (wPath.equals("")) {
			JFileChooser fc = new JFileChooser(".");
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				wPath = fc.getSelectedFile().toString();
			}
		}
		txtLeft.setText(FileUtil.file2String(wPath));
		txtRight.setText(FileUtil.fileInfo(wPath));
	}

	private void writeIt() {
		JFileChooser fc = new JFileChooser(".");
		if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		String wPath = fc.getSelectedFile().toString();
		if (wPath.indexOf(".") == 0) {
			wPath = wPath.trim() + ".txt";
		}
		FileUtil.string2file_(wPath, txtLeft.getText());
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		standAlone(new Pn_Compare_org(), "Compare");
	}
}
