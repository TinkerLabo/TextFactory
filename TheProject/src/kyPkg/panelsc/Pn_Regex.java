package kyPkg.panelsc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.regex.*;

import kyPkg.util.Ruler;

// ============================================================================
public class Pn_Regex extends Pn_Scaffold {
	private static final long serialVersionUID = -3066425890995680687L;
	private JLabel JLab1;
	private JTextField jTf1;
	private JTextField jTf2;
	private JTextField jTf3;
	private JButton jBt1;
	private JButton jBt2;
	private JButton jBt3;
	private JButton jBt4;
	private JButton jBt5;
	private JButton jBt01;
	private CommonTabPane commonRes;

	// -------------------------------------------------------------------------
	// 大文字小文字を曖昧に検索したい場合
	// ※毎回パターンがコンパイルされるので注意する
	// 《使用例》 wFlg = Regexx.findIgnoreCase(pRegex, pCharSeq);
	// -------------------------------------------------------------------------
	public static boolean findIgnoreCase(String regex, String targetStr) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		boolean wFlg = pattern.matcher(targetStr).find();
		return wFlg;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Pn_Regex() {
		super(640, 480);
		int option1 = -1;
		int option2 = -1;
		if (option1 != 0 & option2 != 0) {
			commonRes = new CommonTabPane("input", "outout", option1, option2);
			add(commonRes, BorderLayout.CENTER);
		}
		initGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// 《ＧＵＩ関連》
	void initGUI() {
		// ---------------------------------------------------------------------
		JPanel fr = new JPanel(null) {
			private static final long serialVersionUID = -7279804211806772585L;

			// -------------------------------------------------------------------------
			// ルーラー表示
			// -------------------------------------------------------------------------
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
						new Color(250, 187, 244, 128));
			}
		};
		commonRes.addTab("The Other", fr);
		final JComboBox jCmb1 = new JComboBox();
		jCmb1.addItem("[A-Za-z0-9\\s]*	半角文字か");
		jCmb1.addItem("[ABC]	A,B,Cのいずれか1文字 ");
		jCmb1.addItem("[A-Z]	AからZまでのいずれか1文字 ");
		jCmb1.addItem("[A-Za-z0-9]	AからZ, aからz, 0-9までのいずれか1文字");
		jCmb1.addItem("[^ABC]	A,B.C以外の文字 ");
		jCmb1.addItem("[^A-Z]	AからZ以外の文字 ");
		jCmb1.addItem("\\w	英数文字。[a-zA-Z0-9]と同様 ");
		jCmb1.addItem("\\W	\\w以外の文字 ");
		jCmb1.addItem("\\d	数値文字。[0-9]と同等 ");
		jCmb1.addItem("\\D	\\d以外の文字 ");
		jCmb1.addItem("\\s	空白文字 ");
		jCmb1.addItem("\\S	\\s以外の文字");
		jCmb1.addItem("\\n	改行文字 ");

		JLab1 = new JLabel("");
		jTf1 = new JTextField("");
		jTf2 = new JTextField("");
		jTf3 = new JTextField("");
		jBt1 = new JButton("match?");
		jBt2 = new JButton("Replace");
		jBt3 = new JButton("Find");
		jBt4 = new JButton("EscapeSeq");
		jBt5 = new JButton("MetaCharacter");
		jTf1.setText(".*static\\s*void\\s*main.*"); // debug
		jTf2.setText("	public static void main(String[] argv){"); // debug
		JLab1.setForeground(Color.WHITE);
		JLab1.setBackground(Color.BLUE);
		JLab1.setOpaque(true);
		JPanel pnlVert = new JPanel();
		pnlVert.setLayout(new BoxLayout(pnlVert, BoxLayout.Y_AXIS));
		jTf1.setMaximumSize(new Dimension(500, 20));
		jTf2.setMaximumSize(new Dimension(500, 20));
		jTf3.setMaximumSize(new Dimension(500, 20));
		pnlVert.add(jTf1);
		pnlVert.add(jTf2);
		pnlVert.add(jTf3);
		JPanel pnlCtrl = new JPanel();
		pnlCtrl.setLayout(new BoxLayout(pnlCtrl, BoxLayout.X_AXIS));
		jBt1.setPreferredSize(new Dimension(100, 20));
		jBt2.setPreferredSize(new Dimension(100, 20));
		jBt3.setPreferredSize(new Dimension(100, 20));
		jBt4.setPreferredSize(new Dimension(100, 20));
		pnlCtrl.add(jBt1);
		pnlCtrl.add(jBt2);
		pnlCtrl.add(jBt3);
		pnlCtrl.add(jBt4);
		pnlCtrl.add(jBt5);

		JLab1.setBounds(10, 10, 200, 20);
		fr.add(JLab1);
		jCmb1.setBounds(210, 10, 300, 20);
		fr.add(jCmb1);
		pnlVert.setBounds(10, 50, 500, 80);
		fr.add(pnlVert);
		pnlCtrl.setBounds(10, 130, 500, 20);
		fr.add(pnlCtrl);

		jCmb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wStr = jCmb1.getSelectedItem().toString();
				String[] arr = wStr.split("\\t");
				if (arr != null)
					jTf1.setText(jTf1.getText() + arr[0]);
			}
		});
		jBt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLab1.setText("");
				String regex = jTf1.getText();
				String srcText = jTf2.getText();
				boolean wFlg;
				// -------------------------------------------------------------
				// 標準のstaticメソッドを使った場合
				// -------------------------------------------------------------
				// wFlg = Pattern.matches(pRegex, pCharSeq);
				// -------------------------------------------------------------
				// パターンが固定される場合（パフォーマンスを気にする場合）
				// -------------------------------------------------------------
				Pattern ptn = kyPkg.uRegex.Regex.getPattern(regex, true);
				if (ptn != null) {
					wFlg = ptn.matcher(srcText).matches();
					JLab1.setText("matches => " + wFlg);
				} else {
					JLab1.setText("PatternError");
				}
			}
		});
		jBt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLab1.setText("");
				String pRegex = jTf1.getText();
				String pCharSeq = jTf2.getText();
				String pReplacement = "@";
				// -------------------------------------------------------------
				// パターンが固定される場合（パフォーマンスを気にする場合）
				// -------------------------------------------------------------
				Pattern ptn = kyPkg.uRegex.Regex.getPattern(pRegex, true);
				if (ptn != null) {
					jTf3.setText(ptn.matcher(pCharSeq).replaceAll(pReplacement));
				} else {
					jTf3.setText("PatternError");
				}
			}
		});
		jBt3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLab1.setText("");
				String pRegex = jTf1.getText();
				String pCharSeq = jTf2.getText();
				boolean wFlg;
				// -------------------------------------------------------------
				// パターンが固定される場合（パフォーマンスを気にする場合）
				// -------------------------------------------------------------
				// Pattern ptn = Regexx.patternIgnoreCase(pRegex);
				// wFlg = ptn.matcher(pCharSeq).find();
				wFlg = findIgnoreCase(pRegex, pCharSeq);
				JLab1.setText("find! => " + wFlg);
			}
		});
		jBt4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLab1.setText("");
				String pRegex = jTf1.getText();
				// -------------------------------------------------------------
				// リテラル内に指定するエスケープシーケンスに加工する
				// -------------------------------------------------------------
				pRegex = pRegex.replaceAll("\\\\", "\\\\\\\\");
				pRegex = pRegex.replaceAll("\"", "\\\\\"");
				pRegex = pRegex.replaceAll("\'", "\\\\\'");
				pRegex = pRegex.replaceAll("\t", "\\\\\t'");
				jTf3.setText(pRegex);
			}
		});
		jBt5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLab1.setText("");
				String pRegex = jTf2.getText();
				// -------------------------------------------------------------
				// 文字列を表現するregixに簡易変換する
				// 括弧も問題あるだろうね・・・・
				// 元の文字列はregixでは無いという前提・・
				// -------------------------------------------------------------
				pRegex = pRegex.replaceAll("\\\\", "\\\\");
				pRegex = pRegex.replaceAll("\"", "\\\\\"");
				pRegex = pRegex.replaceAll("\'", "\\\\\'");
				pRegex = pRegex.replaceAll("\t", "\\\\t");
				pRegex = pRegex.replaceAll("\\.", "\\\\.");
				pRegex = pRegex.replaceAll("\\s", "\\\\s");
				pRegex = pRegex.replaceAll("\\*", ".*");
				jTf3.setText(pRegex);
			}
		});

		// ---------------------------------------------------------------------
		jBt01 = new JButton("Regex");
		jBt01.setBounds(200, 200, 150, 20);
		pnlSouth.pnlS.add(jBt01);
		// ---------------------------------------------------------------------
		// Test
		// ---------------------------------------------------------------------
		jBt01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String parm = commonRes.getTextOut();
				String ans = kyPkg.converter.ValueChecker.classifyOld3(parm, "1",
						"@");
				commonRes.setTextOut(ans);
			}
		});
	}

	public static void main(String[] argv) {
		standAlone(new Pn_Regex(), "RegularEx Test");
	}

}