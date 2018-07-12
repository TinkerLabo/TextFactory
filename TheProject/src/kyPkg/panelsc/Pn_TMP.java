package kyPkg.panelsc;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
//============================================================================
//雛形！これを元に簡易なＧＵＩを作成する
//============================================================================

public class Pn_TMP extends Pn_Scaffold {
	private static final long serialVersionUID = -3066425890995680687L;
	private JButton btnExec;
	private CommonTabPane tabPane;

	// -------------------------------------------------------------------------
	// 《コンストラクタ》
	// -------------------------------------------------------------------------
	public Pn_TMP() {
		super(700, 480);
		int option1 = -1;
		int option2 = -1;
		if (option1 != 0 & option2 != 0) {
			tabPane = new CommonTabPane("input", "outout", option1, option2);
			add(BorderLayout.CENTER, tabPane);
		}
		initGUI(); // GUI部作成
	}

	// -------------------------------------------------------------------------
	// initGUI
	// -------------------------------------------------------------------------
	void initGUI() {
		String outPath = "c:/神戸.txt";
		tabPane.addFPanel("出力 データ", null, false, outPath);

		btnExec = new JButton("Execute");
		btnExec.setBounds(200, 200, 150, 20);
		pnlSouth.pnlS.add(btnExec);
		// ---------------------------------------------------------------------
		// Button
		// ---------------------------------------------------------------------
		btnExec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String path = tabPane.getInPath(0);
						String ans = kyPkg.converter.ValueChecker.classifyOld3(path,
								"1", "@");
						tabPane.setTextOut(ans);
					}
				};
				th1.start();
			}
		});
	}

	public static void main(String[] argv) {
		standAlone(new Pn_TMP(), "Title");
	}
}